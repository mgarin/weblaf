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
import com.alee.extended.button.WebSwitch;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.label.WebLabel;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 02.11.12 Time: 16:15
 */

public class SwitchExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Switches";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled switches";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Simple switch
        final WebSwitch ws1 = new WebSwitch ();

        // Customized switch
        final WebSwitch ws2 = new WebSwitch ();
        ws2.setRound ( 11 );
        ws2.setLeftComponent ( createSwitchIcon ( loadIcon ( "switch/on.png" ), 4, 0 ) );
        ws2.setRightComponent ( createSwitchIcon ( loadIcon ( "switch/off.png" ), 0, 4 ) );

        return new GroupPanel ( 4, ws1, ws2 );
    }

    private WebLabel createSwitchIcon ( ImageIcon icon, final int left, final int right )
    {
        final WebLabel rightComponent = new WebLabel ( icon, WebLabel.CENTER );
        rightComponent.setMargin ( 2, left, 2, right );
        return rightComponent;
    }
}