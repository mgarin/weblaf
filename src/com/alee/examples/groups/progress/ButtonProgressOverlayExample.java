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

package com.alee.examples.groups.progress;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.progress.WebProgressOverlay;
import com.alee.laf.button.WebButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: mgarin Date: 07.11.12 Time: 17:59
 */

public class ButtonProgressOverlayExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Button progress";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled button progress overlay";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        final ImageIcon start = loadIcon ( "start.png" );
        final ImageIcon stop = loadIcon ( "stop.png" );

        // Progress overlay
        final WebProgressOverlay progressOverlay = new WebProgressOverlay ();
        progressOverlay.setConsumeEvents ( false );

        // Progress state change button
        final WebButton button = new WebButton ( "Click to start", start );
        button.setRound ( 9 );
        progressOverlay.setComponent ( button );

        // Progress switch
        button.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                boolean showLoad = !progressOverlay.isShowLoad ();

                // Changing progress visibility
                progressOverlay.setShowLoad ( showLoad );

                // Changing buttons text and icons
                button.setText ( showLoad ? "Click to stop" : "Click to start" );
                button.setIcon ( showLoad ? stop : start );
            }
        } );

        return new GroupPanel ( 5, progressOverlay );
    }
}