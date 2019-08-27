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

package com.alee.managers.style;

import com.alee.api.Identifiable;
import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.managers.icon.set.IconSet;

import javax.swing.*;
import java.util.List;

/**
 * Base interface for any skin extension that might exist.
 * It doesn't provide any specific methods for application because it heavily depends on implementation.
 * You can find basic implementation within {@link com.alee.managers.style.data.SkinInfo#applyExtension(SkinExtension)} method.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see StyleManager
 */
public interface SkinExtension extends Identifiable
{
    /**
     * Returns unique extension ID.
     * Used by skins to handle extensions.
     *
     * @return unique extension ID
     */
    @Nullable
    @Override
    public String getId ();

    /**
     * Returns extension {@link Icon}.
     *
     * @return extension {@link Icon}
     */
    public Icon getIcon ();

    /**
     * Returns extension title.
     *
     * @return extension title
     */
    public String getTitle ();

    /**
     * Returns extension description.
     *
     * @return extension description
     */
    public String getDescription ();

    /**
     * Returns extension author.
     *
     * @return extension author
     */
    public String getAuthor ();

    /**
     * Returns whether or not skin with the specified ID is supported by this extension.
     *
     * @param skinId ID of the skin to process
     * @return true if skin with the specified ID is supported by this extension, false otherwise
     */
    public boolean isSupported ( String skinId );

    /**
     * Returns skin icon sets.
     *
     * @return skin icon sets
     */
    public List<IconSet> getIconSets ();
}