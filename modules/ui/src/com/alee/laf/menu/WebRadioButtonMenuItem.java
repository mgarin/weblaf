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

import com.alee.extended.painter.Painter;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.LanguageMethods;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.managers.log.Log;
import com.alee.managers.settings.DefaultValue;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.FontMethods;

import javax.swing.*;
import java.awt.*;

/**
 * This JRadioButtonMenuItem extension class provides a direct access to WebRadioButtonMenuItemUI methods.
 *
 * @author Mikle Garin
 */

public class WebRadioButtonMenuItem extends JRadioButtonMenuItem
        implements LanguageMethods, SettingsMethods, FontMethods<WebRadioButtonMenuItem>
{
    /**
     * Constructs new radiobutton menu item.
     */
    public WebRadioButtonMenuItem ()
    {
        super ();
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param a menu item action
     */
    public WebRadioButtonMenuItem ( final Action a )
    {
        super ( a );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param icon menu item icon
     */
    public WebRadioButtonMenuItem ( final Icon icon )
    {
        super ( icon );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param icon     menu item icon
     * @param selected whether this radiobutton item is selected or not
     */
    public WebRadioButtonMenuItem ( final Icon icon, final boolean selected )
    {
        super ( icon, selected );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param text menu item text
     */
    public WebRadioButtonMenuItem ( final String text )
    {
        super ( text );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param text        menu item text
     * @param accelerator menu item accelerator
     */
    public WebRadioButtonMenuItem ( final String text, final KeyStroke accelerator )
    {
        super ( text );
        setAccelerator ( accelerator );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param text        menu item text
     * @param accelerator menu item accelerator
     */
    public WebRadioButtonMenuItem ( final String text, final HotkeyData accelerator )
    {
        super ( text );
        setAccelerator ( accelerator );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param text menu item text
     * @param icon menu item icon
     */
    public WebRadioButtonMenuItem ( final String text, final Icon icon )
    {
        super ( text, icon );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param text     menu item text
     * @param icon     menu item icon
     * @param selected whether this radiobutton item is selected or not
     */
    public WebRadioButtonMenuItem ( final String text, final Icon icon, final boolean selected )
    {
        super ( text, icon, selected );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param text     menu item text
     * @param selected whether this radiobutton item is selected or not
     */
    public WebRadioButtonMenuItem ( final String text, final boolean selected )
    {
        super ( text, selected );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param text        menu item text
     * @param icon        menu item icon
     * @param accelerator menu item accelerator
     */
    public WebRadioButtonMenuItem ( final String text, final Icon icon, final KeyStroke accelerator )
    {
        super ( text, icon );
        setAccelerator ( accelerator );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param text        menu item text
     * @param icon        menu item icon
     * @param b           whether this checkbox item is selected or not
     * @param accelerator menu item accelerator
     */
    public WebRadioButtonMenuItem ( final String text, final Icon icon, final boolean b, final KeyStroke accelerator )
    {
        super ( text, icon, b );
        setAccelerator ( accelerator );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param text        menu item text
     * @param icon        menu item icon
     * @param accelerator menu item accelerator
     */
    public WebRadioButtonMenuItem ( final String text, final Icon icon, final HotkeyData accelerator )
    {
        super ( text, icon );
        setAccelerator ( accelerator );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param text        menu item text
     * @param icon        menu item icon
     * @param b           whether this checkbox item is selected or not
     * @param accelerator menu item accelerator
     */
    public WebRadioButtonMenuItem ( final String text, final Icon icon, final boolean b, final HotkeyData accelerator )
    {
        super ( text, icon, b );
        setAccelerator ( accelerator );
    }

    /**
     * Sets the key combination which invokes the menu item's action listeners without navigating the menu hierarchy.
     *
     * @param hotkey hotkey data
     */
    public void setAccelerator ( final HotkeyData hotkey )
    {
        SwingUtils.setAccelerator ( this, hotkey );
    }

    /**
     * Returns menu item margin.
     *
     * @return menu item margin
     */
    @Override
    public Insets getMargin ()
    {
        return getWebUI ().getMargin ();
    }

    /**
     * Sets menu item margin.
     *
     * @param margin new menu item margin
     */
    @Override
    public void setMargin ( final Insets margin )
    {
        getWebUI ().setMargin ( margin );
    }

    /**
     * Returns spacing between menu item content and its left/right borders.
     *
     * @return spacing between menu item content and its left/right borders
     */
    public int getSideSpacing ()
    {
        return getWebUI ().getSideSpacing ();
    }

    /**
     * Sets spacing between menu item content and its left/right borders
     *
     * @param sideSpacing spacing between menu item content and its left/right borders
     */
    public void setSideSpacing ( final int sideSpacing )
    {
        getWebUI ().setSideSpacing ( sideSpacing );
    }

    /**
     * Returns disabled menu item foreground.
     *
     * @return disabled menu item foreground
     */
    public Color getDisabledFg ()
    {
        return getWebUI ().getDisabledFg ();
    }

    /**
     * Sets disabled menu item foreground.
     *
     * @param foreground new disabled menu item foreground
     */
    public void setDisabledFg ( final Color foreground )
    {
        getWebUI ().setDisabledFg ( foreground );
    }

    /**
     * Returns top background color for selected item.
     *
     * @return top background color for selected item
     */
    public Color getSelectedTopBg ()
    {
        return getWebUI ().getSelectedTopBg ();
    }

    /**
     * Sets top background color for selected item.
     *
     * @param background new top background color for selected item
     */
    public void setSelectedTopBg ( final Color background )
    {
        getWebUI ().setSelectedTopBg ( background );
    }

    /**
     * Returns bottom background color for selected item.
     *
     * @return bottom background color for selected item
     */
    public Color getSelectedBottomBg ()
    {
        return getWebUI ().getSelectedBottomBg ();
    }

    /**
     * Sets bottom background color for selected item.
     *
     * @param background new bottom background color for selected item
     */
    public void setSelectedBottomBg ( final Color background )
    {
        getWebUI ().setSelectedBottomBg ( background );
    }

    /**
     * Returns accelerator text background.
     *
     * @return accelerator text background
     */
    public Color getAcceleratorBg ()
    {
        return getWebUI ().getAcceleratorBg ();
    }

    /**
     * Sets accelerator text background.
     *
     * @param background new accelerator text background
     */
    public void setAcceleratorBg ( final Color background )
    {
        getWebUI ().setAcceleratorBg ( background );
    }

    /**
     * Returns accelerator foreground.
     *
     * @return accelerator foreground
     */
    public Color getAcceleratorFg ()
    {
        return getWebUI ().getAcceleratorFg ();
    }

    /**
     * Sets accelerator foreground.
     *
     * @param foreground new accelerator foreground
     */
    public void setAcceleratorFg ( final Color foreground )
    {
        getWebUI ().setAcceleratorFg ( foreground );
    }

    /**
     * Returns disabled accelerator foreground.
     *
     * @return disabled accelerator foreground
     */
    public Color getAcceleratorDisabledFg ()
    {
        return getWebUI ().getAcceleratorDisabledFg ();
    }

    /**
     * Sets disabled accelerator foreground.
     *
     * @param foreground new disabled accelerator foreground
     */
    public void setAcceleratorDisabledFg ( final Color foreground )
    {
        getWebUI ().setAcceleratorDisabledFg ( foreground );
    }

    /**
     * Returns gap between menu item icon/text and accelerator.
     *
     * @return gap between menu item icon/text and accelerator
     */
    public int getAcceleratorGap ()
    {
        return getWebUI ().getAcceleratorGap ();
    }

    /**
     * Sets gap between menu icon/text and accelerator.
     *
     * @param gap new gap between menu icon/text and accelerator
     */
    public void setAcceleratorGap ( final int gap )
    {
        getWebUI ().setAcceleratorGap ( gap );
    }

    /**
     * Returns whether should align all item texts to a single vertical line within single popup menu or not.
     *
     * @return true if should align all item texts to a single vertical line within single popup menu, false otherwise
     */
    public boolean isAlignTextToMenuIcons ()
    {
        return getWebUI ().isAlignTextToMenuIcons ();
    }

    /**
     * Sets whether should align all item texts to a single vertical line within single popup menu or not.
     *
     * @param align whether should align all item texts to a single vertical line within single popup menu or not
     */
    public void setAlignTextToMenuIcons ( final boolean align )
    {
        getWebUI ().setAlignTextToMenuIcons ( align );
    }

    /**
     * Returns icon alignment.
     *
     * @return icon alignment
     */
    public int getIconAlignment ()
    {
        return getWebUI ().getIconAlignment ();
    }

    /**
     * Sets icon alignment
     *
     * @param alignment new icon alignment
     */
    public void setIconAlignment ( final int alignment )
    {
        getWebUI ().setIconAlignment ( alignment );
    }

    /**
     * Returns menu item painter.
     *
     * @return menu item painter
     */
    public Painter getPainter ()
    {
        return getWebUI ().getPainter ();
    }

    /**
     * Sets menu item painter.
     *
     * @param painter new menu item painter
     */
    public void setPainter ( final Painter painter )
    {
        getWebUI ().setPainter ( painter );
    }

    /**
     * Returns checkbox menu item check color.
     *
     * @return checkbox menu item check color
     */
    public Color getCheckColor ()
    {
        return getWebUI ().getCheckColor ();
    }

    /**
     * Sets checkbox menu item check color.
     *
     * @param color checkbox menu item check color
     */
    public void setCheckColor ( final Color color )
    {
        getWebUI ().setCheckColor ( color );
    }

    /**
     * Returns Web-UI applied to this class.
     *
     * @return Web-UI applied to this class
     */
    public WebRadioButtonMenuItemUI getWebUI ()
    {
        return ( WebRadioButtonMenuItemUI ) getUI ();
    }

    /**
     * Installs a Web-UI into this component.
     */
    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebRadioButtonMenuItemUI ) )
        {
            try
            {
                setUI ( ( WebRadioButtonMenuItemUI ) ReflectUtils.createInstance ( WebLookAndFeel.radioButtonMenuItemUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebRadioButtonMenuItemUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLanguage ( final String key, final Object... data )
    {
        LanguageManager.registerComponent ( this, key, data );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLanguage ( final Object... data )
    {
        LanguageManager.updateComponent ( this, data );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLanguage ( final String key, final Object... data )
    {
        LanguageManager.updateComponent ( this, key, data );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLanguage ()
    {
        LanguageManager.unregisterComponent ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLanguageSet ()
    {
        return LanguageManager.isRegisteredComponent ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLanguageUpdater ( final LanguageUpdater updater )
    {
        LanguageManager.registerLanguageUpdater ( this, updater );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLanguageUpdater ()
    {
        LanguageManager.unregisterLanguageUpdater ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final String key )
    {
        SettingsManager.registerComponent ( this, key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DefaultValue> void registerSettings ( final String key, final Class<T> defaultValueClass )
    {
        SettingsManager.registerComponent ( this, key, defaultValueClass );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final String key, final Object defaultValue )
    {
        SettingsManager.registerComponent ( this, key, defaultValue );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final String group, final String key )
    {
        SettingsManager.registerComponent ( this, group, key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DefaultValue> void registerSettings ( final String group, final String key, final Class<T> defaultValueClass )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValueClass );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final String group, final String key, final Object defaultValue )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValue );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final String key, final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DefaultValue> void registerSettings ( final String key, final Class<T> defaultValueClass,
                                                            final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final String key, final Object defaultValue, final boolean loadInitialSettings,
                                   final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DefaultValue> void registerSettings ( final String group, final String key, final Class<T> defaultValueClass,
                                                            final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final String group, final String key, final Object defaultValue, final boolean loadInitialSettings,
                                   final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final SettingsProcessor settingsProcessor )
    {
        SettingsManager.registerComponent ( this, settingsProcessor );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterSettings ()
    {
        SettingsManager.unregisterComponent ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadSettings ()
    {
        SettingsManager.loadComponentSettings ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveSettings ()
    {
        SettingsManager.saveComponentSettings ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebRadioButtonMenuItem setPlainFont ()
    {
        return SwingUtils.setPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebRadioButtonMenuItem setPlainFont ( final boolean apply )
    {
        return SwingUtils.setPlainFont ( this, apply );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPlainFont ()
    {
        return SwingUtils.isPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebRadioButtonMenuItem setBoldFont ()
    {
        return SwingUtils.setBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebRadioButtonMenuItem setBoldFont ( final boolean apply )
    {
        return SwingUtils.setBoldFont ( this, apply );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBoldFont ()
    {
        return SwingUtils.isBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebRadioButtonMenuItem setItalicFont ()
    {
        return SwingUtils.setItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebRadioButtonMenuItem setItalicFont ( final boolean apply )
    {
        return SwingUtils.setItalicFont ( this, apply );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isItalicFont ()
    {
        return SwingUtils.isItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebRadioButtonMenuItem setFontStyle ( final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebRadioButtonMenuItem setFontStyle ( final int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebRadioButtonMenuItem setFontSize ( final int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebRadioButtonMenuItem changeFontSize ( final int change )
    {
        return SwingUtils.changeFontSize ( this, change );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFontSize ()
    {
        return SwingUtils.getFontSize ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebRadioButtonMenuItem setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebRadioButtonMenuItem setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebRadioButtonMenuItem setFontName ( final String fontName )
    {
        return SwingUtils.setFontName ( this, fontName );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFontName ()
    {
        return SwingUtils.getFontName ( this );
    }
}