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

package com.alee.laf;

import org.slf4j.LoggerFactory;

/**
 * Custom exceptions handler.
 *
 * @author Mikle Garin
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler
{
    @Override
    public void uncaughtException ( final Thread thread, final Throwable thrown )
    {
        handleException ( thread.getName (), thrown );
    }

    /**
     * Handles {@link Throwable} that occured on the {@link Thread} with the specified name.
     *
     * @param tname  {@link Thread} name
     * @param thrown {@link Throwable} that occured on the {@link Thread}
     */
    protected void handleException ( final String tname, final Throwable thrown )
    {
        final String msg = "Exception in thread %s";
        LoggerFactory.getLogger ( ExceptionHandler.class ).error ( String.format ( msg, tname ), thrown );
    }
}