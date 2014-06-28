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
import com.alee.laf.panel.WebPanel;
import com.alee.laf.tabbedpane.TabbedPaneStyle;
import com.alee.laf.tabbedpane.WebTabbedPane;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 17.02.12 Time: 14:47
 */

public class AttachedTabbedPanesExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Attached tabbed pane";
    }

    @Override
    public String getDescription ()
    {
        return "Attach-styled tabbed pane";
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        final WebTabbedPane tabbedPane1 = new WebTabbedPane ()
        {
            @Override
            public Dimension getPreferredSize ()
            {
                final Dimension ps = super.getPreferredSize ();
                ps.width = 150;
                return ps;
            }
        };
        tabbedPane1.setTabbedPaneStyle ( TabbedPaneStyle.attached );
        setupTabbedPane ( tabbedPane1, "Without panel" );

        final WebTabbedPane tabbedPane2 = new WebTabbedPane ()
        {
            @Override
            public Dimension getPreferredSize ()
            {
                final Dimension ps = super.getPreferredSize ();
                ps.width = 150;
                return ps;
            }
        };
        tabbedPane2.setTabbedPaneStyle ( TabbedPaneStyle.attached );
        setupTabbedPane ( tabbedPane2, "Inside styled panel" );

        final WebPanel tabPanel = new WebPanel ( true, tabbedPane2 );
        tabPanel.setPaintFocus ( true );

        return new GroupPanel ( 4, tabbedPane1, tabPanel );
    }

    private static void setupTabbedPane ( final JTabbedPane tabbedPane, final String text )
    {
        tabbedPane.addTab ( "1", createContent ( text ) );
        tabbedPane.addTab ( "2", createContent ( text ) );
        tabbedPane.addTab ( "3", createContent ( text ) );
        tabbedPane.addTab ( "4", createContent ( text ) );
        tabbedPane.addTab ( "5", createContent ( text ) );
    }

    private static WebLabel createContent ( final String text )
    {
        final WebLabel label = new WebLabel ( text, WebLabel.CENTER );
        label.setMargin ( 5 );
        return label;
    }
}