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

package com.alee.laf.desktoppane;

import com.alee.laf.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicDesktopPaneUI;

/**
 * User: mgarin Date: 17.08.11 Time: 23:14
 */

public class WebDesktopPaneUI extends BasicDesktopPaneUI
{
    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebDesktopPaneUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Default settings
        SwingUtils.setOrientation ( c );
        LookAndFeel.installProperty ( c, WebLookAndFeel.OPAQUE_PROPERTY, Boolean.TRUE );
        c.setBorder ( LafUtils.createWebBorder ( 0, 0, 0, 0 ) );
        c.setBackground ( StyleConstants.backgroundColor );
    }
}