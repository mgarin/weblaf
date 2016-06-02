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

import com.alee.laf.checkbox.CheckState;
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
import com.alee.managers.style.*;
import com.alee.managers.tooltip.ToolTipMethods;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.WebCustomTooltip;
import com.alee.painter.Paintable;
import com.alee.painter.Painter;
import com.alee.utils.swing.MouseButton;
import com.alee.utils.swing.extensions.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.util.List;
import java.util.Map;

/**
 * This WebCheckBox extension class provides additional third selection state - mixed state.
 *
 * @author Mikle Garin
 */

public class WebTristateCheckBox extends JCheckBox
        implements Styleable, Paintable, ShapeProvider, MarginSupport, PaddingSupport, EventMethods, ToolTipMethods,
        LanguageMethods, SettingsMethods, FontMethods<WebTristateCheckBox>, SizeMethods<WebTristateCheckBox>
{
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
     * Constructs new tristate checkbox.
     *
     * @param id style ID
     */
    public WebTristateCheckBox ( final StyleId id )
    {
        super ();
        setStyleId ( id );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param id      style ID
     * @param checked whether checkbox should be checked or not
     */
    public WebTristateCheckBox ( final StyleId id, final boolean checked )
    {
        super ( "", checked );
        setStyleId ( id );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param id    style ID
     * @param state initial check state
     */
    public WebTristateCheckBox ( final StyleId id, final CheckState state )
    {
        super ();
        setState ( state );
        setStyleId ( id );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param id   style ID
     * @param icon checkbox icon
     */
    public WebTristateCheckBox ( final StyleId id, final Icon icon )
    {
        super ( icon );
        setStyleId ( id );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param id      style ID
     * @param icon    checkbox icon
     * @param checked whether checkbox should be checked or not
     */
    public WebTristateCheckBox ( final StyleId id, final Icon icon, final boolean checked )
    {
        super ( icon, checked );
        setStyleId ( id );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param id    style ID
     * @param icon  checkbox icon
     * @param state initial check state
     */
    public WebTristateCheckBox ( final StyleId id, final Icon icon, final CheckState state )
    {
        super ( icon );
        setState ( state );
        setStyleId ( id );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param id     style ID
     * @param action checkbox action
     */
    public WebTristateCheckBox ( final StyleId id, final Action action )
    {
        super ( action );
        setStyleId ( id );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param id   style ID
     * @param text checkbox text
     */
    public WebTristateCheckBox ( final StyleId id, final String text )
    {
        super ( text );
        setStyleId ( id );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param id      style ID
     * @param text    checkbox text
     * @param checked whether checkbox should be checked or not
     */
    public WebTristateCheckBox ( final StyleId id, final String text, final boolean checked )
    {
        super ( text, checked );
        setStyleId ( id );
    }


    /**
     * Constructs new tristate checkbox.
     *
     * @param id    style ID
     * @param text  checkbox text
     * @param state initial check state
     */
    public WebTristateCheckBox ( final StyleId id, final String text, final CheckState state )
    {
        super ( text );
        setState ( state );
        setStyleId ( id );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param id   style ID
     * @param text checkbox text
     * @param icon checkbox icon
     */
    public WebTristateCheckBox ( final StyleId id, final String text, final Icon icon )
    {
        super ( text, icon );
        setStyleId ( id );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param id      style ID
     * @param text    checkbox text
     * @param icon    checkbox icon
     * @param checked whether checkbox should be checked or not
     */
    public WebTristateCheckBox ( final StyleId id, final String text, final Icon icon, final boolean checked )
    {
        super ( text, icon, checked );
        setStyleId ( id );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param id    style ID
     * @param text  checkbox text
     * @param icon  checkbox icon
     * @param state initial check state
     */
    public WebTristateCheckBox ( final StyleId id, final String text, final Icon icon, final CheckState state )
    {
        super ( text, icon );
        setState ( state );
        setStyleId ( id );
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

        // Initializing translation if required
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
    public Shape provideShape ()
    {
        return getWebUI ().provideShape ();
    }

    @Override
    public Insets getMargin ()
    {
        return getWebUI ().getMargin ();
    }

    /**
     * Sets new margin.
     *
     * @param margin new margin
     */
    public void setMargin ( final int margin )
    {
        setMargin ( margin, margin, margin, margin );
    }

    /**
     * Sets new margin.
     *
     * @param top    new top margin
     * @param left   new left margin
     * @param bottom new bottom margin
     * @param right  new right margin
     */
    public void setMargin ( final int top, final int left, final int bottom, final int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        getWebUI ().setMargin ( margin );
    }

    @Override
    public Insets getPadding ()
    {
        return getWebUI ().getPadding ();
    }

    /**
     * Sets new padding.
     *
     * @param padding new padding
     */
    public void setPadding ( final int padding )
    {
        setPadding ( padding, padding, padding, padding );
    }

    /**
     * Sets new padding.
     *
     * @param top    new top padding
     * @param left   new left padding
     * @param bottom new bottom padding
     * @param right  new right padding
     */
    public void setPadding ( final int top, final int left, final int bottom, final int right )
    {
        setPadding ( new Insets ( top, left, bottom, right ) );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        getWebUI ().setPadding ( padding );
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
                setUI ( ( WebTristateCheckBoxUI ) UIManager.getUI ( this ) );
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

    @Override
    public String getUIClassID ()
    {
        return StyleableComponent.tristatecheckbox.getUIClassID ();
    }

    @Override
    public MouseAdapter onMousePress ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMousePress ( this, runnable );
    }

    @Override
    public MouseAdapter onMousePress ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMousePress ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onMouseEnter ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseEnter ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseExit ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseExit ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseDrag ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseDrag ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseDrag ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseDrag ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onMouseClick ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseClick ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseClick ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseClick ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onDoubleClick ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onDoubleClick ( this, runnable );
    }

    @Override
    public MouseAdapter onMenuTrigger ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMenuTrigger ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyType ( final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyType ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyType ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyType ( this, hotkey, runnable );
    }

    @Override
    public KeyAdapter onKeyPress ( final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyPress ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyPress ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyPress ( this, hotkey, runnable );
    }

    @Override
    public KeyAdapter onKeyRelease ( final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyRelease ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyRelease ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyRelease ( this, hotkey, runnable );
    }

    @Override
    public FocusAdapter onFocusGain ( final FocusEventRunnable runnable )
    {
        return EventMethodsImpl.onFocusGain ( this, runnable );
    }

    @Override
    public FocusAdapter onFocusLoss ( final FocusEventRunnable runnable )
    {
        return EventMethodsImpl.onFocusLoss ( this, runnable );
    }

    @Override
    public MouseAdapter onDragStart ( final int shift, final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onDragStart ( this, shift, runnable );
    }

    @Override
    public MouseAdapter onDragStart ( final int shift, final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onDragStart ( this, shift, mouseButton, runnable );
    }

    @Override
    public WebCustomTooltip setToolTip ( final String tooltip )
    {
        return TooltipManager.setTooltip ( this, tooltip );
    }

    @Override
    public WebCustomTooltip setToolTip ( final Icon icon, final String tooltip )
    {
        return TooltipManager.setTooltip ( this, icon, tooltip );
    }

    @Override
    public WebCustomTooltip setToolTip ( final String tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.setTooltip ( this, tooltip, tooltipWay );
    }

    @Override
    public WebCustomTooltip setToolTip ( final Icon icon, final String tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.setTooltip ( this, icon, tooltip, tooltipWay );
    }

    @Override
    public WebCustomTooltip setToolTip ( final String tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.setTooltip ( this, tooltip, tooltipWay, delay );
    }

    @Override
    public WebCustomTooltip setToolTip ( final Icon icon, final String tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.setTooltip ( this, icon, tooltip, tooltipWay, delay );
    }

    @Override
    public WebCustomTooltip setToolTip ( final JComponent tooltip )
    {
        return TooltipManager.setTooltip ( this, tooltip );
    }

    @Override
    public WebCustomTooltip setToolTip ( final JComponent tooltip, final int delay )
    {
        return TooltipManager.setTooltip ( this, tooltip, delay );
    }

    @Override
    public WebCustomTooltip setToolTip ( final JComponent tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.setTooltip ( this, tooltip, tooltipWay );
    }

    @Override
    public WebCustomTooltip setToolTip ( final JComponent tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.setTooltip ( this, tooltip, tooltipWay, delay );
    }

    @Override
    public WebCustomTooltip addToolTip ( final String tooltip )
    {
        return TooltipManager.addTooltip ( this, tooltip );
    }

    @Override
    public WebCustomTooltip addToolTip ( final Icon icon, final String tooltip )
    {
        return TooltipManager.addTooltip ( this, icon, tooltip );
    }

    @Override
    public WebCustomTooltip addToolTip ( final String tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.addTooltip ( this, tooltip, tooltipWay );
    }

    @Override
    public WebCustomTooltip addToolTip ( final Icon icon, final String tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.addTooltip ( this, icon, tooltip, tooltipWay );
    }

    @Override
    public WebCustomTooltip addToolTip ( final String tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.addTooltip ( this, tooltip, tooltipWay, delay );
    }

    @Override
    public WebCustomTooltip addToolTip ( final Icon icon, final String tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.addTooltip ( this, icon, tooltip, tooltipWay, delay );
    }

    @Override
    public WebCustomTooltip addToolTip ( final JComponent tooltip )
    {
        return TooltipManager.addTooltip ( this, tooltip );
    }

    @Override
    public WebCustomTooltip addToolTip ( final JComponent tooltip, final int delay )
    {
        return TooltipManager.addTooltip ( this, tooltip, delay );
    }

    @Override
    public WebCustomTooltip addToolTip ( final JComponent tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.addTooltip ( this, tooltip, tooltipWay );
    }

    @Override
    public WebCustomTooltip addToolTip ( final JComponent tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.addTooltip ( this, tooltip, tooltipWay, delay );
    }

    @Override
    public void removeToolTip ( final WebCustomTooltip tooltip )
    {
        TooltipManager.removeTooltip ( this, tooltip );
    }

    @Override
    public void removeToolTips ()
    {
        TooltipManager.removeTooltips ( this );
    }

    @Override
    public void removeToolTips ( final WebCustomTooltip... tooltips )
    {
        TooltipManager.removeTooltips ( this, tooltips );
    }

    @Override
    public void removeToolTips ( final List<WebCustomTooltip> tooltips )
    {
        TooltipManager.removeTooltips ( this, tooltips );
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
    public WebTristateCheckBox setPlainFont ()
    {
        return FontMethodsImpl.setPlainFont ( this );
    }

    @Override
    public WebTristateCheckBox setPlainFont ( final boolean apply )
    {
        return FontMethodsImpl.setPlainFont ( this, apply );
    }

    @Override
    public boolean isPlainFont ()
    {
        return FontMethodsImpl.isPlainFont ( this );
    }

    @Override
    public WebTristateCheckBox setBoldFont ()
    {
        return FontMethodsImpl.setBoldFont ( this );
    }

    @Override
    public WebTristateCheckBox setBoldFont ( final boolean apply )
    {
        return FontMethodsImpl.setBoldFont ( this, apply );
    }

    @Override
    public boolean isBoldFont ()
    {
        return FontMethodsImpl.isBoldFont ( this );
    }

    @Override
    public WebTristateCheckBox setItalicFont ()
    {
        return FontMethodsImpl.setItalicFont ( this );
    }

    @Override
    public WebTristateCheckBox setItalicFont ( final boolean apply )
    {
        return FontMethodsImpl.setItalicFont ( this, apply );
    }

    @Override
    public boolean isItalicFont ()
    {
        return FontMethodsImpl.isItalicFont ( this );
    }

    @Override
    public WebTristateCheckBox setFontStyle ( final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontStyle ( this, bold, italic );
    }

    @Override
    public WebTristateCheckBox setFontStyle ( final int style )
    {
        return FontMethodsImpl.setFontStyle ( this, style );
    }

    @Override
    public WebTristateCheckBox setFontSize ( final int fontSize )
    {
        return FontMethodsImpl.setFontSize ( this, fontSize );
    }

    @Override
    public WebTristateCheckBox changeFontSize ( final int change )
    {
        return FontMethodsImpl.changeFontSize ( this, change );
    }

    @Override
    public int getFontSize ()
    {
        return FontMethodsImpl.getFontSize ( this );
    }

    @Override
    public WebTristateCheckBox setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    @Override
    public WebTristateCheckBox setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, style );
    }

    @Override
    public WebTristateCheckBox setFontName ( final String fontName )
    {
        return FontMethodsImpl.setFontName ( this, fontName );
    }

    @Override
    public String getFontName ()
    {
        return FontMethodsImpl.getFontName ( this );
    }

    @Override
    public int getPreferredWidth ()
    {
        return SizeMethodsImpl.getPreferredWidth ( this );
    }

    @Override
    public WebTristateCheckBox setPreferredWidth ( final int preferredWidth )
    {
        return SizeMethodsImpl.setPreferredWidth ( this, preferredWidth );
    }

    @Override
    public int getPreferredHeight ()
    {
        return SizeMethodsImpl.getPreferredHeight ( this );
    }

    @Override
    public WebTristateCheckBox setPreferredHeight ( final int preferredHeight )
    {
        return SizeMethodsImpl.setPreferredHeight ( this, preferredHeight );
    }

    @Override
    public int getMinimumWidth ()
    {
        return SizeMethodsImpl.getMinimumWidth ( this );
    }

    @Override
    public WebTristateCheckBox setMinimumWidth ( final int minimumWidth )
    {
        return SizeMethodsImpl.setMinimumWidth ( this, minimumWidth );
    }

    @Override
    public int getMinimumHeight ()
    {
        return SizeMethodsImpl.getMinimumHeight ( this );
    }

    @Override
    public WebTristateCheckBox setMinimumHeight ( final int minimumHeight )
    {
        return SizeMethodsImpl.setMinimumHeight ( this, minimumHeight );
    }

    @Override
    public int getMaximumWidth ()
    {
        return SizeMethodsImpl.getMaximumWidth ( this );
    }

    @Override
    public WebTristateCheckBox setMaximumWidth ( final int maximumWidth )
    {
        return SizeMethodsImpl.setMaximumWidth ( this, maximumWidth );
    }

    @Override
    public int getMaximumHeight ()
    {
        return SizeMethodsImpl.getMaximumHeight ( this );
    }

    @Override
    public WebTristateCheckBox setMaximumHeight ( final int maximumHeight )
    {
        return SizeMethodsImpl.setMaximumHeight ( this, maximumHeight );
    }

    @Override
    public Dimension getPreferredSize ()
    {
        return SizeMethodsImpl.getPreferredSize ( this, super.getPreferredSize () );
    }

    @Override
    public Dimension getOriginalPreferredSize ()
    {
        return SizeMethodsImpl.getOriginalPreferredSize ( this, super.getPreferredSize () );
    }

    @Override
    public WebTristateCheckBox setPreferredSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setPreferredSize ( this, width, height );
    }
}