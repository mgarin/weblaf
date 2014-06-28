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
import com.alee.examples.content.FeatureState;
import com.alee.extended.breadcrumb.WebFileBreadcrumb;
import com.alee.extended.panel.GroupPanel;
import com.alee.utils.FileUtils;

import java.awt.*;

/**
 * User: mgarin Date: 26.06.12 Time: 17:58
 */

public class FileBreadcrumbsExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "File breadcrumbs";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled file breadcrumbs";
    }

    @Override
    public FeatureState getFeatureState ()
    {
        return FeatureState.beta;
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Simple decorated file breadcrumb (by default file system root is used)
        WebFileBreadcrumb dfb = new WebFileBreadcrumb ( FileUtils.getDiskRoots ()[ 0 ] );

        // Simple decorated file breadcrumb (by default file system root is used)
        WebFileBreadcrumb sfb = new WebFileBreadcrumb ();

        // Simple undecorated file breadcrumb (user home used as root
        WebFileBreadcrumb hfb = new WebFileBreadcrumb ( FileUtils.getUserHomePath (), false );
        hfb.setEncloseLastElement ( true );

        return new GroupPanel ( 4, false, new GroupPanel ( dfb ), new GroupPanel ( sfb ), new GroupPanel ( hfb ) );
    }
}