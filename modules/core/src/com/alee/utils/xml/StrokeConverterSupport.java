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

package com.alee.utils.xml;

import java.awt.*;

/**
 * Simple interface for simple stroke XML conversion implementations.
 *
 * @param <T> {@link Stroke} type
 * @author Mikle Garin
 */
public interface StrokeConverterSupport<T extends Stroke>
{
    /**
     * Returns unique stroke identifier.
     *
     * @return unique stroke identifier
     */
    public String getId ();

    /**
     * Returns stroke class.
     *
     * @return stroke class
     */
    public Class<T> getType ();

    /**
     * Returns stroke converted into string.
     *
     * @param stroke stroke to convert
     * @return stroke converted into string
     */
    public String toString ( T stroke );

    /**
     * Returns stroke read from string.
     *
     * @param stroke stroke string
     * @return stroke read from string
     */
    public T fromString ( String stroke );
}