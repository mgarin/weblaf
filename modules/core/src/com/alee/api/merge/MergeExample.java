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

package com.alee.api.merge;

import com.alee.api.merge.behavior.BasicMergeBehavior;
import com.alee.api.merge.behavior.ReflectionMergeBehavior;
import com.alee.api.merge.nullresolver.SkippingNullResolver;
import com.alee.api.merge.type.RelatedTypeMergePolicy;

import java.util.Arrays;

/**
 * @author Mikle Garin
 */

public class MergeExample
{
    public static void main ( final String[] args )
    {
        // Instantiating basic merge
        final Merge merge = new Merge (
                new SkippingNullResolver (),
                new RelatedTypeMergePolicy (),
                new BasicMergeBehavior (),
                // new IndexArrayMergeBehavior (),
                new ReflectionMergeBehavior ()
        );

        // Merging simple objects
        System.out.println ( merge.merge ( "Text 1", "Text 2" ) );
        System.out.println ( merge.merge ( 1, 20 ) );
        System.out.println ( merge.merge ( true, false ) );

        // Merging complex objects
        final SampleObject object1 = new SampleObject ( "Object1", 1, "One", "Two", "Three" );
        final SampleObject object2 = new SampleObject ( null, 2, "Three", "Four" );
        System.out.println ( merge.merge ( object1, object2 ) );
    }

    /**
     * Sample object for merging.
     */
    public static class SampleObject
    {
        private final String title;
        private final int value;
        private final String[] states;
        private final SampleObject object;

        public SampleObject ( final String title, final int value, final String... states )
        {
            this.title = title;
            this.value = value;
            this.states = states;
            this.object = states.length > 0 ? new SampleObject ( states[ 0 ], value ) : null;
        }

        @Override
        public String toString ()
        {
            return title + " - " + value + " - " + Arrays.toString ( states ) + " [ " + object + " ]";
        }
    }
}