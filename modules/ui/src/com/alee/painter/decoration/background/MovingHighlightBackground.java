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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.clone.behavior.OmitOnClone;
import com.alee.api.data.Orientation;
import com.alee.api.merge.behavior.OmitOnMerge;
import com.alee.extended.behavior.VisibilityBehavior;
import com.alee.managers.animation.transition.*;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.ColorUtils;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.parsing.DurationUnits;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;

/**
 * Transparent background with a moving highlight.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> background type
 * @author Mikle Garin
 */
@XStreamAlias ( "MovingHighlightBackground" )
public class MovingHighlightBackground<C extends JComponent, D extends IDecoration<C, D>, I extends MovingHighlightBackground<C, D, I>>
        extends AbstractBackground<C, D, I>
{
    /**
     * Highlight movement {@link Orientation}.
     */
    @Nullable
    @XStreamAsAttribute
    protected Orientation orientation;

    /**
     * Highlight width.
     */
    @Nullable
    @XStreamAsAttribute
    protected Integer width;

    /**
     * Highlight {@link Color}.
     */
    @Nullable
    @XStreamAsAttribute
    protected Color color;

    /**
     * Highlight passes count.
     */
    @Nullable
    @XStreamAsAttribute
    protected Integer passes;

    /**
     * Single pass animation duration.
     */
    @Nullable
    @XStreamAsAttribute
    protected String duration;

    /**
     * Delay between repeated animations.
     */
    @Nullable
    @XStreamAsAttribute
    protected String delay;

    /**
     * Visibility behavior that handles animation.
     */
    @OmitOnClone
    @OmitOnMerge
    protected transient VisibilityBehavior<C> visibilityBehavior;

    /**
     * Highlight position.
     */
    @OmitOnClone
    @OmitOnMerge
    protected transient float position;

    /**
     * Transition used for background animations.
     */
    @Nullable
    @OmitOnClone
    @OmitOnMerge
    protected transient QueueTransition transitionsQueue;

    @Override
    public void activate ( final C c, final D d )
    {
        visibilityBehavior = new VisibilityBehavior<C> ( c, true )
        {
            @Override
            protected void displayed ( @NotNull final C component )
            {
                playAnimation ( getComponent () );
            }

            @Override
            protected void hidden ( @NotNull final C component )
            {
                stopAnimation ( getComponent () );
            }
        };
        visibilityBehavior.install ();
    }

    @Override
    public void deactivate ( final C c, final D d )
    {
        visibilityBehavior.uninstall ();
        visibilityBehavior = null;
        stopAnimation ( c );
    }

    /**
     * Returns highlight movement {@link Orientation}.
     *
     * @return highlight movement {@link Orientation}
     */
    @Nullable
    public Orientation getOrientation ()
    {
        return orientation;
    }

    /**
     * Returns highlight width.
     *
     * @return highlight width
     */
    @Nullable
    public Integer getWidth ()
    {
        return width;
    }

    /**
     * Returns highlight {@link Color}.
     *
     * @return highlight {@link Color}
     */
    @Nullable
    public Color getColor ()
    {
        return color;
    }

    /**
     * Returns highlight passes count.
     *
     * @return highlight passes count
     */
    @Nullable
    protected Integer getPasses ()
    {
        return passes;
    }

    /**
     * Returns delay between repeated animations.
     *
     * @return delay between repeated animations
     */
    @Nullable
    protected Long getDelay ()
    {
        return delay != null ? DurationUnits.get ().fromString ( delay ) : null;
    }

    /**
     * Returns single pass animation duration.
     *
     * @return single pass animation duration
     */
    @Nullable
    protected Long getDuration ()
    {
        return duration != null ? DurationUnits.get ().fromString ( duration ) : null;
    }

    /**
     * Starts background animation.
     *
     * @param c painted component
     */
    protected void playAnimation ( final C c )
    {
        if ( transitionsQueue == null )
        {
            // Checking settings
            final Orientation orientation = getOrientation ();
            final Integer width = getWidth ();
            final Color color = getColor ();
            final Integer passes = getPasses ();
            final Long duration = getDuration ();
            if ( orientation != null && width != null && color != null && passes != null && duration != null )
            {
                // Resetting position
                position = 0f;

                // Custom transition for background animations
                transitionsQueue = new QueueTransition ( true );

                // Adding delay if required
                // Delay is added first to avoid repetitive animation
                final Long delay = getDelay ();
                if ( delay != null && delay > 0 )
                {
                    transitionsQueue.add ( new IdleTransition ( passes % 2 == 0 ? 0f : 1f, delay ) );
                }

                // Adding passes
                for ( int i = 0; i < passes; i++ )
                {
                    final float from = i % 2 == 0 ? 0f : 1f;
                    final float to = i % 2 == 0 ? 1f : 0f;
                    transitionsQueue.add ( new TimedTransition<Float> ( from, to, duration ) );
                }

                // Value update listener
                transitionsQueue.addListener ( new TransitionAdapter<Float> ()
                {
                    @Override
                    public void adjusted ( final Transition transition, final Float value )
                    {
                        position = value;
                        c.repaint ();
                    }
                } );

                // Playing transition
                transitionsQueue.play ();
            }
        }
    }

    /**
     * Stops background animation.
     *
     * @param c painted component
     */
    protected void stopAnimation ( final C c )
    {
        if ( transitionsQueue != null )
        {
            // Stopping transition
            transitionsQueue.stop ();

            // Cleaning up resources
            transitionsQueue = null;

            // Resetting position
            position = 0f;
        }
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final C c, final D d, final Shape shape )
    {
        final float opacity = getOpacity ();
        if ( opacity > 0 )
        {
            final Orientation orientation = getOrientation ();
            final Integer width = getWidth ();
            final Color color = getColor ();
            if ( orientation != null && width != null && color != null )
            {
                final Rectangle b = shape.getBounds ();
                final Paint paint;
                if ( orientation.isHorizontal () )
                {
                    if ( c.getComponentOrientation ().isLeftToRight () )
                    {
                        paint = new RadialGradientPaint ( b.x - width / 2 + ( b.width + width ) * position, b.y + b.height / 2, width / 2,
                                new float[]{ 0f, 1f }, new Color[]{ color, ColorUtils.transparent () } );
                    }
                    else
                    {
                        paint = new RadialGradientPaint ( b.x + b.width + width / 2 - ( b.width + width ) * position, b.y + b.height / 2,
                                width / 2, new float[]{ 0f, 1f }, new Color[]{ color, ColorUtils.transparent () } );
                    }
                }
                else
                {
                    if ( c.getComponentOrientation ().isLeftToRight () )
                    {
                        paint = new RadialGradientPaint ( b.x + b.width / 2, b.y + b.height + width / 2 - ( b.height + width ) * position,
                                width / 2, new float[]{ 0f, 1f }, new Color[]{ color, ColorUtils.transparent () } );
                    }
                    else
                    {
                        paint = new RadialGradientPaint ( b.x + b.width / 2, b.y - width / 2 + ( b.height + width ) * position, width / 2,
                                new float[]{ 0f, 1f }, new Color[]{ color, ColorUtils.transparent () } );
                    }
                }

                final Shape ocl = GraphicsUtils.intersectClip ( g2d, shape );
                final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, opacity, opacity < 1f );
                final Paint op = GraphicsUtils.setupPaint ( g2d, paint );

                g2d.fillRect ( b.x, b.y, b.width, b.height );

                GraphicsUtils.restorePaint ( g2d, op );
                GraphicsUtils.restoreComposite ( g2d, oc, opacity < 1f );
                GraphicsUtils.restoreClip ( g2d, ocl );
            }
        }
    }
}