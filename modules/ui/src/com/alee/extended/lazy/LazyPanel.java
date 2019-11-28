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
import com.alee.laf.panel.WebPanel;
import com.alee.managers.style.StyleId;
import com.alee.managers.task.TaskGroup;

import javax.swing.*;
import java.awt.*;

/**
 * Custom {@link WebPanel} for {@link LazyContent} usage convenience.
 *
 * @param <D> data type
 * @param <C> {@link JComponent} type
 * @author Mikle Garin
 */
public abstract class LazyPanel<D, C extends JComponent> extends WebPanel
{
    /**
     * {@link LazyContent} performing actual data and {@link JComponent} load.
     */
    @NotNull
    protected final LazyContent<D, C> lazyContent;

    /**
     * Constructs new {@link LazyPanel}.
     *
     * @param trigger {@link LazyLoadTrigger} for lazy data and UI elements
     */
    public LazyPanel ( @NotNull final LazyLoadTrigger trigger )
    {
        this ( StyleId.auto, trigger, trigger );
    }

    /**
     * Constructs new {@link LazyPanel}.
     *
     * @param dataTrigger {@link LazyLoadTrigger} for lazy data
     * @param uiTrigger   {@link LazyLoadTrigger} for UI elements
     */
    public LazyPanel ( @NotNull final LazyLoadTrigger dataTrigger, @NotNull final LazyLoadTrigger uiTrigger )
    {
        this ( StyleId.auto, dataTrigger, uiTrigger );
    }

    /**
     * Constructs new {@link LazyPanel}.
     *
     * @param styleId {@link StyleId}
     * @param trigger {@link LazyLoadTrigger} for lazy data and UI elements
     */
    public LazyPanel ( @NotNull final StyleId styleId, @NotNull final LazyLoadTrigger trigger )
    {
        this ( styleId, trigger, trigger );
    }

    /**
     * Constructs new {@link LazyPanel}.
     *
     * @param styleId     {@link StyleId}
     * @param dataTrigger {@link LazyLoadTrigger} for lazy data
     * @param uiTrigger   {@link LazyLoadTrigger} for UI elements
     */
    public LazyPanel ( @NotNull final StyleId styleId, @NotNull final LazyLoadTrigger dataTrigger,
                       @NotNull final LazyLoadTrigger uiTrigger )
    {
        super ( styleId, new LazyPanelLayout () );

        lazyContent = new LazyContent<D, C> ( LazyPanel.this, BorderLayout.CENTER, dataTrigger, uiTrigger )
        {
            @NotNull
            @Override
            protected String getTaskGroupId ()
            {
                return LazyPanel.this.getTaskGroupId ();
            }

            @Nullable
            @Override
            protected JComponent createInitialComponent ()
            {
                final JComponent initialComponent = LazyPanel.this.createInitialComponent ();
                return initialComponent != null ? initialComponent : super.createInitialComponent ();
            }

            @NotNull
            @Override
            protected LazyDataLoadProgress<D> createDataLoadProgress ()
            {
                final LazyDataLoadProgress<D> dataLoadProgress = LazyPanel.this.createDataLoadProgress ();
                return dataLoadProgress != null ? dataLoadProgress : super.createDataLoadProgress ();
            }

            @Override
            @NotNull
            protected JComponent createProgressComponent ( @NotNull final DataLoadProgress<D> progress )
            {
                final JComponent loaderComponent = LazyPanel.this.createLoaderComponent ( progress );
                return loaderComponent != null ? loaderComponent : super.createProgressComponent ( progress );
            }

            @Override
            @NotNull
            protected JComponent createExceptionComponent ( @NotNull final Throwable cause )
            {
                final JComponent exceptionComponent = LazyPanel.this.createExceptionComponent ( cause );
                return exceptionComponent != null ? exceptionComponent : super.createExceptionComponent ( cause );
            }

            @Nullable
            @Override
            protected D loadData ( @NotNull final ProgressCallback callback )
            {
                return LazyPanel.this.loadData ( callback );
            }

            @NotNull
            @Override
            protected C loadUI ( @Nullable final D data )
            {
                return LazyPanel.this.loadUI ( data );
            }
        };
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.lazypanel;
    }

    /**
     * Returns {@link LazyState} for data.
     *
     * @return {@link LazyState} for data
     */
    @NotNull
    public LazyState getDataState ()
    {
        return lazyContent.getDataState ();
    }

    /**
     * Returns {@link LazyState} for UI elements.
     *
     * @return {@link LazyState} for UI elements
     */
    @NotNull
    public LazyState getUIState ()
    {
        return lazyContent.getUIState ();
    }

    /**
     * Manually queues data or UI loading, whichever is necessary.
     * If data and UI are already loaded they will be fully reloaded.
     * Also ensures that actual operation is performed on Event Dispatch Thread if it isn't called from it.
     */
    public void reload ()
    {
        lazyContent.reload ();
    }

    /**
     * Returns {@link TaskGroup} identifier.
     * Referenced {@link TaskGroup} will be used to execute data loading.
     *
     * @return {@link TaskGroup} identifier
     */
    @NotNull
    protected abstract String getTaskGroupId ();

    /**
     * Creates and returns {@link JComponent} to be displayed initially.
     * {@code null} can be returned for default {@link LazyContent} implementation to be used.
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
     * {@code null} can be returned for default {@link LazyContent} implementation to be used.
     *
     * @return {@link LazyDataLoadProgress} provided to displayed custom progress {@link JComponent}
     */
    @Nullable
    protected LazyDataLoadProgress<D> createDataLoadProgress ()
    {
        return null;
    }

    /**
     * Creates and returns custom progress {@link JComponent}.
     * It is displayed upon data loading start and removed upon UI load completion.
     * {@code null} can be returned for default {@link LazyContent} implementation to be used.
     *
     * @param progress {@link DataLoadProgress}
     * @return custom progress {@link JComponent}
     */
    @Nullable
    protected JComponent createLoaderComponent ( @NotNull final DataLoadProgress<D> progress )
    {
        return null;
    }

    /**
     * Creates and returns custom {@link JComponent} for the specified {@link Throwable} cause.
     * It is displayed in case data loading has failed.
     * {@code null} can be returned for default {@link LazyContent} implementation to be used.
     *
     * @param cause {@link Throwable} cause
     * @return custom {@link JComponent} for the specified {@link Throwable} cause
     */
    @Nullable
    protected JComponent createExceptionComponent ( @NotNull final Throwable cause )
    {
        return null;
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
     * Adds {@link LazyStateListener} into this {@link LazyPanel}.
     *
     * @param listener {@link LazyStateListener} to add
     */
    public void addLazyContentStateListener ( @NotNull final LazyStateListener listener )
    {
        lazyContent.addLazyContentStateListener ( listener );
    }

    /**
     * Removes {@link LazyStateListener} from this {@link LazyPanel}.
     *
     * @param listener {@link LazyStateListener} to remove
     */
    public void removeLazyContentStateListener ( @NotNull final LazyStateListener listener )
    {
        lazyContent.removeLazyContentStateListener ( listener );
    }

    /**
     * Adds {@link LazyContentListener} into this {@link LazyContent}.
     *
     * @param listener {@link LazyContentListener} to add
     */
    public void addLazyContentListener ( @NotNull final LazyContentListener<D, C> listener )
    {
        lazyContent.addLazyContentListener ( listener );
    }

    /**
     * Removes {@link LazyContentListener} from this {@link LazyContent}.
     *
     * @param listener {@link LazyContentListener} to remove
     */
    public void removeLazyContentListener ( @NotNull final LazyContentListener<D, C> listener )
    {
        lazyContent.removeLazyContentListener ( listener );
    }
}