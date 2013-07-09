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
import com.alee.extended.panel.WebAccordion;
import com.alee.extended.panel.WebAccordionStyle;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextArea;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 31.01.13 Time: 19:39
 */

public class AccordionExample extends DefaultExample
{
    public String getTitle ()
    {
        return "Accordion";
    }

    public String getDescription ()
    {
        return "Web-styled accordion";
    }

    public FeatureState getFeatureState ()
    {
        return FeatureState.beta;
    }

    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Title icon
        final ImageIcon icon = loadIcon ( "text.png" );

        // Simple pane
        final WebAccordion accordion = new WebAccordion ( WebAccordionStyle.accordionStyle );
        accordion.setMultiplySelectionAllowed ( false );
        accordion.addPane ( icon, "Some pane", createCustomHorContent () );
        accordion.addPane ( icon, "Some long long pane", createCustomHorContent () );
        accordion.addPane ( icon, "Some other pane", createCustomHorContent () );

        return new GroupPanel ( 4, accordion );
    }

    private WebScrollPane createCustomHorContent ()
    {
        return createCustomContent ( 150, 100 );
    }

    private WebScrollPane createCustomVerContent ()
    {
        return createCustomContent ( 100, 100 );
    }

    private WebScrollPane createCustomContent ( int w, int h )
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