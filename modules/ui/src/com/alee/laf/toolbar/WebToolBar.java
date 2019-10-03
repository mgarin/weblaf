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

package com.alee.laf.toolbar;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.BiConsumer;
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
 * {@link JToolBar} extension class.
 * It contains various useful methods to simplify core component usage.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see JToolBar
 * @see WebToolBarUI
 * @see ToolBarPainter
 */
public class WebToolBar extends JToolBar implements Styleable, Paintable, ShapeMethods, MarginMethods, PaddingMethods,
        ContainerMethods<WebToolBar>, EventMethods, ToolTipMethods, LanguageMethods, LanguageEventMethods, SettingsMethods,
        FontMethods<WebToolBar>, SizeMethods<WebToolBar>
{
    /**
     * Component properties.
     */
    public static final String FLOATABLE_PROPERTY = "floatable";

    /**
     * Constructs new toolbar.
     */
    public WebToolBar ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs new toolbar.
     *
     * @param orientation toolbar orientation
     */
    public WebToolBar ( final int orientation )
    {
        this ( StyleId.auto, orientation );
    }

    /**
     * Constructs new toolbar.
     *
     * @param name toolbar name
     */
    public WebToolBar ( final String name )
    {
        this ( StyleId.auto, name );
    }

    /**
     * Constructs new toolbar.
     *
     * @param name        toolbar name
     * @param orientation toolbar orientation
     */
    public WebToolBar ( final String name, final int orientation )
    {
        this ( StyleId.auto, name, orientation );
    }

    /**
     * Constructs new toolbar.
     *
     * @param id style ID
     */
    public WebToolBar ( final StyleId id )
    {
        this ( id, null, HORIZONTAL );
    }

    /**
     * Constructs new toolbar.
     *
     * @param id          style ID
     * @param orientation toolbar orientation
     */
    public WebToolBar ( final StyleId id, final int orientation )
    {
        this ( id, null, orientation );
    }

    /**
     * Constructs new toolbar.
     *
     * @param id   style ID
     * @param name toolbar name
     */
    public WebToolBar ( final StyleId id, final String name )
    {
        this ( id, name, HORIZONTAL );
    }

    /**
     * Constructs new toolbar.
     *
     * @param id          style ID
     * @param name        toolbar name
     * @param orientation toolbar orientation
     */
    public WebToolBar ( final StyleId id, final String name, final int orientation )
    {
        super ( name, orientation );
        setStyleId ( id );
    }

    /**
     * Adds specified {@link Component} under {@link ToolbarLayout#MIDDLE} constraints.
     *
     * @param component {@link Component} to add
     */
    public void addToMiddle ( final Component component )
    {
        add ( component, ToolbarLayout.MIDDLE );
    }

    /**
     * Adds specified {@link Component} under {@link ToolbarLayout#FILL} constraints.
     *
     * @param component {@link Component} to add
     */
    public void addFill ( final Component component )
    {
        add ( component, ToolbarLayout.FILL );
    }

    /**
     * Adds specified {@link Component} under {@link ToolbarLayout#END} constraints.
     *
     * @param component {@link Component} to add
     */
    public void addToEnd ( final Component component )
    {
        add ( component, ToolbarLayout.END );
    }

    /**
     * Adds new {@link WebToolBarSeparator} under {@link ToolbarLayout#START} constraints.
     */
    @Override
    public void addSeparator ()
    {
        addSeparator ( ToolbarLayout.START );
    }

    /**
     * Adds new {@link WebToolBarSeparator} under {@link ToolbarLayout#START} constraints with the specified preferred size.
     *
     * @param size preferred size
     */
    @Override
    public void addSeparator ( @Nullable final Dimension size )
    {
        addSeparator ( ToolbarLayout.START ).setPreferredSize ( size );
    }

    /**
     * Adds new {@link WebToolBarSeparator} under {@link ToolbarLayout#MIDDLE} constraints.
     *
     * @return added {@link WebToolBarSeparator}
     */
    public WebToolBarSeparator addSeparatorToMiddle ()
    {
        return addSeparator ( ToolbarLayout.MIDDLE );
    }

    /**
     * Adds new {@link WebToolBarSeparator} under {@link ToolbarLayout#END} constraints.
     *
     * @return added {@link WebToolBarSeparator}
     */
    public WebToolBarSeparator addSeparatorToEnd ()
    {
        return addSeparator ( ToolbarLayout.END );
    }

    /**
     * Adds new {@link WebToolBarSeparator} under specified constraints.
     *
     * @param constraints constraints for {@link WebToolBarSeparator}
     * @return added {@link WebToolBarSeparator}
     */
    public WebToolBarSeparator addSeparator ( final String constraints )
    {
        return addSeparator ( constraints, StyleId.toolbarseparator );
    }

    /**
     * Adds new {@link WebToolBarSeparator} with the specified {@link StyleId} under {@link ToolbarLayout#START} constraints.
     *
     * @param id {@link StyleId} for {@link WebToolBarSeparator}
     * @return added {@link WebToolBarSeparator}
     */
    public WebToolBarSeparator addSeparator ( final StyleId id )
    {
        return addSeparator ( ToolbarLayout.START, id );
    }

    /**
     * Adds new {@link WebToolBarSeparator} with the specified {@link StyleId} under {@link ToolbarLayout#END} constraints.
     *
     * @param id {@link StyleId} for {@link WebToolBarSeparator}
     * @return added {@link WebToolBarSeparator}
     */
    public WebToolBarSeparator addSeparatorToEnd ( final StyleId id )
    {
        return addSeparator ( ToolbarLayout.END, id );
    }

    /**
     * Adds new {@link WebToolBarSeparator} with the specified {@link StyleId} under the specified constraints.
     *
     * @param constraints constraints for {@link WebToolBarSeparator}
     * @param id          {@link StyleId} for {@link WebToolBarSeparator}
     * @return added {@link WebToolBarSeparator}
     */
    public WebToolBarSeparator addSeparator ( final String constraints, final StyleId id )
    {
        final WebToolBarSeparator separator = new WebToolBarSeparator ( id );
        add ( separator, constraints );
        return separator;
    }

    /**
     * Adds {@link List} of {@link Component}s under the specified constraints.
     *
     * @param components  {@link List} of {@link Component}s to add
     * @param constraints constraints to add {@link Component}s under
     */
    public void add ( final List<? extends Component> components, final String constraints )
    {
        if ( components != null )
        {
            for ( final Component component : components )
            {
                add ( component, constraints );
            }
        }
    }

    /**
     * Adds {@link Component}s at the specified Z-index.
     *
     * @param index      Z-index to add {@link Component}s at
     * @param components {@link Component}s to add
     */
    public void add ( final int index, final Component... components )
    {
        if ( components != null && components.length > 0 )
        {
            for ( int i = 0; i < components.length; i++ )
            {
                add ( components[ i ], index + i );
            }
        }
    }

    /**
     * Adds {@link Component}s under the specified constraints.
     *
     * @param constraints constraints to add {@link Component}s under
     * @param components  {@link Component}s to add
     */
    public void add ( final String constraints, final Component... components )
    {
        if ( components != null && components.length > 0 )
        {
            for ( final Component component : components )
            {
                add ( component, constraints );
            }
        }
    }

    /**
     * Adds spacing between components.
     */
    public void addSpacing ()
    {
        addSpacing ( 2 );
    }

    /**
     * Adds spacing between components.
     *
     * @param spacing spacing size
     */
    public void addSpacing ( final int spacing )
    {
        addSpacing ( spacing, ToolbarLayout.START );
    }

    /**
     * Adds spacing between components at the end.
     */
    public void addSpacingToEnd ()
    {
        addSpacingToEnd ( 2 );
    }

    /**
     * Adds spacing between components at the end.
     *
     * @param spacing spacing size
     */
    public void addSpacingToEnd ( final int spacing )
    {
        addSpacing ( spacing, ToolbarLayout.END );
    }

    /**
     * Adds spacing between components at the specified constraints.
     *
     * @param spacing     spacing size
     * @param constraints layout constraints
     */
    public void addSpacing ( final int spacing, final String constraints )
    {
        // todo Add layout implementation instead of wasted component
        add ( new WhiteSpace ( spacing ), constraints );
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.toolbar;
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

    @Override
    public boolean contains ( final Component component )
    {
        return ContainerMethodsImpl.contains ( this, component );
    }

    @Override
    public WebToolBar add ( final List<? extends Component> components )
    {
        return ContainerMethodsImpl.add ( this, components );
    }

    @Override
    public WebToolBar add ( final List<? extends Component> components, final int index )
    {
        return ContainerMethodsImpl.add ( this, components, index );
    }

    @Override
    public WebToolBar add ( final List<? extends Component> components, final Object constraints )
    {
        return ContainerMethodsImpl.add ( this, components, constraints );
    }

    @Override
    public WebToolBar add ( final Component component1, final Component component2 )
    {
        return ContainerMethodsImpl.add ( this, component1, component2 );
    }

    @Override
    public WebToolBar add ( final Component... components )
    {
        return ContainerMethodsImpl.add ( this, components );
    }

    @Override
    public WebToolBar remove ( final List<? extends Component> components )
    {
        return ContainerMethodsImpl.remove ( this, components );
    }

    @Override
    public WebToolBar remove ( final Component... components )
    {
        return ContainerMethodsImpl.remove ( this, components );
    }

    @Override
    public WebToolBar removeAll ( final Class<? extends Component> componentClass )
    {
        return ContainerMethodsImpl.removeAll ( this, componentClass );
    }

    @Override
    public Component getFirstComponent ()
    {
        return ContainerMethodsImpl.getFirstComponent ( this );
    }

    @Override
    public Component getLastComponent ()
    {
        return ContainerMethodsImpl.getLastComponent ( this );
    }

    @Override
    public WebToolBar equalizeComponentsWidth ()
    {
        return ContainerMethodsImpl.equalizeComponentsWidth ( this );
    }

    @Override
    public WebToolBar equalizeComponentsHeight ()
    {
        return ContainerMethodsImpl.equalizeComponentsHeight ( this );
    }

    @Override
    public WebToolBar equalizeComponentsSize ()
    {
        return ContainerMethodsImpl.equalizeComponentsSize ( this );
    }

    @Override
    public <T extends Component> WebToolBar forEach ( final BiConsumer<Integer, T> consumer )
    {
        return ContainerMethodsImpl.forEach ( this, consumer );
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
    public WebToolBar setPlainFont ()
    {
        return FontMethodsImpl.setPlainFont ( this );
    }

    @Override
    public WebToolBar setPlainFont ( final boolean apply )
    {
        return FontMethodsImpl.setPlainFont ( this, apply );
    }

    @Override
    public boolean isPlainFont ()
    {
        return FontMethodsImpl.isPlainFont ( this );
    }

    @Override
    public WebToolBar setBoldFont ()
    {
        return FontMethodsImpl.setBoldFont ( this );
    }

    @Override
    public WebToolBar setBoldFont ( final boolean apply )
    {
        return FontMethodsImpl.setBoldFont ( this, apply );
    }

    @Override
    public boolean isBoldFont ()
    {
        return FontMethodsImpl.isBoldFont ( this );
    }

    @Override
    public WebToolBar setItalicFont ()
    {
        return FontMethodsImpl.setItalicFont ( this );
    }

    @Override
    public WebToolBar setItalicFont ( final boolean apply )
    {
        return FontMethodsImpl.setItalicFont ( this, apply );
    }

    @Override
    public boolean isItalicFont ()
    {
        return FontMethodsImpl.isItalicFont ( this );
    }

    @Override
    public WebToolBar setFontStyle ( final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontStyle ( this, bold, italic );
    }

    @Override
    public WebToolBar setFontStyle ( final int style )
    {
        return FontMethodsImpl.setFontStyle ( this, style );
    }

    @Override
    public WebToolBar setFontSize ( final int fontSize )
    {
        return FontMethodsImpl.setFontSize ( this, fontSize );
    }

    @Override
    public WebToolBar changeFontSize ( final int change )
    {
        return FontMethodsImpl.changeFontSize ( this, change );
    }

    @Override
    public int getFontSize ()
    {
        return FontMethodsImpl.getFontSize ( this );
    }

    @Override
    public WebToolBar setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    @Override
    public WebToolBar setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, style );
    }

    @Override
    public WebToolBar setFontName ( final String fontName )
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
    public WebToolBar setPreferredWidth ( final int preferredWidth )
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
    public WebToolBar setPreferredHeight ( final int preferredHeight )
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
    public WebToolBar setPreferredSize ( final int width, final int height )
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
    public WebToolBar setMaximumWidth ( final int maximumWidth )
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
    public WebToolBar setMaximumHeight ( final int maximumHeight )
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
    public WebToolBar setMaximumSize ( final int width, final int height )
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
    public WebToolBar setMinimumWidth ( final int minimumWidth )
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
    public WebToolBar setMinimumHeight ( final int minimumHeight )
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
    public WebToolBar setMinimumSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setMinimumSize ( this, width, height );
    }

    /**
     * Returns the look and feel (LaF) object that renders this component.
     *
     * @return the {@link WebToolBarUI} object that renders this component
     */
    @Override
    public WebToolBarUI getUI ()
    {
        return ( WebToolBarUI ) super.getUI ();
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WebToolBarUI}
     */
    public void setUI ( final WebToolBarUI ui )
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