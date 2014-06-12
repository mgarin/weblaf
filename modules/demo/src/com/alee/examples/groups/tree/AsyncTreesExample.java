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
import com.alee.extended.tree.WebAsyncTree;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.tree.TreeSelectionStyle;
import com.alee.laf.tree.WebTree;
import com.alee.utils.SwingUtils;

import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Asynchronous trees example.
 *
 * @author Mikle Garin
 */

public class AsyncTreesExample extends DefaultExample
{
    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle ()
    {
        return "Asynchronous trees";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription ()
    {
        return "Web-styled asynchronous trees with multiselection";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Editable asynchronous tree
        final WebAsyncTree tree1 = new WebAsyncTree ();
        tree1.setEditable ( true );
        tree1.setSelectionMode ( WebTree.DISCONTIGUOUS_TREE_SELECTION );
        final WebScrollPane treeScroll1 = new WebScrollPane ( tree1 );
        treeScroll1.setPreferredSize ( new Dimension ( 200, 150 ) );

        // Asynchronous tree with a custom selection style
        final WebAsyncTree tree2 = new WebAsyncTree ();
        tree2.setEditable ( true );
        tree2.setSelectionMode ( WebTree.DISCONTIGUOUS_TREE_SELECTION );
        tree2.setSelectionStyle ( TreeSelectionStyle.group );
        final WebScrollPane treeScroll2 = new WebScrollPane ( tree2 );
        treeScroll2.setPreferredSize ( new Dimension ( 200, 150 ) );

        // Installing childs reload mouse listener
        installReloadListener ( tree1 );
        installReloadListener ( tree2 );

        return new GroupPanel ( treeScroll1, treeScroll2 );
    }

    /**
     * Installs child reload mouse listener on specified tree.
     *
     * @param tree tree to listen to
     */
    private void installReloadListener ( final WebAsyncTree tree )
    {
        tree.addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mouseReleased ( final MouseEvent e )
            {
                if ( SwingUtils.isRightMouseButton ( e ) )
                {
                    final TreePath path = tree.getClosestPathForLocation ( e.getX (), e.getY () );
                    if ( path != null )
                    {
                        tree.setSelectionPath ( path );
                        tree.reloadPath ( path );
                    }
                }
            }
        } );
    }
}