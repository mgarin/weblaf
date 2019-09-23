package com.alee.laf.table;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.managers.language.Language;
import com.alee.managers.language.LanguageListener;
import com.alee.managers.language.LanguageSensitive;
import com.alee.managers.language.UILanguageManager;
import com.alee.managers.style.Bounds;
import com.alee.painter.AbstractPainter;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Basic painter for {@link JTableHeader} component.
 * It is used as {@link WebTableHeaderUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 * @author Mikle Garin
 */
public class TableHeaderPainter<C extends JTableHeader, U extends WebTableHeaderUI> extends AbstractPainter<C, U>
        implements ITableHeaderPainter<C, U>
{
    /**
     * Listeners.
     */
    protected transient MouseAdapter mouseAdapter;
    protected transient LanguageListener languageSensitive;

    /**
     * Runtime variables.
     */
    protected transient TableHeaderCellArea rolloverCell;

    /**
     * Style settings.
     * todo Replace with single background painter per cell
     */
    protected Integer headerHeight;
    protected Color topBgColor;
    protected Color bottomBgColor;
    protected Color gridColor;

    /**
     * Painting variables.
     */
    protected transient CellRendererPane rendererPane = null;

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
                final int column = component.columnAtPoint ( point );
                if ( column != -1 )
                {
                    final TableHeaderCellArea cell = new TableHeaderCellArea ( 0, column );
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
            private void updateRolloverCell ( final TableHeaderCellArea oldCell, final TableHeaderCellArea newCell )
            {
                // Updating rollover cell
                rolloverCell = newCell;

                // Updating custom WebLaF tooltip display state
                final TableHeaderToolTipProvider tableProvider = getTableToolTipProvider ();
                if ( tableProvider != null )
                {
                    tableProvider.hoverAreaChanged ( component, oldCell, newCell );
                }
                else
                {
                    final TableHeaderToolTipProvider headerProvider = getHeaderToolTipProvider ();
                    if ( headerProvider != null )
                    {
                        headerProvider.hoverAreaChanged ( component, oldCell, newCell );
                    }
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
     * Returns {@link TableHeaderToolTipProvider} for {@link JTable} that uses {@link JTableHeader}.
     *
     * @return {@link TableHeaderToolTipProvider} for {@link JTable} that uses {@link JTableHeader}
     */
    @Nullable
    protected TableHeaderToolTipProvider getTableToolTipProvider ()
    {
        return component != null && component.getTable () != null ?
                ( TableHeaderToolTipProvider ) component.getTable ().getClientProperty ( WebTable.HEADER_TOOLTIP_PROVIDER_PROPERTY ) :
                null;
    }

    /**
     * Returns {@link TableHeaderToolTipProvider} for {@link JTableHeader} that uses this {@link TableHeaderPainter}.
     *
     * @return {@link TableHeaderToolTipProvider} for {@link JTableHeader} that uses this {@link TableHeaderPainter}
     */
    @Nullable
    protected TableHeaderToolTipProvider getHeaderToolTipProvider ()
    {
        return component != null ?
                ( TableHeaderToolTipProvider ) component.getClientProperty ( WebTableHeader.TOOLTIP_PROVIDER_PROPERTY ) :
                null;
    }

    /**
     * Installs language listeners.
     */
    protected void installLanguageListeners ()
    {
        languageSensitive = new LanguageListener ()
        {
            @Override
            public void languageChanged ( @NotNull final Language oldLanguage, @NotNull final Language newLanguage )
            {
                if ( isLanguageSensitive () )
                {
                    final JTable table = component.getTable ();
                    if ( table != null && table.getModel () instanceof AbstractTableModel )
                    {
                        // Calling public model methods when possible
                        final AbstractTableModel tableModel = ( AbstractTableModel ) table.getModel ();
                        tableModel.fireTableRowsUpdated ( TableModelEvent.HEADER_ROW, TableModelEvent.HEADER_ROW );
                    }
                    else
                    {
                        // Simply repainting table header
                        component.repaint ();
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
                component.getDefaultRenderer () instanceof LanguageSensitive ||
                component.getTable () instanceof LanguageSensitive ||
                component.getTable () != null && component.getTable ().getModel () instanceof LanguageSensitive )
        {
            // Either table header, its default renderer, table itself or table model is language-sensitive
            sensitive = true;
        }
        else
        {
            // Checking whether or not one of table header column renderers is language-sensitive
            final TableColumnModel columnModel = component.getColumnModel ();
            for ( int i = 0; i < columnModel.getColumnCount (); i++ )
            {
                if ( getHeaderRenderer ( i ) instanceof LanguageSensitive )
                {
                    sensitive = true;
                    break;
                }
            }
            if ( !sensitive )
            {
                // Checking whether or not one of table header values is language-sensitive
                for ( int i = 0; i < columnModel.getColumnCount (); i++ )
                {
                    if ( columnModel.getColumn ( i ).getHeaderValue () instanceof LanguageSensitive )
                    {
                        sensitive = true;
                        break;
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
        // Saving renderer pane reference
        this.rendererPane = rendererPane;
    }

    @Override
    public void paint ( @NotNull final Graphics2D g2d, @NotNull final C c, @NotNull final U ui, @NotNull final Bounds bounds )
    {
        // Creating background paint
        final Paint bgPaint = getBackgroundPaint ( 0, 0, 0, component.getHeight () - 1 );

        // Table header background
        g2d.setPaint ( bgPaint );
        g2d.fillRect ( 0, 0, component.getWidth (), component.getHeight () - 1 );

        // Bottom border line
        g2d.setPaint ( gridColor );
        g2d.drawLine ( 0, component.getHeight () - 1, component.getWidth () - 1, component.getHeight () - 1 );

        // Optimization for empty header
        if ( component.getColumnModel ().getColumnCount () > 0 )
        {
            // Variables
            final Rectangle clip = g2d.getClipBounds ();
            final Point left = clip.getLocation ();
            final Point right = new Point ( clip.x + clip.width - 1, clip.y );
            final TableColumnModel cm = component.getColumnModel ();
            int cMin = component.columnAtPoint ( ltr ? left : right );
            int cMax = component.columnAtPoint ( ltr ? right : left );

            // This should never happen.
            if ( cMin == -1 )
            {
                cMin = 0;
            }

            // If the table does not have enough columns to fill the view we'll get -1.
            // Replace this with the index of the last column.
            if ( cMax == -1 )
            {
                cMax = cm.getColumnCount () - 1;
            }

            // Table titles
            final TableColumn draggedColumn = component.getDraggedColumn ();
            int columnWidth;
            final Rectangle cellRect = component.getHeaderRect ( ltr ? cMin : cMax );
            TableColumn aColumn;
            if ( ltr )
            {
                for ( int column = cMin; column <= cMax; column++ )
                {
                    aColumn = cm.getColumn ( column );
                    columnWidth = aColumn.getWidth ();
                    cellRect.width = columnWidth;
                    if ( aColumn != draggedColumn )
                    {
                        paintCell ( g2d, c, cellRect, column, aColumn, draggedColumn, cm );
                    }
                    cellRect.x += columnWidth;
                }
            }
            else
            {
                for ( int column = cMax; column >= cMin; column-- )
                {
                    aColumn = cm.getColumn ( column );
                    columnWidth = aColumn.getWidth ();
                    cellRect.width = columnWidth;
                    if ( aColumn != draggedColumn )
                    {
                        paintCell ( g2d, c, cellRect, column, aColumn, draggedColumn, cm );
                    }
                    cellRect.x += columnWidth;
                }
            }

            // Paint the dragged column if we are dragging.
            if ( draggedColumn != null )
            {
                // Calculating dragged cell rect
                final int draggedColumnIndex = getViewIndexForColumn ( draggedColumn );
                final Rectangle draggedCellRect = component.getHeaderRect ( draggedColumnIndex );
                draggedCellRect.x += component.getDraggedDistance ();

                // Background
                g2d.setPaint ( bgPaint );
                g2d.fillRect ( draggedCellRect.x - 1, draggedCellRect.y, draggedCellRect.width, draggedCellRect.height - 1 );

                // Header cell
                paintCell ( g2d, c, draggedCellRect, draggedColumnIndex, draggedColumn, draggedColumn, cm );
            }

            // Remove all components in the rendererPane
            rendererPane.removeAll ();

            // Clearing renderer pane reference
            rendererPane = null;
        }
    }

    /**
     * Paints single table header cell (column).
     *
     * @param g2d           graphics context
     * @param c             table header
     * @param rect          cell bounds
     * @param columnIndex   column index
     * @param column        table column
     * @param draggedColumn currently dragged table column
     * @param columnModel   table column model
     */
    protected void paintCell ( final Graphics2D g2d, final C c, final Rectangle rect, final int columnIndex, final TableColumn column,
                               final TableColumn draggedColumn, final TableColumnModel columnModel )
    {
        // Table reference
        final JTable table = c.getTable ();

        // Complex check for the cases when trailing border should be painted
        // It can be painted for middle columns, dragged column or when table is smaller than viewport
        final JScrollPane scrollPane = SwingUtils.getScrollPane ( table );
        final boolean paintTrailingBorder = scrollPane != null && ( column == draggedColumn ||
                table.getAutoResizeMode () == JTable.AUTO_RESIZE_OFF && scrollPane.getViewport ().getWidth () > table.getWidth () ||
                ( ltr ? columnIndex != columnModel.getColumnCount () - 1 : columnIndex != 0 ) );

        // Left side border
        if ( ltr || paintTrailingBorder )
        {
            g2d.setColor ( gridColor );
            g2d.drawLine ( rect.x - 1, rect.y + 2, rect.x - 1, rect.y + rect.height - 4 );
        }

        // Painting dragged cell renderer
        final JComponent headerRenderer = ( JComponent ) getHeaderRenderer ( columnIndex );
        headerRenderer.setOpaque ( false );
        headerRenderer.setEnabled ( table == null || table.isEnabled () );
        rendererPane.paintComponent ( g2d, headerRenderer, component, rect.x, rect.y, rect.width, rect.height, true );

        // Right side border
        if ( !ltr || paintTrailingBorder )
        {
            g2d.setColor ( gridColor );
            g2d.drawLine ( rect.x + rect.width - 1, rect.y + 2, rect.x + rect.width - 1, rect.y + rect.height - 4 );
        }
    }

    /**
     * Returns table header background {@link Paint}.
     *
     * @param x1 first painting bounds X coordinate
     * @param y1 first painting bounds Y coordinate
     * @param x2 second painting bounds X coordinate
     * @param y2 second painting bounds Y coordinate
     * @return table header background {@link Paint}
     */
    protected Paint getBackgroundPaint ( final int x1, final int y1, final int x2, final int y2 )
    {
        final Paint background;
        if ( bottomBgColor == null || Objects.equals ( topBgColor, bottomBgColor ) )
        {
            background = topBgColor;
        }
        else
        {
            background = new GradientPaint ( x1, y1, topBgColor, x2, y2, bottomBgColor );
        }
        return background;
    }

    /**
     * Returns header cell renderer {@link Component} for the specified column index.
     *
     * @param index column index
     * @return header cell renderer {@link Component} for the specified column index
     */
    protected Component getHeaderRenderer ( final int index )
    {
        final TableColumn aColumn = component.getColumnModel ().getColumn ( index );
        TableCellRenderer renderer = aColumn.getHeaderRenderer ();
        if ( renderer == null )
        {
            renderer = component.getDefaultRenderer ();
        }
        final boolean hasFocus = !component.isPaintingForPrint () && component.hasFocus ();
        return renderer.getTableCellRendererComponent ( component.getTable (), aColumn.getHeaderValue (), false, hasFocus, -1, index );
    }

    /**
     * Returns actual view index for the specified column.
     *
     * @param column table column
     * @return actual view index for the specified column
     */
    protected int getViewIndexForColumn ( final TableColumn column )
    {
        int viewIndex = -1;
        final TableColumnModel cm = component.getColumnModel ();
        for ( int index = 0; index < cm.getColumnCount (); index++ )
        {
            if ( cm.getColumn ( index ) == column )
            {
                viewIndex = index;
                break;
            }
        }
        return viewIndex;
    }

    @NotNull
    @Override
    public Dimension getPreferredSize ()
    {
        final Dimension ps = super.getPreferredSize ();
        if ( headerHeight != null )
        {
            ps.height = Math.max ( ps.height, headerHeight );
        }
        return ps;
    }
}