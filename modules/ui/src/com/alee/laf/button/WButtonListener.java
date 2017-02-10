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

import com.alee.utils.CompareUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.LazyActionMap;
import sun.swing.DefaultLookup;
import sun.swing.UIAction;

import javax.swing.*;
import javax.swing.plaf.ComponentInputMapUIResource;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Basic listener for {@link com.alee.laf.button.WButtonUI} implementation.
 * It is based on common Swing {@link javax.swing.plaf.basic.BasicButtonListener} but doesn't have some unnecessary update calls.
 *
 * @author Jeff Dinkins
 * @author Arnaud Weber
 * @author Mikle Garin
 */

public class WButtonListener implements MouseListener, MouseMotionListener, FocusListener, PropertyChangeListener
{
    /**
     * Last button press timestamp.
     */
    private long lastPressedTimestamp = -1;

    /**
     * Multiclick threshhold support.
     */
    private boolean shouldDiscardRelease = false;

    /**
     * Loads all available button actions.
     * Called through reflection upon lazy actions initialization.
     *
     * @param map {@link LazyActionMap} to populate actions into
     */
    public static void loadActionMap ( final LazyActionMap map )
    {
        map.put ( new Action ( Action.PRESSED ) );
        map.put ( new Action ( Action.RELEASED ) );
    }

    /**
     * Installs default key actions.
     *
     * @param button {@link AbstractButton} to install actions into
     */
    public void installKeyboardActions ( final AbstractButton button )
    {
        updateMnemonicBinding ( button );

        final WButtonUI ui = LafUtils.getUI ( button );
        LazyActionMap.installLazyActionMap ( button, WButtonListener.class, ui.getPropertyPrefix () + "actionMap" );

        SwingUtilities.replaceUIInputMap ( button, JComponent.WHEN_FOCUSED, getInputMap ( JComponent.WHEN_FOCUSED, button ) );
    }

    /**
     * Uninstalls default key actions.
     *
     * @param button {@link AbstractButton} to uninstall actions from
     */
    public void uninstallKeyboardActions ( final AbstractButton button )
    {
        SwingUtilities.replaceUIInputMap ( button, JComponent.WHEN_IN_FOCUSED_WINDOW, null );
        SwingUtilities.replaceUIInputMap ( button, JComponent.WHEN_FOCUSED, null );
        SwingUtilities.replaceUIActionMap ( button, null );
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
                if ( ui != null && DefaultLookup.getBoolean ( button, ui, ui.getPropertyPrefix () + "defaultButtonFollowsFocus", true ) )
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
                if ( ui != null && DefaultLookup.getBoolean ( button, ui, ui.getPropertyPrefix () + "defaultButtonFollowsFocus", true ) )
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
            map.put ( KeyStroke.getKeyStroke ( m, 0, true ), "released" );
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

    /**
     * Returns {@link InputMap} for {@code condition}.
     *
     * @param condition event condition
     * @param button    {@link AbstractButton} to return {@link InputMap} for
     * @return {@link InputMap} for {@code condition}
     */
    protected InputMap getInputMap ( final int condition, final AbstractButton button )
    {
        if ( condition == JComponent.WHEN_FOCUSED )
        {
            final WButtonUI ui = LafUtils.getUI ( button );
            if ( ui != null )
            {
                return ( InputMap ) DefaultLookup.get ( button, ui, ui.getPropertyPrefix () + "focusInputMap" );
            }
        }
        return null;
    }

    /**
     * Actions for Buttons.
     * Two types of action are supported:
     * - pressed: Moves the button to a pressed state
     * - released: Disarms the button.
     */
    protected static class Action extends UIAction
    {
        /**
         * Pressed action name.
         */
        public static final String PRESSED = "pressed";

        /**
         * Released action name.
         */
        public static final String RELEASED = "released";

        /**
         * Constructs new button {@link Action} with the specified name.
         *
         * @param name {@link Action} name
         */
        public Action ( final String name )
        {
            super ( name );
        }

        @Override
        public void actionPerformed ( final ActionEvent e )
        {
            final AbstractButton b = ( AbstractButton ) e.getSource ();
            if ( CompareUtils.equals ( getName (), PRESSED ) )
            {
                final ButtonModel model = b.getModel ();
                model.setArmed ( true );
                model.setPressed ( true );
                if ( !b.hasFocus () )
                {
                    b.requestFocus ();
                }
            }
            else if ( CompareUtils.equals ( getName (), RELEASED ) )
            {
                final ButtonModel model = b.getModel ();
                model.setPressed ( false );
                model.setArmed ( false );
            }
        }

        @Override
        public boolean isEnabled ( final Object sender )
        {
            return !( sender != null && ( sender instanceof AbstractButton ) &&
                    !( ( AbstractButton ) sender ).getModel ().isEnabled () );
        }
    }
}