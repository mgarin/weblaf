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

import com.alee.managers.style.PainterShapeProvider;
import com.alee.painter.AbstractPainter;
import com.alee.utils.*;
import com.alee.utils.ninepatch.NinePatchIcon;
import com.alee.utils.swing.DataProvider;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Base painter for any type of popup components.
 * It is generally used for WebPopupMenuUI default styling but might also be used in other cases.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */

@SuppressWarnings ("UnusedParameters")
public abstract class AbstractPopupPainter<E extends JComponent, U extends ComponentUI> extends AbstractPainter<E, U>
        implements PainterShapeProvider<E>, SwingConstants
{
    /**
     * Shape cache keys.
     */
    protected static final String SIMPLE_FILL_SHAPE = "simple-fill";
    protected static final String SIMPLE_BORDER_SHAPE = "simple-border";
    protected static final String DROPDOWN_FILL_SHAPE = "dropdown-fill";
    protected static final String DROPDOWN_BORDER_SHAPE = "dropdown-border";

    /**
     * Style settings.
     */
    protected PopupStyle popupStyle = PopupStyle.dropdown;
    protected Color borderColor = new Color ( 128, 128, 128, 128 );
    protected int round = 4;
    protected int shadeWidth = 12;
    protected float shadeOpacity = 0.75f;
    protected int cornerWidth = 6;
    protected float opacity = 0.95f;

    /**
     * Runtime variables.
     */
    protected boolean shaped = true;
    protected int cornerSide = TOP;
    protected int relativeCorner = 0;
    protected int cornerAlignment = -1;
    protected NinePatchIcon shade = null;

    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // todo Rename into "shaped" or something similar since this is not exactly transparency mark anymore
        // Initializing transparency availability mark
        shaped = ProprietaryUtils.isWindowTransparencyAllowed () || ProprietaryUtils.isWindowShapeAllowed ();
    }

    /**
     * Returns popup style.
     *
     * @return popup style
     */
    public PopupStyle getPopupStyle ()
    {
        return popupStyle;
    }

    /**
     * Sets popup style.
     *
     * @param style new popup style
     */
    public void setPopupStyle ( final PopupStyle style )
    {
        if ( this.popupStyle != style )
        {
            this.popupStyle = style;
            if ( shaped )
            {
                updateAll ();
            }
        }
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
        if ( this.borderColor != color )
        {
            this.borderColor = color;
            if ( shaped )
            {
                repaint ();
            }
        }
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
        if ( this.round != round )
        {
            this.round = round;
            if ( shaped )
            {
                repaint ();
            }
        }
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
        if ( this.shadeWidth != width )
        {
            this.shadeWidth = width;
            if ( shaped )
            {
                updateAll ();
            }
        }
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
        if ( this.shadeOpacity != opacity )
        {
            this.shadeOpacity = opacity;
            if ( shaped )
            {
                repaint ();
            }
        }
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
        if ( this.cornerWidth != width )
        {
            this.cornerWidth = width;
            if ( shaped )
            {
                updateAll ();
            }
        }
    }

    /**
     * Returns popup background opacity.
     *
     * @return popup background opacity
     */
    public float getOpacity ()
    {
        return opacity;
    }

    /**
     * Sets popup background opacity.
     *
     * @param opacity popup background opacity
     */
    public void setOpacity ( final float opacity )
    {
        if ( this.opacity != opacity )
        {
            this.opacity = opacity;
            if ( shaped )
            {
                repaint ();
            }
        }
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
        if ( this.cornerSide != cornerSide )
        {
            this.cornerSide = cornerSide;
            if ( shaped )
            {
                repaint ();
            }
        }
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
        if ( this.relativeCorner != relativeCorner )
        {
            this.relativeCorner = relativeCorner;
            if ( shaped )
            {
                repaint ();
            }
        }
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
        if ( this.cornerAlignment != cornerAlignment )
        {
            this.cornerAlignment = cornerAlignment;
            if ( shaped )
            {
                repaint ();
            }
        }
    }

    @Override
    public Shape provideShape ( final E component, final Rectangle bounds )
    {
        return getBorderShape ( component, bounds.getSize (), false );
    }

    @Override
    public Boolean isOpaque ()
    {
        return !shaped;
    }

    @Override
    public Insets getBorders ()
    {
        if ( shaped )
        {
            final int sideWidth = getSideWidth ();
            return i ( sideWidth + 1, sideWidth + 1, sideWidth + 1, sideWidth + 1 );
        }
        else
        {
            return i ( 1, 1, 1, 1 );
        }
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E popup, final U ui )
    {
        final Object aa = GraphicsUtils.setupAntialias ( g2d );
        if ( shaped )
        {
            paintTransparentPopup ( g2d, popup );
        }
        else
        {
            paintSimplePopup ( g2d, popup );
        }
        GraphicsUtils.restoreAntialias ( g2d, aa );
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
        g2d.setPaint ( getBackgroundColor ( popup ) );
        g2d.fillRoundRect ( 1, 1, popup.getWidth () - 2, popup.getHeight () - 2, round * 2, round * 2 );

        // Border
        g2d.setPaint ( borderColor );
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
        if ( shadeWidth > 0 )
        {
            shade = NinePatchUtils.getShadeIcon ( shadeWidth, round, getCurrentShadeOpacity () );
            shade.setComponent ( popup );
            shade.paintIcon ( g2d, getShadeBounds ( popupSize ) );
        }
        else
        {
            shade = null;
        }
    }

    /**
     * Returns current shade opacity.
     * This part moved to a separate method to make it simple to override without touching the actual opacity value.
     *
     * @return current shade opacity
     */
    protected float getCurrentShadeOpacity ()
    {
        return shadeOpacity;
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
        final Color backgroundColor = getBackgroundColor ( popup );
        if ( backgroundColor != null )
        {
            g2d.setPaint ( backgroundColor );
            g2d.fill ( getBorderShape ( popup, popupSize, true ) );
        }
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
        if ( borderColor != null )
        {
            g2d.setPaint ( borderColor );
            g2d.draw ( getBorderShape ( popup, popupSize, false ) );
        }
    }

    /**
     * Returns popup background color.
     *
     * @param popup popup component
     * @return popup background color
     */
    protected Color getBackgroundColor ( final E popup )
    {
        final Color bg = getComponentBackground ( popup );
        return !shaped || opacity >= 1f ? bg :
                ColorUtils.getTransparentColor ( bg, Math.max ( 0, Math.min ( ( int ) ( opacity * 255 ), 255 ) ) );
    }

    /**
     * Returns popup background color.
     * This call moved into a separate method so you can override it to provide different background color.
     *
     * @param popup popup component
     * @return popup background color
     */
    protected Color getComponentBackground ( final E popup )
    {
        return popup.getBackground ();
    }

    /**
     * Returns popup shade bounds.
     *
     * @param popupSize popup size
     * @return popup shade bounds
     */
    protected Rectangle getShadeBounds ( final Dimension popupSize )
    {
        switch ( popupStyle )
        {
            case simple:
            {
                return new Rectangle ( 0, 0, popupSize.width, popupSize.height );
            }
            case dropdown:
            {
                final int spacing = Math.max ( 0, cornerWidth - shadeWidth );
                return new Rectangle ( spacing, spacing, popupSize.width - spacing * 2, popupSize.height - spacing * 2 );
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
        switch ( popupStyle )
        {
            case simple:
            {
                return ShapeUtils.getShape ( popup, fill ? SIMPLE_FILL_SHAPE : SIMPLE_BORDER_SHAPE, new DataProvider<Shape> ()
                {
                    @Override
                    public Shape provide ()
                    {
                        return createSimpleShape ( popup, popupSize, fill );
                    }
                }, getCachedShapeSettings ( popup, popupSize ) );
            }
            case dropdown:
            {
                return ShapeUtils.getShape ( popup, fill ? DROPDOWN_FILL_SHAPE : DROPDOWN_BORDER_SHAPE, new DataProvider<Shape> ()
                {
                    @Override
                    public Shape provide ()
                    {
                        return createDropdownShape ( popup, popupSize, fill );
                    }
                }, getCachedShapeSettings ( popup, popupSize ) );
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
     * @param popup     popup component
     * @param popupSize popup size
     * @return an array of shape settings cached along with the shape
     */
    protected Object[] getCachedShapeSettings ( final E popup, final Dimension popupSize )
    {
        return new Object[]{ round, shadeWidth, cornerWidth, cornerSide, relativeCorner, cornerAlignment, popupSize, ltr };
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
    @SuppressWarnings ("ConstantConditions")
    protected GeneralPath createDropdownShape ( final E popup, final Dimension popupSize, final boolean fill )
    {
        final boolean topCorner = cornerSide == TOP;
        final boolean bottomCorner = cornerSide == BOTTOM;
        final boolean leftCorner = cornerSide == LEFT || cornerSide == LEADING;
        final boolean rightCorner = cornerSide == RIGHT || cornerSide == TRAILING;

        // Painting shear
        final int shear = fill ? 1 : 0;

        // Side width
        final int sideWidth = getSideWidth ();

        // Corner left spacing
        final int cornerShear = sideWidth + shear + round + cornerWidth * 2;
        final int length = topCorner || bottomCorner ? popupSize.width : popupSize.height;
        final int spacing;
        if ( cornerAlignment == CENTER || length < sideWidth * 2 + round * 2 + cornerWidth * 4 )
        {
            spacing = length / 2 - sideWidth - round - cornerWidth * 2;
        }
        else if ( cornerAlignment == LEFT || cornerAlignment == LEADING && ltr || cornerAlignment == TRAILING && !ltr )
        {
            spacing = 0;
        }
        else if ( cornerAlignment == RIGHT || cornerAlignment == TRAILING && ltr || cornerAlignment == LEADING && !ltr )
        {
            spacing = length - cornerShear * 2;
        }
        else
        {
            spacing = relativeCorner < sideWidth + round + cornerWidth * 2 ? 0 :
                    Math.min ( relativeCorner - cornerShear, length - cornerShear * 2 );
        }

        // Side spacings
        final int top = sideWidth + shear;
        final int right = popupSize.width - 1 - sideWidth;
        final int bottom = popupSize.height - 1 - sideWidth;
        final int left = sideWidth + shear;

        final GeneralPath shape = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        shape.moveTo ( left, top + round );
        shape.quadTo ( left, top, left + round, top );
        if ( topCorner )
        {
            // Top corner
            shape.lineTo ( left + round + spacing + cornerWidth, top );
            shape.lineTo ( left + round + spacing + cornerWidth * 2, top - cornerWidth );
            shape.lineTo ( left + round + spacing + cornerWidth * 2 + 1, top - cornerWidth );
            shape.lineTo ( left + round + spacing + cornerWidth * 3 + 1, top );
        }
        shape.lineTo ( right - round, top );
        shape.quadTo ( right, top, right, top + round );
        if ( rightCorner )
        {
            // Right corner
            shape.lineTo ( right, top + round + spacing + cornerWidth );
            shape.lineTo ( right + cornerWidth, top + round + spacing + cornerWidth * 2 );
            shape.lineTo ( right + cornerWidth, top + round + spacing + cornerWidth * 2 + 1 );
            shape.lineTo ( right, top + round + spacing + cornerWidth * 3 + 1 );
        }
        shape.lineTo ( right, bottom - round );
        shape.quadTo ( right, bottom, right - round, bottom );
        if ( bottomCorner )
        {
            // Bottom corner
            shape.lineTo ( left + round + spacing + cornerWidth * 3 + 1, bottom );
            shape.lineTo ( left + round + spacing + cornerWidth * 2 + 1, bottom + cornerWidth );
            shape.lineTo ( left + round + spacing + cornerWidth * 2, bottom + cornerWidth );
            shape.lineTo ( left + round + spacing + cornerWidth, bottom );
        }
        shape.lineTo ( left + round, bottom );
        shape.quadTo ( left, bottom, left, bottom - round );
        if ( leftCorner )
        {
            // Left corner
            shape.lineTo ( left, top + round + spacing + cornerWidth * 3 + 1 );
            shape.lineTo ( left - cornerWidth, top + round + spacing + cornerWidth * 2 + 1 );
            shape.lineTo ( left - cornerWidth, top + round + spacing + cornerWidth * 2 );
            shape.lineTo ( left, top + round + spacing + cornerWidth );
        }
        shape.closePath ();

        return shape;
    }

    /**
     * Returns dropdown style corner shape.
     * It is used to paint corner fill when menu item at the same as corner side of popup menu is selected.
     *
     * @param popupMenu popup menu
     * @param menuSize  menu size
     * @param fill      whether it is a fill shape or not
     * @return dropdown style corner shape
     */
    protected Shape getDropdownCornerShape ( final E popupMenu, final Dimension menuSize, final boolean fill )
    {
        return ShapeUtils.getShape ( popupMenu, fill ? "dropdown-corner-fill" : "dropdown-corner-border", new DataProvider<Shape> ()
        {
            @Override
            public Shape provide ()
            {
                return createDropdownCornerShape ( popupMenu, menuSize, fill );
            }
        }, getCachedShapeSettings ( popupMenu, menuSize ) );
    }

    /**
     * Creates and returns dropdown style corner shape.
     * It is used to paint corner fill when menu item at the same as corner side of popup menu is selected.
     *
     * @param popupMenu popup menu
     * @param menuSize  menu size
     * @param fill      whether it is a fill shape or not
     * @return dropdown style corner shape
     */
    @SuppressWarnings ("ConstantConditions")
    protected GeneralPath createDropdownCornerShape ( final E popupMenu, final Dimension menuSize, final boolean fill )
    {
        final boolean topCorner = cornerSide == TOP;
        final boolean bottomCorner = cornerSide == BOTTOM;
        final boolean leftCorner = cornerSide == LEFT || cornerSide == LEADING;
        final boolean rightCorner = cornerSide == RIGHT || cornerSide == TRAILING;

        // Painting shear
        final int shear = fill ? 1 : 0;

        // Side width
        final int sideWidth = getSideWidth ();

        // Corner left spacing
        final int cornerShear = sideWidth + shear + round + cornerWidth * 2;
        final int length = topCorner || bottomCorner ? menuSize.width : menuSize.height;
        final int spacing;
        if ( cornerAlignment == CENTER || length < sideWidth * 2 + round * 2 + cornerWidth * 4 )
        {
            spacing = length / 2 - sideWidth - round - cornerWidth * 2;
        }
        else if ( cornerAlignment == LEFT || cornerAlignment == LEADING && ltr || cornerAlignment == TRAILING && !ltr )
        {
            spacing = 0;
        }
        else if ( cornerAlignment == RIGHT || cornerAlignment == TRAILING && ltr || cornerAlignment == LEADING && !ltr )
        {
            spacing = length - cornerShear * 2;
        }
        else
        {
            spacing = relativeCorner < sideWidth + round + cornerWidth * 2 ? 0 :
                    Math.min ( relativeCorner - cornerShear, length - cornerShear * 2 );
        }

        // Side spacings
        final int top = sideWidth + shear;
        final int right = menuSize.width - 1 - sideWidth;
        final int bottom = menuSize.height - 1 - sideWidth;
        final int left = sideWidth + shear;

        final GeneralPath shape = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        if ( topCorner )
        {
            // Top corner
            shape.moveTo ( left + round + spacing + cornerWidth, top );
            shape.lineTo ( left + round + spacing + cornerWidth * 2, top - cornerWidth );
            shape.lineTo ( left + round + spacing + cornerWidth * 2 + 1, top - cornerWidth );
            shape.lineTo ( left + round + spacing + cornerWidth * 3 + 1, top );
            shape.closePath ();
        }
        if ( rightCorner )
        {
            // Right corner
            shape.lineTo ( right, top + round + spacing + cornerWidth );
            shape.lineTo ( right + cornerWidth, top + round + spacing + cornerWidth * 2 );
            shape.lineTo ( right + cornerWidth, top + round + spacing + cornerWidth * 2 + 1 );
            shape.lineTo ( right, top + round + spacing + cornerWidth * 3 + 1 );
        }
        if ( bottomCorner )
        {
            // Bottom corner
            shape.moveTo ( left + round + spacing + cornerWidth * 3 + 1, bottom );
            shape.lineTo ( left + round + spacing + cornerWidth * 2 + 1, bottom + cornerWidth );
            shape.lineTo ( left + round + spacing + cornerWidth * 2, bottom + cornerWidth );
            shape.lineTo ( left + round + spacing + cornerWidth, bottom );
            shape.closePath ();
        }
        if ( leftCorner )
        {
            // Left corner
            shape.lineTo ( left, top + round + spacing + cornerWidth * 3 + 1 );
            shape.lineTo ( left - cornerWidth, top + round + spacing + cornerWidth * 2 + 1 );
            shape.lineTo ( left - cornerWidth, top + round + spacing + cornerWidth * 2 );
            shape.lineTo ( left, top + round + spacing + cornerWidth );
        }
        return shape;
    }

    /**
     * Returns popup side space width.
     * This is the space taken by popup shade and corner.
     *
     * @return popup side space width
     */
    protected int getSideWidth ()
    {
        return popupStyle == PopupStyle.dropdown ? Math.max ( cornerWidth, shadeWidth ) : shadeWidth;
    }
}