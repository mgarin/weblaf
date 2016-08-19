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

package com.alee.demo.ui.tools;

import com.alee.demo.DemoApplication;
import com.alee.demo.skin.DemoIcons;
import com.alee.demo.skin.DemoStyles;
import com.alee.extended.magnifier.MagnifierGlass;
import com.alee.laf.button.WebToggleButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Mikle Garin
 */

public final class MagnifierToggleTool extends WebToggleButton
{
    /**
     * Magnifier glass instance.
     */
    private final MagnifierGlass magnifier = new MagnifierGlass ();

    /**
     * Constructs new magnifier toggle tool.
     *
     * @param application demo application
     */
    public MagnifierToggleTool ( final DemoApplication application )
    {
        super ( DemoStyles.toolButton, "demo.tool.magnifier", DemoIcons.magnifier16 );
        addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                magnifier.displayOrDispose ( application );
            }
        } );
    }
}
