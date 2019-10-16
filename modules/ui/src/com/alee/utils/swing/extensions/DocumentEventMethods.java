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

package com.alee.utils.swing.extensions;

import com.alee.api.annotations.NotNull;
import com.alee.extended.behavior.DocumentChangeBehavior;

import javax.swing.text.JTextComponent;

/**
 * This interface provides a set of methods that should be added into text components that supports custom WebLaF events.
 *
 * @param <C> {@link JTextComponent} type
 * @author Mikle Garin
 * @see MethodExtension
 * @see DocumentChangeBehavior
 * @see com.alee.utils.swing.extensions.DocumentEventMethodsImpl
 */
public interface DocumentEventMethods<C extends JTextComponent> extends MethodExtension
{
    /**
     * Shortcut method for document change event.
     *
     * @param runnable document event runnable
     * @return used document change and property change listeners
     */
    @NotNull
    public DocumentChangeBehavior<C> onChange ( @NotNull DocumentEventRunnable<C> runnable );
}