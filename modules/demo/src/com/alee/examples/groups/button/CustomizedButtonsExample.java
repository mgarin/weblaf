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
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.panel.WebPanel;
import com.alee.utils.SwingUtils;

import java.awt.*;

/**
 * User: mgarin Date: 23.01.12 Time: 16:47
 */

public class CustomizedButtonsExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Customized buttons";
    }

    @Override
    public String getDescription ()
    {
        return "Buttons with customized UI";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Buttons panel
        WebPanel buttons = new WebPanel ( new HorizontalFlowLayout ( 5, false ) );

        // Filling pane with buttons
        for ( int i = 1; i <= 10; i++ )
        {
            WebToggleButton nb = new WebToggleButton ( loadIcon ( "buttons/" + i + ".png" ) );
            nb.setRolloverDecoratedOnly ( true );
            nb.setDrawFocus ( false );
            buttons.add ( nb );
        }

        // Grouping buttons
        SwingUtils.groupButtons ( buttons );

        return buttons;
    }
}