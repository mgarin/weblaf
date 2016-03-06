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

package com.alee.extended.language;

import com.alee.extended.label.WebStyledLabel;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.data.*;
import com.alee.managers.style.StyleId;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

/**
 * Language dictionaries tree renderer.
 *
 * @author Mikle Garin
 */

public class DictionariesTreeRenderer extends WebStyledLabel implements TreeCellRenderer
{
    /**
     * Various node icons.
     */
    private static final ImageIcon multidicIcon = new ImageIcon ( DictionariesTreeRenderer.class.getResource ( "icons/multidic.png" ) );
    private static final ImageIcon dicIcon = new ImageIcon ( DictionariesTreeRenderer.class.getResource ( "icons/dic.png" ) );
    private static final ImageIcon recordIcon = new ImageIcon ( DictionariesTreeRenderer.class.getResource ( "icons/record.png" ) );
    private static final ImageIcon textIcon = new ImageIcon ( DictionariesTreeRenderer.class.getResource ( "icons/text.png" ) );
    private static final ImageIcon tooltipIcon = new ImageIcon ( DictionariesTreeRenderer.class.getResource ( "icons/tooltip.png" ) );

    /**
     * Returns custom tree cell renderer component.
     *
     * @param tree       tree
     * @param value      cell value
     * @param isSelected whether cell is selected or not
     * @param expanded   whether cell is expanded or not
     * @param leaf       whether cell is leaf or not
     * @param row        cell row number
     * @param hasFocus   whether cell has focusor not
     * @return renderer component
     */
    @Override
    public WebStyledLabel getTreeCellRendererComponent ( final JTree tree, final Object value, final boolean isSelected,
                                                         final boolean expanded, final boolean leaf, final int row, final boolean hasFocus )
    {
        // Updating custom style ID
        setStyleId ( StyleId.treeCellRenderer.at ( tree ) );

        // Visual settings
        setFont ( tree.getFont () );
        setEnabled ( tree.isEnabled () );

        // Orientation
        setComponentOrientation ( tree.getComponentOrientation () );

        // Updating text
        final Object val = ( ( DefaultMutableTreeNode ) value ).getUserObject ();
        if ( val == null )
        {
            setIcon ( multidicIcon );
            setText ( "Dictionaries" );
        }
        else if ( val instanceof Dictionary )
        {
            final Dictionary d = ( Dictionary ) val;
            setIcon ( dicIcon );
            setText ( "{" + d.getPrefix () + ":b} - " + d.getName () );
        }
        else if ( val instanceof Record )
        {
            final Record r = ( Record ) val;
            setIcon ( recordIcon );
            setText ( expanded ? "{" + r.getKey () + ":b}" +
                    ( r.getHotkey () != null ? " (" + r.getHotkey () + ")" : "" ) : r.toString ( true ) );
        }
        else if ( val instanceof Value )
        {
            final Value v = ( Value ) val;
            setIcon ( LanguageManager.getLanguageIcon ( v.getLang () ) );
            setText ( v.getLang () +
                    ( v.getMnemonic () != null ? " (" + v.getMnemonic () + ")" : "" ) +
                    ( v.getHotkey () != null ? " (" + v.getHotkey () + ")" : "" ) );
        }
        else if ( val instanceof Text )
        {
            final Text text = ( Text ) val;
            setIcon ( textIcon );
            setText ( text.getState () == null ? text.toString () : "{" + text.getState () + ":b} -> " + text.toString () );
        }
        else if ( val instanceof Tooltip )
        {
            setIcon ( tooltipIcon );
            setText ( val.toString () );
        }

        return this;
    }
}