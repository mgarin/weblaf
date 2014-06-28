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

import com.alee.examples.content.DefaultExampleGroup;
import com.alee.examples.content.Example;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Checkbox and radio buttons examples group.
 *
 * @author Mikle Garin
 */

public class CheckBoxesGroup extends DefaultExampleGroup
{
    /**
     * {@inheritDoc}
     */
    @Override
    public Icon getGroupIcon ()
    {
        return loadGroupIcon ( "checkbox.png" );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getGroupName ()
    {
        return "Check & Radio";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getGroupDescription ()
    {
        return "Various examples of checkbox and radiobuttons usage";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Example> getGroupExamples ()
    {
        final List<Example> examples = new ArrayList<Example> ();
        examples.add ( new CheckBoxExample () );
        examples.add ( new TristateCheckBoxExample () );
        examples.add ( new RadioButtonExample () );
        examples.add ( new CheckBoxListExample () );
        examples.add ( new CheckBoxTreeExample () );
        return examples;
    }
}