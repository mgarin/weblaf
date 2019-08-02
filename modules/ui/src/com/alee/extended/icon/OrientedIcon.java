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
 * @author Mikle Garin
 */
public class OrientedIcon extends ImageIcon
{
    public OrientedIcon ()
    {
        super ();
    }

    public OrientedIcon ( final byte[] imageData )
    {
        super ( imageData );
    }

    public OrientedIcon ( final byte[] imageData, final String description )
    {
        super ( imageData, description );
    }

    public OrientedIcon ( final Image image )
    {
        super ( image );
    }

    public OrientedIcon ( final Image image, final String description )
    {
        super ( image, description );
    }

    public OrientedIcon ( final URL location )
    {
        super ( location );
    }

    public OrientedIcon ( final URL location, final String description )
    {
        super ( location, description );
    }

    public OrientedIcon ( final String filename )
    {
        super ( filename );
    }

    public OrientedIcon ( final String filename, final String description )
    {
        super ( filename, description );
    }

    public OrientedIcon ( final ImageIcon icon )
    {
        super ( icon.getImage () );
    }

    @Override
    public void paintIcon ( final Component c, final Graphics g, final int x, final int y )
    {
        final int w = getIconWidth ();
        final int h = getIconHeight ();
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