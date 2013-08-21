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
import com.alee.laf.panel.WebPanel;
import com.alee.utils.SwingUtils;

import java.awt.*;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

/**
 * User: mgarin Date: 01.03.12 Time: 18:54
 */

public class WebBreadcrumb extends WebPanel
{
    private int elementOverlap = WebBreadcrumbStyle.elementOverlap;
    private Insets elementMargin = WebBreadcrumbStyle.elementMargin;
    private boolean encloseLastElement = WebBreadcrumbStyle.encloseLastElement;

    private boolean autoUpdate = true;

    public WebBreadcrumb ()
    {
        this ( true );
    }

    public WebBreadcrumb ( boolean decorated )
    {
        super ( decorated, BreadcrumbUtils.createDefaultLayout () );

        setDrawFocus ( true );

        addContainerListener ( new ContainerListener ()
        {
            @Override
            public void componentAdded ( ContainerEvent e )
            {
                if ( autoUpdate )
                {
                    updateElement ( e.getChild () );
                    updateElementTypes ();
                }
            }

            @Override
            public void componentRemoved ( ContainerEvent e )
            {
                if ( autoUpdate )
                {
                    updateElementTypes ();
                }
            }
        } );
    }

    protected boolean isAutoUpdate ()
    {
        return autoUpdate;
    }

    protected void setAutoUpdate ( boolean autoUpdate )
    {
        this.autoUpdate = autoUpdate;
    }

    public int getElementOverlap ()
    {
        return elementOverlap;
    }

    public WebBreadcrumb setElementOverlap ( int overlap )
    {
        this.elementOverlap = overlap;
        getBreadcrumbLayout ().setOverlap ( overlap + WebBreadcrumbStyle.shadeWidth );
        updateElements ();
        return this;
    }

    public Insets getElementMargin ()
    {
        return elementMargin;
    }

    public WebBreadcrumb setElementMargin ( int spacing )
    {
        return setElementMargin ( spacing, spacing, spacing, spacing );
    }

    public WebBreadcrumb setElementMargin ( int top, int left, int bottom, int right )
    {
        return setElementMargin ( new Insets ( top, left, bottom, right ) );
    }

    public WebBreadcrumb setElementMargin ( Insets margin )
    {
        this.elementMargin = margin;
        updateElements ();
        return this;
    }

    public boolean isEncloseLastElement ()
    {
        return encloseLastElement;
    }

    public void setEncloseLastElement ( boolean encloseLastElement )
    {
        this.encloseLastElement = encloseLastElement;
        updateElementTypes ();
    }

    public void updateBreadcrumb ()
    {
        revalidate ();
        repaint ();
    }

    public BreadcrumbLayout getBreadcrumbLayout ()
    {
        return ( BreadcrumbLayout ) super.getLayout ();
    }

    protected void updateElements ()
    {
        // Updating all elements settings
        for ( Component element : getComponents () )
        {
            updateElement ( element );
        }
        updateBreadcrumb ();
    }

    protected void updateElement ( Component element )
    {
        // Updating added component and its childs orientation
        SwingUtils.copyOrientation ( WebBreadcrumb.this, element );

        // Updating standart properties
        if ( element instanceof BreadcrumbElement )
        {
            BreadcrumbElement be = ( BreadcrumbElement ) element;
            updateElementType ( element, be );
            be.setOverlap ( elementOverlap );
            be.setMargin ( elementMargin );
        }
    }

    protected void updateElementTypes ()
    {
        // Updating element types
        for ( Component element : getComponents () )
        {
            if ( element instanceof BreadcrumbElement )
            {
                updateElementType ( element, ( BreadcrumbElement ) element );
            }
        }
        setLayout ( getLayout () );
        updateBreadcrumb ();
    }

    protected void updateElementType ( Component element, BreadcrumbElement be )
    {
        // Updating element type
        int index = getComponentZOrder ( element );
        int last = getComponentCount () - 1;
        if ( last == 0 && !encloseLastElement )
        {
            be.setType ( BreadcrumbElementType.none );
        }
        else if ( index == 0 )
        {
            be.setType ( BreadcrumbElementType.start );
        }
        else if ( index == last && !encloseLastElement )
        {
            be.setType ( BreadcrumbElementType.end );
        }
        else
        {
            be.setType ( BreadcrumbElementType.middle );
        }
    }
}