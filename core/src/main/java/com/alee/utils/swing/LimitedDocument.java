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
 * User: mgarin Date: 29.12.11 Time: 15:15
 */

public class LimitedDocument extends PlainDocument
{
    private int limit;
    private boolean toUppercase = false;

    public LimitedDocument ( int limit )
    {
        super ();
        this.limit = limit;
    }

    public LimitedDocument ( int limit, boolean upper )
    {
        super ();
        this.limit = limit;
        toUppercase = upper;
    }

    public void insertString ( int offset, String str, AttributeSet attr ) throws BadLocationException
    {
        if ( str == null )
        {
            return;
        }

        if ( ( getLength () + str.length () ) <= limit )
        {
            if ( toUppercase )
            {
                str = str.toUpperCase ();
            }
            super.insertString ( offset, str, attr );
        }
    }
}
