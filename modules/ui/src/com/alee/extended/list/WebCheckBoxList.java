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

package com.alee.extended.list;

import com.alee.laf.list.WebList;
import com.alee.laf.list.editor.ListCellEditor;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.style.StyleId;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * This component offers a checkbox list functionality.
 * Each cell acts as a separate checkbox, but it could be selected like the elements in simple list.
 * You can also perform mass check/uncheck operations by selection more than one checkbox and pressing space.
 *
 * @author Mikle Garin
 */

public class WebCheckBoxList extends WebList
{
    /**
     * Whether checkbox selection should be performed only when user clicks exactly on the check icon or not.
     */
    protected boolean checkOnIconOnly = true;

    /**
     * Constructs empty checkbox list.
     */
    public WebCheckBoxList ()
    {
        this ( StyleId.checkboxlist, new CheckBoxListModel () );
    }

    /**
     * Constructs checkbox list with a specified model.
     *
     * @param model checkbox list model
     */
    public WebCheckBoxList ( final CheckBoxListModel model )
    {
        this ( StyleId.checkboxlist, model );
    }

    /**
     * Constructs empty checkbox list.
     *
     * @param id style ID
     */
    public WebCheckBoxList ( final StyleId id )
    {
        this ( id, new CheckBoxListModel () );
    }

    /**
     * Constructs checkbox list with a specified model.
     *
     * @param id    style ID
     * @param model checkbox list model
     */
    public WebCheckBoxList ( final StyleId id, final CheckBoxListModel model )
    {
        super ( id, model );

        // Custom checkbox list cell renderer
        setCellRenderer ( new WebCheckBoxListCellRenderer () );

        // Checkbox selection change listeners
        addMouseListener ( new CheckBoxListMouseAdapter () );
        addKeyListener ( new CheckBoxListKeyAdapter () );
    }

    /**
     * Returns custom checkbox list cell renderer.
     *
     * @return custom checkbox list cell renderer
     */
    public WebCheckBoxListCellRenderer getWebCheckBoxListCellRenderer ()
    {
        final ListCellRenderer renderer = getCellRenderer ();
        return renderer instanceof WebCheckBoxListCellRenderer ? ( WebCheckBoxListCellRenderer ) renderer : null;
    }

    /**
     * Returns custom checkbox list cell model.
     *
     * @return custom checkbox list cell model
     */
    public CheckBoxListModel getCheckBoxListModel ()
    {
        return ( CheckBoxListModel ) getModel ();
    }

    /**
     * Returns default list cell editor for this list.
     *
     * @return default list cell editor for this list
     */
    @Override
    protected ListCellEditor createDefaultCellEditor ()
    {
        return new WebCheckBoxListCellEditor ();
    }

    /**
     * Returns whether checkbox under the specified cell index is selected or not.
     *
     * @param index cell index
     * @return true if checkbox at the specified cell index is selected, false otherwise
     */
    public boolean isCheckBoxSelected ( final int index )
    {
        return getCheckBoxListModel ().isCheckBoxSelected ( index );
    }

    /**
     * Inverts checkbox selection at the specified cell index.
     *
     * @param index cell index
     */
    public void invertCheckBoxSelection ( final int index )
    {
        getCheckBoxListModel ().invertCheckBoxSelection ( index );
        performAnimation ( index );
    }

    /**
     * Sets whether checkbox at the specified cell index is selected or not.
     *
     * @param index    cell index
     * @param selected whether checkbox is selected or not
     */
    public void setCheckBoxSelected ( final int index, final boolean selected )
    {
        if ( getCheckBoxListModel ().setCheckBoxSelected ( index, selected ) )
        {
            performAnimation ( index );
        }
    }

    /**
     * Returns list of values from checked cells.
     *
     * @return list of values from checked cells
     */
    public List<Object> getCheckedValues ()
    {
        return getCheckBoxListModel ().getCheckedValues ();
    }

    /**
     * Performs required list repaints to support checkbox animation.
     *
     * @param index cell index
     */
    protected void performAnimation ( final int index )
    {
        // todo Replace with animated setting in list?
        // todo Replace with icon change listener in renderer component
        //        if ( /*WebCheckBoxStyle.animated*/true )
        //        {
        //            //            // For checkbox proper animation
        //            //            WebTimer.repeat ( "WebCheckBoxList.animator", WebCheckBoxPainter.UPDATE_DELAY, new ActionListener ()
        //            //            {
        //            //                private int left = WebCheckBoxPainter.MAX_DARKNESS + 1;
        //            //
        //            //                @Override
        //            //                public void actionPerformed ( final ActionEvent e )
        //            //                {
        //            //                    if ( left > 0 )
        //            //                    {
        //            //                        repaint ( getCellBounds ( index, index ) );
        //            //                        left--;
        //            //                    }
        //            //                    else
        //            //                    {
        //            //                        ( ( WebTimer ) e.getSource () ).stop ();
        //            //                    }
        //            //                }
        //            //            } );
        //        }
        //        else
        //        {
        //            repaint ( getCellBounds ( index, index ) );
        //        }
    }

    /**
     * Returns whether checkbox selection should be performed only when user clicks exactly on the check icon or not.
     *
     * @return true if checkbox selection should be performed only when user clicks exactly on the check icon, false otherwise
     */
    public boolean isCheckOnIconOnly ()
    {
        return checkOnIconOnly;
    }

    /**
     * Sets whether checkbox selection should be performed only when user clicks exactly on the check icon or not.
     *
     * @param checkOnIconOnly whether checkbox selection should be performed only when user clicks exactly on the check icon or not
     */
    public void setCheckOnIconOnly ( final boolean checkOnIconOnly )
    {
        this.checkOnIconOnly = checkOnIconOnly;
    }

    /**
     * Custom checkbox list mouse adapter that handles checkbox selection changes.
     */
    protected class CheckBoxListMouseAdapter extends MouseAdapter
    {
        @Override
        public void mousePressed ( final MouseEvent e )
        {
            final WebCheckBoxList list = WebCheckBoxList.this;
            final int index = getUI ().locationToIndex ( list, e.getPoint () );
            if ( index != -1 && list.isEnabled () )
            {
                if ( checkOnIconOnly )
                {
                    final WebCheckBoxListCellRenderer renderer = getWebCheckBoxListCellRenderer ();
                    final WebCheckBoxListElement element = renderer.getElement ( list, getCheckBoxListModel ().get ( index ) );
                    final Rectangle cellRect = getWebUI ().getCellBounds ( list, index, index );
                    final Rectangle iconRect = element.getWebUI ().getIconRect ();
                    iconRect.x += cellRect.x;
                    iconRect.y += cellRect.y;
                    if ( iconRect.contains ( e.getPoint () ) )
                    {
                        invertCheckBoxSelection ( index );
                    }
                }
                else
                {
                    invertCheckBoxSelection ( index );
                }
            }
        }
    }

    /**
     * Custom checkbox list key adapter that handles checkbox selection changes.
     */
    protected class CheckBoxListKeyAdapter extends KeyAdapter
    {
        @Override
        public void keyReleased ( final KeyEvent e )
        {
            if ( Hotkey.SPACE.isTriggered ( e ) && getSelectedIndex () != -1 && isEnabled () )
            {
                for ( final int index : getSelectedIndices () )
                {
                    invertCheckBoxSelection ( index );
                }
            }
        }
    }
}