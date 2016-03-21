package com.alee.laf.table;

import com.alee.laf.table.ITablePainter;
import com.alee.laf.table.WebTable;
import com.alee.laf.table.WebTableUI;
import com.alee.managers.tooltip.ToolTipProvider;
import com.alee.painter.AbstractPainter;
import com.alee.utils.CompareUtils;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Alexandr Zernov
 */

public class TablePainter<E extends JTable, U extends WebTableUI> extends AbstractPainter<E, U> implements ITablePainter<E, U>
{
    /**
     * Listeners.
     */
    protected MouseAdapter mouseAdapter;

    /**
     * Runtime variables.
     */
    protected Point rolloverCell;

    /**
     * Painting variables.
     */
    protected CellRendererPane rendererPane = null;

    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Rollover listener
        mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mouseMoved ( final MouseEvent e )
            {
                updateMouseover ( e );
            }

            @Override
            public void mouseDragged ( final MouseEvent e )
            {
                updateMouseover ( e );
            }

            @Override
            public void mouseExited ( final MouseEvent e )
            {
                clearMouseover ();
            }

            private void updateMouseover ( final MouseEvent e )
            {
                final Point point = e.getPoint ();
                final Point cell = p ( component.columnAtPoint ( point ), component.rowAtPoint ( point ) );
                if ( cell.x != -1 && cell.y != -1 )
                {
                    if ( !CompareUtils.equals ( rolloverCell, cell ) )
                    {
                        updateRolloverCell ( rolloverCell, cell );
                    }
                }
                else
                {
                    clearMouseover ();
                }
            }

            private void clearMouseover ()
            {
                if ( rolloverCell != null )
                {
                    updateRolloverCell ( rolloverCell, null );
                }
            }

            private void updateRolloverCell ( final Point oldCell, final Point newCell )
            {
                // Updating rollover cell
                rolloverCell = newCell;

                // Updating custom WebLaF tooltip display state
                final ToolTipProvider provider = getToolTipProvider ();
                if ( provider != null )
                {
                    final int oldIndex = oldCell != null ? oldCell.y : -1;
                    final int oldColumn = oldCell != null ? oldCell.x : -1;
                    final int newIndex = newCell != null ? newCell.y : -1;
                    final int newColumn = newCell != null ? newCell.x : -1;
                    provider.hoverCellChanged ( component, oldIndex, oldColumn, newIndex, newColumn );
                }
            }
        };
        component.addMouseListener ( mouseAdapter );
        component.addMouseMotionListener ( mouseAdapter );
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Removing listeners
        component.removeMouseListener ( mouseAdapter );
        component.removeMouseMotionListener ( mouseAdapter );
        mouseAdapter = null;

        super.uninstall ( c, ui );
    }

    @Override
    public void prepareToPaint ( final CellRendererPane rendererPane )
    {
        this.rendererPane = rendererPane;
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        final Rectangle clip = g2d.getClipBounds ();

        //        Rectangle bounds = table.getBounds();
        // account for the fact that the graphics has already been translated
        // into the table's bounds
        //        bounds.x = bounds.y = 0;

        if ( component.getRowCount () <= 0 || component.getColumnCount () <= 0 ||
                // this check prevents us from painting the entire table
                // when the clip doesn't intersect our bounds at all
                !bounds.intersects ( clip ) )
        {

            paintDropLines ( g2d );
            return;
        }

        final Point upperLeft = clip.getLocation ();
        if ( !ltr )
        {
            upperLeft.x++;
        }

        final Point lowerRight = p ( clip.x + clip.width - ( ltr ? 1 : 0 ), clip.y + clip.height );

        int rMin = component.rowAtPoint ( upperLeft );
        int rMax = component.rowAtPoint ( lowerRight );
        // This should never happen (as long as our bounds intersect the clip,
        // which is why we bail above if that is the case).
        if ( rMin == -1 )
        {
            rMin = 0;
        }
        // If the table does not have enough rows to fill the view we'll get -1.
        // (We could also get -1 if our bounds don't intersect the clip,
        // which is why we bail above if that is the case).
        // Replace this with the index of the last row.
        if ( rMax == -1 )
        {
            rMax = component.getRowCount () - 1;
        }

        int cMin = component.columnAtPoint ( ltr ? upperLeft : lowerRight );
        int cMax = component.columnAtPoint ( ltr ? lowerRight : upperLeft );
        // This should never happen.
        if ( cMin == -1 )
        {
            cMin = 0;
        }
        // If the table does not have enough columns to fill the view we'll get -1.
        // Replace this with the index of the last column.
        if ( cMax == -1 )
        {
            cMax = component.getColumnCount () - 1;
        }

        // Paint the grid.
        paintGrid ( g2d, rMin, rMax, cMin, cMax );

        // Paint the cells.
        paintCells ( g2d, rMin, rMax, cMin, cMax );

        paintDropLines ( g2d );

        rendererPane = null;
    }

    protected void paintDropLines ( final Graphics g )
    {
        final JTable.DropLocation loc = component.getDropLocation ();
        if ( loc == null )
        {
            return;
        }

        final Color color = UIManager.getColor ( "Table.dropLineColor" );
        final Color shortColor = UIManager.getColor ( "Table.dropLineShortColor" );
        if ( color == null && shortColor == null )
        {
            return;
        }

        Rectangle rect;

        rect = getHDropLineRect ( loc );
        if ( rect != null )
        {
            final int x = rect.x;
            final int w = rect.width;
            if ( color != null )
            {
                extendRect ( rect, true );
                g.setColor ( color );
                g.fillRect ( rect.x, rect.y, rect.width, rect.height );
            }
            if ( !loc.isInsertColumn () && shortColor != null )
            {
                g.setColor ( shortColor );
                g.fillRect ( x, rect.y, w, rect.height );
            }
        }

        rect = getVDropLineRect ( loc );
        if ( rect != null )
        {
            final int y = rect.y;
            final int h = rect.height;
            if ( color != null )
            {
                extendRect ( rect, false );
                g.setColor ( color );
                g.fillRect ( rect.x, rect.y, rect.width, rect.height );
            }
            if ( !loc.isInsertRow () && shortColor != null )
            {
                g.setColor ( shortColor );
                g.fillRect ( rect.x, y, rect.width, h );
            }
        }
    }

    protected Rectangle getHDropLineRect ( final JTable.DropLocation loc )
    {
        if ( !loc.isInsertRow () )
        {
            return null;
        }

        int row = loc.getRow ();
        int col = loc.getColumn ();
        if ( col >= component.getColumnCount () )
        {
            col--;
        }

        final Rectangle rect = component.getCellRect ( row, col, true );

        if ( row >= component.getRowCount () )
        {
            row--;
            final Rectangle prevRect = component.getCellRect ( row, col, true );
            rect.y = prevRect.y + prevRect.height;
        }

        if ( rect.y == 0 )
        {
            rect.y = -1;
        }
        else
        {
            rect.y -= 2;
        }

        rect.height = 3;

        return rect;
    }

    protected Rectangle getVDropLineRect ( final JTable.DropLocation loc )
    {
        if ( !loc.isInsertColumn () )
        {
            return null;
        }

        int col = loc.getColumn ();
        Rectangle rect = component.getCellRect ( loc.getRow (), col, true );

        if ( col >= component.getColumnCount () )
        {
            col--;
            rect = component.getCellRect ( loc.getRow (), col, true );
            if ( ltr )
            {
                rect.x = rect.x + rect.width;
            }
        }
        else if ( !ltr )
        {
            rect.x = rect.x + rect.width;
        }

        if ( rect.x == 0 )
        {
            rect.x = -1;
        }
        else
        {
            rect.x -= 2;
        }

        rect.width = 3;

        return rect;
    }

    protected Rectangle extendRect ( final Rectangle rect, final boolean horizontal )
    {
        if ( rect != null )
        {
            if ( horizontal )
            {
                rect.x = 0;
                rect.width = component.getWidth ();
            }
            else
            {
                rect.y = 0;

                if ( component.getRowCount () != 0 )
                {
                    final Rectangle lastRect = component.getCellRect ( component.getRowCount () - 1, 0, true );
                    rect.height = lastRect.y + lastRect.height;
                }
                else
                {
                    rect.height = component.getHeight ();
                }
            }
        }

        return rect;
    }

    /*
     * Paints the grid lines within <I>aRect</I>, using the grid color set with <I>setGridColor</I>. Paints vertical lines
     * if <code>getShowVerticalLines()</code> returns true and paints horizontal lines if <code>getShowHorizontalLines()</code>
     * returns true.
     */
    protected void paintGrid ( final Graphics g, final int rMin, final int rMax, final int cMin, final int cMax )
    {
        g.setColor ( component.getGridColor () );

        final Rectangle minCell = component.getCellRect ( rMin, cMin, true );
        final Rectangle maxCell = component.getCellRect ( rMax, cMax, true );
        final Rectangle damagedArea = minCell.union ( maxCell );

        if ( component.getShowHorizontalLines () )
        {
            final int tableWidth = damagedArea.x + damagedArea.width;
            int y = damagedArea.y;
            for ( int row = rMin; row <= rMax; row++ )
            {
                y += component.getRowHeight ( row );
                g.drawLine ( damagedArea.x, y - 1, tableWidth - 1, y - 1 );
            }
        }
        if ( component.getShowVerticalLines () )
        {
            final TableColumnModel cm = component.getColumnModel ();
            final int tableHeight = damagedArea.y + damagedArea.height;
            int x;
            if ( ltr )
            {
                x = damagedArea.x;
                for ( int column = cMin; column <= cMax; column++ )
                {
                    final int w = cm.getColumn ( column ).getWidth ();
                    x += w;
                    g.drawLine ( x - 1, 0, x - 1, tableHeight - 1 );
                }
            }
            else
            {
                x = damagedArea.x;
                for ( int column = cMax; column >= cMin; column-- )
                {
                    final int w = cm.getColumn ( column ).getWidth ();
                    x += w;
                    g.drawLine ( x - 1, 0, x - 1, tableHeight - 1 );
                }
            }
        }
    }

    protected void paintCells ( final Graphics g, final int rMin, final int rMax, final int cMin, final int cMax )
    {
        final JTableHeader header = component.getTableHeader ();
        final TableColumn draggedColumn = ( header == null ) ? null : header.getDraggedColumn ();

        final TableColumnModel cm = component.getColumnModel ();
        final int columnMargin = cm.getColumnMargin ();

        Rectangle cellRect;
        TableColumn aColumn;
        int columnWidth;
        if ( ltr )
        {
            for ( int row = rMin; row <= rMax; row++ )
            {
                cellRect = component.getCellRect ( row, cMin, false );
                for ( int column = cMin; column <= cMax; column++ )
                {
                    aColumn = cm.getColumn ( column );
                    columnWidth = aColumn.getWidth ();
                    cellRect.width = columnWidth - columnMargin;
                    if ( aColumn != draggedColumn )
                    {
                        paintCell ( g, cellRect, row, column );
                    }
                    cellRect.x += columnWidth;
                }
            }
        }
        else
        {
            for ( int row = rMin; row <= rMax; row++ )
            {
                cellRect = component.getCellRect ( row, cMin, false );
                aColumn = cm.getColumn ( cMin );
                if ( aColumn != draggedColumn )
                {
                    columnWidth = aColumn.getWidth ();
                    cellRect.width = columnWidth - columnMargin;
                    paintCell ( g, cellRect, row, cMin );
                }
                for ( int column = cMin + 1; column <= cMax; column++ )
                {
                    aColumn = cm.getColumn ( column );
                    columnWidth = aColumn.getWidth ();
                    cellRect.width = columnWidth - columnMargin;
                    cellRect.x -= columnWidth;
                    if ( aColumn != draggedColumn )
                    {
                        paintCell ( g, cellRect, row, column );
                    }
                }
            }
        }

        // Paint the dragged column if we are dragging.
        if ( draggedColumn != null )
        {
            paintDraggedArea ( g, rMin, rMax, draggedColumn, header.getDraggedDistance () );
        }

        // Remove any renderers that may be left in the rendererPane.
        rendererPane.removeAll ();
    }

    protected void paintCell ( final Graphics g, final Rectangle cellRect, final int row, final int column )
    {
        if ( component.isEditing () && component.getEditingRow () == row &&
                component.getEditingColumn () == column )
        {
            final Component editor = component.getEditorComponent ();
            editor.setBounds ( cellRect );
            editor.validate ();
        }
        else
        {
            final TableCellRenderer renderer = component.getCellRenderer ( row, column );
            final Component prepareRenderer = component.prepareRenderer ( renderer, row, column );
            rendererPane.paintComponent ( g, prepareRenderer, component, cellRect.x, cellRect.y, cellRect.width, cellRect.height, true );
        }
    }

    protected void paintDraggedArea ( final Graphics g, final int rMin, final int rMax, final TableColumn draggedColumn,
                                      final int distance )
    {
        final int draggedColumnIndex = viewIndexForColumn ( draggedColumn );

        final Rectangle minCell = component.getCellRect ( rMin, draggedColumnIndex, true );
        final Rectangle maxCell = component.getCellRect ( rMax, draggedColumnIndex, true );

        final Rectangle vacatedColumnRect = minCell.union ( maxCell );

        // Paint a gray well in place of the moving column.
        g.setColor ( component.getBackground () );
        g.fillRect ( vacatedColumnRect.x, vacatedColumnRect.y, vacatedColumnRect.width, vacatedColumnRect.height );

        // Move to the where the cell has been dragged.
        vacatedColumnRect.x += distance;

        // Fill the background.
        g.setColor ( component.getBackground () );
        g.fillRect ( vacatedColumnRect.x, vacatedColumnRect.y, vacatedColumnRect.width, vacatedColumnRect.height );

        // Paint the vertical grid lines if necessary.
        if ( component.getShowVerticalLines () )
        {
            g.setColor ( component.getGridColor () );
            final int x1 = vacatedColumnRect.x;
            final int y1 = vacatedColumnRect.y;
            final int x2 = x1 + vacatedColumnRect.width - 1;
            final int y2 = y1 + vacatedColumnRect.height - 1;
            // Left
            g.drawLine ( x1 - 1, y1, x1 - 1, y2 );
            // Right
            g.drawLine ( x2, y1, x2, y2 );
        }

        for ( int row = rMin; row <= rMax; row++ )
        {
            // Render the cell value
            final Rectangle r = component.getCellRect ( row, draggedColumnIndex, false );
            r.x += distance;
            paintCell ( g, r, row, draggedColumnIndex );

            // Paint the (lower) horizontal grid line if necessary.
            if ( component.getShowHorizontalLines () )
            {
                g.setColor ( component.getGridColor () );
                final Rectangle rcr = component.getCellRect ( row, draggedColumnIndex, true );
                rcr.x += distance;
                final int x1 = rcr.x;
                final int y1 = rcr.y;
                final int x2 = x1 + rcr.width - 1;
                final int y2 = y1 + rcr.height - 1;
                g.drawLine ( x1, y2, x2, y2 );
            }
        }
    }

    protected int viewIndexForColumn ( final TableColumn aColumn )
    {
        final TableColumnModel cm = component.getColumnModel ();
        for ( int column = 0; column < cm.getColumnCount (); column++ )
        {
            if ( cm.getColumn ( column ) == aColumn )
            {
                return column;
            }
        }
        return -1;
    }

    /**
     * Returns custom WebLaF tooltip provider.
     *
     * @return custom WebLaF tooltip provider
     */
    protected ToolTipProvider<? extends WebTable> getToolTipProvider ()
    {
        return component != null && component instanceof WebTable ? ( ( WebTable ) component ).getToolTipProvider () : null;
    }
}