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

import com.alee.laf.label.WebLabel;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.TooltipWay;
import com.alee.utils.CollectionUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.XmlUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

/**
 * User: mgarin Date: 11.07.11 Time: 12:54
 * <p/>
 * This manager allows you to quickly register global hotkeys (like accelerators on menu items in menubar menus) for any Swing component.
 * Additionally you can specify a component which will limit hotkey events to its area (meaning that hotkey event will occur only if this
 * component or any of its childs is focused when hotkey pressed).
 * <p/>
 * TooltipManager is integrated with this manager to automatically show component hotkeys in its tooltip if needed/allowed by tooltip and
 * hotkey settings.
 * <p/>
 * All hotkeys are stored into WeakHashMap so hotkeys will be removed as soon as the component for which hotkey is registered gets
 * finalized. HotkeyInfo also keeps a weak reference to both top and hotkey components.
 */

public class HotkeyManager
{
    // Synchronization object
    private static final Object sync = new Object ();

    // Global hotkeys block flag
    private static boolean hotkeysEnabled = true;

    // Pass focus to fired hotkey component
    private static boolean transferFocus = false;

    // Added hotkeys
    private static Map<Component, List<HotkeyInfo>> hotkeys = new WeakHashMap<Component, List<HotkeyInfo>> ();

    // Conditions for top components which might
    private static Map<Container, List<HotkeyCondition>> topComponentConditions = new WeakHashMap<Container, List<HotkeyCondition>> ();

    // Initialization mark
    private static boolean initialized = false;

    /**
     * HotkeyManager initialization
     */

    public static void initialize ()
    {
        if ( !initialized )
        {
            initialized = true;

            // XStream aliases
            XmlUtils.processAnnotations ( HotkeyData.class );

            // AWT hotkeys listener
            Toolkit.getDefaultToolkit ().addAWTEventListener ( new AWTEventListener ()
            {
                public void eventDispatched ( AWTEvent event )
                {
                    // Only if hotkeys enabled and we recieved a KeyEvent
                    if ( hotkeysEnabled && event instanceof KeyEvent )
                    {
                        KeyEvent e = ( KeyEvent ) event;

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

    private static boolean hotkeyForEventExists ( KeyEvent keyEvent )
    {
        synchronized ( sync )
        {
            int hotkeyHash = SwingUtils.hotkeyToString ( keyEvent ).hashCode ();
            for ( Map.Entry<Component, List<HotkeyInfo>> entry : hotkeys.entrySet () )
            {
                for ( HotkeyInfo hotkeyInfo : entry.getValue () )
                {
                    if ( hotkeyInfo.getHotkeyData ().hashCode () == hotkeyHash )
                    {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private static void processHotkeys ( KeyEvent e )
    {
        synchronized ( sync )
        {
            for ( Map.Entry<Component, List<HotkeyInfo>> entry : hotkeys.entrySet () )
            {
                for ( HotkeyInfo hotkeyInfo : entry.getValue () )
                {
                    // Specified components
                    Component topComponent = hotkeyInfo.getTopComponent ();
                    Component forComponent = hotkeyInfo.getForComponent ();

                    // If there is no pointed components - hotkey will be global
                    if ( forComponent == null )
                    {
                        // Checking hotkey
                        if ( hotkeyInfo.getHotkeyData ().isTriggered ( e ) && hotkeyInfo.getAction () != null )
                        {
                            // Performing hotkey action
                            SwingUtils.invokeLater ( hotkeyInfo.getAction (), e );
                        }
                    }
                    else
                    {
                        // Finding top component
                        topComponent = topComponent != null ? topComponent : SwingUtils.getWindowAncestor ( forComponent );

                        // Checking if componen or one of its childs has focus
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
                                    SwingUtils.invokeLater ( hotkeyInfo.getAction (), e );
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean meetsParentConditions ( Component forComponent )
    {
        synchronized ( sync )
        {
            for ( Map.Entry<Container, List<HotkeyCondition>> entry : topComponentConditions.entrySet () )
            {
                if ( entry.getKey ().isAncestorOf ( forComponent ) )
                {
                    for ( HotkeyCondition condition : entry.getValue () )
                    {
                        if ( !condition.checkCondition ( forComponent ) )
                        {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }

    /**
     * Hotkey register methods
     */

    public static HotkeyInfo registerHotkey ( HotkeyData hotkeyData, HotkeyRunnable action )
    {
        HotkeyInfo hotkeyInfo = new HotkeyInfo ();
        hotkeyInfo.setHidden ( true );
        hotkeyInfo.setHotkeyData ( hotkeyData );
        hotkeyInfo.setAction ( action );

        cacheHotkey ( hotkeyInfo );

        return hotkeyInfo;
    }

    public static HotkeyInfo registerHotkey ( Component forComponent, HotkeyData hotkeyData, HotkeyRunnable action )
    {
        return registerHotkey ( null, forComponent, hotkeyData, action );
    }

    public static HotkeyInfo registerHotkey ( Component forComponent, HotkeyData hotkeyData, HotkeyRunnable action, boolean hidden )
    {
        return registerHotkey ( null, forComponent, hotkeyData, action, hidden );
    }

    public static HotkeyInfo registerHotkey ( Component topComponent, Component forComponent, HotkeyData hotkeyData, HotkeyRunnable action )
    {
        return registerHotkey ( topComponent, forComponent, hotkeyData, action, false );
    }

    public static HotkeyInfo registerHotkey ( Component topComponent, Component forComponent, HotkeyData hotkeyData, HotkeyRunnable action,
                                              TooltipWay tooltipWay )
    {
        return registerHotkey ( topComponent, forComponent, hotkeyData, action, false, tooltipWay );
    }

    public static HotkeyInfo registerHotkey ( Component topComponent, Component forComponent, HotkeyData hotkeyData, HotkeyRunnable action,
                                              boolean hidden )
    {
        return registerHotkey ( topComponent, forComponent, hotkeyData, action, hidden, null );
    }

    public static HotkeyInfo registerHotkey ( Component topComponent, Component forComponent, HotkeyData hotkeyData, HotkeyRunnable action,
                                              boolean hidden, TooltipWay tooltipWay )
    {
        HotkeyInfo hotkeyInfo = new HotkeyInfo ();
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

    public static HotkeyInfo registerHotkey ( AbstractButton forComponent, HotkeyData hotkeyData )
    {
        return registerHotkey ( null, forComponent, hotkeyData );
    }

    public static HotkeyInfo registerHotkey ( AbstractButton forComponent, HotkeyData hotkeyData, boolean hidden )
    {
        return registerHotkey ( null, forComponent, hotkeyData, hidden );
    }

    public static HotkeyInfo registerHotkey ( AbstractButton forComponent, HotkeyData hotkeyData, TooltipWay tooltipWay )
    {
        return registerHotkey ( null, forComponent, hotkeyData, tooltipWay );
    }

    public static HotkeyInfo registerHotkey ( Component topComponent, AbstractButton forComponent, HotkeyData hotkeyData )
    {
        return registerHotkey ( topComponent, forComponent, hotkeyData, false );
    }

    public static HotkeyInfo registerHotkey ( Component topComponent, final AbstractButton forComponent, HotkeyData hotkeyData,
                                              TooltipWay tooltipWay )
    {
        return registerHotkey ( topComponent, forComponent, hotkeyData, createAction ( forComponent ), false, tooltipWay );
    }

    public static HotkeyInfo registerHotkey ( Component topComponent, final AbstractButton forComponent, HotkeyData hotkeyData,
                                              boolean hidden )
    {
        return registerHotkey ( topComponent, forComponent, hotkeyData, createAction ( forComponent ), hidden, null );
    }

    private static HotkeyRunnable createAction ( final AbstractButton forComponent )
    {
        return new ButtonHotkeyRunnable ( forComponent );
    }

    /**
     * Sets component hotkey tip way
     */

    public static void setComponentHotkeyDisplayWay ( Component component, TooltipWay tooltipWay )
    {
        synchronized ( sync )
        {
            for ( HotkeyInfo hotkeyInfo : getComponentHotkeysCache ( component ) )
            {
                hotkeyInfo.setHotkeyDisplayWay ( tooltipWay );
            }
        }
    }

    /**
     * Sets top component additional hotkey trigger condition
     */

    public static void addContainerHotkeyCondition ( Container container, HotkeyCondition hotkeyCondition )
    {
        synchronized ( sync )
        {
            List<HotkeyCondition> clist = getContainerHotkeyConditionsCache ( container );
            clist.add ( hotkeyCondition );
            topComponentConditions.put ( container, clist );
        }
    }

    public static void removeContainerHotkeyCondition ( Container container, HotkeyCondition hotkeyCondition )
    {
        synchronized ( sync )
        {
            List<HotkeyCondition> clist = getContainerHotkeyConditionsCache ( container );
            clist.remove ( hotkeyCondition );
        }
    }

    public static void removeContainerHotkeyConditions ( Container container )
    {
        synchronized ( sync )
        {
            topComponentConditions.remove ( container );
        }
    }

    public static List<HotkeyCondition> getContainerHotkeyConditions ( Container container )
    {
        synchronized ( sync )
        {
            List<HotkeyCondition> list = topComponentConditions.get ( container );
            return list != null ? CollectionUtils.copy ( list ) : new ArrayList<HotkeyCondition> ();
        }
    }

    private static List<HotkeyCondition> getContainerHotkeyConditionsCache ( Container container )
    {
        synchronized ( sync )
        {
            List<HotkeyCondition> list = topComponentConditions.get ( container );
            return list != null ? list : new ArrayList<HotkeyCondition> ();
        }
    }

    /**
     * Hotkey removal methods
     */

    public static void unregisterHotkey ( HotkeyInfo hotkeyInfo )
    {
        clearHotkeyCache ( hotkeyInfo );
    }

    public static void unregisterHotkeys ( Component component )
    {
        clearHotkeysCache ( component );
    }

    /**
     * Hotkeys retrieval methods
     */

    public static List<HotkeyInfo> getComponentHotkeys ( Component component )
    {
        synchronized ( sync )
        {
            List<HotkeyInfo> list = hotkeys.get ( component );
            return list != null ? CollectionUtils.copy ( list ) : new ArrayList<HotkeyInfo> ();
        }
    }

    /**
     * All added hotkeys. Make sure you know what you are doing if you want to modify this map from outside, otherwise your actions might
     * change HotkeyManager behavior.
     */

    public static Map<Component, List<HotkeyInfo>> getAllHotkeys ()
    {
        return hotkeys;
    }

    /**
     * Hotkeys cache methods
     */

    private static void cacheHotkey ( HotkeyInfo hotkeyInfo )
    {
        synchronized ( sync )
        {
            List<HotkeyInfo> hlist = getComponentHotkeysCache ( hotkeyInfo.getForComponent () );
            hlist.add ( hotkeyInfo );
            hotkeys.put ( hotkeyInfo.getForComponent (), hlist );
        }
    }

    private static void clearHotkeyCache ( HotkeyInfo hotkeyInfo )
    {
        synchronized ( sync )
        {
            List<HotkeyInfo> hlist = getComponentHotkeysCache ( hotkeyInfo.getForComponent () );
            hlist.remove ( hotkeyInfo );
        }
    }

    private static void clearHotkeysCache ( List<HotkeyInfo> hotkeys )
    {
        for ( HotkeyInfo hotkeyInfo : hotkeys )
        {
            clearHotkeyCache ( hotkeyInfo );
        }
    }

    private static void clearHotkeysCache ( Component component )
    {
        synchronized ( sync )
        {
            hotkeys.remove ( component );
        }
    }

    private static List<HotkeyInfo> getComponentHotkeysCache ( Component component )
    {
        synchronized ( sync )
        {
            List<HotkeyInfo> list = hotkeys.get ( component );
            return list != null ? list : new ArrayList<HotkeyInfo> ();
        }
    }

    /**
     * Shows all visible components hotkeys
     */

    public static void showComponentHotkeys ()
    {
        // Hiding all tooltips
        TooltipManager.hideAllTooltips ();

        // Displaying one-time tips with hotkeys
        for ( Window window : Window.getWindows () )
        {
            showComponentHotkeys ( window );
        }
    }

    public static void showComponentHotkeys ( Component component )
    {
        // Hiding all tooltips
        TooltipManager.hideAllTooltips ();

        // Displaying one-time tips with hotkeys
        showComponentHotkeys ( SwingUtils.getWindowAncestor ( component ) );
    }

    private static void showComponentHotkeys ( Window window )
    {
        synchronized ( sync )
        {
            LinkedHashSet<Component> shown = new LinkedHashSet<Component> ();
            for ( Map.Entry<Component, List<HotkeyInfo>> entry : hotkeys.entrySet () )
            {
                for ( HotkeyInfo hotkeyInfo : entry.getValue () )
                {
                    if ( !hotkeyInfo.isHidden () )
                    {
                        Component forComponent = hotkeyInfo.getForComponent ();
                        if ( forComponent != null && !shown.contains ( forComponent ) && forComponent.isVisible () &&
                                forComponent.isShowing () && SwingUtils.getWindowAncestor ( forComponent ) == window )
                        {
                            WebLabel tip = new WebLabel ( HotkeyManager.getComponentHotkeysString ( forComponent ) );
                            SwingUtils.setBoldFont ( tip );
                            TooltipManager.showOneTimeTooltip ( forComponent, null, tip, hotkeyInfo.getHotkeyDisplayWay () );
                            shown.add ( forComponent );
                        }
                    }
                }
            }
        }
    }

    /**
     * Installs "show all hotkeys" action on window or component
     */

    public static void installShowAllHotkeysAction ( final Component topComponent )
    {
        installShowAllHotkeysAction ( topComponent, Hotkey.F1 );
    }

    public static void installShowAllHotkeysAction ( final Component topComponent, HotkeyData hotkeyData )
    {
        HotkeyManager.registerHotkey ( topComponent, topComponent, hotkeyData, new HotkeyRunnable ()
        {
            public void run ( KeyEvent e )
            {
                HotkeyManager.showComponentHotkeys ( topComponent );
            }
        }, true );
    }

    /**
     * All component hotkeys list
     */

    public static String getComponentHotkeysString ( Component component )
    {
        synchronized ( sync )
        {
            return getComponentHotkeysString ( getComponentHotkeysCache ( component ) );
        }
    }

    private static String getComponentHotkeysString ( List<HotkeyInfo> infoList )
    {
        StringBuilder hotkeys = new StringBuilder ( "" );
        if ( infoList != null )
        {
            for ( HotkeyInfo hotkeyInfo : infoList )
            {
                if ( !hotkeyInfo.isHidden () )
                {
                    hotkeys.append ( hotkeyInfo.getHotkeyData ().toString () );
                    if ( infoList.indexOf ( hotkeyInfo ) < infoList.size () - 1 )
                    {
                        hotkeys.append ( ", " );
                    }
                }
            }
        }
        return hotkeys.toString ();
    }

    /**
     * Global hotkey block
     */

    public static void disableHotkeys ()
    {
        synchronized ( sync )
        {
            hotkeysEnabled = false;
        }
    }

    public static void enableHotkeys ()
    {
        synchronized ( sync )
        {
            hotkeysEnabled = true;
        }
    }

    /**
     * Should transfer focus to fired hotkey component or not
     */

    public static boolean isTransferFocus ()
    {
        synchronized ( sync )
        {
            return transferFocus;
        }
    }

    public static void setTransferFocus ( boolean transferFocus )
    {
        synchronized ( sync )
        {
            HotkeyManager.transferFocus = transferFocus;
        }
    }
}
