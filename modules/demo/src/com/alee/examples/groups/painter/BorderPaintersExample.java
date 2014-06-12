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
import com.alee.managers.style.skin.web.WebLabelPainter;

import javax.swing.*;
import java.awt.*;

/**
 * Border painters example.
 *
 * @author Mikle Garin
 */

public class BorderPaintersExample extends DefaultExample
{
    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle ()
    {
        return "Border painters";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription ()
    {
        return "Web-styled border painters";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Simple BorderPainter
        final WebLabel l1 = new WebLabel ( "Simple border painter" );
        l1.setPainter ( new WebLabelPainter ( new BorderPainter () ) ).setMargin ( 10 );

        // Customized BorderPainter
        final WebLabel l2 = new WebLabel ( "Customized border painter" );
        final BorderPainter bp2 = new BorderPainter ();
        bp2.setRound ( 12 );
        bp2.setWidth ( 2 );
        bp2.setColor ( new Color ( 39, 95, 173 ) );
        l2.setPainter ( new WebLabelPainter ( bp2 ) ).setMargin ( 10 );

        // Simple DashedBorderPainter
        final WebLabel l3 = new WebLabel ( "Dashed border painter" );
        l3.setPainter ( new WebLabelPainter ( new DashedBorderPainter () ) ).setMargin ( 10 );

        // Customized DashedBorderPainter
        final WebLabel l4 = new WebLabel ( "Customized dashed border painter" );
        final DashedBorderPainter bp4 = new DashedBorderPainter ( new float[]{ 3f, 3f } );
        bp4.setRound ( 12 );
        bp4.setWidth ( 2 );
        bp4.setColor ( new Color ( 39, 95, 173 ) );
        l4.setPainter ( new WebLabelPainter ( bp4 ) ).setMargin ( 10 );

        // Simple TitledBorderPainter
        final WebLabel l5 = new WebLabel ( "Titled border painter" );
        l5.setPainter ( new WebLabelPainter ( new TitledBorderPainter ( "Title" ) ) ).setMargin ( 5 );

        // Customized TitledBorderPainter
        final WebLabel l6 = new WebLabel ( "Customized titled border painter" );
        final TitledBorderPainter bp6 = new TitledBorderPainter ( "Title", SwingConstants.CENTER );
        bp6.setWidth ( 5 );
        bp6.setRound ( 12 );
        bp6.setStroke ( new ZigzagStroke ( 2f, 2f ) );
        l6.setPainter ( new WebLabelPainter ( bp6 ) ).setMargin ( 5 );

        return new GroupPanel ( 4, false, new GroupPanel ( 4, l1, l2 ), new GroupPanel ( 4, l3, l4 ), new GroupPanel ( 4, l5, l6 ) );
    }
}