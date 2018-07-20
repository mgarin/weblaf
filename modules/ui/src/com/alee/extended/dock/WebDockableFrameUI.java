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

import com.alee.api.jdk.Consumer;
import com.alee.api.jdk.Objects;
import com.alee.extended.behavior.ComponentMoveBehavior;
import com.alee.extended.dock.drag.DockableFrameTransferHandler;
import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.laf.button.WebButton;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.separator.WebSeparator;
import com.alee.managers.focus.DefaultFocusTracker;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.icon.Icons;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.Stateful;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.MouseButton;
import com.alee.utils.swing.extensions.MouseEventRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom UI for {@link WebDockableFrame} component.
 *
 * @param <C> component type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see WebDockablePane
 */
public class WebDockableFrameUI<C extends WebDockableFrame> extends WDockableFrameUI<C>
        implements ShapeSupport, MarginSupport, PaddingSupport, PropertyChangeListener
{
    /**
     * Component painter.
     */
    @DefaultPainter ( DockableFramePainter.class )
    protected IDockableFramePainter painter;

    /**
     * Listeners.
     */
    protected transient DefaultFocusTracker focusTracker;
    protected transient ComponentMoveBehavior dialogMoveBehavior;

    /**
     * Additional components used be the UI.
     */
    protected transient SidebarButton sidebarButton;
    protected transient WebPanel titlePanel;
    protected transient WebStyledLabel titleLabel;
    protected transient WebPanel buttonsPanel;
    protected transient WebButton dockButton;
    protected transient WebButton minimizeButton;
    protected transient WebButton floatButton;
    protected transient WebButton maximizeButton;
    protected transient WebButton closeButton;

    /**
     * Returns an instance of the {@link WebDockableFrameUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebDockableFrameUI}
     */
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebDockableFrameUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( frame );

        // Installing settings
        installComponents ();
        installActions ();
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling settings
        uninstallActions ();
        uninstallComponents ();

        // Uninstalling applied skin
        StyleManager.uninstallSkin ( frame );

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    /**
     * Installs frame decoration elements.
     */
    protected void installComponents ()
    {
        // Frame sidebar button
        sidebarButton = new SidebarButton ();

        // Default frame layout
        frame.setLayout ( new BorderLayout () );

        // Default frame title
        titlePanel = new WebPanel ( StyleId.dockableframeTitlePanel.at ( frame ) );
        frame.add ( titlePanel, BorderLayout.NORTH );

        titleLabel = new WebStyledLabel ( StyleId.dockableframeTitleLabel.at ( titlePanel ), frame.getTitle (), frame.getIcon () );
        titlePanel.add ( titleLabel, BorderLayout.CENTER );

        buttonsPanel = new WebPanel ( StyleId.dockableframeTitleButtonsPanel.at ( titlePanel ), new HorizontalFlowLayout ( 0, false ) );
        titlePanel.add ( buttonsPanel, BorderLayout.LINE_END );

        buttonsPanel.add ( new WebSeparator ( StyleId.dockableframeTitleSeparator.at ( buttonsPanel ) ) );

        minimizeButton = new WebButton ( StyleId.dockableframeTitleIconButton.at ( buttonsPanel ) );
        updateMinimizeButton ();
        buttonsPanel.add ( minimizeButton );

        dockButton = new WebButton ( StyleId.dockableframeTitleIconButton.at ( buttonsPanel ) );
        updateDockButton ();
        buttonsPanel.add ( dockButton );

        floatButton = new WebButton ( StyleId.dockableframeTitleIconButton.at ( buttonsPanel ) );
        updateFloatButton ();
        buttonsPanel.add ( floatButton );

        maximizeButton = new WebButton ( StyleId.dockableframeTitleIconButton.at ( buttonsPanel ) );
        updateMaximizeButton ();
        buttonsPanel.add ( maximizeButton );

        closeButton = new WebButton ( StyleId.dockableframeTitleIconButton.at ( buttonsPanel ) );
        updateCloseButton ();
        buttonsPanel.add ( closeButton );
    }

    /**
     * Uninstalls frame decoration elements.
     */
    protected void uninstallComponents ()
    {
        // Destroying frame decoration
        frame.remove ( titlePanel );
        titlePanel.resetStyleId ();
        titlePanel = null;
        titleLabel = null;
        buttonsPanel = null;
        minimizeButton = null;
        dockButton = null;
        floatButton = null;
        closeButton = null;
        frame.setLayout ( null );

        // Destroying sidebar button
        if ( frame.getDockablePane () != null )
        {
            frame.getDockablePane ().remove ( sidebarButton );
        }
        StyleManager.resetStyleId ( sidebarButton );
        sidebarButton = null;
    }

    /**
     * Installs actions for UI elements.
     */
    protected void installActions ()
    {
        dialogMoveBehavior = new ComponentMoveBehavior ( titlePanel );
        dialogMoveBehavior.setEnabled ( frame.isFloating () );
        dialogMoveBehavior.install ();

        titlePanel.onMousePress ( MouseButton.left, new MouseEventRunnable ()
        {
            @Override
            public void run ( final MouseEvent e )
            {
                // Requesting focus into the frame
                requestFocusInFrame ();
            }
        } );
        titlePanel.onMousePress ( MouseButton.middle, new MouseEventRunnable ()
        {
            @Override
            public void run ( final MouseEvent e )
            {
                // Hiding frame on middle mouse button press
                frame.minimize ();
            }
        } );
        titlePanel.onDragStart ( 5, new MouseEventRunnable ()
        {
            @Override
            public void run ( final MouseEvent e )
            {
                // Starting frame drag if transfer handler is available
                if ( frame.isDraggable () && !frame.isFloating () )
                {
                    final TransferHandler handler = frame.getTransferHandler ();
                    if ( handler != null )
                    {
                        handler.exportAsDrag ( frame, e, TransferHandler.MOVE );
                    }
                }
            }
        } );

        minimizeButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                frame.minimize ();
            }
        } );

        dockButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( frame.isPreview () || frame.isFloating () )
                {
                    // Requesting focus into the frame
                    requestFocusInFrame ();

                    // Docking frame
                    frame.dock ();
                }
                else if ( frame.isDocked () )
                {
                    // Minimizing frame
                    frame.minimize ();
                }
            }
        } );

        floatButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                // Requesting focus into the frame
                requestFocusInFrame ();

                // Detaching frame
                frame.detach ();
            }
        } );

        maximizeButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                // Requesting focus into the frame
                requestFocusInFrame ();

                // Maximizing frame
                frame.maximize ();
            }
        } );

        closeButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                // Closing frame
                frame.close ();
            }
        } );

        focusTracker = new DefaultFocusTracker ( frame, true )
        {
            @Override
            public void focusChanged ( final boolean focused )
            {
                // Minimize frame on
                if ( !focused && frame.isPreview () )
                {
                    frame.minimize ();
                }
            }
        };
        FocusManager.addFocusTracker ( frame, focusTracker );

        frame.addPropertyChangeListener ( this );

        frame.setTransferHandler ( new DockableFrameTransferHandler () );
    }

    /**
     * Uninstalls actions from UI elements.
     */
    protected void uninstallActions ()
    {
        frame.setTransferHandler ( null );

        frame.removePropertyChangeListener ( this );

        FocusManager.removeFocusTracker ( frame, focusTracker );
        focusTracker = null;

        dialogMoveBehavior.uninstall ();
        dialogMoveBehavior = null;
    }

    @Override
    public void propertyChange ( final PropertyChangeEvent evt )
    {
        final String property = evt.getPropertyName ();
        if ( Objects.equals ( property, WebDockableFrame.STATE_PROPERTY ) )
        {
            final DockableFrameState oldState = ( DockableFrameState ) evt.getOldValue ();
            final DockableFrameState newState = ( DockableFrameState ) evt.getNewValue ();

            // Updating restore state
            if ( oldState == DockableFrameState.docked || oldState == DockableFrameState.floating )
            {
                frame.setRestoreState ( oldState );
            }

            // Enable dialog move behavior only when frame is floating
            dialogMoveBehavior.setEnabled ( frame.isFloating () );

            // Updating sidebar button states
            sidebarButton.updateStates ();

            // Updating buttons
            updateMinimizeButton ();
            updateDockButton ();
            updateFloatButton ();
            updateMaximizeButton ();

            // Resetting maximized mark on any state change
            if ( frame.isMaximized () )
            {
                frame.setMaximized ( false );
            }

            // Informing frame listeners
            frame.fireFrameStateChanged ( oldState, newState );

            // Requesting frame focus on preview or dock
            if ( newState == DockableFrameState.preview || newState == DockableFrameState.docked )
            {
                requestFocusInFrame ();
            }
        }
        else if ( Objects.equals ( property, WebDockableFrame.MAXIMIZABLE_PROPERTY, WebDockableFrame.MAXIMIZED_PROPERTY ) )
        {
            // Updating maximizebutton
            updateMaximizeButton ();
        }
        else if ( Objects.equals ( property, WebDockableFrame.CLOSABLE_PROPERTY ) )
        {
            // Updating close button
            updateCloseButton ();
        }
        else if ( Objects.equals ( property, WebDockableFrame.FLOATABLE_PROPERTY ) )
        {
            // Updating float button
            updateFloatButton ();
        }
        else if ( Objects.equals ( property, WebDockableFrame.ICON_PROPERTY ) )
        {
            // Updating title and sidebar button icons
            titleLabel.setIcon ( frame.getIcon () );
            sidebarButton.setIcon ( frame.getIcon () );
        }
        else if ( Objects.equals ( property, WebDockableFrame.TITLE_PROPERTY ) )
        {
            // Updating title and sidebar button texts
            titleLabel.setText ( frame.getTitle () );
            sidebarButton.setText ( frame.getTitle () );
        }
        else if ( Objects.equals ( property, WebDockableFrame.POSITION_PROPERTY ) )
        {
            // Updating sidebar button states
            sidebarButton.updateStates ();
        }
    }

    /**
     * Updates dock button state.
     */
    private void updateMinimizeButton ()
    {
        minimizeButton.setVisible ( frame.isDocked () || frame.isFloating () );
        minimizeButton.setIcon ( Icons.underline );
        minimizeButton.setRolloverIcon ( Icons.underlineHover );
        minimizeButton.setLanguage ( "weblaf.ex.dockable.frame.minimize" );
    }

    /**
     * Updates dock button state.
     */
    private void updateDockButton ()
    {
        dockButton.setVisible ( !frame.isDocked () );
        dockButton.setIcon ( Icons.pin );
        dockButton.setRolloverIcon ( Icons.pinHover );
        dockButton.setLanguage ( "weblaf.ex.dockable.frame.dock" );
    }

    /**
     * Updates float button state.
     */
    protected void updateFloatButton ()
    {
        floatButton.setVisible ( frame.isFloatable () && !frame.isFloating () );
        floatButton.setIcon ( Icons.external );
        floatButton.setRolloverIcon ( Icons.externalHover );
        floatButton.setLanguage ( "weblaf.ex.dockable.frame.float" );
    }

    /**
     * Updates maximize button state.
     */
    protected void updateMaximizeButton ()
    {
        maximizeButton.setVisible ( frame.isMaximizable () && frame.isDocked () );
        maximizeButton.setIcon ( frame.isMaximized () ? Icons.shrink : Icons.maximize );
        maximizeButton.setRolloverIcon ( frame.isMaximized () ? Icons.shrinkHover : Icons.maximizeHover );
        maximizeButton.setLanguage ( "weblaf.ex.dockable.frame." + ( frame.isMaximized () ? "restore" : "maximize" ) );
    }

    /**
     * Updates close button state.
     */
    protected void updateCloseButton ()
    {
        closeButton.setVisible ( frame.isClosable () );
        closeButton.setIcon ( Icons.cross );
        closeButton.setRolloverIcon ( Icons.crossHover );
        closeButton.setLanguage ( "weblaf.ex.dockable.frame.close" );
    }

    @Override
    public Dimension getMinimumDialogSize ()
    {
        final Insets bi = frame.getInsets ();
        final Dimension ps = titlePanel.isVisible () ? titlePanel.getPreferredSize () : frame.getDockablePane ().getMinimumElementSize ();
        return SwingUtils.stretch ( ps, bi );
    }

    /**
     * Requests focus for frame content if possible.
     */
    protected void requestFocusInFrame ()
    {
        CoreSwingUtils.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                if ( !SwingUtils.hasFocusOwner ( frame ) )
                {
                    final Component component = SwingUtils.findFocusableComponent ( frame );
                    if ( component != null )
                    {
                        // Pass focus to the first focusable component within container
                        component.requestFocusInWindow ();
                    }
                    else
                    {
                        // Pass focus onto the frame itself
                        // Normally focus will never get onto the frame, but we can still use it when we have no other options
                        frame.requestFocusInWindow ();
                    }
                }
            }
        } );
    }

    @Override
    public JComponent getSidebarButton ()
    {
        return sidebarButton;
    }

    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( frame, painter );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( frame, painter );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( frame, painter, enabled );
    }

    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( frame );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        PainterSupport.setMargin ( frame, margin );
    }

    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( frame );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        PainterSupport.setPadding ( frame, padding );
    }

    /**
     * Returns dockable frame painter.
     *
     * @return dockable frame painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getPainter ( painter );
    }

    /**
     * Sets dockable frame painter.
     * Pass null to remove dockable frame painter.
     *
     * @param painter new dockable frame painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( frame, new Consumer<IDockableFramePainter> ()
        {
            @Override
            public void accept ( final IDockableFramePainter newPainter )
            {
                WebDockableFrameUI.this.painter = newPainter;
            }
        }, this.painter, painter, IDockableFramePainter.class, AdaptiveDockableFramePainter.class );
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

    /**
     * Custom sidebar button class providing additional decoration states.
     */
    protected class SidebarButton extends WebToggleButton implements Stateful
    {
        /**
         * Constructs new sidebar button.
         */
        public SidebarButton ()
        {
            super ( StyleId.dockableframeSidebarButton.at ( frame ), frame.getTitle (), frame.getIcon () );
            setFocusable ( false );
            setSelected ( getSelectionState () );
            onMousePress ( MouseButton.right, new MouseEventRunnable ()
            {
                @Override
                public void run ( final MouseEvent e )
                {
                    if ( frame.getDockablePane ().getSidebarButtonAction () == SidebarButtonAction.preview )
                    {
                        if ( frame.isMinimized () )
                        {
                            frame.dock ();
                        }
                        else
                        {
                            frame.minimize ();
                        }
                    }
                    else
                    {
                        if ( frame.isMinimized () )
                        {
                            frame.preview ();
                        }
                        else if ( frame.isPreview () )
                        {
                            frame.minimize ();
                        }
                    }
                }
            } );
            addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    if ( isSelected () )
                    {
                        switch ( frame.getDockablePane ().getSidebarButtonAction () )
                        {
                            case restore:
                                frame.restore ();
                                break;

                            case preview:
                                frame.preview ();
                                break;

                            case dock:
                                frame.dock ();
                                break;

                            case detach:
                                frame.detach ();
                                break;
                        }
                    }
                    else
                    {
                        frame.minimize ();
                    }
                }
            } );
        }

        @Override
        public List<String> getStates ()
        {
            final List<String> states = new ArrayList<String> ();
            states.add ( frame.getState ().name () );
            states.add ( frame.getPosition ().name () );
            return states;
        }

        /**
         * Updates decoration states.
         */
        public void updateStates ()
        {
            final boolean selected = getSelectionState ();
            if ( selected != isSelected () )
            {
                setSelected ( selected );
            }
            DecorationUtils.fireStatesChanged ( this );
        }

        /**
         * Returns sidebar button selection state.
         *
         * @return {@code true} if sidebar button should be selected, {@code false} otherwise
         */
        protected boolean getSelectionState ()
        {
            return frame.isDocked () || frame.isFloating () || frame.isPreview () && frame.getDockablePane () != null &&
                    frame.getDockablePane ().getSidebarButtonAction () == SidebarButtonAction.preview;
        }
    }
}