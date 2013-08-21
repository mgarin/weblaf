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

package com.alee.laf.scroll;

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.language.LanguageContainerMethods;
import com.alee.managers.language.LanguageManager;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SizeUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.swing.SizeMethods;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 29.04.11 Time: 15:37
 */

public class WebScrollPane extends JScrollPane implements ShapeProvider, SizeMethods<WebScrollPane>, LanguageContainerMethods
{
    public WebScrollPane ( Component view )
    {
        this ( view, true );
    }

    public WebScrollPane ( Component view, boolean drawBorder )
    {
        this ( view, drawBorder, WebScrollBarStyle.drawBorder );
    }

    public WebScrollPane ( Component view, boolean drawBorder, boolean drawInnerBorder )
    {
        super ( view );
        setDrawBorder ( drawBorder );
        getWebHorizontalScrollBar ().setDrawBorder ( drawInnerBorder );
        getWebVerticalScrollBar ().setDrawBorder ( drawInnerBorder );
        if ( !drawInnerBorder )
        {
            setCorner ( JScrollPane.LOWER_RIGHT_CORNER, null );
        }
    }

    @Override
    public WebScrollBar createVerticalScrollBar ()
    {
        return new WebScrollBar ( WebScrollBar.VERTICAL );
    }

    @Override
    public WebScrollBar createHorizontalScrollBar ()
    {
        return new WebScrollBar ( WebScrollBar.HORIZONTAL );
    }

    /**
     * Additional Web-component methods
     */

    public WebScrollBar getWebVerticalScrollBar ()
    {
        return ( WebScrollBar ) super.getVerticalScrollBar ();
    }

    public WebScrollBar getWebHorizontalScrollBar ()
    {
        return ( WebScrollBar ) super.getHorizontalScrollBar ();
    }

    /**
     * UI methods
     */

    public boolean isDrawBorder ()
    {
        return getWebUI ().isDrawBorder ();
    }

    public WebScrollPane setDrawBorder ( boolean drawBorder )
    {
        getWebUI ().setDrawBorder ( drawBorder );
        return this;
    }

    public int getRound ()
    {
        return getWebUI ().getRound ();
    }

    public WebScrollPane setRound ( int round )
    {
        getWebUI ().setRound ( round );
        return this;
    }

    public int getShadeWidth ()
    {
        return getWebUI ().getShadeWidth ();
    }

    public WebScrollPane setShadeWidth ( int shadeWidth )
    {
        getWebUI ().setShadeWidth ( shadeWidth );
        return this;
    }

    public Insets getMargin ()
    {
        return getWebUI ().getMargin ();
    }

    public WebScrollPane setMargin ( Insets margin )
    {
        getWebUI ().setMargin ( margin );
        return this;
    }

    public WebScrollPane setMargin ( int top, int left, int bottom, int right )
    {
        return setMargin ( new Insets ( top, left, bottom, right ) );
    }

    public WebScrollPane setMargin ( int spacing )
    {
        return setMargin ( spacing, spacing, spacing, spacing );
    }

    public boolean isDrawFocus ()
    {
        return getWebUI ().isDrawFocus ();
    }

    public WebScrollPane setDrawFocus ( boolean drawFocus )
    {
        getWebUI ().setDrawFocus ( drawFocus );
        return this;
    }

    public boolean isDrawBackground ()
    {
        return getWebUI ().isDrawBackground ();
    }

    public WebScrollPane setDrawBackground ( boolean drawBackground )
    {
        getWebUI ().setDrawBackground ( drawBackground );
        return this;
    }

    public Color getBorderColor ()
    {
        return getWebUI ().getBorderColor ();
    }

    public WebScrollPane setBorderColor ( Color borderColor )
    {
        getWebUI ().setBorderColor ( borderColor );
        return this;
    }

    public Color getDarkBorder ()
    {
        return getWebUI ().getDarkBorder ();
    }

    public WebScrollPane setDarkBorder ( Color darkBorder )
    {
        getWebUI ().setDarkBorder ( darkBorder );
        return this;
    }

    @Override
    public Shape provideShape ()
    {
        return getWebUI ().provideShape ();
    }

    public WebScrollPaneUI getWebUI ()
    {
        return ( WebScrollPaneUI ) getUI ();
    }

    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebScrollPaneUI ) )
        {
            try
            {
                setUI ( ( WebScrollPaneUI ) ReflectUtils.createInstance ( WebLookAndFeel.scrollPaneUI ) );
            }
            catch ( Throwable e )
            {
                e.printStackTrace ();
                setUI ( new WebScrollPaneUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    /**
     * Size methods.
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPreferredWidth ()
    {
        return SizeUtils.getPreferredWidth ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebScrollPane setPreferredWidth ( int preferredWidth )
    {
        return SizeUtils.setPreferredWidth ( this, preferredWidth );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPreferredHeight ()
    {
        return SizeUtils.getPreferredHeight ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebScrollPane setPreferredHeight ( int preferredHeight )
    {
        return SizeUtils.setPreferredHeight ( this, preferredHeight );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinimumWidth ()
    {
        return SizeUtils.getMinimumWidth ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebScrollPane setMinimumWidth ( int minimumWidth )
    {
        return SizeUtils.setMinimumWidth ( this, minimumWidth );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinimumHeight ()
    {
        return SizeUtils.getMinimumHeight ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebScrollPane setMinimumHeight ( int minimumHeight )
    {
        return SizeUtils.setMinimumHeight ( this, minimumHeight );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize ()
    {
        return SizeUtils.getPreferredSize ( this, super.getPreferredSize () );
    }

    /**
     * Language container methods
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLanguageContainerKey ( String key )
    {
        LanguageManager.registerLanguageContainer ( this, key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLanguageContainerKey ()
    {
        LanguageManager.unregisterLanguageContainer ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLanguageContainerKey ()
    {
        return LanguageManager.getLanguageContainerKey ( this );
    }
}