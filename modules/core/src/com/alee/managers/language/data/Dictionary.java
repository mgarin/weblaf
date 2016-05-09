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
import java.util.Iterator;
import java.util.List;

/**
 * @author Mikle Garin
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

    public Dictionary ( final String prefix )
    {
        super ();
        setId ();
        setPrefix ( prefix );
    }

    public Dictionary ( final String prefix, final String name )
    {
        super ();
        setId ();
        setPrefix ( prefix );
        setName ( name );
    }

    public Dictionary ( final String prefix, final String name, final String author )
    {
        super ();
        setId ();
        setPrefix ( prefix );
        setName ( name );
        setAuthor ( author );
    }

    public Dictionary ( final String prefix, final String name, final String author, final String creationDate )
    {
        super ();
        setId ();
        setPrefix ( prefix );
        setName ( name );
        setAuthor ( author );
        setCreationDate ( creationDate );
    }

    public Dictionary ( final String prefix, final String name, final String author, final String creationDate, final String notes )
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

    public void setId ( final String id )
    {
        this.id = id;
    }

    public String getName ()
    {
        return name;
    }

    public void setName ( final String name )
    {
        this.name = name;
    }

    public String getPrefix ()
    {
        return prefix;
    }

    public void setPrefix ( final String prefix )
    {
        this.prefix = prefix;
    }

    public String getAuthor ()
    {
        return author;
    }

    public void setAuthor ( final String author )
    {
        this.author = author;
    }

    public String getCreationDate ()
    {
        return creationDate;
    }

    public void setCreationDate ( final String creationDate )
    {
        this.creationDate = creationDate;
    }

    public String getNotes ()
    {
        return notes;
    }

    public void setNotes ( final String notes )
    {
        this.notes = notes;
    }

    public List<Record> getRecords ()
    {
        return records;
    }

    public void setRecords ( final List<Record> records )
    {
        this.records = records;
    }

    public List<Dictionary> getSubdictionaries ()
    {
        return subdictionaries;
    }

    public void setSubdictionaries ( final List<Dictionary> subdictionaries )
    {
        this.subdictionaries = subdictionaries;
    }

    public List<LanguageInfo> getLanguageInfos ()
    {
        return languageInfos;
    }

    public void setLanguageInfos ( final List<LanguageInfo> languageInfos )
    {
        this.languageInfos = languageInfos;
    }

    public LanguageInfo getLanguageInfo ( final String language )
    {
        if ( languageInfos != null )
        {
            for ( final LanguageInfo languageInfo : languageInfos )
            {
                if ( languageInfo.getLang ().equals ( language ) )
                {
                    return languageInfo;
                }
            }
        }
        return null;
    }

    public void addLanguageInfo ( final LanguageInfo info )
    {
        if ( languageInfos == null )
        {
            languageInfos = new ArrayList<LanguageInfo> ( 1 );
        }
        final Iterator<LanguageInfo> iterator = languageInfos.iterator ();
        while ( iterator.hasNext () )
        {
            final LanguageInfo next = iterator.next ();
            if ( next.getLang ().equals ( info.getLang () ) )
            {
                iterator.remove ();
                break;
            }
        }
        languageInfos.add ( info );
    }

    private void checkRecords ()
    {
        if ( records == null )
        {
            records = new ArrayList<Record> ( 1 );
        }
    }

    public Record addRecord ( final String key, final String language, final String text )
    {
        return addRecord ( key, new Value ( language, text ) );
    }

    public Record addRecord ( final String key, final String language, final Character mnemonic, final String text )
    {
        return addRecord ( key, new Value ( language, mnemonic, text ) );
    }

    public Record addRecord ( final String key, final Value... values )
    {
        return addRecord ( new Record ( key, values ) );
    }

    public Record addRecord ( final String key, final List<Value> values )
    {
        return addRecord ( new Record ( key, values ) );
    }

    public Record addRecord ( final Record record )
    {
        checkRecords ();

        // Records merge used within global dictionary
        for ( final Record r : records )
        {
            if ( r.getKey ().equals ( record.getKey () ) )
            {
                if ( record.getHotkey () == null )
                {
                    record.setHotkey ( r.getHotkey () );
                }
                for ( final Value value : r.getValues () )
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

    public void removeRecord ( final Record record )
    {
        if ( records != null )
        {
            this.records.remove ( record );
        }
    }

    public void removeRecord ( final String key )
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

    public void removeLanguage ( final String language )
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

    public void addSubDictionary ( final Dictionary dictionary )
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

    public void removeSubDictionary ( final Dictionary dictionary )
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

    public List<String> getSupportedLanguages ()
    {
        return getSupportedLanguages ( new ArrayList<String> () );
    }

    public List<String> getSupportedLanguages ( final List<String> languages )
    {
        if ( records != null )
        {
            for ( final Record record : records )
            {
                record.getSupportedLanguages ( languages );
            }
        }
        if ( subdictionaries != null )
        {
            for ( final Dictionary dictionary : subdictionaries )
            {
                dictionary.getSupportedLanguages ( languages );
            }
        }
        return languages;
    }

    @Override
    public boolean equals ( final Object obj )
    {
        return obj != null && obj instanceof Dictionary && ( ( Dictionary ) obj ).getId ().equals ( getId () );
    }

    @Override
    public String toString ()
    {
        return ( name != null ? name + " " : "" ) +
                ( size () > 0 ? " (records: " + size () + ")" : "" ) +
                ( subdictionaries () > 0 ? " (subdictionaries: " + subdictionaries () + ")" : "" );
    }
}