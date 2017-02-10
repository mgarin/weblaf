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

package com.alee.painter.decoration.background;

import com.alee.api.data.Orientation;
import com.alee.extended.behavior.ComponentVisibilityBehavior;
import com.alee.global.StyleConstants;
import com.alee.managers.animation.transition.*;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.TextUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;

/**
 * Transparent background with a moving highlight.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> background type
 * @author Mikle Garin
 */

@XStreamAlias ( "MovingHighlightBackground" )
public class MovingHighlightBackground<E extends JComponent, D extends IDecoration<E, D>, I extends MovingHighlightBackground<E, D, I>>
        extends AbstractBackground<E, D, I>
{
    /**
     * Movement orientation.
     */
    @XStreamAsAttribute
    protected Orientation orientation;

    /**
     * Highlight width.
     */
    @XStreamAsAttribute
    protected Integer width;

    /**
     * Highlight color.
     */
    @XStreamAsAttribute
    protected Color color;

    /**
     * Amount of highlight passes before delay.
     */
    @XStreamAsAttribute
    protected Integer passes;

    /**
     * Single pass duration.
     */
    @XStreamAsAttribute
    protected String duration;

    /**
     * Delay between animations.
     */
    @XStreamAsAttribute
    protected String delay;

    /**
     * Visibility behavior that handles animation.
     */
    protected transient ComponentVisibilityBehavior visibilityBehavior;

    /**
     * Highlight position.
     */
    protected transient float position;

    @Override
    public void activate ( final E c, final D d )
    {
        visibilityBehavior = new ComponentVisibilityBehavior ( c, true )
        {
            /**
             * Transition used for background animations.
             */
            protected transient QueueTransition transitionsQueue;

            @Override
            public void displayed ()
            {
                // Resetting position
                position = 0f;

                // Custom transition for background animations
                transitionsQueue = new QueueTransition ( true );

                // Adding delay if required
                // Delay is added first to avoid repetitive animation
                final long del = TextUtils.parseDelay ( delay );
                if ( del > 0 )
                {
                    transitionsQueue.add ( new IdleTransition ( passes % 2 == 0 ? 0f : 1f, del ) );
                }

                // Adding passes
                final long dur = TextUtils.parseDelay ( duration );
                for ( int i = 0; i < passes; i++ )
                {
                    final float from = i % 2 == 0 ? 0f : 1f;
                    final float to = i % 2 == 0 ? 1f : 0f;
                    transitionsQueue.add ( new TimedTransition<Float> ( from, to, dur ) );
                }

                // Value update listener
                transitionsQueue.addListener ( new TransitionAdapter<Float> ()
                {
                    @Override
                    public void adjusted ( final Transition transition, final Float value )
                    {
                        position = value;
                        getComponent ().repaint ();
                    }
                } );

                // Playing transition
                transitionsQueue.play ();
            }

            @Override
            public void hidden ()
            {
                // Stopping transition
                transitionsQueue.stop ();

                // Cleaning up resources
                transitionsQueue = null;

                // Resetting position
                position = 0f;
            }
        };
        visibilityBehavior.install ();
    }

    @Override
    public void deactivate ( final E c, final D d )
    {
        visibilityBehavior.uninstall ();
        visibilityBehavior = null;
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d, final Shape shape )
    {
        final float opacity = getOpacity ();
        if ( opacity > 0 )
        {
            final Rectangle b = shape.getBounds ();
            final Paint paint;
            if ( orientation.isHorizontal () )
            {
                if ( c.getComponentOrientation ().isLeftToRight () )
                {
                    paint = new RadialGradientPaint ( b.x - width / 2 + ( b.width + width ) * position, b.y + b.height / 2, width / 2,
                            new float[]{ 0f, 1f }, new Color[]{ color, StyleConstants.transparent } );
                }
                else
                {
                    paint = new RadialGradientPaint ( b.x + b.width + width / 2 - ( b.width + width ) * position, b.y + b.height / 2,
                            width / 2, new float[]{ 0f, 1f }, new Color[]{ color, StyleConstants.transparent } );
                }
            }
            else
            {
                if ( c.getComponentOrientation ().isLeftToRight () )
                {
                    paint = new RadialGradientPaint ( b.x + b.width / 2, b.y + b.height + width / 2 - ( b.height + width ) * position,
                            width / 2, new float[]{ 0f, 1f }, new Color[]{ color, StyleConstants.transparent } );
                }
                else
                {
                    paint = new RadialGradientPaint ( b.x + b.width / 2, b.y - width / 2 + ( b.height + width ) * position, width / 2,
                            new float[]{ 0f, 1f }, new Color[]{ color, StyleConstants.transparent } );
                }
            }

            final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, opacity, opacity < 1f );
            final Paint op = GraphicsUtils.setupPaint ( g2d, paint );

            g2d.fillRect ( b.x, b.y, b.width, b.height );

            GraphicsUtils.restorePaint ( g2d, op );
            GraphicsUtils.restoreComposite ( g2d, oc, opacity < 1f );
        }
    }

    @Override
    public I merge ( final I bg )
    {
        super.merge ( bg );
        orientation = bg.isOverwrite () || bg.orientation != null ? bg.orientation : orientation;
        width = bg.isOverwrite () || bg.width != null ? bg.width : width;
        color = bg.isOverwrite () || bg.color != null ? bg.color : color;
        passes = bg.isOverwrite () || bg.passes != null ? bg.passes : passes;
        duration = bg.isOverwrite () || bg.duration != null ? bg.duration : duration;
        delay = bg.isOverwrite () || bg.delay != null ? bg.delay : delay;
        return ( I ) this;
    }
}