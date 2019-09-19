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

package com.alee.extended.tab;

import com.alee.api.annotations.NotNull;

import javax.swing.*;
import java.awt.event.MouseAdapter;

/**
 * Interface that should be implemented by classes providing custom {@link WebDocumentPane} tab title {@link JComponent}s.
 *
 * @param <T> {@link DocumentData} type
 * @author Mikle Garin
 * @see DefaultTabTitleComponentProvider
 * @see TabTitleComponent
 * @see WebDocumentPane
 */
public interface TabTitleComponentProvider<T extends DocumentData>
{
    /**
     * Returns newly created tab title component.
     *
     * @param paneData     {@link PaneData} containing document
     * @param document     {@link DocumentData} of the document to create tab title component for
     * @param mouseAdapter {@link MouseAdapter} that forwards all mouse events to tabbed pane
     * @return newly created tab title component
     */
    @NotNull
    public JComponent createTabTitleComponent ( @NotNull PaneData<T> paneData, @NotNull T document, @NotNull MouseAdapter mouseAdapter );
}