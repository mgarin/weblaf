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

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.log.Log;
import com.alee.managers.style.ShapeProvider;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.Styleable;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.FontMethods;

import javax.swing.*;
import java.awt.*;

/**
 * This JToolTip extension class provides a direct access to WebToolTipUI methods.
 *
 * @author Mikle Garin
 */

public class WebToolTip extends JToolTip implements Styleable, ShapeProvider, FontMethods<WebToolTip>
{
    /**
     * Constructs empty tooltip.
     */
    public WebToolTip ()
    {
        super ();
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

    /**
     * Returns component shape.
     *
     * @return component shape
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
    private WebToolTipUI getWebUI ()
    {
        return ( WebToolTipUI ) getUI ();
    }

    /**
     * Installs a Web-UI into this component.
     */
    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebToolTipUI ) )
        {
            try
            {
                setUI ( ( WebToolTipUI ) ReflectUtils.createInstance ( WebLookAndFeel.toolTipUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebToolTipUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    @Override
    public WebToolTip setPlainFont ()
    {
        return SwingUtils.setPlainFont ( this );
    }

    @Override
    public boolean isPlainFont ()
    {
        return SwingUtils.isPlainFont ( this );
    }

    @Override
    public WebToolTip setPlainFont ( final boolean apply )
    {
        return SwingUtils.setPlainFont ( this, apply );
    }

    @Override
    public WebToolTip setBoldFont ()
    {
        return SwingUtils.setBoldFont ( this );
    }

    @Override
    public boolean isBoldFont ()
    {
        return SwingUtils.isBoldFont ( this );
    }

    @Override
    public WebToolTip setBoldFont ( final boolean apply )
    {
        return SwingUtils.setBoldFont ( this, apply );
    }

    @Override
    public WebToolTip setItalicFont ()
    {
        return SwingUtils.setItalicFont ( this );
    }

    @Override
    public boolean isItalicFont ()
    {
        return SwingUtils.isItalicFont ( this );
    }

    @Override
    public WebToolTip setItalicFont ( final boolean apply )
    {
        return SwingUtils.setItalicFont ( this, apply );
    }

    @Override
    public WebToolTip setFontStyle ( final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    @Override
    public WebToolTip setFontStyle ( final int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    @Override
    public WebToolTip changeFontSize ( final int change )
    {
        return SwingUtils.changeFontSize ( this, change );
    }

    @Override
    public int getFontSize ()
    {
        return SwingUtils.getFontSize ( this );
    }

    @Override
    public WebToolTip setFontSize ( final int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    @Override
    public WebToolTip setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    @Override
    public WebToolTip setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    @Override
    public String getFontName ()
    {
        return SwingUtils.getFontName ( this );
    }

    @Override
    public WebToolTip setFontName ( final String fontName )
    {
        return SwingUtils.setFontName ( this, fontName );
    }
}