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

package com.alee.demo.api;

import com.alee.demo.icons.DemoIcons;
import com.alee.managers.log.Log;
import com.alee.utils.ReflectUtils;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mikle Garin
 */

public abstract class AbstractExampleGroup extends AbstractExampleElement implements ExampleGroup
{
    protected List<ExampleGroup> groups;
    protected List<Example> examples;

    @Override
    public Icon getIcon ()
    {
        final URL resource = getClass ().getResource ( "icons/" + getId () + ".png" );
        return resource != null ? new ImageIcon ( resource ) : DemoIcons.group;
    }

    @Override
    public FeatureState getFeatureState ()
    {
        final List<ExampleGroup> groups = getGroups ();
        final List<Example> examples = getExamples ();
        final List<FeatureState> states = new ArrayList<FeatureState> ( groups.size () + examples.size () );
        for ( final ExampleGroup group : groups )
        {
            states.add ( group.getFeatureState () );
        }
        for ( final Example example : examples )
        {
            states.add ( example.getFeatureState () );
        }
        return ExampleUtils.getResultingState ( states );
    }

    @Override
    public List<ExampleGroup> getGroups ()
    {
        if ( groups == null )
        {
            final List<Class> groupClasses = getExampleGroupClasses ();
            groups = new ArrayList<ExampleGroup> ( groupClasses.size () );
            for ( final Class<ExampleGroup> groupClass : groupClasses )
            {
                try
                {
                    final ExampleGroup group = ReflectUtils.createInstance ( groupClass );
                    if ( group instanceof AbstractExampleElement )
                    {
                        ( ( AbstractExampleElement ) group ).setGroup ( this );
                    }
                    groups.add ( group );
                }
                catch ( final Throwable e )
                {
                    Log.get ().error ( "Unable to initialize group: " + groupClass, e );
                }
            }
        }
        return groups;
    }

    /**
     * Returns list of example sub group classes for this example group.
     *
     * @return list of example sub group classes for this example group
     */
    protected List<Class> getExampleGroupClasses ()
    {
        return Collections.emptyList ();
    }

    @Override
    public List<Example> getExamples ()
    {
        if ( examples == null )
        {
            final List<Class> exampleClasses = getExampleClasses ();
            examples = new ArrayList<Example> ( exampleClasses.size () );
            for ( final Class<Example> exampleClass : exampleClasses )
            {
                try
                {
                    final Example example = ReflectUtils.createInstance ( exampleClass );
                    if ( example instanceof AbstractExampleElement )
                    {
                        ( ( AbstractExampleElement ) example ).setGroup ( this );
                    }
                    examples.add ( example );
                }
                catch ( final Throwable e )
                {
                    Log.get ().error ( "Unable to initialize example: " + exampleClass, e );
                }
            }
        }
        return examples;
    }

    /**
     * Returns list of example classes for this example group.
     *
     * @return list of example classes for this example group
     */
    protected List<Class> getExampleClasses ()
    {
        return Collections.emptyList ();
    }
}