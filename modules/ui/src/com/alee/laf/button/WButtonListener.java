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

package com.alee.laf.button;

import com.alee.api.jdk.Objects;
import com.alee.laf.AbstractUIInputListener;
import com.alee.laf.UIAction;
import com.alee.laf.UIActionMap;
import com.alee.utils.LafLookup;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentInputMapUIResource;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Basic listener for {@link WButtonUI} implementation.
 * It is based on common Swing {@link javax.swing.plaf.basic.BasicButtonListener} but cleaned up and optimized.
 *
 * @param <C> {@link AbstractButton} type
 * @param <U> {@link WButtonUI} type
 * @author Jeff Dinkins
 * @author Arnaud Weber
 * @author Mikle Garin
 */

public class WButtonListener<C extends AbstractButton, U extends WButtonUI<C>>
        extends AbstractUIInputListener<C, U> implements MouseListener, MouseMotionListener, FocusListener, PropertyChangeListener
{
    /**
     * Last button press timestamp.
     */
    protected long lastPressedTimestamp = -1;

    /**
     * Multiclick threshhold support.
     */
    protected boolean shouldDiscardRelease = false;

    @Override
    public void install ( final C component )
    {
        super.install ( component );

        // Installing listeners
        component.addMouseListener ( this );
        component.addMouseMotionListener ( this );
        component.addFocusListener ( this );
        component.addPropertyChangeListener ( this );

        // Installing ActionMap
        final UIActionMap actionMap = new UIActionMap ();
        actionMap.put ( new Action ( component, Action.PRESSED ) );
        actionMap.put ( new Action ( component, Action.RELEASED ) );
        SwingUtilities.replaceUIActionMap ( component, actionMap );

        // Installing InputMap
        final InputMap inputMap = LafLookup.getInputMap ( component, JComponent.WHEN_FOCUSED );
        SwingUtilities.replaceUIInputMap ( component, JComponent.WHEN_FOCUSED, inputMap );
        updateMnemonicBinding ( component );
    }

    @Override
    public void uninstall ( final C component )
    {
        // Uninstalling InputMap
        SwingUtilities.replaceUIInputMap ( component, JComponent.WHEN_IN_FOCUSED_WINDOW, null );
        SwingUtilities.replaceUIInputMap ( component, JComponent.WHEN_FOCUSED, null );

        // Uninstalling ActionMap
        SwingUtilities.replaceUIActionMap ( component, null );

        // Uninstalling listeners
        component.removePropertyChangeListener ( this );
        component.removeFocusListener ( this );
        component.removeMouseMotionListener ( this );
        component.removeMouseListener ( this );

        super.uninstall ( component );
    }

    /**
     * Resets the binding for the mnemonic in the WHEN_IN_FOCUSED_WINDOW UI {@link InputMap}.
     *
     * @param button {@link AbstractButton} to update mnemonic binding for
     */
    protected void updateMnemonicBinding ( final AbstractButton button )
    {
        final int m = button.getMnemonic ();
        if ( m != 0 )
        {
            InputMap map = SwingUtilities.getUIInputMap ( button, JComponent.WHEN_IN_FOCUSED_WINDOW );
            if ( map == null )
            {
                map = new ComponentInputMapUIResource ( button );
                SwingUtilities.replaceUIInputMap ( button, JComponent.WHEN_IN_FOCUSED_WINDOW, map );
            }
            map.clear ();
            map.put ( KeyStroke.getKeyStroke ( m, SwingUtils.getFocusAcceleratorKeyMask (), false ), Action.PRESSED );
            map.put ( KeyStroke.getKeyStroke ( m, SwingUtils.getFocusAcceleratorKeyMask (), true ), Action.RELEASED );
            map.put ( KeyStroke.getKeyStroke ( m, 0, true ), Action.RELEASED );
        }
        else
        {
            final InputMap map = SwingUtilities.getUIInputMap ( button, JComponent.WHEN_IN_FOCUSED_WINDOW );
            if ( map != null )
            {
                map.clear ();
            }
        }
    }

    @Override
    public void propertyChange ( final PropertyChangeEvent e )
    {
        final String prop = e.getPropertyName ();
        if ( prop.equals ( AbstractButton.MNEMONIC_CHANGED_PROPERTY ) )
        {
            updateMnemonicBinding ( ( AbstractButton ) e.getSource () );
        }
    }

    @Override
    public void focusGained ( final FocusEvent e )
    {
        final AbstractButton button = ( AbstractButton ) e.getSource ();
        if ( button instanceof JButton && ( ( JButton ) button ).isDefaultCapable () )
        {
            final JRootPane root = button.getRootPane ();
            if ( root != null )
            {
                final WButtonUI ui = LafUtils.getUI ( button );
                if ( ui != null && LafLookup.getBoolean ( button, ui, ui.getPropertyPrefix () + "defaultButtonFollowsFocus", true ) )
                {
                    root.putClientProperty ( "temporaryDefaultButton", button );
                    root.setDefaultButton ( ( JButton ) button );
                    root.putClientProperty ( "temporaryDefaultButton", null );
                }
            }
        }
    }

    @Override
    public void focusLost ( final FocusEvent e )
    {
        final AbstractButton button = ( AbstractButton ) e.getSource ();
        final JRootPane root = button.getRootPane ();
        if ( root != null )
        {
            final JButton initialDefault = ( JButton ) root.getClientProperty ( "initialDefaultButton" );
            if ( button != initialDefault )
            {
                final WButtonUI ui = LafUtils.getUI ( button );
                if ( ui != null && LafLookup.getBoolean ( button, ui, ui.getPropertyPrefix () + "defaultButtonFollowsFocus", true ) )
                {
                    root.setDefaultButton ( initialDefault );
                }
            }
        }

        final ButtonModel model = button.getModel ();
        model.setPressed ( false );
        model.setArmed ( false );
    }

    @Override
    public void mouseMoved ( final MouseEvent e )
    {
    }

    @Override
    public void mouseDragged ( final MouseEvent e )
    {
    }

    @Override
    public void mouseClicked ( final MouseEvent e )
    {
    }

    @Override
    public void mousePressed ( final MouseEvent e )
    {
        if ( SwingUtilities.isLeftMouseButton ( e ) )
        {
            final AbstractButton b = ( AbstractButton ) e.getSource ();
            if ( b.contains ( e.getX (), e.getY () ) )
            {
                final long multiClickThreshhold = b.getMultiClickThreshhold ();
                final long lastTime = lastPressedTimestamp;
                final long currentTime = lastPressedTimestamp = e.getWhen ();
                if ( lastTime != -1 && currentTime - lastTime < multiClickThreshhold )
                {
                    shouldDiscardRelease = true;
                    return;
                }

                final ButtonModel model = b.getModel ();
                if ( !model.isEnabled () )
                {
                    // Disabled buttons ignore all input...
                    return;
                }
                if ( !model.isArmed () )
                {
                    // button not armed, should be
                    model.setArmed ( true );
                }
                model.setPressed ( true );
                if ( !b.hasFocus () && b.isRequestFocusEnabled () )
                {
                    b.requestFocus ();
                }
            }
        }
    }

    @Override
    public void mouseReleased ( final MouseEvent e )
    {
        if ( SwingUtilities.isLeftMouseButton ( e ) )
        {
            // Support for multiClickThreshhold
            if ( shouldDiscardRelease )
            {
                shouldDiscardRelease = false;
                return;
            }

            final AbstractButton b = ( AbstractButton ) e.getSource ();
            final ButtonModel model = b.getModel ();
            model.setPressed ( false );
            model.setArmed ( false );
        }
    }

    @Override
    public void mouseEntered ( final MouseEvent e )
    {
        final AbstractButton b = ( AbstractButton ) e.getSource ();
        final ButtonModel model = b.getModel ();
        if ( b.isRolloverEnabled () && !SwingUtilities.isLeftMouseButton ( e ) )
        {
            model.setRollover ( true );
        }
        if ( model.isPressed () )
        {
            model.setArmed ( true );
        }
    }

    @Override
    public void mouseExited ( final MouseEvent e )
    {
        final AbstractButton b = ( AbstractButton ) e.getSource ();
        final ButtonModel model = b.getModel ();
        if ( b.isRolloverEnabled () )
        {
            model.setRollover ( false );
        }
        model.setArmed ( false );
    }

    /**
     * Actions for {@link AbstractButton}.
     *
     * @param <B> {@link AbstractButton} type
     */
    protected class Action<B extends AbstractButton> extends UIAction<B>
    {
        /**
         * Supported actions.
         */
        public static final String PRESSED = "pressed";
        public static final String RELEASED = "released";

        /**
         * Constructs new button {@link Action} with the specified name.
         *
         * @param button {@link AbstractButton}
         * @param name   {@link Action} name
         */
        public Action ( final B button, final String name )
        {
            super ( button, name );
        }

        @Override
        public void actionPerformed ( final ActionEvent e )
        {
            final B button = getComponent ();
            if ( Objects.equals ( getName (), PRESSED ) )
            {
                final ButtonModel model = button.getModel ();
                model.setArmed ( true );
                model.setPressed ( true );
                if ( !button.hasFocus () )
                {
                    button.requestFocus ();
                }
            }
            else if ( Objects.equals ( getName (), RELEASED ) )
            {
                final ButtonModel model = button.getModel ();
                model.setPressed ( false );
                model.setArmed ( false );
            }
        }

        @Override
        public boolean isEnabled ()
        {
            final B button = getComponent ();
            return button == null || button.getModel ().isEnabled ();
        }
    }
}