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

package com.alee.managers.language;

import com.alee.managers.language.updaters.*;
import com.alee.utils.SwingUtils;

import java.awt.*;

/**
 * Minor additions over core LanguageManager.
 *
 * @author Mikle Garin
 */

public class WebLanguageManager
{
    /**
     * Manager initialization mark.
     */
    protected static boolean initialized = false;

    /**
     * Initializes LanguageManager settings.
     */
    public static synchronized void initialize ()
    {
        if ( !initialized )
        {
            initialized = true;

            // Initializing LanguageManager if it is not yet initialized
            LanguageManager.initialize ();

            // WebLaF language updaters
            LanguageManager.registerLanguageUpdater ( new WebAbstractButtonLU () );
            LanguageManager.registerLanguageUpdater ( new WebTextFieldLU () );
            LanguageManager.registerLanguageUpdater ( new WebFormattedTextFieldLU () );
            LanguageManager.registerLanguageUpdater ( new WebPasswordFieldLU () );
            LanguageManager.registerLanguageUpdater ( new WebFileDropLU () );
            LanguageManager.registerLanguageUpdater ( new WebCollapsiblePaneLU () );
            LanguageManager.registerLanguageUpdater ( new WebDockableFrameLU () );
            LanguageManager.registerLanguageUpdater ( new WebRootPaneLU () );

            // Tooltip support
            LanguageManager.setTooltipLanguageSupport ( new WeblafTooltipLanguageSupport () );

            // Orientation update on language change if needed
            LanguageManager.addLanguageListener ( new LanguageAdapter ()
            {
                @Override
                public void languageChanged ( final String oldLang, final String newLang )
                {
                    final ComponentOrientation oo = ComponentOrientation.getOrientation ( LanguageManager.getLocale ( oldLang ) );
                    final ComponentOrientation no = ComponentOrientation.getOrientation ( LanguageManager.getLocale ( newLang ) );
                    if ( oo.isLeftToRight () != no.isLeftToRight () )
                    {
                        SwingUtils.updateGlobalOrientations ();
                    }
                }
            } );
        }
    }
}