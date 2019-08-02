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

package com.alee.extended.language;

import com.alee.laf.combobox.ComboBoxCellParameters;
import com.alee.laf.combobox.WebComboBoxRenderer;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.UILanguageManager;

import javax.swing.*;
import java.util.Locale;

/**
 * Custom {@link ListCellRenderer} for {@link LanguageChooser} component.
 *
 * @author Mikle Garin
 * @see com.alee.painter.decoration.content.LocaleTextContent
 */
public class LanguageChooserRenderer extends WebComboBoxRenderer<Locale, JList, ComboBoxCellParameters<Locale, JList>>
{
    @Override
    protected void updateView ( final ComboBoxCellParameters<Locale, JList> parameters )
    {
        // Changing locale for LocaleTextContent usage
        setLocale ( parameters.value () );

        // Updating view
        super.updateView ( parameters );
    }

    @Override
    protected Icon iconForValue ( final ComboBoxCellParameters<Locale, JList> parameters )
    {
        return UILanguageManager.getLocaleIcon ( parameters.value () );
    }

    @Override
    protected String textForValue ( final ComboBoxCellParameters<Locale, JList> parameters )
    {
        return LanguageManager.getLocaleTitle ( parameters.value () );
    }
}