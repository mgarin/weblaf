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

package com.alee.managers.settings;

import com.alee.extended.colorchooser.WebGradientColorChooser;
import com.alee.extended.date.WebDateField;
import com.alee.extended.panel.WebAccordion;
import com.alee.extended.panel.WebCollapsiblePane;
import com.alee.managers.settings.processors.WebAccordionSettingsProcessor;
import com.alee.managers.settings.processors.WebCollapsiblePaneSettingsProcessor;
import com.alee.managers.settings.processors.WebDateFieldSettingsProcessor;
import com.alee.managers.settings.processors.WebGradientColorChooserSettingsProcessor;

/**
 * Minor additions over core SettingsManager.
 *
 * @author Mikle Garin
 */

public class WebSettingsManager
{
    /**
     * Whether SettingsManager is initialized or not.
     */
    protected static boolean initialized = false;

    /**
     * Initializes SettingsManager.
     */
    public static synchronized void initialize ()
    {
        if ( !initialized )
        {
            initialized = true;

            // Ensure SettingsManager is initialized
            SettingsManager.initialize ();

            // Register additional component settings processors
            ComponentSettingsManager.registerSettingsProcessor ( WebDateField.class, WebDateFieldSettingsProcessor.class );
            ComponentSettingsManager.registerSettingsProcessor ( WebCollapsiblePane.class, WebCollapsiblePaneSettingsProcessor.class );
            ComponentSettingsManager.registerSettingsProcessor ( WebAccordion.class, WebAccordionSettingsProcessor.class );
            ComponentSettingsManager
                    .registerSettingsProcessor ( WebGradientColorChooser.class, WebGradientColorChooserSettingsProcessor.class );
        }
    }
}