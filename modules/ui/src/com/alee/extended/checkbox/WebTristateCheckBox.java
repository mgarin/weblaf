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

package com.alee.extended.checkbox;

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.LanguageMethods;
import com.alee.managers.language.LanguageUtils;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.managers.log.Log;
import com.alee.managers.settings.DefaultValue;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.tooltip.ToolTipMethods;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.WebCustomTooltip;
import com.alee.utils.EventUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SizeUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.laf.Styleable;
import com.alee.utils.swing.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.util.List;

/**
 * This WebCheckBox extension class provides additional third selection state - mixed state.
 *
 * @author Mikle Garin
 */

public class WebTristateCheckBox extends JCheckBox
        implements Styleable, ShapeProvider, EventMethods, ToolTipMethods, LanguageMethods, SettingsMethods,
        FontMethods<WebTristateCheckBox>, SizeMethods<WebTristateCheckBox>
{
    /**
     * Unique UI class ID.
     *
     * @see #getUIClassID
     */
    private static final String uiClassID = "TristateCheckBoxUI";

    /**
     * Constructs new tristate checkbox.
     */
    public WebTristateCheckBox ()
    {
        super ();
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param checked whether checkbox should be checked or not
     */
    public WebTristateCheckBox ( final boolean checked )
    {
        super ( "", checked );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param state initial check state
     */
    public WebTristateCheckBox ( final CheckState state )
    {
        super ();
        setState ( state );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param icon checkbox icon
     */
    public WebTristateCheckBox ( final Icon icon )
    {
        super ( icon );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param icon    checkbox icon
     * @param checked whether checkbox should be checked or not
     */
    public WebTristateCheckBox ( final Icon icon, final boolean checked )
    {
        super ( icon, checked );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param icon  checkbox icon
     * @param state initial check state
     */
    public WebTristateCheckBox ( final Icon icon, final CheckState state )
    {
        super ( icon );
        setState ( state );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param action checkbox action
     */
    public WebTristateCheckBox ( final Action action )
    {
        super ( action );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param text checkbox text
     */
    public WebTristateCheckBox ( final String text )
    {
        super ( text );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param text    checkbox text
     * @param checked whether checkbox should be checked or not
     */
    public WebTristateCheckBox ( final String text, final boolean checked )
    {
        super ( text, checked );
    }


    /**
     * Constructs new tristate checkbox.
     *
     * @param text  checkbox text
     * @param state initial check state
     */
    public WebTristateCheckBox ( final String text, final CheckState state )
    {
        super ( text );
        setState ( state );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param text checkbox text
     * @param icon checkbox icon
     */
    public WebTristateCheckBox ( final String text, final Icon icon )
    {
        super ( text, icon );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param text    checkbox text
     * @param icon    checkbox icon
     * @param checked whether checkbox should be checked or not
     */
    public WebTristateCheckBox ( final String text, final Icon icon, final boolean checked )
    {
        super ( text, icon, checked );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param text  checkbox text
     * @param icon  checkbox icon
     * @param state initial check state
     */
    public WebTristateCheckBox ( final String text, final Icon icon, final CheckState state )
    {
        super ( text, icon );
        setState ( state );
    }

    /**
     * Initializes checkbox settings.
     *
     * @param text initial text
     * @param icon initial icon
     */
    @Override
    protected void init ( final String text, final Icon icon )
    {
        // Custom button model
        model = new TristateCheckBoxModel ();
        setModel ( model );

        // Initializing transltation if required
        super.init ( LanguageUtils.getInitialText ( text ), icon );
        LanguageUtils.registerInitialLanguage ( this, text );
    }

    /**
     * Returns actual tristate checkbox model.
     *
     * @return actual tristate checkbox model
     */
    public TristateCheckBoxModel getActualModel ()
    {
        return ( TristateCheckBoxModel ) model;
    }

    /**
     * Returns whether partially checked tristate checkbox should be checked or unchecked on toggle.
     *
     * @return true if partially checked tristate checkbox should be checked on toggle, false if it should be unchecked
     */
    public boolean isCheckMixedOnToggle ()
    {
        return getActualModel ().isCheckMixedOnToggle ();
    }

    /**
     * Sets whether partially checked tristate checkbox should be checked or unchecked on toggle
     *
     * @param checkMixedOnToggle whether partially checked tristate checkbox should be checked or unchecked on toggle
     */
    public void setCheckMixedOnToggle ( final boolean checkMixedOnToggle )
    {
        getActualModel ().setCheckMixedOnToggle ( checkMixedOnToggle );
    }

    /**
     * Returns tristate checkbox check state.
     *
     * @return tristate checkbox check state
     */
    public CheckState getState ()
    {
        return getActualModel ().getState ();
    }

    /**
     * Returns next check state for toggle action.
     *
     * @param checkState current check state
     * @return next check state for toggle action
     */
    public CheckState getNextState ( final CheckState checkState )
    {
        return getActualModel ().getNextState ( checkState );
    }

    /**
     * Sets tristate checkbox check state.
     *
     * @param state new tristate checkbox check state
     */
    public void setState ( final CheckState state )
    {
        getActualModel ().setState ( state );
    }

    /**
     * Returns whether checkbox is checked or not.
     *
     * @return true if checkbox is checked, false otherwise
     */
    public boolean isChecked ()
    {
        return getActualModel ().isSelected ();
    }

    /**
     * Forces checked state.
     */
    public void setChecked ()
    {
        setState ( CheckState.checked );
    }

    /**
     * Returns whether checkbox is in mixed state or not.
     *
     * @return true if checkbox is in mixed state, false otherwise
     */
    public boolean isMixed ()
    {
        return getActualModel ().isMixed ();
    }

    /**
     * Forces mixed state.
     */
    public void setMixed ()
    {
        setState ( CheckState.mixed );
    }

    /**
     * Returns whether checkbox is unchecked or not.
     *
     * @return true if checkbox is unchecked, false otherwise
     */
    public boolean isUnchecked ()
    {
        return !isChecked () && !isMixed ();
    }

    /**
     * Forces unchecked state.
     */
    public void setUnchecked ()
    {
        setState ( CheckState.unchecked );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleId ()
    {
        return getWebUI ().getStyleId ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyleId ( final String id )
    {
        getWebUI ().setStyleId ( id );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape provideShape ()
    {
        return getWebUI ().provideShape ();
    }

    /**
     * Returns Web-UI applied to this class.
     *
     * @return Web-UI applied to this class
     */
    public WebTristateCheckBoxUI getWebUI ()
    {
        return ( WebTristateCheckBoxUI ) getUI ();
    }

    /**
     * Installs a Web-UI into this component.
     */
    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebTristateCheckBoxUI ) )
        {
            try
            {
                setUI ( ( WebTristateCheckBoxUI ) ReflectUtils.createInstance ( WebLookAndFeel.tristateCheckBoxUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebTristateCheckBoxUI () );
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
    public String getUIClassID ()
    {
        return uiClassID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MouseAdapter onMousePress ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMousePress ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MouseAdapter onMousePress ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventUtils.onMousePress ( this, mouseButton, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MouseAdapter onMouseEnter ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseEnter ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MouseAdapter onMouseExit ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseExit ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MouseAdapter onMouseDrag ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseDrag ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MouseAdapter onMouseDrag ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseDrag ( this, mouseButton, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MouseAdapter onMouseClick ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseClick ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MouseAdapter onMouseClick ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseClick ( this, mouseButton, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MouseAdapter onDoubleClick ( final MouseEventRunnable runnable )
    {
        return EventUtils.onDoubleClick ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MouseAdapter onMenuTrigger ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMenuTrigger ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KeyAdapter onKeyType ( final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyType ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KeyAdapter onKeyType ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyType ( this, hotkey, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KeyAdapter onKeyPress ( final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyPress ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KeyAdapter onKeyPress ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyPress ( this, hotkey, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KeyAdapter onKeyRelease ( final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyRelease ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KeyAdapter onKeyRelease ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyRelease ( this, hotkey, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FocusAdapter onFocusGain ( final FocusEventRunnable runnable )
    {
        return EventUtils.onFocusGain ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FocusAdapter onFocusLoss ( final FocusEventRunnable runnable )
    {
        return EventUtils.onFocusLoss ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip setToolTip ( final String tooltip )
    {
        return TooltipManager.setTooltip ( this, tooltip );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip setToolTip ( final Icon icon, final String tooltip )
    {
        return TooltipManager.setTooltip ( this, icon, tooltip );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip setToolTip ( final String tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.setTooltip ( this, tooltip, tooltipWay );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip setToolTip ( final Icon icon, final String tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.setTooltip ( this, icon, tooltip, tooltipWay );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip setToolTip ( final String tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.setTooltip ( this, tooltip, tooltipWay, delay );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip setToolTip ( final Icon icon, final String tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.setTooltip ( this, icon, tooltip, tooltipWay, delay );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip setToolTip ( final JComponent tooltip )
    {
        return TooltipManager.setTooltip ( this, tooltip );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip setToolTip ( final JComponent tooltip, final int delay )
    {
        return TooltipManager.setTooltip ( this, tooltip, delay );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip setToolTip ( final JComponent tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.setTooltip ( this, tooltip, tooltipWay );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip setToolTip ( final JComponent tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.setTooltip ( this, tooltip, tooltipWay, delay );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip addToolTip ( final String tooltip )
    {
        return TooltipManager.addTooltip ( this, tooltip );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip addToolTip ( final Icon icon, final String tooltip )
    {
        return TooltipManager.addTooltip ( this, icon, tooltip );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip addToolTip ( final String tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.addTooltip ( this, tooltip, tooltipWay );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip addToolTip ( final Icon icon, final String tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.addTooltip ( this, icon, tooltip, tooltipWay );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip addToolTip ( final String tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.addTooltip ( this, tooltip, tooltipWay, delay );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip addToolTip ( final Icon icon, final String tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.addTooltip ( this, icon, tooltip, tooltipWay, delay );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip addToolTip ( final JComponent tooltip )
    {
        return TooltipManager.addTooltip ( this, tooltip );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip addToolTip ( final JComponent tooltip, final int delay )
    {
        return TooltipManager.addTooltip ( this, tooltip, delay );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip addToolTip ( final JComponent tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.addTooltip ( this, tooltip, tooltipWay );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip addToolTip ( final JComponent tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.addTooltip ( this, tooltip, tooltipWay, delay );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeToolTip ( final WebCustomTooltip tooltip )
    {
        TooltipManager.removeTooltip ( this, tooltip );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeToolTips ()
    {
        TooltipManager.removeTooltips ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeToolTips ( final WebCustomTooltip... tooltips )
    {
        TooltipManager.removeTooltips ( this, tooltips );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeToolTips ( final List<WebCustomTooltip> tooltips )
    {
        TooltipManager.removeTooltips ( this, tooltips );
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
    public WebTristateCheckBox setPlainFont ()
    {
        return SwingUtils.setPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTristateCheckBox setPlainFont ( final boolean apply )
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
    public WebTristateCheckBox setBoldFont ()
    {
        return SwingUtils.setBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTristateCheckBox setBoldFont ( final boolean apply )
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
    public WebTristateCheckBox setItalicFont ()
    {
        return SwingUtils.setItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTristateCheckBox setItalicFont ( final boolean apply )
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
    public WebTristateCheckBox setFontStyle ( final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTristateCheckBox setFontStyle ( final int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTristateCheckBox setFontSize ( final int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTristateCheckBox changeFontSize ( final int change )
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
    public WebTristateCheckBox setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTristateCheckBox setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTristateCheckBox setFontName ( final String fontName )
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

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPreferredWidth ()
    {
        return SizeUtils.getPreferredWidth ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTristateCheckBox setPreferredWidth ( final int preferredWidth )
    {
        return SizeUtils.setPreferredWidth ( this, preferredWidth );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPreferredHeight ()
    {
        return SizeUtils.getPreferredHeight ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTristateCheckBox setPreferredHeight ( final int preferredHeight )
    {
        return SizeUtils.setPreferredHeight ( this, preferredHeight );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinimumWidth ()
    {
        return SizeUtils.getMinimumWidth ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTristateCheckBox setMinimumWidth ( final int minimumWidth )
    {
        return SizeUtils.setMinimumWidth ( this, minimumWidth );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinimumHeight ()
    {
        return SizeUtils.getMinimumHeight ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTristateCheckBox setMinimumHeight ( final int minimumHeight )
    {
        return SizeUtils.setMinimumHeight ( this, minimumHeight );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaximumWidth ()
    {
        return SizeUtils.getMaximumWidth ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTristateCheckBox setMaximumWidth ( final int maximumWidth )
    {
        return SizeUtils.setMaximumWidth ( this, maximumWidth );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaximumHeight ()
    {
        return SizeUtils.getMaximumHeight ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTristateCheckBox setMaximumHeight ( final int maximumHeight )
    {
        return SizeUtils.setMaximumHeight ( this, maximumHeight );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize ()
    {
        return SizeUtils.getPreferredSize ( this, super.getPreferredSize () );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTristateCheckBox setPreferredSize ( final int width, final int height )
    {
        return SizeUtils.setPreferredSize ( this, width, height );
    }
}