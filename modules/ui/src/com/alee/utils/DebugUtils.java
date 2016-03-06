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

import com.alee.global.GlobalConstants;

import javax.swing.*;
import java.awt.*;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * This class provides a set of utilities for various code and graphics debug cases.
 *
 * @author Mikle Garin
 */

public final class DebugUtils
{
    /**
     * Debug option.
     */
    public static final Font DEBUG_FONT = new Font ( "Dialog", Font.BOLD, 8 );
    public static final NumberFormat DEBUG_FORMAT = new DecimalFormat ( "#0.00" );

    /**
     * Returns deadlocked threads stack trace.
     *
     * @return deadlocked threads stack trace
     */
    public static String getDeadlockStackTrace ()
    {
        final ThreadMXBean bean = ManagementFactory.getThreadMXBean ();
        final long[] threadIds = bean.findDeadlockedThreads ();
        String trace = null;
        if ( threadIds != null )
        {
            final ThreadInfo[] infos = bean.getThreadInfo ( threadIds );
            trace = "";
            for ( final ThreadInfo info : infos )
            {
                final StackTraceElement[] stack = info.getStackTrace ();
                trace += ExceptionUtils.getStackTrace ( stack ) + ( info != infos[ infos.length - 1 ] ? "\n" : "" );
            }
        }
        return trace;
    }

    /**
     * Initializes time debugging.
     * Call this when you want to start measuring painting time.
     */
    public static void initTimeDebugInfo ()
    {
        if ( GlobalConstants.DEBUG )
        {
            TimeUtils.pinNanoTime ();
        }
    }

    /**
     * Paints time debug information.
     * Call this when you want to paint time debug information.
     *
     * @param g graphics
     */
    public static void paintTimeDebugInfo ( final Graphics g )
    {
        if ( GlobalConstants.DEBUG )
        {
            paintDebugInfoImpl ( ( Graphics2D ) g );
        }
    }

    /**
     * Paints time debug information.
     * Call this when you want to paint time debug information.
     *
     * @param g2d graphics
     */
    public static void paintTimeDebugInfo ( final Graphics2D g2d )
    {
        if ( GlobalConstants.DEBUG )
        {
            paintDebugInfoImpl ( g2d );
        }
    }

    /**
     * Debug information painting method.
     *
     * @param g2d graphics
     */
    private static void paintDebugInfoImpl ( final Graphics2D g2d )
    {
        final double ms = TimeUtils.getPassedNanoTime () / 1000000f;
        final String micro = "" + DEBUG_FORMAT.format ( ms );
        final Rectangle cb = g2d.getClip ().getBounds ();
        final Font font = g2d.getFont ();

        g2d.setFont ( DEBUG_FONT );
        final Object aa = GraphicsUtils.setupAntialias ( g2d );

        final FontMetrics fm = g2d.getFontMetrics ();
        final int w = fm.stringWidth ( micro ) + 4;
        final int h = fm.getHeight ();

        g2d.setPaint ( Color.BLACK );
        g2d.fillRect ( cb.x + cb.width - w, cb.y, w, h );

        g2d.setPaint ( Color.WHITE );
        g2d.drawString ( micro, cb.x + cb.width - w + 2, cb.y + h - fm.getDescent () );

        GraphicsUtils.restoreAntialias ( g2d, aa );
        g2d.setFont ( font );
    }

    /**
     * Paints border debug information.
     * This will display border bounds within the component.
     *
     * @param g graphics
     * @param c component
     */
    public static void paintBorderDebugInfo ( final Graphics g, final JComponent c )
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
    public static void paintBorderDebugInfo ( final Graphics g, final JComponent c, final Color color )
    {
        final Insets margin = c.getInsets ();
        g.setColor ( color );
        g.drawRect ( 0, 0, c.getWidth () - 1, c.getHeight () - 1 );
        g.drawRect ( margin.left, margin.top, c.getWidth () - margin.left - margin.right - 1,
                c.getHeight () - margin.top - margin.bottom - 1 );
    }
}