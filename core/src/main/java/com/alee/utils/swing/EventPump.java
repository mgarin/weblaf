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
 * @since 1.4
 */

public class EventPump implements InvocationHandler
{
    /**
     * Modal frame.
     */
    private Frame frame;

    /**
     * Constructs an event pump for modal frame.
     *
     * @param frame modal frame
     */
    public EventPump ( Frame frame )
    {
        this.frame = frame;
    }

    /**
     * Processes a method invocation on a proxy instance and returns the result. This method will be invoked on an invocation handler when
     * a
     * method is invoked on a proxy instance that it is associated with.
     *
     * @param proxy  proxy instance that the method was invoked on
     * @param method method instance corresponding to the interface method invoked on the proxy instance
     * @param args   an array of objects containing the values of the arguments passed in the method invocation on the proxy instance, or
     *               null if interface method takes no arguments
     * @return the value to return from the method invocation on the proxy instance
     * @throws Throwable the exception to throw from the method invocation on the proxy instance
     */
    public Object invoke ( Object proxy, Method method, Object[] args ) throws Throwable
    {
        return frame.isShowing () ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * A small hack to pump an event.
     * Reflection calls in this method has to be replaced once Sun provides a public API to pump events.
     *
     * @throws Exception
     */
    public void start () throws Exception
    {
        Class clazz = Class.forName ( "java.awt.Conditional" );
        Object conditional = Proxy.newProxyInstance ( clazz.getClassLoader (), new Class[]{ clazz }, this );
        Method pumpMethod = Class.forName ( "java.awt.EventDispatchThread" ).getDeclaredMethod ( "pumpEvents", new Class[]{ clazz } );
        pumpMethod.setAccessible ( true );
        pumpMethod.invoke ( Thread.currentThread (), conditional );
    }
}