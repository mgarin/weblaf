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

package com.alee.managers.style.skin.web;

import com.alee.laf.panel.PanelPainter;

import javax.swing.*;

/**
 * Web-style painter for WebPopOver component.
 * It is used as default WebPopOver painter.
 *
 * @author Mikle Garin
 */

public class WebPopOverPainter<E extends JPanel> extends WebPopupPainter<E> implements PanelPainter<E>
{
    /**
     * Runtime variables.
     */
    protected boolean popOverFocused = false;

    /**
     * Returns whether this WebPopOver is focus owner or not.
     *
     * @return true if this WebPopOver is focus owner, false otherwise
     */
    public boolean isPopOverFocused ()
    {
        return popOverFocused;
    }

    /**
     * Sets whether this WebPopOver is focus owner or not.
     *
     * @param focused whether this WebPopOver is focus owner or not
     */
    public void setPopOverFocused ( final boolean focused )
    {
        if ( this.popOverFocused != focused )
        {
            this.popOverFocused = focused;
            repaint ();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected float getCurrentShadeTransparency ()
    {
        // Reducing the shade when WebPopOver is not focused
        return popOverFocused ? shadeTransparency : shadeTransparency * 0.7f;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUndecorated ( final boolean undecorated )
    {
        // Ignored setting
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintFocus ( final boolean paint )
    {
        // Ignored setting
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintTop ( final boolean top )
    {
        // Ignored setting
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintLeft ( final boolean left )
    {
        // Ignored setting
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintBottom ( final boolean bottom )
    {
        // Ignored setting
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintRight ( final boolean right )
    {
        // Ignored setting
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintSides ( final boolean top, final boolean left, final boolean bottom, final boolean right )
    {
        // Ignored setting
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintTopLine ( final boolean top )
    {
        // Ignored setting
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintLeftLine ( final boolean left )
    {
        // Ignored setting
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintBottomLine ( final boolean bottom )
    {
        // Ignored setting
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintRightLine ( final boolean right )
    {
        // Ignored setting
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintSideLines ( final boolean top, final boolean left, final boolean bottom, final boolean right )
    {
        // Ignored setting
    }
}