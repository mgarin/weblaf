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

package com.alee.managers.animation;

import com.alee.managers.animation.event.EventDispatchThreadHandler;
import com.alee.managers.animation.event.EventHandler;
import com.alee.managers.animation.pipeline.AnimationPipelineFactory;
import com.alee.managers.animation.pipeline.TimedAnimationPipelineFactory;
import com.alee.managers.animation.transition.Transition;
import com.alee.managers.animation.types.*;
import com.alee.utils.ReflectUtils;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This manager provides API to play customizable transitions.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-AnimationManager">How to use AnimationManager</a>
 */
public final class AnimationManager
{
    /**
     * Registered transition types.
     */
    private static Map<Class, TransitionType> transitionTypes;

    /**
     * Animation pipeline factory.
     */
    private static AnimationPipelineFactory pipelineFactory;

    /**
     * Default {@link EventHandler} offered by this manager.
     */
    private static EventHandler eventHandler;

    /**
     * Whether {@link AnimationManager} is initialized or not.
     */
    private static boolean initialized = false;

    /**
     * Initializes {@link AnimationManager} settings.
     */
    public static synchronized void initialize ()
    {
        if ( !initialized )
        {
            // Registering basic type transitions
            transitionTypes = new HashMap<Class, TransitionType> ();
            transitionTypes.put ( int.class, new IntegerTransitionType () );
            transitionTypes.put ( Integer.class, new IntegerTransitionType () );
            transitionTypes.put ( long.class, new LongTransitionType () );
            transitionTypes.put ( Long.class, new LongTransitionType () );
            transitionTypes.put ( float.class, new FloatTransitionType () );
            transitionTypes.put ( Float.class, new FloatTransitionType () );
            transitionTypes.put ( double.class, new DoubleTransitionType () );
            transitionTypes.put ( Double.class, new DoubleTransitionType () );
            transitionTypes.put ( Color.class, new ColorTransitionType () );

            // Animation pipeline factory
            pipelineFactory = new TimedAnimationPipelineFactory ();

            // Event handler
            eventHandler = EventDispatchThreadHandler.get ();

            // Updating initialization mark
            initialized = true;
        }
    }

    /**
     * Returns animation pipeline factory.
     *
     * @return animation pipeline factory
     */
    public static AnimationPipelineFactory getPipelineFactory ()
    {
        return pipelineFactory;
    }

    /**
     * Sets animation pipeline factory.
     *
     * @param factory animation pipeline factory
     */
    public static void setPipelineFactory ( final AnimationPipelineFactory factory )
    {
        // todo Should stop all pipelines from old factory
        // todo Should migrate transitions to new pipelines
        AnimationManager.pipelineFactory = factory;
    }

    /**
     * Returns default {@link EventHandler} offered by this manager.
     *
     * @return default {@link EventHandler} offered by this manager
     */
    public static EventHandler getEventHandler ()
    {
        return eventHandler;
    }

    /**
     * Sets default {@link EventHandler} to be offered by this manager.
     *
     * @param eventHandler new default {@link EventHandler} to be offered by this manager
     */
    public static void setEventHandler ( final EventHandler eventHandler )
    {
        AnimationManager.eventHandler = eventHandler;
    }

    /**
     * Returns {@link com.alee.managers.animation.types.TransitionType} implementation for the specified value class.
     *
     * @param type value class
     * @param <V>  value class type
     * @return {@link com.alee.managers.animation.types.TransitionType} implementation for the specified value class
     */
    public static <V> TransitionType<V> getTransitionType ( final Class<V> type )
    {
        TransitionType transitionType = transitionTypes.get ( type );
        if ( transitionType == null )
        {
            Class superType = null;
            for ( final Map.Entry<Class, TransitionType> entry : transitionTypes.entrySet () )
            {
                if ( entry.getKey ().isAssignableFrom ( type ) )
                {
                    superType = entry.getKey ();
                    break;
                }
            }
            if ( superType != null )
            {
                transitionType = transitionTypes.get ( superType );
                transitionTypes.put ( type, transitionType );
            }
            else
            {
                final String cls = ReflectUtils.getClassName ( TransitionType.class );
                throw new AnimationException ( "Unable to find " + cls + " implementation for value type: " + type );
            }
        }
        return transitionType;
    }

    /**
     * Asks manager to play each of the specified transitions on preferred {@link com.alee.managers.animation.pipeline.AnimationPipeline}.
     *
     * @param transitions transitions to play
     */
    public static void play ( final Transition... transitions )
    {
        for ( final Transition transition : transitions )
        {
            pipelineFactory.getPipeline ( transition ).play ( transition );
        }
    }

    /**
     * Asks manager to play each of the specified transitions on preferred {@link com.alee.managers.animation.pipeline.AnimationPipeline}.
     *
     * @param transitions transitions to play
     */
    public static void play ( final List<Transition> transitions )
    {
        for ( final Transition transition : transitions )
        {
            pipelineFactory.getPipeline ( transition ).play ( transition );
        }
    }

    /**
     * Asks manager to stop playing specified transitions.
     *
     * @param transitions transitions to stop playing
     */
    public static void stop ( final Transition... transitions )
    {
        for ( final Transition transition : transitions )
        {
            pipelineFactory.getPipeline ( transition ).stop ( transition );
        }
    }

    /**
     * Asks manager to stop playing specified transitions.
     *
     * @param transitions transitions to stop playing
     */
    public static void stop ( final List<Transition> transitions )
    {
        for ( final Transition transition : transitions )
        {
            pipelineFactory.getPipeline ( transition ).stop ( transition );
        }
    }
}