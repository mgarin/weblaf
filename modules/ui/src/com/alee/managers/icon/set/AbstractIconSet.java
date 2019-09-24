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

import com.alee.api.annotations.Nullable;
import com.alee.api.clone.behavior.OmitOnClone;
import com.alee.api.merge.Overwriting;
import com.alee.api.merge.behavior.OmitOnMerge;
import com.alee.managers.icon.IconException;
import com.alee.managers.icon.data.AbstractIconData;
import com.alee.utils.TextUtils;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract {@link IconSet} common method implementations.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-IconManager">How to use IconManager</a>
 * @see com.alee.managers.icon.IconManager
 */
public abstract class AbstractIconSet implements IconSet, Overwriting, Cloneable, Serializable
{
    /**
     * Unique {@link IconSet} identifier.
     */
    private final String id;

    /**
     * {@link AbstractIconData} cache used for loading icons lazily.
     * It contains: {@link Icon} identifier -> {@link AbstractIconData}.
     */
    private final Map<String, AbstractIconData> iconsData;

    /**
     * Loaded {@link Icon}s cache.
     * It contains: {@link Icon} identifier -> weak reference to loaded {@link Icon}.
     */
    @OmitOnClone
    @OmitOnMerge
    private volatile transient Map<String, Icon> cache;

    /**
     * Constructs new {@link AbstractIconSet}.
     *
     * @param id unique {@link IconSet} identifier
     */
    public AbstractIconSet ( final String id )
    {
        // Checking that ID is appropriate
        if ( TextUtils.isEmpty ( id ) )
        {
            throw new IconException ( "IconSet cannot have empty identifier" );
        }
        this.id = id;

        // Initializing icon information cache
        this.iconsData = new ConcurrentHashMap<String, AbstractIconData> ( 100 );
    }

    @Nullable
    @Override
    public String getId ()
    {
        return id;
    }

    @Override
    public boolean isOverwrite ()
    {
        return true;
    }

    @Override
    public List<String> getIds ()
    {
        return new ArrayList<String> ( iconsData.keySet () );
    }

    @Override
    public void addIcon ( final AbstractIconData icon )
    {
        final String id = icon.getId ();
        if ( id == null )
        {
            throw new IconException ( "Icon identifier must not be nul" );
        }
        iconsData.put ( id, icon );
        if ( cache != null )
        {
            cache.remove ( id );
        }
    }

    @Override
    public Icon getIcon ( final String id )
    {
        if ( id == null )
        {
            throw new IconException ( "Icon identifier must not be nul" );
        }
        Icon icon = cache != null ? cache.get ( id ) : null;
        if ( icon == null )
        {
            final AbstractIconData iconData = iconsData.get ( id );
            if ( iconData != null )
            {
                try
                {
                    icon = iconData.getIcon ();
                    if ( cache == null )
                    {
                        synchronized ( this )
                        {
                            if ( cache == null )
                            {
                                cache = new ConcurrentHashMap<String, Icon> ( 30 );
                            }
                        }
                    }
                    if ( icon == null )
                    {
                        throw new IconException ( "Unable to load icon: " + id );
                    }
                    cache.put ( id, icon );
                }
                catch ( final Exception e )
                {
                    throw new IconException ( "Unable to load icon: " + id, e );
                }
            }
        }
        return icon;
    }
}