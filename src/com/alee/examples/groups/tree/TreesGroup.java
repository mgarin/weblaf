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

package com.alee.examples.groups.tree;

import com.alee.examples.content.DefaultExampleGroup;
import com.alee.examples.content.Example;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Trees example group.
 *
 * @author Mikle Garin
 */

public class TreesGroup extends DefaultExampleGroup
{
    /**
     * Returns example group icon.
     *
     * @return example group icon
     */
    @Override
    public Icon getGroupIcon ()
    {
        return loadGroupIcon ( "tree.png" );
    }

    /**
     * Returns example group name.
     *
     * @return example group name
     */
    @Override
    public String getGroupName ()
    {
        return "Trees";
    }

    /**
     * Returns short example group description.
     *
     * @return short example group description
     */
    @Override
    public String getGroupDescription ()
    {
        return "Various examples of trees usage";
    }

    /**
     * Returns a list of examples for this example group.
     *
     * @return list of examples
     */
    @Override
    public List<Example> getGroupExamples ()
    {
        List<Example> examples = new ArrayList<Example> ();
        examples.add ( new TreesExample () );
        examples.add ( new AsyncTreesExample () );
        examples.add ( new FileTreesExample () );
        return examples;
    }

    /**
     * Returns examples content side width relative to the whole available to example group width.
     *
     * @return examples content side width relative to the whole available to example group width
     */
    @Override
    public double getContentPartSize ()
    {
        return 0.7f;
    }
}