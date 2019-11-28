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

package com.alee.extended.overlay;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.extended.canvas.WebCanvas;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.DecorationState;

import javax.swing.*;

/**
 * {@link WebOverlay} extensions that provides custom progress overlay upon request.
 * Except for the extra progress layer {@link WebProgressOverlay} has all features of {@link WebOverlay}.
 *
 * @author Mikle Garin
 */
public class WebProgressOverlay extends WebOverlay
{
    /**
     * {@link WebCanvas} used to display progress layer.
     */
    @NotNull
    protected final WebCanvas progressLayer;

    /**
     * Constructs new {@link WebProgressOverlay}.
     */
    public WebProgressOverlay ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs new {@link WebProgressOverlay}.
     *
     * @param content {@link JComponent} content
     */
    public WebProgressOverlay ( @Nullable final JComponent content )
    {
        this ( StyleId.auto, content );
    }

    /**
     * Constructs new {@link WebProgressOverlay}.
     *
     * @param id {@link StyleId}
     */
    public WebProgressOverlay ( @NotNull final StyleId id )
    {
        this ( id, null );
    }

    /**
     * Constructs new {@link WebProgressOverlay}.
     *
     * @param id      {@link StyleId}
     * @param content {@link JComponent} content
     */
    public WebProgressOverlay ( @NotNull final StyleId id, @Nullable final JComponent content )
    {
        super ( id, content );

        progressLayer = createProgressLayer ();
        addOverlay ( new FillOverlay ( progressLayer ) );
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.progressoverlay;
    }

    /**
     * Creates and returns {@link WebCanvas} used to display progress layer.
     *
     * @return {@link WebCanvas} used to display progress layer
     */
    @NotNull
    protected WebCanvas createProgressLayer ()
    {
        return new WebCanvas ( StyleId.progressoverlayLayer.at ( this ) );
    }

    /**
     * Returns whether or not progress is currently displayed.
     *
     * @return {@code true} if progress is currently displayed, {@code false} otherwise
     */
    public boolean isProgressDisplayed ()
    {
        return progressLayer.hasState ( DecorationState.progress );
    }

    /**
     * Displays progress layer.
     */
    public void displayProgress ()
    {
        progressLayer.addStates ( DecorationState.progress );
    }

    /**
     * Hides progress layer.
     */
    public void hideProgress ()
    {
        progressLayer.removeStates ( DecorationState.progress );
    }
}