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

package com.alee.extended.layout;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class RowData
{
    private int width = 0;
    private int height = 0;
    private List<Component> components = new ArrayList<Component> ();

    public RowData ()
    {
        super ();
    }

    public int getWidth ()
    {
        return width;
    }

    public void setWidth ( final int width )
    {
        this.width = width;
    }

    public int getHeight ()
    {
        return height;
    }

    public void setHeight ( final int height )
    {
        this.height = height;
    }

    public List<Component> getComponents ()
    {
        return components;
    }

    public void setComponents ( final List<Component> components )
    {
        this.components = components;
    }

    public void addComponent ( final Component component )
    {
        this.components.add ( component );
    }
}