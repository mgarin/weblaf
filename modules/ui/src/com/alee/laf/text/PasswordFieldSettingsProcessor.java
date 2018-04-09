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

import com.alee.managers.settings.Configuration;

import javax.swing.*;

/**
 * {@link TextFieldSettingsProcessor} implementation that handles {@link JPasswordField} settings.
 *
 * @param <C> {@link JPasswordField} type
 * @param <V> {@link PasswordFieldState} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see TextFieldSettingsProcessor
 * @see TextComponentSettingsProcessor
 * @see com.alee.managers.settings.SettingsProcessor
 */
public class PasswordFieldSettingsProcessor<C extends JPasswordField, V extends PasswordFieldState>
        extends TextFieldSettingsProcessor<C, V>
{
    /**
     * Constructs new {@link PasswordFieldSettingsProcessor}.
     *
     * @param component     {@link JPasswordField} which settings are being managed
     * @param configuration {@link Configuration}
     */
    public PasswordFieldSettingsProcessor ( final C component, final Configuration<V> configuration )
    {
        super ( component, configuration );
    }

    @Override
    protected V createDefaultValue ()
    {
        return ( V ) new PasswordFieldState ();
    }

    @Override
    protected V stateFor ( final C passwordField )
    {
        return ( V ) new PasswordFieldState ( passwordField );
    }
}