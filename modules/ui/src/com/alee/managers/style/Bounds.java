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
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Class describing inner component bounds.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see BoundsType
 * @see StyleManager
 */
public final class Bounds
{
    /**
     * Parent bounds.
     * Usually those cover a larger area of component.
     */
    @Nullable
    private final Bounds parent;

    /**
     * Bounds type.
     * Used to retrieve different painting bounds on different abstraction levels.
     */
    @NotNull
    private final BoundsType type;

    /**
     * Actual bounds represented by this class instance.
     * These bounds are always relative to component coordinates system.
     */
    @NotNull
    private final Rectangle bounds;

    /**
     * {@link Bounds} for the whole component of the specified size.
     *
     * @param dimension component size
     */
    public Bounds ( @NotNull final Dimension dimension )
    {
        this.parent = null;
        this.type = BoundsType.component;
        this.bounds = new Rectangle ( 0, 0, dimension.width, dimension.height );
    }

    /**
     * {@link Bounds} for the whole component.
     *
     * @param component component
     */
    public Bounds ( @NotNull final JComponent component )
    {
        this.parent = null;
        this.type = BoundsType.component;
        this.bounds = new Rectangle ( 0, 0, component.getWidth (), component.getHeight () );
    }

    /**
     * {@link Bounds} for the whole component with the specified (X,Y) shift.
     * todo This is a temporary method for supporting this workaround method:
     * todo {@code com.alee.painter.AbstractPainter#paintSection(SectionPainter, Graphics2D, Rectangle)}
     *
     * @param component component
     * @param x         X coordinate shift
     * @param y         Y coordinate shift
     */
    public Bounds ( @NotNull final JComponent component, final int x, final int y )
    {
        this.parent = null;
        this.type = BoundsType.component;
        this.bounds = new Rectangle ( x, y, component.getWidth (), component.getHeight () );
    }

    /**
     * {@link Bounds} for the component section.
     *
     * @param parent parent bounds
     * @param bounds actual bounds
     */
    public Bounds ( @NotNull final Bounds parent, @NotNull final Rectangle bounds )
    {
        this.parent = parent;
        this.type = BoundsType.section;
        this.bounds = new Rectangle ( bounds );
    }

    /**
     * {@link Bounds} for the component decoration and content.
     *
     * @param parent     parent bounds
     * @param type       bounds type
     * @param component  component
     * @param decoration painted decoration
     */
    public Bounds ( @NotNull final Bounds parent, @NotNull final BoundsType type, @NotNull final JComponent component,
                    @NotNull final IDecoration decoration )
    {
        this.parent = parent;
        this.type = type;
        final Insets insets = decoration.isSection () ? type.insets ( component, decoration ) : type.insets ( component );
        this.bounds = SwingUtils.shrink ( parent.get (), insets );
    }

    /**
     * Returns bounds type.
     *
     * @return bounds type
     */
    @NotNull
    public BoundsType type ()
    {
        return type;
    }

    /**
     * Returns actual bounds.
     *
     * @return actual bounds
     */
    @NotNull
    public Rectangle get ()
    {
        return bounds;
    }

    /**
     * Return actual bounds of specific type.
     *
     * @param type bounds type
     * @return actual bounds of specific type
     */
    @NotNull
    public Rectangle get ( @NotNull final BoundsType type )
    {
        Bounds bounds = this;
        while ( bounds != null )
        {
            if ( bounds.type == type )
            {
                return bounds.bounds;
            }
            bounds = bounds.parent;
        }
        throw new StyleException ( String.format ( "Unknown bounds type '%s' requested", type.name () ) );
    }
}