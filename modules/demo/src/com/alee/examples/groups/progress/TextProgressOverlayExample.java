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
import com.alee.examples.content.ExamplesManager;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.progress.WebProgressOverlay;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextArea;
import com.alee.utils.SwingUtils;
import com.alee.utils.ThreadUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * User: mgarin Date: 07.11.12 Time: 18:07
 */

public class TextProgressOverlayExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Text progress";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled text progress overlay";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Overlay panel
        final WebProgressOverlay progressOverlay = new WebProgressOverlay ();
        progressOverlay.setProgressWidth ( 35 );

        // Overlay content
        final WebTextArea textArea = new WebTextArea ();
        textArea.setText ( "Click to load some text..." );
        textArea.setLineWrap ( true );
        textArea.setWrapStyleWord ( true );
        WebScrollPane scrollPane = new WebScrollPane ( textArea );
        scrollPane.setPreferredSize ( new Dimension ( 250, 100 ) );
        progressOverlay.setComponent ( scrollPane );

        // Overlaying components
        final WebLabel progressLabel = new WebLabel ( "0%", WebLabel.CENTER );
        progressLabel.setBoldFont ();
        progressLabel.setMargin ( 5, 10, 5, 10 );
        final WebPanel overlayPanel = new WebPanel ( true, progressLabel );
        overlayPanel.setShadeWidth ( 5 );
        overlayPanel.setVisible ( false );
        overlayPanel.setPreferredSize ( new Dimension ( 60, 40 ) );
        progressOverlay.addOverlay ( overlayPanel, SwingConstants.CENTER, SwingConstants.CENTER );

        // Animation start action
        textArea.addMouseListener ( new MouseAdapter ()
        {
            private String base = ExamplesManager.createSmallString ();
            private String toAdd = "";
            private int step = 5;

            @Override
            public void mousePressed ( MouseEvent e )
            {
                // Updating string to write
                if ( toAdd.length () == 0 )
                {
                    textArea.clear ();
                    toAdd = base;
                }

                progressOverlay.setShowLoad ( true );
                overlayPanel.setVisible ( true );

                final Thread updater = new Thread ( new Runnable ()
                {
                    @Override
                    public void run ()
                    {
                        while ( toAdd.length () > 0 )
                        {
                            final int left = Math.min ( step, toAdd.length () );
                            SwingUtils.invokeAndWaitSafely ( new Runnable ()
                            {
                                @Override
                                public void run ()
                                {
                                    textArea.append ( toAdd.substring ( 0, left ) );
                                    progressLabel.setText ( ( base.length () - toAdd.length () ) * 100 / base.length () + " %" );
                                }
                            } );
                            toAdd = toAdd.substring ( left );
                            ThreadUtils.sleepSafely ( 50 );
                        }
                        SwingUtilities.invokeLater ( new Runnable ()
                        {
                            @Override
                            public void run ()
                            {
                                progressOverlay.setShowLoad ( false );
                                overlayPanel.setVisible ( false );
                            }
                        } );
                    }
                } );
                updater.setDaemon ( true );
                updater.start ();
            }
        } );

        return new GroupPanel ( progressOverlay );
    }
}