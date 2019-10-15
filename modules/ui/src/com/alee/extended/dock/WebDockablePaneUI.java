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

package com.alee.extended.dock;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.data.CompassDirection;
import com.alee.api.jdk.Consumer;
import com.alee.api.jdk.Objects;
import com.alee.extended.behavior.VisibilityBehavior;
import com.alee.extended.dock.data.DockableFrameElement;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.window.WebDialog;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.CollectionUtils;
import com.alee.utils.ImageUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Custom UI for {@link WebDockablePane} component.
 *
 * @param <C> component type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see WebDockablePane
 */
public class WebDockablePaneUI<C extends WebDockablePane> extends WDockablePaneUI<C> implements ShapeSupport, MarginSupport,
        PaddingSupport, PropertyChangeListener
{
    /**
     * UI properties.
     */
    protected static final String FRAME_LISTENER = "frame.listener";
    protected static final String FRAME_DIALOG = "frame.dialog";

    /**
     * Component painter.
     */
    @DefaultPainter ( DockablePanePainter.class )
    protected IDockablePanePainter painter;

    /**
     * Listeners.
     */
    protected transient VisibilityBehavior<C> visibilityBehavior;
    protected transient DockableFrameListener proxyListener;

    /**
     * Runtime variables.
     */
    protected transient JComponent emptyContent;

    /**
     * Returns an instance of the {@link WebDockablePaneUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebDockablePaneUI}
     */
    public static ComponentUI createUI ( @NotNull final JComponent c )
    {
        return new WebDockablePaneUI ();
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( pane );

        // Installing components
        installComponents ();
    }

    @Override
    public void uninstallUI ( @NotNull final JComponent c )
    {
        // Uninstalling components
        uninstallComponents ();

        // Uninstalling applied skin
        StyleManager.uninstallSkin ( pane );

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @Override
    protected void installListeners ()
    {
        // Installing default listeners
        super.installListeners ();

        // Instaling custom listeners
        visibilityBehavior = new VisibilityBehavior<C> ( pane )
        {
            @Override
            protected void displayed ( @NotNull final C pane )
            {
                // We have to manually initialize all dialogs for floating frames when dockable pane becomes visible
                for ( final WebDockableFrame frame : pane.frames )
                {
                    if ( frame.isFloating () )
                    {
                        showFrameDialog ( frame );
                    }
                }
            }

            @Override
            protected void hidden ( @NotNull final C pane )
            {
                for ( final WebDockableFrame frame : pane.frames )
                {
                    if ( frame.isFloating () )
                    {
                        disposeFrameDialog ( frame );
                    }
                }
            }
        };
        visibilityBehavior.install ();
        pane.addPropertyChangeListener ( this );
    }

    @Override
    public void propertyChange ( final PropertyChangeEvent evt )
    {
        final String property = evt.getPropertyName ();
        if ( Objects.equals ( property, WebDockablePane.MODEL_PROPERTY, WebDockablePane.MODEL_STATE_PROPERTY ) )
        {
            // Handling model and its state changes
            updateFrameData ();
        }
        else if ( Objects.equals ( property, WebDockablePane.SIDEBAR_BUTTON_VISIBILITY_PROPERTY ) )
        {
            // Handling sidebar visibility change
            updateSidebarsVisibility ();
        }
        else if ( Objects.equals ( property, WebDockablePane.CONTENT_SPACING_PROPERTY, WebDockablePane.RESIZE_GRIPPER_WIDTH_PROPERTY ) )
        {
            // Updating dockable pane layout
            pane.revalidate ();
            pane.repaint ();
        }
        else if ( Objects.equals ( property, WebDockablePane.GLASS_LAYER_PROPERTY ) )
        {
            // Handling glass layer change
            final Component oldGlassLayer = ( Component ) evt.getOldValue ();
            final Component newGlassLayer = ( Component ) evt.getNewValue ();
            updateGlassLayerVisibility ( oldGlassLayer, newGlassLayer );
        }
        else if ( Objects.equals ( property, WebDockablePane.FRAME_PROPERTY ) )
        {
            // Handling addition of new frame
            if ( evt.getNewValue () != null )
            {
                installFrame ( ( WebDockableFrame ) evt.getNewValue () );
            }
            // Handling removal of a frame
            if ( evt.getOldValue () != null )
            {
                uninstallFrame ( ( WebDockableFrame ) evt.getOldValue () );
            }
        }
        else if ( Objects.equals ( property, WebDockablePane.CONTENT_PROPERTY ) )
        {
            // Handling content change
            final JComponent oldContent = ( JComponent ) evt.getOldValue ();
            final JComponent newContent = ( JComponent ) evt.getNewValue ();
            updateContentVisibility ( oldContent, newContent );
        }
        else if ( Objects.equals ( property, WebDockablePane.MINIMUM_ELEMENT_SIZE_PROPERTY,
                WebDockablePane.OCCUPY_MINIMUM_SIZE_FOR_CHILDREN_PROPERTY ) )
        {
            // Validating sizes
            pane.getModel ().getRoot ().validateSize ( pane );
            pane.revalidate ();
            pane.repaint ();
        }
    }

    @Override
    protected void uninstallListeners ()
    {
        // Uninstaling custom listeners
        pane.removePropertyChangeListener ( this );
        visibilityBehavior.uninstall ();
        visibilityBehavior = null;

        // Uninstalling default listeners
        super.uninstallListeners ();
    }

    /**
     * Installs UI elements.
     */
    protected void installComponents ()
    {
        emptyContent = new WebPanel ( StyleId.dockablepaneEmpty.at ( pane ) );
        if ( pane.getContent () == null )
        {
            pane.setContent ( emptyContent );
        }
    }

    /**
     * Uninstalls UI elements.
     */
    protected void uninstallComponents ()
    {
        if ( pane.getContent () == emptyContent )
        {
            pane.setContent ( null );
        }
        emptyContent = null;
    }

    /**
     * Ensures that model data exists for all previously added frames.
     * This method call will have no effect if all frames have data in model and it equal to the frame states.
     */
    protected void updateFrameData ()
    {
        // Ensures data for all added frames exist in the model
        for ( final WebDockableFrame frame : pane.frames )
        {
            pane.getModel ().updateFrame ( pane, frame );
        }

        // Ensure dockable pane layout is correct
        pane.revalidate ();
        pane.repaint ();
    }

    /**
     * Updates visibility of the dockable pane glass layer.
     *
     * @param oldGlassLayer previously used glass layer
     * @param newGlassLayer currently used glass layer
     */
    protected void updateGlassLayerVisibility ( @Nullable final Component oldGlassLayer, @Nullable final Component newGlassLayer )
    {
        if ( oldGlassLayer != null )
        {
            pane.remove ( oldGlassLayer );
        }
        if ( newGlassLayer != null )
        {
            pane.add ( newGlassLayer, 0 );
        }
        pane.revalidate ();
        pane.repaint ();
    }

    /**
     * Updates visibility of the buttons on all four sidebars.
     */
    protected void updateSidebarsVisibility ()
    {
        if ( CollectionUtils.notEmpty ( pane.frames ) )
        {
            for ( final WebDockableFrame frame : pane.frames )
            {
                if ( frame.isSidebarButtonVisible () )
                {
                    if ( !pane.contains ( frame.getSidebarButton () ) )
                    {
                        pane.add ( frame.getSidebarButton () );
                    }
                }
                else
                {
                    if ( pane.contains ( frame.getSidebarButton () ) )
                    {
                        pane.remove ( frame.getSidebarButton () );
                    }
                }
            }
            pane.revalidate ();
            pane.repaint ();
        }
    }

    /**
     * Updates visibility of the specified {@link WebDockableFrame}.
     *
     * @param frame {@link WebDockableFrame} to update visibility for
     */
    protected void updateFrameVisibility ( @NotNull final WebDockableFrame frame )
    {
        if ( frame.isPreview () || frame.isDocked () )
        {
            if ( !pane.contains ( frame ) )
            {
                pane.add ( frame );
            }
        }
        else
        {
            if ( pane.contains ( frame ) )
            {
                pane.remove ( frame );
            }
        }
    }

    /**
     * Updates visibility of the content.
     *
     * @param oldContent previously displayed content
     * @param newContent currently displayed content
     */
    protected void updateContentVisibility ( @Nullable final JComponent oldContent, @Nullable final JComponent newContent )
    {
        boolean update = false;
        if ( oldContent != null )
        {
            // Removing old content
            pane.remove ( oldContent );
            update = true;
        }
        if ( newContent != null )
        {
            // Adding new content
            pane.add ( newContent );
            update = true;
        }
        else
        {
            // Restore empty content
            pane.setContent ( emptyContent );
        }
        if ( update )
        {
            // Performing update only if necessary
            pane.revalidate ();
            pane.repaint ();
        }
    }

    /**
     * Installs {@link com.alee.extended.dock.WebDockableFrame} onto this dockable pane.
     *
     * @param frame {@link com.alee.extended.dock.WebDockableFrame} to install
     */
    protected void installFrame ( @NotNull final WebDockableFrame frame )
    {
        boolean updateLayout = false;

        // Adding model element
        pane.getModel ().updateFrame ( pane, frame );

        // Adding frame and its elements to this dockable pane
        frame.setDockablePane ( pane );
        if ( frame.isPreview () || frame.isDocked () )
        {
            pane.add ( frame );
            updateLayout = true;
        }
        if ( frame.isSidebarButtonVisible () )
        {
            pane.add ( frame.getSidebarButton () );
            updateLayout = true;
        }
        final PropertyChangeListener frameListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                final String property = evt.getPropertyName ();
                if ( Objects.equals ( property, WebDockableFrame.FRAME_ID_PROPERTY ) )
                {
                    // Since frame ID changed it is almost the same as adding new one
                    // But we skip actual frame addition into dockable pane since it is already there
                    // We also don't need to remove data for old frame ID, we can keep it
                    pane.getModel ().updateFrame ( pane, frame );
                }
                else if ( Objects.equals ( property, WebDockableFrame.STATE_PROPERTY ) )
                {
                    final DockableFrameState oldState = ( DockableFrameState ) evt.getOldValue ();
                    final DockableFrameState newState = ( DockableFrameState ) evt.getNewValue ();

                    // Updating model frame state
                    final DockableFrameElement element = pane.getModel ().getElement ( frame.getId () );
                    element.setState ( frame.getState () );

                    // Disposing floating frame dialog
                    if ( oldState == DockableFrameState.floating )
                    {
                        disposeFrameDialog ( frame );
                    }

                    // Updating frame and its sidebar button visibility
                    updateFrameVisibility ( frame );
                    updateSidebarsVisibility ();
                    pane.revalidate ();
                    pane.repaint ();

                    // Creating floating frame dialog
                    if ( newState == DockableFrameState.floating )
                    {
                        showFrameDialog ( frame );
                    }
                }
                else if ( Objects.equals ( property, WebDockableFrame.POSITION_PROPERTY ) )
                {
                    // Updating frame and its sidebar button visibility
                    updateSidebarsVisibility ();
                }
                else if ( Objects.equals ( property, WebDockableFrame.MAXIMIZED_PROPERTY ) )
                {
                    // Ensure none other frame is maximized
                    if ( frame.isMaximized () )
                    {
                        for ( final WebDockableFrame other : pane.frames )
                        {
                            if ( other != frame && other.isMaximized () )
                            {
                                other.setMaximized ( false );
                                break;
                            }
                        }
                    }

                    // Updating frame in the model
                    pane.getModel ().updateFrame ( pane, frame );
                }
                else if ( Objects.equals ( property, WebDockableFrame.RESTORE_STATE_PROPERTY ) )
                {
                    // Updating frame restore state
                    final DockableFrameElement element = pane.getModel ().getElement ( frame.getId () );
                    element.setRestoreState ( frame.getRestoreState () );
                }
                else if ( Objects.equals ( property, WebDockableFrame.TITLE_PROPERTY ) )
                {
                    // Updating floating frame dialog title
                    if ( frame.isFloating () )
                    {
                        final WebDialog dialog = ( WebDialog ) frame.getClientProperty ( FRAME_DIALOG );
                        dialog.setTitle ( frame.getTitle () );
                    }
                }
                else if ( Objects.equals ( property, WebDockableFrame.ICON_PROPERTY ) )
                {
                    // Updating floating frame dialog icon
                    if ( frame.isFloating () )
                    {
                        final WebDialog dialog = ( WebDialog ) frame.getClientProperty ( FRAME_DIALOG );
                        dialog.setIconImage ( ImageUtils.getBufferedImage ( frame.getIcon () ) );
                    }
                }
            }
        };
        frame.addPropertyChangeListener ( frameListener );
        frame.putClientProperty ( FRAME_LISTENER, frameListener );

        // Registering frame listener
        frame.addFrameListener ( getProxyListener () );

        // Restores frame dialog
        if ( pane.isShowing () && frame.isFloating () )
        {
            showFrameDialog ( frame );
        }

        // Updating dockable pane layout
        if ( updateLayout )
        {
            pane.revalidate ();
            pane.repaint ();
        }

        // Informing frame
        frame.fireFrameAdded ();
    }

    /**
     * Uninstalls {@link com.alee.extended.dock.WebDockableFrame} from this dockable pane.
     *
     * @param frame {@link com.alee.extended.dock.WebDockableFrame} to uninstall
     */
    private void uninstallFrame ( @NotNull final WebDockableFrame frame )
    {
        boolean updateLayout = false;

        // Unregistering frame listener
        frame.removeFrameListener ( getProxyListener () );

        // Removing frame and its elements from this dockable pane
        final PropertyChangeListener frameListener = ( PropertyChangeListener ) frame.getClientProperty ( FRAME_LISTENER );
        frame.removePropertyChangeListener ( frameListener );
        frame.putClientProperty ( FRAME_LISTENER, null );
        if ( frame.isSidebarButtonVisible () )
        {
            pane.remove ( frame.getSidebarButton () );
            updateLayout = true;
        }
        if ( frame.isPreview () || frame.isDocked () )
        {
            pane.remove ( frame );
            updateLayout = true;
        }
        frame.setDockablePane ( null );

        // Removing model element
        pane.getModel ().removeFrame ( pane, frame );

        // Disposes frame dialog
        if ( pane.isShowing () && frame.isFloating () )
        {
            disposeFrameDialog ( frame );
        }

        // Updating dockable pane layout
        if ( updateLayout )
        {
            pane.revalidate ();
            pane.repaint ();
        }

        // Informing frame
        frame.fireFrameRemoved ();
    }

    /**
     * Returns proxy listener for all dockable frames.
     *
     * @return proxy listener for all dockable frames
     */
    @NotNull
    protected DockableFrameListener getProxyListener ()
    {
        if ( proxyListener == null )
        {
            proxyListener = new DockableFrameListener ()
            {
                @Override
                public void frameAdded ( @NotNull final WebDockableFrame frame, @NotNull final WebDockablePane dockablePane )
                {
                    pane.fireFrameAdded ( frame, dockablePane );
                }

                @Override
                public void frameStateChanged ( @NotNull final WebDockableFrame frame, @NotNull final DockableFrameState oldState,
                                                @NotNull final DockableFrameState newState )
                {
                    pane.fireFrameStateChanged ( frame, oldState, newState );
                }

                @Override
                public void frameMoved ( @NotNull final WebDockableFrame frame, @NotNull final CompassDirection position )
                {
                    pane.fireFrameMoved ( frame, position );
                }

                @Override
                public void frameRemoved ( @NotNull final WebDockableFrame frame, @NotNull final WebDockablePane dockablePane )
                {
                    pane.fireFrameRemoved ( frame, dockablePane );
                }
            };
        }
        return proxyListener;
    }

    /**
     * Displays {@link com.alee.extended.dock.WebDockableFrame} dialog.
     * This should only be called when frame is switching to {@link com.alee.extended.dock.DockableFrameState#floating} state.
     *
     * @param frame {@link com.alee.extended.dock.WebDockableFrame}
     */
    protected void showFrameDialog ( @NotNull final WebDockableFrame frame )
    {
        final StyleId dialogStyle = StyleId.dockablepaneFloating.at ( pane );
        final WebDialog dialog = new WebDialog ( dialogStyle, pane, frame.getTitle () );
        dialog.setIconImage ( ImageUtils.getBufferedImage ( frame.getIcon () ) );
        dialog.add ( frame, BorderLayout.CENTER );
        dialog.setMinimumSize ( SwingUtils.stretch ( frame.getUI ().getMinimumDialogSize (), dialog.getRootPane ().getInsets () ) );
        dialog.setBounds ( pane.getModel ().getFloatingBounds ( pane, frame, dialog ) );
        dialog.setDefaultCloseOperation ( WindowConstants.DO_NOTHING_ON_CLOSE );
        dialog.addComponentListener ( new ComponentAdapter ()
        {
            @Override
            public void componentResized ( final ComponentEvent e )
            {
                final DockableFrameElement element = pane.getModel ().getElement ( frame.getId () );
                element.saveFloatingBounds ( frame );
            }

            @Override
            public void componentMoved ( final ComponentEvent e )
            {
                final DockableFrameElement element = pane.getModel ().getElement ( frame.getId () );
                element.saveFloatingBounds ( frame );
            }
        } );
        dialog.addWindowListener ( new WindowAdapter ()
        {
            @Override
            public void windowClosing ( final WindowEvent e )
            {
                // This even will not be called upon dialog disposal, so we will only get here through UI dialog close
                // Still perform this if frame is in floating state (no state change ongoing) for safety reasons
                if ( frame.isFloating () )
                {
                    frame.close ();
                }
            }
        } );
        dialog.setModal ( false );
        dialog.setVisible ( true );
        frame.putClientProperty ( FRAME_DIALOG, dialog );
        frame.requestFocusInWindow ();
    }

    /**
     * Disposes {@link com.alee.extended.dock.WebDockableFrame} dialog.
     * This should only be called when frame is switching out of the {@link com.alee.extended.dock.DockableFrameState#floating} state.
     *
     * @param frame {@link com.alee.extended.dock.WebDockableFrame}
     */
    protected void disposeFrameDialog ( @NotNull final WebDockableFrame frame )
    {
        final WebDialog dialog = ( WebDialog ) frame.getClientProperty ( FRAME_DIALOG );
        dialog.remove ( frame );
        dialog.dispose ();
        frame.putClientProperty ( FRAME_DIALOG, null );
    }

    @NotNull
    @Override
    public JComponent createGlassLayer ()
    {
        return new DockablePaneGlassLayer ( pane );
    }

    @NotNull
    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( pane, painter );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( pane, painter );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( pane, painter, enabled );
    }

    @Nullable
    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( pane );
    }

    @Override
    public void setMargin ( @Nullable final Insets margin )
    {
        PainterSupport.setMargin ( pane, margin );
    }

    @Nullable
    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( pane );
    }

    @Override
    public void setPadding ( @Nullable final Insets padding )
    {
        PainterSupport.setPadding ( pane, padding );
    }

    /**
     * Returns dockable pane painter.
     *
     * @return dockable pane painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getPainter ( painter );
    }

    /**
     * Sets dockable pane painter.
     * Pass null to remove dockable pane painter.
     *
     * @param painter new dockable pane painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( pane, this, new Consumer<IDockablePanePainter> ()
        {
            @Override
            public void accept ( final IDockablePanePainter newPainter )
            {
                WebDockablePaneUI.this.painter = newPainter;
            }
        }, this.painter, painter, IDockablePanePainter.class, AdaptiveDockablePanePainter.class );
    }

    @Override
    public boolean contains ( final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, painter, x, y );
    }

    @Override
    public int getBaseline ( final JComponent c, final int width, final int height )
    {
        return PainterSupport.getBaseline ( c, this, painter, width, height );
    }

    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( final JComponent c )
    {
        return PainterSupport.getBaselineResizeBehavior ( c, this, painter );
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, c, this, new Bounds ( c ) );
        }
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, painter );
    }
}