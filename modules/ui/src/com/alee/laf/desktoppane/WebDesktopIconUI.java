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

import com.alee.laf.WebLookAndFeel;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicDesktopIconUI;
import java.awt.*;

/**
 * User: mgarin Date: 17.08.11 Time: 23:14
 */

public class WebDesktopIconUI extends BasicDesktopIconUI
{
    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebDesktopIconUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Default settings
        SwingUtils.setOrientation ( c );
        c.setBorder ( LafUtils.createWebBorder ( 0, 0, 0, 0 ) );
        LookAndFeel.installProperty ( c, WebLookAndFeel.OPAQUE_PROPERTY, Boolean.FALSE );
    }

    @Override
    protected void installComponents ()
    {
        iconPane = new WebInternalFrameIconPane ( frame );
        desktopIcon.setLayout ( new BorderLayout () );
        desktopIcon.add ( iconPane, BorderLayout.CENTER );
    }
}