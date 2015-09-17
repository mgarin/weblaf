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
 * Special layout for {@link com.alee.extended.button.WebSwitch} component.
 *
 * @author Mikle Garin
 */

public class WebSwitchLayout extends AbstractLayoutManager
{
    public static final String LEFT = "LEFT";
    public static final String RIGHT = "RIGHT";
    public static final String GRIPPER = "GRIPPER";

    private final Map<Component, String> constraints = new WeakHashMap<Component, String> ();

    @Override
    public void addComponent ( final Component component, final Object constraints )
    {
        final String value = ( String ) constraints;
        if ( value == null || !value.equals ( LEFT ) && !value.equals ( RIGHT ) && !value.equals ( GRIPPER ) )
        {
            throw new IllegalArgumentException ( "Cannot add to layout: constraint must be 'LEFT'/'RIGHT'/'GRIPPER' string" );
        }
        this.constraints.put ( component, value );
    }

    @Override
    public void removeComponent ( final Component component )
    {
        this.constraints.remove ( component );
    }

    @Override
    public Dimension preferredLayoutSize ( final Container parent )
    {
        int maxWidth = 0;
        int maxHeight = 0;
        for ( final Map.Entry<Component, String> constraint : constraints.entrySet () )
        {
            final Dimension ps = constraint.getKey ().getPreferredSize ();
            maxWidth = Math.max ( ps.width, maxWidth );
            maxHeight = Math.max ( ps.height, maxHeight );
        }
        final Insets insets = parent.getInsets ();
        return new Dimension ( insets.left + maxWidth * 2 + insets.right, insets.top + maxHeight + insets.bottom );
    }

    @Override
    public void layoutContainer ( final Container parent )
    {
        if ( parent instanceof WebSwitch )
        {
            final WebSwitch wswitch = ( WebSwitch ) parent;
            final boolean ltr = wswitch.getComponentOrientation ().isLeftToRight ();
            final Insets insets = wswitch.getInsets ();
            final int width = wswitch.getWidth () - insets.left - insets.right;
            final int partWidth = width / 2;
            final int height = wswitch.getHeight () - insets.top - insets.bottom;
            final int leftX = insets.left;
            final int rightX = leftX + width / 2;
            final int y = insets.top;
            for ( final Map.Entry<Component, String> entry : constraints.entrySet () )
            {
                final String constraint = entry.getValue ();
                if ( constraint.equals ( GRIPPER ) )
                {
                    // todo Place on top of switch?
                    final float gl = wswitch.getGripperLocation ();
                    final int x = ltr ? leftX + Math.round ( gl * partWidth ) : rightX - Math.round ( gl * partWidth );
                    entry.getKey ().setBounds ( x, y, partWidth, height );
                }
                else if ( ltr ? constraint.equals ( LEFT ) : constraint.equals ( RIGHT ) )
                {
                    entry.getKey ().setBounds ( leftX, y, partWidth, height );
                }
                else if ( ltr ? constraint.equals ( RIGHT ) : constraint.equals ( LEFT ) )
                {
                    entry.getKey ().setBounds ( rightX, y, partWidth, height );
                }
            }
        }
        else
        {
            throw new RuntimeException ( "WebSwitchLayout works only inside of the WebSwitch component" );
        }
    }
}