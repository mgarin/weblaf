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

package com.alee.laf.table;

import com.alee.managers.style.AbstractComponentDescriptor;
import com.alee.managers.style.StyleId;
import com.alee.utils.ReflectUtils;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Custom descriptor for {@link JTable} component.
 *
 * @author Mikle Garin
 */

public final class TableDescriptor extends AbstractComponentDescriptor<JTable>
{
    /**
     * Constructs new descriptor for {@link JTable} component.
     */
    public TableDescriptor ()
    {
        super ( "table", JTable.class, "TableUI", WebTableUI.class, WebTableUI.class, StyleId.table );
    }

    @Override
    public void updateUI ( final JTable component )
    {
        // Update the UIs of the cell renderers, cell editors and header renderers.
        final TableColumnModel cm = component.getColumnModel ();
        for ( int column = 0; column < cm.getColumnCount (); column++ )
        {
            final TableColumn aColumn = cm.getColumn ( column );
            updateRendererOrEditorUI ( aColumn.getCellRenderer () );
            updateRendererOrEditorUI ( aColumn.getCellEditor () );
            updateRendererOrEditorUI ( aColumn.getHeaderRenderer () );
        }

        // Update the UIs of all the default renderers.
        final Hashtable defaultRenderersByColumnClass = ReflectUtils.getFieldValueSafely ( component, "defaultRenderersByColumnClass" );
        final Enumeration defaultRenderers = defaultRenderersByColumnClass.elements ();
        while ( defaultRenderers.hasMoreElements () )
        {
            updateRendererOrEditorUI ( defaultRenderers.nextElement () );
        }

        // Update the UIs of all the default editors.
        final Hashtable defaultEditorsByColumnClass = ReflectUtils.getFieldValueSafely ( component, "defaultEditorsByColumnClass" );
        final Enumeration defaultEditors = defaultEditorsByColumnClass.elements ();
        while ( defaultEditors.hasMoreElements () )
        {
            updateRendererOrEditorUI ( defaultEditors.nextElement () );
        }

        // Updating table header UI
        final JTableHeader header = component.getTableHeader ();
        if ( header != null )
        {
            header.updateUI ();
        }

        // Configuring enclosing scroll pane
        if ( component instanceof WebTable )
        {
            // Configuring through custom WebTable method
            ( ( WebTable ) component ).configureScrollPane ();
        }
        else
        {
            // Configuring through basic JTable method
            ReflectUtils.callMethodSafely ( component, "configureEnclosingScrollPaneUI" );
        }

        // Updating component UI
        super.updateUI ( component );
    }

    /**
     * Updates renderer or editor component tree UIs.
     *
     * @param rendererOrEditor renderer or editor component
     */
    private void updateRendererOrEditorUI ( final Object rendererOrEditor )
    {
        if ( rendererOrEditor != null )
        {
            Component component = null;
            if ( rendererOrEditor instanceof Component )
            {
                component = ( Component ) rendererOrEditor;
            }
            if ( rendererOrEditor instanceof DefaultCellEditor )
            {
                component = ( ( DefaultCellEditor ) rendererOrEditor ).getComponent ();
            }
            if ( component != null )
            {
                SwingUtilities.updateComponentTreeUI ( component );
            }
        }
    }
}