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

package com.alee.examples.groups.colorchooser;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.examples.content.FeatureState;
import com.alee.extended.colorchooser.WebColorChooserField;
import com.alee.extended.panel.GroupPanel;

import java.awt.*;

/**
 * @author Mikle Garin
 */

public class ColorChooserFieldExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Color chooser field";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled color chooser field";
    }

    @Override
    public FeatureState getFeatureState ()
    {
        return FeatureState.beta;
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Default color chooser field
        WebColorChooserField defaultColorChooser = new WebColorChooserField ();
        defaultColorChooser.setColumns ( 11 );

        // Color chooser field without pipette picker
        WebColorChooserField simpleColorChooser = new WebColorChooserField ();
        simpleColorChooser.setPipetteEnabled ( false );
        defaultColorChooser.setColumns ( 11 );

        return new GroupPanel ( 5, defaultColorChooser, simpleColorChooser );
    }
}