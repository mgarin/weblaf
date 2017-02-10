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

package com.alee.managers.icon.data;

import com.alee.extended.svg.SvgIcon;
import com.alee.utils.TextUtils;
import com.kitfox.svg.SVGUniverse;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link IconData} implementation for {@link SvgIcon} icon type.
 *
 * @author Mikle Garin
 * @see SvgIcon
 * @see com.kitfox.svg.app.beans.SVGIcon
 * @see com.kitfox.svg.SVGUniverse
 */

@XStreamAlias ( "SvgIcon" )
public final class SvgIconData extends IconData<SvgIcon>
{
    /**
     * Custom universes.
     * Mostly used for distinct icons from the same source.
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
     * Returns SVG universe for this icon.
     *
     * @return SVG universe for this icon
     */
    protected SVGUniverse getSVGUniverse ()
    {
        if ( TextUtils.isEmpty ( universe ) )
        {
            // Using new one instead of {@code SVGCache.getSVGUniverse()} when not specified
            // This is made to simplify work with SVG icons in general case
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