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
import com.alee.managers.glasspane.WebGlassPane;
import com.alee.managers.style.StyleableComponent;
import com.alee.utils.ReflectUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;

/**
 * @author Mikle Garin
 */

public class InterfaceTreeNode extends UniqueNode implements IconSupport, TitleSupport
{
    /**
     * Additional type icons.
     */
    public static final ImageIcon frameType = new ImageIcon ( InterfaceTreeNode.class.getResource ( "icons/frame.png" ) );
    public static final ImageIcon dialogType = new ImageIcon ( InterfaceTreeNode.class.getResource ( "icons/dialog.png" ) );
    public static final ImageIcon windowType = new ImageIcon ( InterfaceTreeNode.class.getResource ( "icons/window.png" ) );
    public static final ImageIcon layeredPaneType = new ImageIcon ( InterfaceTreeNode.class.getResource ( "icons/layeredpane.png" ) );
    public static final ImageIcon glassPaneType = new ImageIcon ( InterfaceTreeNode.class.getResource ( "icons/glasspane.png" ) );
    public static final ImageIcon unknownType = new ImageIcon ( InterfaceTreeNode.class.getResource ( "icons/unknown.png" ) );

    /**
     * Interface components tree.
     */
    protected final InterfaceTree tree;

    /**
     * Component state listeners.
     */
    private ComponentAdapter componentAdapter;
    private ContainerAdapter containerAdapter;

    /**
     * Constructs interface tree node.
     *
     * @param tree      interface components tree
     * @param component component this node references
     */
    public InterfaceTreeNode ( final InterfaceTree tree, final Component component )
    {
        super ( "" + component.hashCode (), component );
        this.tree = tree;

        // Proper nodes state updates
        componentAdapter = new ComponentAdapter ()
        {
            @Override
            public void componentResized ( final ComponentEvent e )
            {
                tree.updateNode ( InterfaceTreeNode.this  );
            }

            @Override
            public void componentMoved ( final ComponentEvent e )
            {
                tree.updateNode ( InterfaceTreeNode.this );
            }

            @Override
            public void componentShown ( final ComponentEvent e )
            {
                tree.repaint ( InterfaceTreeNode.this );
            }

            @Override
            public void componentHidden ( final ComponentEvent e )
            {
                tree.repaint ( InterfaceTreeNode.this );
            }
        };
        component.addComponentListener ( componentAdapter );
        if ( component instanceof Container )
        {
            containerAdapter = new ContainerAdapter ()
            {
                @Override
                public void componentAdded ( final ContainerEvent e )
                {
                    tree.addChildNode ( InterfaceTreeNode.this, new InterfaceTreeNode ( tree, e.getChild () ) );
                }

                @Override
                public void componentRemoved ( final ContainerEvent e )
                {
                    final Component child = e.getChild ();
                    for ( int i = 0; i < InterfaceTreeNode.this.getChildCount (); i++ )
                    {
                        final InterfaceTreeNode childAt = ( InterfaceTreeNode ) InterfaceTreeNode.this.getChildAt ( i );
                        if ( childAt.getComponent () == child )
                        {
                            tree.removeNode ( childAt );
                            childAt.destroy ();
                            break;
                        }
                    }
                }
            };
            ( ( Container ) component ).addContainerListener ( containerAdapter );
        }
    }

    /**
     * Destroys this node and all of its data.
     */
    private void destroy ()
    {
        // Destroying all child nodes first
        for ( int i = 0; i < getChildCount (); i++ )
        {
            final InterfaceTreeNode childAt = ( InterfaceTreeNode ) getChildAt ( i );
            childAt.destroy ();
        }

        // Destroying this node
        final Component component = getComponent ();
        component.removeComponentListener ( componentAdapter );
        if ( containerAdapter != null )
        {
            ( ( Container ) component ).removeContainerListener ( containerAdapter );
        }
        componentAdapter = null;
        containerAdapter = null;
        setUserObject ( null );
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
        if ( component instanceof WebGlassPane )
        {
            return glassPaneType;
        }
        else if ( StyleableComponent.isSupported ( component ) )
        {
            return StyleableComponent.get ( ( JComponent ) component ).getIcon ();
        }
        else
        {
            if ( component instanceof Frame )
            {
                return frameType;
            }
            else if ( component instanceof Dialog )
            {
                return dialogType;
            }
            else if ( component instanceof Window )
            {
                return windowType;
            }
            else if ( component instanceof JLayeredPane )
            {
                return layeredPaneType;
            }
            else
            {
                return unknownType;
            }
        }
    }

    @Override
    public String getTitle ()
    {
        final Component component = getComponent ();
        if ( StyleableComponent.isSupported ( component ) )
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