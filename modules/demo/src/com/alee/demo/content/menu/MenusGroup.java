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

package com.alee.demo.content.menu;

import com.alee.api.annotations.NotNull;
import com.alee.demo.api.example.AbstractExampleGroup;
import com.alee.managers.hotkey.Hotkey;
import com.alee.utils.CollectionUtils;
import com.alee.utils.swing.menu.JMenuBarGenerator;
import com.alee.utils.swing.menu.MenuGenerator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class MenusGroup extends AbstractExampleGroup
{
    @NotNull
    @Override
    public String getId ()
    {
        return "menus";
    }

    @Override
    protected List<Class> getExampleClasses ()
    {
        return CollectionUtils.<Class>asList (
                JMenuBarExample.class
        );
    }

    /**
     * Fills menu bar with various sample items.
     *
     * @param menuBar menu bar to fill
     * @return filled menu bar
     */
    public static JMenuBar fillMenuBar ( final JMenuBar menuBar )
    {
        final JMenuBarGenerator generator = new JMenuBarGenerator ( menuBar );
        generator.setLanguagePrefix ( "demo.example.menus.menu" );
        generator.setIconSettings ( MenusGroup.class, "icons/menu/", "png" );

        final ActionListener action = new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                final JMenuItem menuItem = ( JMenuItem ) e.getSource ();
                notifyAboutEvent ( menuItem.getText () );
            }
        };

        final MenuGenerator fileMenu = generator.addSubMenu ( "file", "file" );
        final MenuGenerator subMenu = fileMenu.addSubMenu ( "new", "new" );
        subMenu.addItem ( "image", "image", Hotkey.CTRL_N, action );
        subMenu.addItem ( "video", "video", action );
        subMenu.addItem ( "music", "music", action );
        fileMenu.addItem ( "open", "open", Hotkey.CTRL_O, action );
        fileMenu.addSeparator ();
        fileMenu.addItem ( "save", "save", Hotkey.CTRL_S, action );
        fileMenu.addItem ( "print", "print", Hotkey.CTRL_P, action );
        fileMenu.addSeparator ();
        fileMenu.addItem ( "exit", "exit", Hotkey.ALT_X, action );
        menuBar.add ( fileMenu.getMenu () );

        final MenuGenerator editMenu = generator.addSubMenu ( "edit", "edit" );
        editMenu.addItem ( "undo", "undo", Hotkey.CTRL_Z, action );
        editMenu.addItem ( "redo", "redo", Hotkey.CTRL_Y, action );
        editMenu.addSeparator ();
        editMenu.addItem ( "cut", "cut", Hotkey.CTRL_X, action );
        editMenu.addItem ( "copy", "copy", Hotkey.CTRL_C, action );
        editMenu.addItem ( "paste", "paste", Hotkey.CTRL_P, action );
        menuBar.add ( editMenu.getMenu () );

        final MenuGenerator settingsMenu = generator.addSubMenu ( "settings", "settings" );
        settingsMenu.openGroup ();
        settingsMenu.addRadioItem ( "choice1", Hotkey.F1, true, action );
        settingsMenu.addRadioItem ( "choice2", Hotkey.F2, false, action );
        settingsMenu.addRadioItem ( "choice3", Hotkey.F3, false, action );
        settingsMenu.closeGroup ();
        settingsMenu.addSeparator ();
        settingsMenu.addCheckItem ( "option1", Hotkey.F4, true, action );
        settingsMenu.addCheckItem ( "option2", Hotkey.F5, false, action );
        settingsMenu.addCheckItem ( "option3", Hotkey.F6, false, action );
        menuBar.add ( settingsMenu.getMenu () );

        return menuBar;
    }
}