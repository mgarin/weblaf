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
import com.alee.extended.image.WebImage;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.text.WebPasswordField;
import com.alee.laf.text.WebTextField;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.tooltip.TooltipManager;

import java.awt.*;

/**
 * User: mgarin Date: 23.01.12 Time: 17:22
 */

public class FieldComponentsExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Field components";
    }

    @Override
    public String getDescription ()
    {
        return "Fields with leading/trailing components";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Text field with checkbox as leading component
        WebTextField leading = new WebTextField ( "Leading", 10 );
        leading.setMargin ( 0, 2, 0, 0 );
        WebCheckBox checkBox = new WebCheckBox ();
        checkBox.setCursor ( Cursor.getDefaultCursor () );
        checkBox.setSelected ( true );
        checkBox.setFocusable ( false );
        leading.setLeadingComponent ( checkBox );

        // Text field with image as trailing component
        WebTextField trailing = new WebTextField ( "Trailing", 10 );
        trailing.setMargin ( 0, 0, 0, 2 );
        trailing.setTrailingComponent ( new WebImage ( loadIcon ( "search.png" ) ) );

        // Password field with image as leading component
        WebPasswordField leading2 = new WebPasswordField ( "Leading", 10 );
        leading2.setLeadingComponent ( new WebImage ( loadIcon ( "key.png" ) ) );

        // Password field with image as trailing component
        WebPasswordField trailing2 = new WebPasswordField ( "Trailing", 10 );
        trailing2.setTrailingComponent ( new WebImage ( loadIcon ( "key.png" ) ) );

        // Text field with image as leading and trailing components
        WebTextField both = new WebTextField ( "Both", 10 );
        both.setHorizontalAlignment ( WebTextField.CENTER );
        both.setMargin ( 0, 2, 0, 2 );
        WebImage image1 = new WebImage ( loadIcon ( "exclamation.png" ) );
        image1.setCursor ( Cursor.getDefaultCursor () );
        TooltipManager.setTooltip ( image1, "Some left message", TooltipWay.leading, 0 );
        both.setLeadingComponent ( image1 );
        WebImage image2 = new WebImage ( loadIcon ( "exclamation.png" ) );
        image2.setCursor ( Cursor.getDefaultCursor () );
        TooltipManager.setTooltip ( image2, "Some right message", TooltipWay.trailing, 0 );
        both.setTrailingComponent ( image2 );

        return new GroupPanel ( false, leading, trailing, leading2, trailing2, both );
    }
}