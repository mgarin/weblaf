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

import com.alee.painter.PainterSupport;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Enumeration referncing component bounds types.
 * These bounds are relative to actual component bounds within its container and not to the component container bounds.
 * It is basically made for convenient retrieval of inner bounds for various painting operations.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see Bounds
 * @see StyleManager
 */
public enum BoundsType
{
    /**
     * Component bounds.
     * It represents full component bounds: [0,0,w,h]
     */
    component,

    /**
     * Component bounds excluding its margin.
     * Represents bounds in which component decoration is painted.
     */
    margin,

    /**
     * Component bounds excluding its margin and decoration border.
     * Represents bounds in which component background is painted.
     */
    border,

    /**
     * Components bounds excluding its margin, decoration border and padding.
     * Represents bounds in which component content is painted.
     */
    padding,

    /**
     * Component section bounds.
     * This is a special type that represents custom component section.
     * Insets, bounds or border cannot be retrieved for this type from this class.
     */
    section;

    /**
     * Returns insets for bounds of this type for the specified component.
     *
     * @param component component to retrieve insets for
     * @return insets for bounds of this type for the specified component
     */
    public Insets insets ( final JComponent component )
    {
        switch ( this )
        {
            case section:
            {
                throw new StyleException ( "Section bounds are specific for each component" );
            }
            case margin:
            {
                return PainterSupport.getMargin ( component );
            }
            case border:
            {
                final Insets insets = PainterSupport.getInsets ( component );
                SwingUtils.decrease ( insets, PainterSupport.getMargin ( component ) );
                SwingUtils.decrease ( insets, PainterSupport.getPadding ( component ) );
                return insets;
            }
            case padding:
            {
                return PainterSupport.getPadding ( component );
            }
            default:
            {
                return new Insets ( 0, 0, 0, 0 );
            }
        }
    }

    /**
     * Returns border for bounds of this type for the specified component.
     *
     * @param component component to retrieve border for
     * @return border for bounds of this type for the specified component
     */
    public Insets border ( final JComponent component )
    {
        final Insets i = new Insets ( 0, 0, 0, 0 );
        switch ( this )
        {
            case padding:
            {
                SwingUtils.increase ( i, PainterSupport.getInsets ( component ) );
                break;
            }
            case border:
            {
                SwingUtils.increase ( i, PainterSupport.getInsets ( component ) );
                SwingUtils.decrease ( i, PainterSupport.getPadding ( component ) );
                break;
            }
            case margin:
            {
                SwingUtils.increase ( i, PainterSupport.getMargin ( component ) );
                break;
            }
        }
        return i;
    }

    /**
     * Returns insets for bounds of this type for the specified section decoration.
     * These insets never include component margin or padding since those are section only.
     * todo These insets should include decoration margin and padding when those are implemented
     *
     * @param component  decorated component
     * @param decoration decoration to retrieve insets for
     * @return insets for bounds of this type for the specified section decoration
     */
    public Insets insets ( final JComponent component, final IDecoration decoration )
    {
        switch ( this )
        {
            case section:
            {
                throw new StyleException ( "Section insets are specific for each component" );
            }
            case border:
            {
                return decoration.getBorderInsets ( component );
            }
            case component:
            case margin:
            case padding:
            default:
            {
                return new Insets ( 0, 0, 0, 0 );
            }
        }
    }

    /**
     * Returns border for bounds of this type for the specified section decoration.
     * This border never include component margin or padding since those are section only.
     * todo This border should include decoration margin and padding when those are implemented
     *
     * @param component  decorated component
     * @param decoration decoration to retrieve border for
     * @return border for bounds of this type for the specified section decoration
     */
    public Insets border ( final JComponent component, final IDecoration decoration )
    {
        final Insets i = new Insets ( 0, 0, 0, 0 );
        switch ( this )
        {
            case section:
            {
                throw new StyleException ( "Section border is specific for each component" );
            }
            case border:
            case padding:
            {
                SwingUtils.increase ( i, decoration.getBorderInsets ( component ) );
            }
        }
        return i;
    }

    /**
     * Returns bounds of this type for the specified component.
     *
     * @param component component to retrieve bounds for
     * @return bounds of this type for the specified component
     */
    public Rectangle bounds ( final Component component )
    {
        final Insets i = component instanceof JComponent ? border ( ( JComponent ) component ) : new Insets ( 0, 0, 0, 0 );
        return new Rectangle ( i.left, i.top, component.getWidth () - i.left - i.right, component.getHeight () - i.top - i.bottom );
    }
}