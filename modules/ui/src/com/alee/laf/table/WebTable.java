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

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.log.Log;
import com.alee.managers.style.*;
import com.alee.managers.style.Skin;
import com.alee.managers.style.Skinnable;
import com.alee.managers.style.StyleListener;
import com.alee.managers.tooltip.ToolTipProvider;
import com.alee.painter.Paintable;
import com.alee.painter.Painter;
import com.alee.utils.GeometryUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SizeUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.FontMethods;
import com.alee.utils.swing.SizeMethods;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import javax.swing.table.*;
import java.awt.*;
import java.util.EventObject;
import java.util.Map;
import java.util.Vector;

/**
 * @author Mikle Garin
 */

public class WebTable extends JTable
        implements Styleable, Skinnable, Paintable, ShapeProvider, MarginSupport, PaddingSupport, FontMethods<WebTable>,
        SizeMethods<WebTable>
{
    /**
     * Whether or not table is editable.
     * This is an additional global editable state switch for the whole table.
     * It is added for the sake of simplicity as it is missing in common {@link javax.swing.JTable}.
     *
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    private boolean editable = true;

    /**
     * Preferred visible row count.
     * By default or if set to {@code -1} table will try to take all availble vertical space to fit in height of all rows.
     */
    private int visibleRowCount = -1;

    /**
     * Custom WebLaF tooltip provider.
     */
    protected ToolTipProvider<? extends WebTable> toolTipProvider = null;

    public WebTable ()
    {
        super ();
    }

    public WebTable ( final TableModel dm )
    {
        super ( dm );
    }

    public WebTable ( final TableModel dm, final TableColumnModel cm )
    {
        super ( dm, cm );
    }

    public WebTable ( final TableModel dm, final TableColumnModel cm, final ListSelectionModel sm )
    {
        super ( dm, cm, sm );
    }

    public WebTable ( final int numRows, final int numColumns )
    {
        super ( numRows, numColumns );
    }

    public WebTable ( final Vector rowData, final Vector columnNames )
    {
        super ( rowData, columnNames );
    }

    public WebTable ( final Object[][] rowData, final Object[] columnNames )
    {
        super ( rowData, columnNames );
    }

    public WebTable ( final StyleId id )
    {
        super ();
        setStyleId ( id );
    }

    public WebTable ( final StyleId id, final TableModel dm )
    {
        super ( dm );
        setStyleId ( id );
    }

    public WebTable ( final StyleId id, final TableModel dm, final TableColumnModel cm )
    {
        super ( dm, cm );
        setStyleId ( id );
    }

    public WebTable ( final StyleId id, final TableModel dm, final TableColumnModel cm, final ListSelectionModel sm )
    {
        super ( dm, cm, sm );
        setStyleId ( id );
    }

    public WebTable ( final StyleId id, final int numRows, final int numColumns )
    {
        super ( numRows, numColumns );
        setStyleId ( id );
    }

    public WebTable ( final StyleId id, final Vector rowData, final Vector columnNames )
    {
        super ( rowData, columnNames );
        setStyleId ( id );
    }

    public WebTable ( final StyleId id, final Object[][] rowData, final Object[] columnNames )
    {
        super ( rowData, columnNames );
        setStyleId ( id );
    }

    @Override
    protected WebTableHeader createDefaultTableHeader ()
    {
        return new WebTableHeader ( getColumnModel () );
    }

    /**
     * Returns custom WebLaF tooltip provider.
     *
     * @return custom WebLaF tooltip provider
     */
    public ToolTipProvider<? extends WebTable> getToolTipProvider ()
    {
        return toolTipProvider;
    }

    /**
     * Sets custom WebLaF tooltip provider.
     *
     * @param provider custom WebLaF tooltip provider
     */
    public void setToolTipProvider ( final ToolTipProvider<? extends WebTable> provider )
    {
        this.toolTipProvider = provider;
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
        if ( preferredViewportSize != null )
        {
            return preferredViewportSize;
        }

        final Dimension ps = getPreferredSize ();
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
            ps.height = visibleRowCount * rowHeight;
        }
        return ps;
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
        return getWebUI ().getStyleId ();
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return getWebUI ().setStyleId ( id );
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
    public Skin restoreSkin ()
    {
        return StyleManager.restoreSkin ( this );
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
    public Map<String, Painter> getCustomPainters ()
    {
        return StyleManager.getCustomPainters ( this );
    }

    @Override
    public Painter getCustomPainter ()
    {
        return StyleManager.getCustomPainter ( this );
    }

    @Override
    public Painter getCustomPainter ( final String id )
    {
        return StyleManager.getCustomPainter ( this, id );
    }

    @Override
    public Painter setCustomPainter ( final Painter painter )
    {
        return StyleManager.setCustomPainter ( this, painter );
    }

    @Override
    public Painter setCustomPainter ( final String id, final Painter painter )
    {
        return StyleManager.setCustomPainter ( this, id, painter );
    }

    @Override
    public boolean restoreDefaultPainters ()
    {
        return StyleManager.restoreDefaultPainters ( this );
    }

    @Override
    public Shape provideShape ()
    {
        return getWebUI ().provideShape ();
    }

    @Override
    public Insets getMargin ()
    {
        return getWebUI ().getMargin ();
    }

    /**
     * Sets new margin.
     *
     * @param margin new margin
     */
    public void setMargin ( final int margin )
    {
        setMargin ( margin, margin, margin, margin );
    }

    /**
     * Sets new margin.
     *
     * @param top    new top margin
     * @param left   new left margin
     * @param bottom new bottom margin
     * @param right  new right margin
     */
    public void setMargin ( final int top, final int left, final int bottom, final int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        getWebUI ().setMargin ( margin );
    }

    @Override
    public Insets getPadding ()
    {
        return getWebUI ().getPadding ();
    }

    /**
     * Sets new padding.
     *
     * @param padding new padding
     */
    public void setPadding ( final int padding )
    {
        setPadding ( padding, padding, padding, padding );
    }

    /**
     * Sets new padding.
     *
     * @param top    new top padding
     * @param left   new left padding
     * @param bottom new bottom padding
     * @param right  new right padding
     */
    public void setPadding ( final int top, final int left, final int bottom, final int right )
    {
        setPadding ( new Insets ( top, left, bottom, right ) );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        getWebUI ().setPadding ( padding );
    }

    /**
     * Returns Web-UI applied to this class.
     *
     * @return Web-UI applied to this class
     */
    private WebTableUI getWebUI ()
    {
        return ( WebTableUI ) getUI ();
    }

    /**
     * Installs a Web-UI into this component.
     */
    @Override
    public void updateUI ()
    {
        // Update table header UI
        if ( getTableHeader () != null )
        {
            getTableHeader ().updateUI ();
        }

        // Update table scroll view and UI
        configureScrollPane ();

        // Update table UI
        if ( getUI () == null || !( getUI () instanceof WebTableUI ) )
        {
            try
            {
                setUI ( ( WebTableUI ) ReflectUtils.createInstance ( WebLookAndFeel.tableUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebTableUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
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
                        catch ( final Exception e )
                        {
                            // just ignore and don't set corner
                        }
                    }
                    scrollPane.setCorner ( JScrollPane.UPPER_TRAILING_CORNER, corner );
                }
            }
        }
    }

    @Override
    public WebTable setPlainFont ()
    {
        return SwingUtils.setPlainFont ( this );
    }

    @Override
    public WebTable setPlainFont ( final boolean apply )
    {
        return SwingUtils.setPlainFont ( this, apply );
    }

    @Override
    public boolean isPlainFont ()
    {
        return SwingUtils.isPlainFont ( this );
    }

    @Override
    public WebTable setBoldFont ()
    {
        return SwingUtils.setBoldFont ( this );
    }

    @Override
    public WebTable setBoldFont ( final boolean apply )
    {
        return SwingUtils.setBoldFont ( this, apply );
    }

    @Override
    public boolean isBoldFont ()
    {
        return SwingUtils.isBoldFont ( this );
    }

    @Override
    public WebTable setItalicFont ()
    {
        return SwingUtils.setItalicFont ( this );
    }

    @Override
    public WebTable setItalicFont ( final boolean apply )
    {
        return SwingUtils.setItalicFont ( this, apply );
    }

    @Override
    public boolean isItalicFont ()
    {
        return SwingUtils.isItalicFont ( this );
    }

    @Override
    public WebTable setFontStyle ( final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    @Override
    public WebTable setFontStyle ( final int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    @Override
    public WebTable setFontSize ( final int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    @Override
    public WebTable changeFontSize ( final int change )
    {
        return SwingUtils.changeFontSize ( this, change );
    }

    @Override
    public int getFontSize ()
    {
        return SwingUtils.getFontSize ( this );
    }

    @Override
    public WebTable setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    @Override
    public WebTable setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    @Override
    public WebTable setFontName ( final String fontName )
    {
        return SwingUtils.setFontName ( this, fontName );
    }

    @Override
    public String getFontName ()
    {
        return SwingUtils.getFontName ( this );
    }

    @Override
    public int getPreferredWidth ()
    {
        return SizeUtils.getPreferredWidth ( this );
    }

    @Override
    public WebTable setPreferredWidth ( final int preferredWidth )
    {
        return SizeUtils.setPreferredWidth ( this, preferredWidth );
    }

    @Override
    public int getPreferredHeight ()
    {
        return SizeUtils.getPreferredHeight ( this );
    }

    @Override
    public WebTable setPreferredHeight ( final int preferredHeight )
    {
        return SizeUtils.setPreferredHeight ( this, preferredHeight );
    }

    @Override
    public int getMinimumWidth ()
    {
        return SizeUtils.getMinimumWidth ( this );
    }

    @Override
    public WebTable setMinimumWidth ( final int minimumWidth )
    {
        return SizeUtils.setMinimumWidth ( this, minimumWidth );
    }

    @Override
    public int getMinimumHeight ()
    {
        return SizeUtils.getMinimumHeight ( this );
    }

    @Override
    public WebTable setMinimumHeight ( final int minimumHeight )
    {
        return SizeUtils.setMinimumHeight ( this, minimumHeight );
    }

    @Override
    public int getMaximumWidth ()
    {
        return SizeUtils.getMaximumWidth ( this );
    }

    @Override
    public WebTable setMaximumWidth ( final int maximumWidth )
    {
        return SizeUtils.setMaximumWidth ( this, maximumWidth );
    }

    @Override
    public int getMaximumHeight ()
    {
        return SizeUtils.getMaximumHeight ( this );
    }

    @Override
    public WebTable setMaximumHeight ( final int maximumHeight )
    {
        return SizeUtils.setMaximumHeight ( this, maximumHeight );
    }

    @Override
    public Dimension getPreferredSize ()
    {
        return SizeUtils.getPreferredSize ( this, super.getPreferredSize () );
    }

    @Override
    public WebTable setPreferredSize ( final int width, final int height )
    {
        return SizeUtils.setPreferredSize ( this, width, height );
    }
}