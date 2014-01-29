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
import com.alee.utils.ReflectUtils;

import javax.swing.*;
import java.awt.*;

/**
 * This JScrollBar extension class provides a direct access to WebScrollBarUI methods.
 *
 * @author Mikle Garin
 */

public class WebScrollBar extends JScrollBar
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
    public boolean isButtonsVisible ()
    {
        return getWebUI ().isButtonsVisible ();
    }

    /**
     * Sets whether scroll bar arrow buttons should be displayed or not.
     *
     * @param visible whether scroll bar arrow buttons should be displayed or not
     * @return scroll bar
     */
    public WebScrollBar setButtonsVisible ( final boolean visible )
    {
        getWebUI ().setButtonsVisible ( visible );
        return this;
    }

    /**
     * Returns whether scroll bar track should be displayed or not.
     *
     * @return true if scroll bar track should be displayed, false otherwise
     */
    public boolean isDrawTrack ()
    {
        return getWebUI ().isDrawTrack ();
    }

    /**
     * Sets whether scroll bar track should be displayed or not.
     *
     * @param draw whether scroll bar track should be displayed or not
     * @return scroll bar
     */
    public WebScrollBar setDrawTrack ( final boolean draw )
    {
        getWebUI ().setDrawTrack ( draw );
        return this;
    }

    /**
     * Returns scroll bar track border color.
     *
     * @return scroll bar track border color
     */
    public Color getTrackBorderColor ()
    {
        return getWebUI ().getTrackBorderColor ();
    }

    /**
     * Sets scroll bar track border color.
     *
     * @param color new scroll bar track border color
     * @return scroll bar
     */
    public WebScrollBar setTrackBorderColor ( final Color color )
    {
        getWebUI ().setTrackBorderColor ( color );
        return this;
    }

    /**
     * Returns scroll bar track background color.
     *
     * @return scroll bar track background color
     */
    public Color getTrackBackgroundColor ()
    {
        return getWebUI ().getTrackBackgroundColor ();
    }

    /**
     * Sets scroll bar track background color.
     *
     * @param color new scroll bar track background color
     * @return scroll bar
     */
    public WebScrollBar setTrackBackgroundColor ( final Color color )
    {
        getWebUI ().setTrackBackgroundColor ( color );
        return this;
    }

    /**
     * Returns scroll bar thumb border color.
     *
     * @return scroll bar thumb border color
     */
    public Color getThumbBorderColor ()
    {
        return getWebUI ().getThumbBorderColor ();
    }

    /**
     * Sets scroll bar thumb border color.
     *
     * @param color new scroll bar thumb border color
     * @return scroll bar
     */
    public WebScrollBar setThumbBorderColor ( final Color color )
    {
        getWebUI ().setThumbBorderColor ( color );
        return this;
    }

    /**
     * Returns scroll bar thumb background color.
     *
     * @return scroll bar thumb background color
     */
    public Color getThumbBackgroundColor ()
    {
        return getWebUI ().getThumbBackgroundColor ();
    }

    /**
     * Sets scroll bar thumb background color.
     *
     * @param color new scroll bar thumb background color
     * @return scroll bar
     */
    public WebScrollBar setThumbBackgroundColor ( final Color color )
    {
        getWebUI ().setThumbBackgroundColor ( color );
        return this;
    }

    /**
     * Returns scroll bar thumb disabled border color.
     *
     * @return scroll bar thumb disabled border color
     */
    public Color getThumbDisabledBorderColor ()
    {
        return getWebUI ().getThumbDisabledBorderColor ();
    }

    /**
     * Sets scroll bar thumb disabled border color.
     *
     * @param color new scroll bar thumb disabled border color
     * @return scroll bar
     */
    public WebScrollBar setThumbDisabledBorderColor ( final Color color )
    {
        getWebUI ().setThumbDisabledBorderColor ( color );
        return this;
    }

    /**
     * Returns scroll bar thumb disabled background color.
     *
     * @return scroll bar thumb disabled background color
     */
    public Color getThumbDisabledBackgroundColor ()
    {
        return getWebUI ().getThumbDisabledBackgroundColor ();
    }

    /**
     * Sets scroll bar thumb disabled background color.
     *
     * @param color new scroll bar thumb disabled background color
     * @return scroll bar
     */
    public WebScrollBar setThumbDisabledBackgroundColor ( final Color color )
    {
        getWebUI ().setThumbDisabledBackgroundColor ( color );
        return this;
    }

    /**
     * Returns scroll bar thumb rollover border color.
     *
     * @return scroll bar thumb rollover border color
     */
    public Color getThumbRolloverBorderColor ()
    {
        return getWebUI ().getThumbRolloverBorderColor ();
    }

    /**
     * Sets scroll bar thumb rollover border color.
     *
     * @param color new scroll bar thumb rollover border color
     * @return scroll bar
     */
    public WebScrollBar setThumbRolloverBorderColor ( final Color color )
    {
        getWebUI ().setThumbRolloverBorderColor ( color );
        return this;
    }

    /**
     * Returns scroll bar thumb rollover background color.
     *
     * @return scroll bar thumb rollover background color
     */
    public Color getThumbRolloverBackgroundColor ()
    {
        return getWebUI ().getThumbRolloverBackgroundColor ();
    }

    /**
     * Sets scroll bar thumb rollover background color.
     *
     * @param color new scroll bar thumb rollover background color
     * @return scroll bar
     */
    public WebScrollBar setThumbRolloverBackgroundColor ( final Color color )
    {
        getWebUI ().setThumbRolloverBackgroundColor ( color );
        return this;
    }

    /**
     * Returns scroll bar thumb pressed border color.
     *
     * @return scroll bar thumb pressed border color
     */
    public Color getThumbPressedBorderColor ()
    {
        return getWebUI ().getThumbPressedBorderColor ();
    }

    /**
     * Sets scroll bar thumb pressed border color.
     *
     * @param color new scroll bar thumb pressed border color
     * @return scroll bar
     */
    public WebScrollBar setThumbPressedBorderColor ( final Color color )
    {
        getWebUI ().setThumbPressedBorderColor ( color );
        return this;
    }

    /**
     * Returns scroll bar thumb pressed/dragged background color.
     *
     * @return scroll bar thumb pressed/dragged background color
     */
    public Color getThumbPressedBackgroundColor ()
    {
        return getWebUI ().getThumbPressedBackgroundColor ();
    }

    /**
     * Sets scroll bar thumb pressed/dragged background color.
     *
     * @param color new scroll bar thumb pressed/dragged background color
     * @return scroll bar
     */
    public WebScrollBar setThumbPressedBackgroundColor ( final Color color )
    {
        getWebUI ().setThumbPressedBackgroundColor ( color );
        return this;
    }

    /**
     * Returns scroll bar thumb corners rounding.
     *
     * @return scroll bar thumb corners rounding
     */
    public int getThumbRound ()
    {
        return getWebUI ().getThumbRound ();
    }

    /**
     * Sets scroll bar thumb corners rounding.
     *
     * @param round new scroll bar thumb corners rounding
     * @return scroll bar
     */
    public WebScrollBar setThumbRound ( final int round )
    {
        getWebUI ().setThumbRound ( round );
        return this;
    }

    /**
     * Returns scroll bar thumb margin.
     *
     * @return scroll bar thumb margin
     */
    public Insets getThumbMargin ()
    {
        return getWebUI ().getThumbMargin ();
    }

    /**
     * Sets scroll bar thumb margin.
     *
     * @param margin new scroll bar thumb margin
     * @return scroll bar
     */
    public WebScrollBar setThumbMargin ( final Insets margin )
    {
        getWebUI ().setThumbMargin ( margin );
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
    public ScrollBarPainter getPainter ()
    {
        return getWebUI ().getPainter ();
    }

    /**
     * Sets scroll bar painter.
     * Pass null to remove scroll bar painter.
     *
     * @param painter new scroll bar painter
     * @return scroll bar
     */
    public WebScrollBar setPainter ( final ScrollBarPainter painter )
    {
        getWebUI ().setPainter ( painter );
        return this;
    }

    /**
     * Returns decrease button painter.
     * This the button displayed at top or left side of the scroll bar.
     *
     * @return decrease button painter
     */
    public ScrollBarButtonPainter getDecreaseButtonPainter ()
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
    public WebScrollBar setDecreaseButtonPainter ( final ScrollBarButtonPainter painter )
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
    public ScrollBarButtonPainter getIncreaseButtonPainter ()
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
    public WebScrollBar setIncreaseButtonPainter ( final ScrollBarButtonPainter painter )
    {
        getWebUI ().setIncreaseButtonPainter ( painter );
        return this;
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
            catch ( Throwable e )
            {
                e.printStackTrace ();
                setUI ( new WebScrollBarUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }
}