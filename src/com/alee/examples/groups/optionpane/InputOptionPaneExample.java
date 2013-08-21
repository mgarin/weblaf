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

package com.alee.examples.groups.optionpane;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.button.WebButton;
import com.alee.laf.optionpane.WebOptionPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: mgarin Date: 30.01.12 Time: 14:46
 */

public class InputOptionPaneExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Input optionpane";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled input optionpane";
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        WebButton message = new WebButton ( "Show input", loadIcon ( "input.png" ) );
        message.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                WebOptionPane.showInputDialog ( owner, "Write something here:", "Input", JOptionPane.QUESTION_MESSAGE, null, null,
                        "Some default text" );
            }
        } );
        return new GroupPanel ( message );
    }
}