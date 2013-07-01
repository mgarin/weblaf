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
import com.alee.laf.WebLookAndFeel;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.FocusType;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.util.Map;

/**
 * User: mgarin Date: 15.08.11 Time: 19:47
 */

public class WebMenuUI extends BasicMenuUI
{
    private static final Point iconLocation = new Point ( 6, 5 );

    private boolean mouseover = false;

    private MouseAdapter mouseAdapter = null;

    public static ComponentUI createUI ( JComponent c )
    {
        return new WebMenuUI ();
    }

    public void installUI ( JComponent c )
    {
        super.installUI ( c );

        // Default settings
        SwingUtils.setOrientation ( menuItem );
        menuItem.setOpaque ( false );
        menuItem.setBorder ( BorderFactory.createEmptyBorder ( 5, 2, 5, 2 ) );
        menuItem.setBackground ( StyleConstants.menuSelectionColor );

        // Rollover listener
        mouseAdapter = new MouseAdapter ()
        {
            public void mouseEntered ( MouseEvent e )
            {
                mouseover = true;
                menuItem.repaint ();
            }

            public void mouseExited ( MouseEvent e )
            {
                mouseover = false;
                menuItem.repaint ();
            }
        };
        menuItem.addMouseListener ( mouseAdapter );
    }

    public void uninstallUI ( JComponent c )
    {
        menuItem.removeMouseListener ( mouseAdapter );

        super.uninstallUI ( c );
    }

    protected Dimension getPreferredMenuItemSize ( JComponent c, Icon checkIcon, Icon arrowIcon, int defaultTextIconGap )
    {
        JMenuItem mi = ( JMenuItem ) c;
        MenuItemLayoutHelper lh =
                new MenuItemLayoutHelper ( mi, checkIcon, arrowIcon, MenuItemLayoutHelper.createMaxRect (), defaultTextIconGap,
                        getAccelerationDelimeter (), mi.getComponentOrientation ().isLeftToRight (), mi.getFont (), acceleratorFont,
                        MenuItemLayoutHelper.useCheckAndArrow ( menuItem ), getPropertyPrefix () );

        Dimension result = new Dimension ();

        // Calculate the result width
        result.width = lh.getLeadingGap ();
        MenuItemLayoutHelper.addMaxWidth ( lh.getCheckSize (), lh.getAfterCheckIconGap (), result );
        // Take into account mimimal text offset.
        if ( ( !lh.isTopLevelMenu () ) && ( lh.getMinTextOffset () > 0 ) &&
                ( result.width < lh.getMinTextOffset () ) )
        {
            result.width = lh.getMinTextOffset ();
        }
        MenuItemLayoutHelper.addMaxWidth ( lh.getLabelSize (), lh.getGap (), result );
        MenuItemLayoutHelper.addMaxWidth ( lh.getAccSize (), lh.getGap (), result );
        MenuItemLayoutHelper.addMaxWidth ( lh.getArrowSize (), lh.getGap (), result );

        // Calculate the result height
        result.height = MenuItemLayoutHelper
                .max ( lh.getCheckSize ().getHeight (), lh.getLabelSize ().getHeight (), lh.getAccSize ().getHeight (),
                        lh.getArrowSize ().getHeight () );

        // Take into account menu item insets
        Insets insets = lh.getMenuItem ().getInsets ();
        if ( insets != null )
        {
            result.width += insets.left + insets.right;
            result.height += insets.top + insets.bottom;
        }

        return result;
    }

    protected void paintMenuItem ( Graphics g, JComponent c, Icon checkIcon, Icon arrowIcon, Color background, Color foreground,
                                   int defaultTextIconGap )
    {
        // Saving original paint settings
        Font holdf = g.getFont ();
        Color holdc = g.getColor ();

        // Setting font
        JMenuItem mi = ( JMenuItem ) c;
        g.setFont ( mi.getFont () );

        // Creating helper class
        Rectangle viewRect = new Rectangle ( 0, 0, mi.getWidth (), mi.getHeight () );
        applyInsets ( viewRect, mi.getInsets () );
        MenuItemLayoutHelper lh =
                new MenuItemLayoutHelper ( mi, checkIcon, arrowIcon, viewRect, defaultTextIconGap, getAccelerationDelimeter (),
                        mi.getComponentOrientation ().isLeftToRight (), mi.getFont (), acceleratorFont,
                        MenuItemLayoutHelper.useCheckAndArrow ( menuItem ), getPropertyPrefix () );
        MenuItemLayoutHelper.LayoutResult lr = lh.layoutMenuItem ();

        // Painting menu parts
        paintBackground ( g, mi, lh );
        paintCheckIcon ( g, lh );
        paintIcon ( g, lh );
        paintArrowIcon ( g, lh, lr );

        // Painting text parts
        Map hints = SwingUtils.setupTextAntialias ( g, c );
        paintText ( g, lh, lr );
        paintAccText ( g, lh, lr );
        SwingUtils.restoreTextAntialias ( g, hints );

        // Retoring original settings
        g.setColor ( holdc );
        g.setFont ( holdf );
    }

    protected void paintBackground ( Graphics g, JMenuItem menuItem, MenuItemLayoutHelper lh )
    {
        if ( menuItem.isEnabled () )
        {
            ButtonModel model = menuItem.getModel ();
            if ( mouseover || model.isArmed () || model.isSelected () )
            {
                boolean onPopup = lh.getMenuItemParent () instanceof JPopupMenu;
                int shadeWidth = onPopup ? 0 : StyleConstants.shadeWidth;
                LafUtils.drawWebStyle ( ( Graphics2D ) g, menuItem, StyleConstants.shadeColor, shadeWidth, StyleConstants.smallRound,
                        menuItem.isEnabled (), !model.isSelected () || onPopup,
                        model.isSelected () ? StyleConstants.averageBorderColor : StyleConstants.borderColor );

                //            if ( menuItem.isEnabled () )
                //            {
                //                if ( model.isSelected () )
                //                {
                //                    LafUtils.drawWebFocus ( g2d, menuItem, StyleConstants.focusType,
                //                            StyleConstants.shadeWidth, StyleConstants.smallRound, null, true );
                //                }
                //                else
                //                {
                //                    LafUtils.drawWebFocus ( g2d, menuItem, StyleConstants.focusType,
                //                            StyleConstants.shadeWidth, StyleConstants.smallRound, null, true,
                //                            StyleConstants.rolloverMenuBorderColor );
                //                }
                //            }

                if ( menuItem.isEnabled () && onPopup )
                {
                    LafUtils.drawWebFocus ( ( Graphics2D ) g, menuItem, FocusType.fieldFocus, shadeWidth, StyleConstants.smallRound, null,
                            true, StyleConstants.darkBorderColor );
                }
            }
        }
    }

    protected void applyInsets ( Rectangle rect, Insets insets )
    {
        if ( insets != null )
        {
            rect.x += insets.left;
            rect.y += insets.top;
            rect.width -= ( insets.right + rect.x );
            rect.height -= ( insets.bottom + rect.y );
        }
    }

    protected String getAccelerationDelimeter ()
    {
        String delim = UIManager.getString ( "MenuItem.acceleratorDelimiter" );
        return delim != null ? delim : "+";
    }

    protected void paintIcon ( Graphics g, MenuItemLayoutHelper lh )
    {
        if ( lh.getIcon () != null )
        {
            Icon icon;
            ButtonModel model = lh.getMenuItem ().getModel ();
            if ( !model.isEnabled () )
            {
                icon = lh.getMenuItem ().getDisabledIcon ();
            }
            else if ( model.isPressed () && model.isArmed () )
            {
                icon = lh.getMenuItem ().getPressedIcon ();
                if ( icon == null )
                {
                    // Use default icon
                    icon = lh.getMenuItem ().getIcon ();
                }
            }
            else
            {
                icon = lh.getMenuItem ().getIcon ();
            }

            if ( icon != null )
            {
                icon.paintIcon ( lh.getMenuItem (), g, iconLocation.x, iconLocation.y );
            }
        }
    }

    protected void paintCheckIcon ( Graphics g, MenuItemLayoutHelper lh )
    {
        if ( lh.useCheckAndArrow () && lh.getCheckIcon () != null )
        {
            lh.getCheckIcon ().paintIcon ( lh.getMenuItem (), g, iconLocation.x, iconLocation.y );
        }
    }

    protected void paintArrowIcon ( Graphics g, MenuItemLayoutHelper lh, MenuItemLayoutHelper.LayoutResult lr )
    {
        if ( lh.useCheckAndArrow () && lh.getArrowIcon () != null )
        {
            lh.getArrowIcon ().paintIcon ( lh.getMenuItem (), g, lr.getArrowRect ().x, lr.getArrowRect ().y );
        }
    }

    protected void paintText ( Graphics g, MenuItemLayoutHelper lh, MenuItemLayoutHelper.LayoutResult lr )
    {
        if ( !lh.getText ().equals ( "" ) )
        {
            Rectangle rect = lr.getTextRect ();

            Font font = menuItem.getFont ();
            FontRenderContext renderContext = menuItem.getFontMetrics ( font ).getFontRenderContext ();
            GlyphVector glyphVector = font.createGlyphVector ( renderContext, menuItem.getText () );
            Rectangle visualBounds = glyphVector.getVisualBounds ().getBounds ();
            rect.y = menuItem.getHeight () / 2 - visualBounds.height / 2;
            rect.height = visualBounds.height;

            if ( lh.getMenuItemParent () instanceof JPopupMenu )
            {
                rect.x -= lh.getMenuItem ().getIconTextGap () * 2 - 1;
            }
            if ( lh.getHtmlView () != null )
            {
                // Text is HTML
                lh.getHtmlView ().paint ( g, rect );
            }
            else
            {
                // Text isn't HTML
                paintText ( g, lh.getMenuItem (), rect, lh.getText () );
            }
        }
    }

    protected void paintText ( Graphics g, JMenuItem menuItem, Rectangle rect, String text )
    {
        int mnemIndex = WebLookAndFeel.isMnemonicHidden () ? -1 : menuItem.getDisplayedMnemonicIndex ();

        if ( !menuItem.isEnabled () )
        {
            g.setColor ( UIManager.getColor ( "MenuItem.disabledForeground" ) );
        }
        else
        {
            g.setColor ( menuItem.getForeground () );
        }

        SwingUtils.drawStringUnderlineCharAt ( menuItem, g, text, mnemIndex, rect.x, rect.y + rect.height );
    }

    protected void paintAccText ( Graphics g, MenuItemLayoutHelper lh, MenuItemLayoutHelper.LayoutResult lr )
    {
        if ( !lh.getAccText ().equals ( "" ) )
        {
            ButtonModel model = lh.getMenuItem ().getModel ();

            g.setFont ( lh.getAccFontMetrics ().getFont () );
            g.setColor ( model.isEnabled () ? StyleConstants.infoTextColor : StyleConstants.disabledInfoTextColor );

            Rectangle rect = lr.getAccRect ();
            rect.x = lh.getMenuItem ().getWidth () - 7 -
                    lh.getAccFontMetrics ().stringWidth ( lh.getAccText () );
            SwingUtils.drawString ( lh.getMenuItem (), g, lh.getAccText (), rect.x,
                    lh.getMenuItem ().getHeight () / 2 + lh.getAccFontMetrics ().getAscent () / 2 - 1 );
        }
    }
}
