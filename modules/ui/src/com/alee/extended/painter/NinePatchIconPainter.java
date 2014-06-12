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
 * Custom painter for 9-patch icon.
 *
 * @param <E> component type
 * @author Mikle Garin
 * @see NinePatchIcon
 * @see com.alee.extended.painter.NinePatchStatePainter
 * @see AbstractPainter
 * @see Painter
 */

public class NinePatchIconPainter<E extends JComponent> extends AbstractPainter<E>
{
    /**
     * 9-patch icon to paint.
     */
    protected NinePatchIcon icon;

    /**
     * Constructs new 9-patch icon painter.
     */
    public NinePatchIconPainter ()
    {
        this ( ( NinePatchIcon ) null );
    }

    /**
     * Constructs new 9-patch icon painter.
     *
     * @param url 9-patch image URL
     */
    public NinePatchIconPainter ( final URL url )
    {
        this ( new NinePatchIcon ( url ) );
    }

    /**
     * Constructs new 9-patch icon painter.
     *
     * @param iconSrc 9-patch image source
     */
    public NinePatchIconPainter ( final String iconSrc )
    {
        this ( new NinePatchIcon ( iconSrc ) );
    }

    /**
     * Constructs new 9-patch icon painter.
     *
     * @param imageIcon 9-patch image
     */
    public NinePatchIconPainter ( final ImageIcon imageIcon )
    {
        this ( new NinePatchIcon ( imageIcon ) );
    }

    /**
     * Constructs new 9-patch icon painter.
     *
     * @param image 9-patch image
     */
    public NinePatchIconPainter ( final Image image )
    {
        this ( new NinePatchIcon ( image ) );
    }

    /**
     * Constructs new 9-patch icon painter.
     *
     * @param bufferedImage 9-patch image
     */
    public NinePatchIconPainter ( final BufferedImage bufferedImage )
    {
        this ( new NinePatchIcon ( bufferedImage ) );
    }

    /**
     * Constructs new 9-patch icon painter.
     *
     * @param icon 9-patch icon
     */
    public NinePatchIconPainter ( final NinePatchIcon icon )
    {
        super ();
        this.icon = icon;
    }

    /**
     * Returns painted 9-patch icon.
     *
     * @return painted 9-patch icon
     */
    public NinePatchIcon getNinePatchIcon ()
    {
        return icon;
    }

    /**
     * Sets painted 9-patch icon.
     *
     * @param icon painted 9-patch icon
     */
    public void setNinePatchIcon ( final NinePatchIcon icon )
    {
        this.icon = icon;
        updateAll ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize ( final E c )
    {
        if ( icon != null )
        {
            icon.setComponent ( c );
            return icon.getPreferredSize ();
        }
        else
        {
            return super.getPreferredSize ( c );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getMargin ( final E c )
    {
        final Insets margin = super.getMargin ( c );
        if ( icon != null )
        {
            icon.setComponent ( c );
            return SwingUtils.max ( margin, icon.getMargin () );
        }
        else
        {
            return margin;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c )
    {
        if ( icon != null )
        {
            icon.setComponent ( c );
            icon.paintIcon ( c, g2d );
        }
    }
}