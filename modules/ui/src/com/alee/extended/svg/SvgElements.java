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

import com.kitfox.svg.*;
import com.kitfox.svg.animation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Class containing SVG attributes as constants.
 *
 * @author Mikle Garin
 */

public final class SvgElements
{
    /**
     * Element ID.
     */
    public static final String ID = "id";

    /**
     * Element style class.
     */
    public static final String CLAZZ = "class";

    /**
     * Stroke color.
     */
    public static final String STROKE = "stroke";

    /**
     * Fill color.
     */
    public static final String FILL = "fill";

    /**
     * Transform.
     */
    public static final String TRANSFORM = "transform";

    /**
     * Classes used to represent SVG elements.
     */
    public static final Map<String, Class> CLASSES;

    static
    {
        CLASSES = new HashMap<String, Class> ();
        CLASSES.put ( "a", A.class );
        CLASSES.put ( "animate", Animate.class );
        CLASSES.put ( "animatecolor", AnimateColor.class );
        CLASSES.put ( "animatemotion", AnimateMotion.class );
        CLASSES.put ( "animatetransform", AnimateTransform.class );
        CLASSES.put ( "circle", Circle.class );
        CLASSES.put ( "clippath", ClipPath.class );
        CLASSES.put ( "defs", Defs.class );
        CLASSES.put ( "desc", Desc.class );
        CLASSES.put ( "ellipse", Ellipse.class );
        CLASSES.put ( "filter", Filter.class );
        CLASSES.put ( "font", Font.class );
        CLASSES.put ( "font-face", FontFace.class );
        CLASSES.put ( "g", Group.class );
        CLASSES.put ( "glyph", Glyph.class );
        CLASSES.put ( "hkern", Hkern.class );
        CLASSES.put ( "image", ImageSVG.class );
        CLASSES.put ( "line", Line.class );
        CLASSES.put ( "lineargradient", LinearGradient.class );
        CLASSES.put ( "metadata", Metadata.class );
        CLASSES.put ( "missing-glyph", MissingGlyph.class );
        CLASSES.put ( "path", Path.class );
        CLASSES.put ( "pattern", PatternSVG.class );
        CLASSES.put ( "polygon", Polygon.class );
        CLASSES.put ( "polyline", Polyline.class );
        CLASSES.put ( "radialgradient", RadialGradient.class );
        CLASSES.put ( "rect", Rect.class );
        CLASSES.put ( "set", SetSmil.class );
        CLASSES.put ( "shape", ShapeElement.class );
        CLASSES.put ( "stop", Stop.class );
        CLASSES.put ( "style", Style.class );
        CLASSES.put ( "svg", SVGRoot.class );
        CLASSES.put ( "symbol", Symbol.class );
        CLASSES.put ( "text", Text.class );
        CLASSES.put ( "title", Title.class );
        CLASSES.put ( "tspan", Tspan.class );
        CLASSES.put ( "use", Use.class );
    }
}