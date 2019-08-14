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
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
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
 * Abstract behavior that provides hover events for any component containing multiple items.
 * It uses mouse enter/exit/move events and component resized/moved/shown/hidden events to track hover index.
 * It might seem excessive, but simple move listener does not cover whole variety of possible cases when hover index can be changed.
 *
 * Implementing next two methods:
 * - {@link #getObjectAt(java.awt.Point)}
 * - {@link #hoverChanged(Object, Object)}
 * is sufficient for behavior usage.
 *
 * @param <C> component type
 * @param <V> hovered element type
 * @author Mikle Garin
 */
public abstract class AbstractObjectHoverBehavior<C extends JComponent, V> extends AbstractComponentBehavior<C>
        implements MouseListener, MouseMotionListener, ComponentListener, AncestorListener, DragListener, PropertyChangeListener
{

    /**
     * Whether or not behavior should only track hover events when component is enabled.
     */
    protected final boolean enabledOnly;

    /**
     * Current hover object.
     * It is saved explicitly to properly provide previous state object.
     */
    @Nullable
    protected V hoverObject;

    /**
     * Constructs new {@link AbstractObjectHoverBehavior} for the specified {@link JComponent}.
     *
     * @param component {@link JComponent} into which this behavior is installed
     */
    public AbstractObjectHoverBehavior ( @NotNull final C component )
    {
        this ( component, true );
    }

    /**
     * Constructs new {@link AbstractObjectHoverBehavior} for the specified {@link JComponent}.
     *
     * @param component   {@link JComponent} into which this behavior is installed
     * @param enabledOnly whether or not behavior should only track hover events when component is enabled
     */
    public AbstractObjectHoverBehavior ( @NotNull final C component, final boolean enabledOnly )
    {
        super ( component );
        this.enabledOnly = enabledOnly;
    }

    /**
     * Installs behavior into component.
     * todo Proper initial hover object?
     */
    public void install ()
    {
        component.addMouseListener ( this );
        component.addMouseMotionListener ( this );
        component.addAncestorListener ( this );
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
        component.removeAncestorListener ( this );
        component.removeMouseMotionListener ( this );
        component.removeMouseListener ( this );
        hoverObject = null;
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
         * This is necessary for updating hover object after {@link #mouseDragged(MouseEvent)} has finished.
         */
        updateHover ( event );
    }

    @Override
    public void mouseEntered ( @NotNull final MouseEvent event )
    {
        /**
         * Even though {@link #mouseMoved(MouseEvent)} event is always thrown along with this one when we are moving within window bounds,
         * it is still necessary for updating in case we ALT-TAB from another application.
         * todo This causes "stuck hover" effect when mouse is draged from other component.
         */
        updateHover ( event );
    }

    @Override
    public void mouseExited ( @NotNull final MouseEvent event )
    {
        /**
         * This should clear hover upon exiting component area or ALT-TAB-ing to another application.
         */
        clearHover ();
    }

    @Override
    public void mouseMoved ( @NotNull final MouseEvent event )
    {
        /**
         * Upon simple mouse movements over the component we need to constantly update hover.
         * This is the most common case, but all other checks are also necessary to avoid visual misbehavior.
         */
        updateHover ( event );
    }

    @Override
    public void mouseDragged ( @NotNull final MouseEvent event )
    {
        /**
         * Upon normal drag we keep hover updated just as we do with {@link #mouseMoved(MouseEvent)}.
         * This is NOT an actual native drag operation, but simply a mouse drag within this component.
         * Native drag is handled differently within {@link #updateHover()}.
         */
        updateHover ( event );
    }

    @Override
    public void ancestorAdded ( @NotNull final AncestorEvent event )
    {
        /**
         * Updating hover properly upon component ancestor updates.
         */
        updateHover ();
    }

    @Override
    public void ancestorRemoved ( @NotNull final AncestorEvent event )
    {
        /**
         * Updating hover properly upon component ancestor updates.
         */
        updateHover ();
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
                clearHover ();
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
     * Updates hover index.
     */
    protected void updateHover ()
    {
        // Make sure component is visible on the screen and there is no ongoing D&D operation
        if ( component.isShowing () && !DragManager.isDragging () )
        {
            final JRootPane rootPane = CoreSwingUtils.getRootPane ( component );
            if ( rootPane != null )
            {
                // Ensure that this component is the top one under the mouse
                // We have to do that to avoid displaying hover on components which recieve update events while not directly being hovered
                // This case can be easily reproduced by using scroll pane with hovering scroll bars - draging the bars should not trigger hover
                final Point windowPoint = CoreSwingUtils.getMouseLocation ( rootPane );
                final Component topComponentAt = CoreSwingUtils.getTopComponentAt ( rootPane, windowPoint );
                if ( topComponentAt == component )
                {
                    // Ensure that mouse is directly hovering component visible area
                    final Point mouseLocation = CoreSwingUtils.getMouseLocation ( component );
                    if ( component.getVisibleRect ().contains ( mouseLocation ) )
                    {
                        updateHover ( mouseLocation );
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
    protected void updateHover ( @NotNull final MouseEvent e )
    {
        updateHover ( e.getPoint () );
    }

    /**
     * Updates hover path.
     *
     * @param point mouse position on the component
     */
    protected void updateHover ( @NotNull final Point point )
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
    @Nullable
    protected abstract V getObjectAt ( @NotNull Point location );

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
    @Nullable
    protected abstract V getEmptyObject ();

    /**
     * Checks hover object change and fires event if it has changed.
     *
     * @param object hover object
     */
    protected void checkHoverChange ( @Nullable final V object )
    {
        if ( Objects.notEquals ( object, hoverObject ) )
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