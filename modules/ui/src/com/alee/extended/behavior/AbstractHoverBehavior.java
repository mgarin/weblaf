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

import com.alee.managers.drag.DragListener;
import com.alee.managers.drag.DragManager;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.event.*;

/**
 * Custom {@link Behavior} that allows you to track component mouse hover.
 * You need to specify {@link JComponent} which mouse hover state will be tracked.
 * Use {@link #install()} and {@link #uninstall()} methods to setup and remove this behavior.
 *
 * @author Mikle Garin
 */

public abstract class AbstractHoverBehavior<C extends JComponent> extends MouseAdapter
        implements ComponentListener, HierarchyListener, AncestorListener, DragListener, Behavior
{
    /**
     * todo 1. Track hover when some child component is in the way as well (like in CSS)
     */

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
     * Constructs new {@link AbstractHoverBehavior} for the specified component.
     *
     * @param component component into which this behavior is installed
     */
    public AbstractHoverBehavior ( final C component )
    {
        this ( component, true );
    }

    /**
     * Constructs new {@link AbstractHoverBehavior} for the specified component.
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
        hover = SwingUtils.isHovered ( component );
        component.addMouseListener ( this );
        component.addMouseMotionListener ( this );
        component.addAncestorListener ( this );
        component.addHierarchyListener ( this );
        component.addComponentListener ( this );
        DragManager.addDragListener ( this );
    }

    /**
     * Uninstalls behavior from the component.
     */
    public void uninstall ()
    {
        DragManager.removeDragListener ( this );
        component.removeComponentListener ( this );
        component.removeHierarchyListener ( this );
        component.removeAncestorListener ( this );
        component.removeMouseMotionListener ( this );
        component.removeMouseListener ( this );
        hover = false;
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

    /**
     * Returns current hover state.
     *
     * @return current hover state
     */
    public boolean isHover ()
    {
        return hover;
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
    public void ancestorAdded ( final AncestorEvent event )
    {
        //
    }

    @Override
    public void ancestorRemoved ( final AncestorEvent event )
    {
        //
    }

    @Override
    public void ancestorMoved ( final AncestorEvent event )
    {
        updateHover ();
    }

    @Override
    public void hierarchyChanged ( final HierarchyEvent e )
    {
        if ( e.getID () == HierarchyEvent.HIERARCHY_CHANGED )
        {
            updateHover ();
        }
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
    public void mouseDragged ( final MouseEvent e )
    {
        updateHover ();
    }

    @Override
    public void componentShown ( final ComponentEvent e )
    {
        //
    }

    @Override
    public void componentHidden ( final ComponentEvent e )
    {
        //
    }

    @Override
    public void started ( final DragSourceDragEvent event )
    {
        //
    }

    @Override
    public void entered ( final DragSourceDragEvent event )
    {
        updateHover ();
    }

    @Override
    public void moved ( final DragSourceDragEvent event )
    {
        //
    }

    @Override
    public void exited ( final DragSourceEvent event )
    {
        updateHover ();
    }

    @Override
    public void finished ( final DragSourceDropEvent event )
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
            final Point mousePoint = CoreSwingUtils.getMouseLocation ( component );
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