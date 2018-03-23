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

package com.alee.extended.layout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author Mikle Garin
 */

public class WrapFlowLayout extends AbstractLayoutManager implements SwingConstants
{
    /**
     * todo 1. Support for RTL
     * todo 2. Add LEADING/TRAILING alignments
     */

    protected boolean fitWidth = true;
    protected boolean fillWidth = true;
    protected int hgap = 0;
    protected int vgap = 0;
    protected int halign = LEFT;
    protected int valign = TOP;
    protected boolean wrapEachComponent = false;

    protected int maxWidth = 0;
    protected int maxHeight = 0;
    protected ArrayList<RowData> rowsData;

    public WrapFlowLayout ()
    {
        this ( false );
    }

    public WrapFlowLayout ( final boolean fillWidth )
    {
        this ( fillWidth, 0, 0 );
    }

    public WrapFlowLayout ( final int hgap, final int vgap )
    {
        this ( false, hgap, vgap );
    }

    public WrapFlowLayout ( final boolean fillWidth, final int hgap, final int vgap )
    {
        super ();
        this.fillWidth = fillWidth;
        this.hgap = hgap;
        this.vgap = vgap;
    }

    public boolean isFitWidth ()
    {
        return fitWidth;
    }

    public void setFitWidth ( final boolean fitWidth )
    {
        this.fitWidth = fitWidth;
    }

    public boolean isFillWidth ()
    {
        return fillWidth;
    }

    public void setFillWidth ( final boolean fillWidth )
    {
        this.fillWidth = fillWidth;
    }

    public int getHgap ()
    {
        return hgap;
    }

    public void setHgap ( final int hgap )
    {
        this.hgap = hgap;
    }

    public int getVgap ()
    {
        return vgap;
    }

    public void setVgap ( final int vgap )
    {
        this.vgap = vgap;
    }

    public int getHalign ()
    {
        return halign;
    }

    public void setHalign ( final int halign )
    {
        this.halign = halign;
    }

    public int getValign ()
    {
        return valign;
    }

    public void setValign ( final int valign )
    {
        this.valign = valign;
    }

    public boolean isWrapEachComponent ()
    {
        return wrapEachComponent;
    }

    public void setWrapEachComponent ( final boolean wrapEachComponent )
    {
        this.wrapEachComponent = wrapEachComponent;
    }

    public int getMaxWidth ()
    {
        return maxWidth;
    }

    public int getMaxHeight ()
    {
        return maxHeight;
    }

    public ArrayList<RowData> getRowsData ()
    {
        return rowsData;
    }

    @Override
    public Dimension preferredLayoutSize ( final Container container )
    {
        layoutContainer ( container );
        return new Dimension ( maxWidth, maxHeight );
    }

    @Override
    public Dimension minimumLayoutSize ( final Container container )
    {
        layoutContainer ( container );
        return new Dimension ( 0, maxHeight );
    }

    @Override
    public void layoutContainer ( final Container container )
    {
        // Ignore if no children
        if ( container.getComponentCount () == 0 )
        {
            maxWidth = 0;
            maxHeight = 0;
            rowsData = new ArrayList<RowData> ( 0 );
            return;
        }

        // Parent properties
        final Insets insets = container.getInsets ();
        final int parentWidth = container.getWidth () - insets.left - insets.right;

        // Current row
        int currentRow = 0;

        // Current processed row component number
        int componentInRow = 0;

        // Total width & height
        maxWidth = 0;
        maxHeight = 0;

        // Current processed row width & height
        int currentRowWidth = 0;
        int currentRowMaxHeight = 0;

        // Computing rows and components
        rowsData = new ArrayList<RowData> ();
        for ( int i = 0; i < container.getComponentCount (); i++ )
        {
            final Component component = container.getComponent ( i );
            final Dimension ps = component.getPreferredSize ();
            if ( componentInRow > 0 && ( isWrapEachComponent () || currentRowWidth + hgap + ps.width > parentWidth ) )
            {
                // Saving row settings
                rowsData.get ( currentRow ).setWidth ( currentRowWidth );
                rowsData.get ( currentRow ).setHeight ( currentRowMaxHeight );

                // Saving max sizes
                maxWidth = Math.max ( maxWidth, currentRowWidth );
                maxHeight += ( currentRow > 0 ? vgap : 0 ) + currentRowMaxHeight;

                componentInRow = 0;
                currentRowWidth = 0;
                currentRowMaxHeight = 0;
                currentRow++;
            }

            final int componentWidth = fitWidth ? Math.min ( ps.width, parentWidth ) : ps.width;
            currentRowWidth += ( componentInRow > 0 ? hgap : 0 ) + componentWidth;
            currentRowMaxHeight = Math.max ( currentRowMaxHeight, ps.height );
            componentInRow++;

            if ( currentRow >= rowsData.size () )
            {
                rowsData.add ( new RowData () );
            }
            rowsData.get ( currentRow ).addComponent ( component );
        }
        rowsData.get ( currentRow ).setWidth ( currentRowWidth );
        rowsData.get ( currentRow ).setHeight ( currentRowMaxHeight );
        maxHeight += ( currentRow > 0 ? vgap : 0 ) + currentRowMaxHeight;
        maxHeight += insets.top + insets.bottom;

        // Layouting components
        int x;
        int y = getStartY ( container, insets );
        for ( final RowData row : rowsData )
        {
            x = getStartX ( container, insets, row );
            int i = 0;
            for ( final Component component : row.getComponents () )
            {
                final Dimension ps = component.getPreferredSize ();

                int componentWidth = fitWidth ? Math.min ( ps.width, parentWidth ) : ps.width;
                if ( fillWidth && i + 1 == row.getComponents ().size () && halign == LEFT )
                {
                    componentWidth = insets.left + parentWidth - x;
                }
                component.setBounds ( x, y, componentWidth, row.getHeight () );

                x += componentWidth + hgap;
                i++;
            }
            y += row.getHeight () + vgap;
        }
    }

    protected int getStartX ( final Container container, final Insets insets, final RowData row )
    {
        final int x;
        if ( fillWidth || halign == LEFT )
        {
            x = insets.left;
        }
        else if ( halign == RIGHT )
        {
            x = container.getWidth () - insets.right - row.getWidth ();
        }
        else
        {
            x = container.getWidth () / 2 - row.getWidth () / 2;
        }
        return x;
    }

    protected int getStartY ( final Container container, final Insets insets )
    {
        final int y;
        if ( valign == TOP )
        {
            y = insets.top;
        }
        else if ( valign == BOTTOM )
        {
            y = container.getHeight () - insets.bottom - maxHeight;
        }
        else
        {
            y = insets.top + container.getHeight () / 2 - maxHeight / 2;
        }
        return y;
    }
}