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

import com.alee.api.jdk.Supplier;
import com.alee.extended.layout.OverlayData;
import com.alee.extended.layout.OverlayLayout;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.style.StyleId;

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
    private JComponent component;
    private Map<JComponent, OverlayData> overlayData;

    public WebOverlay ()
    {
        this ( StyleId.auto );
    }

    public WebOverlay ( final JComponent component )
    {
        this ( StyleId.auto, component );
    }

    public WebOverlay ( final JComponent component, final JComponent overlay )
    {
        this ( StyleId.auto, component, overlay );
    }

    public WebOverlay ( final JComponent component, final JComponent overlay, final int halign, final int valign )
    {
        this ( StyleId.auto, component, overlay, halign, valign );
    }

    public WebOverlay ( final JComponent component, final JComponent overlay, final Supplier<Rectangle> boundsSupplier )
    {
        this ( StyleId.auto, component, overlay, boundsSupplier );
    }

    public WebOverlay ( final StyleId id )
    {
        super ( id, new OverlayLayout () );
        initialize ();
    }

    public WebOverlay ( final StyleId id, final JComponent component )
    {
        super ( id, new OverlayLayout () );
        initialize ();
        setComponent ( component );
    }

    public WebOverlay ( final StyleId id, final JComponent component, final JComponent overlay )
    {
        super ( id, new OverlayLayout () );
        initialize ();
        setComponent ( component );
        addOverlay ( overlay );
    }

    public WebOverlay ( final StyleId id, final JComponent component, final JComponent overlay, final int halign, final int valign )
    {
        super ( id, new OverlayLayout () );
        initialize ();
        setComponent ( component );
        addOverlay ( overlay, halign, valign );
    }

    public WebOverlay ( final StyleId id, final JComponent component, final JComponent overlay, final Supplier<Rectangle> boundsSupplier )
    {
        super ( id, new OverlayLayout () );
        initialize ();
        setComponent ( component );
        addOverlay ( overlay, boundsSupplier );
    }

    private void initialize ()
    {
        component = null;
        overlayData = new HashMap<JComponent, OverlayData> ();
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

    public void setComponent ( final JComponent component )
    {
        if ( this.component != null )
        {
            remove ( this.component );
        }
        this.component = component;
        add ( component, OverlayLayout.COMPONENT );
    }

    public JComponent getComponent ()
    {
        return component;
    }

    public void addOverlay ( final JComponent overlay )
    {
        overlayData.put ( overlay, new OverlayData () );
        add ( overlay, OverlayLayout.OVERLAY, 0 );
    }

    public void addOverlay ( final JComponent overlay, final int halign, final int valign )
    {
        overlayData.put ( overlay, new OverlayData ( halign, valign ) );
        add ( overlay, OverlayLayout.OVERLAY, 0 );
    }

    public void addOverlay ( final JComponent overlay, final Supplier<Rectangle> boundsSupplier )
    {
        overlayData.put ( overlay, new OverlayData ( boundsSupplier ) );
        add ( overlay, OverlayLayout.OVERLAY, 0 );
    }

    public void removeOverlay ( final JComponent overlay )
    {
        overlayData.remove ( overlay );
        remove ( overlay );
    }

    public OverlayData getOverlayData ( final JComponent overlay )
    {
        return overlayData.get ( overlay );
    }
}