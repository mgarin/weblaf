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

package com.alee.extended.behavior;

import javax.swing.*;

/**
 * Simple abstract {@link Behavior} implementation that keeps {@link JComponent} reference and has its type generic.
 *
 * @param <C> component type
 * @author Mikle Garin
 */

public abstract class AbstractComponentBehavior<C extends JComponent> implements Behavior
{
    /**
     * {@link JComponent} into which this behavior is installed.
     */
    protected final C component;

    /**
     * Constructs new {@link AbstractComponentBehavior} for the specified {@link JComponent}.
     *
     * @param component {@link JComponent} into which this behavior is installed
     */
    public AbstractComponentBehavior ( final C component )
    {
        super ();
        this.component = component;
    }
}