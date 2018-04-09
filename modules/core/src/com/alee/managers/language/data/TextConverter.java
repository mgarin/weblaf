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
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Custom {@link Converter} for {@link Text} object.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see com.alee.managers.language.LanguageManager
 */
public final class TextConverter implements Converter
{
    /**
     * State attribute name.
     */
    private static final String STATE = "state";

    /**
     * Mnemonic attribute name.
     */
    private static final String MNEMONIC = "mnemonic";

    @Override
    public boolean canConvert ( final Class type )
    {
        return Text.class.getCanonicalName ().equals ( type.getCanonicalName () );
    }

    @Override
    public void marshal ( final Object source, final HierarchicalStreamWriter writer, final MarshallingContext context )
    {
        final Text value = ( Text ) source;

        // Adding state
        if ( value.getState () != null )
        {
            writer.addAttribute ( STATE, "" + value.getState () );
        }

        // Adding mnemonic
        if ( value.getMnemonic () != -1 )
        {
            writer.addAttribute ( MNEMONIC, Character.toString ( ( char ) value.getMnemonic () ) );
        }

        // Adding value
        writer.setValue ( value.getText () );
    }

    @Override
    public Object unmarshal ( final HierarchicalStreamReader reader, final UnmarshallingContext context )
    {
        // Reading state
        final String state = reader.getAttribute ( STATE );

        // Reading mnemonic
        final String m = reader.getAttribute ( MNEMONIC );
        final int mnemonic = TextUtils.notEmpty ( m ) ? m.charAt ( 0 ) : -1;

        // Reading value
        final String value = reader.getValue ();

        // Creating Text object
        return new Text ( value, state, mnemonic );
    }
}