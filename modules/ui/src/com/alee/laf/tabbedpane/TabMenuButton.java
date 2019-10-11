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
import com.alee.laf.button.WebButton;
import com.alee.laf.menu.WebPopupMenu;
import com.alee.managers.style.StyleId;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Custom {@link WebButton} used as {@link JTabbedPane} tab menu button.
 * This is a new WebLaF feature that provides faster access to opened tabs when {@link JTabbedPane#SCROLL_TAB_LAYOUT} is used.
 * It is also a {@link UIResource} as it should be exclusively used by the UI implementations.
 *
 * @author Mikle Garin
 */
public class TabMenuButton extends WebButton implements ActionListener, UIResource
{
    /**
     * {@link JTabbedPane} this {@link TabMenuButton} is used for.
     */
    protected final JTabbedPane tabbedPane;

    /**
     * Construct new {@link TabMenuButton} for the specified {@link JTabbedPane} and {@link TabArea}.
     *
     * @param tabbedPane {@link JTabbedPane}
     * @param tabArea    {@link TabArea}
     */
    public TabMenuButton ( @NotNull final JTabbedPane tabbedPane, @NotNull final TabArea tabArea )
    {
        super ( StyleId.tabbedpaneTabMenuButton.at ( tabArea ) );
        this.tabbedPane = tabbedPane;
        addActionListener ( this );
    }

    /**
     * Returns {@link JTabbedPane} this {@link TabMenuButton} is used for.
     *
     * @return {@link JTabbedPane} this {@link TabMenuButton} is used for
     */
    @NotNull
    public JTabbedPane getTabbedPane ()
    {
        return tabbedPane;
    }

    @Override
    public void actionPerformed ( @NotNull final ActionEvent e )
    {
        // Creating menu to show
        final WebPopupMenu menu = new WebPopupMenu ( StyleId.tabbedpaneTabMenu.at ( this ) );
        final ButtonGroup group = new ButtonGroup ();
        for ( int tabIndex = 0; tabIndex < tabbedPane.getTabCount (); tabIndex++ )
        {
            final TabMenuItem menuItem = new TabMenuItem ( tabbedPane, menu, tabIndex );
            menu.add ( menuItem );
            group.add ( menuItem );
        }

        // Positioning it according to tab placement
        final boolean ltr = tabbedPane.getComponentOrientation ().isLeftToRight ();
        final Dimension menuSize = menu.getPreferredSize ();
        if ( tabbedPane.getTabPlacement () == JTabbedPane.TOP )
        {
            menu.show (
                    this,
                    ltr ? getWidth () - menuSize.width : 0,
                    getHeight ()
            );
        }
        else if ( tabbedPane.getTabPlacement () == JTabbedPane.BOTTOM )
        {
            menu.show (
                    this,
                    ltr ? getWidth () - menuSize.width : 0,
                    -menuSize.height
            );
        }
        else if ( ltr && tabbedPane.getTabPlacement () == JTabbedPane.LEFT ||
                !ltr && tabbedPane.getTabPlacement () == JTabbedPane.RIGHT )
        {
            menu.show (
                    this,
                    getWidth (),
                    getHeight () - menuSize.height
            );
        }
        else if ( ltr && tabbedPane.getTabPlacement () == JTabbedPane.RIGHT ||
                !ltr && tabbedPane.getTabPlacement () == JTabbedPane.LEFT )
        {
            menu.show (
                    this,
                    -menuSize.width,
                    getHeight () - menuSize.height
            );
        }
    }
}