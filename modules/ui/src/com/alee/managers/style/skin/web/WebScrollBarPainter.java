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

package com.alee.managers.style.skin.web;

import com.alee.extended.painter.AbstractPainter;
import com.alee.global.StyleConstants;
import com.alee.laf.scroll.ScrollBarPainter;
import com.alee.laf.scroll.WebScrollBarStyle;
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
 * Web-style painter for JScrollBar component.
 * It is used as WebScrollBarUI default painter.
 *
 * @author Mikle Garin
 */

public class WebScrollBarPainter<E extends JScrollBar> extends AbstractPainter<E> implements ScrollBarPainter<E>
{
    /**
     * Style settings.
     */
    protected boolean paintButtons = WebScrollBarStyle.paintButtons;
    protected boolean paintTrack = WebScrollBarStyle.paintTrack;
    protected int thumbRound = WebScrollBarStyle.thumbRound;
    protected Insets thumbMargin = WebScrollBarStyle.thumbMargin;
    protected Color trackBorderColor = WebScrollBarStyle.trackBorderColor;
    protected Color trackBackgroundColor = WebScrollBarStyle.trackBackgroundColor;
    protected Color thumbBorderColor = WebScrollBarStyle.thumbBorderColor;
    protected Color thumbBackgroundColor = WebScrollBarStyle.thumbBackgroundColor;
    protected Color thumbDisabledBorderColor = WebScrollBarStyle.thumbDisabledBorderColor;
    protected Color thumbDisabledBackgroundColor = WebScrollBarStyle.thumbDisabledBackgroundColor;
    protected Color thumbRolloverBorderColor = WebScrollBarStyle.thumbRolloverBorderColor;
    protected Color thumbRolloverBackgroundColor = WebScrollBarStyle.thumbRolloverBackgroundColor;
    protected Color thumbPressedBorderColor = WebScrollBarStyle.thumbPressedBorderColor;
    protected Color thumbPressedBackgroundColor = WebScrollBarStyle.thumbPressedBackgroundColor;

    /**
     * Runtime variables.
     */
    protected boolean animated;
    protected WebTimer rolloverAnimator;
    protected float rolloverState;
    protected boolean rollover;
    protected boolean pressed;
    protected boolean dragged;
    protected Rectangle trackBounds;
    protected Rectangle thumbBounds;
    protected Insets thumbMarginR;
    protected Insets thumbMarginHL;
    protected Insets thumbMarginHR;

    /**
     * Listeners.
     */
    protected MouseAdapter mouseAdapter;

    /**
     * {@inheritDoc}
     */
    @Override
    public void install ( final E scrollbar )
    {
        super.install ( scrollbar );

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
        scrollbar.addMouseListener ( mouseAdapter );
        scrollbar.addMouseMotionListener ( mouseAdapter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uninstall ( final E scrollbar )
    {
        // Removing listeners
        scrollbar.removeMouseListener ( mouseAdapter );
        scrollbar.removeMouseMotionListener ( mouseAdapter );

        super.uninstall ( scrollbar );
    }

    /**
     * Returns whether scroll bar arrow buttons are visible or not.
     * Buttons are painted separately, this mark simply informs whether they are actually visible or not.
     *
     * @return true if scroll bar arrow buttons are visible, false otherwise
     */
    public boolean isPaintButtons ()
    {
        return paintButtons;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintButtons ( final boolean paint )
    {
        if ( this.paintButtons != paint )
        {
            this.paintButtons = paint;
            updateAll ();
        }
    }

    /**
     * Returns whether scroll bar track should be painted or not.
     *
     * @return true if scroll bar track should be painted, false otherwise
     */
    public boolean isPaintTrack ()
    {
        return paintTrack;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintTrack ( final boolean paint )
    {
        if ( this.paintTrack != paint )
        {
            this.paintTrack = paint;
            updateAll ();
        }
    }

    /**
     * Returns scroll bar thumb corners rounding.
     *
     * @return scroll bar thumb corners rounding
     */
    public int getThumbRound ()
    {
        return thumbRound;
    }

    /**
     * Sets scroll bar thumb corners rounding.
     *
     * @param round new scroll bar thumb corners rounding
     */
    public void setThumbRound ( final int round )
    {
        if ( this.thumbRound != round )
        {
            this.thumbRound = round;
            repaintThumb ();
        }
    }

    /**
     * Returns scroll bar thumb margin.
     * This value doesn't affect scroll bar size, just the visual representation of the thumb.
     * Also these margins are the same only for vertical scroll bar, and are rotated clockwise for horizontal scroll bar.
     *
     * @return scroll bar thumb margin
     */
    public Insets getThumbMargin ()
    {
        return thumbMargin;
    }

    /**
     * Sets scroll bar thumb margin.
     * This value doesn't affect scroll bar size, just the visual representation of the thumb.
     * Also these margins are the same only for vertical scroll bar, and are rotated clockwise for horizontal scroll bar.
     *
     * @param margin new scroll bar thumb margin
     */
    public void setThumbMargin ( final Insets margin )
    {
        if ( this.thumbMargin != margin )
        {
            this.thumbMargin = margin;
            updateThumbMargins ();
            repaintThumb ();
        }
        else if ( thumbMarginR == null )
        {
            updateThumbMargins ();
        }
    }

    /**
     * Updates cached thumb margins.
     */
    protected void updateThumbMargins ()
    {
        this.thumbMarginR = new Insets ( thumbMargin.top, thumbMargin.right, thumbMargin.bottom, thumbMargin.left );
        this.thumbMarginHL = new Insets ( thumbMargin.left, thumbMargin.bottom, thumbMargin.right, thumbMargin.top );
        this.thumbMarginHR = new Insets ( thumbMargin.right, thumbMargin.top, thumbMargin.left, thumbMargin.bottom );
    }

    /**
     * Returns scroll bar track border color.
     *
     * @return scroll bar track border color
     */
    public Color getTrackBorderColor ()
    {
        return trackBorderColor;
    }

    /**
     * Sets scroll bar track border color.
     *
     * @param color new scroll bar track border color
     */
    public void setTrackBorderColor ( final Color color )
    {
        if ( this.trackBorderColor != color )
        {
            this.trackBorderColor = color;
            repaint ();
        }
    }

    /**
     * Returns scroll bar track background color.
     *
     * @return scroll bar track background color
     */
    public Color getTrackBackgroundColor ()
    {
        return trackBackgroundColor;
    }

    /**
     * Sets scroll bar track background color.
     *
     * @param color new scroll bar track background color
     */
    public void setTrackBackgroundColor ( final Color color )
    {
        if ( this.trackBackgroundColor != color )
        {
            this.trackBackgroundColor = color;
            repaint ();
        }
    }

    /**
     * Returns scroll bar thumb border color.
     *
     * @return scroll bar thumb border color
     */
    public Color getThumbBorderColor ()
    {
        return thumbBorderColor;
    }

    /**
     * Sets scroll bar thumb border color.
     *
     * @param color new scroll bar thumb border color
     */
    public void setThumbBorderColor ( final Color color )
    {
        if ( this.thumbBorderColor != color )
        {
            this.thumbBorderColor = color;
            repaintThumb ();
        }
    }

    /**
     * Returns scroll bar thumb background color.
     *
     * @return scroll bar thumb background color
     */
    public Color getThumbBackgroundColor ()
    {
        return thumbBackgroundColor;
    }

    /**
     * Sets scroll bar thumb background color.
     *
     * @param color new scroll bar thumb background color
     */
    public void setThumbBackgroundColor ( final Color color )
    {
        if ( this.thumbBackgroundColor != color )
        {
            this.thumbBackgroundColor = color;
            repaintThumb ();
        }
    }

    /**
     * Returns disabled scroll bar thumb border color.
     *
     * @return disabled scroll bar thumb border color
     */
    public Color getThumbDisabledBorderColor ()
    {
        return thumbDisabledBorderColor;
    }

    /**
     * Sets disabled scroll bar thumb border color.
     *
     * @param color new disabled scroll bar thumb border color
     */
    public void setThumbDisabledBorderColor ( final Color color )
    {
        if ( this.thumbDisabledBorderColor != color )
        {
            this.thumbDisabledBorderColor = color;
            repaintThumb ();
        }
    }

    /**
     * Returns disabled scroll bar thumb background color.
     *
     * @return disabled scroll bar thumb background color
     */
    public Color getThumbDisabledBackgroundColor ()
    {
        return thumbDisabledBackgroundColor;
    }

    /**
     * Sets disabled scroll bar thumb background color.
     *
     * @param color new disabled scroll bar thumb background color
     */
    public void setThumbDisabledBackgroundColor ( final Color color )
    {
        if ( this.thumbDisabledBackgroundColor != color )
        {
            this.thumbDisabledBackgroundColor = color;
            repaintThumb ();
        }
    }

    /**
     * Returns scroll bar rollover thumb border color.
     *
     * @return scroll bar rollover thumb border color
     */
    public Color getThumbRolloverBorderColor ()
    {
        return thumbRolloverBorderColor;
    }

    /**
     * Sets scroll bar rollover thumb border color.
     *
     * @param color new scroll bar rollover thumb border color
     */
    public void setThumbRolloverBorderColor ( final Color color )
    {
        if ( this.thumbRolloverBorderColor != color )
        {
            this.thumbRolloverBorderColor = color;
            repaintThumb ();
        }
    }

    /**
     * Returns scroll bar rollover thumb background color.
     *
     * @return scroll bar rollover thumb background color
     */
    public Color getThumbRolloverBackgroundColor ()
    {
        return thumbRolloverBackgroundColor;
    }

    /**
     * Sets scroll bar rollover thumb background color.
     *
     * @param color new scroll bar rollover thumb background color
     */
    public void setThumbRolloverBackgroundColor ( final Color color )
    {
        if ( this.thumbRolloverBackgroundColor != color )
        {
            this.thumbRolloverBackgroundColor = color;
            repaintThumb ();
        }
    }

    /**
     * Returns scroll bar pressed thumb border color.
     *
     * @return scroll bar pressed thumb border color
     */
    public Color getThumbPressedBorderColor ()
    {
        return thumbPressedBorderColor;
    }

    /**
     * Returns scroll bar pressed thumb border color.
     *
     * @param color new scroll bar pressed thumb border color
     */
    public void setThumbPressedBorderColor ( final Color color )
    {
        if ( this.thumbPressedBorderColor != color )
        {
            this.thumbPressedBorderColor = color;
            repaintThumb ();
        }
    }

    /**
     * Returns scroll bar pressed thumb background color.
     *
     * @return scroll bar pressed thumb background color
     */
    public Color getThumbPressedBackgroundColor ()
    {
        return thumbPressedBackgroundColor;
    }

    /**
     * Sets scroll bar pressed thumb background color.
     *
     * @param color new scroll bar pressed thumb background color
     */
    public void setThumbPressedBackgroundColor ( final Color color )
    {
        if ( this.thumbPressedBackgroundColor != color )
        {
            this.thumbPressedBackgroundColor = color;
            repaintThumb ();
        }
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
                        rolloverAnimator = new WebTimer ( StyleConstants.avgAnimationDelay, new ActionListener ()
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDragged ( final boolean dragged )
    {
        this.dragged = dragged;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTrackBounds ( final Rectangle bounds )
    {
        this.trackBounds = bounds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setThumbBounds ( final Rectangle bounds )
    {
        this.thumbBounds = bounds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isOpaque ( final E scrollbar )
    {
        return paintTrack;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getMargin ( final E scrollbar )
    {
        if ( paintTrack )
        {
            // Additional 1px border at scroll bar side
            // Orientation will be taken into account by the UI itself
            final boolean hor = scrollbar.getOrientation () == SwingConstants.HORIZONTAL;
            return new Insets ( hor ? 1 : 0, hor ? 0 : 1, 0, 0 );
        }
        else
        {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E scrollbar )
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
    @SuppressWarnings ( "UnusedParameters" )
    protected void paintBackground ( final Graphics2D g2d, final E scrollbar, final Rectangle bounds )
    {
        if ( paintTrack )
        {
            g2d.setPaint ( trackBackgroundColor );
            g2d.fillRect ( bounds.x, bounds.y, bounds.width, bounds.height );

            if ( scrollbar.getOrientation () == JScrollBar.VERTICAL )
            {
                final boolean ltr = scrollbar.getComponentOrientation ().isLeftToRight ();
                final int x = ltr ? bounds.x : bounds.x + bounds.width - 1;
                g2d.setColor ( trackBorderColor );
                g2d.drawLine ( x, bounds.y, x, bounds.height - 1 );
            }
            else
            {
                g2d.setColor ( trackBorderColor );
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
    @SuppressWarnings ( "UnusedParameters" )
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
    @SuppressWarnings ( "UnusedParameters" )
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
            final boolean ver = scrollbar.getOrientation () == SwingConstants.VERTICAL;
            final boolean ltr = scrollbar.getComponentOrientation ().isLeftToRight ();
            return ver ? ( ltr ? thumbMargin : thumbMarginR ) : ( ltr ? thumbMarginHL : thumbMarginHR );
        }
        else
        {
            return new Insets ( 0, 0, 0, 0 );
        }
    }

    /**
     * Returns current thumb border color.
     *
     * @param scrollbar scroll bar component
     * @return current thumb border color
     */
    protected Color getCurrentThumbBorderColor ( final E scrollbar )
    {
        return scrollbar.isEnabled () ? ( pressed || dragged ? thumbPressedBorderColor : ( rollover ? thumbRolloverBorderColor :
                ColorUtils.getIntermediateColor ( thumbBorderColor, thumbRolloverBorderColor, rolloverState ) ) ) :
                thumbDisabledBorderColor;
    }

    /**
     * Returns current thumb background color.
     *
     * @param scrollbar scroll bar component
     * @return current thumb background color
     */
    protected Color getCurrentThumbBackgroundColor ( final E scrollbar )
    {
        return scrollbar.isEnabled () ? ( pressed || dragged ? thumbPressedBackgroundColor : ( rollover ? thumbRolloverBackgroundColor :
                ColorUtils.getIntermediateColor ( thumbBackgroundColor, thumbRolloverBackgroundColor, rolloverState ) ) ) :
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
}