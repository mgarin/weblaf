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

package com.alee.managers.style.skin.web;

import com.alee.laf.combobox.ComboBoxElementType;
import com.alee.laf.combobox.WebComboBoxElement;
import com.alee.laf.menu.WebMenuItemStyle;

import java.awt.*;

/**
 * Custom painter for default combobox elements renderer.
 * It simply paints gradient background for selected elements in combobox popup list.
 *
 * @author Mikle Garin
 * @see com.alee.managers.style.skin.web.WebLabelPainter
 * @see com.alee.extended.painter.AbstractPainter
 * @see com.alee.extended.painter.Painter
 */

public class WebComboBoxElementPainter<E extends WebComboBoxElement> extends WebLabelPainter<E>
{
    /**
     * Style settings.
     */
    protected Color topSelectedBackgroundColor = WebMenuItemStyle.selectedTopBg;
    protected Color bottomSelectedBackgroundColor = WebMenuItemStyle.selectedBottomBg;

    /**
     * Constructs new combobox element painter.
     */
    public WebComboBoxElementPainter ()
    {
        super ();
    }

    /**
     * Returns top selected background color.
     *
     * @return top selected background color
     */
    public Color getTopSelectedBackgroundColor ()
    {
        return topSelectedBackgroundColor;
    }

    /**
     * Sets top selected background color.
     *
     * @param color new top selected background color
     */
    public void setTopSelectedBackgroundColor ( final Color color )
    {
        this.topSelectedBackgroundColor = color;
    }

    /**
     * Returns bottom selected background color.
     *
     * @return bottom selected background color
     */
    public Color getBottomSelectedBackgroundColor ()
    {
        return bottomSelectedBackgroundColor;
    }

    /**
     * Sets bottom selected background color.
     *
     * @param color new bottom selected background color
     */
    public void setBottomSelectedBackgroundColor ( final Color color )
    {
        this.bottomSelectedBackgroundColor = color;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E element )
    {
        // Painting background
        if ( element.getType () == ComboBoxElementType.box )
        {
            // Painting box background
            paintBoxBackground ( g2d, bounds, element );
        }
        else if ( element.isSelected () )
        {
            // Painting selected list element background
            paintListSelectedBackground ( g2d, bounds, element );
        }
        else
        {
            // Painting deselected list element background
            paintListDeselectedBackground ( g2d, bounds, element );
        }

        // Painting label
        super.paint ( g2d, bounds, element );
    }

    /**
     * Paints combobox box element background.
     *
     * @param g2d     graphics context
     * @param bounds  element bounds
     * @param element combobox element
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void paintBoxBackground ( final Graphics2D g2d, final Rectangle bounds, final E element )
    {
        //
    }

    /**
     * Paints selected combobox popup list element background.
     *
     * @param g2d     graphics context
     * @param b       element bounds
     * @param element combobox element
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void paintListSelectedBackground ( final Graphics2D g2d, final Rectangle b, final E element )
    {
        g2d.setPaint ( new GradientPaint ( 0, b.y, topSelectedBackgroundColor, 0, b.y + b.height, bottomSelectedBackgroundColor ) );
        g2d.fillRect ( 0, 0, element.getWidth (), element.getHeight () );
    }

    /**
     * Paints deselected combobox popup list element background.
     *
     * @param g2d     graphics context
     * @param bounds  element bounds
     * @param element combobox element
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void paintListDeselectedBackground ( final Graphics2D g2d, final Rectangle bounds, final E element )
    {
        // Doesn't paint anything by default
    }
}