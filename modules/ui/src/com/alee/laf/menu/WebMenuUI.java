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
import com.alee.extended.painter.PainterSupport;
import com.alee.global.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.utils.*;
import com.alee.utils.swing.BorderMethods;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicMenuUI;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

/**
 * Custom UI for JMenu component.
 *
 * @author Mikle Garin
 */

public class WebMenuUI extends BasicMenuUI implements BorderMethods
{
    /**
     * Used icons.
     */
    public static final ImageIcon arrowRightIcon = new ImageIcon ( WebMenuUI.class.getResource ( "icons/arrowRight.png" ) );
    public static final ImageIcon arrowLeftIcon = new ImageIcon ( WebMenuUI.class.getResource ( "icons/arrowLeft.png" ) );

    /**
     * Style settings.
     */
    protected int round = WebMenuItemStyle.round;
    protected int shadeWidth = WebMenuItemStyle.shadeWidth;
    protected Insets margin = WebMenuItemStyle.margin;
    protected int sideSpacing = WebMenuItemStyle.sideSpacing;
    protected Color disabledFg = WebMenuItemStyle.disabledFg;
    protected Color selectedTopBg = WebMenuItemStyle.selectedTopBg;
    protected Color selectedBottomBg = WebMenuItemStyle.selectedBottomBg;
    protected int arrowGap = WebMenuItemStyle.itemSidesGap;
    protected boolean alignTextToMenuIcons = WebMenuItemStyle.alignTextToMenuIcons;
    protected int iconAlignment = WebMenuItemStyle.iconAlignment;
    protected Painter painter = WebMenuItemStyle.painter;

    /**
     * Menu listeners.
     */
    protected PropertyChangeListener propertyChangeListener;
    protected MouseAdapter mouseAdapter;
    protected MenuItemChangeListener buttonModelChangeListener;

    /**
     * Runtime variables.
     */
    protected boolean mouseover = false;

    /**
     * Returns an instance of the WebMenuUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebMenuUI
     */
    @SuppressWarnings ("UnusedParameters")
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
        LookAndFeel.installProperty ( menuItem, WebLookAndFeel.OPAQUE_PROPERTY, Boolean.FALSE );
        menuItem.setBackground ( selectedBottomBg );
        menuItem.setIconTextGap ( WebMenuItemStyle.iconTextGap );
        PainterSupport.installPainter ( menuItem, this.painter );
        updateBorder ();

        // Orientation change listener
        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updateBorder ();
            }
        };
        menuItem.addPropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, propertyChangeListener );

        // Rollover listener
        mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mouseEntered ( final MouseEvent e )
            {
                mouseover = true;
                menuItem.repaint ();
            }

            @Override
            public void mouseExited ( final MouseEvent e )
            {
                mouseover = false;
                menuItem.repaint ();
            }
        };
        menuItem.addMouseListener ( mouseAdapter );

        // Button model change listener
        buttonModelChangeListener = MenuItemChangeListener.install ( menuItem );
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling painter
        PainterSupport.uninstallPainter ( menuItem, this.painter );

        // Removing listeners
        menuItem.removePropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, propertyChangeListener );
        propertyChangeListener = null;
        menuItem.removeMouseListener ( mouseAdapter );
        mouseAdapter = null;
        MenuItemChangeListener.uninstall ( buttonModelChangeListener, menuItem );
        buttonModelChangeListener = null;

        super.uninstallUI ( c );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBorder ()
    {
        if ( menuItem != null )
        {
            // Preserve old borders
            if ( SwingUtils.isPreserveBorders ( menuItem ) )
            {
                return;
            }

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
                // Styling borders
                m.top += 5;
                m.left += ( ltr ? 4 : 7 ) + sideSpacing;
                m.bottom += 5;
                m.right += ( ltr ? 7 : 4 ) + sideSpacing;
            }

            // Installing border
            menuItem.setBorder ( LafUtils.createWebBorder ( m ) );
        }
    }

    /**
     * Returns top-level menu border rounding.
     *
     * @return top-level menu border rounding
     */
    public int getRound ()
    {
        return round;
    }

    /**
     * Sets top-level menu border rounding.
     *
     * @param round new top-level menu border rounding
     */
    public void setRound ( final int round )
    {
        this.round = round;
    }

    /**
     * Returns top-level menu shade width.
     *
     * @return top-level menu shade width
     */
    public int getShadeWidth ()
    {
        return shadeWidth;
    }

    /**
     * Sets top-level menu shade width.
     *
     * @param shadeWidth new top-level menu shade width
     */
    public void setShadeWidth ( final int shadeWidth )
    {
        this.shadeWidth = shadeWidth;
    }

    /**
     * Returns menu item margin.
     *
     * @return menu item margin
     */
    public Insets getMargin ()
    {
        return margin;
    }

    /**
     * Sets menu item margin.
     *
     * @param margin new menu item margin
     */
    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        updateBorder ();
    }

    /**
     * Returns spacing between menu item content and its left/right borders.
     *
     * @return spacing between menu item content and its left/right borders
     */
    public int getSideSpacing ()
    {
        return sideSpacing;
    }

    /**
     * Sets spacing between menu item content and its left/right borders
     *
     * @param sideSpacing spacing between menu item content and its left/right borders
     */
    public void setSideSpacing ( final int sideSpacing )
    {
        this.sideSpacing = sideSpacing;
        updateBorder ();
    }

    /**
     * Returns disabled menu item foreground.
     *
     * @return disabled menu item foreground
     */
    public Color getDisabledFg ()
    {
        return disabledFg;
    }

    /**
     * Sets disabled menu item foreground.
     *
     * @param foreground new disabled menu item foreground
     */
    public void setDisabledFg ( final Color foreground )
    {
        this.disabledFg = foreground;
    }

    /**
     * Returns top background color for selected item.
     *
     * @return top background color for selected item
     */
    public Color getSelectedTopBg ()
    {
        return selectedTopBg;
    }

    /**
     * Sets top background color for selected item.
     *
     * @param background new top background color for selected item
     */
    public void setSelectedTopBg ( final Color background )
    {
        this.selectedTopBg = background;
    }

    /**
     * Returns bottom background color for selected item.
     *
     * @return bottom background color for selected item
     */
    public Color getSelectedBottomBg ()
    {
        return selectedBottomBg;
    }

    /**
     * Sets bottom background color for selected item.
     *
     * @param background new bottom background color for selected item
     */
    public void setSelectedBottomBg ( final Color background )
    {
        this.selectedBottomBg = background;
    }

    /**
     * Returns gap between menu icon/text and submenu arrow.
     *
     * @return gap between menu icon/text and submenu arrow
     */
    public int getArrowGap ()
    {
        return arrowGap;
    }

    /**
     * Sets gap between menu icon/text and submenu arrow.
     *
     * @param gap new gap between menu icon/text and submenu arrow
     */
    public void setArrowGap ( final int gap )
    {
        this.arrowGap = gap;
    }

    /**
     * Returns whether should align all item texts to a single vertical line within single popup menu or not.
     *
     * @return true if should align all item texts to a single vertical line within single popup menu, false otherwise
     */
    public boolean isAlignTextToMenuIcons ()
    {
        return alignTextToMenuIcons;
    }

    /**
     * Sets whether should align all item texts to a single vertical line within single popup menu or not.
     *
     * @param align whether should align all item texts to a single vertical line within single popup menu or not
     */
    public void setAlignTextToMenuIcons ( final boolean align )
    {
        this.alignTextToMenuIcons = align;
    }

    /**
     * Returns icon alignment.
     *
     * @return icon alignment
     */
    public int getIconAlignment ()
    {
        return iconAlignment;
    }

    /**
     * Sets icon alignment
     *
     * @param alignment new icon alignment
     */
    public void setIconAlignment ( final int alignment )
    {
        this.iconAlignment = alignment;
    }

    /**
     * Returns menu item painter.
     *
     * @return menu item painter
     */
    public Painter getPainter ()
    {
        return painter;
    }

    /**
     * Sets menu item painter.
     *
     * @param painter new menu item painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.uninstallPainter ( menuItem, this.painter );

        this.painter = painter;
        PainterSupport.installPainter ( menuItem, this.painter );
        updateBorder ();
    }

    /**
     * Returns paint used to fill north popup menu corner when this component is first in the menu.
     *
     * @return paint used to fill north popup menu corner when this component is first in the menu
     */
    public Paint getNorthCornerFill ()
    {
        return selectedTopBg;
    }

    /**
     * Returns paint used to fill south popup menu corner when this component is last in the menu.
     *
     * @return paint used to fill south popup menu corner when this component is last in the menu
     */
    public Paint getSouthCornerFill ()
    {
        return selectedBottomBg;
    }

    /**
     * Paints menu decoration.
     *
     * @param g graphics context
     * @param c menu component
     */
    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        final Graphics2D g2d = ( Graphics2D ) g;
        final Object aa = GraphicsUtils.setupAntialias ( g2d );

        final JMenu menu = ( JMenu ) c;
        final boolean ltr = menu.getComponentOrientation ().isLeftToRight ();
        final int w = menu.getWidth ();
        final int h = menu.getHeight ();
        final Insets bi = menu.getInsets ();
        final int y = bi.top;
        final int ih = h - bi.top - bi.bottom;

        // Painting background
        final ButtonModel model = menu.getModel ();
        final boolean selected = menu.isEnabled () && ( model.isArmed () || model.isSelected () );
        paintBackground ( g2d, menu, selected, ltr );

        // Painting icon
        final int iconPlaceholderWidth = MenuUtils.getIconPlaceholderWidth ( menu, alignTextToMenuIcons );
        final int gap = iconPlaceholderWidth > 0 ? menu.getIconTextGap () : 0;
        int x = ltr ? bi.left : w - bi.right - iconPlaceholderWidth;
        paintIcon ( g2d, menu, x, y, iconPlaceholderWidth, ih, selected, ltr );
        x += ltr ? ( iconPlaceholderWidth + gap ) : -gap;

        // Painting text
        final String text = menu.getText ();
        if ( text != null && text.length () > 0 )
        {
            final Map hints = SwingUtils.setupTextAntialias ( g2d );
            final Font oldFont = GraphicsUtils.setupFont ( g, menu.getFont () );

            final FontMetrics fm = menu.getFontMetrics ( menu.getFont () );
            final View html = ( View ) menu.getClientProperty ( BasicHTML.propertyKey );
            final int tw = html != null ? ( int ) html.getPreferredSpan ( View.X_AXIS ) : fm.stringWidth ( text );

            x -= ltr ? 0 : tw;
            paintText ( g2d, menu, fm, x, y, tw, ih, selected, ltr );

            GraphicsUtils.restoreFont ( g, oldFont );
            SwingUtils.restoreTextAntialias ( g2d, hints );
        }

        // Painting sub-menu arrow icon
        final Icon arrowIcon = getArrowIcon ( menu );
        if ( arrowIcon != null )
        {
            final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, 0.4f, selected );
            arrowIcon.paintIcon ( menu, g2d, ltr ? w - bi.right - arrowIcon.getIconWidth () : bi.left,
                    y + ih / 2 - arrowIcon.getIconHeight () / 2 );
            GraphicsUtils.restoreComposite ( g2d, oc, selected );
        }

        GraphicsUtils.restoreAntialias ( g2d, aa );
    }

    /**
     * Paints menu item background.
     *
     * @param g2d      graphics context
     * @param menu     menu
     * @param selected whether menu item is selected or not
     * @param ltr      whether menu item has left-to-right orientation or not
     */
    @SuppressWarnings ("UnusedParameters")
    protected void paintBackground ( final Graphics2D g2d, final JMenu menu, final boolean selected, final boolean ltr )
    {
        if ( painter != null )
        {
            painter.paint ( g2d, SwingUtils.size ( menu ), menu );
        }
        else
        {
            if ( menu.getParent () instanceof JPopupMenu )
            {
                if ( selected )
                {
                    g2d.setPaint ( new GradientPaint ( 0, 0, selectedTopBg, 0, menu.getHeight (), selectedBottomBg ) );
                    g2d.fillRect ( 0, 0, menu.getWidth (), menu.getHeight () );
                }
            }
            else
            {
                if ( menu.isEnabled () && ( selected || mouseover ) )
                {
                    LafUtils.drawWebStyle ( g2d, menu, StyleConstants.shadeColor, shadeWidth, round, menu.isEnabled (),
                            !selected && mouseover, selected ? StyleConstants.averageBorderColor : StyleConstants.borderColor );
                }
            }
        }
    }

    /**
     * Paints menu item icon.
     *
     * @param g2d      graphics context
     * @param menu     menu
     * @param x        icon placeholder X coordinate
     * @param y        icon placeholder Y coordinate
     * @param w        icon placeholder width
     * @param h        icon placeholder height
     * @param selected whether menu item is selected or not
     * @param ltr      whether menu item has left-to-right orientation or not
     */
    @SuppressWarnings ("UnusedParameters")
    protected void paintIcon ( final Graphics2D g2d, final JMenu menu, final int x, final int y, final int w, final int h,
                               final boolean selected, final boolean ltr )
    {
        final Icon icon = menu.isEnabled () ? menu.getIcon () : menu.getDisabledIcon ();
        if ( icon != null )
        {
            final boolean left = ltr ? ( iconAlignment == SwingConstants.LEFT || iconAlignment == SwingConstants.LEADING ) :
                    ( iconAlignment == SwingConstants.RIGHT || iconAlignment == SwingConstants.TRAILING );
            final boolean center = iconAlignment == SwingConstants.CENTER;
            final int iconX = left ? x : center ? x + w / 2 - icon.getIconWidth () / 2 : x + w - icon.getIconWidth ();
            icon.paintIcon ( menu, g2d, iconX, y + h / 2 - icon.getIconHeight () / 2 );
        }
    }

    /**
     * Paints menu item text.
     *
     * @param g2d      graphics context
     * @param menu     menu
     * @param fm       text font metrics
     * @param x        text X coordinate
     * @param y        text rectangle Y coordinate
     * @param w        text width
     * @param h        text rectangle height
     * @param selected whether menu item is selected or not
     * @param ltr      whether menu item has left-to-right orientation or not
     */
    @SuppressWarnings ("UnusedParameters")
    protected void paintText ( final Graphics2D g2d, final JMenu menu, final FontMetrics fm, final int x, final int y, final int w,
                               final int h, final boolean selected, final boolean ltr )
    {
        g2d.setPaint ( menu.isEnabled () ? menu.getForeground () : disabledFg );

        final View html = ( View ) menu.getClientProperty ( BasicHTML.propertyKey );
        if ( html != null )
        {
            html.paint ( g2d, new Rectangle ( x, y, w, h ) );
        }
        else
        {
            final int mnem = WebLookAndFeel.isMnemonicHidden () ? -1 : menu.getDisplayedMnemonicIndex ();
            SwingUtils.drawStringUnderlineCharAt ( g2d, menu.getText (), mnem, x, y + h / 2 + LafUtils.getTextCenterShearY ( fm ) );
        }
    }

    /**
     * Returns arrow icon displayed when sub-menu is available.
     *
     * @return arrow icon displayed when sub-menu is available
     */
    protected Icon getArrowIcon ( final JMenu menu )
    {
        if ( menu.getParent () instanceof JPopupMenu )
        {
            final boolean ltr = menu.getComponentOrientation ().isLeftToRight ();
            if ( menu.isEnabled () )
            {
                return ltr ? arrowRightIcon : arrowLeftIcon;
            }
            else
            {
                return ltr ? ImageUtils.getDisabledCopy ( "Menu.arrowRightIcon", arrowRightIcon ) :
                        ImageUtils.getDisabledCopy ( "Menu.arrowLeftIcon", arrowLeftIcon );
            }
        }
        else
        {
            return null;
        }
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
        final JMenu menu = ( JMenu ) c;
        final Insets bi = menu.getInsets ();
        final FontMetrics fm = menu.getFontMetrics ( menu.getFont () );
        final FontMetrics afm = menu.getFontMetrics ( acceleratorFont );

        // Icon
        final int iconPlaceholderWidth = MenuUtils.getIconPlaceholderWidth ( menu, alignTextToMenuIcons );

        // Text
        final View html = ( View ) menu.getClientProperty ( BasicHTML.propertyKey );
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
            final String text = menu.getText ();
            textWidth = text != null && text.length () > 0 ? fm.stringWidth ( text ) : 0;
            textHeight = fm.getHeight ();
        }

        // Icon-Text gap
        final int gap = textWidth > 0 && iconPlaceholderWidth > 0 ? menu.getIconTextGap () : 0;

        // Sub-menu arrow icon
        final Icon subMenuArrowIcon = getArrowIcon ( menu );
        final int arrowWidth = subMenuArrowIcon != null ? arrowGap + subMenuArrowIcon.getIconWidth () : 0;

        // Content height
        final int iconHeight = menu.getIcon () != null ? menu.getIcon ().getIconHeight () : 0;
        final int contentHeight = MathUtils.max ( iconHeight, textHeight, afm.getHeight () );

        return new Dimension ( bi.left + iconPlaceholderWidth + gap + textWidth + arrowWidth + bi.right,
                bi.top + contentHeight + bi.bottom );
    }
}