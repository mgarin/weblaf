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

package com.alee.api.clone.unknownresolver;


import com.alee.api.clone.Clone;
import com.alee.api.clone.CloneException;
import com.alee.api.clone.RecursiveClone;
import com.alee.api.clone.UnknownResolver;

/**
 * {@link UnknownResolver} that always throws a {@link CloneException}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Clone">How to use Clone</a>
 * @see Clone
 */
public final class ExceptionUnknownResolver implements UnknownResolver
{
    @Override
    public Object resolve ( final RecursiveClone clone, final Object object )
    {
        throw new CloneException ( "Unsupported object type clone: " + object );
    }
}