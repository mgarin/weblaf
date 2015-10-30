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
 * Custom layout for WebDockablePane component.
 *
 * @author Mikle Garin
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

    @Override
    public void addComponent ( final Component component, final Object constraints )
    {
        this.constraints.put ( component, ( String ) constraints );
    }

    @Override
    public void removeComponent ( final Component component )
    {
        this.constraints.remove ( component );
    }

    @Override
    public Dimension preferredLayoutSize ( final Container parent )
    {
        return new Dimension ( 0, 0 );
    }

    @Override
    public void layoutContainer ( final Container parent )
    {
        // Collecting components positioning info
        info = new DockingPaneInfo ( DockingPaneLayout.this, parent );

        // Layouting button components

        // Runtime variables
        int x;
        int y;

        // Top buttons pane
        if ( info.hasTopButtons )
        {
            final int topY = info.rect.y + buttonsMargin.top;
            final int leftX = info.rect.x + info.leftButtonsPaneBounds.width + buttonsMargin.left;
            x = 0;
            for ( final Component component : info.topLeftButtons )
            {
                final Dimension ps = component.getPreferredSize ();
                component.setBounds ( leftX + x, topY, ps.width, info.topButtonsSize.height );
                x += ps.width + buttonSpacing;
            }

            final boolean fit =
                    info.topButtonsSize.width + info.leftButtonsPaneBounds.width + info.rightButtonsPaneBounds.width < info.rect.width;
            final int rightX = fit ? ( info.rect.x + info.rect.width - info.rightButtonsPaneBounds.width - buttonsMargin.right ) :
                    ( x > 0 ? leftX + x - buttonSpacing + info.buttonSidesSpacing : leftX ) + info.topRightButtonsSize.width -
                            buttonsMargin.right;
            x = 0;
            for ( final Component component : info.topRightButtons )
            {
                final Dimension ps = component.getPreferredSize ();
                x += ps.width;
                component.setBounds ( rightX - x, topY, ps.width, info.topButtonsSize.height );
                x += buttonSpacing;
            }
        }

        // Bottom buttons pane
        if ( info.hasBottomButtons )
        {
            final int bottomY = info.rect.y + info.rect.height - buttonsMargin.bottom - info.bottomButtonsSize.height;
            final int leftX = info.rect.x + info.leftButtonsPaneBounds.width + buttonsMargin.left;
            x = 0;
            for ( final Component component : info.bottomLeftButtons )
            {
                final Dimension ps = component.getPreferredSize ();
                component.setBounds ( leftX + x, bottomY, ps.width, info.bottomButtonsSize.height );
                x += ps.width + buttonSpacing;
            }

            final boolean fit =
                    info.bottomButtonsSize.width + info.leftButtonsPaneBounds.width + info.rightButtonsPaneBounds.width < info.rect.width;
            final int rightX = fit ? ( info.rect.x + info.rect.width - info.rightButtonsPaneBounds.width - buttonsMargin.right ) :
                    ( x > 0 ? leftX + x - buttonSpacing + info.buttonSidesSpacing : leftX ) + info.bottomRightButtonsSize.width -
                            buttonsMargin.right;
            x = 0;
            for ( final Component component : info.bottomRightButtons )
            {
                final Dimension ps = component.getPreferredSize ();
                x += ps.width;
                component.setBounds ( rightX - x, bottomY, ps.width, info.bottomButtonsSize.height );
                x += buttonSpacing;
            }
        }

        // Left buttons pane
        if ( info.hasLeftButtons )
        {
            final int leftX = info.rect.x + buttonsMargin.left;
            final int topY = info.rect.y + info.topButtonsPaneBounds.height + buttonsMargin.top;
            y = 0;
            for ( final Component component : info.leftTopButtons )
            {
                final Dimension ps = component.getPreferredSize ();
                component.setBounds ( leftX, topY + y, info.leftButtonsSize.width, ps.height );
                y += ps.height + buttonSpacing;
            }

            final boolean fit =
                    info.leftButtonsSize.height + info.topButtonsPaneBounds.height + info.bottomButtonsPaneBounds.height < info.rect.height;
            final int bottomY = fit ? ( info.rect.y + info.rect.height - info.bottomButtonsPaneBounds.height - buttonsMargin.bottom ) :
                    ( y > 0 ? topY + y - buttonSpacing + info.buttonSidesSpacing : topY ) + info.leftBottomButtonsSize.height -
                            buttonsMargin.bottom;
            y = 0;
            for ( final Component component : info.leftBottomButtons )
            {
                final Dimension ps = component.getPreferredSize ();
                y += ps.height;
                component.setBounds ( leftX, bottomY - y, info.leftButtonsSize.width, ps.height );
                y += buttonSpacing;
            }
        }

        // Right buttons pane
        if ( info.hasRightButtons )
        {
            final int leftX = info.rect.x + info.rect.width - buttonsMargin.right - info.rightButtonsSize.width;
            final int topY = info.rect.y + info.topButtonsPaneBounds.height + buttonsMargin.top;
            y = 0;
            for ( final Component component : info.rightTopButtons )
            {
                final Dimension ps = component.getPreferredSize ();
                component.setBounds ( leftX, topY + y, info.rightButtonsSize.width, ps.height );
                y += ps.height + buttonSpacing;
            }

            final boolean fit = info.rightButtonsSize.height + info.topButtonsPaneBounds.height + info.bottomButtonsPaneBounds.height <
                    info.rect.height;
            final int bottomY = fit ? ( info.rect.y + info.rect.height - info.bottomButtonsPaneBounds.height - buttonsMargin.bottom ) :
                    ( y > 0 ? topY + y - buttonSpacing + info.buttonSidesSpacing : topY ) + info.rightBottomButtonsSize.height -
                            buttonsMargin.bottom;
            y = 0;
            for ( final Component component : info.rightBottomButtons )
            {
                final Dimension ps = component.getPreferredSize ();
                y += ps.height;
                component.setBounds ( leftX, bottomY - y, info.rightButtonsSize.width, ps.height );
                y += buttonSpacing;
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