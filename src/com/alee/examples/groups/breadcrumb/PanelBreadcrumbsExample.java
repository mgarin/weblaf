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

package com.alee.examples.groups.breadcrumb;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.breadcrumb.WebBreadcrumb;
import com.alee.extended.breadcrumb.WebBreadcrumbPanel;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.radiobutton.WebRadioButton;
import com.alee.laf.text.WebTextField;
import com.alee.utils.SwingUtils;

import java.awt.*;

/**
 * User: mgarin Date: 25.09.12 Time: 13:42
 */

public class PanelBreadcrumbsExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Panel breadcrumbs";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled panel breadcrumbs";
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Decorated breadcrumb
        final WebBreadcrumb breadcrumb1 = new WebBreadcrumb ( true );
        fillBreadcrumb ( breadcrumb1 );

        // Undecorated breadcrumb
        final WebBreadcrumb breadcrumb2 = new WebBreadcrumb ( false );
        fillBreadcrumb ( breadcrumb2 );

        return new GroupPanel ( 4, false, breadcrumb1, breadcrumb2 );
    }

    public static void fillBreadcrumb ( final WebBreadcrumb b )
    {
        // Radio buttons panel
        final WebBreadcrumbPanel panel1 = new WebBreadcrumbPanel ( new HorizontalFlowLayout ( 4 ) );
        panel1.add ( new WebRadioButton ( "1" ) );
        panel1.add ( new WebRadioButton ( "2" ) );
        panel1.add ( new WebRadioButton ( "3" ) );
        SwingUtils.groupButtons ( panel1 );
        b.add ( panel1 );

        // Label and text field panel
        final WebBreadcrumbPanel panel2 = new WebBreadcrumbPanel ( new HorizontalFlowLayout () );
        panel2.add ( new WebLabel ( "Field:" ) );
        final WebTextField textField = new WebTextField ( 5 );
        SwingUtils.setFontSize ( textField, 8 );
        panel2.add ( textField );
        b.add ( panel2 );

        // Check box panel
        b.add ( new WebBreadcrumbPanel ( new WebCheckBox ( "Check box" ) ) );
    }
}