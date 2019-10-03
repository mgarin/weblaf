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
    public static final String MINIMUM_EXPANDED_PANE_COUNT_PROPERTY = "minimumExpandedPaneCount";
    public static final String MAXIMUM_EXPANDED_PANE_COUNT_PROPERTY = "maximumExpandedPaneCount";
    public static final String MINIMUM_PANE_CONTENT_SIZE_PROPERTY = "minimumPaneContentSize";
    public static final String ANIMATED_PROPERTY = "animated";
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
    protected Integer minimumExpandedPaneCount;

    /**
     * Maximum amount of {@link AccordionPane}s that can be expanded.
     * Can vary between {@code 1} and any number, even exceeding total amount of {@link AccordionPane}s available.
     * Cane also be {@code null} in which case default value will be used.
     */
    @Nullable
    protected Integer maximumExpandedPaneCount;

    /**
     * Minimum size in pixels of a single {@link AccordionPane}'s content.
     * Can be set to any value from {@code 0} to any reasonable amount of pixels you want for each {@link AccordionPane}'s content.
     * It is used instead of the content's preferred size to ensure that {@link WebAccordion} preserves it's preferred size at all times.
     * Otherwise whenever you expand/collapse something {@link WebAccordion} preferred size would vary wildly and negatively affect layout.
     */
    @Nullable
    protected Integer minimumPaneContentSize;

    /**
     * Whether or not {@link AccordionPane} expansion and collapse should be animated.
     * By default it is {@code null} which is equal to {@code true} value.
     */
    @Nullable
    protected Boolean animated;

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
        this ( StyleId.auto, BoxOrientation.top, 1, 1 );
    }

    /**
     * Constructs new {@link WebAccordion}.
     *
     * @param minimumExpandedPaneCount minimum amount of {@link AccordionPane}s that should be expanded at all times
     * @param maximumExpandedPaneCount maximum amount of {@link AccordionPane}s that can be expanded
     */
    public WebAccordion ( final int minimumExpandedPaneCount, final int maximumExpandedPaneCount )
    {
        this ( StyleId.auto, BoxOrientation.top, minimumExpandedPaneCount, maximumExpandedPaneCount );
    }

    /**
     * Constructs new {@link WebAccordion}.
     *
     * @param headerPosition {@link BoxOrientation} for header panel positioning, also defines accordion orientation
     */
    public WebAccordion ( @NotNull final BoxOrientation headerPosition )
    {
        this ( StyleId.auto, headerPosition, 1, 1 );
    }

    /**
     * Constructs new {@link WebAccordion}.
     *
     * @param headerPosition           {@link BoxOrientation} for header panel positioning, also defines accordion orientation
     * @param minimumExpandedPaneCount minimum amount of {@link AccordionPane}s that should be expanded at all times
     * @param maximumExpandedPaneCount maximum amount of {@link AccordionPane}s that can be expanded
     */
    public WebAccordion ( @NotNull final BoxOrientation headerPosition, final int minimumExpandedPaneCount,
                          final int maximumExpandedPaneCount )
    {
        this ( StyleId.auto, headerPosition, minimumExpandedPaneCount, maximumExpandedPaneCount );
    }

    /**
     * Constructs new {@link WebAccordion}.
     *
     * @param id style ID
     */
    public WebAccordion ( @NotNull final StyleId id )
    {
        this ( id, BoxOrientation.top, 1, 1 );
    }

    /**
     * Constructs new {@link WebAccordion}.
     *
     * @param id                       style ID
     * @param minimumExpandedPaneCount minimum amount of {@link AccordionPane}s that should be expanded at all times
     * @param maximumExpandedPaneCount maximum amount of {@link AccordionPane}s that can be expanded
     */
    public WebAccordion ( @NotNull final StyleId id, final int minimumExpandedPaneCount, final int maximumExpandedPaneCount )
    {
        this ( id, BoxOrientation.top, minimumExpandedPaneCount, maximumExpandedPaneCount );
    }

    /**
     * Constructs new {@link WebAccordion}.
     *
     * @param id             style ID
     * @param headerPosition {@link BoxOrientation} for header panel positioning, also defines accordion orientation
     */
    public WebAccordion ( @NotNull final StyleId id, @NotNull final BoxOrientation headerPosition )
    {
        this ( id, headerPosition, 1, 1 );
    }

    /**
     * Constructs new {@link WebAccordion}.
     *
     * @param id                       style ID
     * @param headerPosition           {@link BoxOrientation} for header panel positioning, also defines accordion orientation
     * @param minimumExpandedPaneCount minimum amount of {@link AccordionPane}s that should be expanded at all times
     * @param maximumExpandedPaneCount maximum amount of {@link AccordionPane}s that can be expanded
     */
    public WebAccordion ( @NotNull final StyleId id, @NotNull final BoxOrientation headerPosition,
                          final int minimumExpandedPaneCount, final int maximumExpandedPaneCount )
    {
        setHeaderPosition ( headerPosition );
        setMinimumExpandedPaneCount ( minimumExpandedPaneCount );
        setMaximumExpandedPaneCount ( maximumExpandedPaneCount );
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
            firePropertyChange ( ANIMATED_PROPERTY, old, model );
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
    public int getMinimumExpandedPaneCount ()
    {
        return minimumExpandedPaneCount != null ? minimumExpandedPaneCount : 1;
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
    public WebAccordion setMinimumExpandedPaneCount ( final int minimum )
    {
        if ( minimum >= 0 )
        {
            if ( minimum > getMaximumExpandedPaneCount () )
            {
                setMaximumExpandedPaneCount ( minimum );
            }

            final int minimumExpanded = getMinimumExpandedPaneCount ();
            if ( Objects.notEquals ( minimumExpanded, minimum ) )
            {
                this.minimumExpandedPaneCount = minimum;
                firePropertyChange ( MINIMUM_EXPANDED_PANE_COUNT_PROPERTY, minimumExpanded, minimum );
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
    public int getMaximumExpandedPaneCount ()
    {
        return maximumExpandedPaneCount != null ? maximumExpandedPaneCount : 1;
    }

    /**
     * Sets maximum amount of {@link AccordionPane}s that can be expanded.
     * Can vary between {@code 1} and any number, even exceeding total amount of {@link AccordionPane}s available.
     *
     * @param maximum maximum amount of {@link AccordionPane}s that can be expanded
     * @return this {@link WebAccordion}
     */
    @NotNull
    public WebAccordion setMaximumExpandedPaneCount ( final int maximum )
    {
        if ( maximum >= 1 )
        {
            if ( maximum < getMinimumExpandedPaneCount () )
            {
                setMinimumExpandedPaneCount ( maximum );
            }

            final int maximumExpanded = getMaximumExpandedPaneCount ();
            if ( Objects.notEquals ( maximumExpanded, maximum ) )
            {
                this.maximumExpandedPaneCount = maximum;
                firePropertyChange ( MAXIMUM_EXPANDED_PANE_COUNT_PROPERTY, maximumExpanded, maximum );
            }
        }
        else
        {
            throw new IllegalArgumentException ( "Maximum expanded AccordionPane amount cannot be less than one" );
        }
        return this;
    }

    /**
     * Sets minimum and maximum amount of {@link AccordionPane}s that can be expanded.
     * Minimum amount can vary between {@code 0} and any number, even exceeding total amount of {@link AccordionPane}s available.
     * Maximum amount can vary between {@code 1} and any number, even exceeding total amount of {@link AccordionPane}s available.
     *
     * @param minimum minimum amount of {@link AccordionPane}s that should be expanded at all times
     * @param maximum maximum amount of {@link AccordionPane}s that can be expanded
     * @return this {@link WebAccordion}
     */
    @NotNull
    public WebAccordion setExpandedPaneLimit ( final int minimum, final int maximum )
    {
        setMinimumExpandedPaneCount ( minimum );
        setMaximumExpandedPaneCount ( maximum );
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
     * Returns whether or not {@link AccordionPane} expansion and collapse is animated.
     *
     * @return {@code true} if {@link AccordionPane} expansion and collapse is animated, {@code false} otherwise
     */
    public boolean isAnimated ()
    {
        return animated == null || animated;
    }

    /**
     * Sets whether or not {@link AccordionPane} expansion and collapse should be animated.
     *
     * @param animated whether or not {@link AccordionPane} expansion and collapse should be animated
     * @return this {@link WebAccordion}
     */
    public WebAccordion setAnimated ( final boolean animated )
    {
        final Boolean wasAnimated = this.animated;
        if ( Objects.notEquals ( animated, wasAnimated ) )
        {
            this.animated = animated;
            firePropertyChange ( ANIMATED_PROPERTY, wasAnimated, ( Boolean ) animated );
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
        for ( int i = 0; i < getPaneCount (); i++ )
        {
            final AccordionPane pane = getPane ( i );
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
        final List<AccordionPane> panes = new ArrayList<AccordionPane> ( getPaneCount () );
        for ( int i = 0; i < getPaneCount (); i++ )
        {
            panes.add ( getPane ( i ) );
        }
        return panes;
    }

    /**
     * Returns {@link AccordionPane} with the specified identifier.
     *
     * @param id {@link AccordionPane} identifier
     * @return {@link AccordionPane} with the specified identifier.
     */
    @NotNull
    public AccordionPane getPane ( @NotNull final String id )
    {
        for ( int i = 0; i < getPaneCount (); i++ )
        {
            final AccordionPane pane = getPane ( i );
            if ( pane.getId ().equals ( id ) )
            {
                return pane;
            }
        }
        throw new RuntimeException ( "Cannot find AccordionPane with identifier: " + id );
    }

    /**
     * Returns {@link AccordionPane} with the specified identifier or {@code null} if one doesn't exist in {@link WebAccordion}.
     *
     * @param id {@link AccordionPane} identifier
     * @return {@link AccordionPane} with the specified identifier or {@code null} if one doesn't exist in {@link WebAccordion}
     */
    @Nullable
    public AccordionPane findPane ( @NotNull final String id )
    {
        AccordionPane result = null;
        for ( int i = 0; i < getPaneCount (); i++ )
        {
            final AccordionPane pane = getPane ( i );
            if ( pane.getId ().equals ( id ) )
            {
                result = pane;
            }
        }
        return result;
    }

    /**
     * Returns index of {@link AccordionPane} with the specified identifier.
     *
     * @param id {@link AccordionPane} identifier
     * @return index of {@link AccordionPane} with the specified identifier
     */
    public int getPaneIndex ( @NotNull final String id )
    {
        for ( int i = 0; i < getPaneCount (); i++ )
        {
            final AccordionPane pane = getPane ( i );
            if ( pane.getId ().equals ( id ) )
            {
                return i;
            }
        }
        throw new RuntimeException ( "Cannot find AccordionPane with identifier: " + id );
    }

    /**
     * Returns index of the specified {@link AccordionPane} or {@code -1} if it not a part of this {@link WebAccordion}.
     *
     * @param pane {@link AccordionPane} to find index for
     * @return index of the specified {@link AccordionPane} or {@code -1} if it not a part of this {@link WebAccordion}
     */
    public int getPaneIndex ( @Nullable final AccordionPane pane )
    {
        return getComponentZOrder ( pane );
    }

    /**
     * Returns {@link AccordionPane} at the specified index.
     *
     * @param index {@link AccordionPane} index
     * @return {@link AccordionPane} at the specified index.
     */
    @NotNull
    public AccordionPane getPane ( final int index )
    {
        return ( AccordionPane ) getComponent ( index );
    }

    /**
     * Adds new {@link AccordionPane} to this accordion.
     *
     * @param title   {@link AccordionPane} title
     * @param content {@link AccordionPane} content {@link Component}
     * @return created {@link AccordionPane}
     */
    @NotNull
    public AccordionPane addPane ( @Nullable final String title, @NotNull final Component content )
    {
        return addPane ( createAccordionPaneId (), getPaneCount (), null, title, content );
    }

    /**
     * Adds new {@link AccordionPane} to this accordion.
     *
     * @param index   {@link AccordionPane} index
     * @param title   {@link AccordionPane} title
     * @param content {@link AccordionPane} content {@link Component}
     * @return created {@link AccordionPane}
     */
    @NotNull
    public AccordionPane addPane ( final int index, @Nullable final String title, @NotNull final Component content )
    {
        return addPane ( createAccordionPaneId (), index, null, title, content );
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
    public AccordionPane addPane ( @Nullable final Icon icon, @Nullable final String title, @NotNull final Component content )
    {
        return addPane ( createAccordionPaneId (), getPaneCount (), icon, title, content );
    }

    /**
     * Adds new {@link AccordionPane} to this accordion.
     *
     * @param index   {@link AccordionPane} index
     * @param icon    {@link AccordionPane} title icon
     * @param title   {@link AccordionPane} title
     * @param content {@link AccordionPane} content {@link Component}
     * @return created {@link AccordionPane}
     */
    @NotNull
    public AccordionPane addPane ( final int index, @Nullable final Icon icon, @Nullable final String title,
                                   @NotNull final Component content )
    {
        return addPane ( createAccordionPaneId (), index, icon, title, content );
    }

    /**
     * Adds new {@link AccordionPane} to this accordion.
     *
     * @param id      {@link AccordionPane} identifier
     * @param title   {@link AccordionPane} title
     * @param content {@link AccordionPane} content {@link Component}
     * @return created {@link AccordionPane}
     */
    @NotNull
    public AccordionPane addPane ( @NotNull final String id, @Nullable final String title, @NotNull final Component content )
    {
        return addPane ( id, getPaneCount (), null, title, content );
    }

    /**
     * Adds new {@link AccordionPane} to this accordion.
     *
     * @param id      {@link AccordionPane} identifier
     * @param index   {@link AccordionPane} index
     * @param title   {@link AccordionPane} title
     * @param content {@link AccordionPane} content {@link Component}
     * @return created {@link AccordionPane}
     */
    @NotNull
    public AccordionPane addPane ( @NotNull final String id, final int index, @Nullable final String title,
                                   @NotNull final Component content )
    {
        return addPane ( id, index, null, title, content );
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
        return addPane ( id, getPaneCount (), icon, title, content );
    }

    /**
     * Adds new {@link AccordionPane} to this accordion.
     *
     * @param id      {@link AccordionPane} identifier
     * @param index   {@link AccordionPane} index
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
        return addPane ( index, accordionPane );
    }

    /**
     * Returns new unique {@link AccordionPane} identifier.
     *
     * @return new unique {@link AccordionPane} identifier
     */
    @NotNull
    protected String createAccordionPaneId ()
    {
        return TextUtils.generateId ( "AP" );
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
     * Adds specified {@link AccordionPane} to this accordion.
     *
     * @param pane {@link AccordionPane} to add
     * @return added {@link AccordionPane}
     */
    @NotNull
    public AccordionPane addPane ( @NotNull final AccordionPane pane )
    {
        return addPane ( getPaneCount (), pane );
    }

    /**
     * Adds specified {@link AccordionPane} to this accordion.
     *
     * @param pane  {@link AccordionPane} to add
     * @param index {@link AccordionPane} index
     * @return added {@link AccordionPane}
     */
    @NotNull
    public AccordionPane addPane ( final int index, @NotNull final AccordionPane pane )
    {
        add ( pane, index );
        SwingUtils.update ( this );
        return pane;
    }

    @Override
    protected void addImpl ( @NotNull final Component comp, @Nullable final Object constraints, final int index )
    {
        if ( comp instanceof AccordionPane )
        {
            final AccordionPane pane = ( AccordionPane ) comp;
            if ( findPane ( pane.getId () ) == null )
            {
                final List<AccordionPane> oldPanes = getPanes ();
                super.addImpl ( pane, constraints, index );
                firePropertyChange ( PANES_PROPERTY, oldPanes, getPanes () );
            }
            else
            {
                throw new RuntimeException ( "AccordionPane with identifier already exists: " + pane.getId () );
            }
        }
        else
        {
            throw new RuntimeException ( "Only AccordionPane instances can be added to accordion: " + comp );
        }
    }

    /**
     * Removes {@link AccordionPane} at the specified index from this {@link WebAccordion}.
     *
     * @param index {@link AccordionPane} index
     * @return removed {@link AccordionPane}
     */
    @NotNull
    public AccordionPane removePane ( final int index )
    {
        final AccordionPane removed = getPane ( index );
        remove ( index );
        SwingUtils.update ( this );
        return removed;
    }

    /**
     * Removes {@link AccordionPane} with the specified identifier from this {@link WebAccordion} if it exists.
     * Returns either removed {@link AccordionPane} or {@code null} if there was no {@link AccordionPane} with the specified identifier.
     *
     * @param id identifier of {@link AccordionPane} to remove
     * @return either removed {@link AccordionPane} or {@code null} if there was no {@link AccordionPane} with the specified identifier
     */
    @Nullable
    public AccordionPane removePane ( @NotNull final String id )
    {
        final AccordionPane pane = findPane ( id );
        return pane != null ? removePane ( pane ) : null;
    }

    /**
     * Removes specified {@link AccordionPane} from this {@link WebAccordion} if it exists.
     * Returns either specified {@link AccordionPane} or {@code null} if it is not a part of this {@link WebAccordion}.
     *
     * @param pane {@link AccordionPane} to remove
     * @return either specified {@link AccordionPane} or {@code null} if it is not a part of this {@link WebAccordion}
     */
    @Nullable
    public AccordionPane removePane ( @NotNull final AccordionPane pane )
    {
        final AccordionPane removed;
        final int zOrder = getComponentZOrder ( pane );
        if ( zOrder != -1 )
        {
            remove ( zOrder );
            SwingUtils.update ( this );
            removed = pane;
        }
        else
        {
            removed = null;
        }
        return removed;
    }

    @Override
    public void remove ( final int index )
    {
        final List<AccordionPane> oldPanes = getPanes ();
        super.remove ( index );
        firePropertyChange ( PANES_PROPERTY, oldPanes, getPanes () );
    }

    /**
     * Returns first expanded {@link AccordionPane}.
     *
     * @return first expanded {@link AccordionPane}
     */
    @Nullable
    public AccordionPane getFirstExpandedPane ()
    {
        return getModel () != null ? getModel ().getFirstExpandedPane () : null;
    }

    /**
     * Returns first expanded {@link AccordionPane} index or {@code -1} if there none are expanded.
     *
     * @return first expanded {@link AccordionPane} index or {@code -1} if there none are expanded
     */
    public int getFirstExpandedPaneIndex ()
    {
        return getModel () != null ? getPaneIndex ( getModel ().getFirstExpandedPane () ) : -1;
    }

    /**
     * Returns last expanded {@link AccordionPane}.
     *
     * @return last expanded {@link AccordionPane}
     */
    @Nullable
    public AccordionPane getLastExpandedPane ()
    {
        return getModel () != null ? getModel ().getLastExpandedPane () : null;
    }

    /**
     * Returns last expanded {@link AccordionPane} index or {@code -1} if there none are expanded.
     *
     * @return last expanded {@link AccordionPane} index or {@code -1} if there none are expanded
     */
    public int getLastExpandedPaneIndex ()
    {
        return getModel () != null ? getPaneIndex ( getModel ().getLastExpandedPane () ) : -1;
    }

    /**
     * Returns {@link List} of expanded {@link AccordionPane}s.
     *
     * @return {@link List} of expanded {@link AccordionPane}s
     */
    @NotNull
    public List<AccordionPane> getExpandedPanes ()
    {
        return getModel () != null ? getModel ().getExpandedPanes () : new ArrayList<AccordionPane> ();
    }

    /**
     * Returns {@link List} of identifiers of expanded {@link AccordionPane}s.
     *
     * @return {@link List} of identifiers of expanded {@link AccordionPane}s
     */
    @NotNull
    public List<String> getExpandedPaneIds ()
    {
        return getModel () != null ? getModel ().getExpandedPaneIds () : new ArrayList<String> ();
    }

    /**
     * Changes state of the {@link AccordionPane}s with identifiers from the specified {@link List} to expanded.
     *
     * @param ids {@link List} of identifiers of {@link AccordionPane}s to set expanded
     */
    public void setExpandedPaneIds ( @NotNull final List<String> ids )
    {
        if ( getModel () != null )
        {
            getModel ().setExpandedPaneIds ( ids );
        }
    }

    /**
     * Returns array of indices of expanded {@link AccordionPane}s.
     *
     * @return array of indices of expanded {@link AccordionPane}s
     */
    @NotNull
    public int[] getExpandedPaneIndices ()
    {
        return getModel () != null ? getModel ().getExpandedPaneIndices () : new int[ 0 ];
    }

    /**
     * Changes state of the {@link AccordionPane}s at the specified indices to expanded.
     *
     * @param indices indices of {@link AccordionPane}s to set expanded
     */
    public void setExpandedPaneIndices ( @NotNull final int[] indices )
    {
        if ( getModel () != null )
        {
            getModel ().setExpandedPaneIndices ( indices );
        }
    }

    /**
     * Returns whether or not {@link AccordionPane} with the specified identifier is expanded.
     *
     * @param id {@link AccordionPane} identifier
     * @return {@code true} if {@link AccordionPane} with the specified identifier is expanded, {@code false} otherwise
     */
    public boolean isPaneExpanded ( @NotNull final String id )
    {
        return getModel () != null && getModel ().isPaneExpanded ( id );
    }

    /**
     * Changes state of {@link AccordionPane} with the specified identifier to either expanded or collapsed.
     *
     * @param id       {@link AccordionPane} identifier
     * @param expanded whether or not {@link AccordionPane} with the specified identifier needs to be expanded or collapsed
     * @return {@code true} if state of {@link AccordionPane} with the specified identifier was changed
     */
    public boolean setPaneExpanded ( @NotNull final String id, final boolean expanded )
    {
        return expanded ? expandPane ( id ) : collapsePane ( id );
    }

    /**
     * Asks {@link WebAccordion} to expand {@link AccordionPane} with the specified identifier.
     *
     * @param id {@link AccordionPane} identifier
     * @return {@code true} if {@link AccordionPane} was successfully expanded, {@code false} otherwise
     */
    public boolean expandPane ( @NotNull final String id )
    {
        return getModel () != null && getModel ().expandPane ( id );
    }

    /**
     * Asks {@link WebAccordion} to expand {@link AccordionPane}s with the specified identifiers.
     * Note that this will not collapse all other expanded {@link AccordionPane}s unless limit is reached.
     *
     * @param ids {@link AccordionPane} identifiers
     */
    public void expandPanes ( @NotNull final String... ids )
    {
        if ( getModel () != null )
        {
            for ( final String id : ids )
            {
                getModel ().expandPane ( id );
            }
        }
    }

    /**
     * Asks {@link WebAccordion} to expand {@link AccordionPane}s with the specified identifiers.
     * Note that this will not collapse all other expanded {@link AccordionPane}s unless limit is reached.
     *
     * @param ids {@link List} of {@link AccordionPane} identifiers
     */
    public void expandPanes ( @NotNull final List<String> ids )
    {
        if ( getModel () != null )
        {
            for ( final String id : ids )
            {
                getModel ().expandPane ( id );
            }
        }
    }

    /**
     * Asks {@link WebAccordion} to expand {@link AccordionPane}s at the specified indices.
     *
     * @param indices {@link List} of {@link AccordionPane} indices
     */
    public void expandPanes ( @NotNull final int[] indices )
    {
        if ( getModel () != null )
        {
            for ( final int index : indices )
            {
                final AccordionPane pane = getPane ( index );
                getModel ().expandPane ( pane.getId () );
            }
        }
    }

    /**
     * Returns {@link List} of collapsed {@link AccordionPane}s.
     *
     * @return {@link List} of collapsed {@link AccordionPane}s
     */
    @NotNull
    public List<AccordionPane> getCollapsedPanes ()
    {
        return getModel () != null ? getModel ().getCollapsedPanes () : new ArrayList<AccordionPane> ();
    }

    /**
     * Returns {@link List} of identifiers of collapsed {@link AccordionPane}s.
     *
     * @return {@link List} of identifiers of collapsed {@link AccordionPane}s
     */
    @NotNull
    public List<String> getCollapsedPaneIds ()
    {
        return getModel () != null ? getModel ().getCollapsedPaneIds () : new ArrayList<String> ();
    }

    /**
     * Returns array of indices of collapsed {@link AccordionPane}s.
     *
     * @return array of indices of collapsed {@link AccordionPane}s
     */
    @NotNull
    public int[] getCollapsedPaneIndices ()
    {
        return getModel () != null ? getModel ().getCollapsedPaneIndices () : new int[ 0 ];
    }

    /**
     * Returns whether or not {@link AccordionPane} with the specified identifier is collapsed.
     *
     * @param id {@link AccordionPane} identifier
     * @return {@code true} if {@link AccordionPane} with the specified identifier is collapsed, {@code false} otherwise
     */
    public boolean isPaneCollapsed ( @NotNull final String id )
    {
        return getModel () != null && getModel ().isPaneCollapsed ( id );
    }

    /**
     * Changes state of {@link AccordionPane} with the specified identifier to either collapsed or expanded.
     *
     * @param id        {@link AccordionPane} identifier
     * @param collapsed whether or not {@link AccordionPane} with the specified identifier needs to be collapsed or expanded
     * @return {@code true} if state of {@link AccordionPane} with the specified identifier was changed
     */
    public boolean setPaneCollapsed ( @NotNull final String id, final boolean collapsed )
    {
        return collapsed ? collapsePane ( id ) : expandPane ( id );
    }

    /**
     * Asks {@link WebAccordion} to collapse {@link AccordionPane} with the specified identifier.
     *
     * @param id {@link AccordionPane} identifier
     * @return {@code true} if {@link AccordionPane} was successfully collapsed, {@code false} otherwise
     */
    public boolean collapsePane ( @NotNull final String id )
    {
        return getModel () != null && getModel ().collapsePane ( id );
    }

    /**
     * Asks {@link WebAccordion} to collapse {@link AccordionPane}s with the specified identifiers.
     *
     * @param ids {@link AccordionPane} identifiers
     */
    public void collapsePanes ( @NotNull final String... ids )
    {
        if ( getModel () != null )
        {
            for ( final String id : ids )
            {
                getModel ().collapsePane ( id );
            }
        }
    }

    /**
     * Asks {@link WebAccordion} to collapse {@link AccordionPane}s with the specified identifiers.
     *
     * @param ids {@link List} of {@link AccordionPane} identifiers
     */
    public void collapsePanes ( @NotNull final List<String> ids )
    {
        if ( getModel () != null )
        {
            for ( final String id : ids )
            {
                getModel ().collapsePane ( id );
            }
        }
    }

    /**
     * Asks {@link WebAccordion} to collapse {@link AccordionPane}s at the specified indices.
     *
     * @param indices {@link List} of {@link AccordionPane} indices
     */
    public void collapsePanes ( @NotNull final int[] indices )
    {
        if ( getModel () != null )
        {
            for ( final int index : indices )
            {
                final AccordionPane pane = getPane ( index );
                getModel ().collapsePane ( pane.getId () );
            }
        }
    }

    /**
     * Returns whether or not {@link AccordionPane} with the specified identifier is in transition to either of two expansion states.
     *
     * @param id {@link AccordionPane} identifier
     * @return {@code true} if {@link AccordionPane} with the specified identifier is in transition, {@code false} otherwise
     */
    public boolean isPaneInTransition ( @NotNull final String id )
    {
        return getLayout () != null && getLayout ().isPaneInTransition ( id );
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