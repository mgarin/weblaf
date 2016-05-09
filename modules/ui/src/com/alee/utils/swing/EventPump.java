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

import java.awt.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Event pump for modal frame.
 *
 * @author Mikle Garin
 */

public class EventPump implements InvocationHandler
{
    /**
     * Modal frame.
     */
    private final Frame frame;

    /**
     * Constructs an event pump for modal frame.
     *
     * @param frame modal frame
     */
    public EventPump ( final Frame frame )
    {
        super ();
        this.frame = frame;
    }

    @Override
    public Object invoke ( final Object proxy, final Method method, final Object[] args ) throws Throwable
    {
        return frame.isShowing () ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * A small hack to pump an event.
     * Reflection calls in this method has to be replaced once Sun provides a public API to pump events.
     *
     * @throws java.lang.Exception in case event pumping has failed
     */
    public void start () throws Exception
    {
        final Class clazz = Class.forName ( "java.awt.Conditional" );
        final Object conditional = Proxy.newProxyInstance ( clazz.getClassLoader (), new Class[]{ clazz }, this );
        final Method pumpMethod = Class.forName ( "java.awt.EventDispatchThread" ).getDeclaredMethod ( "pumpEvents", new Class[]{ clazz } );
        pumpMethod.setAccessible ( true );
        pumpMethod.invoke ( Thread.currentThread (), conditional );
    }
}