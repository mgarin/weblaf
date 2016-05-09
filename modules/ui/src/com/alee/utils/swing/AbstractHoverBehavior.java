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

import com.alee.api.Behavior;
import com.alee.managers.drag.DragManager;
import com.alee.utils.CoreSwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Mikle Garin
 */

public abstract class AbstractHoverBehavior<C extends JComponent> extends MouseAdapter implements ComponentListener, Behavior
{
    /**
     * Component into which this behavior is installed.
     */
    protected final C component;

    /**
     * Whether or not behavior should only track hover state events when component is enabled.
     */
    protected boolean enabledOnly;

    /**
     * Last hover state.
     */
    protected boolean hover;

    /**
     * Constructs behavior for the specified component.
     *
     * @param component component into which this behavior is installed
     */
    public AbstractHoverBehavior ( final C component )
    {
        this ( component, true );
    }

    /**
     * Constructs behavior for the specified component.
     *
     * @param component   component into which this behavior is installed
     * @param enabledOnly whether or not behavior should only track hover state events when component is enabled
     */
    public AbstractHoverBehavior ( final C component, final boolean enabledOnly )
    {
        super ();
        this.enabledOnly = enabledOnly;
        this.component = component;
        this.hover = false;
    }

    /**
     * Installs behavior into component.
     */
    public void install ()
    {
        component.addMouseListener ( this );
        component.addMouseMotionListener ( this );
        component.addComponentListener ( this );
    }

    /**
     * Uninstalls behavior from the component.
     */
    public void uninstall ()
    {
        component.removeMouseListener ( this );
        component.removeMouseMotionListener ( this );
        component.removeComponentListener ( this );
    }

    /**
     * Returns whether or not behavior should only track hover state events when component is enabled.
     *
     * @return true if behavior should only track hover state events when component is enabled, false otherwise
     */
    public boolean isEnabledOnly ()
    {
        return enabledOnly;
    }

    /**
     * Sets whether or not behavior should only track hover state events when component is enabled.
     *
     * @param enabledOnly whether or not behavior should only track hover state events when component is enabled
     */
    public void setEnabledOnly ( final boolean enabledOnly )
    {
        this.enabledOnly = enabledOnly;
        updateHover ();
    }

    @Override
    public void mouseEntered ( final MouseEvent e )
    {
        setHover ( true );
    }

    @Override
    public void mouseExited ( final MouseEvent e )
    {
        setHover ( false );
    }

    @Override
    public void componentResized ( final ComponentEvent e )
    {
        updateHover ();
    }

    @Override
    public void componentMoved ( final ComponentEvent e )
    {
        updateHover ();
    }

    @Override
    public void componentShown ( final ComponentEvent e )
    {
        updateHover ();
    }

    @Override
    public void componentHidden ( final ComponentEvent e )
    {
        updateHover ();
    }

    /**
     * Updates hover state.
     */
    protected void updateHover ()
    {
        if ( component.isShowing () && !DragManager.isDragging () )
        {
            final Point mousePoint = CoreSwingUtils.getMousePoint ( component );
            if ( component.getVisibleRect ().contains ( mousePoint ) )
            {
                setHover ( true );
            }
            else
            {
                setHover ( false );
            }
        }
        else
        {
            setHover ( false );
        }
    }

    /**
     * Updates hover state.
     *
     * @param hover whether or not component is in hover state
     */
    protected void setHover ( final boolean hover )
    {
        // Disabled components aren't affected
        if ( ( !enabledOnly || component.isEnabled () ) && this.hover != hover )
        {
            this.hover = hover;
            hoverChanged ( hover );
        }
    }

    /**
     * Informs when component hover state changes.
     *
     * @param hover whether or not component is in hover state
     */
    public abstract void hoverChanged ( boolean hover );
}