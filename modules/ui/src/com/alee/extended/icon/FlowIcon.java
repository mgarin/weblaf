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
 * Simple {@link Icon} that renders all provided icons after each other in the specified order.
 *
 * @author Mikle Garin
 */
public class FlowIcon extends MixedIcon<FlowIcon>
{
    /**
     * Spacing between {@link Icon}s.
     */
    protected final int spacing;

    /**
     * Constructs new {@link FlowIcon}.
     *
     * @param icons {@link Icon}s to mix
     */
    public FlowIcon ( @NotNull final Collection<? extends Icon> icons )
    {
        this ( 0, icons );
    }

    /**
     * Constructs new {@link FlowIcon}.
     *
     * @param spacing spacing between {@link Icon}s
     * @param icons   {@link Icon}s to mix
     */
    public FlowIcon ( final int spacing, @NotNull final Collection<? extends Icon> icons )
    {
        this ( spacing, icons.toArray ( new Icon[ icons.size () ] ) );
    }

    /**
     * Constructs new {@link FlowIcon}.
     *
     * @param icons {@link Icon}s to mix
     */
    public FlowIcon ( @NotNull final Icon... icons )
    {
        this ( 0, icons );
    }

    /**
     * Constructs new {@link FlowIcon}.
     *
     * @param spacing spacing between {@link Icon}s
     * @param icons   {@link Icon}s to mix
     */
    public FlowIcon ( final int spacing, @NotNull final Icon... icons )
    {
        super ( icons );
        this.spacing = spacing;
    }

    @Override
    protected FlowIcon newInstance ( @NotNull final Icon... icons )
    {
        return new FlowIcon ( icons );
    }

    @Override
    public void paintIcon ( @NotNull final Component c, @NotNull final Graphics g, final int x, final int y )
    {
        int offset = x;
        for ( final Icon icon : icons )
        {
            icon.paintIcon ( c, g, offset, y );
            offset += icon.getIconWidth () + spacing;
        }
    }

    @Override
    public int getIconWidth ()
    {
        int width = 0;
        for ( final Icon icon : icons )
        {
            width += icon.getIconWidth () + spacing;
        }
        return width - spacing;
    }

    @Override
    public int getIconHeight ()
    {
        return maxHeight ( icons );
    }
}