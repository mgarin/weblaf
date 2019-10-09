/*
 * This file is part of WebLookAndFeel library.
 *
 * WebLookAndFeel library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WebLookAndFeel library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WebLookAndFeel library.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.alee.extended.accordion;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.utils.CollectionUtils;
import com.alee.utils.SwingUtils;

import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

/**
 * Default {@link AccordionModel} implementation.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebAccordion">How to use WebAccordion</a>
 * @see WebAccordion
 * @see AccordionPane
 * @see AccordionModel
 * @see WebAccordionModel
 */
public class WebAccordionModel implements AccordionModel, PropertyChangeListener, ContainerListener
{
    /**
     * {@link WebAccordion} into which this {@link WebAccordionModel} is currently installed.
     */
    @Nullable
    protected WebAccordion accordion;

    /**
     * {@link Map} of all {@link AccordionPaneState}s available in this {@link WebAccordionModel}.
     * It can also contain {@link AccordionPaneState} for panes that do not yet exist in {@link WebAccordion}.
     */
    @NotNull
    protected Map<String, AccordionPaneState> states;

    /**
     * {@link Comparator} for {@link AccordionPane}s for descending sorting by state change times.
     */
    @NotNull
    protected Comparator<AccordionPane> newToOldPaneComparator;

    /**
     * Constructs new {@link WebAccordionModel}.
     */
    public WebAccordionModel ()
    {
        this.states = new HashMap<String, AccordionPaneState> ();
        this.newToOldPaneComparator = new Comparator<AccordionPane> ()
        {
            @Override
            public int compare ( final AccordionPane pane1, final AccordionPane pane2 )
            {
                return new Long ( getPaneState ( pane2.getId () ).getTime () )
                        .compareTo ( getPaneState ( pane1.getId () ).getTime () );
            }
        };
    }

    @Override
    public void install ( @NotNull final WebAccordion accordion )
    {
        if ( this.accordion == null )
        {
            this.accordion = accordion;
            this.accordion.addPropertyChangeListener ( this );
            this.accordion.addContainerListener ( this );

            // Validating states
            validateStates ();
        }
        else
        {
            throw new RuntimeException ( "WebAccordionModel is already installed" );
        }
    }

    @Override
    public void uninstall ( @NotNull final WebAccordion multiSplitPane )
    {
        if ( accordion != null )
        {
            this.accordion.removeContainerListener ( this );
            this.accordion.removePropertyChangeListener ( this );
            this.accordion = null;
        }
        else
        {
            throw new RuntimeException ( "WebAccordionModel is not yet installed" );
        }
    }

    /**
     * Returns non-{@code null} {@link WebAccordion}.
     *
     * @return non-{@code null} {@link WebAccordion}
     * @throws RuntimeException if this model is not yet installed and {@link WebAccordion} is unavailable
     */
    @NotNull
    protected WebAccordion getAccordion ()
    {
        if ( accordion != null )
        {
            return accordion;
        }
        else
        {
            throw new RuntimeException ( "WebAccordionModel is not yet installed" );
        }
    }

    @NotNull
    @Override
    public AccordionState getAccordionState ()
    {
        final Map<String, AccordionPaneState> states;
        if ( accordion != null )
        {
            // Make sure we filter out states for unavailable panes
            states = new HashMap<String, AccordionPaneState> ();
            for ( final Map.Entry<String, AccordionPaneState> state : this.states.entrySet () )
            {
                if ( accordion.containsPane ( state.getKey () ) )
                {
                    states.put ( state.getKey (), state.getValue () );
                }
            }
        }
        else
        {
            // Returning states "as is" since we can't filter them
            states = this.states;
        }
        return new AccordionState ( states );
    }

    @Override
    public void setAccordionState ( @NotNull final AccordionState accordionState )
    {
        // Updating states
        states = accordionState.states ();

        // Validating states
        validateStates ();
    }

    @Override
    public void propertyChange ( final PropertyChangeEvent e )
    {
        final String property = e.getPropertyName ();
        if ( Objects.equals ( property,
                WebAccordion.MINIMUM_EXPANDED_PANE_COUNT_PROPERTY,
                WebAccordion.MAXIMUM_EXPANDED_PANE_COUNT_PROPERTY ) )
        {
            // Validating states
            validateStates ();
        }
    }

    @Override
    public void componentAdded ( final ContainerEvent e )
    {
        // Adding state for newly added pane
        final AccordionPane pane = ( AccordionPane ) e.getChild ();
        if ( !states.containsKey ( pane.getId () ) )
        {
            // Only adding new state if it didn't exist since it might have been loaded
            states.put ( pane.getId (), new AccordionPaneState ( pane.getId (), false ) );
        }

        // Validating states
        validateStates ();
    }

    @Override
    public void componentRemoved ( final ContainerEvent e )
    {
        // Removing state for the removed pane
        final AccordionPane pane = ( AccordionPane ) e.getChild ();
        states.remove ( pane.getId () );

        // Validating states
        validateStates ();
    }

    /**
     * Validating that our states comply with {@link WebAccordion} conditions.
     * If they don't - we will automatically adjust states to fix issues.
     */
    protected void validateStates ()
    {
        if ( accordion != null )
        {
            // Ensure no states are missing
            // This might be the case when states are loaded from settings
            // Or if some major changes have happened in accordion structure
            for ( final AccordionPane pane : accordion.getPanes () )
            {
                if ( !states.containsKey ( pane.getId () ) )
                {
                    states.put ( pane.getId (), new AccordionPaneState ( pane.getId (), false ) );
                }
            }

            // Ensure we are meeting minimum/maximum expanded pane counts as much as possible
            final List<AccordionPane> expanded = getExpandedPanes ();
            final List<AccordionPane> collapsed = getCollapsedPanes ();
            if ( expanded.size () < accordion.getMinimumExpandedPaneCount () )
            {
                // Sorting from newest to oldest
                CollectionUtils.sort ( collapsed, newToOldPaneComparator );

                // Expanding collapsed panes
                final int toExpand = Math.min ( collapsed.size (), accordion.getMinimumExpandedPaneCount () - expanded.size () );
                for ( int i = 0; i < toExpand; i++ )
                {
                    expandPane ( collapsed.get ( i ).getId () );
                }
            }
            else if ( expanded.size () > accordion.getMaximumExpandedPaneCount () )
            {
                // Sorting from newest to oldest
                CollectionUtils.sort ( expanded, newToOldPaneComparator );

                // Collapsing excessive expanded panes
                final int toCollapse = expanded.size () - accordion.getMaximumExpandedPaneCount ();
                for ( int i = 0; i < toCollapse; i++ )
                {
                    collapsePane ( expanded.get ( i ).getId () );
                }
            }
        }
    }

    /**
     * Returns {@link AccordionPaneState} for the {@link AccordionPane} with the specified identifier.
     *
     * @param id {@link AccordionPane} identifier
     * @return {@link AccordionPaneState} for the {@link AccordionPane} with the specified identifier
     */
    @NotNull
    protected AccordionPaneState getPaneState ( @NotNull final String id )
    {
        if ( states.containsKey ( id ) )
        {
            return states.get ( id );
        }
        else
        {
            throw new RuntimeException ( "Cannot find AccordionPaneState for identifier: " + id );
        }
    }

    @Nullable
    @Override
    public AccordionPane getFirstExpandedPane ()
    {
        AccordionPane firstExpanded = null;
        final WebAccordion accordion = getAccordion ();
        for ( int i = 0; i < accordion.getPaneCount (); i++ )
        {
            final AccordionPane pane = accordion.getPane ( i );
            if ( isPaneExpanded ( pane.getId () ) )
            {
                firstExpanded = pane;
                break;
            }
        }
        return firstExpanded;
    }

    @Nullable
    @Override
    public AccordionPane getLastExpandedPane ()
    {
        AccordionPane lastExpanded = null;
        final WebAccordion accordion = getAccordion ();
        for ( int i = accordion.getPaneCount () - 1; i >= 0; i-- )
        {
            final AccordionPane pane = accordion.getPane ( i );
            if ( isPaneExpanded ( pane.getId () ) )
            {
                lastExpanded = pane;
                break;
            }
        }
        return lastExpanded;
    }

    @NotNull
    @Override
    public List<AccordionPane> getExpandedPanes ()
    {
        final WebAccordion accordion = getAccordion ();
        final List<AccordionPane> expandedPanes = new ArrayList<AccordionPane> ( accordion.getPaneCount () );
        for ( int i = 0; i < accordion.getPaneCount (); i++ )
        {
            final AccordionPane pane = accordion.getPane ( i );
            if ( isPaneExpanded ( pane.getId () ) )
            {
                expandedPanes.add ( pane );
            }
        }
        return expandedPanes;
    }

    @NotNull
    @Override
    public List<String> getExpandedPaneIds ()
    {
        final WebAccordion accordion = getAccordion ();
        final List<String> expandedIds = new ArrayList<String> ( accordion.getPaneCount () );
        for ( int i = 0; i < accordion.getPaneCount (); i++ )
        {
            final AccordionPane pane = accordion.getPane ( i );
            if ( isPaneExpanded ( pane.getId () ) )
            {
                expandedIds.add ( pane.getId () );
            }
        }
        return expandedIds;
    }

    @Override
    public void setExpandedPaneIds ( @NotNull final List<String> ids )
    {
        final WebAccordion accordion = getAccordion ();
        for ( int i = 0; i < accordion.getPaneCount (); i++ )
        {
            final AccordionPane pane = accordion.getPane ( i );
            if ( ids.contains ( pane.getId () ) )
            {
                if ( isPaneCollapsed ( pane.getId () ) )
                {
                    expandUnconditionally ( pane.getId () );
                }
            }
            else
            {
                if ( isPaneExpanded ( pane.getId () ) )
                {
                    collapseUnconditionally ( pane.getId () );
                }
            }
        }
        validateStates ();
    }

    @NotNull
    @Override
    public int[] getExpandedPaneIndices ()
    {
        final WebAccordion accordion = getAccordion ();
        final List<Integer> expandedIndices = new ArrayList<Integer> ( accordion.getPaneCount () );
        for ( int i = 0; i < accordion.getPaneCount (); i++ )
        {
            final AccordionPane pane = accordion.getPane ( i );
            if ( isPaneExpanded ( pane.getId () ) )
            {
                expandedIndices.add ( i );
            }
        }
        return CollectionUtils.toIntArray ( expandedIndices );
    }

    @Override
    public void setExpandedPaneIndices ( @NotNull final int[] indices )
    {
        final WebAccordion accordion = getAccordion ();
        final List<Integer> expanded = CollectionUtils.asList ( indices );
        for ( int i = 0; i < accordion.getPaneCount (); i++ )
        {
            final AccordionPane pane = accordion.getPane ( i );
            if ( expanded.contains ( i ) )
            {
                if ( isPaneCollapsed ( pane.getId () ) )
                {
                    expandUnconditionally ( pane.getId () );
                }
            }
            else
            {
                if ( isPaneExpanded ( pane.getId () ) )
                {
                    collapseUnconditionally ( pane.getId () );
                }
            }
        }
        validateStates ();
    }

    @Override
    public boolean isPaneExpanded ( @NotNull final String id )
    {
        return getPaneState ( id ).isExpanded ();
    }

    @Override
    public boolean expandPane ( @NotNull final String id )
    {
        final boolean wasExpanded;
        if ( isPaneCollapsed ( id ) )
        {
            /**
             * Whenever we expand above the maximum allowed expanded panes we will collapse the most recently expanded one.
             * Age is determined by pane expansion times which are saved within {@link AccordionPaneState} object.
             */
            final WebAccordion accordion = getAccordion ();
            final List<AccordionPane> expanded = getExpandedPanes ();
            if ( expanded.size () < accordion.getMaximumExpandedPaneCount () )
            {
                // Simply expanding pane
                expandUnconditionally ( id );
                wasExpanded = true;
            }
            else if ( expanded.size () > 0 )
            {
                // Sorting from newest to oldest
                CollectionUtils.sort ( expanded, newToOldPaneComparator );

                // Collapsing previously expanded pane
                collapseUnconditionally ( expanded.get ( 0 ).getId () );
                expandUnconditionally ( id );
                wasExpanded = true;
            }
            else
            {
                // We do not have enough panes to collapse to meet maximum expanded panes count
                wasExpanded = false;
            }
        }
        else
        {
            wasExpanded = false;
        }
        return wasExpanded;
    }

    /**
     * Expands {@link AccordionPane} with the specified identifier without any additional checks.
     * This method is intended for internal use only to avoid issues with overlapping minimum/maximum conditions.
     *
     * @param id {@link AccordionPane} identifier
     */
    protected void expandUnconditionally ( @NotNull final String id )
    {
        getPaneState ( id ).setExpanded ( true );

        final WebAccordion accordion = getAccordion ();
        final AccordionPane pane = accordion.getPane ( id );

        final AccordionLayout layout = accordion.getLayout ();
        if ( layout != null )
        {
            layout.expandPane ( accordion, id );
        }
        else
        {
            pane.fireExpanding ( accordion );
            accordion.fireExpanding ( pane );
            SwingUtils.update ( accordion );
            pane.fireExpanded ( accordion );
            accordion.fireExpanded ( pane );
        }
    }

    @NotNull
    @Override
    public List<AccordionPane> getCollapsedPanes ()
    {
        final WebAccordion accordion = getAccordion ();
        final List<AccordionPane> collapsedPanes = new ArrayList<AccordionPane> ( accordion.getPaneCount () );
        for ( int i = 0; i < accordion.getPaneCount (); i++ )
        {
            final AccordionPane pane = accordion.getPane ( i );
            if ( isPaneCollapsed ( pane.getId () ) )
            {
                collapsedPanes.add ( pane );
            }
        }
        return collapsedPanes;
    }

    @NotNull
    @Override
    public List<String> getCollapsedPaneIds ()
    {
        final WebAccordion accordion = getAccordion ();
        final List<String> collapsedIds = new ArrayList<String> ( accordion.getPaneCount () );
        for ( int i = 0; i < accordion.getPaneCount (); i++ )
        {
            final AccordionPane pane = accordion.getPane ( i );
            if ( isPaneCollapsed ( pane.getId () ) )
            {
                collapsedIds.add ( pane.getId () );
            }
        }
        return collapsedIds;
    }

    @NotNull
    @Override
    public int[] getCollapsedPaneIndices ()
    {
        final WebAccordion accordion = getAccordion ();
        final List<Integer> collapsedIndices = new ArrayList<Integer> ( accordion.getPaneCount () );
        for ( int i = 0; i < accordion.getPaneCount (); i++ )
        {
            final AccordionPane pane = accordion.getPane ( i );
            if ( isPaneCollapsed ( pane.getId () ) )
            {
                collapsedIndices.add ( i );
            }
        }
        return CollectionUtils.toIntArray ( collapsedIndices );
    }

    @Override
    public boolean isPaneCollapsed ( @NotNull final String id )
    {
        return !isPaneExpanded ( id );
    }

    @Override
    public boolean collapsePane ( @NotNull final String id )
    {
        final boolean wasCollapsed;
        if ( isPaneExpanded ( id ) )
        {
            /**
             * Whenever we collapse below the minimum allowed expanded panes we will expand the most recently collapsed one.
             * Age is determined by pane expansion times which are saved within {@link AccordionPaneState} object.
             */
            final WebAccordion accordion = getAccordion ();
            final List<AccordionPane> expanded = getExpandedPanes ();
            final List<AccordionPane> collapsed = getCollapsedPanes ();
            if ( expanded.size () > accordion.getMinimumExpandedPaneCount () )
            {
                // Simply collapsing pane
                collapseUnconditionally ( id );
                wasCollapsed = true;
            }
            else if ( collapsed.size () > 0 )
            {
                // Sorting from newest to oldest
                CollectionUtils.sort ( collapsed, newToOldPaneComparator );

                // Expanding previously collapsed pane before we collapse new one
                expandUnconditionally ( collapsed.get ( 0 ).getId () );
                collapseUnconditionally ( id );
                wasCollapsed = true;
            }
            else
            {
                // We don't have enough panes to expand to meet minimum expanded panes count
                wasCollapsed = false;
            }
        }
        else
        {
            wasCollapsed = false;
        }
        return wasCollapsed;
    }

    /**
     * Collapses {@link AccordionPane} with the specified identifier without any additional checks.
     * This method is intended for internal use only to avoid issues with overlapping minimum/maximum conditions.
     *
     * @param id {@link AccordionPane} identifier
     */
    protected void collapseUnconditionally ( @NotNull final String id )
    {
        getPaneState ( id ).setExpanded ( false );

        final WebAccordion accordion = getAccordion ();
        final AccordionPane pane = accordion.getPane ( id );

        final AccordionLayout layout = accordion.getLayout ();
        if ( layout != null )
        {
            layout.collapsePane ( accordion, id );
        }
        else
        {
            pane.fireCollapsing ( accordion );
            accordion.fireCollapsing ( pane );
            SwingUtils.update ( accordion );
            pane.fireCollapsed ( accordion );
            accordion.fireCollapsed ( pane );
        }
    }
}