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

import javax.swing.*;
import java.awt.event.MouseAdapter;

/**
 * Custom interface used to create document tab title provider.
 * It can be used to completely alter WebDocumentPane tab title components.
 *
 * @author Mikle Garin
 * @see com.alee.extended.tab.DefaultTabTitleComponentProvider
 */

public interface TabTitleComponentProvider<T extends DocumentData>
{
    /**
     * Returns newly created tab title component.
     *
     * @param paneData     PaneData containing document
     * @param document     document to create tab title component for
     * @param mouseAdapter mouse adapter that forwards all mouse events to tabbed pane
     * @return newly created tab title component
     */
    public JComponent createTabTitleComponent ( PaneData<T> paneData, T document, MouseAdapter mouseAdapter );
}