package com.alee.laf.grouping;

import com.alee.painter.PainterSupport;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.utils.general.Pair;
import com.alee.utils.swing.SizeType;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Layout designed specifically for usage within {@link com.alee.laf.grouping.GroupPane} container.
 * It constructs a grid of components to be visually grouped and knows how to retrieve components at specific cells.
 *
 * @author Mikle Garin
 */

@XStreamAlias ( "GroupPaneLayout" )
public class GroupPaneLayout extends AbstractGroupingLayout implements SwingConstants
{
    /**
     * Components placement order orientation.
     */
    @XStreamAsAttribute
    protected int orientation;

    /**
     * Amount of columns used to place components.
     */
    @XStreamAsAttribute
    protected int columns;

    /**
     * Amount of rows used to place components.
     */
    @XStreamAsAttribute
    protected int rows;

    /**
     * Component constraints.
     */
    protected transient final Map<Component, GroupPaneConstraints> constraints = new HashMap<Component, GroupPaneConstraints> ( 5 );

    /**
     * Constructs default layout.
     */
    public GroupPaneLayout ()
    {
        this ( HORIZONTAL, Integer.MAX_VALUE, 1 );
    }

    /**
     * Constructs layout with the specified amount of rows and columns.
     *
     * @param orientation components placement order orientation
     */
    public GroupPaneLayout ( final int orientation )
    {
        this ( orientation, Integer.MAX_VALUE, 1 );
    }

    /**
     * Constructs layout with the specified amount of rows and columns.
     *
     * @param columns amount of columns used to place components
     * @param rows    amount of rows used to place components
     */
    public GroupPaneLayout ( final int columns, final int rows )
    {
        this ( HORIZONTAL, columns, rows );
    }

    /**
     * Constructs layout with the specified amount of rows and columns.
     *
     * @param orientation components placement order orientation
     * @param columns     amount of columns used to place components
     * @param rows        amount of rows used to place components
     */
    public GroupPaneLayout ( final int orientation, final int columns, final int rows )
    {
        super ();
        setOrientation ( orientation );
        setColumns ( columns );
        setRows ( rows );
    }

    /**
     * Returns components placement order orientation.
     *
     * @return components placement order orientation
     */
    public int getOrientation ()
    {
        return orientation;
    }

    /**
     * Sets components placement order orientation.
     *
     * @param orientation components placement order orientation
     */
    public void setOrientation ( final int orientation )
    {
        this.orientation = orientation;
    }

    /**
     * Returns amount of columns used to place components.
     *
     * @return amount of columns used to place components
     */
    public int getColumns ()
    {
        return columns;
    }

    /**
     * Sets amount of columns used to place components.
     *
     * @param columns amount of columns to place components
     */
    public void setColumns ( final int columns )
    {
        this.columns = columns;
    }

    /**
     * Returns amount of rows used to place components.
     *
     * @return amount of rows used to place components
     */
    public int getRows ()
    {
        return rows;
    }

    /**
     * Sets amount of rows used to place components.
     *
     * @param rows amount of rows to place components
     */
    public void setRows ( final int rows )
    {
        this.rows = rows;
    }

    @Override
    public void addComponent ( final Component component, final Object c )
    {
        // Saving constraints
        if ( c != null && !( c instanceof GroupPaneConstraints ) )
        {
            throw new RuntimeException ( "Unsupported layout constraints: " + c );
        }
        constraints.put ( component, c != null ? ( GroupPaneConstraints ) c : GroupPaneConstraints.PREFERRED );

        // Performing basic operations
        super.addComponent ( component, c );
    }

    @Override
    public void removeComponent ( final Component component )
    {
        // Performing basic operations
        super.removeComponent ( component );

        // Removing saved constraints
        constraints.remove ( component );
    }

    @Override
    public void layoutContainer ( final Container parent )
    {
        // Retrieving actual grid size
        final GridSize gridSize = getActualGridSize ( parent );

        // Calculating children preferred sizes
        final Pair<int[], int[]> sizes = calculateSizes ( parent, gridSize, SizeType.current );

        // Laying out components
        // To do that we will simply iterate through the whole grid
        // Some cells we will iterate through won't have components, we will simply skip those
        final Insets border = parent.getInsets ();
        int y = border.top;
        for ( int row = 0; row < gridSize.rows; row++ )
        {
            int x = border.left;
            for ( int column = 0; column < gridSize.columns; column++ )
            {
                // Converting grid point to component index
                final int index = pointToIndex ( parent, column, row, gridSize );

                // Retrieving cell component if it exists
                final Component component = parent.getComponent ( index );
                if ( component != null )
                {
                    // Updating its bounds
                    component.setBounds ( x, y, sizes.key[ column ], sizes.value[ row ] );
                }

                // Move forward into grid
                x += sizes.key[ column ];
            }

            // Move forward into grid
            y += sizes.value[ row ];
        }
    }

    @Override
    public Dimension preferredLayoutSize ( final Container parent )
    {
        // Retrieving actual grid size
        final GridSize gridSize = getActualGridSize ( parent );

        // Calculating children preferred sizes
        final Pair<int[], int[]> sizes = calculateSizes ( parent, gridSize, SizeType.preferred );

        // Calculating preferred size
        final Dimension ps = new Dimension ( 0, 0 );
        for ( final Integer columnWith : sizes.key )
        {
            ps.width += columnWith;
        }
        for ( final Integer rowHeight : sizes.value )
        {
            ps.height += rowHeight;
        }
        final Insets border = parent.getInsets ();
        ps.width += border.left + border.right;
        ps.height += border.top + border.bottom;

        return ps;
    }

    /**
     * Returns actual grid size according to container components amount.
     * Actual grid size is very important for all calculations as it defines the final size of the grid.
     *
     * For example: Layout settings are set to have 5 columns and 5 rows which in total requires 25 components to fill-in the grid.
     * Though there might not be enough components provided to fill the grid, in that case the actual grid size might be less.
     *
     * @param parent group pane
     * @return actual grid size according to container components amount
     */
    public GridSize getActualGridSize ( final Container parent )
    {
        final int count = parent.getComponentCount ();
        if ( orientation == HORIZONTAL )
        {
            return new GridSize ( Math.min ( count, columns ), ( count - 1 ) / columns + 1 );
        }
        else
        {
            return new GridSize ( ( count - 1 ) / rows + 1, Math.min ( count, rows ) );
        }
    }

    /**
     * Returns component at the specified cell.
     *
     * @param parent group pane
     * @param column component column
     * @param row    component row
     * @return component at the specified cell
     */
    public Component getComponentAt ( final Container parent, final int column, final int row )
    {
        final GridSize gridSize = getActualGridSize ( parent );
        final int index = pointToIndex ( parent, column, row, gridSize );
        final int count = parent.getComponentCount ();
        return index < count ? parent.getComponent ( index ) : null;
    }

    /**
     * Returns grid column in which component under the specified index is placed.
     *
     * @param parent   group pane
     * @param index    component index
     * @param gridSize actual grid size
     * @return grid column in which component under the specified index is placed
     */
    public int indexToColumn ( final Container parent, final int index, final GridSize gridSize )
    {
        final boolean ltr = parent.getComponentOrientation ().isLeftToRight ();
        final int column = orientation == HORIZONTAL ? index % columns : index / rows;
        return ltr ? column : gridSize.columns - 1 - column;
    }

    /**
     * Returns grid row in which component under the specified index is placed.
     *
     * @param index component index
     * @return grid row in which component under the specified index is placed
     */
    public int indexToRow ( final int index )
    {
        return orientation == HORIZONTAL ? index / columns : index % rows;
    }

    /**
     * Returns index of the component placed in the specified grid cell or {@code null} if cell is empty.
     *
     * @param parent   group pane
     * @param column   grid column index
     * @param row      grid row index
     * @param gridSize actual grid size
     * @return index of the component placed in the specified grid cell or {@code null} if cell is empty
     */
    public int pointToIndex ( final Container parent, final int column, final int row, final GridSize gridSize )
    {
        final boolean ltr = parent.getComponentOrientation ().isLeftToRight ();
        final int c = ltr ? column : gridSize.columns - 1 - column;
        return orientation == HORIZONTAL ? row * columns + c : c * rows + row;
    }

    /**
     * Returns column and row sizes.
     *
     * @param parent   group pane
     * @param gridSize actual grid size
     * @param type     requested sizes type
     * @return column and row sizes
     */
    protected Pair<int[], int[]> calculateSizes ( final Container parent, final GridSize gridSize, final SizeType type )
    {
        final int count = parent.getComponentCount ();

        // Calculating initially available column and row sizes
        final int cols = gridSize.columns;
        final int[] colWidths = new int[ cols ];
        final double[] colPercents = new double[ cols ];
        final int rows = gridSize.rows;
        final int[] rowHeights = new int[ rows ];
        final double[] rowPercents = new double[ rows ];
        for ( int i = 0; i < count; i++ )
        {
            final Component component = parent.getComponent ( i );
            final GroupPaneConstraints c = constraints.get ( component );
            final Dimension ps = component.getPreferredSize ();

            final int col = indexToColumn ( parent, i, gridSize );
            final int row = indexToRow ( i );

            colWidths[ col ] = Math.max ( colWidths[ col ], ( int ) Math.floor ( c.width > 1 ? c.width : ps.width ) );
            colPercents[ col ] = Math.max ( colPercents[ col ], 1 >= c.width && c.width > 0 ? c.width : 0 );
            rowHeights[ row ] = Math.max ( rowHeights[ row ], ( int ) Math.floor ( c.height > 1 ? c.height : ps.height ) );
            rowPercents[ row ] = Math.max ( rowPercents[ row ], 1 >= c.height && c.height > 0 ? c.height : 0 );
        }

        // Calculating resulting column and row sizes
        final Dimension size = parent.getSize ();
        final Pair<Double, Integer> rc = calculateSizes ( cols, size.width, colWidths, colPercents );
        final Pair<Double, Integer> rr = calculateSizes ( rows, size.height, rowHeights, rowPercents );

        // Updating sizes with current values
        // This block is only performed for actual layout operation
        if ( type == SizeType.current )
        {
            for ( int i = 0; i < count; i++ )
            {
                final int col = indexToColumn ( parent, i, gridSize );
                if ( colPercents[ col ] > 0 && colPercents[ col ] <= 1 )
                {
                    final int pw = ( int ) Math.floor ( rc.getValue () * colPercents[ col ] / rc.getKey () );
                    colWidths[ col ] = Math.max ( pw, colWidths[ col ] );
                }

                final int row = indexToRow ( i );
                if ( rowPercents[ row ] > 0 && rowPercents[ row ] <= 1 )
                {
                    final int ph = ( int ) Math.floor ( rr.getValue () * rowPercents[ row ] / rr.getKey () );
                    rowHeights[ row ] = Math.max ( ph, rowHeights[ row ] );
                }
            }
            appendDelta ( cols, colWidths, size.width );
            appendDelta ( rows, rowHeights, size.height );
        }

        return new Pair<int[], int[]> ( colWidths, rowHeights );
    }

    /**
     * Calculates proper component sizes along with percents summ and free size.
     *
     * @param count    parts count
     * @param size     total available size
     * @param sizes    part sizes
     * @param percents part percentages
     * @return percents summ and free size pair
     */
    protected Pair<Double, Integer> calculateSizes ( final int count, final int size, final int[] sizes, final double[] percents )
    {
        final int[] initSizes = Arrays.copyOf ( sizes, count );
        boolean changed;
        double maxWeight;
        double freePercents;
        int freeSize;
        do
        {
            changed = false;

            // Determining max column and row weights
            maxWeight = 0;
            for ( int i = 0; i < count; i++ )
            {
                if ( percents[ i ] > 0 )
                {
                    maxWeight = Math.max ( maxWeight, initSizes[ i ] / percents[ i ] );
                }
            }

            // Applying column and row weights
            for ( int i = 0; i < count; i++ )
            {
                if ( percents[ i ] > 0 )
                {
                    sizes[ i ] = ( int ) Math.floor ( maxWeight * percents[ i ] );
                }
                else
                {
                    sizes[ i ] = initSizes[ i ];
                }
            }

            // Calculating summary of percent sizes and free pixel size
            freeSize = size;
            freePercents = 0;
            for ( int i = 0; i < count; i++ )
            {
                freeSize -= percents[ i ] == 0 ? sizes[ i ] : 0;
                freePercents += percents[ i ];
            }

            // Normalize percents so that fill parts will be able to take less than 100% of free space
            // So far it have been disabled due to some minor shrinking issues
            // freePercents = Math.max ( 1, freePercents );

            // Stop parts from shrinking below their preferred size
            for ( int i = 0; i < count; i++ )
            {
                if ( percents[ i ] > 0 )
                {
                    final double availSize = freeSize * percents[ i ] / freePercents;
                    if ( sizes[ i ] > availSize && initSizes[ i ] > availSize )
                    {
                        percents[ i ] = 0;
                        changed = true;
                        break;
                    }
                }
            }
        }
        while ( changed );
        return new Pair<Double, Integer> ( freePercents, freeSize );
    }

    /**
     * Appends delta space equally to last elements to properly fill in all available space.
     *
     * @param count parts count
     * @param sizes part sizes
     * @param size  total available size
     */
    protected void appendDelta ( final int count, final int[] sizes, final int size )
    {
        int roughColSize = 0;
        for ( int i = 0; i < count; i++ )
        {
            roughColSize += sizes[ i ];
        }
        int delta = size - roughColSize;
        if ( delta < count )
        {
            for ( int i = count - 1; delta > 0; i--, delta-- )
            {
                sizes[ i ]++;
            }
        }
    }

    @Override
    protected Pair<String, String> getDescriptors ( final Container parent, final Component component, final int index )
    {
        // Retrieving actual grid size
        final GridSize gridSize = getActualGridSize ( parent );

        // Retrieving component position
        final int row = indexToRow ( index );
        final int col = indexToColumn ( parent, index, gridSize );
        final boolean ltr = parent.getComponentOrientation ().isLeftToRight ();

        // Calculating descriptors values
        final boolean paintTop;
        final boolean paintTopLine;
        final boolean paintLeft;
        final boolean paintLeftLine;
        final boolean paintBottom;
        final boolean paintBottomLine;
        final boolean paintRight;
        final boolean paintRightLine;
        if ( isNeighbourDecoratable ( parent, gridSize, col, row, TOP ) )
        {
            paintTop = false;
            paintTopLine = false;
        }
        else if ( !isPaintTop () && row == 0 )
        {
            paintTop = false;
            paintTopLine = false;
        }
        else
        {
            paintTop = true;
            paintTopLine = false;
        }
        if ( isNeighbourDecoratable ( parent, gridSize, col, row, ltr ? LEFT : RIGHT ) )
        {
            paintLeft = false;
            paintLeftLine = false;
        }
        else if ( !isPaintLeft () && col == ( ltr ? 0 : gridSize.columns - 1 ) )
        {
            paintLeft = false;
            paintLeftLine = false;
        }
        else
        {
            paintLeft = true;
            paintLeftLine = false;
        }
        if ( isNeighbourDecoratable ( parent, gridSize, col, row, BOTTOM ) )
        {
            paintBottom = false;
            paintBottomLine = true;
        }
        else if ( !isPaintBottom () && row == gridSize.rows - 1 )
        {
            paintBottom = false;
            paintBottomLine = false;
        }
        else
        {
            paintBottom = true;
            paintBottomLine = false;
        }
        if ( isNeighbourDecoratable ( parent, gridSize, col, row, ltr ? RIGHT : LEFT ) )
        {
            paintRight = false;
            paintRightLine = true;
        }
        else if ( !isPaintRight () && col == ( ltr ? gridSize.columns - 1 : 0 ) )
        {
            paintRight = false;
            paintRightLine = false;
        }
        else
        {
            paintRight = true;
            paintRightLine = true;
        }

        // Returning descriptors
        final String sides = DecorationUtils.toString ( paintTop, paintLeft, paintBottom, paintRight );
        final String lines = DecorationUtils.toString ( paintTopLine, paintLeftLine, paintBottomLine, paintRightLine );
        return new Pair<String, String> ( sides, lines );
    }

    /**
     * Returns whether or not neighbour component painter is decoratable.
     *
     * @param parent    container
     * @param gridSize  actual grid size
     * @param col       current component column
     * @param row       current component row
     * @param direction neighbour direction
     * @return true if neighbour component painter is decoratable, false otherwise
     */
    public boolean isNeighbourDecoratable ( final Container parent, final GridSize gridSize, final int col, final int row,
                                            final int direction )
    {
        final Component neighbour = getNeighbour ( parent, gridSize, col, row, direction );
        return neighbour != null && PainterSupport.isDecoratable ( neighbour );
    }

    /**
     * Returns neighbour component.
     *
     * @param parent    container
     * @param gridSize  actual grid size
     * @param col       current component column
     * @param row       current component row
     * @param direction neighbour direction
     * @return neighbour component
     */
    public Component getNeighbour ( final Container parent, final GridSize gridSize, final int col, final int row, final int direction )
    {
        if ( direction == TOP )
        {
            return row > 0 ? getComponentAt ( parent, col, row - 1 ) : null;
        }
        else if ( direction == LEFT )
        {
            return col > 0 ? getComponentAt ( parent, col - 1, row ) : null;
        }
        else if ( direction == BOTTOM )
        {
            return row < gridSize.rows - 1 ? getComponentAt ( parent, col, row + 1 ) : null;
        }
        else if ( direction == RIGHT )
        {
            return col < gridSize.columns - 1 ? getComponentAt ( parent, col + 1, row ) : null;
        }
        else
        {
            return null;
        }
    }
}