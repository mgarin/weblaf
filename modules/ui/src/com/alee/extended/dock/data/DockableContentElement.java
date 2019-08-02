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
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * {@link com.alee.extended.dock.data.DockableElement} representing dockable pane content.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see com.alee.extended.dock.WebDockablePane
 */
@XStreamAlias ( "DockableContent" )
public class DockableContentElement extends AbstractDockableElement
{
    /**
     * Content area ID.
     * It is static because only once content area is allowed per dockable pane instance.
     */
    public static final String ID = "dockablepane.content";

    /**
     * Constructs new content element.
     */
    public DockableContentElement ()
    {
        super ( ID );
    }

    @Override
    public boolean isContent ()
    {
        return true;
    }

    @Override
    public Dimension getSize ()
    {
        return new Dimension ( 0, 0 );
    }

    @Override
    public void setSize ( final Dimension size )
    {
        throw new RuntimeException ( "Content element size cannot be modified" );
    }

    @Override
    public boolean isVisible ( final WebDockablePane dockablePane )
    {
        return true;
    }

    @Override
    public void layout ( final WebDockablePane dockablePane, final Rectangle bounds, final List<ResizeData> resizeableAreas )
    {
        // Saving bounds
        setBounds ( bounds );

        // Placing content component
        final JComponent content = dockablePane.getContent ();
        if ( content != null )
        {
            content.setBounds ( bounds );
        }
    }

    @Override
    public Dimension getMinimumSize ( final WebDockablePane dockablePane )
    {
        return dockablePane.getMinimumElementSize ();
    }
}