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

import com.alee.extended.dock.drag.DockableFrameTransferHandler;
import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.laf.button.WebButton;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.focus.DefaultFocusTracker;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.icon.Icons;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.Stateful;
import com.alee.utils.CompareUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.DataRunnable;
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
 * Custom UI for {@link com.alee.extended.dock.WebDockableFrame} component.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see com.alee.extended.dock.WebDockablePane
 */

public class WebDockableFrameUI extends DockableFrameUI implements ShapeProvider, MarginSupport, PaddingSupport, PropertyChangeListener
{
    /**
     * Component painter.
     */
    @DefaultPainter ( DockableFramePainter.class )
    protected IDockableFramePainter painter;

    /**
     * Additional components used be the UI.
     */
    protected SidebarButton sidebarButton;
    protected WebPanel titlePanel;
    protected WebStyledLabel titleLabel;
    protected WebPanel buttonsPanel;
    protected WebButton dockButton;
    protected WebButton floatButton;
    protected WebButton closeButton;

    /**
     * Listeners.
     */
    protected DefaultFocusTracker tracker;

    /**
     * Runtime variables.
     */
    protected WebDockableFrame frame;
    protected Insets margin = null;
    protected Insets padding = null;

    /**
     * Returns an instance of the WebDockableFrameUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebDockableFrameUI
     */
    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebDockableFrameUI ();
    }

    /**
     * Installs UI in the specified component.
     *
     * @param c component for this UI
     */
    @Override
    public void installUI ( final JComponent c )
    {
        // Saving dockable frame reference
        frame = ( WebDockableFrame ) c;

        // Applying skin
        StyleManager.installSkin ( frame );

        // Installing settings
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
        // Uninstalling settings
        uninstallActions ();
        uninstallComponents ();

        // Uninstalling applied skin
        StyleManager.uninstallSkin ( frame );

        // Removing dockable frame reference
        frame = null;
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
        titlePanel.add ( buttonsPanel, BorderLayout.EAST );

        dockButton = new WebButton ( StyleId.dockableframeTitleIconButton.at ( buttonsPanel ), Icons.pin, Icons.pinDark );
        buttonsPanel.add ( dockButton );

        floatButton = new WebButton ( StyleId.dockableframeTitleIconButton.at ( buttonsPanel ), Icons.external, Icons.externalDark );
        floatButton.setVisible ( frame.isFloatable () );
        buttonsPanel.add ( floatButton );

        closeButton = new WebButton ( StyleId.dockableframeTitleIconButton.at ( buttonsPanel ), Icons.cross, Icons.crossDark );
        closeButton.setVisible ( frame.isClosable () );
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
                if ( frame.isDraggable () )
                {
                    final TransferHandler handler = frame.getTransferHandler ();
                    if ( handler != null )
                    {
                        handler.exportAsDrag ( frame, e, TransferHandler.MOVE );
                    }
                }
            }
        } );
        dockButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( frame.getState () == DockableFrameState.preview || frame.getState () == DockableFrameState.floating )
                {
                    frame.dock ();
                }
                else if ( frame.getState () == DockableFrameState.docked )
                {
                    frame.minimize ();
                }
            }
        } );
        floatButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                frame.detach ();
            }
        } );
        closeButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                frame.close ();
            }
        } );
        tracker = new DefaultFocusTracker ( true )
        {
            @Override
            public void focusChanged ( final boolean focused )
            {
                // Minimize frame on
                if ( !focused && frame.getState () == DockableFrameState.preview )
                {
                    frame.minimize ();
                }
            }
        };
        FocusManager.addFocusTracker ( frame, tracker );
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
        FocusManager.removeFocusTracker ( tracker );
    }

    @Override
    public void propertyChange ( final PropertyChangeEvent evt )
    {
        final String property = evt.getPropertyName ();
        if ( CompareUtils.equals ( property, WebDockableFrame.STATE_PROPERTY ) )
        {
            final DockableFrameState oldState = ( DockableFrameState ) evt.getOldValue ();
            final DockableFrameState newState = ( DockableFrameState ) evt.getNewValue ();

            // Updating sidebar button states
            sidebarButton.updateStates ();

            // Updating close button visibility
            floatButton.setVisible ( frame.isFloatable () && newState != DockableFrameState.floating );

            // Informing frame listeners
            if ( oldState == DockableFrameState.closed )
            {
                // Opened event should always be thrown first
                frame.fireFrameOpened ();
            }
            frame.fireFrameStateChanged ( oldState, newState );
            if ( newState == DockableFrameState.closed )
            {
                // Opened event should always be thrown last
                frame.fireFrameClosed ();
            }

            // Requesting frame focus on preview or dock
            if ( newState == DockableFrameState.preview || newState == DockableFrameState.docked )
            {
                requestFocusInFrame ();
            }
        }
        else if ( CompareUtils.equals ( property, WebDockableFrame.CLOSABLE_PROPERTY ) )
        {
            // Updating close button visibility
            closeButton.setVisible ( frame.isClosable () );
        }
        else if ( CompareUtils.equals ( property, WebDockableFrame.FLOATABLE_PROPERTY ) )
        {
            // Updating float button visibility
            floatButton.setVisible ( frame.isFloatable () && frame.getState () != DockableFrameState.floating );
        }
        else if ( CompareUtils.equals ( property, WebDockableFrame.ICON_PROPERTY ) )
        {
            // Updating title and sidebar button icons
            titleLabel.setIcon ( frame.getIcon () );
            sidebarButton.setIcon ( frame.getIcon () );
        }
        else if ( CompareUtils.equals ( property, WebDockableFrame.TITLE_PROPERTY ) )
        {
            // Updating title and sidebar button texts
            titleLabel.setText ( frame.getTitle () );
            sidebarButton.setText ( frame.getTitle () );
        }
        else if ( CompareUtils.equals ( property, WebDockableFrame.POSITION_PROPERTY ) )
        {
            // Updating sidebar button states
            sidebarButton.updateStates ();
        }
    }

    /**
     * Requests focus for frame content if possible.
     */
    protected void requestFocusInFrame ()
    {
        SwingUtils.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
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
        } );
    }

    @Override
    public JComponent getSidebarButton ()
    {
        return sidebarButton;
    }

    @Override
    public Shape provideShape ()
    {
        return PainterSupport.getShape ( frame, painter );
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
     * Returns dockable frame painter.
     *
     * @return dockable frame painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets dockable frame painter.
     * Pass null to remove dockable frame painter.
     *
     * @param painter new dockable frame painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( frame, new DataRunnable<IDockableFramePainter> ()
        {
            @Override
            public void run ( final IDockableFramePainter newPainter )
            {
                WebDockableFrameUI.this.painter = newPainter;
            }
        }, this.painter, painter, IDockableFramePainter.class, AdaptiveDockableFramePainter.class );
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, Bounds.component.of ( c ), c, this );
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
            onMousePress ( MouseButton.right, new MouseEventRunnable ()
            {
                @Override
                public void run ( final MouseEvent e )
                {
                    if ( frame.getState () == DockableFrameState.minimized )
                    {
                        frame.preview ();
                    }
                    else if ( frame.getState () == DockableFrameState.preview )
                    {
                        frame.minimize ();
                    }
                }
            } );
            addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    if ( frame.getState () == DockableFrameState.minimized || frame.getState () == DockableFrameState.preview )
                    {
                        frame.restore ();
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
            final boolean sel = frame.getState () == DockableFrameState.docked || frame.getState () == DockableFrameState.floating;
            if ( sel != isSelected () )
            {
                setSelected ( sel );
            }
            DecorationUtils.fireStatesChanged ( this );
        }
    }
}