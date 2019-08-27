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

package com.alee.managers.language.data;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.utils.CollectionUtils;
import com.alee.utils.TextUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.*;

/**
 * {@link Record} can store multiple {@link Value}s for different {@link Locale}s.
 * It can also provide single and multiple {@link Value}s for any specific {@link Locale} if such exist.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see com.alee.managers.language.LanguageManager
 * @see com.alee.managers.language.Language
 * @see Dictionary
 * @see Value
 */
@XStreamAlias ( "record" )
public final class Record implements Cloneable, Serializable
{
    /**
     * {@link Record} key within its {@link Dictionary}.
     */
    @NotNull
    @XStreamAsAttribute
    private String key;

    /**
     * {@link Value}s of this {@link Record}.
     */
    @NotNull
    @XStreamImplicit
    private List<Value> values;

    /**
     * {@link Map} containing {@link Value}s cached by {@link Locale} keys.
     */
    @Nullable
    private transient Map<String, Value> valuesCache;

    /**
     * Constructs new {@link Record}.
     */
    public Record ()
    {
        this ( "", new ArrayList<Value> ( 0 ) );
    }

    /**
     * Constructs new {@link Record}.
     *
     * @param key {@link Record} key within its {@link Dictionary}
     */
    public Record ( @NotNull final String key )
    {
        this ( key, new ArrayList<Value> ( 0 ) );
    }

    /**
     * Constructs new {@link Record}.
     *
     * @param key    {@link Record} key within its {@link Dictionary}
     * @param values {@link Value}s for new {@link Record}
     */
    public Record ( @NotNull final String key, @NotNull final Value... values )
    {
        this ( key, CollectionUtils.asList ( values ) );
    }

    /**
     * Constructs new {@link Record}.
     *
     * @param key    {@link Record} key within its {@link Dictionary}
     * @param values {@link Value}s for new {@link Record}
     */
    public Record ( @NotNull final String key, @NotNull final List<Value> values )
    {
        this.key = key;
        this.values = CollectionUtils.copy ( values );
    }

    /**
     * Returns {@link Record} key within its {@link Dictionary}.
     *
     * @return {@link Record} key within its {@link Dictionary}
     */
    @NotNull
    public String getKey ()
    {
        return key;
    }

    /**
     * Sets {@link Record} key within its {@link Dictionary}.
     *
     * @param key new {@link Record} key within its {@link Dictionary}
     */
    public void setKey ( @NotNull final String key )
    {
        this.key = key;
    }

    /**
     * Returns {@link Value}s of this {@link Record}.
     *
     * @return {@link Value}s of this {@link Record}
     */
    @NotNull
    public List<Value> getValues ()
    {
        return values;
    }

    /**
     * Sets {@link Value}s for this {@link Record}.
     *
     * @param values new {@link Value}s for this {@link Record}
     */
    public void setValues ( @NotNull final List<Value> values )
    {
        this.values = values;
    }

    /**
     * Adds new {@link Value} for this {@link Record}.
     *
     * @param value new {@link Value}
     */
    public void addValue ( @NotNull final Value value )
    {
        values.add ( value );
    }

    /**
     * Removes {@link Value} from this {@link Record}.
     *
     * @param value {@link Value} to remove
     */
    public void removeValue ( @NotNull final Value value )
    {
        values.remove ( value );
    }

    /**
     * Removes all {@link Value}s from this {@link Record}.
     */
    public void clearValues ()
    {
        values.clear ();
    }

    /**
     * Returns amount of {@link Value}s within this {@link Record}.
     *
     * @return amount of {@link Value}s within this {@link Record}
     */
    public int valuesCount ()
    {
        return values.size ();
    }

    /**
     * Collects all {@link Locale}s from this {@link Record}.
     *
     * @param locales {@link List} to put {@link Locale}s into
     */
    protected void collectAllLocales ( @NotNull final List<Locale> locales )
    {
        for ( final Value value : values )
        {
            final Locale locale = value.getLocale ();
            if ( !locales.contains ( locale ) )
            {
                locales.add ( locale );
            }
        }
    }

    /**
     * Collects all language codes from this {@link Record}.
     *
     * @param codes {@link List} to put language codes into
     */
    protected void collectAllCodes ( @NotNull final List<String> codes )
    {
        for ( final Value value : values )
        {
            final String code = value.getLocale ().getLanguage ();
            if ( !codes.contains ( code ) )
            {
                codes.add ( code );
            }
        }
    }

    /**
     * Returns whether or not this {@link Record} has {@link Value} for the specified {@link Locale}.
     *
     * @param locale {@link Locale} to check {@link Value} for
     * @return {@code true} if this {@link Record} has {@link Value} for the specified {@link Locale}, {@code false} otherwise
     */
    public boolean hasValue ( @NotNull final Locale locale )
    {
        return getValue ( locale ) != null;
    }

    /**
     * Returns {@link Value} most fitting for the specified {@link Locale}.
     *
     * @param locale {@link Locale} to provide {@link Value} for
     * @return {@link Value} most fitting for the specified {@link Locale}
     */
    @Nullable
    public Value getValue ( @NotNull final Locale locale )
    {
        final Value value;
        final String key = locale.getLanguage () + "_" + locale.getCountry ();
        if ( valuesCache != null && valuesCache.containsKey ( key ) )
        {
            // Resulting value is already cached
            value = valuesCache.get ( key );
        }
        else
        {
            // Looking for value within existing values
            if ( CollectionUtils.notEmpty ( values ) )
            {
                // Looking for most fittng value
                final List<Value> values = getValues ( locale );
                final Comparator<Value> comparator = new ValueCountryComparator ( locale );
                value = CollectionUtils.max ( values, comparator );
            }
            else
            {
                // Empty value result
                value = null;
            }

            // Caching result
            if ( valuesCache == null )
            {
                valuesCache = new HashMap<String, Value> ( values.size () );
            }
            valuesCache.put ( key, value );
        }
        return value;
    }

    /**
     * Returns {@link List} of {@link Value}s for the specified {@link Locale}.
     *
     * @param locale {@link Locale} to provide {@link Value}s for
     * @return {@link List} of {@link Value} for the specified {@link Locale}
     */
    @NotNull
    public List<Value> getValues ( @NotNull final Locale locale )
    {
        final List<Value> localeValues = new ArrayList<Value> ( 3 );
        for ( final Value value : values )
        {
            if ( Objects.equals ( value.getLocale ().getLanguage (), locale.getLanguage () ) )
            {
                localeValues.add ( value );
            }
        }
        return localeValues;
    }

    @NotNull
    @Override
    public String toString ()
    {
        return toString ( false );
    }

    /**
     * Returns {@link Record} text representation.
     *
     * @param boldKey whether or not key should be displayed bold
     * @return {@link Record} text representation
     */
    @NotNull
    public String toString ( final boolean boldKey )
    {
        return ( boldKey ? "{" : "" ) + key + ( boldKey ? ":b}" : "" ) +
                ( "[ " + TextUtils.listToString ( values, "; " ) + " ]" );
    }
}