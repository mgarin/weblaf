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

package com.alee.extended.colorchooser;

import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsProcessor;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * {@link SettingsProcessor} implementation that handles {@link WebGradientColorChooser} settings.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see SettingsProcessor
 */
public class GradientColorChooserSettingsProcessor extends SettingsProcessor<WebGradientColorChooser, GradientColorChooserState,
        Configuration<GradientColorChooserState>>
{
    /**
     * {@link ChangeListener} for tracking {@link WebGradientColorChooser} changes.
     */
    protected transient ChangeListener changeListener;

    /**
     * Constructs new {@link GradientColorChooserSettingsProcessor}.
     *
     * @param gradientColorChooser {@link WebGradientColorChooser} which settings are being managed
     * @param configuration        {@link Configuration}
     */
    public GradientColorChooserSettingsProcessor ( final WebGradientColorChooser gradientColorChooser, final Configuration configuration )
    {
        super ( gradientColorChooser, configuration );
    }

    @Override
    protected void register ( final WebGradientColorChooser gradientColorChooser )
    {
        changeListener = new ChangeListener ()
        {
            @Override
            public void stateChanged ( final ChangeEvent e )
            {
                save ();
            }
        };
        gradientColorChooser.addChangeListener ( changeListener );
    }

    @Override
    protected void unregister ( final WebGradientColorChooser gradientColorChooser )
    {
        gradientColorChooser.removeChangeListener ( changeListener );
        changeListener = null;
    }

    @Override
    protected GradientColorChooserState createDefaultValue ()
    {
        return new GradientColorChooserState ( component () );
    }

    @Override
    protected void loadSettings ( final WebGradientColorChooser gradientColorChooser )
    {
        loadSettings ().apply ( gradientColorChooser );
    }

    @Override
    protected void saveSettings ( final WebGradientColorChooser gradientColorChooser )
    {
        saveSettings ( new GradientColorChooserState ( gradientColorChooser ) );
    }
}