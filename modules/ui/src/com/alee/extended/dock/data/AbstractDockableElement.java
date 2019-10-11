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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
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
     * Element identifier.
     */
    @NotNull
    @XStreamAsAttribute
    protected String id;

    /**
     * Element size.
     */
    @XStreamAsAttribute
    protected Dimension size;

    /**
     * Parent structure element.
     * Available only in runtime and initialized in dockable pane model.
     */
    @Nullable
    protected transient DockableContainer parent;

    /**
     * Actual element bounds on dockable pane.
     */
    @Nullable
    protected transient Rectangle bounds;

    /**
     * Constructs new element with the specified ID.
     *
     * @param id   element identifier
     * @param size element size
     */
    public AbstractDockableElement ( @NotNull final String id, @NotNull final Dimension size )
    {
        this.id = id;
        this.size = size;
    }

    @Override
    public void added ( @Nullable final DockableContainer parent )
    {
        this.parent = parent;
    }

    @Nullable
    @Override
    public DockableContainer getParent ()
    {
        return parent;
    }

    @Override
    public void removed ( @Nullable final DockableContainer parent )
    {
        this.parent = null;
    }

    @NotNull
    @Override
    public String getId ()
    {
        return id;
    }

    @NotNull
    @Override
    public Dimension getSize ()
    {
        return size;
    }

    @Override
    public void setSize ( @NotNull final Dimension size )
    {
        this.size = size;
    }

    @Nullable
    @Override
    public Rectangle getBounds ()
    {
        return bounds;
    }

    @Override
    public void setBounds ( @NotNull final Rectangle bounds )
    {
        this.bounds = bounds;
    }

    @Override
    public void validateSize ( @NotNull final WebDockablePane dockablePane )
    {
        getMinimumSize ( dockablePane );
    }
}