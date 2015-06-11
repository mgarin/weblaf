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

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.managers.language.LanguageKeyListener;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.data.Value;
import com.alee.managers.settings.SettingsManager;
import com.alee.utils.ImageUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Custom dialog that contains nine-patch editor.
 *
 * @author Mikle Garin
 * @see com.alee.extended.ninepatch.NinePatchEditorPanel
 */

public class NinePatchEditorDialog extends WebFrame
{
    /**
     * Dialog title language key.
     */
    private static final String DIALOG_TITLE_KEY = "weblaf.ex.npeditor.title";

    private NinePatchEditorPanel ninePatchEditorPanel = null;

    public NinePatchEditorDialog ()
    {
        this ( SettingsManager.get ( "NinePatchEditorDialog", "lastFile", ( String ) null ) );
    }

    public NinePatchEditorDialog ( final String path )
    {
        super ();
        setIconImages ( WebLookAndFeel.getImages () );

        updateTitle ();
        LanguageManager.addLanguageKeyListener ( DIALOG_TITLE_KEY, new LanguageKeyListener ()
        {
            @Override
            public void languageKeyUpdated ( final String key, final Value value )
            {
                updateTitle ();
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
                updateTitle ();
            }
        } );
        getContentPane ().add ( ninePatchEditorPanel );

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
                openDefault ();
            }
        }
        else
        {
            openDefault ();
        }

        // Setting proper dialog size
        final Rectangle bounds = SettingsManager.get ( "NinePatchEditorDialog", "bounds", ( Rectangle ) null );
        if ( bounds == null )
        {
            pack ();
            setLocationRelativeTo ( null );
        }
        else
        {
            setBounds ( bounds );
        }

        // Adding close listener
        setDefaultCloseOperation ( JFrame.DO_NOTHING_ON_CLOSE );
        addWindowListener ( new WindowAdapter ()
        {
            @Override
            public void windowClosing ( final WindowEvent e )
            {
                if ( ninePatchEditorPanel.continueAfterSave () )
                {
                    SettingsManager.set ( "NinePatchEditorDialog", "lastFile", ninePatchEditorPanel.getImageSrc () );
                    SettingsManager.set ( "NinePatchEditorDialog", "bounds", getBounds () );
                    System.exit ( 0 );
                }
            }
        } );
    }

    private void openDefault ()
    {
        ninePatchEditorPanel
                .setNinePatchImage ( ImageUtils.getBufferedImage ( NinePatchEditorDialog.class.getResource ( "icons/example.png" ) ) );
    }

    private void updateTitle ()
    {
        final String imageSrc = ninePatchEditorPanel != null ? ninePatchEditorPanel.getImageSrc () : null;
        setTitle ( LanguageManager.get ( DIALOG_TITLE_KEY ) + ( imageSrc != null ? " - [" + imageSrc + "]" : "" ) );
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
        SwingUtilities.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                // Installing WebLaF
                WebLookAndFeel.install ();

                // Initializing editor dialog
                final NinePatchEditorDialog npe;
                if ( args != null && args.length > 0 )
                {
                    npe = new NinePatchEditorDialog ( args[ 0 ] );
                }
                else
                {
                    npe = new NinePatchEditorDialog ();
                }
                npe.setVisible ( true );
            }
        } );
    }
}