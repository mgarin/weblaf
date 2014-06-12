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

package com.alee.examples.groups.android;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.utils.NinePatchUtils;

import java.awt.*;

/**
 * User: mgarin Date: 12.03.12 Time: 16:36
 */

public class AndroidPanelExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Android panel";
    }

    @Override
    public String getDescription ()
    {
        return "Android-styled panel";
    }

    @Override
    public boolean isFillWidth ()
    {
        return true;
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Panel styled with nine-patch icon painter
        final WebPanel panel = new WebPanel ();
        panel.setPainter ( NinePatchUtils.loadNinePatchIconPainter ( getResource ( "panel.xml" ) ) );

        // Panel content
        final WebLabel label = new WebLabel ( "<html><center>Sample text inside styled panel<br>" +
                "Note that margins are automatically set by the image file</center></html>" );
        label.setHorizontalAlignment ( WebLabel.CENTER );
        label.setForeground ( Color.WHITE );
        panel.add ( label );

        return panel;
    }
}