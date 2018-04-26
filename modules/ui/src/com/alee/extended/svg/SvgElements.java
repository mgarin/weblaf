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

import com.alee.utils.map.ImmutableMap;
import com.kitfox.svg.*;
import com.kitfox.svg.animation.*;

import java.util.Map;

/**
 * Class containing SVG attributes as constants.
 *
 * @author Mikle Garin
 */
public final class SvgElements
{
    /**
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/SVG/Element/svg">SVG element</a>
     */
    public static final String SVG = "svg";

    /**
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/SVG/Element/g">Group element</a>
     */
    public static final String GROUP = "g";

    /**
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/SVG/Element/path">Path element</a>
     */
    public static final String PATH = "path";

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
     * Gradient stop color.
     */
    public static final String STOP_COLOR = "stop-color";

    /**
     * Transform.
     */
    public static final String TRANSFORM = "transform";

    /**
     * Classes used to represent SVG elements.
     */
    public static final Map<String, Class> CLASSES = new ImmutableMap<String, Class> (
            SVG, SVGRoot.class,
            GROUP, Group.class,
            PATH, Path.class,
            "a", A.class,
            "animate", Animate.class,
            "animatecolor", AnimateColor.class,
            "animatemotion", AnimateMotion.class,
            "animatetransform", AnimateTransform.class,
            "circle", Circle.class,
            "clippath", ClipPath.class,
            "defs", Defs.class,
            "desc", Desc.class,
            "ellipse", Ellipse.class,
            "filter", Filter.class,
            "font", Font.class,
            "font-face", FontFace.class,
            "glyph", Glyph.class,
            "hkern", Hkern.class,
            "image", ImageSVG.class,
            "line", Line.class,
            "lineargradient", LinearGradient.class,
            "metadata", Metadata.class,
            "missing-glyph", MissingGlyph.class,
            "pattern", PatternSVG.class,
            "polygon", Polygon.class,
            "polyline", Polyline.class,
            "radialgradient", RadialGradient.class,
            "rect", Rect.class,
            "set", SetSmil.class,
            "shape", ShapeElement.class,
            "stop", Stop.class,
            "style", Style.class,
            "symbol", Symbol.class,
            "text", Text.class,
            "title", Title.class,
            "tspan", Tspan.class,
            "use", Use.class
    );
}