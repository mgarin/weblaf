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

package com.alee.laf.rootpane;

import com.alee.extended.window.WindowResizeAdapter;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @author Mikle Garin
 */

public class WebResizeCorner extends JComponent
{
    /**
     * todo 1. Refactor this to be a custom component with UI and painter
     * todo 2. Provide correct support for various window resize options
     */

    public static final ImageIcon cornerIcon = new ImageIcon ( WebResizeCorner.class.getResource ( "icons/corner.png" ) );

    private static final Dimension preferredSize = new Dimension ( cornerIcon.getIconWidth (), cornerIcon.getIconHeight () );

    public WebResizeCorner ()
    {
        super ();
        SwingUtils.setOrientation ( this );
        setCursor ( Cursor.getPredefinedCursor ( Cursor.SE_RESIZE_CURSOR ) );
        WindowResizeAdapter.install ( this, WindowResizeAdapter.SOUTH_EAST );
    }

    @Override
    protected void paintComponent ( final Graphics g )
    {
        super.paintComponent ( g );
        g.drawImage ( cornerIcon.getImage (), getWidth () - cornerIcon.getIconWidth (), getHeight () - cornerIcon.getIconHeight (), null );
    }

    @Override
    public Dimension getPreferredSize ()
    {
        return preferredSize;
    }
}