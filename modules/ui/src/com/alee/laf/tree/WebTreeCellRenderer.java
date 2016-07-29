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

import com.alee.api.*;
import com.alee.extended.label.WebStyledLabel;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.ChildStyleId;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.Stateful;
import com.alee.utils.CompareUtils;
import com.alee.utils.ImageUtils;
import com.alee.utils.TextUtils;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom default cell renderer for WebLaF trees.
 *
 * @author Mikle Garin
 */

public class WebTreeCellRenderer extends WebStyledLabel implements TreeCellRenderer, Stateful
{
    /**
     * todo 1. Get rid of the hardcoded icons within renderer
     */

    /**
     * Renderer ID prefix.
     */
    public static final String ID_PREFIX = "WTCR";

    /**
     * Renderer unique ID used to cache tree icons.
     */
    protected final String id;

    /**
     * Additional renderer decoration states.
     */
    protected final List<String> states = new ArrayList<String> ( 5 );

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
        setName ( "Tree.cellRenderer" );
        this.id = TextUtils.generateId ( ID_PREFIX );
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
    public List<String> getStates ()
    {
        return states;
    }

    /**
     * Updates custom renderer states based on render cycle settings.
     *
     * @param tree       tree
     * @param value      cell value
     * @param isSelected whether or not cell is selected
     * @param expanded   whether or not cell is expanded
     * @param leaf       whether or not cell is leaf
     * @param row        cell row number
     * @param hasFocus   whether or not cell has focus
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void updateStates ( final JTree tree, final Object value, final boolean isSelected, final boolean expanded,
                                  final boolean leaf, final int row, final boolean hasFocus )
    {
        states.clear ();
        if ( isSelected )
        {
            states.add ( DecorationState.selected );
        }
        if ( expanded )
        {
            states.add ( DecorationState.expanded );
        }
        if ( hasFocus )
        {
            states.add ( DecorationState.focused );
        }
        if ( leaf )
        {
            states.add ( DecorationState.leaf );
        }
        if ( value instanceof Stateful )
        {
            states.addAll ( ( ( Stateful ) value ).getStates () );
        }
    }

    /**
     * Updates tree cell renderer component style ID.
     *
     * @param tree       tree
     * @param value      cell value
     * @param isSelected whether or not cell is selected
     * @param expanded   whether or not cell is expanded
     * @param leaf       whether or not cell is leaf
     * @param row        cell row number
     * @param hasFocus   whether or not cell has focus
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void updateStyleId ( final JTree tree, final Object value, final boolean isSelected, final boolean expanded,
                                   final boolean leaf, final int row, final boolean hasFocus )
    {
        StyleId id = null;
        if ( value instanceof ChildStyleSupport )
        {
            final ChildStyleId childStyleId = ( ( ChildStyleSupport ) value ).getChildStyleId ();
            if ( childStyleId != null )
            {
                id = childStyleId.at ( tree );
            }
        }
        else if ( value instanceof StyleSupport )
        {
            final StyleId styleId = ( ( StyleSupport ) value ).getStyleId ();
            if ( styleId != null )
            {
                id = styleId;
            }
        }
        if ( id == null )
        {
            id = StyleId.treeCellRenderer.at ( tree );
        }
        setStyleId ( id );
    }

    /**
     * Updating renderer based on the provided settings.
     *
     * @param tree       tree
     * @param value      cell value
     * @param isSelected whether or not cell is selected
     * @param expanded   whether or not cell is expanded
     * @param leaf       whether or not cell is leaf
     * @param row        cell row number
     * @param hasFocus   whether or not cell has focus
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void updateView ( final JTree tree, final Object value, final boolean isSelected, final boolean expanded, final boolean leaf,
                                final int row, final boolean hasFocus )
    {
        setEnabled ( tree.isEnabled () );
        setComponentOrientation ( tree.getComponentOrientation () );
        setFont ( tree.getFont () );
        setForeground ( foregroundForValue ( tree, value, isSelected, expanded, leaf, row, hasFocus ) );
        setIcon ( iconForValue ( tree, value, isSelected, expanded, leaf, row, hasFocus ) );
        setText ( textForValue ( tree, value, isSelected, expanded, leaf, row, hasFocus ) );
    }

    /**
     * Returns renderer foreground color for the specified cell value.
     *
     * @param tree       tree
     * @param value      cell value
     * @param isSelected whether or not cell is selected
     * @param expanded   whether or not cell is expanded
     * @param leaf       whether or not cell is leaf
     * @param row        cell row number
     * @param hasFocus   whether or not cell has focus
     * @return renderer foreground color for the specified cell value
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected Color foregroundForValue ( final JTree tree, final Object value, final boolean isSelected, final boolean expanded,
                                         final boolean leaf, final int row, final boolean hasFocus )
    {
        final Color foreground;
        if ( value instanceof ColorSupport )
        {
            final Color color = ( ( ColorSupport ) value ).getColor ();
            foreground = color != null ? color : tree.getForeground ();
        }
        else
        {
            foreground = tree.getForeground ();
        }
        return foreground;
    }

    /**
     * Returns renderer icon for the specified cell value.
     *
     * @param tree       tree
     * @param value      cell value
     * @param isSelected whether or not cell is selected
     * @param expanded   whether or not cell is expanded
     * @param leaf       whether or not cell is leaf
     * @param row        cell row number
     * @param hasFocus   whether or not cell has focus
     * @return renderer icon for the specified cell value
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected Icon iconForValue ( final JTree tree, final Object value, final boolean isSelected, final boolean expanded,
                                  final boolean leaf, final int row, final boolean hasFocus )
    {
        Icon icon;
        if ( value instanceof IconSupport )
        {
            icon = ( ( IconSupport ) value ).getIcon ();
            if ( !tree.isEnabled () )
            {
                final String id = value instanceof UniqueNode ? ( ( UniqueNode ) value ).getId () : "" + value.hashCode ();
                icon = ImageUtils.getDisabledCopy ( getIconTypeKey ( id ), icon );
            }
        }
        else
        {
            icon = leaf ? leafIcon : tree.getModel ().getRoot () == value ? rootIcon : expanded ? openIcon : closedIcon;
            if ( !tree.isEnabled () )
            {
                final String type = leaf ? "leaf" : tree.getModel ().getRoot () == value ? "root" : expanded ? "open" : "closed";
                icon = ImageUtils.getDisabledCopy ( getIconTypeKey ( type ), icon );
            }
        }
        return icon;
    }

    /**
     * Returns renderer text for the specified cell value.
     *
     * @param tree       tree
     * @param value      cell value
     * @param isSelected whether or not cell is selected
     * @param expanded   whether or not cell is expanded
     * @param leaf       whether or not cell is leaf
     * @param row        cell row number
     * @param hasFocus   whether or not cell has focus
     * @return renderer text for the specified cell value
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected String textForValue ( final JTree tree, final Object value, final boolean isSelected, final boolean expanded,
                                    final boolean leaf, final int row, final boolean hasFocus )
    {
        final String text;
        if ( value instanceof TitleSupport )
        {
            text = ( ( TitleSupport ) value ).getTitle ();
        }
        else
        {
            text = tree.convertValueToText ( value, isSelected, expanded, leaf, row, hasFocus );
        }
        return text;
    }

    /**
     * Returns tree cell renderer component.
     *
     * @param tree       tree
     * @param value      cell value
     * @param isSelected whether or not cell is selected
     * @param expanded   whether or not cell is expanded
     * @param leaf       whether or not cell is leaf
     * @param row        cell row number
     * @param hasFocus   whether or not cell has focus
     * @return cell renderer component
     */
    @Override
    public WebStyledLabel getTreeCellRendererComponent ( final JTree tree, final Object value, final boolean isSelected,
                                                         final boolean expanded, final boolean leaf, final int row, final boolean hasFocus )
    {
        // Updating custom states
        updateStates ( tree, value, isSelected, expanded, leaf, row, hasFocus );

        // Updating style ID
        updateStyleId ( tree, value, isSelected, expanded, leaf, row, hasFocus );

        // Updating renderer view
        updateView ( tree, value, isSelected, expanded, leaf, row, hasFocus );

        // Updating decoration states for this render cycle
        DecorationUtils.fireStatesChanged ( this );

        return this;
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
        if ( CompareUtils.equals ( pn, StyleId.STYLE_PROPERTY, StyleId.PARENT_STYLE_PROPERTY, WebLookAndFeel.TEXT_PROPERTY,
                AbstractDecorationPainter.DECORATION_STATES_PROPERTY, WebStyledLabel.STYLE_RANGES_PROPERTY ) )
        {
            super.firePropertyChange ( pn, oldValue, newValue );
        }
        else if ( CompareUtils.equals ( pn, WebLookAndFeel.FONT_PROPERTY, WebLookAndFeel.FOREGROUND_PROPERTY ) &&
                oldValue != newValue && getClientProperty ( javax.swing.plaf.basic.BasicHTML.propertyKey ) != null )
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

    /**
     * A subclass of {@link com.alee.laf.tree.WebTreeCellRenderer} that implements {@link javax.swing.plaf.UIResource}.
     * It is used to determine cell renderer provided by the UI class to properly uninstall it on UI uninstall.
     */
    public static class UIResource extends WebTreeCellRenderer implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link com.alee.laf.tree.WebTreeCellRenderer}.
         */
    }
}