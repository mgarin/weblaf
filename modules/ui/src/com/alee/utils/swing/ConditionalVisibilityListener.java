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

package com.alee.utils.swing;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * This listener allows a quick creation of component visibility change action on any focus changes. You can also invert the way visibility
 * changes on focus change.
 */
public class ConditionalVisibilityListener implements FocusListener, MouseListener
{
    private Boolean focusedVisible;
    private Boolean mouseoverVisible;
    private Component component;
    private boolean focused = false;
    private boolean mouseover = false;

    public static void install ( Component tracked, Component component, Boolean focusedVisible, Boolean mouseoverVisible )
    {
        ConditionalVisibilityListener cvl = new ConditionalVisibilityListener ( component, focusedVisible, mouseoverVisible );
        if ( focusedVisible != null )
        {
            tracked.addFocusListener ( cvl );
        }
        if ( mouseoverVisible != null )
        {
            tracked.addMouseListener ( cvl );
        }
    }

    public ConditionalVisibilityListener ( Component component )
    {
        this ( component, null, null );
    }

    public ConditionalVisibilityListener ( Component component, Boolean focusedVisible, Boolean mouseoverVisible )
    {
        super ();
        setComponent ( component );
        setFocusedVisible ( focusedVisible );
        setMouseoverVisible ( mouseoverVisible );
    }

    public Boolean getFocusedVisible ()
    {
        return focusedVisible;
    }

    public void setFocusedVisible ( Boolean focusedVisible )
    {
        this.focusedVisible = focusedVisible;
    }

    public Boolean getMouseoverVisible ()
    {
        return mouseoverVisible;
    }

    public void setMouseoverVisible ( Boolean mouseoverVisible )
    {
        this.mouseoverVisible = mouseoverVisible;
    }

    public Component getComponent ()
    {
        return component;
    }

    public void setComponent ( Component component )
    {
        this.component = component;
    }

    @Override
    public void focusGained ( FocusEvent e )
    {
        if ( focusedVisible != null )
        {
            focused = true;
            updateVisibility ();
        }
    }

    @Override
    public void focusLost ( FocusEvent e )
    {
        if ( focusedVisible != null )
        {
            focused = false;
            updateVisibility ();
        }
    }

    @Override
    public void mouseClicked ( MouseEvent e )
    {
        //
    }

    @Override
    public void mousePressed ( MouseEvent e )
    {
        //
    }

    @Override
    public void mouseReleased ( MouseEvent e )
    {
        //
    }

    @Override
    public void mouseEntered ( MouseEvent e )
    {
        if ( mouseoverVisible != null )
        {
            mouseover = true;
            updateVisibility ();
        }
    }

    @Override
    public void mouseExited ( MouseEvent e )
    {
        if ( mouseoverVisible != null )
        {
            mouseover = false;
            updateVisibility ();
        }
    }

    public void updateVisibility ()
    {
        component.setVisible ( isVisible () );
    }

    public boolean isVisible ()
    {
        boolean f = focusedVisible == null || focusedVisible == focused;
        boolean m = mouseoverVisible == null || mouseoverVisible == mouseover;
        return f && m;
    }
}
