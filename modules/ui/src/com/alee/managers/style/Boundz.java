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
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Class describing inner component bounds.
 * todo Class name is temporary to avoid IDE commit issues.
 *
 * @author Mikle Garin
 * @see com.alee.managers.style.BoundsType
 */

public final class Boundz
{
    /**
     * Parent bounds.
     * Usually those cover a larger area of component.
     */
    private final Boundz parent;

    /**
     * Bounds type.
     * Used to retrieve different painting bounds on different abstraction levels.
     */
    private final BoundsType type;

    /**
     * Actual bounds represented by this class instance.
     * These bounds are always relative to component coordinates system.
     */
    private final Rectangle bounds;

    /**
     * {@link Boundz} for the whole component of the specified size.
     *
     * @param dimension component size
     */
    public Boundz ( final Dimension dimension )
    {
        super ();
        this.parent = null;
        this.type = BoundsType.component;
        this.bounds = new Rectangle ( 0, 0, dimension.width, dimension.height );
    }

    /**
     * {@link Boundz} for the whole component.
     *
     * @param component component
     */
    public Boundz ( final JComponent component )
    {
        super ();
        this.parent = null;
        this.type = BoundsType.component;
        this.bounds = new Rectangle ( 0, 0, component.getWidth (), component.getHeight () );
    }

    /**
     * {@link Boundz} for the whole component with the specified (X,Y) shift.
     * todo This is a temporary method for supporting {@link com.alee.painter.PainterSupport#paintSection} method workaround.
     *
     * @param component component
     * @param x         X coordinate shift
     * @param y         Y coordinate shift
     */
    public Boundz ( final JComponent component, final int x, final int y )
    {
        super ();
        this.parent = null;
        this.type = BoundsType.component;
        this.bounds = new Rectangle ( x, y, component.getWidth (), component.getHeight () );
    }

    /**
     * {@link Boundz} for the component section.
     *
     * @param parent parent bounds
     * @param bounds actual bounds
     */
    public Boundz ( final Boundz parent, final Rectangle bounds )
    {
        super ();
        this.parent = parent;
        this.type = BoundsType.section;
        this.bounds = bounds;
    }

    /**
     * {@link Boundz} for the component decoration and content.
     *
     * @param parent     parent bounds
     * @param type       bounds type
     * @param component  component
     * @param decoration painted decoration
     */
    public Boundz ( final Boundz parent, final BoundsType type, final JComponent component, final IDecoration decoration )
    {
        super ();
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
    public BoundsType type ()
    {
        return type;
    }

    /**
     * Returns actual bounds.
     *
     * @return actual bounds
     */
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
    public Rectangle get ( final BoundsType type )
    {
        Boundz bounds = this;
        while ( bounds != null )
        {
            if ( bounds.type == type )
            {
                return bounds.bounds;
            }
            bounds = bounds.parent;
        }
        throw new StyleException ( "Unknown bounds requested: " + type );
    }
}