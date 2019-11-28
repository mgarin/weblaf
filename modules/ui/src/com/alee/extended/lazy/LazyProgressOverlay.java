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
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.progressbar.WebProgressBar;
import com.alee.utils.swing.LoadIconType;

import java.awt.*;

/**
 * Custom progress overlay for {@link LazyContent}.
 *
 * @param <D> data type
 * @author Mikle Garin
 */
public class LazyProgressOverlay<D> extends WebPanel
{
    /**
     * {@link DataLoadProgress}.
     */
    @NotNull
    protected final DataLoadProgress<D> progress;

    /**
     * {@link WebLabel} representing indeterminate progress.
     */
    protected WebLabel indeterminate;

    /**
     * {@link WebProgressBar} representing determinate progress.
     */
    protected WebProgressBar determinate;

    /**
     * Constructs new {@link LazyProgressOverlay}.
     *
     * @param progress {@link DataLoadProgress}
     */
    public LazyProgressOverlay ( @NotNull final DataLoadProgress<D> progress )
    {
        super ( new BorderLayout () );
        this.progress = progress;
        updateDisplayedComponent ();
        progress.addListener ( new DataLoadAdapter ()
        {
            @Override
            public void totalChanged ( final int total )
            {
                updateDisplayedComponent ();
                if ( isDeterminate () )
                {
                    determinate.setMaximum ( total );
                }
            }

            @Override
            public void progressChanged ( final int progress, @Nullable final Object[] data )
            {
                updateDisplayedComponent ();
                if ( isDeterminate () )
                {
                    determinate.setValue ( progress );
                    if ( data != null && data.length > 0 && data[ 0 ] instanceof String )
                    {
                        determinate.setStringPainted ( true );
                        determinate.setString ( ( String ) data[ 0 ] );
                    }
                    else
                    {
                        determinate.setStringPainted ( false );
                        determinate.setString ( null );
                    }
                }
                else
                {
                    if ( data != null && data.length > 0 && data[ 0 ] instanceof String )
                    {
                        indeterminate.setText ( ( String ) data[ 0 ] );
                    }
                    else
                    {
                        indeterminate.setText ( null );
                    }
                }
            }
        } );
    }

    /**
     * Returns whether or not determinate progress can be displayed.
     * Determinate progress is only possible when we know total and progress values.
     *
     * @return {@code true} if determinate progress can be displayed, {@code false} otherwise
     */
    protected boolean isDeterminate ()
    {
        return this.progress.getTotal () != -1;
    }

    /**
     * Updates displayed progress component.
     */
    protected void updateDisplayedComponent ()
    {
        if ( isDeterminate () )
        {
            if ( indeterminate != null && indeterminate.getParent () != null )
            {
                remove ( indeterminate );
            }
            if ( determinate == null || determinate.getParent () == null )
            {
                add ( getDeterminateComponent (), BorderLayout.CENTER );
                revalidate ();
                repaint ();
            }
        }
        else
        {
            if ( determinate != null && determinate.getParent () != null )
            {
                remove ( determinate );
            }
            if ( indeterminate == null || indeterminate.getParent () == null )
            {
                add ( getIndeterminateComponent (), BorderLayout.CENTER );
                revalidate ();
                repaint ();
            }
        }
    }

    /**
     * Returns {@link WebLabel} representing indeterminate progress.
     *
     * @return {@link WebLabel} representing indeterminate progress
     */
    @NotNull
    protected WebLabel getIndeterminateComponent ()
    {
        if ( indeterminate == null )
        {
            indeterminate = new WebLabel ( LoadIconType.roller.getIcon (), WebLabel.CENTER );
        }
        return indeterminate;
    }

    /**
     * Returns {@link WebProgressBar} representing determinate progress.
     *
     * @return {@link WebProgressBar} representing determinate progress
     */
    @NotNull
    protected WebProgressBar getDeterminateComponent ()
    {
        if ( determinate == null )
        {
            determinate = new WebProgressBar ( 0, progress.getTotal () );
            determinate.setValue ( progress.getProgress () );
        }
        return determinate;
    }
}