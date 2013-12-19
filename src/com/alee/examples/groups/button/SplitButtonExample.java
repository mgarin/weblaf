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
import com.alee.examples.content.FeatureState;
import com.alee.extended.button.WebSplitButton;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.menu.WebPopupMenu;
import com.alee.managers.hotkey.Hotkey;

import java.awt.*;

/**
 * Split button example
 *
 * @author Mikle Garin
 */

public class SplitButtonExample extends DefaultExample
{
    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle ()
    {
        return "Split button";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription ()
    {
        return "Web-styled split button";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FeatureState getFeatureState ()
    {
        return FeatureState.beta;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Simple split button
        final WebSplitButton splitButton = new WebSplitButton ( "Click me", WebLookAndFeel.getIcon ( 16 ) );

        // Menu for split button
        final WebPopupMenu popupMenu = new WebPopupMenu ();
        popupMenu.add ( new WebMenuItem ( "Menu item 1", WebLookAndFeel.getIcon ( 16 ), Hotkey.ALT_X ) );
        popupMenu.add ( new WebMenuItem ( "Menu item 2", Hotkey.D ) );
        popupMenu.addSeparator ();
        popupMenu.add ( new WebMenuItem ( "Menu item 3", Hotkey.ESCAPE ) );
        splitButton.setPopupMenu ( popupMenu );

        return new GroupPanel ( splitButton );
    }
}