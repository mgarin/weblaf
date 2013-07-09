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

package com.alee.examples.groups.futurico;

import com.alee.examples.content.DefaultExampleGroup;
import com.alee.examples.content.Example;
import com.alee.laf.separator.WebSeparator;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.utils.XmlUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mgarin Date: 12.03.12 Time: 15:33
 */

public class FuturicoStylingGroup extends DefaultExampleGroup
{
    public Icon getGroupIcon ()
    {
        return loadGroupIcon ( "futurico.png" );
    }

    public String getGroupName ()
    {
        return "Futurico styling";
    }

    public String getGroupDescription ()
    {
        return "Various examples of Futurio components styling";
    }

    public List<Example> getGroupExamples ()
    {
        List<Example> examples = new ArrayList<Example> ();
        examples.add ( new FuturicoLabelExample () );
        examples.add ( new FuturicoPanelExample () );
        examples.add ( new FuturicoButtonsExample () );
        examples.add ( new FuturicoToolbarExample () );
        examples.add ( new FuturicoFieldsExample () );
        examples.add ( new FuturicoTextAreaExample () );
        return examples;
    }

    public void modifyExampleTab ( int tabIndex, WebTabbedPane tabbedPane )
    {
        tabbedPane.getWebUI ().setSelectedForegroundAt ( tabIndex, Color.WHITE );
        tabbedPane.getWebUI ().setBackgroundPainterAt ( tabIndex, XmlUtils.loadTexturePainter ( getResource ( "bg.xml" ) ) );
    }

    public WebSeparator modifySeparator ( WebSeparator separator )
    {
        separator.setReversedColors ( true );
        separator.setDrawSideLines ( false );
        return separator;
    }

    public Color getPreferredForeground ()
    {
        return Color.WHITE;
    }
}