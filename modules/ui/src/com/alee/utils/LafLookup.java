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

package com.alee.utils;

import com.alee.laf.LookAndFeelException;
import com.alee.laf.WebUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Lookup created to replace proprietary {@link sun.swing.DefaultLookup} class.
 * This class does basically the same except not allowing to change the way lookup works.
 *
 * @author Mikle Garin
 */
public final class LafLookup
{
    /**
     * Private constructor to avoid instantiation.
     */
    private LafLookup ()
    {
        throw new UtilityException ( "Utility classes are not meant to be instantiated" );
    }

    /**
     * Returns {@link InputMap} for {@code condition} from the specified {@link JComponent}.
     *
     * @param component {@link JComponent}
     * @param condition event condition
     * @return {@link InputMap} for {@code condition} from the specified {@link JComponent}
     */
    public static InputMap getInputMap ( final JComponent component, final int condition )
    {
        final ComponentUI ui = LafUtils.getUI ( component );
        if ( ui != null && ui instanceof WebUI )
        {
            final WebUI webUI = ( WebUI ) ui;
            final String key;
            if ( condition == JComponent.WHEN_FOCUSED )
            {
                key = webUI.getPropertyPrefix () + "focusInputMap";
            }
            else if ( condition == JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT )
            {
                key = webUI.getPropertyPrefix () + "ancestorInputMap";
            }
            else
            {
                throw new LookAndFeelException ( "Unsupported InputMap condition: " + condition );
            }
            return ( InputMap ) LafLookup.get ( component, ui, key );
        }
        return null;
    }

    /**
     * Returns default {@link int} value for the specified {@code key}.
     *
     * @param component    {@link JComponent} to return value for
     * @param ui           {@link ComponentUI} to return value for
     * @param key          value key
     * @param defaultValue value used as default if there is no value provided for the key
     * @return default {@link int} value for the specified {@code key}
     */
    public static int getInt ( final JComponent component, final ComponentUI ui, final String key, final int defaultValue )
    {
        final Object iValue = get ( component, ui, key );

        if ( iValue == null || !( iValue instanceof Number ) )
        {
            return defaultValue;
        }
        return ( ( Number ) iValue ).intValue ();
    }

    /**
     * Returns default {@link int} value for the specified {@code key}.
     *
     * @param component {@link JComponent} to return value for
     * @param ui        {@link ComponentUI} to return value for
     * @param key       value key
     * @return default {@link int} value for the specified {@code key}
     */
    public static int getInt ( final JComponent component, final ComponentUI ui, final String key )
    {
        return getInt ( component, ui, key, -1 );
    }

    /**
     * Returns default {@link Insets} value for the specified {@code key}.
     *
     * @param component    {@link JComponent} to return value for
     * @param ui           {@link ComponentUI} to return value for
     * @param key          value key
     * @param defaultValue value used as default if there is no value provided for the key
     * @return default {@link Insets} value for the specified {@code key}
     */
    public static Insets getInsets ( final JComponent component, final ComponentUI ui, final String key, final Insets defaultValue )
    {
        final Object iValue = get ( component, ui, key );

        if ( iValue == null || !( iValue instanceof Insets ) )
        {
            return defaultValue;
        }
        return ( Insets ) iValue;
    }

    /**
     * Returns default {@link Insets} value for the specified {@code key}.
     *
     * @param component {@link JComponent} to return value for
     * @param ui        {@link ComponentUI} to return value for
     * @param key       value key
     * @return default {@link Insets} value for the specified {@code key}
     */
    public static Insets getInsets ( final JComponent component, final ComponentUI ui, final String key )
    {
        return getInsets ( component, ui, key, null );
    }

    /**
     * Returns default {@link boolean} value for the specified {@code key}.
     *
     * @param component    {@link JComponent} to return value for
     * @param ui           {@link ComponentUI} to return value for
     * @param key          value key
     * @param defaultValue value used as default if there is no value provided for the key
     * @return default {@link boolean} value for the specified {@code key}
     */
    public static boolean getBoolean ( final JComponent component, final ComponentUI ui, final String key, final boolean defaultValue )
    {
        final Object iValue = get ( component, ui, key );
        if ( iValue == null || !( iValue instanceof Boolean ) )
        {
            return defaultValue;
        }
        return ( Boolean ) iValue;
    }

    /**
     * Returns default {@link boolean} value for the specified {@code key}.
     *
     * @param component {@link JComponent} to return value for
     * @param ui        {@link ComponentUI} to return value for
     * @param key       value key
     * @return default {@link boolean} value for the specified {@code key}
     */
    public static boolean getBoolean ( final JComponent component, final ComponentUI ui, final String key )
    {
        return getBoolean ( component, ui, key, false );
    }

    /**
     * Returns default {@link Color} value for the specified {@code key}.
     *
     * @param component    {@link JComponent} to return value for
     * @param ui           {@link ComponentUI} to return value for
     * @param key          value key
     * @param defaultValue value used as default if there is no value provided for the key
     * @return default {@link Color} value for the specified {@code key}
     */
    public static Color getColor ( final JComponent component, final ComponentUI ui, final String key,
                                   final Color defaultValue )
    {
        final Object iValue = get ( component, ui, key );

        if ( iValue == null || !( iValue instanceof Color ) )
        {
            return defaultValue;
        }
        return ( Color ) iValue;
    }

    /**
     * Returns default {@link Color} value for the specified {@code key}.
     *
     * @param component {@link JComponent} to return value for
     * @param ui        {@link ComponentUI} to return value for
     * @param key       value key
     * @return default {@link Color} value for the specified {@code key}
     */
    public static Color getColor ( final JComponent component, final ComponentUI ui, final String key )
    {
        return getColor ( component, ui, key, null );
    }

    /**
     * Returns default {@link Icon} value for the specified {@code key}.
     *
     * @param component    {@link JComponent} to return value for
     * @param ui           {@link ComponentUI} to return value for
     * @param key          value key
     * @param defaultValue value used as default if there is no value provided for the key
     * @return default {@link Icon} value for the specified {@code key}
     */
    public static Icon getIcon ( final JComponent component, final ComponentUI ui, final String key, final Icon defaultValue )
    {
        final Object iValue = get ( component, ui, key );
        if ( iValue == null || !( iValue instanceof Icon ) )
        {
            return defaultValue;
        }
        return ( Icon ) iValue;
    }

    /**
     * Returns default {@link Icon} value for the specified {@code key}.
     *
     * @param component {@link JComponent} to return value for
     * @param ui        {@link ComponentUI} to return value for
     * @param key       value key
     * @return default {@link Icon} value for the specified {@code key}
     */
    public static Icon getIcon ( final JComponent component, final ComponentUI ui, final String key )
    {
        return getIcon ( component, ui, key, null );
    }

    /**
     * Returns default {@link Border} value for the specified {@code key}.
     *
     * @param component    {@link JComponent} to return value for
     * @param ui           {@link ComponentUI} to return value for
     * @param key          value key
     * @param defaultValue value used as default if there is no value provided for the key
     * @return default {@link Border} value for the specified {@code key}
     */
    public static Border getBorder ( final JComponent component, final ComponentUI ui, final String key, final Border defaultValue )
    {
        final Object iValue = get ( component, ui, key );
        if ( iValue == null || !( iValue instanceof Border ) )
        {
            return defaultValue;
        }
        return ( Border ) iValue;
    }

    /**
     * Returns default {@link Border} value for the specified {@code key}.
     *
     * @param component {@link JComponent} to return value for
     * @param ui        {@link ComponentUI} to return value for
     * @param key       value key
     * @return default {@link Border} value for the specified {@code key}
     */
    public static Border getBorder ( final JComponent component, final ComponentUI ui, final String key )
    {
        return getBorder ( component, ui, key, null );
    }

    /**
     * Returns default {@link Object} value for the specified {@code key}.
     *
     * @param component {@link JComponent} to return value for
     * @param ui        {@link ComponentUI} to return value for
     * @param key       value key
     * @return default {@link Object} value for the specified {@code key}
     */
    public static Object get ( final JComponent component, final ComponentUI ui, final String key )
    {
        return UIManager.get ( key, component.getLocale () );
    }
}