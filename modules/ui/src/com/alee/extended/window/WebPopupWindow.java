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

package com.alee.extended.window;

import com.alee.laf.rootpane.WebWindow;
import sun.awt.ModalExclude;

import java.awt.*;

/**
 * Custom popup window with modal exclude.
 *
 * @author Mikle Garin
 */

public class WebPopupWindow extends WebWindow implements ModalExclude
{
    /**
     * Constructs new popup window.
     *
     * @param owner window owner
     */
    public WebPopupWindow ( final Window owner )
    {
        super ( owner );
        setName ( "###focusableSwingPopup###" );
        setFocusableWindowState ( false );
    }

    @Override
    public void setVisible ( final boolean b )
    {
        // Updating visibility
        super.setVisible ( b );

        // Enabling focusable state after window display
        // We have to do that after due to minor issues with window focus
        // Otherwise it causes native window decoration blink on some OS
        setFocusableWindowState ( b );
    }
}