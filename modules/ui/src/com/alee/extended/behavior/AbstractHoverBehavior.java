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

import com.alee.api.annotations.NotNull;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.drag.DragListener;
import com.alee.managers.drag.DragManager;
import com.alee.utils.CoreSwingUtils;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Custom {@link Behavior} that allows you to track component mouse hover.
 * You need to specify {@link JComponent} for which mouse hover state will be tracked.
 * Use {@link #install()} and {@link #uninstall()} methods to setup and remove this behavior.
 *
 * @param <C> component type
 * @author Mikle Garin
 * @deprecated
 */
@Deprecated
public abstract class AbstractHoverBehavior<C extends JComponent> extends AbstractComponentBehavior<C>
        implements MouseListener, ComponentListener, HierarchyListener, AncestorListener, DragListener, PropertyChangeListener
{
    /**
     * Whether or not behavior should only track hover state events when component is enabled.
     */
    protected boolean enabledOnly;

    /**
     * Last hover state.
     */
    protected boolean hover;

    /**
     * Constructs new {@link AbstractHoverBehavior} for the specified {@link JComponent}.
     *
     * @param component   {@link JComponent} into which this behavior is installed
     * @param enabledOnly whether or not behavior should only track hover state events when component is enabled
     */
    public AbstractHoverBehavior ( @NotNull final C component, final boolean enabledOnly )
    {
        super ( component );
        this.enabledOnly = enabledOnly;
    }

    /**
     * Installs behavior into component.
     */
    public void install ()
    {
        hover = CoreSwingUtils.isHovered ( component );
        component.addMouseListener ( this );
        component.addAncestorListener ( this );
        component.addHierarchyListener ( this );
        component.addComponentListener ( this );
        component.addPropertyChangeListener ( WebLookAndFeel.ENABLED_PROPERTY, this );
        DragManager.addDragListener ( component, this );
    }

    /**
     * Uninstalls behavior from the component.
     */
    public void uninstall ()
    {
        DragManager.removeDragListener ( component, this );
        component.removePropertyChangeListener ( WebLookAndFeel.ENABLED_PROPERTY, this );
        component.removeComponentListener ( this );
        component.removeHierarchyListener ( this );
        component.removeAncestorListener ( this );
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
    public void mouseClicked ( @NotNull final MouseEvent event )
    {
        /**
         * No extra updates required on this event.
         */
    }

    @Override
    public void mousePressed ( @NotNull final MouseEvent event )
    {
        /**
         * No extra updates required on this event.
         */
    }

    @Override
    public void mouseReleased ( @NotNull final MouseEvent event )
    {
        /**
         * No extra updates required on this event.
         */
    }

    @Override
    public void mouseEntered ( @NotNull final MouseEvent event )
    {
        /**
         * This should update hover upon entering component area or ALT-TAB-ing into applicaton.
         */
        setHover ( true );
    }

    @Override
    public void mouseExited ( @NotNull final MouseEvent event )
    {
        /**
         * This should clear hover upon exiting component area or ALT-TAB-ing to another application.
         */
        setHover ( false );
    }

    @Override
    public void ancestorMoved ( @NotNull final AncestorEvent event )
    {
        /**
         * Updating hover properly upon component ancestor updates.
         */
        updateHover ();
    }

    @Override
    public void ancestorAdded ( @NotNull final AncestorEvent event )
    {
        /**
         * No extra updates required on this event.
         */
    }

    @Override
    public void ancestorRemoved ( @NotNull final AncestorEvent event )
    {
        /**
         * No extra updates required on this event.
         */
    }

    @Override
    public void hierarchyChanged ( @NotNull final HierarchyEvent event )
    {
        /**
         * Whenever component hierarchy changes we might have to update hover.
         * It is hard to say here what exactly should happen with hover, so it is left for {@link #updateHover()} operation to find out.
         */
        if ( event.getID () == HierarchyEvent.HIERARCHY_CHANGED )
        {
            updateHover ();
        }
    }

    @Override
    public void componentResized ( @NotNull final ComponentEvent event )
    {
        /**
         * Whenever component size or state changes we might have to update hover.
         * It is hard to say here what exactly should happen with hover, so it is left for {@link #updateHover()} operation to find out.
         */
        updateHover ();
    }

    @Override
    public void componentMoved ( @NotNull final ComponentEvent event )
    {
        /**
         * Whenever component size or state changes we might have to update hover.
         * It is hard to say here what exactly should happen with hover, so it is left for {@link #updateHover()} operation to find out.
         */
        updateHover ();
    }

    @Override
    public void componentShown ( @NotNull final ComponentEvent event )
    {
        /**
         * Whenever component size or state changes we might have to update hover.
         * It is hard to say here what exactly should happen with hover, so it is left for {@link #updateHover()} operation to find out.
         */
        updateHover ();
    }

    @Override
    public void componentHidden ( @NotNull final ComponentEvent event )
    {
        /**
         * Whenever component size or state changes we might have to update hover.
         * It is hard to say here what exactly should happen with hover, so it is left for {@link #updateHover()} operation to find out.
         */
        updateHover ();
    }

    @Override
    public void propertyChange ( @NotNull final PropertyChangeEvent event )
    {
        /**
         * Since hover state might be dependant on enabled state we have to update it properly.
         */
        if ( enabledOnly )
        {
            if ( component.isEnabled () )
            {
                updateHover ();
            }
            else
            {
                setHover ( false );
            }
        }
    }

    @Override
    public void started ( @NotNull final DragSourceDragEvent event )
    {
        /**
         * No extra updates required on this event.
         */
    }

    @Override
    public void entered ( @NotNull final DragSourceDragEvent event )
    {
        /**
         * Updating hover on drag enter.
         */
        updateHover ();
    }

    @Override
    public void moved ( @NotNull final DragSourceDragEvent event )
    {
        /**
         * No extra updates required on this event.
         */
    }

    @Override
    public void exited ( @NotNull final DragSourceEvent event )
    {
        /**
         * Updating hover on drag exit.
         */
        updateHover ();
    }

    @Override
    public void finished ( @NotNull final DragSourceDropEvent event )
    {
        /**
         * Updating hover on drag finish.
         */
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