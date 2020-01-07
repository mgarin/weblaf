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

package com.alee.laf.tabbedpane;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.language.*;
import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.settings.UISettingsManager;
import com.alee.managers.style.*;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.swing.ClientProperty;
import com.alee.utils.swing.MouseButton;
import com.alee.utils.swing.NullableClientProperty;
import com.alee.utils.swing.extensions.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;

/**
 * {@link JTabbedPane} extension class.
 * It contains various useful methods to simplify core component usage.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see TabbedPaneDescriptor
 * @see WTabbedPaneUI
 * @see WebTabbedPaneUI
 * @see ITabbedPanePainter
 * @see TabbedPanePainter
 * @see JTabbedPane
 * @see TabArea
 * @see TabViewport
 * @see TabContainer
 * @see Tab
 * @see TabMenuButton
 * @see TabMenuItem
 */
public class WebTabbedPane extends JTabbedPane implements Styleable, EventMethods, LanguageMethods, LanguageEventMethods, SettingsMethods,
        FontMethods<WebTabbedPane>, SizeMethods<WebTabbedPane>
{
    /**
     * Component properties.
     */
    public static final String TAB_LAYOUT_POLICY_PROPERTY = "tabLayoutPolicy";
    public static final String TAB_PLACEMENT_PROPERTY = "tabPlacement";
    public static final String INDEX_FOR_TITLE_PROPERTY = "indexForTitle";
    public static final String INDEX_FOR_TAB_COMPONENT_PROPERTY = "indexForTabComponent";
    public static final String INDEX_FOR_NULL_COMPONENT_PROPERTY = "indexForNullComponent";
    public static final String FOREGROUND_AT_PROPERTY = "foregroundAt";
    public static final String BACKGROUND_AT_PROPERTY = "backgroundAt";
    public static final String ENABLED_AT_PROPERTY = "enabledAt";
    public static final String ICON_AT_PROPERTY = "iconAt";
    public static final String DISABLED_ICON_AT_PROPERTY = "disabledIconAt";
    public static final String MNEMONIC_AT_PROPERTY = "mnemonicAt";
    public static final String DISPLAYED_MNEMONIC_INDEX_AT_PROPERTY = "displayedMnemonicIndexAt";
    public static final String STYLE_ID_AT_PROPERTY = "styleIdAt";
    public static final String CHILD_STYLE_ID_AT_PROPERTY = "childStyleIdAt";

    /**
     * Workaround Swing property for removed tab.
     */
    public static final String REMOVED_TAB_INDEX = "__index_to_remove__";

    /**
     * Whether or not tab area should be hidden when only one tab is opened.
     * This is a custom client property object used for backward compatibility with Swing {@link JTabbedPane}.
     */
    public static final ClientProperty<Boolean> HIDE_SINGLE_TAB_PROPERTY =
            new ClientProperty<Boolean> ( "hideSingleTab", false );

    /**
     * {@link TabbedPaneToolTipProvider} used by this {@link WebTabbedPane}.
     * This is a custom client property object used for backward compatibility with Swing {@link JTabbedPane}.
     *
     * @see TabbedPaneToolTipProvider
     */
    public static final NullableClientProperty<TabbedPaneToolTipProvider> TOOLTIP_PROVIDER_PROPERTY =
            new NullableClientProperty<TabbedPaneToolTipProvider> ( "tooltipProvider", null );

    /**
     * Leading {@link JComponent} for {@link TabArea}.
     * This is a custom client property object used for backward compatibility with Swing {@link JTabbedPane}.
     */
    public static final NullableClientProperty<JComponent> LEADING_TAB_AREA_COMPONENT_PROPERTY =
            new NullableClientProperty<JComponent> ( "leadingTabAreaComponent", null );

    /**
     * Trailing {@link JComponent} for {@link TabArea}.
     * This is a custom client property object used for backward compatibility with Swing {@link JTabbedPane}.
     */
    public static final NullableClientProperty<JComponent> TRAILING_TAB_AREA_COMPONENT_PROPERTY =
            new NullableClientProperty<JComponent> ( "trailingTabAreaComponent", null );

    /**
     * Constructs new tabbed pane.
     */
    public WebTabbedPane ()
    {
        this ( StyleId.auto, TOP, WRAP_TAB_LAYOUT );
    }

    /**
     * Constructs new tabbed pane.
     *
     * @param tabPlacement the placement for the tabs relative to the content
     */
    public WebTabbedPane ( final int tabPlacement )
    {
        this ( StyleId.auto, tabPlacement, WRAP_TAB_LAYOUT );
    }

    /**
     * Constructs new tabbed pane.
     *
     * @param tabPlacement    the placement for the tabs relative to the content
     * @param tabLayoutPolicy the policy for laying out tabs when all tabs will not fit on one run
     */
    public WebTabbedPane ( final int tabPlacement, final int tabLayoutPolicy )
    {
        this ( StyleId.auto, tabPlacement, tabLayoutPolicy );
    }

    /**
     * Constructs new tabbed pane.
     *
     * @param id {@link StyleId}
     */
    public WebTabbedPane ( @NotNull final StyleId id )
    {
        this ( id, TOP, WRAP_TAB_LAYOUT );
    }

    /**
     * Constructs new tabbed pane.
     *
     * @param id           {@link StyleId}
     * @param tabPlacement the placement for the tabs relative to the content
     */
    public WebTabbedPane ( @NotNull final StyleId id, final int tabPlacement )
    {
        this ( id, tabPlacement, WRAP_TAB_LAYOUT );
    }

    /**
     * Constructs new tabbed pane.
     *
     * @param id              {@link StyleId}
     * @param tabPlacement    the placement for the tabs relative to the content
     * @param tabLayoutPolicy the policy for laying out tabs when all tabs will not fit on one run
     */
    public WebTabbedPane ( @NotNull final StyleId id, final int tabPlacement, final int tabLayoutPolicy )
    {
        super ( tabPlacement, tabLayoutPolicy );
        setStyleId ( id );
    }

    /**
     * Returns {@link Tab} at the specified index.
     *
     * @param index {@link Tab} index
     * @return {@link Tab} at the specified index
     */
    @NotNull
    public Tab getTab ( final int index )
    {
        return getUI ().getTab ( index );
    }

    /**
     * Workaround for no event being fired in {@link JTabbedPane}.
     *
     * @see TabbedPaneLayout#invalidateLayout(Container)
     */
    @Override
    public void setForegroundAt ( final int index, @Nullable final Color foreground )
    {
        if ( getForegroundAt ( index ) != foreground )
        {
            super.setForegroundAt ( index, foreground );
            firePropertyChange ( FOREGROUND_AT_PROPERTY, null, index );
        }
    }

    /**
     * Workaround for no event being fired in {@link JTabbedPane}.
     *
     * @see TabbedPaneLayout#invalidateLayout(Container)
     */
    @Override
    public void setBackgroundAt ( final int index, @Nullable final Color background )
    {
        if ( getBackgroundAt ( index ) != background )
        {
            super.setBackgroundAt ( index, background );
            firePropertyChange ( BACKGROUND_AT_PROPERTY, null, index );
        }
    }

    /**
     * Workaround for no event being fired in {@link JTabbedPane}.
     *
     * @see TabbedPaneLayout#invalidateLayout(Container)
     */
    @Override
    public void setEnabledAt ( final int index, final boolean enabled )
    {
        if ( isEnabledAt ( index ) != enabled )
        {
            super.setEnabledAt ( index, enabled );
            firePropertyChange ( ENABLED_AT_PROPERTY, null, index );
        }
    }

    /**
     * Workaround for no event being fired in {@link JTabbedPane}.
     *
     * @see TabbedPaneLayout#invalidateLayout(Container)
     */
    @Override
    public void setIconAt ( final int index, @Nullable final Icon icon )
    {
        if ( getIconAt ( index ) != icon )
        {
            super.setIconAt ( index, icon );
            firePropertyChange ( ICON_AT_PROPERTY, null, index );
        }
    }

    /**
     * Workaround for no event being fired in {@link JTabbedPane}.
     *
     * @see TabbedPaneLayout#invalidateLayout(Container)
     */
    @Override
    public void setDisabledIconAt ( final int index, @Nullable final Icon disabledIcon )
    {
        if ( getDisabledIconAt ( index ) != disabledIcon )
        {
            super.setDisabledIconAt ( index, disabledIcon );
            firePropertyChange ( DISABLED_ICON_AT_PROPERTY, null, index );
        }
    }

    /**
     * Returns {@link StyleId} of the {@link Tab} component at the specified index.
     *
     * @param index {@link Tab} index
     * @return {@link StyleId} of the {@link Tab} component at the specified index
     */
    @NotNull
    public StyleId getStyleIdAt ( final int index )
    {
        return getUI ().getStyleIdAt ( index );
    }

    /**
     * Changes {@link StyleId} for {@link Tab} component at the specified index.
     * Use this method to provide a standalone {@link StyleId} for any particular {@link Tab}.
     *
     * @param index   {@link Tab} index
     * @param styleId new {@link StyleId}
     */
    public void setStyleIdAt ( final int index, @NotNull final StyleId styleId )
    {
        firePropertyChange ( STYLE_ID_AT_PROPERTY, null, new Object[]{ index, styleId } );
    }

    /**
     * Changes {@link StyleId} for {@link Tab} component at the specified index.
     * Use this method to provide a {@link StyleId} with {@link TabContainer} as parent for any particular {@link Tab}.
     * Resulting {@link StyleId} will be retrieved through {@link ChildStyleId#at(JComponent)} with {@link TabContainer} provided.
     *
     * @param index   {@link Tab} index
     * @param styleId new {@link ChildStyleId}, {@link TabContainer} will become it's parent
     */
    public void setStyleIdAt ( final int index, @NotNull final ChildStyleId styleId )
    {
        firePropertyChange ( CHILD_STYLE_ID_AT_PROPERTY, null, new Object[]{ index, styleId } );
    }

    /**
     * Returns tab index for the specified location or -1 if there is no tab there.
     *
     * @param point location
     * @return tab index for the specified location or -1 if there is no tab there
     */
    public int getTabAt ( @NotNull final Point point )
    {
        return getTabAt ( point.x, point.y );
    }

    /**
     * Returns tab index for the specified location or -1 if there is no tab there.
     *
     * @param x location X
     * @param y location Y
     * @return tab index for the specified location or -1 if there is no tab there
     */
    public int getTabAt ( final int x, final int y )
    {
        return indexAtLocation ( x, y );
    }

    /**
     * Returns tab bounds for the specified location or null if there is no tab there.
     *
     * @param point location
     * @return tab bounds for the specified location or null if there is no tab there
     */
    @Nullable
    public Rectangle getBoundsAt ( @NotNull final Point point )
    {
        return getBoundsAt ( point.x, point.y );
    }

    /**
     * Returns tab bounds for the specified location or null if there is no tab there.
     *
     * @param x location X
     * @param y location Y
     * @return tab bounds for the specified location or null if there is no tab there
     */
    @Nullable
    public Rectangle getBoundsAt ( final int x, final int y )
    {
        final int index = getTabAt ( x, y );
        return index != -1 ? getBoundsAt ( index ) : null;
    }

    /**
     * Returns whether or not tab area should be hidden when only one tab is opened.
     *
     * @return {@code true} if tab area should be hidden when only one tab is opened, {@code false} otherwise
     */
    public boolean isHideSingleTab ()
    {
        return HIDE_SINGLE_TAB_PROPERTY.get ( this );
    }

    /**
     * Sets whether or not tab area should be hidden when only one tab is opened.
     *
     * @param hide whether or not tab area should be hidden when only one tab is opened
     */
    public void setHideSingleTab ( final boolean hide )
    {
        HIDE_SINGLE_TAB_PROPERTY.set ( this, hide );
    }

    /**
     * Returns {@link TabbedPaneToolTipProvider} used by this {@link WebTabbedPane}.
     *
     * @return {@link TabbedPaneToolTipProvider} used by this {@link WebTabbedPane}
     */
    @Nullable
    public TabbedPaneToolTipProvider getToolTipProvider ()
    {
        return TOOLTIP_PROVIDER_PROPERTY.get ( this );
    }

    /**
     * Sets {@link TabbedPaneToolTipProvider} for this {@link WebTabbedPane}.
     *
     * @param provider {@link TabbedPaneToolTipProvider}
     */
    public void setToolTipProvider ( @Nullable final TabbedPaneToolTipProvider provider )
    {
        TOOLTIP_PROVIDER_PROPERTY.set ( this, provider );
    }

    /**
     * Returns leading {@link JComponent} for {@link TabArea}.
     *
     * @return leading {@link JComponent} for {@link TabArea}
     */
    @Nullable
    public JComponent getLeadingTabAreaComponent ()
    {
        return LEADING_TAB_AREA_COMPONENT_PROPERTY.get ( this );
    }

    /**
     * Sets leading {@link JComponent} for {@link TabArea}.
     *
     * @param component new leading {@link JComponent} for {@link TabArea}
     * @return previous leading {@link JComponent} for {@link TabArea}
     */
    @Nullable
    public JComponent setLeadingTabAreaComponent ( @Nullable final JComponent component )
    {
        final JComponent old = LEADING_TAB_AREA_COMPONENT_PROPERTY.get ( this );
        LEADING_TAB_AREA_COMPONENT_PROPERTY.set ( this, component );
        return old;
    }

    /**
     * Returns trailing {@link JComponent} for {@link TabArea}.
     *
     * @return trailing {@link JComponent} for {@link TabArea}
     */
    @Nullable
    public JComponent getTrailingTabAreaComponent ()
    {
        return TRAILING_TAB_AREA_COMPONENT_PROPERTY.get ( this );
    }

    /**
     * Sets trailing {@link JComponent} for {@link TabArea}.
     *
     * @param component new trailing {@link JComponent} for {@link TabArea}
     * @return previous trailing {@link JComponent} for {@link TabArea}
     */
    @Nullable
    public JComponent setTrailingTabAreaComponent ( @Nullable final JComponent component )
    {
        final JComponent old = TRAILING_TAB_AREA_COMPONENT_PROPERTY.get ( this );
        TRAILING_TAB_AREA_COMPONENT_PROPERTY.set ( this, component );
        return old;
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.tabbedpane;
    }

    @NotNull
    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( this );
    }

    @NotNull
    @Override
    public StyleId setStyleId ( @NotNull final StyleId id )
    {
        return StyleManager.setStyleId ( this, id );
    }

    @NotNull
    @Override
    public StyleId resetStyleId ()
    {
        return StyleManager.resetStyleId ( this );
    }

    @NotNull
    @Override
    public Skin getSkin ()
    {
        return StyleManager.getSkin ( this );
    }

    @Nullable
    @Override
    public Skin setSkin ( @NotNull final Skin skin )
    {
        return StyleManager.setSkin ( this, skin );
    }

    @Nullable
    @Override
    public Skin setSkin ( @NotNull final Skin skin, final boolean recursively )
    {
        return StyleManager.setSkin ( this, skin, recursively );
    }

    @Nullable
    @Override
    public Skin resetSkin ()
    {
        return StyleManager.resetSkin ( this );
    }

    @Override
    public void addStyleListener ( @NotNull final StyleListener listener )
    {
        StyleManager.addStyleListener ( this, listener );
    }

    @Override
    public void removeStyleListener ( @NotNull final StyleListener listener )
    {
        StyleManager.removeStyleListener ( this, listener );
    }

    @Nullable
    @Override
    public Painter getCustomPainter ()
    {
        return StyleManager.getCustomPainter ( this );
    }

    @Nullable
    @Override
    public Painter setCustomPainter ( @NotNull final Painter painter )
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
    public Shape getPainterShape ()
    {
        return PainterSupport.getShape ( this );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( this );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( this, enabled );
    }

    @Nullable
    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( this );
    }

    @Override
    public void setMargin ( final int margin )
    {
        PainterSupport.setMargin ( this, margin );
    }

    @Override
    public void setMargin ( final int top, final int left, final int bottom, final int right )
    {
        PainterSupport.setMargin ( this, top, left, bottom, right );
    }

    @Override
    public void setMargin ( @Nullable final Insets margin )
    {
        PainterSupport.setMargin ( this, margin );
    }

    @Nullable
    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( this );
    }

    @Override
    public void setPadding ( final int padding )
    {
        PainterSupport.setPadding ( this, padding );
    }

    @Override
    public void setPadding ( final int top, final int left, final int bottom, final int right )
    {
        PainterSupport.setPadding ( this, top, left, bottom, right );
    }

    @Override
    public void setPadding ( @Nullable final Insets padding )
    {
        PainterSupport.setPadding ( this, padding );
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
        UILanguageManager.addLanguageListener ( getRootPane (), listener );
    }

    @Override
    public void removeLanguageListener ( @NotNull final LanguageListener listener )
    {
        UILanguageManager.removeLanguageListener ( getRootPane (), listener );
    }

    @Override
    public void removeLanguageListeners ()
    {
        UILanguageManager.removeLanguageListeners ( getRootPane () );
    }

    @Override
    public void addDictionaryListener ( @NotNull final DictionaryListener listener )
    {
        UILanguageManager.addDictionaryListener ( getRootPane (), listener );
    }

    @Override
    public void removeDictionaryListener ( @NotNull final DictionaryListener listener )
    {
        UILanguageManager.removeDictionaryListener ( getRootPane (), listener );
    }

    @Override
    public void removeDictionaryListeners ()
    {
        UILanguageManager.removeDictionaryListeners ( getRootPane () );
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
    public WebTabbedPane setPlainFont ()
    {
        return FontMethodsImpl.setPlainFont ( this );
    }

    @Override
    public WebTabbedPane setPlainFont ( final boolean apply )
    {
        return FontMethodsImpl.setPlainFont ( this, apply );
    }

    @Override
    public boolean isPlainFont ()
    {
        return FontMethodsImpl.isPlainFont ( this );
    }

    @Override
    public WebTabbedPane setBoldFont ()
    {
        return FontMethodsImpl.setBoldFont ( this );
    }

    @Override
    public WebTabbedPane setBoldFont ( final boolean apply )
    {
        return FontMethodsImpl.setBoldFont ( this, apply );
    }

    @Override
    public boolean isBoldFont ()
    {
        return FontMethodsImpl.isBoldFont ( this );
    }

    @Override
    public WebTabbedPane setItalicFont ()
    {
        return FontMethodsImpl.setItalicFont ( this );
    }

    @Override
    public WebTabbedPane setItalicFont ( final boolean apply )
    {
        return FontMethodsImpl.setItalicFont ( this, apply );
    }

    @Override
    public boolean isItalicFont ()
    {
        return FontMethodsImpl.isItalicFont ( this );
    }

    @Override
    public WebTabbedPane setFontStyle ( final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontStyle ( this, bold, italic );
    }

    @Override
    public WebTabbedPane setFontStyle ( final int style )
    {
        return FontMethodsImpl.setFontStyle ( this, style );
    }

    @Override
    public WebTabbedPane setFontSize ( final int fontSize )
    {
        return FontMethodsImpl.setFontSize ( this, fontSize );
    }

    @Override
    public WebTabbedPane changeFontSize ( final int change )
    {
        return FontMethodsImpl.changeFontSize ( this, change );
    }

    @Override
    public int getFontSize ()
    {
        return FontMethodsImpl.getFontSize ( this );
    }

    @Override
    public WebTabbedPane setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    @Override
    public WebTabbedPane setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, style );
    }

    @Override
    public WebTabbedPane setFontName ( final String fontName )
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
    public WebTabbedPane setPreferredWidth ( final int preferredWidth )
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
    public WebTabbedPane setPreferredHeight ( final int preferredHeight )
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
    public WebTabbedPane setPreferredSize ( final int width, final int height )
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
    public WebTabbedPane setMaximumWidth ( final int maximumWidth )
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
    public WebTabbedPane setMaximumHeight ( final int maximumHeight )
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
    public WebTabbedPane setMaximumSize ( final int width, final int height )
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
    public WebTabbedPane setMinimumWidth ( final int minimumWidth )
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
    public WebTabbedPane setMinimumHeight ( final int minimumHeight )
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
    public WebTabbedPane setMinimumSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setMinimumSize ( this, width, height );
    }

    /**
     * Returns the look and feel (LaF) object that renders this component.
     *
     * @return the {@link WTabbedPaneUI} object that renders this component
     */
    @Override
    public WTabbedPaneUI getUI ()
    {
        return ( WTabbedPaneUI ) super.getUI ();
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WTabbedPaneUI}
     */
    public void setUI ( final WTabbedPaneUI ui )
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