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

package com.alee.extended.window;

import com.alee.extended.image.WebImage;
import com.alee.extended.painter.AlphaLayerPainter;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * @author Mikle Garin
 */

public class ImagePreviewFrame extends WebFrame
{
    public ImagePreviewFrame ( final String src )
    {
        super ();
        initializeUI ( new WebImage ( src ) );
    }

    public ImagePreviewFrame ( final Class nearClass, final String src )
    {
        super ();
        initializeUI ( new WebImage ( nearClass, src ) );
    }

    public ImagePreviewFrame ( final URL url )
    {
        super ();
        initializeUI ( new WebImage ( url ) );
    }

    public ImagePreviewFrame ( final Icon image )
    {
        super ();
        initializeUI ( new WebImage ( image ) );
    }

    public ImagePreviewFrame ( final ImageIcon image )
    {
        super ();
        initializeUI ( new WebImage ( image ) );
    }

    public ImagePreviewFrame ( final Image image )
    {
        super ();
        initializeUI ( new WebImage ( image ) );
    }

    public ImagePreviewFrame ( final BufferedImage image )
    {
        super ();
        initializeUI ( new WebImage ( image ) );
    }

    private void initializeUI ( final WebImage image )
    {
        setIconImages ( WebLookAndFeel.getImages () );


        setLayout ( new BorderLayout () );

        final WebPanel area = new WebPanel ( new AlphaLayerPainter () );
        area.setMargin ( Math.max ( 5, 80 - image.getWidth () ) );
        add ( area );

        area.add ( image );


        setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
        setResizable ( true );
        pack ();
        center ();
        setVisible ( true );
    }
}