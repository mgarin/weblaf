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

package com.alee.laf.menu;

import com.alee.extended.painter.AbstractPainter;
import com.alee.utils.LafUtils;
import com.alee.utils.NinePatchUtils;
import com.alee.utils.ShapeCache;
import com.alee.utils.laf.PainterShapeProvider;
import com.alee.utils.ninepatch.NinePatchIcon;
import com.alee.utils.swing.DataProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Web-styled popup painter for any type of components.
 * It is generally used for WebPopupMenuUI default styling but might also be used in other cases.
 * Be aware that you will have to manually setup this painter if used outside of the WebPopupMenuUI.
 *
 * @author Mikle Garin
 */

@SuppressWarnings ( "UnusedParameters" )
public class WebPopupPainter<E extends JComponent> extends AbstractPainter<E> implements PainterShapeProvider<E>, SwingConstants
{
    /**
     * Style settings.
     */
    protected PopupPainterStyle popupPainterStyle = WebPopupPainterStyle.popupPainterStyle;
    protected Color borderColor = WebPopupPainterStyle.borderColor;
    protected int round = WebPopupPainterStyle.round;
    protected int shadeWidth = WebPopupPainterStyle.shadeWidth;
    protected float shadeOpacity = WebPopupPainterStyle.shadeOpacity;
    protected int cornerWidth = WebPopupPainterStyle.cornerWidth;

    /**
     * Runtime variables.
     */
    protected boolean transparent = true;
    protected int cornerSide = TOP;
    protected int relativeCorner = 0;
    protected int cornerAlignment = -1;

    /**
     * Returns popup style.
     *
     * @return popup style
     */
    public PopupPainterStyle getPopupPainterStyle ()
    {
        return popupPainterStyle;
    }

    /**
     * Sets popup  style.
     *
     * @param style new popup style
     */
    public void setPopupPainterStyle ( final PopupPainterStyle style )
    {
        this.popupPainterStyle = style;
    }

    /**
     * Returns popup border color.
     *
     * @return popup border color
     */
    public Color getBorderColor ()
    {
        return borderColor;
    }

    /**
     * Sets popup border color.
     *
     * @param color new popup border color
     */
    public void setBorderColor ( final Color color )
    {
        this.borderColor = color;
    }

    /**
     * Returns popup border corners rounding.
     *
     * @return popup border corners rounding
     */
    public int getRound ()
    {
        return round;
    }

    /**
     * Sets popup border corners rounding.
     *
     * @param round new popup border corners rounding
     */
    public void setRound ( final int round )
    {
        this.round = round;
    }

    /**
     * Returns popup shade width.
     *
     * @return popup shade width
     */
    public int getShadeWidth ()
    {
        return shadeWidth;
    }

    /**
     * Sets popup shade width.
     *
     * @param width new popup shade width
     */
    public void setShadeWidth ( final int width )
    {
        this.shadeWidth = width;
    }

    /**
     * Returns popup shade opacity.
     *
     * @return popup shade opacity
     */
    public float getShadeOpacity ()
    {
        return shadeOpacity;
    }

    /**
     * Sets popup shade opacity.
     *
     * @param opacity new popup shade opacity
     */
    public void setShadeOpacity ( final float opacity )
    {
        this.shadeOpacity = opacity;
    }

    /**
     * Returns popup dropdown style corner width.
     *
     * @return popup dropdown style corner width
     */
    public int getCornerWidth ()
    {
        return cornerWidth;
    }

    /**
     * Sets popup dropdown style corner width.
     *
     * @param width popup dropdown style corner width
     */
    public void setCornerWidth ( final int width )
    {
        this.cornerWidth = width;
    }

    /**
     * Returns whether popup is transparent or not.
     *
     * @return true if popup is transparent, false otherwise
     */
    public boolean isTransparent ()
    {
        return transparent;
    }

    /**
     * Sets whether popup is transparent or not.
     *
     * @param transparent whether popup is transparent or not
     */
    public void setTransparent ( final boolean transparent )
    {
        this.transparent = transparent;
    }

    /**
     * Returns dropdown style corner side.
     *
     * @return dropdown style corner side
     */
    public int getCornerSide ()
    {
        return cornerSide;
    }

    /**
     * Sets dropdown style corner side.
     *
     * @param cornerSide dropdown style corner side
     */
    public void setCornerSide ( final int cornerSide )
    {
        this.cornerSide = cornerSide;
    }

    /**
     * Returns relative dropdown corner position.
     *
     * @return relative dropdown corner position
     */
    public int getRelativeCorner ()
    {
        return relativeCorner;
    }

    /**
     * Sets relative dropdown corner position.
     *
     * @param relativeCorner relative dropdown corner position
     */
    public void setRelativeCorner ( final int relativeCorner )
    {
        this.relativeCorner = relativeCorner;
    }

    /**
     * Returns dropdown corner alignment.
     *
     * @return dropdown corner alignment
     */
    public int getCornerAlignment ()
    {
        return cornerAlignment;
    }

    /**
     * Sets dropdown corner alignment.
     *
     * @param cornerAlignment dropdown corner alignment
     */
    public void setCornerAlignment ( final int cornerAlignment )
    {
        this.cornerAlignment = cornerAlignment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape provideShape ( final E component, final Rectangle bounds )
    {
        return getBorderShape ( component, component.getSize (), false );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOpaque ( final E c )
    {
        return !transparent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getMargin ( final E c )
    {
        // Actual margin
        final boolean ltr = c.getComponentOrientation ().isLeftToRight ();
        final Insets m = new Insets ( margin.top, ltr ? margin.left : margin.right, margin.bottom, ltr ? margin.right : margin.left );

        // Calculating additional borders
        if ( transparent )
        {
            m.top += shadeWidth + 1;
            m.left += shadeWidth + 1;
            m.bottom += shadeWidth + 1;
            m.right += shadeWidth + 1;
        }
        else
        {
            m.top += 1;
            m.left += 1;
            m.bottom += 1;
            m.right += 1;
        }

        return m;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E popup )
    {
        final Object aa = LafUtils.setupAntialias ( g2d );
        if ( transparent )
        {
            paintTransparentPopup ( g2d, popup );
        }
        else
        {
            paintSimplePopup ( g2d, popup );
        }
        LafUtils.restoreAntialias ( g2d, aa );
    }

    /**
     * Paints transparent popup version.
     * This one is used when popup component can be transparent, otherwise a simple popup version is painted.
     *
     * @param g2d   graphics context
     * @param popup popup component
     */
    protected void paintTransparentPopup ( final Graphics2D g2d, final E popup )
    {
        final Dimension popupSize = popup.getSize ();

        // Painting shade
        paintShade ( g2d, popup, popupSize );

        // Painting background
        paintBackground ( g2d, popup, popupSize );

        // Painting border
        paintBorder ( g2d, popup, popupSize );
    }

    /**
     * Paints simple popup version.
     * This one is used when popup component is opaque.
     *
     * @param g2d   graphics context
     * @param popup popup component
     */
    protected void paintSimplePopup ( final Graphics2D g2d, final E popup )
    {
        // Background
        g2d.setColor ( popup.getBackground () );
        g2d.fillRoundRect ( 1, 1, popup.getWidth () - 2, popup.getHeight () - 2, round * 2, round * 2 );

        // Border
        g2d.setColor ( borderColor );
        g2d.drawRoundRect ( 0, 0, popup.getWidth () - 1, popup.getHeight () - 1, round * 2, round * 2 );
    }

    /**
     * Paints popup shade.
     *
     * @param g2d       graphics context
     * @param popup     popup component
     * @param popupSize popup size
     */
    protected void paintShade ( final Graphics2D g2d, final E popup, final Dimension popupSize )
    {
        final NinePatchIcon shade = NinePatchUtils.getShadeIcon ( shadeWidth, round * 2, shadeOpacity );
        shade.setComponent ( popup );
        shade.paintIcon ( g2d, getShadeBounds ( popupSize ) );
    }

    /**
     * Paints popup background fill.
     *
     * @param g2d       graphics context
     * @param popup     popup component
     * @param popupSize popup size
     */
    protected void paintBackground ( final Graphics2D g2d, final E popup, final Dimension popupSize )
    {
        g2d.setColor ( popup.getBackground () );
        g2d.fill ( getBorderShape ( popup, popupSize, true ) );
    }

    /**
     * Paints popup border.
     *
     * @param g2d       graphics context
     * @param popup     popup component
     * @param popupSize popup size
     */
    protected void paintBorder ( final Graphics2D g2d, final E popup, final Dimension popupSize )
    {
        g2d.setPaint ( borderColor );
        g2d.draw ( getBorderShape ( popup, popupSize, false ) );
    }

    /**
     * Returns popup shade bounds.
     *
     * @param popupSize popup size
     * @return popup shade bounds
     */
    protected Rectangle getShadeBounds ( final Dimension popupSize )
    {
        switch ( popupPainterStyle )
        {
            case simple:
            case dropdown:
            {
                return new Rectangle ( 0, 0, popupSize.width, popupSize.height );
            }
            default:
            {
                return null;
            }
        }
    }

    /**
     * Returns popup border shape.
     *
     * @param popup     popup component
     * @param popupSize popup size
     * @param fill      whether it is a fill shape or not
     * @return popup border shape
     */
    protected Shape getBorderShape ( final E popup, final Dimension popupSize, final boolean fill )
    {
        switch ( popupPainterStyle )
        {
            case simple:
            {
                return ShapeCache.getShape ( popup, fill ? "simple-fill" : "simple-border", new DataProvider<Shape> ()
                {
                    @Override
                    public Shape provide ()
                    {
                        return createSimpleShape ( popup, popupSize, fill );
                    }
                }, getCachedShapeSettings ( popup ) );
            }
            case dropdown:
            {
                return ShapeCache.getShape ( popup, fill ? "dropdown-fill" : "dropdown-border", new DataProvider<Shape> ()
                {
                    @Override
                    public Shape provide ()
                    {
                        return createDropdownShape ( popup, popupSize, fill );
                    }
                }, getCachedShapeSettings ( popup ) );
            }
            default:
            {
                return null;
            }
        }
    }

    /**
     * Returns an array of shape settings cached along with the shape.
     *
     * @param popup popup component
     * @return an array of shape settings cached along with the shape
     */
    protected Object[] getCachedShapeSettings ( final E popup )
    {
        return new Object[]{ round, shadeWidth, cornerWidth, cornerSide, relativeCorner, cornerAlignment, popup.getSize () };
    }

    /**
     * Creates and returns simple popup shape.
     *
     * @param popup     popup component
     * @param popupSize popup size
     * @param fill      whether it is a fill shape or not
     * @return simple popup shape
     */
    protected GeneralPath createSimpleShape ( final E popup, final Dimension popupSize, final boolean fill )
    {
        final int shear = fill ? 1 : 0;
        final GeneralPath shape = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        final int top = shadeWidth + shear;
        final int left = shadeWidth + shear;
        final int bottom = popupSize.height - 1 - shadeWidth;
        final int right = popupSize.width - 1 - shadeWidth;
        shape.moveTo ( left, top + round );
        shape.quadTo ( left, top, left + round, top );
        shape.lineTo ( right - round, top );
        shape.quadTo ( right, top, right, top + round );
        shape.lineTo ( right, bottom - round );
        shape.quadTo ( right, bottom, right - round, bottom );
        shape.lineTo ( left + round, bottom );
        shape.quadTo ( left, bottom, left, bottom - round );
        shape.closePath ();
        return shape;
    }

    /**
     * Creates and returns dropdown style shape.
     *
     * @param popup     popup component
     * @param popupSize popup size
     * @param fill      whether it is a fill shape or not
     * @return dropdown style shape
     */
    protected GeneralPath createDropdownShape ( final E popup, final Dimension popupSize, final boolean fill )
    {
        final boolean north = cornerSide == NORTH || cornerSide == TOP;
        final boolean south = cornerSide == SOUTH || cornerSide == BOTTOM;

        // Painting shear
        final int shear = fill ? 1 : 0;

        // Corner left spacing
        final boolean ltr = popup.getComponentOrientation ().isLeftToRight ();
        final int ds = shadeWidth + shear + round + cornerWidth * 2;
        final int spacing;
        if ( cornerAlignment == CENTER )
        {
            spacing = popupSize.width / 2 - shadeWidth - round - cornerWidth * 2;
        }
        else if ( cornerAlignment == LEFT || cornerAlignment == LEADING && ltr || cornerAlignment == TRAILING && !ltr )
        {
            spacing = 0;
        }
        else if ( cornerAlignment == RIGHT || cornerAlignment == TRAILING && ltr || cornerAlignment == LEADING && !ltr )
        {
            spacing = popupSize.width - ds * 2;
        }
        else
        {
            spacing = relativeCorner < shadeWidth + round + cornerWidth ? 0 : Math.min ( relativeCorner - ds, popupSize.width - ds * 2 );
        }

        // Side spacings
        final int top = shadeWidth + shear;
        final int left = shadeWidth + shear;
        final int botom = popupSize.height - 1 - shadeWidth;
        final int right = popupSize.width - 1 - shadeWidth;

        final GeneralPath shape = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        shape.moveTo ( left, top + round );
        shape.quadTo ( left, top, left + round, top );
        if ( north )
        {
            // Top corner
            shape.lineTo ( left + round + spacing + cornerWidth, top );
            shape.lineTo ( left + round + spacing + cornerWidth * 2, top - cornerWidth );
            shape.lineTo ( left + round + spacing + cornerWidth * 2 + 1, top - cornerWidth );
            shape.lineTo ( left + round + spacing + cornerWidth * 3 + 1, top );
        }
        shape.lineTo ( right - round, top );
        shape.quadTo ( right, top, right, top + round );
        shape.lineTo ( right, botom - round );
        shape.quadTo ( right, botom, right - round, botom );
        if ( south )
        {
            // Bottom corner
            shape.lineTo ( left + round + spacing + cornerWidth * 3 + 1, botom );
            shape.lineTo ( left + round + spacing + cornerWidth * 2 + 1, botom + cornerWidth );
            shape.lineTo ( left + round + spacing + cornerWidth * 2, botom + cornerWidth );
            shape.lineTo ( left + round + spacing + cornerWidth, botom );
        }
        shape.lineTo ( left + round, botom );
        shape.quadTo ( left, botom, shadeWidth, botom - round );
        shape.closePath ();
        return shape;
    }
}