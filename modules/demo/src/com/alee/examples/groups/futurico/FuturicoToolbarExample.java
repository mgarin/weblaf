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

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.toolbar.WebToolBar;
import com.alee.utils.NinePatchUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.swing.UnselectableButtonGroup;

import java.awt.*;

/**
 * User: mgarin Date: 30.07.12 Time: 18:14
 */

public class FuturicoToolbarExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Futurico toolbar";
    }

    @Override
    public String getDescription ()
    {
        return "Futurico-styled toolbar";
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Toolbar with custom painter
        final WebToolBar toolBar = new WebToolBar ();
        toolBar.setPainter ( NinePatchUtils.loadNinePatchStatePainter ( getResource ( "toolbar.xml" ) ) );
        toolBar.setSpacing ( 3 );

        // Buttons with custom painter
        final WebToggleButton info = new WebToggleButton ( "Info", loadIcon ( "info.png" ), true );
        final WebToggleButton zoom = new WebToggleButton ( "Zoom", loadIcon ( "search.png" ) );
        final WebToggleButton game = new WebToggleButton ( "Game", loadIcon ( "game.png" ) );
        final WebToggleButton exit = new WebToggleButton ( "Exit", loadIcon ( "exit.png" ) );
        final WebToggleButton[] buttons = new WebToggleButton[]{ info, zoom, game, exit };

        // Buttons settings
        ReflectUtils.callMethodsSafely ( buttons, "setPainter",
                NinePatchUtils.loadNinePatchStatePainter ( getResource ( "toolbarButton.xml" ) ) );
        ReflectUtils.callMethodsSafely ( buttons, "setForeground", Color.WHITE );
        ReflectUtils.callMethodsSafely ( buttons, "setSelectedForeground", Color.BLACK );
        toolBar.add ( buttons );

        // Buttons grouping
        UnselectableButtonGroup.group ( buttons );

        return new GroupPanel ( toolBar );
    }
}