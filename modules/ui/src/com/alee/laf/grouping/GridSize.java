package com.alee.laf.grouping;

/**
 * @author Mikle Garin
 */

public class GridSize
{
    public final int columns;
    public final int rows;

    public GridSize ( final int columns, final int rows )
    {
        this.columns = columns;
        this.rows = rows;
    }

    public int getColumns ()
    {
        return columns;
    }

    public int getRows ()
    {
        return rows;
    }
}