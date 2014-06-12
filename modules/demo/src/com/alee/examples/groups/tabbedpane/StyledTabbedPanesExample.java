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

package com.alee.examples.groups.tabbedpane;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.painter.TexturePainter;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.label.WebLabel;
import com.alee.laf.tabbedpane.WebTabbedPane;

import java.awt.*;

/**
 * User: mgarin Date: 17.02.12 Time: 14:55
 */

public class StyledTabbedPanesExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Styled tabbed pane";
    }

    @Override
    public String getDescription ()
    {
        return "Tabbed pane with styled tabs background";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Tab background painters
        TexturePainter tp1 = new TexturePainter ( loadIcon ( "bg1.jpg" ) );
        TexturePainter tp2 = new TexturePainter ( loadIcon ( "bg2.jpg" ) );
        TexturePainter tp3 = new TexturePainter ( loadIcon ( "bg3.jpg" ) );
        TexturePainter tp4 = new TexturePainter ( loadIcon ( "bg4.jpg" ) );

        WebTabbedPane tabbedPane = new WebTabbedPane ();
        tabbedPane.setPreferredSize ( new Dimension ( 300, 200 ) );

        tabbedPane.addTab ( "Tab 1", new WebLabel () );
        tabbedPane.setBackgroundPainterAt ( tabbedPane.getTabCount () - 1, tp1 );
        tabbedPane.setSelectedForegroundAt ( tabbedPane.getTabCount () - 1, Color.WHITE );

        tabbedPane.addTab ( "Tab 2", new WebLabel () );
        tabbedPane.setBackgroundPainterAt ( tabbedPane.getTabCount () - 1, tp2 );
        tabbedPane.setSelectedForegroundAt ( tabbedPane.getTabCount () - 1, Color.WHITE );

        tabbedPane.addTab ( "Tab 3", new WebLabel () );
        tabbedPane.setBackgroundPainterAt ( tabbedPane.getTabCount () - 1, tp3 );
        tabbedPane.setSelectedForegroundAt ( tabbedPane.getTabCount () - 1, Color.WHITE );

        tabbedPane.addTab ( "Tab 4", new WebLabel () );
        tabbedPane.setBackgroundPainterAt ( tabbedPane.getTabCount () - 1, tp4 );
        tabbedPane.setSelectedForegroundAt ( tabbedPane.getTabCount () - 1, Color.WHITE );

        tabbedPane.addTab ( "Tab 5", new WebLabel () );
        tabbedPane.setBackgroundPainterAt ( tabbedPane.getTabCount () - 1, tp1 );
        tabbedPane.setSelectedForegroundAt ( tabbedPane.getTabCount () - 1, Color.WHITE );

        tabbedPane.addTab ( "Tab 6", new WebLabel () );
        tabbedPane.setBackgroundPainterAt ( tabbedPane.getTabCount () - 1, tp2 );
        tabbedPane.setSelectedForegroundAt ( tabbedPane.getTabCount () - 1, Color.WHITE );

        tabbedPane.addTab ( "Tab 7", new WebLabel () );
        tabbedPane.setBackgroundPainterAt ( tabbedPane.getTabCount () - 1, tp3 );
        tabbedPane.setSelectedForegroundAt ( tabbedPane.getTabCount () - 1, Color.WHITE );

        tabbedPane.addTab ( "Tab 8", new WebLabel () );
        tabbedPane.setBackgroundPainterAt ( tabbedPane.getTabCount () - 1, tp4 );
        tabbedPane.setSelectedForegroundAt ( tabbedPane.getTabCount () - 1, Color.WHITE );

        return new GroupPanel ( tabbedPane );
    }
}