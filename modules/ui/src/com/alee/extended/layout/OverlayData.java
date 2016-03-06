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
 * @author Mikle Garin
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

    public OverlayData ( final int halign, final int valign )
    {
        super ();
        this.location = OverlayLocation.align;
        this.halign = halign;
        this.valign = valign;
    }

    public OverlayData ( final DataProvider<Rectangle> rectangleProvider )
    {
        super ();
        this.location = OverlayLocation.custom;
        this.rectangleProvider = rectangleProvider;
    }

    public OverlayData ( final OverlayLocation location, final int halign, final int valign,
                         final DataProvider<Rectangle> rectangleProvider )
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

    public void setLocation ( final OverlayLocation location )
    {
        this.location = location;
    }

    public int getHalign ()
    {
        return halign;
    }

    public void setHalign ( final int halign )
    {
        this.halign = halign;
    }

    public int getValign ()
    {
        return valign;
    }

    public void setValign ( final int valign )
    {
        this.valign = valign;
    }

    public DataProvider<Rectangle> getRectangleProvider ()
    {
        return rectangleProvider;
    }

    public void setRectangleProvider ( final DataProvider<Rectangle> rectangleProvider )
    {
        this.rectangleProvider = rectangleProvider;
    }
}