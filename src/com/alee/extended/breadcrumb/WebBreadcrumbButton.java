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

import com.alee.laf.button.WebButton;

import javax.swing.*;

/**
 * User: mgarin Date: 01.03.12 Time: 19:07
 */

public class WebBreadcrumbButton extends WebButton implements BreadcrumbElement<WebButton>
{
    private BreadcrumbElementPainter painter;

    public WebBreadcrumbButton ()
    {
        super ();
        initialize ();
    }

    public WebBreadcrumbButton ( Icon icon )
    {
        super ( icon );
        initialize ();
    }

    public WebBreadcrumbButton ( String text )
    {
        super ( text );
        initialize ();
    }

    public WebBreadcrumbButton ( Action a )
    {
        super ( a );
        initialize ();
    }

    public WebBreadcrumbButton ( String text, Icon icon )
    {
        super ( text, icon );
        initialize ();
    }

    private void initialize ()
    {
        setMargin ( WebBreadcrumbStyle.elementMargin );
        setLeftRightSpacing ( 0 );

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