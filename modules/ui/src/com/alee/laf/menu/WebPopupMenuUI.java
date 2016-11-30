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

import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Custom UI for {@link JPopupMenu} component.
 *
 * @author Mikle Garin
 */

public class WebPopupMenuUI extends WPopupMenuUI implements ShapeSupport, MarginSupport, PaddingSupport, SwingConstants
{
    /**
     * Component painter.
     */
    @DefaultPainter ( PopupMenuPainter.class )
    protected IPopupMenuPainter painter;

    /**
     * Runtime variables.
     */
    protected PopupMenuWay popupMenuWay = null;
    protected Insets margin = null;
    protected Insets padding = null;

    /**
     * Returns an instance of the WebPopupMenuUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebPopupMenuUI
     */
    @SuppressWarnings ("UnusedParameters")
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
        StyleManager.installSkin ( popupMenu );
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
        StyleManager.uninstallSkin ( popupMenu );

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( popupMenu, painter );
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
        PainterSupport.setPainter ( popupMenu, new DataRunnable<IPopupMenuPainter> ()
        {
            @Override
            public void run ( final IPopupMenuPainter newPainter )
            {
                WebPopupMenuUI.this.painter = newPainter;
            }
        }, this.painter, painter, IPopupMenuPainter.class, AdaptivePopupMenuPainter.class );
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

        // Creating actual popup to place menu into
        final Popup p = super.getPopup ( popup, x, y );

        // Configuring actual popup if needed
        if ( painter != null )
        {
            painter.configurePopup ( popup, popup.getInvoker (), x, y, p );
        }

        // Returning actual popup
        return p;
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
        // return PainterSupport.getPreferredSize ( c, painter );
        return null;
    }
}