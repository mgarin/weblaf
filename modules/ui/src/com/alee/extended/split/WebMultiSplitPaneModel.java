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

import com.alee.api.clone.Clone;
import com.alee.extended.layout.AbstractLayoutManager;
import com.alee.utils.CollectionUtils;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.swing.SizeType;

import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Default {@link MultiSplitPaneModel} implementation.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebMultiSplitPane">How to use WebMultiSplitPane</a>
 * @see WebMultiSplitPane
 * @see MultiSplitPaneModel
 */
public class WebMultiSplitPaneModel extends AbstractLayoutManager implements MultiSplitPaneModel, PropertyChangeListener
{
    /**
     * {@link MultiSplitView}s created based on components added to {@link WebMultiSplitPane}.
     */
    protected final List<MultiSplitView> views;

    /**
     * {@link WebMultiSplitPaneDivider}s created to separate components added to {@link WebMultiSplitPane}.
     */
    protected final List<WebMultiSplitPaneDivider> dividers;

    /**
     * {@link WebMultiSplitPane} listeners.
     */
    protected EventListenerList listeners;

    /**
     * {@link WebMultiSplitPane} this model is attached to.
     */
    protected WebMultiSplitPane multiSplitPane;

    /**
     * Whether or not {@link MultiSplitViewState}s have been initialized.
     */
    protected transient boolean initialized;

    /**
     * Type of the last {@link Operation} that have been performed within the model.
     */
    protected transient Operation lastOperation;

    /**
     * Index of currently dragged {@link WebMultiSplitPaneDivider}.
     */
    protected transient int draggedDividerIndex;

    /**
     * {@link WebMultiSplitPaneDivider} drag start mouse location.
     */
    protected transient Point dragStart;

    /**
     * Copy of {@link MultiSplitView}s used upon resize and drag operations.
     */
    protected transient List<MultiSplitView> viewsCopy;

    /**
     * Constructs new {@link WebMultiSplitPaneModel}.
     */
    public WebMultiSplitPaneModel ()
    {
        super ();
        this.views = new ArrayList<MultiSplitView> ();
        this.dividers = new ArrayList<WebMultiSplitPaneDivider> ( 2 );
        this.listeners = new EventListenerList ();
        this.listeners.add ( ComponentListener.class, new ComponentAdapter ()
        {
            @Override
            public void componentResized ( final ComponentEvent e )
            {
                // Informing about operation
                onOperation ( Operation.splitPaneResized );
            }
        } );
        draggedDividerIndex = -1;
        dragStart = null;
    }

    @Override
    public void install ( final WebMultiSplitPane multiSplitPane, final List<MultiSplitView> views,
                          final List<WebMultiSplitPaneDivider> dividers )
    {
        if ( this.multiSplitPane == null )
        {
            this.initialized = false;
            this.multiSplitPane = multiSplitPane;
            this.multiSplitPane.addPropertyChangeListener ( this );
            for ( final ComponentListener listener : listeners.getListeners ( ComponentListener.class ) )
            {
                this.multiSplitPane.addComponentListener ( listener );
            }
            if ( CollectionUtils.notEmpty ( views ) )
            {
                this.views.addAll ( views );
            }
            if ( CollectionUtils.notEmpty ( dividers ) )
            {
                this.dividers.addAll ( dividers );
            }
            onOperation ( Operation.splitPaneModelInstalled );
        }
        else if ( this.multiSplitPane == multiSplitPane )
        {
            throw new IllegalStateException ( "This MultiSplitPaneModel is already installed in specified WebMultiSplitPane" );
        }
        else if ( this.multiSplitPane != null )
        {
            throw new IllegalStateException ( "MultiSplitPaneModel can only be installed into single WebMultiSplitPane at a time" );
        }
        else
        {
            throw new IllegalStateException ( "MultiSplitPaneModel data was not properly cleared" );
        }
    }

    @Override
    public void uninstall ( final WebMultiSplitPane multiSplitPane )
    {
        if ( this.multiSplitPane != null && this.multiSplitPane == multiSplitPane )
        {
            // Informing about operation
            onOperation ( Operation.splitPaneModelUninstalled );

            // Clearing data
            this.views.clear ();
            this.dividers.clear ();
            for ( final ComponentListener listener : listeners.getListeners ( ComponentListener.class ) )
            {
                this.multiSplitPane.removeComponentListener ( listener );
            }
            this.multiSplitPane.removePropertyChangeListener ( this );
            this.multiSplitPane = null;
            this.initialized = false;
        }
        else if ( this.multiSplitPane == null )
        {
            throw new IllegalStateException ( "This MultiSplitPaneModel is not yet installed in any WebMultiSplitPane" );
        }
        else
        {
            throw new IllegalStateException ( "This MultiSplitPaneModel is installed in different WebMultiSplitPane" );
        }
    }

    @Override
    public void propertyChange ( final PropertyChangeEvent event )
    {
        if ( event.getPropertyName ().equals ( WebMultiSplitPane.ORIENTATION_PROPERTY ) )
        {
            onOperation ( Operation.splitOrientationChanged );
        }
    }

    @Override
    public void addComponent ( final Component component, final Object constraints )
    {
        if ( !( component instanceof WebMultiSplitPaneDivider ) )
        {
            // Parsing constraints
            final MultiSplitConstraints multiSplitConstraints;
            if ( constraints == null )
            {
                multiSplitConstraints = new MultiSplitConstraints ();
            }
            else if ( constraints instanceof Number )
            {
                multiSplitConstraints = new MultiSplitConstraints ( ( Number ) constraints );
            }
            else if ( constraints instanceof String )
            {
                multiSplitConstraints = new MultiSplitConstraints ( ( String ) constraints );
            }
            else if ( constraints instanceof MultiSplitConstraints )
            {
                multiSplitConstraints = ( MultiSplitConstraints ) constraints;
            }
            else
            {
                final String msg = "Unknown WebMultiSplitPane layout constraints type: %s";
                throw new IllegalArgumentException ( String.format ( msg, constraints ) );
            }

            // Calculating actual view index
            final int zOrder = multiSplitPane.getComponentZOrder ( component );
            final int viewIndex = zOrder <= views.size () ? zOrder : views.size ();

            // Adding view
            final MultiSplitView view = new MultiSplitView ( component, multiSplitConstraints );
            views.add ( viewIndex, view );

            // Initializing additional view size
            if ( initialized )
            {
                if ( multiSplitConstraints.isPixels () )
                {
                    // Saving pixel size as it is
                    view.state ().setSize ( multiSplitConstraints.pixels () );
                }
                else if ( multiSplitConstraints.isPercents () || multiSplitConstraints.isFill () || multiSplitConstraints.isPreferred () )
                {
                    // Saving percents, fill and preferred as preferred size
                    // We do not have any good way to determine how percents or fill should work at this point
                    final Dimension ps = component.getPreferredSize ();
                    final int preferred = multiSplitPane.getOrientation ().isHorizontal () ? ps.width : ps.height;
                    view.state ().setSize ( preferred );
                }
                else if ( multiSplitConstraints.isMinimum () )
                {
                    // Saving minimum size
                    final Dimension ms = component.getMinimumSize ();
                    final int minimum = multiSplitPane.getOrientation ().isHorizontal () ? ms.width : ms.height;
                    view.state ().setSize ( minimum );
                }
                else
                {
                    throw new IllegalArgumentException ( "Unknown view size value: " + multiSplitConstraints.size () );
                }
            }

            // Adding extra divider if needed
            if ( views.size () - 1 > dividers.size () )
            {
                // Adjusting indices for existing dividers first
                if ( dividers.size () > 0 )
                {
                    int dividerIndex = views.size ();
                    for ( final WebMultiSplitPaneDivider divider : dividers )
                    {
                        multiSplitPane.setComponentZOrderImpl ( divider, dividerIndex );
                        dividerIndex++;
                    }
                }

                // Adding new divider
                final int dividerIndex = viewIndex == 0 ? 0 : viewIndex - 1;
                final WebMultiSplitPaneDivider divider = multiSplitPane.createDivider ();
                multiSplitPane.addImpl ( divider, null, views.size () + dividerIndex );
                dividers.add ( dividerIndex, divider );
            }

            // Informing about operation
            onOperation ( Operation.splitViewsChanged );
        }
    }

    @Override
    public void moveComponent ( final Component component, final int index )
    {
        if ( !( component instanceof WebMultiSplitPaneDivider ) )
        {
            // Checking old view index
            final int oldIndex = getViewIndex ( component );
            if ( oldIndex == -1 )
            {
                throw new IllegalArgumentException ( "Cannot find view for component: " + component );
            }

            // Checking new index
            if ( oldIndex < 0 || views.size () <= oldIndex )
            {
                throw new IndexOutOfBoundsException ( "Incorrect new index: " + oldIndex );
            }

            // Checking that index actually changed
            if ( oldIndex != index )
            {
                // Moving view
                final MultiSplitView view = views.remove ( oldIndex );
                views.add ( index, view );

                // Informing about operation
                onOperation ( Operation.splitViewsChanged );

                // Informing about occurred adjustments
                multiSplitPane.fireViewSizeAdjusted ();
            }
        }
    }

    @Override
    public void removeComponent ( final Component component )
    {
        if ( !( component instanceof WebMultiSplitPaneDivider ) )
        {
            // Checking view index
            final int index = getViewIndex ( component );
            if ( index == -1 )
            {
                throw new IllegalArgumentException ( "Cannot find view for component: " + component );
            }

            // Removing related divider
            if ( views.size () > 1 )
            {
                final WebMultiSplitPaneDivider divider = dividers.get ( index > 0 ? index - 1 : index );
                final int zIndex = multiSplitPane.getComponentZOrder ( divider );
                multiSplitPane.removeImpl ( zIndex );
                dividers.remove ( divider );
            }

            // Removing view
            views.remove ( index );

            // Informing about operation
            onOperation ( Operation.splitViewsChanged );
        }
    }

    @Override
    public int getViewCount ()
    {
        return views.size ();
    }

    @Override
    public List<MultiSplitView> getViews ()
    {
        return Clone.deep ().clone ( views );
    }

    @Override
    public int getViewIndex ( final Component component )
    {
        int index = -1;
        for ( int i = 0; i < views.size (); i++ )
        {
            final MultiSplitView view = views.get ( i );
            if ( view.component () == component )
            {
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    public Component getViewComponent ( final int index )
    {
        return views.get ( index ).component ();
    }

    @Override
    public List<Component> getViewComponents ()
    {
        final List<Component> viewComponents = new ArrayList<Component> ( views.size () );
        for ( final MultiSplitView view : views )
        {
            viewComponents.add ( view.component () );
        }
        return viewComponents;
    }

    @Override
    public List<WebMultiSplitPaneDivider> getDividers ()
    {
        return new ArrayList<WebMultiSplitPaneDivider> ( dividers );
    }

    @Override
    public int getDividerIndex ( final WebMultiSplitPaneDivider divider )
    {
        return dividers.indexOf ( divider );
    }

    @Override
    public MultiSplitState getMultiSplitState ()
    {
        final MultiSplitState state;
        if ( initialized )
        {
            // Creating new state object
            state = new MultiSplitState ();

            // Views sizes
            final List<MultiSplitViewState> states = new ArrayList<MultiSplitViewState> ( views.size () );
            for ( final MultiSplitView view : views )
            {
                states.add ( view.state () );
            }
            state.setStates ( states );
        }
        else
        {
            // There is no state yet
            state = null;
        }
        return state;
    }

    @Override
    public void setMultiSplitState ( final MultiSplitState state )
    {
        final List<MultiSplitViewState> states = state.states ();
        if ( states.size () == views.size () )
        {
            final int previouslyExpandedViewIndex = getExpandedViewIndex ();

            // Updating states
            for ( int i = 0; i < views.size (); i++ )
            {
                views.get ( i ).setState ( states.get ( i ) );
            }

            // Informing about expansion
            // We have to do this manually to ensure appropriate events are thrown
            final int expandedViewIndex = getExpandedViewIndex ();
            if ( expandedViewIndex != previouslyExpandedViewIndex )
            {
                // Firing collapse event
                if ( previouslyExpandedViewIndex != -1 )
                {
                    multiSplitPane.fireViewCollapsed ( views.get ( previouslyExpandedViewIndex ).component () );
                }

                // Firing expansion event
                if ( expandedViewIndex != -1 )
                {
                    multiSplitPane.fireViewExpanded ( views.get ( expandedViewIndex ).component () );
                }
            }

            // Marking initialized
            initialized = true;

            // Informing about operation
            onOperation ( Operation.splitViewsStateApplied );
        }
    }

    @Override
    public void layoutContainer ( final Container container )
    {
        if ( multiSplitPane.getWidth () > 0 && multiSplitPane.getHeight () > 0 && views.size () > 0 )
        {
            initializeViewsStates ();
            adjustViewsStates ();
            layoutMultiSplitPane ();
        }
    }

    /**
     * Initializes actual {@link MultiSplitView}s sizes if they are empty.
     */
    protected void initializeViewsStates ()
    {
        if ( !initialized )
        {
            // Updating view sizes from constraints
            setupViewStatesFromConstraints ();

            // Marking initialized
            initialized = true;

            // Informing about operation
            onOperation ( Operation.splitViewsSizesInitialized );
        }
    }

    @Override
    public void resetViewStates ()
    {
        // Updating view sizes from constraints
        setupViewStatesFromConstraints ();

        // Updating layout
        multiSplitPane.revalidate ();
        multiSplitPane.repaint ();
    }

    /**
     * Replaces all {@link MultiSplitView}s sizes with ones specified by {@link MultiSplitConstraints}.
     */
    protected void setupViewStatesFromConstraints ()
    {
        // Initializing variables
        final MultiSplitLayoutHelper.Static helper =
                new MultiSplitLayoutHelper.Static ( multiSplitPane, views, SizeType.current );

        // Updating view sizes
        for ( final MultiSplitView view : views )
        {
            final Component component = view.component ();
            final MultiSplitConstraints constraints = view.constraints ();
            final MultiSplitViewState viewState = view.state ();

            // Calculating and updating view size
            if ( constraints.isPixels () )
            {
                // Saving pixel size as it is
                viewState.setSize ( constraints.pixels () );
            }
            else if ( constraints.isPercents () )
            {
                // Normalizing percentage view size
                final double normalizedPercents = constraints.percents () / helper.totalPercentViews;
                final int size = ( int ) Math.round ( helper.spaceForPercentViews * normalizedPercents );
                viewState.setSize ( size );
            }
            else if ( constraints.isFill () )
            {
                // Normalizing fill view size
                final double normalizedPercents = helper.fillViewSize / helper.totalPercentViews;
                final int size = ( int ) Math.round ( helper.spaceForPercentViews * normalizedPercents );
                viewState.setSize ( size );
            }
            else if ( constraints.isPreferred () )
            {
                // Saving preferred view size
                final Dimension ps = helper.size ( component, SizeType.preferred );
                final int preferred = helper.horizontal ? ps.width : ps.height;
                viewState.setSize ( preferred );
            }
            else if ( constraints.isMinimum () )
            {
                // Saving minimum view size
                final Dimension ms = helper.size ( component, SizeType.minimum );
                final int minimum = helper.horizontal ? ms.width : ms.height;
                viewState.setSize ( minimum );
            }
            else
            {
                throw new IllegalArgumentException ( "Unknown view size value: " + constraints.size () );
            }
        }
    }

    /**
     * Adjusts actual {@link MultiSplitView}s sizes if they are inconsistent with current container size.
     * This is the most important method in the model as it keeps all {@link MultiSplitView}s sizes consistent with container size.
     */
    protected void adjustViewsStates ()
    {
        // Adjustments flag
        boolean adjusted = false;

        // Choosing initial views
        List<MultiSplitView> baseViews = lastOperation == Operation.splitPaneResized ? viewsCopy : views;

        // Initializing helper instance
        MultiSplitLayoutHelper.Runtime helper = new MultiSplitLayoutHelper.Runtime ( multiSplitPane, baseViews );

        // Adjusting sizes according to area space
        while ( helper.space != helper.totalSizes )
        {
            // Delta between previously available space and currently available space
            int remainingSpaceDelta = helper.space - helper.totalSizes;

            // Act differently for major and minor deltas
            if ( Math.abs ( remainingSpaceDelta ) > 1 )
            {
                // Act differently when any weights are available and not
                if ( remainingSpaceDelta > 0 ? helper.totalWeights > 0.0 : helper.nonZeroViewsWeights > 0.0 )
                {
                    // Using weights to distribute space delta
                    // For increasing - we are using all weights as we want to distribute extra space to all eligible views
                    // For decreasing - we are only using weights of views with non-zero size
                    double remainingDeltaWeight = remainingSpaceDelta > 0 ? helper.totalWeights : helper.nonZeroViewsWeights;

                    // Adjusting views sizes
                    for ( int i = 0; i < baseViews.size (); i++ )
                    {
                        final MultiSplitView view = baseViews.get ( i );
                        final MultiSplitViewState viewState = view.state ();
                        final int size = viewState.size ();
                        final double viewWeight = view.constraints ().weight ();

                        // Only adjusting view size if it has weight
                        // Only adjusting view size if delta is positive or view size is not zero
                        if ( viewWeight > 0.0 && ( remainingSpaceDelta > 0 || size > 0 ) )
                        {
                            // We have specific weight so we can distribute space to views
                            final int rawViewDelta = ( int ) ( remainingSpaceDelta * viewWeight / remainingDeltaWeight );
                            final int actualDelta;
                            if ( rawViewDelta >= 0 )
                            {
                                // Increasing view size
                                // We can always safely proceed with this
                                actualDelta = rawViewDelta;
                            }
                            else if ( size >= Math.abs ( rawViewDelta ) )
                            {
                                // Decreasing view size
                                // We can safely proceed if remaining size is larger than delta
                                actualDelta = rawViewDelta;
                            }
                            else
                            {
                                // Decreasing view size to zero
                                // We have to be aware of delta leftover in this case
                                actualDelta = -size;
                            }

                            // Adjusting actual view size
                            views.get ( i ).state ().setSize ( size + actualDelta );

                            // Updating deltas
                            remainingSpaceDelta -= actualDelta;
                            remainingDeltaWeight -= viewWeight;
                        }
                        else
                        {
                            views.get ( i ).state ().setSize ( size );
                        }
                    }
                }
                else
                {
                    // Using weights synthetic weight to distribute space delta
                    // This will basically attempt to split space delta equally between views
                    final double averageWeight = 1.0;
                    double remainingDeltaWeight = 1.0 * ( remainingSpaceDelta > 0 ? baseViews.size () : helper.nonZeroViews );

                    // Adjusting view sizes
                    for ( int i = 0; i < baseViews.size (); i++ )
                    {
                        final MultiSplitView view = baseViews.get ( i );
                        final MultiSplitViewState viewState = view.state ();
                        final int size = viewState.size ();
                        final double viewWeight = view.constraints ().weight ();

                        // Only adjusting view size if it doesn't have weight
                        // Only adjusting view size if delta is positive or view size is not zero
                        if ( viewWeight == 0.0 && ( remainingSpaceDelta > 0 || size > 0 ) )
                        {
                            // Trying to equally distribute delta space
                            final int rawViewDelta = ( int ) ( remainingSpaceDelta * averageWeight / remainingDeltaWeight );
                            final int actualDelta;
                            if ( rawViewDelta >= 0 )
                            {
                                // Increasing view size
                                // We can always safely proceed with this
                                actualDelta = rawViewDelta;
                            }
                            else if ( size >= Math.abs ( rawViewDelta ) )
                            {
                                // Decreasing view size
                                // We can safely proceed if remaining size is larger than delta
                                actualDelta = rawViewDelta;
                            }
                            else
                            {
                                // Decreasing view size to zero
                                // We have to be aware of delta leftover in this case
                                actualDelta = -size;
                            }

                            // Adjusting actual view size
                            views.get ( i ).state ().setSize ( size + actualDelta );

                            // Updating deltas
                            remainingSpaceDelta -= actualDelta;
                            remainingDeltaWeight -= averageWeight;
                        }
                        else
                        {
                            views.get ( i ).state ().setSize ( size );
                        }
                    }
                }
            }
            else
            {
                // Adjusting first non-zero view size
                // For increasing - we don't want to affect zero size views either as they would randomly get to 1 pixel size
                // For decreasing - we don't want to affect views which already have zero size
                // The only case when we affect zero size views is when all views have zero size
                for ( int i = 0; i < baseViews.size (); i++ )
                {
                    final MultiSplitViewState viewState = baseViews.get ( i ).state ();
                    final int size = viewState.size ();
                    if ( size > 0 || helper.nonZeroViews == 0 )
                    {
                        views.get ( i ).state ().setSize ( size + remainingSpaceDelta );
                        break;
                    }
                }
            }

            // Further iterations should be done on the actual views
            // Otherwise we will lose results of the current iteration
            baseViews = views;

            // Updating helper instance to retry calculations to ensure we have distributed all available space
            helper = new MultiSplitLayoutHelper.Runtime ( multiSplitPane, baseViews );

            // Marking adjusted
            adjusted = true;
        }

        // Informing about occurred adjustments
        if ( adjusted )
        {
            multiSplitPane.fireViewSizeAdjusted ();
        }
    }

    /**
     * Performs actual layouting of {@link MultiSplitView}s.
     */
    protected void layoutMultiSplitPane ()
    {
        // Basic settings
        final Insets insets = multiSplitPane.getInsets ();
        final boolean horizontal = multiSplitPane.getOrientation ().isHorizontal ();
        final int expandedIndex = getExpandedViewIndex ();

        // Layout heavily depends on whether or not any of views is expanded
        if ( expandedIndex == -1 )
        {
            // Divider sizes
            final int dividerSize = multiSplitPane.getDividerSize ();
            final int dividerWidth = horizontal ? dividerSize : multiSplitPane.getWidth () - insets.left - insets.right;
            final int dividerHeight = horizontal ? multiSplitPane.getHeight () - insets.top - insets.bottom : dividerSize;

            // Positioning all components
            int x = insets.left;
            int y = insets.top;
            final int width = horizontal ? 0 : multiSplitPane.getWidth () - insets.left - insets.right;
            final int height = horizontal ? multiSplitPane.getHeight () - insets.top - insets.bottom : 0;
            for ( final MultiSplitView view : views )
            {
                final int index = views.indexOf ( view );
                final boolean lastComponent = index == views.size () - 1;
                final Component component = view.component ();
                final MultiSplitViewState viewState = view.state ();
                final int size = viewState.size ();

                // Positioning view component
                if ( horizontal )
                {
                    component.setBounds ( x, y, size, height );
                }
                else
                {
                    component.setBounds ( x, y, width, size );
                }

                // Positioning divider and adjusting coordinates for next cycle run
                if ( !lastComponent )
                {
                    // Positioning divider
                    final WebMultiSplitPaneDivider divider = dividers.get ( index );
                    if ( horizontal )
                    {
                        divider.setBounds ( x + size, y, dividerWidth, dividerHeight );
                    }
                    else
                    {
                        divider.setBounds ( x, y + size, dividerWidth, dividerHeight );
                    }

                    // Adjusting coordinates for next cycle run
                    if ( horizontal )
                    {
                        x += size + dividerSize;
                    }
                    else
                    {
                        y += size + dividerSize;
                    }
                }
            }
        }
        else
        {
            // Divider components
            final WebMultiSplitPaneDivider dividerBefore = expandedIndex > 0 ? dividers.get ( expandedIndex - 1 ) : null;
            final MultiSplitView expandedView = views.get ( expandedIndex );
            final WebMultiSplitPaneDivider dividerAfter = expandedIndex < views.size () - 1 ? dividers.get ( expandedIndex ) : null;

            // Divider sizes
            final int dividerBeforeSize;
            final int dividerAfterSize;

            // Placing divider in front of the view
            if ( dividerBefore != null )
            {
                final Dimension ps = dividerBefore.getPreferredSize ();
                dividerBeforeSize = horizontal ? ps.width : ps.height;
                final int width = horizontal ? dividerBeforeSize : multiSplitPane.getWidth () - insets.left - insets.right;
                final int height = horizontal ? multiSplitPane.getHeight () - insets.top - insets.bottom : dividerBeforeSize;
                dividerBefore.setBounds ( insets.left, insets.top, width, height );
            }
            else
            {
                dividerBeforeSize = 0;
            }

            // Placing divider after the view
            if ( dividerAfter != null )
            {
                final Dimension ps = dividerAfter.getPreferredSize ();
                dividerAfterSize = horizontal ? ps.width : ps.height;
                final int width = horizontal ? dividerAfterSize : multiSplitPane.getWidth () - insets.left - insets.right;
                final int height = horizontal ? multiSplitPane.getHeight () - insets.top - insets.bottom : dividerAfterSize;
                final int x = horizontal ? multiSplitPane.getWidth () - insets.right - width : insets.left;
                final int y = horizontal ? insets.top : multiSplitPane.getHeight () - insets.bottom - height;
                dividerAfter.setBounds ( x, y, width, height );
            }
            else
            {
                dividerAfterSize = 0;
            }

            // Placing view component
            final int horBefore = horizontal && dividerBefore != null ? dividerBeforeSize : 0;
            final int horAfter = horizontal && dividerAfter != null ? dividerAfterSize : 0;
            final int verBefore = !horizontal && dividerBefore != null ? dividerBeforeSize : 0;
            final int verAfter = !horizontal && dividerAfter != null ? dividerAfterSize : 0;
            final int viewX = insets.left + horBefore;
            final int viewY = insets.top + verBefore;
            final int viewWidth = multiSplitPane.getWidth () - insets.left - insets.right - horBefore - horAfter;
            final int viewHeight = multiSplitPane.getHeight () - insets.top - insets.bottom - verBefore - verAfter;
            expandedView.component ().setBounds ( viewX, viewY, viewWidth, viewHeight );
        }
    }

    @Override
    public Dimension preferredLayoutSize ( final Container container )
    {
        return layoutSize ( SizeType.preferred );
    }

    @Override
    public Dimension minimumLayoutSize ( final Container container )
    {
        return layoutSize ( SizeType.minimum );
    }

    @Override
    public Dimension maximumLayoutSize ( final Container container )
    {
        return layoutSize ( SizeType.maximum );
    }

    /**
     * Returns layout size for the specified {@link SizeType}.
     *
     * @param sizeType {@link SizeType}
     * @return layout size for the specified {@link SizeType}
     */
    protected Dimension layoutSize ( final SizeType sizeType )
    {
        // Calculating content size
        final Dimension contentSize;
        if ( views.size () > 0 )
        {
            // Sizes helper
            final MultiSplitLayoutHelper.Static helper = new MultiSplitLayoutHelper.Static ( multiSplitPane, views, sizeType );

            // Calculating width and height
            int width = 0;
            int height = 0;
            int percentageSize = 0;
            for ( final MultiSplitView view : views )
            {
                final int index = views.indexOf ( view );
                final int extra = index != views.size () - 1 ? multiSplitPane.getDividerSize () : 0;
                final Component component = view.component ();

                // Choosing component size
                final Dimension cs = helper.size ( component, sizeType );

                // Calculating component pixel length
                // Percentage and fill views will be saved for later
                final int length;
                final MultiSplitConstraints constraints = view.constraints ();
                if ( constraints.isPixels () )
                {
                    // Saving pixel size as it is
                    length = constraints.pixels ();
                }
                else if ( constraints.isPercents () )
                {
                    // Normalizing percentage views
                    final int componentLength = helper.horizontal ? cs.width : cs.height;
                    final double normalized = constraints.percents () / helper.totalPercentViews;
                    percentageSize = Math.max ( percentageSize, ( int ) ( componentLength / normalized ) );
                    length = 0;
                }
                else if ( constraints.isFill () )
                {
                    // Normalizing fill views
                    final int componentLength = helper.horizontal ? cs.width : cs.height;
                    final double normalized = helper.fillViewSize / helper.totalPercentViews;
                    percentageSize = Math.max ( percentageSize, ( int ) ( componentLength / normalized ) );
                    length = 0;
                }
                else if ( constraints.isPreferred () )
                {
                    // Saving preferred size
                    final Dimension ps = helper.size ( component, SizeType.preferred );
                    length = helper.horizontal ? ps.width : ps.height;
                }
                else if ( constraints.isMinimum () )
                {
                    // Saving minimum size
                    final Dimension ms = helper.size ( component, SizeType.minimum );
                    length = helper.horizontal ? ms.width : ms.height;
                }
                else
                {
                    throw new IllegalArgumentException ( "Unknown view size value: " + constraints.size () );
                }

                // Adjusting resulting width and height
                if ( helper.horizontal )
                {
                    width += length + extra;
                    height = Math.max ( height, cs.height );
                }
                else
                {
                    width = Math.max ( width, cs.width );
                    height += length + extra;
                }
            }

            // Adding largest found percentage and fill views sizes
            if ( helper.horizontal )
            {
                width += percentageSize;
            }
            else
            {
                height += percentageSize;
            }

            // Combining resulting size
            contentSize = new Dimension ( width, height );
        }
        else
        {
            contentSize = new Dimension ( 0, 0 );
        }

        // Adjusting resulting size according to insets
        final Insets insets = multiSplitPane.getInsets ();
        return new Dimension ( insets.left + contentSize.width + insets.right, insets.top + contentSize.height + insets.bottom );
    }

    @Override
    public boolean isAnyViewExpanded ()
    {
        return getExpandedViewIndex () != -1;
    }

    @Override
    public int getExpandedViewIndex ()
    {
        int index = -1;
        for ( int i = 0; i < views.size (); i++ )
        {
            if ( views.get ( i ).state ().isExpanded () )
            {
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    public void expandView ( final int index )
    {
        // Checking view availability
        if ( 0 <= index && index < views.size () )
        {
            // Checking expanded view
            final int expandedIndex = getExpandedViewIndex ();
            if ( expandedIndex != index )
            {
                // Restoring other expanded view
                if ( expandedIndex != -1 )
                {
                    views.get ( expandedIndex ).state ().setExpanded ( false );
                }

                // Expanding specified view
                final MultiSplitView newlyExpandedView = views.get ( index );
                newlyExpandedView.state ().setExpanded ( true );

                // Updating visibility
                for ( int i = 0; i < views.size (); i++ )
                {
                    views.get ( i ).component ().setVisible ( i == index );
                    if ( i < dividers.size () )
                    {
                        dividers.get ( i ).setVisible ( i == index - 1 || i == index );
                    }
                }

                // Updating layout
                multiSplitPane.revalidate ();
                multiSplitPane.repaint ();

                // Firing expansion event
                multiSplitPane.fireViewExpanded ( newlyExpandedView.component () );
            }
        }
        else
        {
            throw new IllegalArgumentException ( "WebMultiSplitPane doesn't have a view under index: " + index );
        }
    }

    @Override
    public void collapseExpandedView ()
    {
        // Checking expanded view
        final int expandedIndex = getExpandedViewIndex ();
        if ( expandedIndex != -1 )
        {
            // Restoring expanded view
            final MultiSplitView previouslyExpandedView = views.get ( expandedIndex );
            previouslyExpandedView.state ().setExpanded ( false );

            // Updating visibility
            for ( int i = 0; i < views.size (); i++ )
            {
                views.get ( i ).component ().setVisible ( true );
                if ( i < dividers.size () )
                {
                    dividers.get ( i ).setVisible ( true );
                }
            }

            // Updating layout
            multiSplitPane.revalidate ();
            multiSplitPane.repaint ();

            // Firing collapse event
            multiSplitPane.fireViewCollapsed ( previouslyExpandedView.component () );
        }
    }

    @Override
    public void toggleViewExpansion ( final int index )
    {
        final int expandedIndex = getExpandedViewIndex ();
        if ( expandedIndex == -1 )
        {
            expandView ( index );
        }
        else
        {
            collapseExpandedView ();
        }
    }

    @Override
    public void toggleViewToLeft ( final WebMultiSplitPaneDivider divider )
    {
        toggleViewExpansion ( dividers.indexOf ( divider ) );
    }

    @Override
    public void toggleViewToRight ( final WebMultiSplitPaneDivider divider )
    {
        toggleViewExpansion ( dividers.indexOf ( divider ) + 1 );
    }

    @Override
    public boolean isDragAvailable ( final WebMultiSplitPaneDivider divider )
    {
        return multiSplitPane.isEnabled () && getExpandedViewIndex () == -1 && dividers.contains ( divider );
    }

    @Override
    public WebMultiSplitPaneDivider getDraggedDivider ()
    {
        return draggedDividerIndex != -1 ? dividers.get ( draggedDividerIndex ) : null;
    }

    @Override
    public void dividerDragStarted ( final WebMultiSplitPaneDivider divider, final MouseEvent e )
    {
        // Updating last operation
        onOperation ( Operation.splitDividerDragged );

        // Retrieving dragged divider index
        draggedDividerIndex = dividers.indexOf ( divider );
        if ( draggedDividerIndex == -1 )
        {
            throw new IllegalArgumentException ( "WebMultiSplitPane doesn't contain specified divider: " + divider );
        }

        // Initial mouse location
        dragStart = CoreSwingUtils.getMouseLocation ();

        // Informing about divider drag start
        multiSplitPane.fireViewResizeStarted ( divider );
    }

    @Override
    public void dividerDragged ( final WebMultiSplitPaneDivider divider, final MouseEvent e )
    {
        // Updating last operation
        onOperation ( Operation.splitDividerDragged );

        // Current mouse location
        final Point mouse = CoreSwingUtils.getMouseLocation ();

        // Drag delta
        final int delta = multiSplitPane.getOrientation ().isHorizontal () ? mouse.x - dragStart.x : mouse.y - dragStart.y;

        final boolean straightOrder = delta > 0;
        final int startFrom = delta > 0 ? draggedDividerIndex + 1 : draggedDividerIndex;
        final int stretchedIndex = delta > 0 ? draggedDividerIndex : draggedDividerIndex + 1;

        // Adjusting shrinked views sizes
        int remainingDelta = Math.abs ( delta );
        final int startIndex = straightOrder ? 0 : viewsCopy.size () - 1;
        final int increment = straightOrder ? 1 : -1;
        for ( int i = startIndex; straightOrder ? i < viewsCopy.size () : i >= 0; i += increment )
        {
            final int size = viewsCopy.get ( i ).state ().size ();
            if ( ( straightOrder ? i >= startFrom : i <= startFrom ) && remainingDelta > 0 )
            {
                if ( size < remainingDelta )
                {
                    views.get ( i ).state ().setSize ( 0 );
                    remainingDelta -= size;
                }
                else
                {
                    views.get ( i ).state ().setSize ( size - remainingDelta );
                    remainingDelta = 0;
                }
            }
            else
            {
                views.get ( i ).state ().setSize ( size );
            }
        }

        // Delta that we were able to apply
        final int allowedDelta = Math.abs ( delta ) - remainingDelta;
        if ( allowedDelta > 0 )
        {
            // Adjusting stretched view size
            final int stretchedSize = viewsCopy.get ( stretchedIndex ).state ().size ();
            views.get ( stretchedIndex ).state ().setSize ( stretchedSize + allowedDelta );
        }

        // Informing about divider drag
        multiSplitPane.fireViewResized ( divider );

        // Updating layout
        multiSplitPane.revalidate ();
        multiSplitPane.repaint ();
    }

    @Override
    public void dividerDragEnded ( final WebMultiSplitPaneDivider divider, final MouseEvent e )
    {
        // Updating last operation
        onOperation ( Operation.splitDividerDragEnded );

        // Cleaning up resources
        draggedDividerIndex = -1;
        dragStart = null;

        // Informing about divider drag end
        multiSplitPane.fireViewResizeEnded ( divider );
    }

    /**
     * Called upon different operation executions.
     *
     * @param operation {@link Operation} type
     */
    protected void onOperation ( final Operation operation )
    {
        // Reacting to various operations
        if ( lastOperation != Operation.splitPaneResized && operation == Operation.splitPaneResized ||
                lastOperation != Operation.splitDividerDragged && operation == Operation.splitDividerDragged )
        {
            // Copying views information for resize or drag operation
            // This information will be used to provide more precise adjustments to views sizes
            // Unlike common JSpliPane previous initial state is taken into account and used to adjust sizes accordingly
            viewsCopy = getViews ();
        }
        else if ( operation == Operation.splitPaneModelInstalled || operation == Operation.splitPaneModelUninstalled ||
                operation == Operation.splitViewsSizesInitialized || operation == Operation.splitViewsStateApplied ||
                operation == Operation.splitViewsChanged || operation == Operation.splitDividerDragEnded ||
                operation == Operation.splitOrientationChanged )
        {
            // Cleaning up views information upon some operations
            // The only operation we cannot cleanup information after is component resize as it doesn't have specific end time
            // In such case information is cleaned up upon any next operation that forces views copy to be made or cleaned up
            viewsCopy = null;
        }

        // Saving last operation
        lastOperation = operation;
    }

    /**
     * Operations that can be performed with {@link MultiSplitView}s.
     */
    protected enum Operation
    {
        splitPaneModelInstalled,
        splitPaneModelUninstalled,
        splitViewsSizesInitialized,
        splitViewsStateApplied,
        splitOrientationChanged,
        splitViewsChanged,
        splitPaneResized,
        splitDividerDragged,
        splitDividerDragEnded,
    }
}