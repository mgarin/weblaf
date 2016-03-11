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

package com.alee.laf.tree;

import com.alee.api.ColorSupport;
import com.alee.api.IconSupport;
import com.alee.api.TitleSupport;
import com.alee.extended.label.WebStyledLabel;
import com.alee.managers.style.StyleId;
import com.alee.utils.ImageUtils;
import com.alee.utils.TextUtils;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

/**
 * Custom default tree cell renderer for WebLookAndFeel.
 *
 * @author Mikle Garin
 */

public class WebTreeCellRenderer extends WebStyledLabel implements TreeCellRenderer
{
    /**
     * todo 1. Implement Stateful to provide
     */

    /**
     * Renderer ID prefix.
     */
    public static final String ID_PREFIX = "WTCR";

    /**
     * Renderer unique ID used to cache tree icons.
     */
    protected String id;

    /**
     * Icon used to show non-leaf nodes that are expanded.
     */
    protected ImageIcon rootIcon = WebTreeUI.ROOT_ICON;

    /**
     * Icon used to show non-leaf nodes that are expanded.
     */
    protected ImageIcon openIcon = WebTreeUI.OPEN_ICON;

    /**
     * Icon used to show non-leaf nodes that aren't expanded.
     */
    protected ImageIcon closedIcon = WebTreeUI.CLOSED_ICON;

    /**
     * Icon used to show leaf nodes.
     */
    protected ImageIcon leafIcon = WebTreeUI.LEAF_ICON;

    /**
     * Constructs default tree cell renderer.
     */
    public WebTreeCellRenderer ()
    {
        super ();
        setId ();
        setName ( "Tree.cellRenderer" );
    }

    /**
     * Setup unique renderer ID.
     */
    private void setId ()
    {
        this.id = TextUtils.generateId ( ID_PREFIX );
    }

    /**
     * Returns tree cell renderer component.
     *
     * @param tree       tree
     * @param value      cell value
     * @param isSelected whether cell is selected or not
     * @param expanded   whether cell is expanded or not
     * @param leaf       whether cell is leaf or not
     * @param row        cell row number
     * @param hasFocus   whether cell has focus or not
     * @return cell renderer component
     */
    @Override
    public WebStyledLabel getTreeCellRendererComponent ( final JTree tree, final Object value, final boolean isSelected,
                                                         final boolean expanded, final boolean leaf, final int row, final boolean hasFocus )
    {
        final boolean enabled = tree.isEnabled ();

        // Updating custom style ID
        setStyleId ( StyleId.treeCellRenderer.at ( tree ) );

        // Visual settings
        setEnabled ( enabled );
        setFont ( tree.getFont () );
        setComponentOrientation ( tree.getComponentOrientation () );

        // Foreground
        if ( value instanceof ColorSupport )
        {
            final Color color = ( ( ColorSupport ) value ).getColor ();
            setForeground ( color != null ? color : tree.getForeground () );
        }
        else
        {
            setForeground ( tree.getForeground () );
        }

        // Icon
        if ( value instanceof IconSupport )
        {
            final Icon icon = ( ( IconSupport ) value ).getIcon ();
            if ( enabled )
            {
                setIcon ( icon );
            }
            else
            {
                final String id = value instanceof UniqueNode ? ( ( UniqueNode ) value ).getId () : "" + value.hashCode ();
                setIcon ( ImageUtils.getDisabledCopy ( getIconTypeKey ( id ), icon ) );
            }
        }
        else
        {
            final ImageIcon icon = leaf ? leafIcon : tree.getModel ().getRoot () == value ? rootIcon : expanded ? openIcon : closedIcon;
            if ( enabled )
            {
                setIcon ( icon );
            }
            else
            {
                final String type = leaf ? "leaf" : tree.getModel ().getRoot () == value ? "root" : expanded ? "open" : "closed";
                setIcon ( ImageUtils.getDisabledCopy ( getIconTypeKey ( type ), icon ) );
            }
        }

        // Text
        if ( value instanceof TitleSupport )
        {
            setText ( ( ( TitleSupport ) value ).getTitle () );
        }
        else
        {
            setText ( tree.convertValueToText ( value, isSelected, expanded, leaf, row, hasFocus ) );
        }

        return this;
    }

    /**
     * Returns icon type key for this cell renderer.
     *
     * @param type icon type
     * @return icon type key for this cell renderer
     */
    private String getIconTypeKey ( final String type )
    {
        return "WebTreeCellRenderer." + id + "." + type;
    }

    /**
     * Returns the icon used to present root node.
     *
     * @return icon used to present root node
     */
    public Icon getRootIcon ()
    {
        return rootIcon;
    }

    /**
     * Sets the icon used to present root node.
     *
     * @param rootIcon icon used to present root node
     */
    public void setRootIcon ( final Icon rootIcon )
    {
        this.rootIcon = rootIcon != null ? ImageUtils.getImageIcon ( rootIcon ) : null;
        ImageUtils.clearDisabledCopyCache ( getIconTypeKey ( "root" ) );
    }

    /**
     * Returns the icon used to represent non-leaf nodes that are expanded.
     *
     * @return icon used to represent non-leaf nodes that are expanded.
     */
    public Icon getOpenIcon ()
    {
        return openIcon;
    }

    /**
     * Sets the icon used to represent non-leaf nodes that are expanded.
     *
     * @param openIcon icon used to represent non-leaf nodes that are expanded
     */
    public void setOpenIcon ( final Icon openIcon )
    {
        this.openIcon = openIcon != null ? ImageUtils.getImageIcon ( openIcon ) : null;
        ImageUtils.clearDisabledCopyCache ( getIconTypeKey ( "open" ) );
    }

    /**
     * Returns the icon used to represent non-leaf nodes that are not expanded.
     *
     * @return icon used to represent non-leaf nodes that are not expanded
     */
    public Icon getClosedIcon ()
    {
        return closedIcon;
    }

    /**
     * Sets the icon used to represent non-leaf nodes that are not expanded.
     *
     * @param closedIcon icon used to represent non-leaf nodes that are not expanded
     */
    public void setClosedIcon ( final Icon closedIcon )
    {
        this.closedIcon = closedIcon != null ? ImageUtils.getImageIcon ( closedIcon ) : null;
        ImageUtils.clearDisabledCopyCache ( getIconTypeKey ( "closed" ) );
    }

    /**
     * Returns the icon used to represent leaf nodes.
     *
     * @return the icon used to represent leaf nodes
     */
    public Icon getLeafIcon ()
    {
        return leafIcon;
    }

    /**
     * Sets the icon used to represent leaf nodes.
     *
     * @param leafIcon icon used to represent leaf nodes
     */
    public void setLeafIcon ( final Icon leafIcon )
    {
        this.leafIcon = leafIcon != null ? ImageUtils.getImageIcon ( leafIcon ) : null;
        ImageUtils.clearDisabledCopyCache ( getIconTypeKey ( "leaf" ) );
    }

    @Override
    public void validate ()
    {
        // Overridden for performance reasons
    }

    @Override
    public void invalidate ()
    {
        // Overridden for performance reasons
    }

    @Override
    public void revalidate ()
    {
        // Overridden for performance reasons
    }

    @Override
    public void repaint ( final long tm, final int x, final int y, final int width, final int height )
    {
        // Overridden for performance reasons
    }

    @Override
    public void repaint ( final Rectangle r )
    {
        // Overridden for performance reasons
    }

    @Override
    public void repaint ()
    {
        // Overridden for performance reasons
    }

    @Override
    protected void firePropertyChange ( final String pn, final Object oldValue, final Object newValue )
    {
        // Overridden for performance reasons
        if ( pn.equals ( "text" ) || ( ( pn.equals ( "font" ) || pn.equals ( "foreground" ) ) && oldValue != newValue &&
                getClientProperty ( javax.swing.plaf.basic.BasicHTML.propertyKey ) != null ) )
        {
            super.firePropertyChange ( pn, oldValue, newValue );
        }
    }

    @Override
    public void firePropertyChange ( final String propertyName, final byte oldValue, final byte newValue )
    {
        // Overridden for performance reasons
    }

    @Override
    public void firePropertyChange ( final String propertyName, final char oldValue, final char newValue )
    {
        // Overridden for performance reasons
    }

    @Override
    public void firePropertyChange ( final String propertyName, final short oldValue, final short newValue )
    {
        // Overridden for performance reasons
    }

    @Override
    public void firePropertyChange ( final String propertyName, final int oldValue, final int newValue )
    {
        // Overridden for performance reasons
    }

    @Override
    public void firePropertyChange ( final String propertyName, final long oldValue, final long newValue )
    {
        // Overridden for performance reasons
    }

    @Override
    public void firePropertyChange ( final String propertyName, final float oldValue, final float newValue )
    {
        // Overridden for performance reasons
    }

    @Override
    public void firePropertyChange ( final String propertyName, final double oldValue, final double newValue )
    {
        // Overridden for performance reasons
    }

    @Override
    public void firePropertyChange ( final String propertyName, final boolean oldValue, final boolean newValue )
    {
        // Overridden for performance reasons
    }
}