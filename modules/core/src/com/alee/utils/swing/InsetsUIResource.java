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

package com.alee.utils.swing;

import com.alee.api.annotations.NotNull;
import com.alee.api.clone.CloneBehavior;
import com.alee.api.clone.RecursiveClone;
import com.alee.api.merge.Overwriting;

import javax.swing.plaf.UIResource;
import java.awt.*;

/**
 * Simple {@link UIResource} for {@link Insets}.
 *
 * @author Mikle Garin
 */
public final class InsetsUIResource extends Insets implements UIResource, Overwriting, CloneBehavior<InsetsUIResource>
{
    /**
     * Constructs new {@link InsetsUIResource}.
     *
     * @param top    top insets
     * @param left   left insets
     * @param bottom bottom insets
     * @param right  right insets
     */
    public InsetsUIResource ( final int top, final int left, final int bottom, final int right )
    {
        super ( top, left, bottom, right );
    }

    @Override
    public boolean isOverwrite ()
    {
        return true;
    }

    @NotNull
    @Override
    public InsetsUIResource clone ( @NotNull final RecursiveClone clone, final int depth )
    {
        return new InsetsUIResource ( top, left, bottom, right );
    }
}