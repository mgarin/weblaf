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

package com.alee.managers.style;

import com.alee.managers.style.data.ComponentStyle;
import com.alee.managers.style.data.SkinInfo;
import com.alee.managers.style.skin.WebLafSkin;
import com.alee.managers.style.skin.web.WebSkin;
import com.alee.utils.XmlUtils;

import javax.swing.*;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * This class manages WebLaF component styles.
 * It also manages available WebLaF skins and can install/change them in runtime.
 *
 * @author Mikle Garin
 * @see com.alee.managers.style.skin.WebLafSkin
 * @see com.alee.managers.style.data.SkinInfo
 */

public final class StyleManager
{
    /**
     * Skins applied for each specific skinnable component.
     * Used to determine skinnable components, update them properly and detect their current skin.
     */
    private static final Map<JComponent, WebLafSkin> appliedSkins = new WeakHashMap<JComponent, WebLafSkin> ( 50 );

    /**
     * Default WebLaF skin.
     */
    private static WebLafSkin defaultSkin;

    /**
     * Currently applied skin.
     */
    private static WebLafSkin currentSkin;

    /**
     * Manager initialization mark.
     */
    private static boolean initialized = false;

    /**
     * Initializes StyleManager settings.
     */
    public static void initialize ()
    {
        if ( !initialized )
        {
            initialized = true;

            // Class aliases
            XmlUtils.processAnnotations ( SkinInfo.class );
            XmlUtils.processAnnotations ( ComponentStyle.class );

            // Applying default skin as current skin
            defaultSkin = new WebSkin ();
            applySkin ( defaultSkin );
        }
    }

    /**
     * Returns default WebLaF skin.
     *
     * @return default WebLaF skin
     */
    public static WebLafSkin getDefaultSkin ()
    {
        return defaultSkin;
    }

    /**
     * Returns currently applied skin.
     *
     * @return currently applied skin
     */
    public static WebLafSkin getCurrentSkin ()
    {
        return currentSkin;
    }

    /**
     * Applies specified skin to all existing skinnable components.
     * This skin will also be applied to all skinnable components created afterwards.
     *
     * @param skin skin to be applied
     * @return applied skin
     */
    public static WebLafSkin applySkin ( final WebLafSkin skin )
    {
        // Updating currently applied skin
        currentSkin = skin;

        // Applying new skin to all existing skinnable components
        for ( final Map.Entry<JComponent, WebLafSkin> entry : appliedSkins.entrySet () )
        {
            final JComponent component = entry.getKey ();
            removeSkin ( component );
            applySkin ( component, skin );
        }

        return skin;
    }

    /**
     * Applies current skin to the skinnable component.
     *
     * @param component component to apply skin to
     * @return applied skin
     */
    public static WebLafSkin applySkin ( final JComponent component )
    {
        return applySkin ( component, currentSkin );
    }

    /**
     * Applies specified skin to the skinnable component.
     *
     * @param component component to apply skin to
     * @param skin      skin to be applied
     * @return applied skin
     */
    public static WebLafSkin applySkin ( final JComponent component, final WebLafSkin skin )
    {
        // Removing old skin from the component
        removeSkin ( component );

        // Applying new skin
        skin.apply ( component );
        appliedSkins.put ( component, skin );

        return skin;
    }

    /**
     * Removes skin applied to the specified component and returns it.
     *
     * @param component component to remove skin from
     * @return skin which was applied to the specified component or null
     */
    public static WebLafSkin removeSkin ( final JComponent component )
    {
        final WebLafSkin skin = appliedSkins.get ( component );
        if ( skin != null )
        {
            skin.remove ( component );
            appliedSkins.remove ( component );
        }
        return skin;
    }
}