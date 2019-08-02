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

package com.alee.extended.filechooser;

import com.alee.managers.language.Language;
import com.alee.managers.language.LanguageUpdater;
import com.alee.managers.language.ToolTipLU;

/**
 * Custom {@link LanguageUpdater} for {@link WebFileDrop}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see com.alee.managers.language.LanguageManager
 */
public class WebFileDropLU extends ToolTipLU<WebFileDrop>
{
    @Override
    public Class getComponentClass ()
    {
        return WebFileDrop.class;
    }

    @Override
    public void update ( final WebFileDrop component, final Language language, final String key, final Object... data )
    {
        super.update ( component, language, key, data );

        if ( language.containsText ( key, DROP_TEXT ) )
        {
            component.setDropText ( language.getState ( key, DROP_TEXT, data ) );
        }
    }
}