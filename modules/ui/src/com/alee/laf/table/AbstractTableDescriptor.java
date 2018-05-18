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
import javax.swing.plaf.ComponentUI;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Abstract descriptor for {@link JTable} component.
 * Extend this class for creating custom {@link JTable} descriptors.
 *
 * @param <C> {@link JComponent} type
 * @param <U> base {@link ComponentUI} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.managers.style.StyleManager#registerComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 * @see com.alee.managers.style.StyleManager#unregisterComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 * @see com.alee.managers.style.StyleManager#initializeDescriptors()
 */
public abstract class AbstractTableDescriptor<C extends JTable, U extends WebTableUI> extends AbstractComponentDescriptor<C, U>
{
    /**
     * Constructs new {@link AbstractTableDescriptor}.
     *
     * @param id             component identifier
     * @param componentClass component class
     * @param uiClassId      component UI class ID
     * @param baseUIClass    base UI class applicable to this component
     * @param uiClass        UI class applied to the component by default
     * @param defaultStyleId component default style ID
     */
    public AbstractTableDescriptor ( final String id, final Class<C> componentClass, final String uiClassId,
                                     final Class<U> baseUIClass, final Class<? extends U> uiClass, final StyleId defaultStyleId )
    {
        super ( id, componentClass, uiClassId, baseUIClass, uiClass, defaultStyleId );
    }

    @Override
    public void updateUI ( final C component )
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