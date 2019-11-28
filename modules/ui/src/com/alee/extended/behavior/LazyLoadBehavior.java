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
import com.alee.api.annotations.Nullable;
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
import com.alee.managers.task.TaskGroup;
import com.alee.managers.task.TaskManager;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.ExceptionUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.TextUtils;
import com.alee.utils.swing.MouseButton;
import com.alee.utils.swing.extensions.MouseEventRunnable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

import static com.alee.extended.syntax.SyntaxPreset.*;

/**
 * Lazy component load behavior which provides a quick way to load pre-load component with heavy data into UI.
 *
 * @author Mikle Garin
 * @deprecated Practically replaced with better implemented {@link com.alee.extended.lazy.LazyContent} feature
 */
@Deprecated
public class LazyLoadBehavior implements Behavior
{
    /**
     * Performs lazy loading of data and UI element for it.
     * Also uses error fallback for the case when data load has failed.
     * It will also provide temporary loader component in place of final component until its data is fully loaded.
     *
     * @param container      {@link Container} for resulting {@link JComponent}
     * @param constraints    constraints for resulting {@link JComponent} in {@link Container}
     * @param loaderSupplier {@link Supplier} for loader {@link JComponent}, it is executed in EDT
     * @param dataSupplier   {@link UnsafeSupplier} for data, it is executed in a separate thread
     * @param dataHandler    data {@link JComponent} provider, it is executed in EDT
     * @param groupId        identifier of {@link TaskGroup} to execute {@link Runnable} on
     * @param <D>            loaded data type
     */
    public static <D> void perform ( @NotNull final Container container, @NotNull final Object constraints,
                                     @NotNull final Supplier<JComponent> loaderSupplier, @NotNull final UnsafeSupplier<D> dataSupplier,
                                     @NotNull final Function<D, JComponent> dataHandler, @NotNull final String groupId )
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
                        area.applyPresets ( base, viewable, hideMenu, ideaTheme, nonOpaque );
                        info.add ( new WebSyntaxScrollPane ( StyleId.syntaxareaScrollUndecorated, area, false ) );
                        info.show ( error, PopOverDirection.down, PopOverAlignment.centered );
                    }
                } );
                return error;
            }
        }, groupId );
    }

    /**
     * Performs lazy loading of data and UI element for it.
     * Also uses error fallback for the case when data load has failed.
     * It will also provide temporary loader component in place of final component until its data is fully loaded.
     *
     * @param container      {@link Container} for resulting {@link JComponent}
     * @param constraints    constraints for resulting {@link JComponent} in {@link Container}
     * @param loaderSupplier {@link Supplier} for loader {@link JComponent}, it is executed in EDT
     * @param dataSupplier   {@link UnsafeSupplier} for data, it is executed in a separate thread
     * @param dataHandler    data {@link JComponent} provider, it is executed in EDT
     * @param errorHandler   error {@link JComponent} provider, it is executed in EDT
     * @param groupId        identifier of {@link TaskGroup} to execute {@link Runnable} on
     * @param <D>            loaded data type
     */
    public static <D> void perform ( @NotNull final Container container, @Nullable final Object constraints,
                                     @NotNull final Supplier<JComponent> loaderSupplier, @NotNull final UnsafeSupplier<D> dataSupplier,
                                     @NotNull final Function<D, JComponent> dataHandler,
                                     @NotNull final Function<Throwable, JComponent> errorHandler, @NotNull final String groupId )
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
                TaskManager.execute ( groupId, new Runnable ()
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
                                    show ( dataHandler.apply ( data ) );
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
                                    show ( errorHandler.apply ( e ) );
                                }
                            } );
                        }
                    }

                    /**
                     * Displays resulting {@link JComponent}.
                     *
                     * @param component resulting {@link JComponent}
                     */
                    private void show ( final JComponent component )
                    {
                        container.remove ( loader );
                        container.add ( component, constraints, index );
                        SwingUtils.update ( container );
                    }
                } );
            }
        } );
    }
}