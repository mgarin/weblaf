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

import com.alee.extended.layout.AbstractLayoutManager;
import com.alee.laf.grouping.GroupPane;
import com.alee.utils.MathUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Special layout for decorated root pane.
 *
 * @author Mikle Garin
 */

public class WebRootPaneLayout extends AbstractLayoutManager
{
    @Override
    public Dimension preferredLayoutSize ( final Container parent )
    {
        return calculateSize ( parent, true );
    }

    @Override
    public Dimension minimumLayoutSize ( final Container parent )
    {
        return calculateSize ( parent, false );
    }

    @Override
    public void layoutContainer ( final Container parent )
    {
        final JRootPane root = ( JRootPane ) parent;
        final WebRootPaneUI rootUI = ( WebRootPaneUI ) root.getUI ();
        final Insets i = parent.getInsets ();
        final Dimension s = parent.getSize ();
        final int w = s.width - i.right - i.left;
        final int h = s.height - i.top - i.bottom;
        final boolean ltr = root.getComponentOrientation ().isLeftToRight ();

        final GroupPane windowButtons = rootUI.getButtonsPanel ();
        final JComponent titleComponent = rootUI.getTitleComponent ();
        final JMenuBar menuBar = root.getJMenuBar ();
        final boolean showWindowButtons = windowButtons != null && rootUI.isDisplayWindowButtons () &&
                ( rootUI.isDisplayMinimizeButton () || rootUI.isDisplayMaximizeButton () || rootUI.isDisplayCloseButton () );
        final boolean showTitleComponent = titleComponent != null && rootUI.isDisplayTitleComponent ();
        final boolean showMenuBar = menuBar != null && rootUI.isDisplayMenuBar ();

        int nextY = 0;

        // Placing window buttons
        int buttonsWidth = 0;
        if ( showWindowButtons )
        {
            // Moving buttons to top layer
            parent.setComponentZOrder ( windowButtons, 0 );

            // Placing buttons properly
            final Dimension ps = windowButtons.getPreferredSize ();
            final int x = ltr ? s.width - i.right - ps.width : i.left;
            windowButtons.setVisible ( true );
            windowButtons.setBounds ( x, i.top, ps.width, ps.height );
            buttonsWidth = ps.width;
        }
        else if ( windowButtons != null )
        {
            windowButtons.setVisible ( false );
        }

        // Placing window title component
        if ( showTitleComponent )
        {
            final Dimension ps = titleComponent.getPreferredSize ();
            titleComponent.setVisible ( true );
            titleComponent.setBounds ( ltr ? i.left : i.left + buttonsWidth, i.top, w - buttonsWidth, ps.height );
            nextY += ps.height;
        }
        else if ( titleComponent != null )
        {
            titleComponent.setVisible ( false );
        }

        // Placing layered pane
        final JLayeredPane layeredPane = root.getLayeredPane ();
        if ( layeredPane != null )
        {
            layeredPane.setBounds ( i.left, i.top, w, h );
        }

        // Placing menu bar
        if ( showMenuBar )
        {
            final Dimension mbd = menuBar.getPreferredSize ();
            menuBar.setVisible ( true );
            menuBar.setBounds ( 0, nextY, w, mbd.height );
            nextY += mbd.height;
        }
        else if ( menuBar != null )
        {
            menuBar.setVisible ( false );
        }

        // Placing glass pane
        final Component glassPane = root.getGlassPane ();
        if ( glassPane != null )
        {
            glassPane.setBounds ( i.left, i.top, w, h );
        }

        final Container contentPane = root.getContentPane ();
        if ( contentPane != null )
        {
            contentPane.setBounds ( 0, nextY, w, h < nextY ? 0 : h - nextY );
        }
    }

    /**
     * Returns layout size for various cases.
     *
     * @param parent    layout container
     * @param preferred whether return preferred size or not
     * @return layout size for various cases
     */
    private Dimension calculateSize ( final Container parent, final boolean preferred )
    {
        final Insets i = parent.getInsets ();
        final JRootPane root = ( JRootPane ) parent;
        final WebRootPaneUI rootUI = ( WebRootPaneUI ) root.getUI ();

        final GroupPane windowButtons = rootUI.getButtonsPanel ();
        final JComponent titleComponent = rootUI.getTitleComponent ();
        final JMenuBar menuBar = root.getJMenuBar ();
        final boolean showWindowButtons = windowButtons != null && rootUI.isDisplayWindowButtons () &&
                ( rootUI.isDisplayMinimizeButton () || rootUI.isDisplayMaximizeButton () || rootUI.isDisplayCloseButton () );
        final boolean showTitleComponent = titleComponent != null && rootUI.isDisplayTitleComponent ();
        final boolean showMenuBar = menuBar != null && rootUI.isDisplayMenuBar ();

        // Title pane size
        final Dimension titleDim;
        if ( showTitleComponent )
        {
            titleDim = titleComponent.getPreferredSize ();
        }
        else
        {
            titleDim = new Dimension ( 0, 0 );
        }
        final Dimension buttonsDim;
        if ( showWindowButtons )
        {
            buttonsDim = windowButtons.getPreferredSize ();
        }
        else
        {
            buttonsDim = new Dimension ( 0, 0 );
        }
        final Dimension menuDim;
        if ( showMenuBar )
        {
            menuDim = menuBar.getPreferredSize ();
        }
        else
        {
            menuDim = new Dimension ( 0, 0 );
        }
        final int tpWidth;
        final int tpHeight;
        if ( preferred )
        {
            if ( showWindowButtons )
            {
                if ( showTitleComponent )
                {
                    if ( showMenuBar )
                    {
                        // Title, menu and buttons
                        tpWidth = Math.max ( titleDim.width + buttonsDim.width, menuDim.width );
                        tpHeight = Math.max ( titleDim.height, buttonsDim.height ) + menuDim.height;
                    }
                    else
                    {
                        // Title and buttons
                        tpWidth = titleDim.width + buttonsDim.width;
                        tpHeight = Math.max ( titleDim.height, buttonsDim.height );
                    }
                }
                else
                {
                    if ( showMenuBar )
                    {
                        // Menu and buttons
                        tpWidth = menuDim.width + buttonsDim.width;
                        tpHeight = Math.max ( menuDim.height, buttonsDim.height );
                    }
                    else
                    {
                        // Buttons only
                        tpWidth = buttonsDim.width;
                        tpHeight = 0;
                    }
                }
            }
            else
            {
                if ( showTitleComponent && showMenuBar )
                {
                    // Title and menu
                    tpWidth = Math.max ( titleDim.width, menuDim.width );
                    tpHeight = titleDim.height + menuDim.height;
                }
                else if ( showTitleComponent )
                {
                    // Title only
                    tpWidth = titleDim.width;
                    tpHeight = titleDim.height;
                }
                else if ( showMenuBar )
                {
                    // Menu only
                    tpWidth = menuDim.width;
                    tpHeight = menuDim.height;
                }
                else
                {
                    // Empty
                    tpWidth = 0;
                    tpHeight = 0;
                }
            }
        }
        else
        {
            if ( showWindowButtons )
            {
                if ( showTitleComponent )
                {
                    // Title and buttons
                    tpWidth = titleDim.width + buttonsDim.width;
                    tpHeight = Math.max ( titleDim.height, buttonsDim.height );
                }
                else
                {
                    // Buttons only
                    tpWidth = buttonsDim.width;
                    tpHeight = buttonsDim.height;
                }
            }
            else
            {
                if ( showTitleComponent )
                {
                    // Title only
                    tpWidth = titleDim.width;
                    tpHeight = titleDim.height;
                }
                else
                {
                    // Empty
                    tpWidth = 0;
                    tpHeight = 0;
                }
            }
        }

        // Content pane size
        final Dimension cpd;
        int cpWidth = 0;
        int cpHeight = 0;
        if ( preferred )
        {
            if ( root.getContentPane () != null )
            {
                cpd = root.getContentPane ().getPreferredSize ();
            }
            else
            {
                cpd = root.getSize ();
            }
        }
        else
        {
            cpd = new Dimension ( 0, 0 );
        }
        if ( cpd != null )
        {
            cpWidth = cpd.width;
            cpHeight = cpd.height;
        }

        // Computing final size
        final int width = i.left + MathUtils.max ( tpWidth, cpWidth ) + i.right;
        final int height = i.top + tpHeight + cpHeight + i.bottom;
        return new Dimension ( width, height );
    }
}