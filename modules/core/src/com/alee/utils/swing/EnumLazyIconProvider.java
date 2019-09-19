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

package com.alee.utils.swing;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.utils.UtilityException;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Special class for simple and lazy enum icons retrieval.
 *
 * @author Mikle Garin
 */
public final class EnumLazyIconProvider
{
    /**
     * Cached enum icons map.
     */
    @NotNull
    private static final Map<Enum, Map<String, ImageIcon>> icons = new HashMap<Enum, Map<String, ImageIcon>> ();

    /**
     * Default icon files extension.
     */
    @NotNull
    private static final String DEFAULT_EXTENSION = ".png";

    /**
     * Returns cached or just loaded enum icon.
     *
     * @param enumeration enumeration constant for which icon should be loaded
     * @param folder      enumeration icons folder
     * @param <E>         enumeration type
     * @return cached or just loaded enum icon
     */
    @NotNull
    public static <E extends Enum<E>> ImageIcon getIcon ( @NotNull final E enumeration, @NotNull final String folder )
    {
        return getIcon ( enumeration, null, folder );
    }

    /**
     * Returns cached or just loaded enum icon for the specified state.
     * State string will be used to determine icon name automatically.
     *
     * @param enumeration enumeration constant for which icon should be loaded
     * @param state       enumeration icon state
     * @param folder      enumeration icons folder
     * @param <E>         enumeration type
     * @return cached or just loaded enum icon
     */
    @NotNull
    public static <E extends Enum<E>> ImageIcon getIcon ( @NotNull final E enumeration, @Nullable final String state,
                                                          @NotNull final String folder )
    {
        return getIcon ( enumeration, state, folder, DEFAULT_EXTENSION );
    }

    /**
     * Returns cached or just loaded enum icon for the specified state.
     * State string will be used to determine icon name automatically.
     *
     * @param enumeration enumeration constant for which icon should be loaded
     * @param state       enumeration icon state
     * @param folder      enumeration icons folder
     * @param extension   image format extension with dot
     * @param <E>         enumeration type
     * @return cached or just loaded enum icon
     */
    @NotNull
    public static <E extends Enum<E>> ImageIcon getIcon ( @NotNull final E enumeration, @Nullable final String state,
                                                          @NotNull final String folder, @NotNull final String extension )
    {
        Map<String, ImageIcon> stateIcons = icons.get ( enumeration );
        if ( stateIcons == null )
        {
            stateIcons = new HashMap<String, ImageIcon> ( 1 );
            icons.put ( enumeration, stateIcons );
        }
        ImageIcon imageIcon = stateIcons.get ( state );
        if ( imageIcon == null )
        {
            final String stateSuffix = state != null ? "-" + state : "";
            final String path = folder + enumeration + stateSuffix + extension;
            try
            {
                imageIcon = new ImageIcon ( enumeration.getClass ().getResource ( path ) );
                stateIcons.put ( state, imageIcon );
            }
            catch ( final Exception e )
            {
                final String msg = "Unable to find icon '%s' near class: %s";
                final String cn = enumeration.getClass ().getCanonicalName ();
                throw new UtilityException ( String.format ( msg, path, cn ), e );
            }
        }
        return imageIcon;
    }
}