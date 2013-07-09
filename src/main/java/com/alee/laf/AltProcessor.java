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

package com.alee.laf;

import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Alt hotkey processor for application windows with menu.
 *
 * @author Mikle Garin
 * @since 1.0
 */

public class AltProcessor implements KeyEventPostProcessor
{
    /**
     * Runtime variables.
     */
    private static boolean altKeyPressed = false;
    private static boolean menuCanceledOnPress = false;
    private static JRootPane root = null;
    private static Window winAncestor = null;

    /**
     * This method is called by the current KeyboardFocusManager, requesting
     * that this KeyEventPostProcessor perform any necessary post-processing
     * which should be part of the KeyEvent's final resolution. At the time
     * this method is invoked, typically the KeyEvent has already been
     * dispatched to and handled by its target. However, if no Component in
     * the application currently owns the focus, then the KeyEvent has not
     * been dispatched to any Component. Typically, KeyEvent post-processing
     * will be used to implement features which require global KeyEvent
     * post-handling, such as menu shortcuts. Note that if a
     * KeyEventPostProcessor wishes to dispatch the KeyEvent, it must use
     * <code>redispatchEvent</code> to prevent the AWT from recursively
     * requesting that this KeyEventPostProcessor perform post-processing
     * of the event again.
     * <p/>
     * If an implementation of this method returns <code>false</code>, then the
     * KeyEvent is passed to the next KeyEventPostProcessor in the chain,
     * ending with the current KeyboardFocusManager. If an implementation
     * returns <code>true</code>, the KeyEvent is assumed to have been fully
     * handled (although this need not be the case), and the AWT will take no
     * further action with regard to the KeyEvent. If an implementation
     * consumes the KeyEvent but returns <code>false</code>, the consumed
     * event will still be passed to the next KeyEventPostProcessor in the
     * chain. It is important for developers to check whether the KeyEvent has
     * been consumed before performing any post-processing of the KeyEvent. By
     * default, the current KeyboardFocusManager will perform no post-
     * processing in response to a consumed KeyEvent.
     *
     * @param ev the KeyEvent to post-process
     * @return <code>true</code> if the AWT should take no further action with
     *         regard to the KeyEvent; <code>false</code> otherwise
     * @see KeyboardFocusManager#redispatchEvent
     */
    public boolean postProcessKeyEvent ( KeyEvent ev )
    {
        if ( ev.isConsumed () )
        {
            // Ignoring consumed events
            return false;
        }
        if ( ev.getKeyCode () == KeyEvent.VK_ALT )
        {
            root = SwingUtilities.getRootPane ( ev.getComponent () );
            winAncestor = ( root == null ? null : SwingUtils.getWindowAncestor ( root ) );

            if ( ev.getID () == KeyEvent.KEY_PRESSED )
            {
                if ( !altKeyPressed )
                {
                    altPressed ( ev );
                }
                altKeyPressed = true;
                return true;
            }
            else if ( ev.getID () == KeyEvent.KEY_RELEASED )
            {
                if ( altKeyPressed )
                {
                    altReleased ();
                }
                else
                {
                    MenuSelectionManager msm = MenuSelectionManager.defaultManager ();
                    MenuElement[] path = msm.getSelectedPath ();
                    if ( path.length <= 0 )
                    {
                        WebLookAndFeel.setMnemonicHidden ( true );
                        repaintMnemonicsInWindow ( winAncestor );
                    }
                }
                altKeyPressed = false;
            }
            root = null;
            winAncestor = null;
        }
        else
        {
            altKeyPressed = false;
        }
        return false;
    }

    /**
     * Processes alt hotkey press event.
     *
     * @param ev key event
     */
    private void altPressed ( KeyEvent ev )
    {
        MenuSelectionManager msm = MenuSelectionManager.defaultManager ();
        MenuElement[] path = msm.getSelectedPath ();
        if ( path.length > 0 && !( path[ 0 ] instanceof ComboPopup ) )
        {
            msm.clearSelectedPath ();
            menuCanceledOnPress = true;
            ev.consume ();
        }
        else if ( path.length > 0 )
        {
            // In case combobox had focus
            menuCanceledOnPress = false;
            WebLookAndFeel.setMnemonicHidden ( false );
            repaintMnemonicsInWindow ( winAncestor );
            ev.consume ();
        }
        else
        {
            menuCanceledOnPress = false;
            WebLookAndFeel.setMnemonicHidden ( false );
            repaintMnemonicsInWindow ( winAncestor );
            JMenuBar mbar = root != null ? root.getJMenuBar () : null;
            if ( mbar == null && winAncestor instanceof JFrame )
            {
                mbar = ( ( JFrame ) winAncestor ).getJMenuBar ();
            }
            JMenu menu = mbar != null ? mbar.getMenu ( 0 ) : null;
            if ( menu != null )
            {
                ev.consume ();
            }
        }
    }

    /**
     * Processes alt hotkey release event.
     */
    private void altReleased ()
    {
        if ( menuCanceledOnPress )
        {
            WebLookAndFeel.setMnemonicHidden ( true );
            repaintMnemonicsInWindow ( winAncestor );
            return;
        }

        MenuSelectionManager msm = MenuSelectionManager.defaultManager ();
        if ( msm.getSelectedPath ().length == 0 )
        {
            // Activating menu bar
            JMenuBar mbar = root != null ? root.getJMenuBar () : null;
            if ( mbar == null && winAncestor instanceof JFrame )
            {
                mbar = ( ( JFrame ) winAncestor ).getJMenuBar ();
            }
            JMenu menu = mbar != null ? mbar.getMenu ( 0 ) : null;

            if ( menu != null )
            {
                MenuElement[] path = new MenuElement[ 2 ];
                path[ 0 ] = mbar;
                path[ 1 ] = menu;
                msm.setSelectedPath ( path );
            }
            else if ( !WebLookAndFeel.isMnemonicHidden () )
            {
                WebLookAndFeel.setMnemonicHidden ( true );
                repaintMnemonicsInWindow ( winAncestor );
            }
        }
        else
        {
            if ( ( msm.getSelectedPath () )[ 0 ] instanceof ComboPopup )
            {
                WebLookAndFeel.setMnemonicHidden ( true );
                repaintMnemonicsInWindow ( winAncestor );
            }
        }

    }

    /**
     * Repaints all components that contain mnemonics in specified window and all of its owned windows.
     *
     * @param w window to process
     */
    private void repaintMnemonicsInWindow ( Window w )
    {
        if ( w == null || !w.isShowing () )
        {
            return;
        }

        Window[] ownedWindows = w.getOwnedWindows ();
        for ( Window ownedWindow : ownedWindows )
        {
            repaintMnemonicsInWindow ( ownedWindow );
        }

        repaintMnemonicsInContainer ( w );
    }

    /**
     * Repaints all components that contain mnemonics in specified container and all of its owned containers.
     *
     * @param cont container to process
     */
    private void repaintMnemonicsInContainer ( Container cont )
    {
        Component c;
        for ( int i = 0; i < cont.getComponentCount (); i++ )
        {
            c = cont.getComponent ( i );
            if ( c == null || !c.isVisible () )
            {
                continue;
            }
            if ( c instanceof AbstractButton && ( ( AbstractButton ) c ).getMnemonic () != '\0' )
            {
                c.repaint ();
                continue;
            }
            else if ( c instanceof JLabel && ( ( JLabel ) c ).getDisplayedMnemonic () != '\0' )
            {
                c.repaint ();
                continue;
            }
            if ( c instanceof Container )
            {
                repaintMnemonicsInContainer ( ( Container ) c );
            }
        }
    }
}