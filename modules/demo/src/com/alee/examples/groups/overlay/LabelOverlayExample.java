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

package com.alee.examples.groups.overlay;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.image.WebImage;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.WebOverlay;
import com.alee.laf.label.WebLabel;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.tooltip.TooltipManager;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 08.11.12 Time: 16:49
 */

public class LabelOverlayExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Overlayed label";
    }

    @Override
    public String getDescription ()
    {
        return "Label overlayed with an icon";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Overlay
        WebOverlay overlayPanel = new WebOverlay ();

        // Overlayed label
        WebLabel component = new WebLabel ( "Mouseover the small icon" );
        overlayPanel.setComponent ( component );

        // Image displayed as overlay
        WebImage overlay = new WebImage ( loadIcon ( "small.png" ) );
        TooltipManager.setTooltip ( overlay, "Overlay with tooltip", TooltipWay.trailing, 0 );
        overlayPanel.addOverlay ( overlay, SwingConstants.TRAILING, SwingConstants.TOP );
        overlayPanel.setComponentMargin ( 0, 0, 0, overlay.getPreferredSize ().width );

        return new GroupPanel ( overlayPanel );
    }
}