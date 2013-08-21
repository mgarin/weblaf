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

import com.alee.extended.panel.WebButtonGroup;
import com.alee.laf.button.WebButtonStyle;
import com.alee.utils.MathUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.ninepatch.NinePatchIcon;

import javax.swing.*;
import java.awt.*;

/**
 * Special layout for decorated root pane.
 *
 * @author Mikle Garin
 */

public class WebRootPaneLayout implements LayoutManager
{
    /**
     * Returns preferred layout size.
     *
     * @param parent layout container
     * @return preferred layout size
     */
    @Override
    public Dimension preferredLayoutSize ( Container parent )
    {
        return calculateSize ( parent, true );
    }

    /**
     * Returns minimum layout size.
     *
     * @param parent layout container
     * @return minimum layout size
     */
    @Override
    public Dimension minimumLayoutSize ( Container parent )
    {
        return calculateSize ( parent, false );
    }

    /**
     * Returns layout size for various cases.
     *
     * @param parent    layout container
     * @param preferred whether return preferred size or not
     * @return layout size for various cases
     */
    private Dimension calculateSize ( Container parent, boolean preferred )
    {
        Insets i = parent.getInsets ();
        JRootPane root = ( JRootPane ) parent;
        WebRootPaneUI rootUI = ( WebRootPaneUI ) root.getUI ();

        WebButtonGroup windowButtons = rootUI.getWindowButtons ();
        JComponent titleComponent = rootUI.getTitleComponent ();
        JMenuBar menuBar = root.getJMenuBar ();
        JComponent resizeCorner = rootUI.getResizeCorner ();
        boolean isFrame = rootUI.isFrame ();
        boolean showWindowButtons = windowButtons != null && rootUI.isShowWindowButtons () &&
                ( rootUI.isShowMinimizeButton () || rootUI.isShowMaximizeButton () || rootUI.isShowCloseButton () );
        boolean showTitleComponent = titleComponent != null && rootUI.isShowTitleComponent ();
        boolean showMenuBar = menuBar != null && rootUI.isShowMenuBar ();
        boolean showResizeCorner = resizeCorner != null && rootUI.isShowResizeCorner () && !rootUI.isFrameMaximized ();

        // Title pane size
        Dimension titleDim;
        if ( showTitleComponent )
        {
            titleDim = titleComponent.getPreferredSize ();
        }
        else
        {
            titleDim = new Dimension ( 0, 0 );
        }
        Dimension buttonsDim;
        if ( showWindowButtons )
        {
            buttonsDim = windowButtons.getPreferredSize ();
        }
        else
        {
            buttonsDim = new Dimension ( 0, 0 );
        }
        Dimension menuDim;
        if ( showMenuBar )
        {
            menuDim = menuBar.getPreferredSize ();
        }
        else
        {
            menuDim = new Dimension ( 0, 0 );
        }
        int tpWidth;
        int tpHeight;
        if ( preferred )
        {
            if ( showWindowButtons )
            {
                int buttonsShear = getButtonsShear ( rootUI );
                if ( showTitleComponent )
                {
                    if ( showMenuBar )
                    {
                        // Title, menu and buttons
                        tpWidth = Math.max ( titleDim.width + buttonsDim.width + buttonsShear, menuDim.width );
                        tpHeight = Math.max ( titleDim.height, buttonsDim.height ) + menuDim.height;
                    }
                    else
                    {
                        // Title and buttons
                        tpWidth = titleDim.width + buttonsDim.width + buttonsShear;
                        tpHeight = Math.max ( titleDim.height, buttonsDim.height );
                    }
                }
                else
                {
                    if ( showMenuBar )
                    {
                        // Menu and buttons
                        tpWidth = menuDim.width + buttonsDim.width + buttonsShear;
                        tpHeight = Math.max ( menuDim.height, buttonsDim.height );
                    }
                    else
                    {
                        // Buttons only
                        tpWidth = buttonsShear + buttonsDim.width + buttonsShear;
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
                int buttonsShear = getButtonsShear ( rootUI );
                if ( showTitleComponent )
                {
                    // Title and buttons
                    tpWidth = titleDim.width + buttonsDim.width + buttonsShear;
                    tpHeight = Math.max ( titleDim.height, buttonsDim.height );
                }
                else
                {
                    // Buttons only
                    tpWidth = buttonsShear + buttonsDim.width + buttonsShear;
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
        Dimension cpd;
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
        if ( showResizeCorner )
        {
            Dimension rcd = resizeCorner.getPreferredSize ();
            if ( rcd != null )
            {
                cpWidth = Math.max ( cpWidth, rcd.width );
                cpHeight = Math.max ( cpHeight, rcd.height );
            }
        }

        // Computing final size
        int width = i.left + MathUtils.max ( tpWidth, cpWidth ) + i.right;
        int height = i.top + tpHeight + cpHeight + i.bottom;
        NinePatchIcon shadeIcon = rootUI.getShadeIcon ( root );
        Dimension d = new Dimension ( width, height );
        return shadeIcon != null ? SwingUtils.max ( d, shadeIcon.getPreferredSize () ) : d;
    }

    /**
     * Layout container components.
     *
     * @param parent container to process
     */
    @Override
    public void layoutContainer ( Container parent )
    {
        JRootPane root = ( JRootPane ) parent;
        WebRootPaneUI rootUI = ( WebRootPaneUI ) root.getUI ();
        Insets m = parent.getInsets ();
        Dimension s = parent.getSize ();
        int w = s.width - m.right - m.left;
        int h = s.height - m.top - m.bottom;
        int nextY = 0;
        boolean ltr = root.getComponentOrientation ().isLeftToRight ();

        WebButtonGroup windowButtons = rootUI.getWindowButtons ();
        JComponent titleComponent = rootUI.getTitleComponent ();
        JMenuBar menuBar = root.getJMenuBar ();
        JComponent resizeCorner = rootUI.getResizeCorner ();
        boolean showWindowButtons = windowButtons != null && rootUI.isShowWindowButtons () &&
                ( rootUI.isShowMinimizeButton () || rootUI.isShowMaximizeButton () || rootUI.isShowCloseButton () );
        boolean showTitleComponent = titleComponent != null && rootUI.isShowTitleComponent ();
        boolean showMenuBar = menuBar != null && rootUI.isShowMenuBar ();
        boolean showResizeCorner =
                resizeCorner != null && rootUI.isResizable () && rootUI.isShowResizeCorner () && !rootUI.isFrameMaximized ();

        // Placing window buttons
        int buttonsWidth = 0;
        if ( showWindowButtons )
        {
            // Moving buttons to top layer
            parent.setComponentZOrder ( windowButtons, 0 );

            // Placing buttons properly
            Dimension ps = windowButtons.getPreferredSize ();
            int buttonsShear = getButtonsShear ( rootUI );
            int x = ltr ? s.width - m.right - buttonsShear - ps.width : m.left + buttonsShear;
            windowButtons.setVisible ( true );
            windowButtons.setBounds ( x, m.top, ps.width, ps.height );
            buttonsWidth = ps.width;
        }
        else if ( windowButtons != null )
        {
            windowButtons.setVisible ( false );
        }

        // Placing window title component
        if ( showTitleComponent )
        {
            Dimension ps = titleComponent.getPreferredSize ();
            titleComponent.setVisible ( true );
            titleComponent.setBounds ( ltr ? m.left : m.left + buttonsWidth, m.top, w - buttonsWidth, ps.height );
            nextY += ps.height;
        }
        else if ( titleComponent != null )
        {
            titleComponent.setVisible ( false );
        }

        // Placing menu bar
        if ( showMenuBar )
        {
            Dimension mbd = menuBar.getPreferredSize ();
            menuBar.setVisible ( true );
            menuBar.setBounds ( 0, nextY, w, mbd.height );
            nextY += mbd.height;
        }
        else if ( menuBar != null )
        {
            menuBar.setVisible ( false );
        }

        // Placing layered pane
        JLayeredPane layeredPane = root.getLayeredPane ();
        if ( layeredPane != null )
        {
            layeredPane.setBounds ( m.left, m.top, w, h );
        }

        // Placing glass pane
        Component glassPane = root.getGlassPane ();
        if ( glassPane != null )
        {
            glassPane.setBounds ( m.left, m.top, w, h );
        }

        Container contentPane = root.getContentPane ();
        if ( contentPane != null )
        {
            Dimension cpd = contentPane.getPreferredSize ();
            contentPane.setBounds ( 0, nextY, w, h < nextY ? 0 : h - nextY );
        }

        // Placing window resize corner
        if ( showResizeCorner )
        {
            parent.setComponentZOrder ( resizeCorner, 0 );
            Dimension ps = resizeCorner.getPreferredSize ();
            resizeCorner.setVisible ( true );
            resizeCorner.setBounds ( s.width - m.right - ps.width - 2, s.height - m.bottom - ps.height - 2, ps.width, ps.height );
        }
        else if ( resizeCorner != null )
        {
            resizeCorner.setVisible ( false );
        }
    }

    /**
     * Returns button side shear depending on root pane UI settings.
     *
     * @param webRootPaneUI root pane UI
     * @return button side shear
     */
    private int getButtonsShear ( WebRootPaneUI webRootPaneUI )
    {
        int round = webRootPaneUI.getRound ();
        return webRootPaneUI.isAttachButtons () && round > 0 ? round - WebButtonStyle.shadeWidth : 0;
    }

    /**
     * Adds component into layout.
     *
     * @param name component constraint
     * @param comp component to add
     */
    @Override
    public void addLayoutComponent ( String name, Component comp )
    {
        //
    }

    /**
     * Removes component from layout.
     *
     * @param comp component to remove
     */
    @Override
    public void removeLayoutComponent ( Component comp )
    {
        //
    }
}