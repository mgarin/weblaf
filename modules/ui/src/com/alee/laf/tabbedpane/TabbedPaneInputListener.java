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

package com.alee.laf.tabbedpane;

import com.alee.api.annotations.NotNull;
import com.alee.laf.UIInputListener;

import javax.swing.*;

/**
 * Base interface for {@link JTabbedPane} UI input listeners.
 *
 * @param <C> {@link JComponent} type
 * @author Mikle Garin
 */
public interface TabbedPaneInputListener<C extends JTabbedPane> extends UIInputListener<C>
{
    /**
     * Informs {@link TabbedPaneInputListener} implementation about added {@link Tab}.
     *
     * @param tab   added {@link Tab}
     * @param index added {@link Tab} index
     */
    public void tabAdded ( @NotNull Tab tab, int index );

    /**
     * Informs {@link TabbedPaneInputListener} implementation about removed {@link Tab}.
     *
     * @param index removed {@link Tab} index
     */
    public void tabRemoved ( int index );
}