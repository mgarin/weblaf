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

import com.alee.extended.painter.Painter;
import com.alee.laf.WebLookAndFeel;
import com.alee.utils.LafUtils;
import com.alee.utils.MathUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.PopupMenuUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicMenuItemUI;
import javax.swing.text.View;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

/**
 * Custom UI for JMenuItem component.
 *
 * @author Mikle Garin
 */

public class WebMenuItemUI extends BasicMenuItemUI
{
    /**
     * Style settings.
     */
    protected Insets margin = WebMenuItemStyle.margin;
    protected Color disabledFg = WebMenuItemStyle.disabledFg;
    protected Color topBg = WebMenuItemStyle.topBg;
    protected Color bottomBg = WebMenuItemStyle.bottomBg;
    protected Color acceleratorBg = WebMenuItemStyle.acceleratorBg;
    protected Color acceleratorFg = WebMenuItemStyle.acceleratorFg;
    protected Color acceleratorDisabledFg = WebMenuItemStyle.acceleratorDisabledFg;
    protected int iconTextGap = WebMenuItemStyle.iconTextGap;
    protected int acceleratorGap = WebMenuItemStyle.itemSidesGap;
    protected boolean alignTextToMenuIcons = WebMenuItemStyle.alignTextToMenuIcons;
    protected int iconAlignment = WebMenuItemStyle.iconAlignment;
    protected Painter painter = WebMenuItemStyle.painter;

    /**
     * Menu item listeners.
     */
    protected PropertyChangeListener propertyChangeListener;

    /**
     * Returns an instance of the WebMenuItemUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebMenuItemUI
     */
    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebMenuItemUI ();
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
        SwingUtils.setOrientation ( menuItem );
        menuItem.setOpaque ( false );
        menuItem.setIconTextGap ( iconTextGap );
        updateBorder ();

        // Orientation change listener
        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( PropertyChangeEvent evt )
            {
                updateBorder ();
            }
        };
        menuItem.addPropertyChangeListener ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY, propertyChangeListener );
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Removing listeners
        menuItem.removePropertyChangeListener ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY, propertyChangeListener );

        // Restoring basic settings
        menuItem.setOpaque ( true );

        super.uninstallUI ( c );
    }

    /**
     * Updates custom UI border.
     */
    protected void updateBorder ()
    {
        if ( menuItem != null )
        {
            // Actual margin
            final boolean ltr = menuItem.getComponentOrientation ().isLeftToRight ();
            final Insets m = new Insets ( margin.top, ltr ? margin.left : margin.right, margin.bottom, ltr ? margin.right : margin.left );

            // Calculating additional borders
            if ( painter != null )
            {
                // Painter borders
                final Insets pi = painter.getMargin ( menuItem );
                m.top += pi.top;
                m.bottom += pi.bottom;
                m.left += ltr ? pi.left : pi.right;
                m.right += ltr ? pi.right : pi.left;
            }
            else
            {
                // Retrieving popup menu rounding
                int round = 0;
                final Container parent = menuItem.getParent ();
                if ( parent != null && parent instanceof JPopupMenu )
                {
                    final PopupMenuUI ui = ( ( JPopupMenu ) parent ).getUI ();
                    if ( ui instanceof WebPopupMenuUI )
                    {
                        round = ( ( WebPopupMenuUI ) ui ).getRound ();
                    }
                }

                // Styling borders
                m.top += 5;
                m.left += ( ltr ? 4 : 7 ) + round;
                m.bottom += 5;
                m.right += ( ltr ? 7 : 4 ) + round;
            }

            // Installing border
            menuItem.setBorder ( LafUtils.createWebBorder ( m ) );
        }
    }

    /**
     * Paints menu item decoration.
     *
     * @param g graphics context
     * @param c menu item component
     */
    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        final Graphics2D g2d = ( Graphics2D ) g;
        final Object aa = LafUtils.setupAntialias ( g2d );

        // todo RTL painting

        final JMenuItem menuItem = ( JMenuItem ) c;
        final boolean ltr = menuItem.getComponentOrientation ().isLeftToRight ();
        final int w = menuItem.getWidth ();
        final int h = menuItem.getHeight ();
        final Insets bi = menuItem.getInsets ();
        final int y = bi.top;
        final int ih = h - bi.top - bi.bottom;

        // Painting background
        final ButtonModel model = menuItem.getModel ();
        final boolean selected = menuItem.isEnabled () && ( model.isArmed () || ( menuItem instanceof JMenu && model.isSelected () ) );
        paintBackground ( g2d, menuItem, selected, ltr );

        // Painting icon
        final int iconPlaceholderWidth = MenuUtils.getIconPlaceholderWidth ( menuItem, alignTextToMenuIcons );
        final int gap = iconPlaceholderWidth > 0 ? menuItem.getIconTextGap () : 0;
        int x = ltr ? bi.left : w - bi.right - iconPlaceholderWidth;
        paintIcon ( g2d, menuItem, x, y, iconPlaceholderWidth, ih, selected, ltr );
        x += ltr ? ( iconPlaceholderWidth + gap ) : -gap;

        // Painting text and accelerator
        final String text = menuItem.getText ();
        final boolean hasText = text != null && text.length () > 0;
        final String accText = MenuUtils.getAcceleratorText ( menuItem );
        final boolean hasAccelerator = accText != null;
        if ( hasText || hasAccelerator )
        {
            final Map hints = SwingUtils.setupTextAntialias ( g2d );
            final Font oldFont = LafUtils.setupFont ( g, menuItem.getFont () );

            // Painting text
            if ( hasText )
            {
                final FontMetrics fm = menuItem.getFontMetrics ( menuItem.getFont () );
                final View html = ( View ) menuItem.getClientProperty ( BasicHTML.propertyKey );
                final int tw = html != null ? ( int ) html.getPreferredSpan ( View.X_AXIS ) : fm.stringWidth ( text );

                x -= ltr ? 0 : tw;
                paintText ( g2d, menuItem, fm, x, y, tw, ih, selected, ltr );
            }

            // Painting accelerator text
            if ( hasAccelerator )
            {
                final FontMetrics afm = menuItem.getFontMetrics ( acceleratorFont );
                final int aw = afm.stringWidth ( accText );

                x = ltr ? w - bi.right - aw : bi.left;
                paintAcceleratorText ( g2d, menuItem, accText, afm, x, y, aw, ih, selected, ltr );
            }

            LafUtils.restoreFont ( g, oldFont );
            SwingUtils.restoreTextAntialias ( g2d, hints );
        }

        LafUtils.restoreAntialias ( g2d, aa );
    }

    /**
     * Paints menu item background.
     *
     * @param g2d      graphics context
     * @param menuItem menu item
     * @param selected whether menu item is selected or not
     * @param ltr      whether menu item has left-to-right orientation or not
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void paintBackground ( final Graphics2D g2d, final JMenuItem menuItem, final boolean selected, final boolean ltr )
    {
        if ( painter != null )
        {
            painter.paint ( g2d, SwingUtils.size ( menuItem ), menuItem );
        }
        else
        {
            if ( selected )
            {
                g2d.setPaint ( new GradientPaint ( 0, 0, topBg, 0, menuItem.getHeight (), bottomBg ) );
                g2d.fillRect ( 0, 0, menuItem.getWidth (), menuItem.getHeight () );
            }
        }
    }

    /**
     * Paints menu item icon.
     *
     * @param g2d      graphics context
     * @param menuItem menu item
     * @param x        icon placeholder X coordinate
     * @param y        icon placeholder Y coordinate
     * @param w        icon placeholder width
     * @param h        icon placeholder height
     * @param selected whether menu item is selected or not
     * @param ltr      whether menu item has left-to-right orientation or not
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void paintIcon ( final Graphics2D g2d, final JMenuItem menuItem, final int x, final int y, final int w, final int h,
                               final boolean selected, final boolean ltr )
    {
        final Icon icon = menuItem.isEnabled () ? menuItem.getIcon () : menuItem.getDisabledIcon ();
        if ( icon != null )
        {
            final boolean left = ltr ? ( iconAlignment == SwingConstants.LEFT || iconAlignment == SwingConstants.LEADING ) :
                    ( iconAlignment == SwingConstants.RIGHT || iconAlignment == SwingConstants.TRAILING );
            final boolean center = iconAlignment == SwingConstants.CENTER;
            final int iconX = left ? x : center ? x + w / 2 - icon.getIconWidth () / 2 : x + w - icon.getIconWidth ();
            icon.paintIcon ( menuItem, g2d, iconX, y + h / 2 - icon.getIconHeight () / 2 );
        }
    }

    /**
     * Paints menu item text.
     *
     * @param g2d      graphics context
     * @param menuItem menu item
     * @param fm       text font metrics
     * @param x        text X coordinate
     * @param y        text rectangle Y coordinate
     * @param w        text width
     * @param h        text rectangle height
     * @param selected whether menu item is selected or not
     * @param ltr      whether menu item has left-to-right orientation or not
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void paintText ( final Graphics2D g2d, final JMenuItem menuItem, final FontMetrics fm, final int x, final int y, final int w,
                               final int h, final boolean selected, final boolean ltr )
    {
        g2d.setPaint ( menuItem.isEnabled () ? menuItem.getForeground () : disabledFg );

        final View html = ( View ) menuItem.getClientProperty ( BasicHTML.propertyKey );
        if ( html != null )
        {
            html.paint ( g2d, new Rectangle ( x, y, w, h ) );
        }
        else
        {
            final int mnem = WebLookAndFeel.isMnemonicHidden () ? -1 : menuItem.getDisplayedMnemonicIndex ();
            SwingUtils.drawStringUnderlineCharAt ( g2d, menuItem.getText (), mnem, x, y + h / 2 + LafUtils.getTextCenterShearY ( fm ) );
        }
    }

    /**
     * Paints menu item accelerator text.
     *
     * @param g2d      graphics context
     * @param menuItem menu item
     * @param accText  accelerator text
     * @param fm       accelerator text font metrics
     * @param x        accelerator text X coordinate
     * @param y        accelerator text rectangle Y coordinate
     * @param w        accelerator text width
     * @param h        accelerator text rectangle height
     * @param selected whether menu item is selected or not
     * @param ltr      whether menu item has left-to-right orientation or not
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void paintAcceleratorText ( final Graphics2D g2d, final JMenuItem menuItem, final String accText, final FontMetrics fm,
                                          final int x, final int y, final int w, final int h, final boolean selected, final boolean ltr )
    {
        if ( selected && acceleratorBg != null )
        {
            final int th = fm.getHeight ();
            g2d.setPaint ( acceleratorBg );
            g2d.fillRoundRect ( x - 3, y + h / 2 - th / 2, w + 6, th, 4, 4 );
        }

        g2d.setPaint ( menuItem.isEnabled () ? acceleratorFg : acceleratorDisabledFg );
        g2d.drawString ( accText, x, y + h / 2 + LafUtils.getTextCenterShearY ( fm ) );
    }

    /**
     * Returns menu item preferred size.
     *
     * @param c menu item component
     * @return menu item preferred size
     */
    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        final JMenuItem menuItem = ( JMenuItem ) c;
        final Insets bi = menuItem.getInsets ();
        final FontMetrics fm = menuItem.getFontMetrics ( menuItem.getFont () );
        final FontMetrics afm = menuItem.getFontMetrics ( acceleratorFont );

        // Icon
        final int iconPlaceholderWidth = MenuUtils.getIconPlaceholderWidth ( menuItem, alignTextToMenuIcons );

        // Text
        final View html = ( View ) menuItem.getClientProperty ( BasicHTML.propertyKey );
        final int textWidth;
        final int textHeight;
        if ( html != null )
        {
            // Text is HTML
            textWidth = ( int ) html.getPreferredSpan ( View.X_AXIS );
            textHeight = ( int ) html.getPreferredSpan ( View.Y_AXIS );
        }
        else
        {
            // Text isn't HTML
            final String text = menuItem.getText ();
            textWidth = text != null && text.length () > 0 ? fm.stringWidth ( text ) : 0;
            textHeight = fm.getHeight ();
        }

        // Icon-Text gap
        final int gap = textWidth > 0 && iconPlaceholderWidth > 0 ? menuItem.getIconTextGap () : 0;

        // Acceleration text and its gap
        final String accelerationText = MenuUtils.getAcceleratorText ( menuItem );
        final int accWidth = accelerationText != null ? acceleratorGap + afm.stringWidth ( accelerationText ) : 0;

        // Content height
        final int iconHeight = menuItem.getIcon () != null ? menuItem.getIcon ().getIconHeight () : 0;
        final int contentHeight = MathUtils.max ( iconHeight, textHeight, afm.getHeight () );

        return new Dimension ( bi.left + iconPlaceholderWidth + gap + textWidth + accWidth + bi.right, bi.top + contentHeight + bi.bottom );
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //    @Override
    //    protected Dimension getPreferredMenuItemSize ( final JComponent c, final Icon checkIcon, final Icon arrowIcon,
    //                                                   final int defaultTextIconGap )
    //    {
    //        final JMenuItem mi = ( JMenuItem ) c;
    //        final MenuItemLayoutHelper lh =
    //                new MenuItemLayoutHelper ( mi, checkIcon, arrowIcon, MenuItemLayoutHelper.createMaxRect (), defaultTextIconGap,
    //                        getAccelerationDelimeter (), mi.getComponentOrientation ().isLeftToRight (), mi.getFont (), acceleratorFont,
    //                        MenuItemLayoutHelper.useCheckAndArrow ( menuItem ), getPropertyPrefix () );
    //
    //        final Dimension result = new Dimension ();
    //
    //        // Calculate the result width
    //        result.width = lh.getLeadingGap ();
    //        MenuItemLayoutHelper.addMaxWidth ( lh.getCheckSize (), lh.getAfterCheckIconGap (), result );
    //        // Take into account mimimal text offset.
    //        if ( ( !lh.isTopLevelMenu () ) && ( lh.getMinTextOffset () > 0 ) &&
    //                ( result.width < lh.getMinTextOffset () ) )
    //        {
    //            result.width = lh.getMinTextOffset ();
    //        }
    //        MenuItemLayoutHelper.addMaxWidth ( lh.getLabelSize (), lh.getGap (), result );
    //        MenuItemLayoutHelper.addMaxWidth ( lh.getAccSize (), lh.getGap (), result );
    //        MenuItemLayoutHelper.addMaxWidth ( lh.getArrowSize (), lh.getGap (), result );
    //
    //        // Calculate the result height
    //        result.height = MenuItemLayoutHelper
    //                .max ( lh.getCheckSize ().getHeight (), lh.getLabelSize ().getHeight (), lh.getAccSize ().getHeight (),
    //                        lh.getArrowSize ().getHeight () );
    //
    //        // Take into account menu item insets
    //        final Insets insets = lh.getMenuItem ().getInsets ();
    //        if ( insets != null )
    //        {
    //            result.width += insets.left + insets.right;
    //            result.height += insets.top + insets.bottom;
    //        }
    //
    //        return result;
    //    }
    //
    //    @Override
    //    protected void paintMenuItem ( final Graphics g, final JComponent c, final Icon checkIcon, final Icon arrowIcon, final Color background,
    //                                   final Color foreground, final int defaultTextIconGap )
    //    {
    //        // Saving original settings
    //        final Font holdf = g.getFont ();
    //        final Color holdc = g.getColor ();
    //
    //        // Setting font
    //        final JMenuItem mi = ( JMenuItem ) c;
    //        g.setFont ( mi.getFont () );
    //
    //        // creating helper class
    //        final Rectangle viewRect = new Rectangle ( 0, 0, mi.getWidth (), mi.getHeight () );
    //        applyInsets ( viewRect, mi.getInsets () );
    //        final MenuItemLayoutHelper lh =
    //                new MenuItemLayoutHelper ( mi, checkIcon, arrowIcon, viewRect, defaultTextIconGap, getAccelerationDelimeter (),
    //                        mi.getComponentOrientation ().isLeftToRight (), mi.getFont (), acceleratorFont,
    //                        MenuItemLayoutHelper.useCheckAndArrow ( menuItem ), getPropertyPrefix () );
    //        final MenuItemLayoutHelper.LayoutResult lr = lh.layoutMenuItem ();
    //
    //        // Painting all parts
    //        paintBackground ( g, mi );
    //        paintCheckIcon ( g, lh );
    //        paintIcon ( g, lh );
    //        paintArrowIcon ( g, lh, lr );
    //
    //        // Painting text parts
    //        final Map hints = SwingUtils.setupTextAntialias ( g );
    //        paintText ( g, lh, lr );
    //        paintAccText ( g, lh, lr );
    //        SwingUtils.restoreTextAntialias ( g, hints );
    //
    //        // Restoring original settings
    //        g.setColor ( holdc );
    //        g.setFont ( holdf );
    //    }
    //
    //    protected void paintBackground ( final Graphics g, final JMenuItem menuItem )
    //    {
    //        if ( menuItem.isEnabled () )
    //        {
    //            final ButtonModel model = menuItem.getModel ();
    //            if ( mouseover || model.isArmed () ||
    //                    ( menuItem instanceof JMenu && model.isSelected () ) )
    //            {
    //                Graphics2D g2d = ( Graphics2D ) g;
    //                g2d.setPaint ( new GradientPaint ( 0, 0, topBg, 0, menuItem.getHeight (), bottomBg ) );
    //                g2d.fillRect ( 0, 0, menuItem.getWidth (), menuItem.getHeight () );
    //            }
    //        }
    //    }
    //
    //    protected void applyInsets ( final Rectangle rect, final Insets insets )
    //    {
    //        if ( insets != null )
    //        {
    //            rect.x += insets.left;
    //            rect.y += insets.top;
    //            rect.width -= ( insets.right + rect.x );
    //            rect.height -= ( insets.bottom + rect.y );
    //        }
    //    }
    //
    //    protected void paintIcon ( final Graphics g, final MenuItemLayoutHelper lh )
    //    {
    //        final boolean checkOrRadio = lh.getMenuItem () instanceof JCheckBoxMenuItem || lh.getMenuItem () instanceof JRadioButtonMenuItem;
    //        final boolean selected = checkOrRadio && lh.getMenuItem ().getModel ().isSelected ();
    //        if ( lh.getMenuItem () instanceof JCheckBoxMenuItem && selected )
    //        {
    //            final List<ImageIcon> checkStates = SimpleCheckIcon.CHECK_STATES;
    //            final ImageIcon enabled = checkStates.get ( checkStates.size () - 1 );
    //            final ImageIcon disabled = SimpleCheckIcon.DISABLED_CHECK_STATES.get ( 3 );
    //            final ImageIcon check = lh.getMenuItem ().isEnabled () ? enabled : disabled;
    //            check.paintIcon ( lh.getMenuItem (), g, iconLocation.x, iconLocation.y );
    //        }
    //        else if ( lh.getMenuItem () instanceof JRadioButtonMenuItem && selected )
    //        {
    //            final List<ImageIcon> checkStates = WebRadioButtonUI.CHECK_STATES;
    //            final ImageIcon enabled = checkStates.get ( checkStates.size () - 1 );
    //            final ImageIcon disabled = WebRadioButtonUI.DISABLED_CHECK;
    //            final ImageIcon check = lh.getMenuItem ().isEnabled () ? enabled : disabled;
    //            check.paintIcon ( lh.getMenuItem (), g, iconLocation.x, iconLocation.y );
    //        }
    //        else if ( lh.getIcon () != null && !checkOrRadio )
    //        {
    //            //            if ( selected )
    //            //            {
    //            //                Graphics2D g2d = ( Graphics2D ) g;
    //            //                Object aa = g2d.getRenderingHint ( RenderingHints.KEY_ANTIALIASING );
    //            //                g2d.setRenderingHint ( RenderingHints.KEY_ANTIALIASING,
    //            //                        RenderingHints.VALUE_ANTIALIAS_ON );
    //            //
    //            //                g2d.setPaint ( new GradientPaint ( 0, 2, StyleConstants.topBgColor, 0, 24,
    //            //                        StyleConstants.bottomBgColor ) );
    //            //                g.fillRoundRect ( 2, 2, 21, 21, 4, 4 );
    //            //                g.setColor ( StyleConstants.darkBorderColor );
    //            //                g.drawRoundRect ( 2, 2, 21, 21, 4, 4 );
    //            //            }
    //
    //            Icon icon;
    //            final ButtonModel model = lh.getMenuItem ().getModel ();
    //            if ( !model.isEnabled () )
    //            {
    //                icon = lh.getMenuItem ().getDisabledIcon ();
    //            }
    //            else if ( model.isPressed () && model.isArmed () )
    //            {
    //                icon = lh.getMenuItem ().getPressedIcon ();
    //                if ( icon == null )
    //                {
    //                    icon = lh.getMenuItem ().getIcon ();
    //                }
    //            }
    //            else
    //            {
    //                icon = lh.getMenuItem ().getIcon ();
    //            }
    //            if ( icon != null && icon != StyleConstants.EMPTY_ICON )
    //            {
    //                icon.paintIcon ( lh.getMenuItem (), g, iconLocation.x, iconLocation.y );
    //            }
    //        }
    //    }
    //
    //    protected void paintCheckIcon ( final Graphics g, final MenuItemLayoutHelper lh )
    //    {
    //        if ( lh.useCheckAndArrow () && lh.getCheckIcon () != null )
    //        {
    //            lh.getCheckIcon ().paintIcon ( lh.getMenuItem (), g, iconLocation.x, iconLocation.y );
    //        }
    //    }
    //
    //    protected void paintArrowIcon ( final Graphics g, final MenuItemLayoutHelper lh, final MenuItemLayoutHelper.LayoutResult lr )
    //    {
    //        if ( lh.useCheckAndArrow () && lh.getArrowIcon () != null )
    //        {
    //            lh.getArrowIcon ().paintIcon ( lh.getMenuItem (), g, lr.getArrowRect ().x, lr.getArrowRect ().y );
    //        }
    //    }
    //
    //    protected void paintText ( final Graphics g, final MenuItemLayoutHelper lh, final MenuItemLayoutHelper.LayoutResult lr )
    //    {
    //        if ( !lh.getText ().equals ( "" ) )
    //        {
    //            final Rectangle rect = lr.getTextRect ();
    //            if ( lh.getHtmlView () != null )
    //            {
    //                // Text is HTML
    //                lh.getHtmlView ().paint ( g, rect );
    //            }
    //            else
    //            {
    //                // Text isn't HTML
    //                paintText ( g, lh.getMenuItem (), rect, lh.getText () );
    //            }
    //        }
    //    }
    //
    //
    //    @Override
    //    protected void paintText ( final Graphics g, final JMenuItem menuItem, final Rectangle textRect, final String text )
    //    {
    //        final int mnemIndex = WebLookAndFeel.isMnemonicHidden () ? -1 : menuItem.getDisplayedMnemonicIndex ();
    //        g.setColor ( menuItem.isEnabled () ? menuItem.getForeground () : UIManager.getColor ( "MenuItem.disabledForeground" ) );
    //        SwingUtils.drawStringUnderlineCharAt ( g, text, mnemIndex, 32,
    //                menuItem.getHeight () / 2 + LafUtils.getTextCenterShearY ( SwingUtils.getFontMetrics ( menuItem, g ) ) );
    //    }
    //
    //    protected void paintAccText ( final Graphics g, final MenuItemLayoutHelper lh, final MenuItemLayoutHelper.LayoutResult lr )
    //    {
    //        if ( !lh.getAccText ().equals ( "" ) )
    //        {
    //            final ButtonModel model = lh.getMenuItem ().getModel ();
    //
    //            g.setFont ( lh.getAccFontMetrics ().getFont () );
    //            g.setColor ( model.isEnabled () ? StyleConstants.infoTextColor : StyleConstants.disabledInfoTextColor );
    //
    //            final Rectangle rect = lr.getAccRect ();
    //            rect.x = lh.getMenuItem ().getWidth () - 7 - lh.getAccFontMetrics ().stringWidth ( lh.getAccText () );
    //            SwingUtils.drawString ( g, lh.getAccText (), rect.x,
    //                    lh.getMenuItem ().getHeight () / 2 + LafUtils.getTextCenterShearY ( lh.getAccFontMetrics () ) );
    //        }
    //    }
}
