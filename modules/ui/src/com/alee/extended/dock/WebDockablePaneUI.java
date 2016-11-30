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

import com.alee.api.data.CompassDirection;
import com.alee.extended.behavior.ComponentVisibilityBehavior;
import com.alee.extended.dock.data.DockableFrameElement;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.window.WebDialog;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.*;
import com.alee.utils.swing.DataRunnable;

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
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see WebDockablePane
 */

public class WebDockablePaneUI extends WDockablePaneUI implements ShapeSupport, MarginSupport, PaddingSupport, PropertyChangeListener
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
    protected ComponentVisibilityBehavior visibilityBehavior;
    protected DockableFrameListener proxyListener;

    /**
     * Runtime variables.
     */
    protected WebDockablePane dockablePane;
    protected Insets margin = null;
    protected Insets padding = null;
    protected JComponent emptyContent;

    /**
     * Returns an instance of the WebDockablePaneUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebDockablePaneUI
     */
    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebDockablePaneUI ();
    }

    /**
     * Installs UI in the specified component.
     *
     * @param c component for this UI
     */
    @Override
    public void installUI ( final JComponent c )
    {
        // Saving dockable pane reference
        dockablePane = ( WebDockablePane ) c;

        // Installing default settings
        installDefaults ();

        // Applying skin
        StyleManager.installSkin ( dockablePane );

        // Installing actions
        installComponents ();
        installActions ();
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling actions
        uninstallActions ();
        uninstallComponents ();

        // Uninstalling applied skin
        StyleManager.uninstallSkin ( dockablePane );

        // Removing dockable pane reference
        dockablePane = null;
    }

    /**
     * Installs default component settings.
     */
    protected void installDefaults ()
    {
        dockablePane.setSidebarVisibility ( SidebarVisibility.minimized );
        dockablePane.setSidebarButtonAction ( SidebarButtonAction.restore );
        dockablePane.setContentSpacing ( 0 );
        dockablePane.setResizeGripper ( 10 );
        dockablePane.setMinimumElementSize ( new Dimension ( 40, 40 ) );
        dockablePane.setOccupyMinimumSizeForChildren ( true );
    }

    /**
     * Installs UI elements.
     */
    protected void installComponents ()
    {
        emptyContent = new WebPanel ( StyleId.dockablepaneEmpty.at ( dockablePane ) );
        if ( dockablePane.getContent () == null )
        {
            dockablePane.setContent ( emptyContent );
        }
    }

    /**
     * Uninstalls UI elements.
     */
    protected void uninstallComponents ()
    {
        if ( dockablePane.getContent () == emptyContent )
        {
            dockablePane.setContent ( null );
        }
        emptyContent = null;
    }

    /**
     * Installs actions for UI elements.
     */
    protected void installActions ()
    {
        visibilityBehavior = new ComponentVisibilityBehavior ( dockablePane )
        {
            @Override
            public void displayed ()
            {
                // We have to manually initialize all dialogs for floating frames when dockable pane becomes visible
                //
                for ( final WebDockableFrame frame : dockablePane.frames )
                {
                    if ( frame.isFloating () )
                    {
                        showFrameDialog ( frame );
                    }
                }
            }

            @Override
            public void hidden ()
            {
                for ( final WebDockableFrame frame : dockablePane.frames )
                {
                    if ( frame.isFloating () )
                    {
                        disposeFrameDialog ( frame );
                    }
                }
            }
        };
        visibilityBehavior.install ();
        dockablePane.addPropertyChangeListener ( this );
    }

    /**
     * Uninstalls actions for UI elements.
     */
    protected void uninstallActions ()
    {
        dockablePane.removePropertyChangeListener ( this );
        visibilityBehavior.uninstall ();
        visibilityBehavior = null;
    }

    @Override
    public void propertyChange ( final PropertyChangeEvent evt )
    {
        final String property = evt.getPropertyName ();
        if ( CompareUtils.equals ( property, WebDockablePane.MODEL_PROPERTY, WebDockablePane.MODEL_STATE_PROPERTY ) )
        {
            // Handling model and its state changes
            updateFrameData ();
        }
        else if ( CompareUtils.equals ( property, WebDockablePane.SIDEBAR_VISIBILITY_PROPERTY ) )
        {
            // Handling sidebar visibility change
            updateSidebarsVisibility ();
        }
        else if ( CompareUtils.equals ( property, WebDockablePane.CONTENT_SPACING_PROPERTY, WebDockablePane.RESIZE_GRIPPER_PROPERTY ) )
        {
            // Updating dockable pane layout
            dockablePane.revalidate ();
            dockablePane.repaint ();
        }
        else if ( CompareUtils.equals ( property, WebDockablePane.GLASS_LAYER_PROPERTY ) )
        {
            // Handling glass layer change
            final Component oldGlassLayer = ( Component ) evt.getOldValue ();
            final Component newGlassLayer = ( Component ) evt.getNewValue ();
            updateGlassLayerVisibility ( oldGlassLayer, newGlassLayer );
        }
        else if ( CompareUtils.equals ( property, WebDockablePane.FRAME_PROPERTY ) )
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
        else if ( CompareUtils.equals ( property, WebDockablePane.CONTENT_PROPERTY ) )
        {
            // Handling content change
            final JComponent oldContent = ( JComponent ) evt.getOldValue ();
            final JComponent newContent = ( JComponent ) evt.getNewValue ();
            updateContentVisibility ( oldContent, newContent );
        }
        else if ( CompareUtils.equals ( property, WebDockablePane.MINIMUM_ELEMENT_SIZE_PROPERTY,
                WebDockablePane.OCCUPY_MINIMUM_SIZE_FOR_CHILDREN_PROPERTY ) )
        {
            // Validating sizes
            dockablePane.getModel ().getRoot ().validateSize ( dockablePane );
            dockablePane.revalidate ();
            dockablePane.repaint ();
        }
    }

    /**
     * Ensures that model data exists for all previously added frames.
     * This method call will have no effect if all frames have data in model and it equal to the frame states.
     */
    protected void updateFrameData ()
    {
        // Ensures data for all added frames exist in the model
        for ( final WebDockableFrame frame : dockablePane.frames )
        {
            dockablePane.getModel ().updateFrame ( dockablePane, frame );
        }

        // Ensure dockable pane layout is correct
        dockablePane.revalidate ();
        dockablePane.repaint ();
    }

    /**
     * Updates visibility of the dockable pane glass layer.
     *
     * @param oldGlassLayer previously used glass layer
     * @param newGlassLayer currently used glass layer
     */
    protected void updateGlassLayerVisibility ( final Component oldGlassLayer, final Component newGlassLayer )
    {
        if ( oldGlassLayer != null )
        {
            dockablePane.remove ( oldGlassLayer );
        }
        if ( newGlassLayer != null )
        {
            dockablePane.add ( newGlassLayer, 0 );
        }
        dockablePane.revalidate ();
        dockablePane.repaint ();
    }

    /**
     * Updates visibility of the buttons on all four sidebars.
     */
    protected void updateSidebarsVisibility ()
    {
        if ( !CollectionUtils.isEmpty ( dockablePane.frames ) )
        {
            for ( final WebDockableFrame frame : dockablePane.frames )
            {
                if ( frame.isSidebarButtonVisible () )
                {
                    if ( !dockablePane.contains ( frame.getSidebarButton () ) )
                    {
                        dockablePane.add ( frame.getSidebarButton () );
                    }
                }
                else
                {
                    if ( dockablePane.contains ( frame.getSidebarButton () ) )
                    {
                        dockablePane.remove ( frame.getSidebarButton () );
                    }
                }
            }
            dockablePane.revalidate ();
            dockablePane.repaint ();
        }
    }

    /**
     * Updates visibility of the specified {@link WebDockableFrame}.
     *
     * @param frame {@link WebDockableFrame} to update visibility for
     */
    protected void updateFrameVisibility ( final WebDockableFrame frame )
    {
        if ( frame.isPreview () || frame.isDocked () )
        {
            if ( !dockablePane.contains ( frame ) )
            {
                dockablePane.add ( frame );
            }
        }
        else
        {
            if ( dockablePane.contains ( frame ) )
            {
                dockablePane.remove ( frame );
            }
        }
    }

    /**
     * Updates visibility of the content.
     *
     * @param oldContent previously displayed content
     * @param newContent currently displayed content
     */
    protected void updateContentVisibility ( final JComponent oldContent, final JComponent newContent )
    {
        boolean update = false;
        if ( oldContent != null )
        {
            // Removing old content
            dockablePane.remove ( oldContent );
            update = true;
        }
        if ( newContent != null )
        {
            // Adding new content
            dockablePane.add ( newContent );
            update = true;
        }
        else
        {
            // Restore empty content
            dockablePane.setContent ( emptyContent );
        }
        if ( update )
        {
            // Performing update only if necessary
            dockablePane.revalidate ();
            dockablePane.repaint ();
        }
    }

    /**
     * Installs {@link com.alee.extended.dock.WebDockableFrame} onto this dockable pane.
     *
     * @param frame {@link com.alee.extended.dock.WebDockableFrame} to install
     */
    protected void installFrame ( final WebDockableFrame frame )
    {
        boolean updateLayout = false;

        // Adding model element
        dockablePane.getModel ().updateFrame ( dockablePane, frame );

        // Adding frame and its elements to this dockable pane
        frame.setDockablePane ( dockablePane );
        if ( frame.isPreview () || frame.isDocked () )
        {
            dockablePane.add ( frame );
            updateLayout = true;
        }
        if ( frame.isSidebarButtonVisible () )
        {
            dockablePane.add ( frame.getSidebarButton () );
            updateLayout = true;
        }
        final PropertyChangeListener frameListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                final String property = evt.getPropertyName ();
                if ( CompareUtils.equals ( property, WebDockableFrame.FRAME_ID_PROPERTY ) )
                {
                    // Since frame ID changed it is almost the same as adding new one
                    // But we skip actual frame addition into dockable pane since it is already there
                    // We also don't need to remove data for old frame ID, we can keep it
                    dockablePane.getModel ().updateFrame ( dockablePane, frame );
                }
                else if ( CompareUtils.equals ( property, WebDockableFrame.STATE_PROPERTY ) )
                {
                    final DockableFrameState oldState = ( DockableFrameState ) evt.getOldValue ();
                    final DockableFrameState newState = ( DockableFrameState ) evt.getNewValue ();

                    // Updating model frame state
                    final DockableFrameElement element = dockablePane.getModel ().getElement ( frame.getId () );
                    element.setState ( frame.getState () );

                    // Disposing floating frame dialog
                    if ( oldState == DockableFrameState.floating )
                    {
                        disposeFrameDialog ( frame );
                    }

                    // Updating frame and its sidebar button visibility
                    updateFrameVisibility ( frame );
                    updateSidebarsVisibility ();
                    dockablePane.revalidate ();
                    dockablePane.repaint ();

                    // Creating floating frame dialog
                    if ( newState == DockableFrameState.floating )
                    {
                        showFrameDialog ( frame );
                    }
                }
                else if ( CompareUtils.equals ( property, WebDockableFrame.POSITION_PROPERTY ) )
                {
                    // Updating frame and its sidebar button visibility
                    updateSidebarsVisibility ();
                }
                else if ( CompareUtils.equals ( property, WebDockableFrame.MAXIMIZED_PROPERTY ) )
                {
                    // Ensure none other frame is maximized
                    if ( frame.isMaximized () )
                    {
                        for ( final WebDockableFrame other : dockablePane.frames )
                        {
                            if ( other != frame && other.isMaximized () )
                            {
                                other.setMaximized ( false );
                                break;
                            }
                        }
                    }
                }
                else if ( CompareUtils.equals ( property, WebDockableFrame.RESTORE_STATE_PROPERTY ) )
                {
                    // Updating frame restore state
                    final DockableFrameElement element = dockablePane.getModel ().getElement ( frame.getId () );
                    element.setRestoreState ( frame.getRestoreState () );
                }
                else if ( CompareUtils.equals ( property, WebDockableFrame.TITLE_PROPERTY ) )
                {
                    // Updating floating frame dialog title
                    if ( frame.isFloating () )
                    {
                        final WebDialog dialog = ( WebDialog ) frame.getClientProperty ( FRAME_DIALOG );
                        dialog.setTitle ( frame.getTitle () );
                    }
                }
                else if ( CompareUtils.equals ( property, WebDockableFrame.ICON_PROPERTY ) )
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
        if ( dockablePane.isShowing () && frame.isFloating () )
        {
            showFrameDialog ( frame );
        }

        // Updating dockable pane layout
        if ( updateLayout )
        {
            dockablePane.revalidate ();
            dockablePane.repaint ();
        }

        // Informing frame
        frame.fireFrameAdded ();
    }

    /**
     * Uninstalls {@link com.alee.extended.dock.WebDockableFrame} from this dockable pane.
     *
     * @param frame {@link com.alee.extended.dock.WebDockableFrame} to uninstall
     */
    private void uninstallFrame ( final WebDockableFrame frame )
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
            dockablePane.remove ( frame.getSidebarButton () );
            updateLayout = true;
        }
        if ( frame.isPreview () || frame.isDocked () )
        {
            dockablePane.remove ( frame );
            updateLayout = true;
        }
        frame.setDockablePane ( null );

        // Removing model element
        dockablePane.getModel ().removeFrame ( dockablePane, frame );

        // Disposes frame dialog
        if ( dockablePane.isShowing () && frame.isFloating () )
        {
            disposeFrameDialog ( frame );
        }

        // Updating dockable pane layout
        if ( updateLayout )
        {
            dockablePane.revalidate ();
            dockablePane.repaint ();
        }

        // Informing frame
        frame.fireFrameRemoved ();
    }

    /**
     * Returns proxy listener for all dockable frames.
     *
     * @return proxy listener for all dockable frames
     */
    protected DockableFrameListener getProxyListener ()
    {
        if ( proxyListener == null )
        {
            proxyListener = new DockableFrameListener ()
            {
                @Override
                public void frameAdded ( final WebDockableFrame frame, final WebDockablePane dockablePane )
                {
                    dockablePane.fireFrameAdded ( frame, dockablePane );
                }

                @Override
                public void frameStateChanged ( final WebDockableFrame frame, final DockableFrameState oldState,
                                                final DockableFrameState newState )
                {
                    dockablePane.fireFrameStateChanged ( frame, oldState, newState );
                }

                @Override
                public void frameMoved ( final WebDockableFrame frame, final CompassDirection position )
                {
                    dockablePane.fireFrameMoved ( frame, position );
                }

                @Override
                public void frameRemoved ( final WebDockableFrame frame, final WebDockablePane dockablePane )
                {
                    dockablePane.fireFrameRemoved ( frame, dockablePane );
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
    protected void showFrameDialog ( final WebDockableFrame frame )
    {
        final StyleId dialogStyle = StyleId.dockablepaneFloating.at ( dockablePane );
        final WebDialog dialog = new WebDialog ( dialogStyle, dockablePane, frame.getTitle () );
        dialog.setIconImage ( ImageUtils.getBufferedImage ( frame.getIcon () ) );
        dialog.add ( frame, BorderLayout.CENTER );
        dialog.setMinimumSize ( SwingUtils.stretch ( frame.getUI ().getMinimumDialogSize (), dialog.getRootPane ().getInsets () ) );
        dialog.setBounds ( dockablePane.getModel ().getFloatingBounds ( dockablePane, frame, dialog ) );
        dialog.setDefaultCloseOperation ( WindowConstants.DO_NOTHING_ON_CLOSE );
        dialog.addComponentListener ( new ComponentAdapter ()
        {
            @Override
            public void componentResized ( final ComponentEvent e )
            {
                final DockableFrameElement element = dockablePane.getModel ().getElement ( frame.getId () );
                element.saveFloatingBounds ( frame );
            }

            @Override
            public void componentMoved ( final ComponentEvent e )
            {
                final DockableFrameElement element = dockablePane.getModel ().getElement ( frame.getId () );
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
    protected void disposeFrameDialog ( final WebDockableFrame frame )
    {
        final WebDialog dialog = ( WebDialog ) frame.getClientProperty ( FRAME_DIALOG );
        dialog.remove ( frame );
        dialog.dispose ();
        frame.putClientProperty ( FRAME_DIALOG, null );
    }

    @Override
    public JComponent createGlassLayer ()
    {
        return new DockablePaneGlassLayer ( dockablePane );
    }

    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( dockablePane, painter );
    }

    @Override
    public Insets getMargin ()
    {
        return margin;
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        PainterSupport.updateBorder ( getPainter () );
    }

    @Override
    public Insets getPadding ()
    {
        return padding;
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        this.padding = padding;
        PainterSupport.updateBorder ( getPainter () );
    }

    /**
     * Returns dockable pane painter.
     *
     * @return dockable pane painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets dockable pane painter.
     * Pass null to remove dockable pane painter.
     *
     * @param painter new dockable pane painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( dockablePane, new DataRunnable<IDockablePanePainter> ()
        {
            @Override
            public void run ( final IDockablePanePainter newPainter )
            {
                WebDockablePaneUI.this.painter = newPainter;
            }
        }, this.painter, painter, IDockablePanePainter.class, AdaptiveDockablePanePainter.class );
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
            painter.paint ( ( Graphics2D ) g, c, this, new Boundz ( c ) );
        }
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, painter );
    }
}