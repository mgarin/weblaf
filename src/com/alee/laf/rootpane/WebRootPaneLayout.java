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

public class WebRootPaneLayout extends AbstractLayoutManager
{
    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension preferredLayoutSize ( final Container parent )
    {
        return calculateSize ( parent, true );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension minimumLayoutSize ( final Container parent )
    {
        return calculateSize ( parent, false );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layoutContainer ( final Container parent )
    {
        final JRootPane root = ( JRootPane ) parent;
        final WebRootPaneUI rootUI = ( WebRootPaneUI ) root.getUI ();
        final Insets i = parent.getInsets ();
        final Insets ci = new Insets ( 1, 1, 1, 1 );
        final Dimension s = parent.getSize ();
        final int w = s.width - i.right - i.left;
        final int h = s.height - i.top - i.bottom;
        final boolean ltr = root.getComponentOrientation ().isLeftToRight ();

        final WebButtonGroup windowButtons = rootUI.getWindowButtons ();
        final JComponent titleComponent = rootUI.getTitleComponent ();
        final JMenuBar menuBar = root.getJMenuBar ();
        final JComponent resizeCorner = rootUI.getResizeCorner ();
        final boolean showWindowButtons = windowButtons != null && rootUI.isShowWindowButtons () &&
                ( rootUI.isShowMinimizeButton () || rootUI.isShowMaximizeButton () || rootUI.isShowCloseButton () );
        final boolean showTitleComponent = titleComponent != null && rootUI.isShowTitleComponent ();
        final boolean showMenuBar = menuBar != null && rootUI.isShowMenuBar ();
        final boolean showResizeCorner =
                resizeCorner != null && rootUI.isResizable () && rootUI.isShowResizeCorner () && !rootUI.isFrameMaximized ();

        int nextY = 0;

        // Placing window buttons
        int buttonsWidth = 0;
        if ( showWindowButtons )
        {
            // Moving buttons to top layer
            parent.setComponentZOrder ( windowButtons, 0 );

            // Placing buttons properly
            final Dimension ps = windowButtons.getPreferredSize ();
            final int buttonsShear = getButtonsShear ( rootUI );
            final int x = ltr ? s.width - i.right - buttonsShear - ps.width : i.left + buttonsShear;
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
            layeredPane.setBounds ( i.left + ci.left, i.top + ci.top, w - ci.left - ci.right, h - ci.top - ci.bottom );
        }

        // Placing menu bar
        if ( showMenuBar )
        {
            final Dimension mbd = menuBar.getPreferredSize ();
            menuBar.setVisible ( true );
            menuBar.setBounds ( 0, nextY, w - ci.left - ci.right, mbd.height );
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
            contentPane.setBounds ( 0, nextY, w - ci.left - ci.right, h < nextY ? 0 : h - nextY - ci.top - ci.bottom );
        }

        // Placing window resize corner
        if ( showResizeCorner )
        {
            // parent.setComponentZOrder ( resizeCorner, 0 );
            final Dimension ps = resizeCorner.getPreferredSize ();
            resizeCorner.setVisible ( true );
            resizeCorner.setBounds ( s.width - i.right - ps.width - 2, s.height - i.bottom - ps.height - 2, ps.width, ps.height );
        }
        else if ( resizeCorner != null )
        {
            resizeCorner.setVisible ( false );
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
        final Insets ci = new Insets ( 1, 1, 1, 1 );
        final JRootPane root = ( JRootPane ) parent;
        final WebRootPaneUI rootUI = ( WebRootPaneUI ) root.getUI ();

        final WebButtonGroup windowButtons = rootUI.getWindowButtons ();
        final JComponent titleComponent = rootUI.getTitleComponent ();
        final JMenuBar menuBar = root.getJMenuBar ();
        final JComponent resizeCorner = rootUI.getResizeCorner ();
        final boolean showWindowButtons = windowButtons != null && rootUI.isShowWindowButtons () &&
                ( rootUI.isShowMinimizeButton () || rootUI.isShowMaximizeButton () || rootUI.isShowCloseButton () );
        final boolean showTitleComponent = titleComponent != null && rootUI.isShowTitleComponent ();
        final boolean showMenuBar = menuBar != null && rootUI.isShowMenuBar ();
        final boolean showResizeCorner = resizeCorner != null && rootUI.isShowResizeCorner () && !rootUI.isFrameMaximized ();

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
                final int buttonsShear = getButtonsShear ( rootUI );
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
                final int buttonsShear = getButtonsShear ( rootUI );
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
        if ( showResizeCorner )
        {
            // Placing resize corner at the top of all other components within the root pane
            // Called from here since root pane content might be changed and we have to keep this up-to-date
            parent.setComponentZOrder ( resizeCorner, 0 );

            final Dimension rcd = resizeCorner.getPreferredSize ();
            if ( rcd != null )
            {
                cpWidth = Math.max ( cpWidth, rcd.width );
                cpHeight = Math.max ( cpHeight, rcd.height );
            }
        }
        cpWidth += ci.left + ci.right;
        cpHeight += ci.top + ci.bottom;

        // Computing final size
        final int width = i.left + MathUtils.max ( tpWidth, cpWidth ) + i.right;
        final int height = i.top + tpHeight + cpHeight + i.bottom;
        final NinePatchIcon shadeIcon = rootUI.getShadeIcon ( root );
        final Dimension d = new Dimension ( width, height );
        return shadeIcon != null ? SwingUtils.max ( d, shadeIcon.getPreferredSize () ) : d;
    }

    /**
     * Returns button side shear depending on root pane UI settings.
     *
     * @param webRootPaneUI root pane UI
     * @return button side shear
     */
    private int getButtonsShear ( final WebRootPaneUI webRootPaneUI )
    {
        final int round = webRootPaneUI.getRound ();
        return webRootPaneUI.isAttachButtons () && round > 0 ? round - WebButtonStyle.shadeWidth : 0;
    }
}