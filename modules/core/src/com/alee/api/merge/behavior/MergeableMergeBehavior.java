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

package com.alee.api.merge.behavior;

import com.alee.api.merge.GlobalMergeBehavior;
import com.alee.api.merge.Merge;
import com.alee.api.merge.MergeBehavior;
import com.alee.api.merge.RecursiveMerge;

/**
 * Merge behavior for objects implementing {@link MergeBehavior}.
 *
 * @param <T> {@link MergeBehavior} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Merge">How to use Merge</a>
 * @see Merge
 * @see MergeBehavior
 */
public class MergeableMergeBehavior<T extends MergeBehavior<T>> implements GlobalMergeBehavior<T, T, T>
{
    @Override
    public boolean supports ( final RecursiveMerge merge, final Class<T> type, final Object base, final Object merged )
    {
        return base.getClass () == merged.getClass () && base instanceof MergeBehavior && merged instanceof MergeBehavior;
    }

    @Override
    public T merge ( final RecursiveMerge merge, final Class type, final T base, final T merged, final int depth )
    {
        return base.merge ( merge, type, merged, depth );
    }
}