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
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.magnifier.MagnifierGlass;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.style.StyleId;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * {@link com.alee.demo.DemoApplication} magnifier glass.
 *
 * @author Mikle Garin
 */

public final class MagnifierToggleTool extends WebPanel
{
    /**
     * {@link MagnifierGlass} instance.
     */
    private final MagnifierGlass magnifier;

    /**
     * Constructs new {@link MagnifierToggleTool}.
     *
     * @param application {@link DemoApplication}
     */
    public MagnifierToggleTool ( final DemoApplication application )
    {
        super ( StyleId.panelTransparent, new HorizontalFlowLayout ( 0, false ) );

        // Magnifier glass
        magnifier = new MagnifierGlass ();

        // Magnifier glass switcher button
        final WebToggleButton magnifierButton = new WebToggleButton ( DemoStyles.toolButton, DemoIcons.magnifier16 );
        magnifierButton.setLanguage ( "demo.tool.magnifier" );
        magnifierButton.setSelected ( magnifier.isDisplayed () );
        magnifierButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                magnifier.displayOrDispose ( application );
            }
        } );
        add ( magnifierButton );

        // Dummy cursor display switcher button
        final WebToggleButton dummyCursorButton = new WebToggleButton ( DemoStyles.toolIconButton, DemoIcons.cursor16 );
        dummyCursorButton.setLanguage ( "demo.tool.magnifier.cursor" );
        dummyCursorButton.setSelected ( magnifier.isDisplayDummyCursor () );
        dummyCursorButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                magnifier.setDisplayDummyCursor ( !magnifier.isDisplayDummyCursor () );
            }
        } );
        add ( dummyCursorButton );
    }
}