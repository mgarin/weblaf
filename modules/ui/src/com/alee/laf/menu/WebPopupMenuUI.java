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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.managers.style.*;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Custom UI for {@link JPopupMenu} component.
 *
 * @author Mikle Garin
 */
public class WebPopupMenuUI extends WPopupMenuUI implements  SwingConstants
{
    /**
     * Runtime variables.
     */
    protected transient PopupMenuWay popupMenuWay = null;

    /**
     * Returns an instance of the {@link WebPopupMenuUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebPopupMenuUI}
     */
    @NotNull
    public static ComponentUI createUI ( @NotNull final JComponent c )
    {
        return new WebPopupMenuUI ();
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Installing enabled state handling marker
        SwingUtils.setHandlesEnableStateMark ( popupMenu );

        // Applying skin
        StyleManager.installSkin ( popupMenu );

        // Adjusting style for JPopupMenu that uses default style and has a JMenu as invoker
        // This allows us to customize otherwise unaccessible JPopupMenu created in JMenu's private methods
        // The trick here is that when JMenu changes JPopupMenu invoker it also updates it's UI
        if ( StyleManager.getStyleId ( popupMenu ) == StyleId.popupmenu && popupMenu.getInvoker () instanceof JMenu )
        {
            StyleId.menuPopupMenu.at ( ( JMenu ) popupMenu.getInvoker () ).set ( popupMenu );
        }
    }

    @Override
    public void uninstallUI ( @NotNull final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( popupMenu );

        // Uninstalling enabled state handling marker
        SwingUtils.removeHandlesEnableStateMark ( popupMenu );

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @Override
    public void setPopupMenuWay ( final PopupMenuWay way )
    {
        this.popupMenuWay = way;
    }

    @Override
    public PopupMenuWay getPopupMenuWay ()
    {
        return popupMenuWay;
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
    @NotNull
    @Override
    public Popup getPopup ( @NotNull final JPopupMenu popup, int x, int y )
    {
        // Requesting painter to fix popup position if it is required
        final Painter painter = PainterSupport.getPainter ( popup );
        if ( painter instanceof IPopupMenuPainter )
        {
            // Retrieving fixed popup menu location
            final Point fixed = ( ( IPopupMenuPainter ) painter ).preparePopupMenu ( popup, popup.getInvoker (), x, y );
            if ( fixed != null )
            {
                // Applying fixed coordinates
                x = fixed.x;
                y = fixed.y;
            }
        }

        // Creating actual popup to place menu into
        final Popup p = super.getPopup ( popup, x, y );

        // Configuring actual popup if needed
        if ( painter instanceof IPopupMenuPainter )
        {
            ( ( IPopupMenuPainter ) painter ).configurePopup ( popup, popup.getInvoker (), x, y, p );
        }

        // Returning actual popup
        return p;
    }

    @Override
    public boolean contains ( @NotNull final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, x, y );
    }

    @Override
    public int getBaseline ( @NotNull final JComponent c, final int width, final int height )
    {
        return PainterSupport.getBaseline ( c, this, width, height );
    }

    @NotNull
    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( @NotNull final JComponent c )
    {
        return PainterSupport.getBaselineResizeBehavior ( c, this );
    }

    @Override
    public void paint ( @NotNull final Graphics g, @NotNull final JComponent c )
    {
        PainterSupport.paint ( g, c, this );
    }

    @Nullable
    @Override
    public Dimension getPreferredSize ( @NotNull final JComponent c )
    {
        return null;
    }
}