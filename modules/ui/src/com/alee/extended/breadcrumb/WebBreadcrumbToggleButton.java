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
import com.alee.managers.style.skin.web.WebBreadcrumbToggleButtonPainter;

import javax.swing.*;

/**
 * @author Mikle Garin
 */

public class WebBreadcrumbToggleButton extends WebToggleButton implements BreadcrumbElement
{
    protected boolean showProgress;
    protected float progress;

    public WebBreadcrumbToggleButton ()
    {
        super ();
        initialize ();
    }

    public WebBreadcrumbToggleButton ( final Icon icon )
    {
        super ( icon );
        initialize ();
    }

    public WebBreadcrumbToggleButton ( final String text )
    {
        super ( text );
        initialize ();
    }

    public WebBreadcrumbToggleButton ( final Action a )
    {
        super ( a );
        initialize ();
    }

    public WebBreadcrumbToggleButton ( final String text, final Icon icon )
    {
        super ( text, icon );
        initialize ();
    }

    private void initialize ()
    {
        setMargin ( WebBreadcrumbStyle.elementMargin );
        setLeftRightSpacing ( 0 );
        setPainter ( new WebBreadcrumbToggleButtonPainter () );
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