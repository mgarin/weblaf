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

package com.alee.laf.combobox;

import com.alee.laf.text.WebTextField;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxEditor;

/**
 * {@link BasicComboBoxEditor} extension that provides a different editor field implementation.
 *
 * @author Mikle Garin
 */

public class WebComboBoxEditor extends BasicComboBoxEditor
{
    @Override
    protected JTextField createEditorComponent ()
    {
        return new TextField ( "", 9 );
    }

    /**
     * Custom {@link TextField}
     */
    public static class TextField extends WebTextField
    {
        /**
         * Constructs new {@link TextField}.
         *
         * @param text    initially displayed text
         * @param columns number of columns used to calculate field preferred width
         */
        public TextField ( final String text, final int columns )
        {
            super ( text, columns );
        }

        @Override
        public void setText ( final String s )
        {
            // workaround for 4530952
            if ( getText ().equals ( s ) )
            {
                return;
            }
            super.setText ( s );
        }
    }

    /**
     * A subclass of {@link WebComboBoxEditor} that implements {@link javax.swing.plaf.UIResource}.
     * It is used to determine editor provided by the UI class to properly uninstall it on UI uninstall.
     */
    public static final class UIResource extends WebComboBoxEditor implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link WebComboBoxEditor}.
         */
    }
}