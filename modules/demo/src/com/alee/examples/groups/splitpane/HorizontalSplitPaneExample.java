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

package com.alee.examples.groups.splitpane;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.splitpane.WebSplitPane;

import javax.swing.*;
import java.awt.*;

import static javax.swing.JSplitPane.HORIZONTAL_SPLIT;

/**
 * User: mgarin Date: 17.02.12 Time: 13:35
 */

public class HorizontalSplitPaneExample extends DefaultExample implements SwingConstants
{
    @Override
    public String getTitle ()
    {
        return "Horizontal split pane";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled horizontal split pane with direct drag";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Left part content
        WebLabel leftLabel = new WebLabel ( "left", WebLabel.CENTER );
        leftLabel.setMargin ( 5 );
        WebPanel leftPanel = new WebPanel ( true, leftLabel );

        // Right part content
        WebLabel rightLabel = new WebLabel ( "right", WebLabel.CENTER );
        rightLabel.setMargin ( 5 );
        WebPanel rightPanel = new WebPanel ( true, rightLabel );

        // Split
        WebSplitPane splitPane = new WebSplitPane ( HORIZONTAL_SPLIT, leftPanel, rightPanel );
        splitPane.setOneTouchExpandable ( true );
        splitPane.setPreferredSize ( new Dimension ( 250, 200 ) );
        splitPane.setDividerLocation ( 125 );
        splitPane.setContinuousLayout ( true );

        return new GroupPanel ( splitPane );
    }
}