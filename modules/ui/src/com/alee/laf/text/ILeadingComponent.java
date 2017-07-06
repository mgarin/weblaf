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
 * Interface for elements supporting customizable leading component.
 * It could be anything from component or UI to any custom object.
 *
 * @author Mikle Garin
 */

public interface ILeadingComponent
{
    /**
     * Returns field leading component.
     *
     * @return field leading component
     */
    public JComponent getLeadingComponent ();

    /**
     * Sets field leading component.
     * Returns removed leading component or {@code null} if it didn't exist or was the same.
     *
     * @param leadingComponent field leading component
     * @return removed leading component
     */
    public JComponent setLeadingComponent ( JComponent leadingComponent );

    /**
     * Removes field leading component.
     *
     * @return removed leading component
     */
    public JComponent removeLeadingComponent ();
}