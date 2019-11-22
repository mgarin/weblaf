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

package com.alee.managers.icon.data;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.merge.Overwriting;
import com.alee.api.resource.Resource;
import com.alee.extended.svg.SvgIconSource;
import com.alee.managers.icon.IconException;
import com.alee.utils.CollectionUtils;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import javax.swing.*;
import java.util.List;

/**
 * Abstract {@link IconSource} implementation containing basic {@link Icon} information.
 * Implements {@link Overwriting} instead of {@link com.alee.api.merge.Mergeable} to avoid merging any {@link IconSource}s ever.
 *
 * @param <I> {@link Icon} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-IconManager">How to use IconManager</a>
 * @see com.alee.managers.icon.IconManager
 * @see ImageIconSource
 * @see SvgIconSource
 */
public abstract class AbstractIconSource<I extends Icon> implements IconSource<I>
{
    /**
     * Unique {@link Icon} identifier.
     * Used to access icon through {@link com.alee.managers.icon.IconManager}.
     */
    @NotNull
    @XStreamAsAttribute
    protected final String id;

    /**
     * {@link Resource} containing {@link Icon}.
     */
    @NotNull
    @XStreamAsAttribute
    protected final Resource resource;

    /**
     * {@link IconAdjustment} to be applied on loaded {@link Icon} for the final result.
     */
    @Nullable
    @XStreamImplicit
    protected List<IconAdjustment<I>> adjustments;

    /**
     * Constructs new {@link AbstractIconSource}.
     *
     * @param id       unique {@link Icon} identifier
     * @param resource {@link Resource} containing {@link Icon}
     */
    public AbstractIconSource ( @NotNull final String id, @NotNull final Resource resource )
    {
        this ( id, resource, ( List<IconAdjustment<I>> ) null );
    }

    /**
     * Constructs new {@link AbstractIconSource}.
     *
     * @param id          unique {@link Icon} identifier
     * @param resource    {@link Resource} containing {@link Icon}
     * @param adjustments {@link IconAdjustment}s
     */
    public AbstractIconSource ( @NotNull final String id, @NotNull final Resource resource,
                                @NotNull final IconAdjustment<I>... adjustments )
    {
        this ( id, resource, CollectionUtils.asList ( adjustments ) );
    }

    /**
     * Constructs new {@link AbstractIconSource}.
     *
     * @param id          unique {@link Icon} identifier
     * @param resource    {@link Resource} containing {@link Icon}
     * @param adjustments {@link List} of {@link IconAdjustment}s
     */
    public AbstractIconSource ( @NotNull final String id, @NotNull final Resource resource,
                                @Nullable final List<IconAdjustment<I>> adjustments )
    {
        this.id = id;
        this.resource = resource;
        this.adjustments = adjustments;
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
    public Resource getResource ()
    {
        return resource;
    }

    @NotNull
    @Override
    public I loadIcon ()
    {
        try
        {
            final I icon = loadIcon ( resource );
            if ( adjustments != null )
            {
                for ( final IconAdjustment<I> adjustment : adjustments )
                {
                    adjustment.apply ( icon );
                }
            }
            return icon;
        }
        catch ( final Exception e )
        {
            throw new IconException ( "Unable to load Icon: " + getId (), e );
        }
    }

    /**
     * Returns raw {@link Icon} loaded from {@link Resource}.
     *
     * @param resource {@link Resource} containing {@link Icon}
     * @return raw {@link Icon} loaded from {@link Resource}
     */
    @NotNull
    protected abstract I loadIcon ( @NotNull Resource resource );
}