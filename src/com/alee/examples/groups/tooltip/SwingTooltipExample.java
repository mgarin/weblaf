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

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 17.02.12 Time: 13:54
 */

public class SwingTooltipExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Swing tooltip";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled swing tooltip";
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Simple Swing tooltip
        final String tipText = "Swing " + ToolTipManager.sharedInstance ().getInitialDelay () + "ms delay tooltip";
        final WebLabel tip = new WebLabel ( tipText, loadIcon ( "swing.png" ) );
        tip.setToolTipText ( "Delayed swing tooltip" );
        tip.setMargin ( 4 );

        return new GroupPanel ( tip );
    }
}