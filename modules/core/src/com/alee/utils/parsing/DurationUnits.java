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
 * Custom class providing duration units support.
 * You can mix any units (using upper/lower cases) in any order and also provide fractional values.
 * <p/>
 * Here are a few examples of supported values:
 * 1. "10 Min" - one single unit
 * 2. "2h 30m" - two different units in same format
 * 3. "5 Hrs 30m 45 seconds" - three different units in different formats
 * 4. "2.5 Hours" - fractional values with single unit
 * 5. "1 Hr 5.5 Min" - fractional values with multiple units
 * 6. "10m 1h" - reverse units description
 * 7. "2.5m 5m" - same units with different values
 *
 * @author Mikle Garin
 */
public class DurationUnits extends AbstractUnits
{
    /**
     * todo 1. Multi-language support based on LanguageManager record values
     */

    /**
     * Global {@link DurationUnits} instance.
     * Kept for the sake of optimizing overhead.
     */
    protected static DurationUnits instance;

    /**
     * Constructs new {@link DurationUnits}.
     */
    public DurationUnits ()
    {
        super ( new Unit ( 604800000L, 0L, "w", "week", "weeks" ),
                new Unit ( 86400000L, 7L, "d", "day", "days" ),
                new Unit ( 3600000L, 24L, "h", "hr", "hour", "hrs", "hours" ),
                new Unit ( 60000L, 60L, "m", "min", "minute", "mins", "minutes" ),
                new Unit ( 1000L, 60L, "s", "sec", "second", "seconds" ),
                new Unit ( 1L, 1000L, "ms", "millisecond", "milliseconds" ) );
    }

    /**
     * Returns global {@link DurationUnits} instance.
     * You can either use this instance or create a new one.
     *
     * @return global {@link DurationUnits} instance
     */
    public synchronized static DurationUnits get ()
    {
        if ( instance == null )
        {
            instance = new DurationUnits ();
        }
        return instance;
    }
}