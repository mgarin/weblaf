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

package com.alee.examples.groups.slider;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.slider.WebSlider;

import java.awt.*;

/**
 * User: mgarin Date: 16.02.12 Time: 18:35
 */

public class HorizontalSlidersExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Horizontal sliders";
    }

    @Override
    public String getDescription ()
    {
        return "Horizontal Web-styled sliders";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        WebSlider slider1 = new WebSlider ( WebSlider.HORIZONTAL );
        slider1.setMinimum ( 0 );
        slider1.setMaximum ( 100 );
        slider1.setMinorTickSpacing ( 10 );
        slider1.setMajorTickSpacing ( 50 );
        slider1.setPaintTicks ( false );
        slider1.setPaintLabels ( false );

        WebSlider slider2 = new WebSlider ( WebSlider.HORIZONTAL );
        slider2.setMinimum ( 0 );
        slider2.setMaximum ( 100 );
        slider2.setMinorTickSpacing ( 10 );
        slider2.setMajorTickSpacing ( 50 );
        slider2.setPaintTicks ( true );
        slider2.setPaintLabels ( true );

        return new GroupPanel ( 4, false, slider1, slider2 );
    }
}