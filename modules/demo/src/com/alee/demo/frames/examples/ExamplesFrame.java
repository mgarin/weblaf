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

import com.alee.api.data.CompassDirection;
import com.alee.demo.DemoApplication;
import com.alee.demo.api.example.FeatureState;
import com.alee.demo.skin.DemoIcons;
import com.alee.demo.skin.DemoStyles;
import com.alee.extended.dock.WebDockableFrame;
import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.GroupingType;
import com.alee.extended.tree.WebTreeFilterField;
import com.alee.laf.label.WebLabel;
import com.alee.laf.scroll.ScrollPaneState;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.separator.WebSeparator;
import com.alee.laf.tree.TreeNodeEventRunnable;
import com.alee.laf.tree.TreeState;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.settings.Configuration;
import com.alee.managers.style.StyleId;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.TooltipWay;
import com.alee.utils.swing.MouseButton;
import com.alee.utils.swing.extensions.KeyEventRunnable;
import com.alee.utils.swing.extensions.MouseEventRunnable;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Demo Application examples frame.
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
        super ( ID, DemoIcons.examples16, "demo.examples.title" );
        setPosition ( CompassDirection.west );
        setPreferredWidth ( 270 );
        setPreferredHeight ( 400 );

        // Examples tree
        final ExamplesTree examplesTree = new ExamplesTree ();
        examplesTree.onKeyPress ( Hotkey.ENTER, new KeyEventRunnable ()
        {
            @Override
            public void run ( final KeyEvent e )
            {
                open ( examplesTree.getSelectedNode () );
            }
        } );
        examplesTree.onNodeDoubleClick ( new TreeNodeEventRunnable<ExamplesTreeNode> ()
        {
            @Override
            public void run ( final ExamplesTreeNode node )
            {
                open ( node );
            }
        } );
        examplesTree.registerSettings ( new Configuration<TreeState> ( "ExamplesTree" ) );
        final WebScrollPane examplesTreeScroll = new WebScrollPane ( StyleId.scrollpaneTransparentHovering, examplesTree );
        examplesTreeScroll.registerSettings ( new Configuration<ScrollPaneState> ( "ExamplesScroll" ) );

        // Filtering field
        final WebTreeFilterField filter = new WebTreeFilterField ( DemoStyles.filterfield, examplesTree );

        // Legend for colors
        final WebLabel legend = new WebLabel ( DemoIcons.legend16 );
        legend.setCursor ( Cursor.getDefaultCursor () );
        legend.onMousePress ( MouseButton.left, new MouseEventRunnable ()
        {
            private WebStyledLabel legendTip;

            @Override
            public void run ( final MouseEvent e )
            {
                if ( legendTip == null )
                {
                    // todo Include colors for different states
                    // todo Make a better markup for legend elements
                    final FeatureState[] states = FeatureState.values ();
                    final StringBuilder legendText = new StringBuilder ();
                    for ( int i = 0; i < states.length; i++ )
                    {
                        final FeatureState state = states[ i ];
                        final String title = state.getTitle ();
                        legendText.append ( title );
                        legendText.append ( " - " ).append ( state.geDescription () );
                        legendText.append ( i < states.length - 1 ? "\n" : "" );
                    }
                    legendTip = new WebStyledLabel ( legendText.toString () );
                    legendTip.setForeground ( Color.WHITE );
                }
                TooltipManager.showOneTimeTooltip ( legend, null, legendTip, TooltipWay.down );
            }
        } );
        filter.setTrailingComponent ( legend );

        // Frame UI composition
        final WebSeparator separator = new WebSeparator ( StyleId.separatorHorizontal );
        add ( new GroupPanel ( GroupingType.fillLast, 0, false, filter, separator, examplesTreeScroll ) );
    }

    /**
     * Opens content related to the specified node if it is available.
     *
     * @param node examples tree node
     */
    protected void open ( final ExamplesTreeNode node )
    {
        if ( node != null && node.getType () == ExamplesTreeNodeType.example )
        {
            DemoApplication.getInstance ().open ( node.getExample () );
        }
    }
}