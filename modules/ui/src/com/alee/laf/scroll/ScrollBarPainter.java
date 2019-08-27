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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.managers.style.Bounds;
import com.alee.painter.AbstractPainter;
import com.alee.utils.ColorUtils;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.MathUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Basic painter for {@link JScrollBar} component.
 * It is used as {@link WebScrollBarUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */
public class ScrollBarPainter<C extends JScrollBar, U extends WScrollBarUI> extends AbstractPainter<C, U>
        implements IScrollBarPainter<C, U>
{
    /**
     * todo 1. Split into proper AbstractDecorationPainter & AbstractSectionDecorationPainter implementations
     * todo 2. Add extra states (provided when scroll bar is inside of a JScrollPane) indicating whether opposite bar is visible or not
     */

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
    protected transient MouseAdapter mouseAdapter;

    /**
     * Runtime variables.
     */
    protected transient boolean animated;
    protected transient WebTimer rolloverAnimator;
    protected transient float rolloverState;
    protected transient boolean rollover;
    protected transient boolean pressed;
    protected transient boolean dragged;

    /**
     * Painting variables.
     */
    protected transient Rectangle trackBounds;
    protected transient Rectangle thumbBounds;
    protected transient Insets thumbMarginR;
    protected transient Insets thumbMarginHL;
    protected transient Insets thumbMarginHR;

    @Override
    protected void installPropertiesAndListeners ()
    {
        super.installPropertiesAndListeners ();

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
                setRollover ( thumbBounds != null && thumbBounds.contains ( e.getPoint () ) );
            }

            @Override
            public void mouseMoved ( final MouseEvent e )
            {
                setRollover ( thumbBounds != null && thumbBounds.contains ( e.getPoint () ) );
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
    protected void uninstallPropertiesAndListeners ()
    {
        // Removing listeners
        component.removeMouseListener ( mouseAdapter );
        component.removeMouseMotionListener ( mouseAdapter );

        super.uninstallPropertiesAndListeners ();
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
                        rolloverAnimator = new WebTimer ( SwingUtils.frameRateDelay ( 36 ), new ActionListener ()
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

    @NotNull
    @Override
    public Boolean isOpaque ()
    {
        return this.ui.isDisplayTrack ();
    }

    @Nullable
    @Override
    protected Insets getBorder ()
    {
        final Insets border;
        if ( ui.isDisplayTrack () )
        {
            // Additional 1px border at scroll bar side
            // Orientation will be taken into account by the UI itself
            final boolean hor = component.getOrientation () == Adjustable.HORIZONTAL;
            border = new Insets ( hor ? 1 : 0, hor ? 0 : 1, 0, 0 );
        }
        else
        {
            // Using default border
            border = super.getBorder ();
        }
        return border;
    }

    @Override
    public void paint ( @NotNull final Graphics2D g2d, @NotNull final C scrollbar, @NotNull final U ui, @NotNull final Bounds bounds )
    {
        final Object aa = GraphicsUtils.setupAntialias ( g2d );
        paintBackground ( g2d, scrollbar, bounds.get () );
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
    protected void paintBackground ( @NotNull final Graphics2D g2d, @NotNull final C scrollbar, @NotNull final Rectangle bounds )
    {
        if ( ui.isDisplayTrack () )
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
    protected void paintTrack ( @NotNull final Graphics2D g2d, @NotNull final C scrollbar, @NotNull final Rectangle bounds )
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
    protected void paintThumb ( @NotNull final Graphics2D g2d, @NotNull final C scrollbar, @NotNull final Rectangle bounds )
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
    @NotNull
    protected Insets getCurrentThumbMargin ( @NotNull final C scrollbar )
    {
        final Insets margin;
        if ( thumbMargin != null )
        {
            if ( thumbMarginR == null )
            {
                updateThumbMargins ();
            }
            final boolean ver = scrollbar.getOrientation () == Adjustable.VERTICAL;
            margin = ver ? ltr ? thumbMargin : thumbMarginR : ltr ? thumbMarginHL : thumbMarginHR;
        }
        else
        {
            margin = new Insets ( 0, 0, 0, 0 );
        }
        return margin;
    }

    /**
     * Updates thumb margins cache.
     */
    protected void updateThumbMargins ()
    {
        thumbMarginR = new Insets ( thumbMargin.top, thumbMargin.right, thumbMargin.bottom, thumbMargin.left );
        thumbMarginHL = new Insets ( thumbMargin.left, thumbMargin.bottom, thumbMargin.right, thumbMargin.top );
        thumbMarginHR = new Insets ( thumbMargin.right, thumbMargin.top, thumbMargin.left, thumbMargin.bottom );
    }

    /**
     * Returns current thumb border color.
     *
     * @param scrollbar scroll bar component
     * @return current thumb border color
     */
    @NotNull
    protected Color getCurrentThumbBorderColor ( @NotNull final C scrollbar )
    {
        return scrollbar.isEnabled () ? pressed || dragged ? thumbPressedBorderColor : rollover ? thumbRolloverBorderColor :
                ColorUtils.intermediate ( thumbBorderColor, thumbRolloverBorderColor, rolloverState ) : thumbDisabledBorderColor;
    }

    /**
     * Returns current thumb background color.
     *
     * @param scrollbar scroll bar component
     * @return current thumb background color
     */
    @NotNull
    protected Color getCurrentThumbBackgroundColor ( @NotNull final C scrollbar )
    {
        return scrollbar.isEnabled () ? pressed || dragged ? thumbPressedBackgroundColor : rollover ? thumbRolloverBackgroundColor :
                ColorUtils.intermediate ( thumbBackgroundColor, thumbRolloverBackgroundColor, rolloverState ) :
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

    @NotNull
    @Override
    public Dimension getPreferredSize ()
    {
        final Insets i = component.getInsets ();
        final boolean ver = component.getOrientation () == Adjustable.VERTICAL;
        return ver ? new Dimension ( i.left + scrollBarWidth + i.right, i.top + 48 + i.bottom ) :
                new Dimension ( i.left + 48 + i.right, i.top + scrollBarWidth + i.bottom );
    }
}