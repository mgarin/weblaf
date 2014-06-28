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

package com.alee.examples.groups.panel;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.panel.GroupPanel;
import com.alee.global.StyleConstants;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;

import java.awt.*;

/**
 * User: mgarin Date: 15.02.12 Time: 15:33
 */

public class DecoratedPanelExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Decorated panel";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled panel";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Decorated panel
        WebPanel panel = new WebPanel ();
        panel.setUndecorated ( false );
        panel.setLayout ( new BorderLayout () );
        panel.setMargin ( 20 );
        panel.setRound ( StyleConstants.largeRound );

        panel.add ( new WebLabel ( "Decorated panel", WebLabel.CENTER ) );

        return new GroupPanel ( panel );
    }
}