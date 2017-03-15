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

package com.alee.api.merge;

import java.io.Serializable;

/**
 * Objects merge behavior.
 * It is asked to perform merge if it {@link #supports(Merge, Object, Object)} provided objects.
 *
 * @author Mikle Garin
 * @see Merge
 */

public interface MergeBehavior extends Serializable
{
    /**
     * Returns whether or not this behavior supports specified objects merge.
     *
     * @param merge  merge algorithm
     * @param object base object
     * @param merged object to merge
     * @return {@code true} if this behavior supports specified objects merge, {@code false} otherwise
     */
    public boolean supports ( Merge merge, Object object, Object merged );

    /**
     * Performs merge of the two provided objects and returns resulting object.
     * Depending on the case it might be one of the two provided objects or their merge result.
     *
     * @param merge  merge algorithm
     * @param object base object
     * @param merged object to merge
     * @param <T>    resulting object type
     * @return merge result
     */
    public <T> T merge ( Merge merge, Object object, Object merged );
}