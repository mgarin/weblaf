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
        switch ( this )
        {
            case padding:
            {
                final Insets i = LafUtils.getInsets ( c );
                if ( i != null )
                {
                    return new Rectangle ( b.x + i.left, b.y + i.top, b.width - i.left - i.right, b.height - i.top - i.bottom );
                }
                else
                {
                    return component.of ( c, b );
                }
            }
            case border:
            {
                final Insets i = LafUtils.getInsets ( c );
                if ( i != null )
                {
                    final Insets p = LafUtils.getPadding ( c );
                    if ( p != null )
                    {
                        return new Rectangle ( b.x + i.left - p.left, b.y + i.top - p.top, b.width - i.left - i.right + p.left + p.right,
                                b.height - i.top - i.bottom + p.top + p.bottom );
                    }
                    else
                    {
                        return padding.of ( c, b );
                    }
                }
                else
                {
                    return component.of ( c, b );
                }
            }
            case margin:
            {
                final Insets m = LafUtils.getMargin ( c );
                if ( m != null )
                {
                    return new Rectangle ( b.x + m.left, b.y + m.top, b.width - m.left - m.right, b.height - m.top - m.bottom );
                }
                else
                {
                    return component.of ( c, b );
                }
            }
            case component:
            default:
            {
                return b;
            }
        }
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
        switch ( this )
        {
            case padding:
            case border:
            {
                final Insets i = d.getBorderInsets ( c );
                return new Rectangle ( b.x + i.left, b.y + i.top, b.width - i.left - i.right, b.height - i.top - i.bottom );
            }
            case margin:
            case component:
            default:
            {
                return b;
            }
        }
    }
}