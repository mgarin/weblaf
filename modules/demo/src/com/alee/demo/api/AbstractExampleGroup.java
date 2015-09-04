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

import com.alee.utils.ReflectUtils;

import javax.swing.*;
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
        return new ImageIcon ( getClass ().getResource ( "icons/" + getId () + ".png" ) );
    }

    @Override
    public String getTitle ()
    {
        return "demo." + getId () + ".title";
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
                final ExampleGroup group = ReflectUtils.createInstanceSafely ( groupClass );
                if ( group instanceof AbstractExampleElement )
                {
                    ( ( AbstractExampleElement ) group ).setGroup ( this );
                }
                groups.add ( group );
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
                final Example example = ReflectUtils.createInstanceSafely ( exampleClass );
                if ( example instanceof AbstractExampleElement )
                {
                    ( ( AbstractExampleElement ) example ).setGroup ( this );
                }
                examples.add ( example );
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