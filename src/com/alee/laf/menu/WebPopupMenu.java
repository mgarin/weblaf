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
import com.alee.utils.ReflectUtils;
import com.alee.utils.laf.ShapeProvider;

import javax.swing.*;
import java.awt.*;

/**
 * This JPopupMenu extension class provides a direct access to WebPopupMenuUI methods.
 * It also has a few additional methods for easy popup positioning on display.
 *
 * @author Mikle Garin
 */

public class WebPopupMenu extends JPopupMenu implements ShapeProvider
{
    /**
     * Constructs new popup menu.
     */
    public WebPopupMenu ()
    {
        super ();
    }

    /**
     * Constructs new popup menu with the specified label.
     *
     * @param label popup menu label
     */
    public WebPopupMenu ( final String label )
    {
        super ( label );
    }

    /**
     * Displays popup menu under the invoker component.
     * This method also takes into account component orientation.
     *
     * @param invoker invoker component
     */
    public void showUnder ( final Component invoker )
    {
        final boolean ltr = invoker.getComponentOrientation ().isLeftToRight ();
        show ( invoker, ltr ? 0 : invoker.getWidth () - getPreferredSize ().width, invoker.getHeight () );
    }

    /**
     * Displays popup menu above the invoker component.
     * This method also takes into account component orientation.
     *
     * @param invoker invoker component
     */
    public void showAbove ( final Component invoker )
    {
        final boolean ltr = invoker.getComponentOrientation ().isLeftToRight ();
        final Dimension ps = getPreferredSize ();
        show ( invoker, ltr ? 0 : invoker.getWidth () - ps.width, -ps.height );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape provideShape ()
    {
        return getWebUI ().provideShape ();
    }

    /**
     * Returns Web-UI applied to this class.
     *
     * @return Web-UI applied to this class
     */
    public WebPopupMenuUI getWebUI ()
    {
        return ( WebPopupMenuUI ) getUI ();
    }

    /**
     * Installs a Web-UI into this component.
     */
    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebPopupMenuUI ) )
        {
            try
            {
                setUI ( ( WebPopupMenuUI ) ReflectUtils.createInstance ( WebLookAndFeel.popupMenuUI ) );
            }
            catch ( Throwable e )
            {
                e.printStackTrace ();
                setUI ( new WebPopupMenuUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }
}