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

package com.alee.examples.groups.progress;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.image.WebDecoratedImage;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.progress.WebProgressOverlay;
import com.alee.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * User: mgarin Date: 07.11.12 Time: 18:08
 */

public class ImageProgressOverlayExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Image progress";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled image progress overlay";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        final WebProgressOverlay progressOverlay1 = new WebProgressOverlay ();
        progressOverlay1.setProgressColor ( Color.WHITE );
        progressOverlay1.setConsumeEvents ( false );

        WebDecoratedImage ava1 = new WebDecoratedImage ( getIcon ( "ava1.jpg" ) );
        ava1.setDrawGlassLayer ( false, false );
        ava1.setZoomBlur ( true, false );
        ava1.setBlurAlignX ( 0.4f, false );
        ava1.setBlurAlignY ( 0.6f, false );
        ava1.setZoomBlurFactor ( 0.5f, false );
        ava1.setRound ( 10, false );
        ava1.setShadeWidth ( 5 );
        ava1.addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( MouseEvent e )
            {
                progressOverlay1.setShowLoad ( !progressOverlay1.isShowLoad () );
            }
        } );
        progressOverlay1.setComponent ( ava1 );


        final WebProgressOverlay progressOverlay2 = new WebProgressOverlay ();
        progressOverlay2.setProgressColor ( Color.RED );
        progressOverlay2.setConsumeEvents ( false );

        WebDecoratedImage ava2 = new WebDecoratedImage ( getIcon ( "ava2.jpg" ) );
        ava2.setRound ( 0, false );
        ava2.setShadeWidth ( 5 );
        ava2.addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( MouseEvent e )
            {
                progressOverlay2.setShowLoad ( !progressOverlay2.isShowLoad () );
            }
        } );
        progressOverlay2.setComponent ( ava2 );


        final WebProgressOverlay progressOverlay3 = new WebProgressOverlay ();
        progressOverlay3.setProgressColor ( Color.WHITE );
        progressOverlay3.setConsumeEvents ( false );

        WebDecoratedImage ava3 = new WebDecoratedImage ( getIcon ( "ava3.png" ) );
        ava3.setRound ( 10, false );
        ava3.setShadeWidth ( 5 );
        ava3.addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( MouseEvent e )
            {
                progressOverlay3.setShowLoad ( !progressOverlay3.isShowLoad () );
            }
        } );
        progressOverlay3.setComponent ( ava3 );


        final WebProgressOverlay progressOverlay4 = new WebProgressOverlay ();
        progressOverlay4.setProgressColor ( Color.BLACK );
        progressOverlay4.setConsumeEvents ( false );

        WebDecoratedImage ava4 = new WebDecoratedImage ( getIcon ( "ava4.jpg" ) );
        ava4.setRound ( 0, false );
        ava4.setShadeWidth ( 5 );
        ava4.addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( MouseEvent e )
            {
                progressOverlay4.setShowLoad ( !progressOverlay4.isShowLoad () );
            }
        } );
        progressOverlay4.setComponent ( ava4 );


        return new GroupPanel ( progressOverlay1, progressOverlay2, progressOverlay3, progressOverlay4 );
    }

    private ImageIcon getIcon ( String src )
    {
        ImageIcon imageIcon = loadIcon ( src );
        if ( imageIcon.getIconWidth () > 48 )
        {
            imageIcon = ImageUtils.createPreviewIcon ( imageIcon, 48 );
        }
        return imageIcon;
    }
}