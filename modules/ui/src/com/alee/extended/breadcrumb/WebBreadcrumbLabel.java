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

import com.alee.managers.style.StyleId;
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
        super ( StyleId.breadcrumbLabel );
    }

    public WebBreadcrumbLabel ( final Icon icon )
    {
        super ( StyleId.breadcrumbLabel, icon );
    }

    public WebBreadcrumbLabel ( final int horizontalAlignment )
    {
        super ( StyleId.breadcrumbLabel, horizontalAlignment );
    }

    public WebBreadcrumbLabel ( final Icon icon, final int horizontalAlignment )
    {
        super ( StyleId.breadcrumbLabel, icon, horizontalAlignment );
    }

    public WebBreadcrumbLabel ( final String text )
    {
        super ( StyleId.breadcrumbLabel, text );
    }

    public WebBreadcrumbLabel ( final String text, final int horizontalAlignment, final Object... data )
    {
        super ( StyleId.breadcrumbLabel, text, horizontalAlignment, data );
    }

    public WebBreadcrumbLabel ( final String text, final Icon icon )
    {
        super ( StyleId.breadcrumbLabel, text, icon );
    }

    public WebBreadcrumbLabel ( final String text, final Icon icon, final int horizontalAlignment, final Object... data )
    {
        super ( StyleId.breadcrumbLabel, text, icon, horizontalAlignment, data );
    }

    public WebBreadcrumbLabel ( final StyleId id )
    {
        super ( id );
    }

    public WebBreadcrumbLabel ( final StyleId id, final Icon icon )
    {
        super ( id, icon );
    }

    public WebBreadcrumbLabel ( final StyleId id, final int horizontalAlignment )
    {
        super ( id, horizontalAlignment );
    }

    public WebBreadcrumbLabel ( final StyleId id, final Icon icon, final int horizontalAlignment )
    {
        super ( id, icon, horizontalAlignment );
    }

    public WebBreadcrumbLabel ( final StyleId id, final String text )
    {
        super ( id, text );
    }

    public WebBreadcrumbLabel ( final StyleId id, final String text, final int horizontalAlignment, final Object... data )
    {
        super ( id, text, horizontalAlignment, data );
    }

    public WebBreadcrumbLabel ( final StyleId id, final String text, final Icon icon )
    {
        super ( id, text, icon );
    }

    public WebBreadcrumbLabel ( final StyleId id, final String text, final Icon icon, final int horizontalAlignment, final Object... data )
    {
        super ( id, text, icon, horizontalAlignment, data );
    }

    @Override
    public void setShowProgress ( final boolean showProgress )
    {
        this.showProgress = showProgress;
        repaint ();
    }

    @Override
    public boolean isShowProgress ()
    {
        return showProgress;
    }

    @Override
    public void setProgress ( final float progress )
    {
        this.progress = progress;
        repaint ();
    }

    @Override
    public float getProgress ()
    {
        return progress;
    }

    @Override
    public boolean contains ( final int x, final int y )
    {
        return BreadcrumbUtils.contains ( this, x, y );
    }
}