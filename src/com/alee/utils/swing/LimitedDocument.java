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
 * Plain document limited by characters number.
 *
 * @author Mikle Garin
 */

public class LimitedDocument extends PlainDocument
{
    /**
     * Characters limit.
     */
    private int limit;

    /**
     * Whether should translate all characters to upper case or not.
     */
    private boolean toUppercase = false;

    /**
     * Constructs new limited document.
     *
     * @param limit characters limit
     */
    public LimitedDocument ( int limit )
    {
        super ();
        this.limit = limit;
    }

    /**
     * Constructs new limited document.
     *
     * @param limit characters limit
     * @param upper whether to translate all characters to upper case or not
     */
    public LimitedDocument ( int limit, boolean upper )
    {
        super ();
        this.limit = limit;
        toUppercase = upper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
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