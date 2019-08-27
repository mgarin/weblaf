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
import java.util.Locale;

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
    private final int limit;

    /**
     * Whether should translate all characters to upper case or not.
     */
    private final boolean toUppercase;

    /**
     * Constructs new limited document.
     */
    public LimitedDocument ()
    {
        super ();
        this.limit = 0;
        this.toUppercase = false;
    }

    /**
     * Constructs new limited document.
     *
     * @param limit characters limit
     */
    public LimitedDocument ( final int limit )
    {
        super ();
        this.limit = limit;
        this.toUppercase = false;
    }

    /**
     * Constructs new limited document.
     *
     * @param limit characters limit
     * @param upper whether should translate all characters to upper case or not
     */
    public LimitedDocument ( final int limit, final boolean upper )
    {
        super ();
        this.limit = limit;
        this.toUppercase = upper;
    }

    /**
     * Returns document characters limit.
     *
     * @return document characters limit
     */
    public int getLimit ()
    {
        return limit;
    }

    /**
     * Returns whether should translate all characters to upper case or not.
     *
     * @return true if should translate all characters to upper case, false otherwise
     */
    public boolean isToUppercase ()
    {
        return toUppercase;
    }

    @Override
    public void insertString ( final int offset, String str, final AttributeSet attr ) throws BadLocationException
    {
        if ( str != null )
        {
            if ( getLimit () > 0 && getLength () + str.length () <= getLimit () )
            {
                if ( isToUppercase () )
                {
                    str = str.toUpperCase ( Locale.ROOT );
                }
                super.insertString ( offset, str, attr );
            }
        }
    }
}