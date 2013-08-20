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

package com.alee.laf.filechooser;

import com.alee.extended.filefilter.DefaultFileFilter;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.language.LanguageContainerMethods;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.LanguageMethods;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.utils.CollectionUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.swing.Configurator;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * This JFileChooser extension class provides a direct access to WebFileChooserUI methods.
 * There is also a set of additional methods that allows to modify chooser view and access its components and data directly.
 *
 * @author Mikle Garin
 */

public class WebFileChooser extends JFileChooser implements LanguageMethods, LanguageContainerMethods
{
    /**
     * Constructs a WebFileChooser pointing to the user's default directory.
     */
    public WebFileChooser ()
    {
        super ( WebFileChooserStyle.defaultDirectory );
    }

    /**
     * Constructs a WebFileChooser using the given path.
     * Passing in a null string causes the file chooser to point to the user's default directory.
     *
     * @param currentDirectoryPath a String giving the path to a file or directory
     */
    public WebFileChooser ( String currentDirectoryPath )
    {
        super ( currentDirectoryPath );
    }

    /**
     * Constructs a WebFileChooser using the given File as the path.
     * Passing in a null file causes the file chooser to point to the user's default directory.
     *
     * @param currentDirectory a File object specifying the path to a file or directory
     */
    public WebFileChooser ( File currentDirectory )
    {
        super ( currentDirectory );
    }

    /**
     * Constructs a WebFileChooser using the given FileSystemView.
     */
    public WebFileChooser ( FileSystemView fsv )
    {
        super ( fsv );
    }

    /**
     * Constructs a WebFileChooser using the given current directory and FileSystemView.
     */
    public WebFileChooser ( File currentDirectory, FileSystemView fsv )
    {
        super ( currentDirectory, fsv );
    }

    /**
     * Constructs a WebFileChooser using the given current directory path and FileSystemView.
     */
    public WebFileChooser ( String currentDirectoryPath, FileSystemView fsv )
    {
        super ( currentDirectoryPath, fsv );
    }

    /**
     * Sets dialog title language key.
     *
     * @param dialogTitle title language key
     */
    public void setDialogTitleKey ( String dialogTitle )
    {
        setLanguage ( dialogTitle );
    }

    /**
     * Sets currently displayed directory.
     *
     * @param directoryPath directory to display
     */
    public void setCurrentDirectory ( String directoryPath )
    {
        setCurrentDirectory ( new File ( directoryPath ) );
    }

    /**
     * Sets currently selected file.
     *
     * @param filePath file to select
     */
    public void setSelectedFile ( String filePath )
    {
        setSelectedFile ( new File ( filePath ) );
    }

    /**
     * Returns file chooser panel.
     *
     * @return file chooser panel
     */
    public WebFileChooserPanel getFileChooserPanel ()
    {
        return getWebUI ().getFileChooserPanel ();
    }

    /**
     * Returns list of available file filters.
     *
     * @return list of available file filters
     */
    public List<DefaultFileFilter> getAvailableFilters ()
    {
        return getWebUI ().getAvailableFilters ();
    }

    /**
     * Returns currenly active file filter.
     *
     * @return currenly active file filter
     */
    public DefaultFileFilter getActiveFileFilter ()
    {
        return getWebUI ().getActiveFileFilter ();
    }

    /**
     * Returns whether file thumbnails are generated or not.
     *
     * @return true if file thumbnails are generated, false otherwise
     */
    public boolean isGenerateThumbnails ()
    {
        return getWebUI ().isGenerateThumbnails ();
    }

    /**
     * Sets whether file thumbnails should be generated or not.
     *
     * @param generate whether file thumbnails should be generated or not
     */
    public void setGenerateThumbnails ( boolean generate )
    {
        getWebUI ().setGenerateThumbnails ( generate );
    }

    /**
     * Sets approve button text type.
     *
     * @param approveText approve button text type
     */
    public void setApproveButtonText ( FileApproveText approveText )
    {
        getWebUI ().setApproveButtonText ( approveText );
    }

    /**
     * Sets approve button language key.
     *
     * @param key approve button language key
     */
    public void setApproveButtonLanguage ( String key )
    {
        getWebUI ().setApproveButtonLanguage ( key );
    }

    /**
     * Returns Web-UI applied to this class.
     *
     * @return Web-UI applied to this class
     */
    public WebFileChooserUI getWebUI ()
    {
        return ( WebFileChooserUI ) getUI ();
    }

    /**
     * Installs a Web-UI into this component.
     */
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebFileChooserUI ) )
        {
            try
            {
                setUI ( ( WebFileChooserUI ) ReflectUtils.createInstance ( WebLookAndFeel.fileChooserUI ) );
            }
            catch ( Throwable e )
            {
                e.printStackTrace ();
                setUI ( new WebFileChooserUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    /**
     * Language methods
     */

    /**
     * {@inheritDoc}
     */
    public void setLanguage ( String key, Object... data )
    {
        LanguageManager.registerComponent ( this, key, data );
    }

    /**
     * {@inheritDoc}
     */
    public void updateLanguage ( Object... data )
    {
        LanguageManager.updateComponent ( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public void updateLanguage ( String key, Object... data )
    {
        LanguageManager.updateComponent ( this, key, data );
    }

    /**
     * {@inheritDoc}
     */
    public void removeLanguage ()
    {
        LanguageManager.unregisterComponent ( this );
    }

    /**
     * {@inheritDoc}
     */
    public boolean isLanguageSet ()
    {
        return LanguageManager.isRegisteredComponent ( this );
    }

    /**
     * {@inheritDoc}
     */
    public void setLanguageUpdater ( LanguageUpdater updater )
    {
        LanguageManager.registerLanguageUpdater ( this, updater );
    }

    /**
     * {@inheritDoc}
     */
    public void removeLanguageUpdater ()
    {
        LanguageManager.unregisterLanguageUpdater ( this );
    }

    /**
     * Language container methods
     */

    /**
     * {@inheritDoc}
     */
    public void setLanguageContainerKey ( String key )
    {
        LanguageManager.registerLanguageContainer ( this, key );
    }

    /**
     * {@inheritDoc}
     */
    public void removeLanguageContainerKey ()
    {
        LanguageManager.unregisterLanguageContainer ( this );
    }

    /**
     * {@inheritDoc}
     */
    public String getLanguageContainerKey ()
    {
        return LanguageManager.getLanguageContainerKey ( this );
    }

    /**
     * Constructs and displays file open dialog and returns selected file as a result.
     *
     * @return selected file
     */
    public static File showOpenDialog ()
    {
        return showOpenDialog ( null, null, null );
    }

    /**
     * Constructs and displays file open dialog and returns selected file as a result.
     *
     * @param configurator file chooser configurator
     * @return selected file
     */
    public static File showOpenDialog ( Configurator<WebFileChooser> configurator )
    {
        return showOpenDialog ( null, null, configurator );
    }

    /**
     * Constructs and displays file open dialog and returns selected file as a result.
     *
     * @param parent       parent component
     * @param configurator file chooser configurator
     * @return selected file
     */
    public static File showOpenDialog ( Component parent, Configurator<WebFileChooser> configurator )
    {
        return showOpenDialog ( parent, null, configurator );
    }

    /**
     * Constructs and displays file open dialog and returns selected file as a result.
     *
     * @param currentDirectory current file chooser directory
     * @param configurator     file chooser configurator
     * @return selected file
     */
    public static File showOpenDialog ( String currentDirectory, Configurator<WebFileChooser> configurator )
    {
        return showOpenDialog ( null, currentDirectory, configurator );
    }

    /**
     * Constructs and displays file open dialog and returns selected file as a result.
     *
     * @param parent           parent component
     * @param currentDirectory current file chooser directory
     * @return selected file
     */
    public static File showOpenDialog ( Component parent, String currentDirectory )
    {
        return showOpenDialog ( parent, currentDirectory, null );
    }

    /**
     * Constructs and displays file open dialog and returns selected file as a result.
     *
     * @param parent           parent component
     * @param currentDirectory current file chooser directory
     * @param configurator     file chooser configurator
     * @return selected file
     */
    public static File showOpenDialog ( Component parent, String currentDirectory, Configurator<WebFileChooser> configurator )
    {
        WebFileChooser fileChooser = new WebFileChooser ( currentDirectory );
        fileChooser.setMultiSelectionEnabled ( false );
        if ( configurator != null )
        {
            configurator.configure ( fileChooser );
        }
        if ( fileChooser.showOpenDialog ( parent ) == APPROVE_OPTION )
        {
            return fileChooser.getSelectedFile ();
        }
        else
        {
            return null;
        }
    }

    /**
     * Constructs and displays multiply files open dialog and returns selected files list as a result.
     *
     * @return selected files list
     */
    public static List<File> showMultiOpenDialog ()
    {
        return showMultiOpenDialog ( null, null, null );
    }

    /**
     * Constructs and displays multiply files open dialog and returns selected files list as a result.
     *
     * @param configurator file chooser configurator
     * @return selected files list
     */
    public static List<File> showMultiOpenDialog ( Configurator<WebFileChooser> configurator )
    {
        return showMultiOpenDialog ( null, null, configurator );
    }

    /**
     * Constructs and displays multiply files open dialog and returns selected files list as a result.
     *
     * @param parent       parent component
     * @param configurator file chooser configurator
     * @return selected files list
     */
    public static List<File> showMultiOpenDialog ( Component parent, Configurator<WebFileChooser> configurator )
    {
        return showMultiOpenDialog ( parent, null, configurator );
    }

    /**
     * Constructs and displays multiply files open dialog and returns selected files list as a result.
     *
     * @param currentDirectory current file chooser directory
     * @param configurator     file chooser configurator
     * @return selected files list
     */
    public static List<File> showMultiOpenDialog ( String currentDirectory, Configurator<WebFileChooser> configurator )
    {
        return showMultiOpenDialog ( null, currentDirectory, configurator );
    }

    /**
     * Constructs and displays multiply files open dialog and returns selected files list as a result.
     *
     * @param parent           parent component
     * @param currentDirectory current file chooser directory
     * @return selected files list
     */
    public static List<File> showMultiOpenDialog ( Component parent, String currentDirectory )
    {
        return showMultiOpenDialog ( parent, currentDirectory, null );
    }

    /**
     * Constructs and displays multiply files open dialog and returns selected files list as a result.
     *
     * @param parent           parent component
     * @param currentDirectory current file chooser directory
     * @param configurator     file chooser configurator
     * @return selected files list
     */
    public static List<File> showMultiOpenDialog ( Component parent, String currentDirectory, Configurator<WebFileChooser> configurator )
    {
        WebFileChooser fileChooser = new WebFileChooser ( currentDirectory );
        fileChooser.setMultiSelectionEnabled ( true );
        if ( configurator != null )
        {
            configurator.configure ( fileChooser );
        }
        if ( fileChooser.showOpenDialog ( parent ) == APPROVE_OPTION )
        {
            return CollectionUtils.toList ( fileChooser.getSelectedFiles () );
        }
        else
        {
            return null;
        }
    }

    /**
     * Constructs and displays file save dialog and returns selected file as a result.
     *
     * @return selected file
     */
    public static File showSaveDialog ()
    {
        return showSaveDialog ( null, null, null );
    }

    /**
     * Constructs and displays file save dialog and returns selected file as a result.
     *
     * @param configurator file chooser configurator
     * @return selected file
     */
    public static File showSaveDialog ( Configurator<WebFileChooser> configurator )
    {
        return showSaveDialog ( null, null, configurator );
    }

    /**
     * Constructs and displays file save dialog and returns selected file as a result.
     *
     * @param parent       parent component
     * @param configurator file chooser configurator
     * @return selected file
     */
    public static File showSaveDialog ( Component parent, Configurator<WebFileChooser> configurator )
    {
        return showSaveDialog ( parent, null, configurator );
    }

    /**
     * Constructs and displays file save dialog and returns selected file as a result.
     *
     * @param currentDirectory current file chooser directory
     * @param configurator     file chooser configurator
     * @return selected file
     */
    public static File showSaveDialog ( String currentDirectory, Configurator<WebFileChooser> configurator )
    {
        return showSaveDialog ( null, currentDirectory, configurator );
    }

    /**
     * Constructs and displays file save dialog and returns selected file as a result.
     *
     * @param parent           parent component
     * @param currentDirectory current file chooser directory
     * @return selected file
     */
    public static File showSaveDialog ( Component parent, String currentDirectory )
    {
        return showSaveDialog ( parent, currentDirectory, null );
    }

    /**
     * Constructs and displays file save dialog and returns selected file as a result.
     *
     * @param parent           parent component
     * @param currentDirectory current file chooser directory
     * @param configurator     file chooser configurator
     * @return selected file
     */
    public static File showSaveDialog ( Component parent, String currentDirectory, Configurator<WebFileChooser> configurator )
    {
        WebFileChooser fileChooser = new WebFileChooser ( currentDirectory );
        fileChooser.setMultiSelectionEnabled ( true );
        if ( configurator != null )
        {
            configurator.configure ( fileChooser );
        }
        if ( fileChooser.showSaveDialog ( parent ) == APPROVE_OPTION )
        {
            return fileChooser.getSelectedFile ();
        }
        else
        {
            return null;
        }
    }
}