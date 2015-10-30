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

import com.alee.laf.button.WebToggleButton;
import com.alee.managers.style.StyleId;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * @author Mikle Garin
 */

public class WebBreadcrumbToggleButton extends WebToggleButton implements BreadcrumbElement
{
    protected boolean showProgress;
    protected float progress;

    public WebBreadcrumbToggleButton ()
    {
        super ( StyleId.breadcrumbToggleButton);
    }

    public WebBreadcrumbToggleButton ( final Icon icon )
    {
        super (  StyleId.breadcrumbToggleButton,icon );
    }

    public WebBreadcrumbToggleButton ( final Icon icon, final boolean selected )
    {
        super ( StyleId.breadcrumbToggleButton,icon, selected );
    }

    public WebBreadcrumbToggleButton ( final String text )
    {
        super (StyleId.breadcrumbToggleButton, text );
    }

    public WebBreadcrumbToggleButton ( final String text, final boolean selected )
    {
        super ( StyleId.breadcrumbToggleButton,text, selected );
    }

    public WebBreadcrumbToggleButton ( final String text, final Icon icon )
    {
        super (StyleId.breadcrumbToggleButton, text, icon );
    }

    public WebBreadcrumbToggleButton ( final String text, final Icon icon, final boolean selected )
    {
        super ( StyleId.breadcrumbToggleButton,text, icon, selected );
    }

    public WebBreadcrumbToggleButton ( final ActionListener listener )
    {
        super (StyleId.breadcrumbToggleButton, listener );
    }

    public WebBreadcrumbToggleButton ( final Icon icon, final ActionListener listener )
    {
        super ( StyleId.breadcrumbToggleButton,icon, listener );
    }

    public WebBreadcrumbToggleButton ( final Icon icon, final boolean selected, final ActionListener listener )
    {
        super ( StyleId.breadcrumbToggleButton,icon, selected, listener );
    }

    public WebBreadcrumbToggleButton ( final String text, final ActionListener listener )
    {
        super ( StyleId.breadcrumbToggleButton,text, listener );
    }

    public WebBreadcrumbToggleButton ( final String text, final boolean selected, final ActionListener listener )
    {
        super ( StyleId.breadcrumbToggleButton,text, selected, listener );
    }

    public WebBreadcrumbToggleButton ( final String text, final Icon icon, final ActionListener listener )
    {
        super (StyleId.breadcrumbToggleButton, text, icon, listener );
    }

    public WebBreadcrumbToggleButton ( final String text, final Icon icon, final boolean selected, final ActionListener listener )
    {
        super (StyleId.breadcrumbToggleButton,text, icon, selected, listener );
    }

    public WebBreadcrumbToggleButton ( final Action a )
    {
        super ( StyleId.breadcrumbToggleButton,a );
    }

    public WebBreadcrumbToggleButton ( final StyleId id )
    {
        super ( id );
    }

    public WebBreadcrumbToggleButton ( final StyleId id, final Icon icon )
    {
        super ( id, icon );
    }

    public WebBreadcrumbToggleButton ( final StyleId id, final Icon icon, final boolean selected )
    {
        super ( id, icon, selected );
    }

    public WebBreadcrumbToggleButton ( final StyleId id, final String text )
    {
        super ( id, text );
    }

    public WebBreadcrumbToggleButton ( final StyleId id, final String text, final boolean selected )
    {
        super ( id, text, selected );
    }

    public WebBreadcrumbToggleButton ( final StyleId id, final String text, final Icon icon )
    {
        super ( id, text, icon );
    }

    public WebBreadcrumbToggleButton ( final StyleId id, final String text, final Icon icon, final boolean selected )
    {
        super ( id, text, icon, selected );
    }

    public WebBreadcrumbToggleButton ( final StyleId id, final ActionListener listener )
    {
        super ( id, listener );
    }

    public WebBreadcrumbToggleButton ( final StyleId id, final Icon icon, final ActionListener listener )
    {
        super ( id, icon, listener );
    }

    public WebBreadcrumbToggleButton ( final StyleId id, final Icon icon, final boolean selected, final ActionListener listener )
    {
        super ( id, icon, selected, listener );
    }

    public WebBreadcrumbToggleButton ( final StyleId id, final String text, final ActionListener listener )
    {
        super ( id, text, listener );
    }

    public WebBreadcrumbToggleButton ( final StyleId id, final String text, final boolean selected, final ActionListener listener )
    {
        super ( id, text, selected, listener );
    }

    public WebBreadcrumbToggleButton ( final StyleId id, final String text, final Icon icon, final ActionListener listener )
    {
        super ( id, text, icon, listener );
    }

    public WebBreadcrumbToggleButton ( final StyleId id, final String text, final Icon icon, final boolean selected, final ActionListener listener )
    {
        super ( id, text, icon, selected, listener );
    }

    public WebBreadcrumbToggleButton ( final StyleId id, final Action a )
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