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

import javax.swing.*;
import javax.swing.plaf.TreeUI;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * This class provides a styled default cell editor for trees.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class WebTreeCellEditor extends DefaultCellEditor
{
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
    public WebTreeCellEditor ( WebTextField textField )
    {
        super ( textField );

        // Focus listener to stop editing on focus loss event
        textField.addFocusListener ( new FocusAdapter ()
        {
            public void focusLost ( FocusEvent e )
            {
                stopCellEditing ();
            }
        } );
    }

    /**
     * Constructs tree cell editor with a specified check box as editor.
     *
     * @param checkBox editor checkbox
     */
    public WebTreeCellEditor ( WebCheckBox checkBox )
    {
        super ( checkBox );

        // Focus listener to stop editing on focus loss event
        checkBox.addFocusListener ( new FocusAdapter ()
        {
            public void focusLost ( FocusEvent e )
            {
                stopCellEditing ();
            }
        } );
    }

    /**
     * Constructs tree cell editor with a specified combo box as editor.
     *
     * @param comboBox editor combobox
     */
    public WebTreeCellEditor ( WebComboBox comboBox )
    {
        super ( comboBox );

        // Focus listener to stop editing on focus loss event
        comboBox.addFocusListener ( new FocusAdapter ()
        {
            public void focusLost ( FocusEvent e )
            {
                stopCellEditing ();
            }
        } );
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
    public Component getTreeCellEditorComponent ( JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row )
    {
        final Component cellEditor = super.getTreeCellEditorComponent ( tree, value, isSelected, expanded, leaf, row );

        // Copying editor size from cell renderer size
        Component component = tree.getCellRenderer ().getTreeCellRendererComponent ( tree, value, isSelected, expanded, leaf, row, true );
        cellEditor.setPreferredSize ( component.getPreferredSize () );

        // Updating editor styling
        if ( component instanceof JLabel && ( ( JLabel ) component ).getIcon () != null )
        {
            JLabel label = ( JLabel ) component;
            boolean ltr = tree.getComponentOrientation ().isLeftToRight ();

            if ( cellEditor instanceof WebTextField )
            {
                TreeUI tui = tree.getUI ();
                int sw = tui instanceof WebTreeUI ? ( ( WebTreeUI ) tui ).getSelectionShadeWidth () : WebTreeStyle.selectionShadeWidth;

                // Field styling
                WebTextField editor = ( WebTextField ) cellEditor;
                editor.setDrawFocus ( false );
                editor.setShadeWidth ( sw );
                editor.setDrawShade ( false );
                editor.setLeadingComponent ( new WebImage ( label.getIcon () ) );

                // Field side margin
                int sm = sw + 1;
                Insets margin = label.getInsets ();
                editor.setMargin ( margin.top - sm, margin.left - sm, margin.bottom - sm, margin.right - sm - 2 );

                // Gap between leading icon and text
                editor.setFieldMargin ( 0, label.getIconTextGap (), 0, 0 );
            }
        }

        // Applying component orientation
        cellEditor.applyComponentOrientation ( tree.getComponentOrientation () );

        return cellEditor;
    }
}