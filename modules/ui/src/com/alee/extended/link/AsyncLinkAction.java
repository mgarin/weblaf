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

package com.alee.extended.link;

import com.alee.utils.concurrent.DaemonThreadFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Base class for asynchronous {@link WebLink} actions.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebLink">How to use WebLink</a>
 * @see WebLink
 */

public abstract class AsyncLinkAction extends AbstractLinkAction
{
    /**
     * ExecutorService to limit simultaneously running threads.
     */
    protected static ExecutorService executorService = Executors.newFixedThreadPool ( 4, new DaemonThreadFactory ( "AsyncLinkAction" ) );

    /**
     * Constructs new {@link AsyncLinkAction}.
     */
    public AsyncLinkAction ()
    {
        super ( null, null );
    }

    /**
     * Constructs new {@link AsyncLinkAction}.
     *
     * @param icon {@link LinkAction} icon
     */
    public AsyncLinkAction ( final Icon icon )
    {
        super ( icon, null );
    }

    /**
     * Constructs new {@link AsyncLinkAction}.
     *
     * @param text {@link LinkAction} text
     */
    public AsyncLinkAction ( final String text )
    {
        super ( null, text );
    }

    /**
     * Constructs new {@link AsyncLinkAction}.
     *
     * @param icon {@link LinkAction} icon
     * @param text {@link LinkAction} text
     */
    public AsyncLinkAction ( final Icon icon, final String text )
    {
        super ( icon, text );
    }

    @Override
    public void linkExecuted ( final ActionEvent event )
    {
        executorService.execute ( new Runnable ()
        {
            @Override
            public void run ()
            {
                asyncLinkExecuted ( event );
            }
        } );
    }

    /**
     * Invoked when link is being executed asynchronously.
     *
     * @param event link execution event
     */
    protected abstract void asyncLinkExecuted ( ActionEvent event );
}