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

package com.alee.extended.overlay;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.clone.behavior.OmitOnClone;
import com.alee.api.merge.behavior.OmitOnMerge;
import com.alee.extended.canvas.WebCanvas;
import com.alee.managers.animation.easing.Cubic;
import com.alee.managers.animation.easing.Linear;
import com.alee.managers.animation.transition.QueueTransition;
import com.alee.managers.animation.transition.TimedTransition;
import com.alee.managers.animation.transition.Transition;
import com.alee.managers.animation.transition.TransitionAdapter;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.DecorationException;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.background.AbstractBackground;
import com.alee.painter.decoration.background.IBackground;
import com.alee.utils.CollectionUtils;
import com.alee.utils.GraphicsUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Background displaying indeterminate progress.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> background type
 * @author Mikle Garin
 */
@XStreamAlias ( "ProgressOverlayBackground" )
public class ProgressOverlayBackground<C extends WebCanvas, D extends IDecoration<C, D>, I extends ProgressOverlayBackground<C, D, I>>
        extends AbstractBackground<C, D, I>
{
    /**
     * Single progress line width.
     */
    @Nullable
    @XStreamAsAttribute
    protected Integer width;

    /**
     * Progress color.
     */
    @Nullable
    @XStreamAsAttribute
    protected Color color;

    /**
     * {@link List} of {@link IBackground} implementations used to paint background under the progress.
     */
    @XStreamImplicit
    private List<IBackground> backgrounds;

    /**
     * Current progress opacity.
     */
    @OmitOnClone
    @OmitOnMerge
    protected transient float currentOpacity;

    /**
     * {@link AbstractDecorationPainter#DECORATION_STATES_PROPERTY} listener.
     */
    @OmitOnClone
    @OmitOnMerge
    protected transient PropertyChangeListener statesListener;

    /**
     * Current animation state.
     */
    @Nullable
    @OmitOnClone
    @OmitOnMerge
    protected transient AnimationState state;

    /**
     * Opacity transition.
     */
    @Nullable
    @OmitOnClone
    @OmitOnMerge
    protected transient QueueTransition opacityTransition;

    /**
     * Highlight position.
     */
    @OmitOnClone
    @OmitOnMerge
    protected transient int position;

    /**
     * Background position transition.
     */
    @Nullable
    @OmitOnClone
    @OmitOnMerge
    protected transient QueueTransition positionTransition;

    @Override
    public void activate ( @NotNull final C c, @NotNull final D d )
    {
        // Initial values
        currentOpacity = 0f;
        state = AnimationState.awaiting;

        // State change listener
        statesListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent event )
            {
                if ( c.getStates ().contains ( DecorationState.progress ) )
                {
                    if ( state == AnimationState.awaiting || state == AnimationState.hiding )
                    {
                        playAnimation ( c, d, true );
                    }
                }
                else
                {
                    if ( state == AnimationState.displaying || state == AnimationState.animating )
                    {
                        stopAnimation ( c, d, true );
                    }
                }
            }
        };
        c.addPropertyChangeListener ( AbstractDecorationPainter.DECORATION_STATES_PROPERTY, statesListener );

        // Animation start
        if ( c.getStates ().contains ( DecorationState.progress ) )
        {
            playAnimation ( c, d, false );
        }
    }

    @Override
    public void deactivate ( @NotNull final C c, @NotNull final D d )
    {
        // State change listener
        c.removePropertyChangeListener ( AbstractDecorationPainter.DECORATION_STATES_PROPERTY, statesListener );
        statesListener = null;

        // Animation stop
        stopAnimation ( c, d, false );
    }

    @Override
    public float getOpacity ( @NotNull final C c, @NotNull final D d )
    {
        return opacity != null ? opacity : 0.5f;
    }

    /**
     * Returns single progress line width.
     *
     * @param c {@link WebCanvas}
     * @param d {@link IDecoration}
     * @return single progress line width
     */
    public int getWidth ( @NotNull final C c, @NotNull final D d )
    {
        if ( width == null )
        {
            throw new DecorationException ( "Progress line width must be specified" );
        }
        if ( width <= 0 )
        {
            throw new DecorationException ( "Progress line width must be greater than zero" );
        }
        return width;
    }

    /**
     * Returns progress color.
     *
     * @param c {@link WebCanvas}
     * @param d {@link IDecoration}
     * @return progress color
     */
    @NotNull
    public Color getColor ( @NotNull final C c, @NotNull final D d )
    {
        if ( color == null )
        {
            throw new DecorationException ( "Progress color must be specified" );
        }
        return color;
    }

    /**
     * Returns {@link List} of {@link IBackground} implementations used to paint background under the progress.
     *
     * @param c {@link WebCanvas}
     * @param d {@link IDecoration}
     * @return {@link List} of {@link IBackground} implementations used to paint background under the progress
     */
    @Nullable
    protected List<IBackground> getBackgrounds ( @NotNull final C c, @NotNull final D d )
    {
        return backgrounds;
    }

    /**
     * Starts background animation.
     *
     * @param c             {@link WebCanvas}
     * @param d             {@link IDecoration}
     * @param adjustOpacity whether or not opacity needs to be adjusted
     */
    protected void playAnimation ( @NotNull final C c, @NotNull final D d, final boolean adjustOpacity )
    {
        // Updating state
        state = adjustOpacity ? AnimationState.displaying : AnimationState.animating;

        // Stop opacity transition in either case
        if ( opacityTransition != null )
        {
            opacityTransition.stop ();
            opacityTransition = null;
        }

        // Opacity transition
        if ( adjustOpacity )
        {
            opacityTransition = new QueueTransition ( false );
            opacityTransition.add ( new TimedTransition<Float> ( currentOpacity, getOpacity ( c, d ), new Cubic.Out (), 600L ) );
            opacityTransition.addListener ( new TransitionAdapter<Float> ()
            {
                @Override
                public void adjusted ( final Transition transition, final Float value )
                {
                    currentOpacity = value;
                    c.repaint ();
                }

                @Override
                public void finished ( final Transition transition, final Float value )
                {
                    state = AnimationState.animating;
                }
            } );
            opacityTransition.play ();
        }
        else
        {
            currentOpacity = getOpacity ( c, d );
        }

        // Background position transition
        if ( positionTransition == null )
        {
            position = 0;
            positionTransition = new QueueTransition ( true );
            positionTransition.add ( new TimedTransition<Integer> ( 0, getWidth ( c, d ) * 2, new Linear (), 600L ) );
            positionTransition.addListener ( new TransitionAdapter<Integer> ()
            {
                @Override
                public void adjusted ( final Transition transition, final Integer value )
                {
                    position = value;
                    c.repaint ();
                }
            } );
            positionTransition.play ();
        }
    }

    /**
     * Stops background animation.
     *
     * @param c             {@link WebCanvas}
     * @param d             {@link IDecoration}
     * @param adjustOpacity whether or not opacity needs to be adjusted
     */
    protected void stopAnimation ( @NotNull final C c, @NotNull final D d, final boolean adjustOpacity )
    {
        // Stop opacity transition in either case
        if ( opacityTransition != null )
        {
            opacityTransition.stop ();
            opacityTransition = null;
        }

        // Decide how to proceed with animation
        if ( adjustOpacity )
        {
            // Opacity transition
            opacityTransition = new QueueTransition ( false );
            opacityTransition.add ( new TimedTransition<Float> ( currentOpacity, 0f, new Cubic.Out (), 400L ) );
            opacityTransition.addListener ( new TransitionAdapter<Float> ()
            {
                @Override
                public void adjusted ( final Transition transition, final Float value )
                {
                    currentOpacity = value;
                    c.repaint ();
                }

                @Override
                public void finished ( final Transition transition, final Float value )
                {
                    // Stopping background position transition
                    if ( positionTransition != null )
                    {
                        positionTransition.stop ();
                        positionTransition = null;
                        position = 0;
                    }

                    // Cleaning up opacity transition
                    opacityTransition = null;

                    // Updating state
                    state = AnimationState.awaiting;
                }
            } );
            opacityTransition.play ();
        }
        else if ( positionTransition != null )
        {
            // Stopping background position transition
            positionTransition.stop ();
            positionTransition = null;
            position = 0;
        }

        // Updating state
        state = adjustOpacity ? AnimationState.hiding : AnimationState.awaiting;
        currentOpacity = 0f;
    }

    @Override
    public void paint ( @NotNull final Graphics2D g2d, @NotNull final Rectangle bounds, @NotNull final C c, @NotNull final D d,
                        @NotNull final Shape shape )
    {
        if ( currentOpacity > 0f )
        {
            final Rectangle shapeBounds = shape.getBounds ();
            final int w = shapeBounds.width;
            final int h = shapeBounds.height;
            if ( w > 0 && h > 0 )
            {
                final Object aa = GraphicsUtils.setupAntialias ( g2d );
                final Shape oldClip = GraphicsUtils.intersectClip ( g2d, shape );
                final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, currentOpacity, currentOpacity < 1f );

                // Backgrounds
                final List<IBackground> backgrounds = getBackgrounds ( c, d );
                if ( CollectionUtils.notEmpty ( backgrounds ) )
                {
                    for ( final IBackground background : backgrounds )
                    {
                        background.paint ( g2d, bounds, c, d, shape );
                    }
                }

                // Progress lines
                // todo Support for RTL?
                final Paint oldPaint = GraphicsUtils.setupPaint ( g2d, getColor ( c, d ) );
                final int x = shapeBounds.x;
                final int y = shapeBounds.y;
                final int pw = getWidth ( c, d );
                for ( int i = position % ( pw * 2 ); i < w + 2 * h; i += pw * 2 )
                {
                    if ( i > 0 )
                    {
                        final GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );

                        // Top side lines
                        gp.moveTo (
                                x + ( i < w ? i : w ),
                                y + ( i < w ? 0 : Math.min ( h, i - w ) )
                        );
                        if ( i - pw < w && i > w )
                        {
                            gp.lineTo ( x + w, y );
                        }
                        gp.lineTo (
                                x + ( i - pw < w ? Math.max ( 0, i - pw ) : w ),
                                y + ( i - pw < w ? 0 : i - pw - w )
                        );

                        // Bottom side lines
                        gp.lineTo (
                                x + ( i - pw < h ? 0 : i - pw - h ),
                                y + ( i - pw < h ? Math.max ( 0, i - pw ) : h )
                        );
                        if ( i - pw < h && i > h )
                        {
                            gp.lineTo ( x, y + h );
                        }
                        gp.lineTo (
                                x + ( i < h ? 0 : Math.min ( w, i - h ) ),
                                y + ( i < h ? i : h )
                        );

                        gp.closePath ();
                        g2d.fill ( gp );
                    }
                }
                GraphicsUtils.restorePaint ( g2d, oldPaint );

                GraphicsUtils.restoreComposite ( g2d, oc, currentOpacity < 1f );
                GraphicsUtils.restoreClip ( g2d, oldClip );
                GraphicsUtils.restoreAntialias ( g2d, aa );
            }
        }
    }

    /**
     * Simple enumeration representing animation states.
     */
    protected enum AnimationState
    {
        awaiting,
        displaying,
        animating,
        hiding
    }
}