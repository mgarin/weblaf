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

import com.alee.laf.tree.WebTreeCellRenderer;
import com.alee.laf.tree.WebTreeElement;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.data.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Language dictionaries tree renderer.
 *
 * @author Mikle Garin
 */

public class DictionariesTreeRenderer extends WebTreeCellRenderer
{
    /**
     * Various node icons
     */
    private static ImageIcon multidicIcon = new ImageIcon ( DictionariesTreeRenderer.class.getResource ( "icons/multidic.png" ) );
    private static ImageIcon dicIcon = new ImageIcon ( DictionariesTreeRenderer.class.getResource ( "icons/dic.png" ) );
    private static ImageIcon recordIcon = new ImageIcon ( DictionariesTreeRenderer.class.getResource ( "icons/record.png" ) );
    private static ImageIcon textIcon = new ImageIcon ( DictionariesTreeRenderer.class.getResource ( "icons/text.png" ) );
    private static ImageIcon tooltipIcon = new ImageIcon ( DictionariesTreeRenderer.class.getResource ( "icons/tooltip.png" ) );

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
    public WebTreeElement getTreeCellRendererComponent ( JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf,
                                                         int row, boolean hasFocus )
    {
        super.getTreeCellRendererComponent ( tree, value, isSelected, expanded, leaf, row, hasFocus );

        Object val = ( ( DefaultMutableTreeNode ) value ).getUserObject ();
        if ( val == null )
        {
            setIcon ( multidicIcon );
            setText ( "Dictionaries" );
        }
        else if ( val instanceof Dictionary )
        {
            Dictionary d = ( Dictionary ) val;
            setIcon ( dicIcon );
            setText ( "<html><b>" + d.getPrefix () + "</b> - " + d.getName () + "</html>" );
        }
        else if ( val instanceof Record )
        {
            Record r = ( Record ) val;
            setIcon ( recordIcon );
            setText ( expanded ? "<html><b>" + r.getKey () + "</b>" +
                    ( r.getHotkey () != null ? " (" + r.getHotkey () + ")" : "" ) + "</html>" : r.toString ( true ) );
        }
        else if ( val instanceof Value )
        {
            Value v = ( Value ) val;
            setIcon ( LanguageManager.getLanguageIcon ( v.getLang () ) );
            setText ( v.getLang () +
                    ( v.getMnemonic () != null ? " (" + v.getMnemonic () + ")" : "" ) +
                    ( v.getHotkey () != null ? " (" + v.getHotkey () + ")" : "" ) );
        }
        else if ( val instanceof Text )
        {
            Text text = ( Text ) val;
            setIcon ( textIcon );
            setText ( text.getState () == null ? text.toString () :
                    "<html><b>" + text.getState () + "</b> -> " + text.toString () + "</html>" );
        }
        else if ( val instanceof Tooltip )
        {
            setIcon ( tooltipIcon );
            setText ( val.toString () );
        }

        return this;
    }
}