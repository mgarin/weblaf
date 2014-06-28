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

package com.alee.extended.panel;

import com.alee.extended.layout.OverlayData;
import com.alee.extended.layout.OverlayLayout;
import com.alee.laf.panel.WebPanel;
import com.alee.utils.swing.DataProvider;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * User: mgarin Date: 05.06.12 Time: 19:53
 * <p/>
 * This component allows you to add one or more custom overlaying components atop of other component. This will work with any kind of Swing
 * components - you can overlay anyhting with something like loader, progress bar, custom text, image or anything else.
 */

public class WebOverlay extends WebPanel implements SwingConstants
{
    private Component component;
    private Map<Component, OverlayData> overlayData;

    public WebOverlay ()
    {
        super ( new OverlayLayout () );
        initialize ();
    }

    public WebOverlay ( Component component )
    {
        super ( new OverlayLayout () );
        initialize ();
        setComponent ( component );
    }

    public WebOverlay ( Component component, Component overlay )
    {
        super ( new OverlayLayout () );
        initialize ();
        setComponent ( component );
        addOverlay ( overlay );
    }

    public WebOverlay ( Component component, Component overlay, int halign, int valign )
    {
        super ( new OverlayLayout () );
        initialize ();
        setComponent ( component );
        addOverlay ( overlay, halign, valign );
    }

    public WebOverlay ( Component component, Component overlay, DataProvider<Rectangle> rectangleProvider )
    {
        super ( new OverlayLayout () );
        initialize ();
        setComponent ( component );
        addOverlay ( overlay, rectangleProvider );
    }

    private void initialize ()
    {
        component = null;
        overlayData = new HashMap<Component, OverlayData> ();
    }

    public Insets getComponentMargin ()
    {
        return getActualLayout ().getComponentMargin ();
    }

    public void setComponentMargin ( int margin )
    {
        getActualLayout ().setComponentMargin ( margin );
    }

    public void setComponentMargin ( int top, int left, int bottom, int right )
    {
        getActualLayout ().setComponentMargin ( top, left, bottom, right );
    }

    public void setComponentMargin ( Insets margin )
    {
        getActualLayout ().setComponentMargin ( margin );
    }

    public Insets getOverlayMargin ()
    {
        return getActualLayout ().getOverlayMargin ();
    }

    public void setOverlayMargin ( int margin )
    {
        getActualLayout ().setOverlayMargin ( margin );
    }

    public void setOverlayMargin ( int top, int left, int bottom, int right )
    {
        getActualLayout ().setOverlayMargin ( top, left, bottom, right );
    }

    public void setOverlayMargin ( Insets margin )
    {
        getActualLayout ().setOverlayMargin ( margin );
    }

    public OverlayLayout getActualLayout ()
    {
        return ( OverlayLayout ) super.getLayout ();
    }

    public void setComponent ( Component component )
    {
        if ( this.component != null )
        {
            remove ( this.component );
        }
        this.component = component;
        add ( component, OverlayLayout.COMPONENT );
    }

    public Component getComponent ()
    {
        return component;
    }

    public void addOverlay ( Component overlay )
    {
        overlayData.put ( overlay, new OverlayData () );
        add ( overlay, OverlayLayout.OVERLAY, 0 );
    }

    public void addOverlay ( Component overlay, int halign, int valign )
    {
        overlayData.put ( overlay, new OverlayData ( halign, valign ) );
        add ( overlay, OverlayLayout.OVERLAY, 0 );
    }

    public void addOverlay ( Component overlay, DataProvider<Rectangle> rectangleProvider )
    {
        overlayData.put ( overlay, new OverlayData ( rectangleProvider ) );
        add ( overlay, OverlayLayout.OVERLAY, 0 );
    }

    public void removeOverlay ( Component overlay )
    {
        overlayData.remove ( overlay );
        remove ( overlay );
    }

    public OverlayData getOverlayData ( Component overlay )
    {
        return overlayData.get ( overlay );
    }
}
