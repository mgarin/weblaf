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

package com.alee.extended.filechooser;

import com.alee.extended.panel.GroupPanel;
import com.alee.laf.StyleConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyCondition;
import com.alee.managers.hotkey.HotkeyManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mgarin Date: 13.10.11 Time: 15:07
 */

public class WebDirectoryChooser extends WebDialog
{
    private static final ImageIcon ICON = new ImageIcon ( WebDirectoryChooser.class.getResource ( "icons/dir_icon.png" ) );
    private static final ImageIcon SETTINGS_ICON = new ImageIcon ( WebDirectoryChooser.class.getResource ( "icons/settings.png" ) );
    private static final ImageIcon OK_ICON = new ImageIcon ( WebDirectoryChooser.class.getResource ( "icons/ok.png" ) );
    private static final ImageIcon CANCEL_ICON = new ImageIcon ( WebDirectoryChooser.class.getResource ( "icons/cancel.png" ) );

    private List<ActionListener> listeners = new ArrayList<ActionListener> ();
    private WebDirectoryChooserPanel directoryChooserPanel;
    private WebButton ok;
    private WebButton cancel;

    private int result = StyleConstants.NONE_OPTION;

    public WebDirectoryChooser ( Window parent )
    {
        this ( parent, null );
    }

    public WebDirectoryChooser ( Window parent, String title )
    {
        super ( parent, title != null ? title : "" );
        setIconImage ( ICON.getImage () );
        if ( title == null )
        {
            setLanguage ( "weblaf.ex.dirchooser.title" );
        }

        // Hotkeys preview action
        HotkeyManager.installShowAllHotkeysAction ( this, Hotkey.F1 );

        // Default container settings
        getContentPane ().setBackground ( Color.WHITE );
        getContentPane ().setLayout ( new BorderLayout ( 0, 0 ) );

        // Directory chooser itself
        directoryChooserPanel = new WebDirectoryChooserPanel ();
        getContentPane ().add ( directoryChooserPanel, BorderLayout.CENTER );

        // Hotkeys condition
        HotkeyManager.addContainerHotkeyCondition ( this, new HotkeyCondition ()
        {
            public boolean checkCondition ( Component component )
            {
                return directoryChooserPanel.allowHotkeys ();
            }
        } );

        WebPanel buttonsPanel = new WebPanel ();
        buttonsPanel.setOpaque ( false );
        buttonsPanel.setMargin ( 0, 3, 3, 3 );
        buttonsPanel.setLayout ( new BorderLayout ( 0, 0 ) );
        getContentPane ().add ( buttonsPanel, BorderLayout.SOUTH );

        ok = new WebButton ( "", OK_ICON );
        ok.setLanguage ( "weblaf.ex.dirchooser.choose" );
        ok.addHotkey ( WebDirectoryChooser.this, Hotkey.CTRL_ENTER );
        if ( StyleConstants.highlightControlButtons )
        {
            ok.setShineColor ( StyleConstants.greenHighlight );
        }
        ok.putClientProperty ( GroupPanel.FILL_CELL, true );
        ok.setEnabled ( false );
        ok.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                result = StyleConstants.OK_OPTION;
                WebDirectoryChooser.this.dispose ();
            }
        } );

        cancel = new WebButton ( "", CANCEL_ICON );
        cancel.setLanguage ( "weblaf.ex.dirchooser.cancel" );
        cancel.addHotkey ( WebDirectoryChooser.this, Hotkey.ESCAPE );
        if ( StyleConstants.highlightControlButtons )
        {
            cancel.setShineColor ( StyleConstants.redHighlight );
        }
        cancel.putClientProperty ( GroupPanel.FILL_CELL, true );
        cancel.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                result = StyleConstants.CANCEL_OPTION;
                WebDirectoryChooser.this.dispose ();
            }
        } );

        buttonsPanel.add ( new GroupPanel ( 4, ok, cancel ), BorderLayout.LINE_END );

        // Buttons updater
        directoryChooserPanel.addWebDirectoryChooserListener ( new DirectoryChooserListener ()
        {
            public void selectionChanged ( File file )
            {
                updateButtons ( file );
            }
        } );
        updateButtons ( directoryChooserPanel.getSelectedFolder () );

        // Result saver
        addWindowListener ( new WindowAdapter ()
        {
            public void windowClosed ( WindowEvent e )
            {
                result = StyleConstants.CLOSE_OPTION;
            }
        } );

        setModal ( true );
        pack ();
        setDefaultCloseOperation ( JDialog.DISPOSE_ON_CLOSE );
    }

    private void updateButtons ( File file )
    {
        ok.setEnabled ( file != null );
    }

    public int getResult ()
    {
        return result;
    }

    public File getSelectedFolder ()
    {
        return directoryChooserPanel.getSelectedFolder ();
    }

    public void setSelectedFolder ( File selectedFolder )
    {
        directoryChooserPanel.setSelectedFolder ( selectedFolder );
    }

    public void addWebDirectoryChooserListener ( DirectoryChooserListener listener )
    {
        directoryChooserPanel.addWebDirectoryChooserListener ( listener );
    }

    public void removeWebDirectoryChooserListener ( DirectoryChooserListener listener )
    {
        directoryChooserPanel.removeWebDirectoryChooserListener ( listener );
    }

    public int showDialog ()
    {
        setVisible ( true );
        return getResult ();
    }

    public static File showDialog ( Window parent, String title )
    {
        WebDirectoryChooser wdc = new WebDirectoryChooser ( parent, title );
        wdc.setVisible ( true );
        if ( wdc.getResult () == StyleConstants.OK_OPTION )
        {
            return wdc.getSelectedFolder ();
        }
        else
        {
            return null;
        }
    }

    public void setVisible ( boolean b )
    {
        if ( b )
        {
            result = StyleConstants.NONE_OPTION;
            setLocationRelativeTo ( getOwner () );
        }
        super.setVisible ( b );
    }
}
