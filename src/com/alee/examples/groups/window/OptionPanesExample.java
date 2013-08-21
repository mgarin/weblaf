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
import com.alee.laf.optionpane.WebOptionPane;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: mgarin Date: 19.12.12 Time: 19:48
 */

public class OptionPanesExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Option panes";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled option panes decoration";
    }

    @Override
    public FeatureState getFeatureState ()
    {
        return FeatureState.beta;
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        WebButton showFrame = new WebButton ( "Show option pane", loadIcon ( "option.png" ) );
        showFrame.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                // Enabling dialog decoration
                boolean decorateFrames = WebLookAndFeel.isDecorateDialogs ();
                WebLookAndFeel.setDecorateDialogs ( true );

                // Opening dialog
                WebOptionPane.showConfirmDialog ( owner, "Choose one of the options below", "Option pane example",
                        WebOptionPane.YES_NO_CANCEL_OPTION, WebOptionPane.QUESTION_MESSAGE );

                // Restoring frame decoration option
                WebLookAndFeel.setDecorateDialogs ( decorateFrames );
            }
        } );
        return new GroupPanel ( showFrame );
    }
}
