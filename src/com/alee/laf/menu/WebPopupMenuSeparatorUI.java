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

import com.alee.laf.WebLookAndFeel;
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
    @SuppressWarnings ("UnusedParameters")
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
        LookAndFeel.installProperty ( c, WebLookAndFeel.OPAQUE_PROPERTY, Boolean.FALSE );
    }

    /**
     * Returns separator color.
     *
     * @return separator color
     */
    public Color getColor ()
    {
        return color;
    }

    /**
     * Sets separator color.
     *
     * @param color new separator color
     */
    public void setColor ( final Color color )
    {
        this.color = color;
    }

    /**
     * Returns separator stroke.
     *
     * @return separator stroke
     */
    public Stroke getStroke ()
    {
        return stroke;
    }

    /**
     * Sets separator stroke.
     *
     * @param stroke new separator stroke
     */
    public void setStroke ( final Stroke stroke )
    {
        this.stroke = stroke;
    }

    /**
     * Returns separator upper and lower spacing.
     *
     * @return separator upper and lower spacing
     */
    public int getSpacing ()
    {
        return spacing;
    }

    /**
     * Sets separator upper and lower spacing.
     *
     * @param spacing new separator upper and lower spacing
     */
    public void setSpacing ( final int spacing )
    {
        this.spacing = spacing;
    }

    /**
     * Returns separator side spacing.
     *
     * @return separator side spacing
     */
    public int getSideSpacing ()
    {
        return sideSpacing;
    }

    /**
     * Sets separator side spacing.
     *
     * @param sideSpacing new separator side spacing
     */
    public void setSideSpacing ( final int sideSpacing )
    {
        this.sideSpacing = sideSpacing;
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