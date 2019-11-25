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

package com.alee.managers.icon;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.managers.icon.set.IconSet;
import com.alee.managers.style.SkinExtension;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * Runtime {@link SkinExtension} containing extra {@link IconSet}s for {@link com.alee.managers.style.Skin}.
 *
 * @author Mikle Garin
 */
public class IconSetExtension implements SkinExtension
{
    /**
     * {@link IconSetExtension} identifier.
     */
    @NotNull
    protected final String id;

    /**
     * {@link List} of supported {@link com.alee.managers.style.Skin} identifiers.
     */
    @NotNull
    protected final List<String> skinIds;

    /**
     * {@link List} of {@link IconSet}s in this {@link IconSetExtension}.
     */
    @NotNull
    protected final List<IconSet> iconSets;

    /**
     * Constructs new {@link IconSetExtension}.
     *
     * @param id       {@link IconSetExtension} identifier
     * @param skinId   supported {@link com.alee.managers.style.Skin} identifier
     * @param iconSets {@link IconSet}s for this {@link IconSetExtension}
     */
    public IconSetExtension ( @NotNull final String id, @NotNull final String skinId, @NotNull final IconSet... iconSets )
    {
        this ( id, CollectionUtils.asList ( skinId ), CollectionUtils.asList ( iconSets ) );
    }

    /**
     * Constructs new {@link IconSetExtension}.
     *
     * @param id       {@link IconSetExtension} identifier
     * @param skinIds  {@link List} of supported {@link com.alee.managers.style.Skin} identifiers
     * @param iconSets {@link IconSet}s for this {@link IconSetExtension}
     */
    public IconSetExtension ( @NotNull final String id, @NotNull final List<String> skinIds, @NotNull final IconSet... iconSets )
    {
        this ( id, skinIds, CollectionUtils.asList ( iconSets ) );
    }

    /**
     * Constructs new {@link IconSetExtension}.
     *
     * @param id       {@link IconSetExtension} identifier
     * @param skinId   supported {@link com.alee.managers.style.Skin} identifier
     * @param iconSets {@link List} of {@link IconSet}s for this {@link IconSetExtension}.
     */
    public IconSetExtension ( @NotNull final String id, @NotNull final String skinId, @NotNull final List<IconSet> iconSets )
    {
        this ( id, CollectionUtils.asList ( skinId ), iconSets );
    }

    /**
     * Constructs new {@link IconSetExtension}.
     *
     * @param id       {@link IconSetExtension} identifier
     * @param skinIds  {@link List} of supported {@link com.alee.managers.style.Skin} identifiers
     * @param iconSets {@link List} of {@link IconSet}s for this {@link IconSetExtension}.
     */
    public IconSetExtension ( @NotNull final String id, @NotNull final List<String> skinIds, @NotNull final List<IconSet> iconSets )
    {
        this.id = id;
        this.skinIds = skinIds;
        this.iconSets = iconSets;
    }

    @Override
    @NotNull
    public String getId ()
    {
        return id;
    }

    @Override
    @Nullable
    public Icon getIcon ()
    {
        return null;
    }

    @Override
    @Nullable
    public String getTitle ()
    {
        return null;
    }

    @Override
    @Nullable
    public String getDescription ()
    {
        return null;
    }

    @Override
    @Nullable
    public String getAuthor ()
    {
        return null;
    }

    @Override
    public boolean isSupported ( @NotNull final String skinId )
    {
        return skinIds.contains ( skinId );
    }

    @Override
    @NotNull
    public List<IconSet> getIconSets ()
    {
        return iconSets;
    }
}