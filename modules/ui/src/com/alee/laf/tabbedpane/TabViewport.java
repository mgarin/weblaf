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
import com.alee.laf.viewport.WebViewport;
import com.alee.managers.style.StyleId;

import javax.swing.*;
import javax.swing.plaf.UIResource;

/**
 * Custom {@link WebViewport} used as {@link JTabbedPane} tabs viewport.
 * It is also a {@link UIResource} as it should be exclusively used by the UI implementations.
 *
 * @author Mikle Garin
 */
public class TabViewport extends WebViewport implements UIResource
{
    /**
     * {@link JTabbedPane} this {@link TabViewport} is used for.
     */
    protected final JTabbedPane tabbedPane;

    /**
     * Construct new {@link TabViewport} for the specified {@link JTabbedPane} and {@link TabArea}.
     *
     * @param tabbedPane {@link JTabbedPane}
     * @param tabArea    {@link TabArea}
     */
    public TabViewport ( @NotNull final JTabbedPane tabbedPane, @NotNull final TabArea tabArea )
    {
        super ( StyleId.tabbedpaneTabViewport.at ( tabArea ) );
        this.tabbedPane = tabbedPane;
    }

    /**
     * Returns {@link JTabbedPane} this {@link TabViewport} is used for.
     *
     * @return {@link JTabbedPane} this {@link TabViewport} is used for
     */
    @NotNull
    public JTabbedPane getTabbedPane ()
    {
        return tabbedPane;
    }
}