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

import com.alee.api.annotations.NotNull;
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
    @NotNull
    protected final List<String> packages;

    /**
     * List of unique stack trace keys which have already occurred.
     */
    @NotNull
    protected final Map<String, Long> occurred;

    /**
     * Constructs new {@link PackageFilteringNonEventThreadHandler}.
     *
     * @param packages packages of the classes for which stack trace elements should not be filtered out
     */
    public PackageFilteringNonEventThreadHandler ( @NotNull final String... packages )
    {
        this.packages = CollectionUtils.asList ( packages );
        this.occurred = new HashMap<String, Long> ( 1 );
    }

    @Override
    public void handle ( @NotNull final RuntimeException e )
    {
        final String key = getKey ( e );
        final long count = !occurred.containsKey ( key ) ? 1L : occurred.get ( key ) + 1L;
        occurred.put ( key, count );
        handle ( e, count );
    }

    /**
     * Returns unique stack trace key.
     *
     * @param e exception to handle
     * @return unique stack trace key
     */
    @NotNull
    protected String getKey ( @NotNull final RuntimeException e )
    {
        final String result;
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
            final StringBuilder key = new StringBuilder ();
            for ( final StackTraceElement element : stackTrace )
            {
                key.append ( element.toString () );
            }

            // Returning MD5 as actual key
            result = FileUtils.computeMD5 ( key.toString () );
        }
        else
        {
            result = "none";
        }
        return result;
    }

    /**
     * Handles exceptions thrown when any UI operation is executed outside of the Event Dispatch Thread.
     * This method also receives exception occurrences count in addition to the exception itself.
     *
     * @param e     exception to handle
     * @param count exception occurrences count
     */
    protected abstract void handle ( @NotNull RuntimeException e, long count );
}