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

package com.alee.laf.scroll;

import com.alee.global.StyleConstants;
import com.alee.painter.AbstractPainter;
import com.alee.utils.ColorUtils;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.MathUtils;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Basic painter for JScrollBar component.
 * It is used as WebScrollBarUI default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */

public class ScrollBarPainter<E extends JScrollBar, U extends WebScrollBarUI> extends AbstractPainter<E, U>
        implements IScrollBarPainter<E, U>
{
    /**
     * Style settings.
     */
    protected Integer thumbRound;
    protected Insets thumbMargin;
    protected Integer scrollBarWidth;
    protected Color trackBorderColor;
    protected Color trackBackgroundColor;
    protected Color thumbBorderColor;
    protected Color thumbBackgroundColor;
    protected Color thumbDisabledBorderColor;
    protected Color thumbDisabledBackgroundColor;
    protected Color thumbRolloverBorderColor;
    protected Color thumbRolloverBackgroundColor;
    protected Color thumbPressedBorderColor;
    protected Color thumbPressedBackgroundColor;

    /**
     * Listeners.
     */
    protected MouseAdapter mouseAdapter;

    /**
     * Runtime variables.
     */
    protected boolean animated;
    protected WebTimer rolloverAnimator;
    protected float rolloverState;
    protected boolean rollover;
    protected boolean pressed;
    protected boolean dragged;

    /**
     * Painting variables.
     */
    protected Rectangle trackBounds;
    protected Rectangle thumbBounds;
    protected Insets thumbMarginR;
    protected Insets thumbMarginHL;
    protected Insets thumbMarginHR;

    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // This styling is animated
        animated = true;

        // Mouse listener
        mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
            {
                setPressed ( true );
            }

            @Override
            public void mouseReleased ( final MouseEvent e )
            {
                setPressed ( false );
            }

            @Override
            public void mouseEntered ( final MouseEvent e )
            {
                setRollover ( thumbBounds.contains ( e.getPoint () ) );
            }

            @Override
            public void mouseMoved ( final MouseEvent e )
            {
                setRollover ( thumbBounds.contains ( e.getPoint () ) );
            }

            @Override
            public void mouseExited ( final MouseEvent e )
            {
                setRollover ( false );
            }
        };
        component.addMouseListener ( mouseAdapter );
        component.addMouseMotionListener ( mouseAdapter );
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Removing listeners
        component.removeMouseListener ( mouseAdapter );
        component.removeMouseMotionListener ( mouseAdapter );

        super.uninstall ( c, ui );
    }

    /**
     * Returns whether scroll bar thumb is in rollover state or not.
     *
     * @return true if scroll bar thumb is in rollover state, false otherwise
     */
    public boolean isRollover ()
    {
        return rollover;
    }

    /**
     * Sets whether scroll bar thumb is in rollover state or not.
     *
     * @param rollover whether scroll bar thumb is in rollover state or not
     */
    public void setRollover ( final boolean rollover )
    {
        if ( this.rollover != rollover )
        {
            this.rollover = rollover;
            if ( animated )
            {
                if ( rollover )
                {
                    if ( rolloverAnimator != null )
                    {
                        rolloverAnimator.stop ();
                    }
                    repaintThumb ();
                }
                else
                {
                    if ( rolloverAnimator == null )
                    {
                        rolloverAnimator = new WebTimer ( StyleConstants.fps36, new ActionListener ()
                        {
                            @Override
                            public void actionPerformed ( final ActionEvent e )
                            {
                                if ( rolloverState > 0f )
                                {
                                    rolloverState -= 0.1f;
                                    repaintThumb ();
                                }
                                else
                                {
                                    rolloverState = 0f;
                                    rolloverAnimator.stop ();
                                }
                            }
                        } );
                    }
                    rolloverState = 1f;
                    rolloverAnimator.start ();
                }
            }
            else
            {
                rolloverState = rollover ? 1f : 0f;
                repaintThumb ();
            }
        }
    }

    /**
     * Returns whether scroll bar thumb is pressed or not.
     *
     * @return true if scroll bar thumb is pressed, false otherwise
     */
    public boolean isPressed ()
    {
        return pressed;
    }

    /**
     * Sets whether scroll bar thumb is pressed or not.
     *
     * @param pressed whether scroll bar thumb is pressed or not
     */
    public void setPressed ( final boolean pressed )
    {
        if ( this.pressed != pressed )
        {
            this.pressed = pressed;
            repaintThumb ();
        }
    }

    @Override
    public void setDragged ( final boolean dragged )
    {
        this.dragged = dragged;
    }

    @Override
    public void setTrackBounds ( final Rectangle bounds )
    {
        this.trackBounds = bounds;
    }

    @Override
    public void setThumbBounds ( final Rectangle bounds )
    {
        this.thumbBounds = bounds;
    }

    @Override
    public Boolean isOpaque ()
    {
        return this.ui.isPaintTrack ();
    }

    @Override
    public Insets getBorders ()
    {
        if ( ui.isPaintTrack () )
        {
            // Additional 1px border at scroll bar side
            // Orientation will be taken into account by the UI itself
            final boolean hor = component.getOrientation () == Adjustable.HORIZONTAL;
            return i ( hor ? 1 : 0, hor ? 0 : 1, 0, 0 );
        }
        else
        {
            return super.getBorders ();
        }
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E scrollbar, final U ui )
    {
        final Object aa = GraphicsUtils.setupAntialias ( g2d );
        paintBackground ( g2d, scrollbar, bounds );
        paintTrack ( g2d, scrollbar, trackBounds );
        paintThumb ( g2d, scrollbar, thumbBounds );
        GraphicsUtils.restoreAntialias ( g2d, aa );
    }

    /**
     * Paints scroll bar background.
     * Background area includes the space under arrow buttons.
     *
     * @param g2d       graphics context
     * @param scrollbar scroll bar
     * @param bounds    scroll bar bounds
     */
    @SuppressWarnings ("UnusedParameters")
    protected void paintBackground ( final Graphics2D g2d, final E scrollbar, final Rectangle bounds )
    {
        if ( ui.isPaintTrack () )
        {
            g2d.setPaint ( trackBackgroundColor );
            g2d.fillRect ( bounds.x, bounds.y, bounds.width, bounds.height );

            if ( scrollbar.getOrientation () == JScrollBar.VERTICAL )
            {
                final int x = ltr ? bounds.x : bounds.x + bounds.width - 1;
                g2d.setPaint ( trackBorderColor );
                g2d.drawLine ( x, bounds.y, x, bounds.height - 1 );
            }
            else
            {
                g2d.setPaint ( trackBorderColor );
                g2d.drawLine ( bounds.x, bounds.y, bounds.x + bounds.width - 1, bounds.y );
            }
        }
    }

    /**
     * Paints scroll bar track.
     * Track area only excludes the space under arrow buttons.
     *
     * @param g2d       graphics context
     * @param scrollbar scroll bar
     * @param bounds    track bounds
     */
    protected void paintTrack ( final Graphics2D g2d, final E scrollbar, final Rectangle bounds )
    {
        // You can paint your own track decoration by overriding this method
    }

    /**
     * Paints scroll bar thumb.
     * Thumb area is limited to thumb bounds and might be changed frequently.
     *
     * @param g2d       graphics context
     * @param scrollbar scroll bar component
     * @param bounds    thumb bounds
     */
    protected void paintThumb ( final Graphics2D g2d, final E scrollbar, final Rectangle bounds )
    {
        final Insets m = getCurrentThumbMargin ( scrollbar );
        final int w = bounds.width - m.left - m.right;
        final int h = bounds.height - m.top - m.bottom;

        // Round is limited to thumb minimum width/height to avoid painting artifacts
        final int round = MathUtils.min ( thumbRound, w - 1, h - 1 );

        // Painting thumb background
        g2d.setPaint ( getCurrentThumbBackgroundColor ( scrollbar ) );
        g2d.fillRoundRect ( bounds.x + m.left, bounds.y + m.top, w, h, round, round );

        // Painting thumb border
        g2d.setPaint ( getCurrentThumbBorderColor ( scrollbar ) );
        g2d.drawRoundRect ( bounds.x + m.left, bounds.y + m.top, w - 1, h - 1, round, round );
    }

    /**
     * Returns current thumb margin rotated into proper position.
     *
     * @param scrollbar scroll bar component
     * @return current thumb margin rotated into proper position
     */
    protected Insets getCurrentThumbMargin ( final E scrollbar )
    {
        if ( thumbMargin != null )
        {
            if ( thumbMarginR == null )
            {
                updateThumbMargins ();
            }
            final boolean ver = scrollbar.getOrientation () == Adjustable.VERTICAL;
            return ver ? ltr ? thumbMargin : thumbMarginR : ltr ? thumbMarginHL : thumbMarginHR;
        }
        else
        {
            return i ( 0, 0, 0, 0 );
        }
    }

    /**
     * Updates thumb margins cache.
     */
    protected void updateThumbMargins ()
    {
        this.thumbMarginR = i ( thumbMargin.top, thumbMargin.right, thumbMargin.bottom, thumbMargin.left );
        this.thumbMarginHL = i ( thumbMargin.left, thumbMargin.bottom, thumbMargin.right, thumbMargin.top );
        this.thumbMarginHR = i ( thumbMargin.right, thumbMargin.top, thumbMargin.left, thumbMargin.bottom );
    }

    /**
     * Returns current thumb border color.
     *
     * @param scrollbar scroll bar component
     * @return current thumb border color
     */
    protected Color getCurrentThumbBorderColor ( final E scrollbar )
    {
        return scrollbar.isEnabled () ? pressed || dragged ? thumbPressedBorderColor : rollover ? thumbRolloverBorderColor :
                ColorUtils.getIntermediateColor ( thumbBorderColor, thumbRolloverBorderColor, rolloverState ) : thumbDisabledBorderColor;
    }

    /**
     * Returns current thumb background color.
     *
     * @param scrollbar scroll bar component
     * @return current thumb background color
     */
    protected Color getCurrentThumbBackgroundColor ( final E scrollbar )
    {
        return scrollbar.isEnabled () ? pressed || dragged ? thumbPressedBackgroundColor : rollover ? thumbRolloverBackgroundColor :
                ColorUtils.getIntermediateColor ( thumbBackgroundColor, thumbRolloverBackgroundColor, rolloverState ) :
                thumbDisabledBackgroundColor;
    }

    /**
     * Forces scroll bar thumb to be repainted.
     */
    public void repaintThumb ()
    {
        if ( thumbBounds != null )
        {
            repaint ( thumbBounds );
        }
        else
        {
            repaint ();
        }
    }

    @Override
    public Dimension getPreferredSize ()
    {
        final Insets i = component.getInsets ();
        final boolean ver = component.getOrientation () == Adjustable.VERTICAL;
        return ver ? new Dimension ( i.left + scrollBarWidth + i.right, i.top + 48 + i.bottom ) :
                new Dimension ( i.left + 48 + i.right, i.top + scrollBarWidth + i.bottom );
    }
}