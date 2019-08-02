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

package com.alee.api.matcher;

import com.alee.api.jdk.Objects;

/**
 * Custom {@link Matcher} for equal objects.
 *
 * @author Mikle Garin
 */
public final class EqualMatcher extends AbstractMatcher<Object, Object>
{
    @Override
    public boolean supports ( final Object object )
    {
        return true;
    }

    @Override
    protected boolean matchImpl ( final Object first, final Object second )
    {
        return Objects.equals ( first, second );
    }
}