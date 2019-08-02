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

import com.alee.api.ui.IconBridge;
import com.alee.api.ui.TextBridge;
import com.alee.extended.inspector.info.*;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.tree.TreeNodeParameters;
import com.alee.laf.tree.UniqueNode;
import com.alee.managers.style.StyleAdapter;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleListener;
import com.alee.managers.style.StyleManager;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.LafUtils;

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
public class InterfaceTreeNode extends UniqueNode<InterfaceTreeNode, Component>
        implements IconBridge<TreeNodeParameters<InterfaceTreeNode, InterfaceTree>>,
        TextBridge<TreeNodeParameters<InterfaceTreeNode, InterfaceTree>>
{
    /**
     * todo 1. These nodes keep hard reference to components
     */

    /**
     * {@link ComponentPreview} for all windows root.
     */
    protected static final ComponentPreview windowsPreview = new WindowsPreview ();

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
    protected transient InterfaceTree tree;

    /**
     * Constructs new {@link InterfaceTreeNode}.
     *
     * @param tree      {@link InterfaceTree}
     * @param component {@link Component} this node references
     */
    public InterfaceTreeNode ( final InterfaceTree tree, final Component component )
    {
        super ( id ( component ), component );
        this.tree = tree;
        install ();
    }

    @Override
    protected void setId ()
    {
        final Component component = getUserObject ();
        this.id = id ( component );
    }

    /**
     * Returns {@link InterfaceTreeNode} identifier based on the specified {@link Component}.
     *
     * @param component {@link Component} to return {@link InterfaceTreeNode} identifier for
     * @return {@link InterfaceTreeNode} identifier based on the specified {@link Component}
     */
    private static String id ( final Component component )
    {
        return component != null ? Integer.toString ( component.hashCode () ) : "all.windows";
    }

    @Override
    public Icon getIcon ( final TreeNodeParameters<InterfaceTreeNode, InterfaceTree> parameters )
    {
        return getPreview ().getIcon ( getUserObject () );
    }

    @Override
    public String getText ( final TreeNodeParameters<InterfaceTreeNode, InterfaceTree> parameters )
    {
        return getPreview ().getText ( getUserObject () );
    }

    /**
     * Installs component listeners.
     */
    protected void install ()
    {
        // Ensure this is invoked on EDT
        WebLookAndFeel.checkEventDispatchThread ();

        // Ensure this is not a root (all windows) node
        final Component component = getUserObject ();
        if ( component != null )
        {
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
                                final InterfaceTreeNode childNode = new InterfaceTreeNode ( tree, child );
                                tree.addChildNode ( InterfaceTreeNode.this, childNode );
                            }
                        }

                        @Override
                        public void componentRemoved ( final ContainerEvent e )
                        {
                            final Component child = e.getChild ();
                            if ( tree.accept ( child ) )
                            {
                                final String nodeId = id ( child );
                                final InterfaceTreeNode childNode = tree.findNode ( nodeId );
                                childNode.uninstall ();
                                tree.removeNode ( childNode );
                            }
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
                        styleListener = new StyleAdapter ()
                        {
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
    }

    /**
     * Uninstalls component listeners.
     */
    protected void uninstall ()
    {
        // Ensure this is invoked on EDT
        WebLookAndFeel.checkEventDispatchThread ();

        // Destroying all child nodes first
        // We access raw children directly to ensure we don't miss out any if they are currently filtered
        for ( final InterfaceTreeNode child : tree.getModel ().getRawChildren ( InterfaceTreeNode.this ) )
        {
            child.uninstall ();
        }

        // Ensure this is not a root (all windows) node
        final Component component = getUserObject ();
        if ( component != null )
        {
            // Destroying this node
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
        }
    }

    /**
     * Returns component descriptor.
     *
     * @return component descriptor
     */
    protected ComponentPreview getPreview ()
    {
        final Component component = getUserObject ();
        if ( component != null )
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
        else
        {
            return windowsPreview;
        }
    }

    /**
     * Performs delayed {@link InterfaceTreeNode} update.
     *
     * @param tree {@link InterfaceTree}
     */
    protected void updateNodeLater ( final InterfaceTree tree )
    {
        CoreSwingUtils.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                tree.updateNode ( InterfaceTreeNode.this );
            }
        } );
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
        return getPreview ().getText ( getUserObject () );
    }
}