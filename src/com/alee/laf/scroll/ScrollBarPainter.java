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

import com.alee.extended.painter.AbstractPainter;
import com.alee.laf.StyleConstants;
import com.alee.utils.ColorUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Base painter for JScrollBar component.
 * It is used as WebScrollBarUI default styling.
 *
 * @author Mikle Garin
 */

public class ScrollBarPainter<E extends JScrollBar> extends AbstractPainter<E>
{
    /**
     * Style settings.
     */
    protected boolean buttonsVisible = WebScrollBarStyle.buttonsVisible;
    protected boolean drawTrack = WebScrollBarStyle.drawTrack;
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
    protected int thumbRound = WebScrollBarStyle.thumbRound;
    protected Insets thumbMargin = WebScrollBarStyle.thumbMargin;

    /**
     * Runtime variables.
     */
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
                setRollover ( getThumbBounds ().contains ( e.getPoint () ) );
            }

            @Override
            public void mouseMoved ( final MouseEvent e )
            {
                setRollover ( getThumbBounds ().contains ( e.getPoint () ) );
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
     *
     * @return true if scroll bar arrow buttons are visible, false otherwise
     */
    public boolean isButtonsVisible ()
    {
        return buttonsVisible;
    }

    /**
     * Sets whether scroll bar arrow buttons are visible or not.
     *
     * @param visible whether scroll bar arrow buttons are visible or not
     */
    public void setButtonsVisible ( final boolean visible )
    {
        if ( this.buttonsVisible != visible )
        {
            this.buttonsVisible = visible;
            updateAll ();
        }
    }

    /**
     * Returns whether scroll bar track should be painted or not.
     *
     * @return true if scroll bar track should be painted, false otherwise
     */
    public boolean isDrawTrack ()
    {
        return drawTrack;
    }

    /**
     * Sets whether scroll bar track should be painted or not.
     *
     * @param draw whether scroll bar track should be painted or not
     */
    public void setDrawTrack ( final boolean draw )
    {
        if ( this.drawTrack != draw )
        {
            this.drawTrack = draw;
            updateAll ();
        }
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
        this.thumbMarginHL = new Insets ( thumbMargin.right, thumbMargin.top, thumbMargin.left, thumbMargin.bottom );
        this.thumbMarginHR = new Insets ( thumbMargin.left, thumbMargin.bottom, thumbMargin.right, thumbMargin.top );
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
     * Returns whether scroll bar thumb is being dragged or not.
     * This value is updated by WebScrollBarUI on each paint call to ensure that proper value presented.
     *
     * @return true if scroll bar thumb is being dragged, false otherwise
     */
    public boolean isDragged ()
    {
        return dragged;
    }

    /**
     * Sets whether scroll bar thumb is being dragged or not.
     *
     * @param dragged whether scroll bar thumb is being dragged or not
     */
    public void setDragged ( final boolean dragged )
    {
        this.dragged = dragged;
    }

    /**
     * Returns scroll bar track bounds.
     *
     * @return scroll bar track bounds
     */
    public Rectangle getTrackBounds ()
    {
        return trackBounds;
    }

    /**
     * Sets scroll bar track bounds.
     * This value is updated by WebScrollBarUI on each paint call to ensure that proper bounds presented.
     *
     * @param bounds new scroll bar track bounds
     */
    public void setTrackBounds ( final Rectangle bounds )
    {
        this.trackBounds = bounds;
    }

    /**
     * Returns scroll bar thumb bounds.
     *
     * @return scroll bar thumb bounds
     */
    public Rectangle getThumbBounds ()
    {
        return thumbBounds;
    }

    /**
     * Sets scroll bar thumb bounds.
     * This value is updated by WebScrollBarUI on each paint call to ensure that proper bounds presented.
     *
     * @param bounds new scroll bar thumb bounds
     */
    public void setThumbBounds ( final Rectangle bounds )
    {
        this.thumbBounds = bounds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOpaque ( final E scrollbar )
    {
        return drawTrack;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getMargin ( final E scrollbar )
    {
        if ( drawTrack )
        {
            // Additional 1px border at scroll bar side
            // Orientation will be taken into account by the UI itself
            final boolean hor = scrollbar.getOrientation () == SwingConstants.HORIZONTAL;
            return new Insets ( hor ? 1 : 0, hor ? 0 : 1, 0, 0 );
        }
        else
        {
            return new Insets ( 0, 0, 0, 0 );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E scrollbar )
    {
        final Object aa = LafUtils.setupAntialias ( g2d );
        paintBackground ( g2d, scrollbar, bounds );
        paintTrack ( g2d, scrollbar, trackBounds );
        paintThumb ( g2d, scrollbar, thumbBounds );
        LafUtils.restoreAntialias ( g2d, aa );
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
        if ( drawTrack )
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

        // Painting thumb background
        g2d.setPaint ( getCurrentThumbBackgroundColor ( scrollbar ) );
        g2d.fillRoundRect ( bounds.x + m.left, bounds.y + m.top, bounds.width - m.left - m.right, bounds.height - m.top - m.bottom,
                thumbRound, thumbRound );

        // Painting thumb border
        g2d.setPaint ( getCurrentThumbBorderColor ( scrollbar ) );
        g2d.drawRoundRect ( bounds.x + m.left, bounds.y + m.top, bounds.width - m.left - m.right - 1, bounds.height - m.top - m.bottom - 1,
                thumbRound, thumbRound );
    }

    /**
     * Returns current thumb margin rotated into proper position.
     *
     * @param scrollbar scroll bar component
     * @return current thumb margin rotated into proper position
     */
    protected Insets getCurrentThumbMargin ( final E scrollbar )
    {
        final boolean ver = scrollbar.getOrientation () == SwingConstants.VERTICAL;
        final boolean ltr = scrollbar.getComponentOrientation ().isLeftToRight ();
        return ver ? ( ltr ? thumbMargin : thumbMarginR ) : ( ltr ? thumbMarginHL : thumbMarginHR );
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