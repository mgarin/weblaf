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

package com.alee.examples.groups.language;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.data.Value;
import com.alee.managers.language.updaters.DefaultLanguageUpdater;
import com.alee.utils.swing.WindowFollowAdapter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * User: mgarin Date: 29.10.12 Time: 16:01
 */

public class LanguageExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Translation";
    }

    @Override
    public String getDescription ()
    {
        return "Translation using xml language file";
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Loading example language dictionary
        LanguageManager.addDictionary ( getResource ( "language.xml" ) );

        // Adding a custom language setter that supports custom states
        LanguageManager.registerLanguageUpdater ( new MyLabelUpdater () );

        // Creating example UI
        final WebButton myButton = new WebButton ();
        myButton.setLanguage ( "my.button" );
        myButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                final WebDialog myDialog = new WebDialog ( owner );
                myDialog.setLanguage ( "my.dialog.title" );
                WindowFollowAdapter.install ( myDialog, owner );

                final MyLabel myText = new MyLabel ();
                myText.setLanguage ( "my.dialog.text" );
                myDialog.add ( myText );

                myDialog.setSize ( 300, 100 );
                myDialog.setLocationRelativeTo ( myButton );
                myDialog.setVisible ( true );
            }
        } );

        return new GroupPanel ( myButton );
    }

    /**
     * Custom language updater for state support
     */

    public static class MyLabelUpdater extends DefaultLanguageUpdater<MyLabel>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public void update ( final MyLabel c, final String key, final Value value, final Object... data )
        {
            c.setText ( value.getText ( c.isPressed () ? "pressed" : null ) );
        }
    }

    /**
     * Custom label-based component with a new defined "pressed" state
     */

    public static class MyLabel extends WebLabel
    {
        private boolean pressed = false;

        public MyLabel ()
        {
            super ();

            // Configuring label
            setFocusable ( true );
            setFocusTraversalKeysEnabled ( false );
            setHorizontalAlignment ( WebLabel.CENTER );

            // Our specific state change listener
            addKeyListener ( new KeyAdapter ()
            {
                @Override
                public void keyPressed ( final KeyEvent e )
                {
                    if ( Hotkey.TAB.isTriggered ( e ) )
                    {
                        pressed = true;
                        LanguageManager.updateComponent ( MyLabel.this );
                    }
                }

                @Override
                public void keyReleased ( final KeyEvent e )
                {
                    if ( Hotkey.TAB.isTriggered ( e ) )
                    {
                        pressed = false;
                        LanguageManager.updateComponent ( MyLabel.this );
                    }
                }
            } );
        }

        public boolean isPressed ()
        {
            return pressed;
        }
    }
}