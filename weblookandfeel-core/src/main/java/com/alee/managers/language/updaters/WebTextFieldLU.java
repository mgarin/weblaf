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

package com.alee.managers.language.updaters;

import com.alee.laf.text.WebTextField;
import com.alee.managers.language.data.Value;

/**
 * This class provides language default updates for WebTextField component.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class WebTextFieldLU extends DefaultLanguageUpdater<WebTextField>
{
    /**
     * Updates component language-dependant variables.
     *
     * @param c     component
     * @param key   language key
     * @param value language value
     * @param data  formatting data
     */
    public void update ( WebTextField c, String key, Value value, Object... data )
    {
        // Field text
        String text = getDefaultText ( value, data );
        if ( text != null )
        {
            c.setText ( text );
        }

        // Field input prompt
        String inputPrompt = getDefaultText ( INPUT_PROMPT, value, data );
        if ( inputPrompt != null )
        {
            c.setInputPrompt ( inputPrompt );
        }
    }
}
