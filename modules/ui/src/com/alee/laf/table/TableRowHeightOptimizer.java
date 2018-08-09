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

import com.alee.extended.behavior.AbstractComponentBehavior;
import com.alee.extended.behavior.Behavior;
import com.alee.laf.WebLookAndFeel;
import com.alee.utils.CoreSwingUtils;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * {@link Behavior} that adjusts {@link WebTable} row height on the fly according to table data.
 *
 * Note that this behavior will not cover all possible cell sizes as it will only use a small chunk of {@link TableModel} data to test
 * {@link TableCellRenderer} preferred size, otherwise we are risking to hit various issues with {@link TableModel}s of large size.
 *
 * @author Mikle Garin
 */
public class TableRowHeightOptimizer extends AbstractComponentBehavior<JTable> implements PropertyChangeListener, TableModelListener
{
    /**
     * Initial row height of the {@link WebTable}.
     */
    protected int initialRowHeight;

    /**
     * Constructs new {@link TableRowHeightOptimizer}.
     *
     * @param table {@link WebTable} this behavior is attached to
     */
    public TableRowHeightOptimizer ( final JTable table )
    {
        super ( table );
    }

    /**
     * Installs this {@link TableRowHeightOptimizer} into the {@link #component}.
     */
    public void install ()
    {
        initialRowHeight = component.getRowHeight ();
        if ( component.getModel () != null )
        {
            component.getModel ().addTableModelListener ( this );
        }
        component.addPropertyChangeListener ( WebLookAndFeel.MODEL_PROPERTY, this );
    }

    /**
     * Uninstalls this {@link TableRowHeightOptimizer} from the {@link #component}.
     */
    public void uninstall ()
    {
        component.removePropertyChangeListener ( WebLookAndFeel.MODEL_PROPERTY, this );
        if ( component.getModel () != null )
        {
            component.getModel ().removeTableModelListener ( this );
        }
        initialRowHeight = 0;
    }

    @Override
    public void propertyChange ( final PropertyChangeEvent event )
    {
        final TableModel oldModel = ( TableModel ) event.getOldValue ();
        if ( oldModel != null )
        {
            oldModel.removeTableModelListener ( this );
        }
        final TableModel newModel = ( TableModel ) event.getNewValue ();
        if ( newModel != null )
        {
            newModel.addTableModelListener ( this );
        }
    }

    @Override
    public void tableChanged ( final TableModelEvent event )
    {
        /**
         * This call must be made later due to the way {@link javax.swing.table.TableRowSorter} is implemented.
         * Otherwise this listener will receive outdated information from the methods that go to the sorter instead of the model.
         */
        CoreSwingUtils.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                int maxHeight = initialRowHeight;
                if ( component.getColumnCount () > 0 )
                {
                    final TableModel model = component.getModel ();
                    if ( model.getRowCount () > 0 )
                    {
                        final Rectangle vr = component.getVisibleRect ();
                        if ( vr.width > 0 && vr.height > 0 )
                        {
                            final boolean ltr = component.getComponentOrientation ().isLeftToRight ();
                            final Point upperLeft = new Point ( ltr ? vr.x + 1 : vr.x + vr.width - 1, vr.y + 1 );
                            final Point lowerLeft = new Point ( ltr ? vr.x + 1 : vr.x + vr.width - 1, vr.y + vr.height - 1 );
                            final Point upperRight = new Point ( ltr ? vr.x + vr.width - 1 : vr.x + 1, vr.y + 1 );
                            final int rMin = Math.max ( 0, component.rowAtPoint ( upperLeft ) );
                            final int rMax = Math.min ( component.getRowCount () - 1, component.rowAtPoint ( lowerLeft ) );
                            final int cMin = Math.max ( 0, component.columnAtPoint ( upperLeft ) );
                            final int cMax = Math.min ( component.getColumnCount () - 1, component.columnAtPoint ( upperRight ) );
                            for ( int row = rMin; row <= rMax; row++ )
                            {
                                for ( int col = cMin; col < cMax; col++ )
                                {
                                    final TableCellRenderer cellRenderer = component.getCellRenderer ( row, col );
                                    final Component renderer = component.prepareRenderer ( cellRenderer, row, col );
                                    final Dimension ps = renderer.getPreferredSize ();
                                    maxHeight = Math.max ( maxHeight, ps.height );
                                }
                            }
                        }
                        else
                        {
                            for ( int col = 0; col < component.getColumnCount (); col++ )
                            {
                                final TableCellRenderer cellRenderer = component.getCellRenderer ( 0, col );
                                final Component renderer = component.prepareRenderer ( cellRenderer, 0, col );
                                final Dimension ps = renderer.getPreferredSize ();
                                maxHeight = Math.max ( maxHeight, ps.height );
                            }
                        }
                    }
                }
                if ( maxHeight != component.getRowHeight () )
                {
                    component.setRowHeight ( maxHeight );
                }
            }
        } );
    }
}