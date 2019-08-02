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

package com.alee.extended.inspector;

import com.alee.extended.tree.WebExTree;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.style.StyleId;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.compare.Filter;
import com.alee.utils.swing.HoverListener;
import com.alee.utils.swing.extensions.KeyEventRunnable;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link WebExTree} representing Swing components structure.
 * It displays and dynamically updates Swing components strcuture for the specified root {@link Component}.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see InterfaceInspector
 * @see WebExTree
 */
public class InterfaceTree extends WebExTree<InterfaceTreeNode>
        implements HoverListener<InterfaceTreeNode>, TreeSelectionListener, Filter<Component>
{
    /**
     * Highlighter for hovered tree element.
     */
    protected ComponentHighlighter hoverHighlighter;

    /**
     * Highlighters for selected tree elements.
     */
    protected Map<Component, ComponentHighlighter> selectedHighlighters;

    /**
     * Constructs new interface tree.
     *
     * @param root root component
     */
    public InterfaceTree ( final Component root )
    {
        this ( StyleId.auto, root );
    }

    /**
     * Constructs new interface tree.
     *
     * @param id   style ID
     * @param root root component
     */
    public InterfaceTree ( final StyleId id, final Component root )
    {
        super ( id );
        setVisibleRowCount ( 20 );

        // Custom data provider
        setDataProvider ( new InterfaceTreeDataProvider ( this, root ) );

        // Nodes hover listener
        this.hoverHighlighter = new ComponentHighlighter ();
        addHoverListener ( this );

        // Nodes selection listener
        this.selectedHighlighters = new HashMap<Component, ComponentHighlighter> ( 0 );
        addTreeSelectionListener ( this );

        // Simple selection clearing
        onKeyPress ( Hotkey.ESCAPE, new KeyEventRunnable ()
        {
            @Override
            public void run ( final KeyEvent e )
            {
                clearSelection ();
            }
        } );
    }

    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.interfacetree;
    }

    @Override
    public boolean accept ( final Component component )
    {
        return ( ( InterfaceTreeDataProvider ) super.getDataProvider () ).accept ( component );
    }

    @Override
    public void hoverChanged ( final InterfaceTreeNode previous, final InterfaceTreeNode current )
    {
        // Separating action from the tree hover makes UI more responsive
        CoreSwingUtils.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                if ( hoverHighlighter.isShowing () )
                {
                    hoverHighlighter.uninstall ();
                }
                if ( current != null && canHighlight ( current.getUserObject () ) )
                {
                    hoverHighlighter.install ( current.getUserObject () );
                }
            }
        } );
    }

    @Override
    public void valueChanged ( final TreeSelectionEvent e )
    {
        // Separating action from the tree selection makes UI more responsive
        CoreSwingUtils.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                // Selected nodes
                final List<InterfaceTreeNode> selected = getSelectedNodes ();

                // Previous and current highlighters
                final Map<Component, ComponentHighlighter> prevHighlighters = selectedHighlighters;
                selectedHighlighters = new HashMap<Component, ComponentHighlighter> ( selected.size () );

                // Updating displayed highlighters
                for ( final InterfaceTreeNode node : selected )
                {
                    final Component component = node.getUserObject ();
                    final ComponentHighlighter prevHighlighter = prevHighlighters.get ( component );
                    if ( prevHighlighter != null )
                    {
                        // Preserving existing highlighter
                        selectedHighlighters.put ( component, prevHighlighter );
                        prevHighlighters.remove ( component );
                    }
                    else if ( canHighlight ( component ) )
                    {
                        // Adding new highlighter
                        final ComponentHighlighter newHighlighter = new ComponentHighlighter ();
                        selectedHighlighters.put ( component, newHighlighter );
                        newHighlighter.install ( component );
                    }
                }

                // Removing redundant highlighters
                for ( final Map.Entry<Component, ComponentHighlighter> entry : prevHighlighters.entrySet () )
                {
                    entry.getValue ().uninstall ();
                }
            }
        } );
    }

    /**
     * Returns whether or not component can be highlighted.
     *
     * @param component component to be highlighted
     * @return {@code true} if component can be highlighted, {@code false} otherwise
     */
    public boolean canHighlight ( final Component component )
    {
        return component != null && component.isShowing () && !( component instanceof Window );
    }

    /**
     * Returns root component.
     *
     * @return root component
     */
    public Component getRootComponent ()
    {
        return getDataProvider ().getRoot ().getUserObject ();
    }

    /**
     * Sets root component.
     *
     * @param root root component
     */
    public void setRootComponent ( final Component root )
    {
        setDataProvider ( new InterfaceTreeDataProvider ( this, root ) );
    }

    /**
     * Navigates tree to the specified component.
     *
     * @param component component to navigate to
     */
    public void navigate ( final Component component )
    {
        final String nodeId = Integer.toString ( component.hashCode () );
        final InterfaceTreeNode node = findNode ( nodeId );
        if ( node != null )
        {
            expandNode ( node );
            setSelectedNode ( node );
            scrollToNode ( node, true );
        }
    }

    /**
     * Expands tree to the specified component.
     *
     * @param component component to expand to
     */
    public void expand ( final Component component )
    {
        final InterfaceTreeNode node = findNode ( Integer.toString ( component.hashCode () ) );
        if ( node != null )
        {
            expandNode ( node );
        }
    }
}