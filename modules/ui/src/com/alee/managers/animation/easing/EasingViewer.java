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

package com.alee.managers.animation.easing;

import com.alee.api.annotations.NotNull;
import com.alee.extended.canvas.WebCanvas;
import com.alee.graphics.data.Line;
import com.alee.managers.animation.transition.*;
import com.alee.painter.decoration.content.TextRasterization;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.MouseButton;
import com.alee.utils.swing.extensions.MouseEventRunnable;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Simple easing algorithm preview graph.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-AnimationManager">How to use AnimationManager</a>
 * @see com.alee.managers.animation.AnimationManager
 */
public class EasingViewer extends WebCanvas
{
    /**
     * Progress value format.
     */
    protected final DecimalFormat progressFormat = new DecimalFormat ( "0.00" );

    /**
     * Content padding.
     */
    protected final Insets padding;

    /**
     * Amount of transition steps.
     */
    protected final int steps;

    /**
     * Transition progress.
     */
    protected int progress;

    /**
     * Easing algorithm.
     */
    protected Easing easing;

    /**
     * Transition duration.
     */
    protected long duration;

    /**
     * Transition.
     */
    protected AbstractTransition<Integer> transition;

    /**
     * Transition listeners.
     */
    protected List<TransitionListener<Integer>> listeners;

    /**
     * Constructs new easing algorithm preview.
     */
    public EasingViewer ()
    {
        super ();
        this.padding = new Insets ( 55, 55, 55, 55 );
        this.steps = 500;
        this.progress = steps;
        this.easing = null;
        this.duration = 0L;
        this.listeners = new ArrayList<TransitionListener<Integer>> ();

        onMousePress ( MouseButton.left, new MouseEventRunnable ()
        {
            @Override
            public void run ( @NotNull final MouseEvent e )
            {
                preview ( easing, duration );
            }
        } );
    }

    /**
     * Returns amount of transition steps.
     *
     * @return amount of transition steps
     */
    public int getSteps ()
    {
        return steps;
    }

    /**
     * Returns transition progress.
     *
     * @return transition progress
     */
    public int getProgress ()
    {
        return progress;
    }

    /**
     * Returns eased transition progress.
     *
     * @return eased transition progress
     */
    public double getEasedProgress ()
    {
        return easing.calculate ( 0d, 1d, ( double ) progress / steps, 1d );
    }

    /**
     * Adds external transition listener.
     *
     * @param listener external transition listener
     */
    public void addListener ( final TransitionListener<Integer> listener )
    {
        listeners.add ( listener );
    }

    /**
     * Runs easing graph preview.
     *
     * @param easing   easing algorithm to display
     * @param duration transition duration
     */
    public void preview ( final Easing easing, final long duration )
    {
        this.easing = easing;
        this.duration = duration;
        restartTransition ();
    }

    /**
     * Restarts graph transition.
     */
    protected void restartTransition ()
    {
        // Stop previous transition
        if ( transition != null )
        {
            transition.stop ();
            transition = null;
        }

        // Resetting progress
        progress = 0;

        // Play new transition
        transition = new TimedTransition<Integer> ( 0, steps, new Linear (), duration );
        transition.addListener ( new TransitionAdapter<Integer> ()
        {
            @Override
            public void adjusted ( final Transition transition, final Integer value )
            {
                // Updating graph
                progress = value;
                EasingViewer.this.repaint ();

                // Informing listeners
                for ( final TransitionListener<Integer> listener : listeners )
                {
                    listener.adjusted ( transition, value );
                }
            }
        } );
        transition.play ();
    }

    @Override
    protected void paintComponent ( final Graphics g )
    {
        // Leave default painting untouched
        super.paintComponent ( g );

        // Only paint when easing is set
        if ( easing != null )
        {
            // Using Graphics2D API
            final Graphics2D g2d = ( Graphics2D ) g;

            // Graph bounds
            final Rectangle graph = new Rectangle ( padding.left, padding.top,
                    getWidth () - padding.left - padding.right * 2,
                    getHeight () - padding.top - padding.bottom * 2 );

            // Progress position
            final double eased = getEasedProgress ();
            final int x = graph.width * progress / steps;
            final int y = graph.y + graph.height - ( int ) Math.ceil ( graph.height * eased );
            final Point point = new Point ( graph.x + x, y );

            // Painting graph
            paintGraph ( g2d, graph, point );

            // Painting progress line
            paintProgress ( g2d, new Line ( graph.x, graph.y + graph.height + padding.bottom,
                    graph.x + graph.width, graph.y + graph.height + padding.bottom ), point );

            // Painting value line
            paintValue ( g2d, new Line ( graph.x + graph.width + padding.right, graph.y,
                    graph.x + graph.width + padding.right, graph.y + graph.height ), point, eased );
        }
    }

    /**
     * Paints easing graph.
     *
     * @param g2d    graphics context
     * @param bounds graph bounds
     * @param point  current value point
     */
    protected void paintGraph ( final Graphics2D g2d, final Rectangle bounds, final Point point )
    {
        final Object aa = GraphicsUtils.setupAntialias ( g2d );

        // Background
        g2d.setPaint ( new Color ( 240, 240, 240 ) );
        g2d.fill ( bounds );

        // Diagonal guide
        if ( !( easing instanceof Linear ) )
        {
            g2d.setPaint ( Color.LIGHT_GRAY );
            g2d.drawLine ( bounds.x, bounds.y + bounds.height, bounds.x + bounds.width, bounds.y );
        }

        // Progress indicators
        if ( progress < steps )
        {
            g2d.setPaint ( Color.WHITE );
            g2d.drawLine ( point.x, bounds.y, point.x, bounds.y + bounds.height );
            g2d.drawLine ( bounds.x, point.y, bounds.x + bounds.width, point.y );
        }

        // Creating shape
        final GeneralPath done = new GeneralPath ( Path2D.WIND_EVEN_ODD );
        final GeneralPath todo = new GeneralPath ( Path2D.WIND_EVEN_ODD );
        for ( int i = 0; i <= steps; i += 1 )
        {
            final double current = ( double ) i / steps;
            final double eased = easing.calculate ( 0d, 1d, current, 1d );
            final double x = bounds.x + bounds.width * current;
            final double y = bounds.y + bounds.height - bounds.height * eased;
            if ( i < progress || progress == steps )
            {
                if ( i == 0 )
                {
                    done.moveTo ( x, y );
                }
                else
                {
                    done.lineTo ( x, y );
                }
            }
            else
            {
                if ( i == progress )
                {
                    if ( progress > 0 )
                    {
                        done.lineTo ( x, y );
                    }
                    todo.moveTo ( x, y );
                }
                else
                {
                    todo.lineTo ( x, y );
                }
            }
        }

        // Drawing easing graph
        final Stroke os = GraphicsUtils.setupStroke ( g2d, new BasicStroke ( 1.7f ) );
        if ( progress < steps )
        {
            g2d.setPaint ( Color.LIGHT_GRAY );
            g2d.draw ( todo );
        }
        if ( progress > 0 )
        {
            g2d.setPaint ( Color.BLACK );
            g2d.draw ( done );
        }
        GraphicsUtils.restoreStroke ( g2d, os );

        GraphicsUtils.restoreAntialias ( g2d, aa );

        if ( progress == steps )
        {
            final Map taa = SwingUtils.setupTextAntialias ( g2d, TextRasterization.subpixel );
            final FontMetrics fm = g2d.getFontMetrics ( g2d.getFont () );

            final String middleText = "Click to replay";
            g2d.setPaint ( Color.LIGHT_GRAY );
            g2d.drawString ( middleText, bounds.x + bounds.width / 2 - fm.stringWidth ( middleText ) / 2,
                    bounds.y + bounds.height - 10 );

            SwingUtils.restoreTextAntialias ( g2d, taa );
        }
    }

    /**
     * Paints graph progress bar.
     *
     * @param g2d    graphics context
     * @param bounds progress line bounds
     * @param point  current value point
     */
    private void paintProgress ( final Graphics2D g2d, final Line bounds, final Point point )
    {
        final Object aa = GraphicsUtils.setupAntialias ( g2d );
        final Stroke os = GraphicsUtils.setupStroke ( g2d, new BasicStroke ( 2f ) );

        g2d.setPaint ( Color.LIGHT_GRAY );
        g2d.drawLine ( bounds.x1, bounds.y1, bounds.x2, bounds.y2 );

        g2d.setPaint ( Color.BLACK );
        g2d.drawLine ( bounds.x1, bounds.y1, point.x, bounds.y2 );

        GraphicsUtils.restoreStroke ( g2d, os );
        GraphicsUtils.restoreAntialias ( g2d, aa );

        final Map taa = SwingUtils.setupTextAntialias ( g2d, TextRasterization.subpixel );
        final FontMetrics fm = g2d.getFontMetrics ( g2d.getFont () );
        final int th = fm.getAscent () - fm.getLeading () - fm.getDescent ();

        final int mid = ( bounds.x1 + bounds.x2 ) / 2;
        g2d.setPaint ( Color.BLACK );
        final String overBar = "Progress";
        g2d.drawString ( overBar, mid - fm.stringWidth ( overBar ) / 2, bounds.y1 - 10 );
        final String belowBar = progressFormat.format ( ( double ) progress / steps );
        g2d.drawString ( belowBar, mid - fm.stringWidth ( belowBar ) / 2, bounds.y1 + 10 + th );

        final String afterBar = duration * progress / steps + " ms";
        g2d.setPaint ( Color.BLACK );
        g2d.drawString ( afterBar, bounds.x2 + padding.right - fm.stringWidth ( afterBar ) / 2, bounds.y1 + th / 2 );

        SwingUtils.restoreTextAntialias ( g2d, taa );
    }

    /**
     * Paints graph value bar.
     *
     * @param g2d    graphics context
     * @param bounds value line bounds
     * @param point  current value point
     * @param eased  current eased progress
     */
    private void paintValue ( final Graphics2D g2d, final Line bounds, final Point point, final double eased )
    {
        final Object aa = GraphicsUtils.setupAntialias ( g2d );
        final Stroke os = GraphicsUtils.setupStroke ( g2d, new BasicStroke ( 2f ) );

        g2d.setPaint ( Color.LIGHT_GRAY );
        g2d.drawLine ( bounds.x1, bounds.y2, bounds.x2, bounds.y1 );

        g2d.setPaint ( new Color ( 77, 135, 31 ) );
        g2d.drawLine ( bounds.x1, bounds.y2, bounds.x2, point.y );
        g2d.fillOval ( bounds.x2 - 6, point.y - 6, 13, 13 );

        GraphicsUtils.restoreStroke ( g2d, os );
        GraphicsUtils.restoreAntialias ( g2d, aa );

        final Map taa = SwingUtils.setupTextAntialias ( g2d, TextRasterization.subpixel );
        final FontMetrics fm = g2d.getFontMetrics ( g2d.getFont () );
        final int th = fm.getAscent () - fm.getLeading () - fm.getDescent ();

        final String beforeBar = "Value";
        final Point bbp = new Point ( bounds.x1 - 10, ( bounds.y1 + bounds.y2 ) / 2 + fm.stringWidth ( beforeBar ) / 2 );
        g2d.setPaint ( Color.BLACK );
        g2d.rotate ( -Math.PI / 2, bbp.x, bbp.y );
        g2d.drawString ( beforeBar, bbp.x, bbp.y );
        g2d.rotate ( Math.PI / 2, bbp.x, bbp.y );

        final String afterBar = progressFormat.format ( eased );
        final Point abp = new Point ( bounds.x1 + 10 + th, ( bounds.y1 + bounds.y2 ) / 2 + fm.stringWidth ( afterBar ) / 2 );
        g2d.setPaint ( Color.BLACK );
        g2d.rotate ( -Math.PI / 2, abp.x, abp.y );
        g2d.drawString ( afterBar, abp.x, abp.y );
        g2d.rotate ( Math.PI / 2, abp.x, abp.y );

        SwingUtils.restoreTextAntialias ( g2d, taa );
    }

    @NotNull
    @Override
    public Dimension getPreferredSize ()
    {
        return new Dimension ( 450, 450 );
    }
}