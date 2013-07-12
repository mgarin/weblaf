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

package com.alee.laf.table.renderers;

import com.alee.laf.label.WebLabel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * User: mgarin Date: 31.10.12 Time: 16:02
 */

public class WebTableCellRenderer extends WebLabel implements TableCellRenderer
{
    private Color unselectedForeground;
    private Color unselectedBackground;

    public WebTableCellRenderer ()
    {
        super ();
        setOpaque ( true );
        setMargin ( 2 );
        setName ( "Table.cellRenderer" );
    }

    public void setForeground ( Color c )
    {
        super.setForeground ( c );
        unselectedForeground = c;
    }

    public void setBackground ( Color c )
    {
        super.setBackground ( c );
        unselectedBackground = c;
    }

    public void updateUI ()
    {
        super.updateUI ();
        setForeground ( null );
        setBackground ( null );
    }

    public Component getTableCellRendererComponent ( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column )
    {
        Color fg = null;
        Color bg = null;

        // todo Drop indication
        //        JTable.DropLocation dropLocation = table.getDropLocation ();
        //        if ( dropLocation != null && !dropLocation.isInsertRow () && !dropLocation.isInsertColumn () && dropLocation.getRow () == row &&
        //                dropLocation.getColumn () == column )
        //        {
        //
        //            fg = UIManager.getColor ( "Table.dropCellForeground" );
        //            bg = UIManager.getColor ( "Table.dropCellBackground" );
        //            isSelected = true;
        //        }

        if ( isSelected )
        {
            super.setForeground ( fg == null ? table.getSelectionForeground () : fg );
            super.setBackground ( bg == null ? table.getSelectionBackground () : bg );
        }
        else
        {
            Color background = unselectedBackground != null ? unselectedBackground : table.getBackground ();
            if ( background == null || background instanceof javax.swing.plaf.UIResource )
            {
                Color alternateColor = UIManager.getColor ( "Table.alternateRowColor" );
                if ( alternateColor != null && row % 2 == 0 )
                {
                    background = alternateColor;
                }
            }
            super.setForeground ( unselectedForeground != null ? unselectedForeground : table.getForeground () );
            super.setBackground ( background );
        }

        setFont ( table.getFont () );
        setValue ( value );
        setEnabled ( table.isEnabled () );

        return this;
    }

    public boolean isOpaque ()
    {
        Color back = getBackground ();
        Component p = getParent ();
        if ( p != null )
        {
            p = p.getParent ();
        }

        // p should now be the JTable.
        boolean colorMatch = ( back != null ) && ( p != null ) && back.equals ( p.getBackground () ) && p.isOpaque ();
        return !colorMatch && super.isOpaque ();
    }

    protected void setValue ( Object value )
    {
        setText ( ( value == null ) ? "" : value.toString () );
    }

    public static class UIResource extends WebTableCellRenderer implements javax.swing.plaf.UIResource
    {
    }

    /**
     * Overridden for performance reasons.
     */

    // Doesn't work well on OpenJDK

    //    protected void firePropertyChange ( String propertyName, Object oldValue, Object newValue )
    //    {
    //        if ( propertyName.equals ( "text" ) || propertyName.equals ( "labelFor" ) || propertyName.equals ( "displayedMnemonic" ) ||
    //                ( ( propertyName.equals ( "font" ) || propertyName.equals ( "foreground" ) ) && oldValue != newValue &&
    //                        getClientProperty ( javax.swing.plaf.basic.BasicHTML.propertyKey ) != null ) )
    //        {
    //
    //            super.firePropertyChange ( propertyName, oldValue, newValue );
    //        }
    //    }
    //
    //    public void firePropertyChange ( String propertyName, boolean oldValue, boolean newValue )
    //    {
    //    }

    //    public void invalidate ()
    //    {
    //    }
    //
    //    public void validate ()
    //    {
    //    }
    //
    //    public void revalidate ()
    //    {
    //    }
    //
    //    public void repaint ( long tm, int x, int y, int width, int height )
    //    {
    //    }
    //
    //    public void repaint ( Rectangle r )
    //    {
    //    }
    //
    //    public void repaint ()
    //    {
    //    }
}


