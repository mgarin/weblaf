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

import com.alee.utils.XmlUtils;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

/**
 * Custom XStream converter for {@link Class}.
 *
 * @author Mikle Garin
 */
public class ClassConverter extends AbstractSingleValueConverter
{
    @Override
    public boolean canConvert ( final Class aClass )
    {
        return aClass == Class.class;
    }

    @Override
    public String toString ( final Object o )
    {
        final Class clazz = ( Class ) o;
        return XmlUtils.getXStream ().getMapper ().serializedClass ( clazz );
    }

    @Override
    public Object fromString ( final String s )
    {
        return XmlUtils.getXStream ().getMapper ().realClass ( s );
    }
}