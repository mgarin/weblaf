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

import com.alee.laf.label.WebLabel;

import javax.swing.*;

/**
 * User: mgarin Date: 02.03.12 Time: 12:29
 */

public class WebBreadcrumbLabel extends WebLabel implements BreadcrumbElement<WebLabel>
{
    private BreadcrumbElementPainter painter;

    public WebBreadcrumbLabel ()
    {
        super ();
        initialize ();
    }

    public WebBreadcrumbLabel ( Icon image )
    {
        super ( image );
        initialize ();
    }

    public WebBreadcrumbLabel ( Icon image, int horizontalAlignment )
    {
        super ( image, horizontalAlignment );
        initialize ();
    }

    public WebBreadcrumbLabel ( String text )
    {
        super ( text );
        initialize ();
    }

    public WebBreadcrumbLabel ( String text, int horizontalAlignment )
    {
        super ( text, horizontalAlignment );
        initialize ();
    }

    public WebBreadcrumbLabel ( String text, Icon icon )
    {
        super ( text, icon );
        initialize ();
    }

    public WebBreadcrumbLabel ( String text, Icon icon, int horizontalAlignment )
    {
        super ( text, icon, horizontalAlignment );
        initialize ();
    }

    private void initialize ()
    {
        setMargin ( WebBreadcrumbStyle.elementMargin );

        painter = new BreadcrumbElementPainter ();
        setPainter ( painter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setType ( BreadcrumbElementType type )
    {
        painter.setType ( type );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOverlap ( int overlap )
    {
        painter.setOverlap ( overlap );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setShowProgress ( boolean showProgress )
    {
        painter.setShowProgress ( showProgress );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProgress ( float progress )
    {
        painter.setProgress ( progress );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BreadcrumbElementPainter getPainter ()
    {
        return painter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains ( int x, int y )
    {
        return BreadcrumbUtils.contains ( x, y, this );
    }
}