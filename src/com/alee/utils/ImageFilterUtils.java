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

package com.alee.utils;

import com.alee.graphics.filters.BoxBlurFilter;
import com.alee.graphics.filters.GaussianFilter;
import com.alee.graphics.filters.MotionBlurOp;
import com.alee.graphics.filters.OpacityFilter;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

/**
 * This class provides a set of utilities to perform image filtering.
 *
 * @author Mikle Garin
 */

public final class ImageFilterUtils
{
    /**
     * Applies box blur filter to image
     */

    public static BufferedImage applyBoxBlurFilter ( Image src, Image dst, int hRadius, int vRadius, int iterations )
    {
        return applyBoxBlurFilter ( ImageUtils.getBufferedImage ( src ), ImageUtils.getBufferedImage ( dst ), hRadius, vRadius,
                iterations );
    }

    public static BufferedImage applyBoxBlurFilter ( BufferedImage src, BufferedImage dst, int hRadius, int vRadius, int iterations )
    {
        return new BoxBlurFilter ( hRadius, vRadius, iterations ).filter ( src, dst );
    }

    /**
     * Applies grayscale filter to image
     */

    private static ColorConvertOp grayscaleColorConvert = new ColorConvertOp ( ColorSpace.getInstance ( ColorSpace.CS_GRAY ), null );

    public static BufferedImage applyGrayscaleFilter ( Image src, Image dst )
    {
        return applyGrayscaleFilter ( ImageUtils.getBufferedImage ( src ), ImageUtils.getBufferedImage ( dst ) );
    }

    public static BufferedImage applyGrayscaleFilter ( BufferedImage src, BufferedImage dst )
    {
        return grayscaleColorConvert.filter ( src, dst );
    }

    /**
     * Applies gaussian filter to image
     */

    public static BufferedImage applyGaussianFilter ( Image src, Image dst, float radius )
    {
        return applyGaussianFilter ( ImageUtils.getBufferedImage ( src ), ImageUtils.getBufferedImage ( dst ), radius );
    }

    public static BufferedImage applyGaussianFilter ( BufferedImage src, BufferedImage dst, float radius )
    {
        return new GaussianFilter ( radius ).filter ( src, dst );
    }

    /**
     * Applies zoom blur filter to image
     */

    public static BufferedImage applyZoomBlurFilter ( Image src, Image dst, float zoom, float centreX, float centreY )
    {
        return applyZoomBlurFilter ( ImageUtils.getBufferedImage ( src ), ImageUtils.getBufferedImage ( dst ), zoom, centreX, centreY );
    }

    public static BufferedImage applyZoomBlurFilter ( BufferedImage src, BufferedImage dst, float zoom, float centreX, float centreY )
    {
        return new MotionBlurOp ( 0f, 0f, 0f, zoom, centreX, centreY ).filter ( src, dst );
    }

    /**
     * Applies rotation blur filter to image
     */

    public static BufferedImage applyRotationBlurFilter ( Image src, Image dst, float rotation, float centreX, float centreY )
    {
        return applyRotationBlurFilter ( ImageUtils.getBufferedImage ( src ), ImageUtils.getBufferedImage ( dst ), rotation, centreX,
                centreY );
    }

    public static BufferedImage applyRotationBlurFilter ( BufferedImage src, BufferedImage dst, float rotation, float centreX,
                                                          float centreY )
    {
        return new MotionBlurOp ( 0f, 0f, rotation, 0f, centreX, centreY ).filter ( src, dst );
    }

    /**
     * Applies rotation blur filter to image
     */

    public static BufferedImage applyMotionBlurFilter ( Image src, Image dst, float distance, float angle, float rotation, float zoom,
                                                        float centreX, float centreY )
    {
        return applyMotionBlurFilter ( ImageUtils.getBufferedImage ( src ), ImageUtils.getBufferedImage ( dst ), distance, angle, rotation,
                zoom, centreX, centreY );
    }

    public static BufferedImage applyMotionBlurFilter ( BufferedImage src, BufferedImage dst, float distance, float angle, float rotation,
                                                        float zoom, float centreX, float centreY )
    {
        return new MotionBlurOp ( distance, angle, rotation, zoom, centreX, centreY ).filter ( src, dst );
    }

    /**
     * Applies opacity filter to image
     */

    public static BufferedImage applyOpacityFilter ( Image src, Image dst, int opacity )
    {
        return applyOpacityFilter ( ImageUtils.getBufferedImage ( src ), ImageUtils.getBufferedImage ( dst ), opacity );
    }

    public static BufferedImage applyOpacityFilter ( BufferedImage src, BufferedImage dst, int opacity )
    {
        return new OpacityFilter ( opacity ).filter ( src, dst );
    }
}
