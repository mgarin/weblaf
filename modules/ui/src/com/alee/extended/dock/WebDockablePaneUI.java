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

import com.alee.extended.dock.data.DockableFrameElement;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.CollectionUtils;
import com.alee.utils.CompareUtils;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Custom UI for {@link com.alee.extended.dock.WebDockablePane} component.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see com.alee.extended.dock.WebDockablePane
 */

public class WebDockablePaneUI extends DockablePaneUI implements ShapeProvider, MarginSupport, PaddingSupport, PropertyChangeListener
{
    /**
     * UI properties.
     */
    protected static final String FRAME_PROPERTY_LISTENER = "framePropertyListener";

    /**
     * Component painter.
     */
    @DefaultPainter ( DockablePanePainter.class )
    protected IDockablePanePainter painter;

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
        dockablePane.addPropertyChangeListener ( this );
    }

    /**
     * Uninstalls actions for UI elements.
     */
    protected void uninstallActions ()
    {
        dockablePane.removePropertyChangeListener ( this );
    }

    @Override
    public void propertyChange ( final PropertyChangeEvent evt )
    {
        final String property = evt.getPropertyName ();
        if ( CompareUtils.equals ( property, WebDockablePane.SIDEBAR_VISIBILITY_PROPERTY ) )
        {
            // Handling sidebar visibility change
            if ( !CollectionUtils.isEmpty ( dockablePane.frames ) )
            {
                for ( final WebDockableFrame frame : dockablePane.frames )
                {
                    switch ( dockablePane.getSidebarVisibility () )
                    {
                        case none:
                            dockablePane.remove ( frame.getSidebarButton () );
                            break;

                        case minimized:
                            dockablePane.remove ( frame.getSidebarButton () );
                            if ( frame.isSidebarButtonVisible () )
                            {
                                dockablePane.add ( frame.getSidebarButton () );
                            }
                            break;

                        case all:
                            dockablePane.add ( frame.getSidebarButton () );
                            break;
                    }
                }
                dockablePane.revalidate ();
                dockablePane.repaint ();
            }
        }
        else if ( CompareUtils.equals ( property, WebDockablePane.CONTENT_SPACING_PROPERTY, WebDockablePane.RESIZE_GRIPPER_PROPERTY,
                WebDockablePane.MODEL_PROPERTY ) )
        {
            // Updating dockable pane layout
            dockablePane.revalidate ();
            dockablePane.repaint ();
        }
        else if ( CompareUtils.equals ( property, WebDockablePane.GLASS_LAYER_PROPERTY ) )
        {
            // Handling glass layer change
            if ( evt.getOldValue () != null )
            {
                dockablePane.remove ( ( Component ) evt.getOldValue () );
            }
            if ( evt.getNewValue () != null )
            {
                dockablePane.add ( ( Component ) evt.getNewValue (), 0 );
            }
            dockablePane.revalidate ();
            dockablePane.repaint ();
        }
        else if ( CompareUtils.equals ( property, WebDockablePane.FRAME_PROPERTY ) )
        {
            // Handling frames change
            if ( evt.getNewValue () != null )
            {
                // Handling addition of new frame
                installFrame ( ( WebDockableFrame ) evt.getNewValue () );
            }
            else
            {
                // Handling removal of a frame
                uninstallFrame ( ( WebDockableFrame ) evt.getOldValue () );
            }
        }
        else if ( CompareUtils.equals ( property, WebDockablePane.CONTENT_PROPERTY ) )
        {
            // Handling content change
            boolean update = false;
            if ( evt.getOldValue () != null )
            {
                // Removing old content
                dockablePane.remove ( ( JComponent ) evt.getOldValue () );
                update = true;
            }
            if ( evt.getNewValue () != null )
            {
                // Adding new content
                dockablePane.add ( ( JComponent ) evt.getNewValue () );
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
     * Installs frame onto this dockable pane.
     *
     * @param frame frame to install
     */
    protected void installFrame ( final WebDockableFrame frame )
    {
        frame.setDockablePane ( dockablePane );
        if ( frame.isPreview () || frame.isDocked () )
        {
            dockablePane.add ( frame );
        }
        if ( frame.isSidebarButtonVisible () )
        {
            dockablePane.add ( frame.getSidebarButton () );
        }
        final PropertyChangeListener frameListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                final String property = evt.getPropertyName ();
                if ( CompareUtils.equals ( property, WebDockableFrame.ID_PROPERTY ) )
                {
                    // Since frame ID changed it is almost the same as adding new one
                    // But we skip actual frame addition into dockable pane since it is already there
                    // We also don't need to remove data for old frame ID, we can keep it
                    dockablePane.getModel ().updateFrame ( dockablePane, frame );
                }
                else if ( CompareUtils.equals ( property, WebDockableFrame.STATE_PROPERTY ) )
                {
                    // Updating frame state
                    final DockableFrameElement element = dockablePane.getModel ().getElement ( frame.getId () );
                    element.setState ( frame.getState () );

                    // Updating frame and its sidebar button visibility
                    boolean update = false;
                    if ( frame.isPreview () || frame.isDocked () )
                    {
                        if ( !dockablePane.contains ( frame ) )
                        {
                            dockablePane.add ( frame );
                            update = true;
                        }
                    }
                    else
                    {
                        if ( dockablePane.contains ( frame ) )
                        {
                            dockablePane.remove ( frame );
                            update = true;
                        }
                    }
                    final JComponent sideBarButton = frame.getSidebarButton ();
                    if ( frame.isSidebarButtonVisible () )
                    {
                        if ( !dockablePane.contains ( sideBarButton ) )
                        {
                            dockablePane.add ( sideBarButton );
                            update = true;
                        }
                    }
                    else
                    {
                        if ( dockablePane.contains ( sideBarButton ) )
                        {
                            dockablePane.remove ( sideBarButton );
                            update = true;
                        }
                    }
                    if ( update )
                    {
                        dockablePane.revalidate ();
                        dockablePane.repaint ();
                    }
                }
                else if ( CompareUtils.equals ( property, WebDockableFrame.RESTORE_STATE_PROPERTY ) )
                {
                    // Updating frame restore state
                    final DockableFrameElement element = dockablePane.getModel ().getElement ( frame.getId () );
                    element.setRestoreState ( frame.getRestoreState () );
                }
                else if ( CompareUtils.equals ( property, WebDockableFrame.POSITION_PROPERTY ) )
                {
                    // todo Should we really do something about this?
                    // todo We can probably move frame to the specified side at global position (like side drop)
                }
            }
        };
        frame.addPropertyChangeListener ( frameListener );
        frame.putClientProperty ( FRAME_PROPERTY_LISTENER, frameListener );
        dockablePane.revalidate ();
        dockablePane.repaint ();
    }

    /**
     * Uninstalls frame from this dockable pane.
     *
     * @param frame frame to uninstall
     */
    private void uninstallFrame ( final WebDockableFrame frame )
    {
        final PropertyChangeListener frameListener = ( PropertyChangeListener ) frame.getClientProperty ( FRAME_PROPERTY_LISTENER );
        if ( frameListener != null )
        {
            frame.putClientProperty ( FRAME_PROPERTY_LISTENER, null );
            frame.removePropertyChangeListener ( frameListener );
        }
        if ( frame.isSidebarButtonVisible () )
        {
            dockablePane.remove ( frame.getSidebarButton () );
        }
        if ( frame.isPreview () || frame.isDocked () )
        {
            dockablePane.remove ( frame );
        }
        frame.setDockablePane ( null );
        dockablePane.revalidate ();
        dockablePane.repaint ();
    }

    @Override
    public JComponent createGlassLayer ()
    {
        return new DockablePaneGlassLayer ( dockablePane );
    }

    @Override
    public Shape provideShape ()
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
}