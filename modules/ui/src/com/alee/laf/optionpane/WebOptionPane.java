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

package com.alee.laf.optionpane;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.settings.UISettingsManager;
import com.alee.managers.style.*;
import com.alee.painter.Paintable;
import com.alee.painter.Painter;
import com.alee.utils.swing.extensions.FontMethods;
import com.alee.utils.swing.extensions.FontMethodsImpl;
import com.alee.utils.swing.extensions.SizeMethods;
import com.alee.utils.swing.extensions.SizeMethodsImpl;

import javax.swing.*;
import java.awt.*;

/**
 * {@link JOptionPane} extension class.
 * It contains various useful methods to simplify core component usage.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see JOptionPane
 * @see WebOptionPaneUI
 * @see OptionPanePainter
 */
public class WebOptionPane extends JOptionPane implements Styleable, Paintable, ShapeMethods, MarginMethods, PaddingMethods,
        SettingsMethods, FontMethods<WebOptionPane>, SizeMethods<WebOptionPane>
{
    /**
     * Constructs new option pane.
     */
    public WebOptionPane ()
    {
        super ();
    }

    /**
     * Constructs new option pane.
     *
     * @param message message
     */
    public WebOptionPane ( final Object message )
    {
        this ( StyleId.auto, message );
    }

    /**
     * Constructs new option pane.
     *
     * @param message     message
     * @param messageType message type
     */
    public WebOptionPane ( final Object message, final int messageType )
    {
        this ( StyleId.auto, message, messageType );
    }

    /**
     * Constructs new option pane.
     *
     * @param message     message
     * @param messageType message type
     * @param optionType  option pane type
     */
    public WebOptionPane ( final Object message, final int messageType, final int optionType )
    {
        this ( StyleId.auto, message, messageType, optionType );
    }

    /**
     * Constructs new option pane.
     *
     * @param message     message
     * @param messageType message type
     * @param optionType  option pane type
     * @param icon        option pane icon
     */
    public WebOptionPane ( final Object message, final int messageType, final int optionType, final Icon icon )
    {
        this ( StyleId.auto, message, messageType, optionType, icon );
    }

    /**
     * Constructs new option pane.
     *
     * @param message     message
     * @param messageType message type
     * @param optionType  option pane type
     * @param icon        option pane icon
     * @param options     available options
     */
    public WebOptionPane ( final Object message, final int messageType, final int optionType, final Icon icon, final Object[] options )
    {
        this ( StyleId.auto, message, messageType, optionType, icon, options );
    }

    /**
     * Constructs new option pane.
     *
     * @param message      message
     * @param messageType  message type
     * @param optionType   option pane type
     * @param icon         option pane icon
     * @param options      available options
     * @param initialValue initial value
     */
    public WebOptionPane ( final Object message, final int messageType, final int optionType, final Icon icon, final Object[] options,
                           final Object initialValue )
    {
        this ( StyleId.auto, message, messageType, optionType, icon, options, initialValue );
    }

    /**
     * Constructs new option pane.
     *
     * @param id style ID
     */
    public WebOptionPane ( final StyleId id )
    {
        this ( id, "JOptionPane message" );
    }

    /**
     * Constructs new option pane.
     *
     * @param id      style ID
     * @param message message
     */
    public WebOptionPane ( final StyleId id, final Object message )
    {
        this ( id, message, PLAIN_MESSAGE );
    }

    /**
     * Constructs new option pane.
     *
     * @param id          style ID
     * @param message     message
     * @param messageType message type
     */
    public WebOptionPane ( final StyleId id, final Object message, final int messageType )
    {
        this ( id, message, messageType, DEFAULT_OPTION );
    }

    /**
     * Constructs new option pane.
     *
     * @param id          style ID
     * @param message     message
     * @param messageType message type
     * @param optionType  option pane type
     */
    public WebOptionPane ( final StyleId id, final Object message, final int messageType, final int optionType )
    {
        this ( id, message, messageType, optionType, null );
    }

    /**
     * Constructs new option pane.
     *
     * @param id          style ID
     * @param message     message
     * @param messageType message type
     * @param optionType  option pane type
     * @param icon        option pane icon
     */
    public WebOptionPane ( final StyleId id, final Object message, final int messageType, final int optionType, final Icon icon )
    {
        this ( id, message, messageType, optionType, icon, null );
    }

    /**
     * Constructs new option pane.
     *
     * @param id          style ID
     * @param message     message
     * @param messageType message type
     * @param optionType  option pane type
     * @param icon        option pane icon
     * @param options     available options
     */
    public WebOptionPane ( final StyleId id, final Object message, final int messageType, final int optionType, final Icon icon,
                           final Object[] options )
    {
        this ( id, message, messageType, optionType, icon, options, null );
    }

    /**
     * Constructs new option pane.
     *
     * @param id           style ID
     * @param message      message
     * @param messageType  message type
     * @param optionType   option pane type
     * @param icon         option pane icon
     * @param options      available options
     * @param initialValue initial value
     */
    public WebOptionPane ( final StyleId id, final Object message, final int messageType, final int optionType, final Icon icon,
                           final Object[] options, final Object initialValue )
    {
        super ( message, messageType, optionType, icon, options, initialValue );
        setStyleId ( id );
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.optionpane;
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
    public WebOptionPane setPlainFont ()
    {
        return FontMethodsImpl.setPlainFont ( this );
    }

    @Override
    public WebOptionPane setPlainFont ( final boolean apply )
    {
        return FontMethodsImpl.setPlainFont ( this, apply );
    }

    @Override
    public boolean isPlainFont ()
    {
        return FontMethodsImpl.isPlainFont ( this );
    }

    @Override
    public WebOptionPane setBoldFont ()
    {
        return FontMethodsImpl.setBoldFont ( this );
    }

    @Override
    public WebOptionPane setBoldFont ( final boolean apply )
    {
        return FontMethodsImpl.setBoldFont ( this, apply );
    }

    @Override
    public boolean isBoldFont ()
    {
        return FontMethodsImpl.isBoldFont ( this );
    }

    @Override
    public WebOptionPane setItalicFont ()
    {
        return FontMethodsImpl.setItalicFont ( this );
    }

    @Override
    public WebOptionPane setItalicFont ( final boolean apply )
    {
        return FontMethodsImpl.setItalicFont ( this, apply );
    }

    @Override
    public boolean isItalicFont ()
    {
        return FontMethodsImpl.isItalicFont ( this );
    }

    @Override
    public WebOptionPane setFontStyle ( final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontStyle ( this, bold, italic );
    }

    @Override
    public WebOptionPane setFontStyle ( final int style )
    {
        return FontMethodsImpl.setFontStyle ( this, style );
    }

    @Override
    public WebOptionPane setFontSize ( final int fontSize )
    {
        return FontMethodsImpl.setFontSize ( this, fontSize );
    }

    @Override
    public WebOptionPane changeFontSize ( final int change )
    {
        return FontMethodsImpl.changeFontSize ( this, change );
    }

    @Override
    public int getFontSize ()
    {
        return FontMethodsImpl.getFontSize ( this );
    }

    @Override
    public WebOptionPane setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    @Override
    public WebOptionPane setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, style );
    }

    @Override
    public WebOptionPane setFontName ( final String fontName )
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
    public WebOptionPane setPreferredWidth ( final int preferredWidth )
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
    public WebOptionPane setPreferredHeight ( final int preferredHeight )
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
    public WebOptionPane setPreferredSize ( final int width, final int height )
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
    public WebOptionPane setMaximumWidth ( final int maximumWidth )
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
    public WebOptionPane setMaximumHeight ( final int maximumHeight )
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
    public WebOptionPane setMaximumSize ( final int width, final int height )
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
    public WebOptionPane setMinimumWidth ( final int minimumWidth )
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
    public WebOptionPane setMinimumHeight ( final int minimumHeight )
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
    public WebOptionPane setMinimumSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setMinimumSize ( this, width, height );
    }

    /**
     * Returns the look and feel (LaF) object that renders this component.
     *
     * @return the {@link WebOptionPaneUI} object that renders this component
     */
    @Override
    public WebOptionPaneUI getUI ()
    {
        return ( WebOptionPaneUI ) super.getUI ();
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WebOptionPaneUI}
     */
    public void setUI ( final WebOptionPaneUI ui )
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