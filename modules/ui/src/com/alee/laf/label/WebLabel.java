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

package com.alee.laf.label;

import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.LanguageMethods;
import com.alee.managers.language.LanguageUtils;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.managers.log.Log;
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
 * This JLabel extension class provides a direct access to WebLabelUI methods.
 * It also provides a few additional constructors and methods to setup the label.
 * <p/>
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application L&amp;F as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 */

public class WebLabel extends JLabel
        implements Styleable, Skinnable, Paintable, ShapeProvider, MarginSupport, PaddingSupport, EventMethods, ToolTipMethods,
        LanguageMethods, FontMethods<WebLabel>, SizeMethods<WebLabel>
{
    /**
     * Constructs empty label.
     */
    public WebLabel ()
    {
        super ();
    }

    /**
     * Constructs label with the specified preferences.
     *
     * @param icon icon
     */
    public WebLabel ( final Icon icon )
    {
        super ( icon );
    }

    /**
     * Constructs label with the specified preferences.
     *
     * @param horizontalAlignment horizontal alignment
     */
    public WebLabel ( final int horizontalAlignment )
    {
        super ();
        setHorizontalAlignment ( horizontalAlignment );
    }

    /**
     * Constructs label with the specified preferences.
     *
     * @param icon                label icon
     * @param horizontalAlignment horizontal alignment
     */
    public WebLabel ( final Icon icon, final int horizontalAlignment )
    {
        super ( icon, horizontalAlignment );
    }

    /**
     * Constructs label with the specified preferences.
     *
     * @param text text or translation key
     */
    public WebLabel ( final String text )
    {
        super ( LanguageUtils.getInitialText ( text ) );
        LanguageUtils.registerInitialLanguage ( this, text );
    }

    /**
     * Constructs label with the specified preferences.
     *
     * @param text                text or translation key
     * @param horizontalAlignment horizontal alignment
     * @param data                language data, may not be passed
     */
    public WebLabel ( final String text, final int horizontalAlignment, final Object... data )
    {
        super ( LanguageUtils.getInitialText ( text, data ), horizontalAlignment );
        LanguageUtils.registerInitialLanguage ( this, text, data );
    }

    /**
     * Constructs label with the specified preferences.
     *
     * @param text text or translation key
     * @param icon label icon
     */
    public WebLabel ( final String text, final Icon icon )
    {
        super ( LanguageUtils.getInitialText ( text ), icon, LEADING );
        LanguageUtils.registerInitialLanguage ( this, text );
    }

    /**
     * Constructs label with the specified preferences.
     *
     * @param text                text or translation key
     * @param icon                label icon
     * @param horizontalAlignment horizontal alignment
     * @param data                language data, may not be passed
     */
    public WebLabel ( final String text, final Icon icon, final int horizontalAlignment, final Object... data )
    {
        super ( LanguageUtils.getInitialText ( text, data ), icon, horizontalAlignment );
        LanguageUtils.registerInitialLanguage ( this, text, data );
    }

    /**
     * Constructs empty label.
     *
     * @param id style ID
     */
    public WebLabel ( final StyleId id )
    {
        super ();
        setStyleId ( id );
    }

    /**
     * Constructs label with the specified preferences.
     *
     * @param id   style ID
     * @param icon icon
     */
    public WebLabel ( final StyleId id, final Icon icon )
    {
        super ( icon );
        setStyleId ( id );
    }

    /**
     * Constructs label with the specified preferences.
     *
     * @param id                  style ID
     * @param horizontalAlignment horizontal alignment
     */
    public WebLabel ( final StyleId id, final int horizontalAlignment )
    {
        super ();
        setHorizontalAlignment ( horizontalAlignment );
        setStyleId ( id );
    }

    /**
     * Constructs label with the specified preferences.
     *
     * @param id                  style ID
     * @param icon                label icon
     * @param horizontalAlignment horizontal alignment
     */
    public WebLabel ( final StyleId id, final Icon icon, final int horizontalAlignment )
    {
        super ( icon, horizontalAlignment );
        setStyleId ( id );
    }

    /**
     * Constructs label with the specified preferences.
     *
     * @param id   style ID
     * @param text text or translation key
     */
    public WebLabel ( final StyleId id, final String text )
    {
        super ( LanguageUtils.getInitialText ( text ) );
        LanguageUtils.registerInitialLanguage ( this, text );
        setStyleId ( id );
    }

    /**
     * Constructs label with the specified preferences.
     *
     * @param id                  style ID
     * @param text                text or translation key
     * @param horizontalAlignment horizontal alignment
     * @param data                language data, may not be passed
     */
    public WebLabel ( final StyleId id, final String text, final int horizontalAlignment, final Object... data )
    {
        super ( LanguageUtils.getInitialText ( text, data ), horizontalAlignment );
        LanguageUtils.registerInitialLanguage ( this, text, data );
        setStyleId ( id );
    }

    /**
     * Constructs label with the specified preferences.
     *
     * @param id   style ID
     * @param text text or translation key
     * @param icon label icon
     */
    public WebLabel ( final StyleId id, final String text, final Icon icon )
    {
        super ( LanguageUtils.getInitialText ( text ), icon, LEADING );
        LanguageUtils.registerInitialLanguage ( this, text );
        setStyleId ( id );
    }

    /**
     * Constructs label with the specified preferences.
     *
     * @param id                  style ID
     * @param text                text or translation key
     * @param icon                label icon
     * @param horizontalAlignment horizontal alignment
     * @param data                language data, may not be passed
     */
    public WebLabel ( final StyleId id, final String text, final Icon icon, final int horizontalAlignment, final Object... data )
    {
        super ( LanguageUtils.getInitialText ( text, data ), icon, horizontalAlignment );
        LanguageUtils.registerInitialLanguage ( this, text, data );
        setStyleId ( id );
    }

    @Override
    public StyleId getStyleId ()
    {
        return getWebUI ().getStyleId ();
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return getWebUI ().setStyleId ( id );
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
    public Skin restoreSkin ()
    {
        return StyleManager.restoreSkin ( this );
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
    public boolean restoreDefaultPainters ()
    {
        return StyleManager.restoreDefaultPainters ( this );
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
    private WebLabelUI getWebUI ()
    {
        return ( WebLabelUI ) getUI ();
    }

    /**
     * Installs a Web-UI into this component.
     */
    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebLabelUI ) )
        {
            try
            {
                setUI ( ( WebLabelUI ) UIManager.getUI ( this ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebLabelUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
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
    public WebLabel setPlainFont ()
    {
        return FontMethodsImpl.setPlainFont ( this );
    }

    @Override
    public WebLabel setPlainFont ( final boolean apply )
    {
        return FontMethodsImpl.setPlainFont ( this, apply );
    }

    @Override
    public boolean isPlainFont ()
    {
        return FontMethodsImpl.isPlainFont ( this );
    }

    @Override
    public WebLabel setBoldFont ()
    {
        return FontMethodsImpl.setBoldFont ( this );
    }

    @Override
    public WebLabel setBoldFont ( final boolean apply )
    {
        return FontMethodsImpl.setBoldFont ( this, apply );
    }

    @Override
    public boolean isBoldFont ()
    {
        return FontMethodsImpl.isBoldFont ( this );
    }

    @Override
    public WebLabel setItalicFont ()
    {
        return FontMethodsImpl.setItalicFont ( this );
    }

    @Override
    public WebLabel setItalicFont ( final boolean apply )
    {
        return FontMethodsImpl.setItalicFont ( this, apply );
    }

    @Override
    public boolean isItalicFont ()
    {
        return FontMethodsImpl.isItalicFont ( this );
    }

    @Override
    public WebLabel setFontStyle ( final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontStyle ( this, bold, italic );
    }

    @Override
    public WebLabel setFontStyle ( final int style )
    {
        return FontMethodsImpl.setFontStyle ( this, style );
    }

    @Override
    public WebLabel setFontSize ( final int fontSize )
    {
        return FontMethodsImpl.setFontSize ( this, fontSize );
    }

    @Override
    public WebLabel changeFontSize ( final int change )
    {
        return FontMethodsImpl.changeFontSize ( this, change );
    }

    @Override
    public int getFontSize ()
    {
        return FontMethodsImpl.getFontSize ( this );
    }

    @Override
    public WebLabel setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    @Override
    public WebLabel setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, style );
    }

    @Override
    public WebLabel setFontName ( final String fontName )
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
    public WebLabel setPreferredWidth ( final int preferredWidth )
    {
        return SizeMethodsImpl.setPreferredWidth ( this, preferredWidth );
    }

    @Override
    public int getPreferredHeight ()
    {
        return SizeMethodsImpl.getPreferredHeight ( this );
    }

    @Override
    public WebLabel setPreferredHeight ( final int preferredHeight )
    {
        return SizeMethodsImpl.setPreferredHeight ( this, preferredHeight );
    }

    @Override
    public int getMinimumWidth ()
    {
        return SizeMethodsImpl.getMinimumWidth ( this );
    }

    @Override
    public WebLabel setMinimumWidth ( final int minimumWidth )
    {
        return SizeMethodsImpl.setMinimumWidth ( this, minimumWidth );
    }

    @Override
    public int getMinimumHeight ()
    {
        return SizeMethodsImpl.getMinimumHeight ( this );
    }

    @Override
    public WebLabel setMinimumHeight ( final int minimumHeight )
    {
        return SizeMethodsImpl.setMinimumHeight ( this, minimumHeight );
    }

    @Override
    public int getMaximumWidth ()
    {
        return SizeMethodsImpl.getMaximumWidth ( this );
    }

    @Override
    public WebLabel setMaximumWidth ( final int maximumWidth )
    {
        return SizeMethodsImpl.setMaximumWidth ( this, maximumWidth );
    }

    @Override
    public int getMaximumHeight ()
    {
        return SizeMethodsImpl.getMaximumHeight ( this );
    }

    @Override
    public WebLabel setMaximumHeight ( final int maximumHeight )
    {
        return SizeMethodsImpl.setMaximumHeight ( this, maximumHeight );
    }

    @Override
    public Dimension getPreferredSize ()
    {
        return SizeMethodsImpl.getPreferredSize ( this, super.getPreferredSize () );
    }

    @Override
    public WebLabel setPreferredSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setPreferredSize ( this, width, height );
    }
}