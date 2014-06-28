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

package com.alee.examples.groups.button;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.panel.WebButtonGroup;
import com.alee.laf.button.WebToggleButton;

import java.awt.*;

/**
 * User: mgarin Date: 23.01.12 Time: 15:48
 */

public class DoubleGroupedButtonsExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Double grouped buttons";
    }

    @Override
    public String getDescription ()
    {
        return "Example of double WebButtonGroup usage";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // 1st line buttons
        WebToggleButton b1 = new WebToggleButton ( "1" );
        WebToggleButton b2 = new WebToggleButton ( "2" );
        WebToggleButton b3 = new WebToggleButton ( "3" );
        WebButtonGroup g1 = new WebButtonGroup ( b1, b2, b3 );

        // 2nd line buttons
        WebToggleButton b4 = new WebToggleButton ( "4" );
        WebToggleButton b5 = new WebToggleButton ( "5" );
        WebToggleButton b6 = new WebToggleButton ( "6" );
        WebButtonGroup g2 = new WebButtonGroup ( b4, b5, b6 );

        // Grouping lines together
        WebButtonGroup group = new WebButtonGroup ( WebButtonGroup.VERTICAL, true, g1, g2 );
        group.setButtonsDrawFocus ( false );

        return group;
    }
}