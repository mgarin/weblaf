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

package com.alee.extended.tree;

/**
 * This enumeration represents possible async tree node states.
 * <p>
 * Usually node might change in one of these directions:
 * 1. waiting -&gt; (2)
 * 2. loading -&gt; (3) or (4)
 * 3. loaded -&gt; when reload called (2)
 * 4. failed -&gt; when reload called (2)
 *
 * @author Mikle Garin
 */
public enum AsyncNodeState
{
    /**
     * Waiting for node expansion to load children.
     */
    waiting,

    /**
     * Loading children.
     */
    loading,

    /**
     * Children loaded.
     */
    loaded,

    /**
     * Children load failed.
     */
    failed
}