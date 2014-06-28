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

package com.alee.examples.groups.android;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.painter.NinePatchStatePainter;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.button.WebToggleButton;
import com.alee.utils.NinePatchUtils;
import com.alee.utils.swing.UnselectableButtonGroup;

import java.awt.*;

/**
 * User: mgarin Date: 12.03.12 Time: 15:32
 */

public class AndroidButtonsExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Android buttons";
    }

    @Override
    public String getDescription ()
    {
        return "Android-styled buttons";
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Single painter used for all example buttons
        final NinePatchStatePainter npbbp = NinePatchUtils.loadNinePatchStatePainter ( getResource ( "button.xml" ) );

        // Buttons
        final WebToggleButton info = new WebToggleButton ( "Info", loadIcon ( "info.png" ) );
        info.setPainter ( npbbp );
        final WebToggleButton search = new WebToggleButton ( "Search", loadIcon ( "search.png" ) );
        search.setPainter ( npbbp );
        final WebToggleButton game = new WebToggleButton ( "Game", loadIcon ( "game.png" ) );
        game.setPainter ( npbbp );
        final WebToggleButton exit = new WebToggleButton ( "Exit", loadIcon ( "exit.png" ) );
        exit.setPainter ( npbbp );
        exit.setEnabled ( false );

        // Grouping toggle buttons
        UnselectableButtonGroup.group ( info, search, game, exit );

        return new GroupPanel ( info, search, game, exit );
    }
}