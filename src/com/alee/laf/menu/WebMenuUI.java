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
import javax.swing.plaf.basic.BasicMenuUI;
import javax.swing.text.View;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

/**
 * User: mgarin Date: 15.08.11 Time: 19:47
 */

public class WebMenuUI extends BasicMenuUI
{
    /**
     * Style settings.
     */
    protected Insets margin = WebMenuItemStyle.margin;
    protected Color disabledFg = WebMenuItemStyle.disabledFg;
    protected Color topBg = WebMenuItemStyle.topBg;
    protected Color bottomBg = WebMenuItemStyle.bottomBg;
    protected int iconTextGap = WebMenuItemStyle.iconTextGap;
    protected int arrowGap = WebMenuItemStyle.itemSidesGap;
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
        return new WebMenuUI ();
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

        // Painting text
        final String text = menuItem.getText ();
        if ( text != null && text.length () > 0 )
        {
            final Map hints = SwingUtils.setupTextAntialias ( g2d );
            final Font oldFont = LafUtils.setupFont ( g, menuItem.getFont () );

            final FontMetrics fm = menuItem.getFontMetrics ( menuItem.getFont () );
            final View html = ( View ) menuItem.getClientProperty ( BasicHTML.propertyKey );
            final int tw = html != null ? ( int ) html.getPreferredSpan ( View.X_AXIS ) : fm.stringWidth ( text );

            x -= ltr ? 0 : tw;
            paintText ( g2d, menuItem, fm, x, y, tw, ih, selected, ltr );

            LafUtils.restoreFont ( g, oldFont );
            SwingUtils.restoreTextAntialias ( g2d, hints );
        }

        // Painting sub-menu arrow icon
        final Icon arrowIcon = getArrowIcon ( menuItem );
        if ( arrowIcon != null )
        {
            final Composite oc = LafUtils.setupAlphaComposite ( g2d, 0.5f, selected );
            arrowIcon.paintIcon ( menuItem, g2d, ltr ? w - bi.right - arrowIcon.getIconWidth () : bi.left,
                    y + ih / 2 - arrowIcon.getIconHeight () / 2 );
            LafUtils.restoreComposite ( g2d, oc, selected );
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
     * Returns arrow icon displayed when sub-menu is available.
     *
     * @return arrow icon displayed when sub-menu is available
     */
    protected Icon getArrowIcon ( final JMenuItem menuItem )
    {
        return menuItem.getParent () instanceof JPopupMenu ? UIManager.getIcon ( getPropertyPrefix () + ".arrowIcon" ) : null;
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

        // Sub-menu arrow icon
        final Icon subMenuArrowIcon = getArrowIcon ( menuItem );
        final int arrowWidth = subMenuArrowIcon != null ? arrowGap + subMenuArrowIcon.getIconWidth () : 0;

        // Content height
        final int iconHeight = menuItem.getIcon () != null ? menuItem.getIcon ().getIconHeight () : 0;
        final int contentHeight = MathUtils.max ( iconHeight, textHeight, afm.getHeight () );

        return new Dimension ( bi.left + iconPlaceholderWidth + gap + textWidth + arrowWidth + bi.right,
                bi.top + contentHeight + bi.bottom );
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

    //    private static final Point iconLocation = new Point ( 6, 5 );
    //
    //    private boolean mouseover = false;
    //
    //    private MouseAdapter mouseAdapter = null;
    //
    //    @SuppressWarnings ("UnusedParameters")
    //    public static ComponentUI createUI ( JComponent c )
    //    {
    //        return new WebMenuUI ();
    //    }
    //
    //    @Override
    //    public void installUI ( JComponent c )
    //    {
    //        super.installUI ( c );
    //
    //        // Default settings
    //        SwingUtils.setOrientation ( menuItem );
    //        menuItem.setOpaque ( false );
    //        menuItem.setBorder ( BorderFactory.createEmptyBorder ( 5, 2, 5, 2 ) );
    //        menuItem.setBackground ( StyleConstants.menuSelectionColor );
    //
    //        // Rollover listener
    //        mouseAdapter = new MouseAdapter ()
    //        {
    //            @Override
    //            public void mouseEntered ( MouseEvent e )
    //            {
    //                mouseover = true;
    //                menuItem.repaint ();
    //            }
    //
    //            @Override
    //            public void mouseExited ( MouseEvent e )
    //            {
    //                mouseover = false;
    //                menuItem.repaint ();
    //            }
    //        };
    //        menuItem.addMouseListener ( mouseAdapter );
    //    }
    //
    //    @Override
    //    public void uninstallUI ( JComponent c )
    //    {
    //        menuItem.removeMouseListener ( mouseAdapter );
    //
    //        super.uninstallUI ( c );
    //    }
    //
    //    @Override
    //    protected Dimension getPreferredMenuItemSize ( JComponent c, Icon checkIcon, Icon arrowIcon, int defaultTextIconGap )
    //    {
    //        JMenuItem mi = ( JMenuItem ) c;
    //        MenuItemLayoutHelper lh =
    //                new MenuItemLayoutHelper ( mi, checkIcon, arrowIcon, MenuItemLayoutHelper.createMaxRect (), defaultTextIconGap,
    //                        getAccelerationDelimeter (), mi.getComponentOrientation ().isLeftToRight (), mi.getFont (), acceleratorFont,
    //                        MenuItemLayoutHelper.useCheckAndArrow ( menuItem ), getPropertyPrefix () );
    //
    //        Dimension result = new Dimension ();
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
    //        Insets insets = lh.getMenuItem ().getInsets ();
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
    //    protected void paintMenuItem ( Graphics g, JComponent c, Icon checkIcon, Icon arrowIcon, Color background, Color foreground,
    //                                   int defaultTextIconGap )
    //    {
    //        // Saving original paint settings
    //        Font holdf = g.getFont ();
    //        Color holdc = g.getColor ();
    //
    //        // Setting font
    //        JMenuItem mi = ( JMenuItem ) c;
    //        g.setFont ( mi.getFont () );
    //
    //        // Creating helper class
    //        Rectangle viewRect = new Rectangle ( 0, 0, mi.getWidth (), mi.getHeight () );
    //        applyInsets ( viewRect, mi.getInsets () );
    //        MenuItemLayoutHelper lh =
    //                new MenuItemLayoutHelper ( mi, checkIcon, arrowIcon, viewRect, defaultTextIconGap, getAccelerationDelimeter (),
    //                        mi.getComponentOrientation ().isLeftToRight (), mi.getFont (), acceleratorFont,
    //                        MenuItemLayoutHelper.useCheckAndArrow ( menuItem ), getPropertyPrefix () );
    //        MenuItemLayoutHelper.LayoutResult lr = lh.layoutMenuItem ();
    //
    //        // Painting menu parts
    //        paintBackground ( g, mi, lh );
    //        paintCheckIcon ( g, lh );
    //        paintIcon ( g, lh );
    //        paintArrowIcon ( g, lh, lr );
    //
    //        // Painting text parts
    //        Map hints = SwingUtils.setupTextAntialias ( g );
    //        paintText ( g, lh, lr );
    //        paintAccText ( g, lh, lr );
    //        SwingUtils.restoreTextAntialias ( g, hints );
    //
    //        // Retoring original settings
    //        g.setColor ( holdc );
    //        g.setFont ( holdf );
    //    }
    //
    //    protected void paintBackground ( Graphics g, JMenuItem menuItem, MenuItemLayoutHelper lh )
    //    {
    //        if ( menuItem.isEnabled () )
    //        {
    //            ButtonModel model = menuItem.getModel ();
    //            if ( mouseover || model.isArmed () || model.isSelected () )
    //            {
    //                boolean onPopup = lh.getMenuItemParent () instanceof JPopupMenu;
    //                int shadeWidth = onPopup ? 0 : StyleConstants.shadeWidth;
    //                LafUtils.drawWebStyle ( ( Graphics2D ) g, menuItem, StyleConstants.shadeColor, shadeWidth, StyleConstants.smallRound,
    //                        menuItem.isEnabled (), !model.isSelected () || onPopup,
    //                        model.isSelected () ? StyleConstants.averageBorderColor : StyleConstants.borderColor );
    //
    //                //            if ( menuItem.isEnabled () )
    //                //            {
    //                //                if ( model.isSelected () )
    //                //                {
    //                //                    LafUtils.drawWebFocus ( g2d, menuItem, StyleConstants.focusType,
    //                //                            StyleConstants.shadeWidth, StyleConstants.smallRound, null, true );
    //                //                }
    //                //                else
    //                //                {
    //                //                    LafUtils.drawWebFocus ( g2d, menuItem, StyleConstants.focusType,
    //                //                            StyleConstants.shadeWidth, StyleConstants.smallRound, null, true,
    //                //                            StyleConstants.rolloverMenuBorderColor );
    //                //                }
    //                //            }
    //
    //                if ( menuItem.isEnabled () && onPopup )
    //                {
    //                    LafUtils.drawWebFocus ( ( Graphics2D ) g, menuItem, FocusType.fieldFocus, shadeWidth, StyleConstants.smallRound, null,
    //                            true, StyleConstants.darkBorderColor );
    //                }
    //            }
    //        }
    //    }
    //
    //    protected void applyInsets ( Rectangle rect, Insets insets )
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
    //    protected String getAccelerationDelimeter ()
    //    {
    //        String delim = UIManager.getString ( "MenuItem.acceleratorDelimiter" );
    //        return delim != null ? delim : "+";
    //    }
    //
    //    protected void paintIcon ( Graphics g, MenuItemLayoutHelper lh )
    //    {
    //        if ( lh.getIcon () != null )
    //        {
    //            Icon icon;
    //            ButtonModel model = lh.getMenuItem ().getModel ();
    //            if ( !model.isEnabled () )
    //            {
    //                icon = lh.getMenuItem ().getDisabledIcon ();
    //            }
    //            else if ( model.isPressed () && model.isArmed () )
    //            {
    //                icon = lh.getMenuItem ().getPressedIcon ();
    //                if ( icon == null )
    //                {
    //                    // Use default icon
    //                    icon = lh.getMenuItem ().getIcon ();
    //                }
    //            }
    //            else
    //            {
    //                icon = lh.getMenuItem ().getIcon ();
    //            }
    //
    //            if ( icon != null )
    //            {
    //                icon.paintIcon ( lh.getMenuItem (), g, iconLocation.x, iconLocation.y );
    //            }
    //        }
    //    }
    //
    //    protected void paintCheckIcon ( Graphics g, MenuItemLayoutHelper lh )
    //    {
    //        if ( lh.useCheckAndArrow () && lh.getCheckIcon () != null )
    //        {
    //            lh.getCheckIcon ().paintIcon ( lh.getMenuItem (), g, iconLocation.x, iconLocation.y );
    //        }
    //    }
    //
    //    protected void paintArrowIcon ( Graphics g, MenuItemLayoutHelper lh, MenuItemLayoutHelper.LayoutResult lr )
    //    {
    //        if ( lh.useCheckAndArrow () && lh.getArrowIcon () != null )
    //        {
    //            lh.getArrowIcon ().paintIcon ( lh.getMenuItem (), g, lr.getArrowRect ().x, lr.getArrowRect ().y );
    //        }
    //    }
    //
    //    protected void paintText ( Graphics g, MenuItemLayoutHelper lh, MenuItemLayoutHelper.LayoutResult lr )
    //    {
    //        if ( !lh.getText ().equals ( "" ) )
    //        {
    //            Rectangle rect = lr.getTextRect ();
    //
    //            Font font = menuItem.getFont ();
    //            FontRenderContext renderContext = menuItem.getFontMetrics ( font ).getFontRenderContext ();
    //            GlyphVector glyphVector = font.createGlyphVector ( renderContext, menuItem.getText () );
    //            Rectangle visualBounds = glyphVector.getVisualBounds ().getBounds ();
    //            rect.y = menuItem.getHeight () / 2 - visualBounds.height / 2;
    //            rect.height = visualBounds.height;
    //
    //            if ( lh.getMenuItemParent () instanceof JPopupMenu )
    //            {
    //                rect.x -= lh.getMenuItem ().getIconTextGap () * 2 - 1;
    //            }
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
    //    @Override
    //    protected void paintText ( Graphics g, JMenuItem menuItem, Rectangle rect, String text )
    //    {
    //        // todo Proper placement with RTL orientation
    //        final int mnemIndex = WebLookAndFeel.isMnemonicHidden () ? -1 : menuItem.getDisplayedMnemonicIndex ();
    //        g.setColor ( menuItem.isEnabled () ? menuItem.getForeground () : UIManager.getColor ( "MenuItem.disabledForeground" ) );
    //        SwingUtils.drawStringUnderlineCharAt ( g, text, mnemIndex, rect.x, rect.y + rect.height );
    //    }
    //
    //    protected void paintAccText ( Graphics g, MenuItemLayoutHelper lh, MenuItemLayoutHelper.LayoutResult lr )
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
