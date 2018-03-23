package com.alee.laf.grouping;

/**
 * Simple object representing {@link GroupPaneLayout} grid size.
 *
 * @author Mikle Garin
 */

public class GridSize
{
    /**
     * Grid columns count.
     */
    public final int columns;

    /**
     * Grid rows count.
     */
    public final int rows;

    /**
     * Constructs new {@link GridSize}.
     *
     * @param columns grid columns count
     * @param rows    grid rows count
     */
    public GridSize ( final int columns, final int rows )
    {
        this.columns = columns;
        this.rows = rows;
    }

    /**
     * Returns grid columns count.
     *
     * @return grid columns count
     */
    public int getColumns ()
    {
        return columns;
    }

    /**
     * Returns grid rows count.
     *
     * @return grid rows count
     */
    public int getRows ()
    {
        return rows;
    }
}