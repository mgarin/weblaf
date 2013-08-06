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
 * User: mgarin Date: 07.10.11 Time: 15:14
 */

public class WrapFlowLayout implements LayoutManager, SwingConstants
{
    // todo support RTL + LEADING/TRAILING support

    private boolean fitWidth = true;
    private boolean fillWidth = true;
    private int hgap = 0;
    private int vgap = 0;
    private int halign = LEFT;
    private int valign = TOP;
    private boolean wrapEachComponent = false;

    private int maxWidth = 0;
    private int maxHeight = 0;
    private ArrayList<RowData> rowsData;

    public WrapFlowLayout ()
    {
        this ( false );
    }

    public WrapFlowLayout ( boolean fillWidth )
    {
        this ( fillWidth, 0, 0 );
    }

    public WrapFlowLayout ( int hgap, int vgap )
    {
        this ( false, hgap, vgap );
    }

    public WrapFlowLayout ( boolean fillWidth, int hgap, int vgap )
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

    public void setFitWidth ( boolean fitWidth )
    {
        this.fitWidth = fitWidth;
    }

    public boolean isFillWidth ()
    {
        return fillWidth;
    }

    public void setFillWidth ( boolean fillWidth )
    {
        this.fillWidth = fillWidth;
    }

    public int getHgap ()
    {
        return hgap;
    }

    public void setHgap ( int hgap )
    {
        this.hgap = hgap;
    }

    public int getVgap ()
    {
        return vgap;
    }

    public void setVgap ( int vgap )
    {
        this.vgap = vgap;
    }

    public int getHalign ()
    {
        return halign;
    }

    public void setHalign ( int halign )
    {
        this.halign = halign;
    }

    public int getValign ()
    {
        return valign;
    }

    public void setValign ( int valign )
    {
        this.valign = valign;
    }

    public boolean isWrapEachComponent ()
    {
        return wrapEachComponent;
    }

    public void setWrapEachComponent ( boolean wrapEachComponent )
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

    public void addLayoutComponent ( String name, Component comp )
    {
        //
    }

    public void removeLayoutComponent ( Component comp )
    {
        //
    }

    public void layoutContainer ( Container parent )
    {
        // Ignore if no childs
        if ( parent.getComponentCount () == 0 )
        {
            return;
        }

        // Parent properties
        Insets insets = parent.getInsets ();
        int parentWidth = parent.getWidth () - insets.left - insets.right;

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
        for ( int i = 0; i < parent.getComponentCount (); i++ )
        {
            Component component = parent.getComponent ( i );
            Dimension ps = component.getPreferredSize ();
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

            int componentWidth = fitWidth ? Math.min ( ps.width, parentWidth ) : ps.width;
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
        int y = getStartY ( parent, insets );
        for ( RowData row : rowsData )
        {
            x = getStartX ( parent, insets, row );
            int i = 0;
            for ( Component component : row.getComponents () )
            {
                Dimension ps = component.getPreferredSize ();

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

    private int getStartX ( Container parent, Insets insets, RowData row )
    {
        int x;
        if ( fillWidth || halign == LEFT )
        {
            x = insets.left;
        }
        else if ( halign == RIGHT )
        {
            x = parent.getWidth () - insets.right - row.getWidth ();
        }
        else
        {
            x = parent.getWidth () / 2 - row.getWidth () / 2;
        }
        return x;
    }

    private int getStartY ( Container parent, Insets insets )
    {
        int y;
        if ( valign == TOP )
        {
            y = insets.top;
        }
        else if ( valign == BOTTOM )
        {
            y = parent.getHeight () - insets.bottom - maxHeight;
        }
        else
        {
            y = insets.top + parent.getHeight () / 2 - maxHeight / 2;
        }
        return y;
    }

    public Dimension minimumLayoutSize ( Container parent )
    {
        layoutContainer ( parent );
        return new Dimension ( 0, maxHeight );
    }

    public Dimension preferredLayoutSize ( Container parent )
    {
        layoutContainer ( parent );
        return new Dimension ( maxWidth, maxHeight );
    }
}