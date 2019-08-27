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

package com.alee.laf.splitpane;

import com.alee.api.annotations.NotNull;
import com.alee.api.jdk.Objects;
import com.alee.extended.canvas.WebCanvas;
import com.alee.laf.WebUI;
import com.alee.managers.style.BoundsType;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.utils.LafUtils;

import javax.swing.*;
import javax.swing.plaf.SplitPaneUI;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Pluggable look and feel interface for {@link JSplitPane} component.
 *
 * @param <C> component type
 * @author Mikle Garin
 */
public abstract class WSplitPaneUI<C extends JSplitPane> extends SplitPaneUI implements WebUI<C>
{
    /**
     * The divider used for non-continuous layout is added to the split pane with this object.
     */
    protected static final String NON_CONTINUOUS_DIVIDER = "nonContinuousDivider";

    /**
     * {@link SplitPaneLayout} that is used as {@link LayoutManager} for the {@link JSplitPane}.
     */
    protected SplitPaneLayout layoutManager;

    /**
     * {@link WebSplitPaneDivider} instance used for the {@link JSplitPane}.
     */
    protected WebSplitPaneDivider divider;

    /**
     * {@link ComponentListener} for the side components of the {@link JSplitPane}.
     */
    protected ComponentListener componentListener;

    /**
     * {@link ContainerListener} for the {@link JSplitPane}.
     */
    protected ContainerListener containerListener;

    /**
     * {@link SplitPaneInputListener} for the {@link JSplitPane}.
     */
    protected SplitPaneInputListener<C> inputListener;

    /**
     * {@link PropertyChangeListener} for the {@link JSplitPane}.
     */
    protected PropertyChangeListener propertyChangeListener;

    /**
     * The size of the divider while the dragging session is valid.
     */
    protected int dividerSize;

    /**
     * Instance for the shadow of the divider when non continuous layout is being used.
     */
    protected JComponent nonContinuousLayoutDivider;

    /**
     * Location of the divider when the dragging session began.
     */
    protected int beginDragDividerLocation;

    /**
     * Whether or not pane sizes should be preserved upon {@link #addHeavyweightDivider()} call.
     */
    protected boolean rememberPaneSizes;

    /**
     * Indicates wether the one of {@link JSplitPane} sides is expanded.
     */
    protected boolean keepHidden = false;

    /**
     * Indicates that {@link JSplitPane} have benn painted once.
     * This is used by the LayoutManager to determine when it should use the divider location provided by the JSplitPane.
     * This is used as there is no way to determine when the layout process has completed.
     */
    protected boolean painted;

    /**
     * Whenever this is set to {@code true} method {@link #setDividerLocation(JSplitPane, int)} does nothing.
     * Although call to that method will reset this to {@code false}.
     */
    protected boolean ignoreDividerLocationChange;

    /**
     * Runtime variables.
     */
    protected C splitPane;
    protected int orientation;
    protected int lastDragLocation;
    protected boolean continuousLayout;
    protected boolean dividerKeyboardResize;
    protected boolean dividerLocationIsSet;

    @NotNull
    @Override
    public String getPropertyPrefix ()
    {
        return "SplitPane.";
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        // Saving split pane reference
        splitPane = ( C ) c;

        // Initializing runtime variables
        setDividerLocationSet ( false );
        setDividerKeyboardResize ( false );
        keepHidden = false;

        // Installing defaults and listeners
        installDefaults ();
        installListeners ();

        // More runtime variables
        setLastDragLocation ( -1 );
    }

    @Override
    public void uninstallUI ( @NotNull final JComponent c )
    {
        // Uninstalling defaults and listeners
        uninstallListeners ();
        uninstallDefaults ();

        // Removing runtime variables
        setDividerKeyboardResize ( false );
        setDividerLocationSet ( false );

        // Removing split pane reference
        splitPane = null;
    }

    /**
     * Installs the UI defaults.
     */
    protected void installDefaults ()
    {
        // Installing defaults
        LafUtils.installDefaults ( splitPane, getPropertyPrefix () );

        // Updating divider
        if ( divider == null )
        {
            divider = createDivider ();
        }
        divider.setSplitPane ( splitPane );

        // Updating orientation
        setOrientation ( splitPane.getOrientation () );

        // Updating divider
        final Integer dividerSize = ( Integer ) UIManager.get ( "SplitPane.dividerSize" );
        LookAndFeel.installProperty ( splitPane, "dividerSize", dividerSize );
        divider.setDividerSize ( splitPane.getDividerSize () );
        updateDividerVisibility ();

        // Updating split pane layout
        splitPane.add ( divider, JSplitPane.DIVIDER );
        setContinuousLayout ( splitPane.isContinuousLayout () );
        resetLayoutManager ();

        /**
         * Install the nonContinuousLayoutDivider here to avoid having to add/remove everything later.
         */
        if ( nonContinuousLayoutDivider == null )
        {
            setNonContinuousLayoutDivider ( createNonContinuousDivider (), true );
        }
        else
        {
            setNonContinuousLayoutDivider ( nonContinuousLayoutDivider, true );
        }
    }

    /**
     * Uninstalls the UI defaults.
     */
    protected void uninstallDefaults ()
    {
        // Updating split pane layout
        if ( splitPane.getLayout () == layoutManager )
        {
            splitPane.setLayout ( null );
        }
        if ( nonContinuousLayoutDivider != null )
        {
            splitPane.remove ( nonContinuousLayoutDivider );
        }
        splitPane.remove ( divider );
        divider.setSplitPane ( null );
        layoutManager = null;

        // Resetting dividers
        divider = null;
        nonContinuousLayoutDivider = null;
        setNonContinuousLayoutDivider ( null );

        // Uninstalling defaults
        LafUtils.uninstallDefaults ( splitPane );
    }

    /**
     * Installs default component listeners.
     */
    protected void installListeners ()
    {
        componentListener = createComponentListener ();
        addSideComponentListener ( splitPane.getLeftComponent () );
        addSideComponentListener ( splitPane.getRightComponent () );
        containerListener = createContainerListener ();
        splitPane.addContainerListener ( containerListener );

        propertyChangeListener = createPropertyChangeListener ();
        splitPane.addPropertyChangeListener ( propertyChangeListener );

        inputListener = createInputListener ();
        inputListener.install ( splitPane );
    }

    /**
     * Uninstalls default component listeners.
     */
    protected void uninstallListeners ()
    {
        inputListener.uninstall ( splitPane );
        inputListener = null;

        splitPane.removePropertyChangeListener ( propertyChangeListener );
        propertyChangeListener = null;

        splitPane.removeContainerListener ( containerListener );
        containerListener = null;
        removeSideComponentListener ( splitPane.getRightComponent () );
        removeSideComponentListener ( splitPane.getLeftComponent () );
        componentListener = null;
    }

    /**
     * Returns {@link ComponentListener} for the side components of the {@link JSplitPane}.
     *
     * @return {@link ComponentListener} for the side components of the {@link JSplitPane}
     */
    protected ComponentListener createComponentListener ()
    {
        return new ComponentAdapter ()
        {
            @Override
            public void componentShown ( final ComponentEvent e )
            {
                updateDividerVisibility ();
            }

            @Override
            public void componentHidden ( final ComponentEvent e )
            {
                updateDividerVisibility ();
            }
        };
    }

    /**
     * Returns {@link ContainerListener} for the {@link JSplitPane}.
     *
     * @return {@link ContainerListener} for the {@link JSplitPane}
     */
    protected ContainerListener createContainerListener ()
    {
        return new ContainerListener ()
        {
            @Override
            public void componentAdded ( final ContainerEvent e )
            {
                final Component added = e.getChild ();
                if ( added != getDivider () && added != getNonContinuousLayoutDivider () )
                {
                    addSideComponentListener ( added );
                }
                updateDividerVisibility ();
            }

            @Override
            public void componentRemoved ( final ContainerEvent e )
            {
                final Component removed = e.getChild ();
                if ( removed != getDivider () && removed != getNonContinuousLayoutDivider () )
                {
                    removeSideComponentListener ( removed );
                }
                updateDividerVisibility ();
            }
        };
    }

    /**
     * Adds {@link ComponentListener} for the specified side {@link Component} of the {@link JSplitPane}.
     *
     * @param component side {@link Component}
     */
    protected void addSideComponentListener ( final Component component )
    {
        if ( component != null )
        {
            component.addComponentListener ( componentListener );
        }
    }

    /**
     * Removes {@link ComponentListener} from the specified side {@link Component} of the {@link JSplitPane}
     *
     * @param component side {@link Component}
     */
    protected void removeSideComponentListener ( final Component component )
    {
        if ( component != null )
        {
            component.removeComponentListener ( componentListener );
        }
    }

    /**
     * Updates divider visibility.
     */
    protected void updateDividerVisibility ()
    {
        if ( getDivider () != null )
        {
            final Component left = splitPane.getLeftComponent ();
            final Component right = splitPane.getRightComponent ();
            getDivider ().setVisible ( left != null && left.isVisible () && right != null && right.isVisible () );
            splitPane.revalidate ();
            splitPane.repaint ();
        }
    }

    /**
     * Returns {@link PropertyChangeListener} for the {@link JSplitPane}.
     *
     * @return {@link PropertyChangeListener} for the {@link JSplitPane}
     */
    protected PropertyChangeListener createPropertyChangeListener ()
    {
        return new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent event )
            {
                if ( getNonContinuousLayoutDivider () != null && Objects.equals ( event.getPropertyName (),
                        JSplitPane.ORIENTATION_PROPERTY, JSplitPane.ONE_TOUCH_EXPANDABLE_PROPERTY ) )
                {
                    DecorationUtils.fireStatesChanged ( getNonContinuousLayoutDivider () );
                }
            }
        };
    }

    /**
     * Returns {@link SplitPaneInputListener} for the {@link JSplitPane}.
     *
     * @return {@link SplitPaneInputListener} for the {@link JSplitPane}
     */
    protected SplitPaneInputListener<C> createInputListener ()
    {
        return new WSplitPaneInputListener<C, WSplitPaneUI<C>> ();
    }

    /**
     * Returns {@link JSplitPane} orientation.
     *
     * @return {@link JSplitPane} orientation
     */
    protected int getOrientation ()
    {
        return orientation;
    }

    /**
     * Set {@link JSplitPane} orientation.
     *
     * @param orientation {@link JSplitPane} orientation
     */
    protected void setOrientation ( final int orientation )
    {
        this.orientation = orientation;
    }

    /**
     * Returns whether or not {@link JSplitPane} uses continuous layout.
     *
     * @return {@code true} if {@link JSplitPane} uses continuous layout, {@code false} otherwise
     */
    protected boolean isContinuousLayout ()
    {
        return continuousLayout;
    }

    /**
     * Sets whether or not {@link JSplitPane} uses continuous layout.
     *
     * @param continuous whether or not {@link JSplitPane} uses continuous layout
     */
    protected void setContinuousLayout ( final boolean continuous )
    {
        this.continuousLayout = continuous;
    }

    /**
     * Returns {@link JSplitPane} last divider drag location.
     *
     * @return {@link JSplitPane} last divider drag location
     */
    public int getLastDragLocation ()
    {
        return lastDragLocation;
    }

    /**
     * Set {@link JSplitPane} last divider drag location.
     *
     * @param location {@link JSplitPane} last divider drag location
     */
    public void setLastDragLocation ( final int location )
    {
        this.lastDragLocation = location;
    }

    /**
     * Returns whether or not divider location was set.
     *
     * @return {@code true} if divider location was set, {@code false} otherwise
     */
    protected boolean isDividerLocationSet ()
    {
        return dividerLocationIsSet;
    }

    /**
     * Sets whether or not divider location was set.
     *
     * @param set whether or not divider location was set
     */
    protected void setDividerLocationSet ( final boolean set )
    {
        this.dividerLocationIsSet = set;
    }

    /**
     * Sets whether or not keyboard divider resize is active.
     *
     * @return {@code true} if keyboard divider resize is active, {@code false} otherwise
     */
    protected boolean isDividerKeyboardResize ()
    {
        return dividerKeyboardResize;
    }

    /**
     * Sets whether or not keyboard divider resize is active.
     *
     * @param active whether or not keyboard divider resize is active
     */
    protected void setDividerKeyboardResize ( final boolean active )
    {
        this.dividerKeyboardResize = active;
    }

    /**
     * Returns whether or not flag to ignore location changes is set.
     *
     * @return {@code true} if flag to ignore location changes is set, {@code false} otherwise
     */
    protected boolean isIgnoreDividerLocationChange ()
    {
        return ignoreDividerLocationChange;
    }

    /**
     * Sets whether or not next location change should be ignored.
     *
     * @param ignore whether or not next location change should be ignored
     */
    protected void setIgnoreDividerLocationChange ( final boolean ignore )
    {
        this.ignoreDividerLocationChange = ignore;
    }

    /**
     * Returns pixel increment for moving split via keyboard.
     *
     * @return pixel increment for moving split via keyboard
     */
    protected int getKeyboardMoveIncrement ()
    {
        return 3;
    }

    /**
     * Returns {@link JSplitPane} this {@link WSplitPaneUI} is used for.
     *
     * @return {@link JSplitPane} this {@link WSplitPaneUI} is used for
     */
    public JSplitPane getSplitPane ()
    {
        return splitPane;
    }

    /**
     * Sets size of the divider.
     *
     * @param dividerSize size of the divider
     */
    public void setDividerSize ( final int dividerSize )
    {
        divider.setDividerSize ( dividerSize );
        this.dividerSize = dividerSize;
    }

    /**
     * Returns current divider positioned between left and right panes.
     *
     * @return current divider positioned between left and right panes
     */
    public WebSplitPaneDivider getDivider ()
    {
        return divider;
    }

    /**
     * Returns default divider positioned between left and right panes.
     *
     * @return default divider positioned between left and right panes
     */
    protected WebSplitPaneDivider createDivider ()
    {
        return new WebSplitPaneDivider ( StyleId.splitpaneContinuousDivider.at ( splitPane ), splitPane );
    }

    /**
     * Returns current divider used when {@link JSplitPane} is configured to not continuously layout.
     * This divider will only be used during a dragging session.
     *
     * @return current divider used when {@link JSplitPane} is configured to not continuously layout
     */
    public JComponent getNonContinuousLayoutDivider ()
    {
        return nonContinuousLayoutDivider;
    }

    /**
     * Returns default divider used when {@link JSplitPane} is configured to not continuously layout.
     * This divider will only be used during a dragging session.
     *
     * @return default divider used when {@link JSplitPane} is configured to not continuously layout
     */
    protected JComponent createNonContinuousDivider ()
    {
        return new NonContinuousDivider ();
    }

    /**
     * Sets divider used when {@link JSplitPane} is configured to not continuously layout.
     * This divider will only be used during a dragging session.
     *
     * @param divider new non-continuous divider
     */
    protected void setNonContinuousLayoutDivider ( final JComponent divider )
    {
        setNonContinuousLayoutDivider ( divider, true );
    }

    /**
     * Sets divider used when {@link JSplitPane} is configured to not continuously layout.
     * This divider will only be used during a dragging session.
     *
     * @param divider       new non-continuous divider
     * @param rememberSizes whether or not pane sizes should be preserved
     */
    protected void setNonContinuousLayoutDivider ( final JComponent divider, final boolean rememberSizes )
    {
        rememberPaneSizes = rememberSizes;
        if ( nonContinuousLayoutDivider != null && splitPane != null )
        {
            splitPane.remove ( nonContinuousLayoutDivider );
        }
        nonContinuousLayoutDivider = divider;
    }

    /**
     * Adds non-continuous divider onto the {@link JSplitPane} and performs required updates.
     */
    protected void addHeavyweightDivider ()
    {
        if ( nonContinuousLayoutDivider != null && splitPane != null )
        {
            /**
             * Needs to remove all the components and re-add them! YECK!
             * This is all done so that the nonContinuousLayoutDivider will be drawn on top of the other components,
             * without this, one of the heavyweights will draw over the divider!
             */
            final Component leftC = splitPane.getLeftComponent ();
            final Component rightC = splitPane.getRightComponent ();
            final int lastLocation = splitPane.getDividerLocation ();

            if ( leftC != null )
            {
                splitPane.setLeftComponent ( null );
            }
            if ( rightC != null )
            {
                splitPane.setRightComponent ( null );
            }
            splitPane.remove ( divider );
            splitPane.add ( nonContinuousLayoutDivider, WSplitPaneUI.NON_CONTINUOUS_DIVIDER, splitPane.getComponentCount () );
            splitPane.setLeftComponent ( leftC );
            splitPane.setRightComponent ( rightC );
            splitPane.add ( divider, JSplitPane.DIVIDER );
            if ( rememberPaneSizes )
            {
                splitPane.setDividerLocation ( lastLocation );
            }
        }
    }

    @Override
    public void resetToPreferredSizes ( final JSplitPane jc )
    {
        if ( splitPane != null )
        {
            layoutManager.resetToPreferredSizes ();
            splitPane.revalidate ();
            splitPane.repaint ();
        }
    }

    @Override
    public void setDividerLocation ( final JSplitPane jc, final int location )
    {
        if ( !isIgnoreDividerLocationChange () )
        {
            setDividerLocationSet ( true );
            splitPane.revalidate ();
            splitPane.repaint ();

            if ( keepHidden )
            {
                final Insets insets = splitPane.getInsets ();
                final int orientation = splitPane.getOrientation ();
                if ( orientation == JSplitPane.VERTICAL_SPLIT && location != insets.top &&
                        location != splitPane.getHeight () - divider.getHeight () - insets.top ||
                        orientation == JSplitPane.HORIZONTAL_SPLIT && location != insets.left &&
                                location != splitPane.getWidth () - divider.getWidth () - insets.left )
                {
                    setKeepHidden ( false );
                }
            }
        }
        else
        {
            setIgnoreDividerLocationChange ( false );
        }
    }

    @Override
    public int getDividerLocation ( final JSplitPane jc )
    {
        return getOrientation () == JSplitPane.HORIZONTAL_SPLIT ? divider.getLocation ().x : divider.getLocation ().y;
    }

    @Override
    public int getMinimumDividerLocation ( final JSplitPane jc )
    {
        int minLoc = 0;
        final Component leftC = splitPane.getLeftComponent ();
        if ( leftC != null && leftC.isVisible () )
        {
            final Insets insets = splitPane.getInsets ();
            final Dimension minSize = leftC.getMinimumSize ();
            if ( getOrientation () == JSplitPane.HORIZONTAL_SPLIT )
            {
                minLoc = minSize.width;
            }
            else
            {
                minLoc = minSize.height;
            }
            if ( insets != null )
            {
                if ( getOrientation () == JSplitPane.HORIZONTAL_SPLIT )
                {
                    minLoc += insets.left;
                }
                else
                {
                    minLoc += insets.top;
                }
            }
        }
        return minLoc;
    }

    @Override
    public int getMaximumDividerLocation ( final JSplitPane jc )
    {
        final Dimension splitPaneSize = splitPane.getSize ();
        int maxLoc = 0;
        final Component rightC = splitPane.getRightComponent ();

        if ( rightC != null )
        {
            final Insets insets = splitPane.getInsets ();
            Dimension minSize = new Dimension ( 0, 0 );
            if ( rightC.isVisible () )
            {
                minSize = rightC.getMinimumSize ();
            }
            if ( getOrientation () == JSplitPane.HORIZONTAL_SPLIT )
            {
                maxLoc = splitPaneSize.width - minSize.width;
            }
            else
            {
                maxLoc = splitPaneSize.height - minSize.height;
            }
            maxLoc -= dividerSize;
            if ( insets != null )
            {
                if ( getOrientation () == JSplitPane.HORIZONTAL_SPLIT )
                {
                    maxLoc -= insets.right;
                }
                else
                {
                    maxLoc -= insets.top;
                }
            }
        }
        return Math.max ( getMinimumDividerLocation ( splitPane ), maxLoc );
    }

    @Override
    public void finishedPaintingChildren ( final JSplitPane jc, final Graphics g )
    {
        /**
         * WebLaF doesn't use lightweight divider for styling convenience.
         * It might be added in the future, but the is no real point to complicate this.
         */
    }

    @Override
    public void paint ( final Graphics g, final JComponent jc )
    {
        if ( !painted && splitPane.getDividerLocation () < 0 )
        {
            setIgnoreDividerLocationChange ( true );
            splitPane.setDividerLocation ( getDividerLocation ( splitPane ) );
        }
        painted = true;
    }

    @Override
    public Dimension getPreferredSize ( final JComponent jc )
    {
        return splitPane != null ? layoutManager.preferredLayoutSize ( splitPane ) : new Dimension ( 0, 0 );
    }

    @Override
    public Dimension getMinimumSize ( final JComponent jc )
    {
        return splitPane != null ? layoutManager.minimumLayoutSize ( splitPane ) : new Dimension ( 0, 0 );
    }

    @Override
    public Dimension getMaximumSize ( final JComponent jc )
    {
        return splitPane != null ? layoutManager.maximumLayoutSize ( splitPane ) : new Dimension ( 0, 0 );
    }

    /**
     * Resets the layout manager based on orientation and messages it with invalidateLayout to pull in appropriate Components.
     */
    protected void resetLayoutManager ()
    {
        layoutManager = new SplitPaneLayout ( getOrientation () );
        splitPane.setLayout ( layoutManager );
        layoutManager.updateComponents ( splitPane );
        splitPane.revalidate ();
        splitPane.repaint ();
    }

    /**
     * Set the value to indicate if one of the splitpane sides is expanded.
     *
     * @param keepHidden whether or not one of the splitpane sides is expanded
     */
    protected void setKeepHidden ( final boolean keepHidden )
    {
        this.keepHidden = keepHidden;
    }

    /**
     * The value returned indicates if one of the splitpane sides is expanded.
     *
     * @return true if one of the splitpane sides is expanded, false otherwise.
     */
    protected boolean isKeepHidden ()
    {
        return keepHidden;
    }

    /**
     * Returns whether or not split pane have been painted once already.
     *
     * @return {@code true} if split pane have been painted once already, {@code false} otherwise
     */
    protected boolean isPainted ()
    {
        return painted;
    }

    /**
     * Should be messaged before the dragging session starts, resets lastDragLocation and dividerSize.
     */
    protected void startDragging ()
    {
        beginDragDividerLocation = getDividerLocation ( splitPane );
        if ( getOrientation () == JSplitPane.HORIZONTAL_SPLIT )
        {
            setLastDragLocation ( divider.getBounds ().x );
            dividerSize = divider.getSize ().width;

            if ( !isContinuousLayout () )
            {
                final Rectangle border = BoundsType.border.bounds ( splitPane );
                getNonContinuousLayoutDivider ().setBounds ( getLastDragLocation (), border.y, dividerSize, border.height );
                addHeavyweightDivider ();
            }
        }
        else
        {
            setLastDragLocation ( divider.getBounds ().y );
            dividerSize = divider.getSize ().height;

            if ( !isContinuousLayout () )
            {
                final Rectangle border = BoundsType.border.bounds ( splitPane );
                getNonContinuousLayoutDivider ().setBounds ( border.x, getLastDragLocation (), border.width, dividerSize );
                addHeavyweightDivider ();
            }
        }
    }

    /**
     * Messaged during a dragging session to move the divider to the passed in location.
     * If {@link #isContinuousLayout()} is true the location is reset and the splitPane validated.
     *
     * @param location new divider location
     */
    protected void dragDividerTo ( final int location )
    {
        if ( getLastDragLocation () != location )
        {
            if ( isContinuousLayout () )
            {
                splitPane.setDividerLocation ( location );
                setLastDragLocation ( location );
            }
            else
            {
                setLastDragLocation ( location );
                final Rectangle border = BoundsType.border.bounds ( splitPane );
                if ( getOrientation () == JSplitPane.HORIZONTAL_SPLIT )
                {
                    getNonContinuousLayoutDivider ().setLocation ( getLastDragLocation (), border.y );
                }
                else
                {
                    getNonContinuousLayoutDivider ().setLocation ( border.x, getLastDragLocation () );
                }
            }
        }
    }

    /**
     * Messaged to finish the dragging session.
     * If not continuous display the dividers location will be reset.
     *
     * @param location last divider location
     */
    protected void finishDraggingTo ( final int location )
    {
        dragDividerTo ( location );
        setLastDragLocation ( -1 );

        if ( !isContinuousLayout () )
        {
            final Rectangle border = BoundsType.border.bounds ( splitPane );
            if ( getOrientation () == JSplitPane.HORIZONTAL_SPLIT )
            {
                getNonContinuousLayoutDivider ().setLocation ( -dividerSize, border.y );
            }
            else
            {
                getNonContinuousLayoutDivider ().setLocation ( border.x, -dividerSize );
            }
            splitPane.remove ( getNonContinuousLayoutDivider () );
            splitPane.setDividerLocation ( location );
        }
    }

    /**
     * Custom non-continuous divider.
     */
    public class NonContinuousDivider extends WebCanvas
    {
        /**
         * Constructs new {@link NonContinuousDivider}.
         */
        public NonContinuousDivider ()
        {
            super ( StyleId.splitpaneNonContinuousDivider.at ( WSplitPaneUI.this.splitPane ) );
        }

        @NotNull
        @Override
        public List<String> getStates ()
        {
            final List<String> states = super.getStates ();

            final JSplitPane splitPane = getSplitPane ();
            if ( splitPane != null )
            {
                // Divider orientation
                final boolean vertical = splitPane.getOrientation () == JSplitPane.HORIZONTAL_SPLIT;
                states.add ( vertical ? DecorationState.vertical : DecorationState.horizontal );

                // One-touch
                if ( splitPane.isOneTouchExpandable () )
                {
                    states.add ( DecorationState.oneTouch );
                }
            }

            return states;
        }
    }
}