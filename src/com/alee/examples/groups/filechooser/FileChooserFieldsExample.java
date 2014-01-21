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
import com.alee.extended.filechooser.WebFileChooserField;
import com.alee.extended.panel.GroupPanel;

import java.awt.*;
import java.io.File;

/**
 * User: mgarin Date: 16.02.12 Time: 17:23
 */

public class FileChooserFieldsExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "File chooser fields";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled file chooser fields";
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Simple file chooser field
        final WebFileChooserField fileChooserField1 = new WebFileChooserField ( owner );
        fileChooserField1.setPreferredWidth ( 200 );

        // Single file chooser field with custom root
        final WebFileChooserField fileChooserField2 = new WebFileChooserField ( owner );
        fileChooserField2.setPreferredWidth ( 200 );
        fileChooserField2.setMultiSelectionEnabled ( false );
        fileChooserField2.setShowFileShortName ( false );
        fileChooserField2.setShowRemoveButton ( false );
        fileChooserField2.setSelectedFile ( File.listRoots ()[ 0 ] );

        return new GroupPanel ( 4, false, new GroupPanel ( fileChooserField1 ), new GroupPanel ( fileChooserField2 ) );
    }
}