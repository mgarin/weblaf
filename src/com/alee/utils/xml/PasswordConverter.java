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

import com.alee.utils.EncryptionUtils;
import com.thoughtworks.xstream.converters.basic.StringConverter;

/**
 * User: mgarin Date: 05.05.12 Time: 16:15
 */

public class PasswordConverter extends StringConverter
{
    @Override
    public Object fromString ( String str )
    {
        return EncryptionUtils.decrypt ( ( String ) super.fromString ( str ) );
    }

    @Override
    public String toString ( Object obj )
    {
        return EncryptionUtils.encrypt ( super.toString ( obj ) );
    }
}