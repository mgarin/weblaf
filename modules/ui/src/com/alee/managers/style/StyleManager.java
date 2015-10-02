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

import com.alee.extended.painter.Painter;
import com.alee.managers.style.data.ComponentStyle;
import com.alee.managers.style.data.SkinInfo;
import com.alee.managers.style.skin.AbstractSkin;
import com.alee.managers.style.skin.web.WebSkin;
import com.alee.managers.style.skin.web.data.SeparatorLine;
import com.alee.managers.style.skin.web.data.SeparatorLineColor;
import com.alee.managers.style.skin.web.data.SeparatorLines;
import com.alee.utils.MapUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.XmlUtils;
import com.alee.utils.ninepatch.NinePatchIcon;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * This class manages WebLaF component styles.
 * It also manages available WebLaF skins and can install/change them in runtime.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.skin.AbstractSkin
 * @see com.alee.managers.style.data.SkinInfo
 */

public final class StyleManager
{
    /**
     * todo 1. Probably add "setCustomUIStyle" to override settings provided by skin
     */

    /**
     * Default painter ID.
     */
    public static final String DEFAULT_PAINTER_ID = "painter";

    /**
     * Static class aliases processing.
     * This allows StyleManager usage without its initialization.
     */
    static
    {
        // Class aliases
        XmlUtils.processAnnotations ( SkinInfo.class );
        XmlUtils.processAnnotations ( ComponentStyle.class );
        XmlUtils.processAnnotations ( NinePatchIcon.class );

        // Data aliases
        XmlUtils.processAnnotations ( SeparatorLines.class );
        XmlUtils.processAnnotations ( SeparatorLine.class );
        XmlUtils.processAnnotations ( SeparatorLineColor.class );
    }

    /**
     * Various component style related data.
     * <p/>
     * This data includes:
     * <p/>
     * 1. Skins applied for each specific skinnable component
     * Used to determine skinnable components, update them properly and detect their current skin.
     * <p/>
     * 2. Style IDs set for each specific component
     * They are all collected and stored in StyleManager to determine their changes correctly.
     * <p/>
     * 3. Style children each styled component has
     * Those children are generally collected here for convenient changes tracking.
     */
    private static final Map<JComponent, StyleData> styleData = new WeakHashMap<JComponent, StyleData> ();

    /**
     * Custom component painters.
     * These are the painters set from the code and they replace default painters provided by skin.
     * Map structure: JComponent -> Painter ID -> Painter
     * todo REMOVE?
     */
    private static final Map<JComponent, Map<String, Painter>> customPainters = new WeakHashMap<JComponent, Map<String, Painter>> ();

    /**
     * Default WebLaF skin.
     * Skin used by default when no other skins provided.
     * This skin can be set before WebLaF initialization to avoid unnecessary UI updates afterwards.
     */
    private static AbstractSkin defaultSkin = null;

    /**
     * Currently used skin.
     * This skin is applied to all newly created components styled by WebLaF except customized ones.
     */
    private static AbstractSkin currentSkin = null;

    /**
     * Whether strict style checks are enabled or not.
     * <p/>
     * In case strict checks are enabled any incorrect properties or painters getter and setter calls will cause exceptions.
     * These exceptions will not cause UI to halt but they will properly inform about missing styles, incorrect settings etc.
     * <p/>
     * It is highly recommended to keep this property enabled to see and fix all problems right away.
     */
    private static boolean strictStyleChecks = true;

    /**
     * Manager initialization mark.
     */
    private static boolean initialized = false;

    /**
     * Initializes StyleManager settings.
     */
    public static synchronized void initialize ()
    {
        if ( !initialized )
        {
            initialized = true;

            // Applying default skin as current skin
            applyDefaultSkin ();
        }
    }

    /**
     * Returns whether strict style checks are enabled or not.
     *
     * @return true if strict style checks are enabled, false otherwise
     */
    public static boolean isStrictStyleChecks ()
    {
        return strictStyleChecks;
    }

    /**
     * Sets whether strict style checks are enabled or not.
     *
     * @param strict whether strict style checks are enabled or not
     */
    public static void setStrictStyleChecks ( final boolean strict )
    {
        StyleManager.strictStyleChecks = strict;
    }

    /**
     * Performs skin support check and throws an exception if skin is not supported.
     *
     * @param skin skin to check
     */
    protected static void checkSupport ( final AbstractSkin skin )
    {
        if ( skin == null )
        {
            throw new StyleException ( "Skin is not provided or failed to load" );
        }
        if ( !skin.isSupported () )
        {
            throw new StyleException ( "Skin \"" + skin.getTitle () + "\" is not supported in this system" );
        }
    }

    /**
     * Returns default skin.
     *
     * @return default skin
     */
    public static AbstractSkin getDefaultSkin ()
    {
        if ( defaultSkin == null )
        {
            setDefaultSkin ( new WebSkin () );
        }
        return defaultSkin;
    }

    /**
     * Sets default skin.
     *
     * @param skinClassName default skin class name
     * @return previous default skin
     */
    public static AbstractSkin setDefaultSkin ( final String skinClassName )
    {
        return setDefaultSkin ( ReflectUtils.getClassSafely ( skinClassName ) );
    }

    /**
     * Sets default skin.
     *
     * @param skinClass default skin class
     * @return previous default skin
     */
    public static AbstractSkin setDefaultSkin ( final Class skinClass )
    {
        return setDefaultSkin ( createSkin ( skinClass ) );
    }

    /**
     * Returns newly created skin class instance.
     *
     * @param skinClass skin class
     * @return newly created skin class instance
     */
    public static AbstractSkin createSkin ( final Class skinClass )
    {
        try
        {
            return ( AbstractSkin ) ReflectUtils.createInstance ( skinClass );
        }
        catch ( final Throwable e )
        {
            throw new StyleException ( "Unable to initialize skin from its class", e );
        }
    }

    /**
     * Sets default skin.
     *
     * @param skin default skin
     * @return previous default skin
     */
    public static AbstractSkin setDefaultSkin ( final AbstractSkin skin )
    {
        // Checking skin support
        checkSupport ( skin );

        // Saving new default skin
        final AbstractSkin oldSkin = StyleManager.defaultSkin;
        StyleManager.defaultSkin = skin;
        return oldSkin;
    }

    /**
     * Applies default skin to all existing skinnable components.
     * This skin will also be applied to all skinnable components created afterwards.
     *
     * @return previously applied skin
     */
    public static AbstractSkin applyDefaultSkin ()
    {
        return installSkin ( getDefaultSkin () );
    }

    /**
     * Returns currently applied skin.
     *
     * @return currently applied skin
     */
    public static AbstractSkin getCurrentSkin ()
    {
        return currentSkin != null ? currentSkin : getDefaultSkin ();
    }

    /**
     * Applies specified skin to all existing skinnable components.
     * This skin will also be applied to all skinnable components created afterwards.
     *
     * @param skinClassName class name of the skin to be applied
     * @return previously applied skin
     */
    public static AbstractSkin installSkin ( final String skinClassName )
    {
        return installSkin ( ReflectUtils.getClassSafely ( skinClassName ) );
    }

    /**
     * Applies specified skin to all existing skinnable components.
     * This skin will also be applied to all skinnable components created afterwards.
     *
     * @param skinClass class of the skin to be applied
     * @return previously applied skin
     */
    public static AbstractSkin installSkin ( final Class skinClass )
    {
        return installSkin ( createSkin ( skinClass ) );
    }

    /**
     * Applies specified skin to all existing skinnable components.
     * This skin will also be applied to all skinnable components created afterwards.
     *
     * @param skin skin to be applied
     * @return previously applied skin
     */
    public static synchronized AbstractSkin installSkin ( final AbstractSkin skin )
    {
        // Checking skin support
        checkSupport ( skin );

        // Saving previously applied skin
        final AbstractSkin previousSkin = currentSkin;

        // Updating currently applied skin
        currentSkin = skin;

        // Applying new skin to all existing skinnable components
        final HashMap<JComponent, StyleData> skins = MapUtils.copyMap ( styleData );
        for ( final Map.Entry<JComponent, StyleData> entry : skins.entrySet () )
        {
            final JComponent component = entry.getKey ();
            final StyleData data = getData ( component );
            if ( data != null )
            {
                data.changeSkin ( component, skin );
            }
        }

        return previousSkin;
    }

    /**
     * Applies current skin to the skinnable component.
     *
     * @param component component to apply skin to
     * @return previously applied skin
     */
    public static AbstractSkin applySkin ( final JComponent component )
    {
        return applySkin ( component, getCurrentSkin () );
    }

    /**
     * Applies specified skin to the skinnable component.
     * You can provide different skin for any specific component here.
     * todo Do not replace custom applied skins when current skin changed?
     *
     * @param component component to apply skin to
     * @param skin      skin to be applied
     * @return previously applied skin
     */
    public static AbstractSkin applySkin ( final JComponent component, final AbstractSkin skin )
    {
        // Checking skin support
        checkSupport ( skin );

        // Replacing component skin
        final StyleData data = getData ( component );
        return data != null ? data.changeSkin ( component, skin ) : null;
    }

    /**
     * Removes skin applied to the specified component and returns it.
     *
     * @param component component to remove skin from
     * @return previously applied skin
     */
    public static AbstractSkin removeSkin ( final JComponent component )
    {
        // Removing component skin
        final StyleData data = getData ( component );
        return data != null ? data.removeSkin ( component ) : null;
    }

    public static StyleId getStyleId ( final JComponent component )
    {
        final StyleData data = getData ( component );
        return data != null && data.getStyleId () != null ? data.getStyleId () : StyleId.getDefault ( component );
    }

    public static void setStyleId ( final JComponent component, final StyleId id )
    {
        final StyleData data = getData ( component );
        if ( data != null )
        {
            final StyleId old = data.getStyleId ();
            final StyleId styleId = id != null ? id : StyleId.getDefault ( component );

            // Applying style ID
            data.setStyleId ( component, styleId );

            // Removing child reference from old parent style data
            if ( old != null )
            {
                final StyleData oldParentData = getData ( old.getParent () );
                if ( oldParentData != null )
                {
                    oldParentData.removeChild ( component );
                }
            }

            // Adding child reference into new parent style data
            final StyleData parentData = getData ( styleId.getParent () );
            if ( parentData != null )
            {
                parentData.addChild ( component );
            }
        }
    }

    private static StyleData getData ( final JComponent component )
    {
        if ( component != null )
        {
            StyleData data = styleData.get ( component );
            if ( data == null )
            {
                data = new StyleData ();
                styleData.put ( component, data );
            }
            return data;
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns painter for the specified component.
     *
     * @param component component to retrieve painter from
     * @param <T>       painter type
     * @return current component painter
     */
    public static <T extends Painter> T getPainter ( final JComponent component )
    {
        // todo Change ID to null?
        return getPainter ( component, DEFAULT_PAINTER_ID );
    }

    /**
     * Returns painter for the specified component and painter ID.
     *
     * @param component component to retrieve painter from
     * @param painterId painter ID
     * @param <T>       painter type
     * @return current component painter for the specified painter ID
     */
    public static <T extends Painter> T getPainter ( final JComponent component, final String painterId )
    {
        final Map<String, Painter> painters = customPainters.get ( component );
        if ( painters != null && painters.containsKey ( painterId ) )
        {
            // Return custom installed painter
            return ( T ) painters.get ( painterId );
        }
        else
        {
            // Return painter applied by skin
            return getCurrentSkin ().getPainter ( component, painterId );
        }
    }

    /**
     * Sets custom default painter for the specified component.
     * You should call this method when setting painter outside of the UI to avoid
     *
     * @param component component to set painter for
     * @param painter   painter
     * @param <T>       painter type
     * @return old custom painter
     */
    public static <T extends Painter> T setCustomPainter ( final JComponent component, final T painter )
    {
        // todo Change ID to null
        return setCustomPainter ( component, DEFAULT_PAINTER_ID, painter );
    }

    /**
     * Sets custom painter for the specified component.
     * You should call this method when setting painter outside of the UI to avoid
     *
     * @param component component to set painter for
     * @param id        painter ID
     * @param painter   painter
     * @param <T>       painter type
     * @return old custom painter
     */
    public static <T extends Painter> T setCustomPainter ( final JComponent component, final String id, final T painter )
    {
        // Saving custom painter
        Map<String, Painter> painters = customPainters.get ( component );
        if ( painters == null )
        {
            painters = new HashMap<String, Painter> ( 1 );
            customPainters.put ( component, painters );
        }
        final T oldValue = ( T ) painters.put ( id, painter );

        // Forcing component update
        applySkin ( component );

        return oldValue;
    }

    /**
     * Returns all custom painters for the specified component.
     *
     * @param component component to retrieve custom painters for
     * @return all custom painters for the specified component
     */
    public static Map<String, Painter> getCustomPainters ( final JComponent component )
    {
        return customPainters.get ( component );
    }

    /**
     * Removes all custom painters from the specified component.
     *
     * @param component component to remove custom painters from
     */
    public static void removeCustomPainters ( final JComponent component )
    {
        final Map<String, Painter> painters = customPainters.get ( component );
        if ( painters != null )
        {
            // Removing all custom painters
            painters.clear ();

            // Forcing component skin update
            applySkin ( component );
        }
    }
}