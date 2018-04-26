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

import java.io.Serializable;
import java.util.Collection;

/**
 * Base interface for any class that detects and resolves duplicates within any {@link Collection}.
 * What actually happens with duplicates upon {@link #resolve(Collection)} call is up to each specific implementation of this interface.
 *
 * @author Mikle Garin
 */
public interface DuplicateResolver extends Serializable
{
    /**
     * Resolves duplicates within specified {@link Collection}.
     *
     * @param collection {@link Collection} to resolve duplicates for
     */
    public void resolve ( Collection collection );
}