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

package com.alee.managers.language;

import com.alee.managers.language.data.Dictionary;
import com.alee.managers.language.data.LanguageInfo;
import com.alee.managers.language.data.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a set of utilities to work with LanguageManager.
 *
 * @author Mikle Garin
 */

public class LanguageUtils
{
    /**
     * Returns proper initial component text.
     *
     * @param text text provided into component constructor
     * @param data language data, may not be passed
     * @return proper initial component text
     */
    public static String getInitialText ( final String text, final Object... data )
    {
        return LanguageManager.isCheckComponentsTextForTranslations () ?
                LanguageManager.contains ( text ) ? LanguageManager.get ( text, data ) : text : text;
    }

    /**
     * Registers proper initial component language key.
     *
     * @param component translated component
     * @param text      text provided into component constructor
     * @param data      language data, may not be passed
     */
    public static void registerInitialLanguage ( final LanguageMethods component, final String text, final Object... data )
    {
        if ( LanguageManager.isCheckComponentsTextForTranslations () && LanguageManager.contains ( text ) )
        {
            component.setLanguage ( text, data );
        }
    }

    /**
     * Returns all dictionary keys.
     * This method returns complete keys for each record, not just record keys.
     *
     * @param dictionary dictionary to gather keys for
     * @return all dictionary keys
     */
    public static List<String> gatherKeys ( final Dictionary dictionary )
    {
        final List<String> keys = new ArrayList<String> ();
        gatherKeys ( dictionary.getPrefix (), dictionary, keys );
        return keys;
    }

    /**
     * Returns all dictionary keys.
     * This method returns complete keys for each record, not just record keys.
     *
     * @param prefix     key prefix
     * @param dictionary dictionary to gather keys for
     * @param keys       list to gather keys into
     */
    private static void gatherKeys ( String prefix, final Dictionary dictionary, final List<String> keys )
    {
        // Determining prefix
        prefix = fixKeyPrefix ( prefix );

        // Gathering keys
        if ( dictionary.getRecords () != null )
        {
            for ( final Record record : dictionary.getRecords () )
            {
                keys.add ( prefix + record.getKey () );
            }
        }
        if ( dictionary.getSubdictionaries () != null )
        {
            for ( final Dictionary subDictionary : dictionary.getSubdictionaries () )
            {
                gatherKeys ( combineKeyPrefix ( prefix, subDictionary ), subDictionary, keys );
            }
        }
    }

    /**
     * Merges specified dictionary with the global dictionary.
     *
     * @param dictionary dictionary to merge
     * @param mergeInto  dictionary to merge into
     */
    public static void mergeDictionary ( final Dictionary dictionary, final Dictionary mergeInto )
    {
        mergeDictionary ( dictionary.getPrefix (), dictionary, mergeInto );
    }

    /**
     * Merges specified dictionary with the global dictionary.
     *
     * @param prefix     dictionary prefix
     * @param dictionary dictionary to merge
     * @param mergeInto  dictionary to merge into
     */
    private static void mergeDictionary ( String prefix, final Dictionary dictionary, final Dictionary mergeInto )
    {
        // Determining prefix
        prefix = fixKeyPrefix ( prefix );

        // Merging current level records
        if ( dictionary.getRecords () != null )
        {
            for ( final Record record : dictionary.getRecords () )
            {
                final Record clone = record.clone ();
                clone.setKey ( prefix + clone.getKey () );
                mergeInto.addRecord ( clone );
            }
        }

        // Merging language information data
        if ( dictionary.getLanguageInfos () != null )
        {
            for ( final LanguageInfo info : dictionary.getLanguageInfos () )
            {
                mergeInto.addLanguageInfo ( info );
            }
        }

        // Parsing sub-dictionaries
        if ( dictionary.getSubdictionaries () != null )
        {
            for ( final Dictionary subDictionary : dictionary.getSubdictionaries () )
            {
                mergeDictionary ( combineKeyPrefix ( prefix, subDictionary ), subDictionary, mergeInto );
            }
        }
    }

    /**
     * Returns fixed key prefix.
     *
     * @param prefix key prefix
     * @return fixed key prefix
     */
    private static String fixKeyPrefix ( final String prefix )
    {
        return prefix != null && !prefix.equals ( "" ) && !prefix.endsWith ( "." ) ? prefix + "." : "";
    }

    /**
     * Combines key prefix with dictionary prefix.
     *
     * @param prefix     key prefix
     * @param dictionary dictionary
     * @return key prefix combined with dictionary prefix
     */
    private static String combineKeyPrefix ( final String prefix, final Dictionary dictionary )
    {
        final String sp = dictionary.getPrefix ();
        return prefix + ( sp != null && !sp.equals ( "" ) ? sp : "" );
    }
}