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
import com.alee.extended.layout.TableLayout;
import com.alee.extended.panel.CenterPanel;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.text.WebPasswordField;
import com.alee.laf.text.WebTextField;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.utils.SwingUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: mgarin Date: 19.12.12 Time: 19:21
 */

public class WebDialogExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Dialog";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled dialog decoration";
    }

    @Override
    public FeatureState getFeatureState ()
    {
        return FeatureState.beta;
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        WebButton showFrame = new WebButton ( "Show dialog", loadIcon ( "dialog.png" ) );
        showFrame.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                // Enabling dialog decoration
                boolean decorateFrames = WebLookAndFeel.isDecorateDialogs ();
                WebLookAndFeel.setDecorateDialogs ( true );

                // Opening dialog
                ExampleDialog exampleDialog = new ExampleDialog ( owner );
                exampleDialog.pack ();
                exampleDialog.setLocationRelativeTo ( owner );
                exampleDialog.setVisible ( true );

                // Restoring frame decoration option
                WebLookAndFeel.setDecorateDialogs ( decorateFrames );
            }
        } );
        return new GroupPanel ( showFrame );
    }

    private class ExampleDialog extends WebDialog
    {
        public ExampleDialog ( Window owner )
        {
            super ( owner, "Example dialog" );
            setIconImages ( WebLookAndFeel.getImages () );
            setDefaultCloseOperation ( WebDialog.DISPOSE_ON_CLOSE );
            setResizable ( false );
            setModal ( true );

            TableLayout layout = new TableLayout ( new double[][]{ { TableLayout.PREFERRED, TableLayout.FILL },
                    { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED } } );
            layout.setHGap ( 5 );
            layout.setVGap ( 5 );
            WebPanel content = new WebPanel ( layout );
            content.setMargin ( 15, 30, 15, 30 );
            content.setOpaque ( false );

            content.add ( new WebLabel ( "Login", WebLabel.TRAILING ), "0,0" );
            content.add ( new WebTextField ( 15 ), "1,0" );

            content.add ( new WebLabel ( "Password", WebLabel.TRAILING ), "0,1" );
            content.add ( new WebPasswordField ( 15 ), "1,1" );

            WebButton login = new WebButton ( "Login" );
            WebButton cancel = new WebButton ( "Cancel" );
            ActionListener listener = new ActionListener ()
            {
                @Override
                public void actionPerformed ( ActionEvent e )
                {
                    setVisible ( false );
                }
            };
            login.addActionListener ( listener );
            cancel.addActionListener ( listener );
            content.add ( new CenterPanel ( new GroupPanel ( 5, login, cancel ) ), "0,2,1,2" );
            SwingUtils.equalizeComponentsWidths ( login, cancel );

            add ( content );

            HotkeyManager.registerHotkey ( this, login, Hotkey.ESCAPE );
            HotkeyManager.registerHotkey ( this, login, Hotkey.ENTER );
        }
    }
}
