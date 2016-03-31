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

import com.alee.extended.checkbox.MixedIcon;
import com.alee.extended.statusbar.WebMemoryBarBackground;
import com.alee.laf.checkbox.CheckIcon;
import com.alee.laf.radiobutton.RadioIcon;
import com.alee.painter.decoration.shape.ArrowShape;
import com.alee.managers.style.data.ComponentStyle;
import com.alee.managers.style.data.SkinInfo;
import com.alee.painter.decoration.shape.EllipseShape;
import com.alee.skin.web.WebSkin;
import com.alee.painter.Painter;
import com.alee.painter.common.TextureType;
import com.alee.painter.decoration.AbstractDecoration;
import com.alee.painter.decoration.WebDecoration;
import com.alee.painter.decoration.background.*;
import com.alee.painter.decoration.border.AbstractBorder;
import com.alee.painter.decoration.border.LineBorder;
import com.alee.laf.separator.SeparatorLine;
import com.alee.laf.separator.SeparatorLines;
import com.alee.painter.decoration.shadow.AbstractShadow;
import com.alee.painter.decoration.shadow.ExpandingShadow;
import com.alee.painter.decoration.shadow.WebShadow;
import com.alee.painter.decoration.shape.WebShape;
import com.alee.utils.CompareUtils;
import com.alee.utils.MapUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.XmlUtils;
import com.alee.utils.ninepatch.NinePatchIcon;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * This class manages WebLaF component styles.
 * It also manages available WebLaF skins and can install/change them in runtime.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see Skin
 * @see Skinnable
 * @see com.alee.managers.style.data.SkinInfo
 * @see com.alee.managers.style.StyleId
 * @see com.alee.managers.style.StyleData
 */

public final class StyleManager
{
    /**
     * Various component style related data.
     * <p>
     * This data includes:
     * <p>
     * 1. Skins applied for each specific skinnable component
     * Used to determine skinnable components, update them properly and detect their current skin.
     * <p>
     * 2. Style IDs set for each specific component
     * They are all collected and stored in StyleManager to determine their changes correctly.
     * <p>
     * 3. Style children each styled component has
     * Those children are generally collected here for convenient changes tracking.
     */
    private static final Map<JComponent, StyleData> styleData = new WeakHashMap<JComponent, StyleData> ();

    /**
     * Default WebLaF skin class.
     * Class of the skin used by default when no other skins provided.
     * This skin can be set before WebLaF initialization to avoid unnecessary UI updates afterwards.
     * <p>
     * Every skin set as default must have an empty constructor that properly initializes that skin.
     * Otherwise you have to set that skin manually through one of the methods in this manager.
     */
    private static Class<? extends Skin> defaultSkinClass = null;

    /**
     * Currently used skin.
     * This skin is applied to all newly created components styled by WebLaF except customized ones.
     */
    private static Skin currentSkin = null;

    /**
     * Whether strict style checks are enabled or not.
     * <p>
     * In case strict checks are enabled any incorrect properties or painters getter and setter calls will cause exceptions.
     * These exceptions will not cause UI to halt but they will properly inform about missing styles, incorrect settings etc.
     * <p>
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

            // Class aliases
            XmlUtils.processAnnotations ( SkinInfo.class );
            XmlUtils.processAnnotations ( ComponentStyle.class );
            XmlUtils.processAnnotations ( NinePatchIcon.class );
            XmlUtils.processAnnotations ( AbstractDecoration.class );
            XmlUtils.processAnnotations ( WebDecoration.class );
            XmlUtils.processAnnotations ( AbstractShadow.class );
            XmlUtils.processAnnotations ( WebShape.class );
            XmlUtils.processAnnotations ( EllipseShape.class );
            XmlUtils.processAnnotations ( ArrowShape.class );
            XmlUtils.processAnnotations ( WebShadow.class );
            XmlUtils.processAnnotations ( ExpandingShadow.class );
            XmlUtils.processAnnotations ( AbstractBorder.class );
            XmlUtils.processAnnotations ( LineBorder.class );
            XmlUtils.processAnnotations ( AbstractBackground.class );
            XmlUtils.processAnnotations ( ColorBackground.class );
            XmlUtils.processAnnotations ( GradientBackground.class );
            XmlUtils.processAnnotations ( PresetTextureBackground.class );
            XmlUtils.processAnnotations ( AlphaLayerBackground.class );
            XmlUtils.processAnnotations ( TextureType.class );
            XmlUtils.processAnnotations ( GradientType.class );
            XmlUtils.processAnnotations ( GradientColor.class );
            XmlUtils.processAnnotations ( SeparatorLines.class );
            XmlUtils.processAnnotations ( SeparatorLine.class );
            XmlUtils.processAnnotations ( CheckIcon.class );
            XmlUtils.processAnnotations ( RadioIcon.class );
            XmlUtils.processAnnotations ( MixedIcon.class );
            XmlUtils.processAnnotations ( WebMemoryBarBackground.class );

            // Applying default skin as current skin
            setSkin ( getDefaultSkin () );
        }
    }

    /**
     * Throws runtime exception if manager was not initialized yet.
     */
    private static void checkInitialization ()
    {
        if ( !initialized )
        {
            throw new StyleException ( "StyleManager must be initialized" );
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
     * Returns default skin.
     *
     * @return default skin
     */
    public static Class<? extends Skin> getDefaultSkin ()
    {
        return defaultSkinClass != null ? defaultSkinClass : WebSkin.class;
    }

    /**
     * Sets default skin.
     *
     * @param skinClassName default skin class name
     * @return previous default skin
     */
    public static Class<? extends Skin> setDefaultSkin ( final String skinClassName )
    {
        return setDefaultSkin ( ReflectUtils.getClassSafely ( skinClassName ) );
    }

    /**
     * Sets default skin.
     *
     * @param skinClass default skin class
     * @return previous default skin
     */
    public static Class<? extends Skin> setDefaultSkin ( final Class<? extends Skin> skinClass )
    {
        final Class<? extends Skin> oldSkin = StyleManager.defaultSkinClass;
        StyleManager.defaultSkinClass = skinClass;
        return oldSkin;
    }

    /**
     * Returns currently applied skin.
     *
     * @return currently applied skin
     */
    public static Skin getSkin ()
    {
        return currentSkin;
    }

    /**
     * Applies specified skin to all existing skinnable components.
     * This skin will also be applied to all skinnable components created afterwards.
     *
     * @param skinClassName class name of the skin to be applied
     * @return previously applied skin
     */
    public static Skin setSkin ( final String skinClassName )
    {
        return setSkin ( ReflectUtils.getClassSafely ( skinClassName ) );
    }

    /**
     * Applies specified skin to all existing skinnable components.
     * This skin will also be applied to all skinnable components created afterwards.
     *
     * @param skinClass class of the skin to be applied
     * @return previously applied skin
     */
    public static Skin setSkin ( final Class skinClass )
    {
        return setSkin ( createSkin ( skinClass ) );
    }

    /**
     * Applies specified skin to all existing skinnable components.
     * This skin will also be applied to all skinnable components created afterwards.
     *
     * @param skin skin to be applied
     * @return previously applied skin
     */
    public static synchronized Skin setSkin ( final Skin skin )
    {
        // Checking manager initialization
        checkInitialization ();

        // Checking skin support
        checkSupport ( skin );

        // Saving previously applied skin
        final Skin previousSkin = currentSkin;

        // Updating currently applied skin
        currentSkin = skin;

        // Applying new skin to all existing skinnable components
        final HashMap<JComponent, StyleData> skins = MapUtils.copyMap ( styleData );
        for ( final Map.Entry<JComponent, StyleData> entry : skins.entrySet () )
        {
            final JComponent component = entry.getKey ();
            final StyleData data = getData ( component );
            if ( !data.isPinnedSkin () && data.getSkin () == previousSkin )
            {
                data.applySkin ( skin, false );
            }
        }

        return previousSkin;
    }

    /**
     * Returns component style ID.
     *
     * @param component component to retrieve style ID for
     * @return component style ID
     */
    public static StyleId getStyleId ( final JComponent component )
    {
        final StyleData data = getData ( component );
        return data.getStyleId () != null ? data.getStyleId () : StyleId.getDefault ( component );
    }

    /**
     * Sets new component style ID.
     *
     * @param component component to set style ID for
     * @param id        new style ID
     * @return previously used style ID
     */
    public static StyleId setStyleId ( final JComponent component, final StyleId id )
    {
        final StyleData data = getData ( component );
        final StyleId old = data.getStyleId ();
        final StyleId styleId = id != null ? id : StyleId.getDefault ( component );

        // Perform operation if IDs are actually different
        if ( !CompareUtils.equals ( old, styleId ) )
        {
            // Applying style ID
            data.setStyleId ( styleId );

            // Removing child reference from old parent style data
            if ( old != null )
            {
                final JComponent oldParent = old.getParent ();
                if ( oldParent != null )
                {
                    getData ( oldParent ).removeChild ( component );
                }
            }

            // Adding child reference into new parent style data
            final JComponent parent = styleId.getParent ();
            if ( parent != null )
            {
                getData ( parent ).addChild ( component );
            }
        }

        return old;
    }

    /**
     * Restores component default style ID.
     *
     * @param component component to restore style ID for
     * @return previously used style ID
     */
    public static StyleId restoreStyleId ( final JComponent component )
    {
        final StyleId defaultStyleId = StyleableComponent.get ( component ).getDefaultStyleId ();
        return setStyleId ( component, defaultStyleId );
    }

    /**
     * Applies current skin to the skinnable component.
     * <p>
     * This method is used only to setup style data into UI on install.
     * It is not recommended to use it outside of that install behavior.
     *
     * @param component component to apply skin to
     * @return previously applied skin
     */
    public static Skin installSkin ( final JComponent component )
    {
        return getData ( component ).applySkin ( getSkin (), false );
    }

    /**
     * Updates current skin in the skinnable component.
     * <p>
     * This method is used only to properly update skin on various changes.
     * It is not recommended to use it outside of style manager behavior.
     *
     * @param component component to update skin for
     */
    public static void updateSkin ( final JComponent component )
    {
        getData ( component ).updateSkin ();
    }

    /**
     * Removes skin applied to the specified component and returns it.
     * <p>
     * This method is used only to cleanup style data from UI on uninstall.
     * It is not recommended to use it outside of that uninstall behavior.
     *
     * @param component component to remove skin from
     * @return previously applied skin
     */
    public static Skin uninstallSkin ( final JComponent component )
    {
        return getData ( component ).removeSkin ();
    }

    /**
     * Returns skin currently applied to the specified component.
     *
     * @param component component to retrieve applied skin from
     * @return skin currently applied to the specified component
     */
    public static Skin getSkin ( final JComponent component )
    {
        return getData ( component ).getSkin ();
    }

    /**
     * Applies specified skin to the skinnable component and all of its children linked via {@link com.alee.managers.style.StyleId}.
     * Actual linked children information is stored within {@link com.alee.managers.style.StyleData} data objects.
     * Custom skin provided using this method will not be replaced if application skin changes.
     *
     * @param component component to apply skin to
     * @param skin      skin to be applied
     * @return previously applied skin
     */
    public static Skin setSkin ( final JComponent component, final Skin skin )
    {
        return setSkin ( component, skin, false );
    }

    /**
     * Applies specified skin to the skinnable component and all of its children linked via {@link com.alee.managers.style.StyleId}.
     * Actual linked children information is stored within {@link com.alee.managers.style.StyleData} data objects.
     * Custom skin provided using this method will not be replaced if application skin changes.
     *
     * @param component   component to apply skin to
     * @param skin        skin to be applied
     * @param recursively whether or not should apply skin to child components
     * @return previously applied skin
     */
    public static Skin setSkin ( final JComponent component, final Skin skin, final boolean recursively )
    {
        // Replacing component skin
        // Asking not to update linked style children in case we are going recursively here
        // This is made to avoid double style update occuring there
        // todo This might skip style child which is not a direct child in components tree
        final StyleData data = getData ( component );
        final Skin previousSkin = data.applySkin ( skin, !recursively );

        // Pinning applied skin
        // This will keep this skin even if global skin is changed
        data.setPinnedSkin ( true );

        // Applying new skin to all existing skinnable components
        if ( recursively )
        {
            for ( int i = 0; i < component.getComponentCount (); i++ )
            {
                final Component child = component.getComponent ( i );
                if ( child instanceof JComponent )
                {
                    setSkin ( ( JComponent ) child, skin, recursively );
                }
            }
        }

        return previousSkin;
    }

    /**
     * Restores global skin for the skinnable component and all of its children linked via {@link com.alee.managers.style.StyleId}.
     * Actual linked children information is stored within {@link com.alee.managers.style.StyleData} data objects.
     * Restoring component skin will also include it back into the skin update cycle in case global skin will be changed.
     *
     * @param component component to restore global skin for
     * @return skin applied to the specified component after restoration
     */
    public static Skin restoreSkin ( final JComponent component )
    {
        // Retrieving currently applied skin
        final StyleData data = getData ( component );
        final Skin skin = data.getSkin ();

        // Restoring skin to globally set one if it is actually different
        final Skin globalSkin = getSkin ();
        if ( globalSkin == skin )
        {
            data.applySkin ( globalSkin, true );
            return globalSkin;
        }
        else
        {
            return skin;
        }
    }

    /**
     * Adds skin change listener.
     *
     * @param component component to listen skin changes on
     * @param listener  skin change listener to add
     */
    public static void addStyleListener ( final JComponent component, final StyleListener listener )
    {
        getData ( component ).addStyleListener ( listener );
    }

    /**
     * Removes skin change listener.
     *
     * @param component component to listen skin changes on
     * @param listener  skin change listener to remove
     */
    public static void removeStyleListener ( final JComponent component, final StyleListener listener )
    {
        getData ( component ).removeStyleListener ( listener );
    }

    /**
     * Returns all custom painters for the specified component.
     *
     * @param component component to retrieve custom painters for
     * @return all custom painters for the specified component
     */
    public static Map<String, Painter> getCustomPainters ( final JComponent component )
    {
        return getData ( component ).getPainters ();
    }

    /**
     * Returns custom base painter for the specified component.
     *
     * @param component component to retrieve custom base painter for
     * @return custom base painter for the specified component
     */
    public static Painter getCustomPainter ( final JComponent component )
    {
        return getCustomPainter ( component, ComponentStyle.BASE_PAINTER_ID );
    }

    /**
     * Returns custom painter for the specified component.
     *
     * @param component component to retrieve custom painter for
     * @param id        painter ID
     * @return custom painter for the specified component
     */
    public static Painter getCustomPainter ( final JComponent component, final String id )
    {
        final Map<String, Painter> customPainters = getCustomPainters ( component );
        return customPainters != null ? customPainters.get ( id ) : null;
    }

    /**
     * Sets custom base painter for the specified component.
     * You should call this method when setting painter outside of the UI.
     *
     * @param component component to set painter for
     * @param painter   painter
     * @param <T>       painter type
     * @return old custom painter
     */
    public static <T extends Painter> T setCustomPainter ( final JComponent component, final T painter )
    {
        return setCustomPainter ( component, ComponentStyle.BASE_PAINTER_ID, painter );
    }

    /**
     * Sets custom painter for the specified component under the specified painter ID.
     * You should call this method when setting painter outside of the UI.
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
        final StyleData data = getData ( component );
        Map<String, Painter> painters = data.getPainters ();
        if ( painters == null )
        {
            painters = new HashMap<String, Painter> ( 1 );
            data.setPainters ( painters );
        }
        final T oldValue = ( T ) painters.put ( id, painter );

        // Forcing component update
        // todo Some methods in skin instead of full reinstall?
        // todo This also incorrectly resets custom skin!
        installSkin ( component );

        return oldValue;
    }

    /**
     * Restores default painters for the specified component.
     *
     * @param component component to restore default painters for
     * @return true if default painters were restored, false otherwise
     */
    public static boolean restoreDefaultPainters ( final JComponent component )
    {
        final Map<String, Painter> painters = getData ( component ).getPainters ();
        if ( painters != null && painters.size () > 0 )
        {
            // Removing all custom painters
            painters.clear ();

            // Forcing component skin update
            // todo Some methods in skin instead of full reinstall?
            // todo This also incorrectly resets custom skin!
            installSkin ( component );

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns component style data.
     *
     * @param component component to retrieve style data for
     * @return component style data
     */
    private static StyleData getData ( final JComponent component )
    {
        // Checking manager initialization
        checkInitialization ();

        // Checking component support
        checkSupport ( component );

        // Retrieving component style data
        StyleData data = styleData.get ( component );
        if ( data == null )
        {
            data = new StyleData ( component );
            styleData.put ( component, data );
        }
        return data;
    }

    /**
     * Performs component styling support check and throws an exception if it is not supported.
     *
     * @param component component to check
     * @throws com.alee.managers.style.StyleException in case component is not specified or not supported
     */
    private static void checkSupport ( final JComponent component )
    {
        if ( component == null )
        {
            throw new StyleException ( "Component is not specified" );
        }
        if ( !StyleableComponent.isSupported ( component ) )
        {
            throw new StyleException ( "Component \"" + component + "\" is not supported" );
        }
    }

    /**
     * Performs skin support check and throws an exception if skin is not supported.
     *
     * @param skin skin to check
     * @throws com.alee.managers.style.StyleException in case skin is not specified or not supported
     */
    private static void checkSupport ( final Skin skin )
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
     * Returns newly created skin class instance.
     *
     * @param skinClass skin class
     * @return newly created skin class instance
     */
    private static Skin createSkin ( final Class skinClass )
    {
        try
        {
            return ( Skin ) ReflectUtils.createInstance ( skinClass );
        }
        catch ( final Throwable e )
        {
            throw new StyleException ( "Unable to initialize skin from its class", e );
        }
    }
}