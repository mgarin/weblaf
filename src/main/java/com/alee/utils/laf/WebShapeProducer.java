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

package com.alee.utils.laf;

import com.alee.utils.SwingUtils;

import java.awt.*;

/**
 * User: mgarin Date: 12/6/11 Time: 3:52 PM
 */

public class WebShapeProducer extends ShapeProducer
{
    private ShapeProvider shapeProvider;

    public WebShapeProducer ( Component component )
    {
        super ( component );
    }

    public void setProduceFor ( Component produceFor )
    {
        super.setProduceFor ( produceFor );

        this.shapeProvider = SwingUtils.getShapeProvider ( produceFor );
    }

    public Shape produce ()
    {
        if ( shapeProvider != null )
        {
            return shapeProvider.provideShape ();
        }
        else
        {
            return null;
        }
    }
}
