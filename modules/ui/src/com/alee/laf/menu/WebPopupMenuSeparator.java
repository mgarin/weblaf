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

package com.alee.laf.menu;

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.log.Log;
import com.alee.utils.ReflectUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Custom menu separator component based on JSeparator.
 *
 * @author Mikle Garin
 */

public class WebPopupMenuSeparator extends JSeparator
{
    /**
     * Constructs new menu separator.
     */
    public WebPopupMenuSeparator ()
    {
        super ( JSeparator.HORIZONTAL );
    }

    /**
     * Returns separator color.
     *
     * @return separator color
     */
    public Color getColor ()
    {
        return getWebUI ().getColor ();
    }

    /**
     * Sets separator color.
     *
     * @param color new separator color
     */
    public WebPopupMenuSeparator setColor ( final Color color )
    {
        getWebUI ().setColor ( color );
        return this;
    }

    /**
     * Returns separator stroke.
     *
     * @return separator stroke
     */
    public Stroke getStroke ()
    {
        return getWebUI ().getStroke ();
    }

    /**
     * Sets separator stroke.
     *
     * @param stroke new separator stroke
     */
    public WebPopupMenuSeparator setStroke ( final Stroke stroke )
    {
        getWebUI ().setStroke ( stroke );
        return this;
    }

    /**
     * Returns separator upper and lower spacing.
     *
     * @return separator upper and lower spacing
     */
    public int getSpacing ()
    {
        return getWebUI ().getSpacing ();
    }

    /**
     * Sets separator upper and lower spacing.
     *
     * @param spacing new separator upper and lower spacing
     */
    public WebPopupMenuSeparator setSpacing ( final int spacing )
    {
        getWebUI ().setSpacing ( spacing );
        return this;
    }

    /**
     * Returns separator side spacing.
     *
     * @return separator side spacing
     */
    public int getSideSpacing ()
    {
        return getWebUI ().getSideSpacing ();
    }

    /**
     * Sets separator side spacing.
     *
     * @param sideSpacing new separator side spacing
     */
    public WebPopupMenuSeparator setSideSpacing ( final int sideSpacing )
    {
        getWebUI ().setSideSpacing ( sideSpacing );
        return this;
    }

    /**
     * Returns Web-UI applied to this class.
     *
     * @return Web-UI applied to this class
     */
    public WebPopupMenuSeparatorUI getWebUI ()
    {
        return ( WebPopupMenuSeparatorUI ) getUI ();
    }

    /**
     * Installs a Web-UI into this component.
     */
    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebPopupMenuSeparatorUI ) )
        {
            try
            {
                setUI ( ( WebPopupMenuSeparatorUI ) ReflectUtils.createInstance ( WebLookAndFeel.popupMenuSeparatorUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebPopupMenuSeparatorUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    /**
     * Returns the name of the L&F class that renders this component.
     *
     * @return the string "PopupMenuSeparatorUI"
     * @see JComponent#getUIClassID
     * @see UIDefaults#getUI
     */
    @Override
    public String getUIClassID ()
    {
        return "PopupMenuSeparatorUI";
    }
}