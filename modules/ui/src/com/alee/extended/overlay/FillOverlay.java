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

import javax.swing.*;
import java.awt.*;

/**
 * {@link Overlay} implementation that fuly covers {@link WebOverlay} component bounds.
 * Note that specified margin can either reduce or increase covered area relative to {@link WebOverlay} component bounds.
 *
 * @author Mikle Garin
 */
public class FillOverlay extends AbstractOverlay
{
    /**
     * Constructs new {@link FillOverlay}.
     *
     * @param component overlay {@link JComponent}
     */
    public FillOverlay ( @NotNull final JComponent component )
    {
        this ( component, new Insets ( 0, 0, 0, 0 ) );
    }

    /**
     * Constructs new {@link FillOverlay}.
     *
     * @param component overlay {@link JComponent}
     * @param margin    additional margin for the {@link WebOverlay} content {@link JComponent}
     */
    public FillOverlay ( @NotNull final JComponent component, @NotNull final Insets margin )
    {
        super ( component, margin );
    }

    @NotNull
    @Override
    public Rectangle bounds ( @NotNull final WebOverlay container, @Nullable final JComponent component, @NotNull final Rectangle bounds )
    {
        return bounds;
    }
}