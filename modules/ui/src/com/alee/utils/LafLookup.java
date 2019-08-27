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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.laf.LookAndFeelException;
import com.alee.laf.WebUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Lookup created to replace proprietary {@code sun.swing.DefaultLookup} class.
 * This class does basically the same except not allowing to change the way lookup works.
 *
 * @author Scott Violet
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
    @Nullable
    public static InputMap getInputMap ( @NotNull final JComponent component, final int condition )
    {
        final InputMap inputMap;
        final ComponentUI ui = LafUtils.getUI ( component );
        if ( ui instanceof WebUI )
        {
            final WebUI webUI = ( WebUI ) ui;

            // Retrieving input map settings key
            final String key;
            if ( condition == JComponent.WHEN_IN_FOCUSED_WINDOW )
            {
                key = webUI.getPropertyPrefix () + "windowBindings";
            }
            else if ( condition == JComponent.WHEN_FOCUSED )
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

            // Retrieving input map
            if ( condition == JComponent.WHEN_IN_FOCUSED_WINDOW )
            {
                // Custom input map for window bindings
                final Object[] bindings = ( Object[] ) LafLookup.get ( component, ui, key );
                inputMap = bindings != null ? LookAndFeel.makeComponentInputMap ( component, bindings ) : null;
            }
            else
            {
                // Existing input map for key
                inputMap = ( InputMap ) LafLookup.get ( component, ui, key );
            }
        }
        else
        {
            inputMap = null;
        }
        return inputMap;
    }

    /**
     * Returns default {@code int} value for the specified {@code key}.
     *
     * @param component {@link JComponent} to return value for
     * @param ui        {@link ComponentUI} to return value for
     * @param key       value key
     * @return default {@code int} value for the specified {@code key}
     */
    public static int getInt ( @NotNull final JComponent component, @NotNull final ComponentUI ui,
                               @NotNull final String key )
    {
        return getInt ( component, ui, key, -1 );
    }

    /**
     * Returns default {@code int} value for the specified {@code key}.
     *
     * @param component    {@link JComponent} to return value for
     * @param ui           {@link ComponentUI} to return value for
     * @param key          value key
     * @param defaultValue value used as default if there is no value provided for the key
     * @return default {@code int} value for the specified {@code key}
     */
    public static int getInt ( @NotNull final JComponent component, @NotNull final ComponentUI ui,
                               @NotNull final String key, final int defaultValue )
    {
        final int result;
        final Object iValue = get ( component, ui, key );
        if ( iValue instanceof Number )
        {
            result = ( ( Number ) iValue ).intValue ();
        }
        else
        {
            result = defaultValue;
        }
        return result;
    }

    /**
     * Returns default {@link Insets} value for the specified {@code key}.
     *
     * @param component {@link JComponent} to return value for
     * @param ui        {@link ComponentUI} to return value for
     * @param key       value key
     * @return default {@link Insets} value for the specified {@code key}
     */
    @Nullable
    public static Insets getInsets ( @NotNull final JComponent component, @NotNull final ComponentUI ui,
                                     @NotNull final String key )
    {
        return getInsets ( component, ui, key, null );
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
    @Nullable
    public static Insets getInsets ( @NotNull final JComponent component, @NotNull final ComponentUI ui,
                                     @NotNull final String key, @Nullable final Insets defaultValue )
    {
        final Insets result;
        final Object iValue = get ( component, ui, key );
        if ( iValue instanceof Insets )
        {
            result = ( Insets ) iValue;
        }
        else
        {
            result = defaultValue;
        }
        return result;
    }

    /**
     * Returns default {@code boolean} value for the specified {@code key}.
     *
     * @param component {@link JComponent} to return value for
     * @param ui        {@link ComponentUI} to return value for
     * @param key       value key
     * @return default {@code boolean} value for the specified {@code key}
     */
    public static boolean getBoolean ( @NotNull final JComponent component, @NotNull final ComponentUI ui,
                                       @NotNull final String key )
    {
        return getBoolean ( component, ui, key, false );
    }

    /**
     * Returns default {@code boolean} value for the specified {@code key}.
     *
     * @param component    {@link JComponent} to return value for
     * @param ui           {@link ComponentUI} to return value for
     * @param key          value key
     * @param defaultValue value used as default if there is no value provided for the key
     * @return default {@code boolean} value for the specified {@code key}
     */
    public static boolean getBoolean ( @NotNull final JComponent component, @NotNull final ComponentUI ui,
                                       @NotNull final String key, final boolean defaultValue )
    {
        final boolean result;
        final Object iValue = get ( component, ui, key );
        if ( iValue instanceof Boolean )
        {
            result = ( Boolean ) iValue;
        }
        else
        {
            result = defaultValue;
        }
        return result;
    }

    /**
     * Returns default {@link Color} value for the specified {@code key}.
     *
     * @param component {@link JComponent} to return value for
     * @param ui        {@link ComponentUI} to return value for
     * @param key       value key
     * @return default {@link Color} value for the specified {@code key}
     */
    @Nullable
    public static Color getColor ( @NotNull final JComponent component, @NotNull final ComponentUI ui,
                                   @NotNull final String key )
    {
        return getColor ( component, ui, key, null );
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
    @Nullable
    public static Color getColor ( @NotNull final JComponent component, @NotNull final ComponentUI ui,
                                   @NotNull final String key, @Nullable final Color defaultValue )
    {
        final Color result;
        final Object iValue = get ( component, ui, key );
        if ( iValue instanceof Color )
        {
            result = ( Color ) iValue;
        }
        else
        {
            result = defaultValue;
        }
        return result;
    }

    /**
     * Returns default {@link Icon} value for the specified {@code key}.
     *
     * @param component {@link JComponent} to return value for
     * @param ui        {@link ComponentUI} to return value for
     * @param key       value key
     * @return default {@link Icon} value for the specified {@code key}
     */
    @Nullable
    public static Icon getIcon ( @NotNull final JComponent component, @NotNull final ComponentUI ui,
                                 @NotNull final String key )
    {
        return getIcon ( component, ui, key, null );
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
    @Nullable
    public static Icon getIcon ( @NotNull final JComponent component, @NotNull final ComponentUI ui,
                                 @NotNull final String key, @Nullable final Icon defaultValue )
    {
        final Icon result;
        final Object iValue = get ( component, ui, key );
        if ( iValue instanceof Icon )
        {
            result = ( Icon ) iValue;
        }
        else
        {
            result = defaultValue;
        }
        return result;
    }

    /**
     * Returns default {@link Border} value for the specified {@code key}.
     *
     * @param component {@link JComponent} to return value for
     * @param ui        {@link ComponentUI} to return value for
     * @param key       value key
     * @return default {@link Border} value for the specified {@code key}
     */
    @Nullable
    public static Border getBorder ( @NotNull final JComponent component, @NotNull final ComponentUI ui,
                                     @NotNull final String key )
    {
        return getBorder ( component, ui, key, null );
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
    @Nullable
    public static Border getBorder ( @NotNull final JComponent component, @NotNull final ComponentUI ui,
                                     @NotNull final String key, @Nullable final Border defaultValue )
    {
        final Border result;
        final Object iValue = get ( component, ui, key );
        if ( iValue instanceof Border )
        {
            result = ( Border ) iValue;
        }
        else
        {
            result = defaultValue;
        }
        return result;
    }

    /**
     * Returns default {@link Object} value for the specified {@code key}.
     *
     * @param component {@link JComponent} to return value for
     * @param ui        {@link ComponentUI} to return value for
     * @param key       value key
     * @return default {@link Object} value for the specified {@code key}
     */
    @Nullable
    public static Object get ( @NotNull final JComponent component, @NotNull final ComponentUI ui,
                               @NotNull final String key )
    {
        return UIManager.get ( key, component.getLocale () );
    }
}