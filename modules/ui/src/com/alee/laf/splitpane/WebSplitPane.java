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

package com.alee.laf.splitpane;

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.log.Log;
import com.alee.utils.ReflectUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentListener;

/**
 * User: mgarin Date: 08.07.11 Time: 16:16
 */

public class WebSplitPane extends JSplitPane
{
    public WebSplitPane ()
    {
        super ();
    }

    public WebSplitPane ( final int newOrientation )
    {
        super ( newOrientation );
    }

    public WebSplitPane ( final int newOrientation, final boolean newContinuousLayout )
    {
        super ( newOrientation, newContinuousLayout );
    }

    public WebSplitPane ( final int newOrientation, final Component newLeftComponent, final Component newRightComponent )
    {
        super ( newOrientation, newLeftComponent, newRightComponent );
    }

    public WebSplitPane ( final int newOrientation, final boolean newContinuousLayout, final Component newLeftComponent,
                          final Component newRightComponent )
    {
        super ( newOrientation, newContinuousLayout, newLeftComponent, newRightComponent );
    }

    public void addDividerListener ( final ComponentListener listener )
    {
        getWebUI ().getDivider ().addComponentListener ( listener );
    }

    public void removeDividerListener ( final ComponentListener listener )
    {
        getWebUI ().getDivider ().removeComponentListener ( listener );
    }

    public Insets getMargin ()
    {
        return getWebUI ().getMargin ();
    }

    public void setMargin ( final Insets margin )
    {
        getWebUI ().setMargin ( margin );
    }

    public WebSplitPane setMargin ( final int top, final int left, final int bottom, final int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
        return this;
    }

    public WebSplitPane setMargin ( final int spacing )
    {
        return setMargin ( spacing, spacing, spacing, spacing );
    }

    public Color getDragDividerColor ()
    {
        return getWebUI ().getDragDividerColor ();
    }

    public WebSplitPane setDragDividerColor ( final Color dragDividerColor )
    {
        getWebUI ().setDragDividerColor ( dragDividerColor );
        return this;
    }

    /**
     * Returns whether divider border is painted or not.
     *
     * @return true if divider border is painted, false otherwise
     */
    public boolean isDrawDividerBorder ()
    {
        return getWebUI ().isDrawDividerBorder ();
    }

    /**
     * Sets whether divider border is painted or not.
     *
     * @param draw whether divider border is painted or not
     */
    public WebSplitPane setDrawDividerBorder ( final boolean draw )
    {
        getWebUI ().setDrawDividerBorder ( draw );
        return this;
    }

    /**
     * Returns divider border color.
     *
     * @return divider border color
     */
    public Color getDividerBorderColor ()
    {
        return getWebUI ().getDividerBorderColor ();
    }

    /**
     * Sets divider border color.
     *
     * @param color new divider border color
     */
    public WebSplitPane setDividerBorderColor ( final Color color )
    {
        getWebUI ().setDividerBorderColor ( color );
        return this;
    }

    /**
     * Returns proportional split divider location.
     *
     * @return proportional split divider location
     */
    public double getProportionalDividerLocation ()
    {
        final int l = getOrientation () == WebSplitPane.HORIZONTAL_SPLIT ? getWidth () : getHeight ();
        return Math.max ( 0.0, Math.min ( ( double ) getDividerLocation () / ( l - getDividerSize () ), 1.0 ) );
    }

    public WebSplitPaneUI getWebUI ()
    {
        return ( WebSplitPaneUI ) getUI ();
    }

    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebSplitPaneUI ) )
        {
            try
            {
                setUI ( ( WebSplitPaneUI ) ReflectUtils.createInstance ( WebLookAndFeel.splitPaneUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebSplitPaneUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
        revalidate ();
    }
}