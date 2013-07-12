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
import com.alee.utils.ReflectUtils;
import com.alee.utils.XmlUtils;
import com.alee.utils.swing.UnselectableButtonGroup;

import java.awt.*;

/**
 * User: mgarin Date: 30.07.12 Time: 18:14
 */

public class FuturicoToolbarExample extends DefaultExample
{
    public String getTitle ()
    {
        return "Futurico toolbar";
    }

    public String getDescription ()
    {
        return "Futurico-styled toolbar";
    }

    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Toolbar with custom painter
        WebToolBar toolBar = new WebToolBar ();
        toolBar.setPainter ( XmlUtils.loadNinePatchStatePainter ( getResource ( "toolbar.xml" ) ) );
        toolBar.setSpacing ( 3 );

        // Buttons with custom painter
        WebToggleButton info = new WebToggleButton ( "Info", loadIcon ( "info.png" ), true );
        WebToggleButton zoom = new WebToggleButton ( "Zoom", loadIcon ( "search.png" ) );
        WebToggleButton game = new WebToggleButton ( "Game", loadIcon ( "game.png" ) );
        WebToggleButton exit = new WebToggleButton ( "Exit", loadIcon ( "exit.png" ) );
        WebToggleButton[] buttons = new WebToggleButton[]{ info, zoom, game, exit };

        // Buttons settings
        ReflectUtils.callMethodSafely ( buttons, "setPainter", XmlUtils.loadNinePatchStatePainter ( getResource ( "toolbarButton.xml" ) ) );
        ReflectUtils.callMethodSafely ( buttons, "setForeground", Color.WHITE );
        ReflectUtils.callMethodSafely ( buttons, "setSelectedForeground", Color.BLACK );
        toolBar.add ( buttons );

        // Buttons grouping
        UnselectableButtonGroup.group ( buttons );

        return new GroupPanel ( toolBar );
    }
}