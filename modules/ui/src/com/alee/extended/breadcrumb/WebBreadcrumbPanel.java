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

import com.alee.laf.panel.WebPanel;
import com.alee.managers.style.StyleId;

import java.awt.*;

/**
 * @author Mikle Garin
 */

public class WebBreadcrumbPanel extends WebPanel implements BreadcrumbElement
{
    protected boolean showProgress;
    protected float progress;

    public WebBreadcrumbPanel ()
    {
        super ( StyleId.breadcrumbPanel );
    }

    public WebBreadcrumbPanel ( final Component component )
    {
        super ( StyleId.breadcrumbPanel, component );
    }

    public WebBreadcrumbPanel ( final LayoutManager layout, final Component... components )
    {
        super ( layout, components );
    }

    public WebBreadcrumbPanel ( final StyleId id )
    {
        super ( id );
    }

    public WebBreadcrumbPanel ( final StyleId id, final Component component )
    {
        super ( id, component );
    }

    public WebBreadcrumbPanel ( final StyleId id, final LayoutManager layout, final Component... components )
    {
        super ( id, layout, components );
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