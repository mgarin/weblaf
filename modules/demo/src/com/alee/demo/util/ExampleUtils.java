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

package com.alee.demo.util;

import com.alee.demo.api.example.FeatureState;
import com.alee.utils.UtilityException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utilities for {@link com.alee.demo.api.example.Example}s.
 *
 * @author Mikle Garin
 */
public final class ExampleUtils
{
    /**
     * Private constructor to avoid instantiation.
     */
    private ExampleUtils ()
    {
        throw new UtilityException ( "Utility classes are not meant to be instantiated" );
    }

    /**
     * Returns resulting {@link com.alee.demo.api.example.FeatureState} for the specified list.
     *
     * @param states {@link com.alee.demo.api.example.FeatureState} list
     * @return resulting {@link com.alee.demo.api.example.FeatureState} for the specified list
     */
    public static FeatureState getResultingState ( final List<FeatureState> states )
    {
        final int all = states.size ();
        final FeatureState[] featureStates = FeatureState.values ();

        // Counting each state
        final Map<FeatureState, Integer> count = new HashMap<FeatureState, Integer> ( featureStates.length );
        for ( final FeatureState state : featureStates )
        {
            count.put ( state, 0 );
        }
        for ( final FeatureState state : states )
        {
            count.put ( state, count.get ( state ) + 1 );
        }

        // Checking if all features has some specific state
        if ( all > 0 )
        {
            for ( final FeatureState state : featureStates )
            {
                if ( count.get ( state ) == all )
                {
                    return state;
                }
            }
        }

        // Returning updated state if anything non-common exists
        return count.get ( FeatureState.beta ) > 0 || count.get ( FeatureState.release ) > 0 || count.get ( FeatureState.updated ) > 0 ||
                count.get ( FeatureState.deprecated ) > 0 ? FeatureState.updated : FeatureState.common;
    }
}