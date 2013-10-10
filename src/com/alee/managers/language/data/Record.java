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
import com.alee.utils.TextUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mgarin Date: 20.04.12 Time: 13:57
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

    public Record ( String key )
    {
        this ( key, new ArrayList<Value> () );
    }

    public Record ( String key, Value... values )
    {
        super ();
        setKey ( key );
        setValues ( CollectionUtils.copy ( values ) );
    }

    public Record ( String key, List<Value> values )
    {
        super ();
        setKey ( key );
        setValues ( CollectionUtils.copy ( values ) );
    }

    public Record ( String key, String hotkey, Value... values )
    {
        super ();
        setKey ( key );
        setHotkey ( hotkey );
        setValues ( CollectionUtils.copy ( values ) );
    }

    public Record ( String key, String hotkey, List<Value> values )
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

    public void setKey ( String key )
    {
        this.key = key;
    }

    public String getHotkey ()
    {
        return hotkey;
    }

    public void setHotkey ( String hotkey )
    {
        this.hotkey = hotkey;
    }

    public List<Value> getValues ()
    {
        return values;
    }

    public void setValues ( List<Value> values )
    {
        this.values = values;
    }

    public Value addValue ( Value value )
    {
        //        for ( Value v : values )
        //        {
        //            if ( v.getLang ().equals ( value.getLang () ) )
        //            {
        //                v.setMnemonic ( value.getMnemonic () );
        //                v.setHotkey ( value.getHotkey () );
        //                v.setTexts ( value.getTexts () );
        //                v.setTooltips ( value.getTooltips () );
        //                return v;
        //            }
        //        }

        this.values.add ( value );
        return value;
    }

    public void removeValue ( Value value )
    {
        this.values.remove ( value );
    }

    public void removeValue ( String language )
    {
        for ( int i = 0; i < values.size (); i++ )
        {
            String valueLang = values.get ( i ).getLang ();
            if ( CompareUtils.equals ( valueLang, language ) )
            {
                values.remove ( i );
            }
        }
    }

    public void clear ()
    {
        values.clear ();
    }

    public int size ()
    {
        return values.size ();
    }

    public String getText ( String lang )
    {
        return getText ( lang, null );
    }

    public String getText ( String lang, String state )
    {
        Value value = getValue ( lang );
        return value != null ? value.getText ( state ) : null;
    }

    public Value getValue ( String lang )
    {
        for ( Value value : values )
        {
            final String valueLang = value.getLang ();
            if ( valueLang == null || CompareUtils.equals ( valueLang, lang ) )
            {
                return value;
            }
        }
        return null;
    }

    public boolean hasValue ( String lang )
    {
        return getValue ( lang ) != null;
    }

    @Override
    public Record clone ()
    {
        return new Record ( key, hotkey, CollectionUtils.clone ( values ) );
    }

    public String toString ()
    {
        return toString ( false );
    }

    public String toString ( boolean boldKey )
    {
        return ( boldKey ? "<html><b>" : "" ) + key + ( boldKey ? "</b>" : "" ) +
                ( hotkey != null ? " (" + hotkey + ")" : "" ) + " -> " +
                ( values != null ? ( "{ " + TextUtils.listToString ( values, "; " ) + " }" ) : "null" ) +
                ( boldKey ? "</html>" : "" );
    }
}