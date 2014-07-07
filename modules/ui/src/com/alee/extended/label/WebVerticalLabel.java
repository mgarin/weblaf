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

package com.alee.extended.label;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.label.WebLabel;
import com.alee.managers.log.Log;
import com.alee.utils.ReflectUtils;

import javax.swing.*;

/**
 * This WebLabel extension class allows you to display text vertically and provides a direct access to WebVerticalLabelUI methods.
 * It also provides a few additional constructors nad methods to setup the label.
 * <p/>
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application L&amp;F as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 */

public class WebVerticalLabel extends WebLabel
{
    /**
     * todo 1. Integrate rotation right into WebLabel and remove WebVerticalLabel
     */

    /**
     * Unique UI class ID.
     *
     * @see #getUIClassID
     */
    private static final String uiClassID = "VerticalLabelUI";

    public WebVerticalLabel ()
    {
        this ( false );
    }

    public WebVerticalLabel ( final boolean clockwise )
    {
        super ();
        setClockwise ( clockwise );
    }

    public WebVerticalLabel ( final Icon image )
    {
        this ( image, false );
    }

    public WebVerticalLabel ( final Icon image, final boolean clockwise )
    {
        super ( image );
        setClockwise ( clockwise );
    }

    public WebVerticalLabel ( final Icon image, final int horizontalAlignment )
    {
        this ( image, horizontalAlignment, false );
    }

    public WebVerticalLabel ( final Icon image, final int horizontalAlignment, final boolean clockwise )
    {
        super ( image, horizontalAlignment );
        setClockwise ( clockwise );
    }

    public WebVerticalLabel ( final String text )
    {
        this ( text, false );
    }

    public WebVerticalLabel ( final String text, final boolean clockwise )
    {
        super ( text );
        setClockwise ( clockwise );
    }

    public WebVerticalLabel ( final String text, final int horizontalAlignment )
    {
        this ( text, horizontalAlignment, false );
    }

    public WebVerticalLabel ( final String text, final int horizontalAlignment, final boolean clockwise )
    {
        super ( text, horizontalAlignment );
        setClockwise ( clockwise );
    }

    public WebVerticalLabel ( final String text, final Icon icon, final int horizontalAlignment )
    {
        this ( text, icon, horizontalAlignment, false );
    }

    public WebVerticalLabel ( final String text, final Icon icon, final int horizontalAlignment, final boolean clockwise )
    {
        super ( text, icon, horizontalAlignment );
        setClockwise ( clockwise );
    }

    /**
     * Returns whether text should be rotated clockwise or not.
     *
     * @return true if text should be rotated clockwise, false if counter-clockwise
     */
    public boolean isClockwise ()
    {
        return getWebUI ().isClockwise ();
    }

    /**
     * Sets whether text should be rotated clockwise or not.
     *
     * @param clockwise whether text should be rotated clockwise or not
     */
    public void setClockwise ( final boolean clockwise )
    {
        getWebUI ().setClockwise ( clockwise );
    }

    /**
     * Returns Web-UI applied to this class.
     *
     * @return Web-UI applied to this class
     */
    @Override
    public WebVerticalLabelUI getWebUI ()
    {
        return ( WebVerticalLabelUI ) getUI ();
    }

    /**
     * Installs a Web-UI into this component.
     */
    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebVerticalLabelUI ) )
        {
            try
            {
                setUI ( ( WebVerticalLabelUI ) ReflectUtils.createInstance ( WebLookAndFeel.verticalLabelUI ) );

            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebVerticalLabelUI () );
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
    public String getUIClassID ()
    {
        return uiClassID;
    }
}