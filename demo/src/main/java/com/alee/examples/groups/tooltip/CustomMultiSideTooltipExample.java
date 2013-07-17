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
import com.alee.laf.label.WebLabel;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.TooltipWay;

import java.awt.*;

/**
 * User: mgarin Date: 30.11.12 Time: 16:55
 */

public class CustomMultiSideTooltipExample extends DefaultExample
{
    public String getTitle ()
    {
        return "Instant tooltip";
    }

    public String getDescription ()
    {
        return "Custom Web-styled instant tooltip";
    }

    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Double tooltip for single component
        WebLabel tip = new WebLabel ( "Multiply tooltips", loadIcon ( "web.png" ) );
        TooltipManager.addTooltip ( tip, "Upper tooltip", TooltipWay.up, 0 );
        TooltipManager.addTooltip ( tip, "Leading tooltip", TooltipWay.leading, 0 );
        TooltipManager.addTooltip ( tip, "Trailing tooltip", TooltipWay.trailing, 0 );
        TooltipManager.addTooltip ( tip, "Lower tooltip", TooltipWay.down, 0 );
        tip.setMargin ( 4 );

        return new GroupPanel ( tip );
    }
}
