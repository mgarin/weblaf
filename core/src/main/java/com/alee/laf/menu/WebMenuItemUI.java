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
import com.alee.laf.checkbox.WebCheckBoxUI;
import com.alee.laf.radiobutton.WebRadioButtonUI;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.FocusType;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuItemUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

/**
 * User: mgarin Date: 15.07.11 Time: 18:57
 */

public class WebMenuItemUI extends BasicMenuItemUI
{
    private static final Point iconLocation = new Point ( 6, 5 );

    private boolean mouseover = false;

    private MouseAdapter mouseAdapter = null;
    private ActionListener actionListener = null;

    public static ComponentUI createUI ( JComponent c )
    {
        return new WebMenuItemUI ();
    }

    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Default settings
        SwingUtils.setOrientation ( menuItem );
        menuItem.setOpaque ( false );
        menuItem.setBorder ( BorderFactory.createEmptyBorder ( 5, 2, 5, 6 ) );

        // Rollover listeners
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
        actionListener = new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                mouseover = false;
                menuItem.repaint ();
            }
        };
        menuItem.addActionListener ( actionListener );
    }

    public void uninstallUI ( JComponent c )
    {
        menuItem.removeMouseListener ( mouseAdapter );
        menuItem.removeActionListener ( actionListener );

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

    public void paint ( Graphics g, JComponent c )
    {
        super.paint ( g, c );
    }

    protected void paintMenuItem ( Graphics g, JComponent c, Icon checkIcon, Icon arrowIcon, Color background, Color foreground,
                                   int defaultTextIconGap )
    {
        // Saving original settings
        Font holdf = g.getFont ();
        Color holdc = g.getColor ();

        // Setting font
        JMenuItem mi = ( JMenuItem ) c;
        g.setFont ( mi.getFont () );

        // creating helper class
        Rectangle viewRect = new Rectangle ( 0, 0, mi.getWidth (), mi.getHeight () );
        applyInsets ( viewRect, mi.getInsets () );
        MenuItemLayoutHelper lh =
                new MenuItemLayoutHelper ( mi, checkIcon, arrowIcon, viewRect, defaultTextIconGap, getAccelerationDelimeter (),
                        mi.getComponentOrientation ().isLeftToRight (), mi.getFont (), acceleratorFont,
                        MenuItemLayoutHelper.useCheckAndArrow ( menuItem ), getPropertyPrefix () );
        MenuItemLayoutHelper.LayoutResult lr = lh.layoutMenuItem ();

        // Painting all parts
        paintBackground ( g, mi );
        paintCheckIcon ( g, lh );
        paintIcon ( g, lh );
        paintArrowIcon ( g, lh, lr );

        // Painting text parts
        Map hints = SwingUtils.setupTextAntialias ( g, c );
        paintText ( g, lh, lr );
        paintAccText ( g, lh, lr );
        SwingUtils.restoreTextAntialias ( g, hints );

        // Restoring original settings
        g.setColor ( holdc );
        g.setFont ( holdf );
    }

    protected void paintBackground ( Graphics g, JMenuItem menuItem )
    {
        if ( menuItem.isEnabled () )
        {
            ButtonModel model = menuItem.getModel ();
            if ( mouseover || model.isArmed () ||
                    ( menuItem instanceof JMenu && model.isSelected () ) )
            {
                LafUtils.drawWebStyle ( ( Graphics2D ) g, menuItem, StyleConstants.shadeColor, 0, StyleConstants.smallRound,
                        menuItem.isEnabled (), true, StyleConstants.averageBorderColor );

                //            if ( menuItem.isEnabled () )
                //            {
                //                LafUtils.drawWebFocus ( ( Graphics2D ) g, menuItem, StyleConstants.focusType,
                //                        StyleConstants.shadeWidth, StyleConstants.smallRound, null, true );
                //            }

                if ( menuItem.isEnabled () )
                {
                    LafUtils.drawWebFocus ( ( Graphics2D ) g, menuItem, FocusType.fieldFocus, 0, StyleConstants.smallRound, null, true,
                            StyleConstants.darkBorderColor );
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
        final boolean checkOrRadio = lh.getMenuItem () instanceof JCheckBoxMenuItem || lh.getMenuItem () instanceof JRadioButtonMenuItem;
        boolean selected = checkOrRadio && lh.getMenuItem ().getModel ().isSelected ();
        if ( lh.getMenuItem () instanceof JCheckBoxMenuItem && selected )
        {
            ImageIcon check = lh.getMenuItem ().isEnabled () ? WebCheckBoxUI.CHECK_STATES.get ( WebCheckBoxUI.CHECK_STATES.size () - 1 ) :
                    WebCheckBoxUI.DISABLED_CHECK;
            check.paintIcon ( lh.getMenuItem (), g, iconLocation.x, iconLocation.y );
        }
        else if ( lh.getMenuItem () instanceof JRadioButtonMenuItem && selected )
        {
            ImageIcon check =
                    lh.getMenuItem ().isEnabled () ? WebRadioButtonUI.CHECK_STATES.get ( WebRadioButtonUI.CHECK_STATES.size () - 1 ) :
                            WebRadioButtonUI.DISABLED_CHECK;
            check.paintIcon ( lh.getMenuItem (), g, iconLocation.x, iconLocation.y );
        }
        else if ( lh.getIcon () != null && !checkOrRadio )
        {
            //            if ( selected )
            //            {
            //                Graphics2D g2d = ( Graphics2D ) g;
            //                Object aa = g2d.getRenderingHint ( RenderingHints.KEY_ANTIALIASING );
            //                g2d.setRenderingHint ( RenderingHints.KEY_ANTIALIASING,
            //                        RenderingHints.VALUE_ANTIALIAS_ON );
            //
            //                g2d.setPaint ( new GradientPaint ( 0, 2, StyleConstants.topBgColor, 0, 24,
            //                        StyleConstants.bottomBgColor ) );
            //                g.fillRoundRect ( 2, 2, 21, 21, 4, 4 );
            //                g.setColor ( StyleConstants.darkBorderColor );
            //                g.drawRoundRect ( 2, 2, 21, 21, 4, 4 );
            //            }

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
            if ( icon != null && icon != StyleConstants.EMPTY_ICON )
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


    protected void paintText ( Graphics g, JMenuItem menuItem, Rectangle textRect, String text )
    {
        FontMetrics fm = SwingUtils.getFontMetrics ( menuItem, g );
        int mnemIndex = WebLookAndFeel.isMnemonicHidden () ? -1 : menuItem.getDisplayedMnemonicIndex ();

        if ( !menuItem.isEnabled () )
        {
            g.setColor ( UIManager.getColor ( "MenuItem.disabledForeground" ) );
        }
        else
        {
            g.setColor ( menuItem.getForeground () );
        }

        SwingUtils.drawStringUnderlineCharAt ( menuItem, g, text, mnemIndex, 32, menuItem.getHeight () / 2 + fm.getAscent () / 2 - 1 );
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
