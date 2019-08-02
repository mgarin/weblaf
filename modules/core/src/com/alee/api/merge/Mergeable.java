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

/**
 * This marker interface can be implemented by any objects that can be merged with each other.
 *
 * Easiest way to merge two object instances is to use {@link Merge} to perform merge operation.
 * Result can vary a lot depending on object implementation and provided {@link Merge} settings.
 *
 * To customize the way object can be merged implement {@link MergeBehavior} directly instead of this interface.
 * It will provide you with a method which could be invoked upon merge operation in case it is supported by specific {@link Merge} instance.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Merge">How to use Merge</a>
 * @see Merge
 * @see MergeBehavior
 */
public interface Mergeable
{
    /**
     * This is a marker interface, it doesn't have any methods to implement.
     */
}