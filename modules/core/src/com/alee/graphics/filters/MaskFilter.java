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

/*
Copyright 2006 Jerry Huxtable

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.alee.graphics.filters;

/**
 * Applies a bit mask to each ARGB pixel of an image. You can use this for, say, masking out the red channel.
 */

public class MaskFilter extends PointFilter
{
    private int mask;

    public MaskFilter ()
    {
        this ( 0xff00ffff );
    }

    public MaskFilter ( final int mask )
    {
        canFilterIndexColorModel = true;
        setMask ( mask );
    }

    public void setMask ( final int mask )
    {
        this.mask = mask;
    }

    public int getMask ()
    {
        return mask;
    }

    public int filterRGB ( final int x, final int y, final int rgb )
    {
        return rgb & mask;
    }

    @Override
    public String toString ()
    {
        return "Mask";
    }
}