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

package com.alee.extended.svg;

import com.alee.managers.icon.data.AbstractIconData;
import com.alee.utils.TextUtils;
import com.kitfox.svg.SVGUniverse;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link AbstractIconData} implementation for {@link SvgIcon} icon type.
 *
 * @author Mikle Garin
 * @see SvgIcon
 * @see SVGUniverse
 */
@XStreamAlias ( "SvgIcon" )
public class SvgIconData extends AbstractIconData<SvgIcon>
{
    /**
     * Static {@link SVGUniverse} cache shared between all {@link SvgIconData} instances.
     * It can be used for adjusting {@link SvgIcon}s originating from the same source.
     */
    protected static Map<String, SVGUniverse> universes;

    /**
     * Preferred icon size.
     */
    @XStreamAsAttribute
    protected Dimension size;

    /**
     * Custom SVG universe key.
     * Can be provided to force icon use distinct SVG universe.
     */
    @XStreamAsAttribute
    protected String universe;

    /**
     * Returns preferred icon size.
     *
     * @return preferred icon size
     */
    public Dimension getSize ()
    {
        return size;
    }

    /**
     * Sets preferred icon size.
     *
     * @param size preferred icon size
     */
    public void setSize ( final Dimension size )
    {
        this.size = size;
    }

    /**
     * Returns custom SVG universe key.
     *
     * @return custom SVG universe key
     */
    public String getUniverse ()
    {
        return universe;
    }

    /**
     * Sets custom SVG universe key.
     *
     * @param universe custom SVG universe key
     */
    public void setUniverse ( final String universe )
    {
        this.universe = universe;
    }

    @Override
    public SvgIcon loadIcon ()
    {
        final SvgIcon icon;
        final int width = size != null ? size.width : 16;
        final int height = size != null ? size.height : 16;
        if ( getNearClass () != null )
        {
            final URL url = getNearClass ().getResource ( getPath () );
            icon = new SvgIcon ( getSVGUniverse (), url, width, height );
        }
        else
        {
            final String file = getPath ();
            icon = new SvgIcon ( getSVGUniverse (), file, width, height );
        }
        return icon;
    }

    /**
     * Returns {@link SVGUniverse} for this {@link SvgIconData}.
     * Returns new {@link SVGUniverse} instance instead of {@link com.kitfox.svg.SVGCache#getSVGUniverse()} by default.
     * This is made to simplify work with {@link SvgIcon} as they would share all changes when originating from the same source otherwise.
     *
     * Practically this method would rarely be called outside of {@link EventDispatchThread}, but synchronization is still added to ensure
     * it is thread-safe and that there won't b two {@link #universes} instances created at any time.
     *
     * @return {@link SVGUniverse} for this {@link SvgIconData}
     */
    protected synchronized SVGUniverse getSVGUniverse ()
    {
        if ( TextUtils.isEmpty ( universe ) )
        {
            return new SVGUniverse ();
        }
        else
        {
            if ( universes == null )
            {
                universes = new HashMap<String, SVGUniverse> ( 1 );
            }
            SVGUniverse svgUniverse = universes.get ( universe );
            if ( svgUniverse == null )
            {
                svgUniverse = new SVGUniverse ();
                universes.put ( universe, svgUniverse );
            }
            return svgUniverse;
        }
    }
}