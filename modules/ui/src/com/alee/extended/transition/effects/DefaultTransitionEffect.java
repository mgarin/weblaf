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

package com.alee.extended.transition.effects;

import com.alee.extended.transition.ImageTransition;
import com.alee.utils.SwingUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mikle Garin
 */
public abstract class DefaultTransitionEffect implements TransitionEffect
{
    protected static final String DIRECTION = "DIRECTION";

    protected Map<String, Object> effectSettings = new HashMap<String, Object> ();
    protected boolean animating = false;

    /**
     * Default methods
     */

    @Override
    public long getAnimationDelay ()
    {
        return SwingUtils.frameRateDelay ( 36 );
    }

    @Override
    public boolean isAnimating ()
    {
        return animating;
    }

    public Direction getDirection ()
    {
        return get ( DIRECTION, Direction.random );
    }

    public void setDirection ( Direction direction )
    {
        put ( DIRECTION, direction );
    }

    /**
     * Effect settings storage
     */

    protected Map<String, Object> getEffectSettings ()
    {
        return effectSettings;
    }

    protected void setEffectSettings ( Map<String, Object> effectSettings )
    {
        this.effectSettings = effectSettings;
    }

    protected void put ( String property, Object value )
    {
        effectSettings.put ( property, value );
    }

    protected void remove ( String property )
    {
        effectSettings.remove ( property );
    }

    protected void clearSettings ()
    {
        effectSettings.clear ();
    }

    protected <T> T get ( String property, T defaultValue )
    {
        if ( effectSettings.containsKey ( property ) )
        {
            return ( T ) effectSettings.get ( property );
        }
        else
        {
            return defaultValue;
        }
    }

    /**
     * Default animation methods
     */

    @Override
    public boolean performAnimationTick ( ImageTransition transition )
    {
        if ( !animating )
        {
            animating = true;
            prepareAnimation ( transition );
            return false;
        }
        else
        {
            boolean finished = performAnimation ( transition );
            if ( finished )
            {
                animating = false;
            }
            return finished;
        }
    }

    /**
     * Initial animation tick used for preparations
     */
    public abstract void prepareAnimation ( ImageTransition imageTransition );

    /**
     * Single animation tick
     */
    public abstract boolean performAnimation ( ImageTransition imageTransition );
}