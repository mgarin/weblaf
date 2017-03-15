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

import com.alee.api.Mergeable;
import com.alee.api.merge.Merge;
import com.alee.api.merge.MergeBehavior;

/**
 * {@link Mergeable} objects merge behavior.
 *
 * @author Mikle Garin
 */

public final class MergeableMergeBehavior implements MergeBehavior
{
    @Override
    public boolean supports ( final Merge merge, final Object object, final Object merged )
    {
        return object instanceof Mergeable && merged instanceof Mergeable;
    }

    @Override
    public <T> T merge ( final Merge merge, final Object object, final Object merged )
    {
        return ( T ) ( ( Mergeable ) object ).merge ( ( Mergeable ) merged );
    }
}