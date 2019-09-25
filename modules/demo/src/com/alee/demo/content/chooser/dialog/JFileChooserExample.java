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


import com.alee.api.annotations.NotNull;
import com.alee.api.jdk.Function;
import com.alee.demo.DemoApplication;
import com.alee.demo.api.example.*;
import com.alee.demo.api.example.wiki.OracleWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.laf.button.WebButton;
import com.alee.managers.language.LM;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.TextUtils;

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
     * Simple {@link Function} providing {@link File} name.
     */
    public static final Function<File, String> FILE_NAME = new Function<File, String> ()
    {
        @Override
        public String apply ( final File file )
        {
            return file.getName ();
        }
    };

    @NotNull
    @Override
    public String getId ()
    {
        return "jfilechooser";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "filechooser";
    }

    @NotNull
    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.swing;
    }

    @NotNull
    @Override
    public WikiPage getWikiPage ()
    {
        return new OracleWikiPage ( "How to Use File Choosers", "filechooser" );
    }

    @NotNull
    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new OpenFileChooserDialog ( StyleId.filechooser ),
                new SaveFileChooserDialog ( StyleId.filechooser )
        );
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

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            return CollectionUtils.asList ( new WebButton ( getExampleLanguageKey ( "show" ), new ActionListener ()
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

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            return CollectionUtils.asList ( new WebButton ( getExampleLanguageKey ( "show" ), new ActionListener ()
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
        final String key;
        final Object[] data;
        switch ( result )
        {
            case JFileChooser.APPROVE_OPTION:
            {
                final String suffix = fileChooser.isMultiSelectionEnabled () ? ".multi" : ".single";
                final File[] selectedFiles = fileChooser.getSelectedFiles ();
                if ( selectedFiles != null && selectedFiles.length > 0 )
                {
                    key = "result.selected" + suffix;
                    final String files = TextUtils.arrayToString ( ", ", FILE_NAME, selectedFiles );
                    data = new Object[]{ files != null ? TextUtils.shortenText ( files, 100, true ) : "" };
                }
                else
                {
                    key = "result.none" + suffix;
                    data = new Object[ 0 ];
                }
                break;
            }
            case JFileChooser.CANCEL_OPTION:
            {
                key = "result.cancelled";
                data = new Object[ 0 ];
                break;
            }
            default:
            case JFileChooser.ERROR_OPTION:
            {
                key = "result.error";
                data = new Object[ 0 ];
                break;
            }
        }
        final String message = LM.get ( getExampleLanguageKey ( key ), data );
        NotificationManager.showNotification ( source, message );
    }
}