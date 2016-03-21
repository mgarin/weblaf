package com.alee.laf.menu;

import com.alee.laf.menu.AbstractMenuItemPainter;
import com.alee.laf.menu.ICheckBoxMenuItemPainter;
import com.alee.laf.menu.WebCheckBoxMenuItemUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Alexandr Zernov
 */

public class CheckBoxMenuItemPainter<E extends JMenuItem, U extends WebCheckBoxMenuItemUI> extends AbstractMenuItemPainter<E, U>
        implements ICheckBoxMenuItemPainter<E, U>
{
    /**
     * Used icons.
     */
    protected static final ImageIcon boxIcon = new ImageIcon ( WebCheckBoxMenuItemUI.class.getResource ( "icons/box.png" ) );
    protected static final ImageIcon boxCheckIcon = new ImageIcon ( WebCheckBoxMenuItemUI.class.getResource ( "icons/boxCheck.png" ) );

    /**
     * Style settings.
     */
    protected Color checkColor = new Color ( 230, 230, 220 );

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
            final int ix = x + w / 2 - boxIcon.getIconWidth () / 2;
            final int iy = y + h / 2 - boxIcon.getIconHeight () / 2;
            g2d.drawImage ( component.isSelected () ? boxCheckIcon.getImage () : boxIcon.getImage (), ix, iy, null );
        }
    }
}