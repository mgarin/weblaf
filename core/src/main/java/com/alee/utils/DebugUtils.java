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

package com.alee.utils;

import com.alee.laf.StyleConstants;

import javax.swing.*;
import java.awt.*;

/**
 * This class provides a set of utilities for various code and graphics debug cases.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class DebugUtils
{
    /**
     * Initializes time debugging.
     * Call this when you want to start measuring painting time.
     */
    public static void initTimeDebugInfo ()
    {
        if ( StyleConstants.DEBUG )
        {
            TimeUtils.pinNanoTime ();
        }
    }

    /**
     * Paints time debug information.
     * Call this when you want to paint time debug information.
     *
     * @param g graphics
     * @param c component
     */
    public static void paintTimeDebugInfo ( Graphics g, JComponent c )
    {
        if ( StyleConstants.DEBUG )
        {
            paintDebugInfoImpl ( ( Graphics2D ) g, c );
        }
    }

    /**
     * Paints time debug information.
     * Call this when you want to paint time debug information.
     *
     * @param g2d graphics
     * @param c   component
     */
    public static void paintTimeDebugInfo ( Graphics2D g2d, JComponent c )
    {
        if ( StyleConstants.DEBUG )
        {
            paintDebugInfoImpl ( g2d, c );
        }
    }

    /**
     * Debug information painting method.
     *
     * @param g2d graphics
     * @param c   component
     */
    private static void paintDebugInfoImpl ( Graphics2D g2d, JComponent c )
    {
        double ms = TimeUtils.getPassedNanoTime () / 1000000f;
        String micro = "" + StyleConstants.DEBUG_FORMAT.format ( ms );
        Rectangle cb = g2d.getClip ().getBounds ();
        Font font = g2d.getFont ();

        g2d.setFont ( StyleConstants.DEBUG_FONT );
        Object aa = LafUtils.setupAntialias ( g2d );

        FontMetrics fm = g2d.getFontMetrics ();
        int w = fm.stringWidth ( micro ) + 4;
        int h = fm.getHeight ();

        g2d.setPaint ( Color.BLACK );
        g2d.fillRect ( cb.x + cb.width - w, cb.y, w, h );

        g2d.setPaint ( Color.WHITE );
        g2d.drawString ( micro, cb.x + cb.width - w + 2, cb.y + h - fm.getDescent () );

        LafUtils.restoreAntialias ( g2d, aa );
        g2d.setFont ( font );
    }

    /**
     * Paints border debug information.
     * This will display border bounds within the component.
     *
     * @param g graphics
     * @param c component
     */
    public static void paintBorderDebugInfo ( Graphics g, JComponent c )
    {
        paintBorderDebugInfo ( g, c, Color.RED );
    }

    /**
     * Paints border debug information.
     * This will display border bounds within the component.
     *
     * @param g     graphics
     * @param c     component
     * @param color debug shape color
     */
    public static void paintBorderDebugInfo ( Graphics g, JComponent c, Color color )
    {
        Insets margin = c.getInsets ();
        g.setColor ( color );
        g.drawRect ( 0, 0, c.getWidth () - 1, c.getHeight () - 1 );
        g.drawRect ( margin.left, margin.top, c.getWidth () - margin.left - margin.right - 1,
                c.getHeight () - margin.top - margin.bottom - 1 );
    }
}