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

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Custom UI for JRadioButtonMenuItem component.
 *
 * @author Mikle Garin
 */

public class WebRadioButtonMenuItemUI extends WebMenuItemUI
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

    /**
     * Returns an instance of the WebRadioButtonMenuItemUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebRadioButtonMenuItemUI
     */
    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebRadioButtonMenuItemUI ();
    }

    /**
     * Returns property prefix for this specific UI.
     *
     * @return property prefix for this specific UI
     */
    @Override
    protected String getPropertyPrefix ()
    {
        return "RadioButtonMenuItem";
    }

    /**
     * Returns radiobutton menu item check color.
     *
     * @return radiobutton menu item check color
     */
    public Color getCheckColor ()
    {
        return checkColor;
    }

    /**
     * Sets radiobutton menu item check color.
     *
     * @param color radiobutton menu item check color
     */
    public void setCheckColor ( final Color color )
    {
        this.checkColor = color;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Paint getNorthCornerFill ()
    {
        final boolean selected = menuItem.isEnabled () && menuItem.getModel ().isArmed ();
        return !selected && menuItem.isSelected () ? checkColor : super.getNorthCornerFill ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Paint getSouthCornerFill ()
    {
        final boolean selected = menuItem.isEnabled () && menuItem.getModel ().isArmed ();
        return !selected && menuItem.isSelected () ? checkColor : super.getSouthCornerFill ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintBackground ( final Graphics2D g2d, final JMenuItem menuItem, final int x, final int y, final int w, final int h,
                                     final boolean selected, final boolean ltr )
    {
        super.paintBackground ( g2d, menuItem, x, y, w, h, selected, ltr );

        // Painting check selection
        if ( painter == null && !selected && menuItem.isSelected () && checkColor != null )
        {
            g2d.setPaint ( checkColor );
            g2d.fillRect ( 0, 0, menuItem.getWidth (), menuItem.getHeight () );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintIcon ( final Graphics2D g2d, final JMenuItem menuItem, final int x, final int y, final int w, final int h,
                               final boolean selected, final boolean ltr )
    {
        super.paintIcon ( g2d, menuItem, x, y, w, h, selected, ltr );

        // Painting check icon
        if ( menuItem.getIcon () == null )
        {
            final int ix = x + w / 2 - radioIcon.getIconWidth () / 2;
            final int iy = y + h / 2 - radioIcon.getIconHeight () / 2;
            g2d.drawImage ( menuItem.isSelected () ? radioCheckIcon.getImage () : radioIcon.getImage (), ix, iy, null );
        }
    }
}