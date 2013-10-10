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

import com.alee.utils.TextUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mgarin Date: 20.04.12 Time: 13:48
 */

@XStreamAlias ("Dictionary")
public final class Dictionary implements Serializable
{
    private static final String ID_PREFIX = "DIC";

    @XStreamAsAttribute
    private String id;

    @XStreamAsAttribute
    private String name;

    @XStreamAsAttribute
    private String prefix;

    @XStreamAsAttribute
    private String author;

    @XStreamAsAttribute
    private String creationDate;

    @XStreamAsAttribute
    private String notes;

    @XStreamImplicit (itemFieldName = "record")
    private List<Record> records;

    @XStreamImplicit (itemFieldName = "Dictionary")
    private List<Dictionary> subdictionaries;

    @XStreamAlias ("LanguageInfo")
    private List<LanguageInfo> languageInfos;

    public Dictionary ()
    {
        super ();
        setId ();
    }

    public Dictionary ( String prefix )
    {
        super ();
        setId ();
        setPrefix ( prefix );
    }

    public Dictionary ( String prefix, String name )
    {
        super ();
        setId ();
        setPrefix ( prefix );
        setName ( name );
    }

    public Dictionary ( String prefix, String name, String author )
    {
        super ();
        setId ();
        setPrefix ( prefix );
        setName ( name );
        setAuthor ( author );
    }

    public Dictionary ( String prefix, String name, String author, String creationDate )
    {
        super ();
        setId ();
        setPrefix ( prefix );
        setName ( name );
        setAuthor ( author );
        setCreationDate ( creationDate );
    }

    public Dictionary ( String prefix, String name, String author, String creationDate, String notes )
    {
        super ();
        setId ();
        setPrefix ( prefix );
        setName ( name );
        setAuthor ( author );
        setCreationDate ( creationDate );
        setNotes ( notes );
    }

    public String getId ()
    {
        if ( id == null )
        {
            setId ();
        }
        return id;
    }

    public void setId ()
    {
        this.id = TextUtils.generateId ( ID_PREFIX );
    }

    public void setId ( String id )
    {
        this.id = id;
    }

    public String getName ()
    {
        return name;
    }

    public void setName ( String name )
    {
        this.name = name;
    }

    public String getPrefix ()
    {
        return prefix;
    }

    public void setPrefix ( String prefix )
    {
        this.prefix = prefix;
    }

    public String getAuthor ()
    {
        return author;
    }

    public void setAuthor ( String author )
    {
        this.author = author;
    }

    public String getCreationDate ()
    {
        return creationDate;
    }

    public void setCreationDate ( String creationDate )
    {
        this.creationDate = creationDate;
    }

    public String getNotes ()
    {
        return notes;
    }

    public void setNotes ( String notes )
    {
        this.notes = notes;
    }

    public List<Record> getRecords ()
    {
        return records;
    }

    public void setRecords ( List<Record> records )
    {
        this.records = records;
    }

    public List<Dictionary> getSubdictionaries ()
    {
        return subdictionaries;
    }

    public void setSubdictionaries ( List<Dictionary> subdictionaries )
    {
        this.subdictionaries = subdictionaries;
    }

    public List<LanguageInfo> getLanguageInfos ()
    {
        return languageInfos;
    }

    public void setLanguageInfos ( List<LanguageInfo> languageInfos )
    {
        this.languageInfos = languageInfos;
    }

    private void checkRecords ()
    {
        if ( records == null )
        {
            records = new ArrayList<Record> ();
        }
    }

    public Record addRecord ( String key, String language, String text )
    {
        return addRecord ( key, new Value ( language, text ) );
    }

    public Record addRecord ( String key, String language, Character mnemonic, String text )
    {
        return addRecord ( key, new Value ( language, mnemonic, text ) );
    }

    public Record addRecord ( String key, Value... values )
    {
        return addRecord ( new Record ( key, values ) );
    }

    public Record addRecord ( String key, List<Value> values )
    {
        return addRecord ( new Record ( key, values ) );
    }

    public Record addRecord ( Record record )
    {
        checkRecords ();

        // Records merge used within global dictionary
        for ( Record r : records )
        {
            if ( r.getKey ().equals ( record.getKey () ) )
            {
                if ( record.getHotkey () == null )
                {
                    record.setHotkey ( r.getHotkey () );
                }
                for ( Value value : r.getValues () )
                {
                    if ( !record.hasValue ( value.getLang () ) )
                    {
                        record.addValue ( value );
                    }
                }
                return r;
            }
        }

        this.records.add ( record );
        return record;
    }

    public void removeRecord ( Record record )
    {
        if ( records != null )
        {
            this.records.remove ( record );
        }
    }

    public void removeRecord ( String key )
    {
        if ( records != null )
        {
            for ( int i = 0; i < records.size (); i++ )
            {
                if ( records.get ( i ).getKey ().equals ( key ) )
                {
                    records.remove ( i );
                    break;
                }
            }
        }
    }

    public void removeLanguage ( String language )
    {
        if ( records != null )
        {
            for ( int i = records.size () - 1; i >= 0; i-- )
            {
                records.get ( i ).removeValue ( language );
                if ( records.get ( i ).size () == 0 )
                {
                    records.remove ( i );
                }
            }
        }
    }

    public void clear ()
    {
        if ( records != null )
        {
            records.clear ();
        }
    }

    public int size ()
    {
        return records != null ? records.size () : 0;
    }

    public void addSubdictionary ( Dictionary dictionary )
    {
        checkSubdictionaries ();
        this.subdictionaries.add ( dictionary );
    }

    private void checkSubdictionaries ()
    {
        if ( subdictionaries == null )
        {
            subdictionaries = new ArrayList<Dictionary> ();
        }
    }

    public void removeSubdictionary ( Dictionary dictionary )
    {
        if ( subdictionaries != null )
        {
            this.subdictionaries.remove ( dictionary );
        }
    }

    public int subdictionaries ()
    {
        return subdictionaries != null ? subdictionaries.size () : 0;
    }

    public String toString ()
    {
        return ( name != null ? name + " " : "" ) +
                ( size () > 0 ? " (records: " + size () + ")" : "" ) +
                ( subdictionaries () > 0 ? " (subdictionaries: " + subdictionaries () + ")" : "" );
    }
}