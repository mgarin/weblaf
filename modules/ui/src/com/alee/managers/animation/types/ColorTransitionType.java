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

package com.alee.managers.animation.types;

import com.alee.managers.animation.easing.Easing;

import java.awt.*;

/**
 * {@link Color} values transition support.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-AnimationManager">How to use AnimationManager</a>
 * @see com.alee.managers.animation.AnimationManager
 */
public final class ColorTransitionType implements TransitionType<Color>
{
    @Override
    public Color value ( final Easing easing, final Color start, final Color goal, final double progress, final double total )
    {
        return adjust ( easing, start, goal, progress, total );
    }

    /**
     * Returns adjusted {@link Color} value.
     * Separated into static method for convenient usage within other type support classes.
     *
     * @param easing   easing algorithm
     * @param start    initial value
     * @param goal     goal value
     * @param progress current transition progress, should always be in [0..total] range
     * @param total    maximum transition progress
     * @return adjusted {@link Color} value
     */
    public static Color adjust ( final Easing easing, final Color start, final Color goal, final double progress, final double total )
    {
        final int r = IntegerTransitionType.adjust ( easing, start.getRed (), goal.getRed (), progress, total );
        final int g = IntegerTransitionType.adjust ( easing, start.getGreen (), goal.getGreen (), progress, total );
        final int b = IntegerTransitionType.adjust ( easing, start.getBlue (), goal.getBlue (), progress, total );
        final int a = IntegerTransitionType.adjust ( easing, start.getAlpha (), goal.getAlpha (), progress, total );
        return new Color ( r, g, b, a );
    }
}