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

import com.alee.painter.decoration.IDecoration;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Enumeration referncing inner component bounds types.
 * These bounds are relative to actual component bounds within its container and not to the component container bounds.
 * It is basically made for convenient retrieval of inner bounds for various painting operations.
 *
 * @author Mikle Garin
 */

public enum Bounds
{
    /**
     * Component bounds.
     * It represents full component bounds: [0,0,w,h]
     */
    component,

    /**
     * Component bounds minus its margin.
     * Represents bounds in which component decoration is painted: [m,m,w-m*2,h-m*2]
     */
    margin,

    /**
     * Component bounds minus its margin and decoration border width.
     * Represents bounds in which component background is painted: [m+b,m+b,w-m*2-b*2,h-m*2-b*2]
     */
    border,

    /**
     * Components bounds minus its margin, decoration border width and padding.
     * Represents bounds in which component content is painted: [m+b+p,m+b+p,w-m*2-b*2-p*2,h-m*2-b*2-p*2]
     */
    padding;

    /**
     * Returns bounds of this type for the specified component.
     *
     * @param c component to retrieve bounds for
     * @return bounds of this type for the specified component
     */
    public Rectangle of ( final Component c )
    {
        return of ( c, new Rectangle ( 0, 0, c.getWidth (), c.getHeight () ) );
    }

    /**
     * Returns bounds of this type for the specified component.
     *
     * @param c component to retrieve bounds for
     * @param b component painting bounds
     * @return bounds of this type for the specified component
     */
    public Rectangle of ( final Component c, final Rectangle b )
    {
        final Insets i = insets ( c );
        return new Rectangle ( b.x + i.left, b.y + i.top, b.width - i.left - i.right, b.height - i.top - i.bottom );
    }

    /**
     * Returns insets for bounds of this type for the specified component.
     *
     * @param c component to retrieve insets for
     * @return insets for bounds of this type for the specified component
     */
    public Insets insets ( final Component c )
    {
        final Insets i = new Insets ( 0, 0, 0, 0 );
        switch ( this )
        {
            case padding:
            {
                SwingUtils.increase ( i, LafUtils.getInsets ( c ) );
                break;
            }
            case border:
            {
                SwingUtils.increase ( i, LafUtils.getInsets ( c ) );
                SwingUtils.decrease ( i, LafUtils.getPadding ( c ) );
                break;
            }
            case margin:
            {
                SwingUtils.increase ( i, LafUtils.getMargin ( c ) );
                break;
            }
        }
        return i;
    }

    /**
     * Returns bounds of this type for the specified decoration.
     *
     * @param c decorated component
     * @param d decoration to retrieve bounds for
     * @param b decoration painting bounds
     * @return bounds of this type for the specified decoration
     */
    public Rectangle of ( final JComponent c, final IDecoration d, final Rectangle b )
    {
        final Insets i = insets ( c, d );
        return new Rectangle ( b.x + i.left, b.y + i.top, b.width - i.left - i.right, b.height - i.top - i.bottom );
    }

    /**
     * Returns insets for bounds of this type for the specified section decoration.
     * These insets never include component margin or padding since they are section only.
     * todo These insets should include decoration margin and padding when those are implemented
     *
     * @param c decorated component
     * @param d decoration to retrieve bounds for
     * @return insets for bounds of this type for the specified section decoration
     */
    public Insets insets ( final JComponent c, final IDecoration d )
    {
        final Insets i = new Insets ( 0, 0, 0, 0 );
        switch ( this )
        {
            case padding:
            case border:
            {
                SwingUtils.increase ( i, d.getBorderInsets ( c ) );
            }
        }
        return i;
    }
}