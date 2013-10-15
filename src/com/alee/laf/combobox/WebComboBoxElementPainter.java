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

package com.alee.laf.combobox;

import com.alee.extended.painter.AbstractPainter;
import com.alee.laf.menu.WebMenuItemStyle;

import java.awt.*;

/**
 * Custom painter for ComboBox elements.
 *
 * @author Mikle Garin
 * @see AbstractPainter
 * @see com.alee.extended.painter.Painter
 */

public class WebComboBoxElementPainter extends AbstractPainter<WebComboBoxElement>
{
    /**
     * Style settings.
     */
    protected static Insets boxMargin = new Insets ( 2, 2, 2, 2 );
    protected static Insets elementMargin = new Insets ( 4, 6, 4, 6 );

    /**
     * Constructs new combobox element painter.
     */
    public WebComboBoxElementPainter ()
    {
        super ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getMargin ( final WebComboBoxElement element )
    {
        return element.getIndex () != -1 ? elementMargin : boxMargin;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final WebComboBoxElement element )
    {
        if ( element.getIndex () == -1 )
        {
            paintBoxBackground ( g2d, element );
        }
        else if ( element.isSelected () )
        {
            paintSelectedBackground ( g2d, element );
        }
        else
        {
            paintDeselectedBackground ( g2d, element );
        }
    }

    /**
     * Paints combobox box element background.
     *
     * @param g2d     graphics context
     * @param element combobox element
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void paintBoxBackground ( final Graphics2D g2d, final WebComboBoxElement element )
    {
        //
    }

    /**
     * Paints selected combobox popup list element background.
     *
     * @param g2d     graphics context
     * @param element combobox element
     */
    protected void paintSelectedBackground ( final Graphics2D g2d, final WebComboBoxElement element )
    {
        final Color topBg = WebMenuItemStyle.selectedTopBg;
        final Color bottomBg = WebMenuItemStyle.selectedBottomBg;
        g2d.setPaint ( new GradientPaint ( 0, 0, topBg, 0, element.getHeight (), bottomBg ) );
        g2d.fillRect ( 0, 0, element.getWidth (), element.getHeight () );
    }

    /**
     * Paints deselected combobox popup list element background.
     *
     * @param g2d     graphics context
     * @param element combobox element
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void paintDeselectedBackground ( final Graphics2D g2d, final WebComboBoxElement element )
    {
        //
    }
}