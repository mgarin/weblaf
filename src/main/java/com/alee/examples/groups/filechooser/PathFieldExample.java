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

package com.alee.examples.groups.filechooser;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.examples.content.FeatureState;
import com.alee.extended.filechooser.WebPathField;
import com.alee.extended.panel.GroupPanel;
import com.alee.utils.FileUtils;

import java.awt.*;

/**
 * User: mgarin Date: 16.02.12 Time: 17:10
 */

public class PathFieldExample extends DefaultExample
{
    public String getTitle ()
    {
        return "Path field";
    }

    public String getDescription ()
    {
        return "Web-styled path field";
    }

    public FeatureState getFeatureState ()
    {
        return FeatureState.beta;
    }

    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Simple path field
        WebPathField pathField = new WebPathField ( FileUtils.getDiskRoots ()[ 0 ] );
        pathField.setPreferredWidth ( 200 );
        return new GroupPanel ( pathField );
    }
}