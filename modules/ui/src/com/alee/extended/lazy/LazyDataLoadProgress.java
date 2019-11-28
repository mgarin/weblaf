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

package com.alee.extended.lazy;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.utils.CoreSwingUtils;

import javax.swing.event.EventListenerList;

/**
 * {@link DataLoadProgress} and {@link ProgressCallback} implementation that translates received events to progress UI.
 *
 * @param <D> data type
 * @author Mikle Garin
 */
public class LazyDataLoadProgress<D> implements DataLoadProgress<D>, ProgressCallback
{
    /**
     * Last provided total amount of data chunks to be loaded or {@code -1} if it was never specified.
     */
    protected int total;

    /**
     * Last provided amount of loaded data chunks or {@code -1} if it was never specified.
     */
    protected int progress;

    /**
     * Last provided additional progress data or {@code null} if it was never specified.
     */
    @Nullable
    protected Object[] progressData;

    /**
     * {@link EventListenerList} for {@link DataLoadListener}s.
     */
    @Nullable
    protected EventListenerList listeners;

    /**
     * Constructs new {@link LazyDataLoadProgress}.
     */
    public LazyDataLoadProgress ()
    {
        this ( -1, -1, ( Object[] ) null );
    }

    /**
     * Constructs new {@link LazyDataLoadProgress}.
     *
     * @param total    total amount of data chunks to be loaded or {@code -1}
     * @param progress amount of loaded data chunks or {@code -1}
     * @param data     additional progress data or {@code null}
     */
    public LazyDataLoadProgress ( final int total, final int progress, @Nullable final Object... data )
    {
        setTotal ( total );
        setProgress ( progress, data );
    }

    @Override
    public int getTotal ()
    {
        return total;
    }

    @Override
    public int getProgress ()
    {
        return progress;
    }

    @Override
    @Nullable
    public Object[] getProgressData ()
    {
        return progressData;
    }

    @Override
    public void setTotal ( final int total )
    {
        LazyDataLoadProgress.this.total = total;
        CoreSwingUtils.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                fireTotalChanged ( total );
            }
        } );
    }

    @Override
    public void setProgress ( final int progress, @Nullable final Object... data )
    {
        if ( total == -1 || progress <= total )
        {
            LazyDataLoadProgress.this.progress = progress;
            LazyDataLoadProgress.this.progressData = data;
            CoreSwingUtils.invokeLater ( new Runnable ()
            {
                @Override
                public void run ()
                {
                    fireProgressChanged ( progress, data );
                }
            } );
        }
        else
        {
            throw new RuntimeException ( "Total must be equal or greater than progress" );
        }
    }

    @Override
    public void addListener ( @NotNull final DataLoadListener<D> listener )
    {
        if ( listeners == null )
        {
            listeners = new EventListenerList ();
        }
        listeners.add ( DataLoadListener.class, listener );
    }

    @Override
    public void removeListener ( @NotNull final DataLoadListener<D> listener )
    {
        if ( listeners != null )
        {
            listeners.remove ( DataLoadListener.class, listener );
        }
    }

    /**
     * Informs about data loading start.
     */
    public void fireLoadingStarted ()
    {
        if ( listeners != null )
        {
            for ( final DataLoadListener<D> listener : listeners.getListeners ( DataLoadListener.class ) )
            {
                listener.loadingStarted ();
            }
        }
    }

    /**
     * Informs about total amount of data chunks to be loaded change.
     *
     * @param total total amount of data chunks to be loaded
     */
    public void fireTotalChanged ( final int total )
    {
        if ( listeners != null )
        {
            for ( final DataLoadListener<D> listener : listeners.getListeners ( DataLoadListener.class ) )
            {
                listener.totalChanged ( total );
            }
        }
    }

    /**
     * Informs about amount of loaded data chunks change.
     *
     * @param progress current amount of loaded data chunks
     * @param data     additional progress data
     */
    public void fireProgressChanged ( final int progress, @Nullable final Object... data )
    {
        if ( listeners != null )
        {
            for ( final DataLoadListener<D> listener : listeners.getListeners ( DataLoadListener.class ) )
            {
                listener.progressChanged ( progress, data );
            }
        }
    }

    /**
     * Informs about data loading completion.
     *
     * @param data loaded data
     */
    public void fireLoaded ( @Nullable final D data )
    {
        if ( listeners != null )
        {
            for ( final DataLoadListener<D> listener : listeners.getListeners ( DataLoadListener.class ) )
            {
                listener.loaded ( data );
            }
        }
    }

    /**
     * Informs about data loading failure.
     *
     * @param cause {@link Throwable} cause
     */
    public void fireFailed ( @NotNull final Throwable cause )
    {
        if ( listeners != null )
        {
            for ( final DataLoadListener<D> listener : listeners.getListeners ( DataLoadListener.class ) )
            {
                listener.failed ( cause );
            }
        }
    }
}