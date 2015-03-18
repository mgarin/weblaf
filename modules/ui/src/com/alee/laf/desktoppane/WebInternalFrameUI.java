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

import com.alee.global.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.focus.DefaultFocusTracker;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.focus.FocusTracker;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Custom UI for JInternalFrame component.
 *
 * @author Mikle Garin
 */

public class WebInternalFrameUI extends BasicInternalFrameUI
{
    /**
     * Frame backgroundColor.
     */
    protected Color backgroundColor = WebInternalFrameStyle.backgroundColor;

    /**
     * Style settings.
     */
    protected int sideSpacing = 1;

    /**
     * Panel focus tracker.
     */
    protected FocusTracker focusTracker;

    /**
     * Whether internal frame is focused or owns focused component or not.
     */
    protected boolean focused = false;

    /**
     * Returns an instance of the WebInternalFrameUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebInternalFrameUI
     */
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebInternalFrameUI ( ( JInternalFrame ) c );
    }

    /**
     * Constructs new internal frame UI.
     *
     * @param b internal frame to which this UI will be applied
     */
    public WebInternalFrameUI ( final JInternalFrame b )
    {
        super ( b );
    }

    /**
     * Installs UI in the specified component.
     *
     * @param c component for this UI
     */
    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Default settings
        SwingUtils.setOrientation ( frame );
        LookAndFeel.installProperty ( frame, WebLookAndFeel.OPAQUE_PROPERTY, Boolean.FALSE );
        frame.setBackground ( backgroundColor );
        frame.setBorder ( LafUtils.createWebBorder ( 0, 0, 0, 0 ) );

        // Focus tracker for the panel content
        focusTracker = new DefaultFocusTracker ()
        {
            @Override
            public void focusChanged ( final boolean focused )
            {
                WebInternalFrameUI.this.focused = focused;
                frame.repaint ();
            }
        };
        FocusManager.addFocusTracker ( frame, focusTracker );
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Removing focus tracker
        FocusManager.removeFocusTracker ( focusTracker );

        super.uninstallUI ( c );
    }

    /**
     * Creates and returns internal pane north panel.
     *
     * @param w internal pane to process
     * @return north panel for specified internal frame
     */
    @Override
    protected JComponent createNorthPane ( final JInternalFrame w )
    {
        titlePane = new WebInternalFrameTitlePane ( w );
        return titlePane;
    }

    /**
     * Creates and returns internal pane west panel.
     *
     * @param w internal pane to process
     * @return west panel for specified internal frame
     */
    @Override
    protected JComponent createWestPane ( final JInternalFrame w )
    {
        // todo Proper internal frame resize
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

    /**
     * Creates and returns internal pane east panel.
     *
     * @param w internal pane to process
     * @return east panel for specified internal frame
     */
    @Override
    protected JComponent createEastPane ( final JInternalFrame w )
    {
        // todo Proper internal frame resize
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

    /**
     * Creates and returns internal pane south panel.
     *
     * @param w internal pane to process
     * @return south panel for specified internal frame
     */
    @Override
    protected JComponent createSouthPane ( final JInternalFrame w )
    {
        // todo Proper internal frame resize
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

    /**
     * Paints internal frame.
     *
     * @param g graphics
     * @param c component
     */
    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        final Graphics2D g2d = ( Graphics2D ) g;
        final Object aa = GraphicsUtils.setupAntialias ( g2d );

        // Border and background
        LafUtils.drawWebStyle ( g2d, c, c.isEnabled () && focused ? StyleConstants.fieldFocusColor : StyleConstants.shadeColor,
                StyleConstants.shadeWidth, StyleConstants.bigRound, true, false );

        // Inner border
        final Insets insets = c.getInsets ();
        final int x = insets.left + 3 + sideSpacing;
        final int y = insets.top + titlePane.getHeight () - 1;
        final int w = c.getWidth () - 1 - insets.left - 3 - sideSpacing - insets.right - 3 - sideSpacing;
        final int h = c.getHeight () - 1 - insets.top - titlePane.getHeight () + 1 - insets.bottom - 3 - sideSpacing;
        final int round = ( StyleConstants.bigRound - 1 ) * 2;
        g2d.setPaint ( Color.GRAY );
        g2d.draw ( new RoundRectangle2D.Double ( x, y, w, h, round, round ) );

        GraphicsUtils.restoreAntialias ( g2d, aa );
    }
}