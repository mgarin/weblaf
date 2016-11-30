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

package com.alee.extended.link;

import com.alee.extended.label.WStyledLabelUI;
import com.alee.managers.style.BoundsType;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Pluggable look and feel interface for {@link WebLink} component.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebLink">How to use WebLink</a>
 * @see WebLink
 */

public abstract class WLinkUI extends WStyledLabelUI
{
    /**
     * Listeners.
     */
    protected MouseAdapter linkExecutionListener;

    /**
     * Runtime variables.
     */
    protected WebLink link;

    @Override
    public void installUI ( final JComponent c )
    {
        // Saving link reference
        link = ( WebLink ) c;

        super.installUI ( c );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        super.uninstallUI ( c );

        // Removing link reference
        link = null;
    }

    @Override
    protected String getFontKey ()
    {
        return "Link.font";
    }

    @Override
    protected void installDefaults ()
    {
        super.installDefaults ();

        link.setFocusable ( false );
        link.setVisitable ( true );
        link.setVisited ( false );
    }

    @Override
    protected void installListeners ()
    {
        super.installListeners ();

        linkExecutionListener = new MouseAdapter ()
        {
            private boolean pressed;

            @Override
            public void mousePressed ( final MouseEvent e )
            {
                if ( link.isEnabled () && SwingUtils.isLeftMouseButton ( e ) && BoundsType.margin.bounds ( link ).contains ( e.getPoint () ) )
                {
                    pressed = true;
                    if ( link.isFocusable () )
                    {
                        link.requestFocusInWindow ();
                    }
                }
            }

            @Override
            public void mouseReleased ( final MouseEvent e )
            {
                if ( SwingUtils.isLeftMouseButton ( e ) )
                {
                    if ( link.isEnabled () && pressed && BoundsType.margin.bounds ( link ).contains ( e.getPoint () ) )
                    {
                        link.fireLinkExecuted ();
                    }
                    pressed = false;
                }
            }
        };
        link.addMouseListener ( linkExecutionListener );
    }

    @Override
    protected void uninstallListeners ()
    {
        link.removeMouseListener ( linkExecutionListener );
        linkExecutionListener = null;

        super.uninstallListeners ();
    }
}