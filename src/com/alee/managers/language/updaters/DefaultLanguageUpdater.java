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

package com.alee.managers.language.updaters;

import com.alee.managers.hotkey.HotkeyInfo;
import com.alee.managers.language.data.Value;

import java.awt.*;
import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * This class prvides an additional set of methods to simplify language updaters usage.
 * Most of default predefined language updaters extend this class.
 *
 * @author Mikle Garin
 */

public abstract class DefaultLanguageUpdater<E extends Component> implements LanguageUpdater<E>
{
    /**
     * Predefined component states.
     */
    public static final String INPUT_PROMPT = "inputPropmt";
    public static final String DROP_TEXT = "dropText";

    /**
     * Hotkeys cache map.
     */
    private static final Map<Component, HotkeyInfo> hotkeysCache = new WeakHashMap<Component, HotkeyInfo> ();

    /**
     * {@inheritDoc}
     */
    @Override
    public Class getComponentClass ()
    {
        try
        {
            return ( Class ) ( ( ParameterizedType ) getClass ().getGenericSuperclass () ).getActualTypeArguments ()[ 0 ];
        }
        catch ( final Throwable e )
        {
            return null;
        }
    }

    /**
     * Caches component's hotkey
     *
     * @param component  component
     * @param hotkeyInfo hotkey data
     */
    protected static void cacheHotkey ( final Component component, final HotkeyInfo hotkeyInfo )
    {
        hotkeysCache.put ( component, hotkeyInfo );
    }

    /**
     * Returns wether hotkey is cached or not.
     *
     * @param component hotkey's component
     * @return true if hotkey is cached, false otherwise
     */
    protected static boolean isHotkeyCached ( final Component component )
    {
        return hotkeysCache.containsKey ( component );
    }

    /**
     * Returns cached hotkey data.
     *
     * @param component hotkey's component
     * @return cached hotkey data
     */
    protected static HotkeyInfo getCachedHotkey ( final Component component )
    {
        return hotkeysCache.get ( component );
    }

    /**
     * Returns default text taken from value and formatted using specified data.
     *
     * @param value language value
     * @param data  formatting data
     * @return formatted default text
     */
    protected String getDefaultText ( final Value value, final Object... data )
    {
        return getDefaultText ( null, value, data );
    }

    /**
     * Returns state text taken from value and formatted using specified data.
     *
     * @param state component state
     * @param value language value
     * @param data  formatting data
     * @return formatted state text
     */
    protected String getDefaultText ( final String state, final Value value, final Object... data )
    {
        return getDefaultText ( state, false, value, data );
    }

    /**
     * Returns state text taken from value and formatted using specified data.
     *
     * @param state        component state
     * @param defaultState whether default text should be taken if no state text found or not
     * @param value        language value
     * @param data         formatting data
     * @return formatted state text
     */
    protected String getDefaultText ( final String state, final boolean defaultState, final Value value, final Object... data )
    {
        final String text = value.getText ( state, defaultState );
        return data != null && text != null ? formatDefaultText ( text, data ) : text;
    }

    /**
     * Returns text formatted using specified data.
     *
     * @param text text to format
     * @param data formatting data
     * @return formatted  text
     */
    private String formatDefaultText ( final String text, final Object[] data )
    {
        return String.format ( text, data );
    }
}