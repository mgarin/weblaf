/*
 * Copyright 2006 Jerry Huxtable
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alee.graphics.filters;

/**
 * A filter which converts an image to grayscale using the NTSC brightness calculation.
 *
 * @author Jerry Huxtable
 */
public class GrayscaleFilter extends PointFilter
{
    public GrayscaleFilter ()
    {
        canFilterIndexColorModel = true;
    }

    @Override
    public int filterRGB ( final int x, final int y, int rgb )
    {
        final int a = rgb & 0xff000000;
        final int r = ( rgb >> 16 ) & 0xff;
        final int g = ( rgb >> 8 ) & 0xff;
        final int b = rgb & 0xff;
        //		rgb = (r + g + b) / 3;	// simple average
        rgb = ( r * 77 + g * 151 + b * 28 ) >> 8;    // NTSC luma
        return a | ( rgb << 16 ) | ( rgb << 8 ) | rgb;
    }

    @Override
    public String toString ()
    {
        return "Colors/Grayscale";
    }
}