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
import com.alee.extended.inspector.info.AWTComponentPreview;
import com.alee.extended.inspector.info.ComponentPreview;
import com.alee.extended.inspector.info.JComponentPreview;
import com.alee.extended.inspector.info.WComponentPreview;
import com.alee.laf.tree.TreeState;
import com.alee.laf.tree.UniqueNode;
import com.alee.managers.style.Skin;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleListener;
import com.alee.managers.style.StyleManager;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;

/**
 * Custom node representing a single component in {@link InterfaceTree}.
 *
 * @author Mikle Garin
 */

public class InterfaceTreeNode extends UniqueNode implements IconSupport, TitleSupport
{
    /**
     * {@link ComponentPreview} for extended components.
     */
    protected static final ComponentPreview wComponentPreview = new WComponentPreview ();

    /**
     * {@link ComponentPreview} for {@link JComponent}s.
     */
    protected static final ComponentPreview jComponentPreview = new JComponentPreview ();

    /**
     * {@link ComponentPreview} for {@link Component}s.
     */
    protected static final ComponentPreview awtComponentPreview = new AWTComponentPreview ();

    /**
     * Component state listeners.
     */
    protected transient ComponentAdapter componentAdapter;
    protected transient ContainerAdapter containerAdapter;
    protected transient StyleListener styleListener;

    /**
     * Constructs new {@link InterfaceTreeNode}.
     *
     * @param tree      {@link InterfaceTree}
     * @param component {@link Component} this node references
     */
    public InterfaceTreeNode ( final InterfaceTree tree, final Component component )
    {
        super ( Integer.toString ( component.hashCode () ), component );

        /**
         * Node component listener.
         */
        componentAdapter = new ComponentAdapter ()
        {
            @Override
            public void componentResized ( final ComponentEvent e )
            {
                /**
                 * We don't need to react to size changes.
                 * Also call to {@link InterfaceTreeNode#updateNodeLater(InterfaceTree)} causes UI flickering so this is disabled.
                 */
            }

            @Override
            public void componentMoved ( final ComponentEvent e )
            {
                /**
                 * We don't need to react to location changes.
                 * Also call to {@link InterfaceTreeNode#updateNodeLater(InterfaceTree)} causes UI flickering so this is disabled.
                 */
            }

            @Override
            public void componentShown ( final ComponentEvent e )
            {
                updateNodeLater ( tree );
            }

            @Override
            public void componentHidden ( final ComponentEvent e )
            {
                updateNodeLater ( tree );
            }
        };
        component.addComponentListener ( componentAdapter );

        /**
         * It is important to avoid listening to {@link CellRendererPane}.
         * It can generates hundreds of events and will clutter us with unnecessary updates.
         * This will basically workaround any issues with {@link CellRendererPane} in {@link JTree}, {@link JList} and {@link JTable}.
         */
        if ( !( component instanceof CellRendererPane ) )
        {
            /**
             * Additional container listeners.
             */
            if ( component instanceof Container )
            {
                containerAdapter = new ContainerAdapter ()
                {
                    @Override
                    public void componentAdded ( final ContainerEvent e )
                    {
                        final Component child = e.getChild ();
                        if ( tree.accept ( child ) )
                        {
                            final TreeState treeState = tree.getTreeState ( InterfaceTreeNode.this );
                            tree.addChildNode ( InterfaceTreeNode.this, new InterfaceTreeNode ( tree, child ) );
                            tree.setTreeState ( treeState, InterfaceTreeNode.this );
                        }
                    }

                    @Override
                    public void componentRemoved ( final ContainerEvent e )
                    {
                        final TreeState treeState = tree.getTreeState ( InterfaceTreeNode.this );
                        final Component child = e.getChild ();
                        for ( int i = 0; i < InterfaceTreeNode.this.getChildCount (); i++ )
                        {
                            final InterfaceTreeNode childNode = ( InterfaceTreeNode ) InterfaceTreeNode.this.getChildAt ( i );
                            if ( childNode.getComponent () == child )
                            {
                                tree.removeNode ( childNode );
                                childNode.destroy ();
                                break;
                            }
                        }
                        tree.setTreeState ( treeState, InterfaceTreeNode.this );
                    }
                };
                ( ( Container ) component ).addContainerListener ( containerAdapter );
            }

            /**
             * Additional listeners for components using WebLaF-based UI.
             * This is checked instead of styling support to avoid issues when WebLaF is not installed as LaF.
             */
            if ( component instanceof JComponent )
            {
                final JComponent jComponent = ( JComponent ) component;
                if ( LafUtils.hasWebLafUI ( jComponent ) )
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
                            updateNodeLater ( tree );
                        }
                    };
                    StyleManager.addStyleListener ( jComponent, styleListener );
                }
            }
        }
    }

    /**
     * Performs delayed {@link InterfaceTreeNode} update.
     *
     * @param tree {@link InterfaceTree}
     */
    protected void updateNodeLater ( final InterfaceTree tree )
    {
        SwingUtils.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                tree.updateNode ( InterfaceTreeNode.this );
            }
        } );
    }

    /**
     * Destroys this node and all of its data.
     */
    protected void destroy ()
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
    protected void setId ()
    {
        this.id = Integer.toString ( getComponent ().hashCode () );
    }

    @Override
    public Icon getIcon ()
    {
        final Component component = getComponent ();
        return getPreview ( component ).getIcon ( component );
    }

    @Override
    public String getTitle ()
    {
        final Component component = getComponent ();
        return getPreview ( component ).getText ( component );
    }

    /**
     * Returns component descriptor.
     *
     * @param component component to return descriptor for
     * @return component descriptor
     */
    protected ComponentPreview getPreview ( final Component component )
    {
        if ( component instanceof JComponent )
        {
            if ( StyleManager.isSupported ( ( JComponent ) component ) )
            {
                return wComponentPreview;
            }
            else
            {
                return jComponentPreview;
            }
        }
        else
        {
            return awtComponentPreview;
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