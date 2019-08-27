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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.painter.PainterSupport;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Enumeration referncing {@link JComponent} bounds types.
 * These bounds are relative to the {@link JComponent} coordinates grid.
 * It was made for convenient retrieval of inner bounds for various painting operations.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see Bounds
 * @see StyleManager
 */
public enum BoundsType
{
    /**
     * {@link JComponent} bounds.
     * It represents full {@link JComponent} bounds: [0,0,w,h]
     */
    component,

    /**
     * {@link JComponent} bounds excluding its margin.
     * Represents bounds in which {@link JComponent} decoration is painted.
     */
    margin,

    /**
     * {@link JComponent} bounds excluding its margin and decoration border.
     * Represents bounds in which {@link JComponent} background is painted.
     */
    border,

    /**
     * {@link JComponent} bounds excluding its margin, decoration border and padding.
     * Represents bounds in which {@link JComponent} content is painted.
     */
    padding,

    /**
     * {@link JComponent} section bounds.
     * This is a special type that represents custom {@link JComponent} section.
     * Insets, bounds or border cannot be retrieved for this type from this class.
     */
    section;

    /**
     * Returns insets for bounds of this type for the specified {@link JComponent}.
     *
     * @param component {@link JComponent} to retrieve insets for
     * @return insets for bounds of this type for the specified {@link JComponent}
     */
    @Nullable
    public Insets insets ( @NotNull final JComponent component )
    {
        final Insets insets;
        switch ( this )
        {
            case section:
            {
                throw new StyleException ( "Section bounds are specific for each component" );
            }
            case margin:
            {
                insets = PainterSupport.getMargin ( component );
                break;
            }
            case border:
            {
                insets = PainterSupport.getInsets ( component );
                if ( insets != null )
                {
                    SwingUtils.decrease ( insets, PainterSupport.getMargin ( component ) );
                    SwingUtils.decrease ( insets, PainterSupport.getPadding ( component ) );
                }
                break;
            }
            case padding:
            {
                insets = PainterSupport.getPadding ( component );
                break;
            }
            default:
            {
                insets = new Insets ( 0, 0, 0, 0 );
                break;
            }
        }
        return insets;
    }

    /**
     * Returns border for bounds of this type for the specified {@link JComponent}.
     *
     * @param component {@link JComponent} to retrieve border for
     * @return border for bounds of this type for the specified {@link JComponent}
     */
    @NotNull
    public Insets border ( @NotNull final JComponent component )
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
     * Returns insets for bounds of this type for the specified section {@link IDecoration}.
     * These insets never include {@link JComponent} margin or padding since those are section only.
     * todo These insets should include decoration margin and padding when those are implemented
     *
     * @param component  decorated {@link JComponent}
     * @param decoration {@link IDecoration} to retrieve insets for
     * @return insets for bounds of this type for the specified section {@link IDecoration}
     */
    @NotNull
    public Insets insets ( @NotNull final JComponent component, @NotNull final IDecoration decoration )
    {
        final Insets i;
        switch ( this )
        {
            case section:
            {
                throw new StyleException ( "Section insets are specific for each component" );
            }
            case border:
            {
                i = decoration.getBorderInsets ( component );
                break;
            }
            case component:
            case margin:
            case padding:
            default:
            {
                i = new Insets ( 0, 0, 0, 0 );
                break;
            }
        }
        return i;
    }

    /**
     * Returns border for bounds of this type for the specified section {@link IDecoration}.
     * This border never include {@link JComponent} margin or padding since those are section only.
     * todo This border should include decoration margin and padding when those are implemented
     *
     * @param component  decorated {@link JComponent}
     * @param decoration {@link IDecoration} to retrieve border for
     * @return border for bounds of this type for the specified section {@link IDecoration}
     */
    @NotNull
    public Insets border ( @NotNull final JComponent component, @NotNull final IDecoration decoration )
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
                break;
            }
        }
        return i;
    }

    /**
     * Returns bounds of this type for the specified {@link JComponent}.
     *
     * @param component {@link JComponent} to retrieve bounds for
     * @return bounds of this type for the specified {@link JComponent}
     */
    @NotNull
    public Rectangle bounds ( @NotNull final Component component )
    {
        final Insets insets = component instanceof JComponent ?
                border ( ( JComponent ) component ) :
                new Insets ( 0, 0, 0, 0 );

        return new Rectangle (
                insets.left,
                insets.top,
                component.getWidth () - insets.left - insets.right,
                component.getHeight () - insets.top - insets.bottom
        );
    }
}