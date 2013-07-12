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

package com.alee.examples.groups.android;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.image.WebImage;
import com.alee.extended.painter.NinePatchStatePainter;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.text.WebPasswordField;
import com.alee.laf.text.WebTextField;
import com.alee.utils.XmlUtils;

import java.awt.*;

/**
 * User: mgarin Date: 12.03.12 Time: 15:54
 */

public class AndroidFieldsExample extends DefaultExample
{
    public String getTitle ()
    {
        return "Android text fields";
    }

    public String getDescription ()
    {
        return "Android-styled text fields";
    }

    public boolean isFillWidth ()
    {
        return true;
    }

    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Single painter used for all example fields
        NinePatchStatePainter npfbp = XmlUtils.loadNinePatchStatePainter ( getResource ( "field.xml" ) );

        // Text field
        WebTextField textField = new WebTextField ( "Styled text field" );
        textField.setPainter ( npfbp );

        // Password field
        WebPasswordField passwordField = new WebPasswordField ( "Styled password field" );
        passwordField.setPainter ( npfbp );

        // Field with leading component
        WebTextField componentField = new WebTextField ( "Styled text field with leading component" );
        componentField.setLeadingComponent ( new WebImage ( loadIcon ( "search.png" ) ) );
        componentField.setFieldMargin ( 0, 6, 0, 0 );
        componentField.setPainter ( npfbp );

        return new GroupPanel ( false, textField, passwordField, componentField );
    }
}