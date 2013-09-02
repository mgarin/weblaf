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

package com.alee.extended.button;

import com.alee.extended.layout.AbstractLayoutManager;

import java.awt.*;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * User: mgarin Date: 02.11.12 Time: 14:18
 */

public class WebSwitchLayout extends AbstractLayoutManager
{
    public static final String LEFT = "LEFT";
    public static final String RIGHT = "RIGHT";
    public static final String GRIPPER = "GRIPPER";

    private Map<Component, String> constraints = new WeakHashMap<Component, String> ();

    private float gripperLocation = 0;

    public float getGripperLocation ()
    {
        return gripperLocation;
    }

    public void setGripperLocation ( float gripperLocation )
    {
        this.gripperLocation = gripperLocation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addComponent ( Component component, Object constraints )
    {
        String value = ( String ) constraints;
        if ( value == null || !value.equals ( LEFT ) && !value.equals ( RIGHT ) && !value.equals ( GRIPPER ) )
        {
            throw new IllegalArgumentException ( "Cannot add to layout: constraint must be 'LEFT'/'RIGHT'/'GRIPPER' string" );
        }
        this.constraints.put ( component, value );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeComponent ( Component component )
    {
        this.constraints.remove ( component );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension preferredLayoutSize ( Container parent )
    {
        int maxWidth = 0;
        int maxHeight = 0;
        for ( Map.Entry<Component, String> constraint : constraints.entrySet () )
        {
            Dimension ps = constraint.getKey ().getPreferredSize ();
            maxWidth = Math.max ( ps.width, maxWidth );
            maxHeight = Math.max ( ps.height, maxHeight );
        }
        Insets insets = parent.getInsets ();
        return new Dimension ( insets.left + maxWidth * 2 + insets.right, insets.top + maxHeight + insets.bottom );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layoutContainer ( Container parent )
    {
        boolean ltr = parent.getComponentOrientation ().isLeftToRight ();
        Insets insets = parent.getInsets ();
        int width = parent.getWidth () - insets.left - insets.right;
        int partWidth = width / 2;
        int height = parent.getHeight () - insets.top - insets.bottom;
        int leftX = insets.left;
        int rightX = leftX + width / 2;
        int y = insets.top;
        for ( Map.Entry<Component, String> entry : constraints.entrySet () )
        {
            String constraint = entry.getValue ();
            if ( constraint.equals ( GRIPPER ) )
            {
                int x = ltr ? leftX + Math.round ( gripperLocation * partWidth ) : rightX - Math.round ( gripperLocation * partWidth );
                entry.getKey ().setBounds ( x, y, partWidth, height );
            }
            else if ( ltr && constraint.equals ( LEFT ) || !ltr && constraint.equals ( RIGHT ) )
            {
                entry.getKey ().setBounds ( leftX, y, partWidth, height );
            }
            else if ( ltr && constraint.equals ( RIGHT ) || !ltr && constraint.equals ( LEFT ) )
            {
                entry.getKey ().setBounds ( rightX, y, partWidth, height );
            }
        }
    }
}