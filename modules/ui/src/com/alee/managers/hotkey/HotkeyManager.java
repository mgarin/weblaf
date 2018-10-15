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

package com.alee.managers.hotkey;

import com.alee.api.jdk.BiConsumer;
import com.alee.api.jdk.BiPredicate;
import com.alee.api.jdk.Function;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.TooltipWay;
import com.alee.utils.*;
import com.alee.utils.compare.Filter;
import com.alee.utils.swing.WeakComponentDataList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * This manager allows you to quickly register global hotkeys (like accelerators on menu items in menubar menus) for any Swing component.
 * Additionally you can specify a component which will limit hotkey events to its area (meaning that hotkey event will occur only if this
 * component or any of its children is focused when hotkey pressed).
 *
 * TooltipManager is integrated with this manager to automatically show component hotkeys in its tooltip if needed/allowed by tooltip and
 * hotkey settings.
 *
 * All hotkeys are stored into WeakHashMap so hotkeys will be removed as soon as the component for which hotkey is registered gets
 * finalized. HotkeyInfo also keeps a weak reference to both top and hotkey components.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-HotkeyManager">How to use HotkeyManager</a>
 * @deprecated Requires a rework to make use of the common Swing action map but in a more convenient fashion
 */
@Deprecated
public final class HotkeyManager
{
    /**
     * Separator used between multiple hotkeys displayed in a single line.
     */
    private static final String HOTKEYS_SEPARATOR = ", ";

    /**
     * HotkeyInfo text provider.
     */
    private static final Function<HotkeyInfo, String> HOTKEY_TEXT_PROVIDER = new Function<HotkeyInfo, String> ()
    {
        @Override
        public String apply ( final HotkeyInfo hotkeyInfo )
        {
            return hotkeyInfo.getHotkeyData ().toString ();
        }
    };

    /**
     * Displayed hotkeys filter.
     */
    private static final Filter<HotkeyInfo> HOTKEY_DISPLAY_FILTER = new Filter<HotkeyInfo> ()
    {
        @Override
        public boolean accept ( final HotkeyInfo object )
        {
            return !object.isHidden ();
        }
    };

    /**
     * Global hotkeys block flag.
     */
    private static boolean hotkeysEnabled = true;

    /**
     * Pass focus to fired hotkey component.
     */
    private static boolean transferFocus = false;

    /**
     * Added hotkeys.
     */
    private static final WeakComponentDataList<JComponent, HotkeyInfo> hotkeys =
            new WeakComponentDataList<JComponent, HotkeyInfo> ( "HotkeyManager.HotkeyInfo", 20 );

    /**
     * Global hotkeys list.
     */
    private static final List<HotkeyInfo> globalHotkeys = new ArrayList<HotkeyInfo> ( 2 );

    /**
     * Conditions for top components which might.
     * todo Get rid of this and make hotkeys use Swing mapping instead of global listening
     */
    @Deprecated
    private static final WeakComponentDataList<JComponent, HotkeyCondition> containerConditions =
            new WeakComponentDataList<JComponent, HotkeyCondition> ( "HotkeyManager.HotkeyCondition", 5 );

    /**
     * Initialization mark.
     */
    private static boolean initialized = false;

    /**
     * Initializes hotkey manager.
     */
    public static synchronized void initialize ()
    {
        if ( !initialized )
        {
            initialized = true;

            // XStream aliases
            XmlUtils.processAnnotations ( HotkeyData.class );

            // AWT hotkeys listener
            Toolkit.getDefaultToolkit ().addAWTEventListener ( new AWTEventListener ()
            {
                @Override
                public void eventDispatched ( final AWTEvent event )
                {
                    // Only if hotkeys enabled and we received a KeyEvent
                    if ( hotkeysEnabled && event instanceof KeyEvent )
                    {
                        final KeyEvent e = ( KeyEvent ) event;

                        // Ignore consumed and non-press events
                        if ( e.isConsumed () || e.getID () != KeyEvent.KEY_PRESSED )
                        {
                            return;
                        }

                        // Ignore nonexisting hotkeys
                        if ( !hotkeyForEventExists ( e ) )
                        {
                            return;
                        }

                        // Processing all added hotkeys
                        processHotkeys ( e );
                    }
                }
            }, AWTEvent.KEY_EVENT_MASK );
        }
    }

    /**
     * Returns whether at least one hotkey for the specified key event exists or not.
     * todo Might need a rework since events like Ctrl+Alt+A won't trigger Ctrl+A hotkey
     *
     * @param keyEvent key event to search hotkeys for
     * @return true if at least one hotkey for the specified key event exists, false otherwise
     */
    private static boolean hotkeyForEventExists ( final KeyEvent keyEvent )
    {
        for ( final HotkeyInfo hotkeyInfo : globalHotkeys )
        {
            if ( hotkeyInfo.getHotkeyData ().isTriggered ( keyEvent ) )
            {
                return true;
            }
        }
        return hotkeys.anyDataMatch ( new BiPredicate<JComponent, HotkeyInfo> ()
        {
            @Override
            public boolean test ( final JComponent component, final HotkeyInfo hotkeyInfo )
            {
                return hotkeyInfo.getHotkeyData ().isTriggered ( keyEvent );
            }
        } );
    }

    /**
     * Processes all available registered hotkeys.
     *
     * @param e key event
     */
    private static void processHotkeys ( final KeyEvent e )
    {
        for ( final HotkeyInfo hotkeyInfo : globalHotkeys )
        {
            processHotkey ( e, hotkeyInfo );
        }
        hotkeys.forEachData ( new BiConsumer<JComponent, HotkeyInfo> ()
        {
            @Override
            public void accept ( final JComponent component, final HotkeyInfo hotkeyInfo )
            {
                processHotkey ( e, hotkeyInfo );
            }
        } );
    }

    /**
     * Processes single hotkey.
     *
     * @param e          key event
     * @param hotkeyInfo hotkey information
     */
    private static void processHotkey ( final KeyEvent e, final HotkeyInfo hotkeyInfo )
    {
        // Specified components
        final Component forComponent = hotkeyInfo.getForComponent ();

        // If there is no pointed components - hotkey will be global
        if ( forComponent == null )
        {
            // Checking hotkey
            if ( hotkeyInfo.getHotkeyData ().isTriggered ( e ) && hotkeyInfo.getAction () != null )
            {
                // Performing hotkey action
                invokeLater ( hotkeyInfo.getAction (), e );
            }
        }
        else
        {
            // Finding top component
            Component topComponent = hotkeyInfo.getTopComponent ();
            topComponent = topComponent != null ? topComponent : CoreSwingUtils.getWindowAncestor ( forComponent );

            // Checking if componen or one of its children has focus
            if ( SwingUtils.hasFocusOwner ( topComponent ) )
            {
                // Checking hotkey
                if ( hotkeyInfo.getHotkeyData ().isTriggered ( e ) && hotkeyInfo.getAction () != null )
                {
                    // Checking that hotkey meets parent containers conditions
                    if ( meetsParentConditions ( forComponent ) )
                    {
                        // Transferring focus to hotkey component
                        if ( transferFocus )
                        {
                            forComponent.requestFocusInWindow ();
                        }

                        // Performing hotkey action
                        invokeLater ( hotkeyInfo.getAction (), e );
                    }
                }
            }
        }
    }

    /**
     * Will invoke {@link HotkeyRunnable} later in EDT in case it is called from non-EDT thread.
     * todo This shouldn't be needed anymore after {@link HotkeyManager} rework
     *
     * @param runnable hotkey runnable
     * @param e        key event
     */
    private static void invokeLater ( final HotkeyRunnable runnable, final KeyEvent e )
    {
        if ( SwingUtilities.isEventDispatchThread () )
        {
            runnable.run ( e );
        }
        else
        {
            CoreSwingUtils.invokeLater ( new Runnable ()
            {
                @Override
                public void run ()
                {
                    runnable.run ( e );
                }
            } );
        }
    }

    private static boolean meetsParentConditions ( final Component forComponent )
    {
        return containerConditions.allDataMatch ( new BiPredicate<JComponent, HotkeyCondition> ()
        {
            @Override
            public boolean test ( final JComponent component, final HotkeyCondition hotkeyCondition )
            {
                return !component.isAncestorOf ( forComponent ) || hotkeyCondition.checkCondition ( forComponent );
            }
        } );
    }

    /**
     * Hotkey register methods
     */

    public static HotkeyInfo registerHotkey ( final HotkeyData hotkeyData, final HotkeyRunnable action )
    {
        final HotkeyInfo hotkeyInfo = new HotkeyInfo ();
        hotkeyInfo.setHidden ( true );
        hotkeyInfo.setHotkeyData ( hotkeyData );
        hotkeyInfo.setAction ( action );
        cacheHotkey ( hotkeyInfo );
        return hotkeyInfo;
    }

    public static HotkeyInfo registerHotkey ( final JComponent forComponent, final HotkeyData hotkeyData, final HotkeyRunnable action )
    {
        return registerHotkey ( null, forComponent, hotkeyData, action );
    }

    public static HotkeyInfo registerHotkey ( final JComponent forComponent, final HotkeyData hotkeyData, final HotkeyRunnable action,
                                              final boolean hidden )
    {
        return registerHotkey ( null, forComponent, hotkeyData, action, hidden );
    }

    public static HotkeyInfo registerHotkey ( final Component topComponent, final JComponent forComponent, final HotkeyData hotkeyData,
                                              final HotkeyRunnable action )
    {
        return registerHotkey ( topComponent, forComponent, hotkeyData, action, false );
    }

    public static HotkeyInfo registerHotkey ( final Component topComponent, final JComponent forComponent, final HotkeyData hotkeyData,
                                              final HotkeyRunnable action, final TooltipWay tooltipWay )
    {
        return registerHotkey ( topComponent, forComponent, hotkeyData, action, false, tooltipWay );
    }

    public static HotkeyInfo registerHotkey ( final Component topComponent, final JComponent forComponent, final HotkeyData hotkeyData,
                                              final HotkeyRunnable action, final boolean hidden )
    {
        return registerHotkey ( topComponent, forComponent, hotkeyData, action, hidden, null );
    }

    public static HotkeyInfo registerHotkey ( final Component topComponent, final JComponent forComponent, final HotkeyData hotkeyData,
                                              final HotkeyRunnable action, final boolean hidden, final TooltipWay tooltipWay )
    {
        final HotkeyInfo hotkeyInfo = new HotkeyInfo ();
        hotkeyInfo.setHidden ( hidden );
        hotkeyInfo.setTopComponent ( topComponent );
        hotkeyInfo.setForComponent ( forComponent );
        hotkeyInfo.setHotkeyData ( hotkeyData );
        hotkeyInfo.setHotkeyDisplayWay ( tooltipWay );
        hotkeyInfo.setAction ( action );
        cacheHotkey ( hotkeyInfo );
        return hotkeyInfo;
    }

    /**
     * Button-specific hotkey register methods
     */

    public static HotkeyInfo registerHotkey ( final AbstractButton forComponent, final HotkeyData hotkeyData )
    {
        return registerHotkey ( null, forComponent, hotkeyData );
    }

    public static HotkeyInfo registerHotkey ( final AbstractButton forComponent, final HotkeyData hotkeyData, final boolean hidden )
    {
        return registerHotkey ( null, forComponent, hotkeyData, hidden );
    }

    public static HotkeyInfo registerHotkey ( final AbstractButton forComponent, final HotkeyData hotkeyData, final TooltipWay tooltipWay )
    {
        return registerHotkey ( null, forComponent, hotkeyData, tooltipWay );
    }

    public static HotkeyInfo registerHotkey ( final Component topComponent, final AbstractButton forComponent, final HotkeyData hotkeyData )
    {
        return registerHotkey ( topComponent, forComponent, hotkeyData, false );
    }

    public static HotkeyInfo registerHotkey ( final Component topComponent, final AbstractButton forComponent, final HotkeyData hotkeyData,
                                              final TooltipWay tooltipWay )
    {
        return registerHotkey ( topComponent, forComponent, hotkeyData, createAction ( forComponent ), false, tooltipWay );
    }

    public static HotkeyInfo registerHotkey ( final Component topComponent, final AbstractButton forComponent, final HotkeyData hotkeyData,
                                              final boolean hidden )
    {
        return registerHotkey ( topComponent, forComponent, hotkeyData, createAction ( forComponent ), hidden, null );
    }

    private static HotkeyRunnable createAction ( final AbstractButton forComponent )
    {
        return new ButtonHotkeyRunnable ( forComponent );
    }

    /**
     * Sets top component additional hotkey trigger condition
     */

    public static void addContainerHotkeyCondition ( final JComponent container, final HotkeyCondition hotkeyCondition )
    {
        containerConditions.add ( container, hotkeyCondition );
    }

    public static void removeContainerHotkeyCondition ( final JComponent container, final HotkeyCondition hotkeyCondition )
    {
        containerConditions.remove ( container, hotkeyCondition );
    }

    public static void removeContainerHotkeyConditions ( final JComponent container, final List<HotkeyCondition> hotkeyConditions )
    {
        for ( final HotkeyCondition hotkeyCondition : hotkeyConditions )
        {
            containerConditions.remove ( container, hotkeyCondition );
        }
    }

    public static void removeContainerHotkeyConditions ( final JComponent container )
    {
        containerConditions.clear ( container );
    }

    public static List<HotkeyCondition> getContainerHotkeyConditions ( final JComponent container )
    {
        final List<HotkeyCondition> list = containerConditions.get ( container );
        return list != null ? CollectionUtils.copy ( list ) : new ArrayList<HotkeyCondition> ();
    }

    /**
     * Hotkey removal methods
     */

    public static void unregisterHotkey ( final HotkeyInfo hotkeyInfo )
    {
        clearHotkeyCache ( hotkeyInfo );
    }

    public static void unregisterHotkeys ( final JComponent component )
    {
        clearHotkeysCache ( component );
    }

    /**
     * Hotkeys cache methods
     */

    private static void cacheHotkey ( final HotkeyInfo hotkeyInfo )
    {
        final JComponent forComponent = hotkeyInfo.getForComponent ();
        if ( forComponent != null )
        {
            // Component hotkey
            hotkeys.add ( forComponent, hotkeyInfo );
        }
        else
        {
            // Caching global hotkey
            if ( !globalHotkeys.contains ( hotkeyInfo ) )
            {
                globalHotkeys.add ( hotkeyInfo );
            }
        }
    }

    private static void clearHotkeyCache ( final HotkeyInfo hotkeyInfo )
    {
        if ( hotkeyInfo != null )
        {
            final JComponent forComponent = hotkeyInfo.getForComponent ();
            if ( forComponent != null )
            {
                // Clearing component hotkey cache
                hotkeys.remove ( forComponent, hotkeyInfo );
            }
            else
            {
                // Clearing global hotkey cache
                globalHotkeys.remove ( hotkeyInfo );
            }
        }
    }

    private static void clearHotkeysCache ( final JComponent component )
    {
        hotkeys.clear ( component );
    }

    public static List<HotkeyInfo> getComponentHotkeys ( final JComponent component )
    {
        final List<HotkeyInfo> list = hotkeys.get ( component );
        return list != null ? CollectionUtils.copy ( list ) : new ArrayList<HotkeyInfo> ();
    }

    /**
     * Shows all visible components hotkeys
     */

    public static void showComponentHotkeys ()
    {
        // Hiding all tooltips
        TooltipManager.hideAllTooltips ();

        // Displaying one-time tips with hotkeys
        for ( final Window window : Window.getWindows () )
        {
            showComponentHotkeys ( window );
        }
    }

    public static void showComponentHotkeys ( final Component component )
    {
        // Hiding all tooltips
        TooltipManager.hideAllTooltips ();

        // Displaying one-time tips with hotkeys
        showComponentHotkeys ( CoreSwingUtils.getWindowAncestor ( component ) );
    }

    private static void showComponentHotkeys ( final Window window )
    {
        final LinkedHashSet<Component> shown = new LinkedHashSet<Component> ();
        hotkeys.forEachData ( new BiConsumer<JComponent, HotkeyInfo> ()
        {
            @Override
            public void accept ( final JComponent component, final HotkeyInfo hotkeyInfo )
            {
                if ( !hotkeyInfo.isHidden () )
                {
                    final JComponent forComponent = hotkeyInfo.getForComponent ();
                    if ( forComponent != null && !shown.contains ( forComponent ) && forComponent.isVisible () &&
                            forComponent.isShowing () && CoreSwingUtils.getWindowAncestor ( forComponent ) == window )
                    {
                        final String hotkey = HotkeyManager.getComponentHotkeysString ( forComponent );
                        final TooltipWay displayWay = hotkeyInfo.getHotkeyDisplayWay ();
                        TooltipManager.showOneTimeTooltip ( forComponent, null, hotkey, displayWay );
                        shown.add ( forComponent );
                    }
                }
            }
        } );
    }

    /**
     * Installs "show all hotkeys" action on window or component
     */

    public static void installShowAllHotkeysAction ( final JComponent topComponent )
    {
        installShowAllHotkeysAction ( topComponent, Hotkey.F1 );
    }

    public static void installShowAllHotkeysAction ( final JComponent topComponent, final HotkeyData hotkeyData )
    {
        HotkeyManager.registerHotkey ( topComponent, topComponent, hotkeyData, new HotkeyRunnable ()
        {
            @Override
            public void run ( final KeyEvent e )
            {
                HotkeyManager.showComponentHotkeys ( topComponent );
            }
        }, true );
    }

    /**
     * All component hotkeys list
     */

    public static String getComponentHotkeysString ( final JComponent component )
    {
        final List<HotkeyInfo> keys = hotkeys.get ( component );
        return TextUtils.listToString ( keys, HOTKEYS_SEPARATOR, HOTKEY_TEXT_PROVIDER, HOTKEY_DISPLAY_FILTER );
    }

    /**
     * Global hotkey block
     */

    public static void disableHotkeys ()
    {
        hotkeysEnabled = false;
    }

    public static void enableHotkeys ()
    {
        hotkeysEnabled = true;
    }

    /**
     * Should transfer focus to fired hotkey component or not
     */

    public static boolean isTransferFocus ()
    {
        return transferFocus;
    }

    public static void setTransferFocus ( final boolean transferFocus )
    {
        HotkeyManager.transferFocus = transferFocus;
    }
}