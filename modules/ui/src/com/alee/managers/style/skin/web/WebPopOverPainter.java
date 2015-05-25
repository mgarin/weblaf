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
import com.alee.laf.panel.WebPanelUI;

import javax.swing.*;

/**
 * Web-style painter for WebPopOver component.
 * It is used as default WebPopOver painter.
 *
 * @author Mikle Garin
 */

public class WebPopOverPainter<E extends JPanel, U extends WebPanelUI> extends WebPopupPainter<E, U> implements PanelPainter<E, U>
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
}