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

package com.alee.extended.behavior;

import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

/**
 * @author Mikle Garin
 */

public abstract class ComponentVisibilityBehavior<C extends JComponent> implements HierarchyListener, ComponentListener, Behavior
{
    /**
     * Component into which this behavior is installed.
     */
    protected final C component;

    /**
     * Whether or not component is currently added to a displayable window and visible.
     * This doesn't certainly mean that component is visible on the screen for user right now.
     */
    protected boolean visible;

    /**
     * Constructs behavior for the specified component.
     *
     * @param component component into which this behavior is installed
     */
    public ComponentVisibilityBehavior ( final C component )
    {
        super ();
        this.component = component;
        this.visible = isVisible ();
    }

    /**
     * Installs behavior into component.
     */
    public void install ()
    {
        component.addHierarchyListener ( this );
        component.addComponentListener ( this );
    }

    /**
     * Uninstalls behavior from the component.
     */
    public void uninstall ()
    {
        component.removeComponentListener ( this );
        component.removeHierarchyListener ( this );
    }

    /**
     * Returns component into which this behavior is installed.
     *
     * @return component into which this behavior is installed
     */
    public C getComponent ()
    {
        return component;
    }

    @Override
    public void hierarchyChanged ( final HierarchyEvent e )
    {
        checkVisibility ();
    }

    @Override
    public void componentShown ( final ComponentEvent e )
    {
        checkVisibility ();
    }

    @Override
    public void componentHidden ( final ComponentEvent e )
    {
        checkVisibility ();
    }

    @Override
    public void componentResized ( final ComponentEvent e )
    {
        // We are not interested in this event
    }

    @Override
    public void componentMoved ( final ComponentEvent e )
    {
        // We are not interested in this event
    }

    /**
     * Performs component visibility check.
     */
    protected void checkVisibility ()
    {
        final boolean v = isVisible ();
        if ( visible != v )
        {
            if ( v )
            {
                visible = true;
                displayed ();
            }
            else
            {
                visible = false;
                hidden ();
            }
        }
    }

    /**
     * Returns whether or not component is currently added to a displayable window and visible.
     *
     * @return true if component is currently added to a displayable window and visible, false otherwise
     */
    protected boolean isVisible ()
    {
        final Window window = SwingUtils.getWindowAncestor ( component );
        return window != null && window.isDisplayable () && component.isVisible ();
    }

    /**
     * Called when component becomes visible according to behavior conditions.
     */
    public abstract void displayed ();

    /**
     * Called when component becomes hidden according to behavior conditions.
     */
    public abstract void hidden ();
}