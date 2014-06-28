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
import com.alee.laf.text.WebFormattedTextField;

import java.awt.*;
import java.util.Date;

/**
 * User: mgarin Date: 23.01.12 Time: 17:18
 */

public class FormattedTextFieldsExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Formatted text fields";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled formatted text fields";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Date formatted text field
        WebFormattedTextField date = new WebFormattedTextField ();
        date.setValue ( new Date () );
        date.setColumns ( 15 );

        // Integer formatted text field
        WebFormattedTextField integer = new WebFormattedTextField ();
        integer.setValue ( 1024 );
        integer.setColumns ( 15 );

        // Double formatted text field
        WebFormattedTextField pi = new WebFormattedTextField ();
        pi.setValue ( Math.PI );
        pi.setColumns ( 15 );

        return new GroupPanel ( false, date, integer, pi );
    }
}