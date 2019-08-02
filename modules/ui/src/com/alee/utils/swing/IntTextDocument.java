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

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Custom document for integer-value only fields.
 *
 * @author Mikle Garin
 */
public class IntTextDocument extends PlainDocument
{
    @Override
    public void insertString ( final int offs, final String str, final AttributeSet a ) throws BadLocationException
    {
        if ( str == null )
        {
            return;
        }
        final int length = getLength ();
        if ( length + str.length () <= 5 )
        {
            final String oldString = getText ( 0, length );
            final String newString = oldString.substring ( 0, offs ) + str + oldString.substring ( offs );
            try
            {
                if ( accept ( Integer.parseInt ( newString ) ) )
                {
                    super.insertString ( offs, str, a );
                }
            }
            catch ( final NumberFormatException ignored )
            {
                // Ignore any exceptions here
            }
        }
    }

    /**
     * Returns whether entered integer number is accepted or not.
     *
     * @param integer entered integer number
     * @return true if entered integer number is accepted, false otherwise
     */
    protected boolean accept ( final int integer )
    {
        return true;
    }
}