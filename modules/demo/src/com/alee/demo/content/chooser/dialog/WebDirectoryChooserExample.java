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
import com.alee.extended.filechooser.WebDirectoryChooser;
import com.alee.laf.button.WebButton;
import com.alee.managers.language.LM;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.FileUtils;
import com.alee.utils.filefilter.NonHiddenFilter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebDirectoryChooserExample extends AbstractStylePreviewExample
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
        return "directorychooser";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "directorychooser";
    }

    @NotNull
    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.extended;
    }

    @NotNull
    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new StaticDirectoryChooserDialog ( StyleId.directorychooser ),
                new CustomDirectoryChooserDialog ( StyleId.directorychooser )
        );
    }

    /**
     * Static directory chooser.
     */
    protected class StaticDirectoryChooserDialog extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public StaticDirectoryChooserDialog ( final StyleId styleId )
        {
            super ( WebDirectoryChooserExample.this, "basic", FeatureState.updated, styleId );
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
                    // Displaying directory chooser directly and waiting for selected directory
                    final String title = getPreviewLanguageKey ( "title" );
                    final File file = WebDirectoryChooser.showDialog ( DemoApplication.getInstance (), title );

                    // Displaying result notification
                    showNotification ( ( JComponent ) e.getSource (), file );
                }
            } ) );
        }

        /**
         * Displays directory selection result notification.
         *
         * @param source    event source component
         * @param directory selected directory
         */
        protected void showNotification ( final JComponent source, final File directory )
        {
            final String key;
            final Object[] data;
            if ( directory != null )
            {
                key = "result.selected";
                data = new Object[]{ directory.getAbsolutePath () };
            }
            else
            {
                key = "result.none";
                data = new Object[ 0 ];
            }
            final String message = LM.get ( getExampleLanguageKey ( key ), data );
            NotificationManager.showNotification ( source, message );
        }
    }

    /**
     * Customized directory chooser.
     */
    protected class CustomDirectoryChooserDialog extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public CustomDirectoryChooserDialog ( final StyleId styleId )
        {
            super ( WebDirectoryChooserExample.this, "custom", FeatureState.updated, styleId );
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
                    // Customizing directory chooser
                    final String title = getPreviewLanguageKey ( "title" );
                    final WebDirectoryChooser chooser = new WebDirectoryChooser ( DemoApplication.getInstance (), title );
                    chooser.setFilter ( new NonHiddenFilter () );
                    chooser.setSelectedDirectory ( FileUtils.getUserHome () );

                    // Displaying directory chooser and waiting for result
                    final int result = chooser.showDialog ();

                    // Displaying result notification
                    showNotification ( ( JComponent ) e.getSource (), chooser, result );
                }
            } ) );
        }

        /**
         * Displays directory selection result notification.
         *
         * @param source           event source component
         * @param directoryChooser directory chooser
         * @param result           file chooser result
         */
        protected void showNotification ( final JComponent source, final WebDirectoryChooser directoryChooser, final int result )
        {
            final String key;
            final Object[] data;
            switch ( result )
            {
                case WebDirectoryChooser.OK_OPTION:
                {
                    key = "result.selected";
                    data = new Object[]{ directoryChooser.getSelectedDirectory ().getAbsolutePath () };
                    break;
                }
                case WebDirectoryChooser.CANCEL_OPTION:
                {
                    key = "result.cancelled";
                    data = new Object[ 0 ];
                    break;
                }
                case WebDirectoryChooser.CLOSE_OPTION:
                {
                    key = "result.closed";
                    data = new Object[ 0 ];
                    break;
                }
                default:
                case WebDirectoryChooser.NONE_OPTION:
                {
                    key = "result.none";
                    data = new Object[ 0 ];
                    break;
                }
            }
            final String message = LM.get ( getExampleLanguageKey ( key ), data );
            NotificationManager.showNotification ( source, message );
        }
    }
}