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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * {@link TextComponentSettingsProcessor} implementation that handles {@link JTextField} settings.
 *
 * @param <C> {@link JTextField} type
 * @param <V> {@link TextComponentState} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see TextComponentSettingsProcessor
 * @see com.alee.managers.settings.SettingsProcessor
 */
public class TextFieldSettingsProcessor<C extends JTextField, V extends TextComponentState> extends TextComponentSettingsProcessor<C, V>
{
    /**
     * {@link ActionListener} for tracking field editing completion.
     */
    protected transient ActionListener actionListener;

    /**
     * Constructs new {@link TextFieldSettingsProcessor}.
     *
     * @param textField     {@link JTextField} which settings are being managed
     * @param configuration {@link Configuration}
     */
    public TextFieldSettingsProcessor ( final C textField, final Configuration<V> configuration )
    {
        super ( textField, configuration );
    }

    @Override
    protected void register ( final C textField )
    {
        super.register ( textField );

        actionListener = new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                save ();
            }
        };
        textField.addActionListener ( actionListener );
    }

    @Override
    protected void unregister ( final C textField )
    {
        textField.removeActionListener ( actionListener );
        actionListener = null;

        super.unregister ( textField );
    }
}