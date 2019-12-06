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

package com.alee.api.merge.clonepolicy;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.clone.Clone;
import com.alee.api.merge.ClonePolicy;

/**
 * {@link ClonePolicy} that always performs clone operation.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Merge">How to use Merge</a>
 * @see ClonePolicy
 * @see Clone
 * @see com.alee.api.merge.Merge
 */
public final class PerformClonePolicy implements ClonePolicy
{
    /**
     * {@link Clone} used for cloning objects.
     */
    @NotNull
    private final Clone clone;

    /**
     * Constucts new {@link PerformClonePolicy}.
     *
     * @param clone {@link Clone} used for cloning objects
     */
    public PerformClonePolicy ( @NotNull final Clone clone )
    {
        this.clone = clone;
    }

    @Nullable
    @Override
    public Object clone ( @Nullable final Object source )
    {
        return clone.clone ( source );
    }
}