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

package com.alee.utils.parsing;

/**
 * Custom class providing file size units support.
 * You can mix any units (using upper/lower cases) in any order and also provide fractional values.
 * <p/>
 * Here are a few examples of supported values:
 * 1. "10 KBytes" - one single unit
 * 2. "2gb 512mb" - two different units in same format
 * 3. "5 GB 128mb 256 kilobytes" - three different units in different formats
 * 4. "3.4 Petabytes" - fractional values with single unit
 * 5. "1 Gb 12.5 Mb" - fractional values with multiple units
 * 6. "10kb 1mb" - reverse units description
 * 7. "5.2 MB 10 MB" - same units with different values
 *
 * @author Mikle Garin
 */

public class FileSizeUnits extends AbstractUnits
{
    /**
     * todo 1. Multi-language support based on LanguageManager record values
     */

    /**
     * Global {@link FileSizeUnits} instance.
     * Kept for the sake of optimizing overhead.
     */
    protected static FileSizeUnits instance;

    /**
     * Constructs new {@link FileSizeUnits}.
     */
    public FileSizeUnits ()
    {
        super ( new Unit ( 1125899906842624L, 1024L, "pb", "p", "pbyte", "pbytes", "petabyte", "petabytes" ),
                new Unit ( 1099511627776L, 1024L, "tb", "t", "tbyte", "tbytes", "terabyte", "terabytes" ),
                new Unit ( 1073741824L, 1024L, "gb", "g", "gbyte", "gbytes", "gigabyte", "gigabytes" ),
                new Unit ( 1048576L, 1024L, "mb", "m", "mbyte", "mbytes", "megabyte", "megabytes" ),
                new Unit ( 1024L, 1024L, "kb", "k", "kbyte", "kbytes", "kilobyte", "kilobytes" ),
                new Unit ( 1L, 1024L, "b", "byte", "bytes" ) );
    }

    /**
     * Returns global {@link FileSizeUnits} instance.
     * You can either use this instance or create a new one.
     *
     * @return global {@link FileSizeUnits} instance
     */
    public synchronized static FileSizeUnits get ()
    {
        if ( instance == null )
        {
            instance = new FileSizeUnits ();
        }
        return instance;
    }
}