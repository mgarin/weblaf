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

package com.alee.painter.decoration.content;

import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Convenient component property listener for {@link com.alee.painter.decoration.content.IContent} implementations.
 *
 * @author Mikle Garin
 */

public abstract class ContentPropertyListener<E extends JComponent, D extends IDecoration<E, D>> implements PropertyChangeListener
{
    /**
     * Component this content is used for.
     */
    protected transient E component;

    /**
     * Decoration this content is used for.
     */
    protected transient D decoration;

    /**
     * Constructs new listener.
     *
     * @param component  component to listen
     * @param decoration active decoration
     */
    public ContentPropertyListener ( final E component, final D decoration )
    {
        super ();
        this.component = component;
        this.decoration = decoration;
    }

    @Override
    public final void propertyChange ( final PropertyChangeEvent evt )
    {
        propertyChange ( component, decoration, evt.getPropertyName (), evt.getOldValue (), evt.getNewValue () );
    }

    /**
     * Informs about property change event.
     *
     * @param component  component firing event
     * @param decoration active decoration
     * @param property   changed property
     * @param oldValue   old property value
     * @param newValue   new property value
     */
    public abstract void propertyChange ( E component, D decoration, String property, Object oldValue, Object newValue );
}