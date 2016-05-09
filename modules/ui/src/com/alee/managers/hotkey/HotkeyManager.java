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
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.style.StyleId;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.utils.CollectionUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.TextUtils;
import com.alee.utils.XmlUtils;
import com.alee.utils.compare.Filter;
import com.alee.utils.text.TextProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.List;

/**
 * This manager allows you to quickly register global hotkeys (like accelerators on menu items in menubar menus) for any Swing component.
 * Additionally you can specify a component which will limit hotkey events to its area (meaning that hotkey event will occur only if this
 * component or any of its children is focused when hotkey pressed).
 * <p>
 * TooltipManager is integrated with this manager to automatically show component hotkeys in its tooltip if needed/allowed by tooltip and
 * hotkey settings.
 * <p>
 * All hotkeys are stored into WeakHashMap so hotkeys will be removed as soon as the component for which hotkey is registered gets
 * finalized. HotkeyInfo also keeps a weak reference to both top and hotkey components.
 *
 * @author Mikle Garin
 */

public class HotkeyManager
{
    /**
     * Keys used to store custom data in JComponent.
     */
    public static final String COMPONENT_HOTKEYS_LIST_KEY = "hotkeys.list";
    public static final String CONTAINER_HOTKEY_CONDITIONS_LIST_KEY = "hotkey.conditions.list";

    /**
     * Separator used between multiply hotkeys displayed in a single line.
     */
    protected static final String HOTKEYS_SEPARATOR = ", ";

    /**
     * HotkeyInfo text provider.
     */
    protected static final TextProvider<HotkeyInfo> HOTKEY_TEXT_PROVIDER = new TextProvider<HotkeyInfo> ()
    {
        @Override
        public String getText ( final HotkeyInfo object )
        {
            return object.getHotkeyData ().toString ();
        }
    };

    /**
     * Displayed hotkeys filter.
     */
    protected static final Filter<HotkeyInfo> HOTKEY_DISPLAY_FILTER = new Filter<HotkeyInfo> ()
    {
        @Override
        public boolean accept ( final HotkeyInfo object )
        {
            return !object.isHidden ();
        }
    };

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
    protected static Map<JComponent, WeakReference<List<HotkeyInfo>>> hotkeys =
            new WeakHashMap<JComponent, WeakReference<List<HotkeyInfo>>> ();

    /**
     * Global hotkeys list.
     */
    protected static List<HotkeyInfo> globalHotkeys = new ArrayList<HotkeyInfo> ( 2 );

    /**
     * Conditions for top components which might.
     */
    protected static Map<JComponent, WeakReference<List<HotkeyCondition>>> containerConditions =
            new WeakHashMap<JComponent, WeakReference<List<HotkeyCondition>>> ();

    /**
     * Initialization mark.
     */
    protected static boolean initialized = false;

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
     * Returns a full copy of hotkeys map.
     * Returned map is a HashMap instead of WeakHashMap used in manager and will keep stong references to hotkey components.
     *
     * @return full copy of hotkeys map
     */
    protected static Map<Component, List<HotkeyInfo>> copyComponentHotkeys ()
    {
        synchronized ( sync )
        {
            final Map<Component, List<HotkeyInfo>> copy = new HashMap<Component, List<HotkeyInfo>> ( hotkeys.size () );
            for ( final Map.Entry<JComponent, WeakReference<List<HotkeyInfo>>> entry : hotkeys.entrySet () )
            {
                final JComponent component = entry.getKey ();
                final WeakReference<List<HotkeyInfo>> value = entry.getValue ();
                final List<HotkeyInfo> hotkeys = value != null ? value.get () : null;
                if ( component != null && hotkeys != null && !hotkeys.isEmpty () )
                {
                    copy.put ( component, CollectionUtils.copy ( hotkeys ) );
                }
            }
            return copy;
        }
    }

    /**
     * Returns a full copy of container conditions map.
     * Returned map is a HashMap instead of WeakHashMap used in manager and will keep stong references to condition containers.
     *
     * @return full copy of container conditions map
     */
    protected static Map<JComponent, List<HotkeyCondition>> copyContainerConditions ()
    {
        synchronized ( sync )
        {

            final Map<JComponent, List<HotkeyCondition>> copy =
                    new HashMap<JComponent, List<HotkeyCondition>> ( containerConditions.size () );
            for ( final Map.Entry<JComponent, WeakReference<List<HotkeyCondition>>> entry : containerConditions.entrySet () )
            {
                final JComponent component = entry.getKey ();
                final WeakReference<List<HotkeyCondition>> value = entry.getValue ();
                final List<HotkeyCondition> conditions = value != null ? value.get () : null;
                if ( component != null && conditions != null && !conditions.isEmpty () )
                {
                    copy.put ( component, conditions );
                }
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
        for ( final HotkeyInfo hotkeyInfo : globalHotkeys )
        {
            if ( hotkeyInfo.getHotkeyData ().hashCode () == hotkeyHash )
            {
                return true;
            }
        }
        for ( final Map.Entry<Component, List<HotkeyInfo>> entry : copyComponentHotkeys ().entrySet () )
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
     * Processes all available registered hotkeys.
     *
     * @param e key event
     */
    protected static void processHotkeys ( final KeyEvent e )
    {
        final Map<Component, List<HotkeyInfo>> hotkeysCopy = copyComponentHotkeys ();
        for ( final HotkeyInfo hotkeyInfo : globalHotkeys )
        {
            processHotkey ( e, hotkeyInfo );
        }
        for ( final Map.Entry<Component, List<HotkeyInfo>> entry : hotkeysCopy.entrySet () )
        {
            for ( final HotkeyInfo hotkeyInfo : entry.getValue () )
            {
                processHotkey ( e, hotkeyInfo );
            }
        }
    }

    /**
     * Processes single hotkey.
     *
     * @param e          key event
     * @param hotkeyInfo hotkey information
     */
    protected static void processHotkey ( final KeyEvent e, final HotkeyInfo hotkeyInfo )
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
                        SwingUtils.invokeLater ( hotkeyInfo.getAction (), e );
                    }
                }
            }
        }
    }

    protected static boolean meetsParentConditions ( final Component forComponent )
    {
        for ( final Map.Entry<JComponent, List<HotkeyCondition>> entry : copyContainerConditions ().entrySet () )
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

    protected static HotkeyRunnable createAction ( final AbstractButton forComponent )
    {
        return new ButtonHotkeyRunnable ( forComponent );
    }

    /**
     * Sets component hotkey tip way
     */

    public static void setComponentHotkeyDisplayWay ( final JComponent component, final TooltipWay tooltipWay )
    {
        synchronized ( sync )
        {
            final List<HotkeyInfo> hotkeys = getComponentHotkeysCache ( component );
            if ( hotkeys != null )
            {
                for ( final HotkeyInfo hotkeyInfo : hotkeys )
                {
                    hotkeyInfo.setHotkeyDisplayWay ( tooltipWay );
                }
            }
        }
    }

    /**
     * Sets top component additional hotkey trigger condition
     */

    public static void addContainerHotkeyCondition ( final JComponent container, final HotkeyCondition hotkeyCondition )
    {
        synchronized ( sync )
        {
            List<HotkeyCondition> clist = getContainerHotkeyConditionsCache ( container );
            if ( clist == null )
            {
                clist = new ArrayList<HotkeyCondition> ( 1 );
                container.putClientProperty ( CONTAINER_HOTKEY_CONDITIONS_LIST_KEY, clist );
                containerConditions.put ( container, new WeakReference<List<HotkeyCondition>> ( clist ) );
            }
            clist.add ( hotkeyCondition );
        }
    }

    public static void removeContainerHotkeyCondition ( final JComponent container, final HotkeyCondition hotkeyCondition )
    {
        synchronized ( sync )
        {
            final List<HotkeyCondition> clist = getContainerHotkeyConditionsCache ( container );
            if ( clist != null )
            {
                clist.remove ( hotkeyCondition );
            }
        }
    }

    public static void removeContainerHotkeyConditions ( final JComponent container, final List<HotkeyCondition> hotkeyConditions )
    {
        synchronized ( sync )
        {
            final List<HotkeyCondition> clist = getContainerHotkeyConditionsCache ( container );
            if ( clist != null )
            {
                clist.removeAll ( hotkeyConditions );
            }
        }
    }

    public static void removeContainerHotkeyConditions ( final JComponent container )
    {
        synchronized ( sync )
        {
            container.putClientProperty ( CONTAINER_HOTKEY_CONDITIONS_LIST_KEY, null );
            containerConditions.remove ( container );
        }
    }

    protected static List<HotkeyCondition> getContainerHotkeyConditionsCache ( final JComponent container )
    {
        synchronized ( sync )
        {
            final WeakReference<List<HotkeyCondition>> reference = containerConditions.get ( container );
            return reference != null ? reference.get () : null;
        }
    }

    public static List<HotkeyCondition> getContainerHotkeyConditions ( final JComponent container )
    {
        synchronized ( sync )
        {
            final List<HotkeyCondition> list = getContainerHotkeyConditionsCache ( container );
            return list != null ? CollectionUtils.copy ( list ) : new ArrayList<HotkeyCondition> ();
        }
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

    protected static void cacheHotkey ( final HotkeyInfo hotkeyInfo )
    {
        synchronized ( sync )
        {
            final JComponent forComponent = hotkeyInfo.getForComponent ();
            if ( forComponent != null )
            {
                // Caching component hotkey
                List<HotkeyInfo> hlist = getComponentHotkeysCache ( hotkeyInfo.getForComponent () );
                if ( hlist == null )
                {
                    hlist = new ArrayList<HotkeyInfo> ( 1 );
                    forComponent.putClientProperty ( COMPONENT_HOTKEYS_LIST_KEY, hlist );
                    hotkeys.put ( forComponent, new WeakReference<List<HotkeyInfo>> ( hlist ) );
                }
                hlist.add ( hotkeyInfo );
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
    }

    protected static void clearHotkeyCache ( final HotkeyInfo hotkeyInfo )
    {
        if ( hotkeyInfo != null )
        {
            synchronized ( sync )
            {
                final JComponent forComponent = hotkeyInfo.getForComponent ();
                if ( forComponent != null )
                {
                    // Clearing component hotkey cache
                    final List<HotkeyInfo> hlist = getComponentHotkeysCache ( forComponent );
                    if ( hlist != null )
                    {
                        hlist.remove ( hotkeyInfo );
                    }
                }
                else
                {
                    // Clearing global hotkey cache
                    globalHotkeys.remove ( hotkeyInfo );
                }
            }
        }
    }

    protected static void clearHotkeysCache ( final List<HotkeyInfo> hotkeysInfo )
    {
        if ( hotkeysInfo != null && !hotkeysInfo.isEmpty () )
        {
            synchronized ( sync )
            {
                for ( final HotkeyInfo hotkeyInfo : hotkeysInfo )
                {
                    // We have to clear each hotkey separately
                    // since there might be both global and component hotkeys in the list
                    clearHotkeyCache ( hotkeyInfo );
                }
            }
        }
    }

    protected static void clearHotkeysCache ( final JComponent component )
    {
        synchronized ( sync )
        {
            component.putClientProperty ( COMPONENT_HOTKEYS_LIST_KEY, null );
            hotkeys.remove ( component );
        }
    }

    protected static List<HotkeyInfo> getComponentHotkeysCache ( final JComponent forComponent )
    {
        synchronized ( sync )
        {
            final WeakReference<List<HotkeyInfo>> reference = hotkeys.get ( forComponent );
            return reference != null ? reference.get () : null;
        }
    }

    public static List<HotkeyInfo> getComponentHotkeys ( final JComponent component )
    {
        synchronized ( sync )
        {
            final List<HotkeyInfo> list = getComponentHotkeysCache ( component );
            return list != null ? CollectionUtils.copy ( list ) : new ArrayList<HotkeyInfo> ();
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
        // todo Can be optimized to use cache directly w/o copying the map
        final LinkedHashSet<Component> shown = new LinkedHashSet<Component> ();
        for ( final Map.Entry<Component, List<HotkeyInfo>> entry : copyComponentHotkeys ().entrySet () )
        {
            for ( final HotkeyInfo hotkeyInfo : entry.getValue () )
            {
                if ( !hotkeyInfo.isHidden () )
                {
                    final JComponent forComponent = hotkeyInfo.getForComponent ();
                    if ( forComponent != null && !shown.contains ( forComponent ) && forComponent.isVisible () &&
                            forComponent.isShowing () && SwingUtils.getWindowAncestor ( forComponent ) == window )
                    {
                        final WebLabel tip = new WebLabel ( HotkeyManager.getComponentHotkeysString ( forComponent ) );
                        tip.setStyleId ( StyleId.customtooltipLabel );
                        tip.setBoldFont ();
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
        synchronized ( sync )
        {
            final List<HotkeyInfo> hotkeys = getComponentHotkeysCache ( component );
            return TextUtils.listToString ( hotkeys, HOTKEYS_SEPARATOR, HOTKEY_TEXT_PROVIDER, HOTKEY_DISPLAY_FILTER );
        }
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