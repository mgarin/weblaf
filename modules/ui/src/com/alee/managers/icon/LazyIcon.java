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
     * Predefined icon size.
     */
    protected final Dimension size;

    /**
     * Constructs new {@link com.alee.managers.icon.LazyIcon}.
     * It will retrieve icon from {@link com.alee.managers.icon.IconManager} using the specified {@code id}.
     *
     * @param id icon ID
     * @see com.alee.managers.icon.IconManager
     */
    public LazyIcon ( final String id )
    {
        this ( id, null );
    }

    /**
     * Constructs new {@link com.alee.managers.icon.LazyIcon}.
     * It will retrieve icon from {@link com.alee.managers.icon.IconManager} using the specified {@code id}.
     * Specified {@code width} and {@code height} will be used to delay icon loading until it is actually needed.
     *
     * @param id     icon ID
     * @param width  predefined icon width
     * @param height predefined icon height
     */
    public LazyIcon ( final String id, final int width, final int height )
    {
        this ( id, new Dimension ( width, height ) );
    }

    /**
     * Constructs new {@link com.alee.managers.icon.LazyIcon}.
     * It will retrieve icon from {@link com.alee.managers.icon.IconManager} using the specified {@code id}.
     * Specified {@code size} will be used to delay icon loading until it is actually needed.
     *
     * @param id   icon ID
     * @param size predefined icon size
     */
    public LazyIcon ( final String id, final Dimension size )
    {
        super ();
        this.id = id;
        this.size = size;
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
        return size != null ? size.width : getIcon ().getIconWidth ();
    }

    @Override
    public int getIconHeight ()
    {
        return size != null ? size.height : getIcon ().getIconHeight ();
    }
}