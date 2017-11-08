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

package com.alee.laf.tooltip;

import com.alee.managers.style.*;
import com.alee.painter.Paintable;
import com.alee.painter.Painter;
import com.alee.utils.swing.extensions.FontMethods;
import com.alee.utils.swing.extensions.FontMethodsImpl;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * {@link JToolTip} extension class.
 * It contains various useful methods to simplify core component usage.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application L&amp;F as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see JToolTip
 * @see WebToolTipUI
 * @see ToolTipPainter
 */

public class WebToolTip extends JToolTip
        implements Styleable, Paintable, ShapeMethods, MarginMethods, PaddingMethods, FontMethods<WebToolTip>
{
    /**
     * Constructs empty tooltip.
     */
    public WebToolTip ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs empty tooltip.
     *
     * @param id style ID
     */
    public WebToolTip ( final StyleId id )
    {
        super ();
        setStyleId ( id );
    }

    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.tooltip;
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

    /**
     * Returns the look and feel (L&amp;F) object that renders this component.
     *
     * @return the {@link WToolTipUI} object that renders this component
     */
    @Override
    public WToolTipUI getUI ()
    {
        return ( WToolTipUI ) super.getUI ();
    }

    /**
     * Sets the L&amp;F object that renders this component.
     *
     * @param ui {@link WToolTipUI}
     */
    public void setUI ( final WToolTipUI ui )
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

    @Override
    public WebToolTip setPlainFont ()
    {
        return FontMethodsImpl.setPlainFont ( this );
    }

    @Override
    public boolean isPlainFont ()
    {
        return FontMethodsImpl.isPlainFont ( this );
    }

    @Override
    public WebToolTip setPlainFont ( final boolean apply )
    {
        return FontMethodsImpl.setPlainFont ( this, apply );
    }

    @Override
    public WebToolTip setBoldFont ()
    {
        return FontMethodsImpl.setBoldFont ( this );
    }

    @Override
    public boolean isBoldFont ()
    {
        return FontMethodsImpl.isBoldFont ( this );
    }

    @Override
    public WebToolTip setBoldFont ( final boolean apply )
    {
        return FontMethodsImpl.setBoldFont ( this, apply );
    }

    @Override
    public WebToolTip setItalicFont ()
    {
        return FontMethodsImpl.setItalicFont ( this );
    }

    @Override
    public boolean isItalicFont ()
    {
        return FontMethodsImpl.isItalicFont ( this );
    }

    @Override
    public WebToolTip setItalicFont ( final boolean apply )
    {
        return FontMethodsImpl.setItalicFont ( this, apply );
    }

    @Override
    public WebToolTip setFontStyle ( final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontStyle ( this, bold, italic );
    }

    @Override
    public WebToolTip setFontStyle ( final int style )
    {
        return FontMethodsImpl.setFontStyle ( this, style );
    }

    @Override
    public WebToolTip changeFontSize ( final int change )
    {
        return FontMethodsImpl.changeFontSize ( this, change );
    }

    @Override
    public int getFontSize ()
    {
        return FontMethodsImpl.getFontSize ( this );
    }

    @Override
    public WebToolTip setFontSize ( final int fontSize )
    {
        return FontMethodsImpl.setFontSize ( this, fontSize );
    }

    @Override
    public WebToolTip setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    @Override
    public WebToolTip setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, style );
    }

    @Override
    public String getFontName ()
    {
        return FontMethodsImpl.getFontName ( this );
    }

    @Override
    public WebToolTip setFontName ( final String fontName )
    {
        return FontMethodsImpl.setFontName ( this, fontName );
    }
}