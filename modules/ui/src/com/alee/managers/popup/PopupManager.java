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

import com.alee.api.jdk.BiConsumer;
import com.alee.api.jdk.Function;
import com.alee.managers.style.StyleId;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.swing.WeakComponentData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

/**
 * This manager allows you to add your own popups within the window/applet root pane bounds.
 *
 * @author Mikle Garin
 * @see com.alee.managers.popup.PopupLayer
 * @see com.alee.managers.popup.WebInnerPopup
 * @see com.alee.managers.popup.WebButtonPopup
 * @see com.alee.managers.notification.WebInnerNotification
 */
public final class PopupManager
{
    /**
     * Shade layers cache.
     */
    protected static final WeakComponentData<JComponent, ShadeLayer> shadeLayers =
            new WeakComponentData<JComponent, ShadeLayer> ( "PopupManager.ShadeLayer", 3 );

    /**
     * Popup layers cache.
     */
    protected static final WeakComponentData<JComponent, PopupLayer> popupLayers =
            new WeakComponentData<JComponent, PopupLayer> ( "PopupManager.PopupLayer", 3 );

    /**
     * Default style used for popups.
     */
    protected static StyleId defaultPopupStyleId = StyleId.innerpopup;

    /**
     * Hides all visible popups on all cached popup layers.
     */
    public static void hideAllPopups ()
    {
        shadeLayers.forEach ( new BiConsumer<JComponent, ShadeLayer> ()
        {
            @Override
            public void accept ( final JComponent component, final ShadeLayer shadeLayer )
            {
                shadeLayer.hideAllPopups ();
            }
        } );
        popupLayers.forEach ( new BiConsumer<JComponent, PopupLayer> ()
        {
            @Override
            public void accept ( final JComponent component, final PopupLayer popupLayer )
            {
                popupLayer.hideAllPopups ();
            }
        } );
    }

    /**
     * Hides all visible popups for the root pane under the specified component.
     *
     * @param component component to process
     */
    public static void hideAllPopups ( final JComponent component )
    {
        hideAllPopups ( CoreSwingUtils.getRootPane ( component ) );
    }

    /**
     * Hides all visible popups for the specified root pane.
     *
     * @param rootPane root pane to process
     */
    public static void hideAllPopups ( final JRootPane rootPane )
    {
        if ( shadeLayers.contains ( rootPane ) )
        {
            shadeLayers.get ( rootPane ).hideAllPopups ();
        }
        if ( popupLayers.contains ( rootPane ) )
        {
            popupLayers.get ( rootPane ).hideAllPopups ();
        }
    }

    /**
     * Returns default popup style.
     *
     * @return default popup style
     */
    public static StyleId getDefaultPopupStyleId ()
    {
        return defaultPopupStyleId;
    }

    /**
     * Sets default popup style.
     *
     * @param id default popup style
     */
    public static void setDefaultPopupStyleId ( final StyleId id )
    {
        PopupManager.defaultPopupStyleId = id;
    }

    /**
     * Displays popup for the root pane containing specified component.
     *
     * @param component component used to determine root pane for which modal popup will be displayed
     * @param popup     popup to display
     */
    public static void showPopup ( final Component component, final WebInnerPopup popup )
    {
        showPopup ( component, popup, true );
    }

    /**
     * Displays popup for the root pane containing specified component.
     *
     * @param component     component used to determine root pane for which modal popup will be displayed
     * @param popup         popup to display
     * @param transferFocus whether to transfer focus to content of the displayed popup or not
     */
    public static void showPopup ( final Component component, final WebInnerPopup popup, final boolean transferFocus )
    {
        final JRootPane rootPane = CoreSwingUtils.getRootPane ( component );
        if ( rootPane != null )
        {
            showPopup ( rootPane, popup, transferFocus );
        }
    }

    /**
     * Displays popup for the specified root pane.
     *
     * @param rootPane      root pane used to display popup
     * @param popup         popup to display
     * @param transferFocus whether to transfer focus to content of the displayed popup or not
     */
    public static void showPopup ( final JRootPane rootPane, final WebInnerPopup popup, final boolean transferFocus )
    {
        // Displaying new modal popup
        getPopupLayer ( rootPane ).showPopup ( popup );

        // Transferring focus to first focusable component in the popup
        if ( transferFocus )
        {
            popup.transferFocus ();
        }
    }

    /**
     * Displays popup as modal for the root pane containing specified component.
     *
     * @param component component used to determine root pane for which modal popup will be displayed
     * @param popup     popup to display
     * @param hfill     whether popup should fill the whole available width or not
     * @param vfill     whether popup should fill the whole available height or not
     */
    public static void showModalPopup ( final Component component, final WebInnerPopup popup, final boolean hfill, final boolean vfill )
    {
        showModalPopup ( component, popup, hfill, vfill, false );
    }

    /**
     * Displays popup as modal for the root pane containing specified component.
     *
     * @param component  component used to determine root pane for which modal popup will be displayed
     * @param popup      popup to display
     * @param hfill      whether popup should fill the whole available width or not
     * @param vfill      whether popup should fill the whole available height or not
     * @param blockClose whether or not popup close attemps should be blocked or not
     */
    public static void showModalPopup ( final Component component, final WebInnerPopup popup, final boolean hfill, final boolean vfill,
                                        final boolean blockClose )
    {
        final JRootPane rootPane = CoreSwingUtils.getRootPane ( component );
        if ( rootPane != null )
        {
            showModalPopup ( rootPane, popup, hfill, vfill, blockClose );
        }
    }

    /**
     * Displays popup as modal for the specified root pane.
     *
     * @param rootPane root pane used to display modal popup
     * @param popup    popup to display
     * @param hfill    whether popup should fill the whole available width or not
     * @param vfill    whether popup should fill the whole available height or not
     */
    public static void showModalPopup ( final JRootPane rootPane, final WebInnerPopup popup, final boolean hfill, final boolean vfill )
    {
        showModalPopup ( rootPane, popup, hfill, vfill, false );
    }

    /**
     * Displays popup as modal for the specified root pane.
     *
     * @param rootPane   root pane used to display modal popup
     * @param popup      popup to display
     * @param hfill      whether popup should fill the whole available width or not
     * @param vfill      whether popup should fill the whole available height or not
     * @param blockClose whether or not popup close attemps should be blocked or not
     */
    public static void showModalPopup ( final JRootPane rootPane, final WebInnerPopup popup, final boolean hfill, final boolean vfill,
                                        final boolean blockClose )
    {
        // Hiding all modal and simple popups inside root pane
        hideAllPopups ( rootPane );

        // Displaying new modal popup
        final ShadeLayer shadeLayer = getShadeLayer ( rootPane );
        shadeLayer.setBlockClose ( blockClose );
        shadeLayer.showPopup ( popup, hfill, vfill );

        // Transferring focus to first focusable component in the popup
        popup.transferFocus ();
    }

    /**
     * Returns cached popup layer for root pane containing specified component.
     *
     * @param component component used to determine root pane for popup layer
     * @return cached popup layer for root pane containing specified component
     */
    public static PopupLayer getPopupLayer ( final Component component )
    {
        return getPopupLayer ( CoreSwingUtils.getRootPane ( component ) );
    }

    /**
     * Returns cached popup layer for the specified root pane.
     *
     * @param rootPane root pane for popup layer
     * @return cached popup layer for the specified root pane
     */
    public static PopupLayer getPopupLayer ( final JRootPane rootPane )
    {
        if ( rootPane != null )
        {
            return popupLayers.get ( rootPane, new Function<JComponent, PopupLayer> ()
            {
                @Override
                public PopupLayer apply ( final JComponent component )
                {
                    final PopupLayer popupLayer;
                    final JLayeredPane layeredPane = rootPane.getLayeredPane ();
                    if ( layeredPane != null )
                    {
                        popupLayer = new PopupLayer ();
                        installPopupLayer ( popupLayer, rootPane, layeredPane );
                    }
                    else
                    {
                        throw new RuntimeException ( "PopupLayer can be installed only into window or applet with JLayeredPane" );
                    }
                    return popupLayer;
                }
            } );
        }
        else
        {
            throw new RuntimeException ( "JRootPane for PopupLayer was not specified" );
        }
    }

    /**
     * Returns cached shade layer for the specified root pane.
     *
     * @param rootPane root pane for the shade layer
     * @return cached shade layer for the specified root pane
     */
    public static ShadeLayer getShadeLayer ( final JRootPane rootPane )
    {
        if ( rootPane != null )
        {
            return shadeLayers.get ( rootPane, new Function<JComponent, ShadeLayer> ()
            {
                @Override
                public ShadeLayer apply ( final JComponent component )
                {
                    final ShadeLayer shadeLayer;
                    final JLayeredPane layeredPane = rootPane.getLayeredPane ();
                    if ( layeredPane == null )
                    {
                        shadeLayer = new ShadeLayer ();
                        installPopupLayer ( shadeLayer, rootPane, layeredPane );
                    }
                    else
                    {
                        throw new RuntimeException ( "ShadeLayer can be installed only into window or applet with JLayeredPane" );
                    }
                    return shadeLayer;
                }
            } );
        }
        else
        {
            throw new RuntimeException ( "JRootPane for ShadeLayer was not specified" );
        }
    }

    /**
     * Installs popup layer for the specified root pane.
     *
     * @param popupLayer  popup layer to install
     * @param rootPane    root pane for which popup layer should be installed
     * @param layeredPane window's layered pane
     */
    protected static void installPopupLayer ( final PopupLayer popupLayer, final JRootPane rootPane, final JLayeredPane layeredPane )
    {
        popupLayer.setBounds ( 0, 0, layeredPane.getWidth (), layeredPane.getHeight () );
        popupLayer.setVisible ( true );
        layeredPane.add ( popupLayer, JLayeredPane.PALETTE_LAYER );
        layeredPane.revalidate ();

        layeredPane.addComponentListener ( new ComponentAdapter ()
        {
            @Override
            public void componentResized ( final ComponentEvent e )
            {
                popupLayer.setBounds ( 0, 0, layeredPane.getWidth (), layeredPane.getHeight () );
                popupLayer.revalidate ();
            }
        } );

        final Window window = CoreSwingUtils.getWindowAncestor ( rootPane );
        if ( window != null )
        {
            window.addWindowStateListener ( new WindowStateListener ()
            {
                @Override
                public void windowStateChanged ( final WindowEvent e )
                {
                    popupLayer.setBounds ( 0, 0, layeredPane.getWidth (), layeredPane.getHeight () );
                    popupLayer.revalidate ();
                }
            } );
        }
    }
}