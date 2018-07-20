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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Pluggable look and feel interface for {@link WebLink} component.
 *
 * @param <C> component type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebLink">How to use WebLink</a>
 * @see WebLink
 */
public abstract class WLinkUI<C extends WebLink> extends WStyledLabelUI<C>
{
    /**
     * Listeners.
     */
    protected MouseAdapter linkExecutionListener;

    @Override
    public String getPropertyPrefix ()
    {
        return "Link.";
    }

    @Override
    protected void installDefaults ()
    {
        // Installing styled label defaults
        super.installDefaults ();

        // Installing link defaults
        label.setFocusable ( false );
        label.setVisitable ( true );
        label.setVisited ( false );
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
                if ( label.isEnabled () && SwingUtils.isLeftMouseButton ( e ) &&
                        BoundsType.margin.bounds ( label ).contains ( e.getPoint () ) )
                {
                    pressed = true;
                    if ( label.isFocusable () )
                    {
                        label.requestFocusInWindow ();
                    }
                }
            }

            @Override
            public void mouseReleased ( final MouseEvent e )
            {
                if ( SwingUtils.isLeftMouseButton ( e ) )
                {
                    if ( label.isEnabled () && pressed && BoundsType.margin.bounds ( label ).contains ( e.getPoint () ) )
                    {
                        label.fireLinkExecuted ();
                    }
                    pressed = false;
                }
            }
        };
        label.addMouseListener ( linkExecutionListener );
    }

    @Override
    protected void uninstallListeners ()
    {
        label.removeMouseListener ( linkExecutionListener );
        linkExecutionListener = null;

        super.uninstallListeners ();
    }
}