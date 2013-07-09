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

package com.alee.extended.button;

import com.alee.laf.button.WebButtonUI;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * User: mgarin Date: 30.12.11 Time: 19:47
 */

public class WebAngleButtonUI extends WebButtonUI
{
    private int angleLength = 10;

    public int getAngleLength ()
    {
        return angleLength;
    }

    public void setAngleLength ( int angleLength )
    {
        this.angleLength = angleLength;
    }

    protected Shape getButtonShape ( AbstractButton button )
    {
        int height = button.getHeight ();
        int round = getRound ();
        int shadeWidth = getShadeWidth ();

        GeneralPath shape = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        shape.moveTo ( shadeWidth + round, shadeWidth );
        shape.quadTo ( shadeWidth, shadeWidth, shadeWidth, shadeWidth + round );
        shape.lineTo ( shadeWidth, height - shadeWidth - round );
        shape.quadTo ( shadeWidth, height - shadeWidth, shadeWidth + round, height - shadeWidth );
        return shape;
    }
}
