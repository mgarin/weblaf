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

package com.alee.laf.edt;

import com.alee.utils.CollectionUtils;
import com.alee.utils.FileUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Custom handler for exceptions thrown when any UI operation is executed outside of the Event Dispatch Thread.
 * It filters out unique exceptions judging only by stack trace elements that reference classes within specified packages.
 *
 * @author Mikle Garin
 */
public abstract class PackageFilteringNonEventThreadHandler implements NonEventThreadHandler
{
    /**
     * Packages that should not be filtered out.
     */
    protected final List<String> packages;

    /**
     * List of unique stack trace keys which have already occured.
     */
    protected final Map<String, Long> occured;

    /**
     * Constructs new {@link PackageFilteringNonEventThreadHandler}.
     *
     * @param packages packages of the classes for which stack trace elements should not be filtered out
     */
    public PackageFilteringNonEventThreadHandler ( final String... packages )
    {
        super ();
        this.packages = CollectionUtils.asList ( packages );
        this.occured = new HashMap<String, Long> ( 1 );
    }

    @Override
    public void handle ( final RuntimeException e )
    {
        final String key = getKey ( e );
        final Long count = !occured.containsKey ( key ) ? 1L : occured.get ( key ) + 1L;
        occured.put ( key, count );
        handle ( e, count );
    }

    /**
     * Returns unique stack trace key.
     *
     * @param e exception to handle
     * @return unique stack trace key
     */
    protected String getKey ( final RuntimeException e )
    {
        if ( packages.size () > 0 )
        {
            // Filtering out elements
            final List<StackTraceElement> stackTrace = CollectionUtils.asList ( e.getStackTrace () );
            final Iterator<StackTraceElement> iterator = stackTrace.iterator ();
            while ( iterator.hasNext () )
            {
                final StackTraceElement element = iterator.next ();
                boolean accepted = false;
                for ( final String pkg : packages )
                {
                    if ( element.getClassName ().startsWith ( pkg ) )
                    {
                        accepted = true;
                        break;
                    }
                }
                if ( !accepted )
                {
                    iterator.remove ();
                }
            }

            // Calculating resulting key
            final StringBuilder key = new StringBuilder ( "" );
            for ( final StackTraceElement element : stackTrace )
            {
                key.append ( element.toString () );
            }

            // Returning MD5 as actual key
            return FileUtils.computeMD5 ( key.toString () );
        }
        return "none";
    }

    /**
     * Handles exceptions thrown when any UI operation is executed outside of the Event Dispatch Thread.
     * This method also receives exception occurences count in addition to the exception itself.
     *
     * @param e     exception to handle
     * @param count exception occurences count
     */
    protected abstract void handle ( RuntimeException e, Long count );
}