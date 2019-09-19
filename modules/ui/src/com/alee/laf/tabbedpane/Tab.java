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
import com.alee.extended.label.WebStyledLabel;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.Stateful;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom {@link WebStyledLabel} used for rendering {@link JTabbedPane} tabs.
 * It is also a {@link UIResource} as it should be exclusively used by the UI implementations.
 *
 * @author Mikle Garin
 */
public class Tab extends WebStyledLabel implements Stateful, UIResource
{
    /**
     * {@link JTabbedPane} this {@link Tab} is used for.
     */
    protected final JTabbedPane tabbedPane;

    /**
     * Constructs new {@link Tab}.
     *
     * @param tabbedPane   {@link JTabbedPane} this {@link Tab} is used for
     * @param tabContainer {@link TabContainer} this {@link Tab} will be added into
     * @param index        {@link Tab} index
     */
    public Tab ( @NotNull final JTabbedPane tabbedPane, @NotNull final TabContainer tabContainer, final int index )
    {
        super ( StyleId.tabbedpaneTabTitle.at ( tabContainer ), tabbedPane.getTitleAt ( index ), tabbedPane.getIconAt ( index ) );
        this.tabbedPane = tabbedPane;
        setDisplayedMnemonicIndex ( tabbedPane.getDisplayedMnemonicIndexAt ( index ) );
        setEnabled ( tabbedPane.isEnabledAt ( index ) );
        applyComponentOrientation ( tabbedPane.getComponentOrientation () );
    }

    /**
     * Returns {@link JTabbedPane} this {@link Tab} is used for.
     *
     * @return {@link JTabbedPane} this {@link Tab} is used for
     */
    @NotNull
    public JTabbedPane getTabbedPane ()
    {
        return tabbedPane;
    }

    /**
     * Changes {@link Component} displayed within {@link Tab}.
     * If set to {@code null} - default title icon and text will be displayed instead.
     *
     * @param component {@link Component}
     */
    public void setComponent ( @Nullable final Component component )
    {
        if ( component != null )
        {
            if ( getComponentCount () > 0 )
            {
                removeAll ();
            }
            setLayout ( new BorderLayout () );
            add ( component );
        }
        else
        {
            removeAll ();
            setLayout ( null );
        }
    }

    /**
     * Returns {@link Component} displayed within {@link Tab}.
     *
     * @return {@link Component} displayed within {@link Tab}
     */
    @Nullable
    public Component getComponent ()
    {
        return getComponentCount () > 0 ? getComponent ( 0 ) : null;
    }

    @Nullable
    @Override
    public List<String> getStates ()
    {
        final List<String> states;
        if ( tabbedPane != null )
        {
            states = new ArrayList<String> ( 2 );

            // Selected state
            final int index = getIndex ();
            if ( index != -1 && index == tabbedPane.getSelectedIndex () )
            {
                states.add ( DecorationState.selected );
            }

            // Tab placement
            states.add ( BoxOrientation.get ( tabbedPane.getTabPlacement () ).name () );
        }
        else
        {
            states = null;
        }
        return states;
    }

    /**
     * Returns index of this {@link Tab} within {@link JTabbedPane} or {@code -1} if it is not within {@link JTabbedPane}.
     * Returned index is also adjusted for potential ongoing tab removal operation since it might perform intermediate updates.
     *
     * @return index of this {@link Tab} within {@link JTabbedPane} or {@code -1} if it is not within {@link JTabbedPane}
     */
    protected int getIndex ()
    {
        int index = -1;
        if ( tabbedPane != null )
        {
            final Container parent = getParent ();
            if ( parent != null )
            {
                final int parentIndex = parent.getComponentZOrder ( Tab.this );
                final Integer removedIndex = ( Integer ) tabbedPane.getClientProperty ( WebTabbedPane.REMOVED_TAB_INDEX );
                if ( removedIndex != null )
                {
                    if ( removedIndex > parentIndex )
                    {
                        index = parentIndex;
                    }
                    else if ( removedIndex < parentIndex )
                    {
                        index = parentIndex - 1;
                    }
                    else
                    {
                        index = -1;
                    }
                }
                else
                {
                    index = parentIndex;
                }
            }
        }
        return index;
    }
}