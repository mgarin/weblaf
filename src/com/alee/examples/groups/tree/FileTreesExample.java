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
import com.alee.extended.tree.WebFileTree;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.tree.TreeSelectionStyle;
import com.alee.utils.FileUtils;

import java.awt.*;

/**
 * Files tree example.
 *
 * @author Mikle Garin
 */

public class FileTreesExample extends DefaultExample
{
    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle ()
    {
        return "Files tree";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription ()
    {
        return "Web-styled files tree";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Files tree with default root
        WebFileTree fileTree = new WebFileTree ();
        fileTree.setAutoExpandSelectedNode ( false );
        fileTree.setShowsRootHandles ( true );
        WebScrollPane fileTreeScroll = new WebScrollPane ( fileTree );
        fileTreeScroll.setPreferredSize ( new Dimension ( 200, 150 ) );

        // Files tree with user home directory as root
        WebFileTree homeFileTree = new WebFileTree ( FileUtils.getUserHomePath () );
        homeFileTree.setAutoExpandSelectedNode ( false );
        homeFileTree.setShowsRootHandles ( true );
        homeFileTree.setSelectionStyle ( TreeSelectionStyle.group );
        WebScrollPane homeFileTreeScroll = new WebScrollPane ( homeFileTree );
        homeFileTreeScroll.setPreferredSize ( new Dimension ( 200, 150 ) );

        return new GroupPanel ( 5, fileTreeScroll, homeFileTreeScroll );
    }
}