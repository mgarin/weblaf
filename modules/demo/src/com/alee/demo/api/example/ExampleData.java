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

package com.alee.demo.api.example;

import com.alee.extended.tab.DocumentData;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mikle Garin
 */
public final class ExampleData extends DocumentData
{
    /**
     * Examples data cache.
     */
    private static final Map<String, ExampleData> cache = new HashMap<String, ExampleData> ();

    /**
     * Example this data represents.
     */
    private final Example example;

    /**
     * Constructs new example data.
     *
     * @param example example to construct data for
     */
    private ExampleData ( final Example example )
    {
        super ( example.getId (), example.getIcon (), example.getTitle (), example.createContent () );
        // todo setForeground ( example.getFeatureState ().getForeground () );
        this.example = example;
    }

    /**
     * Returns example this data represents.
     *
     * @return example this data represents
     */
    public Example getExample ()
    {
        return example;
    }

    /**
     * Returns new or cached example data.
     *
     * @param example example to retrieve data for
     * @return new or cached example data
     */
    public static ExampleData forExample ( final Example example )
    {
        final String id = example.getId ();
        if ( !cache.containsKey ( id ) )
        {
            cache.put ( id, new ExampleData ( example ) );
        }
        return cache.get ( id );
    }
}