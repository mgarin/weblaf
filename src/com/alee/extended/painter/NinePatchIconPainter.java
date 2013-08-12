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

package com.alee.extended.painter;

import com.alee.utils.SwingUtils;
import com.alee.utils.ninepatch.NinePatchIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * User: mgarin Date: 14.12.11 Time: 15:20
 */

public class NinePatchIconPainter<E extends JComponent> extends DefaultPainter<E>
{
    private NinePatchIcon ninePatchIcon;

    public NinePatchIconPainter ()
    {
        this ( ( NinePatchIcon ) null );
    }

    public NinePatchIconPainter ( URL url )
    {
        this ( new NinePatchIcon ( url ) );
    }

    public NinePatchIconPainter ( String iconSrc )
    {
        this ( new NinePatchIcon ( iconSrc ) );
    }

    public NinePatchIconPainter ( ImageIcon imageIcon )
    {
        this ( new NinePatchIcon ( imageIcon ) );
    }

    public NinePatchIconPainter ( Image image )
    {
        this ( new NinePatchIcon ( image ) );
    }

    public NinePatchIconPainter ( BufferedImage bufferedImage )
    {
        this ( new NinePatchIcon ( bufferedImage ) );
    }


    public NinePatchIconPainter ( NinePatchIcon ninePatchIcon )
    {
        super ();
        this.ninePatchIcon = ninePatchIcon;
    }

    public NinePatchIcon getNinePatchIcon ()
    {
        return ninePatchIcon;
    }

    public void setNinePatchIcon ( NinePatchIcon ninePatchIcon )
    {
        this.ninePatchIcon = ninePatchIcon;
    }

    public void paint ( Graphics2D g2d, Rectangle bounds, E c )
    {
        if ( ninePatchIcon != null )
        {
            ninePatchIcon.setComponent ( c );
            ninePatchIcon.paintIcon ( c, g2d );
        }
    }

    public Dimension getPreferredSize ( E c )
    {
        if ( ninePatchIcon != null )
        {
            ninePatchIcon.setComponent ( c );
            return ninePatchIcon.getPreferredSize ();
        }
        else
        {
            return super.getPreferredSize ( c );
        }
    }

    public Insets getMargin ( E c )
    {
        Insets margin = super.getMargin ( c );
        if ( ninePatchIcon != null )
        {
            ninePatchIcon.setComponent ( c );
            return SwingUtils.max ( margin, ninePatchIcon.getMargin () );
        }
        else
        {
            return margin;
        }
    }
}
