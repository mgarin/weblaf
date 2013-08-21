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
import com.alee.extended.label.WebVerticalLabel;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.StyleConstants;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 15.02.12 Time: 15:33
 */

public class PartialPanelsExample extends DefaultExample implements SwingConstants
{
    @Override
    public String getTitle ()
    {
        return "Partially decorated panels";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled panels with missing sides";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        return new GroupPanel ( createFirstPanel (), createSecondPanel () );
    }

    private WebPanel createFirstPanel ()
    {
        WebPanel panel = new WebPanel ();
        panel.setUndecorated ( false );
        panel.setLayout ( new BorderLayout () );
        panel.setWebColored ( false );

        WebPanel northPanel = new WebPanel ();
        northPanel.setDrawSides ( false, false, true, false );
        setupPanel ( northPanel, NORTH );
        panel.add ( northPanel, BorderLayout.NORTH );

        WebPanel southPanel = new WebPanel ();
        southPanel.setDrawSides ( true, false, false, false );
        setupPanel ( southPanel, SOUTH );
        panel.add ( southPanel, BorderLayout.SOUTH );

        WebPanel leadingPanel = new WebPanel ();
        leadingPanel.setDrawLeft ( false );
        setupPanel ( leadingPanel, WEST );
        panel.add ( leadingPanel, BorderLayout.LINE_START );

        WebPanel trailingPanel = new WebPanel ();
        trailingPanel.setDrawRight ( false );
        setupPanel ( trailingPanel, EAST );
        panel.add ( trailingPanel, BorderLayout.LINE_END );

        WebPanel centerPanel = new WebPanel ();
        setupPanel ( centerPanel, CENTER );
        panel.add ( centerPanel, BorderLayout.CENTER );

        return panel;
    }

    private WebPanel createSecondPanel ()
    {
        WebPanel panel = new WebPanel ();
        panel.setUndecorated ( false );
        panel.setLayout ( new TableLayout ( new double[][]{ { TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED },
                { TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED } } ) );
        panel.setWebColored ( false );

        WebPanel northPanel = new WebPanel ();
        northPanel.setDrawSides ( false, false, true, true );
        setupPanel ( northPanel, NORTH );
        panel.add ( northPanel, "0,0,1,0" );

        WebPanel southPanel = new WebPanel ();
        southPanel.setDrawSides ( true, true, false, false );
        setupPanel ( southPanel, SOUTH );
        panel.add ( southPanel, "1,2,2,2" );

        WebPanel leadingPanel = new WebPanel ();
        leadingPanel.setDrawSides ( true, false, false, true );
        setupPanel ( leadingPanel, WEST );
        panel.add ( leadingPanel, "0,1,0,2" );

        WebPanel trailingPanel = new WebPanel ();
        trailingPanel.setDrawSides ( false, true, true, false );
        setupPanel ( trailingPanel, EAST );
        panel.add ( trailingPanel, "2,0,2,1" );

        WebPanel centerPanel = new WebPanel ();
        setupPanel ( centerPanel, CENTER );
        panel.add ( centerPanel, "1,1" );

        return panel;
    }

    private void setupPanel ( WebPanel panel, int location )
    {
        // Decoration settings
        panel.setUndecorated ( false );
        panel.setMargin ( new Insets ( 3, 3, 3, 3 ) );
        panel.setRound ( StyleConstants.largeRound );

        // Custom content
        switch ( location )
        {
            case NORTH:
            {
                panel.add ( new WebLabel ( "North panel", WebLabel.CENTER ) );
                break;
            }
            case SOUTH:
            {
                panel.add ( new WebLabel ( "South panel", WebLabel.CENTER ) );
                break;
            }
            case WEST:
            {
                panel.add ( new WebVerticalLabel ( "West panel", WebLabel.CENTER, false ) );
                break;
            }
            case EAST:
            {
                panel.add ( new WebVerticalLabel ( "East panel", WebLabel.CENTER, true ) );
                break;
            }
            case CENTER:
            {
                panel.add ( new WebLabel ( "Center panel", WebLabel.CENTER ) );
                break;
            }
        }
    }
}