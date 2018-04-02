package com.alee.laf.table;

import com.alee.api.jdk.Objects;
import com.alee.managers.language.Language;
import com.alee.managers.language.LanguageListener;
import com.alee.managers.language.LanguageSensitive;
import com.alee.managers.language.UILanguageManager;
import com.alee.painter.DefaultPainter;
import com.alee.painter.SectionPainter;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.CollectionUtils;
import com.alee.utils.GeometryUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.general.Pair;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

/**
 * Basic painter for {@link JTable} component.
 * It is used as {@link WebTableUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class TablePainter<C extends JTable, U extends WebTableUI, D extends IDecoration<C, D>> extends AbstractDecorationPainter<C, U, D>
        implements ITablePainter<C, U>
{
    /**
     * Table rows background painter.
     * It can be used to provide background customization for specific table rows.
     */
    @DefaultPainter ( TableRowPainter.class )
    protected ITableRowPainter rowPainter;

    /**
     * Table columns background painter.
     * It can be used to provide background customization for specific table columns.
     */
    @DefaultPainter ( TableColumnPainter.class )
    protected ITableColumnPainter columnPainter;

    /**
     * Table cell background painter.
     * It can be used to provide background customization for specific table cells.
     */
    @DefaultPainter ( TableCellPainter.class )
    protected ITableCellPainter cellPainter;

    /**
     * Table selection painter.
     * It can be used to provide table selection customization.
     */
    @DefaultPainter ( TableSelectionPainter.class )
    protected ITableSelectionPainter selectionPainter;

    /**
     * Dragged column background painter
     */
    @DefaultPainter ( TableColumnPainter.class )
    protected ITableColumnPainter draggedColumnPainter;

    /**
     * Listeners.
     */
    protected transient MouseAdapter mouseAdapter;
    protected transient LanguageListener languageSensitive;

    /**
     * Runtime variables.
     */
    protected transient TableCellArea rolloverCell;

    /**
     * Painting variables.
     */
    protected transient CellRendererPane rendererPane = null;

    @Override
    protected List<SectionPainter<C, U>> getSectionPainters ()
    {
        return asList ( rowPainter, columnPainter, cellPainter, selectionPainter, draggedColumnPainter );
    }

    @Override
    protected void installPropertiesAndListeners ()
    {
        super.installPropertiesAndListeners ();
        installTableMouseListeners ();
        installLanguageListeners ();
    }

    @Override
    protected void uninstallPropertiesAndListeners ()
    {
        uninstallLanguageListeners ();
        uninstallTableMouseListeners ();
        super.uninstallPropertiesAndListeners ();
    }

    /**
     * Installs table mouse listeners.
     */
    protected void installTableMouseListeners ()
    {
        mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mouseMoved ( final MouseEvent e )
            {
                // Ensure component is still available
                // This might happen if painter is replaced from another MouseMotionListener
                if ( component != null )
                {
                    updateMouseover ( e );
                }
            }

            @Override
            public void mouseDragged ( final MouseEvent e )
            {
                // Ensure component is still available
                // This might happen if painter is replaced from another MouseMotionListener
                if ( component != null )
                {
                    updateMouseover ( e );
                }
            }

            @Override
            public void mouseExited ( final MouseEvent e )
            {
                // Ensure component is still available
                // This might happen if painter is replaced from another MouseListener
                if ( component != null )
                {
                    clearMouseover ();
                }
            }

            /**
             * Performs mouseover cell update.
             *
             * @param e mouse event
             */
            private void updateMouseover ( final MouseEvent e )
            {
                final Point point = e.getPoint ();
                final int row = component.rowAtPoint ( point );
                final int column = component.columnAtPoint ( point );
                if ( row != -1 && column != -1 )
                {
                    final TableCellArea cell = new TableCellArea ( row, column );
                    if ( Objects.notEquals ( rolloverCell, cell ) )
                    {
                        updateRolloverCell ( rolloverCell, cell );
                    }
                }
                else
                {
                    clearMouseover ();
                }
            }

            /**
             * Clears mouseover cell.
             */
            private void clearMouseover ()
            {
                if ( rolloverCell != null )
                {
                    updateRolloverCell ( rolloverCell, null );
                }
            }

            /**
             * Performs mouseover cell update.
             *
             * @param oldCell previous mouseover cell
             * @param newCell current mouseover cell
             */
            private void updateRolloverCell ( final TableCellArea oldCell, final TableCellArea newCell )
            {
                // Updating rollover cell
                rolloverCell = newCell;

                // Updating custom WebLaF tooltip display state
                final TableToolTipProvider provider = getToolTipProvider ();
                if ( provider != null )
                {
                    provider.hoverAreaChanged ( component, oldCell, newCell );
                }
            }
        };
        component.addMouseListener ( mouseAdapter );
        component.addMouseMotionListener ( mouseAdapter );
    }

    /**
     * Uninstalls table mouse listeners.
     */
    protected void uninstallTableMouseListeners ()
    {
        component.removeMouseListener ( mouseAdapter );
        component.removeMouseMotionListener ( mouseAdapter );
        mouseAdapter = null;
    }

    /**
     * Installs language listeners.
     */
    protected void installLanguageListeners ()
    {
        languageSensitive = new LanguageListener ()
        {
            @Override
            public void languageChanged ( final Language oldLanguage, final Language newLanguage )
            {
                if ( isLanguageSensitive () )
                {
                    final TableModel model = component.getModel ();
                    if ( model.getRowCount () > 0 )
                    {
                        if ( model instanceof AbstractTableModel )
                        {
                            // Calling public model methods when possible
                            ( ( AbstractTableModel ) model ).fireTableRowsUpdated ( 0, model.getRowCount () - 1 );
                        }
                        else
                        {
                            // Simply repainting table when we don't have tools to update it properly
                            component.repaint ();
                        }
                    }
                }
            }
        };
        UILanguageManager.addLanguageListener ( component, languageSensitive );
    }

    /**
     * Returns whether or not table is language-sensitive.
     *
     * @return {@code true} if table is language-sensitive, {@code false} otherwise
     */
    protected boolean isLanguageSensitive ()
    {
        boolean sensitive = false;
        if ( component instanceof LanguageSensitive ||
                component.getModel () instanceof LanguageSensitive )
        {
            // Either table or its model is language-sensitive
            sensitive = true;
        }
        else
        {
            // Checking whether or not one of table column renderers is language-sensitive
            final TableColumnModel columnModel = component.getColumnModel ();
            for ( int i = 0; i < columnModel.getColumnCount (); i++ )
            {
                if ( columnModel.getColumn ( i ).getCellRenderer () instanceof LanguageSensitive )
                {
                    sensitive = true;
                    break;
                }
            }
            if ( !sensitive )
            {
                // Checking whether or not one of table default renderers is language-sensitive
                final Hashtable defaultRenderers = ReflectUtils.getFieldValueSafely ( component, "defaultRenderersByColumnClass" );
                if ( defaultRenderers != null )
                {
                    for ( final Object renderer : defaultRenderers.values () )
                    {
                        if ( renderer instanceof LanguageSensitive )
                        {
                            sensitive = true;
                            break;
                        }
                    }
                }
                if ( !sensitive )
                {
                    // Checking whether or not table data is language-sensitive
                    final TableModel model = component.getModel ();
                    for ( int row = 0; row < model.getRowCount (); row++ )
                    {
                        for ( int col = 0; col < model.getColumnCount (); col++ )
                        {
                            if ( model.getValueAt ( row, col ) instanceof LanguageSensitive )
                            {
                                sensitive = true;
                                break;
                            }
                        }
                        if ( sensitive )
                        {
                            break;
                        }
                    }
                }
            }
        }
        return sensitive;
    }

    /**
     * Uninstalls language listeners.
     */
    protected void uninstallLanguageListeners ()
    {
        UILanguageManager.removeLanguageListener ( component, languageSensitive );
        languageSensitive = null;
    }

    @Override
    public void prepareToPaint ( final CellRendererPane rendererPane )
    {
        this.rendererPane = rendererPane;
    }

    @Override
    protected void paintContent ( final Graphics2D g2d, final Rectangle bounds, final C c, final U ui )
    {
        final Rectangle clip = g2d.getClipBounds ();

        // Avoiding painting the entire table when there are no cells
        // Also this check prevents us from painting the entire table when the clip doesn't intersect our bounds at all
        if ( component.getRowCount () <= 0 || component.getColumnCount () <= 0 || !bounds.intersects ( clip ) )
        {
            paintDropLocation ( g2d );
            return;
        }

        final Point upperLeft = clip.getLocation ();
        if ( !ltr )
        {
            upperLeft.x++;
        }

        final Point lowerRight = new Point ( clip.x + clip.width - ( ltr ? 1 : 0 ), clip.y + clip.height );

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

        // Paint table background
        paintBackground ( g2d, bounds, rMin, rMax, cMin, cMax );

        // Paint table grid
        paintGrid ( g2d, rMin, rMax, cMin, cMax );

        // Paint table selection
        paintSelection ( g2d );

        // Painting table cells
        paintContent ( g2d, rMin, rMax, cMin, cMax );

        // Painting drop location
        paintDropLocation ( g2d );

        rendererPane = null;
    }

    /**
     * Paints table background.
     *
     * @param g2d    graphics context
     * @param bounds painting bounds
     * @param rMin   least visible row index
     * @param rMax   last visible row index
     * @param cMin   least visible column index
     * @param cMax   last visible column index
     */
    protected void paintBackground ( final Graphics2D g2d, final Rectangle bounds, final int rMin, final int rMax, final int cMin,
                                     final int cMax )
    {
        final TableColumnModel cm = component.getColumnModel ();
        final int columnMargin = cm.getColumnMargin ();

        Rectangle cellRect;
        TableColumn aColumn;
        int columnWidth;

        // Painting rows background
        if ( rowPainter != null )
        {
            Rectangle rowRect;
            for ( int row = rMin; row <= rMax; row++ )
            {
                cellRect = component.getCellRect ( row, cMin, false );
                rowRect = new Rectangle ( bounds.x, cellRect.y, bounds.width, cellRect.height );
                rowPainter.prepareToPaint ( row );
                paintSection ( rowPainter, g2d, rowRect );
            }
        }

        // Painting columns background
        if ( columnPainter != null )
        {
            Rectangle colRect;
            cellRect = component.getCellRect ( rMin, cMin, false );
            for ( int column = cMin; column <= cMax; column++ )
            {
                aColumn = cm.getColumn ( column );
                columnWidth = aColumn.getWidth ();
                cellRect.width = columnWidth - columnMargin;
                colRect = new Rectangle ( cellRect.x, bounds.y, cellRect.width, bounds.height );
                columnPainter.prepareToPaint ( column );
                paintSection ( columnPainter, g2d, colRect );
                cellRect.x += columnWidth;
            }
        }

        // Painting background
        if ( cellPainter != null )
        {
            for ( int row = rMin; row <= rMax; row++ )
            {
                // First column cell bounds
                cellRect = component.getCellRect ( row, cMin, false );
                for ( int column = cMin; column <= cMax; column++ )
                {
                    // Current column cell bounds
                    aColumn = cm.getColumn ( column );
                    columnWidth = aColumn.getWidth ();
                    cellRect.width = columnWidth - columnMargin;

                    // Shift for RTL orientation
                    if ( !ltr && column != cMin )
                    {
                        cellRect.x -= columnWidth;
                    }

                    // Painting cell background
                    if ( cellPainter != null )
                    {
                        cellPainter.prepareToPaint ( row, column );
                        paintSection ( cellPainter, g2d, cellRect );
                    }

                    // Shift for LTR orientation
                    if ( ltr )
                    {
                        cellRect.x += columnWidth;
                    }
                }
            }
        }
    }

    /**
     * Paints table grid lines within visible bounds.
     *
     * @param g2d  graphics context
     * @param rMin least visible row index
     * @param rMax last visible row index
     * @param cMin least visible column index
     * @param cMax last visible column index
     */
    protected void paintGrid ( final Graphics2D g2d, final int rMin, final int rMax, final int cMin, final int cMax )
    {
        g2d.setColor ( component.getGridColor () );

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
                g2d.drawLine ( damagedArea.x, y - 1, tableWidth - 1, y - 1 );
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
                    g2d.drawLine ( x - 1, 0, x - 1, tableHeight - 1 );
                }
            }
            else
            {
                x = damagedArea.x;
                for ( int column = cMax; column >= cMin; column-- )
                {
                    final int w = cm.getColumn ( column ).getWidth ();
                    x += w;
                    g2d.drawLine ( x - 1, 0, x - 1, tableHeight - 1 );
                }
            }
        }
    }

    /**
     * Paints table selection.
     *
     * @param g2d graphics context
     */
    protected void paintSelection ( final Graphics2D g2d )
    {
        if ( selectionPainter != null )
        {
            final JTableHeader header = component.getTableHeader ();
            final TableColumn draggedColumn = header == null ? null : header.getDraggedColumn ();
            final int draggedIndex = component.convertColumnIndexToView ( draggedColumn != null ? draggedColumn.getModelIndex () : -1 );

            final int[] rows = component.getSelectedRows ();
            final int[] cols = component.getSelectedColumns ();

            if ( rows.length > 0 || cols.length > 0 )
            {
                final boolean rs = component.getRowSelectionAllowed ();
                final boolean cs = component.getColumnSelectionAllowed ();
                if ( rs && cs )
                {
                    final List<Pair<Integer, Integer>> rowSections = getSections ( rows, -1 );
                    final List<Pair<Integer, Integer>> colSections = getSections ( cols, draggedIndex );
                    for ( final Pair<Integer, Integer> rowSection : rowSections )
                    {
                        for ( final Pair<Integer, Integer> colSection : colSections )
                        {
                            final Rectangle first = component.getCellRect ( rowSection.getKey (), colSection.getKey (), false );
                            final Rectangle last = component.getCellRect ( rowSection.getValue (), colSection.getValue (), false );
                            final Rectangle selection = GeometryUtils.getContainingRect ( first, last );
                            paintSection ( selectionPainter, g2d, selection );
                        }
                    }
                }
                else if ( rs )
                {
                    final List<Pair<Integer, Integer>> rowSections = getSections ( rows, -1 );
                    for ( final Pair<Integer, Integer> rowSection : rowSections )
                    {
                        final Rectangle first = component.getCellRect ( rowSection.getKey (), 0, false );
                        final Rectangle last = component.getCellRect ( rowSection.getValue (), component.getColumnCount () - 1, false );
                        final Rectangle selection = GeometryUtils.getContainingRect ( first, last );
                        paintSection ( selectionPainter, g2d, selection );
                    }
                }
                else if ( cs )
                {
                    final List<Pair<Integer, Integer>> colSections = getSections ( cols, draggedIndex );
                    for ( final Pair<Integer, Integer> colSection : colSections )
                    {
                        final Rectangle first = component.getCellRect ( 0, colSection.getKey (), false );
                        final Rectangle last = component.getCellRect ( component.getRowCount () - 1, colSection.getValue (), false );
                        final Rectangle selection = GeometryUtils.getContainingRect ( first, last );
                        paintSection ( selectionPainter, g2d, selection );
                    }
                }
            }
        }
    }

    /**
     * Paints table contents.
     *
     * @param g2d  graphics context
     * @param rMin least visible row index
     * @param rMax last visible row index
     * @param cMin least visible column index
     * @param cMax last visible column index
     */
    protected void paintContent ( final Graphics2D g2d, final int rMin, final int rMax, final int cMin, final int cMax )
    {
        final JTableHeader header = component.getTableHeader ();
        final TableColumn draggedColumn = header == null ? null : header.getDraggedColumn ();
        final TableColumnModel cm = component.getColumnModel ();
        final int columnMargin = cm.getColumnMargin ();

        Rectangle cellRect;
        TableColumn aColumn;
        int columnWidth;

        // Painting cells content
        for ( int row = rMin; row <= rMax; row++ )
        {
            // First column cell bounds
            cellRect = component.getCellRect ( row, cMin, false );
            for ( int column = cMin; column <= cMax; column++ )
            {
                // Current column cell bounds
                aColumn = cm.getColumn ( column );
                columnWidth = aColumn.getWidth ();
                cellRect.width = columnWidth - columnMargin;

                // Shift for RTL orientation
                if ( !ltr && column != cMin )
                {
                    cellRect.x -= columnWidth;
                }

                // Painting non-dragged cell
                if ( aColumn != draggedColumn )
                {
                    paintCell ( g2d, cellRect, row, column );
                }

                // Shift for LTR orientation
                if ( ltr )
                {
                    cellRect.x += columnWidth;
                }
            }
        }

        // Paint the dragged column if we are dragging.
        if ( draggedColumn != null )
        {
            paintDraggedColumn ( g2d, rMin, rMax, draggedColumn, header.getDraggedDistance () );
        }

        // Remove any renderers that may be left in the rendererPane.
        rendererPane.removeAll ();
    }

    /**
     * Returns groupable sections of indices.
     *
     * @param indices  indices
     * @param excluded excluded index
     * @return groupable sections of indices
     */
    protected List<Pair<Integer, Integer>> getSections ( final int[] indices, final int excluded )
    {
        // Sorting indices first
        // We have to do it to ensure they are strictly ordered
        Arrays.sort ( indices );

        // Removing excluded index
        final List<Integer> inx = CollectionUtils.asList ( indices );
        inx.remove ( ( Integer ) excluded );

        // Collecting sections
        final List<Pair<Integer, Integer>> sections = new ArrayList<Pair<Integer, Integer>> ( 1 );
        int first = -1;
        for ( int i = 0; i < inx.size (); i++ )
        {
            if ( first != -1 )
            {
                if ( inx.get ( i ) - 1 != inx.get ( i - 1 ) )
                {
                    sections.add ( new Pair<Integer, Integer> ( first, inx.get ( i - 1 ) ) );
                    first = inx.get ( i );
                }
            }
            else
            {
                first = inx.get ( i );
            }
            if ( i == inx.size () - 1 )
            {
                sections.add ( new Pair<Integer, Integer> ( first, inx.get ( i ) ) );
            }
        }
        return sections;
    }

    /**
     * Paint single table cell at the specified row and column.
     *
     * @param g2d    graphics context
     * @param bounds cell bounds
     * @param row    cell row index
     * @param column cell column index
     */
    protected void paintCell ( final Graphics2D g2d, final Rectangle bounds, final int row, final int column )
    {
        // Placing cell editor or painting cell renderer
        if ( component.isEditing () && component.getEditingRow () == row && component.getEditingColumn () == column )
        {
            // Correctly place cell editor
            final Component editor = component.getEditorComponent ();
            editor.setBounds ( bounds );
            editor.validate ();
        }
        else
        {
            // Paint cell renderer
            final TableCellRenderer renderer = component.getCellRenderer ( row, column );
            final Component prepareRenderer = component.prepareRenderer ( renderer, row, column );
            rendererPane.paintComponent ( g2d, prepareRenderer, component, bounds.x, bounds.y, bounds.width, bounds.height, true );
        }
    }

    /**
     * Paints dragged table column.
     *
     * @param g2d      graphics context
     * @param rMin     least visible row index
     * @param rMax     last visible row index
     * @param column   dragged column
     * @param distance dragged distance
     */
    protected void paintDraggedColumn ( final Graphics2D g2d, final int rMin, final int rMax, final TableColumn column, final int distance )
    {
        final int index = component.convertColumnIndexToView ( column.getModelIndex () );
        final Rectangle minCell = component.getCellRect ( rMin, index, true );
        final Rectangle maxCell = component.getCellRect ( rMax, index, true );

        // Vacated cell bounds
        final Rectangle vcr = minCell.union ( maxCell );

        // Paint a gray well in place of the moving column.
        // g2d.setColor ( component.getBackground () );
        // g2d.fillRect ( vacatedColumnRect.x, vacatedColumnRect.y, vacatedColumnRect.width - 1, vacatedColumnRect.height );

        // Move to the where the cell has been dragged.
        vcr.x += distance;

        // Fill the background
        if ( draggedColumnPainter != null )
        {
            final Rectangle b = new Rectangle ( vcr.x, vcr.y, vcr.width, vcr.height );
            draggedColumnPainter.prepareToPaint ( index );
            paintSection ( draggedColumnPainter, g2d, b );
        }

        // Paint the vertical grid lines if necessary.
        if ( component.getShowVerticalLines () )
        {
            g2d.setColor ( component.getGridColor () );
            final int x1 = vcr.x;
            final int y1 = vcr.y;
            final int x2 = x1 + vcr.width - 1;
            final int y2 = y1 + vcr.height - 1;
            g2d.drawLine ( x1 - 1, y1, x1 - 1, y2 );
            g2d.drawLine ( x2, y1, x2, y2 );
        }

        for ( int row = rMin; row <= rMax; row++ )
        {
            // Render the cell value
            final Rectangle r = component.getCellRect ( row, index, false );
            r.x += distance;
            paintCell ( g2d, r, row, index );

            // Paint the (lower) horizontal grid line if necessary.
            if ( component.getShowHorizontalLines () )
            {
                g2d.setColor ( component.getGridColor () );
                final Rectangle rcr = component.getCellRect ( row, index, true );
                rcr.x += distance;
                final int x1 = rcr.x;
                final int y1 = rcr.y;
                final int x2 = x1 + rcr.width - 1;
                final int y2 = y1 + rcr.height - 1;
                g2d.drawLine ( x1, y2, x2, y2 );
            }
        }
    }

    /**
     * Paints drop location.
     *
     * @param g2d graphics context
     */
    protected void paintDropLocation ( final Graphics2D g2d )
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
                g2d.setColor ( color );
                g2d.fillRect ( rect.x, rect.y, rect.width, rect.height );
            }
            if ( !loc.isInsertColumn () && shortColor != null )
            {
                g2d.setColor ( shortColor );
                g2d.fillRect ( x, rect.y, w, rect.height );
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
                g2d.setColor ( color );
                g2d.fillRect ( rect.x, rect.y, rect.width, rect.height );
            }
            if ( !loc.isInsertRow () && shortColor != null )
            {
                g2d.setColor ( shortColor );
                g2d.fillRect ( rect.x, y, rect.width, h );
            }
        }
    }

    /**
     * Returns horizontal drop line bounds.
     *
     * @param location drop location
     * @return horizontal drop line bounds
     */
    protected Rectangle getHDropLineRect ( final JTable.DropLocation location )
    {
        if ( !location.isInsertRow () )
        {
            return null;
        }

        int row = location.getRow ();
        int col = location.getColumn ();
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

    /**
     * Returns vertical drop line bounds.
     *
     * @param location drop location
     * @return vertical drop line bounds
     */
    protected Rectangle getVDropLineRect ( final JTable.DropLocation location )
    {
        if ( !location.isInsertColumn () )
        {
            return null;
        }

        int col = location.getColumn ();
        Rectangle rect = component.getCellRect ( location.getRow (), col, true );

        if ( col >= component.getColumnCount () )
        {
            col--;
            rect = component.getCellRect ( location.getRow (), col, true );
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

    /**
     * Returns extended drop line bounds.
     *
     * @param rect       drop line bounds
     * @param horizontal whether or not line is horizontal
     * @return extended drop line bounds
     */
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

    /**
     * Returns {@link TableToolTipProvider} for {@link JTable} that uses this {@link TablePainter}.
     *
     * @return {@link TableToolTipProvider} for {@link JTable} that uses this {@link TablePainter}
     */
    protected TableToolTipProvider getToolTipProvider ()
    {
        return component != null && component instanceof WebTable ? ( ( WebTable ) component ).getToolTipProvider () : null;
    }
}