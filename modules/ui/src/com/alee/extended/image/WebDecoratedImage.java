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

import com.alee.utils.GraphicsUtils;
import com.alee.utils.ImageFilterUtils;
import com.alee.utils.ImageUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * User: mgarin Date: 05.06.12 Time: 16:23
 */

public class WebDecoratedImage extends JComponent implements SwingConstants, ShapeProvider
{
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

    public WebDecoratedImage ( String src )
    {
        this ( ImageUtils.loadImage ( src ) );
    }

    public WebDecoratedImage ( Class nearClass, String src )
    {
        this ( ImageUtils.loadImage ( nearClass, src ) );
    }

    public WebDecoratedImage ( Image image )
    {
        super ();
        SwingUtils.setOrientation ( this );
        setImage ( image );
    }

    public WebDecoratedImage ( ImageIcon icon )
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

    public void setImage ( Image image )
    {
        setImage ( image, true );
    }

    public void setImage ( Image image, boolean update )
    {
        setIcon ( new ImageIcon ( image ), update );
    }

    public void setIcon ( ImageIcon icon )
    {
        setIcon ( icon, true );
    }

    public void setIcon ( ImageIcon icon, boolean update )
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

    public void setHorizontalAlignment ( int horizontalAlignment )
    {
        this.horizontalAlignment = horizontalAlignment;
        repaint ();
    }

    public int getVerticalAlignment ()
    {
        return verticalAlignment;
    }

    public void setVerticalAlignment ( int verticalAlignment )
    {
        this.verticalAlignment = verticalAlignment;
        repaint ();
    }

    public boolean isDrawBorder ()
    {
        return drawBorder;
    }

    public void setDrawBorder ( boolean drawBorder )
    {
        setDrawBorder ( drawBorder, true );
    }

    public void setDrawBorder ( boolean drawBorder, boolean update )
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

    public void setBorderColor ( Color borderColor )
    {
        setBorderColor ( borderColor, true );
    }

    public void setBorderColor ( Color borderColor, boolean update )
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

    public void setDrawGlassLayer ( boolean drawGlassLayer )
    {
        setDrawGlassLayer ( drawGlassLayer, true );
    }

    public void setDrawGlassLayer ( boolean drawGlassLayer, boolean update )
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

    public void setShadeWidth ( int shadeWidth )
    {
        setShadeWidth ( shadeWidth, true );
    }

    public void setShadeWidth ( int shadeWidth, boolean update )
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

    public void setRound ( int round )
    {
        setRound ( round, true );
    }

    public void setRound ( int round, boolean update )
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

    public void setGrayscale ( boolean grayscale )
    {
        setGrayscale ( grayscale, true );
    }

    public void setGrayscale ( boolean grayscale, boolean update )
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

    public void setBlur ( boolean blur )
    {
        setBlur ( blur, true );
    }

    public void setBlur ( boolean blur, boolean update )
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

    public void setBlurFactor ( float blurFactor )
    {
        setBlurFactor ( blurFactor, true );
    }

    public void setBlurFactor ( float blurFactor, boolean update )
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

    public void setZoomBlur ( boolean zoomBlur )
    {
        setZoomBlur ( zoomBlur, true );
    }

    public void setZoomBlur ( boolean zoomBlur, boolean update )
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

    public void setZoomBlurFactor ( float zoomBlurFactor )
    {
        setZoomBlurFactor ( zoomBlurFactor, true );
    }

    public void setZoomBlurFactor ( float zoomBlurFactor, boolean update )
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

    public void setRotationBlur ( boolean rotationBlur )
    {
        setRotationBlur ( rotationBlur, true );
    }

    public void setRotationBlur ( boolean rotationBlur, boolean update )
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

    public void setRotationBlurFactor ( float rotationBlurFactor )
    {
        setRotationBlurFactor ( rotationBlurFactor, true );
    }

    public void setRotationBlurFactor ( float rotationBlurFactor, boolean update )
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

    public void setBlurAlignX ( float blurAlignX )
    {
        setBlurAlignX ( blurAlignX, true );
    }

    public void setBlurAlignX ( float blurAlignX, boolean update )
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

    public void setBlurAlignY ( float blurAlignY )
    {
        setBlurAlignY ( blurAlignY, true );
    }

    public void setBlurAlignY ( float blurAlignY, boolean update )
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
        Image image = ImageUtils.copy ( icon.getImage () );

        // Applying filters
        if ( grayscale )
        {
            ImageFilterUtils.applyGrayscaleFilter ( image, image );
        }
        if ( blur )
        {
            ImageFilterUtils.applyGaussianFilter ( image, image, blurFactor );
        }
        if ( zoomBlur && rotationBlur )
        {
            ImageFilterUtils.applyMotionBlurFilter ( image, image, 0f, 0f, rotationBlurFactor, zoomBlurFactor, blurAlignX, blurAlignY );
        }
        else if ( zoomBlur )
        {
            ImageFilterUtils.applyZoomBlurFilter ( image, image, zoomBlurFactor, blurAlignX, blurAlignY );
        }
        else if ( rotationBlur )
        {
            ImageFilterUtils.applyRotationBlurFilter ( image, image, rotationBlurFactor, blurAlignX, blurAlignY );
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
            Dimension ps = getPreferredSize ();
            BufferedImage img = ImageUtils.createCompatibleImage ( ps.width, ps.height, Transparency.TRANSLUCENT );
            Graphics2D g2d = img.createGraphics ();
            GraphicsUtils.setupAntialias ( g2d );
            Shape bs = getBorderShape ();

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
        Dimension ps = getPreferredSize ();
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

    private Shape getBorderShape ( int x, int y )
    {
        Dimension ps = getPreferredSize ();
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
    protected void paintComponent ( Graphics g )
    {
        super.paintComponent ( g );

        // Painting preview
        if ( previewIcon != null )
        {
            Point location = getPreviewLocation ();
            g.drawImage ( previewIcon.getImage (), location.x, location.y, null );
        }
    }

    private Point getPreviewLocation ()
    {
        int width = previewIcon.getIconWidth ();
        int height = previewIcon.getIconHeight ();
        int x;
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
        int y;
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
    public Shape provideShape ()
    {
        Point location = getPreviewLocation ();
        return getBorderShape ( location.x, location.y );
    }
}