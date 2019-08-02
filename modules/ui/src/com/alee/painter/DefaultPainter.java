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

package com.alee.painter;

import java.lang.annotation.*;

/**
 * Special runtime annotation which allows specifying default painter class.
 *
 * @author Mikle Garin
 */
@Documented
@Retention ( RetentionPolicy.RUNTIME )
@Target ( ElementType.FIELD )
public @interface DefaultPainter
{
    /**
     * Returns default painter implementation class.
     *
     * @return default painter implementation class
     */
    public Class<? extends Painter> value ();
}