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
 * This component allows you to add one or more custom overlaying components atop of other component. This will work with any kind of Swing
 * components - you can overlay anyhting with something like loader, progress bar, custom text, image or anything else.
 *
 * @author Mikle Garin
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

    public WebOverlay ( final Component component )
    {
        super ( new OverlayLayout () );
        initialize ();
        setComponent ( component );
    }

    public WebOverlay ( final Component component, final Component overlay )
    {
        super ( new OverlayLayout () );
        initialize ();
        setComponent ( component );
        addOverlay ( overlay );
    }

    public WebOverlay ( final Component component, final Component overlay, final int halign, final int valign )
    {
        super ( new OverlayLayout () );
        initialize ();
        setComponent ( component );
        addOverlay ( overlay, halign, valign );
    }

    public WebOverlay ( final Component component, final Component overlay, final DataProvider<Rectangle> rectangleProvider )
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

    public void setComponentMargin ( final int margin )
    {
        getActualLayout ().setComponentMargin ( margin );
    }

    public void setComponentMargin ( final int top, final int left, final int bottom, final int right )
    {
        getActualLayout ().setComponentMargin ( top, left, bottom, right );
    }

    public void setComponentMargin ( final Insets margin )
    {
        getActualLayout ().setComponentMargin ( margin );
    }

    public Insets getOverlayMargin ()
    {
        return getActualLayout ().getOverlayMargin ();
    }

    public void setOverlayMargin ( final int margin )
    {
        getActualLayout ().setOverlayMargin ( margin );
    }

    public void setOverlayMargin ( final int top, final int left, final int bottom, final int right )
    {
        getActualLayout ().setOverlayMargin ( top, left, bottom, right );
    }

    public void setOverlayMargin ( final Insets margin )
    {
        getActualLayout ().setOverlayMargin ( margin );
    }

    public OverlayLayout getActualLayout ()
    {
        return ( OverlayLayout ) super.getLayout ();
    }

    public void setComponent ( final Component component )
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

    public void addOverlay ( final Component overlay )
    {
        overlayData.put ( overlay, new OverlayData () );
        add ( overlay, OverlayLayout.OVERLAY, 0 );
    }

    public void addOverlay ( final Component overlay, final int halign, final int valign )
    {
        overlayData.put ( overlay, new OverlayData ( halign, valign ) );
        add ( overlay, OverlayLayout.OVERLAY, 0 );
    }

    public void addOverlay ( final Component overlay, final DataProvider<Rectangle> rectangleProvider )
    {
        overlayData.put ( overlay, new OverlayData ( rectangleProvider ) );
        add ( overlay, OverlayLayout.OVERLAY, 0 );
    }

    public void removeOverlay ( final Component overlay )
    {
        overlayData.remove ( overlay );
        remove ( overlay );
    }

    public OverlayData getOverlayData ( final Component overlay )
    {
        return overlayData.get ( overlay );
    }
}