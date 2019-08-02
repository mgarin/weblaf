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

package com.alee.managers.plugin;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * Custom {@link java.lang.ClassLoader} for plugins based on {@link URLClassLoader}.
 * This class loader is used when a sandbox is required for loaded plugins.
 *
 * @author Mikle Garin
 */
public class PluginClassLoader extends URLClassLoader
{
    /**
     * Constructs a new URLClassLoader for the given URLs. The URLs will be
     * searched in the order specified for classes and resources after first
     * searching in the specified parent class loader. Any URL that ends with
     * a '/' is assumed to refer to a directory. Otherwise, the URL is assumed
     * to refer to a JAR file which will be downloaded and opened as needed.
     *
     * <p>If there is a security manager, this method first
     * calls the security manager's {@code checkCreateClassLoader} method
     * to ensure creation of a class loader is allowed.
     *
     * @param urls   the URLs from which to load classes and resources
     * @param parent the parent class loader for delegation
     * @throws SecurityException if a security manager exists and its {@code checkCreateClassLoader}
     *                           method doesn't allow creation of a class loader
     * @see SecurityManager#checkCreateClassLoader
     */
    public PluginClassLoader ( final URL[] urls, final ClassLoader parent )
    {
        super ( urls, parent );
    }

    /**
     * Constructs a new URLClassLoader for the specified URLs using the
     * default delegation parent {@code ClassLoader}. The URLs will
     * be searched in the order specified for classes and resources after
     * first searching in the parent class loader. Any URL that ends with
     * a '/' is assumed to refer to a directory. Otherwise, the URL is
     * assumed to refer to a JAR file which will be downloaded and opened
     * as needed.
     *
     * <p>If there is a security manager, this method first
     * calls the security manager's {@code checkCreateClassLoader} method
     * to ensure creation of a class loader is allowed.
     *
     * @param urls the URLs from which to load classes and resources
     * @throws SecurityException if a security manager exists and its {@code checkCreateClassLoader}
     *                           method doesn't allow creation of a class loader.
     * @see SecurityManager#checkCreateClassLoader
     */
    public PluginClassLoader ( final URL[] urls )
    {
        super ( urls );
    }

    /**
     * Constructs a new URLClassLoader for the specified URLs, parent
     * class loader, and URLStreamHandlerFactory. The parent argument
     * will be used as the parent class loader for delegation. The
     * factory argument will be used as the stream handler factory to
     * obtain protocol handlers when creating new jar URLs.
     *
     * <p>If there is a security manager, this method first
     * calls the security manager's {@code checkCreateClassLoader} method
     * to ensure creation of a class loader is allowed.
     *
     * @param urls    the URLs from which to load classes and resources
     * @param parent  the parent class loader for delegation
     * @param factory the URLStreamHandlerFactory to use when creating URLs
     * @throws SecurityException if a security manager exists and its{@code checkCreateClassLoader}
     *                           method doesn't allow creation of a class loader.
     * @see SecurityManager#checkCreateClassLoader
     */
    public PluginClassLoader ( final URL[] urls, final ClassLoader parent, final URLStreamHandlerFactory factory )
    {
        super ( urls, parent, factory );
    }

    /**
     * Appends the specified URL to the list of URLs to search for classes and resources.
     *
     * @param url the URL to be added to the search path of URLs
     */
    @Override
    public void addURL ( final URL url )
    {
        super.addURL ( url );
    }
}