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

package com.alee.extended.breadcrumb;

import com.alee.extended.layout.BreadcrumbLayout;
import com.alee.managers.style.StyleId;
import com.alee.laf.panel.WebPanel;

import java.awt.*;

/**
 * @author Mikle Garin
 */

public class WebBreadcrumb extends WebPanel
{
    private int elementOverlap = WebBreadcrumbStyle.elementOverlap;
    private Insets elementMargin = WebBreadcrumbStyle.elementMargin;
    private boolean encloseLastElement = WebBreadcrumbStyle.encloseLastElement;

    private boolean autoUpdate = true;

    public WebBreadcrumb ()
    {
        this ( StyleId.breadcrumb );
    }

    public WebBreadcrumb ( final StyleId id )
    {
        super ( id, createDefaultLayout () );
    }

    protected boolean isAutoUpdate ()
    {
        return autoUpdate;
    }

    protected void setAutoUpdate ( final boolean autoUpdate )
    {
        this.autoUpdate = autoUpdate;
    }

    public int getElementOverlap ()
    {
        return elementOverlap;
    }

    public WebBreadcrumb setElementOverlap ( final int overlap )
    {
        this.elementOverlap = overlap;
        getBreadcrumbLayout ().setOverlap ( overlap + WebBreadcrumbStyle.shadeWidth );
        //        updateElements ();
        return this;
    }

    public Insets getElementMargin ()
    {
        return elementMargin;
    }

    public WebBreadcrumb setElementMargin ( final int spacing )
    {
        return setElementMargin ( spacing, spacing, spacing, spacing );
    }

    public WebBreadcrumb setElementMargin ( final int top, final int left, final int bottom, final int right )
    {
        return setElementMargin ( new Insets ( top, left, bottom, right ) );
    }

    public WebBreadcrumb setElementMargin ( final Insets margin )
    {
        this.elementMargin = margin;
        //        updateElements ();
        return this;
    }

    public boolean isEncloseLastElement ()
    {
        return encloseLastElement;
    }

    public void setEncloseLastElement ( final boolean encloseLastElement )
    {
        this.encloseLastElement = encloseLastElement;
        //        updateElementTypes ();
    }

    //    public void updateBreadcrumb ()
    //    {
    //        revalidate ();
    //        repaint ();
    //    }

    public BreadcrumbLayout getBreadcrumbLayout ()
    {
        return ( BreadcrumbLayout ) super.getLayout ();
    }

    //    protected void updateElements ()
    //    {
    //        // Updating all elements settings
    //        for ( final Component element : getComponents () )
    //        {
    //            updateElement ( element );
    //        }
    //        updateBreadcrumb ();
    //    }

    //    protected void updateElement ( final Component element )
    //    {
    //        // Updating added component and its children orientation
    //        SwingUtils.copyOrientation ( WebBreadcrumb.this, element );
    //
    //        // Updating standard properties
    //        if ( element instanceof BreadcrumbElement )
    //        {
    //            final BreadcrumbElement be = ( BreadcrumbElement ) element;
    //            updateElementType ( element, be );
    //            be.setOverlap ( elementOverlap );
    //            be.setMargin ( elementMargin );
    //        }
    //    }

    //    protected void updateElementTypes ()
    //    {
    //        // Updating element types
    //        for ( final Component element : getComponents () )
    //        {
    //            if ( element instanceof BreadcrumbElement )
    //            {
    //                updateElementType ( element, ( BreadcrumbElement ) element );
    //            }
    //        }
    //        setLayout ( getLayout () );
    //        updateBreadcrumb ();
    //    }

    //    protected void updateElementType ( final Component element, final BreadcrumbElement be )
    //    {
    //        // Updating element type
    //        final int index = getComponentZOrder ( element );
    //        final int last = getComponentCount () - 1;
    //        if ( last == 0 && !encloseLastElement )
    //        {
    //            be.setType ( BreadcrumbElementType.none );
    //        }
    //        else if ( index == 0 )
    //        {
    //            be.setType ( BreadcrumbElementType.start );
    //        }
    //        else if ( index == last && !encloseLastElement )
    //        {
    //            be.setType ( BreadcrumbElementType.end );
    //        }
    //        else
    //        {
    //            be.setType ( BreadcrumbElementType.middle );
    //        }
    //    }

    /**
     * Creates and returns default breadcrumb layout.
     *
     * @return default breadcrumb layout
     */
    protected static BreadcrumbLayout createDefaultLayout ()
    {
        return new BreadcrumbLayout ( WebBreadcrumbStyle.elementOverlap + WebBreadcrumbStyle.shadeWidth );
    }
}