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
import com.alee.managers.glasspane.GlassPaneManager;
import com.alee.managers.glasspane.WebGlassPane;
import com.alee.managers.style.StyleId;
import com.alee.utils.swing.HoverListener;

import java.awt.*;

/**
 * @author Mikle Garin
 */

public class InterfaceTree extends WebExTree<InterfaceTreeNode> implements HoverListener<InterfaceTreeNode>
{
    /**
     * Component inspector used to highlight hover elements.
     */
    protected ComponentInspector hoverInspector;

    /**
     * Constructs new interface tree.
     *
     * @param root root component
     */
    public InterfaceTree ( final Component root )
    {
        this ( null, root );
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
        this.hoverInspector = new ComponentInspector ();
        addHoverListener ( this );
    }

    @Override
    public void hoverChanged ( final InterfaceTreeNode previous, final InterfaceTreeNode current )
    {
        final WebGlassPane glassPane = GlassPaneManager.getGlassPane ( getRootComponent () );
        if ( hoverInspector.isShowing () )
        {
            glassPane.hideComponent ( hoverInspector );
            hoverInspector.uninstall ();
        }
        if ( current != null && current.getComponent () != null && current.getComponent ().isShowing () )
        {
            hoverInspector.install ( current.getComponent () );
            glassPane.showComponent ( hoverInspector );
        }
    }

    /**
     * Returns root component.
     *
     * @return root component
     */
    public Component getRootComponent ()
    {
        return getDataProvider ().getRoot ().getComponent ();
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
}