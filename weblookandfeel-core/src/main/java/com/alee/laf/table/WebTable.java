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
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.FontMethods;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.Vector;

/**
 * User: mgarin Date: 07.07.11 Time: 17:55
 */

public class WebTable extends JTable implements FontMethods<WebTable>
{
    private boolean editable = true;
    private int visibleRowCount = -1;

    public WebTable ()
    {
        super ();
    }

    public WebTable ( TableModel dm )
    {
        super ( dm );
    }

    public WebTable ( TableModel dm, TableColumnModel cm )
    {
        super ( dm, cm );
    }

    public WebTable ( TableModel dm, TableColumnModel cm, ListSelectionModel sm )
    {
        super ( dm, cm, sm );
    }

    public WebTable ( int numRows, int numColumns )
    {
        super ( numRows, numColumns );
    }

    public WebTable ( Vector rowData, Vector columnNames )
    {
        super ( rowData, columnNames );
    }

    public WebTable ( Object[][] rowData, Object[] columnNames )
    {
        super ( rowData, columnNames );
    }

    public boolean isCellEditable ( int row, int column )
    {
        return editable && super.isCellEditable ( row, column );
    }

    public boolean isEditable ()
    {
        return editable;
    }

    public void setEditable ( boolean editable )
    {
        this.editable = editable;
    }

    public void setVisibleRowCount ( int visibleRowCount )
    {
        this.visibleRowCount = visibleRowCount;

        // Reset preferred viewport size
        setPreferredScrollableViewportSize ( null );

        // Update viewport size
        JScrollPane scrollPane = SwingUtils.getScrollPane ( this );
        if ( scrollPane != null )
        {
            scrollPane.getViewport ().invalidate ();
        }
    }

    public int getVisibleRowCount ()
    {
        return visibleRowCount;
    }

    public Dimension getPreferredScrollableViewportSize ()
    {
        if ( preferredViewportSize != null )
        {
            return preferredViewportSize;
        }

        final Dimension ps = getPreferredSize ();
        if ( visibleRowCount != -1 )
        {
            int rowHeight;
            if ( getRowCount () > 0 )
            {
                Rectangle r = getCellRect ( 0, 0, true );
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

    protected void initializeLocalVars ()
    {
        super.initializeLocalVars ();
        setPreferredScrollableViewportSize ( null );
    }

    public WebTableUI getWebUI ()
    {
        return ( WebTableUI ) getUI ();
    }

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
            catch ( Throwable e )
            {
                e.printStackTrace ();
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
        Container p = getParent ();
        if ( p instanceof JViewport )
        {
            Container gp = p.getParent ();
            if ( gp instanceof JScrollPane )
            {
                JScrollPane scrollPane = ( JScrollPane ) gp;
                // Make certain we are the viewPort's view and not, for
                // example, the rowHeaderView of the scrollPane -
                // an implementor of fixed columns might do this.
                JViewport viewport = scrollPane.getViewport ();
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
                    Object componentClass = UIManager.get ( "Table.scrollPaneCornerComponent" );
                    if ( componentClass instanceof Class )
                    {
                        try
                        {
                            corner = ( Component ) ( ( Class ) componentClass ).newInstance ();
                        }
                        catch ( Exception e )
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
    public WebTable setPlainFont ()
    {
        return SwingUtils.setPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public boolean isPlainFont ()
    {
        return SwingUtils.isPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebTable setBoldFont ()
    {
        return SwingUtils.setBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public boolean isBoldFont ()
    {
        return SwingUtils.isBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebTable setItalicFont ()
    {
        return SwingUtils.setItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public boolean isItalicFont ()
    {
        return SwingUtils.isItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebTable setFontStyle ( boolean bold, boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    public WebTable setFontStyle ( int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    /**
     * {@inheritDoc}
     */
    public WebTable setFontSize ( int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    /**
     * {@inheritDoc}
     */
    public WebTable changeFontSize ( int change )
    {
        return SwingUtils.changeFontSize ( this, change );
    }

    /**
     * {@inheritDoc}
     */
    public int getFontSize ()
    {
        return SwingUtils.getFontSize ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebTable setFontSizeAndStyle ( int fontSize, boolean bold, boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    public WebTable setFontSizeAndStyle ( int fontSize, int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    /**
     * {@inheritDoc}
     */
    public WebTable setFontName ( String fontName )
    {
        return SwingUtils.setFontName ( this, fontName );
    }

    /**
     * {@inheritDoc}
     */
    public String getFontName ()
    {
        return SwingUtils.getFontName ( this );
    }
}