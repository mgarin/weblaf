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
import com.alee.api.resource.Resource;
import com.alee.utils.ImageUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import javax.swing.*;
import java.util.List;

/**
 * {@link AbstractIconSource} implementation for {@link ImageIcon} icon type.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-IconManager">How to use IconManager</a>
 * @see ImageIcon
 * @see com.alee.managers.icon.IconManager
 */
@XStreamAlias ( "ImageIcon" )
@XStreamConverter ( ImageIconSourceConverter.class )
public class ImageIconSource extends AbstractIconSource<ImageIcon>
{
    /**
     * Constructs new {@link ImageIconSource}.
     *
     * @param id       unique {@link ImageIcon} identifier
     * @param resource {@link Resource} containing {@link ImageIcon}
     */
    public ImageIconSource ( @NotNull final String id, @NotNull final Resource resource )
    {
        super ( id, resource );
    }

    /**
     * Constructs new {@link ImageIconSource}.
     *
     * @param id          unique {@link ImageIcon} identifier
     * @param resource    {@link Resource} containing {@link ImageIcon}
     * @param adjustments {@link IconAdjustment}s
     */
    public ImageIconSource ( @NotNull final String id, @NotNull final Resource resource,
                             @NotNull final IconAdjustment<ImageIcon>... adjustments )
    {
        super ( id, resource, adjustments );
    }

    /**
     * Constructs new {@link ImageIconSource}.
     *
     * @param id          unique {@link ImageIcon} identifier
     * @param resource    {@link Resource} containing {@link ImageIcon}
     * @param adjustments {@link List} of {@link IconAdjustment}s
     */
    public ImageIconSource ( @NotNull final String id, @NotNull final Resource resource,
                             @Nullable final List<IconAdjustment<ImageIcon>> adjustments )
    {
        super ( id, resource, adjustments );
    }

    @NotNull
    @Override
    public ImageIcon loadIcon ( @NotNull final Resource resource )
    {
        return ImageUtils.loadImageIcon ( resource );
    }
}