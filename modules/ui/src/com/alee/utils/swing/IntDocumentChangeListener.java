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

/**
 * DocumentChangeListener extension that informs about integer value changes.
 *
 * @author Mikle Garin
 */

public abstract class IntDocumentChangeListener extends DocumentChangeListener
{
    @Override
    public void documentChanged ( final DocumentEvent e )
    {
        try
        {
            documentChanged ( Integer.parseInt ( getText ( e ) ), e );
        }
        catch ( final Throwable ex )
        {
            documentChanged ( null, e );
        }
    }

    /**
     * Informs that integer value contained in the document has changed in some way.
     *
     * @param newValue new integer value or null if value cannot be parsed
     * @param e        document event
     */
    public abstract void documentChanged ( Integer newValue, DocumentEvent e );
}