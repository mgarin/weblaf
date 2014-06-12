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

package com.alee.extended.dock;

import com.alee.utils.swing.ZOrderComparator;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * DockingPaneLayout information.
 *
 * @author Mikle Garin
 */

public class DockingPaneInfo implements DockingPaneConstants
{
    private static final ZOrderComparator comparator = new ZOrderComparator ();
    private static final int initialCalcArraysSize = 2;

    public Map<Component, String> constraints;
    public Insets buttonsMargin;
    public int buttonSpacing;
    public int buttonSidesSpacing;
    public Insets framesMargin;
    public Insets contentMargin;

    public Insets margin;
    public Rectangle rect;

    public boolean hasTopButtons;
    public List<Component> topLeftButtons;
    public Dimension topLeftButtonsSize;
    public List<Component> topRightButtons;
    public Dimension topRightButtonsSize;
    public Dimension topButtonsSize;
    public Rectangle topButtonsPaneBounds;

    public boolean hasLeftButtons;
    public List<Component> leftTopButtons;
    public Dimension leftTopButtonsSize;
    public List<Component> leftBottomButtons;
    public Dimension leftBottomButtonsSize;
    public Dimension leftButtonsSize;
    public Rectangle leftButtonsPaneBounds;

    public boolean hasRightButtons;
    public List<Component> rightTopButtons;
    public Dimension rightTopButtonsSize;
    public List<Component> rightBottomButtons;
    public Dimension rightBottomButtonsSize;
    public Dimension rightButtonsSize;
    public Rectangle rightButtonsPaneBounds;

    public boolean hasBottomButtons;
    public List<Component> bottomLeftButtons;
    public Dimension bottomLeftButtonsSize;
    public List<Component> bottomRightButtons;
    public Dimension bottomRightButtonsSize;
    public Dimension bottomButtonsSize;
    public Rectangle bottomButtonsPaneBounds;

    public Component topFrame;
    public Rectangle topFrameBounds;
    public Component leftFrame;
    public Rectangle leftFrameBounds;
    public Component rightFrame;
    public Rectangle rightFrameBounds;
    public Component bottomFrame;
    public Rectangle bottomFrameBounds;

    public Component content;
    public Rectangle contentBounds;

    public DockingPaneInfo ( final DockingPaneLayout layout, final Container parent )
    {
        super ();

        // Used layout variables
        constraints = layout.getConstraints ();
        buttonsMargin = layout.getButtonsMargin ();
        buttonSpacing = layout.getButtonSpacing ();
        buttonSidesSpacing = layout.getButtonSidesSpacing ();
        framesMargin = layout.getFramesMargin ();
        contentMargin = layout.getContentMargin ();

        // Container border
        margin = parent.getInsets ();
        rect = new Rectangle ( margin.left, margin.top, parent.getWidth () - margin.left - margin.right,
                parent.getHeight () - margin.top - margin.bottom );

        // Collecting various side components
        topLeftButtons = new ArrayList<Component> ( initialCalcArraysSize );
        topRightButtons = new ArrayList<Component> ( initialCalcArraysSize );
        leftTopButtons = new ArrayList<Component> ( initialCalcArraysSize );
        leftBottomButtons = new ArrayList<Component> ( initialCalcArraysSize );
        rightTopButtons = new ArrayList<Component> ( initialCalcArraysSize );
        rightBottomButtons = new ArrayList<Component> ( initialCalcArraysSize );
        bottomLeftButtons = new ArrayList<Component> ( initialCalcArraysSize );
        bottomRightButtons = new ArrayList<Component> ( initialCalcArraysSize );
        topFrame = null;
        leftFrame = null;
        rightFrame = null;
        bottomFrame = null;
        content = null;
        for ( final Component component : constraints.keySet () )
        {
            if ( constraints.get ( component ).equals ( TOP_LEFT ) )
            {
                topLeftButtons.add ( component );
            }
            else if ( constraints.get ( component ).equals ( TOP_RIGHT ) )
            {
                topRightButtons.add ( component );
            }
            else if ( constraints.get ( component ).equals ( LEFT_TOP ) )
            {
                leftTopButtons.add ( component );
            }
            else if ( constraints.get ( component ).equals ( LEFT_BOTTOM ) )
            {
                leftBottomButtons.add ( component );
            }
            else if ( constraints.get ( component ).equals ( RIGHT_TOP ) )
            {
                rightTopButtons.add ( component );
            }
            else if ( constraints.get ( component ).equals ( RIGHT_BOTTOM ) )
            {
                rightBottomButtons.add ( component );
            }
            else if ( constraints.get ( component ).equals ( BOTTOM_LEFT ) )
            {
                bottomLeftButtons.add ( component );
            }
            else if ( constraints.get ( component ).equals ( BOTTOM_RIGHT ) )
            {
                bottomRightButtons.add ( component );
            }
            else if ( constraints.get ( component ).equals ( TOP_FRAME ) )
            {
                topFrame = component;
            }
            else if ( constraints.get ( component ).equals ( LEFT_FRAME ) )
            {
                leftFrame = component;
            }
            else if ( constraints.get ( component ).equals ( RIGHT_FRAME ) )
            {
                rightFrame = component;
            }
            else if ( constraints.get ( component ).equals ( BOTTOM_FRAME ) )
            {
                bottomFrame = component;
            }
            else if ( constraints.get ( component ).equals ( CONTENT ) )
            {
                content = component;
            }
        }
        Collections.sort ( topLeftButtons, comparator );
        Collections.sort ( topRightButtons, comparator );
        Collections.sort ( leftTopButtons, comparator );
        Collections.sort ( leftBottomButtons, comparator );
        Collections.sort ( rightTopButtons, comparator );
        Collections.sort ( rightBottomButtons, comparator );
        Collections.sort ( bottomLeftButtons, comparator );
        Collections.sort ( bottomRightButtons, comparator );

        // Button panes sizes

        boolean twoSided;

        twoSided = topLeftButtons.size () > 0 && topRightButtons.size () > 0;
        hasTopButtons = topLeftButtons.size () > 0 || topRightButtons.size () > 0;
        topLeftButtonsSize = hasTopButtons ? getSideSize ( TOP_LEFT, topLeftButtons ) : new Dimension ( 0, 0 );
        topRightButtonsSize = hasTopButtons ? getSideSize ( TOP_RIGHT, topRightButtons ) : new Dimension ( 0, 0 );
        topButtonsSize = hasTopButtons ? new Dimension ( topLeftButtonsSize.width + ( twoSided ? buttonSidesSpacing : 0 ) +
                topRightButtonsSize.width, Math.max ( topLeftButtonsSize.height, topRightButtonsSize.height ) ) : new Dimension ( 0, 0 );

        twoSided = leftTopButtons.size () > 0 && leftBottomButtons.size () > 0;
        hasLeftButtons = leftTopButtons.size () > 0 || leftBottomButtons.size () > 0;
        leftTopButtonsSize = hasLeftButtons ? getSideSize ( LEFT_TOP, leftTopButtons ) : new Dimension ( 0, 0 );
        leftBottomButtonsSize = hasLeftButtons ? getSideSize ( LEFT_BOTTOM, leftBottomButtons ) : new Dimension ( 0, 0 );
        leftButtonsSize = hasLeftButtons ? new Dimension ( Math.max ( leftTopButtonsSize.width, leftBottomButtonsSize.width ),
                leftTopButtonsSize.height + ( twoSided ? buttonSidesSpacing : 0 ) +
                        leftBottomButtonsSize.height
        ) : new Dimension ( 0, 0 );

        twoSided = rightTopButtons.size () > 0 && rightBottomButtons.size () > 0;
        hasRightButtons = rightTopButtons.size () > 0 || rightBottomButtons.size () > 0;
        rightTopButtonsSize = hasRightButtons ? getSideSize ( LEFT_TOP, rightTopButtons ) : new Dimension ( 0, 0 );
        rightBottomButtonsSize = hasRightButtons ? getSideSize ( LEFT_BOTTOM, rightBottomButtons ) : new Dimension ( 0, 0 );
        rightButtonsSize = hasRightButtons ? new Dimension ( Math.max ( rightTopButtonsSize.width, rightBottomButtonsSize.width ),
                rightTopButtonsSize.height + ( twoSided ? buttonSidesSpacing : 0 ) +
                        rightBottomButtonsSize.height
        ) : new Dimension ( 0, 0 );

        twoSided = bottomLeftButtons.size () > 0 && bottomRightButtons.size () > 0;
        hasBottomButtons = bottomLeftButtons.size () > 0 || bottomRightButtons.size () > 0;
        bottomLeftButtonsSize = hasBottomButtons ? getSideSize ( BOTTOM_LEFT, bottomLeftButtons ) : new Dimension ( 0, 0 );
        bottomRightButtonsSize = hasBottomButtons ? getSideSize ( BOTTOM_RIGHT, bottomRightButtons ) : new Dimension ( 0, 0 );
        bottomButtonsSize = hasBottomButtons ? new Dimension ( bottomLeftButtonsSize.width + ( twoSided ? buttonSidesSpacing : 0 ) +
                bottomRightButtonsSize.width, Math.max ( bottomLeftButtonsSize.height, bottomRightButtonsSize.height ) ) :
                new Dimension ( 0, 0 );

        // Content parts bounds update
        updateBounds ();
    }

    public void updateBounds ()
    {
        // Button panes bounds
        topButtonsPaneBounds = hasTopButtons ?
                new Rectangle ( rect.x, rect.y, rect.width, buttonsMargin.top + topButtonsSize.height + buttonsMargin.bottom ) :
                new Rectangle ( 0, 0 );
        bottomButtonsPaneBounds =
                hasBottomButtons ? new Rectangle ( rect.x, rect.y + rect.height - buttonsMargin.top - bottomButtonsSize.height -
                        buttonsMargin.bottom, rect.width, buttonsMargin.top + bottomButtonsSize.height + buttonsMargin.bottom ) :
                        new Rectangle ( 0, 0 );
        leftButtonsPaneBounds = hasLeftButtons ? new Rectangle ( rect.x, rect.y + topButtonsPaneBounds.height,
                buttonsMargin.left + leftButtonsSize.width + buttonsMargin.right, rect.height - topButtonsPaneBounds.height -
                bottomButtonsPaneBounds.height
        ) : new Rectangle ( 0, 0 );
        rightButtonsPaneBounds = hasRightButtons ? new Rectangle ( rect.x + rect.width - buttonsMargin.left - rightButtonsSize.width -
                buttonsMargin.right, rect.y + topButtonsPaneBounds.height,
                buttonsMargin.left + rightButtonsSize.width + buttonsMargin.right, rect.height - topButtonsPaneBounds.height -
                bottomButtonsPaneBounds.height
        ) : new Rectangle ( 0, 0 );

        // Frames bounds
        // Additional 1px added @ each side that has buttons
        final int topBonus = ( hasTopButtons ? 1 : 0 ) + framesMargin.top;
        final int leftBonus = ( hasLeftButtons ? 1 : 0 ) + framesMargin.left;
        final int rightBonus = ( hasRightButtons ? 1 : 0 ) + framesMargin.right;
        final int bottomBonus = ( hasBottomButtons ? 1 : 0 ) + framesMargin.bottom;
        final Dimension tfps = topFrame != null ? topFrame.getPreferredSize () : null;
        topFrameBounds = topFrame != null ?
                new Rectangle ( rect.x + leftButtonsPaneBounds.width + leftBonus, rect.y + topButtonsPaneBounds.height + topBonus,
                        rect.width - leftButtonsPaneBounds.width - rightButtonsPaneBounds.width -
                                leftBonus - rightBonus, tfps.height
                ) : new Rectangle ( 0, 0 );
        final Dimension bfps = bottomFrame != null ? bottomFrame.getPreferredSize () : null;
        bottomFrameBounds = bottomFrame != null ?
                new Rectangle ( rect.x + leftButtonsPaneBounds.width + leftBonus, rect.y + rect.height - bottomButtonsPaneBounds.height -
                        bottomBonus - bfps.height, rect.width - leftButtonsPaneBounds.width - rightButtonsPaneBounds.width -
                        leftBonus - rightBonus, bfps.height ) : new Rectangle ( 0, 0 );
        final Dimension lfps = leftFrame != null ? leftFrame.getPreferredSize () : null;
        leftFrameBounds =
                leftFrame != null ? new Rectangle ( rect.x + leftButtonsPaneBounds.width + leftBonus, rect.y + topButtonsPaneBounds.height +
                        topBonus + topFrameBounds.height, lfps.width, rect.height - topButtonsPaneBounds.height - topBonus -
                        topFrameBounds.height -
                        bottomButtonsPaneBounds.height - bottomBonus -
                        bottomFrameBounds.height ) : new Rectangle ( 0, 0 );
        final Dimension rfps = rightFrame != null ? rightFrame.getPreferredSize () : null;
        rightFrameBounds = rightFrame != null ? new Rectangle ( rect.x + rect.width - rightButtonsPaneBounds.width - rightBonus -
                rfps.width, rect.y + topButtonsPaneBounds.height +
                topBonus + topFrameBounds.height, rfps.width, rect.height - topButtonsPaneBounds.height - topBonus -
                topFrameBounds.height -
                bottomButtonsPaneBounds.height - bottomBonus -
                bottomFrameBounds.height ) : new Rectangle ( 0, 0 );

        // Content bounds
        contentBounds = content != null ? new Rectangle ( rect.x + leftButtonsPaneBounds.width + leftBonus +
                leftFrameBounds.width + contentMargin.left, rect.y + topButtonsPaneBounds.height + topBonus +
                topFrameBounds.height + contentMargin.top, rect.width - leftButtonsPaneBounds.width - rightButtonsPaneBounds.width -
                leftBonus - rightBonus -
                leftFrameBounds.width - rightFrameBounds.width -
                contentMargin.left - contentMargin.right, rect.height - topButtonsPaneBounds.height - bottomButtonsPaneBounds.height -
                topBonus - bottomBonus -
                topFrameBounds.height - bottomFrameBounds.height -
                contentMargin.top - contentMargin.bottom ) : null;
    }

    private Dimension getSideSize ( final String side, final List<Component> components )
    {
        final Dimension size = new Dimension ( 0, 0 );
        if ( side.startsWith ( "TOP" ) || side.startsWith ( "BOTTOM" ) )
        {
            // Horizontal buttons bar
            for ( final Component button : components )
            {
                final Dimension ps = button.getPreferredSize ();
                size.width += ps.width + buttonSpacing;
                size.height = Math.max ( size.height, ps.height );
            }
            if ( components.size () > 0 )
            {
                size.width -= buttonSpacing;
            }
        }
        else
        {
            // Vertical buttons bar
            for ( final Component button : components )
            {
                final Dimension ps = button.getPreferredSize ();
                size.height += ps.height + buttonSpacing;
                size.width = Math.max ( size.width, ps.width );
            }
            if ( components.size () > 0 )
            {
                size.height -= buttonSpacing;
            }
        }
        return size;
    }
}