package com.alee.laf.menu;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.menu.IAbstractMenuItemPainter;
import com.alee.laf.menu.MenuCornerSupport;
import com.alee.laf.menu.MenuItemChangeListener;
import com.alee.laf.menu.MenuUtils;
import com.alee.painter.AbstractPainter;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.MathUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicMenuItemUI;
import javax.swing.text.View;
import java.awt.*;
import java.util.Map;

/**
 * @author Alexandr Zernov
 */

public abstract class AbstractMenuItemPainter<E extends JMenuItem, U extends BasicMenuItemUI> extends AbstractPainter<E, U>
        implements IAbstractMenuItemPainter<E, U>, MenuCornerSupport
{
    /**
     * Style settings.
     */
    protected int acceleratorGap = 15;
    protected int iconAlignment = SwingConstants.CENTER;
    protected Color disabledFg = Color.LIGHT_GRAY;
    protected Color selectedTopBg = new Color ( 208, 208, 198 );
    protected Color selectedBottomBg = new Color ( 196, 196, 186 );
    protected Color acceleratorBg = new Color ( 255, 255, 255, 200 );
    protected Color acceleratorFg = new Color ( 90, 90, 90 );
    protected Color acceleratorDisabledFg = new Color ( 170, 170, 170 );
    protected boolean alignTextToMenuIcons = true;

    /**
     * Listeners.
     */
    protected MenuItemChangeListener buttonModelChangeListener;

    /**
     * Runtime variables.
     */
    protected Font acceleratorFont = null;

    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Setting accelerator font.
        setAcceleratorFont ();

        // Button model change listener
        buttonModelChangeListener = MenuItemChangeListener.install ( component );
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Removing listeners
        MenuItemChangeListener.uninstall ( buttonModelChangeListener, component );
        buttonModelChangeListener = null;

        super.uninstall ( c, ui );
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        final Object aa = GraphicsUtils.setupAntialias ( g2d );

        final int w = component.getWidth ();
        final int h = component.getHeight ();
        final Insets bi = component.getInsets ();
        final int y = bi.top;
        final int ih = h - bi.top - bi.bottom;

        // Painting background
        final ButtonModel model = component.getModel ();
        final boolean selected = component.isEnabled () && ( model.isArmed () || model.isSelected () );
        paintBackground ( g2d, selected );

        // Painting icon
        final int iconPlaceholderWidth = MenuUtils.getIconPlaceholderWidth ( component, alignTextToMenuIcons );
        final int gap = iconPlaceholderWidth > 0 ? component.getIconTextGap () : 0;
        int x = ltr ? bi.left : w - bi.right - iconPlaceholderWidth;
        paintIcon ( g2d, x, y, iconPlaceholderWidth, ih, selected );
        x += ltr ? iconPlaceholderWidth + gap : -gap;

        // Painting text and accelerator
        final String text = component.getText ();
        final boolean hasText = text != null && text.length () > 0;
        final String accText = MenuUtils.getAcceleratorText ( component );
        final boolean hasAccelerator = accText != null;
        if ( hasText || hasAccelerator )
        {
            final Map hints = SwingUtils.setupTextAntialias ( g2d );
            if ( hasText )
            {
                // Painting text
                final FontMetrics fm = component.getFontMetrics ( component.getFont () );
                final View html = ( View ) component.getClientProperty ( BasicHTML.propertyKey );
                final int tw = html != null ? ( int ) html.getPreferredSpan ( View.X_AXIS ) : fm.stringWidth ( text );
                x -= ltr ? 0 : tw;
                paintText ( g2d, fm, x, y, tw, ih, selected );
            }
            if ( hasAccelerator )
            {
                // Painting accelerator text
                final FontMetrics afm = component.getFontMetrics ( acceleratorFont );
                final int aw = afm.stringWidth ( accText );
                x = ltr ? w - bi.right - aw : bi.left;
                paintAcceleratorText ( g2d, accText, afm, x, y, aw, ih, selected );
            }
            SwingUtils.restoreTextAntialias ( g2d, hints );
        }

        GraphicsUtils.restoreAntialias ( g2d, aa );
    }

    /**
     * Paints menu item background.
     *
     * @param g2d      graphics context
     * @param selected whether menu item is selected or not
     */
    protected void paintBackground ( final Graphics2D g2d, final boolean selected )
    {
        if ( selected )
        {
            g2d.setPaint ( new GradientPaint ( 0, 0, selectedTopBg, 0, component.getHeight (), selectedBottomBg ) );
            g2d.fillRect ( 0, 0, component.getWidth (), component.getHeight () );
        }
    }

    /**
     * Paints menu item icon.
     *
     * @param g2d      graphics context
     * @param x        icon placeholder X coordinate
     * @param y        icon placeholder Y coordinate
     * @param w        icon placeholder width
     * @param h        icon placeholder height
     * @param selected whether menu item is selected or not
     */
    protected void paintIcon ( final Graphics2D g2d, final int x, final int y, final int w, final int h, final boolean selected )
    {
        final boolean enabled = component.isEnabled ();
        final Icon icon = component.isSelected () && component.getSelectedIcon () != null ?
                enabled ? component.getSelectedIcon () : component.getDisabledSelectedIcon () :
                enabled ? component.getIcon () : component.getDisabledIcon ();
        if ( icon != null )
        {
            final boolean left = ltr ? iconAlignment == SwingConstants.LEFT || iconAlignment == SwingConstants.LEADING :
                    iconAlignment == SwingConstants.RIGHT || iconAlignment == SwingConstants.TRAILING;
            final boolean center = iconAlignment == SwingConstants.CENTER;
            final int iconX = left ? x : center ? x + w / 2 - icon.getIconWidth () / 2 : x + w - icon.getIconWidth ();
            icon.paintIcon ( component, g2d, iconX, y + h / 2 - icon.getIconHeight () / 2 );
        }
    }

    /**
     * Paints menu item text.
     *
     * @param g2d      graphics context
     * @param fm       text font metrics
     * @param x        text X coordinate
     * @param y        text rectangle Y coordinate
     * @param w        text width
     * @param h        text rectangle height
     * @param selected whether menu item is selected or not
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void paintText ( final Graphics2D g2d, final FontMetrics fm, final int x, final int y, final int w, final int h,
                               final boolean selected )
    {
        g2d.setPaint ( component.isEnabled () ? component.getForeground () : disabledFg );

        final Font oldFont = GraphicsUtils.setupFont ( g2d, component.getFont () );
        final View html = ( View ) component.getClientProperty ( BasicHTML.propertyKey );
        if ( html != null )
        {
            html.paint ( g2d, new Rectangle ( x, y, w, h ) );
        }
        else
        {
            final int mnemonic = WebLookAndFeel.isMnemonicHidden () ? -1 : component.getDisplayedMnemonicIndex ();
            SwingUtils
                    .drawStringUnderlineCharAt ( g2d, component.getText (), mnemonic, x, y + h / 2 + LafUtils.getTextCenterShiftY ( fm ) );
        }
        GraphicsUtils.restoreFont ( g2d, oldFont );
    }

    /**
     * Paints menu item accelerator text.
     *
     * @param g2d      graphics context
     * @param accText  accelerator text
     * @param fm       accelerator text font metrics
     * @param x        accelerator text X coordinate
     * @param y        accelerator text rectangle Y coordinate
     * @param w        accelerator text width
     * @param h        accelerator text rectangle height
     * @param selected whether menu item is selected or not
     */
    protected void paintAcceleratorText ( final Graphics2D g2d, final String accText, final FontMetrics fm, final int x, final int y,
                                          final int w, final int h, final boolean selected )
    {
        if ( selected && acceleratorBg != null )
        {
            final int th = fm.getHeight ();
            g2d.setPaint ( acceleratorBg );
            g2d.fillRoundRect ( x - 3, y + h / 2 - th / 2, w + 6, th, 4, 4 );
        }

        final Font oldFont = GraphicsUtils.setupFont ( g2d, acceleratorFont );
        g2d.setPaint ( component.isEnabled () ? acceleratorFg : acceleratorDisabledFg );
        g2d.drawString ( accText, x, y + h / 2 + LafUtils.getTextCenterShiftY ( fm ) );
        GraphicsUtils.restoreFont ( g2d, oldFont );
    }

    /**
     * Sets accelerator font.
     */
    protected void setAcceleratorFont ()
    {
        acceleratorFont = UIManager.getFont ( "MenuItem.acceleratorFont" );
        // use default if missing so that BasicMenuItemUI can be used in other
        // LAFs like Nimbus
        if ( acceleratorFont == null )
        {
            acceleratorFont = UIManager.getFont ( "MenuItem.font" );
        }
    }

    @Override
    public Rectangle getSelectedBounds ()
    {
        // todo Implement corner support
        return SwingUtils.getRelativeBounds ( component, component.getParent () );
    }

    @Override
    public void fillCorner ( final Graphics2D g2d, final Rectangle clip, final Shape corner, final int cornerSide )
    {
        // todo Implement corner support
    }

    @Override
    public Dimension getPreferredSize ()
    {
        final Insets bi = component.getInsets ();
        final FontMetrics fm = component.getFontMetrics ( component.getFont () );
        final FontMetrics afm = component.getFontMetrics ( acceleratorFont );

        // Icon
        final int iconPlaceholderWidth = MenuUtils.getIconPlaceholderWidth ( component, alignTextToMenuIcons );

        // Text
        final View html = ( View ) component.getClientProperty ( BasicHTML.propertyKey );
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
            final String text = component.getText ();
            textWidth = text != null && text.length () > 0 ? fm.stringWidth ( text ) : 0;
            textHeight = fm.getHeight ();
        }

        // Icon-Text gap
        final int gap = textWidth > 0 && iconPlaceholderWidth > 0 ? component.getIconTextGap () : 0;

        // Acceleration text and its gap
        final String accelerationText = MenuUtils.getAcceleratorText ( component );
        final int accWidth = accelerationText != null ? acceleratorGap + afm.stringWidth ( accelerationText ) : 0;

        // Content height
        final int iconHeight = component.getIcon () != null ? component.getIcon ().getIconHeight () : 0;
        final int contentHeight = MathUtils.max ( iconHeight, textHeight, afm.getHeight () );

        return new Dimension ( bi.left + iconPlaceholderWidth + gap + textWidth + accWidth + bi.right, bi.top + contentHeight + bi.bottom );
    }
}