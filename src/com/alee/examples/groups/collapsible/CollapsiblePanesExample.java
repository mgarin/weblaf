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

package com.alee.examples.groups.collapsible;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.examples.content.ExamplesManager;
import com.alee.examples.content.FeatureState;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.WebCollapsiblePane;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextArea;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 20.02.12 Time: 15:26
 */

public class CollapsiblePanesExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Collapsible panes";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled collapsible panes";
    }

    @Override
    public FeatureState getFeatureState ()
    {
        return FeatureState.beta;
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Title icon
        final ImageIcon icon = loadIcon ( "text.png" );

        // Simple pane
        final WebCollapsiblePane topPane = new WebCollapsiblePane ( icon, "Top pane", createCustomHorContent () );
        topPane.setExpanded ( false );

        // Simple pane with bottom title position
        final WebCollapsiblePane bottomPane = new WebCollapsiblePane ( icon, "Bottom pane", createCustomHorContent () );
        bottomPane.setTitlePanePostion ( SwingConstants.BOTTOM );

        // Simple pane with left title position
        final WebCollapsiblePane leftPane = new WebCollapsiblePane ( icon, "Left pane", createCustomVerContent () );
        leftPane.setTitlePanePostion ( SwingConstants.LEFT );

        return new GroupPanel ( 4, new GroupPanel ( 4, false, topPane, bottomPane ), leftPane );
    }

    public static WebScrollPane createCustomHorContent ()
    {
        return createCustomContent ( 150, 100 );
    }

    public static WebScrollPane createCustomVerContent ()
    {
        return createCustomContent ( 150, 100 );
    }

    public static WebScrollPane createCustomContent ( final int w, final int h )
    {
        // Content text area
        final WebTextArea textArea = new WebTextArea ( ExamplesManager.createLongString () );
        textArea.setLineWrap ( true );
        textArea.setWrapStyleWord ( true );

        // Text area scroll
        final WebScrollPane scrollPane = new WebScrollPane ( textArea, false );
        scrollPane.setPreferredSize ( new Dimension ( w, h ) );

        return scrollPane;
    }
}