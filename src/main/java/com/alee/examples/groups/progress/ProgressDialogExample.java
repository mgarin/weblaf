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
import com.alee.examples.content.FeatureState;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.window.WebProgressDialog;
import com.alee.laf.button.WebButton;
import com.alee.utils.ThreadUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: mgarin Date: 14.02.12 Time: 12:44
 */

public class ProgressDialogExample extends DefaultExample
{
    public String getTitle ()
    {
        return "Progress dialog";
    }

    public String getDescription ()
    {
        return "WebProgressDialog usage example";
    }

    public FeatureState getFeatureState ()
    {
        return FeatureState.alpha;
    }

    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Progress dialog display button
        WebButton showModalLoad = new WebButton ( "Show dialog", loadIcon ( "dialog.png" ) );
        showModalLoad.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                // Load dialog
                final WebProgressDialog progress = new WebProgressDialog ( owner, "Some progress" );
                progress.setText ( "Loading something..." );

                // Starting updater thread
                new Thread ( new Runnable ()
                {
                    public void run ()
                    {
                        for ( int i = 0; i <= 100; i++ )
                        {
                            ThreadUtils.sleepSafely ( 50 );
                            progress.setProgress ( i );
                            if ( i == 25 )
                            {
                                progress.setText ( "1/4 done" );
                            }
                            else if ( i == 50 )
                            {
                                progress.setText ( "Half done" );
                            }
                            else if ( i == 75 )
                            {
                                progress.setText ( "3/4 done" );
                            }
                        }
                        progress.setText ( "Done! Closing in 3..." );
                        ThreadUtils.sleepSafely ( 1000 );
                        progress.setText ( "Done! Closing in 2..." );
                        ThreadUtils.sleepSafely ( 1000 );
                        progress.setText ( "Done! Closing in 1..." );
                        ThreadUtils.sleepSafely ( 1000 );
                        progress.setVisible ( false );
                    }
                } ).start ();

                // Displaying dialog
                progress.setModal ( true );
                progress.setVisible ( true );
            }
        } );
        return new GroupPanel ( showModalLoad );
    }
}