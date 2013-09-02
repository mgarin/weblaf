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

package com.alee.managers.popup;

import com.alee.extended.painter.NinePatchIconPainter;
import com.alee.extended.painter.Painter;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.HashMap;
import java.util.Map;

/**
 * This manager allows you to add your own popups within the frame bounds (within the window root pane bounds to be exact). These popups
 * could be either modal or not and could be also bound to component. Also there are few specific popup types that are created for buttons
 * and other Swing components and act differently than standart popups.
 *
 * @author Mikle Garin
 * @see PopupLayer
 */

public final class PopupManager
{
    /**
     * Shade layers cache.
     */
    private static Map<Window, ShadeLayer> shadeLayers = new HashMap<Window, ShadeLayer> ();

    /**
     * Popup layers cache.
     */
    private static Map<Window, PopupLayer> popupLayers = new HashMap<Window, PopupLayer> ();

    /**
     * Default style used for popups.
     */
    private static PopupStyle defaultPopupStyle = PopupStyle.bordered;

    /**
     * Style painters cache.
     */
    private static Map<PopupStyle, Painter> stylePainters = new HashMap<PopupStyle, Painter> ();

    /**
     * Hides all visible popups on all cached popup layers.
     */
    public static void hideAllPopups ()
    {
        for ( ShadeLayer layer : shadeLayers.values () )
        {
            layer.hideAllPopups ();
        }
        for ( PopupLayer layer : popupLayers.values () )
        {
            layer.hideAllPopups ();
        }
    }

    /**
     * Hides all visible popups on the specified window.
     *
     * @param window window to process
     */
    public static void hideAllPopups ( final Window window )
    {
        if ( shadeLayers.containsKey ( window ) )
        {
            shadeLayers.get ( window ).hideAllPopups ();
        }
        if ( popupLayers.containsKey ( window ) )
        {
            popupLayers.get ( window ).hideAllPopups ();
        }
    }

    /**
     * Returns default popup style.
     *
     * @return default popup style
     */
    public static PopupStyle getDefaultPopupStyle ()
    {
        return defaultPopupStyle;
    }

    /**
     * Sets default popup style.
     *
     * @param style default popup style
     */
    public static void setDefaultPopupStyle ( final PopupStyle style )
    {
        PopupManager.defaultPopupStyle = style;
    }

    /**
     * Returns default popup painter.
     *
     * @return default popup painter
     */
    public static Painter getDefaultPopupPainter ()
    {
        return getPopupPainter ( defaultPopupStyle );
    }

    /**
     * Returns popup painter for the specified popup style.
     *
     * @param style popup style
     * @return popup painter for the specified popup style
     */
    public static Painter getPopupPainter ( final PopupStyle style )
    {
        Painter painter = stylePainters.get ( style );
        if ( painter == null )
        {
            painter = style == PopupStyle.none ? null :
                    new NinePatchIconPainter ( PopupManager.class.getResource ( "icons/popup/" + style + ".9.png" ) );
            stylePainters.put ( style, painter );
        }
        return painter;
    }

    /**
     * Displays popup on the window containing specified component.
     *
     * @param component component used to determine window on which modal popup will be displayed
     * @param popup     popup to display
     */
    public static void showPopup ( final Component component, final WebPopup popup )
    {
        showPopup ( component, popup, true );
    }

    /**
     * Displays popup on the window containing specified component.
     *
     * @param component     component used to determine window on which modal popup will be displayed
     * @param popup         popup to display
     * @param transferFocus whether to transfer focus to content of the displayed popup or not
     */
    public static void showPopup ( final Component component, final WebPopup popup, final boolean transferFocus )
    {
        Window window = SwingUtils.getWindowAncestor ( component );
        if ( window != null )
        {
            showPopup ( window, popup, transferFocus );
        }
    }

    /**
     * Displays popup on the specified window.
     *
     * @param window        window used to display popup
     * @param popup         popup to display
     * @param transferFocus whether to transfer focus to content of the displayed popup or not
     */
    public static void showPopup ( final Window window, final WebPopup popup, final boolean transferFocus )
    {
        // Hiding all modal and simple popups inside window
        // hideAllPopups ( window );

        // Displaying new modal popup
        getPopupLayer ( window ).showPopup ( popup );

        // Transfering focus to first focusable component in the popup
        if ( transferFocus )
        {
            popup.transferFocus ();
        }
    }

    /**
     * Returns cached popup layer for window containing specified component.
     *
     * @param component component used to determine window for popup layer
     * @return cached popup layer for window containing specified component
     */
    public static PopupLayer getPopupLayer ( final Component component )
    {
        return getPopupLayer ( SwingUtils.getWindowAncestor ( component ) );
    }

    /**
     * Returns cached popup layer for the specified window.
     *
     * @param window window for popup layer
     * @return cached popup layer for the specified window
     */
    public static PopupLayer getPopupLayer ( final Window window )
    {
        if ( popupLayers.containsKey ( window ) )
        {
            return popupLayers.get ( window );
        }
        else
        {
            final JLayeredPane layeredPane = SwingUtils.getLayeredPane ( window );
            if ( layeredPane == null )
            {
                throw new IllegalArgumentException ( "Popup layer can be installed only into window containing JLayeredPane" );
            }

            final PopupLayer popupLayer = new PopupLayer ();
            installPopupLayer ( popupLayer, window, layeredPane );
            popupLayers.put ( window, popupLayer );

            return popupLayer;
        }
    }

    /**
     * Displays popup as modal on the window containing specified component.
     *
     * @param component component used to determine window on which modal popup will be displayed
     * @param popup     popup to display
     * @param hfill     whether popup should fill the whole available window width or not
     * @param vfill     whether popup should fill the whole available window height or not
     */
    public static void showModalPopup ( final Component component, final WebPopup popup, final boolean hfill, final boolean vfill )
    {
        Window window = SwingUtils.getWindowAncestor ( component );
        if ( window != null )
        {
            showModalPopup ( window, popup, hfill, vfill );
        }
    }

    /**
     * Displays popup as modal on the specified window.
     *
     * @param window window used to display modal popup
     * @param popup  popup to display
     * @param hfill  whether popup should fill the whole available window width or not
     * @param vfill  whether popup should fill the whole available window height or not
     */
    public static void showModalPopup ( final Window window, final WebPopup popup, final boolean hfill, final boolean vfill )
    {
        // Hiding all modal and simple popups inside window
        hideAllPopups ( window );

        // Displaying new modal popup
        getShadeLayer ( window ).showPopup ( popup, hfill, vfill );

        // Transfering focus to first focusable component in the popup
        popup.transferFocus ();
    }

    /**
     * Returns cached shade layer for the specified window.
     *
     * @param window window for the shade layer
     * @return cached shade layer for the specified window
     */
    private static ShadeLayer getShadeLayer ( final Window window )
    {
        if ( shadeLayers.containsKey ( window ) )
        {
            return shadeLayers.get ( window );
        }
        else
        {
            final JLayeredPane layeredPane = SwingUtils.getLayeredPane ( window );
            if ( layeredPane == null )
            {
                throw new IllegalArgumentException ( "Popup layer can be installed only into window containing JLayeredPane" );
            }

            final ShadeLayer shadeLayer = new ShadeLayer ();
            installPopupLayer ( shadeLayer, window, layeredPane );
            shadeLayers.put ( window, shadeLayer );

            return shadeLayer;
        }
    }

    /**
     * Installs popup layer into the specified window.
     *
     * @param popupLayer  popup layer to install
     * @param window      window into which popup layer should be installed
     * @param layeredPane window's layered pane
     */
    private static void installPopupLayer ( final PopupLayer popupLayer, final Window window, final JLayeredPane layeredPane )
    {
        layeredPane.add ( popupLayer, JLayeredPane.PALETTE_LAYER );
        layeredPane.addComponentListener ( new ComponentAdapter ()
        {
            @Override
            public void componentResized ( ComponentEvent e )
            {
                popupLayer.setBounds ( 0, 0, layeredPane.getWidth (), layeredPane.getHeight () );
                popupLayer.revalidate ();
            }
        } );
        window.addWindowStateListener ( new WindowStateListener ()
        {
            @Override
            public void windowStateChanged ( WindowEvent e )
            {
                popupLayer.setBounds ( 0, 0, layeredPane.getWidth (), layeredPane.getHeight () );
                popupLayer.revalidate ();
            }
        } );
    }
}