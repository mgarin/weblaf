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

package com.alee.managers.drag.view;

import com.alee.utils.CoreSwingUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.image.BufferedImage;
import java.lang.ref.WeakReference;

/**
 * Abstract {@link DragViewHandler} implementation that displays semi-transparent component preview.
 *
 * @param <C> dragged component type
 * @param <T> dragged data type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-DragManager">How to use DragManager</a>
 * @see com.alee.managers.drag.DragManager
 */
public abstract class ComponentDragViewHandler<C extends JComponent, T> implements DragViewHandler<T>
{
    /**
     * Initial mouse location on dragged component.
     * Used to position component snapshot correctly under the mouse.
     */
    protected Point location;

    /**
     * Dragged object reference.
     */
    protected WeakReference<C> reference;

    @Override
    public BufferedImage getView ( final T object, final DragSourceDragEvent event )
    {
        // Retrieving dragged component
        final C component = getComponent ( object, event );

        // Saving initial mouse location
        if ( reference == null || reference.get () != component )
        {
            location = calculateViewRelativeLocation ( component, event );
            reference = new WeakReference<C> ( component );
        }

        // Returning component snapshot
        return createComponentView ( component );
    }

    /**
     * Returns image object representation location relative to mouse location.
     *
     * @param component dragged component
     * @param event     drag event
     * @return image object representation location relative to mouse location
     */
    protected Point calculateViewRelativeLocation ( final C component, final DragSourceDragEvent event )
    {
        final Point los = CoreSwingUtils.locationOnScreen ( component );
        final Point eloc = event.getLocation ();
        return new Point ( los.x - eloc.x, los.y - eloc.y );
    }

    /**
     * Returns component view image.
     *
     * @param component dragged component
     * @return component view image
     */
    protected BufferedImage createComponentView ( final C component )
    {
        return SwingUtils.createComponentSnapshot ( component, getSnapshotOpacity () );
    }

    /**
     * Returns component snapshot opacity.
     * By default snapshot is semi-transparent for usability purposes.
     *
     * @return component snapshot opacity
     */
    public float getSnapshotOpacity ()
    {
        return 0.8f;
    }

    @Override
    public Point getViewRelativeLocation ( final T object, final DragSourceDragEvent event, final BufferedImage view )
    {
        return location;
    }

    @Override
    public void dragEnded ( final T object, final DragSourceDropEvent event )
    {
        // Clearing initial mouse location
        location = null;
        reference = null;
    }

    /**
     * Returns dragged component retrieved from drag data.
     *
     * @param object drag data
     * @param event  drag event
     * @return dragged component retrieved from drag data
     */
    public abstract C getComponent ( T object, DragSourceDragEvent event );
}