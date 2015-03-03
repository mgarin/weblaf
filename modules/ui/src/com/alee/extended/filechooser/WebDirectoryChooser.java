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
import com.alee.global.StyleConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyCondition;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.utils.SwingUtils;
import com.alee.utils.filefilter.AbstractFileFilter;
import com.alee.utils.swing.DialogOptions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

/**
 * This custom component provides a dialog shell for WebDirectoryChooserPanel component.
 *
 * @author Mikle Garin
 */

public class WebDirectoryChooser extends WebDialog implements DialogOptions
{
    /**
     * Icons.
     */
    protected static final ImageIcon ICON = new ImageIcon ( WebDirectoryChooser.class.getResource ( "icons/dir_icon.png" ) );
    protected static final ImageIcon SETTINGS_ICON = new ImageIcon ( WebDirectoryChooser.class.getResource ( "icons/settings.png" ) );
    protected static final ImageIcon OK_ICON = new ImageIcon ( WebDirectoryChooser.class.getResource ( "icons/ok.png" ) );
    protected static final ImageIcon CANCEL_ICON = new ImageIcon ( WebDirectoryChooser.class.getResource ( "icons/cancel.png" ) );

    /**
     * UI components.
     */
    protected WebDirectoryChooserPanel directoryChooserPanel;
    protected WebButton approveButton;
    protected WebButton cancelButton;

    /**
     * Dialog result.
     */
    protected int result = NONE_OPTION;

    /**
     * Constructs new directory chooser dialog with the specified parent window.
     *
     * @param parent parent window
     */
    public WebDirectoryChooser ( final Window parent )
    {
        this ( parent, null );
    }

    /**
     * Constructs new directory chooser dialog with the specified parent window and title.
     *
     * @param parent parent window
     * @param title  dialog title
     */
    public WebDirectoryChooser ( final Window parent, final String title )
    {
        super ( parent, title != null ? title : "weblaf.ex.dirchooser.title" );
        setIconImage ( ICON.getImage () );

        // Hotkeys preview action
        HotkeyManager.installShowAllHotkeysAction ( getRootPane (), Hotkey.F1 );

        // Default container settings
        getContentPane ().setLayout ( new BorderLayout ( 0, 0 ) );

        // Directory chooser itself
        directoryChooserPanel = new WebDirectoryChooserPanel ();
        getContentPane ().add ( directoryChooserPanel, BorderLayout.CENTER );

        // Hotkeys condition
        HotkeyManager.addContainerHotkeyCondition ( getRootPane (), new HotkeyCondition ()
        {
            @Override
            public boolean checkCondition ( final Component component )
            {
                return directoryChooserPanel.allowHotkeys ();
            }
        } );

        final WebPanel buttonsPanel = new WebPanel ();
        buttonsPanel.setMargin ( 0, 3, 3, 3 );
        buttonsPanel.setLayout ( new BorderLayout ( 0, 0 ) );
        getContentPane ().add ( buttonsPanel, BorderLayout.SOUTH );

        approveButton = new WebButton ( "", OK_ICON );
        approveButton.setLanguage ( "weblaf.ex.dirchooser.choose" );
        approveButton.addHotkey ( WebDirectoryChooser.this, Hotkey.CTRL_ENTER );
        approveButton.setRolloverShine ( StyleConstants.highlightControlButtons );
        approveButton.setShineColor ( StyleConstants.greenHighlight );
        approveButton.setEnabled ( false );
        approveButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                result = OK_OPTION;
                WebDirectoryChooser.this.dispose ();
            }
        } );

        cancelButton = new WebButton ( "", CANCEL_ICON );
        cancelButton.setLanguage ( "weblaf.ex.dirchooser.cancel" );
        cancelButton.addHotkey ( WebDirectoryChooser.this, Hotkey.ESCAPE );
        cancelButton.setRolloverShine ( StyleConstants.highlightControlButtons );
        cancelButton.setShineColor ( StyleConstants.redHighlight );
        cancelButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                result = CANCEL_OPTION;
                WebDirectoryChooser.this.dispose ();
            }
        } );

        buttonsPanel.add ( new GroupPanel ( 4, approveButton, cancelButton ), BorderLayout.LINE_END );

        // For proper equal sizing of control buttons
        SwingUtils.equalizeComponentsSize ( approveButton, cancelButton );
        final PropertyChangeListener pcl = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                approveButton.setPreferredSize ( null );
                cancelButton.setPreferredSize ( null );
                SwingUtils.equalizeComponentsSize ( approveButton, cancelButton );
                buttonsPanel.revalidate ();
            }
        };
        approveButton.addPropertyChangeListener ( AbstractButton.TEXT_CHANGED_PROPERTY, pcl );
        cancelButton.addPropertyChangeListener ( AbstractButton.TEXT_CHANGED_PROPERTY, pcl );

        // Buttons updater
        directoryChooserPanel.addDirectoryChooserListener ( new DirectoryChooserListener ()
        {
            @Override
            public void selectionChanged ( final File file )
            {
                updateButtonsState ( file );
            }
        } );
        updateButtonsState ( directoryChooserPanel.getSelectedDirectory () );

        // Result saver
        addWindowListener ( new WindowAdapter ()
        {
            @Override
            public void windowClosed ( final WindowEvent e )
            {
                result = CLOSE_OPTION;
            }
        } );

        setModal ( true );
        pack ();
        setDefaultCloseOperation ( JDialog.DISPOSE_ON_CLOSE );
    }

    /**
     * Forces buttons update according to selected file.
     *
     * @param file newly selected file
     */
    protected void updateButtonsState ( final File file )
    {
        approveButton.setEnabled ( file != null );
    }

    /**
     * Returns directory chooser file filter.
     *
     * @return directory chooser file filter
     */
    public AbstractFileFilter getFilter ()
    {
        return directoryChooserPanel.getFilter ();
    }

    /**
     * Sets directory chooser file filter.
     *
     * @param filter directory chooser file filter
     */
    public void setFilter ( final AbstractFileFilter filter )
    {
        directoryChooserPanel.setFilter ( filter );
    }

    /**
     * Returns dialog result.
     *
     * @return dialog result
     */
    public int getResult ()
    {
        return result;
    }

    /**
     * Returns currently selected directory.
     *
     * @return currently selected directory
     */
    public File getSelectedDirectory ()
    {
        return directoryChooserPanel.getSelectedDirectory ();
    }

    /**
     * Sets currently selected directory.
     *
     * @param selectedDirectory currently selected directory
     */
    public void setSelectedDirectory ( final File selectedDirectory )
    {
        directoryChooserPanel.setSelectedDirectory ( selectedDirectory );
    }

    /**
     * Adds directory chooser listener.
     *
     * @param listener directory chooser listener to add
     */
    public void addDirectoryChooserListener ( final DirectoryChooserListener listener )
    {
        directoryChooserPanel.addDirectoryChooserListener ( listener );
    }

    /**
     * Removes directory chooser listener.
     *
     * @param listener directory chooser listener to remove
     */
    public void removeDirectoryChooserListener ( final DirectoryChooserListener listener )
    {
        directoryChooserPanel.removeDirectoryChooserListener ( listener );
    }

    /**
     * Displays directory chooser dialog and returns its result.
     *
     * @return directory chooser dialog result
     */
    public int showDialog ()
    {
        setVisible ( true );
        return getResult ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setVisible ( final boolean b )
    {
        if ( b )
        {
            result = NONE_OPTION;
            setLocationRelativeTo ( getOwner () );
        }
        super.setVisible ( b );
    }

    /**
     * Displays directory chooser dialog with the specified parent window.
     * Returns selected directory as a result.
     *
     * @param parent parent window
     * @return selected directory
     */
    public static File showDialog ( final Window parent )
    {
        return showDialog ( parent, null, null );
    }

    /**
     * Displays directory chooser dialog with the specified parent window and title.
     * Returns selected directory as a result.
     *
     * @param parent parent window
     * @param title  dialog title
     * @return selected directory
     */
    public static File showDialog ( final Window parent, final String title )
    {
        return showDialog ( parent, title, null );
    }

    /**
     * Displays directory chooser dialog with the specified parent window and title.
     * Returns selected directory as a result.
     *
     * @param parent    parent window
     * @param directory initially selected directory
     * @return selected directory
     */
    public static File showDialog ( final Window parent, final File directory )
    {
        return showDialog ( parent, null, directory );
    }

    /**
     * Displays directory chooser dialog with the specified parent window and title.
     * Returns selected directory as a result.
     *
     * @param parent    parent window
     * @param title     dialog title
     * @param directory initially selected directory
     * @return selected directory
     */
    public static File showDialog ( final Window parent, final String title, final File directory )
    {
        final WebDirectoryChooser wdc = new WebDirectoryChooser ( parent, title );
        if ( directory != null )
        {
            wdc.setSelectedDirectory ( directory );
        }
        wdc.setVisible ( true );
        return wdc.getResult () == OK_OPTION ? wdc.getSelectedDirectory () : null;
    }
}