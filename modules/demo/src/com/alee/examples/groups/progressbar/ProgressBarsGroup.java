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

package com.alee.examples.groups.progressbar;

import com.alee.examples.content.DefaultExampleGroup;
import com.alee.examples.content.Example;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mgarin Date: 16.02.12 Time: 15:08
 */

public class ProgressBarsGroup extends DefaultExampleGroup
{
    @Override
    public Icon getGroupIcon ()
    {
        return loadGroupIcon ( "progressbar.png" );
    }

    @Override
    public String getGroupName ()
    {
        return "Progress bars";
    }

    @Override
    public String getGroupDescription ()
    {
        return "Various examples of progress bars usage";
    }

    @Override
    public List<Example> getGroupExamples ()
    {
        List<Example> examples = new ArrayList<Example> ();
        examples.add ( new HorizontalProgressBarsExample () );
        examples.add ( new VerticalProgressBarsExample () );
        return examples;
    }
}