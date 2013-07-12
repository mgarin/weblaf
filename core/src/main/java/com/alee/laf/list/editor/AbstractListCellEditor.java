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

import com.alee.laf.list.WebList;
import com.alee.managers.hotkey.Hotkey;
import com.alee.utils.CompareUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * An abstract list cell editor that provides basic method implementations for list cell editor creation.
 *
 * @param <E> Editor component type
 * @param <T> Editor value type
 * @author Mikle Garin
 * @since 1.3
 */

public abstract class AbstractListCellEditor<E extends Component, T> implements ListCellEditor<E, T>
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
    protected E editor;

    /**
     * List resize adapter.
     */
    protected ComponentAdapter editorPositionUpdater;

    /**
     * List mouse adapter.
     */
    protected MouseAdapter mouseAdapter = null;

    /**
     * List key adapter.
     */
    protected KeyAdapter keyAdapter = null;

    /**
     * Installs cell editor in the list.
     * This method should add all required listeners in the list that will cause editing to start.
     *
     * @param list list to process
     */
    public void install ( final JList list )
    {
        // Installing edit bounds updater
        editorPositionUpdater = new ComponentAdapter ()
        {
            public void componentResized ( ComponentEvent e )
            {
                checkEditorBounds ();
            }

            private void checkEditorBounds ()
            {
                if ( isEditing () )
                {
                    Rectangle newBounds = getEditorBounds ( list, editedCell, oldValue );
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
            public void mouseClicked ( MouseEvent e )
            {
                if ( e.getClickCount () == 2 && SwingUtilities.isLeftMouseButton ( e ) )
                {
                    final Point point = e.getPoint ();
                    final int index = list.getUI ().locationToIndex ( list, point );
                    final Rectangle cell = list.getCellBounds ( index, index );
                    if ( cell.contains ( point ) )
                    {
                        startEdit ( list, index );
                    }
                }
            }
        };
        list.addMouseListener ( mouseAdapter );

        keyAdapter = new KeyAdapter ()
        {
            public void keyReleased ( KeyEvent e )
            {
                if ( Hotkey.F2.isTriggered ( e ) )
                {
                    startEdit ( list, list.getSelectedIndex () );
                }
            }
        };
        list.addKeyListener ( keyAdapter );
    }

    /**
     * Uninstalls cell editor from the list.
     * This method should remove all listeners from the list and cleanup all associated resources.
     *
     * @param list list to process
     */
    public void uninstall ( JList list )
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
    protected void uninstallStartEditActions ( JList list )
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

    /**
     * Returns whether list cell under the specified index is editable or not.
     *
     * @param list  list to process
     * @param index cell index
     * @param value cell value
     * @return whether list cell under the specified index is editable or not
     */
    public boolean isCellEditable ( JList list, int index, T value )
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

    /**
     * Returns list cell editor created for the cell under specified index.
     *
     * @param list  list to process
     * @param index cell index
     * @param value cell value
     * @return list cell editor for the cell under specified index
     */
    public E getCellEditor ( final JList list, int index, T value )
    {
        // Creating editor component
        editor = createCellEditor ( list, index, value );
        createCellEditorListeners ( list, index, value );
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
    protected abstract E createCellEditor ( JList list, int index, T value );

    /**
     * Creates listeners for list cell editor component.
     *
     * @param list
     */
    protected void createCellEditorListeners ( final JList list, int index, T value )
    {
        // Editing stop on focus loss event
        final FocusAdapter focusAdapter = new FocusAdapter ()
        {
            public void focusLost ( FocusEvent e )
            {
                stopEdit ( list );
            }
        };
        editor.addFocusListener ( focusAdapter );

        // Editing stop and cancel on key events
        editor.addKeyListener ( new KeyAdapter ()
        {
            public void keyReleased ( KeyEvent e )
            {
                if ( Hotkey.ENTER.isTriggered ( e ) )
                {
                    // To avoid double-saving
                    editor.removeFocusListener ( focusAdapter );

                    stopEdit ( list );
                    list.requestFocusInWindow ();
                }
                else if ( Hotkey.ESCAPE.isTriggered ( e ) )
                {
                    // To avoid saving
                    editor.removeFocusListener ( focusAdapter );

                    cancelEdit ( list );
                    list.requestFocusInWindow ();
                }
            }
        } );
    }

    /**
     * Starts list cell editing.
     *
     * @param list  list to process
     * @param index
     */
    public void startEdit ( final JList list, int index )
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

    /**
     * Cancels list cell editing.
     *
     * @param list list to process
     */
    public void cancelEdit ( final JList list )
    {
        // Removing cell editor from the list
        removeEditor ( list );

        // Notifying about editing cancel
        editCancelled ( list, editedCell );
    }

    /**
     * Stops list cell editing.
     *
     * @param list list to process
     */
    public void stopEdit ( final JList list )
    {
        // Saving selected indices to restore them later
        int[] indices = list.getSelectedIndices ();

        // Checking whether value has changed or not
        T newValue = getCellEditorValue ( list, editedCell, oldValue );
        boolean changed = updateListModel ( list, editedCell, oldValue, newValue, true );

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
    }

    /**
     * Adds cell editor into the list.
     *
     * @param list list to process
     */
    protected void addEditor ( JList list )
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
    protected void removeEditor ( JList list )
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
    protected Rectangle getEditorBounds ( JList list, int index, T value, Rectangle cellBounds )
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
    protected Rectangle getEditorBounds ( JList list, int index, T value )
    {
        Rectangle cellBounds = list.getCellBounds ( index, index );
        if ( cellBounds != null )
        {
            Rectangle editorBounds = getEditorBounds ( list, index, value, cellBounds );
            return new Rectangle ( cellBounds.x + editorBounds.x, cellBounds.y + editorBounds.y, editorBounds.width, editorBounds.height );
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns whether value update operation completed successfully or not.
     * Basically this method should replace old value with a new one in list model and update list view.
     *
     * @param list            list to process
     * @param index           cell index
     * @param oldValue        old cell value
     * @param newValue        new cell value
     * @param updateSelection whether update list selection or not
     * @return true if list model was updated
     */
    public boolean updateListModel ( JList list, int index, T oldValue, T newValue, boolean updateSelection )
    {
        // Checking if value has changed
        if ( CompareUtils.equals ( oldValue, newValue ) )
        {
            return false;
        }

        // Updating list model
        ListModel model = list.getModel ();
        if ( model instanceof DefaultListModel )
        {
            DefaultListModel defaultListModel = ( DefaultListModel ) model;
            defaultListModel.setElementAt ( newValue, index );
            return true;
        }
        else if ( model instanceof AbstractListModel )
        {
            final Object[] values = new Object[ model.getSize () ];
            for ( int i = 0; i < model.getSize (); i++ )
            {
                if ( editedCell != i )
                {
                    values[ i ] = model.getElementAt ( i );
                }
                else
                {
                    values[ i ] = newValue;
                }
            }
            list.setModel ( new AbstractListModel ()
            {
                public int getSize ()
                {
                    return values.length;
                }

                public Object getElementAt ( int index )
                {
                    return values[ index ];
                }
            } );
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Notifies that list cell editing has started.
     *
     * @param list  list to process
     * @param index edited cell index
     */
    public void editStarted ( JList list, int index )
    {
        editedCell = index;
        if ( list instanceof WebList )
        {
            ( ( WebList ) list ).fireEditStarted ( index );
        }
    }

    /**
     * Notifies that list cell editing has finished.
     *
     * @param list     list to process
     * @param index    edited cell index
     * @param oldValue old cell value
     * @param newValue new cell value
     */
    public void editStopped ( JList list, int index, T oldValue, T newValue )
    {
        editedCell = -1;
        if ( list instanceof WebList )
        {
            ( ( WebList ) list ).fireEditFinished ( index, oldValue, newValue );
        }
    }

    /**
     * Notifies that list cell editing was cancelled.
     *
     * @param list  list to process
     * @param index edited cell index
     */
    public void editCancelled ( JList list, int index )
    {
        editedCell = -1;
        if ( list instanceof WebList )
        {
            ( ( WebList ) list ).fireEditCancelled ( index );
        }
    }

    /**
     * Returns whether editor is currently active or not.
     *
     * @return true if editor is currently active, false otherwise
     */
    public boolean isEditing ()
    {
        return editedCell != -1;
    }
}