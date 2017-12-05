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

package com.alee.utils.swing;

import com.alee.utils.ReflectUtils;

import javax.swing.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * {@link SwingLazyValue} is a copy of {@link javax.swing.UIDefaults.ProxyLazyValue} that does not snapshot the
 * {@link java.security.AccessControlContext} or use doPrivileged to resolve the class name. It's intended for use in places in Swing where
 * we need {@link javax.swing.UIDefaults.ProxyLazyValue} and should never be used in a place where the developer could supply the arguments.
 *
 * @author Mikle Garin
 * @see javax.swing.UIDefaults.ProxyLazyValue
 */

public final class SwingLazyValue implements UIDefaults.LazyValue
{
    /**
     * Lazy value class name or name of the class for the static method invocation.
     */
    private final String className;

    /**
     * Lazy value class static method name.
     * If specified, it will be invoked to create the actual value.
     */
    private final String methodName;

    /**
     * Class constructor or static method arguments.
     */
    private final Object[] arguments;

    /**
     * Constructs {@link SwingLazyValue} that will instantiate {@link Class} with the specified name as value.
     *
     * @param className {@link Class} name
     */
    public SwingLazyValue ( final String className )
    {
        this ( className, ( String ) null );
    }

    /**
     * Constructs {@link SwingLazyValue} that will retrieve value from a static {@link Method} from the class with the specified name.
     *
     * @param className  {@link Class} name
     * @param methodName {@link Method} name
     */
    public SwingLazyValue ( final String className, final String methodName )
    {
        this ( className, methodName, null );
    }

    /**
     * Constructs {@link SwingLazyValue} that will instantiate {@link Class} with the specified name and arguments as value.
     *
     * @param className {@link Class} name
     * @param arguments {@link Class} constructor arguments
     */
    public SwingLazyValue ( final String className, final Object[] arguments )
    {
        this ( className, null, arguments );
    }

    /**
     * Constructs {@link SwingLazyValue} that will retrieve value from a static {@link Method} from the class with the specified name.
     *
     * @param className  {@link Class} name
     * @param methodName {@link Method} name
     * @param arguments  {@link Method} arguments
     */
    public SwingLazyValue ( final String className, final String methodName, final Object[] arguments )
    {
        this.className = className;
        this.methodName = methodName;
        this.arguments = arguments != null ? arguments.clone () : null;
    }

    @Override
    public Object createValue ( final UIDefaults table )
    {
        Object value = null;
        try
        {
            final Class clazz = Class.forName ( className, true, null );
            if ( methodName != null )
            {
                final Method m = ReflectUtils.getMethod ( clazz, methodName, arguments );
                value = m.invoke ( clazz, arguments );
            }
            else
            {
                final Class[] types = ReflectUtils.getClassTypes ( arguments );
                final Constructor constructor = ReflectUtils.getConstructor ( clazz, types );
                value = constructor.newInstance ( arguments );
            }
        }
        catch ( final Exception e )
        {
            /**
             * Ideally we would throw an exception, unfortunately often times there are errors as an initial look and feel is loaded
             * before one can be switched. Perhaps a flag should be added for debugging, so that if true the exception would be thrown.
             */
        }
        return value;
    }
}