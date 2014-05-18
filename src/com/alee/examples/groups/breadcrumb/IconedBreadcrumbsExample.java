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
import com.alee.extended.breadcrumb.WebBreadcrumbToggleButton;
import com.alee.extended.panel.GroupPanel;
import com.alee.utils.SwingUtils;

import java.awt.*;

/**
 * User: mgarin Date: 15.03.12 Time: 13:08
 */

public class IconedBreadcrumbsExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Iconed breadcrumbs";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled iconed breadcrumbs";
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

    public void fillBreadcrumb ( final WebBreadcrumb b )
    {
        // Sample breadcrumb data
        b.add ( new WebBreadcrumbToggleButton ( "Element", loadIcon ( "numbers/0.png" ) ) );
        b.add ( new WebBreadcrumbToggleButton ( "Element", loadIcon ( "numbers/1.png" ) ) );
        b.add ( new WebBreadcrumbToggleButton ( "Element", loadIcon ( "numbers/2.png" ) ) );
        b.add ( new WebBreadcrumbToggleButton ( "Element", loadIcon ( "numbers/3.png" ) ) );
        SwingUtils.groupButtons ( b );
    }
}