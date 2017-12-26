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

import com.alee.laf.text.WebTextField;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.style.StyleId;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Simple {@link ListCellEditor} implementation for editing text values.
 *
 * @author Mikle Garin
 */

public abstract class TextListCellEditor<T> extends AbstractListCellEditor<WebTextField, T>
{
    @Override
    protected WebTextField createCellEditor ( final JList list, final int index, final T value )
    {
        // Creating editor component
        final String text = valueToText ( list, index, value );
        final WebTextField editor = new WebTextField ( StyleId.listTextCellEditor.at ( list ) );
        editor.setText ( text );
        editor.selectAll ();

        // Editing stop on focus loss event
        final FocusAdapter focusAdapter = new FocusAdapter ()
        {
            @Override
            public void focusLost ( final FocusEvent e )
            {
                // Stopping list editing
                stopEdit ( list );
            }
        };
        editor.addFocusListener ( focusAdapter );

        // Editing stop and cancel on key events
        editor.addKeyListener ( new KeyAdapter ()
        {
            @Override
            public void keyReleased ( final KeyEvent e )
            {
                if ( Hotkey.ENTER.isTriggered ( e ) )
                {
                    // Removing focus listener first to avoid double-saving
                    editor.removeFocusListener ( focusAdapter );

                    // Stopping list editing
                    stopEdit ( list );

                    // Transferring focus to list
                    list.requestFocusInWindow ();
                }
                else if ( Hotkey.ESCAPE.isTriggered ( e ) )
                {
                    // Removing focus listener first to avoid double-saving
                    editor.removeFocusListener ( focusAdapter );

                    // Cancelling list editing
                    cancelEdit ( list );

                    // Transferring focus to list
                    list.requestFocusInWindow ();
                }
            }
        } );

        return editor;
    }

    @Override
    public T getCellEditorValue ( final JList list, final int index, final T oldValue )
    {
        final String text = editor.getText ();
        return textToValue ( list, index, oldValue, text );
    }

    /**
     * Returns text for the specified value.
     *
     * @param list  edited {@link JList}
     * @param index edited cell index
     * @param value edited value
     * @return text for the specified value
     */
    protected abstract String valueToText ( JList list, int index, T value );

    /**
     * Returns value with the edited text.
     *
     * @param list     edited {@link JList}
     * @param index    edited cell index
     * @param oldValue old cell value
     * @param text     edited text
     * @return value with the edited text
     */
    protected abstract T textToValue ( JList list, int index, T oldValue, String text );
}