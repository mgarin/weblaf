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

import com.alee.extended.painter.Painter;
import com.alee.managers.style.StyleId;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.log.Log;
import com.alee.utils.ReflectUtils;
import com.alee.managers.style.ShapeProvider;
import com.alee.managers.style.Styleable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentListener;

/**
 * @author Mikle Garin
 */

public class WebSplitPane extends JSplitPane implements Styleable, ShapeProvider
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

    public WebSplitPane ( final StyleId id )
    {
        super ();
        setStyleId ( id );
    }

    public WebSplitPane ( final StyleId id, final int newOrientation )
    {
        super ( newOrientation );
        setStyleId ( id );
    }

    public WebSplitPane ( final StyleId id, final int newOrientation, final boolean newContinuousLayout )
    {
        super ( newOrientation, newContinuousLayout );
        setStyleId ( id );
    }

    public WebSplitPane ( final StyleId id, final int newOrientation, final Component newLeftComponent, final Component newRightComponent )
    {
        super ( newOrientation, newLeftComponent, newRightComponent );
        setStyleId ( id );
    }

    public WebSplitPane ( final StyleId id, final int newOrientation, final boolean newContinuousLayout, final Component newLeftComponent,
                          final Component newRightComponent )
    {
        super ( newOrientation, newContinuousLayout, newLeftComponent, newRightComponent );
        setStyleId ( id );
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
    public void setDrawDividerBorder ( final boolean draw )
    {
        getWebUI ().setDrawDividerBorder ( draw );
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
    public void setDividerBorderColor ( final Color color )
    {
        getWebUI ().setDividerBorderColor ( color );
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

    /**
     * Returns split pane painter.
     *
     * @return split pane painter
     */
    public Painter getPainter ()
    {
        return getWebUI ().getPainter ();
    }

    /**
     * Sets split pane painter.
     * Pass null to remove split pane painter.
     *
     * @param painter new split pane painter
     * @return this split pane
     */
    public WebSplitPane setPainter ( final Painter painter )
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

    /**
     * Returns Web-UI applied to this class.
     *
     * @return Web-UI applied to this class
     */
    private WebSplitPaneUI getWebUI ()
    {
        return ( WebSplitPaneUI ) getUI ();
    }

    /**
     * Installs a Web-UI into this component.
     */
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