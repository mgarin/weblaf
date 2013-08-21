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

package com.alee.managers.tooltip;

import com.alee.laf.label.WebLabel;
import com.alee.managers.glasspane.GlassPaneManager;
import com.alee.managers.glasspane.WebGlassPane;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.managers.hotkey.HotkeyRunnable;
import com.alee.utils.CollectionUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * This manager allows you to set extended tooltips for any Swing component with any possible content (would it be simple text or some
 * JComponent ancestor) or show one-time tooltips at custom location inside any window. Also this manager is integrated with HotkeyManager
 * and provides an opportunity to automatically show components hotkeys on their tooltips set with this class.
 *
 * @author Mikle Garin
 * @see GlassPaneManager
 * @see HotkeyManager
 */

public final class TooltipManager
{
    // Default settings
    private static int defaultDelay = 500;
    private static boolean allowMultiplyTooltips = true;
    private static boolean showHotkeysInTooltips = true;
    private static boolean showHotkeysInOneTimeTooltips = false;

    // Standart tooltips
    private static Map<Component, List<WebCustomTooltip>> webTooltips = new WeakHashMap<Component, List<WebCustomTooltip>> ();
    private static Map<Component, MouseAdapter> adapters = new WeakHashMap<Component, MouseAdapter> ();
    private static Map<Component, WebTimer> timers = new WeakHashMap<Component, WebTimer> ();

    // One-time tooltips
    private static List<WebCustomTooltip> oneTimeTooltips = new ArrayList<WebCustomTooltip> ();

    // Initialization mark
    private static boolean initialized = false;

    /**
     * TooltipManager initialization
     */

    public static void initialize ()
    {
        if ( !initialized )
        {
            initialized = true;

            // Tooltips hide listener
            Toolkit.getDefaultToolkit ().addAWTEventListener ( new AWTEventListener ()
            {
                @Override
                public void eventDispatched ( AWTEvent event )
                {
                    if ( event instanceof MouseWheelEvent )
                    {
                        hideAllTooltips ();
                    }
                }
            }, AWTEvent.MOUSE_WHEEL_EVENT_MASK );
        }
    }

    /**
     * Hides all visible tooltips
     */

    public static void hideAllTooltips ()
    {
        // Hiding standart tooltips
        for ( Component component : CollectionUtils.copy ( webTooltips.keySet () ) )
        {
            // Stopping any timers
            WebTimer timer = timers.get ( component );
            if ( timer != null )
            {
                timer.stop ();
            }

            // Closing tooltips
            List<WebCustomTooltip> list = webTooltips.get ( component );
            if ( list != null && list.size () > 0 )
            {
                for ( WebCustomTooltip tooltip : CollectionUtils.copy ( list ) )
                {
                    tooltip.closeTooltip ();
                }
            }
        }

        // Hiding one-time tooltips
        List<WebCustomTooltip> clonedOneTimeTooltips = new ArrayList<WebCustomTooltip> ();
        clonedOneTimeTooltips.addAll ( oneTimeTooltips );
        for ( WebCustomTooltip tooltip : clonedOneTimeTooltips )
        {
            tooltip.closeTooltip ();
        }
    }

    /**
     * Registers standart tooltip
     */

    public static WebCustomTooltip setTooltip ( Component component, String tooltip )
    {
        return setTooltip ( component, tooltip, null );
    }

    public static WebCustomTooltip setTooltip ( Component component, Icon icon, String tooltip )
    {
        return setTooltip ( component, icon, tooltip, null );
    }

    public static WebCustomTooltip setTooltip ( Component component, String tooltip, TooltipWay tooltipWay )
    {
        return setTooltip ( component, tooltip, tooltipWay, defaultDelay );
    }

    public static WebCustomTooltip setTooltip ( Component component, Icon icon, String tooltip, TooltipWay tooltipWay )
    {
        return setTooltip ( component, icon, tooltip, tooltipWay, defaultDelay );
    }

    public static WebCustomTooltip setTooltip ( Component component, String tooltip, TooltipWay tooltipWay, int delay )
    {
        return setTooltip ( component, null, tooltip, tooltipWay, delay );
    }

    public static WebCustomTooltip setTooltip ( Component component, Icon icon, String tooltip, TooltipWay tooltipWay, int delay )
    {
        return setTooltip ( component, new WebLabel ( tooltip, icon ), tooltipWay, delay );
    }

    public static WebCustomTooltip setTooltip ( Component component, JComponent tooltip )
    {
        return setTooltip ( component, tooltip, null );
    }

    public static WebCustomTooltip setTooltip ( Component component, JComponent tooltip, int delay )
    {
        return setTooltip ( component, tooltip, null, delay );
    }

    public static WebCustomTooltip setTooltip ( Component component, JComponent tooltip, TooltipWay tooltipWay )
    {
        return setTooltip ( component, tooltip, tooltipWay, defaultDelay );
    }

    public static WebCustomTooltip setTooltip ( final Component component, final JComponent tooltip, final TooltipWay tooltipWay,
                                                final int delay )
    {
        return addTooltip ( component, tooltip, tooltipWay, delay, true );
    }

    public static WebCustomTooltip addTooltip ( Component component, String tooltip )
    {
        return addTooltip ( component, tooltip, null );
    }

    public static WebCustomTooltip addTooltip ( Component component, Icon icon, String tooltip )
    {
        return addTooltip ( component, icon, tooltip, null );
    }

    public static WebCustomTooltip addTooltip ( Component component, String tooltip, TooltipWay tooltipWay )
    {
        return addTooltip ( component, tooltip, tooltipWay, defaultDelay );
    }

    public static WebCustomTooltip addTooltip ( Component component, Icon icon, String tooltip, TooltipWay tooltipWay )
    {
        return addTooltip ( component, icon, tooltip, tooltipWay, defaultDelay );
    }

    public static WebCustomTooltip addTooltip ( Component component, String tooltip, TooltipWay tooltipWay, int delay )
    {
        return addTooltip ( component, null, tooltip, tooltipWay, delay );
    }

    public static WebCustomTooltip addTooltip ( Component component, Icon icon, String tooltip, TooltipWay tooltipWay, int delay )
    {
        return addTooltip ( component, new WebLabel ( tooltip, icon ), tooltipWay, delay );
    }

    public static WebCustomTooltip addTooltip ( Component component, JComponent tooltip )
    {
        return addTooltip ( component, tooltip, null );
    }

    public static WebCustomTooltip addTooltip ( Component component, JComponent tooltip, int delay )
    {
        return addTooltip ( component, tooltip, null, delay );
    }

    public static WebCustomTooltip addTooltip ( Component component, JComponent tooltip, TooltipWay tooltipWay )
    {
        return addTooltip ( component, tooltip, tooltipWay, defaultDelay );
    }

    public static WebCustomTooltip addTooltip ( final Component component, final JComponent tooltip, final TooltipWay tooltipWay,
                                                final int delay )
    {
        List<WebCustomTooltip> tooltips = webTooltips.get ( component );
        boolean clear = tooltips != null && tooltips.size () > 0 && !allowMultiplyTooltips;
        return addTooltip ( component, tooltip, tooltipWay, delay, clear );
    }

    private static WebCustomTooltip addTooltip ( final Component component, final JComponent tooltip, final TooltipWay tooltipWay,
                                                 final int delay, boolean clear )
    {
        // Erase old tooltip if more than one not allowed in this case
        if ( clear )
        {
            removeTooltips ( component );
        }

        // Create tooltips list if needed
        if ( webTooltips.get ( component ) == null )
        {
            webTooltips.put ( component, new ArrayList<WebCustomTooltip> () );
        }

        // Creating tooltip component
        final WebCustomTooltip customTooltip = new WebCustomTooltip ( component, tooltip, tooltipWay, showHotkeysInTooltips );
        webTooltips.get ( component ).add ( customTooltip );

        // Creating listeners for component if they aren't created yet
        if ( !timers.containsKey ( component ) || !adapters.containsKey ( component ) )
        {
            // Tooltip pop timer
            final WebTimer showTips = new WebTimer ( "TooltipManager.displayTimer", delay );
            showTips.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( ActionEvent e )
                {
                    Window wa = SwingUtils.getWindowAncestor ( component );
                    if ( wa != null && wa.isActive () )
                    {
                        showTooltips ( component, false );
                    }
                }
            } );
            showTips.setRepeats ( false );
            timers.put ( component, showTips );

            // Show/hide listener
            MouseAdapter mouseAdapter = new MouseAdapter ()
            {
                @Override
                public void mouseEntered ( MouseEvent e )
                {
                    // Component ancestor window
                    Window window = SwingUtils.getWindowAncestor ( component );

                    // Starting show timer if needed
                    if ( window.isShowing () && window.isActive () )
                    {
                        showTips.start ();
                    }
                }

                @Override
                public void mouseExited ( MouseEvent e )
                {
                    cancelTooltips ();
                }

                @Override
                public void mousePressed ( MouseEvent e )
                {
                    cancelTooltips ();
                }

                @Override
                public void mouseReleased ( MouseEvent e )
                {
                    cancelTooltips ();
                }

                private void cancelTooltips ()
                {
                    // Hiding component tooltips
                    showTips.stop ();
                    hideTooltips ( component );
                }
            };
            component.addMouseListener ( mouseAdapter );
            adapters.put ( component, mouseAdapter );
        }

        return customTooltip;
    }

    private static void hideTooltips ( Component component )
    {
        if ( webTooltips.get ( component ) != null )
        {
            List<WebCustomTooltip> tooltips = new ArrayList<WebCustomTooltip> ();
            tooltips.addAll ( webTooltips.get ( component ) );
            for ( WebCustomTooltip tooltip : tooltips )
            {
                tooltip.closeTooltip ();
            }
        }
    }

    /**
     * Displays component tooltips
     */

    public static boolean showTooltips ( Component component )
    {
        return showTooltips ( component, false );
    }

    public static boolean showTooltips ( Component component, boolean delayed )
    {
        if ( webTooltips.containsKey ( component ) && component.isShowing () )
        {
            if ( delayed )
            {
                timers.get ( component ).restart ();
            }
            else
            {
                WebGlassPane webGlassPane = GlassPaneManager.getGlassPane ( component );
                if ( webGlassPane != null )
                {
                    List<WebCustomTooltip> tooltips = new ArrayList<WebCustomTooltip> ();
                    tooltips.addAll ( webTooltips.get ( component ) );
                    for ( WebCustomTooltip tooltip : tooltips )
                    {
                        webGlassPane.showComponent ( tooltip );
                    }
                }
            }
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Displays all tooltips for component's window
     */

    public static void showAllTooltips ( Component component )
    {
        // Hiding all tooltips
        TooltipManager.hideAllTooltips ();

        // Displaying tooltips
        showAllTooltips ( SwingUtils.getWindowAncestor ( component ) );
    }

    private static void showAllTooltips ( Window window )
    {
        if ( window.isShowing () )
        {
            for ( Component component : webTooltips.keySet () )
            {
                if ( SwingUtils.getWindowAncestor ( component ) == window && component.isShowing () )
                {
                    for ( WebCustomTooltip tooltip : webTooltips.get ( component ) )
                    {
                        showOneTimeTooltip ( tooltip, false );
                    }
                }
            }
        }
    }

    /**
     * Displays all tooltips for all visible windows
     */

    public static void showAllTooltips ()
    {
        for ( Component component : webTooltips.keySet () )
        {
            if ( component.isShowing () )
            {
                for ( WebCustomTooltip tooltip : webTooltips.get ( component ) )
                {
                    showOneTimeTooltip ( tooltip, false );
                }
            }
        }
    }

    /**
     * Installs "show all hotkeys" action on window or component
     */

    //    public static void installShowAllTooltipsAction ( JDialog dialog )
    //    {
    //        installShowAllTooltipsAction ( dialog.getRootPane (), Hotkey.F1 );
    //    }
    //
    //    public static void installShowAllTooltipsAction ( JDialog dialog, HotkeyData hotkeyData )
    //    {
    //        installShowAllTooltipsAction ( dialog.getRootPane (), hotkeyData );
    //    }
    //
    //    public static void installShowAllTooltipsAction ( JFrame frame )
    //    {
    //        installShowAllTooltipsAction ( frame.getRootPane (), Hotkey.F1 );
    //    }
    //
    //    public static void installShowAllTooltipsAction ( JFrame frame, HotkeyData hotkeyData )
    //    {
    //        installShowAllTooltipsAction ( frame.getRootPane (), hotkeyData );
    //    }
    //
    //    public static void installShowAllTooltipsAction ( JWindow window )
    //    {
    //        installShowAllTooltipsAction ( window.getRootPane (), Hotkey.F1 );
    //    }
    //
    //    public static void installShowAllTooltipsAction ( JWindow window, HotkeyData hotkeyData )
    //    {
    //        installShowAllTooltipsAction ( window.getRootPane (), hotkeyData );
    //    }
    public static void installShowAllTooltipsAction ( final Component topComponent )
    {
        installShowAllTooltipsAction ( topComponent, Hotkey.F2 );
    }

    public static void installShowAllTooltipsAction ( final Component topComponent, HotkeyData hotkeyData )
    {
        HotkeyManager.registerHotkey ( topComponent, hotkeyData, new HotkeyRunnable ()
        {
            @Override
            public void run ( KeyEvent e )
            {
                showAllTooltips ( topComponent );
            }
        }, true );
    }

    /**
     * Removes component tooltips
     */

    public static void removeTooltips ( Component component )
    {
        if ( webTooltips.get ( component ) != null )
        {
            for ( WebCustomTooltip tooltip : CollectionUtils.copy ( webTooltips.get ( component ) ) )
            {
                removeTooltip ( component, tooltip );
            }
        }
    }

    public static void removeTooltips ( Component component, List<WebCustomTooltip> tooltips )
    {
        for ( WebCustomTooltip tooltip : tooltips )
        {
            removeTooltip ( component, tooltip );
        }
    }

    public static void removeTooltip ( Component component, WebCustomTooltip tooltip )
    {
        List<WebCustomTooltip> tooltips = webTooltips.get ( component );
        if ( tooltips != null && tooltips.contains ( tooltip ) )
        {
            // Removing all listeners in case its last component tooltip
            if ( tooltips.size () <= 1 )
            {
                // Removing mouse listeners
                component.removeMouseListener ( adapters.get ( component ) );
                adapters.remove ( component );

                // Clearing timers
                timers.get ( component ).stop ();
                timers.remove ( component );
            }

            // Removing registered tooltip
            tooltips.remove ( tooltip );

            // Hiding and destroying tooltip
            tooltip.closeTooltip ();
            tooltip.destroyTooltip ();

            // Removing component from list if its last tooltip removed
            if ( tooltips.size () == 0 )
            {
                webTooltips.remove ( component );
            }
        }
    }

    /**
     * Shows one-time tooltip
     */

    public static WebCustomTooltip showOneTimeTooltip ( Component component, Point point, String tooltip )
    {
        return showOneTimeTooltip ( component, point, tooltip, null );
    }

    public static WebCustomTooltip showOneTimeTooltip ( Component component, Point point, Icon icon, String tooltip )
    {
        return showOneTimeTooltip ( component, point, icon, tooltip, null );
    }

    public static WebCustomTooltip showOneTimeTooltip ( Component component, Point point, String tooltip, TooltipWay tooltipWay )
    {
        return showOneTimeTooltip ( component, point, null, tooltip, tooltipWay );
    }

    public static WebCustomTooltip showOneTimeTooltip ( Component component, Point point, Icon icon, String tooltip, TooltipWay tooltipWay )
    {
        WebCustomTooltip customTooltip = new WebCustomTooltip ( component, icon, tooltip, tooltipWay, showHotkeysInOneTimeTooltips );
        customTooltip.setDisplayLocation ( point );
        return showOneTimeTooltip ( customTooltip, true );
    }

    public static WebCustomTooltip showOneTimeTooltip ( Component component, Point point, JComponent tooltip )
    {
        return showOneTimeTooltip ( component, point, tooltip, null );
    }

    public static WebCustomTooltip showOneTimeTooltip ( Component component, Point point, JComponent tooltip, TooltipWay tooltipWay )
    {
        WebCustomTooltip customTooltip = new WebCustomTooltip ( component, tooltip, tooltipWay, showHotkeysInOneTimeTooltips );
        customTooltip.setDisplayLocation ( point );
        return showOneTimeTooltip ( customTooltip, true );
    }

    public static WebCustomTooltip showOneTimeTooltip ( WebCustomTooltip customTooltip )
    {
        return showOneTimeTooltip ( customTooltip, true );
    }

    private static WebCustomTooltip showOneTimeTooltip ( final WebCustomTooltip customTooltip, final boolean destroyOnClose )
    {
        // Checking if component is properly set and showing
        if ( customTooltip.getComponent () == null || !customTooltip.getComponent ().isShowing () )
        {
            return null;
        }

        // Checking if glass pane is available
        Window window = SwingUtils.getWindowAncestor ( customTooltip.getComponent () );
        final WebGlassPane webGlassPane = GlassPaneManager.getGlassPane ( window );
        if ( webGlassPane == null )
        {
            return null;
        }

        // Adding relocate listener
        final ComponentAdapter componentAdapter = new ComponentAdapter ()
        {
            @Override
            public void componentResized ( ComponentEvent e )
            {
                customTooltip.updateLocation ();
            }
        };
        webGlassPane.addComponentListener ( componentAdapter );

        // Global mouse listener
        final AWTEventListener closeListener = customTooltip.isDefaultCloseBehavior () ? new AWTEventListener ()
        {
            @Override
            public void eventDispatched ( AWTEvent event )
            {
                if ( event instanceof MouseEvent && event.getID () == MouseEvent.MOUSE_PRESSED )
                {
                    customTooltip.closeTooltip ();
                }
            }
        } : null;

        // Adding destroy sequence
        customTooltip.addTooltipListener ( new TooltipAdapter ()
        {
            @Override
            public void tooltipShowing ()
            {
                if ( customTooltip.isDefaultCloseBehavior () )
                {
                    // Mouse press listener to close one-time tooltip
                    Toolkit.getDefaultToolkit ().addAWTEventListener ( closeListener, AWTEvent.MOUSE_EVENT_MASK );
                }
            }

            @Override
            public void tooltipHidden ()
            {
                // Removing tooltip listener
                customTooltip.removeTooltipListener ( this );

                if ( customTooltip.isDefaultCloseBehavior () )
                {
                    // Removing press listener
                    Toolkit.getDefaultToolkit ().removeAWTEventListener ( closeListener );
                }

                // Removing tooltip from list
                oneTimeTooltips.remove ( customTooltip );

                // Removing relocation listener
                webGlassPane.removeComponentListener ( componentAdapter );

                if ( destroyOnClose )
                {
                    // Destroying tooltip
                    customTooltip.destroyTooltip ();
                }
            }
        } );

        // Registering one-time tooltip
        oneTimeTooltips.add ( customTooltip );

        // Displaying one-time tooltip
        webGlassPane.showComponent ( customTooltip );

        return customTooltip;
    }

    /**
     * Default tooltip show delay
     */

    public static int getDefaultDelay ()
    {
        return defaultDelay;
    }

    public static void setDefaultDelay ( int delay )
    {
        defaultDelay = delay;
    }

    /**
     * Allow more than one tooltip per component
     */

    public static boolean isAllowMultiplyTooltips ()
    {
        return allowMultiplyTooltips;
    }

    public static void setAllowMultiplyTooltips ( boolean allowMultiplyTooltips )
    {
        TooltipManager.allowMultiplyTooltips = allowMultiplyTooltips;
    }

    /**
     * Show hotkeys in tooltips by default
     */

    public static boolean isShowHotkeysInTooltips ()
    {
        return showHotkeysInTooltips;
    }

    public static void setShowHotkeysInTooltips ( boolean showHotkeysInTooltips )
    {
        TooltipManager.showHotkeysInTooltips = showHotkeysInTooltips;
    }

    /**
     * Show hotkeys in one-time tooltips by default
     */

    public static boolean isShowHotkeysInOneTimeTooltips ()
    {
        return showHotkeysInOneTimeTooltips;
    }

    public static void setShowHotkeysInOneTimeTooltips ( boolean showHotkeysInOneTimeTooltips )
    {
        TooltipManager.showHotkeysInOneTimeTooltips = showHotkeysInOneTimeTooltips;
    }
}