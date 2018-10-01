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

import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.language.*;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.settings.UISettingsManager;
import com.alee.managers.style.*;
import com.alee.painter.Paintable;
import com.alee.painter.Painter;
import com.alee.utils.GeometryUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.MouseButton;
import com.alee.utils.swing.extensions.*;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.util.EventObject;
import java.util.Vector;

/**
 * {@link JTable} extension class.
 * It contains various useful methods to simplify core component usage.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see JTable
 * @see WebTableUI
 * @see TablePainter
 */
public class WebTable extends JTable implements Styleable, Paintable, ShapeMethods, MarginMethods, PaddingMethods, EventMethods,
        LanguageMethods, LanguageEventMethods, SettingsMethods, FontMethods<WebTable>, SizeMethods<WebTable>
{
    /**
     * Component properties.
     */
    public static final String ROW_HEIGHT_PROPERTY = "rowHeight";

    /**
     * Whether or not table is editable.
     * This is an additional global editable state switch for the whole table.
     * It is added for the sake of simplicity as it is missing in common {@link javax.swing.JTable}.
     *
     * @see TableModel#isCellEditable(int, int)
     */
    protected boolean editable;

    /**
     * Preferred visible row count.
     * If set to {@code -1} table will try to take all availble vertical space to fit in height of all rows.
     */
    protected int visibleRowCount;

    /**
     * Custom WebLaF tooltip provider.
     */
    protected transient TableToolTipProvider toolTipProvider = null;

    /**
     * {@link TableRowHeightOptimizer} if it is enabled, {@code null} otherwise.
     */
    protected transient TableRowHeightOptimizer rowHeightOptimizer;

    /**
     * Constructs new table.
     */
    public WebTable ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs new table.
     *
     * @param model table model
     */
    public WebTable ( final TableModel model )
    {
        this ( StyleId.auto, model );
    }

    /**
     * Constructs new table.
     *
     * @param model       table model
     * @param columnModel table column model
     */
    public WebTable ( final TableModel model, final TableColumnModel columnModel )
    {
        this ( StyleId.auto, model, columnModel );
    }

    /**
     * Constructs new table.
     *
     * @param model          table model
     * @param columnModel    table column model
     * @param selectionModel table selection model
     */
    public WebTable ( final TableModel model, final TableColumnModel columnModel, final ListSelectionModel selectionModel )
    {
        this ( StyleId.auto, model, columnModel, selectionModel );
    }

    /**
     * Constructs new table.
     *
     * @param rows    table rows amount
     * @param columns table columns amount
     */
    public WebTable ( final int rows, final int columns )
    {
        this ( StyleId.auto, rows, columns );
    }

    /**
     * Constructs new table.
     *
     * @param data        table data
     * @param columnNames table column names
     */
    public WebTable ( final Vector data, final Vector columnNames )
    {
        this ( StyleId.auto, data, columnNames );
    }

    /**
     * Constructs new table.
     *
     * @param data        table data
     * @param columnNames table column names
     */
    public WebTable ( final Object[][] data, final Object[] columnNames )
    {
        this ( StyleId.auto, data, columnNames );
    }

    /**
     * Constructs new table.
     *
     * @param id style ID
     */
    public WebTable ( final StyleId id )
    {
        this ( id, null, null, null );
    }

    /**
     * Constructs new table.
     *
     * @param id      style ID
     * @param rows    table rows amount
     * @param columns table columns amount
     */
    public WebTable ( final StyleId id, final int rows, final int columns )
    {
        this ( id, new DefaultTableModel ( rows, columns ), null, null );
    }

    /**
     * Constructs new table.
     *
     * @param id          style ID
     * @param data        table data
     * @param columnNames table column names
     */
    public WebTable ( final StyleId id, final Vector data, final Vector columnNames )
    {
        this ( id, new DefaultTableModel ( data, columnNames ), null, null );
    }

    /**
     * Constructs new table.
     *
     * @param id          style ID
     * @param data        table data
     * @param columnNames table column names
     */
    public WebTable ( final StyleId id, final Object[][] data, final Object[] columnNames )
    {
        this ( id, new DefaultTableModel ( data, columnNames ), null, null );
    }

    /**
     * Constructs new table.
     *
     * @param id    style ID
     * @param model table model
     */
    public WebTable ( final StyleId id, final TableModel model )
    {
        this ( id, model, null, null );
    }

    /**
     * Constructs new table.
     *
     * @param id          style ID
     * @param model       table model
     * @param columnModel table column model
     */
    public WebTable ( final StyleId id, final TableModel model, final TableColumnModel columnModel )
    {
        this ( id, model, columnModel, null );
    }

    /**
     * Constructs new table.
     *
     * @param id             style ID
     * @param model          table model
     * @param columnModel    table column model
     * @param selectionModel table selection model
     */
    public WebTable ( final StyleId id, final TableModel model, final TableColumnModel columnModel,
                      final ListSelectionModel selectionModel )
    {
        super ( model, columnModel, selectionModel );
        this.editable = true;
        this.visibleRowCount = 10;
        setStyleId ( id );
    }

    @Override
    protected WebTableHeader createDefaultTableHeader ()
    {
        return new WebTableHeader ( getColumnModel () );
    }

    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.table;
    }

    /**
     * Returns {@link TableToolTipProvider}.
     *
     * @return {@link TableToolTipProvider}
     */
    public TableToolTipProvider getToolTipProvider ()
    {
        return toolTipProvider;
    }

    /**
     * Sets {@link TableToolTipProvider}.
     *
     * @param provider {@link TableToolTipProvider}
     */
    public void setToolTipProvider ( final TableToolTipProvider provider )
    {
        this.toolTipProvider = provider;
    }

    /**
     * Returns whether or not {@link TableRowHeightOptimizer} is enabled.
     *
     * @return {@code true} if {@link TableRowHeightOptimizer} is enabled, {@code false} otherwise
     */
    public boolean isOptimizeRowHeight ()
    {
        return rowHeightOptimizer != null;
    }

    /**
     * Sets whether or not {@link TableRowHeightOptimizer} should be enabled.
     *
     * @param optimize whether or not {@link TableRowHeightOptimizer} should be enabled
     */
    public void setOptimizeRowHeight ( final boolean optimize )
    {
        if ( optimize )
        {
            if ( rowHeightOptimizer == null )
            {
                rowHeightOptimizer = new TableRowHeightOptimizer ( this );
                rowHeightOptimizer.install ();
            }
        }
        else
        {
            if ( rowHeightOptimizer != null )
            {
                rowHeightOptimizer.uninstall ();
                rowHeightOptimizer = null;
            }
        }
    }

    /**
     * Optimizes table column widths to fit content.
     */
    public void optimizeColumnWidths ()
    {
        optimizeColumnWidths ( false, 20, Integer.MAX_VALUE );
    }

    /**
     * Optimizes table column widths to fit content.
     *
     * @param processData whether or not should take table data into account
     */
    public void optimizeColumnWidths ( final boolean processData )
    {
        optimizeColumnWidths ( processData, 20, Integer.MAX_VALUE );
    }

    /**
     * Optimizes table column widths to fit content.
     *
     * @param processData whether or not should take table data into account
     * @param minWidth    min column width
     * @param maxWidth    max column width
     */
    public void optimizeColumnWidths ( final boolean processData, final int minWidth, final int maxWidth )
    {
        if ( getTableHeader () != null || processData )
        {
            for ( int column = 0; column < getColumnCount (); column++ )
            {
                final int width = getOptimalColumnWidth ( column, processData, minWidth, maxWidth );
                getColumnModel ().getColumn ( column ).setPreferredWidth ( width );
            }
        }
    }

    /**
     * Returns optimal width for the specified table column.
     *
     * @param column      table column index
     * @param processData whether or not should take table data into account
     * @param minWidth    min column width
     * @param maxWidth    max column width
     * @return optimal width for the specified table column
     */
    protected int getOptimalColumnWidth ( final int column, final boolean processData, final int minWidth, final int maxWidth )
    {
        int width = 0;
        final JTableHeader th = getTableHeader ();
        if ( th != null )
        {
            final Object value = th.getColumnModel ().getColumn ( column ).getHeaderValue ();
            final TableCellRenderer hr = th.getDefaultRenderer ();
            final Component r = hr.getTableCellRendererComponent ( WebTable.this, value, false, false, -1, column );
            width = Math.max ( width, r.getPreferredSize ().width );
        }
        if ( processData )
        {
            for ( int row = 0; row < getRowCount (); row++ )
            {
                final Object value = getModel ().getValueAt ( row, column );
                final TableCellRenderer cr = getCellRenderer ( row, column );
                final Component r = cr.getTableCellRendererComponent ( WebTable.this, value, false, false, row, column );
                width = Math.max ( width, r.getPreferredSize ().width );
            }
        }
        return Math.max ( minWidth, Math.min ( width, maxWidth ) );
    }

    public void setSelectedRow ( final int row )
    {
        setSelectedRow ( row, true );
    }

    public void setSelectedRow ( final int row, final boolean scroll )
    {
        clearSelection ();
        addSelectedRow ( row );
        if ( row != -1 && scroll )
        {
            scrollToRow ( row );
        }
    }

    public void addSelectedRow ( final int row )
    {
        if ( row != -1 )
        {
            addColumnSelectionInterval ( 0, getColumnCount () - 1 );
            addRowSelectionInterval ( row, row );
        }
    }

    public void setSelectedRows ( final int startRow, final int endRow )
    {
        clearSelection ();
        addSelectedRows ( startRow, endRow );
    }

    public void addSelectedRows ( final int startRow, final int endRow )
    {
        if ( startRow != -1 && endRow != -1 )
        {
            addColumnSelectionInterval ( 0, getColumnCount () - 1 );
            addRowSelectionInterval ( startRow, endRow );
        }
    }

    public void setSelectedColumn ( final int column )
    {
        setSelectedColumn ( column, true );
    }

    public void setSelectedColumn ( final int column, final boolean scroll )
    {
        clearSelection ();
        addSelectedColumn ( column );
        if ( scroll )
        {
            scrollToColumn ( column );
        }
    }

    public void addSelectedColumn ( final int column )
    {
        if ( column != -1 )
        {
            addColumnSelectionInterval ( column, column );
            addRowSelectionInterval ( 0, getRowCount () - 1 );
        }
    }

    public void setSelectedColumns ( final int startColumn, final int endColumn )
    {
        clearSelection ();
        addSelectedColumns ( startColumn, endColumn );
    }

    public void addSelectedColumns ( final int startColumn, final int endColumn )
    {
        if ( startColumn != -1 && endColumn != -1 )
        {
            addColumnSelectionInterval ( startColumn, endColumn );
            addRowSelectionInterval ( 0, getRowCount () - 1 );
        }
    }

    public void scrollToRow ( final int row )
    {
        final Rectangle firstCell = getCellRect ( row, 0, true );
        final Rectangle lastCell = getCellRect ( row, getColumnCount () - 1, true );
        final Rectangle rect = GeometryUtils.getContainingRect ( firstCell, lastCell );
        if ( rect != null )
        {
            scrollRectToVisible ( rect );
        }
    }

    public void scrollToColumn ( final int column )
    {
        final Rectangle firstCell = getCellRect ( 0, column, true );
        final Rectangle lastCell = getCellRect ( getRowCount () - 1, column, true );
        final Rectangle rect = GeometryUtils.getContainingRect ( firstCell, lastCell );
        if ( rect != null )
        {
            scrollRectToVisible ( rect );
        }
    }

    @Override
    public boolean editCellAt ( final int row, final int column, final EventObject event )
    {
        final boolean editingStarted = super.editCellAt ( row, column, event );
        if ( editingStarted )
        {
            final CellEditor cellEditor = getCellEditor ();
            try
            {
                // todo There should be a proper interface for that
                //noinspection JavaReflectionMemberAccess
                final Object o = cellEditor.getClass ().getMethod ( "getComponent" ).invoke ( cellEditor );
                if ( o instanceof Component )
                {
                    ( ( Component ) o ).requestFocusInWindow ();
                }
            }
            catch ( final Exception e )
            {
                // ignore
            }
        }
        return editingStarted;
    }

    /**
     * Stops cell editing if table is in the middle of it.
     *
     * @return true if editing was stopped, false otherwise
     */
    public boolean stopCellEditing ()
    {
        final TableCellEditor cellEditor = getCellEditor ();
        return cellEditor != null && cellEditor.stopCellEditing ();
    }

    @Override
    public boolean isCellEditable ( final int row, final int column )
    {
        return editable && super.isCellEditable ( row, column );
    }

    /**
     * Returns whether or not table is editable.
     *
     * @return true if table is editable, false otherwise
     */
    public boolean isEditable ()
    {
        return editable;
    }

    /**
     * Sets whether or not table is editable.
     *
     * @param editable whether or not table is editable
     */
    public void setEditable ( final boolean editable )
    {
        this.editable = editable;
    }

    /**
     * Sets preferred visible row count.
     *
     * @param visibleRowCount preferred visible row count
     */
    public void setVisibleRowCount ( final int visibleRowCount )
    {
        this.visibleRowCount = visibleRowCount;

        // Reset preferred viewport size
        setPreferredScrollableViewportSize ( null );

        // Update viewport size
        final JScrollPane scrollPane = SwingUtils.getScrollPane ( this );
        if ( scrollPane != null )
        {
            scrollPane.getViewport ().invalidate ();
        }
    }

    /**
     * Returns preferred visible row count
     *
     * @return preferred visible row count
     */
    public int getVisibleRowCount ()
    {
        return visibleRowCount;
    }

    @Override
    public Dimension getPreferredScrollableViewportSize ()
    {
        // Custom preferred viewport size from {@link JTable}
        if ( preferredViewportSize != null )
        {
            return preferredViewportSize;
        }

        // Visible rows -based preferred viewport size
        if ( visibleRowCount != -1 )
        {
            final int rowHeight;
            if ( getRowCount () > 0 )
            {
                final Rectangle r = getCellRect ( 0, 0, true );
                rowHeight = r.height;
            }
            else
            {
                rowHeight = getRowHeight ();
            }

            final Dimension ps = getPreferredSize ();
            ps.height = visibleRowCount * rowHeight;
            return ps;
        }

        // Default preferred size
        return getPreferredSize ();
    }

    @Override
    protected void initializeLocalVars ()
    {
        super.initializeLocalVars ();
        setPreferredScrollableViewportSize ( null );
    }

    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( this );
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return StyleManager.setStyleId ( this, id );
    }

    @Override
    public StyleId resetStyleId ()
    {
        return StyleManager.resetStyleId ( this );
    }

    @Override
    public Skin getSkin ()
    {
        return StyleManager.getSkin ( this );
    }

    @Override
    public Skin setSkin ( final Skin skin )
    {
        return StyleManager.setSkin ( this, skin );
    }

    @Override
    public Skin setSkin ( final Skin skin, final boolean recursively )
    {
        return StyleManager.setSkin ( this, skin, recursively );
    }

    @Override
    public Skin resetSkin ()
    {
        return StyleManager.resetSkin ( this );
    }

    @Override
    public void addStyleListener ( final StyleListener listener )
    {
        StyleManager.addStyleListener ( this, listener );
    }

    @Override
    public void removeStyleListener ( final StyleListener listener )
    {
        StyleManager.removeStyleListener ( this, listener );
    }

    @Override
    public Painter getCustomPainter ()
    {
        return StyleManager.getCustomPainter ( this );
    }

    @Override
    public Painter setCustomPainter ( final Painter painter )
    {
        return StyleManager.setCustomPainter ( this, painter );
    }

    @Override
    public boolean resetCustomPainter ()
    {
        return StyleManager.resetCustomPainter ( this );
    }

    @Override
    public Shape getShape ()
    {
        return ShapeMethodsImpl.getShape ( this );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return ShapeMethodsImpl.isShapeDetectionEnabled ( this );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        ShapeMethodsImpl.setShapeDetectionEnabled ( this, enabled );
    }

    @Override
    public Insets getMargin ()
    {
        return MarginMethodsImpl.getMargin ( this );
    }

    @Override
    public void setMargin ( final int margin )
    {
        MarginMethodsImpl.setMargin ( this, margin );
    }

    @Override
    public void setMargin ( final int top, final int left, final int bottom, final int right )
    {
        MarginMethodsImpl.setMargin ( this, top, left, bottom, right );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        MarginMethodsImpl.setMargin ( this, margin );
    }

    @Override
    public Insets getPadding ()
    {
        return PaddingMethodsImpl.getPadding ( this );
    }

    @Override
    public void setPadding ( final int padding )
    {
        PaddingMethodsImpl.setPadding ( this, padding );
    }

    @Override
    public void setPadding ( final int top, final int left, final int bottom, final int right )
    {
        PaddingMethodsImpl.setPadding ( this, top, left, bottom, right );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        PaddingMethodsImpl.setPadding ( this, padding );
    }

    @Override
    public MouseAdapter onMousePress ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMousePress ( this, runnable );
    }

    @Override
    public MouseAdapter onMousePress ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMousePress ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onMouseEnter ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseEnter ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseExit ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseExit ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseDrag ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseDrag ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseDrag ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseDrag ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onMouseClick ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseClick ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseClick ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseClick ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onDoubleClick ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onDoubleClick ( this, runnable );
    }

    @Override
    public MouseAdapter onMenuTrigger ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMenuTrigger ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyType ( final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyType ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyType ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyType ( this, hotkey, runnable );
    }

    @Override
    public KeyAdapter onKeyPress ( final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyPress ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyPress ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyPress ( this, hotkey, runnable );
    }

    @Override
    public KeyAdapter onKeyRelease ( final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyRelease ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyRelease ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyRelease ( this, hotkey, runnable );
    }

    @Override
    public FocusAdapter onFocusGain ( final FocusEventRunnable runnable )
    {
        return EventMethodsImpl.onFocusGain ( this, runnable );
    }

    @Override
    public FocusAdapter onFocusLoss ( final FocusEventRunnable runnable )
    {
        return EventMethodsImpl.onFocusLoss ( this, runnable );
    }

    @Override
    public MouseAdapter onDragStart ( final int shift, final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onDragStart ( this, shift, runnable );
    }

    @Override
    public MouseAdapter onDragStart ( final int shift, final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onDragStart ( this, shift, mouseButton, runnable );
    }

    @Override
    public String getLanguage ()
    {
        return UILanguageManager.getComponentKey ( this );
    }

    @Override
    public void setLanguage ( final String key, final Object... data )
    {
        UILanguageManager.registerComponent ( this, key, data );
    }

    @Override
    public void updateLanguage ( final Object... data )
    {
        UILanguageManager.updateComponent ( this, data );
    }

    @Override
    public void updateLanguage ( final String key, final Object... data )
    {
        UILanguageManager.updateComponent ( this, key, data );
    }

    @Override
    public void removeLanguage ()
    {
        UILanguageManager.unregisterComponent ( this );
    }

    @Override
    public boolean isLanguageSet ()
    {
        return UILanguageManager.isRegisteredComponent ( this );
    }

    @Override
    public void setLanguageUpdater ( final LanguageUpdater updater )
    {
        UILanguageManager.registerLanguageUpdater ( this, updater );
    }

    @Override
    public void removeLanguageUpdater ()
    {
        UILanguageManager.unregisterLanguageUpdater ( this );
    }

    @Override
    public void addLanguageListener ( final LanguageListener listener )
    {
        UILanguageManager.addLanguageListener ( this, listener );
    }

    @Override
    public void removeLanguageListener ( final LanguageListener listener )
    {
        UILanguageManager.removeLanguageListener ( this, listener );
    }

    @Override
    public void removeLanguageListeners ()
    {
        UILanguageManager.removeLanguageListeners ( this );
    }

    @Override
    public void addDictionaryListener ( final DictionaryListener listener )
    {
        UILanguageManager.addDictionaryListener ( this, listener );
    }

    @Override
    public void removeDictionaryListener ( final DictionaryListener listener )
    {
        UILanguageManager.removeDictionaryListener ( this, listener );
    }

    @Override
    public void removeDictionaryListeners ()
    {
        UILanguageManager.removeDictionaryListeners ( this );
    }

    @Override
    public void registerSettings ( final Configuration configuration )
    {
        UISettingsManager.registerComponent ( this, configuration );
    }

    @Override
    public void registerSettings ( final SettingsProcessor processor )
    {
        UISettingsManager.registerComponent ( this, processor );
    }

    @Override
    public void unregisterSettings ()
    {
        UISettingsManager.unregisterComponent ( this );
    }

    @Override
    public void loadSettings ()
    {
        UISettingsManager.loadSettings ( this );
    }

    @Override
    public void saveSettings ()
    {
        UISettingsManager.saveSettings ( this );
    }

    @Override
    public WebTable setPlainFont ()
    {
        return FontMethodsImpl.setPlainFont ( this );
    }

    @Override
    public WebTable setPlainFont ( final boolean apply )
    {
        return FontMethodsImpl.setPlainFont ( this, apply );
    }

    @Override
    public boolean isPlainFont ()
    {
        return FontMethodsImpl.isPlainFont ( this );
    }

    @Override
    public WebTable setBoldFont ()
    {
        return FontMethodsImpl.setBoldFont ( this );
    }

    @Override
    public WebTable setBoldFont ( final boolean apply )
    {
        return FontMethodsImpl.setBoldFont ( this, apply );
    }

    @Override
    public boolean isBoldFont ()
    {
        return FontMethodsImpl.isBoldFont ( this );
    }

    @Override
    public WebTable setItalicFont ()
    {
        return FontMethodsImpl.setItalicFont ( this );
    }

    @Override
    public WebTable setItalicFont ( final boolean apply )
    {
        return FontMethodsImpl.setItalicFont ( this, apply );
    }

    @Override
    public boolean isItalicFont ()
    {
        return FontMethodsImpl.isItalicFont ( this );
    }

    @Override
    public WebTable setFontStyle ( final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontStyle ( this, bold, italic );
    }

    @Override
    public WebTable setFontStyle ( final int style )
    {
        return FontMethodsImpl.setFontStyle ( this, style );
    }

    @Override
    public WebTable setFontSize ( final int fontSize )
    {
        return FontMethodsImpl.setFontSize ( this, fontSize );
    }

    @Override
    public WebTable changeFontSize ( final int change )
    {
        return FontMethodsImpl.changeFontSize ( this, change );
    }

    @Override
    public int getFontSize ()
    {
        return FontMethodsImpl.getFontSize ( this );
    }

    @Override
    public WebTable setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    @Override
    public WebTable setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, style );
    }

    @Override
    public WebTable setFontName ( final String fontName )
    {
        return FontMethodsImpl.setFontName ( this, fontName );
    }

    @Override
    public String getFontName ()
    {
        return FontMethodsImpl.getFontName ( this );
    }

    @Override
    public int getPreferredWidth ()
    {
        return SizeMethodsImpl.getPreferredWidth ( this );
    }

    @Override
    public WebTable setPreferredWidth ( final int preferredWidth )
    {
        return SizeMethodsImpl.setPreferredWidth ( this, preferredWidth );
    }

    @Override
    public int getPreferredHeight ()
    {
        return SizeMethodsImpl.getPreferredHeight ( this );
    }

    @Override
    public WebTable setPreferredHeight ( final int preferredHeight )
    {
        return SizeMethodsImpl.setPreferredHeight ( this, preferredHeight );
    }

    @Override
    public int getMinimumWidth ()
    {
        return SizeMethodsImpl.getMinimumWidth ( this );
    }

    @Override
    public WebTable setMinimumWidth ( final int minimumWidth )
    {
        return SizeMethodsImpl.setMinimumWidth ( this, minimumWidth );
    }

    @Override
    public int getMinimumHeight ()
    {
        return SizeMethodsImpl.getMinimumHeight ( this );
    }

    @Override
    public WebTable setMinimumHeight ( final int minimumHeight )
    {
        return SizeMethodsImpl.setMinimumHeight ( this, minimumHeight );
    }

    @Override
    public int getMaximumWidth ()
    {
        return SizeMethodsImpl.getMaximumWidth ( this );
    }

    @Override
    public WebTable setMaximumWidth ( final int maximumWidth )
    {
        return SizeMethodsImpl.setMaximumWidth ( this, maximumWidth );
    }

    @Override
    public int getMaximumHeight ()
    {
        return SizeMethodsImpl.getMaximumHeight ( this );
    }

    @Override
    public WebTable setMaximumHeight ( final int maximumHeight )
    {
        return SizeMethodsImpl.setMaximumHeight ( this, maximumHeight );
    }

    @Override
    public Dimension getPreferredSize ()
    {
        return SizeMethodsImpl.getPreferredSize ( this, super.getPreferredSize () );
    }

    @Override
    public Dimension getOriginalPreferredSize ()
    {
        return SizeMethodsImpl.getOriginalPreferredSize ( this, super.getPreferredSize () );
    }

    @Override
    public WebTable setPreferredSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setPreferredSize ( this, width, height );
    }

    /**
     * Returns the look and feel (LaF) object that renders this component.
     *
     * @return the {@link WebTableUI} object that renders this component
     */
    @Override
    public WebTableUI getUI ()
    {
        return ( WebTableUI ) super.getUI ();
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WebTableUI}
     */
    public void setUI ( final WebTableUI ui )
    {
        super.setUI ( ui );
    }

    @Override
    public void updateUI ()
    {
        StyleManager.getDescriptor ( this ).updateUI ( this );
    }

    @Override
    public String getUIClassID ()
    {
        return StyleManager.getDescriptor ( this ).getUIClassId ();
    }

    /**
     * Configures enclosing scroll pane.
     * todo Make sure this is called when table parent changes?
     */
    protected void configureScrollPane ()
    {
        final Container p = getParent ();
        if ( p instanceof JViewport )
        {
            final Container gp = p.getParent ();
            if ( gp instanceof JScrollPane )
            {
                final JScrollPane scrollPane = ( JScrollPane ) gp;
                // Make certain we are the viewPort's view and not, for
                // example, the rowHeaderView of the scrollPane -
                // an implementor of fixed columns might do this.
                final JViewport viewport = scrollPane.getViewport ();
                if ( viewport == null || viewport.getView () != this )
                {
                    return;
                }
                //  scrollPane.getViewport().setBackingStoreEnabled(true);
                //                Border border = scrollPane.getBorder ();
                //                if ( border == null || border instanceof UIResource )
                //                {
                //                    Border scrollPaneBorder = UIManager.getBorder ( "Table.scrollPaneBorder" );
                //                    if ( scrollPaneBorder != null )
                //                    {
                //                        scrollPane.setBorder ( scrollPaneBorder );
                //                    }
                //                }
                // add JScrollBar corner component if available from LAF and not already set by the user
                Component corner = scrollPane.getCorner ( JScrollPane.UPPER_TRAILING_CORNER );
                if ( corner == null || corner instanceof UIResource )
                {
                    corner = null;
                    final Object componentClass = UIManager.get ( "Table.scrollPaneCornerComponent" );
                    if ( componentClass instanceof Class )
                    {
                        try
                        {
                            corner = ( Component ) ( ( Class ) componentClass ).newInstance ();
                        }
                        catch ( final Exception ignored )
                        {
                            // just ignore and don't set corner
                        }
                    }
                    scrollPane.setCorner ( JScrollPane.UPPER_TRAILING_CORNER, corner );
                }
            }
        }
    }
}