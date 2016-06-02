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

package com.alee.extended.dock;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

/**
 * Simple {@link com.alee.extended.dock.IDockableFramePainter} adapter class.
 * It is used to install simple non-specific painters into {@link com.alee.extended.dock.WebDockableFrameUI}.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */

public final class AdaptiveDockableFramePainter<E extends WebDockableFrame, U extends WebDockableFrameUI> extends AdaptivePainter<E, U>
        implements IDockableFramePainter<E, U>
{
    /**
     * Constructs new {@link com.alee.extended.dock.AdaptiveDockableFramePainter} for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveDockableFramePainter ( final Painter painter )
    {
        super ( painter );
    }
}