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
 * Custom {@link WebPanel} used as {@link JTabbedPane} tab area.
 * It is necessary to make it a {@link UIResource} to avoid it being added as a tab.
 *
 * @author Mikle Garin
 */
public class TabArea extends WebPanel implements Stateful, UIResource
{
    /**
     * {@link JTabbedPane} this {@link TabArea} is used for.
     */
    protected final JTabbedPane tabbedPane;

    /**
     * Construct new {@link TabArea} for the specified {@link JTabbedPane}.
     *
     * @param tabbedPane {@link JTabbedPane}
     */
    public TabArea ( @NotNull final JTabbedPane tabbedPane )
    {
        super ( StyleId.tabbedpaneTabArea.at ( tabbedPane ), ( LayoutManager ) null );
        this.tabbedPane = tabbedPane;
    }

    /**
     * Returns {@link JTabbedPane} this {@link TabArea} is used for.
     *
     * @return {@link JTabbedPane} this {@link TabArea} is used for
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