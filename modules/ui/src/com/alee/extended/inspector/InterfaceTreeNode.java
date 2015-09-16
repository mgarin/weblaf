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

import com.alee.laf.tree.UniqueNode;
import com.alee.managers.style.StyleableComponent;

import javax.swing.*;
import java.awt.*;

/**
 * @author Mikle Garin
 */

public class InterfaceTreeNode extends UniqueNode
{
    /**
     * Constructs interface tree node.
     *
     * @param component component this node references
     */
    public InterfaceTreeNode ( final Component component )
    {
        super ( "" + component.hashCode (), component );
    }

    /**
     * Returns component this node references.
     *
     * @return component this node references
     */
    public Component getComponent ()
    {
        return ( Component ) getUserObject ();
    }

    /**
     * Returns node icon.
     *
     * @return node icon
     */
    public Icon getIcon ()
    {
        final Component component = getComponent ();
        if ( component instanceof JComponent )
        {
            return StyleableComponent.get ( ( JComponent ) component ).getIcon ();
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns proper node text representation.
     * Used to filter nodes in tree and to display its name.
     *
     * @return proper node text representation
     */
    @Override
    public String toString ()
    {
        final Component component = getComponent ();
        if ( component instanceof JComponent )
        {
            final JComponent jComponent = ( JComponent ) component;
            return StyleableComponent.get ( jComponent ).getText ( jComponent );
        }
        else
        {
            return component.toString ();
        }
    }
}