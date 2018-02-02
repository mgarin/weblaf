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

package com.alee.extended.inspector.info;

import javax.swing.*;
import java.awt.*;

/**
 * Short component information provider interface.
 *
 * @param <C> component type
 * @author Mikle Garin
 */

public interface ComponentPreview<C extends Component>
{
    /**
     * Returns icon for the specified component.
     *
     * @param component component to provide icon for
     * @return icon for the specified component
     */
    public Icon getIcon ( C component );

    /**
     * Returns string value for the specified component.
     *
     * @param component object to provide text for
     * @return string value for the specified component
     */
    public String getText ( C component );
}