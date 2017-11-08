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
import com.alee.utils.CompareUtils;

/**
 * Custom {@link Matcher} for {@link Identifiable} objects.
 *
 * @author Mikle Garin
 */

public final class IdentifiableMatcher extends AbstractMatcher<Identifiable, Identifiable>
{
    @Override
    public boolean supports ( final Object object )
    {
        return object instanceof Identifiable;
    }

    @Override
    protected boolean matchImpl ( final Identifiable first, final Identifiable second )
    {
        return CompareUtils.equals ( first.getId (), second.getId () );
    }
}