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

import com.alee.extended.painter.Painter;
import com.alee.laf.panel.WebPanel;

import java.awt.*;

/**
 * User: mgarin Date: 25.09.12 Time: 13:26
 */

public class WebBreadcrumbPanel extends WebPanel implements BreadcrumbElement<WebPanel>
{
    private BreadcrumbElementPainter painter;

    public WebBreadcrumbPanel ()
    {
        super ();
        initialize ();
    }

    public WebBreadcrumbPanel ( boolean decorated )
    {
        super ( decorated );
        initialize ();
    }

    public WebBreadcrumbPanel ( boolean decorated, LayoutManager layout )
    {
        super ( decorated, layout );
        initialize ();
    }

    public WebBreadcrumbPanel ( boolean decorated, Component component )
    {
        super ( decorated, component );
        initialize ();
    }

    public WebBreadcrumbPanel ( Component component )
    {
        super ( component );
        initialize ();
    }

    public WebBreadcrumbPanel ( Painter painter )
    {
        super ( painter );
        initialize ();
    }

    public WebBreadcrumbPanel ( Painter painter, Component component )
    {
        super ( painter, component );
        initialize ();
    }

    public WebBreadcrumbPanel ( LayoutManager layout )
    {
        super ( layout );
        initialize ();
    }

    public WebBreadcrumbPanel ( LayoutManager layout, boolean isDoubleBuffered )
    {
        super ( layout, isDoubleBuffered );
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