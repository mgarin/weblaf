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
import com.alee.extended.breadcrumb.WebBreadcrumbLabel;
import com.alee.extended.panel.GroupPanel;

import java.awt.*;

/**
 * User: mgarin Date: 15.03.12 Time: 13:16
 */

public class LabelBreadcrumbsExample extends DefaultExample
{
    public String getTitle ()
    {
        return "Label breadcrumbs";
    }

    public String getDescription ()
    {
        return "Web-styled label breadcrumbs";
    }

    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Decorated breadcrumb
        WebBreadcrumb breadcrumb1 = new WebBreadcrumb ( true );
        fillBreadcrumb ( breadcrumb1 );

        // Undecorated breadcrumb
        WebBreadcrumb breadcrumb2 = new WebBreadcrumb ( false );
        fillBreadcrumb ( breadcrumb2 );

        return new GroupPanel ( 4, false, breadcrumb1, breadcrumb2 );
    }

    private void fillBreadcrumb ( WebBreadcrumb b )
    {
        // Sample breadcrumb data
        b.add ( new WebBreadcrumbLabel ( "1" ) );
        b.add ( new WebBreadcrumbLabel ( "2" ) );
        b.add ( new WebBreadcrumbLabel ( "3" ) );
        b.add ( new WebBreadcrumbLabel ( "4" ) );
    }
}