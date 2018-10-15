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

import com.alee.api.jdk.BiConsumer;
import com.alee.managers.glasspane.GlassPaneManager;
import com.alee.managers.glasspane.WebGlassPane;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.managers.hotkey.HotkeyRunnable;
import com.alee.utils.CollectionUtils;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.swing.WeakComponentData;
import com.alee.utils.swing.WeakComponentDataList;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This manager allows you to set extended tooltips for any Swing component with any possible content (would it be simple text or some
 * JComponent ancestor) or show one-time tooltips at custom location inside any window.
 *
 * Also this manager is integrated with {@link HotkeyManager} to provide components hotkeys on their tooltips.
 *
 * @author Mikle Garin
 * @see com.alee.managers.glasspane.GlassPaneManager
 * @see com.alee.managers.hotkey.HotkeyManager
 */
public final class TooltipManager
{
    /**
     * todo 1. Cleanup methods and JavaDoc
     * todo 2. Separate mapping of listeners for each tooltip?
     */

    // Default settings
    private static int defaultDelay = 400;
    private static boolean allowMultipleTooltips = true;
    private static boolean showHotkeysInTooltips = true;
    private static boolean showHotkeysInOneTimeTooltips = false;

    // Standart tooltips
    private static final WeakComponentDataList<JComponent, WebCustomTooltip> webTooltips =
            new WeakComponentDataList<JComponent, WebCustomTooltip> ( "TooltipManager.WebCustomTooltip", 50 );
    private static final WeakComponentData<JComponent, MouseAdapter> adapters =
            new WeakComponentData<JComponent, MouseAdapter> ( "TooltipManager.MouseAdapter", 50 );
    private static final WeakComponentData<JComponent, WebTimer> timers =
            new WeakComponentData<JComponent, WebTimer> ( "TooltipManager.WebTimer", 50 );

    // One-time tooltips
    private static final List<WebCustomTooltip> oneTimeTooltips = new ArrayList<WebCustomTooltip> ();

    // Initialization mark
    private static boolean initialized = false;

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
        // Stopping any display timers
        timers.forEach ( new BiConsumer<JComponent, WebTimer> ()
        {
            @Override
            public void accept ( final JComponent component, final WebTimer webTimer )
            {
                webTimer.stop ();
            }
        } );

        // Hiding standart tooltips
        webTooltips.forEachData ( new BiConsumer<JComponent, WebCustomTooltip> ()
        {
            @Override
            public void accept ( final JComponent component, final WebCustomTooltip webCustomTooltip )
            {
                webCustomTooltip.closeTooltip ();
            }
        } );

        // Hiding one-time tooltips
        for ( final WebCustomTooltip tooltip : CollectionUtils.copy ( oneTimeTooltips ) )
        {
            tooltip.closeTooltip ();
        }
    }

    /**
     * Registers standart tooltip
     */

    public static WebCustomTooltip setTooltip ( final JComponent component, final String tooltip )
    {
        return setTooltip ( component, tooltip, null );
    }

    public static WebCustomTooltip setTooltip ( final JComponent component, final String tooltip, final int delay )
    {
        return setTooltip ( component, tooltip, null, delay );
    }

    public static WebCustomTooltip setTooltip ( final JComponent component, final Icon icon, final String tooltip )
    {
        return setTooltip ( component, icon, tooltip, null );
    }

    public static WebCustomTooltip setTooltip ( final JComponent component, final String tooltip, final TooltipWay tooltipWay )
    {
        return setTooltip ( component, tooltip, tooltipWay, defaultDelay );
    }

    public static WebCustomTooltip setTooltip ( final JComponent component, final Icon icon, final String tooltip,
                                                final TooltipWay tooltipWay )
    {
        return setTooltip ( component, icon, tooltip, tooltipWay, defaultDelay );
    }

    public static WebCustomTooltip setTooltip ( final JComponent component, final String tooltip, final TooltipWay tooltipWay,
                                                final int delay )
    {
        return setTooltip ( component, null, tooltip, tooltipWay, delay );
    }

    public static WebCustomTooltip setTooltip ( final JComponent component, final Icon icon, final String tooltip,
                                                final TooltipWay tooltipWay, final int delay )
    {
        return setTooltip ( component, WebCustomTooltip.createDefaultComponent ( icon, tooltip ), tooltipWay, delay );
    }

    public static WebCustomTooltip setTooltip ( final JComponent component, final JComponent tooltip )
    {
        return setTooltip ( component, tooltip, null );
    }

    public static WebCustomTooltip setTooltip ( final JComponent component, final JComponent tooltip, final int delay )
    {
        return setTooltip ( component, tooltip, null, delay );
    }

    public static WebCustomTooltip setTooltip ( final JComponent component, final JComponent tooltip, final TooltipWay tooltipWay )
    {
        return setTooltip ( component, tooltip, tooltipWay, defaultDelay );
    }

    public static WebCustomTooltip setTooltip ( final JComponent component, final JComponent tooltip, final TooltipWay tooltipWay,
                                                final int delay )
    {
        return addTooltip ( component, tooltip, tooltipWay, delay, true );
    }

    public static WebCustomTooltip addTooltip ( final JComponent component, final String tooltip )
    {
        return addTooltip ( component, tooltip, null );
    }

    public static WebCustomTooltip addTooltip ( final JComponent component, final String tooltip, final int delay )
    {
        return addTooltip ( component, tooltip, null, delay );
    }

    public static WebCustomTooltip addTooltip ( final JComponent component, final Icon icon, final String tooltip )
    {
        return addTooltip ( component, icon, tooltip, null );
    }

    public static WebCustomTooltip addTooltip ( final JComponent component, final String tooltip, final TooltipWay tooltipWay )
    {
        return addTooltip ( component, tooltip, tooltipWay, defaultDelay );
    }

    public static WebCustomTooltip addTooltip ( final JComponent component, final Icon icon, final String tooltip,
                                                final TooltipWay tooltipWay )
    {
        return addTooltip ( component, icon, tooltip, tooltipWay, defaultDelay );
    }

    public static WebCustomTooltip addTooltip ( final JComponent component, final String tooltip, final TooltipWay tooltipWay,
                                                final int delay )
    {
        return addTooltip ( component, null, tooltip, tooltipWay, delay );
    }

    public static WebCustomTooltip addTooltip ( final JComponent component, final Icon icon, final String tooltip,
                                                final TooltipWay tooltipWay, final int delay )
    {
        return addTooltip ( component, WebCustomTooltip.createDefaultComponent ( icon, tooltip ), tooltipWay, delay );
    }

    public static WebCustomTooltip addTooltip ( final JComponent component, final JComponent tooltip )
    {
        return addTooltip ( component, tooltip, null );
    }

    public static WebCustomTooltip addTooltip ( final JComponent component, final JComponent tooltip, final int delay )
    {
        return addTooltip ( component, tooltip, null, delay );
    }

    public static WebCustomTooltip addTooltip ( final JComponent component, final JComponent tooltip, final TooltipWay tooltipWay )
    {
        return addTooltip ( component, tooltip, tooltipWay, defaultDelay );
    }

    public static WebCustomTooltip addTooltip ( final JComponent component, final JComponent tooltip, final TooltipWay tooltipWay,
                                                final int delay )
    {
        final boolean clear = webTooltips.containsData ( component ) && !allowMultipleTooltips;
        return addTooltip ( component, tooltip, tooltipWay, delay, clear );
    }

    private static WebCustomTooltip addTooltip ( final JComponent component, final JComponent tooltip, final TooltipWay tooltipWay,
                                                 final int delay, final boolean clear )
    {
        // Erase old tooltips if more than one not allowed in this case
        if ( clear )
        {
            removeTooltips ( component );
        }

        // Creating tooltip component
        final WebCustomTooltip customTooltip = new WebCustomTooltip ( component, tooltip, tooltipWay, showHotkeysInTooltips );
        webTooltips.add ( component, customTooltip );

        // Creating listeners for component if they aren't created yet
        if ( !timers.contains ( component ) )
        {
            // Tooltip pop timer
            final WebTimer showTips = new WebTimer ( "TooltipManager.displayTimer", delay );
            showTips.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    final Window window = CoreSwingUtils.getWindowAncestor ( component );
                    if ( window != null && window.isShowing () )
                    {
                        showTooltips ( component, false );
                    }
                }
            } );
            showTips.setRepeats ( false );
            timers.set ( component, showTips );

            // Show/hide listener
            final MouseAdapter mouseAdapter = new MouseAdapter ()
            {
                @Override
                public void mouseEntered ( final MouseEvent e )
                {
                    displayTooltips ();
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

                /**
                 * Startup tooltips display timer.
                 */
                private void displayTooltips ()
                {
                    final Window window = CoreSwingUtils.getWindowAncestor ( component );
                    if ( window != null && window.isShowing () )
                    {
                        showTips.start ();
                    }
                }

                /**
                 * Cancel timers and hide displayed tooltips.
                 */
                private void cancelTooltips ()
                {
                    showTips.stop ();
                    hideTooltips ( component );
                }
            };
            component.addMouseListener ( mouseAdapter );
            adapters.set ( component, mouseAdapter );
        }

        return customTooltip;
    }

    private static void hideTooltips ( final JComponent component )
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

    public static boolean showTooltips ( final JComponent component )
    {
        return showTooltips ( component, false );
    }

    public static boolean showTooltips ( final JComponent component, final boolean delayed )
    {
        if ( webTooltips.contains ( component ) && component.isShowing () )
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
                    webTooltips.forEachData ( component, new BiConsumer<JComponent, WebCustomTooltip> ()
                    {
                        @Override
                        public void accept ( final JComponent component, final WebCustomTooltip webCustomTooltip )
                        {
                            webGlassPane.showComponent ( webCustomTooltip );
                        }
                    } );
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

    public static void showAllTooltips ( final JComponent component )
    {
        // Hiding all tooltips
        TooltipManager.hideAllTooltips ();

        // Displaying tooltips
        showAllTooltips ( CoreSwingUtils.getWindowAncestor ( component ) );
    }

    private static void showAllTooltips ( final Window window )
    {
        if ( window.isShowing () )
        {
            webTooltips.forEachData ( new BiConsumer<JComponent, WebCustomTooltip> ()
            {
                @Override
                public void accept ( final JComponent component, final WebCustomTooltip webCustomTooltip )
                {
                    if ( CoreSwingUtils.getWindowAncestor ( component ) == window && component.isShowing () )
                    {
                        showOneTimeTooltip ( webCustomTooltip, false );
                    }
                }
            } );
        }
    }

    /**
     * Displays all tooltips for all visible windows
     */

    public static void showAllTooltips ()
    {
        webTooltips.forEachData ( new BiConsumer<JComponent, WebCustomTooltip> ()
        {
            @Override
            public void accept ( final JComponent component, final WebCustomTooltip webCustomTooltip )
            {
                if ( component.isShowing () )
                {
                    showOneTimeTooltip ( webCustomTooltip, false );
                }
            }
        } );
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

    public static void removeTooltips ( final JComponent component )
    {
        if ( webTooltips.get ( component ) != null )
        {
            for ( final WebCustomTooltip tooltip : CollectionUtils.copy ( webTooltips.get ( component ) ) )
            {
                removeTooltip ( component, tooltip );
            }
        }
    }

    public static void removeTooltips ( final JComponent component, final WebCustomTooltip... tooltips )
    {
        for ( final WebCustomTooltip tooltip : tooltips )
        {
            removeTooltip ( component, tooltip );
        }
    }

    public static void removeTooltips ( final JComponent component, final List<WebCustomTooltip> tooltips )
    {
        for ( final WebCustomTooltip tooltip : tooltips )
        {
            removeTooltip ( component, tooltip );
        }
    }

    public static void removeTooltip ( final JComponent component, final WebCustomTooltip tooltip )
    {
        webTooltips.remove ( component, tooltip, new BiConsumer<JComponent, WebCustomTooltip> ()
        {
            @Override
            public void accept ( final JComponent component, final WebCustomTooltip webCustomTooltip )
            {
                // Removing all listeners in case its last component tooltip
                if ( webTooltips.size ( component ) <= 1 )
                {
                    // Removing mouse listeners
                    adapters.clear ( component, new BiConsumer<JComponent, MouseAdapter> ()
                    {
                        @Override
                        public void accept ( final JComponent component, final MouseAdapter mouseAdapter )
                        {
                            component.removeMouseListener ( mouseAdapter );
                        }
                    } );

                    // Clearing timers
                    timers.clear ( component, new BiConsumer<JComponent, WebTimer> ()
                    {
                        @Override
                        public void accept ( final JComponent component, final WebTimer webTimer )
                        {
                            webTimer.stop ();
                        }
                    } );
                }

                // Hiding and destroying tooltip
                webCustomTooltip.closeTooltip ();
                webCustomTooltip.destroyTooltip ();
            }
        } );
    }

    /**
     * Shows one-time tooltip
     */

    public static WebCustomTooltip showOneTimeTooltip ( final JComponent component, final Point point, final String tooltip )
    {
        return showOneTimeTooltip ( component, point, tooltip, null );
    }

    public static WebCustomTooltip showOneTimeTooltip ( final JComponent component, final Point point, final Icon icon,
                                                        final String tooltip )
    {
        return showOneTimeTooltip ( component, point, icon, tooltip, null );
    }

    public static WebCustomTooltip showOneTimeTooltip ( final JComponent component, final Point point, final String tooltip,
                                                        final TooltipWay tooltipWay )
    {
        return showOneTimeTooltip ( component, point, null, tooltip, tooltipWay );
    }

    public static WebCustomTooltip showOneTimeTooltip ( final JComponent component, final Point point, final Icon icon,
                                                        final String tooltip,
                                                        final TooltipWay tooltipWay )
    {
        final WebCustomTooltip customTooltip = new WebCustomTooltip ( component, icon, tooltip, tooltipWay, showHotkeysInOneTimeTooltips );
        customTooltip.setDisplayLocation ( point );
        return showOneTimeTooltip ( customTooltip, true );
    }

    public static WebCustomTooltip showOneTimeTooltip ( final JComponent component, final Point point, final JComponent tooltip )
    {
        return showOneTimeTooltip ( component, point, tooltip, null );
    }

    public static WebCustomTooltip showOneTimeTooltip ( final JComponent component, final Point point, final JComponent tooltip,
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

    private static WebCustomTooltip showOneTimeTooltip ( final WebCustomTooltip customTooltip, final boolean destroyOnClose )
    {
        // Checking if component is properly set and showing
        if ( customTooltip.getComponent () == null || !customTooltip.getComponent ().isShowing () )
        {
            return null;
        }

        // Checking if glass pane is available
        final Window window = CoreSwingUtils.getWindowAncestor ( customTooltip.getComponent () );
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

    public static boolean isAllowMultipleTooltips ()
    {
        return allowMultipleTooltips;
    }

    public static void setAllowMultipleTooltips ( final boolean allowMultipleTooltips )
    {
        TooltipManager.allowMultipleTooltips = allowMultipleTooltips;
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