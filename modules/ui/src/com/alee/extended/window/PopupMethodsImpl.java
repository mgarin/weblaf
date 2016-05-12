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

/**
 * Common implementations for {@link com.alee.extended.window.PopupMethods} interface methods.
 *
 * @author Mikle Garin
 * @see com.alee.extended.window.PopupMethods
 */

public final class PopupMethodsImpl
{
    /**
     * Shortcut method for on-before-open popup event.
     *
     * @param popup  popup to add listener to
     * @param action action to perform
     * @return used popup listener
     */
    public static PopupListener beforePopupOpen ( final Popup popup, final Runnable action )
    {
        final PopupAdapter listener = new PopupAdapter ()
        {
            @Override
            public void popupWillBeOpened ()
            {
                action.run ();
            }
        };
        popup.addPopupListener ( listener );
        return listener;
    }

    /**
     * Shortcut method for on-open popup event.
     *
     * @param popup  popup to add listener to
     * @param action action to perform
     * @return used popup listener
     */
    public static PopupListener onPopupOpen ( final Popup popup, final Runnable action )
    {
        final PopupAdapter listener = new PopupAdapter ()
        {
            @Override
            public void popupOpened ()
            {
                action.run ();
            }
        };
        popup.addPopupListener ( listener );
        return listener;
    }

    /**
     * Shortcut method for on-before-close popup event.
     *
     * @param popup  popup to add listener to
     * @param action action to perform
     * @return used popup listener
     */
    public static PopupListener beforePopupClose ( final Popup popup, final Runnable action )
    {
        final PopupAdapter listener = new PopupAdapter ()
        {
            @Override
            public void popupWillBeClosed ()
            {
                action.run ();
            }
        };
        popup.addPopupListener ( listener );
        return listener;
    }

    /**
     * Shortcut method for on-close popup event.
     *
     * @param popup  popup to add listener to
     * @param action action to perform
     * @return used popup listener
     */
    public static PopupListener onPopupClose ( final Popup popup, final Runnable action )
    {
        final PopupAdapter listener = new PopupAdapter ()
        {
            @Override
            public void popupClosed ()
            {
                action.run ();
            }
        };
        popup.addPopupListener ( listener );
        return listener;
    }
}