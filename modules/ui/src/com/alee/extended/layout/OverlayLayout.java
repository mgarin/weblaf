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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
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
    /**
     * Positions component on the whole container area.
     */
    public static final String COMPONENT = "COMPONENT";

    /**
     * Positions the component depending on WebOverlay settings.
     */
    public static final String OVERLAY = "OVERLAY";

    /**
     * Saved layout constraints.
     */
    protected Map<Component, String> constraints = new HashMap<Component, String> ();

    /**
     * Component side margins (this one is additional to the container margins).
     */
    protected Insets componentMargin = null;

    /**
     * Overlay side margins (this one is additional to the container margins).
     */
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
    public void addComponent ( @NotNull final Component component, @Nullable final Object constraints )
    {
        final String value = ( String ) constraints;
        if ( value == null || !value.equals ( COMPONENT ) && !value.equals ( OVERLAY ) )
        {
            throw new IllegalArgumentException ( "Cannot add to layout: constraint must be 'COMPONENT' or 'OVERLAY' string" );
        }
        this.constraints.put ( component, value );
    }

    @Override
    public void removeComponent ( @NotNull final Component component )
    {
        this.constraints.remove ( component );
    }

    @NotNull
    @Override
    public Dimension preferredLayoutSize ( @NotNull final Container container )
    {
        final Insets bi = container.getInsets ();
        final Insets ci = getActualComponentInsets ( container );
        Dimension ps = new Dimension ();
        for ( final Component component : container.getComponents () )
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

    protected Insets getActualComponentInsets ( final Container container )
    {
        return componentMargin != null ?
                container.getComponentOrientation ().isLeftToRight () ? componentMargin : SwingUtils.toRTL ( componentMargin ) :
                new Insets ( 0, 0, 0, 0 );
    }

    @Override
    public void layoutContainer ( @NotNull final Container container )
    {
        final Insets bi = container.getInsets ();
        final Insets ci = getActualComponentInsets ( container );
        for ( final Component component : container.getComponents () )
        {
            final String constraint = constraints.get ( component );
            if ( constraint != null )
            {
                final int pw = container.getWidth ();
                final int ph = container.getHeight ();
                if ( constraint.equals ( COMPONENT ) )
                {
                    final int x = bi.left + ci.left;
                    final int y = bi.top + ci.top;
                    final int w = pw - bi.left - bi.right - ci.left - ci.right;
                    final int h = ph - bi.top - bi.bottom - ci.top - ci.bottom;
                    component.setBounds ( limit ( pw, ph, new Rectangle ( x, y, w, h ), bi ) );
                }
                else if ( constraint.equals ( OVERLAY ) )
                {
                    final WebOverlay webOverlay = ( WebOverlay ) container;
                    final OverlayData data = webOverlay.getOverlayData ( ( JComponent ) component );
                    final Insets om = overlayMargin != null ? overlayMargin : new Insets ( 0, 0, 0, 0 );
                    final Rectangle bounds;
                    if ( data.getLocation ().equals ( OverlayLocation.fill ) )
                    {
                        bounds = new Rectangle ( bi.left + om.left, bi.top + om.top, pw - bi.left - om.left - bi.right - om.right,
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
                        final int w = halign == -1 ? pw - bi.left - om.left - bi.right - om.right : ps.width;
                        final int h = valign == -1 ? ph - bi.top - om.top - bi.bottom - om.bottom : ps.height;
                        bounds = new Rectangle ( x, y, w, h );
                    }
                    else
                    {
                        bounds = new Rectangle ( data.getBoundsSupplier ().get () );
                    }
                    component.setBounds ( limit ( pw, ph, bounds, bi ) );
                }
            }
        }
    }

    protected Rectangle limit ( final int pw, final int ph, final Rectangle bounds, final Insets insets )
    {
        final int x = Math.max ( bounds.x, insets.left );
        final int y = Math.max ( bounds.y, insets.top );
        final int w = Math.min ( bounds.width, pw - insets.left - insets.right );
        final int h = Math.min ( bounds.height, ph - insets.top - insets.bottom );
        return new Rectangle ( x, y, w, h );
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