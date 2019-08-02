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

import com.alee.api.Identifiable;
import com.alee.api.jdk.Objects;

/**
 * Custom {@link Matcher} for {@link Identifiable} objects and {@link Enum}s.
 *
 * @author Mikle Garin
 */
public final class IdentifiableMatcher extends AbstractMatcher<Object, Object>
{
    /**
     * todo 1. Split into multiple Matcher implementations grouped by grouping implementation
     * todo 2. Add matcher for basic types like int, boolean, String etc
     * todo 3. Add matcher for simple immutable types?
     */

    @Override
    public boolean supports ( final Object object )
    {
        return object instanceof Identifiable || object instanceof Enum;
    }

    @Override
    protected boolean matchImpl ( final Object first, final Object second )
    {
        if ( first instanceof Identifiable && second instanceof Identifiable )
        {
            final String id1 = ( ( Identifiable ) first ).getId ();
            final String id2 = ( ( Identifiable ) second ).getId ();
            return Objects.equals ( id1, id2 );
        }
        else if ( first instanceof Enum && second instanceof Enum )
        {
            return first == second;
        }
        else
        {
            final String message = "Cannot match objects: %s and %s";
            throw new MatchingException ( String.format ( message, first, second ) );
        }
    }
}