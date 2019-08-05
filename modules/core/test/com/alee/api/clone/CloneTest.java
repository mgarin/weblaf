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

package com.alee.api.clone;

import com.alee.api.clone.behavior.BasicCloneBehavior;
import com.alee.api.clone.behavior.OmitOnClone;
import com.alee.api.clone.unknownresolver.ExceptionUnknownResolver;
import com.alee.api.jdk.Objects;
import com.alee.utils.CollectionUtils;
import com.alee.utils.MapUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Set of JUnit tests for {@link Clone}.
 *
 * @author Mikle Garin
 */
@FixMethodOrder ( MethodSorters.JVM )
public final class CloneTest
{
    /**
     * Testing custom clone configuration on basic objects.
     */
    @Test
    public void customBasicClone ()
    {
        final Clone clone = new Clone (
                new ExceptionUnknownResolver (),
                new BasicCloneBehavior ()
        );

        checkCloneResult ( clone.clone ( null ), null );
        checkCloneResult ( clone.clone ( 1 ), 1 );
        checkCloneResult ( clone.clone ( true ), true );
        checkCloneResult ( clone.clone ( false ), false );
        checkCloneResult ( clone.clone ( TestObject.class ), TestObject.class );
        checkCloneResult ( clone.clone ( "text" ), "text" );
        checkCloneResult ( clone.clone ( new Date ( 0 ) ), new Date ( 0 ) );
        checkCloneResult ( clone.clone ( Color.RED ), new Color ( 255, 0, 0 ) );
        checkCloneResult ( clone.clone ( new Font ( "Tahoma", Font.BOLD, 12 ) ), new Font ( "Tahoma", Font.BOLD, 12 ) );
        checkCloneResult ( clone.clone ( new Insets ( 0, 1, 2, 3 ) ), new Insets ( 0, 1, 2, 3 ) );
        checkCloneResult ( clone.clone ( new Dimension ( 1, 2 ) ), new Dimension ( 1, 2 ) );
        checkCloneResult ( clone.clone ( new Point ( 0, 1 ) ), new Point ( 0, 1 ) );
        checkCloneResult ( clone.clone ( new Point2D.Float ( 0.0f, 1.0f ) ), new Point2D.Float ( 0.0f, 1.0f ) );
        checkCloneResult ( clone.clone ( new Point2D.Double ( 0.0d, 1.0d ) ), new Point2D.Double ( 0.0d, 1.0d ) );
        checkCloneResult ( clone.clone ( new Rectangle ( 0, 1, 2, 3 ) ), new Rectangle ( 0, 1, 2, 3 ) );
        checkCloneResult (
                clone.clone ( new Rectangle2D.Float ( 0.0f, 1.0f, 2.0f, 3.0f ) ),
                new Rectangle2D.Float ( 0.0f, 1.0f, 2.0f, 3.0f )
        );
        checkCloneResult (
                clone.clone ( new Rectangle2D.Double ( 0.0d, 1.0d, 2.0d, 3.0d ) ),
                new Rectangle2D.Double ( 0.0d, 1.0d, 2.0d, 3.0d )
        );
    }

    /**
     * Testing {@link Clone#basic()} configuration on custom objects.
     * These tests are important as outcome of predefined configurations shouldn't normally be changed.
     */
    @Test
    public void basicObjectClone ()
    {
        final Clone clone = Clone.basic ();

        checkCloneResult (
                clone.clone ( CollectionUtils.asList ( "1", "2" ) ),
                CollectionUtils.asList ( "1", "2" )
        );
        checkCloneResult (
                clone.clone ( MapUtils.newHashMap ( "key1", "value1", "key2", "value2" ) ),
                MapUtils.newHashMap ( "key2", "value2", "key1", "value1" )
        );
        checkCloneException (
                clone,
                new TestObject ( true, "text", 1, CollectionUtils.asList ( "1", "2" ) ),
                CloneException.class
        );
    }

    /**
     * Testing {@link Clone#deep()} configuration on custom objects.
     * These tests are important as outcome of predefined configurations shouldn't normally be changed.
     */
    @Test
    public void deepObjectClone ()
    {
        final Clone clone = Clone.deep ();

        checkCloneResult (
                clone.clone ( CollectionUtils.asList ( "1", "2" ) ),
                CollectionUtils.asList ( "1", "2" )
        );
        checkCloneResult (
                clone.clone ( MapUtils.newHashMap ( "key1", "value1", "key2", "value2" ) ),
                MapUtils.newHashMap ( "key2", "value2", "key1", "value1" )
        );
        checkCloneResult (
                clone.clone ( new TestObject ( true, "text", 1, CollectionUtils.asList ( "1", "2" ) ) ),
                new TestObject ( false, "text", null, CollectionUtils.asList ( "1", "2" ) )
        );
    }

    /**
     * Testing {@link Clone#deep()} configuration on object that recursively references itself.
     * These tests are important as outcome of predefined configurations shouldn't normally be changed.
     */
    @Test
    public void recursiveObjectClone ()
    {
        final Clone clone = Clone.deep ();

        final RecursiveTestObject recursive = new RecursiveTestObject ();
        final RecursiveTestObject copy = clone.clone ( recursive );
        for ( final RecursiveTestObject object : copy.getList () )
        {
            if ( object != copy )
            {
                throw new CloneException ( "Recursive object clone has failed" );
            }
        }
    }

    /**
     * Asserts clone result.
     *
     * @param result   clone result
     * @param expected expected result
     */
    private void checkCloneResult ( final Object result, final Object expected )
    {
        if ( Objects.notEquals ( result, expected ) )
        {
            throw new CloneException ( String.format (
                    "Unexpected clone result: %s" + "\n" + "Expected result: %s",
                    result, expected
            ) );
        }
    }

    /**
     * Asserts clone result.
     *
     * @param clone     {@link Clone}
     * @param object    object to clone
     * @param exception expected {@link Exception} class
     */
    private void checkCloneException ( final Clone clone, final Object object,
                                       final Class<? extends Exception> exception )
    {
        try
        {
            final Object result = clone.clone ( object );
            throw new CloneException ( String.format (
                    "Clone succeeded when it shouldn't: %s" + "\n" + "Resulting object is: %s",
                    object, result
            ) );
        }
        catch ( final Exception e )
        {
            if ( Objects.notEquals ( e.getClass (), exception ) )
            {
                throw new CloneException ( "Unknown clone exception", e );
            }
        }
    }

    /**
     * Sample object for cloning.
     */
    public static class TestObject implements Cloneable
    {
        /**
         * Sample {@code boolean} data.
         */
        @OmitOnClone
        private final boolean bool;

        /**
         * Sample {@link String} data.
         */
        protected final String text;

        /**
         * Sample {@link Integer} data.
         */
        @OmitOnClone
        final Integer number;

        /**
         * Sample {@link List} data.
         */
        public transient final List<String> list;

        /**
         * Constructs new {@link TestObject}.
         *
         * @param bool   sample {@code boolean} data
         * @param text   sample {@link String} data
         * @param number sample {@link Integer} data
         * @param list   sample {@link List} data
         */
        public TestObject ( final boolean bool, final String text, final Integer number, final List<String> list )
        {
            this.bool = bool;
            this.text = text;
            this.number = number;
            this.list = list;
        }

        /**
         * Overridden to properly compare all data within {@link #checkCloneResult(Object, Object)}.
         */
        @Override
        public boolean equals ( final Object object )
        {
            return object instanceof TestObject &&
                    bool == ( ( TestObject ) object ).bool &&
                    Objects.equals ( text, ( ( TestObject ) object ).text ) &&
                    Objects.equals ( number, ( ( TestObject ) object ).number ) &&
                    CollectionUtils.equals ( list, ( ( TestObject ) object ).list, true );
        }

        @Override
        public String toString ()
        {
            return getClass ().getSimpleName () + "{" + "bool=" + bool + ", " + "text='" + text + "', " +
                    "number=" + number + ", " + "list=" + list + "}";
        }
    }

    /**
     * Sample self-referencing object for cloning.
     */
    public static class RecursiveTestObject implements Cloneable
    {
        /**
         * Sample {@link List} data.
         */
        private final List<RecursiveTestObject> list;

        /**
         * Constructs new {@link RecursiveTestObject}.
         */
        public RecursiveTestObject ()
        {
            this.list = new ArrayList<RecursiveTestObject> ( 3 );
            this.list.add ( this );
            this.list.add ( this );
            this.list.add ( this );
        }

        /**
         * Returns sample {@link List} data.
         *
         * @return sample {@link List} data
         */
        public List<RecursiveTestObject> getList ()
        {
            return list;
        }
    }
}