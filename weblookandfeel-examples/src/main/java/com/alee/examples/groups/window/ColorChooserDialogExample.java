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
import com.alee.laf.colorchooser.WebColorChooser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: mgarin Date: 19.12.12 Time: 16:50
 */

public class ColorChooserDialogExample extends DefaultExample
{
    public String getTitle ()
    {
        return "Color chooser dialog";
    }

    public String getDescription ()
    {
        return "Web-styled color chooser dialog decoration";
    }

    public FeatureState getFeatureState ()
    {
        return FeatureState.beta;
    }

    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        WebButton showFrame = new WebButton ( "Show color chooser", loadIcon ( "color.png" ) );
        showFrame.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                // Enabling dialog decoration
                boolean decorateFrames = WebLookAndFeel.isDecorateDialogs ();
                WebLookAndFeel.setDecorateDialogs ( true );

                // Opening color chooser dialog
                WebColorChooser.showDialog ( owner );

                // Restoring dialog decoration option
                WebLookAndFeel.setDecorateDialogs ( decorateFrames );
            }
        } );
        return new GroupPanel ( showFrame );
    }
}
