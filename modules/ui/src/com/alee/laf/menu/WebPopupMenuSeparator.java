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
import com.alee.managers.style.StyleId;
import com.alee.managers.style.Styleable;
import com.alee.utils.ReflectUtils;

import javax.swing.*;

/**
 * Custom menu separator component based on JSeparator.
 *
 * @author Mikle Garin
 */

public class WebPopupMenuSeparator extends JSeparator implements Styleable
{
    /**
     * Constructs new menu separator.
     */
    public WebPopupMenuSeparator ()
    {
        super ( JSeparator.HORIZONTAL );
    }

    @Override
    public StyleId getStyleId ()
    {
        return getWebUI ().getStyleId ();
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return getWebUI ().setStyleId ( id );
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