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

package com.alee.extended.behavior;

import com.alee.api.annotations.NotNull;
import com.alee.api.jdk.Function;
import com.alee.api.jdk.Supplier;
import com.alee.api.jdk.UnsafeSupplier;
import com.alee.extended.syntax.WebSyntaxArea;
import com.alee.extended.syntax.WebSyntaxScrollPane;
import com.alee.extended.window.PopOverAlignment;
import com.alee.extended.window.PopOverDirection;
import com.alee.extended.window.WebPopOver;
import com.alee.laf.label.WebLabel;
import com.alee.managers.style.StyleId;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.ExceptionUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.TextUtils;
import com.alee.utils.concurrent.DaemonThreadFactory;
import com.alee.utils.swing.MouseButton;
import com.alee.utils.swing.extensions.MouseEventRunnable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.alee.extended.syntax.SyntaxPreset.*;

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
    protected static ExecutorService executorService = Executors.newFixedThreadPool ( 4, new DaemonThreadFactory ( "LazyLoadBehavior" ) );

    /**
     * Performs lazy UI component load.
     * Also uses error fallback for the case when data load has failed.
     *
     * @param container      container for component
     * @param constraints    component constraints
     * @param loaderSupplier loader component supplier, it is executed in EDT
     * @param dataSupplier   data supplier, it is executed in a separate thread
     * @param dataHandler    data component provider, it is executed in EDT
     * @param <D>            loaded data type
     */
    public static <D> void perform ( final Container container, final Object constraints, final Supplier<JComponent> loaderSupplier,
                                     final UnsafeSupplier<D> dataSupplier, final Function<D, JComponent> dataHandler )
    {
        perform ( container, constraints, loaderSupplier, dataSupplier, dataHandler, new Function<Throwable, JComponent> ()
        {
            @Override
            public JComponent apply ( final Throwable throwable )
            {
                final WebLabel error = new WebLabel ( TextUtils.shortenText ( throwable.getMessage (), 100, true ) );
                error.onMousePress ( MouseButton.left, new MouseEventRunnable ()
                {
                    @Override
                    public void run ( @NotNull final MouseEvent e )
                    {
                        final WebPopOver info = new WebPopOver ( error );
                        final WebSyntaxArea area = new WebSyntaxArea ( ExceptionUtils.getStackTrace ( throwable ), 12, 60 );
                        area.applyPresets ( base, viewable, hideMenu, ideaTheme, transparent );
                        info.add ( new WebSyntaxScrollPane ( StyleId.syntaxareaScrollUndecorated, area, false ) );
                        info.show ( error, PopOverDirection.down, PopOverAlignment.centered );
                    }
                } );
                return error;
            }
        } );
    }

    /**
     * Performs lazy UI component load.
     * Also uses error fallback for the case when data load has failed.
     * It will provide temporary loader component in place of final component until its data is fully loaded.
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
                                     final UnsafeSupplier<D> dataSupplier, final Function<D, JComponent> dataHandler,
                                     final Function<Throwable, JComponent> errorHandler )
    {
        CoreSwingUtils.invokeAndWait ( new Runnable ()
        {
            @Override
            public void run ()
            {
                // Adding loader into container
                final JComponent loader = loaderSupplier.get ();
                container.add ( loader, constraints );
                SwingUtils.update ( container );

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
                            CoreSwingUtils.invokeLater ( new Runnable ()
                            {
                                @Override
                                public void run ()
                                {
                                    final JComponent component = dataHandler.apply ( data );
                                    container.remove ( loader );
                                    container.add ( component, constraints, index );
                                    SwingUtils.update ( container );
                                }
                            } );
                        }
                        catch ( final Exception e )
                        {
                            CoreSwingUtils.invokeLater ( new Runnable ()
                            {
                                @Override
                                public void run ()
                                {
                                    final JComponent error = errorHandler.apply ( e );
                                    container.remove ( loader );
                                    container.add ( error, constraints, index );
                                    SwingUtils.update ( container );
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