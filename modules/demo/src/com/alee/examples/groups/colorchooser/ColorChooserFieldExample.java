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
import com.alee.extended.colorchooser.ColorDisplayType;
import com.alee.extended.colorchooser.WebColorChooserField;
import com.alee.extended.panel.GroupPanel;

import java.awt.*;

/**
 * Color chooser fields example.
 *
 * @author Mikle Garin
 */

public class ColorChooserFieldExample extends DefaultExample
{
    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle ()
    {
        return "Color chooser field";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription ()
    {
        return "Web-styled color chooser field";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FeatureState getFeatureState ()
    {
        return FeatureState.beta;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Default color chooser field
        final WebColorChooserField defaultColorChooser = new WebColorChooserField ();

        // Color chooser field without pipette picker
        final WebColorChooserField simpleColorChooser = new WebColorChooserField ();
        simpleColorChooser.setDisplayEyedropper ( false );
        simpleColorChooser.setColorDisplayType ( ColorDisplayType.hex );

        return new GroupPanel ( 5, defaultColorChooser, simpleColorChooser );
    }
}