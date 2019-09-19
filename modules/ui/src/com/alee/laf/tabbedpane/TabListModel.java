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

import javax.swing.*;

/**
 * Simple {@link ListModel} that provides list of all {@link JTabbedPane} tab indices.
 *
 * @author Mikle Garin
 */
public class TabListModel extends AbstractListModel
{
    /**
     * {@link JTabbedPane} this model provides tab indices for.
     */
    @NotNull
    protected final JTabbedPane tabbedPane;

    /**
     * Constucts new {@link TabListModel}.
     *
     * @param tabbedPane {@link JTabbedPane} this model provides tab indices for
     */
    public TabListModel ( @NotNull final JTabbedPane tabbedPane )
    {
        this.tabbedPane = tabbedPane;
    }

    /**
     * Returns {@link JTabbedPane} this model provides tab indices for.
     *
     * @return {@link JTabbedPane} this model provides tab indices for
     */
    @NotNull
    public JTabbedPane getTabbedPane ()
    {
        return tabbedPane;
    }

    @Override
    public int getSize ()
    {
        return tabbedPane.getTabCount ();
    }

    @NotNull
    @Override
    public Object getElementAt ( final int index )
    {
        return index;
    }
}