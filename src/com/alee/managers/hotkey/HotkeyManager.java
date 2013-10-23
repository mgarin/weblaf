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
 * This manager allows you to quickly register global hotkeys (like accelerators on menu items in menubar menus) for any Swing component.
 * Additionally you can specify a component which will limit hotkey events to its area (meaning that hotkey event will occur only if this
 * component or any of its childs is focused when hotkey pressed).
 * <p/>
 * TooltipManager is integrated with this manager to automatically show component hotkeys in its tooltip if needed/allowed by tooltip and
 * hotkey settings.
 * <p/>
 * All hotkeys are stored into WeakHashMap so hotkeys will be removed as soon as the component for which hotkey is registered gets
 * finalized. HotkeyInfo also keeps a weak reference to both top and hotkey components.
 *
 * @author Mikle Garin
 */

public final class HotkeyManager
{
    /**
     * Synchronization object.
     */
    protected static final Object sync = new Object ();

    /**
     * Global hotkeys block flag.
     */
    protected static boolean hotkeysEnabled = true;

    /**
     * Pass focus to fired hotkey component.
     */
    protected static boolean transferFocus = false;

    /**
     * Added hotkeys.
     */
    protected static Map<Component, List<HotkeyInfo>> hotkeys = new WeakHashMap<Component, List<HotkeyInfo>> ();

    /**
     * Conditions for top components which might.
     */
    protected static Map<Container, List<HotkeyCondition>> containerConditions = new WeakHashMap<Container, List<HotkeyCondition>> ();

    /**
     * Initialization mark.
     */
    protected static boolean initialized = false;

    /**
     * Initializes hotkey manager.
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
                @Override
                public void eventDispatched ( final AWTEvent event )
                {
                    // Only if hotkeys enabled and we recieved a KeyEvent
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
     * Returns a full copy of hotkeys map.
     * Returned map is a HashMap instead of WeakHashMap used in manager and will keep stong references to hotkey components.
     *
     * @return full copy of hotkeys map
     */
    protected static Map<Component, List<HotkeyInfo>> copyHotkeys ()
    {
        synchronized ( sync )
        {
            final Map<Component, List<HotkeyInfo>> copy = new HashMap<Component, List<HotkeyInfo>> ( hotkeys.size () );
            for ( final Map.Entry<Component, List<HotkeyInfo>> entry : hotkeys.entrySet () )
            {
                copy.put ( entry.getKey (), CollectionUtils.copy ( entry.getValue () ) );
            }
            return copy;
        }
    }

    protected static Map<Container, List<HotkeyCondition>> copyContainerConditions ()
    {
        synchronized ( sync )
        {
            final Map<Container, List<HotkeyCondition>> copy =
                    new HashMap<Container, List<HotkeyCondition>> ( containerConditions.size () );
            for ( final Map.Entry<Container, List<HotkeyCondition>> entry : containerConditions.entrySet () )
            {
                copy.put ( entry.getKey (), CollectionUtils.copy ( entry.getValue () ) );
            }
            return copy;
        }
    }

    /**
     * Returns whether at least one hotkey for the specified key event exists or not.
     *
     * @param keyEvent key event to search hotkeys for
     * @return true if at least one hotkey for the specified key event exists, false otherwise
     */
    protected static boolean hotkeyForEventExists ( final KeyEvent keyEvent )
    {
        final int hotkeyHash = SwingUtils.hotkeyToString ( keyEvent ).hashCode ();
        for ( final Map.Entry<Component, List<HotkeyInfo>> entry : copyHotkeys ().entrySet () )
        {
            for ( final HotkeyInfo hotkeyInfo : entry.getValue () )
            {
                if ( hotkeyInfo.getHotkeyData ().hashCode () == hotkeyHash )
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param e
     */
    protected static void processHotkeys ( final KeyEvent e )
    {
        for ( final Map.Entry<Component, List<HotkeyInfo>> entry : copyHotkeys ().entrySet () )
        {
            for ( final HotkeyInfo hotkeyInfo : entry.getValue () )
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
                        SwingUtils.invokeLater ( hotkeyInfo.getAction (), e );
                    }
                }
                else
                {
                    // Finding top component
                    Component topComponent = hotkeyInfo.getTopComponent ();
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

    protected static boolean meetsParentConditions ( final Component forComponent )
    {
        for ( final Map.Entry<Container, List<HotkeyCondition>> entry : copyContainerConditions ().entrySet () )
        {
            if ( entry.getKey ().isAncestorOf ( forComponent ) )
            {
                for ( final HotkeyCondition condition : entry.getValue () )
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

    public static HotkeyInfo registerHotkey ( final Component forComponent, final HotkeyData hotkeyData, final HotkeyRunnable action )
    {
        return registerHotkey ( null, forComponent, hotkeyData, action );
    }

    public static HotkeyInfo registerHotkey ( final Component forComponent, final HotkeyData hotkeyData, final HotkeyRunnable action,
                                              final boolean hidden )
    {
        return registerHotkey ( null, forComponent, hotkeyData, action, hidden );
    }

    public static HotkeyInfo registerHotkey ( final Component topComponent, final Component forComponent, final HotkeyData hotkeyData,
                                              final HotkeyRunnable action )
    {
        return registerHotkey ( topComponent, forComponent, hotkeyData, action, false );
    }

    public static HotkeyInfo registerHotkey ( final Component topComponent, final Component forComponent, final HotkeyData hotkeyData,
                                              final HotkeyRunnable action, final TooltipWay tooltipWay )
    {
        return registerHotkey ( topComponent, forComponent, hotkeyData, action, false, tooltipWay );
    }

    public static HotkeyInfo registerHotkey ( final Component topComponent, final Component forComponent, final HotkeyData hotkeyData,
                                              final HotkeyRunnable action, final boolean hidden )
    {
        return registerHotkey ( topComponent, forComponent, hotkeyData, action, hidden, null );
    }

    public static HotkeyInfo registerHotkey ( final Component topComponent, final Component forComponent, final HotkeyData hotkeyData,
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

    protected static HotkeyRunnable createAction ( final AbstractButton forComponent )
    {
        return new ButtonHotkeyRunnable ( forComponent );
    }

    /**
     * Sets component hotkey tip way
     */

    public static void setComponentHotkeyDisplayWay ( final Component component, final TooltipWay tooltipWay )
    {
        synchronized ( sync )
        {
            for ( final HotkeyInfo hotkeyInfo : getComponentHotkeysCache ( component ) )
            {
                hotkeyInfo.setHotkeyDisplayWay ( tooltipWay );
            }
        }
    }

    /**
     * Sets top component additional hotkey trigger condition
     */

    public static void addContainerHotkeyCondition ( final Container container, final HotkeyCondition hotkeyCondition )
    {
        synchronized ( sync )
        {
            final List<HotkeyCondition> clist = getContainerHotkeyConditionsCache ( container );
            clist.add ( hotkeyCondition );
            containerConditions.put ( container, clist );
        }
    }

    public static void removeContainerHotkeyCondition ( final Container container, final HotkeyCondition hotkeyCondition )
    {
        synchronized ( sync )
        {
            final List<HotkeyCondition> clist = getContainerHotkeyConditionsCache ( container );
            clist.remove ( hotkeyCondition );
        }
    }

    public static void removeContainerHotkeyConditions ( final Container container )
    {
        synchronized ( sync )
        {
            containerConditions.remove ( container );
        }
    }

    public static List<HotkeyCondition> getContainerHotkeyConditions ( final Container container )
    {
        synchronized ( sync )
        {
            final List<HotkeyCondition> list = containerConditions.get ( container );
            return list != null ? CollectionUtils.copy ( list ) : new ArrayList<HotkeyCondition> ();
        }
    }

    protected static List<HotkeyCondition> getContainerHotkeyConditionsCache ( final Container container )
    {
        synchronized ( sync )
        {
            final List<HotkeyCondition> list = containerConditions.get ( container );
            return list != null ? list : new ArrayList<HotkeyCondition> ();
        }
    }

    /**
     * Hotkey removal methods
     */

    public static void unregisterHotkey ( final HotkeyInfo hotkeyInfo )
    {
        clearHotkeyCache ( hotkeyInfo );
    }

    public static void unregisterHotkeys ( final Component component )
    {
        clearHotkeysCache ( component );
    }

    /**
     * Hotkeys retrieval methods
     */

    public static List<HotkeyInfo> getComponentHotkeys ( final Component component )
    {
        synchronized ( sync )
        {
            final List<HotkeyInfo> list = hotkeys.get ( component );
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

    protected static void cacheHotkey ( final HotkeyInfo hotkeyInfo )
    {
        synchronized ( sync )
        {
            final List<HotkeyInfo> hlist = getComponentHotkeysCache ( hotkeyInfo.getForComponent () );
            hlist.add ( hotkeyInfo );
            hotkeys.put ( hotkeyInfo.getForComponent (), hlist );
        }
    }

    protected static void clearHotkeyCache ( final HotkeyInfo hotkeyInfo )
    {
        synchronized ( sync )
        {
            final List<HotkeyInfo> hlist = getComponentHotkeysCache ( hotkeyInfo.getForComponent () );
            hlist.remove ( hotkeyInfo );
        }
    }

    protected static void clearHotkeysCache ( final List<HotkeyInfo> hotkeys )
    {
        for ( final HotkeyInfo hotkeyInfo : hotkeys )
        {
            clearHotkeyCache ( hotkeyInfo );
        }
    }

    protected static void clearHotkeysCache ( final Component component )
    {
        synchronized ( sync )
        {
            hotkeys.remove ( component );
        }
    }

    protected static List<HotkeyInfo> getComponentHotkeysCache ( final Component component )
    {
        synchronized ( sync )
        {
            final List<HotkeyInfo> list = hotkeys.get ( component );
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
        showComponentHotkeys ( SwingUtils.getWindowAncestor ( component ) );
    }

    protected static void showComponentHotkeys ( final Window window )
    {
        final LinkedHashSet<Component> shown = new LinkedHashSet<Component> ();
        for ( final Map.Entry<Component, List<HotkeyInfo>> entry : copyHotkeys ().entrySet () )
        {
            for ( final HotkeyInfo hotkeyInfo : entry.getValue () )
            {
                if ( !hotkeyInfo.isHidden () )
                {
                    final Component forComponent = hotkeyInfo.getForComponent ();
                    if ( forComponent != null && !shown.contains ( forComponent ) && forComponent.isVisible () &&
                            forComponent.isShowing () && SwingUtils.getWindowAncestor ( forComponent ) == window )
                    {
                        final WebLabel tip = new WebLabel ( HotkeyManager.getComponentHotkeysString ( forComponent ) );
                        SwingUtils.setBoldFont ( tip );
                        TooltipManager.showOneTimeTooltip ( forComponent, null, tip, hotkeyInfo.getHotkeyDisplayWay () );
                        shown.add ( forComponent );
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

    public static void installShowAllHotkeysAction ( final Component topComponent, final HotkeyData hotkeyData )
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

    public static String getComponentHotkeysString ( final Component component )
    {
        synchronized ( sync )
        {
            return getComponentHotkeysString ( getComponentHotkeysCache ( component ) );
        }
    }

    protected static String getComponentHotkeysString ( final List<HotkeyInfo> infoList )
    {
        final StringBuilder hotkeys = new StringBuilder ( "" );
        if ( infoList != null )
        {
            for ( final HotkeyInfo hotkeyInfo : infoList )
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

    public static void setTransferFocus ( final boolean transferFocus )
    {
        synchronized ( sync )
        {
            HotkeyManager.transferFocus = transferFocus;
        }
    }
}