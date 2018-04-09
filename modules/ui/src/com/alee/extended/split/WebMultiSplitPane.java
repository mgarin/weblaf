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

package com.alee.extended.split;

import com.alee.api.data.Orientation;
import com.alee.extended.WebContainer;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;

import java.awt.*;
import java.util.List;

/**
 * Split pane component that unlike {@link javax.swing.JSplitPane} can contain more than two views.
 * It is also more successful at adjusting sizes for existing views and handling weights and resize operations.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebMultiSplitPane">How to use WebMultiSplitPane</a>
 * @see MultiSplitPaneModel
 * @see MultiSplitPaneDescriptor
 * @see WMultiSplitPaneUI
 * @see WebMultiSplitPaneUI
 * @see IMultiSplitPanePainter
 * @see MultiSplitPanePainter
 * @see WebContainer
 */
public class WebMultiSplitPane extends WebContainer<WebMultiSplitPane, WMultiSplitPaneUI>
{
    /**
     * Component properties.
     */
    public static final String ORIENTATION_PROPERTY = "orientation";
    public static final String CONTINUOUS_LAYOUT_PROPERTY = "continuousLayout";
    public static final String DIVIDER_SIZE_PROPERTY = "dividerSize";
    public final static String ONE_TOUCH_EXPANDABLE_PROPERTY = "oneTouchExpandable";
    public static final String MODEL_PROPERTY = "model";

    /**
     * Split {@link Orientation}.
     * For {@link Orientation#horizontal} all components are placed in one horizontal row.
     * For {@link Orientation#vertical} all components are placed in one vertical column.
     */
    protected Orientation orientation;

    /**
     * Whether or not split pane layout should be continuously updated upon dividers drag.
     * Changing it to {@code false} might improve overall performance and visual feedback in complex UIs.
     */
    protected boolean continuousLayout;

    /**
     * Divider size in pixels.
     * This size forces {@link IMultiSplitPaneDividerPainter} implementation to paint within set bounds.
     */
    protected int dividerSize;

    /**
     * Whether or not split pane should display one-touch-expand buttons on dividers.
     */
    protected boolean oneTouchExpandable;

    /**
     * {@link MultiSplitPaneModel} implementation.
     */
    protected MultiSplitPaneModel model;

    /**
     * Constructs new {@link WebMultiSplitPane}.
     */
    public WebMultiSplitPane ()
    {
        this ( StyleId.auto, Orientation.horizontal );
    }

    /**
     * Constructs new {@link WebMultiSplitPane}.
     *
     * @param orientation split {@link Orientation}
     */
    public WebMultiSplitPane ( final Orientation orientation )
    {
        this ( StyleId.auto, orientation );
    }

    /**
     * Constructs new {@link WebMultiSplitPane}.
     *
     * @param id style ID
     */
    public WebMultiSplitPane ( final StyleId id )
    {
        this ( id, Orientation.horizontal );
    }

    /**
     * Constructs new {@link WebMultiSplitPane}.
     *
     * @param id          style ID
     * @param orientation split {@link Orientation}
     */
    public WebMultiSplitPane ( final StyleId id, final Orientation orientation )
    {
        super ();
        setOrientation ( orientation );
        setContinuousLayout ( true );
        setDividerSize ( 11 );
        setOneTouchExpandable ( false );
        setModel ( createModel () );
        updateUI ();
        setStyleId ( id );
    }

    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.multisplitpane;
    }

    /**
     * Returns split {@link Orientation}.
     *
     * @return split {@link Orientation}
     */
    public Orientation getOrientation ()
    {
        return orientation;
    }

    /**
     * Sets split {@link Orientation}.
     *
     * @param orientation new split {@link Orientation}
     * @return this {@link WebMultiSplitPane}
     */
    public WebMultiSplitPane setOrientation ( final Orientation orientation )
    {
        if ( this.orientation != orientation )
        {
            final Orientation old = this.orientation;
            this.orientation = orientation;
            firePropertyChange ( ORIENTATION_PROPERTY, old, orientation );
        }
        return this;
    }

    /**
     * Returns whether or not split pane layout should be continuously updated on resize operations.
     *
     * @return {@code true} if split pane layout should be continuously updated on resize operations, {@code false} otherwise
     */
    public boolean isContinuousLayout ()
    {
        return continuousLayout;
    }

    /**
     * Sets whether or not split pane layout should be continuously updated on resize operations.
     *
     * @param continuousLayout whether or not split pane layout should be continuously updated on resize operations
     * @return this {@link WebMultiSplitPane}
     */
    public WebMultiSplitPane setContinuousLayout ( final boolean continuousLayout )
    {
        if ( this.continuousLayout != continuousLayout )
        {
            final boolean old = this.continuousLayout;
            this.continuousLayout = continuousLayout;
            firePropertyChange ( CONTINUOUS_LAYOUT_PROPERTY, old, continuousLayout );
        }
        return this;
    }

    /**
     * Returns divider size in pixels.
     *
     * @return divider size in pixels
     */
    public int getDividerSize ()
    {
        return dividerSize;
    }

    /**
     * Sets divider size in pixels.
     *
     * @param size divider size in pixels
     * @return this {@link WebMultiSplitPane}
     */
    public WebMultiSplitPane setDividerSize ( final int size )
    {
        if ( this.dividerSize != size )
        {
            final int old = this.dividerSize;
            this.dividerSize = size;
            firePropertyChange ( DIVIDER_SIZE_PROPERTY, old, size );
        }
        return this;
    }

    /**
     * Returns whether or not split pane should display one-touch-expand buttons on dividers.
     *
     * @return whether or not split pane should display one-touch-expand buttons on dividers
     */
    public boolean isOneTouchExpandable ()
    {
        return oneTouchExpandable;
    }

    /**
     * Sets whether or not split pane should display one-touch-expand buttons on dividers.
     *
     * @param oneTouchExpandable whether or not split pane should display one-touch-expand buttons on dividers
     * @return this {@link WebMultiSplitPane}
     */
    public WebMultiSplitPane setOneTouchExpandable ( final boolean oneTouchExpandable )
    {
        if ( this.oneTouchExpandable != oneTouchExpandable )
        {
            final boolean old = this.oneTouchExpandable;
            this.oneTouchExpandable = oneTouchExpandable;
            firePropertyChange ( ONE_TOUCH_EXPANDABLE_PROPERTY, old, oneTouchExpandable );
        }
        return this;
    }

    /**
     * Returns {@link MultiSplitPaneModel} implementation.
     *
     * @return {@link MultiSplitPaneModel} implementation
     */
    public MultiSplitPaneModel getModel ()
    {
        return model;
    }

    /**
     * Returns default {@link MultiSplitPaneModel} implementation to be used.
     *
     * @return default {@link MultiSplitPaneModel} implementation to be used
     */
    protected MultiSplitPaneModel createModel ()
    {
        return new WebMultiSplitPaneModel ();
    }

    /**
     * Sets {@link MultiSplitPaneModel} implementation.
     *
     * @param model new {@link MultiSplitPaneModel} implementation
     * @return this {@link WebMultiSplitPane}
     */
    public WebMultiSplitPane setModel ( final MultiSplitPaneModel model )
    {
        if ( this.model != model )
        {
            final MultiSplitPaneModel old = this.model;

            // Copying old model data over to new one
            final List<MultiSplitView> views = old != null ? old.getViews () : null;
            final List<WebMultiSplitPaneDivider> dividers = old != null ? old.getDividers () : null;

            // Uninstalling previous model
            if ( this.model != null )
            {
                this.model.uninstall ( this );
            }

            // Updating layout manager
            this.model = model;
            setLayout ( model );

            // Installing new model
            if ( model != null )
            {
                model.install ( this, views, dividers );
            }

            firePropertyChange ( MODEL_PROPERTY, old, model );
        }
        return this;
    }

    /**
     * Returns amount of view {@link Component}s.
     *
     * @return amount of view {@link Component}s
     */
    public int getViewCount ()
    {
        return getModel ().getViewCount ();
    }

    /**
     * Returns copy of the {@link List} of all {@link MultiSplitView}s contained in {@link MultiSplitPaneModel}.
     *
     * @return copy of the {@link List} of all {@link MultiSplitView}s contained in {@link MultiSplitPaneModel}
     */
    public List<MultiSplitView> getViews ()
    {
        return getModel ().getViews ();
    }

    /**
     * Returns index of the {@link MultiSplitView} that contains specified {@link Component}.
     *
     * @param component {@link Component} contained in {@link MultiSplitView}
     * @return index of the {@link MultiSplitView} that contains specified {@link Component}
     */
    public int getViewIndex ( final Component component )
    {
        return getModel ().getViewIndex ( component );
    }

    /**
     * Returns {@link Component} from {@link MultiSplitView} at the specified index.
     *
     * @param index {@link MultiSplitView} index
     * @return {@link Component} from {@link MultiSplitView} at the specified index
     */
    public Component getViewComponent ( final int index )
    {
        return getModel ().getViewComponent ( index );
    }

    /**
     * Returns {@link List} of view {@link Component}s.
     *
     * @return {@link List} of view {@link Component}s
     */
    public List<Component> getViewComponents ()
    {
        return getModel ().getViewComponents ();
    }

    /**
     * Returns copy of the {@link List} of all {@link WebMultiSplitPaneDivider}s used by {@link WebMultiSplitPane}.
     *
     * @return copy of the {@link List} of all {@link WebMultiSplitPaneDivider}s used by {@link WebMultiSplitPane}
     */
    public List<WebMultiSplitPaneDivider> getDividers ()
    {
        return getModel ().getDividers ();
    }

    /**
     * Returns default {@link WebMultiSplitPaneDivider} to be used.
     *
     * @return default {@link WebMultiSplitPaneDivider} to be used
     */
    protected WebMultiSplitPaneDivider createDivider ()
    {
        final StyleId styleId = StyleId.multisplitpaneContinuousDivider.at ( this );
        return new WebMultiSplitPaneDivider ( styleId, this );
    }

    /**
     * Returns {@link MultiSplitState} describing current {@link WebMultiSplitPane} state.
     *
     * @return {@link MultiSplitState} describing current {@link WebMultiSplitPane} state
     */
    public MultiSplitState getMultiSplitState ()
    {
        return getModel ().getMultiSplitState ();
    }

    /**
     * Updates {@link WebMultiSplitPane} using specified {@link MultiSplitState}.
     *
     * @param state{@link MultiSplitState} to update {@link WebMultiSplitPane} with
     */
    public void setMultiSplitState ( final MultiSplitState state )
    {
        getModel ().setMultiSplitState ( state );
    }

    /**
     * Resets {@link MultiSplitView} sizes to ones specified by {@link MultiSplitConstraints}.
     * Such reset always takes place at layout initialization to assign initial sizes but normally isn't required later on.
     * It can be used to reset all {@link MultiSplitView} sizes to initial ones if they were added after layout initialization.
     */
    public void resetViewSizes ()
    {
        getModel ().resetViewStates ();
    }

    /**
     * Returns whether or not any view is currently expanded in {@link WebMultiSplitPane}.
     *
     * @return {@code true} if any view is currently expanded in {@link WebMultiSplitPane}, {@code false} otherwise
     */
    public boolean isAnyViewExpanded ()
    {
        return getModel ().isAnyViewExpanded ();
    }

    /**
     * Returns index of currently expanded view in {@link WebMultiSplitPane}.
     * Might return {@code -1} if there are currently no expanded view.
     *
     * @return index of currently expanded view in {@link WebMultiSplitPane}
     */
    public int getExpandedViewIndex ()
    {
        return getModel ().getExpandedViewIndex ();
    }

    /**
     * Expands view positioned in {@link WebMultiSplitPane} at the specified index.
     *
     * @param index view index
     */
    public void expandView ( final int index )
    {
        getModel ().expandView ( index );
    }

    /**
     * Collapses expanded view if any view is currently expanded in {@link WebMultiSplitPane}.
     */
    public void collapseExpandedView ()
    {
        getModel ().collapseExpandedView ();
    }

    /**
     * Toggles expansion state for the view positioned in {@link WebMultiSplitPane} at the specified index.
     *
     * @param index view index
     */
    public void toggleViewExpansion ( final int index )
    {
        getModel ().toggleViewExpansion ( index );
    }

    @Override
    public Component add ( final Component component )
    {
        if ( component instanceof WebMultiSplitPaneDivider )
        {
            throw new IllegalArgumentException ( "Dividers cannot be added explicitely" );
        }
        addImpl ( component, null, getViewCount () );
        return component;
    }

    @Override
    public Component add ( final String constraints, final Component component )
    {
        if ( component instanceof WebMultiSplitPaneDivider )
        {
            throw new IllegalArgumentException ( "Dividers cannot be added explicitely" );
        }
        addImpl ( component, constraints, getViewCount () );
        return component;
    }

    @Override
    public Component add ( final Component component, final int index )
    {
        if ( component instanceof WebMultiSplitPaneDivider )
        {
            throw new IllegalArgumentException ( "Dividers cannot be added explicitely" );
        }
        if ( index > getViewCount () )
        {
            throw new IndexOutOfBoundsException ( "Illegal view component index" );
        }
        addImpl ( component, null, index );
        return component;
    }

    @Override
    public void add ( final Component component, final Object constraints )
    {
        if ( component instanceof WebMultiSplitPaneDivider )
        {
            throw new IllegalArgumentException ( "Dividers cannot be added explicitely" );
        }
        addImpl ( component, constraints, getViewCount () );
    }

    @Override
    public void add ( final Component component, final Object constraints, final int index )
    {
        if ( component instanceof WebMultiSplitPaneDivider )
        {
            throw new IllegalArgumentException ( "Dividers cannot be added explicitely" );
        }
        if ( index > getViewCount () )
        {
            throw new IndexOutOfBoundsException ( "Illegal view component index" );
        }
        addImpl ( component, constraints, index );
    }

    /**
     * Overriden for accessibility.
     */
    @Override
    protected void addImpl ( final Component component, final Object constraints, final int index )
    {
        super.addImpl ( component, constraints, index );
    }

    @Override
    public void setComponentZOrder ( final Component component, final int index )
    {
        if ( component instanceof WebMultiSplitPaneDivider )
        {
            throw new IllegalArgumentException ( "Dividers order cannot be modified explicitely" );
        }
        if ( index > getViewCount () )
        {
            throw new IndexOutOfBoundsException ( "Illegal view component index" );
        }
        setComponentZOrderImpl ( component, index );
        getModel ().moveComponent ( component, index );
    }

    /**
     * Special method for internal usage within {@link MultiSplitPaneModel}.
     * Unlike {@link #setComponentZOrder(Component, int)} method it allows modifying {@link WebMultiSplitPaneDivider} z-order.
     *
     * @param component {@link Component} to change z-order for
     * @param index     z-order index
     */
    protected void setComponentZOrderImpl ( final Component component, final int index )
    {
        super.setComponentZOrder ( component, index );
    }

    @Override
    public void remove ( final int index )
    {
        if ( index < 0 || getViewCount () <= index )
        {
            throw new IndexOutOfBoundsException ( "There is no view component at specified index" );
        }
        if ( getComponent ( index ) instanceof WebMultiSplitPaneDivider )
        {
            throw new IllegalArgumentException ( "Dividers cannot be added explicitely" );
        }
        removeImpl ( index );
    }

    /**
     * Special method for internal usage within {@link MultiSplitPaneModel}.
     * Unlike {@link #remove(int)} method it allows removing {@link WebMultiSplitPaneDivider}s.
     *
     * @param index index of child {@link Component} to remove
     */
    protected void removeImpl ( final int index )
    {
        super.remove ( index );
    }

    @Override
    public void removeAll ()
    {
        // Removing view components one by one from the end
        // This will ensure that everything goes smooth and clean
        for ( int index = getViewCount () - 1; index >= 0; index-- )
        {
            remove ( index );
        }
    }

    /**
     * Adds specified {@link MultiSplitExpansionListener} to this {@link WebMultiSplitPane}.
     *
     * @param listener {@link MultiSplitExpansionListener} to add
     */
    public void addResizeListener ( final MultiSplitResizeListener listener )
    {
        listenerList.add ( MultiSplitResizeListener.class, listener );
    }

    /**
     * Removes specified {@link MultiSplitExpansionListener} from this {@link WebMultiSplitPane}.
     *
     * @param listener {@link MultiSplitExpansionListener} to remove
     */
    public void removeResizeListener ( final MultiSplitResizeListener listener )
    {
        listenerList.remove ( MultiSplitResizeListener.class, listener );
    }

    /**
     * Informs about view resize being started due to the specified {@link WebMultiSplitPaneDivider} being pressed.
     *
     * @param divider {@link WebMultiSplitPaneDivider} that is being pressed
     */
    public void fireViewResizeStarted ( final WebMultiSplitPaneDivider divider )
    {
        for ( final MultiSplitResizeListener listener : listenerList.getListeners ( MultiSplitResizeListener.class ) )
        {
            listener.viewResizeStarted ( this, divider );
        }
    }

    /**
     * Informs about occurred view resize due to the specified {@link WebMultiSplitPaneDivider} being dragged.
     *
     * @param divider {@link WebMultiSplitPaneDivider} that is being dragged
     */
    public void fireViewResized ( final WebMultiSplitPaneDivider divider )
    {
        for ( final MultiSplitResizeListener listener : listenerList.getListeners ( MultiSplitResizeListener.class ) )
        {
            listener.viewResized ( this, divider );
        }
    }

    /**
     * Informs about view resize being finished due to the specified {@link WebMultiSplitPaneDivider} being released.
     *
     * @param divider {@link WebMultiSplitPaneDivider} that is being released
     */
    public void fireViewResizeEnded ( final WebMultiSplitPaneDivider divider )
    {
        for ( final MultiSplitResizeListener listener : listenerList.getListeners ( MultiSplitResizeListener.class ) )
        {
            listener.viewResizeEnded ( this, divider );
        }
    }

    /**
     * Informs about view size adjustments occurred due to {@link WebMultiSplitPane} size or settings changes.
     */
    public void fireViewSizeAdjusted ()
    {
        for ( final MultiSplitResizeListener listener : listenerList.getListeners ( MultiSplitResizeListener.class ) )
        {
            listener.viewSizeAdjusted ( this );
        }
    }

    /**
     * Adds specified {@link MultiSplitExpansionListener} to this {@link WebMultiSplitPane}.
     *
     * @param listener {@link MultiSplitExpansionListener} to add
     */
    public void addExpansionListener ( final MultiSplitExpansionListener listener )
    {
        listenerList.add ( MultiSplitExpansionListener.class, listener );
    }

    /**
     * Removes specified {@link MultiSplitExpansionListener} from this {@link WebMultiSplitPane}.
     *
     * @param listener {@link MultiSplitExpansionListener} to remove
     */
    public void removeExpansionListener ( final MultiSplitExpansionListener listener )
    {
        listenerList.remove ( MultiSplitExpansionListener.class, listener );
    }

    /**
     * Informs all listeners about {@link Component} becoming an expanded view within this {@link WebMultiSplitPane}.
     *
     * @param view expanded view {@link Component}
     */
    public void fireViewExpanded ( final Component view )
    {
        for ( final MultiSplitExpansionListener listener : listenerList.getListeners ( MultiSplitExpansionListener.class ) )
        {
            listener.viewExpanded ( this, view );
        }
    }

    /**
     * Informs all listeners about previously expanded {@link Component} view becoming collapsed within this {@link WebMultiSplitPane}.
     *
     * @param view collapsed view {@link Component}
     */
    public void fireViewCollapsed ( final Component view )
    {
        for ( final MultiSplitExpansionListener listener : listenerList.getListeners ( MultiSplitExpansionListener.class ) )
        {
            listener.viewCollapsed ( this, view );
        }
    }

    /**
     * Returns the look and feel (LaF) object that renders this component.
     *
     * @return the {@link WMultiSplitPaneUI} object that renders this component
     */
    public WMultiSplitPaneUI getUI ()
    {
        return ( WMultiSplitPaneUI ) ui;
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WMultiSplitPaneUI}
     */
    public void setUI ( final WMultiSplitPaneUI ui )
    {
        super.setUI ( ui );
    }

    @Override
    public void updateUI ()
    {
        StyleManager.getDescriptor ( this ).updateUI ( this );
    }

    @Override
    public String getUIClassID ()
    {
        return StyleManager.getDescriptor ( this ).getUIClassId ();
    }
}