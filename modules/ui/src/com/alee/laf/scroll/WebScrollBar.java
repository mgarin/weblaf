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

import com.alee.extended.painter.Painter;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.log.Log;
import com.alee.managers.style.StyleManager;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SizeUtils;
import com.alee.utils.laf.Styleable;
import com.alee.utils.swing.SizeMethods;

import javax.swing.*;
import java.awt.*;

/**
 * This JScrollBar extension class provides a direct access to WebScrollBarUI methods.
 *
 * @author Mikle Garin
 */

public class WebScrollBar extends JScrollBar implements Styleable, SizeMethods<WebScrollBar>
{
    /**
     * Constructs new scroll bar.
     */
    public WebScrollBar ()
    {
        super ();
    }

    /**
     * Constructs new scroll bar with the specified orientation.
     *
     * @param orientation scroll bar orientation
     */
    public WebScrollBar ( final int orientation )
    {
        super ( orientation );
    }

    /**
     * Constructs new scroll bar with the specified orientation and values.
     *
     * @param orientation scroll bar orientation
     * @param value       scroll bar value
     * @param extent      scroll bar extent
     * @param min         scroll bar minimum value
     * @param max         scroll bar maximum value
     */
    public WebScrollBar ( final int orientation, final int value, final int extent, final int min, final int max )
    {
        super ( orientation, value, extent, min, max );
    }

    /**
     * Returns whether scroll bar arrow buttons should be displayed or not.
     *
     * @return true if scroll bar arrow buttons should be displayed, false otherwise
     */
    public boolean isPaintButtons ()
    {
        return getWebUI ().isPaintButtons ();
    }

    /**
     * Sets whether scroll bar arrow buttons should be displayed or not.
     *
     * @param paintButtons whether scroll bar arrow buttons should be displayed or not
     * @return scroll bar
     */
    public WebScrollBar setPaintButtons ( final boolean paintButtons )
    {
        getWebUI ().setPaintButtons ( paintButtons );
        return this;
    }

    /**
     * Returns whether scroll bar track should be displayed or not.
     *
     * @return true if scroll bar track should be displayed, false otherwise
     */
    public boolean isPaintTrack ()
    {
        return getWebUI ().isPaintTrack ();
    }

    /**
     * Sets whether scroll bar track should be displayed or not.
     *
     * @param paintTrack whether scroll bar track should be displayed or not
     * @return scroll bar
     */
    public WebScrollBar setPaintTrack ( final boolean paintTrack )
    {
        getWebUI ().setPaintTrack ( paintTrack );
        return this;
    }

    /**
     * Returns scroll bar content margin.
     *
     * @return scroll bar content margin
     */
    public Insets getMargin ()
    {
        return getWebUI ().getMargin ();
    }

    /**
     * Sets scroll bar content margin.
     *
     * @param margin new scroll bar content margin
     * @return scroll bar
     */
    public WebScrollBar setMargin ( final Insets margin )
    {
        getWebUI ().setMargin ( margin );
        return this;
    }

    /**
     * Sets scroll bar content margin.
     *
     * @param top    new scroll bar content top margin
     * @param left   new scroll bar content left margin
     * @param bottom new scroll bar content bottom margin
     * @param right  new scroll bar content right margin
     * @return scroll bar
     */
    public WebScrollBar setMargin ( final int top, final int left, final int bottom, final int right )
    {
        return setMargin ( new Insets ( top, left, bottom, right ) );
    }

    /**
     * Sets scroll bar content margin.
     *
     * @param spacing new scroll bar content margin
     * @return scroll bar
     */
    public WebScrollBar setMargin ( final int spacing )
    {
        return setMargin ( spacing, spacing, spacing, spacing );
    }

    /**
     * Returns scroll bar painter.
     *
     * @return scroll bar painter
     */
    public Painter getPainter ()
    {
        return StyleManager.getPainter ( this );
    }

    /**
     * Sets scroll bar painter.
     * Pass null to remove scroll bar painter.
     *
     * @param painter new scroll bar painter
     * @return scroll bar
     */
    public WebScrollBar setPainter ( final Painter painter )
    {
        StyleManager.setCustomPainter ( this, painter );
        return this;
    }

    /**
     * Returns decrease button painter.
     * This the button displayed at top or left side of the scroll bar.
     *
     * @return decrease button painter
     */
    public Painter getDecreaseButtonPainter ()
    {
        return getWebUI ().getDecreaseButtonPainter ();
    }

    /**
     * Sets decrease button painter.
     * This the button displayed at top or left side of the scroll bar.
     *
     * @param painter new decrease button painter
     * @return scroll bar
     */
    public WebScrollBar setDecreaseButtonPainter ( final Painter painter )
    {
        getWebUI ().setDecreaseButtonPainter ( painter );
        return this;
    }

    /**
     * Returns increase button painter.
     * This the button displayed at bottom or right side of the scroll bar.
     *
     * @return increase button painter
     */
    public Painter getIncreaseButtonPainter ()
    {
        return getWebUI ().getIncreaseButtonPainter ();
    }

    /**
     * Sets increase button painter.
     * This the button displayed at bottom or right side of the scroll bar.
     *
     * @param painter new increase button painter
     * @return scroll bar
     */
    public WebScrollBar setIncreaseButtonPainter ( final Painter painter )
    {
        getWebUI ().setIncreaseButtonPainter ( painter );
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleId ()
    {
        return getWebUI ().getStyleId ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyleId ( final String id )
    {
        getWebUI ().setStyleId ( id );
    }

    /**
     * Returns Web-UI applied to this class.
     *
     * @return Web-UI applied to this class
     */
    public WebScrollBarUI getWebUI ()
    {
        return ( WebScrollBarUI ) getUI ();
    }

    /**
     * Installs a Web-UI into this component.
     */
    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebScrollBarUI ) )
        {
            try
            {
                setUI ( ( WebScrollBarUI ) ReflectUtils.createInstance ( WebLookAndFeel.scrollBarUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebScrollBarUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

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
    public WebScrollBar setPreferredWidth ( final int preferredWidth )
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
    public WebScrollBar setPreferredHeight ( final int preferredHeight )
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
    public WebScrollBar setMinimumWidth ( final int minimumWidth )
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
    public WebScrollBar setMinimumHeight ( final int minimumHeight )
    {
        return SizeUtils.setMinimumHeight ( this, minimumHeight );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaximumWidth ()
    {
        return SizeUtils.getMaximumWidth ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebScrollBar setMaximumWidth ( final int maximumWidth )
    {
        return SizeUtils.setMaximumWidth ( this, maximumWidth );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaximumHeight ()
    {
        return SizeUtils.getMaximumHeight ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebScrollBar setMaximumHeight ( final int maximumHeight )
    {
        return SizeUtils.setMaximumHeight ( this, maximumHeight );
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
     * {@inheritDoc}
     */
    @Override
    public WebScrollBar setPreferredSize ( final int width, final int height )
    {
        return SizeUtils.setPreferredSize ( this, width, height );
    }
}