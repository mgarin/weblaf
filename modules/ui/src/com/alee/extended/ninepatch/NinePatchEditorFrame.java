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

package com.alee.extended.ninepatch;

import com.alee.api.jdk.Supplier;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.rootpane.WindowState;
import com.alee.laf.window.WebFrame;
import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsManager;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.ImageUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

/**
 * Launchable {@link WebFrame} containing {@link NinePatchEditorPanel}.
 *
 * @author Mikle Garin
 * @see NinePatchEditorPanel
 */
public class NinePatchEditorFrame extends WebFrame
{
    protected static final String SETTINGS_GROUP = "NinePatchEditorFrame";
    protected static final String LAST_USED_FILE_KEY = "lastUsedFile";
    protected static final String WINDOW_STATE_KEY = "windowState";

    protected final NinePatchEditorPanel ninePatchEditorPanel;

    public NinePatchEditorFrame ()
    {
        this ( SettingsManager.get ( SETTINGS_GROUP, LAST_USED_FILE_KEY, ( String ) null ) );
    }

    public NinePatchEditorFrame ( final String path )
    {
        super ();
        setIconImages ( WebLookAndFeel.getImages () );
        setLanguage ( "weblaf.ex.npeditor.title", new Supplier<String> ()
        {
            @Override
            public String get ()
            {
                final String imageSrc = ninePatchEditorPanel != null ? ninePatchEditorPanel.getImageSrc () : null;
                return imageSrc != null ? " - [" + imageSrc + "]" : "";
            }
        } );

        getContentPane ().setLayout ( new BorderLayout () );

        // Main editor panel
        ninePatchEditorPanel = new NinePatchEditorPanel ();
        ninePatchEditorPanel.addChangeListener ( new ChangeListener ()
        {
            @Override
            public void stateChanged ( final ChangeEvent e )
            {
                NinePatchEditorFrame.this.updateLanguage ();
            }
        } );

        // Using provided image/directory or loading default image
        if ( path != null )
        {
            final File file = new File ( path );
            if ( file.exists () )
            {
                if ( file.isFile () )
                {
                    ninePatchEditorPanel.openImage ( file );
                }
                else
                {
                    ninePatchEditorPanel.setSelectedDirectory ( file );
                }
            }
            else
            {
                ninePatchEditorPanel.setNinePatchImage ( getSampleImage () );
            }
        }
        else
        {
            ninePatchEditorPanel.setNinePatchImage ( getSampleImage () );
        }

        getContentPane ().add ( ninePatchEditorPanel );

        // Adding close listener
        setDefaultCloseOperation ( JFrame.DO_NOTHING_ON_CLOSE );
        addWindowListener ( new WindowAdapter ()
        {
            @Override
            public void windowClosing ( final WindowEvent e )
            {
                if ( ninePatchEditorPanel.continueAfterSave () )
                {
                    SettingsManager.set ( SETTINGS_GROUP, LAST_USED_FILE_KEY, ninePatchEditorPanel.getImageSrc () );
                    NinePatchEditorFrame.this.dispose ();
                }
            }
        } );

        // Dialog settings registration
        registerSettings ( new Configuration<WindowState> ( SETTINGS_GROUP, WINDOW_STATE_KEY, new WindowState () ) );
    }

    protected BufferedImage getSampleImage ()
    {
        final URL resource = NinePatchEditorFrame.class.getResource ( "icons/example.png" );
        return ImageUtils.getBufferedImage ( resource );
    }

    public NinePatchEditorPanel getNinePatchEditorPanel ()
    {
        return ninePatchEditorPanel;
    }

    public BufferedImage getNinePatchImage ()
    {
        return ninePatchEditorPanel.getNinePatchImage ();
    }

    public static void main ( final String[] args )
    {
        CoreSwingUtils.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                // Installing WebLaF
                WebLookAndFeel.install ();

                // Initializing editor dialog
                final NinePatchEditorFrame npe;
                if ( args != null && args.length > 0 )
                {
                    npe = new NinePatchEditorFrame ( args[ 0 ] );
                }
                else
                {
                    npe = new NinePatchEditorFrame ();
                }
                npe.setVisible ( true );
            }
        } );
    }
}