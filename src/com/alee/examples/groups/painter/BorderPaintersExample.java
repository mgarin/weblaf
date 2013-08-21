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

package com.alee.examples.groups.painter;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.painter.BorderPainter;
import com.alee.extended.painter.DashedBorderPainter;
import com.alee.extended.painter.TitledBorderPainter;
import com.alee.extended.panel.GroupPanel;
import com.alee.graphics.strokes.ZigzagStroke;
import com.alee.laf.label.WebLabel;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 13.09.12 Time: 18:47
 */

public class BorderPaintersExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Border painters";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled border painters";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Simple BorderPainter
        WebLabel l1 = new WebLabel ( "Simple border painter" );
        l1.setMargin ( 10 );
        l1.setPainter ( new BorderPainter () );

        // Customized BorderPainter
        WebLabel l2 = new WebLabel ( "Customized border painter" );
        l2.setMargin ( 10 );
        BorderPainter bp2 = new BorderPainter ();
        bp2.setRound ( 12 );
        bp2.setWidth ( 2 );
        bp2.setColor ( new Color ( 39, 95, 173 ) );
        l2.setPainter ( bp2 );

        // Simple DashedBorderPainter
        WebLabel l3 = new WebLabel ( "Dashed border painter" );
        l3.setMargin ( 10 );
        l3.setPainter ( new DashedBorderPainter () );

        // Customized DashedBorderPainter
        WebLabel l4 = new WebLabel ( "Customized dashed border painter" );
        l4.setMargin ( 10 );
        DashedBorderPainter bp4 = new DashedBorderPainter ( new float[]{ 3f, 3f } );
        bp4.setRound ( 12 );
        bp4.setWidth ( 2 );
        bp4.setColor ( new Color ( 39, 95, 173 ) );
        l4.setPainter ( bp4 );

        // Simple TitledBorderPainter
        WebLabel l5 = new WebLabel ( "Titled border painter" );
        l5.setMargin ( 5 );
        l5.setPainter ( new TitledBorderPainter ( "Title" ) );

        // Customized TitledBorderPainter
        WebLabel l6 = new WebLabel ( "Customized titled border painter" );
        l6.setMargin ( 5 );
        TitledBorderPainter bp6 = new TitledBorderPainter ( "Title", SwingConstants.CENTER );
        bp6.setWidth ( 5 );
        bp6.setRound ( 12 );
        bp6.setStroke ( new ZigzagStroke ( 2f, 2f ) );
        l6.setPainter ( bp6 );

        return new GroupPanel ( 4, false, new GroupPanel ( 4, l1, l2 ), new GroupPanel ( 4, l3, l4 ), new GroupPanel ( 4, l5, l6 ) );
    }
}