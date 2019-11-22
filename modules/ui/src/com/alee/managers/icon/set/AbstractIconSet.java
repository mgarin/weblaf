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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.clone.behavior.OmitOnClone;
import com.alee.api.merge.Overwriting;
import com.alee.api.merge.behavior.OmitOnMerge;
import com.alee.managers.icon.IconException;
import com.alee.managers.icon.IconManager;
import com.alee.managers.icon.data.IconSource;

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
 * @see IconSet
 * @see com.alee.managers.icon.IconManager
 */
public abstract class AbstractIconSet implements IconSet, Overwriting, Cloneable, Serializable
{
    /**
     * Unique {@link IconSet} identifier.
     */
    @NotNull
    protected final String id;

    /**
     * {@link IconSource} cache used for loading icons lazily.
     * It contains: {@link Icon} identifier -> {@link IconSource}.
     */
    @NotNull
    protected final Map<String, IconSource> iconsData;

    /**
     * Loaded {@link Icon}s cache.
     * It contains: {@link Icon} identifier -> weak reference to loaded {@link Icon}.
     */
    @Nullable
    @OmitOnClone
    @OmitOnMerge
    protected transient Map<String, Icon> cache;

    /**
     * Constructs new {@link AbstractIconSet}.
     *
     * @param id unique {@link IconSet} identifier
     */
    public AbstractIconSet ( @NotNull final String id )
    {
        this.id = id;
        this.iconsData = new ConcurrentHashMap<String, IconSource> ( 50 );
    }

    @NotNull
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

    @NotNull
    @Override
    public List<String> getIds ()
    {
        return new ArrayList<String> ( iconsData.keySet () );
    }

    @Override
    public void addIcon ( @NotNull final IconSource icon )
    {
        iconsData.put ( icon.getId (), icon );
        if ( cache != null )
        {
            cache.remove ( icon.getId () );
        }
        if ( IconManager.hasIconSet ( getId () ) )
        {
            IconManager.clearIconCache ( icon );
        }
    }

    @Nullable
    @Override
    public Icon findIcon ( @NotNull final String id )
    {
        Icon icon = cache != null ? cache.get ( id ) : null;
        if ( icon == null )
        {
            final IconSource iconSource = iconsData.get ( id );
            if ( iconSource != null )
            {
                try
                {
                    icon = iconSource.loadIcon ();
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