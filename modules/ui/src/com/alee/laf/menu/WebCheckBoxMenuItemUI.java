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
 * Custom UI for JCheckBoxMenuItem component.
 *
 * @author Mikle Garin
 */

public class WebCheckBoxMenuItemUI extends WebMenuItemUI
{
    /**
     * Used icons.
     */
    protected static final ImageIcon boxIcon = new ImageIcon ( WebCheckBoxMenuItemUI.class.getResource ( "icons/box.png" ) );
    protected static final ImageIcon boxCheckIcon = new ImageIcon ( WebCheckBoxMenuItemUI.class.getResource ( "icons/boxCheck.png" ) );

    /**
     * Style settings.
     */
    protected Color checkColor = WebMenuItemStyle.checkColor;

    /**
     * Returns an instance of the WebCheckBoxMenuItemUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebCheckBoxMenuItemUI
     */
    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebCheckBoxMenuItemUI ();
    }

    /**
     * Returns property prefix for this specific UI.
     *
     * @return property prefix for this specific UI
     */
    @Override
    protected String getPropertyPrefix ()
    {
        return "CheckBoxMenuItem";
    }

    /**
     * Returns checkbox menu item check color.
     *
     * @return checkbox menu item check color
     */
    public Color getCheckColor ()
    {
        return checkColor;
    }

    /**
     * Sets checkbox menu item check color.
     *
     * @param color checkbox menu item check color
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
            final int ix = x + w / 2 - boxIcon.getIconWidth () / 2;
            final int iy = y + h / 2 - boxIcon.getIconHeight () / 2;
            g2d.drawImage ( menuItem.isSelected () ? boxCheckIcon.getImage () : boxIcon.getImage (), ix, iy, null );
        }
    }
}