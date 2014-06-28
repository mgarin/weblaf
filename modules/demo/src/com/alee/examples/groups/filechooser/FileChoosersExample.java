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
import com.alee.extended.panel.GroupPanel;
import com.alee.global.GlobalConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.filechooser.WebFileChooser;
import com.alee.utils.FileUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * User: mgarin Date: 16.02.12 Time: 17:40
 */

public class FileChoosersExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "File choosers";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled file choosers";
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Simple file chooser example
        final WebButton fileChooserButton1 = new WebButton ( "Choose any file..." );
        fileChooserButton1.addActionListener ( new ActionListener ()
        {
            private WebFileChooser fileChooser = null;
            private File file = null;

            @Override
            public void actionPerformed ( ActionEvent e )
            {
                if ( fileChooser == null )
                {
                    fileChooser = new WebFileChooser ();
                    fileChooser.setMultiSelectionEnabled ( false );
                }
                if ( file != null )
                {
                    fileChooser.setSelectedFile ( file );
                }
                if ( fileChooser.showOpenDialog ( owner ) == WebFileChooser.APPROVE_OPTION )
                {
                    file = fileChooser.getSelectedFile ();
                    fileChooserButton1.setIcon ( FileUtils.getFileIcon ( file ) );
                    fileChooserButton1.setText ( FileUtils.getDisplayFileName ( file ) );
                }
            }
        } );

        // Images only chooser
        final WebButton fileChooserButton2 = new WebButton ( "Choose any image..." );
        fileChooserButton2.addActionListener ( new ActionListener ()
        {
            private WebFileChooser imageChooser = null;
            private File file = null;

            @Override
            public void actionPerformed ( ActionEvent e )
            {
                if ( imageChooser == null )
                {
                    imageChooser = new WebFileChooser ();
                    imageChooser.setMultiSelectionEnabled ( false );
                    imageChooser.setAcceptAllFileFilterUsed ( false );
                    imageChooser.addChoosableFileFilter ( GlobalConstants.IMAGES_FILTER );
                }
                if ( file != null )
                {
                    imageChooser.setSelectedFile ( file );
                }
                if ( imageChooser.showOpenDialog ( owner ) == WebFileChooser.APPROVE_OPTION )
                {
                    file = imageChooser.getSelectedFile ();
                    fileChooserButton2.setIcon ( FileUtils.getFileIcon ( file ) );
                    fileChooserButton2.setText ( FileUtils.getDisplayFileName ( file ) );
                }
            }
        } );

        return new GroupPanel ( 4, false, new GroupPanel ( fileChooserButton1 ), new GroupPanel ( fileChooserButton2 ) );
    }
}