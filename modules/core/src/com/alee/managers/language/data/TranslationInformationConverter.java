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

import com.alee.managers.language.LanguageUtils;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.util.Locale;

/**
 * Custom {@link Converter} for {@link TranslationInformation} object.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see com.alee.managers.language.LanguageManager
 */
public class TranslationInformationConverter implements Converter
{
    /**
     * Language attribute name.
     */
    private static final String LANGUAGE = "lang";

    /**
     * Single-text stae attribute name.
     */
    private static final String TITLE = "title";

    /**
     * Single-text mnemonic attribute name.
     */
    private static final String AUTHOR = "author";

    @Override
    public boolean canConvert ( final Class type )
    {
        return TranslationInformation.class.getCanonicalName ().equals ( type.getCanonicalName () );
    }

    @Override
    public void marshal ( final Object source, final HierarchicalStreamWriter writer, final MarshallingContext context )
    {
        final TranslationInformation translation = ( TranslationInformation ) source;

        // Writing language
        final Locale locale = translation.getLocale ();
        writer.addAttribute ( LANGUAGE, LanguageUtils.toString ( locale ) );

        // Writing title
        writer.addAttribute ( TITLE, translation.getTitle () );

        // Writing author
        writer.addAttribute ( AUTHOR, translation.getAuthor () );
    }

    @Override
    public Object unmarshal ( final HierarchicalStreamReader reader, final UnmarshallingContext context )
    {
        final TranslationInformation value = new TranslationInformation ();

        // Reading language
        final String locale = reader.getAttribute ( LANGUAGE );
        value.setLocale ( LanguageUtils.fromString ( locale ) );

        // Reading title
        final String title = reader.getAttribute ( TITLE );
        value.setTitle ( title );

        // Reading authoer
        final String author = reader.getAttribute ( AUTHOR );
        value.setAuthor ( author );

        return value;
    }
}