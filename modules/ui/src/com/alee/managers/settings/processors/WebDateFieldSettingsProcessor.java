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

package com.alee.managers.settings.processors;

import com.alee.extended.date.DateListener;
import com.alee.extended.date.WebDateField;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.settings.SettingsProcessorData;

import java.util.Date;

/**
 * Custom SettingsProcessor for WebDateField component.
 *
 * @author Mikle Garin
 */

public class WebDateFieldSettingsProcessor extends SettingsProcessor<WebDateField, Long>
{
    /**
     * Date selection change listener.
     */
    private DateListener selectionListener;

    /**
     * Constructs SettingsProcessor using the specified SettingsProcessorData.
     *
     * @param data SettingsProcessorData
     */
    public WebDateFieldSettingsProcessor ( final SettingsProcessorData data )
    {
        super ( data );
    }

    @Override
    protected void doInit ( final WebDateField dateField )
    {
        selectionListener = new DateListener ()
        {
            @Override
            public void dateChanged ( final Date date )
            {
                save ();
            }
        };
        dateField.addDateListener ( selectionListener );
    }

    @Override
    protected void doDestroy ( final WebDateField dateField )
    {
        dateField.removeDateListener ( selectionListener );
        selectionListener = null;
    }

    @Override
    protected void doLoad ( final WebDateField dateField )
    {
        final Long date = loadValue ();
        final Date value = date != null ? new Date ( date ) : null;
        dateField.setDate ( value );
    }

    @Override
    protected void doSave ( final WebDateField dateField )
    {
        final Date date = dateField.getDate ();
        final Long value = date != null ? date.getTime () : null;
        saveValue ( value );
    }
}