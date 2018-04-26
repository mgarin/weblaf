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

package com.alee.extended.dock.data;

import com.alee.extended.dock.WebDockablePane;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.awt.*;

/**
 * Abstract dockable element containing basic methods implementations.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see com.alee.extended.dock.WebDockablePane
 */
public abstract class AbstractDockableElement implements DockableElement
{
    /**
     * Parent structure element.
     * Available only in runtime and initialized in dockable pane model.
     */
    protected transient DockableContainer parent;

    /**
     * Actual element bounds on dockable pane.
     */
    protected transient Rectangle bounds;

    /**
     * Element ID.
     */
    @XStreamAsAttribute
    protected String id;

    /**
     * Element size.
     */
    @XStreamAsAttribute
    protected Dimension size;

    /**
     * Constructs new element with the specified ID.
     *
     * @param id element ID
     */
    public AbstractDockableElement ( final String id )
    {
        super ();
        this.id = id;
    }

    @Override
    public void added ( final DockableContainer parent )
    {
        this.parent = parent;
    }

    @Override
    public void removed ( final DockableContainer parent )
    {
        this.parent = null;
    }

    @Override
    public DockableContainer getParent ()
    {
        return parent;
    }

    @Override
    public String getId ()
    {
        return id;
    }

    @Override
    public Dimension getSize ()
    {
        return size;
    }

    @Override
    public void setSize ( final Dimension size )
    {
        this.size = size;
    }

    @Override
    public Rectangle getBounds ()
    {
        return bounds;
    }

    @Override
    public void setBounds ( final Rectangle bounds )
    {
        this.bounds = bounds;
    }

    @Override
    public void validateSize ( final WebDockablePane dockablePane )
    {
        getMinimumSize ( dockablePane );
    }
}