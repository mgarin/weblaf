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

package com.alee.examples.groups.label;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.label.WebVerticalLabel;
import com.alee.extended.panel.GroupPanel;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 24.01.12 Time: 13:45
 */

public class VerticalLabelsExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Vertical labels";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled vertical labels";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Counter-clockwise vertical label
        WebVerticalLabel vl = new WebVerticalLabel ( "Counter-clockwise", loadIcon ( "icon.png" ), WebVerticalLabel.CENTER );
        vl.setClockwise ( false );
        vl.setHorizontalTextPosition ( WebVerticalLabel.RIGHT );

        // Clockwise vertical label
        WebVerticalLabel dvl = new WebVerticalLabel ( "Clockwise", loadIcon ( "icon.png" ), WebVerticalLabel.CENTER );
        dvl.setClockwise ( true );
        dvl.setHorizontalTextPosition ( WebVerticalLabel.RIGHT );

        // Shaded verical label
        WebVerticalLabel svl = new WebVerticalLabel ( "Shaded label", WebVerticalLabel.CENTER );
        svl.setClockwise ( false );
        svl.setDrawShade ( true );

        // Vertical label with HTML content
        WebVerticalLabel hvl =
                new WebVerticalLabel ( "<html><center>Some <b>HTML</b> label<br>with two lines</center></html>", JLabel.CENTER );
        hvl.setClockwise ( false );

        return new GroupPanel ( 8, vl, dvl, svl, hvl );
    }
}