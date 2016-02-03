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

import com.alee.utils.CollectionUtils;
import com.alee.utils.CompareUtils;
import com.alee.utils.MergeUtils;
import com.alee.utils.TextUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikle Garin
 */

@XStreamAlias ("record")
public final class Record implements Serializable, Cloneable
{
    @XStreamAsAttribute
    private String key;

    @XStreamAsAttribute
    private String hotkey;

    @XStreamImplicit
    private List<Value> values;

    public Record ()
    {
        this ( null );
    }

    public Record ( final String key )
    {
        this ( key, new ArrayList<Value> () );
    }

    public Record ( final String key, final Value... values )
    {
        super ();
        setKey ( key );
        setValues ( CollectionUtils.asList ( values ) );
    }

    public Record ( final String key, final List<Value> values )
    {
        super ();
        setKey ( key );
        setValues ( CollectionUtils.copy ( values ) );
    }

    public Record ( final String key, final String hotkey, final Value... values )
    {
        super ();
        setKey ( key );
        setHotkey ( hotkey );
        setValues ( CollectionUtils.asList ( values ) );
    }

    public Record ( final String key, final String hotkey, final List<Value> values )
    {
        super ();
        setKey ( key );
        setHotkey ( hotkey );
        setValues ( CollectionUtils.copy ( values ) );
    }

    public String getKey ()
    {
        return key;
    }

    public void setKey ( final String key )
    {
        this.key = key;
    }

    public String getHotkey ()
    {
        return hotkey;
    }

    public void setHotkey ( final String hotkey )
    {
        this.hotkey = hotkey;
    }

    public List<Value> getValues ()
    {
        return values;
    }

    public void setValues ( final List<Value> values )
    {
        this.values = values;
    }

    public Value addValue ( final Value value )
    {
        if ( values == null )
        {
            values = new ArrayList<Value> ( 1 );
        }
        values.add ( value );
        return value;
    }

    public void removeValue ( final Value value )
    {
        if ( values != null )
        {
            values.remove ( value );
        }
    }

    public void removeValue ( final String language )
    {
        if ( values != null )
        {
            for ( int i = 0; i < values.size (); i++ )
            {
                final String valueLang = values.get ( i ).getLang ();
                if ( CompareUtils.equals ( valueLang, language ) )
                {
                    values.remove ( i );
                }
            }
        }
    }

    public void clear ()
    {
        if ( values != null )
        {
            values.clear ();
        }
    }

    public int size ()
    {
        return values != null ? values.size () : 0;
    }

    public String getText ( final String lang )
    {
        return getText ( lang, null );
    }

    public String getText ( final String lang, final String state )
    {
        final Value value = getValue ( lang );
        return value != null ? value.getText ( state ) : null;
    }

    public Value getValue ( final String lang )
    {
        if ( values != null )
        {
            for ( final Value value : values )
            {
                final String valueLang = value.getLang ();
                if ( valueLang == null || CompareUtils.equals ( valueLang, lang ) )
                {
                    return value;
                }
            }
        }
        return null;
    }

    public boolean hasValue ( final String lang )
    {
        return getValue ( lang ) != null;
    }

    public List<String> getSupportedLanguages ()
    {
        return getSupportedLanguages ( new ArrayList<String> ( size () ) );
    }

    public List<String> getSupportedLanguages ( final List<String> languages )
    {
        if ( values != null )
        {
            for ( final Value value : values )
            {
                if ( !languages.contains ( value.getLang () ) )
                {
                    languages.add ( value.getLang () );
                }
            }
        }
        return languages;
    }

    @Override
    public Record clone ()
    {
        return MergeUtils.cloneByFieldsSafely ( this );
    }

    @Override
    public String toString ()
    {
        return toString ( false );
    }

    public String toString ( final boolean boldKey )
    {
        return ( boldKey ? "{" : "" ) + key + ( boldKey ? ":b}" : "" ) +
                ( hotkey != null ? " (" + hotkey + ")" : "" ) + " -> " +
                ( values != null ? ( "[ " + TextUtils.listToString ( values, "; " ) + " ]" ) : "null" );
    }
}