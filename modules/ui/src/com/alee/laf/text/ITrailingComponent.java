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

package com.alee.laf.text;

import javax.swing.*;

/**
 * Interface for elements supporting customizable trailing component.
 * It could be anything from component or UI to any custom object.
 *
 * @author Mikle Garin
 */

public interface ITrailingComponent
{
    /**
     * Returns field trailing component.
     *
     * @return field trailing component
     */
    public JComponent getTrailingComponent ();

    /**
     * Sets field trailing component.
     * Returns removed trailing component or {@code null} if it didn't exist or was the same.
     *
     * @param trailingComponent field trailing component
     * @return removed trailing component
     */
    public JComponent setTrailingComponent ( JComponent trailingComponent );

    /**
     * Removes field trailing component.
     *
     * @return removed trailing component
     */
    public JComponent removeTrailingComponent ();
}