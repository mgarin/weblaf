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

import com.alee.laf.text.WebTextField;
import com.alee.laf.tree.WebTreeCellEditor;
import com.alee.managers.language.data.Dictionary;
import com.alee.managers.language.data.Record;
import com.alee.managers.language.data.Text;
import com.alee.managers.language.data.Value;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.EventObject;
import java.util.Locale;

/**
 * Language dictionaries tree cell editor.
 *
 * @author Mikle Garin
 */

public class DictionariesTreeEditor extends WebTreeCellEditor
{
    /**
     * Dictionaries tree for this editor.
     */
    private final DictionariesTree tree;

    /**
     * Last editor's value.
     */
    private DefaultMutableTreeNode lastValue = null;

    /**
     * Constructs dictionaries tree editor for the specified dictionaries tree.
     *
     * @param tree dictionaries tree
     */
    public DictionariesTreeEditor ( final DictionariesTree tree )
    {
        super ();
        this.tree = tree;
    }

    /**
     * Returns custom tree cell editor component.
     *
     * @param tree       tree
     * @param value      cell value
     * @param isSelected whether cell is selected or not
     * @param expanded   whether cell is expanded or not
     * @param leaf       whether cell is leaf or not
     * @param row        cell row index
     * @return cell editor component
     */
    @Override
    public Component getTreeCellEditorComponent ( final JTree tree, final Object value, final boolean isSelected, final boolean expanded,
                                                  final boolean leaf, final int row )
    {
        final WebTextField editor = ( WebTextField ) super.getTreeCellEditorComponent ( tree, value, isSelected, expanded, leaf, row );

        lastValue = ( DefaultMutableTreeNode ) value;
        final Object val = lastValue.getUserObject ();
        if ( val instanceof Dictionary )
        {
            editor.setText ( ( ( Dictionary ) val ).getPrefix () );
        }
        else if ( val instanceof Record )
        {
            editor.setText ( ( ( Record ) val ).getKey () );
        }
        else if ( val instanceof Value )
        {
            // todo Add country editing
            editor.setText ( ( ( Value ) val ).getLocale ().getLanguage () );
        }
        else if ( val instanceof Text )
        {
            // todo Add mnemonic editor
            editor.setText ( ( ( Text ) val ).getText () );
        }

        return editor;
    }

    /**
     * Returns current editor's value.
     *
     * @return current editor's value
     */
    @Override
    public Object getCellEditorValue ()
    {
        final String editorValue = ( String ) super.getCellEditorValue ();
        final Object val = lastValue.getUserObject ();
        if ( val instanceof Dictionary )
        {
            ( ( Dictionary ) val ).setPrefix ( editorValue );
        }
        else if ( val instanceof Record )
        {
            ( ( Record ) val ).setKey ( editorValue );
        }
        else if ( val instanceof Value )
        {
            // todo Add country editing
            ( ( Value ) val ).setLocale ( new Locale ( editorValue ) );
        }
        else if ( val instanceof Text )
        {
            // todo Add mnemonic editor
            ( ( Text ) val ).setText ( editorValue );
        }
        return val;
    }

    /**
     * Returns whether cell is editable or not.
     *
     * @param event event that editor should use to consider whether to begin editing or no
     * @return true if cell is editable, false otherwise
     */
    @Override
    public boolean isCellEditable ( final EventObject event )
    {
        final Object value = tree.getSelectedValue ();
        return value != null &&
                ( value instanceof Dictionary || value instanceof Record || value instanceof Value || value instanceof Text ) &&
                super.isCellEditable ( event );
    }
}