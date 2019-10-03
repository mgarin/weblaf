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

package com.alee.laf.desktoppane;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.language.*;
import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.settings.UISettingsManager;
import com.alee.managers.style.*;
import com.alee.painter.Paintable;
import com.alee.painter.Painter;
import com.alee.utils.swing.MouseButton;
import com.alee.utils.swing.extensions.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.beans.PropertyVetoException;

/**
 * {@link JInternalFrame} extension class.
 * It contains various useful methods to simplify core component usage.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see InternalFrameDescriptor
 * @see WInternalFrameUI
 * @see WebInternalFrameUI
 * @see IInternalFramePainter
 * @see InternalFramePainter
 * @see JInternalFrame
 */
public class WebInternalFrame extends JInternalFrame implements Styleable, Paintable, ShapeMethods, MarginMethods, PaddingMethods,
        EventMethods, LanguageMethods, LanguageEventMethods, SettingsMethods, FontMethods<WebInternalFrame>, SizeMethods<WebInternalFrame>
{
    /**
     * Event properties.
     */
    public static final String CLOSABLE_PROPERTY = "closable";
    public static final String MAXIMIZABLE_PROPERTY = "maximizable";
    public static final String ICONABLE_PROPERTY = "iconable";

    /**
     * Creates a non-resizable, non-closable, non-maximizable, non-iconifiable {@code JInternalFrame} with no title.
     */
    public WebInternalFrame ()
    {
        this ( StyleId.auto, null, false, false, false, false );
    }

    /**
     * Creates a non-resizable, non-closable, non-maximizable, non-iconifiable {@code WebInternalFrame} with the specified title.
     * Note that passing in a {@code null} {@code title} results in unspecified behavior and possibly an exception.
     *
     * @param title the non-{@code null} {@code String} to display in the title bar
     */
    public WebInternalFrame ( @Nullable final String title )
    {
        this ( StyleId.auto, title, false, false, false, false );
    }

    /**
     * Creates a non-closable, non-maximizable, non-iconifiable {@code WebInternalFrame} with the specified title and resizability.
     *
     * @param title     the {@code String} to display in the title bar
     * @param resizable if {@code true}, the internal frame can be resized
     */
    public WebInternalFrame ( @Nullable final String title, final boolean resizable )
    {
        this ( StyleId.auto, title, resizable, false, false, false );
    }

    /**
     * Creates a non-maximizable, non-iconifiable {@code WebInternalFrame} with the specified title, resizability, and closability.
     *
     * @param title     the {@code String} to display in the title bar
     * @param resizable if {@code true}, the internal frame can be resized
     * @param closable  if {@code true}, the internal frame can be closed
     */
    public WebInternalFrame ( @Nullable final String title, final boolean resizable, final boolean closable )
    {
        this ( StyleId.auto, title, resizable, closable, false, false );
    }

    /**
     * Creates a non-iconifiable {@code WebInternalFrame} with the specified title, resizability, closability, and maximizability.
     *
     * @param title       the {@code String} to display in the title bar
     * @param resizable   if {@code true}, the internal frame can be resized
     * @param closable    if {@code true}, the internal frame can be closed
     * @param maximizable if {@code true}, the internal frame can be maximized
     */
    public WebInternalFrame ( @Nullable final String title, final boolean resizable, final boolean closable, final boolean maximizable )
    {
        this ( StyleId.auto, title, resizable, closable, maximizable, false );
    }

    /**
     * Creates a {@code WebInternalFrame} with the specified title, resizability, closability, maximizability, and iconifiability.
     *
     * @param title       the {@code String} to display in the title bar
     * @param resizable   if {@code true}, the internal frame can be resized
     * @param closable    if {@code true}, the internal frame can be closed
     * @param maximizable if {@code true}, the internal frame can be maximized
     * @param iconifiable if {@code true}, the internal frame can be iconified
     */
    public WebInternalFrame ( @Nullable final String title, final boolean resizable, final boolean closable, final boolean maximizable,
                              final boolean iconifiable )
    {
        this ( StyleId.auto, title, resizable, closable, maximizable, iconifiable );
    }

    /**
     * Creates a non-resizable, non-closable, non-maximizable, non-iconifiable {@code JInternalFrame} with no title.
     *
     * @param id style ID
     */
    public WebInternalFrame ( @NotNull final StyleId id )
    {
        this ( id, null, false, false, false, false );
    }

    /**
     * Creates a non-resizable, non-closable, non-maximizable, non-iconifiable {@code WebInternalFrame} with the specified title.
     * Note that passing in a {@code null} {@code title} results in unspecified behavior and possibly an exception.
     *
     * @param id    style ID
     * @param title the non-{@code null} {@code String} to display in the title bar
     */
    public WebInternalFrame ( @NotNull final StyleId id, @Nullable final String title )
    {
        this ( id, title, false, false, false, false );
    }

    /**
     * Creates a non-closable, non-maximizable, non-iconifiable {@code WebInternalFrame} with the specified title and resizability.
     *
     * @param id        style ID
     * @param title     the {@code String} to display in the title bar
     * @param resizable if {@code true}, the internal frame can be resized
     */
    public WebInternalFrame ( @NotNull final StyleId id, @Nullable final String title, final boolean resizable )
    {
        this ( id, title, resizable, false, false, false );
    }

    /**
     * Creates a non-maximizable, non-iconifiable {@code WebInternalFrame} with the specified title, resizability, and closability.
     *
     * @param id        style ID
     * @param title     the {@code String} to display in the title bar
     * @param resizable if {@code true}, the internal frame can be resized
     * @param closable  if {@code true}, the internal frame can be closed
     */
    public WebInternalFrame ( @NotNull final StyleId id, @Nullable final String title, final boolean resizable, final boolean closable )
    {
        this ( id, title, resizable, closable, false, false );
    }

    /**
     * Creates a non-iconifiable {@code WebInternalFrame} with the specified title, resizability, closability, and maximizability.
     *
     * @param id          style ID
     * @param title       the {@code String} to display in the title bar
     * @param resizable   if {@code true}, the internal frame can be resized
     * @param closable    if {@code true}, the internal frame can be closed
     * @param maximizable if {@code true}, the internal frame can be maximized
     */
    public WebInternalFrame ( @NotNull final StyleId id, @Nullable final String title, final boolean resizable, final boolean closable,
                              final boolean maximizable )
    {
        this ( id, title, resizable, closable, maximizable, false );
    }

    /**
     * Creates a {@code WebInternalFrame} with the specified title, resizability, closability, maximizability, and iconifiability.
     *
     * @param id          style ID
     * @param title       the {@code String} to display in the title bar
     * @param resizable   if {@code true}, the internal frame can be resized
     * @param closable    if {@code true}, the internal frame can be closed
     * @param maximizable if {@code true}, the internal frame can be maximized
     * @param iconifiable if {@code true}, the internal frame can be iconified
     */
    public WebInternalFrame ( @NotNull final StyleId id, @Nullable final String title, final boolean resizable, final boolean closable,
                              final boolean maximizable, final boolean iconifiable )
    {
        super ( title, resizable, closable, maximizable, iconifiable );
        setStyleId ( id );
    }

    @Override
    public void setIcon ( final boolean b )
    {
        try
        {
            super.setIcon ( b );
        }
        catch ( final PropertyVetoException e )
        {
            throw new RuntimeException ( "Unable to iconify JInternalFrame", e );
        }
    }

    /**
     * Safely hides internal frame.
     */
    public void close ()
    {
        try
        {
            setClosed ( true );
        }
        catch ( final PropertyVetoException e )
        {
            throw new RuntimeException ( "Unable to close JInternalFrame", e );
        }
    }

    /**
     * Safely displays internal frame.
     */
    public void open ()
    {
        try
        {
            setClosed ( false );
            setVisible ( true );
        }
        catch ( final PropertyVetoException e )
        {
            throw new RuntimeException ( "Unable to open JInternalFrame", e );
        }
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.internalframe;
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
    public WebInternalFrame setPlainFont ()
    {
        return FontMethodsImpl.setPlainFont ( this );
    }

    @Override
    public WebInternalFrame setPlainFont ( final boolean apply )
    {
        return FontMethodsImpl.setPlainFont ( this, apply );
    }

    @Override
    public boolean isPlainFont ()
    {
        return FontMethodsImpl.isPlainFont ( this );
    }

    @Override
    public WebInternalFrame setBoldFont ()
    {
        return FontMethodsImpl.setBoldFont ( this );
    }

    @Override
    public WebInternalFrame setBoldFont ( final boolean apply )
    {
        return FontMethodsImpl.setBoldFont ( this, apply );
    }

    @Override
    public boolean isBoldFont ()
    {
        return FontMethodsImpl.isBoldFont ( this );
    }

    @Override
    public WebInternalFrame setItalicFont ()
    {
        return FontMethodsImpl.setItalicFont ( this );
    }

    @Override
    public WebInternalFrame setItalicFont ( final boolean apply )
    {
        return FontMethodsImpl.setItalicFont ( this, apply );
    }

    @Override
    public boolean isItalicFont ()
    {
        return FontMethodsImpl.isItalicFont ( this );
    }

    @Override
    public WebInternalFrame setFontStyle ( final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontStyle ( this, bold, italic );
    }

    @Override
    public WebInternalFrame setFontStyle ( final int style )
    {
        return FontMethodsImpl.setFontStyle ( this, style );
    }

    @Override
    public WebInternalFrame setFontSize ( final int fontSize )
    {
        return FontMethodsImpl.setFontSize ( this, fontSize );
    }

    @Override
    public WebInternalFrame changeFontSize ( final int change )
    {
        return FontMethodsImpl.changeFontSize ( this, change );
    }

    @Override
    public int getFontSize ()
    {
        return FontMethodsImpl.getFontSize ( this );
    }

    @Override
    public WebInternalFrame setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    @Override
    public WebInternalFrame setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, style );
    }

    @Override
    public WebInternalFrame setFontName ( final String fontName )
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
    public WebInternalFrame setPreferredWidth ( final int preferredWidth )
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
    public WebInternalFrame setPreferredHeight ( final int preferredHeight )
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
    public WebInternalFrame setPreferredSize ( final int width, final int height )
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
    public WebInternalFrame setMaximumWidth ( final int maximumWidth )
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
    public WebInternalFrame setMaximumHeight ( final int maximumHeight )
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
    public WebInternalFrame setMaximumSize ( final int width, final int height )
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
    public WebInternalFrame setMinimumWidth ( final int minimumWidth )
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
    public WebInternalFrame setMinimumHeight ( final int minimumHeight )
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
    public WebInternalFrame setMinimumSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setMinimumSize ( this, width, height );
    }

    /**
     * Returns the look and feel (LaF) object that renders this component.
     *
     * @return the {@link WInternalFrameUI} object that renders this component
     */
    @Override
    public WInternalFrameUI getUI ()
    {
        return ( WInternalFrameUI ) super.getUI ();
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WInternalFrameUI}
     */
    public void setUI ( final WInternalFrameUI ui )
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