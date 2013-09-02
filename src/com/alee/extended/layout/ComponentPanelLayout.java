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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: mgarin Date: 30.05.12 Time: 17:54
 */

public class ComponentPanelLayout extends AbstractLayoutManager
{
    protected List<Component> components = new ArrayList<Component> ();
    protected Map<Component, Integer> yShift = new HashMap<Component, Integer> ();

    public List<Component> getComponents ()
    {
        return components;
    }

    public void setComponentShift ( Component component, Integer shift )
    {
        yShift.put ( component, shift );
    }

    public Integer getComponentShift ( Component component )
    {
        return yShift.get ( component );
    }

    public int indexOf ( Component component )
    {
        return components.indexOf ( component );
    }

    public Component getComponent ( int index )
    {
        return components.get ( index );
    }

    @Override
    public void addComponent ( Component component, Object constraints )
    {
        components.add ( component );
    }

    @Override
    public void removeComponent ( Component component )
    {
        components.remove ( component );
    }

    public void insertLayoutComponent ( int index, Component comp )
    {
        components.add ( index, comp );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension preferredLayoutSize ( Container parent )
    {
        Insets insets = parent.getInsets ();
        int width = insets.left + insets.right;
        int height = insets.top + insets.bottom;
        for ( Component component : components )
        {
            Dimension ps = component.getPreferredSize ();
            width = Math.max ( width, insets.left + ps.width + insets.right );
            height += ps.height;
        }
        return new Dimension ( width, height );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layoutContainer ( Container parent )
    {
        Insets insets = parent.getInsets ();
        int y = insets.top;
        for ( Component component : components )
        {
            Dimension ps = component.getPreferredSize ();

            Integer shift = yShift.get ( component );
            component.setBounds ( insets.left, shift == null ? y : y + shift, parent.getWidth () - insets.left - insets.right, ps.height );

            y += ps.height;
        }
    }
}