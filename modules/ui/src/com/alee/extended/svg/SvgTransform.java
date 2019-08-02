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

import com.alee.utils.TextUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Adds or replaces existing transform on the target {@link com.kitfox.svg.SVGElement}.
 *
 * @author Mikle Garin
 */
@XStreamAlias ( "SvgTransform" )
public class SvgTransform extends AbstractSvgAttributeAdjustment
{
    /**
     * Icon translation.
     */
    @XStreamAsAttribute
    protected Point2D translate;

    /**
     * Icon scaling.
     */
    @XStreamAsAttribute
    protected Point2D scale;

    /**
     * Icon rotation.
     */
    @XStreamAsAttribute
    protected Double rotate;

    @Override
    protected String getAttribute ( final SvgIcon icon )
    {
        return SvgElements.TRANSFORM;
    }

    @Override
    protected String getValue ( final SvgIcon icon )
    {
        String transform = null;
        if ( translate != null || scale != null || rotate != null )
        {
            transform = "";
            if ( translate != null )
            {
                transform += "translate(" + translate.getX () + " " + translate.getY () + ")";
            }
            if ( scale != null )
            {
                if ( !TextUtils.isEmpty ( transform ) )
                {
                    transform += " ";
                }
                final Dimension ps = icon.getPreferredSize ();
                final double x = ( ps.width - ps.width * scale.getX () ) / 2;
                final double y = ( ps.height - ps.height * scale.getY () ) / 2;
                transform += "translate(" + x + " " + y + ") scale(" + scale.getX () + " " + scale.getY () + ")";
            }
            if ( rotate != null )
            {
                if ( !TextUtils.isEmpty ( transform ) )
                {
                    transform += " ";
                }
                final Dimension ps = icon.getPreferredSize ();
                transform += "rotate(" + rotate + " " + ps.width / 2 + " " + ps.height / 2 + ")";
            }
        }
        return transform;
    }
}