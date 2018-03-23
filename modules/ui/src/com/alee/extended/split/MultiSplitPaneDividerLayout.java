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

package com.alee.extended.split;

import com.alee.extended.layout.AbstractLayoutManager;
import com.alee.laf.button.WebButton;
import com.alee.utils.SwingUtils;

import java.awt.*;

/**
 * Custom {@link LayoutManager} for {@link WebMultiSplitPaneDivider}.
 * Layout for the divider involves appropriately moving the left/right buttons around.
 *
 * @author Mikle Garin
 */

public class MultiSplitPaneDividerLayout extends AbstractLayoutManager
{
    @Override
    public void layoutContainer ( final Container container )
    {
        final WebMultiSplitPaneDivider divider = ( WebMultiSplitPaneDivider ) container;
        final WebButton leftButton = divider.getLeftOneTouchButton ();
        final WebButton rightButton = divider.getRightOneTouchButton ();
        if ( leftButton != null && rightButton != null )
        {
            if ( divider.getMultiSplitPane ().isOneTouchExpandable () )
            {
                /**
                 * Buttons positioning is replaced with custom code to avoid some unnecessary restrictions.
                 * Check out {@link javax.swing.plaf.basic.BasicSplitPaneDivider.DividerLayout#layoutContainer(Container)} for original.
                 */
                final Insets insets = divider.getInsets ();
                final Dimension lps = leftButton.getPreferredSize ();
                final Dimension rps = rightButton.getPreferredSize ();
                if ( divider.getOrientation ().isHorizontal () )
                {
                    final boolean ltr = divider.getComponentOrientation ().isLeftToRight ();
                    final int dividerHeight = divider.getHeight () - ( insets != null ? insets.top + insets.bottom : 0 );
                    final int y = insets != null ? insets.top : 0;
                    if ( ltr )
                    {
                        final int x = insets != null ? insets.left : 0;
                        leftButton.setBounds ( x, y, lps.width, dividerHeight );
                        rightButton.setBounds ( x + lps.width, y, rps.width, dividerHeight );
                    }
                    else
                    {
                        final int x = divider.getWidth () - lps.width - rps.width - ( insets != null ? insets.right : 0 );
                        rightButton.setBounds ( x, y, rps.width, dividerHeight );
                        leftButton.setBounds ( x + rps.width, y, lps.width, dividerHeight );
                    }
                }
                else
                {
                    final int x = insets != null ? insets.left : 0;
                    final int y = insets != null ? insets.top : 0;
                    final int dividerWidth = divider.getWidth () - ( insets != null ? insets.left + insets.right : 0 );

                    leftButton.setBounds ( x, y, dividerWidth, lps.height );
                    rightButton.setBounds ( x, y + lps.height, dividerWidth, rps.height );
                }

                // Displaying buttons
                leftButton.setVisible ( true );
                rightButton.setVisible ( true );
            }
            else
            {
                // Hiding buttons
                leftButton.setVisible ( false );
                rightButton.setVisible ( false );
            }
        }
    }

    @Override
    public Dimension minimumLayoutSize ( final Container container )
    {
        return preferredLayoutSize ( container );
    }

    /**
     * NOTE: This isn't really used due to divider being forcefully positioned according to predefined sizes.
     * I leave a proper implementation for this method in hopes of using it at some point instead of workarounds.
     */
    @Override
    public Dimension preferredLayoutSize ( final Container container )
    {
        final WebMultiSplitPaneDivider divider = ( WebMultiSplitPaneDivider ) container;
        final WebButton leftButton = divider.getLeftOneTouchButton ();
        final WebButton rightButton = divider.getRightOneTouchButton ();

        final Dimension ps;
        Dimension buttonSize = null;
        if ( divider.getMultiSplitPane ().isOneTouchExpandable () )
        {
            if ( leftButton != null )
            {
                buttonSize = leftButton.getPreferredSize ();
            }
            if ( rightButton != null )
            {
                buttonSize = SwingUtils.max ( buttonSize, rightButton.getPreferredSize () );
            }
        }

        final Insets insets = divider.getInsets ();
        int dividerWidth = 0; // divider.getMultiSplitPane ().getDividerSize ();
        if ( divider.getOrientation ().isHorizontal () )
        {
            if ( buttonSize != null )
            {
                int size = buttonSize.height;
                if ( insets != null )
                {
                    size += insets.top + insets.bottom;
                }
                dividerWidth = Math.max ( dividerWidth, size );
            }
            ps = new Dimension ( 1, dividerWidth );
        }
        else
        {
            if ( buttonSize != null )
            {
                int size = buttonSize.width;
                if ( insets != null )
                {
                    size += insets.left + insets.right;
                }
                dividerWidth = Math.max ( dividerWidth, size );
            }
            ps = new Dimension ( dividerWidth, 1 );
        }
        return ps;
    }
}