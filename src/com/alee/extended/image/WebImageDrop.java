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

package com.alee.extended.image;

import com.alee.extended.drag.ImageDropHandler;
import com.alee.utils.ImageUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * User: mgarin Date: 11/14/11 Time: 4:34 PM
 */

public class WebImageDrop extends JComponent
{
    private int round;
    private int width;
    private int height;
    private BufferedImage actualImage;
    private BufferedImage image;

    public WebImageDrop ()
    {
        this ( 64, 64 );
    }

    public WebImageDrop ( int width, int height )
    {
        this ( width, height, null );
    }

    public WebImageDrop ( int width, int height, BufferedImage image )
    {
        super ();

        this.width = width;
        this.height = height;

        this.round = Math.max ( Math.max ( width, height ) / 10, 3 );

        this.actualImage = image;

        this.image = image;
        updatePreview ();

        SwingUtils.setOrientation ( this );

        // Image drop handler
        setTransferHandler ( new ImageDropHandler ()
        {
            @Override
            protected boolean imagesImported ( List<ImageIcon> images )
            {
                for ( ImageIcon image : images )
                {
                    try
                    {
                        setImage ( ImageUtils.getBufferedImage ( images.get ( 0 ) ) );
                        return true;
                    }
                    catch ( Throwable e )
                    {
                        //
                    }
                }
                return false;
            }
        } );
    }

    public BufferedImage getImage ()
    {
        return actualImage;
    }

    public BufferedImage getThumbnail ()
    {
        return image;
    }

    public int getRound ()
    {
        return round;
    }

    public void setRound ( int round )
    {
        this.round = round;
        updatePreview ();
    }

    public int getImageWidth ()
    {
        return width;
    }

    public void setImageWidth ( int width )
    {
        this.width = width;
        updatePreview ();
    }

    public int getImageHeight ()
    {
        return height;
    }

    public void setImageHeight ( int height )
    {
        this.height = height;
        updatePreview ();
    }

    public void setImage ( BufferedImage image )
    {
        this.actualImage = image;

        this.image = image;
        updatePreview ();

        repaint ();
    }

    private void updatePreview ()
    {
        if ( image != null )
        {
            // Validate size
            image = ImageUtils.createPreviewImage ( actualImage, width, height );

            // Restore decoration
            BufferedImage f = ImageUtils.createCompatibleImage ( image, Transparency.TRANSLUCENT );
            Graphics2D g2d = f.createGraphics ();
            LafUtils.setupAntialias ( g2d );
            g2d.setPaint ( Color.WHITE );
            g2d.fillRoundRect ( 0, 0, image.getWidth (), image.getHeight (), round * 2, round * 2 );
            g2d.setComposite ( AlphaComposite.getInstance ( AlphaComposite.SRC_IN ) );
            g2d.drawImage ( image, 0, 0, null );
            g2d.dispose ();

            image.flush ();
            image = f;
        }
    }

    @Override
    protected void paintComponent ( Graphics g )
    {
        super.paintComponent ( g );

        Graphics2D g2d = ( Graphics2D ) g;
        LafUtils.setupAntialias ( g2d );

        if ( image != null )
        {
            g2d.drawImage ( image, getWidth () / 2 - image.getWidth () / 2 + 1, getHeight () / 2 - image.getHeight () / 2 + 1, null );
        }

        Shape border = new RoundRectangle2D.Double ( getWidth () / 2 - width / 2 + 1, getHeight () / 2 - height / 2 + 1,
                width - ( image == null ? 3 : 1 ), height - ( image == null ? 3 : 1 ), round * 2, round * 2 );

        if ( image == null )
        {
            g2d.setPaint ( new Color ( 242, 242, 242 ) );
            g2d.fill ( border );

            g2d.setStroke ( new BasicStroke ( 2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f,
                    new float[]{ Math.max ( 5f, Math.min ( Math.max ( width, height ) / 6, 10f ) ), 8f }, 4f ) );
            g2d.setPaint ( Color.LIGHT_GRAY );
            g2d.draw ( border );
        }
    }

    @Override
    public Dimension getPreferredSize ()
    {
        return new Dimension ( width + 2, height + 2 );
    }
}
