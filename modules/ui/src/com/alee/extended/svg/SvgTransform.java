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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.utils.TextUtils;
import com.alee.utils.swing.Scale;
import com.kitfox.svg.SVGElement;
import com.kitfox.svg.xml.StyleAttribute;
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
     * {@link SvgIcon} translation.
     * By default (if all options set to {@code null}) it will use {@code "none"} transform value.
     */
    @Nullable
    @XStreamAsAttribute
    protected Point2D translate;

    /**
     * {@link SvgIcon} scaling relative to center point.
     * By default (if all options set to {@code null}) it will use {@code "none"} transform value.
     */
    @Nullable
    @XStreamAsAttribute
    protected Scale scale;

    /**
     * {@link SvgIcon} rotation relative to center point.
     * By default (if all options set to {@code null}) it will use {@code "none"} transform value.
     */
    @Nullable
    @XStreamAsAttribute
    protected Double rotate;

    /**
     * Constructs new {@link SvgTransform}.
     */
    public SvgTransform ()
    {
        this ( null, null, null, null );
    }

    /**
     * Constructs new {@link SvgTransform}.
     *
     * @param translate {@link SvgIcon} translation
     */
    public SvgTransform ( @Nullable final Point2D translate )
    {
        this ( null, translate, null, null );
    }

    /**
     * Constructs new {@link SvgTransform}.
     *
     * @param scale {@link SvgIcon} scaling relative to center point
     */
    public SvgTransform ( @Nullable final Scale scale )
    {
        this ( null, null, scale, null );
    }

    /**
     * Constructs new {@link SvgTransform}.
     *
     * @param rotate {@link SvgIcon} rotation relative to center point
     */
    public SvgTransform ( @Nullable final Double rotate )
    {
        this ( null, null, null, rotate );
    }

    /**
     * Constructs new {@link SvgTransform}.
     *
     * @param translate {@link SvgIcon} translation
     * @param scale     {@link SvgIcon} scaling relative to center point
     * @param rotate    {@link SvgIcon} rotation relative to center point
     */
    public SvgTransform ( @Nullable final Point2D translate, @Nullable final Scale scale, @Nullable final Double rotate )
    {
        this ( null, translate, scale, rotate );
    }

    /**
     * Constructs new {@link SvgTransform}.
     *
     * @param selector {@link com.kitfox.svg.SVGElement} selector
     */
    public SvgTransform ( @Nullable final String selector )
    {
        this ( selector, null, null, null );
    }

    /**
     * Constructs new {@link SvgTransform}.
     *
     * @param selector  {@link com.kitfox.svg.SVGElement} selector
     * @param translate {@link SvgIcon} translation
     */
    public SvgTransform ( @Nullable final String selector, @Nullable final Point2D translate )
    {
        this ( selector, translate, null, null );
    }

    /**
     * Constructs new {@link SvgTransform}.
     *
     * @param selector {@link com.kitfox.svg.SVGElement} selector
     * @param scale    {@link SvgIcon} scaling relative to center point
     */
    public SvgTransform ( @Nullable final String selector, @Nullable final Scale scale )
    {
        this ( selector, null, scale, null );
    }

    /**
     * Constructs new {@link SvgTransform}.
     *
     * @param selector {@link com.kitfox.svg.SVGElement} selector
     * @param rotate   {@link SvgIcon} rotation relative to center point
     */
    public SvgTransform ( @Nullable final String selector, @Nullable final Double rotate )
    {
        this ( selector, null, null, rotate );
    }

    /**
     * Constructs new {@link SvgTransform}.
     *
     * @param selector  {@link com.kitfox.svg.SVGElement} selector
     * @param translate {@link SvgIcon} translation
     * @param scale     {@link SvgIcon} scaling relative to center point
     * @param rotate    {@link SvgIcon} rotation relative to center point
     */
    public SvgTransform ( @Nullable final String selector, @Nullable final Point2D translate, @Nullable final Scale scale,
                          @Nullable final Double rotate )
    {
        super ( selector );
        this.translate = translate;
        this.scale = scale;
        this.rotate = rotate;
    }

    @NotNull
    @Override
    protected String getAttribute ( @NotNull final SvgIcon icon )
    {
        return SvgElements.TRANSFORM;
    }

    @Nullable
    @Override
    protected String getValue ( @NotNull final SvgIcon icon, @NotNull final SVGElement element, @Nullable final StyleAttribute attribute )
    {
        String transform;
        if ( translate != null || scale != null || rotate != null )
        {
            transform = "";
            if ( translate != null )
            {
                transform += "translate(" + translate.getX () + " " + translate.getY () + ")";
            }
            if ( scale != null )
            {
                if ( TextUtils.notEmpty ( transform ) )
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
                if ( TextUtils.notEmpty ( transform ) )
                {
                    transform += " ";
                }
                final Dimension ps = icon.getPreferredSize ();
                transform += "rotate(" + rotate + " " + ps.width / 2 + " " + ps.height / 2 + ")";
            }
        }
        else
        {
            transform = "none";
        }
        return transform;
    }
}