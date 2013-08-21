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

import java.awt.*;

import static com.alee.laf.splitpane.WebSplitPane.VERTICAL_SPLIT;

/**
 * User: mgarin Date: 17.02.12 Time: 13:35
 */

public class VerticalSplitPaneExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Vertical split pane";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled vertical split pane with proxy drag";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Top part content
        WebLabel topLabel = new WebLabel ( "top", WebLabel.CENTER );
        topLabel.setMargin ( 5 );
        WebPanel topPanel = new WebPanel ( true, topLabel );

        // Bottom part content
        WebLabel bottomLabel = new WebLabel ( "bottom", WebLabel.CENTER );
        bottomLabel.setMargin ( 5 );
        WebPanel bottomPanel = new WebPanel ( true, bottomLabel );

        // Split
        WebSplitPane splitPane = new WebSplitPane ( VERTICAL_SPLIT, topPanel, bottomPanel );
        splitPane.setOneTouchExpandable ( true );
        splitPane.setPreferredSize ( new Dimension ( 250, 200 ) );
        splitPane.setDividerLocation ( 100 );
        splitPane.setContinuousLayout ( false );

        return new GroupPanel ( splitPane );
    }
}