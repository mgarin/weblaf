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
import com.alee.laf.menu.WebPopupMenu;
import com.alee.laf.menu.WebRadioButtonMenuItem;
import com.alee.managers.style.StyleId;
import com.alee.utils.LafUtils;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Custom {@link WebRadioButtonMenuItem} used as {@link JTabbedPane} tab menu item.
 * It is also a {@link UIResource} as it should be exclusively used by the UI implementations.
 *
 * @author Mikle Garin
 */
public class TabMenuItem extends WebRadioButtonMenuItem implements ActionListener, UIResource
{
    /**
     * {@link JTabbedPane} this {@link TabMenuItem} is used for.
     */
    @NotNull
    protected final JTabbedPane tabbedPane;

    /**
     * {@link Tab} index this {@link TabMenuItem} is used for.
     */
    protected final int index;

    /**
     * Constucts new {@link TabMenuItem}.
     *
     * @param tabbedPane {@link JTabbedPane} this item represents tab from
     * @param menu       {@link WebPopupMenu} this item will be added to
     * @param index      tab index
     */
    public TabMenuItem ( @NotNull final JTabbedPane tabbedPane, @NotNull final WebPopupMenu menu, final int index )
    {
        super ( StyleId.tabbedpaneTabMenuItem.at ( menu ) );
        this.tabbedPane = tabbedPane;
        this.index = index;

        // Selected state
        setSelected ( tabbedPane.getSelectedIndex () == index );

        // Retrieving icon and title
        final Icon icon;
        final String title;
        final WTabbedPaneUI ui = LafUtils.getUI ( tabbedPane );
        if ( ui != null )
        {
            final Tab tab = ui.getTab ( index );
            if ( tab != null )
            {
                if ( tab.getComponent () instanceof TabComponent )
                {
                    // Retrieving icon and title from tab component
                    final TabComponent tabComponent = ( TabComponent ) tab.getComponent ();
                    icon = tabComponent.getIcon ();
                    title = tabComponent.getTitle ();
                }
                else
                {
                    // Trying to retrieve icon and title from tab directly
                    icon = tab.getIcon ();
                    title = tab.getText ();
                }
            }
            else
            {
                // Retrieving icon and title provided by JTabbedPane
                icon = tabbedPane.getIconAt ( index );
                title = tabbedPane.getTitleAt ( index );
            }
        }
        else
        {
            // Retrieving icon and title provided by JTabbedPane
            icon = tabbedPane.getIconAt ( index );
            title = tabbedPane.getTitleAt ( index );
        }

        // Updating menu item settings
        if ( icon != null || title != null )
        {
            // Using retrieved icon and title
            setIcon ( icon );
            setText ( title );
        }
        else
        {
            // Using default fallback title
            setLanguage ( "weblaf.tabbedpane.menu.tab", index + 1 );
        }
        setEnabled ( tabbedPane.isEnabledAt ( index ) );

        // Tab selection action
        addActionListener ( this );
    }

    /**
     * Returns {@link JTabbedPane} this {@link TabMenuItem} is used for.
     *
     * @return {@link JTabbedPane} this {@link TabMenuItem} is used for
     */
    @NotNull
    public JTabbedPane getTabbedPane ()
    {
        return tabbedPane;
    }

    /**
     * Returns {@link Tab} index this {@link TabMenuItem} is used for.
     *
     * @return {@link Tab} index this {@link TabMenuItem} is used for
     */
    public int getIndex ()
    {
        return index;
    }

    @Override
    public void actionPerformed ( final ActionEvent e )
    {
        tabbedPane.setSelectedIndex ( index );
    }
}