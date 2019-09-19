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

package com.alee.laf.desktoppane;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.extended.layout.AbstractLayoutManager;

import javax.swing.*;
import java.awt.*;

/**
 * Custom {@link LayoutManager} for {@link JInternalFrame}.
 * Unlike {@code BasicInternalFrameUI.Handler} it uses constraints for different panes to avoid direct requests to UI.
 * Also unlike {@code BasicInternalFrameUI.Handler} you can easily override this one.
 *
 * @author Mikle Garin
 */
public class InternalFrameLayout extends AbstractLayoutManager
{
    /**
     * Constraints.
     */
    protected static final String NORTH_PANE = "north";
    protected static final String SOUTH_PANE = "south";
    protected static final String WEST_PANE = "west";
    protected static final String EAST_PANE = "east";

    /**
     * Panes.
     */
    protected Component northPane;
    protected Component southPane;
    protected Component westPane;
    protected Component eastPane;

    @Override
    public void addComponent ( @NotNull final Component component, @Nullable final Object constraints )
    {
        if ( Objects.equals ( constraints, NORTH_PANE ) )
        {
            northPane = component;
        }
        else if ( Objects.equals ( constraints, SOUTH_PANE ) )
        {
            southPane = component;
        }
        else if ( Objects.equals ( constraints, WEST_PANE ) )
        {
            westPane = component;
        }
        else if ( Objects.equals ( constraints, EAST_PANE ) )
        {
            eastPane = component;
        }
        else
        {
            throw new IllegalArgumentException ( "Unknown constraints provided: " + constraints );
        }
    }

    @Override
    public void removeComponent ( @NotNull final Component component )
    {
        if ( component == northPane )
        {
            northPane = null;
        }
        else if ( component == southPane )
        {
            southPane = null;
        }
        else if ( component == westPane )
        {
            westPane = null;
        }
        else if ( component == eastPane )
        {
            eastPane = null;
        }
    }

    @Override
    public void layoutContainer ( @NotNull final Container container )
    {
        final JInternalFrame frame = ( JInternalFrame ) container;

        final Insets insets = frame.getInsets ();
        int cx = insets.left;
        int cy = insets.top;
        int cw = frame.getWidth () - insets.left - insets.right;
        int ch = frame.getHeight () - insets.top - insets.bottom;

        if ( northPane != null )
        {
            final Dimension northSize = northPane.getPreferredSize ();
            northPane.setBounds ( cx, cy, cw, northSize.height );
            cy += northSize.height;
            ch -= northSize.height;
        }

        if ( southPane != null )
        {
            final Dimension southSize = southPane.getPreferredSize ();
            southPane.setBounds ( cx, frame.getHeight () - insets.bottom - southSize.height, cw, southSize.height );
            ch -= southSize.height;
        }

        if ( westPane != null )
        {
            final Dimension westSize = westPane.getPreferredSize ();
            westPane.setBounds ( cx, cy, westSize.width, ch );
            cw -= westSize.width;
            cx += westSize.width;
        }

        if ( eastPane != null )
        {
            final Dimension eastSize = eastPane.getPreferredSize ();
            eastPane.setBounds ( cw - eastSize.width, cy, eastSize.width, ch );
            cw -= eastSize.width;
        }

        final JRootPane rootPane = frame.getRootPane ();
        if ( rootPane != null )
        {
            rootPane.setBounds ( cx, cy, cw, ch );
        }
    }

    @NotNull
    @Override
    public Dimension preferredLayoutSize ( @NotNull final Container container )
    {
        final JInternalFrame frame = ( JInternalFrame ) container;
        final Dimension ps = new Dimension ( frame.getRootPane ().getPreferredSize () );

        final Insets insets = frame.getInsets ();
        ps.width += insets.left + insets.right;
        ps.height += insets.top + insets.bottom;

        if ( northPane != null )
        {
            final Dimension north = northPane.getPreferredSize ();
            ps.width = Math.max ( north.width, ps.width );
            ps.height += north.height;
        }

        if ( southPane != null )
        {
            final Dimension south = southPane.getPreferredSize ();
            ps.width = Math.max ( south.width, ps.width );
            ps.height += south.height;
        }

        if ( eastPane != null )
        {
            final Dimension east = eastPane.getPreferredSize ();
            ps.width += east.width;
            ps.height = Math.max ( east.height, ps.height );
        }

        if ( westPane != null )
        {
            final Dimension west = westPane.getPreferredSize ();
            ps.width += west.width;
            ps.height = Math.max ( west.height, ps.height );
        }

        return ps;
    }

    /**
     * The minimum size of the internal frame only takes into account the title pane and internal frame insets.
     * That allows you to resize the frames to the point where just the title pane is visible.
     */
    @NotNull
    @Override
    public Dimension minimumLayoutSize ( @NotNull final Container container )
    {
        final JInternalFrame frame = ( JInternalFrame ) container;
        final Dimension ms = northPane != null ? northPane.getMinimumSize () : new Dimension ();

        final Insets insets = frame.getInsets ();
        ms.width += insets.left + insets.right;
        ms.height += insets.top + insets.bottom;

        return ms;
    }
}