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
import com.alee.api.data.BoxOrientation;
import com.alee.api.jdk.Objects;
import com.alee.extended.WebContainer;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.utils.SwingUtils;
import com.alee.utils.TextUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Accordion component implementation.
 * It represents a set of grouped {@link AccordionPane}s that change their expansion state according to rules set by accordion.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebAccordion">How to use WebAccordion</a>
 * @see WebAccordion
 * @see AccordionPane
 * @see AccordionModel
 * @see WebAccordionModel
 */
public class WebAccordion extends WebContainer<WebAccordion, WAccordionUI>
{
    /**
     * Component properties.
     */
    public static final String MODEL_PROPERTY = "model";
    public static final String HEADER_POSITION_PROPERTY = "headerPosition";
    public static final String MINIMUM_EXPANDED_PROPERTY = "minimumExpanded";
    public static final String MAXIMUM_EXPANDED_PROPERTY = "maximumExpanded";
    public static final String MINIMUM_PANE_CONTENT_SIZE_PROPERTY = "minimumPaneContentSizee";
    public static final String PANES_PROPERTY = "panes";

    /**
     * {@link BoxOrientation} for header panel positioning, also defines accordion orientation.
     * {@link BoxOrientation#top} and {@link BoxOrientation#bottom} will result in vertical orientation.
     * {@link BoxOrientation#left} and {@link BoxOrientation#right} will result in horizontal orientation.
     * Cane also be {@code null} in which case default value will be used.
     */
    @Nullable
    protected BoxOrientation headerPosition;

    /**
     * Minimum amount of {@link AccordionPane}s that should be expanded at all times.
     * Can vary between {@code 0} and any number, even exceeding total amount of {@link AccordionPane}s available.
     * If minimum amount is higher than amount of {@link AccordionPane}s available - all of those panes will be kept expanded at all times.
     * Cane also be {@code null} in which case default value will be used.
     */
    @Nullable
    protected Integer minimumExpanded;

    /**
     * Maximum amount of {@link AccordionPane}s that can be expanded.
     * Can vary between {@code 1} and any number, even exceeding total amount of {@link AccordionPane}s available.
     * Cane also be {@code null} in which case default value will be used.
     */
    @Nullable
    protected Integer maximumExpanded;

    /**
     * Minimum size in pixels of a single {@link AccordionPane}'s content.
     * Can be set to any value from {@code 0} to any reasonable amount of pixels you want for each {@link AccordionPane}'s content.
     * It is used instead of the content's preferred size to ensure that {@link WebAccordion} preserves it's preferred size at all times.
     * Otherwise whenever you expand/collapse something {@link WebAccordion} preferred size would vary wildly and negatively affect layout.
     */
    @Nullable
    protected Integer minimumPaneContentSize;

    /**
     * {@link AccordionModel} implementation.
     */
    @Nullable
    protected AccordionModel model;

    /**
     * Constructs new {@link WebAccordion}.
     */
    public WebAccordion ()
    {
        this ( StyleId.auto, BoxOrientation.top );
    }

    /**
     * Constructs new {@link WebAccordion}.
     *
     * @param headerPosition {@link BoxOrientation} for header panel positioning, also defines accordion orientation
     */
    public WebAccordion ( @NotNull final BoxOrientation headerPosition )
    {
        this ( StyleId.auto, headerPosition );
    }

    /**
     * Constructs new {@link WebAccordion}.
     *
     * @param id style ID
     */
    public WebAccordion ( @NotNull final StyleId id )
    {
        this ( id, BoxOrientation.top );
    }

    /**
     * Constructs new {@link WebAccordion}.
     *
     * @param id             style ID
     * @param headerPosition {@link BoxOrientation} for header panel positioning, also defines accordion orientation
     */
    public WebAccordion ( @NotNull final StyleId id, @NotNull final BoxOrientation headerPosition )
    {
        setHeaderPosition ( headerPosition );
        setMinimumExpanded ( 1 );
        setMaximumExpanded ( 1 );
        setMinimumPaneContentSize ( 100 );
        setModel ( createModel () );
        updateUI ();
        setStyleId ( id );
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.accordion;
    }

    /**
     * Returns {@link AccordionModel} implementation.
     *
     * @return {@link AccordionModel} implementation
     */
    @Nullable
    public AccordionModel getModel ()
    {
        return model;
    }

    /**
     * Sets {@link AccordionModel} implementation.
     *
     * @param model new {@link AccordionModel} implementation
     * @return this {@link WebAccordion}
     */
    @NotNull
    public WebAccordion setModel ( @NotNull final AccordionModel model )
    {
        if ( this.model != model )
        {
            // Uninstalling previous model
            final AccordionModel old = this.model;
            if ( this.model != null )
            {
                this.model.uninstall ( this );
            }

            // Installing new model
            this.model = model;
            model.install ( this );

            // Firing model change event
            firePropertyChange ( MODEL_PROPERTY, old, model );
        }
        return this;
    }

    /**
     * Returns default {@link AccordionModel} implementation to be used.
     *
     * @return default {@link AccordionModel} implementation to be used
     */
    @NotNull
    protected AccordionModel createModel ()
    {
        return new WebAccordionModel ();
    }

    @Nullable
    @Override
    public AccordionLayout getLayout ()
    {
        return ( AccordionLayout ) super.getLayout ();
    }

    @Override
    public void setLayout ( @NotNull final LayoutManager layout )
    {
        if ( layout instanceof AccordionLayout && getLayout () != layout )
        {
            // Uninstalling previous layout manager
            final AccordionLayout old = getLayout ();
            if ( old != null )
            {
                old.uninstall ( this );
            }

            // Installing new layout manager
            final AccordionLayout newLayout = ( AccordionLayout ) layout;
            super.setLayout ( newLayout );
            newLayout.install ( this );


        }
        else
        {
            throw new IllegalArgumentException ( "Only AccordionModel instances are supported" );
        }
    }

    @Override
    protected void addImpl ( @NotNull final Component comp, @Nullable final Object constraints, final int index )
    {
        final List<AccordionPane> oldPanes = getPanes ();
        super.addImpl ( comp, constraints, index );
        firePropertyChange ( PANES_PROPERTY, oldPanes, getPanes () );
    }

    @Override
    public void remove ( final int index )
    {
        final List<AccordionPane> oldPanes = getPanes ();
        super.remove ( index );
        firePropertyChange ( PANES_PROPERTY, oldPanes, getPanes () );
    }

    /**
     * Returns {@link AccordionState} of this {@link WebAccordion}.
     *
     * @return {@link AccordionState} of this {@link WebAccordion}
     */
    @NotNull
    public AccordionState getAccordionState ()
    {
        return getModel () != null ? getModel ().getAccordionState () : new AccordionState ();
    }

    /**
     * Sets {@link AccordionState} for this {@link WebAccordion}.
     *
     * @param state {@link AccordionState}
     * @return this {@link WebAccordion}
     */
    @NotNull
    public WebAccordion setAccordionState ( @NotNull final AccordionState state )
    {
        if ( getModel () != null )
        {
            getModel ().setAccordionState ( state );
        }
        return this;
    }

    /**
     * Returns {@link BoxOrientation} for header panel positioning, also defines accordion orientation.
     *
     * @return {@link BoxOrientation} for header panel positioning, also defines accordion orientation
     */
    @NotNull
    public BoxOrientation getHeaderPosition ()
    {
        return headerPosition != null ? headerPosition : BoxOrientation.top;
    }

    /**
     * Sets {@link BoxOrientation} for header panel positioning, also defines accordion orientation.
     *
     * @param position {@link BoxOrientation} for header panel positioning, also defines accordion orientation
     * @return this {@link WebAccordion}
     */
    @NotNull
    public WebAccordion setHeaderPosition ( @NotNull final BoxOrientation position )
    {
        final BoxOrientation headerPosition = getHeaderPosition ();
        if ( headerPosition != position )
        {
            this.headerPosition = position;
            firePropertyChange ( HEADER_POSITION_PROPERTY, headerPosition, position );
        }
        return this;
    }

    /**
     * Returns minimum amount of {@link AccordionPane}s that should be expanded at all times.
     * Can vary between {@code 0} and any number, even exceeding total amount of {@link AccordionPane}s available.
     * If minimum amount is higher than amount of {@link AccordionPane}s available - all of those panes will be kept expanded at all times.
     *
     * @return minimum amount of {@link AccordionPane}s that should be expanded at all times
     */
    public int getMinimumExpanded ()
    {
        return minimumExpanded != null ? minimumExpanded : 1;
    }

    /**
     * Sets minimum amount of {@link AccordionPane}s that should be expanded at all times.
     * Can vary between {@code 0} and any number, even exceeding total amount of {@link AccordionPane}s available.
     * If minimum amount is set to {@code 0} you will be able to collapse all panes, although it is not recommended for some parent layouts.
     * If minimum amount is higher than amount of {@link AccordionPane}s available - all of those panes will be kept expanded at all times.
     *
     * @param minimum minimum amount of {@link AccordionPane}s that should be expanded at all times
     * @return this {@link WebAccordion}
     */
    @NotNull
    public WebAccordion setMinimumExpanded ( final int minimum )
    {
        if ( minimum >= 0 )
        {
            if ( minimum > getMaximumExpanded () )
            {
                setMaximumExpanded ( minimum );
            }

            final int minimumExpanded = getMinimumExpanded ();
            if ( Objects.notEquals ( minimumExpanded, minimum ) )
            {
                this.minimumExpanded = minimum;
                firePropertyChange ( MINIMUM_EXPANDED_PROPERTY, minimumExpanded, minimum );
            }
        }
        else
        {
            throw new IllegalArgumentException ( "Minimum expanded AccordionPane amount cannot be less than zero" );
        }
        return this;
    }

    /**
     * Returns maximum amount of {@link AccordionPane}s that can be expanded.
     * Can vary between {@code 1} and any number, even exceeding total amount of {@link AccordionPane}s available.
     *
     * @return maximum amount of {@link AccordionPane}s that can be expanded
     */
    public int getMaximumExpanded ()
    {
        return maximumExpanded != null ? maximumExpanded : 1;
    }

    /**
     * Sets maximum amount of {@link AccordionPane}s that can be expanded.
     * Can vary between {@code 1} and any number, even exceeding total amount of {@link AccordionPane}s available.
     *
     * @param maximum maximum amount of {@link AccordionPane}s that can be expanded
     * @return this {@link WebAccordion}
     */
    @NotNull
    public WebAccordion setMaximumExpanded ( final int maximum )
    {
        if ( maximum >= 1 )
        {
            if ( maximum < getMinimumExpanded () )
            {
                setMinimumExpanded ( maximum );
            }

            final int maximumExpanded = getMaximumExpanded ();
            if ( Objects.notEquals ( maximumExpanded, maximum ) )
            {
                this.maximumExpanded = maximum;
                firePropertyChange ( MAXIMUM_EXPANDED_PROPERTY, maximumExpanded, maximum );
            }
        }
        else
        {
            throw new IllegalArgumentException ( "Maximum expanded AccordionPane amount cannot be less than one" );
        }
        return this;
    }

    /**
     * Returns minimum size in pixels of a single {@link AccordionPane}'s content.
     * Can be any value from {@code 0} to any reasonable amount of pixels you want for each {@link AccordionPane}'s content.
     * It is used instead of the content's preferred size to ensure that {@link WebAccordion} preserves it's preferred size at all times.
     * Otherwise whenever you expand/collapse something {@link WebAccordion} preferred size would vary wildly and negatively affect layout.
     *
     * @return minimum size in pixels of a single {@link AccordionPane}'s content
     */
    public int getMinimumPaneContentSize ()
    {
        return minimumPaneContentSize != null ? minimumPaneContentSize : 0;
    }

    /**
     * Sets minimum size in pixels of a single {@link AccordionPane}'s content.
     * Can be set to any value from {@code 0} to any reasonable amount of pixels you want for each {@link AccordionPane}'s content.
     * It is used instead of the content's preferred size to ensure that {@link WebAccordion} preserves it's preferred size at all times.
     * Otherwise whenever you expand/collapse something {@link WebAccordion} preferred size would vary wildly and negatively affect layout.
     *
     * @param minimum minimum size in pixels of a single {@link AccordionPane}'s content
     * @return this {@link WebAccordion}
     */
    @NotNull
    public WebAccordion setMinimumPaneContentSize ( @Nullable final Integer minimum )
    {
        final Integer minimumPaneContentSize = this.minimumPaneContentSize;
        if ( Objects.notEquals ( minimumPaneContentSize, minimum ) )
        {
            this.minimumPaneContentSize = minimum;
            firePropertyChange ( MINIMUM_PANE_CONTENT_SIZE_PROPERTY, minimumPaneContentSize, minimum );
        }
        return this;
    }

    /**
     * Returns {@link AccordionPane} count within the {@link WebAccordion}.
     *
     * @return {@link AccordionPane} count within the {@link WebAccordion}
     */
    public int getPaneCount ()
    {
        return getComponentCount ();
    }

    /**
     * Returns whether or not {@link AccordionPane} with the specified identifier exists in {@link WebAccordion}.
     *
     * @param id {@link AccordionPane} identifier
     * @return {@code true} if {@link AccordionPane} with the specified identifier exists in {@link WebAccordion}, {@code false} otherwise
     */
    public boolean containsPane ( @NotNull final String id )
    {
        boolean contains = false;
        for ( int i = 0; i < getComponentCount (); i++ )
        {
            final AccordionPane pane = ( AccordionPane ) getComponent ( i );
            if ( pane.getId ().equals ( id ) )
            {
                contains = true;
                break;
            }
        }
        return contains;
    }

    /**
     * Returns {@link List} of all existing {@link AccordionPane} within this {@link WebAccordion}.
     *
     * @return {@link List} of all existing {@link AccordionPane} within this {@link WebAccordion}
     */
    @NotNull
    public List<AccordionPane> getPanes ()
    {
        final List<AccordionPane> panes = new ArrayList<AccordionPane> ( getComponentCount () );
        for ( int i = 0; i < getComponentCount (); i++ )
        {
            panes.add ( ( AccordionPane ) getComponent ( i ) );
        }
        return panes;
    }

    /**
     * Returns {@link AccordionPane} with the specified identifier or {@code null} if one doesn't exist in {@link WebAccordion}.
     *
     * @param id {@link AccordionPane} identifier
     * @return {@link AccordionPane} with the specified identifier or {@code null} if one doesn't exist in {@link WebAccordion}
     */
    @NotNull
    public AccordionPane getPane ( @NotNull final String id )
    {
        for ( int i = 0; i < getComponentCount (); i++ )
        {
            final AccordionPane pane = ( AccordionPane ) getComponent ( i );
            if ( pane.getId ().equals ( id ) )
            {
                return pane;
            }
        }
        throw new RuntimeException ( "Cannot find AccordionPane with identifier: " + id );
    }

    /**
     * Adds new {@link AccordionPane} to this accordion.
     *
     * @param icon    {@link AccordionPane} title icon
     * @param title   {@link AccordionPane} title
     * @param content {@link AccordionPane} content {@link Component}
     * @return created {@link AccordionPane}
     */
    @NotNull
    public AccordionPane addPane ( @Nullable final Icon icon, @Nullable final String title,
                                   @NotNull final Component content )
    {
        return addPane ( TextUtils.generateId ( "AP" ), getComponentCount (), icon, title, content );
    }

    /**
     * Adds new {@link AccordionPane} to this accordion.
     *
     * @param index   {@link AccordionPane} z-index in this accordion
     * @param icon    {@link AccordionPane} title icon
     * @param title   {@link AccordionPane} title
     * @param content {@link AccordionPane} content {@link Component}
     * @return created {@link AccordionPane}
     */
    @NotNull
    public AccordionPane addPane ( final int index, @Nullable final Icon icon, @Nullable final String title,
                                   @NotNull final Component content )
    {
        return addPane ( TextUtils.generateId ( "AP" ), index, icon, title, content );
    }

    /**
     * Adds new {@link AccordionPane} to this accordion.
     *
     * @param id      {@link AccordionPane} identifier
     * @param icon    {@link AccordionPane} title icon
     * @param title   {@link AccordionPane} title
     * @param content {@link AccordionPane} content {@link Component}
     * @return created {@link AccordionPane}
     */
    @NotNull
    public AccordionPane addPane ( @NotNull final String id, @Nullable final Icon icon, @Nullable final String title,
                                   @NotNull final Component content )
    {
        return addPane ( id, getComponentCount (), icon, title, content );
    }

    /**
     * Adds new {@link AccordionPane} to this accordion.
     *
     * @param id      {@link AccordionPane} identifier
     * @param index   {@link AccordionPane} z-index in this accordion
     * @param icon    {@link AccordionPane} title icon
     * @param title   {@link AccordionPane} title
     * @param content {@link AccordionPane} content {@link Component}
     * @return created {@link AccordionPane}
     */
    @NotNull
    public AccordionPane addPane ( @NotNull final String id, final int index, @Nullable final Icon icon, @Nullable final String title,
                                   @NotNull final Component content )
    {
        final AccordionPane accordionPane = createAccordionPane ( id, icon, title, content );
        add ( accordionPane, index );
        SwingUtils.update ( this );
        return accordionPane;
    }

    /**
     * Returns newly created {@link AccordionPane}.
     *
     * @param id      {@link AccordionPane} identifier
     * @param icon    {@link AccordionPane} title icon
     * @param title   {@link AccordionPane} title
     * @param content {@link AccordionPane} content {@link Component}
     * @return newly created {@link AccordionPane}
     */
    @NotNull
    protected AccordionPane createAccordionPane ( @NotNull final String id, @Nullable final Icon icon, @Nullable final String title,
                                                  @NotNull final Component content )
    {
        return new AccordionPane ( this, id, icon, title, content );
    }

    /**
     * Removes {@link AccordionPane} with the specified identifier from this {@link WebAccordion} if it exists.
     * Returns either removed {@link AccordionPane} or {@code null} if there was no {@link AccordionPane} with the specified identifier.
     *
     * @param id {@link AccordionPane} identifier
     * @return either removed {@link AccordionPane} or {@code null} if there was no {@link AccordionPane} with the specified identifier
     */
    @Nullable
    public AccordionPane removePane ( @NotNull final String id )
    {
        final AccordionPane pane = getPane ( id );
        remove ( pane );
        SwingUtils.update ( this );
        return pane;
    }

    /**
     * Returns whether or not {@link AccordionPane} with the specified identifier is expanded.
     *
     * @param id {@link AccordionPane} identifier
     * @return {@code true} if {@link AccordionPane} with the specified identifier is expanded, {@code false} otherwise
     */
    public boolean isExpanded ( @NotNull final String id )
    {
        return getModel () != null && getModel ().isExpanded ( id );
    }

    /**
     * Changes state of {@link AccordionPane} with the specified identifier to either expanded or collapsed.
     *
     * @param id       {@link AccordionPane} identifier
     * @param expanded whether or not {@link AccordionPane} with the specified identifier needs to be expanded or collapsed
     * @return {@code true} if state of {@link AccordionPane} with the specified identifier was changed
     */
    public boolean setExpanded ( @NotNull final String id, final boolean expanded )
    {
        return expanded ? expand ( id ) : collapse ( id );
    }

    /**
     * Asks {@link WebAccordion} to expand {@link AccordionPane} with the specified identifier.
     *
     * @param id {@link AccordionPane} identifier
     * @return {@code true} if {@link AccordionPane} was successfully expanded, {@code false} otherwise
     */
    public boolean expand ( @NotNull final String id )
    {
        return getModel () != null && getModel ().expand ( id, true );
    }

    /**
     * Returns whether or not {@link AccordionPane} with the specified identifier is collapsed.
     *
     * @param id {@link AccordionPane} identifier
     * @return {@code true} if {@link AccordionPane} with the specified identifier is collapsed, {@code false} otherwise
     */
    public boolean isCollapsed ( @NotNull final String id )
    {
        return getModel () != null && getModel ().isCollapsed ( id );
    }

    /**
     * Changes state of {@link AccordionPane} with the specified identifier to either collapsed or expanded.
     *
     * @param id        {@link AccordionPane} identifier
     * @param collapsed whether or not {@link AccordionPane} with the specified identifier needs to be collapsed or expanded
     * @return {@code true} if state of {@link AccordionPane} with the specified identifier was changed
     */
    public boolean setCollapsed ( @NotNull final String id, final boolean collapsed )
    {
        return collapsed ? collapse ( id ) : expand ( id );
    }

    /**
     * Asks {@link WebAccordion} to collapse {@link AccordionPane} with the specified identifier.
     *
     * @param id {@link AccordionPane} identifier
     * @return {@code true} if {@link AccordionPane} was successfully collapsed, {@code false} otherwise
     */
    public boolean collapse ( @NotNull final String id )
    {
        return getModel () != null && getModel ().collapse ( id, true );
    }

    /**
     * Returns {@link List} of expanded {@link AccordionPane}s.
     *
     * @return {@link List} of expanded {@link AccordionPane}s
     */
    @NotNull
    public List<AccordionPane> getExpanded ()
    {
        return getModel () != null ? getModel ().getExpanded () : new ArrayList<AccordionPane> ();
    }

    /**
     * Returns {@link List} of collapsed {@link AccordionPane}s
     *
     * @return {@link List} of collapsed {@link AccordionPane}s
     */
    @NotNull
    public List<AccordionPane> getCollapsed ()
    {
        return getModel () != null ? getModel ().getCollapsed () : new ArrayList<AccordionPane> ();
    }

    /**
     * Returns whether or not {@link AccordionPane} with the specified identifier is in transition to either of two expansion states.
     *
     * @param id {@link AccordionPane} identifier
     * @return {@code true} if {@link AccordionPane} with the specified identifier is in transition, {@code false} otherwise
     */
    public boolean isInTransition ( @NotNull final String id )
    {
        return getLayout () != null && getLayout ().isInTransition ( id );
    }

    /**
     * Adds {@link AccordionListener}.
     *
     * @param listener {@link AccordionListener} to add
     */
    public void addAccordionListener ( @NotNull final AccordionListener listener )
    {
        listenerList.add ( AccordionListener.class, listener );
    }

    /**
     * Removes {@link AccordionListener}.
     *
     * @param listener {@link AccordionListener} to remove
     */
    public void removeAccordionListener ( @NotNull final AccordionListener listener )
    {
        listenerList.remove ( AccordionListener.class, listener );
    }

    /**
     * Notifies that specified {@link AccordionPane} started expanding.
     *
     * @param pane {@link AccordionPane} that is being expanded
     */
    public void fireExpanding ( @NotNull final AccordionPane pane )
    {
        for ( final AccordionListener listener : listenerList.getListeners ( AccordionListener.class ) )
        {
            listener.expanding ( this, pane );
        }
    }

    /**
     * Notifies when {@link WebAccordion} finished expanding.
     *
     * @param pane {@link AccordionPane} that was expanded
     */
    public void fireExpanded ( @NotNull final AccordionPane pane )
    {
        for ( final AccordionListener listener : listenerList.getListeners ( AccordionListener.class ) )
        {
            listener.expanded ( this, pane );
        }
    }

    /**
     * Notifies when {@link WebAccordion} starts to collapse.
     *
     * @param pane {@link AccordionPane} that is being collapsed
     */
    public void fireCollapsing ( @NotNull final AccordionPane pane )
    {
        for ( final AccordionListener listener : listenerList.getListeners ( AccordionListener.class ) )
        {
            listener.collapsing ( this, pane );
        }
    }

    /**
     * Notifies when {@link WebAccordion} finished collapsing.
     *
     * @param pane {@link AccordionPane} that was collapsed
     */
    public void fireCollapsed ( @NotNull final AccordionPane pane )
    {
        for ( final AccordionListener listener : listenerList.getListeners ( AccordionListener.class ) )
        {
            listener.collapsed ( this, pane );
        }
    }

    /**
     * Returns the look and feel (LaF) object that renders this component.
     *
     * @return the {@link WAccordionUI} object that renders this component
     */
    @Nullable
    public WAccordionUI getUI ()
    {
        return ( WAccordionUI ) ui;
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WAccordionUI}
     */
    public void setUI ( @Nullable final WAccordionUI ui )
    {
        super.setUI ( ui );
    }

    @Override
    public void updateUI ()
    {
        StyleManager.getDescriptor ( this ).updateUI ( this );
    }

    @NotNull
    @Override
    public String getUIClassID ()
    {
        return StyleManager.getDescriptor ( this ).getUIClassId ();
    }
}