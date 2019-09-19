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

package com.alee.utils.jar;

import com.alee.api.annotations.NotNull;
import com.alee.utils.swing.EnumLazyIconProvider;

import javax.swing.*;

/**
 * This enumeration represents possible {@link JarEntry} types.
 *
 * @author Mikle Garin
 */
public enum JarEntryType
{
    /**
     * Jar entry.
     */
    JAR,

    /**
     * Package entry.
     */
    PACKAGE,

    /**
     * Java class entry.
     */
    CLASS,

    /**
     * Java class source entry.
     */
    JAVA,

    /**
     * File entry.
     */
    FILE;

    /**
     * Returns JAR entry type icon.
     *
     * @return JAR entry type icon
     */
    @NotNull
    public Icon getIcon ()
    {
        return EnumLazyIconProvider.getIcon ( this, "icons/" );
    }
}