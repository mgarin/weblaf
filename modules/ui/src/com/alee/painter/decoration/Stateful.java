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

package com.alee.painter.decoration;

import java.util.List;

/**
 * Interface designed to allow components or UIs provide additional decoration states.
 * It is used by {@link AbstractDecorationPainter} which handles basic states.
 *
 * @author Mikle Garin
 * @see AbstractDecorationPainter#getDecorationStates()
 * @see AbstractDecorationPainter#collectDecorationStates()
 */

public interface Stateful
{
    /**
     * todo 1. When moved to JDK8 add default method to update decoration
     */

    /**
     * Returns current decoratable states or {@code null} if no additional states are available at the time.
     * These states will be requested each time component decoration update is requested.
     * Ensure that you do not perform any long-term operations within this method implementation.
     *
     * @return current decoratable states or {@code null} if no additional states are available at the time
     */
    public List<String> getStates ();

    //    /**
    //     * Force decoration updates.
    //     */
    //    public default void updateDecoration ()
    //    {
    //        DecorationUtils.fireStatesChanged ( ( JComponent ) this );
    //    }
}