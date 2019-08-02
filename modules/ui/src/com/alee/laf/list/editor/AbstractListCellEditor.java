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

package com.alee.laf.list.editor;

import com.alee.api.jdk.Objects;
import com.alee.laf.list.WebList;
import com.alee.laf.list.WebListModel;
import com.alee.managers.hotkey.Hotkey;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * An abstract list cell editor that provides basic method implementations for list cell editor creation.
 *
 * @param <C> Editor component type
 * @param <T> Editor value type
 * @author Mikle Garin
 */
public abstract class AbstractListCellEditor<C extends Component, T> implements ListCellEditor<C, T>
{
    /**
     * Last edited cell index.
     */
    protected int editedCell = -1;

    /**
     * Old value from the edited cell.
     */
    protected T oldValue;

    /**
     * Currently active editor.
     */
    protected C editor;

    /**
     * List resize adapter.
     */
    protected ComponentAdapter editorPositionUpdater;

    /**
     * List mouse adapter.
     */
    protected MouseAdapter mouseAdapter = null;

    /**
     * Amount of mouse clicks required to start editing cell.
     */
    protected int clicksToEdit = 2;

    /**
     * List key adapter.
     */
    protected KeyAdapter keyAdapter = null;

    @Override
    public void install ( final JList list )
    {
        // Installing edit bounds updater
        editorPositionUpdater = new ComponentAdapter ()
        {
            @Override
            public void componentResized ( final ComponentEvent e )
            {
                checkEditorBounds ();
            }

            /**
             * Validates list editor bounds.
             */
            private void checkEditorBounds ()
            {
                if ( isEditing () )
                {
                    final Rectangle newBounds = getEditorBounds ( list, editedCell, oldValue );
                    if ( newBounds != null && !newBounds.equals ( editor.getBounds () ) )
                    {
                        editor.setBounds ( newBounds );
                        list.revalidate ();
                        list.repaint ();
                    }
                }
            }
        };
        list.addComponentListener ( editorPositionUpdater );

        // Installing start edit actions in the list
        installStartEditActions ( list );
    }

    /**
     * Installs start edit actions in the list.
     *
     * @param list list to process
     */
    protected void installStartEditActions ( final JList list )
    {
        mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mouseClicked ( final MouseEvent e )
            {
                if ( getClicksToEdit () > 0 && e.getClickCount () == getClicksToEdit () && SwingUtils.isLeftMouseButton ( e ) )
                {
                    final Point point = e.getPoint ();
                    final int index = list.getUI ().locationToIndex ( list, point );
                    if ( index >= 0 && index < list.getModel ().getSize () )
                    {
                        final Rectangle cell = list.getCellBounds ( index, index );
                        if ( cell.contains ( point ) )
                        {
                            startEdit ( list, index );
                        }
                    }
                }
            }
        };
        list.addMouseListener ( mouseAdapter );

        keyAdapter = new KeyAdapter ()
        {
            @Override
            public void keyReleased ( final KeyEvent e )
            {
                if ( Hotkey.F2.isTriggered ( e ) )
                {
                    startEdit ( list, list.getSelectedIndex () );
                }
            }
        };
        list.addKeyListener ( keyAdapter );
    }

    @Override
    public void uninstall ( final JList list )
    {
        // Uninstalling edit bounds updater
        list.removeComponentListener ( editorPositionUpdater );

        // Uninstalling start edit actions from the list
        uninstallStartEditActions ( list );
    }

    /**
     * Uninstalls start edit actions from the list.
     *
     * @param list list to process
     */
    protected void uninstallStartEditActions ( final JList list )
    {
        if ( mouseAdapter != null )
        {
            list.removeMouseListener ( mouseAdapter );
        }
        if ( keyAdapter != null )
        {
            list.removeKeyListener ( keyAdapter );
        }
    }

    @Override
    public boolean isCellEditable ( final JList list, final int index, final T value )
    {
        if ( list instanceof WebList )
        {
            return ( ( WebList ) list ).isEditable () && list.isEnabled ();
        }
        else
        {
            return list.isEnabled ();
        }
    }

    @Override
    public C getCellEditor ( final JList list, final int index, final T value )
    {
        editor = createCellEditor ( list, index, value );
        return editor;
    }

    /**
     * Creates list cell editor component for the cell nder specified index.
     *
     * @param list  list to process
     * @param index cell index
     * @param value cell value
     * @return list cell editor created for the cell under specified index
     */
    protected abstract C createCellEditor ( JList list, int index, T value );

    @Override
    public void startEdit ( final JList list, final int index )
    {
        // Checking that selection is not empty
        if ( index == -1 )
        {
            return;
        }

        // Checking if selected cell is editable
        oldValue = ( T ) list.getModel ().getElementAt ( index );
        if ( !isCellEditable ( list, index, oldValue ) )
        {
            oldValue = null;
            return;
        }

        // Creating cell editor for the selected cell
        editor = getCellEditor ( list, index, oldValue );

        // Updating initial cell editor bounds in the list
        editor.setBounds ( getEditorBounds ( list, index, oldValue ) );

        // Adding cell editor onto the list
        addEditor ( list );

        // Requesting focus into the cell editor
        if ( editor.isFocusable () )
        {
            editor.requestFocusInWindow ();
        }

        // Notifying about editing start
        editStarted ( list, index );
    }

    @Override
    public void cancelEdit ( final JList list )
    {
        // Removing cell editor from the list
        removeEditor ( list );

        // Notifying about editing cancel
        editCancelled ( list, editedCell );
    }

    @Override
    public boolean stopEdit ( final JList list )
    {
        if ( !isEditing () )
        {
            return false;
        }

        // Saving selected indices to restore them later
        final int[] indices = list.getSelectedIndices ();

        // Checking whether value has changed or not
        final T newValue = getCellEditorValue ( list, editedCell, oldValue );
        final boolean changed = updateListModel ( list, editedCell, oldValue, newValue, true );

        // Removing cell editor from the list
        removeEditor ( list );

        // Firing either edit stopped or cancelled
        if ( changed )
        {
            // Restoring selected indices
            list.setSelectedIndices ( indices );

            // Notifying about editing stop
            editStopped ( list, editedCell, oldValue, newValue );
        }
        else
        {
            // Notifying about editing cancel
            editCancelled ( list, editedCell );
        }

        return true;
    }

    /**
     * Adds cell editor into the list.
     *
     * @param list list to process
     */
    protected void addEditor ( final JList list )
    {
        list.add ( editor );
        list.revalidate ();
        list.repaint ();
    }

    /**
     * Removes cell editor from the list.
     *
     * @param list list to process
     */
    protected void removeEditor ( final JList list )
    {
        list.remove ( editor );
        list.revalidate ();
        list.repaint ();
    }

    /**
     * Returns list cell editor bounds within the cell.
     *
     * @param list       list to process
     * @param index      cell index
     * @param value      cell value
     * @param cellBounds cell bounds
     * @return list cell editor bounds within the list
     */
    protected Rectangle getEditorBounds ( final JList list, final int index, final T value, final Rectangle cellBounds )
    {
        return new Rectangle ( 0, 0, cellBounds.width, cellBounds.height );
    }

    /**
     * Returns list cell editor bounds within the list.
     *
     * @param list  list to process
     * @param index cell index
     * @param value cell value
     * @return cell editor bounds for the cell under the specified index
     */
    protected Rectangle getEditorBounds ( final JList list, final int index, final T value )
    {
        final Rectangle cellBounds = list.getCellBounds ( index, index );
        if ( cellBounds != null )
        {
            final Rectangle editorBounds = getEditorBounds ( list, index, value, cellBounds );
            return new Rectangle ( cellBounds.x + editorBounds.x, cellBounds.y + editorBounds.y, editorBounds.width, editorBounds.height );
        }
        else
        {
            return null;
        }
    }

    @Override
    public boolean updateListModel ( final JList list, final int index, final T oldValue, final T newValue, final boolean updateSelection )
    {
        // Checking if value has changed
        if ( Objects.equals ( oldValue, newValue ) )
        {
            return false;
        }

        // Updating list model
        final ListModel model = list.getModel ();
        if ( model instanceof WebListModel )
        {
            final WebListModel webListModel = ( WebListModel ) model;
            webListModel.set ( index, newValue );
            return true;
        }
        else if ( model instanceof DefaultListModel )
        {
            final DefaultListModel defaultListModel = ( DefaultListModel ) model;
            defaultListModel.setElementAt ( newValue, index );
            return true;
        }
        else if ( model instanceof AbstractListModel )
        {
            final ArrayList<Object> values = new ArrayList<Object> ( model.getSize () );
            for ( int i = 0; i < model.getSize (); i++ )
            {
                if ( editedCell != i )
                {
                    values.set ( i, model.getElementAt ( i ) );
                }
                else
                {
                    values.set ( i, newValue );
                }
            }
            list.setModel ( new AbstractListModel ()
            {
                @Override
                public int getSize ()
                {
                    return values.size ();
                }

                @Override
                public Object getElementAt ( final int index )
                {
                    return values.get ( index );
                }
            } );
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void editStarted ( final JList list, final int index )
    {
        editedCell = index;
        if ( list instanceof WebList )
        {
            final WebList webList = ( WebList ) list;
            webList.fireEditStarted ( index );
        }
    }

    @Override
    public void editStopped ( final JList list, final int index, final T oldValue, final T newValue )
    {
        editedCell = -1;
        if ( list instanceof WebList )
        {
            final WebList webList = ( WebList ) list;
            webList.fireEditFinished ( index, oldValue, newValue );
        }
    }

    @Override
    public void editCancelled ( final JList list, final int index )
    {
        editedCell = -1;
        if ( list instanceof WebList )
        {
            final WebList webList = ( WebList ) list;
            webList.fireEditCancelled ( index );
        }
    }

    @Override
    public boolean isEditing ()
    {
        return editedCell != -1;
    }

    /**
     * Returns amount of mouse clicks required to start editing cell.
     *
     * @return amount of mouse clicks required to start editing cell
     */
    public int getClicksToEdit ()
    {
        return clicksToEdit;
    }

    /**
     * Sets amount of mouse clicks required to start editing cell.
     *
     * @param clicksToEdit amount of mouse clicks required to start editing cell
     */
    public void setClicksToEdit ( final int clicksToEdit )
    {
        this.clicksToEdit = clicksToEdit;
    }
}