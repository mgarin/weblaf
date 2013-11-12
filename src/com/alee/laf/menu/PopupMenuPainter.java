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
import com.alee.utils.SwingUtils;
import com.alee.utils.ninepatch.NinePatchIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Base painter for popupmenu-like components.
 * It is generally used for WebPopupMenuUI default styling but might also be used in other cases.
 * Be aware that you will have to manually setup this painter if used outside of the WebPopupMenuUI.
 *
 * @author Mikle Garin
 */

public class PopupMenuPainter<E extends JComponent> extends AbstractPainter<E> implements SwingConstants
{
    /**
     * Style settings.
     */
    protected PopupMenuStyle popupMenuStyle = PopupMenuPainterStyle.popupMenuStyle;
    protected Color borderColor = PopupMenuPainterStyle.borderColor;
    protected int round = PopupMenuPainterStyle.round;
    protected int shadeWidth = PopupMenuPainterStyle.shadeWidth;
    protected float shadeOpacity = PopupMenuPainterStyle.shadeOpacity;
    protected int cornerWidth = PopupMenuPainterStyle.cornerWidth;

    /**
     * Runtime variables.
     */
    protected boolean transparent = false;
    protected int cornerSide = TOP;
    protected int relativeCorner = 0;
    protected int cornerAlignment = -1;

    /**
     * Returns popup menu style.
     *
     * @return popup menu style
     */
    public PopupMenuStyle getPopupMenuStyle ()
    {
        return popupMenuStyle;
    }

    /**
     * Sets popup menu style.
     *
     * @param style new popup menu style
     */
    public void setPopupMenuStyle ( final PopupMenuStyle style )
    {
        this.popupMenuStyle = style;
    }

    /**
     * Returns popup menu border color.
     *
     * @return popup menu border color
     */
    public Color getBorderColor ()
    {
        return borderColor;
    }

    /**
     * Sets popup menu border color.
     *
     * @param color new popup menu border color
     */
    public void setBorderColor ( final Color color )
    {
        this.borderColor = color;
    }

    /**
     * Returns popup menu border corners rounding.
     *
     * @return popup menu border corners rounding
     */
    public int getRound ()
    {
        return round;
    }

    /**
     * Sets popup menu border corners rounding.
     *
     * @param round new popup menu border corners rounding
     */
    public void setRound ( final int round )
    {
        this.round = round;
    }

    /**
     * Returns popup menu shade width.
     *
     * @return popup menu shade width
     */
    public int getShadeWidth ()
    {
        return shadeWidth;
    }

    /**
     * Sets popup menu shade width.
     *
     * @param width new popup menu shade width
     */
    public void setShadeWidth ( final int width )
    {
        this.shadeWidth = width;
    }

    /**
     * Returns popup menu shade opacity.
     *
     * @return popup menu shade opacity
     */
    public float getShadeOpacity ()
    {
        return shadeOpacity;
    }

    /**
     * Sets popup menu shade opacity.
     *
     * @param opacity new popup menu shade opacity
     */
    public void setShadeOpacity ( final float opacity )
    {
        this.shadeOpacity = opacity;
    }

    /**
     * Returns popup menu dropdown style corner width.
     *
     * @return popup menu dropdown style corner width
     */
    public int getCornerWidth ()
    {
        return cornerWidth;
    }

    /**
     * Sets popup menu dropdown style corner width.
     *
     * @param width popup menu dropdown style corner width
     */
    public void setCornerWidth ( final int width )
    {
        this.cornerWidth = width;
    }

    /**
     * Returns whether popup menu is transparent or not.
     *
     * @return true if popup menu is transparent, false otherwise
     */
    public boolean isTransparent ()
    {
        return transparent;
    }

    /**
     * Sets whether popup menu is transparent or not.
     *
     * @param transparent whether popup menu is transparent or not
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
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E popupMenu )
    {
        final Object aa = LafUtils.setupAntialias ( g2d );

        if ( transparent )
        {
            final Rectangle mb = SwingUtils.getBoundsOnScreen ( popupMenu );

            // Painting shade
            final NinePatchIcon shade = NinePatchUtils.getShadeIcon ( shadeWidth, round * 2, shadeOpacity );
            shade.setComponent ( popupMenu );
            shade.paintIcon ( g2d, getShadeBounds ( mb ) );

            // Filling background
            g2d.setColor ( popupMenu.getBackground () );
            g2d.fill ( getBorderShape ( popupMenu, mb, true ) );

            // Filling corner if needed
            fillDropdownCorner ( g2d, popupMenu, mb );

            // Painting border
            g2d.setPaint ( borderColor );
            g2d.draw ( getBorderShape ( popupMenu, mb, false ) );
        }
        else
        {
            // Background
            g2d.setColor ( popupMenu.getBackground () );
            g2d.fillRoundRect ( 1, 1, popupMenu.getWidth () - 2, popupMenu.getHeight () - 2, round * 2, round * 2 );

            // Border
            g2d.setColor ( borderColor );
            g2d.drawRoundRect ( 0, 0, popupMenu.getWidth () - 1, popupMenu.getHeight () - 1, round * 2, round * 2 );
        }

        LafUtils.restoreAntialias ( g2d, aa );
    }

    /**
     * Fills dropdown-styled popup menu corner if menu item near it is selected.
     *
     * @param g2d       graphics context
     * @param popupMenu popup menu
     * @param mb        menu bounds on screen
     */
    protected void fillDropdownCorner ( final Graphics2D g2d, final E popupMenu, final Rectangle mb )
    {
        // todo Round check is meaninful only within WebPopupMenuUI
        if ( popupMenuStyle == PopupMenuStyle.dropdown && round == 0 )
        {
            // Checking whether corner should be filled or not
            final boolean north = cornerSide == NORTH;
            final int zIndex = north ? 0 : popupMenu.getComponentCount () - 1;
            final Component component = popupMenu.getComponent ( zIndex );
            if ( component instanceof JMenuItem )
            {
                final JMenuItem menuItem = ( JMenuItem ) component;
                final ButtonModel model = menuItem.getModel ();
                if ( model.isArmed () || model.isSelected () )
                {
                    // Filling corner properly
                    if ( menuItem.getUI () instanceof WebMenuUI )
                    {
                        final WebMenuUI ui = ( WebMenuUI ) menuItem.getUI ();
                        g2d.setPaint ( north ? ui.getNorthCornerFill () : ui.getSouthCornerFill () );
                        g2d.fill ( createDropdownCornerShape ( popupMenu, mb, true ) );
                    }
                    else if ( menuItem.getUI () instanceof WebMenuItemUI )
                    {
                        final WebMenuItemUI ui = ( WebMenuItemUI ) menuItem.getUI ();
                        g2d.setPaint ( north ? ui.getNorthCornerFill () : ui.getSouthCornerFill () );
                        g2d.fill ( createDropdownCornerShape ( popupMenu, mb, true ) );
                    }
                }
            }
        }
    }

    /**
     * Returns popup menu shade bounds.
     *
     * @param mb menu bounds on screen
     * @return popup menu shade bounds
     */
    protected Rectangle getShadeBounds ( final Rectangle mb )
    {
        switch ( popupMenuStyle )
        {
            case simple:
            case dropdown:
            {
                return new Rectangle ( 0, 0, mb.width, mb.height );
            }
            default:
            {
                return null;
            }
        }
    }

    /**
     * Returns popup menu border shape.
     *
     * @param mb   menu bounds on screen
     * @param fill whether it is a fill shape or not
     * @return popup menu border shape
     */
    protected Shape getBorderShape ( final E popupMenu, final Rectangle mb, final boolean fill )
    {
        switch ( popupMenuStyle )
        {
            case simple:
            {
                return createSimpleShape ( mb, fill );
            }
            case dropdown:
            {
                return createDropdownShape ( popupMenu, mb, fill );
            }
            default:
            {
                return null;
            }
        }
    }

    /**
     * Creates and returns simple menu shape.
     *
     * @param mb   menu bounds on screen
     * @param fill whether it is a fill shape or not
     * @return simple menu shape
     */
    protected GeneralPath createSimpleShape ( final Rectangle mb, final boolean fill )
    {
        final int shear = fill ? 1 : 0;
        final GeneralPath shape = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        final int top = shadeWidth + shear;
        final int left = shadeWidth + shear;
        final int bottom = mb.height - 1 - shadeWidth;
        final int right = mb.width - 1 - shadeWidth;
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
     * @param mb   menu bounds on screen
     * @param fill whether it is a fill shape or not
     * @return dropdown style shape
     */
    protected GeneralPath createDropdownShape ( final E popupMenu, final Rectangle mb, final boolean fill )
    {
        final boolean north = cornerSide == NORTH || cornerSide == TOP;
        final boolean south = cornerSide == SOUTH || cornerSide == BOTTOM;

        // Painting shear
        final int shear = fill ? 1 : 0;

        // Corner left spacing
        final boolean ltr = popupMenu.getComponentOrientation ().isLeftToRight ();
        final int ds = shadeWidth + shear + round + cornerWidth * 2;
        final int spacing;
        if ( cornerAlignment == CENTER )
        {
            spacing = mb.width / 2 - shadeWidth - round - cornerWidth * 2;
        }
        else if ( cornerAlignment == LEFT || cornerAlignment == LEADING && ltr || cornerAlignment == TRAILING && !ltr )
        {
            spacing = 0;
        }
        else if ( cornerAlignment == RIGHT || cornerAlignment == TRAILING && ltr || cornerAlignment == LEADING && !ltr )
        {
            spacing = mb.width - ds * 2;
        }
        else
        {
            spacing = relativeCorner < shadeWidth + round + cornerWidth ? 0 : Math.min ( relativeCorner - ds, mb.width - ds * 2 );
        }

        // Side spacings
        final int top = shadeWidth + shear;
        final int left = shadeWidth + shear;
        final int botom = mb.height - 1 - shadeWidth;
        final int right = mb.width - 1 - shadeWidth;

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

    /**
     * Creates and returns dropdown style corner shape.
     * It is used to paint corner fill when menu item at the same as corner side of popup menu is selected.
     *
     * @param mb   menu bounds on screen
     * @param fill whether it is a fill shape or not
     * @return dropdown style corner shape
     */
    protected GeneralPath createDropdownCornerShape ( final E popupMenu, final Rectangle mb, final boolean fill )
    {
        final boolean north = cornerSide == NORTH || cornerSide == TOP;
        final boolean south = cornerSide == SOUTH || cornerSide == BOTTOM;

        // Painting shear
        final int shear = fill ? 1 : 0;

        // Corner left spacing
        final boolean ltr = popupMenu.getComponentOrientation ().isLeftToRight ();
        final int ds = shadeWidth + shear + round + cornerWidth * 2;
        final int spacing;
        if ( cornerAlignment == CENTER )
        {
            spacing = mb.width / 2 - shadeWidth - round - cornerWidth * 2;
        }
        else if ( cornerAlignment == LEFT || cornerAlignment == LEADING && ltr || cornerAlignment == TRAILING && !ltr )
        {
            spacing = 0;
        }
        else if ( cornerAlignment == RIGHT || cornerAlignment == TRAILING && ltr || cornerAlignment == LEADING && !ltr )
        {
            spacing = mb.width - ds * 2;
        }
        else
        {
            spacing = relativeCorner < shadeWidth + round + cornerWidth ? 0 : Math.min ( relativeCorner - ds, mb.width - ds * 2 );
        }

        // Side spacings
        final int top = shadeWidth + shear;
        final int left = shadeWidth + shear;
        final int botom = mb.height - 1 - shadeWidth;

        final GeneralPath shape = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        if ( north )
        {
            // Top corner
            shape.moveTo ( left + round + spacing + cornerWidth, top );
            shape.lineTo ( left + round + spacing + cornerWidth * 2, top - cornerWidth );
            shape.lineTo ( left + round + spacing + cornerWidth * 2 + 1, top - cornerWidth );
            shape.lineTo ( left + round + spacing + cornerWidth * 3 + 1, top );
            shape.closePath ();
        }
        if ( south )
        {
            // Bottom corner
            shape.moveTo ( left + round + spacing + cornerWidth * 3 + 1, botom );
            shape.lineTo ( left + round + spacing + cornerWidth * 2 + 1, botom + cornerWidth );
            shape.lineTo ( left + round + spacing + cornerWidth * 2, botom + cornerWidth );
            shape.lineTo ( left + round + spacing + cornerWidth, botom );
            shape.closePath ();
        }
        return shape;
    }
}