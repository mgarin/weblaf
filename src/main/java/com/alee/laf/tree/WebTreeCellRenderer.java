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

import com.alee.utils.ImageUtils;
import com.alee.utils.TextUtils;

import javax.swing.*;
import javax.swing.plaf.TreeUI;
import javax.swing.tree.TreeCellRenderer;

/**
 * Custom default tree cell renderer for WebLookAndFeel.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class WebTreeCellRenderer extends WebTreeElement implements TreeCellRenderer
{
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
        setForeground ( UIManager.getColor ( "Tree.textForeground" ) );
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
    public WebTreeElement getTreeCellRendererComponent ( JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf,
                                                         int row, boolean hasFocus )
    {
        final boolean enabled = tree.isEnabled ();

        // Visual settings
        setFont ( tree.getFont () );
        setEnabled ( enabled );

        // Icon
        ImageIcon icon = leaf ? leafIcon : ( tree.getModel ().getRoot () == value ? rootIcon : ( expanded ? openIcon : closedIcon ) );
        if ( enabled )
        {
            setIcon ( icon );
        }
        else
        {
            String type = leaf ? "leaf" : ( tree.getModel ().getRoot () == value ? "root" : ( expanded ? "open" : "closed" ) );
            setIcon ( ImageUtils.getDisabledCopy ( getIconTypeKey ( type ), icon ) );
        }

        // Text
        setText ( tree.convertValueToText ( value, isSelected, expanded, leaf, row, hasFocus ) );

        // Border
        TreeUI tui = tree.getUI ();
        int sw = tui instanceof WebTreeUI ? ( ( WebTreeUI ) tui ).getSelectionShadeWidth () : WebTreeStyle.selectionShadeWidth;
        setMargin ( sw + 2, sw + 2, sw + 2, sw + 4 );

        // Orientation
        setComponentOrientation ( tree.getComponentOrientation () );

        return this;
    }

    /**
     * Returns icon type key for this cell renderer.
     *
     * @param type icon type
     * @return icon type key for this cell renderer
     */
    private String getIconTypeKey ( String type )
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
    public void setRootIcon ( Icon rootIcon )
    {
        this.rootIcon = ImageUtils.getImageIcon ( rootIcon );
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
    public void setOpenIcon ( Icon openIcon )
    {
        this.openIcon = ImageUtils.getImageIcon ( openIcon );
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
    public void setClosedIcon ( Icon closedIcon )
    {
        this.closedIcon = ImageUtils.getImageIcon ( closedIcon );
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
    public void setLeafIcon ( Icon leafIcon )
    {
        this.leafIcon = ImageUtils.getImageIcon ( leafIcon );
        ImageUtils.clearDisabledCopyCache ( getIconTypeKey ( "leaf" ) );
    }
}