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

package com.alee.examples.groups.tooltip;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.button.WebButton;
import com.alee.managers.hotkey.ButtonHotkeyRunnable;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.tooltip.TooltipManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: mgarin Date: 30.11.12 Time: 17:12
 */

public class CustomHotkeyTooltipExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Tooltip with hotkey";
    }

    @Override
    public String getDescription ()
    {
        return "Custom Web-styled tooltip with hotkey";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Hotkey and Tooltip managers integration
        final WebButton tip = new WebButton ( "Tooltip with hotkey", loadIcon ( "web.png" ) );
        HotkeyManager.registerHotkey ( owner, tip, Hotkey.CTRL_S, new ButtonHotkeyRunnable ( tip, 50 ), TooltipWay.trailing );
        TooltipManager.setTooltip ( tip, "Increase counter", TooltipWay.trailing, 0 );
        tip.addActionListener ( new ActionListener ()
        {
            private int count = 0;

            @Override
            public void actionPerformed ( ActionEvent e )
            {
                count++;
                tip.setText ( "Pressed " + count + ( count == 1 ? " time" : " times" ) );
            }
        } );

        return new GroupPanel ( tip );
    }
}
