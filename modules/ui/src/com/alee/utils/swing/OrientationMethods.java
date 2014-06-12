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

/**
 * This interface provides a set of methods that should be added into components that support orientation changes.
 *
 * @author Mikle Garin
 */

// todo Make custom saveable component orientation possible
public interface OrientationMethods<C extends Component> extends SwingMethods
{
    public C updateOrientation ();

    public boolean isInvertOrientation ();

    public C setInvertOrientation ();

    public boolean isLeftToRight ();

    public C setLeftToRight ( boolean ltr );

    // todo Might need a renaming for some components
    public ComponentOrientation getOrientation ();

    // todo Might need a renaming for some components
    public C setOrientation ( ComponentOrientation orientation );
}