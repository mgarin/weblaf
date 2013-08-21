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
import com.alee.extended.label.HotkeyPainter;
import com.alee.extended.painter.AlphaLayerPainter;
import com.alee.extended.painter.TexturePainter;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.label.WebLabel;

import java.awt.*;

/**
 * User: mgarin Date: 14.09.12 Time: 17:50
 */

public class CustomPaintersExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Custom painters";
    }

    @Override
    public String getDescription ()
    {
        return "Various custom painters";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // AlphaLayerPainter
        WebLabel label1 = new WebLabel ( "Alpha background painter", WebLabel.CENTER );
        label1.setMargin ( 5 );
        label1.setPainter ( new AlphaLayerPainter ( 8 ) );

        // HotkeyPainter
        WebLabel label2 = new WebLabel ( "Texture background painter", WebLabel.CENTER );
        label2.setMargin ( 5 );
        label2.setPainter ( new TexturePainter ( loadIcon ( "bg.png" ) ) );

        // HotkeyPainter
        WebLabel label3 = new WebLabel ( "Hotkey background painter", WebLabel.CENTER );
        label3.setMargin ( 5 );
        label3.setPainter ( new HotkeyPainter () );

        return new GroupPanel ( 4, false, label1, label2, label3 );
    }
}