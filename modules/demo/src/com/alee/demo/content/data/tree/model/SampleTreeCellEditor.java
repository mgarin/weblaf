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

package com.alee.demo.content.data.tree.model;

import com.alee.laf.text.WebTextField;
import com.alee.laf.tree.WebTreeCellEditor;

import javax.swing.*;
import java.awt.*;

/**
 * Sample tree cell editor.
 *
 * @author Mikle Garin
 */
public class SampleTreeCellEditor extends WebTreeCellEditor
{
    /**
     * Last edited node.
     */
    protected SampleNode sampleNode;

    /**
     * Constructs sample tree cell editor.
     */
    public SampleTreeCellEditor ()
    {
        super ();
    }

    @Override
    public Component getTreeCellEditorComponent ( final JTree tree, final Object value, final boolean isSelected, final boolean expanded,
                                                  final boolean leaf, final int row )
    {
        this.sampleNode = ( SampleNode ) value;
        final WebTextField editor = ( WebTextField ) super.getTreeCellEditorComponent ( tree, value, isSelected, expanded, leaf, row );
        editor.setText ( sampleNode.getTitle () );
        return editor;
    }

    @Override
    public Object getCellEditorValue ()
    {
        final SampleObject sampleObject = sampleNode.getUserObject ();
        sampleObject.setTitle ( delegate.getCellEditorValue ().toString () );
        return sampleObject;
    }
}