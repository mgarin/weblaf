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

package com.alee.extended.icon;

import com.alee.api.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

/**
 * Simple {@link Icon} that renders all provided icons on top of each other in the specified order.
 *
 * @author Mikle Garin
 */
public class StackIcon extends MixedIcon<StackIcon>
{
    /**
     * Constructs new {@link StackIcon}.
     *
     * @param icons {@link Icon}s to stack
     */
    public StackIcon ( @NotNull final Collection<? extends Icon> icons )
    {
        this ( icons.toArray ( new Icon[ icons.size () ] ) );
    }

    /**
     * Constructs new {@link StackIcon}.
     *
     * @param icons {@link Icon}s to stack
     */
    public StackIcon ( @NotNull final Icon... icons )
    {
        super ( icons );
    }

    @Override
    protected StackIcon newInstance ( @NotNull final Icon... icons )
    {
        return new StackIcon ( icons );
    }

    @Override
    public void paintIcon ( @NotNull final Component c, @NotNull final Graphics g, final int x, final int y )
    {
        for ( final Icon icon : icons )
        {
            icon.paintIcon ( c, g, x, y );
        }
    }

    @Override
    public int getIconWidth ()
    {
        return maxWidth ( icons );
    }

    @Override
    public int getIconHeight ()
    {
        return maxHeight ( icons );
    }
}