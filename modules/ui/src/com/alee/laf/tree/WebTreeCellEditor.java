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

import com.alee.extended.image.WebImage;
import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.text.WebTextField;
import com.alee.managers.style.StyleId;
import com.alee.utils.swing.WebDefaultCellEditor;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * This class provides a styled default cell editor for trees.
 *
 * @param <C> editor {@link JComponent} type
 * @author Mikle Garin
 */
public class WebTreeCellEditor<C extends JComponent> extends WebDefaultCellEditor<C> implements FocusListener
{
    /**
     * todo 1. Break this editor into proper separate implementations instead of Swing-way chaos
     */

    /**
     * Whether should update editor's leading icon automatically when it is possible or not.
     */
    protected boolean autoUpdateLeadingIcon = true;

    /**
     * Constructs default tree cell editor with a text field as editor.
     */
    public WebTreeCellEditor ()
    {
        this ( new WebTextField () );
    }

    /**
     * Constructs tree cell editor with a specified text field as editor.
     *
     * @param textField editor textfield
     */
    public WebTreeCellEditor ( final WebTextField textField )
    {
        super ( textField );
    }

    /**
     * Constructs tree cell editor with a specified check box as editor.
     *
     * @param checkBox editor checkbox
     */
    public WebTreeCellEditor ( final WebCheckBox checkBox )
    {
        super ( checkBox );
    }

    /**
     * Constructs tree cell editor with a specified combo box as editor.
     *
     * @param comboBox editor combobox
     */
    public WebTreeCellEditor ( final WebComboBox comboBox )
    {
        super ( comboBox );
    }

    @Override
    public void focusGained ( final FocusEvent e )
    {
        // Do nothing
    }

    @Override
    public void focusLost ( final FocusEvent e )
    {
        stopCellEditing ();
    }

    @Override
    public boolean stopCellEditing ()
    {
        // Properly remove focus listener to avoid incorrect stop edit calls
        final boolean stopped = super.stopCellEditing ();
        if ( stopped )
        {
            editorComponent.removeFocusListener ( this );
        }
        return stopped;
    }

    @Override
    public void cancelCellEditing ()
    {
        // Properly remove focus listener to avoid incorrect stop edit calls
        editorComponent.removeFocusListener ( this );
        super.cancelCellEditing ();
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
        final Component cellEditor = super.getTreeCellEditorComponent ( tree, value, isSelected, expanded, leaf, row );

        // Focus listener to stop editing on focus loss event
        cellEditor.addFocusListener ( this );

        // Copying editor size from cell renderer size
        final TreeCellRenderer r = tree.getCellRenderer ();
        final Component component = r.getTreeCellRendererComponent ( tree, value, isSelected, expanded, leaf, row, true );
        cellEditor.setPreferredSize ( component.getPreferredSize () );

        // Updating editor styling
        if ( cellEditor instanceof WebTextField )
        {
            // Field styling
            final WebTextField editor = ( WebTextField ) cellEditor;
            editor.setStyleId ( StyleId.treeCellEditor.at ( tree ) );

            // Updating leading icon styling
            if ( component instanceof JLabel && ( ( JLabel ) component ).getIcon () != null )
            {
                final JLabel label = ( JLabel ) component;
                if ( autoUpdateLeadingIcon )
                {
                    // Leading icon styling
                    final WebImage image = new WebImage ( StyleId.treeCellEditorIcon.at ( editor ), label.getIcon () );
                    editor.setLeadingComponent ( image );
                }
            }
        }

        // Applying component orientation
        cellEditor.applyComponentOrientation ( tree.getComponentOrientation () );

        return cellEditor;
    }
}