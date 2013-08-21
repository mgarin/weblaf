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

package com.alee.examples.groups.date;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.date.WebDateField;
import com.alee.extended.panel.GroupPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

/**
 * User: mgarin Date: 20.02.12 Time: 15:17
 */

public class DateFieldExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Date field";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled date field";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Simple date field
        WebDateField dateField = new WebDateField ( new Date () );
        dateField.setHorizontalAlignment ( SwingConstants.CENTER );
        return new GroupPanel ( dateField );
    }
}