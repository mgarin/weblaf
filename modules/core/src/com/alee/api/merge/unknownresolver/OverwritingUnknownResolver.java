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

package com.alee.api.merge.unknownresolver;

import com.alee.api.merge.Merge;
import com.alee.api.merge.RecursiveMerge;
import com.alee.api.merge.UnknownResolver;

/**
 * {@link UnknownResolver} that always overwrites base object.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Merge">How to use Merge</a>
 * @see Merge
 */
public final class OverwritingUnknownResolver implements UnknownResolver
{
    @Override
    public Object resolve ( final RecursiveMerge merge, final Object object, final Object merged )
    {
        return merged;
    }
}