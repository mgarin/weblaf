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

import com.alee.extended.layout.AbstractLayoutManager;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * User: mgarin Date: 28.06.12 Time: 14:46
 */

public class DockingPaneLayout extends AbstractLayoutManager implements DockingPaneConstants
{
    // Layout settings
    private boolean buttonPanesVisible = true;
    private String filledFrame = null; // todo

    // Button panes settings
    private Insets buttonsMargin = new Insets ( 1, 1, 1, 1 );
    private int buttonSpacing = 0;
    private int buttonSidesSpacing = 20;

    // Frame panes settings                                      
    private Insets framesMargin = new Insets ( 0, 0, 0, 0 );

    // Content pane settings    
    private Insets contentMargin = new Insets ( 0, 0, 0, 0 );

    // Layout components
    private Map<Component, String> constraints = new HashMap<Component, String> ();

    // Cached DockingPaneInfo
    private DockingPaneInfo info = null;

    /**
     * Layout settings
     */

    public boolean isButtonPanesVisible ()
    {
        return buttonPanesVisible;
    }

    public void setButtonPanesVisible ( final boolean buttonPanesVisible )
    {
        this.buttonPanesVisible = buttonPanesVisible;
    }

    public String getFilledFrame ()
    {
        return filledFrame;
    }

    public void setFilledFrame ( final String filledFrame )
    {
        this.filledFrame = filledFrame;
    }

    public Insets getButtonsMargin ()
    {
        return buttonsMargin;
    }

    public void setButtonsMargin ( final Insets buttonsMargin )
    {
        this.buttonsMargin = buttonsMargin;
    }

    public int getButtonSpacing ()
    {
        return buttonSpacing;
    }

    public void setButtonSpacing ( final int buttonSpacing )
    {
        this.buttonSpacing = buttonSpacing;
    }

    public int getButtonSidesSpacing ()
    {
        return buttonSidesSpacing;
    }

    public void setButtonSidesSpacing ( final int buttonSidesSpacing )
    {
        this.buttonSidesSpacing = buttonSidesSpacing;
    }

    public Insets getFramesMargin ()
    {
        return framesMargin;
    }

    public void setFramesMargin ( final Insets framesMargin )
    {
        this.framesMargin = framesMargin;
    }

    public Insets getContentMargin ()
    {
        return contentMargin;
    }

    public void setContentMargin ( final Insets contentMargin )
    {
        this.contentMargin = contentMargin;
    }

    public Map<Component, String> getConstraints ()
    {
        return constraints;
    }

    public void setConstraints ( final Map<Component, String> constraints )
    {
        this.constraints = constraints;
    }

    /**
     * Cached DockingPaneInfo
     */

    public DockingPaneInfo getDockingPaneInfo ()
    {
        return info;
    }

    /**
     * Standard layout methods
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public void addComponent ( final Component component, final Object constraints )
    {
        this.constraints.put ( component, ( String ) constraints );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeComponent ( final Component component )
    {
        this.constraints.remove ( component );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension preferredLayoutSize ( final Container parent )
    {
        // Collecting components positioning info
        info = new DockingPaneInfo ( DockingPaneLayout.this, parent );

        final Dimension top = new Dimension ( buttonsMargin.left + info.topButtonsSize.width + buttonsMargin.right,
                buttonsMargin.top + info.topButtonsSize.height + buttonsMargin.bottom );
        final Dimension left = new Dimension ( buttonsMargin.left + info.leftButtonsSize.width + buttonsMargin.right,
                buttonsMargin.top + info.leftButtonsSize.height + buttonsMargin.bottom );
        final Dimension right = new Dimension ( buttonsMargin.left + info.rightButtonsSize.width + buttonsMargin.right,
                buttonsMargin.top + info.rightButtonsSize.height + buttonsMargin.bottom );
        final Dimension bottom = new Dimension ( buttonsMargin.left + info.bottomButtonsSize.width + buttonsMargin.right,
                buttonsMargin.top + info.bottomButtonsSize.height + buttonsMargin.bottom );

        final int width = info.margin.left + left.width + Math.max ( top.width, bottom.width ) +
                right.width + info.margin.right;
        final int height = info.margin.top + top.height + Math.max ( left.height, right.height ) +
                bottom.height + info.margin.bottom;

        // Frames and content is not counted for preferred size to allow area to be reduced
        return new Dimension ( width, height );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layoutContainer ( final Container parent )
    {
        // Collecting components positioning info
        final Dimension pls = preferredLayoutSize ( parent );

        // Limiting minimum layout size to preferred size
        info.rect.width = Math.max ( info.rect.width, pls.width - info.margin.left - info.margin.right );
        info.rect.height = Math.max ( info.rect.height, pls.height - info.margin.top - info.margin.bottom );
        info.updateBounds ();

        // Layouting button components

        // Runtime variables
        int x;
        int y;

        // Top buttons pane
        if ( info.hasTopButtons )
        {
            x = 0;
            for ( final Component component : info.topLeftButtons )
            {
                final Dimension ps = component.getPreferredSize ();
                component.setBounds ( info.rect.x + info.leftButtonsPaneBounds.width + buttonsMargin.left + x,
                        info.rect.y + buttonsMargin.top, ps.width, info.topButtonsSize.height );
                x += ps.width + buttonSpacing;
            }
            x = 0;
            for ( final Component component : info.topRightButtons )
            {
                final Dimension ps = component.getPreferredSize ();
                x += ps.width;
                component.setBounds ( info.rect.x + info.rect.width - info.rightButtonsPaneBounds.width -
                        buttonsMargin.right - x, info.rect.y + buttonsMargin.top, ps.width, info.topButtonsSize.height );
                x += buttonSpacing;
            }
        }

        // Left buttons pane
        if ( info.hasLeftButtons )
        {
            y = 0;
            for ( final Component component : info.leftTopButtons )
            {
                final Dimension ps = component.getPreferredSize ();
                component.setBounds ( info.rect.x + buttonsMargin.left,
                        info.rect.y + info.topButtonsPaneBounds.height + buttonsMargin.top + y, info.leftButtonsSize.width, ps.height );
                y += ps.height + buttonSpacing;
            }
            y = 0;
            for ( final Component component : info.leftBottomButtons )
            {
                final Dimension ps = component.getPreferredSize ();
                y += ps.height;
                component.setBounds ( info.rect.x + buttonsMargin.left,
                        info.rect.y + info.rect.height - info.bottomButtonsPaneBounds.height -
                                buttonsMargin.bottom - y, info.leftButtonsSize.width, ps.height
                );
                y += buttonSpacing;
            }
        }

        // Right buttons pane
        if ( info.hasRightButtons )
        {
            y = 0;
            for ( final Component component : info.rightTopButtons )
            {
                final Dimension ps = component.getPreferredSize ();
                component.setBounds ( info.rect.x + info.rect.width - buttonsMargin.right -
                                info.rightButtonsSize.width, info.rect.y + info.topButtonsPaneBounds.height + buttonsMargin.top + y,
                        info.rightButtonsSize.width, ps.height
                );
                y += ps.height + buttonSpacing;
            }
            y = 0;
            for ( final Component component : info.rightBottomButtons )
            {
                final Dimension ps = component.getPreferredSize ();
                y += ps.height;
                component.setBounds ( info.rect.x + info.rect.width - buttonsMargin.right -
                        info.rightButtonsSize.width, info.rect.y + info.rect.height - info.bottomButtonsPaneBounds.height -
                        buttonsMargin.bottom - y, info.rightButtonsSize.width, ps.height );
                y += buttonSpacing;
            }
        }

        // Bottom buttons pane
        if ( info.hasBottomButtons )
        {
            x = 0;
            for ( final Component component : info.bottomLeftButtons )
            {
                final Dimension ps = component.getPreferredSize ();
                component.setBounds ( info.rect.x + info.leftButtonsPaneBounds.width + buttonsMargin.left + x,
                        info.rect.y + info.rect.height - buttonsMargin.bottom -
                                info.bottomButtonsSize.height, ps.width, info.bottomButtonsSize.height
                );
                x += ps.width + buttonSpacing;
            }
            x = 0;
            for ( final Component component : info.bottomRightButtons )
            {
                final Dimension ps = component.getPreferredSize ();
                x += ps.width;
                component.setBounds ( info.rect.x + info.rect.width - info.rightButtonsPaneBounds.width -
                        buttonsMargin.right - x, info.rect.y + info.rect.height - buttonsMargin.bottom -
                        info.bottomButtonsSize.height, ps.width, info.bottomButtonsSize.height );
                x += buttonSpacing;
            }
        }

        // Frames
        if ( info.topFrame != null )
        {
            info.topFrame.setBounds ( info.topFrameBounds );
        }
        if ( info.leftFrame != null )
        {
            info.leftFrame.setBounds ( info.leftFrameBounds );
        }
        if ( info.rightFrame != null )
        {
            info.rightFrame.setBounds ( info.rightFrameBounds );
        }
        if ( info.bottomFrame != null )
        {
            info.bottomFrame.setBounds ( info.bottomFrameBounds );
        }

        // Content
        if ( info.content != null )
        {
            info.content.setBounds ( info.contentBounds );
        }
    }
}