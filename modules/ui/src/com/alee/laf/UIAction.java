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

package com.alee.laf;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;

/**
 * Replacement for the {@code sun.swing.UIAction} due to it being proprietary Sun API.
 * Unlike {@code sun.swing.UIAction} it is not used globally and instead contain {@link JComponent} reference.
 *
 * This replacement must be used carefully as it lacks {@code sun.swing.UIAction#isEnabled(Object)} method.
 * That method is referenced within {@link SwingUtilities#notifyAction(Action, KeyStroke, KeyEvent, Object, int)} to provide sender object
 * for convenience, but that convenience costs us full rework of every action whenever we want to provide a custom UI implementation.
 * Maybe it will be exposed as public API in the future JDK versions so we can use default implementation then.
 *
 * @param <C> {@link JComponent} type
 * @author Mikle Garin
 */
public abstract class UIAction<C extends JComponent> implements Action
{
    /**
     * {@link JComponent} this action is attached to.
     */
    private final C component;

    /**
     * Action name.
     */
    private final String name;

    /**
     * Constructs new UI action.
     *
     * @param component {@link JComponent} this action should be attached to
     * @param name      action name
     */
    public UIAction ( final C component, final String name )
    {
        this.component = component;
        this.name = name;
    }

    /**
     * Returns {@link JComponent} this action is attached to.
     *
     * @return {@link JComponent} this action is attached to
     */
    public C getComponent ()
    {
        return component;
    }

    /**
     * Returns action name.
     *
     * @return action name
     */
    public final String getName ()
    {
        return name;
    }

    @Override
    public Object getValue ( final String key )
    {
        return key.equals ( NAME ) ? name : null;
    }

    @Override
    public void putValue ( final String key, final Object value )
    {
        /**
         * {@link UIAction} is not mutable, this does nothing.
         */
    }

    @Override
    public void setEnabled ( final boolean b )
    {
        /**
         * {@link UIAction} is not mutable, this does nothing.
         */
    }

    @Override
    public boolean isEnabled ()
    {
        return true;
    }

    @Override
    public void addPropertyChangeListener ( final PropertyChangeListener listener )
    {
        /**
         * {@link UIAction} is not mutable, this does nothing.
         */
    }

    @Override
    public void removePropertyChangeListener ( final PropertyChangeListener listener )
    {
        /**
         * {@link UIAction} is not mutable, this does nothing.
         */
    }
}