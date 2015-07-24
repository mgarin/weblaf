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

package com.alee.examples.groups.overlay;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.examples.content.ExamplesManager;
import com.alee.extended.panel.CenterPanel;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.WebOverlay;
import com.alee.laf.progressbar.WebProgressBar;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextArea;
import com.alee.utils.SwingUtils;
import com.alee.utils.ThreadUtils;
import com.alee.utils.swing.EmptyMouseAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * User: mgarin Date: 08.11.12 Time: 17:00
 */

public class TextAreaOverlayExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Overlayed text area";
    }

    @Override
    public String getDescription ()
    {
        return "Text area overlayed with a progress bar";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Overlay
        final WebOverlay overlayPanel = new WebOverlay ();

        // Progress displayed as overlay
        final WebProgressBar overlay = new WebProgressBar ();
        overlay.setPreferredProgressWidth ( 100 );
        overlay.setStringPainted ( true );

        // Additional centering panel that soaks mouse events to prevent textarea interactions
        final CenterPanel centeredOverlay = new CenterPanel ( overlay );
        centeredOverlay.setVisible ( false );
        EmptyMouseAdapter.install ( centeredOverlay );
        overlayPanel.addOverlay ( centeredOverlay );

        // Overlayed text area
        final WebTextArea component = new WebTextArea ();
        component.setColumns ( 20 );
        component.setRows ( 3 );
        component.setLineWrap ( true );
        component.setWrapStyleWord ( true );
        component.addMouseListener ( new MouseAdapter ()
        {
            private String toAdd = "";
            private int step = 5;

            @Override
            public void mousePressed ( MouseEvent e )
            {
                // Block action on disabled state
                if ( !component.isEnabled () )
                {
                    return;
                }

                // Updating string to write
                if ( toAdd.length () == 0 )
                {
                    toAdd = ExamplesManager.createSmallString ();
                }

                // Disabling textarea editing
                component.setEditable ( false );
                component.setSelectionStart ( component.getText ().length () );
                component.setSelectionEnd ( component.getText ().length () );
                component.setCaretPosition ( component.getText ().length () );

                // Initializing progress
                overlay.setMinimum ( 0 );
                overlay.setValue ( 0 );
                overlay.setMaximum ( toAdd.length () );

                // Displaying overlay
                centeredOverlay.setVisible ( true );

                // Starting update thread
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
                                    component.append ( toAdd.substring ( 0, left ) );
                                    overlay.setValue ( overlay.getValue () + left );
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
                                // Hiding overlay
                                centeredOverlay.setVisible ( false );

                                // Enabling textarea editing
                                component.setEditable ( false );
                            }
                        } );
                    }
                } );
                updater.setDaemon ( true );
                updater.start ();
            }
        } );
        overlayPanel.setComponent ( new WebScrollPane ( component ) );

        return new GroupPanel ( overlayPanel );
    }
}