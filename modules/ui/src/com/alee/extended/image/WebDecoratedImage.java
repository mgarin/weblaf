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

import com.alee.graphics.filters.GaussianFilter;
import com.alee.graphics.filters.GrayscaleFilter;
import com.alee.graphics.filters.MotionBlurFilter;
import com.alee.managers.style.ShapeMethods;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.ImageUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * @author Mikle Garin
 */

public class WebDecoratedImage extends JComponent implements SwingConstants, ShapeMethods
{
    /**
     * todo 1. Implement proper UI and styling
     */

    private ImageIcon icon;
    private ImageIcon previewIcon;

    // Preview alignment 
    private int horizontalAlignment = WebDecoratedImageStyle.horizontalAlignment;
    private int verticalAlignment = WebDecoratedImageStyle.verticalAlignment;

    // Basic effects
    private boolean drawBorder = WebDecoratedImageStyle.drawBorder;
    private Color borderColor = WebDecoratedImageStyle.borderColor;
    private boolean drawGlassLayer = WebDecoratedImageStyle.drawGlassLayer;
    private int shadeWidth = WebDecoratedImageStyle.shadeWidth;
    private int round = WebDecoratedImageStyle.round;

    // Filters
    private float blurAlignX = WebDecoratedImageStyle.blurAlignX;
    private float blurAlignY = WebDecoratedImageStyle.blurAlignY;
    private boolean grayscale = WebDecoratedImageStyle.grayscale;
    private boolean blur = WebDecoratedImageStyle.blur;
    private float blurFactor = WebDecoratedImageStyle.blurFactor;
    private boolean zoomBlur = WebDecoratedImageStyle.zoomBlur;
    private float zoomBlurFactor = WebDecoratedImageStyle.zoomBlurFactor;
    private boolean rotationBlur = WebDecoratedImageStyle.rotationBlur;
    private float rotationBlurFactor = WebDecoratedImageStyle.rotationBlurFactor;

    public WebDecoratedImage ()
    {
        this ( ( ImageIcon ) null );
    }

    public WebDecoratedImage ( final String src )
    {
        this ( ImageUtils.loadImage ( src ) );
    }

    public WebDecoratedImage ( final Class nearClass, final String src )
    {
        this ( ImageUtils.loadImage ( nearClass, src ) );
    }

    public WebDecoratedImage ( final Image image )
    {
        super ();
        SwingUtils.setOrientation ( this );
        setImage ( image );
    }

    public WebDecoratedImage ( final ImageIcon icon )
    {
        super ();
        SwingUtils.setOrientation ( this );
        setIcon ( icon );
    }

    public ImageIcon getPreviewIcon ()
    {
        return previewIcon;
    }

    public ImageIcon getIcon ()
    {
        return icon;
    }

    public void setImage ( final Image image )
    {
        setImage ( image, true );
    }

    public void setImage ( final Image image, final boolean update )
    {
        setIcon ( new ImageIcon ( image ), update );
    }

    public void setIcon ( final ImageIcon icon )
    {
        setIcon ( icon, true );
    }

    public void setIcon ( final ImageIcon icon, final boolean update )
    {
        this.icon = icon;
        if ( update )
        {
            updatePreview ();
        }
    }

    public int getHorizontalAlignment ()
    {
        return horizontalAlignment;
    }

    public void setHorizontalAlignment ( final int horizontalAlignment )
    {
        this.horizontalAlignment = horizontalAlignment;
        repaint ();
    }

    public int getVerticalAlignment ()
    {
        return verticalAlignment;
    }

    public void setVerticalAlignment ( final int verticalAlignment )
    {
        this.verticalAlignment = verticalAlignment;
        repaint ();
    }

    public boolean isDrawBorder ()
    {
        return drawBorder;
    }

    public void setDrawBorder ( final boolean drawBorder )
    {
        setDrawBorder ( drawBorder, true );
    }

    public void setDrawBorder ( final boolean drawBorder, final boolean update )
    {
        this.drawBorder = drawBorder;
        if ( update )
        {
            updatePreview ();
        }
    }

    public Color getBorderColor ()
    {
        return borderColor;
    }

    public void setBorderColor ( final Color borderColor )
    {
        setBorderColor ( borderColor, true );
    }

    public void setBorderColor ( final Color borderColor, final boolean update )
    {
        this.borderColor = borderColor;
        if ( update )
        {
            updatePreview ();
        }
    }

    public boolean isDrawGlassLayer ()
    {
        return drawGlassLayer;
    }

    public void setDrawGlassLayer ( final boolean drawGlassLayer )
    {
        setDrawGlassLayer ( drawGlassLayer, true );
    }

    public void setDrawGlassLayer ( final boolean drawGlassLayer, final boolean update )
    {
        this.drawGlassLayer = drawGlassLayer;
        if ( update )
        {
            updatePreview ();
        }
    }

    public int getShadeWidth ()
    {
        return shadeWidth;
    }

    public void setShadeWidth ( final int shadeWidth )
    {
        setShadeWidth ( shadeWidth, true );
    }

    public void setShadeWidth ( final int shadeWidth, final boolean update )
    {
        this.shadeWidth = shadeWidth;
        if ( update )
        {
            updatePreview ();
        }
    }

    public int getRound ()
    {
        return round;
    }

    public void setRound ( final int round )
    {
        setRound ( round, true );
    }

    public void setRound ( final int round, final boolean update )
    {
        this.round = round;
        if ( update )
        {
            updatePreview ();
        }
    }

    public boolean isGrayscale ()
    {
        return grayscale;
    }

    public void setGrayscale ( final boolean grayscale )
    {
        setGrayscale ( grayscale, true );
    }

    public void setGrayscale ( final boolean grayscale, final boolean update )
    {
        this.grayscale = grayscale;
        if ( update )
        {
            updatePreview ();
        }
    }

    public boolean isBlur ()
    {
        return blur;
    }

    public void setBlur ( final boolean blur )
    {
        setBlur ( blur, true );
    }

    public void setBlur ( final boolean blur, final boolean update )
    {
        this.blur = blur;
        if ( update )
        {
            updatePreview ();
        }
    }

    public float getBlurFactor ()
    {
        return blurFactor;
    }

    public void setBlurFactor ( final float blurFactor )
    {
        setBlurFactor ( blurFactor, true );
    }

    public void setBlurFactor ( final float blurFactor, final boolean update )
    {
        this.blurFactor = blurFactor;
        if ( update )
        {
            updatePreview ();
        }
    }

    public boolean isZoomBlur ()
    {
        return zoomBlur;
    }

    public void setZoomBlur ( final boolean zoomBlur )
    {
        setZoomBlur ( zoomBlur, true );
    }

    public void setZoomBlur ( final boolean zoomBlur, final boolean update )
    {
        this.zoomBlur = zoomBlur;
        if ( update )
        {
            updatePreview ();
        }
    }

    public float getZoomBlurFactor ()
    {
        return zoomBlurFactor;
    }

    public void setZoomBlurFactor ( final float zoomBlurFactor )
    {
        setZoomBlurFactor ( zoomBlurFactor, true );
    }

    public void setZoomBlurFactor ( final float zoomBlurFactor, final boolean update )
    {
        this.zoomBlurFactor = zoomBlurFactor;
        if ( update )
        {
            updatePreview ();
        }
    }

    public boolean isRotationBlur ()
    {
        return rotationBlur;
    }

    public void setRotationBlur ( final boolean rotationBlur )
    {
        setRotationBlur ( rotationBlur, true );
    }

    public void setRotationBlur ( final boolean rotationBlur, final boolean update )
    {
        this.rotationBlur = rotationBlur;
        if ( update )
        {
            updatePreview ();
        }
    }

    public float getRotationBlurFactor ()
    {
        return rotationBlurFactor;
    }

    public void setRotationBlurFactor ( final float rotationBlurFactor )
    {
        setRotationBlurFactor ( rotationBlurFactor, true );
    }

    public void setRotationBlurFactor ( final float rotationBlurFactor, final boolean update )
    {
        this.rotationBlurFactor = rotationBlurFactor;
        if ( update )
        {
            updatePreview ();
        }
    }

    public float getBlurAlignX ()
    {
        return blurAlignX;
    }

    public void setBlurAlignX ( final float blurAlignX )
    {
        setBlurAlignX ( blurAlignX, true );
    }

    public void setBlurAlignX ( final float blurAlignX, final boolean update )
    {
        this.blurAlignX = blurAlignX;
        if ( update )
        {
            updatePreview ();
        }
    }

    public float getBlurAlignY ()
    {
        return blurAlignY;
    }

    public void setBlurAlignY ( final float blurAlignY )
    {
        setBlurAlignY ( blurAlignY, true );
    }

    public void setBlurAlignY ( final float blurAlignY, final boolean update )
    {
        this.blurAlignY = blurAlignY;
        if ( update )
        {
            updatePreview ();
        }
    }

    public void updatePreview ()
    {
        if ( icon == null )
        {
            // No preview available
            previewIcon = null;

            // Updating component view
            repaint ();

            return;
        }

        // Source image
        BufferedImage image = ImageUtils.copy ( icon.getImage () );

        // Applying filters
        if ( grayscale )
        {
            new GrayscaleFilter ().filter ( image, image );
        }
        if ( blur )
        {
            new GaussianFilter ( blurFactor ).filter ( image, image );
        }
        if ( zoomBlur && rotationBlur )
        {
            new MotionBlurFilter ( 0f, 0f, rotationBlurFactor, zoomBlurFactor, blurAlignX, blurAlignY ).filter ( image, image );
        }
        else if ( zoomBlur )
        {
            new MotionBlurFilter ( 0f, 0f, 0f, zoomBlurFactor, blurAlignX, blurAlignY ).filter ( image, image );
        }
        else if ( rotationBlur )
        {
            new MotionBlurFilter ( 0f, 0f, rotationBlurFactor, 0f, blurAlignX, blurAlignY ).filter ( image, image );
        }

        // Applying rounded corners
        if ( round > 0 )
        {
            image = ImageUtils
                    .cutImage ( new RoundRectangle2D.Double ( 0, 0, icon.getIconWidth (), icon.getIconHeight (), round * 2, round * 2 ),
                            image );
        }

        // Creating additional effects
        if ( shadeWidth > 0 || drawGlassLayer || drawBorder )
        {
            final Dimension ps = getPreferredSize ();
            final BufferedImage img = ImageUtils.createCompatibleImage ( ps.width, ps.height, Transparency.TRANSLUCENT );
            final Graphics2D g2d = img.createGraphics ();
            GraphicsUtils.setupAntialias ( g2d );
            final Shape bs = getBorderShape ();

            // Shade
            GraphicsUtils.drawShade ( g2d, bs, WebDecoratedImageStyle.shadeType, new Color ( 90, 90, 90 ), shadeWidth );

            // Image itself
            g2d.drawImage ( image, shadeWidth, shadeWidth, null );

            // Glass-styled shade
            if ( drawGlassLayer )
            {
                g2d.setPaint ( new GradientPaint ( 0, shadeWidth, new Color ( 255, 255, 255, 160 ), 0,
                        shadeWidth + ( ps.height - shadeWidth * 2 ) / 2, new Color ( 255, 255, 255, 32 ) ) );
                g2d.fill ( getGlanceShape () );
            }

            // Border
            if ( drawBorder )
            {
                g2d.setPaint ( borderColor );
                g2d.draw ( bs );
            }

            g2d.dispose ();
            image = img;
        }

        // Updating preview
        previewIcon = new ImageIcon ( image );

        // Updating component view
        repaint ();
    }

    private Shape getGlanceShape ()
    {
        final Dimension ps = getPreferredSize ();
        if ( round > 0 )
        {
            return new RoundRectangle2D.Double ( shadeWidth, shadeWidth, ps.width - shadeWidth * 2, ( ps.height - shadeWidth * 2 ) / 2,
                    round * 2, round * 2 );
        }
        else
        {
            return new Rectangle ( shadeWidth, shadeWidth, ps.width - shadeWidth * 2, ( ps.height - shadeWidth * 2 ) / 2 );
        }
    }

    private Shape getBorderShape ()
    {
        return getBorderShape ( 0, 0 );
    }

    private Shape getBorderShape ( final int x, final int y )
    {
        final Dimension ps = getPreferredSize ();
        if ( round > 0 )
        {
            return new RoundRectangle2D.Double ( x + shadeWidth, y + shadeWidth, ps.width - shadeWidth * 2 - 1,
                    ps.height - shadeWidth * 2 - 1, round * 2, round * 2 );
        }
        else
        {
            return new Rectangle ( x + shadeWidth, y + shadeWidth, ps.width - shadeWidth * 2 - 1, ps.height - shadeWidth * 2 - 1 );
        }
    }

    @Override
    protected void paintComponent ( final Graphics g )
    {
        super.paintComponent ( g );

        // Painting preview
        if ( previewIcon != null )
        {
            final Point location = getPreviewLocation ();
            g.drawImage ( previewIcon.getImage (), location.x, location.y, null );
        }
    }

    private Point getPreviewLocation ()
    {
        final int width = previewIcon.getIconWidth ();
        final int height = previewIcon.getIconHeight ();
        final int x;
        if ( horizontalAlignment == LEFT )
        {
            x = 0;
        }
        else if ( horizontalAlignment == RIGHT )
        {
            x = getWidth () - width;
        }
        else
        {
            x = getWidth () / 2 - width / 2;
        }
        final int y;
        if ( verticalAlignment == TOP )
        {
            y = 0;
        }
        else if ( verticalAlignment == BOTTOM )
        {
            y = getHeight () - height;
        }
        else
        {
            y = getHeight () / 2 - height / 2;
        }
        return new Point ( x, y );
    }

    @Override
    public Dimension getPreferredSize ()
    {
        if ( icon != null )
        {
            return new Dimension ( shadeWidth * 2 + icon.getIconWidth (), shadeWidth * 2 + icon.getIconHeight () );
        }
        else
        {
            return new Dimension ( 0, 0 );
        }
    }

    @Override
    public Shape getShape ()
    {
        final Point location = getPreviewLocation ();
        return getBorderShape ( location.x, location.y );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return false;
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        throw new UnsupportedOperationException ( "Shape detection is not yet supported for WebDecoratedImage" );
    }
}