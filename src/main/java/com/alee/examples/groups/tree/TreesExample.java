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

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.tree.TreeSelectionStyle;
import com.alee.laf.tree.WebTree;

import java.awt.*;

/**
 * Trees example.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class TreesExample extends DefaultExample
{
    /**
     * Returns example title.
     *
     * @return example title
     */
    public String getTitle ()
    {
        return "Trees";
    }

    /**
     * Returns short example description.
     *
     * @return short example description
     */
    public String getDescription ()
    {
        return "Web-styled trees with multiselection";
    }

    /**
     * Returns preview component for this example.
     *
     * @param owner demo application main frame
     * @return preview component
     */
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Editable tree
        WebTree tree1 = new WebTree ();
        tree1.setEditable ( true );
        tree1.setSelectionMode ( WebTree.DISCONTIGUOUS_TREE_SELECTION );
        WebScrollPane treeScroll1 = new WebScrollPane ( tree1 );
        treeScroll1.setPreferredSize ( new Dimension ( 200, 150 ) );

        // Tree with a custom selection style
        WebTree tree2 = new WebTree ();
        tree2.setEditable ( true );
        tree2.setSelectionMode ( WebTree.DISCONTIGUOUS_TREE_SELECTION );
        tree2.setSelectionStyle ( TreeSelectionStyle.group );
        WebScrollPane treeScroll2 = new WebScrollPane ( tree2 );
        treeScroll2.setPreferredSize ( new Dimension ( 200, 150 ) );

        return new GroupPanel ( treeScroll1, treeScroll2 );
    }
}