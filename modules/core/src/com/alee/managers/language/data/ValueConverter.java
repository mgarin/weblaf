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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Custom {@link Converter} for {@link Value} object.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see com.alee.managers.language.LanguageManager
 */
public final class ValueConverter implements Converter
{
    /**
     * Language attribute name.
     */
    private static final String LANGUAGE = "lang";

    /**
     * Single-text state attribute name.
     */
    private static final String STATE = "state";

    /**
     * Single-text mnemonic attribute name.
     */
    private static final String MNEMONIC = "mnemonic";

    @Override
    public boolean canConvert ( final Class type )
    {
        return Value.class.getCanonicalName ().equals ( type.getCanonicalName () );
    }

    @Override
    public void marshal ( final Object source, final HierarchicalStreamWriter writer, final MarshallingContext context )
    {
        final Value value = ( Value ) source;

        // Adding language
        final Locale locale = value.getLocale ();
        writer.addAttribute ( LANGUAGE, LanguageUtils.toString ( locale ) );

        // Adding either single or multiple values
        if ( value.textsCount () == 1 )
        {
            final Text text = value.getTexts ().get ( 0 );

            // Adding state attribute if needed
            if ( text.getState () != null )
            {
                writer.addAttribute ( STATE, text.getState () );
            }

            // Adding mnemonic if needed
            if ( text.getMnemonic () != -1 )
            {
                writer.addAttribute ( MNEMONIC, Character.toString ( ( char ) text.getMnemonic () ) );
            }

            // Adding single text as value
            writer.setValue ( text.getText () );
        }
        else if ( value.textsCount () > 0 )
        {
            // Adding multiple texts
            context.convertAnother ( value.getTexts () );
        }
        else
        {
            // Empty value, generally this should never happen
            writer.setValue ( "" );
        }
    }

    @Override
    public Object unmarshal ( final HierarchicalStreamReader reader, final UnmarshallingContext context )
    {
        final Value value = new Value ();

        // Reading language
        final String locale = reader.getAttribute ( LANGUAGE );
        value.setLocale ( LanguageUtils.fromString ( locale ) );

        // Reading possible single-value case attributes
        final String state = reader.getAttribute ( STATE );
        final String character = reader.getAttribute ( MNEMONIC );
        final int mnemonic = character != null ? character.charAt ( 0 ) : -1;

        // Reading texts and tooltips
        final String text = reader.getValue ();
        final List<Text> texts = new ArrayList<Text> ();
        while ( reader.hasMoreChildren () )
        {
            reader.moveDown ();
            texts.add ( ( Text ) context.convertAnother ( value, Text.class ) );
            reader.moveUp ();
        }

        // Determining what should we save
        if ( texts.size () == 0 )
        {
            // Saving single text
            value.addText ( new Text ( text, state, mnemonic ) );
        }
        else
        {
            // Saving multiple texts
            value.setTexts ( texts );
        }

        return value;
    }
}