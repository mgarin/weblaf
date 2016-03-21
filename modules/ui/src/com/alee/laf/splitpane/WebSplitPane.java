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

import com.alee.painter.Paintable;
import com.alee.painter.Painter;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.log.Log;
import com.alee.managers.style.*;
import com.alee.managers.style.Skin;
import com.alee.managers.style.StyleListener;
import com.alee.managers.style.Skinnable;
import com.alee.utils.ReflectUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentListener;
import java.util.Map;

/**
 * @author Mikle Garin
 */

public class WebSplitPane extends JSplitPane implements Styleable, Skinnable, Paintable, ShapeProvider, MarginSupport, PaddingSupport
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