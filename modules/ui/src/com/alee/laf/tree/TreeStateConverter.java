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

package com.alee.laf.tree;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Custom converter for TreeState class.
 *
 * @author Mikle Garin
 */

public class TreeStateConverter extends ReflectionConverter
{
    /**
     * Constructs TreeStateConverter with the specified mapper and reflection provider.
     *
     * @param mapper             mapper
     * @param reflectionProvider reflection provider
     */
    public TreeStateConverter ( final Mapper mapper, final ReflectionProvider reflectionProvider )
    {
        super ( mapper, reflectionProvider );
    }

    @Override
    public boolean canConvert ( final Class type )
    {
        return type.equals ( TreeState.class );
    }

    @Override
    public void marshal ( final Object source, final HierarchicalStreamWriter writer, final MarshallingContext context )
    {
        final TreeState treeState = ( TreeState ) source;
        for ( final Map.Entry<String, NodeState> entry : treeState.getStates ().entrySet () )
        {
            final String nodeId = entry.getKey ();
            final NodeState nodeState = entry.getValue ();
            writer.startNode ( "node" );
            writer.addAttribute ( "id", nodeId );
            writer.addAttribute ( "expanded", "" + nodeState.isExpanded () );
            writer.addAttribute ( "selected", "" + nodeState.isSelected () );
            writer.endNode ();
        }
    }

    @Override
    public Object unmarshal ( final HierarchicalStreamReader reader, final UnmarshallingContext context )
    {
        final Map<String, NodeState> states = new LinkedHashMap<String, NodeState> ();
        while ( reader.hasMoreChildren () )
        {
            reader.moveDown ();
            final String nodeIdAttribue = reader.getAttribute ( "id" );
            final String nodeId = nodeIdAttribue != null ? nodeIdAttribue : reader.getNodeName ();
            final String expandedAttribue = reader.getAttribute ( "expanded" );
            final String expanded = expandedAttribue != null ? expandedAttribue : "false";
            final String selectedAttribue = reader.getAttribute ( "selected" );
            final String selected = selectedAttribue != null ? selectedAttribue : "false";
            states.put ( nodeId, new NodeState ( Boolean.parseBoolean ( expanded ), Boolean.parseBoolean ( selected ) ) );
            reader.moveUp ();
        }
        return new TreeState ( states );
    }
}