package com.alee.managers.style.skin.web;

import com.alee.laf.rootpane.IRootPanePainter;
import com.alee.laf.rootpane.WebRootPaneUI;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.NinePatchUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.ninepatch.NinePatchIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowStateListener;
import java.awt.geom.RoundRectangle2D;

/**
 * @author Alexandr Zernov
 * @author Mikle Garin
 */

public class WebRootPanePainter<E extends JRootPane, U extends WebRootPaneUI> extends AbstractDecorationPainter<E, U>
        implements IRootPanePainter<E, U>
{
    /**
     * Style settings.
     */
    protected boolean paintWatermark;
    protected ImageIcon watermark;
    protected int inactiveShadeWidth;
    protected Color topBg;
    protected Color middleBg;
    protected Color innerBorderColor;

    /**
     * Listeners.
     */
    protected WindowFocusListener windowFocusListener;
    protected WindowStateListener windowStateListener;

    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Installing window-related settings
        final Window window = SwingUtils.getWindowAncestor ( c );
        if ( window != null )
        {
            // Enabling window decorations
            if ( isUndecorated () )
            {
                disableWindowDecoration ( c, window );
            }
            else
            {
                enableWindowDecoration ( c, window );
            }

            // Window focus change listener
            windowFocusListener = new WindowFocusListener ()
            {
                @Override
                public void windowGainedFocus ( final WindowEvent e )
                {
                    if ( ui.isDecorated () )
                    {
                        repaint ();
                    }
                }

                @Override
                public void windowLostFocus ( final WindowEvent e )
                {
                    if ( ui.isDecorated () )
                    {
                        repaint ();
                    }
                }
            };
            window.addWindowFocusListener ( windowFocusListener );

            // Window state change listener
            if ( window instanceof Frame )
            {
                windowStateListener = new WindowStateListener ()
                {
                    @Override
                    public void windowStateChanged ( final WindowEvent e )
                    {
                        if ( ui.isDecorated () )
                        {
                            updateBorder ();
                        }
                    }
                };
                window.addWindowStateListener ( windowStateListener );
            }
        }
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Uninstalling window-related settings
        final Window window = SwingUtils.getWindowAncestor ( c );
        if ( window != null )
        {
            // Disabling window decorations
            if ( !isUndecorated () )
            {
                disableWindowDecoration ( c, window );
            }

            // Removing listeners
            if ( window instanceof Frame )
            {
                window.removeWindowStateListener ( windowStateListener );
                windowStateListener = null;
            }
            window.removeWindowFocusListener ( windowFocusListener );
            windowFocusListener = null;
        }

        super.uninstall ( c, ui );
    }

    /**
     * Enables root pane window decoration.
     *
     * @param c      root pane
     * @param window window to enable decoration for
     */
    protected void enableWindowDecoration ( final E c, final Window window )
    {
        if ( !window.isDisplayable () )
        {
            if ( window instanceof Frame )
            {
                ( ( Frame ) window ).setUndecorated ( true );
                c.setWindowDecorationStyle ( JRootPane.FRAME );
            }
            else if ( window instanceof Dialog )
            {
                ( ( Dialog ) window ).setUndecorated ( true );
                c.setWindowDecorationStyle ( JRootPane.PLAIN_DIALOG );
            }
        }
    }

    /**
     * Disables root pane window decoration.
     *
     * @param c      root pane
     * @param window window to disable decoration for
     */
    protected void disableWindowDecoration ( final E c, final Window window )
    {
        if ( !window.isDisplayable () )
        {
            if ( window instanceof Frame )
            {
                ( ( Frame ) window ).setUndecorated ( false );
                c.setWindowDecorationStyle ( JRootPane.NONE );
            }
            else if ( window instanceof Dialog )
            {
                ( ( Dialog ) window ).setUndecorated ( false );
                c.setWindowDecorationStyle ( JRootPane.NONE );
            }
        }
    }

    @Override
    public Boolean isOpaque ()
    {
        return !ui.isDecorated ();
    }

    @Override
    public Insets getBorders ()
    {
        // todo Maybe keep decorated flag the same?
        return ui.isDecorated () ? super.getBorders () : null;
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        // todo Merge with decoration
        if ( ui.isDecorated () )
        {
            final Object aa = GraphicsUtils.setupAntialias ( g2d );
            final boolean max = ui.isMaximized ();

            if ( max )
            {
                // Background
                g2d.setPaint ( new GradientPaint ( 0, 0, topBg, 0, 30, middleBg ) );
                g2d.fillRect ( 0, 0, c.getWidth (), c.getHeight () );

                // Border
                g2d.setPaint ( borderColor );
                g2d.drawRect ( 0, 0, c.getWidth () - 1, c.getHeight () - 1 );
                g2d.setPaint ( innerBorderColor );
                g2d.drawRect ( 1, 1, c.getWidth () - 3, c.getHeight () - 3 );

                // Watermark
                if ( paintWatermark && watermark != null )
                {
                    final Shape old = GraphicsUtils.intersectClip ( g2d, getWatermarkClip ( c ) );
                    g2d.drawImage ( watermark.getImage (), 2, 2, null );
                    GraphicsUtils.restoreClip ( g2d, old );
                }
            }
            else
            {
                // Shade
                if ( shadeWidth > 0 )
                {
                    final int diff = isActive ( c ) ? 0 : shadeWidth - inactiveShadeWidth;
                    getShadeIcon ( c ).paintIcon ( g2d, diff, diff, c.getWidth () - diff * 2, c.getHeight () - diff * 2 );
                }

                // Background
                g2d.setPaint ( new GradientPaint ( 0, shadeWidth, topBg, 0, shadeWidth + 30, middleBg ) );
                g2d.fillRoundRect ( shadeWidth, shadeWidth, c.getWidth () - shadeWidth * 2, c.getHeight () - shadeWidth * 2, round * 2,
                        round * 2 );

                // Border
                g2d.setPaint ( borderColor );
                g2d.drawRoundRect ( shadeWidth, shadeWidth, c.getWidth () - shadeWidth * 2 - 1, c.getHeight () - shadeWidth * 2 - 1,
                        round * 2 - 2, round * 2 - 2 );
                g2d.setPaint ( innerBorderColor );
                g2d.drawRoundRect ( shadeWidth + 1, shadeWidth + 1, c.getWidth () - shadeWidth * 2 - 3, c.getHeight () - shadeWidth * 2 - 3,
                        round * 2 - 4, round * 2 - 4 );

                // Watermark
                if ( paintWatermark && watermark != null )
                {
                    final Shape old = GraphicsUtils.intersectClip ( g2d, getWatermarkClip ( c ) );
                    g2d.drawImage ( watermark.getImage (), shadeWidth + 2, shadeWidth + 2, null );
                    GraphicsUtils.restoreClip ( g2d, old );
                }
            }

            GraphicsUtils.restoreAntialias ( g2d, aa );
        }
    }

    /**
     * Returns current decoration shade width.
     *
     * @param c root pane
     * @return current decoration shade width
     */
    protected int getShadeWidth ( final E c )
    {
        return isActive ( c ) ? shadeWidth : inactiveShadeWidth;
    }

    /**
     * Returns decoration shade icon.
     *
     * @param c root pane
     * @return decoration shade icon
     */
    protected NinePatchIcon getShadeIcon ( final E c )
    {
        if ( shadeWidth > 0 )
        {
            return NinePatchUtils.getShadeIcon ( getShadeWidth ( c ), round, 0.8f );
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns watermark clip shape.
     *
     * @param c root pane
     * @return watermark clip shape
     */
    protected Shape getWatermarkClip ( final E c )
    {
        return new RoundRectangle2D.Double ( shadeWidth + 2, shadeWidth + 2, c.getWidth () - shadeWidth * 2 - 3,
                c.getHeight () - shadeWidth * 2 - 3, round * 2 - 4, round * 2 - 4 );
    }

    /**
     * Returns whether or not root pane window is currently active.
     *
     * @param c root pane
     * @return true if root pane window is currently active, false otherwise
     */
    protected boolean isActive ( final E c )
    {
        return SwingUtils.getWindowAncestor ( c ).isFocused ();
    }
}