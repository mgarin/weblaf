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
 * Lazy icon implementation.
 *
 * @author Mikle Garin
 * @see com.alee.managers.icon.IconManager
 */

public final class LazyIcon implements Icon
{
    /**
     * Referenced icon ID.
     */
    protected final String id;

    /**
     * Constructs new {@link com.alee.managers.icon.LazyIcon}.
     * It will retrieve icon from {@link com.alee.managers.icon.IconManager} using the specified {@code id}.
     *
     * @param id icon ID
     * @see com.alee.managers.icon.IconManager
     */
    public LazyIcon ( final String id )
    {
        super ();
        this.id = id;
    }

    /**
     * Returns referenced icon ID.
     *
     * @return referenced icon ID
     */
    public String getId ()
    {
        return id;
    }

    /**
     * Returns actual icon.
     * Use this method wisely as it will load icon into memory.
     *
     * @return actual icon
     */
    protected Icon getIcon ()
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
}