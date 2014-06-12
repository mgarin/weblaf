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

package com.alee.examples.groups.image;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.image.WebDecoratedImage;
import com.alee.extended.panel.GroupPanel;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 05.06.12 Time: 16:57
 */

public class WebDecoratedImageExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Decorated images";
    }

    @Override
    public String getDescription ()
    {
        return "WebDecoratedImage examples";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        ImageIcon i1 = loadIcon ( "dec1.jpg" );
        ImageIcon i2 = loadIcon ( "dec2.png" );
        ImageIcon i3 = loadIcon ( "dec3.jpg" );

        WebDecoratedImage img1 = new WebDecoratedImage ( i1 );

        WebDecoratedImage img2 = new WebDecoratedImage ( i2 );
        img2.setShadeWidth ( 5 );

        WebDecoratedImage img3 = new WebDecoratedImage ( i1 );
        img3.setGrayscale ( true, false );
        img3.setRound ( 0 );

        WebDecoratedImage img4 = new WebDecoratedImage ( i2 );
        img4.setGrayscale ( true, false );
        img4.setShadeWidth ( 5, false );
        img4.setRound ( 0 );

        WebDecoratedImage img5 = new WebDecoratedImage ( i1 );
        img5.setDrawGlassLayer ( false, false );
        img5.setBlur ( true, false );
        img5.setRound ( 0 );

        WebDecoratedImage img6 = new WebDecoratedImage ( i3 );
        img6.setDrawGlassLayer ( false, false );
        img6.setBlur ( true, false );
        img6.setRound ( 0, false );
        img6.setShadeWidth ( 5 );

        WebDecoratedImage img7 = new WebDecoratedImage ( i1 );
        img7.setDrawGlassLayer ( false, false );
        img7.setRotationBlur ( true, false );
        img7.setZoomBlur ( true );

        WebDecoratedImage img8 = new WebDecoratedImage ( i3 );
        img8.setDrawGlassLayer ( false, false );
        img8.setZoomBlur ( true, false );
        img8.setBlurAlignX ( 0.4f, false );
        img8.setBlurAlignY ( 0.6f, false );
        img8.setZoomBlurFactor ( 0.5f, false );
        img8.setShadeWidth ( 5 );

        return new GroupPanel ( 4, false, new GroupPanel ( 4, img1, img2, img3, img4 ), new GroupPanel ( 4, img5, img6, img7, img8 ) );
    }
}