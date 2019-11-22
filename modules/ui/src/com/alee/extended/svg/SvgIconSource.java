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

package com.alee.extended.svg;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.resource.Resource;
import com.alee.managers.icon.data.AbstractIconSource;
import com.alee.managers.icon.data.IconAdjustment;
import com.alee.utils.CollectionUtils;
import com.kitfox.svg.SVGUniverse;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.awt.*;
import java.util.List;

/**
 * {@link AbstractIconSource} implementation for {@link SvgIcon} icon type.
 *
 * @author Mikle Garin
 * @see SvgIcon
 * @see SVGUniverse
 */
@XStreamAlias ( "SvgIcon" )
@XStreamConverter ( SvgIconSourceConverter.class )
public class SvgIconSource extends AbstractIconSource<SvgIcon>
{
    /**
     * Preferred {@link SvgIcon} size.
     */
    @Nullable
    @XStreamAsAttribute
    protected final Dimension size;

    /**
     * Constructs new {@link SvgIconSource}.
     *
     * @param id       unique {@link SvgIcon} identifier
     * @param resource {@link Resource} containing {@link SvgIcon} data
     */
    public SvgIconSource ( @NotNull final String id, @NotNull final Resource resource )
    {
        this ( id, resource, null, ( List<IconAdjustment<SvgIcon>> ) null );
    }

    /**
     * Constructs new {@link SvgIconSource}.
     *
     * @param id       unique {@link SvgIcon} identifier
     * @param resource {@link Resource} containing {@link SvgIcon} data
     * @param size     preferred {@link SvgIcon} size
     */
    public SvgIconSource ( @NotNull final String id, @NotNull final Resource resource, @Nullable final Dimension size )
    {
        this ( id, resource, size, ( List<IconAdjustment<SvgIcon>> ) null );
    }

    /**
     * Constructs new {@link SvgIconSource}.
     *
     * @param id          unique {@link SvgIcon} identifier
     * @param resource    {@link Resource} containing {@link SvgIcon} data
     * @param adjustments {@link IconAdjustment}s
     */
    public SvgIconSource ( @NotNull final String id, @NotNull final Resource resource,
                           @NotNull final IconAdjustment<SvgIcon>... adjustments )
    {
        this ( id, resource, null, CollectionUtils.asList ( adjustments ) );
    }

    /**
     * Constructs new {@link SvgIconSource}.
     *
     * @param id          unique {@link SvgIcon} identifier
     * @param resource    {@link Resource} containing {@link SvgIcon} data
     * @param size        preferred {@link SvgIcon} size
     * @param adjustments {@link IconAdjustment}s
     */
    public SvgIconSource ( @NotNull final String id, @NotNull final Resource resource, @Nullable final Dimension size,
                           @NotNull final IconAdjustment<SvgIcon>... adjustments )
    {
        this ( id, resource, size, CollectionUtils.asList ( adjustments ) );
    }

    /**
     * Constructs new {@link SvgIconSource}.
     *
     * @param id          unique {@link SvgIcon} identifier
     * @param resource    {@link Resource} containing {@link SvgIcon} data
     * @param adjustments {@link List} of {@link IconAdjustment}s
     */
    public SvgIconSource ( @NotNull final String id, @NotNull final Resource resource,
                           @Nullable final List<IconAdjustment<SvgIcon>> adjustments )
    {
        this ( id, resource, null, adjustments );
    }

    /**
     * Constructs new {@link SvgIconSource}.
     *
     * @param id          unique {@link SvgIcon} identifier
     * @param resource    {@link Resource} containing {@link SvgIcon} data
     * @param size        preferred {@link SvgIcon} size
     * @param adjustments {@link List} of {@link IconAdjustment}s
     */
    public SvgIconSource ( @NotNull final String id, @NotNull final Resource resource, @Nullable final Dimension size,
                           @Nullable final List<IconAdjustment<SvgIcon>> adjustments )
    {
        super ( id, resource, adjustments );
        this.size = size;
    }

    /**
     * Returns preferred icon size.
     *
     * @return preferred icon size
     */
    @Nullable
    public Dimension getSize ()
    {
        return size;
    }

    @NotNull
    @Override
    public SvgIcon loadIcon ( @NotNull final Resource resource )
    {
        final int width = size != null ? size.width : 16;
        final int height = size != null ? size.height : 16;
        return new SvgIcon ( resource, width, height );
    }
}