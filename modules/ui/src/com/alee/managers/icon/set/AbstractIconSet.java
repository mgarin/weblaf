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

package com.alee.managers.icon.set;

import com.alee.managers.icon.data.IconData;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract icon set containing implementations for most common methods.
 *
 * @author Mikle Garin
 */

public abstract class AbstractIconSet implements IconSet
{
    /**
     * Unique icon set ID.
     */
    private final String id;

    /**
     * Icons information kept for lazy loading.
     * It contains: icon key -> icon information
     */
    private final Map<String, IconData> iconsData;

    /**
     * Loaded icons cache.
     * It contains: icon key -> weak icon reference
     */
    private final Map<String, Icon> cache;

    /**
     * Constructs new abstract icon set.
     *
     * @param id icon set ID
     */
    public AbstractIconSet ( final String id )
    {
        super ();
        this.id = id;
        iconsData = new HashMap<String, IconData> ( 100 );
        cache = new HashMap<String, Icon> ( 30 );
    }

    @Override
    public String getId ()
    {
        return id;
    }

    @Override
    public List<String> getIds ()
    {
        return new ArrayList<String> ( iconsData.keySet () );
    }

    @Override
    public void addIcon ( final IconData icon )
    {
        // Saving lazy icon information
        iconsData.put ( icon.getId (), icon );

        // Clearing icon cache
        cache.remove ( icon.getId () );
    }

    @Override
    public Icon getIcon ( final String id )
    {
        Icon icon = cache.get ( id );
        if ( icon == null )
        {
            final IconData iconData = iconsData.get ( id );
            if ( iconData != null )
            {
                icon = iconData.loadIcon ();
                cache.put ( id, icon );
            }
        }
        return icon;
    }
}