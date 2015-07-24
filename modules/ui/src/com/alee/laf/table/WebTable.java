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
import com.alee.managers.tooltip.ToolTipProvider;
import com.alee.utils.GeometryUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.FontMethods;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.EventObject;
import java.util.Vector;

/**
 * @author Mikle Garin
 */

public class WebTable extends JTable implements FontMethods<WebTable>
{
    private boolean editable = true;
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

    public void setSelectedRow ( final int row )
    {
        setSelectedRow ( row, true );
    }

    public void setSelectedRow ( final int row, final boolean shouldScroll )
    {
        clearSelection ();
        addSelectedRow ( row );
        if ( row != -1 && shouldScroll )
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

    public void setSelectedColumn ( final int column, final boolean shouldScroll )
    {
        clearSelection ();
        addSelectedColumn ( column );
        if ( shouldScroll )
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

    public boolean isEditable ()
    {
        return editable;
    }

    public void setEditable ( final boolean editable )
    {
        this.editable = editable;
    }

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

    private void configureEnclosingScrollPaneUI ()
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
    public WebTable setPlainFont ( final boolean apply )
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
    public WebTable setBoldFont ( final boolean apply )
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
    public WebTable setItalicFont ( final boolean apply )
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
    public WebTable setFontStyle ( final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTable setFontStyle ( final int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTable setFontSize ( final int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTable changeFontSize ( final int change )
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
    public WebTable setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTable setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTable setFontName ( final String fontName )
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
