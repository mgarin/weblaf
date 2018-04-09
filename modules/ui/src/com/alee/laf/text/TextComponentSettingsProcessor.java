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
import com.alee.managers.settings.SettingsProcessor;

import javax.swing.text.JTextComponent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * {@link SettingsProcessor} implementation that handles {@link JTextComponent} settings.
 *
 * @param <C> {@link JTextComponent} type
 * @param <V> {@link TextComponentState} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see SettingsProcessor
 */
public class TextComponentSettingsProcessor<C extends JTextComponent, V extends TextComponentState>
        extends SettingsProcessor<C, V, Configuration<V>>
{
    /**
     * {@link FocusListener} for tracking focus loss.
     */
    protected transient FocusListener focusListener;

    /**
     * Constructs new {@link TextComponentSettingsProcessor}.
     *
     * @param textComponent {@link JTextComponent} which settings are being managed
     * @param configuration {@link Configuration}
     */
    public TextComponentSettingsProcessor ( final C textComponent, final Configuration<V> configuration )
    {
        super ( textComponent, configuration );
    }

    @Override
    protected void register ( final C textComponent )
    {
        focusListener = new FocusAdapter ()
        {
            @Override
            public void focusLost ( final FocusEvent e )
            {
                save ();
            }
        };
        textComponent.addFocusListener ( focusListener );
    }

    @Override
    protected void unregister ( final C textComponent )
    {
        textComponent.removeFocusListener ( focusListener );
        focusListener = null;
    }

    @Override
    protected V createDefaultValue ()
    {
        return ( V ) new TextComponentState ();
    }

    @Override
    protected void loadSettings ( final C textComponent )
    {
        loadSettings ().apply ( textComponent );
    }

    @Override
    protected void saveSettings ( final C textComponent )
    {
        saveSettings ( stateFor ( textComponent ) );
    }

    /**
     * Returns {@link TextComponentState} for the specified {@link JTextComponent}.
     *
     * @param textComponent {@link JTextComponent} to retrieve {@link TextComponentState} for
     * @return {@link TextComponentState} for the specified {@link JTextComponent}
     */
    protected V stateFor ( final C textComponent )
    {
        return ( V ) new TextComponentState ( textComponent );
    }
}