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
import com.alee.extended.inspector.info.AWTComponentInfo;
import com.alee.extended.inspector.info.ComponentInfo;
import com.alee.extended.inspector.info.JComponentInfo;
import com.alee.extended.inspector.info.StyleableInfo;
import com.alee.laf.tree.UniqueNode;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.managers.style.StyleableComponent;
import com.alee.managers.style.Skin;
import com.alee.managers.style.StyleListener;

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
     * Component short info providers.
     */
    private static final ComponentInfo styleableInfo = new StyleableInfo ();
    private static final ComponentInfo jComponentInfo = new JComponentInfo ();
    private static final ComponentInfo awtComponentInfo = new AWTComponentInfo ();

    /**
     * Interface components tree.
     */
    protected final InterfaceTree tree;

    /**
     * Component state listeners.
     */
    private ComponentAdapter componentAdapter;
    private ContainerAdapter containerAdapter;
    private StyleListener styleListener;

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
                tree.updateNode ( InterfaceTreeNode.this );
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
        if ( component instanceof JComponent && StyleableComponent.isSupported ( component ) )
        {
            styleListener = new StyleListener ()
            {
                @Override
                public void skinChanged ( final JComponent component, final Skin oldSkin, final Skin newSkin )
                {
                    // We don't need to react to visual updates
                }

                @Override
                public void styleChanged ( final JComponent component, final StyleId oldStyleId, final StyleId newStyleId )
                {
                    // We don't need to react to visual updates
                }

                @Override
                public void skinUpdated ( final JComponent component, final StyleId styleId )
                {
                    tree.repaint ( InterfaceTreeNode.this );
                }
            };
            StyleManager.addStyleListener ( ( JComponent ) component, styleListener );
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
        if ( styleListener != null )
        {
            StyleManager.removeStyleListener ( ( JComponent ) component, styleListener );
        }
        componentAdapter = null;
        containerAdapter = null;
        styleListener = null;
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
        final StyleableComponent type = getType ( component );
        return getInfo ( component ).getIcon ( type, component );
    }

    @Override
    public String getTitle ()
    {
        final Component component = getComponent ();
        final StyleableComponent type = getType ( component );
        return getInfo ( component ).getText ( type, component );
    }

    protected StyleableComponent getType ( final Component component )
    {
        return StyleableComponent.isSupported ( component ) ? StyleableComponent.get ( ( JComponent ) component ) : null;
    }

    protected ComponentInfo getInfo ( final Component component )
    {
        if ( StyleableComponent.isSupported ( component ) )
        {
            return styleableInfo;
        }
        else if ( component instanceof JComponent )
        {
            return jComponentInfo;
        }
        else
        {
            return awtComponentInfo;
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