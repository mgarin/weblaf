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

package com.alee.laf.text;

import com.alee.managers.style.StyleId;

import javax.swing.*;

/**
 * Custom descriptor for {@link JPasswordField} component.
 *
 * @author Mikle Garin
 */

public final class PasswordFieldDescriptor extends AbstractTextComponentDescriptor<JPasswordField>
{
    /**
     * todo 1. Retrieve echo char from style variables or component settings?
     */

    /**
     * Constructs new descriptor for {@link JPasswordField} component.
     */
    public PasswordFieldDescriptor ()
    {
        super ( "passwordfield", JPasswordField.class, "PasswordFieldUI", WPasswordFieldUI.class, WebPasswordFieldUI.class,
                StyleId.passwordfield );
    }

    @Override
    public void updateUI ( final JPasswordField component )
    {
        // Default echo char
        if ( !component.echoCharIsSet () )
        {
            component.setEchoChar ( '*' );
        }

        // Updating component UI
        super.updateUI ( component );
    }
}