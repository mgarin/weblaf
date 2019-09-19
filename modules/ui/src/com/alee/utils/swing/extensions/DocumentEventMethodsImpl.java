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
import com.alee.api.annotations.Nullable;
import com.alee.extended.behavior.DocumentChangeBehavior;

import javax.swing.event.DocumentEvent;
import javax.swing.text.JTextComponent;

/**
 * Common implementations for {@link com.alee.utils.swing.extensions.DocumentEventMethods} interface methods.
 *
 * @author Mikle Garin
 * @see DocumentChangeBehavior
 * @see com.alee.utils.swing.extensions.DocumentEventMethods
 */
public final class DocumentEventMethodsImpl
{
    /**
     * Shortcut method for document change event.
     *
     * @param textComponent text component to handle events for
     * @param runnable      document event runnable
     * @param <C>           {@link JTextComponent} type
     * @return used document change and property change listeners
     */
    public static <C extends JTextComponent> DocumentChangeBehavior onChange ( @NotNull final C textComponent,
                                                                               @NotNull final DocumentEventRunnable<C> runnable )
    {
        return new DocumentChangeBehavior<C> ( textComponent )
        {
            @Override
            public void documentChanged ( @NotNull final C component, @Nullable final DocumentEvent event )
            {
                runnable.run ( component, event );
            }
        }.install ();
    }
}