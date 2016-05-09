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
import com.alee.managers.style.StyleId;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * @author Mikle Garin
 */

public class WebBreadcrumbButton extends WebButton implements BreadcrumbElement
{
    protected boolean showProgress;
    protected float progress;

    public WebBreadcrumbButton ()
    {
        super ( StyleId.breadcrumbButton );
    }

    public WebBreadcrumbButton ( final Icon icon )
    {
        super ( StyleId.breadcrumbButton, icon );
    }

    public WebBreadcrumbButton ( final Icon icon, final Icon rolloverIcon )
    {
        super ( StyleId.breadcrumbButton, icon, rolloverIcon );
    }

    public WebBreadcrumbButton ( final String text )
    {
        super ( StyleId.breadcrumbButton, text );
    }

    public WebBreadcrumbButton ( final String text, final Icon icon )
    {
        super ( StyleId.breadcrumbButton, text, icon );
    }

    public WebBreadcrumbButton ( final ActionListener listener )
    {
        super ( StyleId.breadcrumbButton, listener );
    }

    public WebBreadcrumbButton ( final Icon icon, final ActionListener listener )
    {
        super ( StyleId.breadcrumbButton, icon, listener );
    }

    public WebBreadcrumbButton ( final String text, final ActionListener listener )
    {
        super ( StyleId.breadcrumbButton, text, listener );
    }

    public WebBreadcrumbButton ( final String text, final Icon icon, final ActionListener listener )
    {
        super ( StyleId.breadcrumbButton, text, icon, listener );
    }

    public WebBreadcrumbButton ( final Action a )
    {
        super ( StyleId.breadcrumbButton, a );
    }

    public WebBreadcrumbButton ( final StyleId id )
    {
        super ( id );
    }

    public WebBreadcrumbButton ( final StyleId id, final Icon icon )
    {
        super ( id, icon );
    }

    public WebBreadcrumbButton ( final StyleId id, final Icon icon, final Icon rolloverIcon )
    {
        super ( id, icon, rolloverIcon );
    }

    public WebBreadcrumbButton ( final StyleId id, final String text )
    {
        super ( id, text );
    }

    public WebBreadcrumbButton ( final StyleId id, final String text, final Icon icon )
    {
        super ( id, text, icon );
    }

    public WebBreadcrumbButton ( final StyleId id, final ActionListener listener )
    {
        super ( id, listener );
    }

    public WebBreadcrumbButton ( final StyleId id, final Icon icon, final ActionListener listener )
    {
        super ( id, icon, listener );
    }

    public WebBreadcrumbButton ( final StyleId id, final String text, final ActionListener listener )
    {
        super ( id, text, listener );
    }

    public WebBreadcrumbButton ( final StyleId id, final String text, final Icon icon, final ActionListener listener )
    {
        super ( id, text, icon, listener );
    }

    public WebBreadcrumbButton ( final StyleId id, final Action a )
    {
        super ( id, a );
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