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

import com.alee.managers.drag.transfer.ImageTransferHandler;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.ImageUtils;
import com.alee.utils.MathUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * This is a custom image drop component.
 * It serves as a drop area for images and will display dropped image preview.
 * You can always retrieve actual and thumbnail images from this components if there are any.
 *
 * @author Mikle Garin
 */

public class WebImageDrop extends JComponent
{
    /**
     * Preview image corners rounding.
     */
    protected int round;

    /**
     * Preview image area width.
     */
    protected int width;

    /**
     * Preview image area height.
     */
    protected int height;

    /**
     * Actual image placed into WebImageDrop component.
     */
    protected BufferedImage actualImage;

    /**
     * Preview image.
     */
    protected BufferedImage image;

    /**
     * Constructs new WebImageDrop component with 64x64 preview image area size.
     */
    public WebImageDrop ()
    {
        this ( 64, 64 );
    }

    /**
     * Constructs new WebImageDrop component with the specified preview image area size.
     *
     * @param width  preview image area width
     * @param height preview image area height
     */
    public WebImageDrop ( final int width, final int height )
    {
        this ( width, height, null );
    }

    /**
     * Constructs new WebImageDrop component with the specified preview image area size and actual image.
     *
     * @param width  preview image area width
     * @param height preview image area height
     * @param image  actual image
     */
    public WebImageDrop ( final int width, final int height, final BufferedImage image )
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
        setTransferHandler ( new ImageTransferHandler ( false, true )
        {
            @Override
            protected boolean imagesImported ( final List<ImageIcon> images )
            {
                for ( final ImageIcon image : images )
                {
                    try
                    {
                        setImage ( ImageUtils.getBufferedImage ( image ) );
                        return true;
                    }
                    catch ( final Exception e )
                    {
                        //
                    }
                }
                return false;
            }
        } );
    }

    /**
     * Returns actual image.
     *
     * @return actual image
     */
    public BufferedImage getImage ()
    {
        return actualImage;
    }

    /**
     * Returns preview image.
     *
     * @return preview image
     */
    public BufferedImage getThumbnail ()
    {
        return image;
    }

    /**
     * Returns preview image corners rounding.
     *
     * @return preview image corners rounding
     */
    public int getRound ()
    {
        return round;
    }

    /**
     * Sets preview image corners rounding.
     *
     * @param round new preview image corners rounding
     */
    public void setRound ( final int round )
    {
        this.round = round;
        updatePreview ();
    }

    /**
     * Returns preview image area width.
     *
     * @return preview image area width
     */
    public int getImageWidth ()
    {
        return width;
    }

    /**
     * Sets preview image area width.
     *
     * @param width preview image area width
     */
    public void setImageWidth ( final int width )
    {
        this.width = width;
        updatePreview ();
    }

    /**
     * Returns preview image area height.
     *
     * @return preview image area height
     */
    public int getImageHeight ()
    {
        return height;
    }

    /**
     * Sets preview image area height.
     *
     * @param height new preview image area height
     */
    public void setImageHeight ( final int height )
    {
        this.height = height;
        updatePreview ();
    }

    /**
     * Sets new displayed image.
     * This forces a new preview image to be generated so be aware that this call does some heavy work.
     *
     * @param image new displayed image
     */
    public void setImage ( final BufferedImage image )
    {
        this.actualImage = image;
        this.image = image;
        updatePreview ();
        repaint ();
    }

    /**
     * Updates image preview.
     */
    protected void updatePreview ()
    {
        if ( image != null )
        {
            // Creating image preview
            image = ImageUtils.createPreviewImage ( actualImage, width, height );

            // Restore decoration
            final BufferedImage f = ImageUtils.createCompatibleImage ( image, Transparency.TRANSLUCENT );
            final Graphics2D g2d = f.createGraphics ();
            GraphicsUtils.setupAntialias ( g2d );
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
    protected void paintComponent ( final Graphics g )
    {
        super.paintComponent ( g );

        final Graphics2D g2d = ( Graphics2D ) g;
        final Object aa = GraphicsUtils.setupAntialias ( g2d );

        if ( image != null )
        {
            g2d.drawImage ( image, getWidth () / 2 - image.getWidth () / 2 + 1, getHeight () / 2 - image.getHeight () / 2 + 1, null );
        }

        final Shape border = new RoundRectangle2D.Double ( getWidth () / 2 - width / 2 + 1, getHeight () / 2 - height / 2 + 1,
                width - ( image == null ? 3 : 1 ), height - ( image == null ? 3 : 1 ), round * 2, round * 2 );

        if ( image == null )
        {
            g2d.setPaint ( new Color ( 242, 242, 242 ) );
            g2d.fill ( border );

            g2d.setStroke ( new BasicStroke ( 2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f,
                    new float[]{ MathUtils.limit ( 5f, Math.max ( width, height ) / 6, 10f ), 8f }, 4f ) );
            g2d.setPaint ( Color.LIGHT_GRAY );
            g2d.draw ( border );
        }

        GraphicsUtils.restoreAntialias ( g2d, aa );
    }

    @Override
    public Dimension getPreferredSize ()
    {
        return new Dimension ( width + 2, height + 2 );
    }
}