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

import com.alee.api.IconSupport;
import com.alee.api.Identifiable;
import com.alee.api.TitleSupport;

import javax.swing.*;

/**
 * Base interface for any skin extension that might exist.
 * It doesn't provide any specific methods for application because it heavily depends on implementation.
 * You can find basic implementation within {@link com.alee.managers.style.data.SkinInfo#applyExtension(SkinExtension)} method.
 *
 * @author Mikle Garin
 */

public interface SkinExtension extends IconSupport, TitleSupport, Identifiable
{
    /**
     * Returns unique extension ID.
     * Used by skins to handle extensions.
     *
     * @return unique extension ID
     */
    @Override
    public String getId ();

    /**
     * Returns extension icon.
     *
     * @return extension icon
     */
    @Override
    public Icon getIcon ();

    /**
     * Returns extension title.
     *
     * @return extension title
     */
    @Override
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
     * Returns whether or not specified skin is supported by this extension.
     *
     * @param skin skin to process
     * @return true if specified skin is supported by this extension, false otherwise
     */
    public boolean isSupported ( Skin skin );
}