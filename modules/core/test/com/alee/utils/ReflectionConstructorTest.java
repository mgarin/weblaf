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

package com.alee.utils;

import com.alee.api.jdk.Objects;
import com.alee.utils.classes.ClassData;
import com.alee.utils.reflection.ReflectionException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.lang.reflect.InvocationTargetException;

/**
 * Set of JUnit tests for {@link ReflectUtils}.
 *
 * @author Mikle Garin
 */
@FixMethodOrder ( MethodSorters.JVM )
public final class ReflectionConstructorTest
{
    /**
     * Tests public class construction.
     *
     * @throws ClassNotFoundException    when {@link Class} cannot be found
     * @throws NoSuchMethodException     when {@link Class} constructor cannot be found
     * @throws InstantiationException    when {@link Class} instantiation failed
     * @throws IllegalAccessException    when {@link Class} or its constructor is not accessible
     * @throws InvocationTargetException when {@link Class} constructor call failed
     */
    @Test
    public void publicClassConstructor ()
            throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException
    {
        final String className = "com.alee.utils.classes.PublicClassConstructor";
        final ClassData instance = ReflectUtils.createInstance ( className );
        checkConstructionResult ( instance.get (), ClassData.SUCCESS );
    }

    /**
     * Tests private class construction.
     *
     * @throws ClassNotFoundException    when {@link Class} cannot be found
     * @throws NoSuchMethodException     when {@link Class} constructor cannot be found
     * @throws InstantiationException    when {@link Class} instantiation failed
     * @throws IllegalAccessException    when {@link Class} or its constructor is not accessible
     * @throws InvocationTargetException when {@link Class} constructor call failed
     */
    @Test
    public void privateClassConstructor ()
            throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException
    {
        final String className = "com.alee.utils.classes.PrivateClassConstructor";
        final ClassData instance = ReflectUtils.createInstance ( className );
        checkConstructionResult ( instance.get (), ClassData.SUCCESS );
    }

    /**
     * Tests private class construction.
     *
     * @throws ClassNotFoundException    when {@link Class} cannot be found
     * @throws NoSuchMethodException     when {@link Class} constructor cannot be found
     * @throws InstantiationException    when {@link Class} instantiation failed
     * @throws IllegalAccessException    when {@link Class} or its constructor is not accessible
     * @throws InvocationTargetException when {@link Class} constructor call failed
     */
    @Test
    public void innerStaticClassConstructor ()
            throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException
    {
        final String className = "com.alee.utils.classes.InnerStaticClassConstructor$InnerClass";
        final ClassData instance = ReflectUtils.createInstance ( className );
        checkConstructionResult ( instance.get (), ClassData.SUCCESS );
    }

    /**
     * Tests private class construction.
     *
     * @throws ClassNotFoundException    when {@link Class} cannot be found
     * @throws NoSuchMethodException     when {@link Class} constructor cannot be found
     * @throws InstantiationException    when {@link Class} instantiation failed
     * @throws IllegalAccessException    when {@link Class} or its constructor is not accessible
     * @throws InvocationTargetException when {@link Class} constructor call failed
     */
    @Test
    public void innerClassConstructor ()
            throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException
    {
        final String className = "com.alee.utils.classes.InnerClassConstructor";
        final Object instance = ReflectUtils.createInstance ( className );
        final String innerClassName = className + "$InnerClass";
        final ClassData innerInstance = ReflectUtils.createInstance ( innerClassName, instance );
        checkConstructionResult ( innerInstance.get (), ClassData.SUCCESS );
    }

    /**
     * Asserts construction result.
     *
     * @param result   merge result
     * @param expected expected result
     */
    private void checkConstructionResult ( final Object result, final Object expected )
    {
        if ( Objects.notEquals ( result, expected ) )
        {
            throw new ReflectionException ( String.format (
                    "Unexpected class construction result: %s" + "\n" + "Expected result: %s",
                    result, expected
            ) );
        }
    }
}