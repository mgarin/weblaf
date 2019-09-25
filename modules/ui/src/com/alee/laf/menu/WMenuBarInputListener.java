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

package com.alee.laf.menu;

import com.alee.api.annotations.NotNull;
import com.alee.api.jdk.Objects;
import com.alee.laf.AbstractUIInputListener;
import com.alee.laf.UIAction;
import com.alee.laf.UIActionMap;
import com.alee.utils.LafLookup;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

/**
 * Basic UI input listener for {@link WMenuBarUI} implementation.
 * It is partially based on Swing {@link javax.swing.plaf.basic.BasicMenuBarUI} but cleaned up and optimized.
 *
 * @param <C> {@link JMenuBar} type
 * @param <U> {@link WMenuBarUI} type
 * @author Georges Saab
 * @author David Karlton
 * @author Arnaud Weber
 * @author Mikle Garin
 */
public class WMenuBarInputListener<C extends JMenuBar, U extends WMenuBarUI<C>> extends AbstractUIInputListener<C, U>
        implements MenuBarInputListener<C>, ContainerListener, ChangeListener
{
    @Override
    public void install ( @NotNull final C component )
    {
        super.install ( component );

        // Installing listeners
        for ( int i = 0; i < component.getMenuCount (); i++ )
        {
            final JMenu menu = component.getMenu ( i );
            if ( menu != null )
            {
                menu.getModel ().addChangeListener ( WMenuBarInputListener.this );
            }
        }
        component.addContainerListener ( WMenuBarInputListener.this );

        // Installing ActionMap
        final UIActionMap actionMap = new UIActionMap ();
        actionMap.put ( new Action ( component, Action.TAKE_FOCUS ) );
        SwingUtilities.replaceUIActionMap ( component, actionMap );

        // Installing InputMap
        final InputMap inputMap = LafLookup.getInputMap ( component, JComponent.WHEN_IN_FOCUSED_WINDOW );
        SwingUtilities.replaceUIInputMap ( component, JComponent.WHEN_IN_FOCUSED_WINDOW, inputMap );
    }

    @Override
    public void uninstall ( @NotNull final C component )
    {
        // Uninstalling InputMap
        SwingUtilities.replaceUIInputMap ( component, JComponent.WHEN_IN_FOCUSED_WINDOW, null );

        // Uninstalling ActionMap
        SwingUtilities.replaceUIActionMap ( component, null );

        // Uninstalling listeners
        component.removeContainerListener ( WMenuBarInputListener.this );
        for ( int i = 0; i < component.getMenuCount (); i++ )
        {
            final JMenu menu = component.getMenu ( i );
            if ( menu != null )
            {
                menu.getModel ().removeChangeListener ( WMenuBarInputListener.this );
            }
        }

        super.uninstall ( component );
    }

    @Override
    public void componentAdded ( final ContainerEvent e )
    {
        final Component child = e.getChild ();
        if ( child instanceof JMenu )
        {
            ( ( JMenu ) child ).getModel ().addChangeListener ( WMenuBarInputListener.this );
        }
    }

    @Override
    public void componentRemoved ( final ContainerEvent e )
    {
        final Component child = e.getChild ();
        if ( child instanceof JMenu )
        {
            ( ( JMenu ) child ).getModel ().removeChangeListener ( WMenuBarInputListener.this );
        }
    }

    @Override
    public void stateChanged ( final ChangeEvent e )
    {
        final int menuCount = component.getMenuCount ();
        for ( int i = 0; i < menuCount; i++ )
        {
            final JMenu menu = component.getMenu ( i );
            if ( menu != null && menu.isSelected () )
            {
                component.getSelectionModel ().setSelectedIndex ( i );
                break;
            }
        }
    }

    /**
     * Actions for {@link JMenuBar}.
     *
     * @param <M> {@link JMenuBar} type
     */
    public static class Action<M extends JMenuBar> extends UIAction<M>
    {
        /**
         * Supported actions.
         */
        public static final String TAKE_FOCUS = "takeFocus";

        /**
         * Constructs new menubar {@link Action} with the specified name.
         *
         * @param menuBar {@link JMenuBar}
         * @param name    {@link Action} name
         */
        public Action ( @NotNull final M menuBar, @NotNull final String name )
        {
            super ( menuBar, name );
        }

        @Override
        public void actionPerformed ( final ActionEvent e )
        {
            final M menuBar = ( M ) e.getSource ();
            final String key = getName ();
            if ( Objects.equals ( key, TAKE_FOCUS ) )
            {
                final JMenu menu = menuBar.getMenu ( 0 );
                if ( menu != null )
                {
                    final MenuSelectionManager defaultManager = MenuSelectionManager.defaultManager ();
                    defaultManager.setSelectedPath ( new MenuElement[]{ menuBar, menu, menu.getPopupMenu () } );
                }
            }
        }
    }
}