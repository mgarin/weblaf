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

package com.alee.api.duplicate;

import com.alee.api.matcher.Matcher;

import java.util.Collection;

/**
 * {@link DuplicateResolver} implementation that removes all duplicates within {@link Collection}.
 *
 * @author Mikle Garin
 */
public final class RemoveDuplicates extends AbstractDuplicateResolver
{
    /**
     * Constructs new {@link RemoveDuplicates}.
     *
     * @param matcher {@link Matcher} for duplicates detection
     */
    public RemoveDuplicates ( final Matcher matcher )
    {
        super ( matcher );
    }

    @Override
    public void resolve ( final Collection collection )
    {
        removeDuplicates ( collection );
    }
}