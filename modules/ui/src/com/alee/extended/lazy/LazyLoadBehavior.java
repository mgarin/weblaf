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

import com.alee.api.Behavior;
import com.alee.api.Function;
import com.alee.api.Supplier;
import com.alee.utils.SwingUtils;
import com.alee.utils.concurrent.DaemonThreadFactory;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Lazy component load behavior which provides a quick way to load pre-load component with heavy data into UI.
 *
 * @author Mikle Garin
 */

public class LazyLoadBehavior implements Behavior
{
    /**
     * ExecutorService to limit simultaneously running threads.
     */
    protected static ExecutorService executorService = Executors.newFixedThreadPool ( 4, new DaemonThreadFactory () );

    /**
     * Performs lazy UI component load.
     * It will provide temporary loader component in place of final component until its data is fully loaded.
     * It also provides an error fallback for the case when data load has failed.
     *
     * @param container      container for component
     * @param constraints    component constraints
     * @param loaderSupplier loader component supplier, it is executed in EDT
     * @param dataSupplier   data supplier, it is executed in a separate thread
     * @param dataHandler    data component provider, it is executed in EDT
     * @param errorHandler   error component provider, it is executed in EDT
     * @param <D>            loaded data type
     */
    public static <D> void perform ( final Container container, final Object constraints, final Supplier<JComponent> loaderSupplier,
                                     final Supplier<D> dataSupplier, final Function<D, JComponent> dataHandler,
                                     final Function<Throwable, JComponent> errorHandler )
    {
        SwingUtils.invokeAndWaitSafely ( new Runnable ()
        {
            @Override
            public void run ()
            {
                // Adding loader into container
                final JComponent loader = loaderSupplier.get ();
                container.add ( loader, constraints );
                container.invalidate ();
                container.repaint ();

                // Saving loader index to avoid layer issues
                final int index = container.getComponentZOrder ( loader );

                // Loading data
                executorService.submit ( new Runnable ()
                {
                    @Override
                    public void run ()
                    {
                        try
                        {
                            final D data = dataSupplier.get ();
                            SwingUtils.invokeLater ( new Runnable ()
                            {
                                @Override
                                public void run ()
                                {
                                    final JComponent component = dataHandler.apply ( data );
                                    container.remove ( loader );
                                    container.add ( component, constraints, index );
                                    container.invalidate ();
                                    container.repaint ();
                                }
                            } );
                        }
                        catch ( final Throwable e )
                        {
                            SwingUtils.invokeLater ( new Runnable ()
                            {
                                @Override
                                public void run ()
                                {
                                    final JComponent error = errorHandler.apply ( e );
                                    container.remove ( loader );
                                    container.add ( error, constraints, index );
                                    container.invalidate ();
                                    container.repaint ();
                                }
                            } );
                        }
                    }
                } );
            }
        } );
    }

    /**
     * Sets executor service for data loading threads.
     *
     * @param service executor service for data loading threads
     */
    public static void setExecutorService ( final ExecutorService service )
    {
        LazyLoadBehavior.executorService = service;
    }
}