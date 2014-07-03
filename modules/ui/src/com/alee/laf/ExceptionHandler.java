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

import com.alee.managers.log.Log;

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

    protected void handleException ( final String tname, final Throwable thrown )
    {
        Log.error ( ExceptionHandler.class, "Exception in thread " + tname + ": ", thrown );
    }
}