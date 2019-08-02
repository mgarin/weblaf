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

package com.alee.extended.date;

import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsProcessor;

import java.util.Date;

/**
 * {@link SettingsProcessor} implementation that handles {@link WebDateField} settings.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see SettingsProcessor
 */
public class DateFieldSettingsProcessor extends SettingsProcessor<WebDateField, DateFieldState, Configuration<DateFieldState>>
{
    /**
     * {@link DateListener} for tracking {@link WebDateField} date changes.
     */
    protected transient DateListener dateListener;

    /**
     * Constructs new {@link DateFieldSettingsProcessor}.
     *
     * @param dateField     {@link WebDateField} which settings are being managed
     * @param configuration {@link Configuration}
     */
    public DateFieldSettingsProcessor ( final WebDateField dateField, final Configuration configuration )
    {
        super ( dateField, configuration );
    }

    @Override
    protected void register ( final WebDateField dateField )
    {
        dateListener = new DateListener ()
        {
            @Override
            public void dateChanged ( final Date date )
            {
                save ();
            }
        };
        dateField.addDateListener ( dateListener );
    }

    @Override
    protected void unregister ( final WebDateField dateField )
    {
        dateField.removeDateListener ( dateListener );
        dateListener = null;
    }

    @Override
    protected DateFieldState createDefaultValue ()
    {
        return new DateFieldState ( component () );
    }

    @Override
    protected void loadSettings ( final WebDateField dateField )
    {
        loadSettings ().apply ( dateField );
    }

    @Override
    protected void saveSettings ( final WebDateField dateField )
    {
        saveSettings ( new DateFieldState ( dateField ) );
    }
}