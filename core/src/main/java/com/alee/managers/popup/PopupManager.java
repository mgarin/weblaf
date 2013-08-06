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
import java.util.HashMap;
import java.util.Map;

/**
 * User: mgarin Date: 13.07.11 Time: 17:19
 * <p/>
 * This manager allows you to add your own popups within the frame bounds (within the window root pane bounds to be exact). These popups
 * could be either modal or not and could be also bound to component. Also there are few specific popup types that are created for buttons
 * and other Swing components and act differently than standart popups.
 */

public class PopupManager
{
    private static Map<Window, ShadeLayer> shadeLayers = new HashMap<Window, ShadeLayer> ();
    private static Map<Window, PopupLayer> popupLayers = new HashMap<Window, PopupLayer> ();

    private static PopupStyle defaultPopupStyle = PopupStyle.bordered;

    /**
     * Popup hide methods
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

    public static void hideAllPopups ( Window window )
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
     * Popup style methods
     */

    private static Map<PopupStyle, Painter> stylePainters = new HashMap<PopupStyle, Painter> ();

    public static PopupStyle getDefaultPopupStyle ()
    {
        return defaultPopupStyle;
    }

    public static void setDefaultPopupStyle ( PopupStyle defaultPopupStyle )
    {
        PopupManager.defaultPopupStyle = defaultPopupStyle;
    }

    public static Painter getPopupPainter ()
    {
        return getPopupPainter ( defaultPopupStyle );
    }

    public static Painter getPopupPainter ( PopupStyle popupStyle )
    {
        if ( !stylePainters.containsKey ( popupStyle ) )
        {
            if ( popupStyle.equals ( PopupStyle.none ) )
            {
                stylePainters.put ( PopupStyle.none, null );
            }
            else
            {
                stylePainters.put ( popupStyle,
                        new NinePatchIconPainter ( PopupManager.class.getResource ( "icons/popup/" + popupStyle + ".9.png" ) ) );
            }
        }
        return stylePainters.get ( popupStyle );
    }

    /**
     * Modal popup methods
     */

    public static void showModalPopup ( Component component, WebPopup popup, boolean hfill, boolean vfill )
    {
        Window window = SwingUtils.getWindowAncestor ( component );
        if ( window != null )
        {
            showModalPopup ( window, popup, hfill, vfill );
        }
    }

    public static void showModalPopup ( Window window, WebPopup popup, boolean hfill, boolean vfill )
    {
        // Hiding all modal and simple popups inside window
        hideAllPopups ( window );

        // Displaying new modal popup
        getShadeLayer ( window ).showPopup ( popup, hfill, vfill );

        // Transfering focus to first focusable component in the popup
        popup.transferFocus ();
    }

    private static ShadeLayer getShadeLayer ( Window window )
    {
        if ( shadeLayers.containsKey ( window ) )
        {
            return shadeLayers.get ( window );
        }
        else
        {
            final ShadeLayer shadeLayer = new ShadeLayer ();
            if ( window instanceof JDialog )
            {
                final JDialog dialog = ( JDialog ) window;
                dialog.getLayeredPane ().add ( shadeLayer, JLayeredPane.PALETTE_LAYER );
                dialog.getLayeredPane ().addComponentListener ( new ComponentAdapter ()
                {
                    public void componentResized ( ComponentEvent e )
                    {
                        shadeLayer.setBounds ( 0, 0, dialog.getLayeredPane ().getWidth (), dialog.getLayeredPane ().getHeight () );
                    }
                } );
            }
            else if ( window instanceof JFrame )
            {
                final JFrame frame = ( JFrame ) window;
                frame.getLayeredPane ().add ( shadeLayer, JLayeredPane.PALETTE_LAYER );
                frame.getLayeredPane ().addComponentListener ( new ComponentAdapter ()
                {
                    public void componentResized ( ComponentEvent e )
                    {
                        shadeLayer.setBounds ( 0, 0, frame.getLayeredPane ().getWidth (), frame.getLayeredPane ().getHeight () );
                    }
                } );
            }
            shadeLayers.put ( window, shadeLayer );
            return shadeLayer;
        }
    }

    /**
     * Simple popup methods
     */

    public static void showPopup ( Component component, WebPopup popup )
    {
        showPopup ( component, popup, true );
    }

    public static void showPopup ( Component component, WebPopup popup, boolean transferFocus )
    {
        Window window = SwingUtils.getWindowAncestor ( component );
        if ( window != null )
        {
            showPopup ( window, popup, transferFocus );
        }
    }

    public static void showPopup ( Window window, WebPopup popup, boolean transferFocus )
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

    private static PopupLayer getPopupLayer ( Window window )
    {
        if ( popupLayers.containsKey ( window ) )
        {
            return popupLayers.get ( window );
        }
        else
        {
            final PopupLayer popupLayer = new PopupLayer ();
            if ( window instanceof JDialog )
            {
                final JDialog dialog = ( JDialog ) window;
                dialog.getLayeredPane ().add ( popupLayer, JLayeredPane.PALETTE_LAYER );
                dialog.getLayeredPane ().addComponentListener ( new ComponentAdapter ()
                {
                    public void componentResized ( ComponentEvent e )
                    {
                        popupLayer.setBounds ( 0, 0, dialog.getLayeredPane ().getWidth (), dialog.getLayeredPane ().getHeight () );
                    }
                } );
            }
            else if ( window instanceof JFrame )
            {
                final JFrame frame = ( JFrame ) window;
                frame.getLayeredPane ().add ( popupLayer, JLayeredPane.PALETTE_LAYER );
                frame.getLayeredPane ().addComponentListener ( new ComponentAdapter ()
                {
                    public void componentResized ( ComponentEvent e )
                    {
                        popupLayer.setBounds ( 0, 0, frame.getLayeredPane ().getWidth (), frame.getLayeredPane ().getHeight () );
                    }
                } );
            }
            else if ( window instanceof JWindow )
            {
                final JWindow jwindow = ( JWindow ) window;
                jwindow.getLayeredPane ().add ( popupLayer, JLayeredPane.PALETTE_LAYER );
                jwindow.getLayeredPane ().addComponentListener ( new ComponentAdapter ()
                {
                    public void componentResized ( ComponentEvent e )
                    {
                        popupLayer.setBounds ( 0, 0, jwindow.getLayeredPane ().getWidth (), jwindow.getLayeredPane ().getHeight () );
                    }
                } );
            }
            popupLayers.put ( window, popupLayer );
            return popupLayer;
        }
    }
}
