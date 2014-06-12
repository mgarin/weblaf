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

package com.alee.examples.groups.gallery;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.examples.content.FeatureState;
import com.alee.extended.image.GalleryTransferHandler;
import com.alee.extended.image.WebImageGallery;
import com.alee.global.GlobalConstants;
import com.alee.global.StyleConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.filechooser.WebFileChooser;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.toolbar.ToolbarStyle;
import com.alee.laf.toolbar.WebToolBar;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * User: mgarin Date: 17.02.12 Time: 14:19
 */

public class GalleryExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Image gallery";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled image gallery";
    }

    @Override
    public FeatureState getFeatureState ()
    {
        return FeatureState.beta;
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        WebPanel imagegalleryPanel = new WebPanel ();
        imagegalleryPanel.setLayout ( new BorderLayout () );

        WebToolBar wigToolBar = new WebToolBar ( WebToolBar.HORIZONTAL );
        wigToolBar.setFloatable ( false );
        wigToolBar.setToolbarStyle ( ToolbarStyle.attached );
        imagegalleryPanel.add ( wigToolBar, BorderLayout.NORTH );

        final WebImageGallery wig = new WebImageGallery ();
        wig.setPreferredColumnCount ( 3 );
        wig.setTransferHandler ( new GalleryTransferHandler ( wig ) );
        wig.addImage ( loadIcon ( "1.jpg" ) );
        wig.addImage ( loadIcon ( "2.jpg" ) );
        wig.addImage ( loadIcon ( "3.jpg" ) );
        wig.addImage ( loadIcon ( "4.jpg" ) );
        wig.addImage ( loadIcon ( "5.jpg" ) );
        imagegalleryPanel.add ( wig.getView ( false ), BorderLayout.CENTER );

        WebButton add = new WebButton ( "Add", loadIcon ( "add.png" ) );
        add.setRound ( StyleConstants.smallRound );
        add.addActionListener ( new ActionListener ()
        {
            private WebFileChooser fileChooser = null;

            @Override
            public void actionPerformed ( ActionEvent e )
            {
                if ( fileChooser == null )
                {
                    fileChooser = new WebFileChooser ();
                    fileChooser.setDialogTitle ( "Choose an image to add" );
                    fileChooser.setMultiSelectionEnabled ( true );
                    fileChooser.setAcceptAllFileFilterUsed ( false );
                    fileChooser.addChoosableFileFilter ( GlobalConstants.IMAGES_FILTER );
                }
                if ( fileChooser.showOpenDialog ( owner ) == WebFileChooser.APPROVE_OPTION )
                {
                    for ( File file : fileChooser.getSelectedFiles () )
                    {
                        wig.addImage ( 0, new ImageIcon ( file.getAbsolutePath () ) );
                    }
                    wig.setSelectedIndex ( 0 );
                }
            }
        } );
        wigToolBar.add ( add );

        WebButton remove = new WebButton ( "Remove", loadIcon ( "remove.png" ) );
        remove.setRound ( StyleConstants.smallRound );
        remove.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                wig.removeImage ( wig.getSelectedIndex () );
            }
        } );
        wigToolBar.add ( remove );

        WebLabel infoLabel = new WebLabel ( "To add an image simply drag it to the gallery", loadIcon ( "info.png" ), WebLabel.CENTER );
        infoLabel.setForeground ( Color.DARK_GRAY );
        infoLabel.setMargin ( 0, 4, 0, 4 );
        wigToolBar.addToEnd ( infoLabel );

        SwingUtils.equalizeComponentsSize ( add, remove );

        return imagegalleryPanel;
    }
}