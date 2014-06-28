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
import com.alee.extended.painter.AlphaLayerPainter;
import com.alee.extended.painter.TexturePainter;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.label.WebLabel;
import com.alee.managers.style.skin.web.WebHotkeyLabelPainter;
import com.alee.managers.style.skin.web.WebLabelPainter;

import java.awt.*;

/**
 * Custom painters example.
 *
 * @author Mikle Garin
 */

public class CustomPaintersExample extends DefaultExample
{
    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle ()
    {
        return "Custom painters";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription ()
    {
        return "Various custom painters";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // AlphaLayerPainter
        final WebLabel label1 = new WebLabel ( "Alpha background painter", WebLabel.CENTER );
        label1.setPainter ( new WebLabelPainter ( new AlphaLayerPainter ( 8 ) ) ).setMargin ( 5 );

        // HotkeyPainter
        final WebLabel label2 = new WebLabel ( "Texture background painter", WebLabel.CENTER );
        label2.setPainter ( new WebLabelPainter ( new TexturePainter ( loadIcon ( "bg.png" ) ) ) ).setMargin ( 5 );

        // HotkeyPainter
        final WebLabel label3 = new WebLabel ( "Hotkey background painter", WebLabel.CENTER );
        label3.setPainter ( new WebLabelPainter ( new WebHotkeyLabelPainter () ) ).setMargin ( 5 );

        return new GroupPanel ( 4, false, label1, label2, label3 );
    }
}