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

import com.alee.utils.EncryptionUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;
import javax.swing.text.JTextComponent;

/**
 * {@link JPasswordField} state holder.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see PasswordFieldSettingsProcessor
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 */
@XStreamAlias ( "PasswordFieldState" )
public class PasswordFieldState extends TextComponentState
{
    /**
     * Constructs default {@link PasswordFieldState}.
     */
    public PasswordFieldState ()
    {
        this ( 0, "" );
    }

    /**
     * Constructs new {@link PasswordFieldState} with settings from {@link JPasswordField}.
     *
     * @param passwordField {@link JPasswordField} to retrieve settings from
     */
    public PasswordFieldState ( final JPasswordField passwordField )
    {
        this ( passwordField.getCaretPosition (), new String ( passwordField.getPassword () ) );
    }

    /**
     * Constructs new {@link PasswordFieldState} with specified settings.
     *
     * @param caretPosition {@link JPasswordField} caret position
     * @param password      {@link JPasswordField} password
     */
    public PasswordFieldState ( final int caretPosition, final String password )
    {
        super ( EncryptionUtils.encrypt ( password ), caretPosition );
    }

    @Override
    public void apply ( final JTextComponent textComponent )
    {
        textComponent.setText ( EncryptionUtils.decrypt ( text ) );
        if ( caretPosition != null )
        {
            textComponent.setCaretPosition ( caretPosition );
        }
    }
}