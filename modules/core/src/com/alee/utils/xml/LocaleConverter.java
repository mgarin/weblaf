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

package com.alee.utils.xml;

import com.alee.utils.TextUtils;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.util.Locale;

/**
 * Custom XStream converter for {@link Locale}.
 *
 * @author Mikle Garin
 */

public final class LocaleConverter implements Converter
{
    @Override
    public boolean canConvert ( final Class type )
    {
        return Locale.class.getCanonicalName ().equals ( type.getCanonicalName () );
    }

    @Override
    public void marshal ( final Object source, final HierarchicalStreamWriter writer, final MarshallingContext context )
    {
        final Locale locale = ( Locale ) source;

        // Adding language
        writer.addAttribute ( "language", locale.getLanguage () );

        // Adding country
        final String country = locale.getCountry ();
        if ( TextUtils.notEmpty ( country ) )
        {
            writer.addAttribute ( "country", country );
        }

        // Adding country
        final String variant = locale.getVariant ();
        if ( TextUtils.notEmpty ( variant ) )
        {
            writer.addAttribute ( "variant", variant );
        }
    }

    @Override
    public Object unmarshal ( final HierarchicalStreamReader reader, final UnmarshallingContext context )
    {
        // Reading language
        final String language = reader.getAttribute ( "language" );

        // Reading country
        final String country = reader.getAttribute ( "country" );

        // Reading variant
        final String variant = reader.getAttribute ( "variant" );

        // Creating Text object
        return new Locale ( language, TextUtils.notEmpty ( country ) ? country : "", TextUtils.notEmpty ( variant ) ? variant : "" );
    }
}