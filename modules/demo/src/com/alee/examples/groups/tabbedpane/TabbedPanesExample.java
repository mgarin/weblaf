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
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.label.WebLabel;
import com.alee.laf.tabbedpane.WebTabbedPane;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 17.02.12 Time: 14:33
 */

public class TabbedPanesExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Tabbed panes";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled tabbed panes";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        WebTabbedPane tabbedPane1 = new WebTabbedPane ();
        tabbedPane1.setPreferredSize ( new Dimension ( 150, 100 ) );
        tabbedPane1.setTabPlacement ( WebTabbedPane.TOP );
        setupTabbedPane ( tabbedPane1 );

        WebTabbedPane tabbedPane2 = new WebTabbedPane ();
        tabbedPane2.setPreferredSize ( new Dimension ( 150, 100 ) );
        tabbedPane2.setTabPlacement ( WebTabbedPane.BOTTOM );
        setupTabbedPane ( tabbedPane2 );

        WebTabbedPane tabbedPane3 = new WebTabbedPane ();
        tabbedPane3.setPreferredSize ( new Dimension ( 150, 120 ) );
        tabbedPane3.setTabPlacement ( WebTabbedPane.LEFT );
        setupTabbedPane ( tabbedPane3 );

        WebTabbedPane tabbedPane4 = new WebTabbedPane ();
        tabbedPane4.setPreferredSize ( new Dimension ( 150, 120 ) );
        tabbedPane4.setTabPlacement ( WebTabbedPane.RIGHT );
        setupTabbedPane ( tabbedPane4 );

        return new GroupPanel ( 4, false, new GroupPanel ( 4, tabbedPane1, tabbedPane2 ), new GroupPanel ( 4, tabbedPane3, tabbedPane4 ) );
    }

    private static void setupTabbedPane ( JTabbedPane tabbedPane )
    {
        // Simple tab
        tabbedPane.addTab ( "Normal 1", new WebLabel () );

        // Disabled tab
        tabbedPane.addTab ( "Disabled 2", new WebLabel () );
        tabbedPane.setEnabledAt ( 1, false );

        // Selected tab
        tabbedPane.addTab ( "Selected 3", new WebLabel () );
        tabbedPane.setSelectedIndex ( 2 );

        // Colored tab
        tabbedPane.addTab ( "Colored 4", new WebLabel () );
        tabbedPane.setBackgroundAt ( 3, new Color ( 255, 212, 161 ) );
    }
}