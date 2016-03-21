package com.alee.laf.menu;

import com.alee.global.StyleConstants;
import com.alee.laf.menu.AbstractMenuItemPainter;
import com.alee.laf.menu.IMenuPainter;
import com.alee.laf.menu.MenuUtils;
import com.alee.laf.menu.WebMenuUI;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.ImageUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

/**
 * @author Alexandr Zernov
 */

public class MenuPainter<E extends JMenu, U extends WebMenuUI> extends AbstractMenuItemPainter<E, U> implements IMenuPainter<E, U>
{
    /**
     * Used icons.
     */
    public static final ImageIcon arrowRightIcon = new ImageIcon ( WebMenuUI.class.getResource ( "icons/arrowRight.png" ) );
    public static final ImageIcon arrowLeftIcon = new ImageIcon ( WebMenuUI.class.getResource ( "icons/arrowLeft.png" ) );

    /**
     * Style settings.
     */
    protected int round = 2;
    protected int shadeWidth = 2;
    protected int arrowGap = 15;

    /**
     * Listeners.
     */
    protected MouseAdapter mouseAdapter;

    /**
     * Runtime variables.
     */
    protected boolean mouseover = false;

    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Rollover listener
        mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mouseEntered ( final MouseEvent e )
            {
                mouseover = true;
                component.repaint ();
            }

            @Override
            public void mouseExited ( final MouseEvent e )
            {
                mouseover = false;
                component.repaint ();
            }
        };
        component.addMouseListener ( mouseAdapter );
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Removing listeners
        component.removeMouseListener ( mouseAdapter );
        mouseAdapter = null;

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
        x += ltr ? ( iconPlaceholderWidth + gap ) : -gap;

        // Painting text
        final String text = component.getText ();
        if ( text != null && text.length () > 0 )
        {
            final Map hints = SwingUtils.setupTextAntialias ( g2d );

            final FontMetrics fm = component.getFontMetrics ( component.getFont () );
            final View html = ( View ) component.getClientProperty ( BasicHTML.propertyKey );
            final int tw = html != null ? ( int ) html.getPreferredSpan ( View.X_AXIS ) : fm.stringWidth ( text );

            x -= ltr ? 0 : tw;
            paintText ( g2d, fm, x, y, tw, ih, selected );

            SwingUtils.restoreTextAntialias ( g2d, hints );
        }

        // Painting sub-menu arrow icon
        final Icon arrowIcon = getArrowIcon ( component );
        if ( arrowIcon != null )
        {
            final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, 0.4f, selected );
            arrowIcon.paintIcon ( component, g2d, ltr ? w - bi.right - arrowIcon.getIconWidth () : bi.left,
                    y + ih / 2 - arrowIcon.getIconHeight () / 2 );
            GraphicsUtils.restoreComposite ( g2d, oc, selected );
        }

        GraphicsUtils.restoreAntialias ( g2d, aa );
    }

    @Override
    protected void paintBackground ( final Graphics2D g2d, final boolean selected )
    {
        if ( component.getParent () instanceof JPopupMenu )
        {
            super.paintBackground ( g2d, selected );
        }
        else
        {
            if ( component.isEnabled () && ( selected || mouseover ) )
            {
                LafUtils.drawWebStyle ( g2d, component, new Color ( 210, 210, 210 ), shadeWidth, round, component.isEnabled (),
                        !selected && mouseover, selected ? StyleConstants.averageBorderColor : StyleConstants.borderColor );
            }
        }
    }

    /**
     * Returns arrow icon displayed when sub-menu is available.
     *
     * @param menu menu
     * @return arrow icon displayed when sub-menu is available
     */
    protected Icon getArrowIcon ( final JMenu menu )
    {
        if ( menu.getParent () instanceof JPopupMenu )
        {
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

    @Override
    public Dimension getPreferredSize ()
    {
        final Dimension size = super.getPreferredSize ();

        // Sub-menu arrow icon
        final Icon subMenuArrowIcon = getArrowIcon ( component );
        final int arrowWidth = subMenuArrowIcon != null ? arrowGap + subMenuArrowIcon.getIconWidth () : 0;

        return new Dimension ( size.width + arrowWidth, size.height );
    }
}