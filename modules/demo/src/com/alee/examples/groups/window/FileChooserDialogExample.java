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

package com.alee.examples.groups.window;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.examples.content.FeatureState;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.filechooser.WebFileChooser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: mgarin Date: 19.12.12 Time: 16:56
 */

public class FileChooserDialogExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "File chooser dialog";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled file chooser dialog decoration";
    }

    @Override
    public FeatureState getFeatureState ()
    {
        return FeatureState.beta;
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        WebButton showFrame = new WebButton ( "Show file chooser", loadIcon ( "file.png" ) );
        showFrame.addActionListener ( new ActionListener ()
        {
            private WebFileChooser fileChooser = null;

            private WebFileChooser getFileChooser ()
            {
                if ( fileChooser == null )
                {
                    fileChooser = new WebFileChooser ();
                }
                return fileChooser;
            }

            @Override
            public void actionPerformed ( ActionEvent e )
            {
                // Enabling dialog decoration
                boolean decorateFrames = WebLookAndFeel.isDecorateDialogs ();
                WebLookAndFeel.setDecorateDialogs ( true );

                // Opening color chooser dialog
                getFileChooser ().showDialog ( owner, null );

                // Restoring dialog decoration option
                WebLookAndFeel.setDecorateDialogs ( decorateFrames );
            }
        } );
        return new GroupPanel ( showFrame );
    }
}