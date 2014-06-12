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
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.tooltip.TooltipManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: mgarin Date: 30.11.12 Time: 17:26
 */

public class CustomOneTimeTooltipExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "One-time tooltip";
    }

    @Override
    public String getDescription ()
    {
        return "Custom Web-styled one-tme tooltip";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // One-time tooltip that could be shown once anywhere you want
        final WebButton tip = new WebButton ( "One-time tooltip", loadIcon ( "web.png" ) );
        tip.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                // If we don't want to change tooltip's default positioning we just pass "null" as point
                TooltipManager.showOneTimeTooltip ( tip, null, "<html><center>One-time <font color=yellow>HTML</font> tooltip<br>" +
                        "<font size=2>just click anywhere to close it</font></center></html>", TooltipWay.trailing );
            }
        } );

        return new GroupPanel ( tip );
    }
}