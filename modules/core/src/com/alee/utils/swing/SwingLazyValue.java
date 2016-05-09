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

import javax.swing.*;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * SwingLazyValue is a copy of ProxyLazyValue that does not snapshot the AccessControlContext or use a doPrivileged to resolve the class
 * name. It's intended for use in places in Swing where we need ProxyLazyValue, this should never be used in a place where the developer
 * could supply the arguments.
 *
 * @author Mikle Garin
 */
public class SwingLazyValue implements UIDefaults.LazyValue
{
    private final String className;
    private final String methodName;
    private Object[] args;

    public SwingLazyValue ( final String c )
    {
        this ( c, ( String ) null );
    }

    public SwingLazyValue ( final String c, final String m )
    {
        this ( c, m, null );
    }

    public SwingLazyValue ( final String c, final Object[] o )
    {
        this ( c, null, o );
    }

    public SwingLazyValue ( final String c, final String m, final Object[] o )
    {
        className = c;
        methodName = m;
        if ( o != null )
        {
            args = o.clone ();
        }
    }

    @Override
    public Object createValue ( final UIDefaults table )
    {
        try
        {
            final Class c = Class.forName ( className, true, null );
            if ( methodName != null )
            {
                final Class[] types = getClassArray ( args );
                final Method m = c.getMethod ( methodName, types );
                makeAccessible ( m );
                return m.invoke ( c, args );
            }
            else
            {
                final Class[] types = getClassArray ( args );
                final Constructor constructor = c.getConstructor ( types );
                makeAccessible ( constructor );
                return constructor.newInstance ( args );
            }
        }
        catch ( final Exception e )
        {
            // Ideally we would throw an exception, unfortunately
            // often times there are errors as an initial look and
            // feel is loaded before one can be switched. Perhaps a
            // flag should be added for debugging, so that if true
            // the exception would be thrown.
        }
        return null;
    }

    private void makeAccessible ( final AccessibleObject object )
    {
        AccessController.doPrivileged ( new PrivilegedAction<Void> ()
        {
            @Override
            public Void run ()
            {
                object.setAccessible ( true );
                return null;
            }
        } );
    }

    private Class[] getClassArray ( final Object[] args )
    {
        Class[] types = null;
        if ( args != null )
        {
            types = new Class[ args.length ];
            for ( int i = 0; i < args.length; i++ )
            {
                /* PENDING(ges): At present only the primitive types
                   used are handled correctly; this should eventually
                   handle all primitive types */
                if ( args[ i ] instanceof java.lang.Integer )
                {
                    types[ i ] = Integer.TYPE;
                }
                else if ( args[ i ] instanceof java.lang.Boolean )
                {
                    types[ i ] = Boolean.TYPE;
                }
                else if ( args[ i ] instanceof javax.swing.plaf.ColorUIResource )
                {
                    /* PENDING(ges) Currently the Reflection APIs do not
                       search superclasses of parameters supplied for
                       constructor/method lookup.  Since we only have
                       one case where this is needed, we substitute
                       directly instead of adding a massive amount
                       of mechanism for this.  Eventually this will
                       probably need to handle the general case as well.
                    */
                    types[ i ] = java.awt.Color.class;
                }
                else
                {
                    types[ i ] = args[ i ].getClass ();
                }
            }
        }
        return types;
    }
}