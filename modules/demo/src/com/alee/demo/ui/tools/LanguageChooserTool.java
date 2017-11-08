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

package com.alee.demo.ui.tools;

import com.alee.demo.skin.DemoStyles;
import com.alee.extended.language.LanguageChooser;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.style.StyleId;

import java.awt.*;

/**
 * {@link com.alee.demo.DemoApplication} language chooser.
 *
 * @author Mikle Garin
 */

public final class LanguageChooserTool extends WebPanel
{
    /**
     * Constructs new {@link LanguageChooserTool}.
     */
    public LanguageChooserTool ()
    {
        super ( StyleId.panelTransparent, new BorderLayout ( 0, 0 ) );

        // Orientation chooser combobox
        final LanguageChooser language = new LanguageChooser ( DemoStyles.toolLangCombobox );
        language.setLanguage ( "demo.tool.language" );
        add ( language, BorderLayout.CENTER );
    }
}