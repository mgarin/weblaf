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

package com.alee.laf.menu;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.PainterSupport;
import com.alee.managers.style.StyleManager;
import com.alee.utils.CompareUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.laf.Styleable;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import java.awt.*;

/**
 * Custom UI for JPopupMenu component.
 *
 * @author Mikle Garin
 */

public class WebPopupMenuUI extends BasicPopupMenuUI implements SwingConstants, Styleable, ShapeProvider
{
    /**
     * Component painter.
     */
    protected PopupMenuPainter painter;

    /**
     * Runtime variables.
     */
    protected String styleId = null;
    protected PopupMenuWay popupMenuWay = null;

    /**
     * Returns an instance of the WebPopupMenuUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebPopupMenuUI
     */
    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebPopupMenuUI ();
    }

    /**
     * Installs UI in the specified component.
     *
     * @param c component for this UI
     */
    @Override
    public void installUI ( final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Default settings
        SwingUtils.setHandlesEnableStateMark ( popupMenu );

        // Applying skin
        StyleManager.applySkin ( popupMenu );
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.removeSkin ( popupMenu );

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleId ()
    {
        return styleId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyleId ( final String id )
    {
        if ( !CompareUtils.equals ( this.styleId, id ) )
        {
            this.styleId = id;
            StyleManager.applySkin ( popupMenu );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape provideShape ()
    {
        return PainterSupport.getShape ( popupMenu, painter );
    }

    /**
     * Assists popup menu to allow it choose the best position relative to invoker.
     * Its value nullified right after first usage to avoid popup menu display issues in future.
     *
     * @param way approximate popup menu display way
     */
    public void setPopupMenuWay ( final PopupMenuWay way )
    {
        this.popupMenuWay = way;
    }

    /**
     * Returns currently set preferred popup menu display way.
     * It might be null in case menu was just shown and this value wasn't updated afterwards.
     *
     * @return currently set preferred popup menu display way
     */
    public PopupMenuWay getPopupMenuWay ()
    {
        return popupMenuWay;
    }

    /**
     * Returns popup menu painter.
     *
     * @return popup menu painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets popup menu painter.
     * Pass null to remove popup menu painter.
     *
     * @param painter new popup menu painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( popupMenu, new DataRunnable<PopupMenuPainter> ()
        {
            @Override
            public void run ( final PopupMenuPainter newPainter )
            {
                WebPopupMenuUI.this.painter = newPainter;
            }
        }, this.painter, painter, PopupMenuPainter.class, AdaptivePopupMenuPainter.class );
    }

    /**
     * Returns the {@code Popup} that will be responsible for displaying the {@code JPopupMenu}.
     * Also does necessary modifications to popup coordinates in case they are actually required.
     *
     * @param popup JPopupMenu requesting Popup
     * @param x     screen x location Popup is to be shown at
     * @param y     screen y location Popup is to be shown at
     * @return Popup that will show the JPopupMenu
     */
    @Override
    public Popup getPopup ( final JPopupMenu popup, int x, int y )
    {
        // Requesting painter to fix popup position if it is required
        if ( painter != null )
        {
            // Retrieving fixed popup menu location
            final Point fixed = painter.preparePopupMenu ( popup, popup.getInvoker (), x, y );
            if ( fixed != null )
            {
                // Applying fixed coordinates
                x = fixed.x;
                y = fixed.y;
            }
        }

        // Resetting preferred popup menu display way
        popupMenuWay = null;

        // Returning popup
        return super.getPopup ( popup, x, y );
    }

    /**
     * Paints popup menu decorations.
     * The whole painting process is delegated to installed painter class.
     *
     * @param g graphics context
     * @param c popup menu component
     */
    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, SwingUtils.size ( c ), c, this );
        }
    }
}