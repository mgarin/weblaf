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

package com.alee.extended.layout;

import com.alee.utils.swing.DataProvider;

import java.awt.*;

/**
 * User: mgarin Date: 05.06.12 Time: 20:06
 */

public class OverlayData
{
    private OverlayLocation location;
    private int halign;
    private int valign;
    private DataProvider<Rectangle> rectangleProvider;

    public OverlayData ()
    {
        super ();
        this.location = OverlayLocation.fill;
    }

    public OverlayData ( int halign, int valign )
    {
        super ();
        this.location = OverlayLocation.align;
        this.halign = halign;
        this.valign = valign;
    }

    public OverlayData ( DataProvider<Rectangle> rectangleProvider )
    {
        super ();
        this.location = OverlayLocation.custom;
        this.rectangleProvider = rectangleProvider;
    }

    public OverlayData ( OverlayLocation location, int halign, int valign, DataProvider<Rectangle> rectangleProvider )
    {
        super ();
        this.location = location;
        this.halign = halign;
        this.valign = valign;
        this.rectangleProvider = rectangleProvider;
    }

    public OverlayLocation getLocation ()
    {
        return location;
    }

    public void setLocation ( OverlayLocation location )
    {
        this.location = location;
    }

    public int getHalign ()
    {
        return halign;
    }

    public void setHalign ( int halign )
    {
        this.halign = halign;
    }

    public int getValign ()
    {
        return valign;
    }

    public void setValign ( int valign )
    {
        this.valign = valign;
    }

    public DataProvider<Rectangle> getRectangleProvider ()
    {
        return rectangleProvider;
    }

    public void setRectangleProvider ( DataProvider<Rectangle> rectangleProvider )
    {
        this.rectangleProvider = rectangleProvider;
    }
}