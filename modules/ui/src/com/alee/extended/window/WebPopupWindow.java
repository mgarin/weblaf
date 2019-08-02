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

import com.alee.laf.window.WebWindow;
import com.alee.utils.ProprietaryUtils;
import com.alee.utils.SystemUtils;

import java.awt.*;

/**
 * Custom popup window with modal exclude.
 *
 * @author Mikle Garin
 */
public class WebPopupWindow extends WebWindow
{
    /**
     * Constructs new popup window.
     *
     * @param owner window owner
     */
    public WebPopupWindow ( final Window owner )
    {
        super ( owner );

        /**
         * Special {@link Window} name used to trigger some Swing workarounds.
         * In most cases it is not necessary but it doesn't hurt either.
         */
        setName ( "###focusableSwingPopup###" );

        /**
         * Disabled to ensure we do not force {@link Window} to request focus upon display.
         * It is reenabled within {@link #setVisible(boolean)} method.
         */
        setFocusableWindowState ( false );

        /**
         * Configuring {@link Window} type to be {@code Window.Type.POPUP} to ensure better OS behavior.
         * {@code Window.Type.POPUP} is not set for Unix systems due to window prioritization issues.
         */
        if ( !SystemUtils.isUnix () )
        {
            ProprietaryUtils.setPopupWindowType ( this );
        }

        /**
         * Modal exclusion is disabled to avoid our popups being blocked by modal dialogs.
         * This can be reenabled if necessary but in most cases it shouldn't be needed.
         */
        setModalExclusionType ( Dialog.ModalExclusionType.APPLICATION_EXCLUDE );
    }

    @Override
    public void setVisible ( final boolean b )
    {
        // Updating visibility
        super.setVisible ( b );

        /**
         * Enabling focusable state AFTER window display.
         * We have to do it in that specific order due to issues with window focus state on some OS.
         * For instance it causes native window decoration to blink sometimes under Windows OS.
         *
         * todo In JDK7+ releases there was a new Window "type" option introduced that can be set to "POPUP".
         * todo Maybe it can solve some of the focusing issues appearing on the later JDK releases, but that needs to be tested.
         */
        setFocusableWindowState ( b );
    }
}