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

package com.alee.api.annotations;

import java.lang.annotation.*;

/**
 * An element annotated with {@link NotNull} claims to never return/contain/operate with {@code null} values.
 *
 * @author Mikle Garin
 */
@Documented
@Retention ( RetentionPolicy.CLASS )
@Target ( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE/*, ElementType.TYPE_USE*/ } )
public @interface NotNull
{
    /**
     * Doesn't have any settings.
     */
}