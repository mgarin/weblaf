package com.alee.managers.style.skin.web;

import com.alee.laf.menu.RadioButtonMenuItemPainter;
import com.alee.laf.menu.WebMenuItemStyle;
import com.alee.laf.menu.WebRadioButtonMenuItemUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Alexandr Zernov
 */

public class WebRadioButtonMenuItemPainter<E extends JMenuItem, U extends WebRadioButtonMenuItemUI> extends WebAbstractMenuItemPainter<E, U>
        implements RadioButtonMenuItemPainter<E, U>
{
    /**
     * Used icons.
     */
    protected static final ImageIcon radioIcon = new ImageIcon ( WebRadioButtonMenuItemUI.class.getResource ( "icons/radio.png" ) );
    protected static final ImageIcon radioCheckIcon =
            new ImageIcon ( WebRadioButtonMenuItemUI.class.getResource ( "icons/radioCheck.png" ) );

    /**
     * Style settings.
     */
    protected Color checkColor = WebMenuItemStyle.checkColor;

    @Override
    protected void paintBackground ( final Graphics2D g2d, final boolean selected )
    {
        super.paintBackground ( g2d, selected );

        // Painting check selection
        if ( !selected && component.isSelected () && checkColor != null )
        {
            g2d.setPaint ( checkColor );
            g2d.fillRect ( 0, 0, component.getWidth (), component.getHeight () );
        }
    }

    @Override
    protected void paintIcon ( final Graphics2D g2d, final int x, final int y, final int w, final int h, final boolean selected )
    {
        super.paintIcon ( g2d, x, y, w, h, selected );

        // Painting check icon
        if ( component.getIcon () == null )
        {
            final int ix = x + w / 2 - radioIcon.getIconWidth () / 2;
            final int iy = y + h / 2 - radioIcon.getIconHeight () / 2;
            g2d.drawImage ( component.isSelected () ? radioCheckIcon.getImage () : radioIcon.getImage (), ix, iy, null );
        }
    }
}