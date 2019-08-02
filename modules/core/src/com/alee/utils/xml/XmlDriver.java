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

import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Custom {@link com.thoughtworks.xstream.io.HierarchicalStreamDriver} based on {@link com.thoughtworks.xstream.io.xml.StaxDriver}.
 * It adds {@link com.thoughtworks.xstream.io.xml.PrettyPrintWriter} usage for nicer XML output.
 *
 * @author Mikle Garin
 */
public class XmlDriver extends StaxDriver
{
    @Override
    public HierarchicalStreamWriter createWriter ( final Writer out )
    {
        return new PrettyPrintWriter ( out, getNameCoder () );
    }

    @Override
    public HierarchicalStreamWriter createWriter ( final OutputStream out )
    {
        return createWriter ( new OutputStreamWriter ( out ) );
    }
}