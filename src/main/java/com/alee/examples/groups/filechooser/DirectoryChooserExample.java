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
import com.alee.extended.filechooser.WebDirectoryChooser;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.StyleConstants;
import com.alee.laf.button.WebButton;
import com.alee.utils.FileUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * User: mgarin Date: 16.02.12 Time: 18:07
 */

public class DirectoryChooserExample extends DefaultExample
{
    public String getTitle ()
    {
        return "Directory chooser";
    }

    public String getDescription ()
    {
        return "Web-styled directory chooser";
    }

    public FeatureState getFeatureState ()
    {
        return FeatureState.beta;
    }

    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Simple directory chooser
        final WebButton directoryChooserButton = new WebButton ( "Choose any directory..." );
        directoryChooserButton.addActionListener ( new ActionListener ()
        {
            private WebDirectoryChooser directoryChooser = null;

            public void actionPerformed ( ActionEvent e )
            {
                if ( directoryChooser == null )
                {
                    directoryChooser = new WebDirectoryChooser ( owner, "Choose any directory" );
                }
                directoryChooser.setVisible ( true );

                if ( directoryChooser.getResult () == StyleConstants.OK_OPTION )
                {
                    File file = directoryChooser.getSelectedFolder ();
                    directoryChooserButton.setIcon ( FileUtils.getFileIcon ( file ) );
                    directoryChooserButton.setText ( FileUtils.getDisplayFileName ( file ) );
                }
            }
        } );
        return new GroupPanel ( directoryChooserButton );
    }
}