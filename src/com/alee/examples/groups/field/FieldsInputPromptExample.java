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

package com.alee.examples.groups.field;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.date.WebDateField;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.text.WebPasswordField;
import com.alee.laf.text.WebTextField;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 25.01.13 Time: 15:43
 */

public class FieldsInputPromptExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Input prompt";
    }

    @Override
    public String getDescription ()
    {
        return "Input prompt for various fields";
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Text field input prompt
        final WebTextField textField = new WebTextField ( 15 );
        textField.setInputPrompt ( "Enter text..." );
        textField.setInputPromptFont ( textField.getFont ().deriveFont ( Font.ITALIC ) );

        // Password field input prompt
        final WebPasswordField passwordField = new WebPasswordField ( 15 );
        passwordField.setInputPrompt ( "Enter pass..." );
        passwordField.setHideInputPromptOnFocus ( false );

        // Date field input prompt
        final WebDateField dateField = new WebDateField ();
        dateField.setInputPrompt ( "Enter date..." );
        dateField.setInputPromptPosition ( SwingConstants.CENTER );

        return new GroupPanel ( false, textField, passwordField, dateField );
    }
}
