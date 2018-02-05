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

import com.alee.laf.combobox.WebComboBoxRenderer;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.UILanguageManager;

import javax.swing.*;
import java.util.Locale;

/**
 * Custom {@link ListCellRenderer} for {@link LanguageChooser} component.
 *
 * @author Mikle Garin
 */

public class LanguageChooserRenderer extends WebComboBoxRenderer<Locale, JList>
{
    @Override
    protected Icon iconForValue ( final JList list, final Locale value, final int index,
                                  final boolean isSelected, final boolean hasFocus )
    {
        return UILanguageManager.getLocaleIcon ( value );
    }

    @Override
    protected String textForValue ( final JList list, final Locale value, final int index,
                                    final boolean isSelected, final boolean hasFocus )
    {
        return LanguageManager.getLocaleTitle ( value );
    }

    @Override
    protected void updateView ( final JList list, final Locale value, final int index, final boolean isSelected, final boolean hasFocus )
    {
        // Special property for locale value
        // todo Maybe use setLocale ( locale ) instead?
        putClientProperty ( LanguageItemLocale.LOCALE_VALUE_KEY, value );

        // Updating view
        super.updateView ( list, value, index, isSelected, hasFocus );
    }
}