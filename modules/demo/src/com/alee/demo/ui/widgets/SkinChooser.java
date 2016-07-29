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

package com.alee.demo.ui.widgets;

import com.alee.demo.DemoApplication;
import com.alee.demo.DemoStyles;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.grouping.GroupPane;
import com.alee.managers.style.Skin;
import com.alee.managers.style.StyleManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Demo application skin chooser.
 *
 * @author Mikle Garin
 */

public class SkinChooser extends GroupPane
{
    /**
     * Constructs new skin chooser.
     */
    public SkinChooser ()
    {
        super ( true );

        // Initializing separate skin buttons
        for ( final Skin skin : DemoApplication.skins )
        {
            final WebToggleButton skinButton = new WebToggleButton ( DemoStyles.skinButton, skin.getTitle (), skin.getIcon () );
            skinButton.setSelected ( StyleManager.getSkin () == skin );
            skinButton.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    StyleManager.setSkin ( skin );
                }
            } );
            add ( skinButton );
        }

        // Equalizing button widths
        equalizeComponentsWidth ();

        //        // Global skins switch hotkey
        //        HotkeyManager.registerHotkey ( Hotkey.CTRL_S, new HotkeyRunnable ()
        //        {
        //            @Override
        //            public void run ( final KeyEvent e )
        //            {
        //                final Skin skin = StyleManager.getSkin ();
        //                final int current = DemoApplication.skins.indexOf ( skin );
        //                final int index = current < DemoApplication.skins.size () - 1 ? current + 1 : 0;
        //                StyleManager.setSkin ( DemoApplication.skins.get ( index ) );
        //            }
        //        } );
    }
}