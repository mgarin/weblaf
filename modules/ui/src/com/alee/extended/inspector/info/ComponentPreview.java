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
     * Windows root icon.
     */
    public static final ImageIcon windows = new ImageIcon ( ComponentPreview.class.getResource ( "icons/windows.png" ) );

    /**
     * Returns icon for the specified {@link Component}.
     *
     * @param component {@link Component} to provide icon for
     * @return icon for the specified {@link Component}
     */
    public Icon getIcon ( C component );

    /**
     * Returns text value for the specified {@link Component}.
     *
     * @param component {@link Component} to provide text for
     * @return text value for the specified {@link Component}
     */
    public String getText ( C component );
}