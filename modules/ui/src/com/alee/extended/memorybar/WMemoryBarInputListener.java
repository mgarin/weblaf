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

package com.alee.extended.memorybar;

import com.alee.api.annotations.NotNull;
import com.alee.api.jdk.Objects;
import com.alee.laf.AbstractUIInputListener;
import com.alee.laf.UIAction;
import com.alee.laf.UIActionMap;
import com.alee.utils.LafLookup;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.event.*;

/**
 * Basic UI input listener for {@link WMemoryBarUI} implementation.
 * It is partially based on Swing {@link javax.swing.plaf.basic.BasicButtonListener} but cleaned up and optimized.
 *
 * @param <C> {@link WebMemoryBar} type
 * @param <U> {@link WMemoryBarUI} type
 * @author Jeff Dinkins
 * @author Arnaud Weber
 * @author Mikle Garin
 */
public class WMemoryBarInputListener<C extends WebMemoryBar, U extends WMemoryBarUI<C>> extends AbstractUIInputListener<C, U>
        implements MemoryBarInputListener<C>, MouseListener, MouseMotionListener, FocusListener, AncestorListener
{
    @Override
    public void install ( @NotNull final C component )
    {
        super.install ( component );

        // Installing listeners
        component.addMouseListener ( this );
        component.addMouseMotionListener ( this );
        component.addFocusListener ( this );
        component.addAncestorListener ( this );

        // Installing ActionMap
        final UIActionMap actionMap = new UIActionMap ();
        actionMap.put ( new Action ( component, Action.PRESSED ) );
        actionMap.put ( new Action ( component, Action.RELEASED ) );
        SwingUtilities.replaceUIActionMap ( component, actionMap );

        // Installing InputMap
        final InputMap inputMap = LafLookup.getInputMap ( component, JComponent.WHEN_FOCUSED );
        SwingUtilities.replaceUIInputMap ( component, JComponent.WHEN_FOCUSED, inputMap );
    }

    @Override
    public void uninstall ( @NotNull final C component )
    {
        // Uninstalling InputMap
        SwingUtilities.replaceUIInputMap ( component, JComponent.WHEN_IN_FOCUSED_WINDOW, null );
        SwingUtilities.replaceUIInputMap ( component, JComponent.WHEN_FOCUSED, null );

        // Uninstalling ActionMap
        SwingUtilities.replaceUIActionMap ( component, null );

        // Uninstalling listeners
        component.removeAncestorListener ( this );
        component.removeFocusListener ( this );
        component.removeMouseMotionListener ( this );
        component.removeMouseListener ( this );

        super.uninstall ( component );
    }

    @Override
    public void focusGained ( @NotNull final FocusEvent event )
    {
    }

    @Override
    public void focusLost ( @NotNull final FocusEvent event )
    {
        final WebMemoryBar memoryBar = ( WebMemoryBar ) event.getSource ();
        final ButtonModel model = memoryBar.getModel ();
        model.setPressed ( false );
        model.setArmed ( false );
    }

    @Override
    public void mouseMoved ( @NotNull final MouseEvent event )
    {
    }

    @Override
    public void mouseDragged ( @NotNull final MouseEvent event )
    {
    }

    @Override
    public void mouseClicked ( @NotNull final MouseEvent event )
    {
    }

    @Override
    public void mousePressed ( @NotNull final MouseEvent event )
    {
        final WebMemoryBar memoryBar = ( WebMemoryBar ) event.getSource ();
        if ( SwingUtilities.isLeftMouseButton ( event ) )
        {
            if ( memoryBar.contains ( event.getX (), event.getY () ) )
            {
                final ButtonModel model = memoryBar.getModel ();
                if ( model.isEnabled () )
                {
                    if ( !model.isArmed () )
                    {
                        // button not armed, should be
                        model.setArmed ( true );
                    }
                    model.setPressed ( true );
                }
                if ( memoryBar.isEnabled () && !memoryBar.hasFocus () && memoryBar.isRequestFocusEnabled () )
                {
                    memoryBar.requestFocus ();
                }
            }
        }
    }

    @Override
    public void mouseReleased ( @NotNull final MouseEvent event )
    {
        final WebMemoryBar memoryBar = ( WebMemoryBar ) event.getSource ();
        if ( SwingUtilities.isLeftMouseButton ( event ) )
        {
            final ButtonModel model = memoryBar.getModel ();
            model.setPressed ( false );
            model.setArmed ( false );
        }
    }

    @Override
    public void mouseEntered ( @NotNull final MouseEvent event )
    {
        final WebMemoryBar memoryBar = ( WebMemoryBar ) event.getSource ();
        final ButtonModel model = memoryBar.getModel ();
        if ( !SwingUtilities.isLeftMouseButton ( event ) )
        {
            model.setRollover ( true );
        }
        if ( model.isPressed () )
        {
            model.setArmed ( true );
        }
    }

    @Override
    public void mouseExited ( @NotNull final MouseEvent event )
    {
        final WebMemoryBar memoryBar = ( WebMemoryBar ) event.getSource ();
        final ButtonModel model = memoryBar.getModel ();
        model.setRollover ( false );
        model.setArmed ( false );
    }

    @Override
    public void ancestorAdded ( @NotNull final AncestorEvent event )
    {
        final WebMemoryBar memoryBar = ( WebMemoryBar ) event.getSource ();
        memoryBar.getModel ().setRollover ( false );
    }

    @Override
    public void ancestorRemoved ( @NotNull final AncestorEvent event )
    {
        final WebMemoryBar memoryBar = ( WebMemoryBar ) event.getSource ();
        memoryBar.getModel ().setRollover ( false );
    }

    @Override
    public void ancestorMoved ( @NotNull final AncestorEvent event )
    {
        final WebMemoryBar memoryBar = ( WebMemoryBar ) event.getSource ();
        memoryBar.getModel ().setRollover ( false );
    }

    /**
     * Actions for {@link WebMemoryBar}.
     *
     * @param <M> {@link WebMemoryBar} type
     */
    public static class Action<M extends WebMemoryBar> extends UIAction<M>
    {
        /**
         * Supported actions.
         */
        public static final String PRESSED = "pressed";
        public static final String RELEASED = "released";

        /**
         * Constructs new memory bar {@link Action} with the specified name.
         *
         * @param memoryBar {@link WebMemoryBar}
         * @param name      {@link Action} name
         */
        public Action ( @NotNull final M memoryBar, @NotNull final String name )
        {
            super ( memoryBar, name );
        }

        @Override
        public void actionPerformed ( @NotNull final ActionEvent e )
        {
            final M memoryBar = getComponent ();
            final String action = getName ();
            if ( Objects.equals ( action, PRESSED ) )
            {
                final ButtonModel model = memoryBar.getModel ();
                model.setArmed ( true );
                model.setPressed ( true );
                if ( !memoryBar.hasFocus () )
                {
                    memoryBar.requestFocus ();
                }
            }
            else if ( Objects.equals ( action, RELEASED ) )
            {
                final ButtonModel model = memoryBar.getModel ();
                model.setPressed ( false );
                model.setArmed ( false );
            }
        }

        @Override
        public boolean isEnabled ()
        {
            final M memoryBar = getComponent ();
            return memoryBar == null || memoryBar.getModel ().isEnabled ();
        }
    }
}