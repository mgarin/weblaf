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
import com.alee.api.annotations.Nullable;
import com.alee.api.data.BoxOrientation;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.Stateful;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom {@link WebPanel} used as {@link JTabbedPane} tab container.
 * It is also a {@link UIResource} as it should be exclusively used by the UI implementations.
 *
 * @author Mikle Garin
 */
public class TabContainer extends WebPanel implements Stateful, UIResource
{
    /**
     * {@link JTabbedPane} this {@link TabContainer} is used for.
     */
    protected final JTabbedPane tabbedPane;

    /**
     * Construct new {@link TabContainer} for the specified {@link JTabbedPane} and {@link JViewport}.
     *
     * @param tabbedPane {@link JTabbedPane}
     * @param viewport   {@link JViewport}
     */
    public TabContainer ( @NotNull final JTabbedPane tabbedPane, @NotNull final JViewport viewport )
    {
        super ( StyleId.tabbedpaneTabContainer.at ( viewport ), ( LayoutManager ) null );
        this.tabbedPane = tabbedPane;
    }

    /**
     * Returns {@link JTabbedPane} this {@link TabContainer} is used for.
     *
     * @return {@link JTabbedPane} this {@link TabContainer} is used for
     */
    @NotNull
    public JTabbedPane getTabbedPane ()
    {
        return tabbedPane;
    }

    @Nullable
    @Override
    public List<String> getStates ()
    {
        final List<String> states;
        if ( tabbedPane != null )
        {
            states = new ArrayList<String> ( 2 );

            // Tab placement
            states.add ( BoxOrientation.get ( tabbedPane.getTabPlacement () ).name () );
        }
        else
        {
            states = null;
        }
        return states;
    }
}