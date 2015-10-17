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

package com.alee.laf.separator;

import com.alee.extended.painter.Painter;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.log.Log;
import com.alee.managers.style.StyleId;
import com.alee.utils.ReflectUtils;
import com.alee.managers.style.MarginSupport;
import com.alee.managers.style.PaddingSupport;
import com.alee.managers.style.ShapeProvider;
import com.alee.managers.style.Styleable;

import javax.swing.*;
import java.awt.*;

/**
 * This JSeparator extension class provides a direct access to WebSeparatorUI methods.
 *
 * @author Mikle Garin
 */

public class WebSeparator extends JSeparator implements Styleable, ShapeProvider, MarginSupport, PaddingSupport
{
    /**
     * Constructs new separator.
     */
    public WebSeparator ()
    {
        super ();
    }

    /**
     * Constructs new separator with the specified orientation.
     *
     * @param orientation component orientation
     */
    public WebSeparator ( final int orientation )
    {
        super ( orientation );
    }

    /**
     * Constructs new separator.
     *
     * @param id style ID
     */
    public WebSeparator ( final StyleId id )
    {
        super ();
        setStyleId ( id );
    }

    /**
     * Constructs new separator with the specified orientation.
     *
     * @param id          style ID
     * @param orientation component orientation
     */
    public WebSeparator ( final StyleId id, final int orientation )
    {
        super ( orientation );
        setStyleId ( id );
    }

    /**
     * Returns separator painter.
     *
     * @return separator painter
     */
    public Painter getPainter ()
    {
        return getWebUI ().getPainter ();
    }

    /**
     * Sets separator painter.
     * Pass null to remove separator painter.
     *
     * @param painter new separator painter
     * @return this separator
     */
    public WebSeparator setPainter ( final Painter painter )
    {
        getWebUI ().setPainter ( painter );
        return this;
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
    private WebSeparatorUI getWebUI ()
    {
        return ( WebSeparatorUI ) getUI ();
    }

    /**
     * Installs a Web-UI into this component.
     */
    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebSeparatorUI ) )
        {
            try
            {
                setUI ( ( WebSeparatorUI ) ReflectUtils.createInstance ( WebLookAndFeel.separatorUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebSeparatorUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    /**
     * Returns newly created horizontal separator.
     *
     * @return newly created horizontal separator
     */
    public static WebSeparator createHorizontal ()
    {
        return new WebSeparator ( WebSeparator.HORIZONTAL );
    }

    /**
     * Returns newly created vertical separator.
     *
     * @return newly created vertical separator
     */
    public static WebSeparator createVertical ()
    {
        return new WebSeparator ( WebSeparator.VERTICAL );
    }
}