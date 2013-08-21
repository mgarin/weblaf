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

import com.alee.laf.StyleConstants;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import java.awt.*;

/**
 * User: mgarin Date: 15.07.11 Time: 18:56
 */

public class WebPopupMenuUI extends BasicPopupMenuUI implements ShapeProvider
{
    public static ComponentUI createUI ( JComponent c )
    {
        return new WebPopupMenuUI ();
    }

    @Override
    public void installUI ( JComponent c )
    {
        super.installUI ( c );

        // Default settings
        SwingUtils.setOrientation ( popupMenu );
        popupMenu.setOpaque ( false );
        popupMenu.setBackground ( Color.WHITE );

        // Popup-type dependant border
        popupMenu.setBorder ( popupMenu instanceof BasicComboPopup ? BorderFactory.createEmptyBorder ( 1, 1, 1, 1 ) :
                BorderFactory.createEmptyBorder ( 0, 0, 0, 0 ) );
    }

    @Override
    public Shape provideShape ()
    {
        return LafUtils.getWebBorderShape ( popupMenu, 0, StyleConstants.smallRound );
    }

    @Override
    public void paint ( Graphics g, JComponent c )
    {
        super.paint ( g, c );

        Graphics2D g2d = ( Graphics2D ) g;

        // Border and background
        LafUtils.drawWebStyle ( g2d, c, StyleConstants.shadeColor, 0, StyleConstants.smallRound, true, false,
                StyleConstants.averageBorderColor );
    }
}