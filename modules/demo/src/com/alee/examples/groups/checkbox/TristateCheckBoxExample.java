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

package com.alee.examples.groups.checkbox;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.checkbox.WebTristateCheckBox;
import com.alee.extended.panel.GroupPanel;

import java.awt.*;

/**
 * Checkbox example.
 *
 * @author Mikle Garin
 */

public class TristateCheckBoxExample extends DefaultExample
{
    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle ()
    {
        return "Tristate checkboxes";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription ()
    {
        return "Web-styled tristate checkboxes";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Animated tristate check box
        final WebTristateCheckBox tristateCheckBoxA = new WebTristateCheckBox ( "Animated tristate checkbox" );
        tristateCheckBoxA.setMixed ();

        // Static tristate check box
        final WebTristateCheckBox tristateCheckBoxS = new WebTristateCheckBox ( "Static tristate checkbox" );
        tristateCheckBoxS.setAnimated ( false );

        // Tristate check box with reversed checking order
        final WebTristateCheckBox reversedTristateCheckbox = new WebTristateCheckBox ( "Tristate checkbox with reversed order" );
        reversedTristateCheckbox.setCheckMixedOnToggle ( true );

        return new GroupPanel ( 4, false, tristateCheckBoxA, tristateCheckBoxS, reversedTristateCheckbox );
    }
}