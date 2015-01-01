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
import com.alee.utils.swing.WebDefaultCellEditor;

import javax.swing.*;
import javax.swing.plaf.TreeUI;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * This class provides a styled default cell editor for trees.
 *
 * @author Mikle Garin
 */

public class WebTreeCellEditor<C extends JComponent> extends WebDefaultCellEditor<C> implements FocusListener
{
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void focusGained ( final FocusEvent e )
    {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void focusLost ( final FocusEvent e )
    {
        stopCellEditing ();
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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
        final Component component =
                tree.getCellRenderer ().getTreeCellRendererComponent ( tree, value, isSelected, expanded, leaf, row, true );
        cellEditor.setPreferredSize ( component.getPreferredSize () );

        // Updating editor styling
        if ( component instanceof JLabel && ( ( JLabel ) component ).getIcon () != null )
        {
            final JLabel label = ( JLabel ) component;

            // todo Proper editor for RTL
            // boolean ltr = tree.getComponentOrientation ().isLeftToRight ();

            if ( cellEditor instanceof WebTextField )
            {
                final TreeUI tui = tree.getUI ();
                final int sw =
                        tui instanceof WebTreeUI ? ( ( WebTreeUI ) tui ).getSelectionShadeWidth () : WebTreeStyle.selectionShadeWidth;

                // Field styling
                final WebTextField editor = ( WebTextField ) cellEditor;
                editor.setDrawFocus ( false );
                editor.setShadeWidth ( sw );
                editor.setDrawShade ( false );

                // Leading icon
                if ( autoUpdateLeadingIcon )
                {
                    editor.setLeadingComponent ( new WebImage ( label.getIcon () ) );
                }

                // Field side margin
                final int sm = sw + 1;
                final Insets margin = label.getInsets ();
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