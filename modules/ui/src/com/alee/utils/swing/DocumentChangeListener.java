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

package com.alee.utils.swing;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * Special document listener that only notifies about occured changes.
 *
 * @author Mikle Garin
 */

public abstract class DocumentChangeListener implements DocumentListener
{
    @Override
    public void insertUpdate ( final DocumentEvent e )
    {
        documentChanged ( e );
    }

    @Override
    public void removeUpdate ( final DocumentEvent e )
    {
        documentChanged ( e );
    }

    @Override
    public void changedUpdate ( final DocumentEvent e )
    {
        documentChanged ( e );
    }

    /**
     * Informs that document has changed in some way.
     *
     * @param e document event
     */
    public abstract void documentChanged ( DocumentEvent e );

    /**
     * Returns text contained in the event's document.
     *
     * @param e document event
     * @return text contained in the event's document
     */
    protected String getText ( final DocumentEvent e )
    {
        final Document doc = e.getDocument ();
        String txt;
        try
        {
            txt = doc.getText ( 0, doc.getLength () );
        }
        catch ( final BadLocationException ex )
        {
            txt = null;
        }
        return txt;
    }
}