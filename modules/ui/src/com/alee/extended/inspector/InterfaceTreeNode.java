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

import com.alee.api.IconSupport;
import com.alee.api.TitleSupport;
import com.alee.laf.tree.UniqueNode;
import com.alee.managers.style.StyleableComponent;
import com.alee.utils.ReflectUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @author Mikle Garin
 */

public class InterfaceTreeNode extends UniqueNode implements IconSupport, TitleSupport
{
    /**
     * Unknown component type icon.
     */
    public static final ImageIcon unknownType = new ImageIcon ( InterfaceTreeNode.class.getResource ( "icons/unknown.png" ) );

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

    @Override
    public Icon getIcon ()
    {
        final Component component = getComponent ();
        if ( component instanceof JComponent )
        {
            return StyleableComponent.get ( ( JComponent ) component ).getIcon ();
        }
        else
        {
            return unknownType;
        }
    }

    @Override
    public String getTitle ()
    {
        final Component component = getComponent ();
        if ( component instanceof JComponent )
        {
            final JComponent jComponent = ( JComponent ) component;
            return StyleableComponent.get ( jComponent ).getText ( jComponent );
        }
        else
        {
            final String titleColor = component.isShowing () ? "165,145,70" : "180,180,180";
            return "{" + ReflectUtils.getClassName ( component ) + ":c(" + titleColor + ")}";
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
        return getTitle ();
    }
}