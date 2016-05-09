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

import com.alee.managers.glasspane.GlassPaneManager;
import com.alee.managers.glasspane.WebGlassPane;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.managers.hotkey.HotkeyRunnable;
import com.alee.managers.language.data.TooltipWay;
import com.alee.utils.CollectionUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * This manager allows you to set extended tooltips for any Swing component with any possible content (would it be simple text or some
 * JComponent ancestor) or show one-time tooltips at custom location inside any window.
 * <p>
 * Also this manager is integrated with HotkeyManager to provide components hotkeys on their tooltips.
 *
 * @author Mikle Garin
 * @see com.alee.managers.glasspane.GlassPaneManager
 * @see com.alee.managers.hotkey.HotkeyManager
 */

public class TooltipManager
{
    /**
     * todo 1. Synchronize actions on data maps and lists
     */

    // Default settings
    protected static int defaultDelay = 400;
    protected static boolean allowMultiplyTooltips = true;
    protected static boolean showHotkeysInTooltips = true;
    protected static boolean showHotkeysInOneTimeTooltips = false;

    // Standart tooltips
    protected static final Map<Component, List<WebCustomTooltip>> webTooltips = new WeakHashMap<Component, List<WebCustomTooltip>> ();
    protected static final Map<Component, MouseAdapter> adapters = new WeakHashMap<Component, MouseAdapter> ();
    protected static final Map<Component, WebTimer> timers = new WeakHashMap<Component, WebTimer> ();

    // One-time tooltips
    protected static final List<WebCustomTooltip> oneTimeTooltips = new ArrayList<WebCustomTooltip> ();

    // Initialization mark
    protected static boolean initialized = false;

    /**
     * TooltipManager initialization
     */

    public static synchronized void initialize ()
    {
        if ( !initialized )
        {
            initialized = true;

            // Tooltips hide listener
            Toolkit.getDefaultToolkit ().addAWTEventListener ( new AWTEventListener ()
            {
                @Override
                public void eventDispatched ( final AWTEvent event )
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
     * Hides all visible tooltips.
     */
    public static void hideAllTooltips ()
    {
        // Hiding standart tooltips
        for ( final Component component : CollectionUtils.copy ( webTooltips.keySet () ) )
        {
            // Stopping any timers
            final WebTimer timer = timers.get ( component );
            if ( timer != null )
            {
                timer.stop ();
            }

            // Closing tooltips
            final List<WebCustomTooltip> list = webTooltips.get ( component );
            if ( list != null && list.size () > 0 )
            {
                for ( final WebCustomTooltip tooltip : CollectionUtils.copy ( list ) )
                {
                    tooltip.closeTooltip ();
                }
            }
        }

        // Hiding one-time tooltips
        for ( final WebCustomTooltip tooltip : CollectionUtils.copy ( oneTimeTooltips ) )
        {
            tooltip.closeTooltip ();
        }
    }

    /**
     * Registers standart tooltip
     */

    public static WebCustomTooltip setTooltip ( final Component component, final String tooltip )
    {
        return setTooltip ( component, tooltip, null );
    }

    public static WebCustomTooltip setTooltip ( final Component component, final String tooltip, final int delay )
    {
        return setTooltip ( component, tooltip, null, delay );
    }

    public static WebCustomTooltip setTooltip ( final Component component, final Icon icon, final String tooltip )
    {
        return setTooltip ( component, icon, tooltip, null );
    }

    public static WebCustomTooltip setTooltip ( final Component component, final String tooltip, final TooltipWay tooltipWay )
    {
        return setTooltip ( component, tooltip, tooltipWay, defaultDelay );
    }

    public static WebCustomTooltip setTooltip ( final Component component, final Icon icon, final String tooltip,
                                                final TooltipWay tooltipWay )
    {
        return setTooltip ( component, icon, tooltip, tooltipWay, defaultDelay );
    }

    public static WebCustomTooltip setTooltip ( final Component component, final String tooltip, final TooltipWay tooltipWay,
                                                final int delay )
    {
        return setTooltip ( component, null, tooltip, tooltipWay, delay );
    }

    public static WebCustomTooltip setTooltip ( final Component component, final Icon icon, final String tooltip,
                                                final TooltipWay tooltipWay, final int delay )
    {
        return setTooltip ( component, WebCustomTooltip.createDefaultComponent ( icon, tooltip ), tooltipWay, delay );
    }

    public static WebCustomTooltip setTooltip ( final Component component, final JComponent tooltip )
    {
        return setTooltip ( component, tooltip, null );
    }

    public static WebCustomTooltip setTooltip ( final Component component, final JComponent tooltip, final int delay )
    {
        return setTooltip ( component, tooltip, null, delay );
    }

    public static WebCustomTooltip setTooltip ( final Component component, final JComponent tooltip, final TooltipWay tooltipWay )
    {
        return setTooltip ( component, tooltip, tooltipWay, defaultDelay );
    }

    public static WebCustomTooltip setTooltip ( final Component component, final JComponent tooltip, final TooltipWay tooltipWay,
                                                final int delay )
    {
        return addTooltip ( component, tooltip, tooltipWay, delay, true );
    }

    public static WebCustomTooltip addTooltip ( final Component component, final String tooltip )
    {
        return addTooltip ( component, tooltip, null );
    }

    public static WebCustomTooltip addTooltip ( final Component component, final String tooltip, final int delay )
    {
        return addTooltip ( component, tooltip, null, delay );
    }

    public static WebCustomTooltip addTooltip ( final Component component, final Icon icon, final String tooltip )
    {
        return addTooltip ( component, icon, tooltip, null );
    }

    public static WebCustomTooltip addTooltip ( final Component component, final String tooltip, final TooltipWay tooltipWay )
    {
        return addTooltip ( component, tooltip, tooltipWay, defaultDelay );
    }

    public static WebCustomTooltip addTooltip ( final Component component, final Icon icon, final String tooltip,
                                                final TooltipWay tooltipWay )
    {
        return addTooltip ( component, icon, tooltip, tooltipWay, defaultDelay );
    }

    public static WebCustomTooltip addTooltip ( final Component component, final String tooltip, final TooltipWay tooltipWay,
                                                final int delay )
    {
        return addTooltip ( component, null, tooltip, tooltipWay, delay );
    }

    public static WebCustomTooltip addTooltip ( final Component component, final Icon icon, final String tooltip,
                                                final TooltipWay tooltipWay, final int delay )
    {
        return addTooltip ( component, WebCustomTooltip.createDefaultComponent ( icon, tooltip ), tooltipWay, delay );
    }

    public static WebCustomTooltip addTooltip ( final Component component, final JComponent tooltip )
    {
        return addTooltip ( component, tooltip, null );
    }

    public static WebCustomTooltip addTooltip ( final Component component, final JComponent tooltip, final int delay )
    {
        return addTooltip ( component, tooltip, null, delay );
    }

    public static WebCustomTooltip addTooltip ( final Component component, final JComponent tooltip, final TooltipWay tooltipWay )
    {
        return addTooltip ( component, tooltip, tooltipWay, defaultDelay );
    }

    public static WebCustomTooltip addTooltip ( final Component component, final JComponent tooltip, final TooltipWay tooltipWay,
                                                final int delay )
    {
        final List<WebCustomTooltip> tooltips = webTooltips.get ( component );
        final boolean clear = tooltips != null && tooltips.size () > 0 && !allowMultiplyTooltips;
        return addTooltip ( component, tooltip, tooltipWay, delay, clear );
    }

    protected static WebCustomTooltip addTooltip ( final Component component, final JComponent tooltip, final TooltipWay tooltipWay,
                                                   final int delay, final boolean clear )
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
            // Weak component reference to avoid memory leaks due to listeners
            final WeakReference<Component> reference = new WeakReference<Component> ( component );

            // Tooltip pop timer
            final WebTimer showTips = new WebTimer ( "TooltipManager.displayTimer", delay );
            showTips.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    final Component c = reference.get ();
                    if ( c != null )
                    {
                        final Window wa = SwingUtils.getWindowAncestor ( c );
                        if ( wa != null && wa.isActive () )
                        {
                            showTooltips ( c, false );
                        }
                    }
                }
            } );
            showTips.setRepeats ( false );
            timers.put ( component, showTips );

            // Show/hide listener
            final MouseAdapter mouseAdapter = new MouseAdapter ()
            {
                @Override
                public void mouseEntered ( final MouseEvent e )
                {
                    // Checking component existance
                    final Component c = reference.get ();
                    if ( c != null )
                    {
                        // Component ancestor window
                        final Window window = SwingUtils.getWindowAncestor ( c );

                        // Starting show timer if needed
                        if ( window.isShowing () && window.isActive () )
                        {
                            showTips.start ();
                        }
                    }
                }

                @Override
                public void mouseExited ( final MouseEvent e )
                {
                    cancelTooltips ();
                }

                @Override
                public void mousePressed ( final MouseEvent e )
                {
                    cancelTooltips ();
                }

                @Override
                public void mouseReleased ( final MouseEvent e )
                {
                    cancelTooltips ();
                }

                private void cancelTooltips ()
                {
                    // Checking component existance
                    final Component c = reference.get ();
                    if ( c != null )
                    {
                        // Hiding component tooltips
                        showTips.stop ();
                        hideTooltips ( c );
                    }
                }
            };
            component.addMouseListener ( mouseAdapter );
            adapters.put ( component, mouseAdapter );
        }

        return customTooltip;
    }

    protected static void hideTooltips ( final Component component )
    {
        if ( webTooltips.get ( component ) != null )
        {
            final List<WebCustomTooltip> tooltips = new ArrayList<WebCustomTooltip> ();
            tooltips.addAll ( webTooltips.get ( component ) );
            for ( final WebCustomTooltip tooltip : tooltips )
            {
                tooltip.closeTooltip ();
            }
        }
    }

    /**
     * Displays component tooltips
     */

    public static boolean showTooltips ( final Component component )
    {
        return showTooltips ( component, false );
    }

    public static boolean showTooltips ( final Component component, final boolean delayed )
    {
        if ( webTooltips.containsKey ( component ) && component.isShowing () )
        {
            if ( delayed )
            {
                timers.get ( component ).restart ();
            }
            else
            {
                final WebGlassPane webGlassPane = GlassPaneManager.getGlassPane ( component );
                if ( webGlassPane != null )
                {
                    final List<WebCustomTooltip> tooltips = new ArrayList<WebCustomTooltip> ();
                    tooltips.addAll ( webTooltips.get ( component ) );
                    for ( final WebCustomTooltip tooltip : tooltips )
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

    public static void showAllTooltips ( final Component component )
    {
        // Hiding all tooltips
        TooltipManager.hideAllTooltips ();

        // Displaying tooltips
        showAllTooltips ( SwingUtils.getWindowAncestor ( component ) );
    }

    protected static void showAllTooltips ( final Window window )
    {
        if ( window.isShowing () )
        {
            for ( final Component component : webTooltips.keySet () )
            {
                if ( SwingUtils.getWindowAncestor ( component ) == window && component.isShowing () )
                {
                    for ( final WebCustomTooltip tooltip : webTooltips.get ( component ) )
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
        for ( final Component component : webTooltips.keySet () )
        {
            if ( component.isShowing () )
            {
                for ( final WebCustomTooltip tooltip : webTooltips.get ( component ) )
                {
                    showOneTimeTooltip ( tooltip, false );
                }
            }
        }
    }

    /**
     * Installs "show all hotkeys" action on window or component
     */

    public static void installShowAllTooltipsAction ( final JComponent topComponent )
    {
        installShowAllTooltipsAction ( topComponent, Hotkey.F2 );
    }

    public static void installShowAllTooltipsAction ( final JComponent topComponent, final HotkeyData hotkeyData )
    {
        HotkeyManager.registerHotkey ( topComponent, hotkeyData, new HotkeyRunnable ()
        {
            @Override
            public void run ( final KeyEvent e )
            {
                showAllTooltips ( topComponent );
            }
        }, true );
    }

    /**
     * Removes component tooltips
     */

    public static void removeTooltips ( final Component component )
    {
        if ( webTooltips.get ( component ) != null )
        {
            for ( final WebCustomTooltip tooltip : CollectionUtils.copy ( webTooltips.get ( component ) ) )
            {
                removeTooltip ( component, tooltip );
            }
        }
    }

    public static void removeTooltips ( final Component component, final WebCustomTooltip... tooltips )
    {
        for ( final WebCustomTooltip tooltip : tooltips )
        {
            removeTooltip ( component, tooltip );
        }
    }

    public static void removeTooltips ( final Component component, final List<WebCustomTooltip> tooltips )
    {
        for ( final WebCustomTooltip tooltip : tooltips )
        {
            removeTooltip ( component, tooltip );
        }
    }

    public static void removeTooltip ( final Component component, final WebCustomTooltip tooltip )
    {
        final List<WebCustomTooltip> tooltips = webTooltips.get ( component );
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

    public static WebCustomTooltip showOneTimeTooltip ( final Component component, final Point point, final String tooltip )
    {
        return showOneTimeTooltip ( component, point, tooltip, null );
    }

    public static WebCustomTooltip showOneTimeTooltip ( final Component component, final Point point, final Icon icon,
                                                        final String tooltip )
    {
        return showOneTimeTooltip ( component, point, icon, tooltip, null );
    }

    public static WebCustomTooltip showOneTimeTooltip ( final Component component, final Point point, final String tooltip,
                                                        final TooltipWay tooltipWay )
    {
        return showOneTimeTooltip ( component, point, null, tooltip, tooltipWay );
    }

    public static WebCustomTooltip showOneTimeTooltip ( final Component component, final Point point, final Icon icon, final String tooltip,
                                                        final TooltipWay tooltipWay )
    {
        final WebCustomTooltip customTooltip = new WebCustomTooltip ( component, icon, tooltip, tooltipWay, showHotkeysInOneTimeTooltips );
        customTooltip.setDisplayLocation ( point );
        return showOneTimeTooltip ( customTooltip, true );
    }

    public static WebCustomTooltip showOneTimeTooltip ( final Component component, final Point point, final JComponent tooltip )
    {
        return showOneTimeTooltip ( component, point, tooltip, null );
    }

    public static WebCustomTooltip showOneTimeTooltip ( final Component component, final Point point, final JComponent tooltip,
                                                        final TooltipWay tooltipWay )
    {
        final WebCustomTooltip customTooltip = new WebCustomTooltip ( component, tooltip, tooltipWay, showHotkeysInOneTimeTooltips );
        customTooltip.setDisplayLocation ( point );
        return showOneTimeTooltip ( customTooltip, true );
    }

    public static WebCustomTooltip showOneTimeTooltip ( final WebCustomTooltip customTooltip )
    {
        return showOneTimeTooltip ( customTooltip, true );
    }

    protected static WebCustomTooltip showOneTimeTooltip ( final WebCustomTooltip customTooltip, final boolean destroyOnClose )
    {
        // Checking if component is properly set and showing
        if ( customTooltip.getComponent () == null || !customTooltip.getComponent ().isShowing () )
        {
            return null;
        }

        // Checking if glass pane is available
        final Window window = SwingUtils.getWindowAncestor ( customTooltip.getComponent () );
        final WebGlassPane webGlassPane = GlassPaneManager.getGlassPane ( window );
        if ( webGlassPane == null )
        {
            return null;
        }

        // Adding relocate listener
        final ComponentAdapter componentAdapter = new ComponentAdapter ()
        {
            @Override
            public void componentResized ( final ComponentEvent e )
            {
                customTooltip.updateLocation ();
            }
        };
        webGlassPane.addComponentListener ( componentAdapter );

        // Global mouse listener
        final AWTEventListener closeListener = customTooltip.isDefaultCloseBehavior () ? new AWTEventListener ()
        {
            @Override
            public void eventDispatched ( final AWTEvent event )
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

    public static void setDefaultDelay ( final int delay )
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

    public static void setAllowMultiplyTooltips ( final boolean allowMultiplyTooltips )
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

    public static void setShowHotkeysInTooltips ( final boolean showHotkeysInTooltips )
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

    public static void setShowHotkeysInOneTimeTooltips ( final boolean showHotkeysInOneTimeTooltips )
    {
        TooltipManager.showHotkeysInOneTimeTooltips = showHotkeysInOneTimeTooltips;
    }
}