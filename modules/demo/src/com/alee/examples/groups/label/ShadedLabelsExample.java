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

package com.alee.examples.groups.label;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.label.WebLabel;

import java.awt.*;

/**
 * User: mgarin Date: 28.05.12 Time: 15:08
 */

public class ShadedLabelsExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Shaded labels";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled shaded labels";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Label with default shade
        WebLabel l1 = new WebLabel ( "Simple shaded label" );
        l1.setDrawShade ( true );

        // Label with custom colored foreground and shade
        WebLabel l2 = new WebLabel ( "Shaded white label", loadIcon ( "icon.png" ) );
        l2.setDrawShade ( true );
        l2.setForeground ( Color.WHITE );
        l2.setShadeColor ( Color.BLACK );

        // Label with custom colored shade
        WebLabel l3 = new WebLabel ( "Colored shade label" );
        l3.setDrawShade ( true );
        l3.setShadeColor ( new Color ( 255, 128, 0 ) );

        return new GroupPanel ( 4, false, l1, l2, l3 );
    }
}