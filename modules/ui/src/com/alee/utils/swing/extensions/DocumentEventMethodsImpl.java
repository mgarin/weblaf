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

package com.alee.utils.swing.extensions;

import com.alee.laf.WebLookAndFeel;
import com.alee.utils.general.Pair;
import com.alee.utils.swing.DocumentChangeListener;

import javax.swing.event.DocumentEvent;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Common implementations for {@link com.alee.utils.swing.extensions.DocumentEventMethods} interface methods.
 *
 * @author Mikle Garin
 * @see com.alee.utils.swing.extensions.DocumentEventMethods
 */

public final class DocumentEventMethodsImpl
{
    /**
     * Shortcut method for document change event.
     *
     * @param textComponent text component to handle events for
     * @param runnable      document event runnable
     * @return used document change and property change listeners
     */
    public static Pair<DocumentChangeListener, PropertyChangeListener> onChange ( final JTextComponent textComponent,
                                                                                  final DocumentEventRunnable runnable )
    {
        // Listening to document content changes
        final DocumentChangeListener documentChangeListener = new DocumentChangeListener ()
        {
            @Override
            public void documentChanged ( final DocumentEvent e )
            {
                runnable.run ( e );
            }
        };
        textComponent.getDocument ().addDocumentListener ( documentChangeListener );

        // Listening to component document changes
        final PropertyChangeListener propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent e )
            {
                final Object oldDocument = e.getOldValue ();
                if ( oldDocument != null && oldDocument instanceof Document )
                {
                    ( ( Document ) oldDocument ).removeDocumentListener ( documentChangeListener );
                }
                final Object newDocument = e.getNewValue ();
                if ( newDocument != null && newDocument instanceof Document )
                {
                    ( ( Document ) newDocument ).addDocumentListener ( documentChangeListener );
                }
            }
        };
        textComponent.addPropertyChangeListener ( WebLookAndFeel.DOCUMENT_PROPERTY, propertyChangeListener );

        return new Pair<DocumentChangeListener, PropertyChangeListener> ( documentChangeListener, propertyChangeListener );
    }
}