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
 * @see AccordionModel
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
     * Constructs new {@link WebAccordionModel}.
     */
    public WebAccordionModel ()
    {
        this.states = new HashMap<String, AccordionPaneState> ();
    }

    @Override
    public void install ( @NotNull final WebAccordion accordion )
    {
        if ( this.accordion == null )
        {
            this.accordion = accordion;
            this.accordion.addPropertyChangeListener ( this );
            this.accordion.addContainerListener ( this );

            // Adding all missing states
            for ( final AccordionPane pane : accordion.getPanes () )
            {
                if ( !states.containsKey ( pane.getId () ) )
                {
                    states.put ( pane.getId (), new AccordionPaneState ( pane.getId (), false ) );
                }
            }

            // Validating states
            validateStates ( false );
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
        // Updating
        states = accordionState.states ();

        // Validating states
        validateStates ( true );
    }

    @Override
    public void propertyChange ( final PropertyChangeEvent e )
    {
        final String property = e.getPropertyName ();
        if ( Objects.equals ( property, WebAccordion.MINIMUM_EXPANDED_PROPERTY, WebAccordion.MAXIMUM_EXPANDED_PROPERTY ) )
        {
            // Validating states
            validateStates ( true );
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
        validateStates ( false );
    }

    @Override
    public void componentRemoved ( final ContainerEvent e )
    {
        // Removing state for the removed pane
        final AccordionPane pane = ( AccordionPane ) e.getChild ();
        states.remove ( pane.getId () );

        // Validating states
        validateStates ( false );
    }

    /**
     * Validating that our states comply with {@link WebAccordion} conditions.
     * If they don't - we will automatically adjust states to fix issues.
     *
     * @param animated whether or not transition should be animated
     */
    protected void validateStates ( final boolean animated )
    {
        if ( accordion != null )
        {
            final List<AccordionPane> expanded = getExpanded ();
            final List<AccordionPane> collapsed = getCollapsed ();
            if ( expanded.size () < accordion.getMinimumExpanded () )
            {
                final int toExpand = Math.min ( collapsed.size (), accordion.getMinimumExpanded () - expanded.size () );
                for ( int i = 0; i < toExpand; i++ )
                {
                    expand ( collapsed.get ( i ).getId (), animated );
                }
            }
            else if ( expanded.size () > accordion.getMaximumExpanded () )
            {
                final int toCollapse = expanded.size () - accordion.getMaximumExpanded ();
                for ( int i = 0; i < toCollapse; i++ )
                {
                    collapse ( expanded.get ( i ).getId (), animated );
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

    @NotNull
    @Override
    public List<AccordionPane> getExpanded ()
    {
        final WebAccordion accordion = getAccordion ();
        final List<AccordionPane> expanded = new ArrayList<AccordionPane> ( accordion.getComponentCount () );
        for ( int i = 0; i < accordion.getComponentCount (); i++ )
        {
            final AccordionPane pane = ( AccordionPane ) accordion.getComponent ( i );
            if ( isExpanded ( pane.getId () ) )
            {
                expanded.add ( pane );
            }
        }

        // Sorting from oldest to newest
        CollectionUtils.sort ( expanded, new Comparator<AccordionPane> ()
        {
            @Override
            public int compare ( final AccordionPane pane1, final AccordionPane pane2 )
            {
                return new Long ( getPaneState ( pane1.getId () ).getTime () )
                        .compareTo ( getPaneState ( pane2.getId () ).getTime () );
            }
        } );

        return expanded;
    }

    @Override
    public boolean isExpanded ( @NotNull final String id )
    {
        return getPaneState ( id ).isExpanded ();
    }

    @Override
    public boolean expand ( @NotNull final String id, final boolean animated )
    {
        final boolean wasExpanded;
        if ( isCollapsed ( id ) )
        {
            /**
             * Whenever we expand above the maximum allowed expanded panes we have to collapse the oldest one.
             * Age is determined by pane expansion times which are saved within {@link AccordionPaneState} object.
             * All panes returned from {@link #getExpanded()} are automatically sorted by age (from oldest to most recent).
             */
            final WebAccordion accordion = getAccordion ();
            final List<AccordionPane> expanded = getExpanded ();
            final int maximumExpanded = accordion.getMaximumExpanded ();
            if ( expanded.size () < maximumExpanded )
            {
                // Simply expanding pane
                expandUnconditionally ( id, animated );
                wasExpanded = true;
            }
            else
            {
                // Collapsing previously expanded pane
                collapseUnconditionally ( expanded.get ( expanded.size () - 1 ).getId (), animated );
                expandUnconditionally ( id, animated );
                wasExpanded = true;
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
     * @param id       {@link AccordionPane} identifier
     * @param animated whether or not transition should be animated
     */
    protected void expandUnconditionally ( @NotNull final String id, final boolean animated )
    {
        getPaneState ( id ).setExpanded ( true );

        final WebAccordion accordion = getAccordion ();
        final AccordionPane pane = accordion.getPane ( id );

        final AccordionLayout layout = accordion.getLayout ();
        if ( layout != null )
        {
            layout.expand ( accordion, id, animated );
        }
        else
        {
            accordion.fireExpanding ( pane );
            SwingUtils.update ( accordion );
            accordion.fireExpanded ( pane );
        }
    }

    @NotNull
    @Override
    public List<AccordionPane> getCollapsed ()
    {
        final WebAccordion accordion = getAccordion ();
        final List<AccordionPane> collapsed = new ArrayList<AccordionPane> ( accordion.getComponentCount () );
        for ( int i = 0; i < accordion.getComponentCount (); i++ )
        {
            final AccordionPane pane = ( AccordionPane ) accordion.getComponent ( i );
            if ( isCollapsed ( pane.getId () ) )
            {
                collapsed.add ( pane );
            }
        }

        // Sorting from oldest to newest
        CollectionUtils.sort ( collapsed, new Comparator<AccordionPane> ()
        {
            @Override
            public int compare ( final AccordionPane pane1, final AccordionPane pane2 )
            {
                return new Long ( getPaneState ( pane1.getId () ).getTime () )
                        .compareTo ( getPaneState ( pane2.getId () ).getTime () );
            }
        } );

        return collapsed;
    }

    @Override
    public boolean isCollapsed ( @NotNull final String id )
    {
        return !isExpanded ( id );
    }

    @Override
    public boolean collapse ( @NotNull final String id, final boolean animated )
    {
        final boolean wasCollapsed;
        if ( isExpanded ( id ) )
        {
            /**
             * We cannot be sure what to expand when we're going below minimum.
             * This is why we simply ignore such collapse operations to avoid unpredictable result.
             * The only exception is when amount of panes that can still be expanded besides the collapsed one is one.
             */
            final WebAccordion accordion = getAccordion ();
            final List<AccordionPane> expanded = getExpanded ();
            final List<AccordionPane> collapsed = getCollapsed ();
            final int minimumExpanded = accordion.getMinimumExpanded ();
            if ( expanded.size () > minimumExpanded )
            {
                // Simply collapsing pane
                collapseUnconditionally ( id, animated );
                wasCollapsed = true;
            }
            else
            {
                // Expanding previously collapsed pane before we collapse new one
                expandUnconditionally ( collapsed.get ( collapsed.size () - 1 ).getId (), animated );
                collapseUnconditionally ( id, animated );
                wasCollapsed = true;
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
     * @param id       {@link AccordionPane} identifier
     * @param animated whether or not transition should be animated
     */
    protected void collapseUnconditionally ( @NotNull final String id, final boolean animated )
    {
        getPaneState ( id ).setExpanded ( false );

        final WebAccordion accordion = getAccordion ();
        final AccordionPane pane = accordion.getPane ( id );

        final AccordionLayout layout = accordion.getLayout ();
        if ( layout != null )
        {
            layout.collapse ( accordion, id, animated );
        }
        else
        {
            accordion.fireCollapsing ( pane );
            SwingUtils.update ( accordion );
            accordion.fireCollapsed ( accordion.getPane ( id ) );
        }
    }
}