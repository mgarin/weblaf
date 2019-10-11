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

package com.alee.demo.frames.examples;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.data.CompassDirection;
import com.alee.demo.DemoApplication;
import com.alee.demo.skin.DemoIcons;
import com.alee.demo.skin.DemoStyles;
import com.alee.extended.dock.WebDockableFrame;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.GroupingType;
import com.alee.extended.tree.WebTreeFilterField;
import com.alee.laf.scroll.ScrollPaneState;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.separator.WebSeparator;
import com.alee.laf.tree.TreeNodeEventRunnable;
import com.alee.laf.tree.TreeState;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.settings.Configuration;
import com.alee.managers.style.StyleId;
import com.alee.utils.swing.extensions.KeyEventRunnable;

import java.awt.event.KeyEvent;

/**
 * {@link DemoApplication} examples frame.
 *
 * @author Mikle Garin
 */
public final class ExamplesFrame extends WebDockableFrame
{
    /**
     * Frame ID.
     */
    public static final String ID = "demo.examples";

    /**
     * Constructs examples frame.
     */
    public ExamplesFrame ()
    {
        super ( StyleId.dockableframeCompact, ID, DemoIcons.examples16, "demo.examples.title" );
        setPosition ( CompassDirection.west );
        setPreferredSize ( 300, 200 );

        // Examples tree
        final ExamplesTree examplesTree = new ExamplesTree ();
        examplesTree.onKeyPress ( Hotkey.ENTER, new KeyEventRunnable ()
        {
            @Override
            public void run ( @NotNull final KeyEvent e )
            {
                open ( examplesTree.getSelectedNode () );
            }
        } );
        examplesTree.onNodeDoubleClick ( new TreeNodeEventRunnable<ExamplesTreeNode> ()
        {
            @Override
            public void run ( @NotNull final ExamplesTreeNode node )
            {
                open ( node );
            }
        } );
        examplesTree.registerSettings ( new Configuration<TreeState> ( "ExamplesTree" ) );
        final WebScrollPane examplesTreeScroll = new WebScrollPane ( StyleId.scrollpaneTransparentHovering, examplesTree );
        examplesTreeScroll.registerSettings ( new Configuration<ScrollPaneState> ( "ExamplesScroll" ) );

        // Filtering field
        final WebTreeFilterField filter = new WebTreeFilterField ( DemoStyles.filterfield, examplesTree );

        // Frame UI composition
        final WebSeparator separator = new WebSeparator ( StyleId.separatorHorizontal );
        add ( new GroupPanel ( GroupingType.fillLast, 0, false, filter, separator, examplesTreeScroll ) );
    }

    /**
     * Opens content related to the specified node if it is available.
     *
     * @param node examples tree node
     */
    protected void open ( @Nullable final ExamplesTreeNode node )
    {
        if ( node != null && node.getType () == ExamplesTreeNodeType.example )
        {
            DemoApplication.getInstance ().open ( node.getExample () );
        }
    }
}