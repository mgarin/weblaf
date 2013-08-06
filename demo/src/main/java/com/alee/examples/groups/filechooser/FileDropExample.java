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
import com.alee.extended.filechooser.WebFileDrop;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.scroll.WebScrollPane;

import java.awt.*;

/**
 * User: mgarin Date: 16.02.12 Time: 18:14
 */

public class FileDropExample extends DefaultExample
{
    public String getTitle ()
    {
        return "File drop area";
    }

    public String getDescription ()
    {
        return "Web-styled file drop area";
    }

    public FeatureState getFeatureState ()
    {
        return FeatureState.beta;
    }

    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Simple file drop area
        WebFileDrop webFileDrop = new WebFileDrop ();

        // File drop area scroll
        WebScrollPane webFilesDropScroll = new WebScrollPane ( webFileDrop );
        webFilesDropScroll.setPreferredSize ( new Dimension ( 300, 150 ) );

        return new GroupPanel ( webFilesDropScroll );
    }
}