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
import com.alee.extended.heatmap.HeatMap;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.style.StyleId;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * {@link com.alee.demo.DemoApplication} heat map tool.
 *
 * @author Mikle Garin
 */

public class HeatMapTool extends WebPanel
{
    /**
     * {@link HeatMap} instance.
     */
    private final HeatMap heatMap;

    /**
     * Constructs new {@link HeatMapTool}.
     *
     * @param application {@link DemoApplication}
     */
    public HeatMapTool ( final DemoApplication application )
    {
        super ( StyleId.panelTransparent, new BorderLayout ( 0, 0 ) );

        // Heat map
        heatMap = new HeatMap ();

        // Magnifier glass switcher button
        final WebToggleButton heatMapButton = new WebToggleButton ( DemoStyles.toolButton, DemoIcons.fire16 );
        heatMapButton.setLanguage ( "demo.tool.heatmap" );
        heatMapButton.setSelected ( heatMap.isDisplayed () );
        heatMapButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                heatMap.displayOrDispose ( application );
            }
        } );
        add ( heatMapButton );
    }
}