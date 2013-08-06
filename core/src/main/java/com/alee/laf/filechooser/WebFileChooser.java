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
import com.alee.managers.language.LanguageContainer;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.LanguageMethods;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.utils.ReflectUtils;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.List;

/**
 * This JFileChooser extension class provides a direct access to WebFileChooserUI methods.
 * There is also a set of additional methods that allows to modify chooser view and access its components and data directly.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class WebFileChooser extends JFileChooser implements LanguageMethods, LanguageContainer
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
}