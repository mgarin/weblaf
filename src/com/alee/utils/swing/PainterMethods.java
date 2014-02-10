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

import com.alee.extended.painter.Painter;
import com.alee.managers.style.data.PainterStyle;

/**
 * This interface is made specially for usage within StyleManager.
 * It should be implemented in UI where you want to support dynamic painters installation.
 * It allows StyleManager to determine how one or another painter should be created and used.
 *
 * @author Mikle Garin
 */

public interface PainterMethods extends SwingMethods
{
    /**
     * Creates new painter based on specified style settings and returns it.
     *
     * @param style painter style settings
     * @return newly created painter
     */
    public Painter createPainter ( PainterStyle style );

    /**
     * Properly installs the specified painter.
     *
     * @param painter painter to install
     * @param style   painter style settings
     */
    public void installPainter ( Painter painter, PainterStyle style );

    /**
     * Performs painters uninstallation.
     * This method will be called when UI is uninstalled from the component.
     */
    public void uninstallPainters ();
}