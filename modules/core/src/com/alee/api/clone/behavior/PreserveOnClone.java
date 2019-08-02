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

package com.alee.api.clone.behavior;

import java.lang.annotation.*;

/**
 * Runtime annotation for preserving object field value used by {@link ReflectionCloneBehavior}.
 * Field with this annotation will preserve their original value whenever object clone is created.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Clone">How to use Clone</a>
 * @see com.alee.api.clone.Clone
 */
@Documented
@Retention ( RetentionPolicy.RUNTIME )
@Target ( ElementType.FIELD )
public @interface PreserveOnClone
{
    /**
     * Doesn't have any settings.
     */
}