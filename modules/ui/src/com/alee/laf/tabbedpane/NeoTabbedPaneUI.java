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

package com.alee.laf.tabbedpane;

import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.View;
import java.awt.*;
import java.util.Map;

/**
 * @author Mikle Garin
 */

@Deprecated
public class NeoTabbedPaneUI extends BasicTabbedPaneUI
{
    private final Color borderColor = new Color ( 165, 165, 165 );
    private final Color innerBorderColor = new Color ( 248, 248, 248 );

    public static ComponentUI createUI ( final JComponent c )
    {
        return new NeoTabbedPaneUI ();
    }

    @Override
    protected void paintTabArea ( final Graphics g, final int tabPlacement, final int selectedIndex )
    {
        final Graphics2D g2d = ( Graphics2D ) g;

        final int tabCount = tabPane.getTabCount ();

        final Rectangle iconRect = new Rectangle ();
        final Rectangle textRect = new Rectangle ();
        final Rectangle clipRect = g.getClipBounds ();

        // Paint tabRuns of tabs from back to front
        int sr = -1;
        int sc = -1;
        int scr = -1;
        for ( int i = runCount - 1; i >= 0; i-- )
        {
            int col = 0;
            final int start = tabRuns[ i ];
            final int next = tabRuns[ i == runCount - 1 ? 0 : i + 1 ];
            final int end = next != 0 ? next - 1 : tabCount - 1;
            final int colsInRow = end - start + 1;
            for ( int j = start; j <= end; j++ )
            {
                if ( j != selectedIndex && rects[ j ].intersects ( clipRect ) )
                {
                    paintTab ( g2d, tabPlacement, rects, runCount - 1 - i, col, colsInRow, j, iconRect, textRect );
                }
                if ( j == selectedIndex )
                {
                    sr = runCount - 1 - i;
                    sc = col;
                    scr = colsInRow;
                }
                col++;
            }
        }

        // Paint selected tab if its in the front run
        // since it may overlap other tabs
        if ( selectedIndex >= 0 && rects[ selectedIndex ].intersects ( clipRect ) )
        {
            paintTab ( g2d, tabPlacement, rects, sr, sc, scr, selectedIndex, iconRect, textRect );
        }

        // Shade/bg
        //        if ( runCount == 1 )
        //        {
        //            Rectangle lastTab = rects[rects.length-1];
        //            int x = lastTab.x;
        //            int y = lastTab.y;
        //            int w = lastTab.width;
        //            int h = lastTab.height;
        //            Insets insets = tabPane.getInsets ();
        //
        //            // Bottom shade
        //            g2d.setPaint ( new Color ( 191, 191, 191 ) );
        //            g2d.drawLine ( x+w, y + h - 1, tabPane.getWidth ()-insets.right-tabAreaInsets.right, y + h - 1 );
        //            g2d.setPaint ( new Color ( 200, 200, 200 ) );
        //            g2d.drawLine ( x+w, y + h - 2, tabPane.getWidth ()-insets.right-tabAreaInsets.right, y + h - 2 );
        //        }
    }

    protected void paintTab ( final Graphics2D g2d, final int tabPlacement, final Rectangle[] rects, final int row, final int col,
                              final int colsInRow, final int tabIndex, final Rectangle iconRect, final Rectangle textRect )
    {
        final Rectangle tabRect = rects[ tabIndex ];
        final int selectedIndex = tabPane.getSelectedIndex ();
        final boolean isSelected = selectedIndex == tabIndex;

        paintTabBackground ( g2d, tabPlacement, row, col, colsInRow, tabIndex, tabRect.x, tabRect.y, tabRect.width, tabRect.height,
                isSelected );

        final String title = tabPane.getTitleAt ( tabIndex );
        final Font font = tabPane.getFont ();
        final FontMetrics metrics = SwingUtils.getFontMetrics ( tabPane, g2d, font );
        final Icon icon = getIconForTab ( tabIndex );

        layoutLabel ( tabPlacement, metrics, tabIndex, title, icon, tabRect, iconRect, textRect, isSelected );

        final boolean customTabComponent = tabPane.getTabComponentAt ( tabIndex ) == null;
        if ( customTabComponent )
        {
            paintIcon ( g2d, tabPlacement, tabIndex, icon, iconRect, isSelected );
            paintText ( g2d, tabPlacement, font, metrics, tabIndex, title, textRect, isSelected );
        }
        //paintFocusIndicator ( g2d, tabPlacement, rects, tabIndex, iconRect, textRect, isSelected, customTabComponent );
    }

    @Override
    protected void paintText ( final Graphics g, final int tabPlacement, final Font font, final FontMetrics metrics, final int tabIndex,
                               final String title, final Rectangle textRect, final boolean isSelected )
    {
        final Map aa = SwingUtils.setupTextAntialias ( g );
        g.setFont ( font );
        final View v = getTextViewForTab ( tabIndex );
        if ( v != null )
        {
            // html
            v.paint ( g, textRect );
        }
        else
        {
            // plain text
            final int mnemIndex = tabPane.getDisplayedMnemonicIndexAt ( tabIndex );

            if ( tabPane.isEnabled () && tabPane.isEnabledAt ( tabIndex ) )
            {
                Color fg = tabPane.getForegroundAt ( tabIndex );
                if ( isSelected && fg instanceof UIResource )
                {
                    final Color selectedFG = UIManager.getColor ( "TabbedPane.selectedForeground" );
                    if ( selectedFG != null )
                    {
                        fg = selectedFG;
                    }
                }
                g.setColor ( fg );
                SwingUtils.drawStringUnderlineCharAt ( g, title, mnemIndex, textRect.x, textRect.y + metrics.getAscent () );

            }
            else
            {
                // tab disabled
                g.setColor ( tabPane.getBackgroundAt ( tabIndex ).brighter () );
                SwingUtils.drawStringUnderlineCharAt ( g, title, mnemIndex, textRect.x, textRect.y + metrics.getAscent () );
                g.setColor ( tabPane.getBackgroundAt ( tabIndex ).darker () );
                SwingUtils.drawStringUnderlineCharAt ( g, title, mnemIndex, textRect.x - 1, textRect.y + metrics.getAscent () - 1 );
            }
        }
        SwingUtils.restoreTextAntialias ( g, aa );
    }

    //    protected void paintFocusIndicator ( Graphics2D g2d, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect,
    //                                         Rectangle textRect, boolean isSelected, boolean customTabComponent )
    //    {
    //        Rectangle tabRect = rects[ tabIndex ];
    //        if ( tabPane.hasFocus () && isSelected )
    //        {
    //            int x, y, w, h;
    //            switch ( tabPlacement )
    //            {
    //                case LEFT:
    //                    x = tabRect.x + 3;
    //                    y = tabRect.y + 3;
    //                    w = tabRect.width - 5;
    //                    h = tabRect.height - 6;
    //                    break;
    //                case RIGHT:
    //                    x = tabRect.x + 2;
    //                    y = tabRect.y + 3;
    //                    w = tabRect.width - 5;
    //                    h = tabRect.height - 6;
    //                    break;
    //                case BOTTOM:
    //                    x = tabRect.x + 3;
    //                    y = tabRect.y + 2;
    //                    w = tabRect.width - 6;
    //                    h = tabRect.height - 5;
    //                    break;
    //                case TOP:
    //                default:
    //                    x = tabRect.x + 1;
    //                    y = tabRect.y + 2;
    //                    w = tabRect.width - 4;
    //                    h = tabRect.height - 4;
    //            }
    //
    //            g2d.setPaint ( focus );
    //            LafUtils.drawDashedRect ( g2d, x, y, x + w, y + h, 4, 4 );
    //        }
    //    }

    protected void paintTabBackground ( final Graphics2D g2d, final int tabPlacement, final int row, final int col, final int colsInRow,
                                        final int tabIndex, final int x, final int y, final int w, final int h, final boolean isSelected )
    {

        // Background
        if ( isSelected )
        {
            g2d.setPaint ( new GradientPaint ( x, y, Color.WHITE, x, y + h, new Color ( 232, 232, 232 ) ) );
            g2d.fillRect ( x + 1, y, w - 2, h );
        }
        else
        {
            g2d.setPaint ( new GradientPaint ( x, y, new Color ( 226, 226, 226 ), x, y + h, new Color ( 205, 205, 205 ) ) );
            g2d.fillRect ( x, y, w, h );
        }

        // Border
        final boolean topBorder = row > 0;
        final boolean trailingBorder = runCount == 1 || col < colsInRow - 1;
        if ( isSelected )
        {
            // Border
            g2d.setPaint ( borderColor );
            if ( topBorder )
            {
                g2d.drawLine ( x, y, x + w - 1, y );
            }
            if ( trailingBorder )
            {
                g2d.drawLine ( x + w - 1, y, x + w - 1, y + h - 2 );
            }

            // Inner border
            g2d.setPaint ( innerBorderColor );
            g2d.drawLine ( x, y + 1, x, y + h - 1 );
            g2d.drawLine ( x + 1, y + ( topBorder ? 1 : 0 ), x + w - ( trailingBorder ? 2 : 1 ), y + ( topBorder ? 1 : 0 ) );
            g2d.drawLine ( x + w - ( trailingBorder ? 2 : 1 ), y + 1, x + w - ( trailingBorder ? 2 : 1 ), y + h - 1 );
        }
        else
        {
            // Top shade
            if ( row == 0 )
            {
                g2d.setPaint ( new Color ( 191, 191, 191 ) );
                g2d.drawLine ( x, y, x + w - 1, y );
            }

            // Bottom shade
            g2d.setPaint ( new Color ( 191, 191, 191 ) );
            g2d.drawLine ( x, y + h - 1, x + w - 1, y + h - 1 );
            g2d.setPaint ( new Color ( 200, 200, 200 ) );
            g2d.drawLine ( x, y + h - 2, x + w - 1, y + h - 2 );

            // Border
            g2d.setPaint ( borderColor );
            if ( topBorder )
            {
                g2d.drawLine ( x, y, x + w - 1, y );
            }
            if ( trailingBorder )
            {
                g2d.drawLine ( x + w - 1, y, x + w - 1, y + h - 1 );
            }

            // Inner border
            if ( topBorder )
            {
                g2d.setPaint ( innerBorderColor );
                g2d.drawLine ( x, y + 1, x + w - 2, y + 1 );
            }
        }
    }

    @Override
    protected void paintContentBorder ( final Graphics g, final int tabPlacement, final int selectedIndex )
    {
        final int width = tabPane.getWidth ();
        // int height = tabPane.getHeight ();
        final Insets insets = tabPane.getInsets ();

        int x = insets.left;
        int y = insets.top;
        int w = width - insets.right - insets.left;
        // int h = height - insets.top - insets.bottom;

        switch ( tabPlacement )
        {
            case LEFT:
                x += calculateTabAreaWidth ( tabPlacement, runCount, maxTabWidth );
                w -= x - insets.left;
                break;
            case RIGHT:
                w -= calculateTabAreaWidth ( tabPlacement, runCount, maxTabWidth );
                break;
            case BOTTOM:
                // h -= calculateTabAreaHeight ( tabPlacement, runCount, maxTabHeight );
                break;
            case TOP:
            default:
                y += calculateTabAreaHeight ( tabPlacement, runCount, maxTabHeight );
                // h -= ( y - insets.top );
                break;
        }

        // Rectangle selectedTab = getTabBounds ( selectedIndex, calcRect );

        switch ( tabPlacement )
        {
            case LEFT:
            case RIGHT:
            case BOTTOM:
            case TOP:
            default:
            {
                final Graphics2D g2d = ( Graphics2D ) g;

                g2d.setPaint ( new GradientPaint ( x, y + 1, new Color ( 232, 232, 232 ), x, y + 3, new Color ( 230, 230, 230 ) ) );
                g2d.fillRect ( x, y + 1, w, 3 );

                g2d.setPaint ( borderColor );
                g2d.drawLine ( x, y, x + w, y );

                g2d.setPaint ( innerBorderColor );
                g2d.drawLine ( x, y + 1, x, y + 3 );
                g2d.drawLine ( x + w - 1, y + 1, x + w - 1, y + 3 );
                g2d.drawLine ( x, y + 1, x + w, y + 1 );

                g2d.setPaint ( new Color ( 184, 184, 184 ) );
                g2d.drawLine ( x, y + 4, x + w, y + 4 );

                break;
            }
        }
    }
}