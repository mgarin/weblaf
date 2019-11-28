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
import com.alee.extended.behavior.VisibilityBehavior;
import com.alee.extended.syntax.WebSyntaxArea;
import com.alee.extended.syntax.WebSyntaxScrollPane;
import com.alee.extended.window.PopOverAlignment;
import com.alee.extended.window.PopOverDirection;
import com.alee.extended.window.WebPopOver;
import com.alee.laf.WebLookAndFeel;
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
import javax.swing.event.EventListenerList;
import java.awt.event.MouseEvent;

import static com.alee.extended.syntax.SyntaxPreset.*;

/**
 * Custom utility allowing convenient UI initialization involving heavy data loading.
 * It can also be used to hide UI parts behind lazy load until they become necessary.
 *
 * @param <D> data type
 * @param <C> {@link JComponent} type
 * @author Mikle Garin
 */
public abstract class LazyContent<D, C extends JComponent>
{
    /**
     * {@link JComponent} container to add resulting {@link JComponent} into.
     */
    @NotNull
    protected final JComponent container;

    /**
     * Constraints to add resulting {@link JComponent} under.
     */
    @Nullable
    protected final Object constraints;

    /**
     * Data loading trigger.
     */
    @NotNull
    protected final LazyLoadTrigger dataTrigger;

    /**
     * {@link JComponent} loading trigger.
     * Note that {@link JComponent} cannot be loaded before data loading is completed.
     * That means that even if this trigger can happen earlier it will have no effect until data is fully loaded.
     */
    @NotNull
    protected final LazyLoadTrigger uiTrigger;

    /**
     * {@link EventListenerList} for various {@link LazyContent} listeners.
     */
    @NotNull
    protected final EventListenerList listeners;

    /**
     * {@link VisibilityBehavior} for queuing load for {@link LazyLoadTrigger#onDisplay} setting.
     */
    @Nullable
    protected VisibilityBehavior<JComponent> onDisplayBehavior;

    /**
     * Current {@link LazyState} for data.
     */
    @NotNull
    protected LazyState dataState;

    /**
     * Current {@link LazyState} for UI elements.
     */
    @NotNull
    protected LazyState uiState;

    /**
     * Loaded data.
     */
    @Nullable
    protected D data;

    /**
     * Data load failure cause.
     */
    @Nullable
    protected Throwable dataCause;

    /**
     * {@link JComponent} currently placed as content.
     * This can be actual content {@link JComponent}, progress or exception component.
     */
    @Nullable
    protected JComponent content;

    /**
     * Z-index of {@link JComponent} currently placed as content.
     * Used to correctly restore Z-index to avoid issues with layouts relying on Z-index instead of constraints.
     */
    protected int contentIndex;

    /**
     * Content {@link JComponent} load failure cause.
     */
    @Nullable
    protected Throwable contentCause;

    /**
     * Constructs new {@link LazyContent}.
     *
     * @param container   {@link JComponent} container to add resulting {@link JComponent} into
     * @param constraints constraints to add resulting {@link JComponent} under
     * @param trigger     {@link LazyLoadTrigger} for lazy data and UI elements
     */
    public LazyContent ( @NotNull final JComponent container, @Nullable final Object constraints, @NotNull final LazyLoadTrigger trigger )
    {
        this ( container, constraints, trigger, trigger );
    }

    /**
     * Constructs new {@link LazyContent}.
     *
     * @param container   {@link JComponent} container to add resulting {@link JComponent} into
     * @param constraints constraints to add resulting {@link JComponent} under
     * @param dataTrigger {@link LazyLoadTrigger} for lazy data
     * @param uiTrigger   {@link LazyLoadTrigger} for UI elements
     */
    public LazyContent ( @NotNull final JComponent container, @Nullable final Object constraints,
                         @NotNull final LazyLoadTrigger dataTrigger, @NotNull final LazyLoadTrigger uiTrigger )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Immutable fields
        this.container = container;
        this.constraints = constraints;
        this.dataTrigger = dataTrigger;
        this.uiTrigger = uiTrigger;
        this.listeners = new EventListenerList ();

        // Initial load states
        this.dataState = LazyState.awaiting;
        this.uiState = LazyState.awaiting;

        // Initial component
        final JComponent initialComponent = createInitialComponent ();
        if ( initialComponent != null )
        {
            this.content = initialComponent;
            this.container.add ( initialComponent, constraints );
            this.contentIndex = this.container.getComponentZOrder ( initialComponent );
        }
        else
        {
            this.content = null;
            this.contentIndex = -1;
        }

        // Queuing data loading
        if ( this.dataTrigger == LazyLoadTrigger.onInit )
        {
            // Loading asap
            queueDataLoading ( false );
        }
        else if ( this.dataTrigger == LazyLoadTrigger.onDisplay )
        {
            // Loading data upon display
            onDisplayBehavior = new VisibilityBehavior<JComponent> ( this.container, true )
            {
                @Override
                protected void displayed ( @NotNull final JComponent component )
                {
                    queueDataLoading ( false );
                }
            };
            onDisplayBehavior.install ();
        }
    }

    /**
     * Returns {@link LazyState} for data.
     *
     * @return {@link LazyState} for data
     */
    @NotNull
    public LazyState getDataState ()
    {
        return dataState;
    }

    /**
     * Updates {@link LazyState} for data.
     * Each state change is performed on a separate invoke to EDT for better UI responsiveness.
     *
     * @param state new {@link LazyState} for data
     */
    protected void setDataState ( @NotNull final LazyState state )
    {
        WebLookAndFeel.checkEventDispatchThread ();
        final LazyState old = this.dataState;
        dataState = state;
        fireDataStateChanged ( old, state );
    }

    /**
     * Returns {@link LazyState} for UI elements.
     *
     * @return {@link LazyState} for UI elements
     */
    @NotNull
    public LazyState getUIState ()
    {
        return uiState;
    }

    /**
     * Updates {@link LazyState} for UI elements.
     * Each state change is performed on a separate invoke to EDT for better UI responsiveness.
     *
     * @param state new {@link LazyState} for UI elements
     */
    protected void setUIState ( @NotNull final LazyState state )
    {
        WebLookAndFeel.checkEventDispatchThread ();
        final LazyState old = this.uiState;
        uiState = state;
        fireUIStateChanged ( old, state );
    }

    /**
     * Manually queues data or UI loading, whichever is necessary.
     * If data and UI are already loaded they will be fully reloaded.
     * Also ensures that actual operation is performed on Event Dispatch Thread if it isn't called from it.
     */
    public void reload ()
    {
        CoreSwingUtils.invokeOnEventDispatchThread ( new Runnable ()
        {
            @Override
            public void run ()
            {
                if ( dataState != LazyState.queued && dataState != LazyState.loading &&
                        uiState != LazyState.queued && uiState != LazyState.loading )
                {
                    if ( dataState == LazyState.awaiting || dataState == LazyState.failed ||
                            dataState == LazyState.loaded && uiState == LazyState.loaded )
                    {
                        queueDataLoading ( true );
                    }
                    else if ( dataState == LazyState.loaded && ( uiState == LazyState.awaiting || uiState == LazyState.failed ) )
                    {
                        queueUILoading ( true );
                    }
                }
            }
        } );
    }

    /**
     * Queues data loading.
     * This method is always executed on EDT.
     * This method can be called again to reload data if previous loading completed or failed.
     *
     * @param forced whether or not data loading is forced
     */
    protected void queueDataLoading ( final boolean forced )
    {
        WebLookAndFeel.checkEventDispatchThread ();

        // Ignore the request if data is not in appropriate state
        final LazyState dataState = getDataState ();
        if ( dataState == LazyState.awaiting || forced && ( dataState == LazyState.loaded || dataState == LazyState.failed ) )
        {
            // Destroy on-display behavior as it isn't necessary anymore
            if ( onDisplayBehavior != null )
            {
                onDisplayBehavior.uninstall ();
                onDisplayBehavior = null;
            }

            // Updating states
            setDataState ( LazyState.queued );
            setUIState ( forced || uiTrigger == LazyLoadTrigger.onInit || uiTrigger == LazyLoadTrigger.onDisplay && container.isShowing ()
                    ? LazyState.queued : LazyState.awaiting );

            // Adding visual progress
            final LazyDataLoadProgress<D> progress = createDataLoadProgress ();
            setCurrentContent ( createProgressComponent ( progress ) );

            // Queuing data loading
            TaskManager.execute ( getTaskGroupId (), new Runnable ()
            {
                @Override
                public void run ()
                {
                    try
                    {
                        // Marking state as loading
                        CoreSwingUtils.invokeLater ( new Runnable ()
                        {
                            @Override
                            public void run ()
                            {
                                // Marking state as loading
                                setDataState ( LazyState.loading );

                                // Informing progress
                                progress.fireLoadingStarted ();
                            }
                        } );

                        // Loading data
                        final D data = loadData ( progress );

                        // Saving data
                        LazyContent.this.data = data;

                        // Performing UI updates
                        CoreSwingUtils.invokeLater ( new Runnable ()
                        {
                            @Override
                            public void run ()
                            {
                                // Marking state as loaded
                                setDataState ( LazyState.loaded );

                                // Informing progress
                                progress.fireLoaded ( data );

                                // Progress component is kept until UI is loaded
                                // removeCurrentContent ();

                                // Firing event
                                fireDataLoaded ( data );

                                // Proceeding to loading UI if necessary
                                if ( forced || uiState == LazyState.queued )
                                {
                                    // Loading component right away
                                    queueUILoading ( forced );
                                }
                                else if ( uiTrigger == LazyLoadTrigger.onDisplay )
                                {
                                    // Loading UI upon display
                                    onDisplayBehavior = new VisibilityBehavior<JComponent> ( container, true )
                                    {
                                        @Override
                                        protected void displayed ( @NotNull final JComponent component )
                                        {
                                            queueUILoading ( false );
                                        }
                                    };
                                    onDisplayBehavior.install ();
                                }
                            }
                        } );
                    }
                    catch ( final Throwable cause )
                    {
                        // Saving data
                        dataCause = cause;

                        // Performing UI updates
                        CoreSwingUtils.invokeLater ( new Runnable ()
                        {
                            @Override
                            public void run ()
                            {
                                // Marking state as failed
                                setDataState ( LazyState.failed );

                                // Returning UI state to awaiting in any case
                                setUIState ( LazyState.awaiting );

                                // Notifying progress
                                progress.fireFailed ( cause );

                                // Adding exception component
                                setCurrentContent ( createExceptionComponent ( dataCause ) );

                                // Firing event
                                fireDataFailed ( dataCause );
                            }
                        } );
                    }
                }
            } );
        }
    }

    /**
     * Queues UI elements loading and ensures it is performed on EDT.
     *
     * @param forced whether or not UI elements loading is forced
     */
    protected void queueUILoading ( final boolean forced )
    {
        WebLookAndFeel.checkEventDispatchThread ();

        // We need to make sure that data is available first
        final LazyState dataState = getDataState ();
        if ( dataState == LazyState.loaded )
        {
            // Destroy on-display behavior as it isn't necessary anymore
            if ( onDisplayBehavior != null )
            {
                onDisplayBehavior.uninstall ();
                onDisplayBehavior = null;
            }

            // Loading component if possible
            final LazyState uiState = getUIState ();
            if ( uiState == LazyState.awaiting || uiState == LazyState.queued ||
                    forced && ( uiState == LazyState.loaded || uiState == LazyState.failed ) )
            {
                // Marking state as queued
                setUIState ( LazyState.loading );

                // Separating UI loading to allow all previous updates to be finished
                CoreSwingUtils.invokeLater ( new Runnable ()
                {
                    @Override
                    public void run ()
                    {
                        try
                        {
                            // Loading component
                            final C content = loadUI ( data );
                            setCurrentContent ( content );

                            // Marking state as loaded
                            setUIState ( LazyState.loaded );

                            // Firing event
                            fireUILoaded ( data, content );
                        }
                        catch ( final Throwable cause )
                        {
                            // Must synchronize data and state changes
                            // Saving data
                            contentCause = cause;

                            // Marking state as loaded
                            setUIState ( LazyState.failed );

                            // Adding exception component
                            setCurrentContent ( createExceptionComponent ( contentCause ) );

                            // Firing event
                            fireUIFailed ( data, contentCause );
                        }
                        finally
                        {
                            // Clearing up resources
                            data = null;
                        }
                    }
                } );
            }
        }
        else
        {
            // Queueing data loading first
            queueDataLoading ( forced );
        }
    }

    /**
     * Sets new content {@link JComponent}.
     *
     * @param content content {@link JComponent}
     */
    protected void setCurrentContent ( @NotNull final JComponent content )
    {
        // Ensure old content is removed
        removeCurrentContent ();

        // Add new content
        this.content = content;
        container.add ( content, constraints, contentIndex );
        SwingUtils.update ( container );
    }

    /**
     * Removes currently added content {@link JComponent}.
     */
    protected void removeCurrentContent ()
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Ensure old content exists
        if ( content != null && content.getParent () == container )
        {
            contentIndex = container.getComponentZOrder ( content );
            container.remove ( content );
        }
    }

    /**
     * Returns identifier of the {@link TaskGroup} to execute data loading on.
     *
     * @return identifier of the {@link TaskGroup} to execute data loading on
     */
    @NotNull
    protected abstract String getTaskGroupId ();

    /**
     * Creates and returns {@link JComponent} to be displayed initially.
     * {@code null} can be returned for no initial {@link JComponent}.
     *
     * @return {@link JComponent} to be displayed initially
     */
    @Nullable
    protected JComponent createInitialComponent ()
    {
        return null;
    }

    /**
     * Creates and returns {@link LazyDataLoadProgress} provided to displayed custom progress {@link JComponent}.
     *
     * @return {@link LazyDataLoadProgress} provided to displayed custom progress {@link JComponent}
     */
    @NotNull
    protected LazyDataLoadProgress<D> createDataLoadProgress ()
    {
        return new LazyDataLoadProgress<D> ();
    }

    /**
     * Creates and returns custom progress {@link JComponent}.
     * It is displayed in {@link #container} upon data loading start and removed upon UI load completion.
     *
     * @param progress {@link DataLoadProgress}
     * @return custom progress {@link JComponent}
     */
    @NotNull
    protected JComponent createProgressComponent ( @NotNull final DataLoadProgress<D> progress )
    {
        return new LazyProgressOverlay<D> ( progress );
    }

    /**
     * Creates and returns custom {@link JComponent} for the specified {@link Throwable} cause.
     * It is displayed in {@link #container} in case data loading has failed.
     *
     * @param cause {@link Throwable} cause
     * @return custom {@link JComponent} for the specified {@link Throwable} cause
     */
    @NotNull
    protected JComponent createExceptionComponent ( @NotNull final Throwable cause )
    {
        final String message = TextUtils.shortenText ( cause.getMessage (), 100, true );
        final WebLabel component = new WebLabel ( message, WebLabel.CENTER );
        component.onMousePress ( MouseButton.left, new MouseEventRunnable ()
        {
            @Override
            public void run ( @NotNull final MouseEvent event )
            {
                final WebPopOver info = new WebPopOver ( component );
                info.setCloseOnFocusLoss ( true );
                final WebSyntaxArea area = new WebSyntaxArea ( ExceptionUtils.getStackTrace ( cause ), 12, 60 );
                area.applyPresets ( base, viewable, hideMenu, ideaTheme, nonOpaque );
                info.add ( new WebSyntaxScrollPane ( StyleId.syntaxareaScrollUndecorated, area, false ) );
                info.show ( component, PopOverDirection.down, PopOverAlignment.centered );
            }
        } );
        return component;
    }

    /**
     * Loads and returns data for {@link JComponent} to display.
     *
     * @param callback {@link ProgressCallback}
     * @return loaded data for {@link JComponent} to display
     */
    @Nullable
    protected abstract D loadData ( @NotNull ProgressCallback callback );

    /**
     * Creates and returns {@link JComponent} to be displayed for the loaded data.
     *
     * @param data loaded data
     * @return {@link JComponent} to be displayed for the loaded data
     */
    @NotNull
    protected abstract C loadUI ( @Nullable D data );

    /**
     * Adds {@link LazyStateListener} into this {@link LazyContent}.
     *
     * @param listener {@link LazyStateListener} to add
     */
    public void addLazyContentStateListener ( @NotNull final LazyStateListener listener )
    {
        listeners.add ( LazyStateListener.class, listener );
    }

    /**
     * Removes {@link LazyStateListener} from this {@link LazyContent}.
     *
     * @param listener {@link LazyStateListener} to remove
     */
    public void removeLazyContentStateListener ( @NotNull final LazyStateListener listener )
    {
        listeners.remove ( LazyStateListener.class, listener );
    }

    /**
     * Fires {@link LazyContent} data {@link LazyState} change.
     *
     * @param oldState new {@link LazyState} for data
     * @param newState new {@link LazyState} for data
     */
    public void fireDataStateChanged ( @NotNull final LazyState oldState, @NotNull final LazyState newState )
    {
        for ( final LazyStateListener listener : listeners.getListeners ( LazyStateListener.class ) )
        {
            listener.dataStateChanged ( oldState, newState );
        }
    }

    /**
     * Fires {@link LazyContent} UI elements {@link LazyState} change.
     *
     * @param oldState new {@link LazyState} for UI elements
     * @param newState new {@link LazyState} for UI elements
     */
    public void fireUIStateChanged ( @NotNull final LazyState oldState, @NotNull final LazyState newState )
    {
        for ( final LazyStateListener listener : listeners.getListeners ( LazyStateListener.class ) )
        {
            listener.uiStateChanged ( oldState, newState );
        }
    }

    /**
     * Adds {@link LazyContentListener} into this {@link LazyContent}.
     *
     * @param listener {@link LazyContentListener} to add
     */
    public void addLazyContentListener ( @NotNull final LazyContentListener<D, C> listener )
    {
        listeners.add ( LazyContentListener.class, listener );
    }

    /**
     * Removes {@link LazyContentListener} from this {@link LazyContent}.
     *
     * @param listener {@link LazyContentListener} to remove
     */
    public void removeLazyContentListener ( @NotNull final LazyContentListener<D, C> listener )
    {
        listeners.remove ( LazyContentListener.class, listener );
    }

    /**
     * Fires {@link LazyContent} data load completion.
     *
     * @param data loaded data
     */
    public void fireDataLoaded ( @Nullable final D data )
    {
        for ( final LazyContentListener listener : listeners.getListeners ( LazyContentListener.class ) )
        {
            listener.dataLoaded ( data );
        }
    }

    /**
     * Fires {@link LazyContent} data load failure.
     *
     * @param cause {@link Throwable}
     */
    public void fireDataFailed ( @NotNull final Throwable cause )
    {
        for ( final LazyContentListener listener : listeners.getListeners ( LazyContentListener.class ) )
        {
            listener.dataFailed ( cause );
        }
    }

    /**
     * Fires {@link LazyContent} UI elements load completion.
     *
     * @param data      loaded data
     * @param component loaded {@link JComponent}
     */
    public void fireUILoaded ( @Nullable final D data, @NotNull final C component )
    {
        for ( final LazyContentListener listener : listeners.getListeners ( LazyContentListener.class ) )
        {
            listener.uiLoaded ( data, component );
        }
    }

    /**
     * Fires {@link LazyContent} UI elements load failure.
     *
     * @param data  loaded data
     * @param cause {@link Throwable}
     */
    public void fireUIFailed ( @Nullable final D data, @NotNull final Throwable cause )
    {
        for ( final LazyContentListener listener : listeners.getListeners ( LazyContentListener.class ) )
        {
            listener.uiFailed ( data, cause );
        }
    }
}