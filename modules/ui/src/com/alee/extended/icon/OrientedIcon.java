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

package com.alee.extended.icon;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * User: mgarin Date: 08.11.12 Time: 13:20
 */

public class OrientedIcon extends ImageIcon
{
    public OrientedIcon ()
    {
        super ();
    }

    public OrientedIcon ( byte[] imageData )
    {
        super ( imageData );
    }

    public OrientedIcon ( byte[] imageData, String description )
    {
        super ( imageData, description );
    }

    public OrientedIcon ( Image image )
    {
        super ( image );
    }

    public OrientedIcon ( Image image, String description )
    {
        super ( image, description );
    }

    public OrientedIcon ( URL location )
    {
        super ( location );
    }

    public OrientedIcon ( URL location, String description )
    {
        super ( location, description );
    }

    public OrientedIcon ( String filename )
    {
        super ( filename );
    }

    public OrientedIcon ( String filename, String description )
    {
        super ( filename, description );
    }

    public OrientedIcon ( ImageIcon icon )
    {
        super ( icon.getImage () );
    }

    @Override
    public void paintIcon ( Component c, Graphics g, int x, int y )
    {
        int w = getIconWidth ();
        int h = getIconHeight ();
        if ( c == null || c.getComponentOrientation ().isLeftToRight () )
        {
            g.drawImage ( getImage (), x, y, w, h, getImageObserver () );
        }
        else
        {
            g.drawImage ( getImage (), x + w, y, -w, h, getImageObserver () );
        }
    }
}
