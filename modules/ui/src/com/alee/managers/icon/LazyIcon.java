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

import javax.swing.*;
import java.awt.*;

/**
 * Lazy {@link Icon} implementation.
 * It represents an {@link Icon} that will be loaded on demand from the {@link IconManager}.
 * Actual {@link Icon} is referenced through {@link #id} which is unique for all icons within {@link IconManager}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-IconManager">How to use IconManager</a>
 * @see IconManager
 */
public final class LazyIcon implements Icon
{
    /**
     * Referenced icon ID.
     */
    private final String id;

    /**
     * Constructs new {@link LazyIcon}.
     * It will retrieve icon from {@link IconManager} using the specified {@code id}.
     *
     * @param id icon ID
     * @see IconManager
     */
    public LazyIcon ( final String id )
    {
        super ();
        this.id = id;
    }

    /**
     * Returns referenced {@link Icon} identifier.
     *
     * @return referenced {@link Icon} identifier
     */
    public String getId ()
    {
        return id;
    }

    /**
     * Returns actual {@link Icon} used by this {@link LazyIcon}.
     * Use this method wisely as it will load {@link Icon} into memory.
     *
     * @param <I> {@link Icon} type
     * @return actual {@link Icon} used by this {@link LazyIcon}
     */
    public <I extends Icon> I getIcon ()
    {
        return IconManager.getIcon ( getId () );
    }

    @Override
    public void paintIcon ( final Component c, final Graphics g, final int x, final int y )
    {
        getIcon ().paintIcon ( c, g, x, y );
    }

    @Override
    public int getIconWidth ()
    {
        return getIcon ().getIconWidth ();
    }

    @Override
    public int getIconHeight ()
    {
        return getIcon ().getIconHeight ();
    }

    @Override
    public String toString ()
    {
        return getClass ().getCanonicalName () + "[id=" + getId () + "]";
    }
}