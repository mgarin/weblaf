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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.laf.checkbox.CheckState;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.language.*;
import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.settings.UISettingsManager;
import com.alee.managers.style.*;
import com.alee.managers.tooltip.ToolTipMethods;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.TooltipWay;
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

/**
 * This {@link JCheckBox} extension class provides additional third selection state - mixed state.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see TristateCheckBoxDescriptor
 * @see WTristateCheckBoxUI
 * @see WebTristateCheckBoxUI
 * @see ITristateCheckBoxPainter
 * @see TristateCheckBoxPainter
 * @see JCheckBox
 */
public class WebTristateCheckBox extends JCheckBox
        implements Styleable, Paintable, ShapeMethods, MarginMethods, PaddingMethods, EventMethods, ToolTipMethods, LanguageMethods,
        LanguageEventMethods, SettingsMethods, FontMethods<WebTristateCheckBox>, SizeMethods<WebTristateCheckBox>
{
    /**
     * Constructs new tristate checkbox.
     */
    public WebTristateCheckBox ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param checked whether checkbox should be checked or not
     */
    public WebTristateCheckBox ( final boolean checked )
    {
        this ( StyleId.auto, checked );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param state initial check state
     */
    public WebTristateCheckBox ( final CheckState state )
    {
        this ( StyleId.auto, state );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param icon checkbox icon
     */
    public WebTristateCheckBox ( final Icon icon )
    {
        this ( StyleId.auto, icon );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param icon    checkbox icon
     * @param checked whether checkbox should be checked or not
     */
    public WebTristateCheckBox ( final Icon icon, final boolean checked )
    {
        this ( StyleId.auto, icon, checked );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param icon  checkbox icon
     * @param state initial check state
     */
    public WebTristateCheckBox ( final Icon icon, final CheckState state )
    {
        this ( StyleId.auto, icon, state );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param action checkbox action
     */
    public WebTristateCheckBox ( final Action action )
    {
        this ( StyleId.auto, action );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param text checkbox text
     */
    public WebTristateCheckBox ( final String text )
    {
        this ( StyleId.auto, text );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param text    checkbox text
     * @param checked whether checkbox should be checked or not
     */
    public WebTristateCheckBox ( final String text, final boolean checked )
    {
        this ( StyleId.auto, text, checked );
    }


    /**
     * Constructs new tristate checkbox.
     *
     * @param text  checkbox text
     * @param state initial check state
     */
    public WebTristateCheckBox ( final String text, final CheckState state )
    {
        this ( StyleId.auto, text, state );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param text checkbox text
     * @param icon checkbox icon
     */
    public WebTristateCheckBox ( final String text, final Icon icon )
    {
        this ( StyleId.auto, text, icon );
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
        this ( StyleId.auto, text, icon, checked );
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
        this ( StyleId.auto, text, icon, state );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param id     style ID
     * @param action checkbox action
     */
    public WebTristateCheckBox ( final StyleId id, final Action action )
    {
        this ( id, "", null, CheckState.unchecked );
        setAction ( action );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param id style ID
     */
    public WebTristateCheckBox ( final StyleId id )
    {
        this ( id, "", null, CheckState.unchecked );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param id      style ID
     * @param checked whether checkbox should be checked or not
     */
    public WebTristateCheckBox ( final StyleId id, final boolean checked )
    {
        this ( id, "", null, checked );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param id    style ID
     * @param state initial check state
     */
    public WebTristateCheckBox ( final StyleId id, final CheckState state )
    {
        this ( id, "", null, state );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param id   style ID
     * @param icon checkbox icon
     */
    public WebTristateCheckBox ( final StyleId id, final Icon icon )
    {
        this ( id, null, icon, CheckState.unchecked );
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
        this ( id, null, icon, checked );
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
        this ( id, null, icon, state );
    }

    /**
     * Constructs new tristate checkbox.
     *
     * @param id   style ID
     * @param text checkbox text
     */
    public WebTristateCheckBox ( final StyleId id, final String text )
    {
        this ( id, text, null, CheckState.unchecked );
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
        this ( id, text, null, checked );
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
        this ( id, text, null, state );
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
        this ( id, text, icon, CheckState.unchecked );
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
        this ( id, text, icon, checked ? CheckState.checked : CheckState.unchecked );
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
        super.init ( UILanguageManager.getInitialText ( text ), icon );
        UILanguageManager.registerInitialLanguage ( this, text );
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.tristatecheckbox;
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

    @NotNull
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
    public Painter getCustomPainter ()
    {
        return StyleManager.getCustomPainter ( this );
    }

    @Override
    public Painter setCustomPainter ( final Painter painter )
    {
        return StyleManager.setCustomPainter ( this, painter );
    }

    @Override
    public boolean resetCustomPainter ()
    {
        return StyleManager.resetCustomPainter ( this );
    }

    @NotNull
    @Override
    public Shape getShape ()
    {
        return ShapeMethodsImpl.getShape ( this );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return ShapeMethodsImpl.isShapeDetectionEnabled ( this );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        ShapeMethodsImpl.setShapeDetectionEnabled ( this, enabled );
    }

    @Nullable
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
    public void setMargin ( @Nullable final Insets margin )
    {
        MarginMethodsImpl.setMargin ( this, margin );
    }

    @Nullable
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
    public void setPadding ( @Nullable final Insets padding )
    {
        PaddingMethodsImpl.setPadding ( this, padding );
    }

    @NotNull
    @Override
    public MouseAdapter onMousePress ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMousePress ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMousePress ( @Nullable final MouseButton mouseButton, @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMousePress ( this, mouseButton, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseEnter ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseEnter ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseExit ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseExit ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseDrag ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseDrag ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseDrag ( @Nullable final MouseButton mouseButton, @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseDrag ( this, mouseButton, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseClick ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseClick ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseClick ( @Nullable final MouseButton mouseButton, @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseClick ( this, mouseButton, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onDoubleClick ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onDoubleClick ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMenuTrigger ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMenuTrigger ( this, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyType ( @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyType ( this, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyType ( @Nullable final HotkeyData hotkey, @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyType ( this, hotkey, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyPress ( @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyPress ( this, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyPress ( @Nullable final HotkeyData hotkey, @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyPress ( this, hotkey, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyRelease ( @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyRelease ( this, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyRelease ( @Nullable final HotkeyData hotkey, @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyRelease ( this, hotkey, runnable );
    }

    @NotNull
    @Override
    public FocusAdapter onFocusGain ( @NotNull final FocusEventRunnable runnable )
    {
        return EventMethodsImpl.onFocusGain ( this, runnable );
    }

    @NotNull
    @Override
    public FocusAdapter onFocusLoss ( @NotNull final FocusEventRunnable runnable )
    {
        return EventMethodsImpl.onFocusLoss ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onDragStart ( final int shift, @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onDragStart ( this, shift, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onDragStart ( final int shift, @Nullable final MouseButton mouseButton, @NotNull final MouseEventRunnable runnable )
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

    @Nullable
    @Override
    public String getLanguage ()
    {
        return UILanguageManager.getComponentKey ( this );
    }

    @Override
    public void setLanguage ( @NotNull final String key, @Nullable final Object... data )
    {
        UILanguageManager.registerComponent ( this, key, data );
    }

    @Override
    public void updateLanguage ( @Nullable final Object... data )
    {
        UILanguageManager.updateComponent ( this, data );
    }

    @Override
    public void updateLanguage ( @NotNull final String key, @Nullable final Object... data )
    {
        UILanguageManager.updateComponent ( this, key, data );
    }

    @Override
    public void removeLanguage ()
    {
        UILanguageManager.unregisterComponent ( this );
    }

    @Override
    public boolean isLanguageSet ()
    {
        return UILanguageManager.isRegisteredComponent ( this );
    }

    @Override
    public void setLanguageUpdater ( @NotNull final LanguageUpdater updater )
    {
        UILanguageManager.registerLanguageUpdater ( this, updater );
    }

    @Override
    public void removeLanguageUpdater ()
    {
        UILanguageManager.unregisterLanguageUpdater ( this );
    }

    @Override
    public void addLanguageListener ( @NotNull final LanguageListener listener )
    {
        UILanguageManager.addLanguageListener ( this, listener );
    }

    @Override
    public void removeLanguageListener ( @NotNull final LanguageListener listener )
    {
        UILanguageManager.removeLanguageListener ( this, listener );
    }

    @Override
    public void removeLanguageListeners ()
    {
        UILanguageManager.removeLanguageListeners ( this );
    }

    @Override
    public void addDictionaryListener ( @NotNull final DictionaryListener listener )
    {
        UILanguageManager.addDictionaryListener ( this, listener );
    }

    @Override
    public void removeDictionaryListener ( @NotNull final DictionaryListener listener )
    {
        UILanguageManager.removeDictionaryListener ( this, listener );
    }

    @Override
    public void removeDictionaryListeners ()
    {
        UILanguageManager.removeDictionaryListeners ( this );
    }

    @Override
    public void registerSettings ( final Configuration configuration )
    {
        UISettingsManager.registerComponent ( this, configuration );
    }

    @Override
    public void registerSettings ( final SettingsProcessor processor )
    {
        UISettingsManager.registerComponent ( this, processor );
    }

    @Override
    public void unregisterSettings ()
    {
        UISettingsManager.unregisterComponent ( this );
    }

    @Override
    public void loadSettings ()
    {
        UISettingsManager.loadSettings ( this );
    }

    @Override
    public void saveSettings ()
    {
        UISettingsManager.saveSettings ( this );
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

    @NotNull
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

    @NotNull
    @Override
    public WebTristateCheckBox setPreferredHeight ( final int preferredHeight )
    {
        return SizeMethodsImpl.setPreferredHeight ( this, preferredHeight );
    }

    @NotNull
    @Override
    public Dimension getPreferredSize ()
    {
        return SizeMethodsImpl.getPreferredSize ( this, super.getPreferredSize () );
    }

    @NotNull
    @Override
    public Dimension getOriginalPreferredSize ()
    {
        return SizeMethodsImpl.getOriginalPreferredSize ( this, super.getPreferredSize () );
    }

    @NotNull
    @Override
    public WebTristateCheckBox setPreferredSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setPreferredSize ( this, width, height );
    }

    @Override
    public int getMaximumWidth ()
    {
        return SizeMethodsImpl.getMaximumWidth ( this );
    }

    @NotNull
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

    @NotNull
    @Override
    public WebTristateCheckBox setMaximumHeight ( final int maximumHeight )
    {
        return SizeMethodsImpl.setMaximumHeight ( this, maximumHeight );
    }

    @NotNull
    @Override
    public Dimension getMaximumSize ()
    {
        return SizeMethodsImpl.getMaximumSize ( this, super.getMaximumSize () );
    }

    @NotNull
    @Override
    public Dimension getOriginalMaximumSize ()
    {
        return SizeMethodsImpl.getOriginalMaximumSize ( this, super.getMaximumSize () );
    }

    @NotNull
    @Override
    public WebTristateCheckBox setMaximumSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setMaximumSize ( this, width, height );
    }

    @Override
    public int getMinimumWidth ()
    {
        return SizeMethodsImpl.getMinimumWidth ( this );
    }

    @NotNull
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

    @NotNull
    @Override
    public WebTristateCheckBox setMinimumHeight ( final int minimumHeight )
    {
        return SizeMethodsImpl.setMinimumHeight ( this, minimumHeight );
    }

    @NotNull
    @Override
    public Dimension getMinimumSize ()
    {
        return SizeMethodsImpl.getMinimumSize ( this, super.getMinimumSize () );
    }

    @NotNull
    @Override
    public Dimension getOriginalMinimumSize ()
    {
        return SizeMethodsImpl.getOriginalMinimumSize ( this, super.getMinimumSize () );
    }

    @NotNull
    @Override
    public WebTristateCheckBox setMinimumSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setMinimumSize ( this, width, height );
    }

    /**
     * Returns the look and feel (LaF) object that renders this component.
     *
     * @return the {@link WTristateCheckBoxUI} object that renders this component
     */
    @Override
    public WTristateCheckBoxUI getUI ()
    {
        return ( WTristateCheckBoxUI ) ui;
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WTristateCheckBoxUI}
     */
    public void setUI ( final WTristateCheckBoxUI ui )
    {
        super.setUI ( ui );
    }

    @Override
    public void updateUI ()
    {
        StyleManager.getDescriptor ( this ).updateUI ( this );
    }

    @Override
    public String getUIClassID ()
    {
        return StyleManager.getDescriptor ( this ).getUIClassId ();
    }
}