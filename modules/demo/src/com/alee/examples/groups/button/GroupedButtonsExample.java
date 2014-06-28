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
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.WebButtonGroup;
import com.alee.laf.button.WebToggleButton;

import java.awt.*;

/**
 * User: mgarin Date: 23.01.12 Time: 16:35
 */

public class GroupedButtonsExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Grouped buttons";
    }

    @Override
    public String getDescription ()
    {
        return "Example of WebButtonGroup usage";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Text buttons group
        WebToggleButton left = new WebToggleButton ( "Left" );
        WebToggleButton right = new WebToggleButton ( "Right" );
        WebButtonGroup textGroup = new WebButtonGroup ( true, left, right );
        textGroup.setButtonsDrawFocus ( false );

        // Iconed buttons group
        WebToggleButton sort1 = new WebToggleButton ( loadIcon ( "buttons/1.png" ) );
        WebToggleButton sort2 = new WebToggleButton ( loadIcon ( "buttons/2.png" ) );
        WebToggleButton sort3 = new WebToggleButton ( loadIcon ( "buttons/3.png" ) );
        WebToggleButton sort4 = new WebToggleButton ( loadIcon ( "buttons/4.png" ) );
        WebButtonGroup iconsGroup = new WebButtonGroup ( true, sort1, sort2, sort3, sort4 );
        iconsGroup.setButtonsDrawFocus ( false );

        return new GroupPanel ( 2, textGroup, iconsGroup );
    }
}