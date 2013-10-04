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

import com.alee.utils.LafUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPopupMenuSeparatorUI;
import java.awt.*;

/**
 * Custom UI for JPopupMenu.Separator component.
 *
 * @author Mikle Garin
 */

public class WebPopupMenuSeparatorUI extends BasicPopupMenuSeparatorUI
{
    /**
     * Style settings.
     */
    protected Color color = WebPopupMenuSeparatorStyle.color;
    protected Stroke stroke = WebPopupMenuSeparatorStyle.stroke;
    protected int spacing = WebPopupMenuSeparatorStyle.spacing;
    protected int sideSpacing = WebPopupMenuSeparatorStyle.sideSpacing;

    /**
     * Returns an instance of the WebPopupMenuSeparatorUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebPopupMenuSeparatorUI
     */
    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebPopupMenuSeparatorUI ();
    }

    /**
     * Installs UI in the specified component.
     *
     * @param c component for this UI
     */
    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );
        c.setOpaque ( false );
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        c.setOpaque ( true );
        super.uninstallUI ( c );
    }

    /**
     * Paints popup menu separator.
     *
     * @param g graphics context
     * @param c separator component
     */
    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        final Graphics2D g2d = ( Graphics2D ) g;
        final Object aa = LafUtils.setupAntialias ( g2d );
        final Stroke stroke = LafUtils.setupStroke ( g2d, this.stroke );
        g.setColor ( color );
        g.drawLine ( sideSpacing, c.getHeight () / 2, c.getWidth () - sideSpacing - 1, c.getHeight () / 2 );
        LafUtils.restoreStroke ( g2d, stroke );
        LafUtils.restoreAntialias ( g2d, aa );
    }

    /**
     * Returns preferred separator size.
     *
     * @param c separator component
     * @return preferred separator size
     */
    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return new Dimension ( 0, spacing * 2 + 1 );
    }
}