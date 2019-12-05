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

package com.alee.painter.decoration.background;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.painter.decoration.DecorationException;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.ImageUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Alpha layer background.
 * Fills component shape with an alpha layer -like background.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> background type
 * @author Mikle Garin
 */
@XStreamAlias ( "AlphaLayerBackground" )
public class AlphaLayerBackground<C extends JComponent, D extends IDecoration<C, D>, I extends AlphaLayerBackground<C, D, I>>
        extends AbstractTextureBackground<C, D, I>
{
    /**
     * Default cell size.
     */
    public static final Dimension DEFAULT_CELL_SIZE = new Dimension ( 10, 10 );

    /**
     * Default dark cells {@link Color}.
     */
    public static final Color DEFAULT_DARK_COLOR = new Color ( 204, 204, 204 );

    /**
     * Default light cells {@link Color}.
     */
    public static final Color DEFAULT_LIGHT_COLOR = Color.WHITE;

    /**
     * Cells size.
     */
    @Nullable
    @XStreamAsAttribute
    protected Dimension size;

    /**
     * Dark cell color.
     */
    @Nullable
    @XStreamAsAttribute
    protected Color darkColor;

    /**
     * Light cell color.
     */
    @Nullable
    @XStreamAsAttribute
    protected Color lightColor;

    /**
     * Returns cells size.
     *
     * @param c {@link JComponent} that is being painted
     * @param d {@link IDecoration} state
     * @return cells size
     */
    @NotNull
    protected Dimension getSize ( @NotNull final C c, @NotNull final D d )
    {
        final Dimension size = this.size != null ? this.size : DEFAULT_CELL_SIZE;
        if ( size.width <= 0 || size.height <= 0 )
        {
            throw new DecorationException ( "Cell size must be greater than zero" );
        }
        return size;
    }

    /**
     * Returns dark cell color.
     *
     * @param c {@link JComponent} that is being painted
     * @param d {@link IDecoration} state
     * @return dark cell color
     */
    @NotNull
    protected Color getDarkColor ( @NotNull final C c, @NotNull final D d )
    {
        return darkColor != null ? darkColor : DEFAULT_DARK_COLOR;
    }

    /**
     * Returns light cell color.
     *
     * @param c {@link JComponent} that is being painted
     * @param d {@link IDecoration} state
     * @return light cell color
     */
    @NotNull
    protected Color getLightColor ( @NotNull final C c, @NotNull final D d )
    {
        return lightColor != null ? lightColor : DEFAULT_LIGHT_COLOR;
    }

    @NotNull
    @Override
    protected TexturePaint createTexturePaint ( @NotNull final Rectangle bounds, @NotNull final C c, @NotNull final D d )
    {
        final BufferedImage image = createTextureImage ( c, d );
        final Rectangle anchor = new Rectangle ( bounds.x, bounds.y, image.getWidth (), image.getHeight () );
        return new TexturePaint ( image, anchor );
    }

    /**
     * Returns texture image.
     *
     * @param c {@link JComponent} that is being painted
     * @param d {@link IDecoration} state
     * @return texture image
     */
    @NotNull
    protected BufferedImage createTextureImage ( @NotNull final C c, @NotNull final D d )
    {
        return createAlphaBackgroundTexture ( getSize ( c, d ), getDarkColor ( c, d ), getLightColor ( c, d ) );
    }

    /**
     * Returns new {@link BufferedImage} containg alpha background texture.
     *
     * @return new {@link BufferedImage} containg alpha background texture
     */
    @NotNull
    public static BufferedImage createAlphaBackgroundTexture ()
    {
        return createAlphaBackgroundTexture ( DEFAULT_CELL_SIZE, DEFAULT_DARK_COLOR, DEFAULT_LIGHT_COLOR );
    }

    /**
     * Returns new {@link BufferedImage} containg alpha background texture of the specified size.
     *
     * @param size {@link Dimension} of a single cell, texture size will be double of this
     * @return new {@link BufferedImage} containg alpha background texture of the specified size
     */
    @NotNull
    public static BufferedImage createAlphaBackgroundTexture ( @NotNull final Dimension size )
    {
        return createAlphaBackgroundTexture ( size, DEFAULT_DARK_COLOR, DEFAULT_LIGHT_COLOR );
    }

    /**
     * Returns new {@link BufferedImage} containg alpha background texture of the specified size and colors.
     *
     * @param size       {@link Dimension} of a single cell, texture size will be double of this
     * @param darkColor  dark cells {@link Color}
     * @param lightColor light cells {@link Color}
     * @return new {@link BufferedImage} containg alpha background texture of the specified size and colors
     */
    @NotNull
    public static BufferedImage createAlphaBackgroundTexture ( @NotNull final Dimension size, @NotNull final Color darkColor,
                                                               @NotNull final Color lightColor )
    {
        final BufferedImage image = ImageUtils.createCompatibleImage ( size.width * 2, size.height * 2, Transparency.OPAQUE );
        final Graphics2D g2d = image.createGraphics ();

        g2d.setPaint ( darkColor );
        g2d.fillRect ( 0, 0, size.width, size.height );
        g2d.fillRect ( size.width, size.height, size.width, size.height );

        g2d.setPaint ( lightColor );
        g2d.fillRect ( size.width, 0, size.width, size.height );
        g2d.fillRect ( 0, size.height, size.width, size.height );

        g2d.dispose ();
        return image;
    }
}