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

package com.alee.extended.layout;

import com.alee.extended.panel.WebOverlay;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a specific layout for WebOverlay component that allows you to add overlaying components atop of single main component.
 *
 * @author Mikle Garin
 */

public class OverlayLayout extends AbstractLayoutManager implements SwingConstants
{
    // Positions component on the whole container area
    public static final String COMPONENT = "COMPONENT";
    // Positions the component depending on WebOverlay settings
    public static final String OVERLAY = "OVERLAY";

    // Saved layout constraints
    protected Map<Component, String> constraints = new HashMap<Component, String> ();

    // Component side margins (this one is additional to the container margins)
    protected Insets componentMargin = null;

    // Overlay side margins (this one is additional to the container margins)
    protected Insets overlayMargin = null;

    public OverlayLayout ()
    {
        super ();
    }

    public Insets getComponentMargin ()
    {
        return componentMargin;
    }

    public void setComponentMargin ( final int margin )
    {
        setComponentMargin ( margin, margin, margin, margin );
    }

    public void setComponentMargin ( final int top, final int left, final int bottom, final int right )
    {
        setComponentMargin ( new Insets ( top, left, bottom, right ) );
    }

    public void setComponentMargin ( final Insets margin )
    {
        this.componentMargin = margin;
    }

    public Insets getOverlayMargin ()
    {
        return overlayMargin;
    }

    public void setOverlayMargin ( final int margin )
    {
        setOverlayMargin ( margin, margin, margin, margin );
    }

    public void setOverlayMargin ( final int top, final int left, final int bottom, final int right )
    {
        setOverlayMargin ( new Insets ( top, left, bottom, right ) );
    }

    public void setOverlayMargin ( final Insets overlayMargin )
    {
        this.overlayMargin = overlayMargin;
    }

    @Override
    public void addComponent ( final Component component, final Object constraints )
    {
        final String value = ( String ) constraints;
        if ( value == null || !value.equals ( COMPONENT ) && !value.equals ( OVERLAY ) )
        {
            throw new IllegalArgumentException ( "Cannot add to layout: constraint must be 'COMPONENT' or 'OVERLAY' string" );
        }
        this.constraints.put ( component, value );
    }

    @Override
    public void removeComponent ( final Component component )
    {
        this.constraints.remove ( component );
    }

    @Override
    public Dimension preferredLayoutSize ( final Container parent )
    {
        final Insets bi = parent.getInsets ();
        final Insets ci = getActualComponentInsets ( parent );
        Dimension ps = new Dimension ();
        for ( final Component component : parent.getComponents () )
        {
            final String constraint = constraints.get ( component );
            if ( constraint != null && constraint.equals ( COMPONENT ) )
            {
                final Dimension cps = component.getPreferredSize ();
                ps = SwingUtils.max ( ps, new Dimension ( bi.left + ci.left + cps.width + ci.right + bi.right,
                        bi.top + ci.top + cps.height + ci.bottom + bi.bottom ) );
            }
        }
        return ps;
    }

    protected Insets getActualComponentInsets ( final Container parent )
    {
        return componentMargin != null ?
                ( parent.getComponentOrientation ().isLeftToRight () ? componentMargin : SwingUtils.toRTL ( componentMargin ) ) :
                new Insets ( 0, 0, 0, 0 );
    }

    @Override
    public void layoutContainer ( final Container parent )
    {
        final Insets bi = parent.getInsets ();
        final Insets ci = getActualComponentInsets ( parent );
        for ( final Component component : parent.getComponents () )
        {
            final String constraint = constraints.get ( component );
            if ( constraint != null )
            {
                final int pw = parent.getWidth ();
                final int ph = parent.getHeight ();
                if ( constraint.equals ( COMPONENT ) )
                {
                    component.setBounds ( bi.left + ci.left, bi.top + ci.top, pw - bi.left - bi.right - ci.left - ci.right,
                            ph - bi.top - bi.bottom - ci.top - ci.bottom );
                }
                else if ( constraint.equals ( OVERLAY ) )
                {
                    final WebOverlay webOverlay = ( WebOverlay ) parent;
                    final OverlayData data = webOverlay.getOverlayData ( component );
                    final Insets om = overlayMargin != null ? overlayMargin : new Insets ( 0, 0, 0, 0 );
                    if ( data.getLocation ().equals ( OverlayLocation.fill ) )
                    {
                        component.setBounds ( bi.left + om.left, bi.top + om.top, pw - bi.left - om.left - bi.right - om.right,
                                ph - bi.top - om.top - bi.bottom - om.bottom );
                    }
                    else if ( data.getLocation ().equals ( OverlayLocation.align ) )
                    {
                        final Dimension ps = component.getPreferredSize ();
                        final int x;
                        final int halign = getActualHalign ( component, data );
                        if ( halign == LEFT || halign == -1 )
                        {
                            x = bi.left + om.left;
                        }
                        else if ( halign == RIGHT )
                        {
                            x = pw - bi.right - om.right - ps.width;
                        }
                        else
                        {
                            x = pw / 2 - ps.width / 2;
                        }
                        final int y;
                        final int valign = data.getValign ();
                        if ( valign == TOP || valign == -1 )
                        {
                            y = bi.top + om.top;
                        }
                        else if ( valign == BOTTOM )
                        {
                            y = ph - bi.bottom - om.bottom - ps.height;
                        }
                        else
                        {
                            y = ph / 2 - ps.height / 2;
                        }
                        component.setBounds ( x, y, halign == -1 ? pw - bi.left - om.left - bi.right - om.right : ps.width,
                                valign == -1 ? ph - bi.top - om.top - bi.bottom - om.bottom : ps.height );
                    }
                    else
                    {
                        component.setBounds ( data.getRectangleProvider ().provide () );
                    }
                }
            }
        }
    }

    protected int getActualHalign ( final Component component, final OverlayData data )
    {
        final boolean ltr = component.getComponentOrientation ().isLeftToRight ();
        if ( data.getHalign () == LEADING )
        {
            return ltr ? LEFT : RIGHT;
        }
        else if ( data.getHalign () == TRAILING )
        {
            return ltr ? RIGHT : LEFT;
        }
        else
        {
            return data.getHalign ();
        }
    }
}