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

package com.alee.extended.menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Standard dynamic menu item data.
 *
 * @author Mikle Garin
 */

public class WebDynamicMenuItem
{
    /**
     * Menu item icon.
     */
    protected ImageIcon icon;

    /**
     * Menu item action.
     */
    protected ActionListener action;

    /**
     * Menu item icon margin.
     */
    protected Insets margin = new Insets ( 5, 5, 5, 5 );

    /**
     *
     */
    protected boolean drawBorder = true;
    protected Color borderColor = new Color ( 89, 122, 222 );
    protected Color disabledBorderColor = new Color ( 149, 151, 170 );

    public WebDynamicMenuItem ()
    {
        super ();
    }

    public WebDynamicMenuItem ( final ImageIcon icon )
    {
        super ();
        this.icon = icon;
    }

    public WebDynamicMenuItem ( final ImageIcon icon, final ActionListener action )
    {
        super ();
        this.icon = icon;
        this.action = action;
    }

    public ImageIcon getIcon ()
    {
        return icon;
    }

    public void setIcon ( final ImageIcon icon )
    {
        this.icon = icon;
    }

    public ActionListener getAction ()
    {
        return action;
    }

    public void setAction ( final ActionListener action )
    {
        this.action = action;
    }

    public Insets getMargin ()
    {
        return margin;
    }

    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
    }

    public boolean isDrawBorder ()
    {
        return drawBorder;
    }

    public void setDrawBorder ( final boolean drawBorder )
    {
        this.drawBorder = drawBorder;
    }

    public Color getBorderColor ()
    {
        return borderColor;
    }

    public void setBorderColor ( final Color borderColor )
    {
        this.borderColor = borderColor;
    }

    public Color getDisabledBorderColor ()
    {
        return disabledBorderColor;
    }

    public void setDisabledBorderColor ( final Color disabledBorderColor )
    {
        this.disabledBorderColor = disabledBorderColor;
    }
}