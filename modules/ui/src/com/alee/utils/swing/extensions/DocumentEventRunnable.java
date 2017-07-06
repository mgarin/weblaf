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

import javax.swing.event.DocumentEvent;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

/**
 * Custom runnable that provides DocumentEvent.
 *
 * @param <C> {@link JTextComponent} type
 * @author Mikle Garin
 */

public interface DocumentEventRunnable<C extends JTextComponent>
{
    /**
     * Performs action according to document event.
     *
     * @param component {@link JTextComponent} containing changed document
     * @param event     occured document event, {@code null} if the whole {@link Document} was replaced
     */
    public void run ( C component, DocumentEvent event );
}