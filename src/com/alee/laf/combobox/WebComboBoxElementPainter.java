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

import com.alee.extended.painter.DefaultPainter;
import com.alee.laf.StyleConstants;
import com.alee.utils.LafUtils;

import java.awt.*;

/**
 * User: mgarin Date: 27.07.12 Time: 19:29
 */

public class WebComboBoxElementPainter extends DefaultPainter<WebComboBoxElement>
{
    public WebComboBoxElementPainter ()
    {
        super ();
    }

    @Override
    public Insets getMargin ( WebComboBoxElement element )
    {
        return element.getIndex () != -1 ?
                new Insets ( element.getIndex () == 0 ? 2 : 3, 2, element.getIndex () + 1 == element.getTotalElements () ? 2 : 3, 2 ) :
                new Insets ( 2, 2, 2, 2 );
    }

    @Override
    public void paint ( Graphics2D g2d, Rectangle bounds, WebComboBoxElement element )
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

    protected void paintBoxBackground ( Graphics2D g2d, WebComboBoxElement element )
    {
        //
    }

    protected void paintSelectedBackground ( Graphics2D g2d, WebComboBoxElement element )
    {
        // Background
        g2d.setPaint ( LafUtils.getWebGradientPaint ( 0, 0, 0, element.getHeight () ) );
        g2d.fillRect ( 0, 0, element.getWidth (), element.getHeight () );

        // Border
        g2d.setPaint ( element.isEnabled () ? StyleConstants.darkBorderColor : StyleConstants.disabledBorderColor );
        if ( element.getIndex () != 0 )
        {
            g2d.drawLine ( 0, 0, element.getWidth () - 1, 0 );
        }
        if ( element.getIndex () < element.getTotalElements () - 1 )
        {
            g2d.drawLine ( 0, element.getHeight () - 1, element.getWidth () - 1, element.getHeight () - 1 );
        }
    }

    protected void paintDeselectedBackground ( Graphics2D g2d, WebComboBoxElement element )
    {
        //
    }
}