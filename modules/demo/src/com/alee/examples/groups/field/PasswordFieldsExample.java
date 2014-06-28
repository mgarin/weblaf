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
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.text.WebPasswordField;

import java.awt.*;

/**
 * User: mgarin Date: 23.01.12 Time: 17:15
 */

public class PasswordFieldsExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Password fields";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled password fields";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Simple password field
        WebPasswordField pf1 = new WebPasswordField ( "password", 15 );

        // Non-editable password field
        WebPasswordField pf2 = new WebPasswordField ( "password", 15 );
        pf2.setEditable ( false );

        return new GroupPanel ( false, pf1, pf2 );
    }
}