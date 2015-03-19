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

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.language.LanguageContainerMethods;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.LanguageMethods;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.managers.log.Log;
import com.alee.utils.CollectionUtils;
import com.alee.utils.ImageUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.filefilter.AbstractFileFilter;
import com.alee.utils.swing.Customizer;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.util.Arrays;
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
     * Custom icons for file chooser dialog.
     */
    protected List<? extends Image> customIcons = null;

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
    public WebFileChooser ( final String currentDirectoryPath )
    {
        super ( currentDirectoryPath );
    }

    /**
     * Constructs a WebFileChooser using the given File as the path.
     * Passing in a null file causes the file chooser to point to the user's default directory.
     *
     * @param currentDirectory a File object specifying the path to a file or directory
     */
    public WebFileChooser ( final File currentDirectory )
    {
        super ( currentDirectory );
    }

    /**
     * Constructs a WebFileChooser using the given FileSystemView.
     */
    public WebFileChooser ( final FileSystemView fsv )
    {
        super ( fsv );
    }

    /**
     * Constructs a WebFileChooser using the given current directory and FileSystemView.
     */
    public WebFileChooser ( final File currentDirectory, final FileSystemView fsv )
    {
        super ( currentDirectory, fsv );
    }

    /**
     * Constructs a WebFileChooser using the given current directory path and FileSystemView.
     */
    public WebFileChooser ( final String currentDirectoryPath, final FileSystemView fsv )
    {
        super ( currentDirectoryPath, fsv );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JDialog createDialog ( final Component parent ) throws HeadlessException
    {
        final JDialog dialog = super.createDialog ( parent );
        if ( customIcons != null )
        {
            dialog.setIconImages ( customIcons );
        }
        return dialog;
    }

    /**
     * Returns custom dialog icon.
     *
     * @return custom dialog icon
     */
    public Image getDialogIcon ()
    {
        return customIcons != null && customIcons.size () > 0 ? customIcons.get ( 0 ) : null;
    }

    /**
     * Returns custom dialog icons.
     *
     * @return custom dialog icons
     */
    public List<? extends Image> getDialogIcons ()
    {
        return customIcons;
    }

    /**
     * Sets custom dialog icon.
     *
     * @param icon new custom dialog icon
     */
    public void setDialogIcon ( final ImageIcon icon )
    {
        setDialogImage ( icon.getImage () );
    }

    /**
     * Sets custom dialog icon.
     *
     * @param icon new custom dialog icon
     */
    public void setDialogImage ( final Image icon )
    {
        setDialogImages ( Arrays.asList ( icon ) );
    }

    /**
     * Sets custom dialog icons.
     *
     * @param customIcons new custom dialog icons
     */
    public void setDialogIcons ( final List<? extends ImageIcon> customIcons )
    {
        setDialogImages ( ImageUtils.toImagesList ( customIcons ) );
    }

    /**
     * Sets custom dialog icons.
     *
     * @param customIcons new custom dialog icons
     */
    public void setDialogImages ( final List<? extends Image> customIcons )
    {
        this.customIcons = customIcons;

        // Updating icon on displayed dialog
        final Window window = SwingUtils.getWindowAncestor ( this );
        if ( window != null && window instanceof JDialog )
        {
            window.setIconImages ( customIcons );
        }
    }

    /**
     * Sets dialog title language key.
     *
     * @param dialogTitle title language key
     */
    public void setDialogTitleKey ( final String dialogTitle )
    {
        setLanguage ( dialogTitle );
    }

    /**
     * Sets currently displayed directory.
     *
     * @param path directory to display
     */
    public void setCurrentDirectory ( final String path )
    {
        setCurrentDirectory ( path != null ? new File ( path ) : null );
    }

    /**
     * Sets currently selected file.
     *
     * @param path file to select
     */
    public void setSelectedFile ( final String path )
    {
        setSelectedFile ( path != null ? new File ( path ) : null );
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
    public List<AbstractFileFilter> getAvailableFilters ()
    {
        return getWebUI ().getAvailableFilters ();
    }

    /**
     * Returns currenly active file filter.
     *
     * @return currenly active file filter
     */
    public AbstractFileFilter getActiveFileFilter ()
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
    public void setGenerateThumbnails ( final boolean generate )
    {
        getWebUI ().setGenerateThumbnails ( generate );
    }

    /**
     * Sets approve button text type.
     *
     * @param approveText approve button text type
     */
    public void setApproveButtonText ( final FileApproveText approveText )
    {
        getWebUI ().setApproveButtonText ( approveText );
    }

    /**
     * Sets approve button language key.
     *
     * @param key approve button language key
     */
    public void setApproveButtonLanguage ( final String key )
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
    @Override
    public void updateUI ()
    {
        // Removing all files filter
        if ( isAcceptAllFileFilterUsed () )
        {
            removeChoosableFileFilter ( getAcceptAllFileFilter () );
        }

        // Updating the UI itself
        if ( getUI () == null || !( getUI () instanceof WebFileChooserUI ) )
        {
            try
            {
                setUI ( ( WebFileChooserUI ) ReflectUtils.createInstance ( WebLookAndFeel.fileChooserUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebFileChooserUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }

        // Update file view as file chooser was probably deserialized
        if ( getFileSystemView () == null )
        {
            setFileSystemView ( FileSystemView.getFileSystemView () );
        }

        // Updating UI file view for this file chooser
        ReflectUtils.setFieldValueSafely ( this, "uiFileView", getUI ().getFileView ( this ) );

        // Adding all files filter
        if ( isAcceptAllFileFilterUsed () )
        {
            addChoosableFileFilter ( getAcceptAllFileFilter () );
        }
    }

    /**
     * Language methods
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLanguage ( final String key, final Object... data )
    {
        LanguageManager.registerComponent ( this, key, data );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLanguage ( final Object... data )
    {
        LanguageManager.updateComponent ( this, data );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLanguage ( final String key, final Object... data )
    {
        LanguageManager.updateComponent ( this, key, data );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLanguage ()
    {
        LanguageManager.unregisterComponent ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLanguageSet ()
    {
        return LanguageManager.isRegisteredComponent ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLanguageUpdater ( final LanguageUpdater updater )
    {
        LanguageManager.registerLanguageUpdater ( this, updater );
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
    @Override
    public void setLanguageContainerKey ( final String key )
    {
        LanguageManager.registerLanguageContainer ( this, key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLanguageContainerKey ()
    {
        LanguageManager.unregisterLanguageContainer ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
     * @param customizer file chooser customizer
     * @return selected file
     */
    public static File showOpenDialog ( final Customizer<WebFileChooser> customizer )
    {
        return showOpenDialog ( null, null, customizer );
    }

    /**
     * Constructs and displays file open dialog and returns selected file as a result.
     *
     * @param parent     parent component
     * @param customizer file chooser customizer
     * @return selected file
     */
    public static File showOpenDialog ( final Component parent, final Customizer<WebFileChooser> customizer )
    {
        return showOpenDialog ( parent, null, customizer );
    }

    /**
     * Constructs and displays file open dialog and returns selected file as a result.
     *
     * @param currentDirectory current file chooser directory
     * @param customizer       file chooser customizer
     * @return selected file
     */
    public static File showOpenDialog ( final String currentDirectory, final Customizer<WebFileChooser> customizer )
    {
        return showOpenDialog ( null, currentDirectory, customizer );
    }

    /**
     * Constructs and displays file open dialog and returns selected file as a result.
     *
     * @param parent           parent component
     * @param currentDirectory current file chooser directory
     * @return selected file
     */
    public static File showOpenDialog ( final Component parent, final String currentDirectory )
    {
        return showOpenDialog ( parent, currentDirectory, null );
    }

    /**
     * Constructs and displays file open dialog and returns selected file as a result.
     *
     * @param parent           parent component
     * @param currentDirectory current file chooser directory
     * @param customizer       file chooser customizer
     * @return selected file
     */
    public static File showOpenDialog ( final Component parent, final String currentDirectory, final Customizer<WebFileChooser> customizer )
    {
        final WebFileChooser fileChooser = new WebFileChooser ( currentDirectory );
        fileChooser.setMultiSelectionEnabled ( false );
        if ( customizer != null )
        {
            customizer.customize ( fileChooser );
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
     * @param customizer file chooser customizer
     * @return selected files list
     */
    public static List<File> showMultiOpenDialog ( final Customizer<WebFileChooser> customizer )
    {
        return showMultiOpenDialog ( null, null, customizer );
    }

    /**
     * Constructs and displays multiply files open dialog and returns selected files list as a result.
     *
     * @param parent     parent component
     * @param customizer file chooser customizer
     * @return selected files list
     */
    public static List<File> showMultiOpenDialog ( final Component parent, final Customizer<WebFileChooser> customizer )
    {
        return showMultiOpenDialog ( parent, null, customizer );
    }

    /**
     * Constructs and displays multiply files open dialog and returns selected files list as a result.
     *
     * @param currentDirectory current file chooser directory
     * @param customizer       file chooser customizer
     * @return selected files list
     */
    public static List<File> showMultiOpenDialog ( final String currentDirectory, final Customizer<WebFileChooser> customizer )
    {
        return showMultiOpenDialog ( null, currentDirectory, customizer );
    }

    /**
     * Constructs and displays multiply files open dialog and returns selected files list as a result.
     *
     * @param parent           parent component
     * @param currentDirectory current file chooser directory
     * @return selected files list
     */
    public static List<File> showMultiOpenDialog ( final Component parent, final String currentDirectory )
    {
        return showMultiOpenDialog ( parent, currentDirectory, null );
    }

    /**
     * Constructs and displays multiply files open dialog and returns selected files list as a result.
     *
     * @param parent           parent component
     * @param currentDirectory current file chooser directory
     * @param customizer       file chooser customizer
     * @return selected files list
     */
    public static List<File> showMultiOpenDialog ( final Component parent, final String currentDirectory,
                                                   final Customizer<WebFileChooser> customizer )
    {
        final WebFileChooser fileChooser = new WebFileChooser ( currentDirectory );
        fileChooser.setMultiSelectionEnabled ( true );
        if ( customizer != null )
        {
            customizer.customize ( fileChooser );
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
     * @param customizer file chooser customizer
     * @return selected file
     */
    public static File showSaveDialog ( final Customizer<WebFileChooser> customizer )
    {
        return showSaveDialog ( null, null, customizer );
    }

    /**
     * Constructs and displays file save dialog and returns selected file as a result.
     *
     * @param parent     parent component
     * @param customizer file chooser customizer
     * @return selected file
     */
    public static File showSaveDialog ( final Component parent, final Customizer<WebFileChooser> customizer )
    {
        return showSaveDialog ( parent, null, customizer );
    }

    /**
     * Constructs and displays file save dialog and returns selected file as a result.
     *
     * @param currentDirectory current file chooser directory
     * @param customizer       file chooser customizer
     * @return selected file
     */
    public static File showSaveDialog ( final String currentDirectory, final Customizer<WebFileChooser> customizer )
    {
        return showSaveDialog ( null, currentDirectory, customizer );
    }

    /**
     * Constructs and displays file save dialog and returns selected file as a result.
     *
     * @param parent           parent component
     * @param currentDirectory current file chooser directory
     * @return selected file
     */
    public static File showSaveDialog ( final Component parent, final String currentDirectory )
    {
        return showSaveDialog ( parent, currentDirectory, null );
    }

    /**
     * Constructs and displays file save dialog and returns selected file as a result.
     *
     * @param parent           parent component
     * @param currentDirectory current file chooser directory
     * @param customizer       file chooser customizer
     * @return selected file
     */
    public static File showSaveDialog ( final Component parent, final String currentDirectory, final Customizer<WebFileChooser> customizer )
    {
        final WebFileChooser fileChooser = new WebFileChooser ( currentDirectory );
        fileChooser.setMultiSelectionEnabled ( true );
        if ( customizer != null )
        {
            customizer.customize ( fileChooser );
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