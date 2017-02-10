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

package com.alee.demo.content.chooser.dialog;


import com.alee.demo.DemoApplication;
import com.alee.demo.api.example.*;
import com.alee.demo.api.example.wiki.OracleWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.laf.button.WebButton;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.TextUtils;
import com.alee.utils.text.TextProvider;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

/**
 * @author Michka Popoff
 */

public class JFileChooserExample extends AbstractStylePreviewExample
{
    /**
     * Simple text provider for file name.
     */
    public static final TextProvider<File> FILE_NAME_PROVIDER = new TextProvider<File> ()
    {
        @Override
        public String getText ( final File file )
        {
            return file.getName ();
        }
    };

    @Override
    public String getId ()
    {
        return "jfilechooser";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "filechooser";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.swing;
    }

    @Override
    public WikiPage getWikiPage ()
    {
        return new OracleWikiPage ( "How to Use File Choosers", "filechooser" );
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        final OpenFileChooserDialog open = new OpenFileChooserDialog ( StyleId.filechooser );
        final SaveFileChooserDialog save = new SaveFileChooserDialog ( StyleId.filechooser );
        return CollectionUtils.<Preview>asList ( open, save );
    }

    /**
     * File Chooser dialog preview (open).
     */
    protected class OpenFileChooserDialog extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public OpenFileChooserDialog ( final StyleId styleId )
        {
            super ( JFileChooserExample.this, "open", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            return CollectionUtils.asList ( new WebButton ( getExampleLanguagePrefix () + "show", new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    // Displaying file chooser
                    final JFileChooser fileChooser = new JFileChooser ();
                    fileChooser.setMultiSelectionEnabled ( true );
                    final int result = fileChooser.showOpenDialog ( DemoApplication.getInstance () );

                    // Displaying result notification
                    showNotification ( ( JComponent ) e.getSource (), fileChooser, result );
                }
            } ) );
        }
    }

    /**
     * File Chooser dialog preview (save).
     */
    protected class SaveFileChooserDialog extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public SaveFileChooserDialog ( final StyleId styleId )
        {
            super ( JFileChooserExample.this, "save", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            return CollectionUtils.asList ( new WebButton ( getExampleLanguagePrefix () + "show", new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    // Displaying file chooser
                    final JFileChooser fileChooser = new JFileChooser ();
                    final int result = fileChooser.showSaveDialog ( DemoApplication.getInstance () );

                    // Displaying result notification
                    showNotification ( ( JComponent ) e.getSource (), fileChooser, result );
                }
            } ) );
        }
    }

    /**
     * Displays file selection result notification.
     *
     * @param source      event source component
     * @param fileChooser file chooser
     * @param result      file chooser result
     */
    protected void showNotification ( final JComponent source, final JFileChooser fileChooser, final int result )
    {
        final String languagePrefix = getExampleLanguagePrefix ();
        final String msg;
        switch ( result )
        {
            case JFileChooser.APPROVE_OPTION:
            {
                final String suffix = fileChooser.isMultiSelectionEnabled () ? ".multi" : ".single";
                final File[] selectedFiles = fileChooser.getSelectedFiles ();
                if ( selectedFiles != null && selectedFiles.length > 0 )
                {
                    final String files = TextUtils.arrayToString ( selectedFiles, FILE_NAME_PROVIDER, ", " );
                    msg = LanguageManager.get ( languagePrefix + "result.selected" + suffix, files );
                }
                else
                {
                    msg = LanguageManager.get ( languagePrefix + "result.none" + suffix );
                }
                break;
            }
            case JFileChooser.CANCEL_OPTION:
            {
                msg = LanguageManager.get ( languagePrefix + "result.cancelled" );
                break;
            }
            default:
            case JFileChooser.ERROR_OPTION:
            {
                msg = LanguageManager.get ( languagePrefix + "result.error" );
                break;
            }
        }
        NotificationManager.showNotification ( source, msg );
    }
}