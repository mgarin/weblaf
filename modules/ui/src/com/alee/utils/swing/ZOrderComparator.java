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

package com.alee.utils.swing;

import java.awt.*;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Component Z-order comparator.
 *
 * @author Mikle Garin
 */

public final class ZOrderComparator implements Comparator<Component>, Serializable
{
    @Override
    public int compare ( final Component o1, final Component o2 )
    {
        final Container parent1 = o1.getParent ();
        final Container parent2 = o2.getParent ();
        final int z1 = parent1.getComponentZOrder ( o1 );
        final int z2 = parent2.getComponentZOrder ( o2 );
        return z1 < z2 ? -1 : z1 == z2 ? 0 : 1;
    }
}