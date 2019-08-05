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

import com.alee.utils.CollectionUtils;
import com.alee.utils.TextUtils;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Abstract class providing convenient conversion suport between user-friendly unit-based text values and single {@code long} value.
 *
 * @author Mikle Garin
 */
public abstract class AbstractUnits
{
    /**
     * Supported {@link Unit}s.
     */
    protected List<Unit> units;

    /**
     * Constructs new {@link AbstractUnits}.
     *
     * @param units supported {@link Unit}s.
     */
    public AbstractUnits ( final Unit... units )
    {
        this ( CollectionUtils.asList ( units ) );
    }

    /**
     * Constructs new {@link AbstractUnits}.
     *
     * @param units supported {@link Unit}s.
     */
    public AbstractUnits ( final List<Unit> units )
    {
        super ();
        this.units = units;
        Collections.sort ( this.units );
    }

    /**
     * Returns single {@code long} value parsed from user-friendly unit-based text.
     *
     * @param text user-friendly unit-based text
     * @return single {@code long} value parsed from user-friendly unit-based text
     */
    public long fromString ( final String text )
    {
        try
        {
            // Resulting value
            long summ = 0;

            // Flags to track position
            boolean hasNumber = false;
            boolean hasDot = false;
            boolean hasUnit = false;
            int start = 0;
            int unitStart = -1;

            // Iterating through each text character
            final String string = TextUtils.removeSpacings ( TextUtils.removeLineBreaks ( text.toLowerCase ( Locale.ROOT ) ) );
            for ( int i = 0; i < string.length (); i++ )
            {
                final char ch = string.charAt ( i );
                if ( Character.isDigit ( ch ) )
                {
                    // Encountered number
                    hasNumber = true;
                }
                else if ( ch == '.' )
                {
                    // Encountered dot
                    if ( hasDot )
                    {
                        // Only one dot per number is allowed
                        throw new UnitsParsingException ( "Two dots found in one number: " + string );
                    }
                    hasDot = true;
                }
                else
                {
                    // Encountered some other character
                    // We simply consider it to be part of unit
                    if ( !hasNumber )
                    {
                        // Unit is only allowed after number
                        throw new UnitsParsingException ( "Units specified without specific value: " + string );
                    }
                    if ( unitStart == -1 )
                    {
                        // Mark unit start position for later usage
                        unitStart = i - start;
                    }
                    hasUnit = true;
                }

                // Checking next character to parse part and reset flags if needed
                // This will 
                final Character next = i + 1 < string.length () ? string.charAt ( i + 1 ) : null;
                if ( hasUnit && ( next == null || Character.isDigit ( next ) || next == '.' ) )
                {
                    final String part = string.substring ( start, i + 1 );
                    final String value = part.substring ( 0, unitStart );
                    final String unit = part.substring ( unitStart );
                    for ( final Unit units : this.units )
                    {
                        if ( units.uses ( unit ) )
                        {
                            summ += units.fromString ( value );
                            break;
                        }
                    }

                    hasNumber = false;
                    hasDot = false;
                    hasUnit = false;
                    start = i + 1;
                    unitStart = -1;
                }
            }
            return summ;
        }
        catch ( final Exception e )
        {
            throw new UnitsParsingException ( "Unable to parse user-friendly unit-based text", e );
        }
    }

    /**
     * Returns user-friendly unit-based text of the {@code long} value.
     *
     * @param value {@code long} value
     * @return user-friendly unit-based text of the {@code long} value
     */
    public String toString ( final long value )
    {
        if ( value >= 0 )
        {
            if ( value > 0 )
            {
                // Resolving user-friendly unit-based text
                final StringBuilder result = new StringBuilder ();
                for ( final Unit units : this.units )
                {
                    final String amount = units.toString ( value );
                    if ( amount != null )
                    {
                        if ( result.length () > 0 )
                        {
                            result.append ( " " );
                        }
                        result.append ( amount );
                    }
                }
                return result.toString ();
            }
            else
            {
                // Sepcial zero value case
                final Unit smallest = units.get ( units.size () - 1 );
                return "0" + smallest.names.get ( 0 );
            }
        }
        else
        {
            throw new UnitsParsingException ( "Negative values are not supported yet: " + value );
        }
    }

    /**
     * Class representing single measurement unit.
     */
    protected static final class Unit implements Comparable<Unit>
    {
        /**
         * Base value to which this single unit is equal to.
         * For example if base value is milliseconds and this unit is seconds - amount is 1000.
         */
        private final long amount;

        /**
         * Value used to limit displayed value with this unit.
         * For example for hours it might be 24 since we don't want to see more than 23 hours - anything above should be displayed in days.
         */
        private final long limit;

        /**
         * Displayable unit names.
         * Multiple options mostly provided for usage convenience.
         * For example for minites you might want to have "m", "min", "minute" and maybe even some others.
         */
        private final List<String> names;

        /**
         * Constructs new {@link Unit}.
         *
         * @param amount base value to which this single unit is equal to
         * @param limit  value used to limit displayed value with this unit
         * @param names  displayable unit names
         */
        public Unit ( final long amount, final long limit, final String... names )
        {
            super ();

            // Saving amount and limit
            this.amount = amount;
            this.limit = limit;

            // Ensure that unit names are correct
            for ( final String unit : names )
            {
                // Ensure unit name is not empty
                if ( TextUtils.isEmpty ( unit ) )
                {
                    throw new UnitsParsingException ( "Unit name cannot be empty" );
                }

                // Ensure unit name do not have any digits or dots
                for ( int i = 0; i < unit.length (); i++ )
                {
                    final char ch = unit.charAt ( i );
                    if ( Character.isDigit ( ch ) || ch == '.' )
                    {
                        throw new UnitsParsingException ( "Digits and dot symbol are not allowed in unit: " + unit );
                    }
                }
            }
            this.names = CollectionUtils.asList ( names );
        }

        /**
         * Returns whether or not this {@link Unit} uses specified name.
         *
         * @param name {@link Unit} name
         * @return {@code true} if this {@link Unit} uses specified name, {@code false} otherwise
         */
        public boolean uses ( final String name )
        {
            for ( final String n : names )
            {
                if ( n.equalsIgnoreCase ( name ) )
                {
                    return true;
                }
            }
            return false;
        }

        /**
         * Returns base {@code long} value calculated from the unit {@link String} value.
         *
         * @param value unit {@link String} value
         * @return base {@code long} value calculated from the unit {@link String} value
         */
        public long fromString ( final String value )
        {
            return Math.round ( Double.parseDouble ( value ) * amount );
        }

        /**
         * Returns user-friendly unit-based {@link String} of the base {@code double} value.
         *
         * @param value base {@code double} value
         * @return user-friendly unit-based {@link String} of the base {@code double} value
         */
        public String toString ( final long value )
        {
            final long display = limit > 0 ? value / amount % limit : value / amount;
            return display > 0 ? display + names.get ( 0 ) : null;
        }

        @Override
        public int compareTo ( final Unit other )
        {
            return amount < other.amount ? 1 : amount > other.amount ? -1 : 0;
        }
    }
}