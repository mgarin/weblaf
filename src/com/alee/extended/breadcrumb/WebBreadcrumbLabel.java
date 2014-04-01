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
 * Custom label that can be used as WebBreadcrumb element.
 * It has its own unique styling and progress display.
 *
 * @author Mikle Garin
 */

public class WebBreadcrumbLabel extends WebLabel implements BreadcrumbElement
{
    protected boolean showProgress;
    protected float progress;

    public WebBreadcrumbLabel ()
    {
        super ();
        initialize ();
    }

    public WebBreadcrumbLabel ( final Icon image )
    {
        super ( image );
        initialize ();
    }

    public WebBreadcrumbLabel ( final Icon image, final int horizontalAlignment )
    {
        super ( image, horizontalAlignment );
        initialize ();
    }

    public WebBreadcrumbLabel ( final String text )
    {
        super ( text );
        initialize ();
    }

    public WebBreadcrumbLabel ( final String text, final int horizontalAlignment )
    {
        super ( text, horizontalAlignment );
        initialize ();
    }

    public WebBreadcrumbLabel ( final String text, final Icon icon )
    {
        super ( text, icon );
        initialize ();
    }

    public WebBreadcrumbLabel ( final String text, final Icon icon, final int horizontalAlignment )
    {
        super ( text, icon, horizontalAlignment );
        initialize ();
    }

    private void initialize ()
    {
        setStyleId ( WebBreadcrumb.ELEMENT_STYLE_ID );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setShowProgress ( final boolean showProgress )
    {
        this.showProgress = showProgress;
        repaint ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isShowProgress ()
    {
        return showProgress;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProgress ( final float progress )
    {
        this.progress = progress;
        repaint ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getProgress ()
    {
        return progress;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains ( final int x, final int y )
    {
        return BreadcrumbUtils.contains ( this, x, y );
    }
}