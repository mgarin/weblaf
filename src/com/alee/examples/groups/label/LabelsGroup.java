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

import com.alee.examples.content.DefaultExampleGroup;
import com.alee.examples.content.Example;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mgarin Date: 23.01.12 Time: 17:27
 */

public class LabelsGroup extends DefaultExampleGroup
{
    @Override
    public Icon getGroupIcon ()
    {
        return loadGroupIcon ( "label.png" );
    }

    @Override
    public String getGroupName ()
    {
        return "Labels";
    }

    @Override
    public String getGroupDescription ()
    {
        return "Various examples of labels usage";
    }

    @Override
    public List<Example> getGroupExamples ()
    {
        List<Example> examples = new ArrayList<Example> ();
        examples.add ( new LabelsExample () );
        examples.add ( new ShadedLabelsExample () );
        examples.add ( new MultilineLabelExample () );
        examples.add ( new VerticalLabelsExample () );
        examples.add ( new LinkLabelsExample () );
        examples.add ( new HotkeyLabelsExample () );
        examples.add ( new StepLabelsExample () );
        return examples;
    }
}