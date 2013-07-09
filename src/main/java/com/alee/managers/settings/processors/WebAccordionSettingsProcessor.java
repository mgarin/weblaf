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

import com.alee.extended.panel.AccordionListener;
import com.alee.extended.panel.WebAccordion;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.settings.SettingsProcessorData;
import com.alee.utils.TextUtils;

import java.util.List;

/**
 * Custom SettingsProcessor for WebAccordion component.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class WebAccordionSettingsProcessor extends SettingsProcessor<WebAccordion, String>
{
    /**
     * Accordion selection listener.
     */
    private AccordionListener accordionListener;

    /**
     * Constructs SettingsProcessor using the specified SettingsProcessorData.
     *
     * @param data SettingsProcessorData
     */
    public WebAccordionSettingsProcessor ( SettingsProcessorData data )
    {
        super ( data );
    }

    /**
     * {@inheritDoc}
     */
    protected void doInit ( WebAccordion accordion )
    {
        accordionListener = new AccordionListener ()
        {
            public void selectionChanged ()
            {
                save ();
            }
        };
        accordion.addAccordionListener ( accordionListener );
    }

    /**
     * {@inheritDoc}
     */
    protected void doDestroy ( WebAccordion accordion )
    {
        accordion.removeAccordionListener ( accordionListener );
        accordionListener = null;
    }

    /**
     * {@inheritDoc}
     */
    protected void doLoad ( WebAccordion accordion )
    {
        // Empty string identifies empty selection
        final String string = loadValue ();
        final List<Integer> indices = string == null || string.trim ().equals ( "" ) ? null : TextUtils.stringToIntList ( string, "," );
        accordion.setSelectedIndices ( indices );
    }

    /**
     * {@inheritDoc}
     */
    protected void doSave ( WebAccordion accordion )
    {
        // For empty selection empty string is used to avoid having null as value as this will call for default value on load
        final String value = TextUtils.listToString ( accordion.getSelectedIndices (), "," );
        saveValue ( value != null ? value : "" );
        System.out.println ( value );
    }
}