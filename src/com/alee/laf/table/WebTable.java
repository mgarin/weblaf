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
import com.alee.utils.GeometryUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.FontMethods;
import com.alee.utils.swing.WebDefaultCellEditor;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.EventObject;
import java.util.Vector;

/**
 * User: mgarin Date: 07.07.11 Time: 17:55
 */

public class WebTable extends JTable implements FontMethods<WebTable>
{
    private boolean editable = true;
    private int visibleRowCount = -1;

    public WebTable ()
    {
        super ();
    }

    public WebTable ( TableModel dm )
    {
        super ( dm );
    }

    public WebTable ( TableModel dm, TableColumnModel cm )
    {
        super ( dm, cm );
    }

    public WebTable ( TableModel dm, TableColumnModel cm, ListSelectionModel sm )
    {
        super ( dm, cm, sm );
    }

    public WebTable ( int numRows, int numColumns )
    {
        super ( numRows, numColumns );
    }

    public WebTable ( Vector rowData, Vector columnNames )
    {
        super ( rowData, columnNames );
    }

    public WebTable ( Object[][] rowData, Object[] columnNames )
    {
        super ( rowData, columnNames );
    }

    public void setSelectedRow ( int row )
    {
        setSelectedRow ( row, true );
    }

    public void setSelectedRow ( int row, boolean shouldScroll )
    {
        clearSelection ();
        addSelectedRow ( row );
        if ( shouldScroll )
        {
            scrollToRow ( row );
        }
    }

    public void addSelectedRow ( int row )
    {
        if ( row != -1 )
        {
            addColumnSelectionInterval ( 0, getColumnCount () - 1 );
            addRowSelectionInterval ( row, row );
        }
    }

    public void setSelectedRows ( int startRow, int endRow )
    {
        clearSelection ();
        addSelectedRows ( startRow, endRow );
    }

    public void addSelectedRows ( int startRow, int endRow )
    {
        if ( startRow != -1 && endRow != -1 )
        {
            addColumnSelectionInterval ( 0, getColumnCount () - 1 );
            addRowSelectionInterval ( startRow, endRow );
        }
    }

    public void setSelectedColumn ( int column )
    {
        setSelectedColumn ( column, true );
    }

    public void setSelectedColumn ( int column, boolean shouldScroll )
    {
        clearSelection ();
        addSelectedColumn ( column );
        if ( shouldScroll )
        {
            scrollToColumn ( column );
        }
    }

    public void addSelectedColumn ( int column )
    {
        if ( column != -1 )
        {
            addColumnSelectionInterval ( column, column );
            addRowSelectionInterval ( 0, getRowCount () - 1 );
        }
    }

    public void setSelectedColumns ( int startColumn, int endColumn )
    {
        clearSelection ();
        addSelectedColumns ( startColumn, endColumn );
    }

    public void addSelectedColumns ( int startColumn, int endColumn )
    {
        if ( startColumn != -1 && endColumn != -1 )
        {
            addColumnSelectionInterval ( startColumn, endColumn );
            addRowSelectionInterval ( 0, getRowCount () - 1 );
        }
    }

    public void scrollToRow ( int row )
    {
        final Rectangle firstCell = getCellRect ( row, 0, true );
        final Rectangle lastCell = getCellRect ( row, getColumnCount () - 1, true );
        final Rectangle rect = GeometryUtils.getContainingRect ( firstCell, lastCell );
        scrollRectToVisible ( rect );
    }

    public void scrollToColumn ( int column )
    {
        final Rectangle firstCell = getCellRect ( 0, column, true );
        final Rectangle lastCell = getCellRect ( getRowCount () - 1, column, true );
        final Rectangle rect = GeometryUtils.getContainingRect ( firstCell, lastCell );
        scrollRectToVisible ( rect );
    }

    @Override
    public boolean editCellAt ( int row, int column, EventObject e )
    {
        final boolean editingStarted = super.editCellAt ( row, column, e );
        if ( editingStarted )
        {
            CellEditor cellEditor = getCellEditor ();
            if ( cellEditor instanceof DefaultCellEditor )
            {
                ( ( DefaultCellEditor ) cellEditor ).getComponent ().requestFocusInWindow ();
            }
            if ( cellEditor instanceof WebDefaultCellEditor )
            {
                ( ( WebDefaultCellEditor ) cellEditor ).getComponent ().requestFocusInWindow ();
            }
        }
        return editingStarted;
    }

    public boolean stopCellEditing ()
    {
        final TableCellEditor cellEditor = getCellEditor ();
        return cellEditor != null && cellEditor.stopCellEditing ();
    }

    @Override
    public boolean isCellEditable ( int row, int column )
    {
        return editable && super.isCellEditable ( row, column );
    }

    public boolean isEditable ()
    {
        return editable;
    }

    public void setEditable ( boolean editable )
    {
        this.editable = editable;
    }

    public void setVisibleRowCount ( int visibleRowCount )
    {
        this.visibleRowCount = visibleRowCount;

        // Reset preferred viewport size
        setPreferredScrollableViewportSize ( null );

        // Update viewport size
        JScrollPane scrollPane = SwingUtils.getScrollPane ( this );
        if ( scrollPane != null )
        {
            scrollPane.getViewport ().invalidate ();
        }
    }

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
            int rowHeight;
            if ( getRowCount () > 0 )
            {
                Rectangle r = getCellRect ( 0, 0, true );
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

    public WebTableUI getWebUI ()
    {
        return ( WebTableUI ) getUI ();
    }

    @Override
    public void updateUI ()
    {
        // Update table header UI
        if ( getTableHeader () != null )
        {
            getTableHeader ().updateUI ();
        }

        // Update table scroll view and UI
        configureEnclosingScrollPaneUI ();

        // Update table UI
        if ( getUI () == null || !( getUI () instanceof WebTableUI ) )
        {
            try
            {
                setUI ( ( WebTableUI ) ReflectUtils.createInstance ( WebLookAndFeel.tableUI ) );
            }
            catch ( Throwable e )
            {
                e.printStackTrace ();
                setUI ( new WebTableUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    private void configureEnclosingScrollPaneUI ()
    {
        Container p = getParent ();
        if ( p instanceof JViewport )
        {
            Container gp = p.getParent ();
            if ( gp instanceof JScrollPane )
            {
                JScrollPane scrollPane = ( JScrollPane ) gp;
                // Make certain we are the viewPort's view and not, for
                // example, the rowHeaderView of the scrollPane -
                // an implementor of fixed columns might do this.
                JViewport viewport = scrollPane.getViewport ();
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
                    Object componentClass = UIManager.get ( "Table.scrollPaneCornerComponent" );
                    if ( componentClass instanceof Class )
                    {
                        try
                        {
                            corner = ( Component ) ( ( Class ) componentClass ).newInstance ();
                        }
                        catch ( Exception e )
                        {
                            // just ignore and don't set corner
                        }
                    }
                    scrollPane.setCorner ( JScrollPane.UPPER_TRAILING_CORNER, corner );
                }
            }
        }
    }

    /**
     * Font methods
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTable setPlainFont ()
    {
        return SwingUtils.setPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTable setPlainFont ( boolean apply )
    {
        return SwingUtils.setPlainFont ( this, apply );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPlainFont ()
    {
        return SwingUtils.isPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTable setBoldFont ()
    {
        return SwingUtils.setBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTable setBoldFont ( boolean apply )
    {
        return SwingUtils.setBoldFont ( this, apply );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBoldFont ()
    {
        return SwingUtils.isBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTable setItalicFont ()
    {
        return SwingUtils.setItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTable setItalicFont ( boolean apply )
    {
        return SwingUtils.setItalicFont ( this, apply );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isItalicFont ()
    {
        return SwingUtils.isItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTable setFontStyle ( boolean bold, boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTable setFontStyle ( int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTable setFontSize ( int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTable changeFontSize ( int change )
    {
        return SwingUtils.changeFontSize ( this, change );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFontSize ()
    {
        return SwingUtils.getFontSize ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTable setFontSizeAndStyle ( int fontSize, boolean bold, boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTable setFontSizeAndStyle ( int fontSize, int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTable setFontName ( String fontName )
    {
        return SwingUtils.setFontName ( this, fontName );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFontName ()
    {
        return SwingUtils.getFontName ( this );
    }
}