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
import com.alee.laf.spinner.WebSpinner;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;

/**
 * User: mgarin Date: 23.01.12 Time: 17:20
 */

public class SpinnersExample extends DefaultExample
{
    public String getTitle ()
    {
        return "Spinners";
    }

    public String getDescription ()
    {
        return "Web-styled spinners";
    }

    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Integer value spinner
        WebSpinner s1 = new WebSpinner ();
        s1.setValue ( 1024 );

        // Date value spinner
        WebSpinner s2 = new WebSpinner ();
        SpinnerDateModel model = new SpinnerDateModel ();
        model.setCalendarField ( Calendar.YEAR );
        s2.setModel ( model );
        s2.setValue ( new Date () );

        return new GroupPanel ( false, s1, s2 );
    }
}