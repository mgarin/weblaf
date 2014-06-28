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

package com.alee.examples.groups.progressbar;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.examples.content.FeatureState;
import com.alee.extended.panel.GroupPanel;
import com.alee.global.StyleConstants;
import com.alee.laf.progressbar.WebProgressBar;
import com.alee.utils.swing.ComponentUpdater;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: mgarin Date: 16.02.12 Time: 15:41
 */

public class VerticalProgressBarsExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Vertical progress bars";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled vertical progress bars";
    }

    @Override
    public FeatureState getFeatureState ()
    {
        return FeatureState.beta;
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Simple progress bar
        final WebProgressBar progressBar1 = new WebProgressBar ( WebProgressBar.VERTICAL, 0, 100 );
        progressBar1.setValue ( 0 );
        progressBar1.setIndeterminate ( false );
        progressBar1.setStringPainted ( true );

        // Simple progress bar without text
        final WebProgressBar progressBar2 = new WebProgressBar ( WebProgressBar.VERTICAL, 0, 100 );
        progressBar2.setValue ( 0 );
        progressBar2.setIndeterminate ( false );
        progressBar2.setStringPainted ( false );

        // todo Fix text positioning
        // Simple indetrminate progress bar
        //        WebProgressBar progressBar3 = new WebProgressBar ( WebProgressBar.VERTICAL );
        //        progressBar3.setIndeterminate ( true );
        //        progressBar3.setStringPainted ( true );
        //        progressBar3.setString ( "Please wait..." );

        // Simple indetrminate progress bar without text
        WebProgressBar progressBar4 = new WebProgressBar ( WebProgressBar.VERTICAL );
        progressBar4.setIndeterminate ( true );
        progressBar4.setStringPainted ( false );

        GroupPanel view = new GroupPanel ( 4, true, progressBar1, progressBar2, /*progressBar3,*/
                progressBar4 );

        // Progress updater
        ComponentUpdater.install ( view, "VerticalProgressBarsExample.progressUpdater", StyleConstants.animationDelay, new ActionListener ()
        {
            private boolean increasing = true;

            @Override
            public void actionPerformed ( ActionEvent e )
            {
                int value = progressBar1.getValue ();
                if ( increasing )
                {
                    progressBar1.setValue ( value + 1 );
                    progressBar2.setValue ( value + 1 );
                    if ( value + 1 == 100 )
                    {
                        increasing = false;
                    }
                }
                else
                {
                    progressBar1.setValue ( value - 1 );
                    progressBar2.setValue ( value - 1 );
                    if ( value - 1 == 0 )
                    {
                        increasing = true;
                    }
                }
            }
        } );

        return view;
    }
}