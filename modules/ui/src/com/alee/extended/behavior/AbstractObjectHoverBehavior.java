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

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.drag.DragListener;
import com.alee.managers.drag.DragManager;
import com.alee.utils.CompareUtils;
import com.alee.utils.CoreSwingUtils;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Abstract behavior that provides hover events for any component containing multiple items.
 * It uses mouse enter/exit/move events and component resized/moved/shown/hidden events to track hover index.
 * It might seem excessive, but simple move listener does not cover whole variety of possible cases when hover index can be changed.
 * <p/>
 * Implementing {@link #getObjectAt(java.awt.Point)} and {@link #hoverChanged(Object, Object)} methods is sufficient for further usage of
 * this behavior in any custom component it is being implemented for.
 *
 * @param <C> component type
 * @param <V> hovered element type
 * @author Mikle Garin
 */

public abstract class AbstractObjectHoverBehavior<C extends JComponent, V> extends MouseAdapter
        implements ComponentListener, AncestorListener, DragListener, PropertyChangeListener, Behavior
{
    /**
     * Component into which this behavior is installed.
     */
    protected final C component;

    /**
     * Whether or not behavior should only track hover events when component is enabled.
     */
    protected final boolean enabledOnly;

    /**
     * Current hover object.
     * It is saved explicitly to properly provide previous state object.
     */
    protected V hoverObject;

    /**
     * Constructs behavior for the specified component.
     *
     * @param component component into which this behavior is installed
     */
    public AbstractObjectHoverBehavior ( final C component )
    {
        this ( component, true );
    }

    /**
     * Constructs behavior for the specified component.
     *
     * @param component   component into which this behavior is installed
     * @param enabledOnly whether or not behavior should only track hover events when component is enabled
     */
    public AbstractObjectHoverBehavior ( final C component, final boolean enabledOnly )
    {
        super ();
        this.enabledOnly = enabledOnly;
        this.component = component;
    }

    /**
     * Installs behavior into component.
     */
    public void install ()
    {
        component.addMouseListener ( this );
        component.addMouseMotionListener ( this );
        component.addAncestorListener ( this );
        component.addComponentListener ( this );
        DragManager.addDragListener ( this );
        component.addPropertyChangeListener ( WebLookAndFeel.ENABLED_PROPERTY, this );
    }

    /**
     * Uninstalls behavior from the component.
     */
    public void uninstall ()
    {
        component.removePropertyChangeListener ( WebLookAndFeel.ENABLED_PROPERTY, this );
        DragManager.removeDragListener ( this );
        component.removeComponentListener ( this );
        component.removeAncestorListener ( this );
        component.removeMouseMotionListener ( this );
        component.removeMouseListener ( this );
    }

    @Override
    public void mouseEntered ( final MouseEvent event )
    {
        updateHover ( event );
    }

    @Override
    public void mouseMoved ( final MouseEvent event )
    {
        updateHover ( event );
    }

    @Override
    public void mouseDragged ( final MouseEvent event )
    {
        updateHover ( event );
    }

    @Override
    public void mouseExited ( final MouseEvent event )
    {
        clearHover ();
    }

    @Override
    public void ancestorAdded ( final AncestorEvent event )
    {
        updateHover ();
    }

    @Override
    public void ancestorRemoved ( final AncestorEvent event )
    {
        updateHover ();
    }

    @Override
    public void ancestorMoved ( final AncestorEvent event )
    {
        updateHover ();
    }

    @Override
    public void componentResized ( final ComponentEvent event )
    {
        updateHover ();
    }

    @Override
    public void componentMoved ( final ComponentEvent event )
    {
        updateHover ();
    }

    @Override
    public void componentShown ( final ComponentEvent event )
    {
        updateHover ();
    }

    @Override
    public void componentHidden ( final ComponentEvent event )
    {
        updateHover ();
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

    @Override
    public void propertyChange ( final PropertyChangeEvent event )
    {
        if ( enabledOnly )
        {
            if ( component.isEnabled () )
            {
                updateHover ();
            }
            else
            {
                clearHover ();
            }
        }
    }

    /**
     * Updates hover index.
     */
    protected void updateHover ()
    {
        if ( component.isShowing () && !DragManager.isDragging () )
        {
            final Point mousePoint = CoreSwingUtils.getMousePoint ( component );
            if ( component.getVisibleRect ().contains ( mousePoint ) )
            {
                updateHover ( mousePoint );
            }
            else
            {
                clearHover ();
            }
        }
        else
        {
            clearHover ();
        }
    }

    /**
     * Updates hover index.
     *
     * @param e mouse event
     */
    protected void updateHover ( final MouseEvent e )
    {
        updateHover ( e.getPoint () );
    }

    /**
     * Updates hover path.
     *
     * @param point mouse position on the component
     */
    protected void updateHover ( final Point point )
    {
        // Disabled components aren't affected
        if ( !enabledOnly || component.isEnabled () )
        {
            checkHoverChange ( getObjectAt ( point ) );
        }
    }

    /**
     * Returns object at the specified location.
     *
     * @param location hovered location
     * @return object at the specified location
     */
    protected abstract V getObjectAt ( Point location );

    /**
     * Clears hover path.
     */
    protected void clearHover ()
    {
        checkHoverChange ( getEmptyObject () );
    }

    /**
     * Returns empty hover object value.
     *
     * @return empty hover object value
     */
    protected abstract V getEmptyObject ();

    /**
     * Checks hover object change and fires event if it has changed.
     *
     * @param object hover object
     */
    protected void checkHoverChange ( final V object )
    {
        if ( !CompareUtils.equals ( object, hoverObject ) )
        {
            final V previousPath = hoverObject;
            this.hoverObject = object;
            hoverChanged ( previousPath, hoverObject );
        }
    }

    /**
     * Informs when hover object changes.
     *
     * @param previous previous hover object
     * @param current  current hover object
     */
    public abstract void hoverChanged ( V previous, V current );
}