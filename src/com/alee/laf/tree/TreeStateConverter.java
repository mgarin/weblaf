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
    public TreeStateConverter ( Mapper mapper, ReflectionProvider reflectionProvider )
    {
        super ( mapper, reflectionProvider );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canConvert ( Class type )
    {
        return type.equals ( TreeState.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void marshal ( Object source, HierarchicalStreamWriter writer, MarshallingContext context )
    {
        TreeState treeState = ( TreeState ) source;
        for ( Map.Entry<String, NodeState> entry : treeState.getStates ().entrySet () )
        {
            String nodeId = entry.getKey ();
            NodeState nodeState = entry.getValue ();
            writer.startNode ( "node" );
            writer.addAttribute ( "id", nodeId );
            writer.addAttribute ( "expanded", "" + nodeState.isExpanded () );
            writer.addAttribute ( "selected", "" + nodeState.isSelected () );
            writer.endNode ();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object unmarshal ( HierarchicalStreamReader reader, UnmarshallingContext context )
    {
        Map<String, NodeState> states = new LinkedHashMap<String, NodeState> ();
        while ( reader.hasMoreChildren () )
        {
            reader.moveDown ();
            String nodeIdAttribue = reader.getAttribute ( "id" );
            String nodeId = nodeIdAttribue != null ? nodeIdAttribue : reader.getNodeName ();
            String expandedAttribue = reader.getAttribute ( "expanded" );
            String expanded = expandedAttribue != null ? expandedAttribue : "false";
            String selectedAttribue = reader.getAttribute ( "selected" );
            String selected = selectedAttribue != null ? selectedAttribue : "false";
            states.put ( nodeId, new NodeState ( Boolean.parseBoolean ( expanded ), Boolean.parseBoolean ( selected ) ) );
            reader.moveUp ();
        }
        return new TreeState ( states );
    }
}