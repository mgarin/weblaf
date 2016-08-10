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

package com.alee.laf.menu;

import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.LanguageMethods;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.managers.log.Log;
import com.alee.managers.settings.DefaultValue;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.style.*;
import com.alee.painter.Paintable;
import com.alee.painter.Painter;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.extensions.FontMethods;
import com.alee.utils.swing.extensions.FontMethodsImpl;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * {@link JCheckBoxMenuItem} extension class.
 * It contains various useful methods to simplify core component usage.
 * <p/>
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application L&amp;F as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see JCheckBoxMenuItem
 * @see WebCheckBoxMenuItemUI
 * @see CheckBoxMenuItemPainter
 */

public class WebCheckBoxMenuItem extends JCheckBoxMenuItem
        implements Styleable, Paintable, ShapeMethods, MarginMethods, PaddingMethods, LanguageMethods, SettingsMethods,
        FontMethods<WebCheckBoxMenuItem>
{
    /**
     * Constructs new checkbox menu item.
     */
    public WebCheckBoxMenuItem ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs new checkbox menu item using the specified settings.
     *
     * @param action menu item action
     */
    public WebCheckBoxMenuItem ( final Action action )
    {
        this ( StyleId.auto, action );
    }

    /**
     * Constructs new checkbox menu item using the specified settings.
     *
     * @param icon menu item icon
     */
    public WebCheckBoxMenuItem ( final Icon icon )
    {
        this ( StyleId.auto, icon );
    }

    /**
     * Constructs new checkbox menu item using the specified settings.
     *
     * @param text menu item text
     */
    public WebCheckBoxMenuItem ( final String text )
    {
        this ( StyleId.auto, text );
    }

    /**
     * Constructs new checkbox menu item using the specified settings.
     *
     * @param text        menu item text
     * @param accelerator menu item accelerator
     */
    public WebCheckBoxMenuItem ( final String text, final KeyStroke accelerator )
    {
        this ( StyleId.auto, text, accelerator );
    }

    /**
     * Constructs new checkbox menu item using the specified settings.
     *
     * @param text        menu item text
     * @param accelerator menu item accelerator
     */
    public WebCheckBoxMenuItem ( final String text, final HotkeyData accelerator )
    {
        this ( StyleId.auto, text, accelerator );
    }

    /**
     * Constructs new checkbox menu item using the specified settings.
     *
     * @param text     menu item text
     * @param selected whether this checkbox item is selected or not
     */
    public WebCheckBoxMenuItem ( final String text, final boolean selected )
    {
        this ( StyleId.auto, text, selected );
    }

    /**
     * Constructs new checkbox menu item using the specified settings.
     *
     * @param text menu item text
     * @param icon menu item icon
     */
    public WebCheckBoxMenuItem ( final String text, final Icon icon )
    {
        this ( StyleId.auto, text, icon );
    }

    /**
     * Constructs new checkbox menu item using the specified settings.
     *
     * @param text     menu item text
     * @param icon     menu item icon
     * @param selected whether this checkbox item is selected or not
     */
    public WebCheckBoxMenuItem ( final String text, final Icon icon, final boolean selected )
    {
        this ( StyleId.auto, text, icon, selected );
    }

    /**
     * Constructs new checkbox menu item using the specified settings.
     *
     * @param text        menu item text
     * @param icon        menu item icon
     * @param accelerator menu item accelerator
     */
    public WebCheckBoxMenuItem ( final String text, final Icon icon, final KeyStroke accelerator )
    {
        this ( StyleId.auto, text, icon, accelerator );
    }

    /**
     * Constructs new checkbox menu item using the specified settings.
     *
     * @param text        menu item text
     * @param icon        menu item icon
     * @param selected    whether this checkbox item is selected or not
     * @param accelerator menu item accelerator
     */
    public WebCheckBoxMenuItem ( final String text, final Icon icon, final boolean selected, final KeyStroke accelerator )
    {
        this ( StyleId.auto, text, icon, selected, accelerator );
    }

    /**
     * Constructs new checkbox menu item using the specified settings.
     *
     * @param text        menu item text
     * @param icon        menu item icon
     * @param accelerator menu item accelerator
     */
    public WebCheckBoxMenuItem ( final String text, final Icon icon, final HotkeyData accelerator )
    {
        this ( StyleId.auto, text, icon, accelerator );
    }

    /**
     * Constructs new checkbox menu item using the specified settings.
     *
     * @param text        menu item text
     * @param icon        menu item icon
     * @param selected    whether this checkbox item is selected or not
     * @param accelerator menu item accelerator
     */
    public WebCheckBoxMenuItem ( final String text, final Icon icon, final boolean selected, final HotkeyData accelerator )
    {
        this ( StyleId.auto, text, icon, selected, accelerator );
    }

    /**
     * Constructs new checkbox menu item using the specified settings.
     *
     * @param id     style ID
     * @param action menu item action
     */
    public WebCheckBoxMenuItem ( final StyleId id, final Action action )
    {
        this ( id, null, null, false, ( KeyStroke ) null );
        setAction ( action );
    }

    /**
     * Constructs new checkbox menu item.
     *
     * @param id style ID
     */
    public WebCheckBoxMenuItem ( final StyleId id )
    {
        this ( id, null, null, false, ( KeyStroke ) null );
    }

    /**
     * Constructs new checkbox menu item using the specified settings.
     *
     * @param id   style ID
     * @param icon menu item icon
     */
    public WebCheckBoxMenuItem ( final StyleId id, final Icon icon )
    {
        this ( id, null, icon, false, ( KeyStroke ) null );
    }

    /**
     * Constructs new checkbox menu item using the specified settings.
     *
     * @param id   style ID
     * @param text menu item text
     */
    public WebCheckBoxMenuItem ( final StyleId id, final String text )
    {
        this ( id, text, null, false, ( KeyStroke ) null );
    }

    /**
     * Constructs new checkbox menu item using the specified settings.
     *
     * @param id          style ID
     * @param text        menu item text
     * @param accelerator menu item accelerator
     */
    public WebCheckBoxMenuItem ( final StyleId id, final String text, final KeyStroke accelerator )
    {
        this ( id, text, null, false, accelerator );
    }

    /**
     * Constructs new checkbox menu item using the specified settings.
     *
     * @param id          style ID
     * @param text        menu item text
     * @param accelerator menu item accelerator
     */
    public WebCheckBoxMenuItem ( final StyleId id, final String text, final HotkeyData accelerator )
    {
        this ( id, text, null, false, accelerator );
    }

    /**
     * Constructs new checkbox menu item using the specified settings.
     *
     * @param id       style ID
     * @param text     menu item text
     * @param selected whether this checkbox item is selected or not
     */
    public WebCheckBoxMenuItem ( final StyleId id, final String text, final boolean selected )
    {
        this ( id, text, null, selected, ( KeyStroke ) null );
    }

    /**
     * Constructs new checkbox menu item using the specified settings.
     *
     * @param id   style ID
     * @param text menu item text
     * @param icon menu item icon
     */
    public WebCheckBoxMenuItem ( final StyleId id, final String text, final Icon icon )
    {
        this ( id, text, icon, false, ( KeyStroke ) null );
    }

    /**
     * Constructs new checkbox menu item using the specified settings.
     *
     * @param id       style ID
     * @param text     menu item text
     * @param icon     menu item icon
     * @param selected whether this checkbox item is selected or not
     */
    public WebCheckBoxMenuItem ( final StyleId id, final String text, final Icon icon, final boolean selected )
    {
        this ( id, text, icon, selected, ( KeyStroke ) null );
    }

    /**
     * Constructs new checkbox menu item using the specified settings.
     *
     * @param id          style ID
     * @param text        menu item text
     * @param icon        menu item icon
     * @param accelerator menu item accelerator
     */
    public WebCheckBoxMenuItem ( final StyleId id, final String text, final Icon icon, final KeyStroke accelerator )
    {
        this ( id, text, icon, false, accelerator );
    }

    /**
     * Constructs new checkbox menu item using the specified settings.
     *
     * @param id          style ID
     * @param text        menu item text
     * @param icon        menu item icon
     * @param accelerator menu item accelerator
     */
    public WebCheckBoxMenuItem ( final StyleId id, final String text, final Icon icon, final HotkeyData accelerator )
    {
        this ( id, text, icon, false, accelerator );
    }

    /**
     * Constructs new checkbox menu item using the specified settings.
     *
     * @param id          style ID
     * @param text        menu item text
     * @param icon        menu item icon
     * @param selected    whether this checkbox item is selected or not
     * @param accelerator menu item accelerator
     */
    public WebCheckBoxMenuItem ( final StyleId id, final String text, final Icon icon, final boolean selected,
                                 final HotkeyData accelerator )
    {
        this ( id, text, icon, selected, SwingUtils.getAccelerator ( accelerator ) );
    }

    /**
     * Constructs new checkbox menu item using the specified settings.
     *
     * @param id          style ID
     * @param text        menu item text
     * @param icon        menu item icon
     * @param selected    whether this checkbox item is selected or not
     * @param accelerator menu item accelerator
     */
    public WebCheckBoxMenuItem ( final StyleId id, final String text, final Icon icon, final boolean selected, final KeyStroke accelerator )
    {
        super ( text, icon, selected );
        setAccelerator ( accelerator );
        setStyleId ( id );
    }

    /**
     * Sets the key combination which invokes the menu item's action listeners without navigating the menu hierarchy.
     *
     * @param accelerator hotkey data
     */
    public void setAccelerator ( final HotkeyData accelerator )
    {
        setAccelerator ( SwingUtils.getAccelerator ( accelerator ) );
    }

    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.checkboxmenuitem;
    }

    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( this );
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return StyleManager.setStyleId ( this, id );
    }

    @Override
    public StyleId resetStyleId ()
    {
        return StyleManager.resetStyleId ( this );
    }

    @Override
    public Skin getSkin ()
    {
        return StyleManager.getSkin ( this );
    }

    @Override
    public Skin setSkin ( final Skin skin )
    {
        return StyleManager.setSkin ( this, skin );
    }

    @Override
    public Skin setSkin ( final Skin skin, final boolean recursively )
    {
        return StyleManager.setSkin ( this, skin, recursively );
    }

    @Override
    public Skin resetSkin ()
    {
        return StyleManager.resetSkin ( this );
    }

    @Override
    public void addStyleListener ( final StyleListener listener )
    {
        StyleManager.addStyleListener ( this, listener );
    }

    @Override
    public void removeStyleListener ( final StyleListener listener )
    {
        StyleManager.removeStyleListener ( this, listener );
    }

    @Override
    public Map<String, Painter> getCustomPainters ()
    {
        return StyleManager.getCustomPainters ( this );
    }

    @Override
    public Painter getCustomPainter ()
    {
        return StyleManager.getCustomPainter ( this );
    }

    @Override
    public Painter getCustomPainter ( final String id )
    {
        return StyleManager.getCustomPainter ( this, id );
    }

    @Override
    public Painter setCustomPainter ( final Painter painter )
    {
        return StyleManager.setCustomPainter ( this, painter );
    }

    @Override
    public Painter setCustomPainter ( final String id, final Painter painter )
    {
        return StyleManager.setCustomPainter ( this, id, painter );
    }

    @Override
    public boolean resetPainter ()
    {
        return StyleManager.resetPainter ( this );
    }

    @Override
    public Shape getShape ()
    {
        return ShapeMethodsImpl.getShape ( this );
    }

    @Override
    public Insets getMargin ()
    {
        return MarginMethodsImpl.getMargin ( this );
    }

    @Override
    public void setMargin ( final int margin )
    {
        MarginMethodsImpl.setMargin ( this, margin );
    }

    @Override
    public void setMargin ( final int top, final int left, final int bottom, final int right )
    {
        MarginMethodsImpl.setMargin ( this, top, left, bottom, right );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        MarginMethodsImpl.setMargin ( this, margin );
    }

    @Override
    public Insets getPadding ()
    {
        return PaddingMethodsImpl.getPadding ( this );
    }

    @Override
    public void setPadding ( final int padding )
    {
        PaddingMethodsImpl.setPadding ( this, padding );
    }

    @Override
    public void setPadding ( final int top, final int left, final int bottom, final int right )
    {
        PaddingMethodsImpl.setPadding ( this, top, left, bottom, right );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        PaddingMethodsImpl.setPadding ( this, padding );
    }

    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebCheckBoxMenuItemUI ) )
        {
            try
            {
                setUI ( ( WebCheckBoxMenuItemUI ) UIManager.getUI ( this ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebCheckBoxMenuItemUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    @Override
    public void setLanguage ( final String key, final Object... data )
    {
        LanguageManager.registerComponent ( this, key, data );
    }

    @Override
    public void updateLanguage ( final Object... data )
    {
        LanguageManager.updateComponent ( this, data );
    }

    @Override
    public void updateLanguage ( final String key, final Object... data )
    {
        LanguageManager.updateComponent ( this, key, data );
    }

    @Override
    public void removeLanguage ()
    {
        LanguageManager.unregisterComponent ( this );
    }

    @Override
    public boolean isLanguageSet ()
    {
        return LanguageManager.isRegisteredComponent ( this );
    }

    @Override
    public void setLanguageUpdater ( final LanguageUpdater updater )
    {
        LanguageManager.registerLanguageUpdater ( this, updater );
    }

    @Override
    public void removeLanguageUpdater ()
    {
        LanguageManager.unregisterLanguageUpdater ( this );
    }

    @Override
    public void registerSettings ( final String key )
    {
        SettingsManager.registerComponent ( this, key );
    }

    @Override
    public <T extends DefaultValue> void registerSettings ( final String key, final Class<T> defaultValueClass )
    {
        SettingsManager.registerComponent ( this, key, defaultValueClass );
    }

    @Override
    public void registerSettings ( final String key, final Object defaultValue )
    {
        SettingsManager.registerComponent ( this, key, defaultValue );
    }

    @Override
    public void registerSettings ( final String group, final String key )
    {
        SettingsManager.registerComponent ( this, group, key );
    }

    @Override
    public <T extends DefaultValue> void registerSettings ( final String group, final String key, final Class<T> defaultValueClass )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValueClass );
    }

    @Override
    public void registerSettings ( final String group, final String key, final Object defaultValue )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValue );
    }

    @Override
    public void registerSettings ( final String key, final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, loadInitialSettings, applySettingsChanges );
    }

    @Override
    public <T extends DefaultValue> void registerSettings ( final String key, final Class<T> defaultValueClass,
                                                            final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    @Override
    public void registerSettings ( final String key, final Object defaultValue, final boolean loadInitialSettings,
                                   final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    @Override
    public <T extends DefaultValue> void registerSettings ( final String group, final String key, final Class<T> defaultValueClass,
                                                            final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    @Override
    public void registerSettings ( final String group, final String key, final Object defaultValue, final boolean loadInitialSettings,
                                   final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    @Override
    public void registerSettings ( final SettingsProcessor settingsProcessor )
    {
        SettingsManager.registerComponent ( this, settingsProcessor );
    }

    @Override
    public void unregisterSettings ()
    {
        SettingsManager.unregisterComponent ( this );
    }

    @Override
    public void loadSettings ()
    {
        SettingsManager.loadComponentSettings ( this );
    }

    @Override
    public void saveSettings ()
    {
        SettingsManager.saveComponentSettings ( this );
    }

    @Override
    public WebCheckBoxMenuItem setPlainFont ()
    {
        return FontMethodsImpl.setPlainFont ( this );
    }

    @Override
    public WebCheckBoxMenuItem setPlainFont ( final boolean apply )
    {
        return FontMethodsImpl.setPlainFont ( this, apply );
    }

    @Override
    public boolean isPlainFont ()
    {
        return FontMethodsImpl.isPlainFont ( this );
    }

    @Override
    public WebCheckBoxMenuItem setBoldFont ()
    {
        return FontMethodsImpl.setBoldFont ( this );
    }

    @Override
    public WebCheckBoxMenuItem setBoldFont ( final boolean apply )
    {
        return FontMethodsImpl.setBoldFont ( this, apply );
    }

    @Override
    public boolean isBoldFont ()
    {
        return FontMethodsImpl.isBoldFont ( this );
    }

    @Override
    public WebCheckBoxMenuItem setItalicFont ()
    {
        return FontMethodsImpl.setItalicFont ( this );
    }

    @Override
    public WebCheckBoxMenuItem setItalicFont ( final boolean apply )
    {
        return FontMethodsImpl.setItalicFont ( this, apply );
    }

    @Override
    public boolean isItalicFont ()
    {
        return FontMethodsImpl.isItalicFont ( this );
    }

    @Override
    public WebCheckBoxMenuItem setFontStyle ( final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontStyle ( this, bold, italic );
    }

    @Override
    public WebCheckBoxMenuItem setFontStyle ( final int style )
    {
        return FontMethodsImpl.setFontStyle ( this, style );
    }

    @Override
    public WebCheckBoxMenuItem setFontSize ( final int fontSize )
    {
        return FontMethodsImpl.setFontSize ( this, fontSize );
    }

    @Override
    public WebCheckBoxMenuItem changeFontSize ( final int change )
    {
        return FontMethodsImpl.changeFontSize ( this, change );
    }

    @Override
    public int getFontSize ()
    {
        return FontMethodsImpl.getFontSize ( this );
    }

    @Override
    public WebCheckBoxMenuItem setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    @Override
    public WebCheckBoxMenuItem setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, style );
    }

    @Override
    public WebCheckBoxMenuItem setFontName ( final String fontName )
    {
        return FontMethodsImpl.setFontName ( this, fontName );
    }

    @Override
    public String getFontName ()
    {
        return FontMethodsImpl.getFontName ( this );
    }
}