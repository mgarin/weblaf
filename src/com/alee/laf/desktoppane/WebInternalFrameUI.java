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
import com.alee.managers.focus.FocusManager;
import com.alee.managers.focus.FocusTracker;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * User: mgarin Date: 17.08.11 Time: 23:15
 */

public class WebInternalFrameUI extends BasicInternalFrameUI implements FocusTracker
{
    private int sideSpacing = 1;
    private boolean focused = false;

    public WebInternalFrameUI ( JInternalFrame b )
    {
        super ( b );
    }

    public static ComponentUI createUI ( JComponent c )
    {
        return new WebInternalFrameUI ( ( JInternalFrame ) c );
    }

    @Override
    public void installUI ( JComponent c )
    {
        super.installUI ( c );

        // Default settings
        SwingUtils.setOrientation ( c );
        c.setOpaque ( false );
        c.setBackground ( new Color ( 90, 90, 90, 220 ) );
        c.setBorder ( BorderFactory.createEmptyBorder () );

        FocusManager.addFocusTracker ( frame, WebInternalFrameUI.this );
    }

    @Override
    public void uninstallUI ( JComponent c )
    {
        FocusManager.removeFocusTracker ( WebInternalFrameUI.this );

        super.uninstallUI ( c );
    }

    @Override
    public boolean isTrackingEnabled ()
    {
        return frame.isShowing ();
    }

    @Override
    public boolean isUniteWithChilds ()
    {
        return true;
    }

    @Override
    public void focusChanged ( boolean focused )
    {
        this.focused = focused;
        frame.repaint ();
    }

    @Override
    protected JComponent createNorthPane ( JInternalFrame w )
    {
        titlePane = new WebInternalFrameTitlePane ( w );
        return titlePane;
    }

    @Override
    protected JComponent createWestPane ( JInternalFrame w )
    {
        return new JComponent ()
        {
            {
                setOpaque ( false );
            }

            @Override
            public Dimension getPreferredSize ()
            {
                return new Dimension ( 4 + sideSpacing, 0 );
            }
        };
    }

    @Override
    protected JComponent createEastPane ( JInternalFrame w )
    {
        return new JComponent ()
        {
            {
                setOpaque ( false );
            }

            @Override
            public Dimension getPreferredSize ()
            {
                return new Dimension ( 4 + sideSpacing, 0 );
            }
        };
    }

    @Override
    protected JComponent createSouthPane ( JInternalFrame w )
    {
        return new JComponent ()
        {
            {
                setOpaque ( false );
            }

            @Override
            public Dimension getPreferredSize ()
            {
                return new Dimension ( 0, 4 + sideSpacing );
            }
        };
    }

    @Override
    public void paint ( Graphics g, JComponent c )
    {
        Graphics2D g2d = ( Graphics2D ) g;
        Object aa = LafUtils.setupAntialias ( g2d );

        // Shape border = LafUtils.getWebBorderShape ( c, StyleConstants.shadeWidth, StyleConstants.round );

        Insets insets = c.getInsets ();
        RoundRectangle2D innerBorder = new RoundRectangle2D.Double ( insets.left + 3 + sideSpacing, insets.top + titlePane.getHeight () - 1,
                c.getWidth () - 1 - insets.left - 3 - sideSpacing - insets.right - 3 - sideSpacing,
                c.getHeight () - 1 - insets.top - titlePane.getHeight () + 1 - insets.bottom - 3 -
                        sideSpacing, ( StyleConstants.bigRound - 1 ) * 2, ( StyleConstants.bigRound - 1 ) * 2 );

        // Border and background
        LafUtils.drawWebStyle ( g2d, c, c.isEnabled () && focused ? StyleConstants.fieldFocusColor : StyleConstants.shadeColor,
                StyleConstants.shadeWidth, StyleConstants.bigRound, true, false );

        // Inner border
        g2d.setPaint ( Color.GRAY );
        g2d.draw ( innerBorder );

        LafUtils.restoreAntialias ( g2d, aa );
    }
}
