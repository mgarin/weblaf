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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.data.CompassDirection;
import com.alee.api.data.Orientation;
import com.alee.api.jdk.Objects;
import com.alee.extended.dock.data.*;
import com.alee.extended.dock.drag.FrameDragData;
import com.alee.extended.dock.drag.FrameDropData;
import com.alee.extended.dock.drag.FrameTransferable;
import com.alee.laf.grouping.AbstractGroupingLayout;
import com.alee.laf.window.WebDialog;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.utils.CollectionUtils;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.general.Pair;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alee.api.data.CompassDirection.*;
import static com.alee.api.data.Orientation.horizontal;
import static com.alee.api.data.Orientation.vertical;

/**
 * Basic {@link DockablePaneModel} implementation.
 * It handles elements location data and also provides appropriate layout for all elements on the dockable pane.
 * It also provides frames drag data and various methods to modify element locations.
 * todo Separate layout from the model
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see WebDockablePane
 * @see DockablePaneModel
 */
@XStreamAlias ( "DockablePaneModel" )
public class WebDockablePaneModel extends AbstractGroupingLayout implements DockablePaneModel
{
    /**
     * Side width used to calculated side frame attachment area.
     */
    protected static final int dropSide = 40;

    /**
     * Root element on the {@link WebDockablePane}.
     */
    @SuppressWarnings ( "NullableProblems" )
    @NotNull
    protected DockableContainer root;

    /**
     * Content element reference.
     */
    @SuppressWarnings ( "NullableProblems" )
    @NotNull
    protected DockableContentElement content;

    /**
     * Sidebar widths cache.
     */
    @NotNull
    protected transient final Map<CompassDirection, Integer> sidebarSizes;

    /**
     * Resizable areas cache.
     * Used to optimize resize areas detection.
     */
    @NotNull
    protected transient final List<ResizeData> resizableAreas;

    /**
     * Preview frame bounds.
     * Saved to check intersection with resizable areas.
     */
    @Nullable
    protected transient Rectangle previewBounds;

    /**
     * Constructs new {@link DockablePaneModel} implementation with only content in its structure.
     */
    public WebDockablePaneModel ()
    {
        this ( new DockableListContainer ( horizontal, new DockableContentElement () ) );
    }

    /**
     * Constructs new {@link DockablePaneModel} implementation with the specified structure of the elements.
     *
     * @param root root container on the {@link WebDockablePane}
     */
    public WebDockablePaneModel ( @NotNull final DockableContainer root )
    {
        this.groupButtons = false;
        this.sidebarSizes = new HashMap<CompassDirection, Integer> ();
        this.resizableAreas = new ArrayList<ResizeData> ();
        this.previewBounds = null;
        setRoot ( root );
    }

    @NotNull
    @Override
    public DockableContainer getRoot ()
    {
        return root;
    }

    @Override
    public void setRoot ( @NotNull final DockableContainer root )
    {
        this.root = root;
        this.content = root.get ( DockableContentElement.ID );
        this.root.added ( null );
    }

    @NotNull
    @Override
    public <T extends DockableElement> T getElement ( @NotNull final String id )
    {
        return root.get ( id );
    }

    @Override
    public void updateFrame ( @NotNull final WebDockablePane dockablePane, @NotNull final WebDockableFrame frame )
    {
        // Check whether or not frame data is already available
        if ( root.contains ( frame.getId () ) )
        {
            // Restoring frame states from model
            final DockableFrameElement element = root.get ( frame.getId () );
            frame.setState ( element.getState () );
            frame.setRestoreState ( element.getRestoreState () );
        }
        else
        {
            // Model didn't store state for this frame, creating new one
            final DockableFrameElement element = new DockableFrameElement ( frame );
            addStructureElement ( content, element, frame.getPosition () );
        }

        // Ensuring frame position is correct
        final CompassDirection position = getFramePosition ( frame );
        frame.setPosition ( position );

        // Updating grouping
        resetDescriptors ();
    }

    @Override
    public void removeFrame ( @NotNull final WebDockablePane dockablePane, @NotNull final WebDockableFrame frame )
    {
        if ( root.contains ( frame.getId () ) )
        {
            // Removing frame state
            final DockableFrameElement element = root.get ( frame.getId () );
            removeStructureElement ( element );

            // Updating grouping
            resetDescriptors ();
        }
    }

    /**
     * Adds specified {@code newElement} relative to another {@code element} shifting to the specified {@code direction}.
     *
     * @param element    element relative to which {@code newElement} should be added
     * @param newElement element to add
     * @param direction  placement direction
     */
    protected void addStructureElement ( @NotNull final DockableElement element, @NotNull final DockableElement newElement,
                                         @NotNull final CompassDirection direction )
    {
        final Orientation orientation = direction == north || direction == south ? vertical : horizontal;
        final DockableContainer parent = element.getParent ();
        if ( parent == null )
        {
            if ( orientation == root.getOrientation () || root.getElementCount () <= 1 )
            {
                if ( direction == north || direction == west )
                {
                    // Redirecting addition
                    addStructureElement ( root.get ( 0 ), newElement, direction );
                }
                else if ( direction == south || direction == east )
                {
                    // Redirecting addition
                    addStructureElement ( root.get ( root.getElementCount () - 1 ), newElement, direction );
                }
                else
                {
                    throw new RuntimeException ( "Unknown element position specified" );
                }
            }
            else
            {
                // Creating new root container
                root = new DockableListContainer ( orientation, element );

                // Redirecting addition
                addStructureElement ( element, newElement, direction );
            }
        }
        else
        {
            final int index = parent.indexOf ( element );
            if ( orientation == parent.getOrientation () || parent.getElementCount () <= 1 )
            {
                // Ensure orientation is correct
                parent.setOrientation ( orientation );

                // Add element to either start or end of the container
                if ( orientation.isHorizontal () && direction == west || orientation.isVertical () && direction == north )
                {
                    parent.add ( index, newElement );
                }
                else if ( orientation.isHorizontal () && direction == east || orientation.isVertical () && direction == south )
                {
                    parent.add ( index + 1, newElement );
                }
                else
                {
                    throw new RuntimeException ( "Unknown element position specified" );
                }
            }
            else
            {
                // Saving fraction to keep it intact
                final Dimension size = element.getSize ();

                // Removing initial relation element
                parent.remove ( element );

                // Creating new list container
                final DockableListContainer list = new DockableListContainer ( orientation, element );
                if ( !list.isContent () )
                {
                    list.setSize ( size );
                }
                parent.add ( index, list );

                // Redirecting addition
                addStructureElement ( element, newElement, direction );
            }
        }
    }

    /**
     * Removes specified {@code element} from the model structure.
     *
     * @param element element to remove
     */
    protected void removeStructureElement ( @NotNull final DockableElement element )
    {
        if ( element.getParent () == null )
        {
            throw new RuntimeException ( "Structure element cannot be removed" );
        }
        else if ( element == content )
        {
            throw new RuntimeException ( "Content element cannot be removed" );
        }
        else
        {
            final DockableContainer container = element.getParent ();

            // Removing element from container
            container.remove ( element );

            // Removing redundant container
            if ( container.getParent () != null && container.getElementCount () <= 1 )
            {
                // Moving single child up
                if ( container.getElementCount () == 1 )
                {
                    // Retrieving container's location in it's parent
                    final DockableContainer containerParent = container.getParent ();
                    final int index = containerParent.indexOf ( container );

                    // Updating last child that we're moving up
                    // This is only needed for non-content elements or containers
                    final DockableElement lastChild = container.get ( 0 );
                    if ( !( lastChild instanceof DockableContentElement ) )
                    {
                        // We need to preserve length container has before we get rid of it
                        final Dimension oldChildSize = lastChild.getSize ();
                        final Dimension containerSize = container.getSize ();
                        final boolean horizontal = containerParent.getOrientation ().isHorizontal ();
                        lastChild.setSize ( new Dimension (
                                horizontal ? containerSize.width : oldChildSize.width,
                                horizontal ? oldChildSize.height : containerSize.height
                        ) );
                    }

                    // Moving last child up
                    containerParent.add ( index, lastChild );
                }

                // Removing empty container
                removeStructureElement ( container );
            }
        }
    }

    @Nullable
    @Override
    public FrameDropData dropData ( @NotNull final WebDockablePane dockablePane, @NotNull final TransferHandler.TransferSupport support )
    {
        FrameDropData dropData = null;
        if ( support.getTransferable ().isDataFlavorSupported ( FrameTransferable.dataFlavor ) )
        {
            try
            {
                // Retrieving draged frame data
                final FrameDragData drag = ( FrameDragData ) support.getTransferable ().getTransferData ( FrameTransferable.dataFlavor );

                // Checking frame existence on this dockable pane
                // This is needed to avoid drag between different panes
                if ( dockablePane.findFrame ( drag.getId () ) != null )
                {
                    // Basic drag information
                    final String id = drag.getId ();
                    final Point dropPoint = support.getDropLocation ().getDropPoint ();

                    // Checking global side drop
                    final Rectangle innerBounds = getInnerBounds ( dockablePane );
                    final FrameDropData globalDropData = createDropData ( id, root, innerBounds, dropPoint, dropSide );
                    if ( globalDropData != null )
                    {
                        // Global drop data
                        dropData = globalDropData;
                    }
                    else
                    {
                        // Checking content side drop
                        // We cannot use "getComponentAt" method here as it will point at glass pane
                        final JComponent paneContent = dockablePane.getContent ();
                        final Point paneLocation = paneContent != null ? paneContent.getLocation () : null;
                        if ( paneContent != null && paneContent.contains ( dropPoint.x - paneLocation.x, dropPoint.y - paneLocation.y ) )
                        {
                            final Rectangle dropBounds = getDropBounds ( paneContent );
                            dropData = createDropData ( id, content, dropBounds, dropPoint, dropSide * 2 );
                        }
                        else
                        {
                            // Checking frame side drop
                            // We cannot use "getComponentAt" method here as it will point at glass pane
                            for ( final WebDockableFrame paneFrame : dockablePane.getFrames () )
                            {
                                // Ensure frame is showing and it is not the dragged frame
                                if ( paneFrame.isDocked () && Objects.notEquals ( paneFrame.getId (), id ) )
                                {
                                    final Point location = paneFrame.getLocation ();
                                    if ( paneFrame.contains ( dropPoint.x - location.x, dropPoint.y - location.y ) )
                                    {
                                        final DockableElement element = root.get ( paneFrame.getId () );
                                        final Rectangle dropBounds = getDropBounds ( paneFrame );
                                        dropData = createDropData ( id, element, dropBounds, dropPoint, dropSide * 2 );
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            catch ( final Exception e )
            {
                throw new RuntimeException ( "Unknown frame drop exception occured", e );
            }
        }
        return dropData;
    }

    /**
     * Returns drop location bounds.
     *
     * @param component drop location component
     * @return drop location bounds
     */
    @NotNull
    protected Rectangle getDropBounds ( @NotNull final JComponent component )
    {
        final Rectangle bounds = component.getBounds ();
        final Insets i = component.getInsets ();
        bounds.x += i.left - 1;
        bounds.y += i.top - 1;
        bounds.width -= i.left + i.right - 1;
        bounds.height -= i.top + i.bottom - 1;
        return bounds;
    }

    /**
     * Returns component drop data.
     *
     * @param id        dragged frame ID
     * @param element   element currently placed at the drop location
     * @param bounds    drop location bounds
     * @param dropPoint drop point
     * @param dropSide  size of droppable side
     * @return component drop data
     */
    @Nullable
    protected FrameDropData createDropData ( @NotNull final String id, @NotNull final DockableElement element,
                                             @NotNull final Rectangle bounds, @NotNull final Point dropPoint, final int dropSide )
    {
        // Calculating drop direction
        final boolean wSmall = dropSide > bounds.width / 2;
        final boolean hSmall = dropSide > bounds.height / 2;
        final int xSide = Math.min ( dropSide, bounds.width / 2 );
        final int ySide = Math.min ( dropSide, bounds.height / 2 );
        final boolean w = dropPoint.x <= bounds.x + xSide;
        final boolean e = dropPoint.x >= bounds.x + bounds.width - xSide;
        final boolean n = dropPoint.y <= bounds.y + ySide;
        final boolean s = dropPoint.y >= bounds.y + bounds.height - ySide;
        final CompassDirection direction;
        if ( wSmall || hSmall )
        {
            // Special behavior in case width or height are too small
            if ( wSmall )
            {
                // Prioritize north and south zones
                direction = n ? north : s ? south : w ? west : e ? east : null;
            }
            else
            {
                // Prioritize west and east zones
                direction = w ? west : e ? east : n ? north : s ? south : null;
            }
        }
        else if ( w && n )
        {
            // NW corner drop behavior
            direction = dropPoint.x - bounds.x > dropPoint.y - bounds.y ? north : west;
        }
        else if ( w && s )
        {
            // SW corner drop behavior
            direction = dropPoint.x - bounds.x > bounds.y + bounds.height - dropPoint.y ? south : west;
        }
        else if ( e && n )
        {
            // NE corner drop behavior
            direction = bounds.x + bounds.width - dropPoint.x > dropPoint.y - bounds.y ? north : east;
        }
        else if ( e && s )
        {
            // SE corner drop behavior
            direction = bounds.x + bounds.width - dropPoint.x > bounds.y + bounds.height - dropPoint.y ? south : east;
        }
        else
        {
            // General drop behavior
            direction = n ? north : s ? south : w ? west : e ? east : null;
        }

        // Creating drop data
        final FrameDropData dropData;
        if ( direction != null )
        {
            switch ( direction )
            {
                case west:
                    bounds.width = xSide;
                    break;

                case east:
                    bounds.x = bounds.x + bounds.width - xSide;
                    bounds.width = xSide;
                    break;

                case north:
                    bounds.height = ySide;
                    break;

                case south:
                    bounds.y = bounds.y + bounds.height - ySide;
                    bounds.height = ySide;
                    break;
            }
            dropData = new FrameDropData ( id, bounds, element, direction );
        }
        else
        {
            dropData = null;
        }
        return dropData;
    }

    @Override
    public boolean drop ( @NotNull final WebDockablePane dockablePane, @NotNull final TransferHandler.TransferSupport support )
    {
        final FrameDropData dropData = dropData ( dockablePane, support );
        if ( dropData != null )
        {
            // Dropped element
            final DockableElement element = root.get ( dropData.getId () );

            // Removing frame from initial location first
            removeStructureElement ( element );

            // Adding frame to target location
            addStructureElement ( dropData.getElement (), element, dropData.getDirection () );

            // Updating frame position
            final WebDockableFrame frame = dockablePane.getFrame ( element.getId () );
            final CompassDirection position = getFramePosition ( frame );
            frame.setPosition ( position );

            // Updating grouping
            resetDescriptors ();

            // Updating dockable
            dockablePane.revalidate ();
            dockablePane.repaint ();

            // Informing frame listeners
            frame.fireFrameMoved ( position );
        }
        return false;
    }

    @Override
    public void layoutContainer ( @NotNull final Container container )
    {
        // Base settings
        final WebDockablePane dockablePane = ( WebDockablePane ) container;
        final int w = dockablePane.getWidth ();
        final int h = dockablePane.getHeight ();

        // Outer bounds, sidebars are placed within these bounds
        final Rectangle outer = getOuterBounds ( dockablePane );

        // Positioning sidebar elements
        final List<CompassDirection> locations = CollectionUtils.asList ( north, west, south, east );
        final Map<CompassDirection, List<SidebarButton>> allButtons = new HashMap<CompassDirection, List<SidebarButton>> ();
        for ( final CompassDirection location : locations )
        {
            final List<SidebarButton> barButtons = getVisibleButtons ( dockablePane, location );
            allButtons.put ( location, barButtons );
            sidebarSizes.put ( location, calculateBarSize ( location, barButtons ) );
        }
        for ( final CompassDirection location : locations )
        {
            // Retrieving bar cache
            final List<SidebarButton> buttons = allButtons.get ( location );
            if ( buttons.size () > 0 )
            {
                // Calculating bar bounds
                final int barWidth = sidebarSizes.get ( location );
                final Rectangle bounds;
                final int fw;
                final int lw;
                switch ( location )
                {
                    case north:
                        fw = sidebarSizes.get ( west );
                        lw = sidebarSizes.get ( east );
                        bounds = new Rectangle ( outer.x + fw, outer.y, outer.width - fw - lw, barWidth );
                        break;

                    case west:
                        fw = sidebarSizes.get ( north );
                        lw = sidebarSizes.get ( south );
                        bounds = new Rectangle ( outer.x, outer.y + fw, barWidth, outer.height - fw - lw );
                        break;

                    case south:
                        fw = sidebarSizes.get ( west );
                        lw = sidebarSizes.get ( east );
                        bounds = new Rectangle ( outer.x + fw, outer.y + outer.height - barWidth, outer.width - fw - lw, barWidth );
                        break;

                    case east:
                        fw = sidebarSizes.get ( north );
                        lw = sidebarSizes.get ( south );
                        bounds = new Rectangle ( outer.x + outer.width - barWidth, outer.y + fw, barWidth, outer.height - fw - lw );
                        break;

                    default:
                        throw new RuntimeException ( "Unknown location specified: " + location );
                }

                // Placing buttons
                int x = bounds.x;
                int y = bounds.y;
                for ( final JComponent button : buttons )
                {
                    final Dimension bps = button.getPreferredSize ();
                    if ( location == north || location == south )
                    {
                        // Horizontal placement
                        button.setBounds ( x, y, bps.width, barWidth );
                        x += button.getWidth ();
                    }
                    else
                    {
                        // Vertical placement
                        button.setBounds ( x, y, barWidth, bps.height );
                        y += button.getHeight ();
                    }
                }
            }
        }

        // Inner bounds, frames and content are placed within these bounds
        final Rectangle inner = getInnerBounds ( dockablePane );

        // Looking for special frames
        // There could be only single preview and maximized frame at a time
        WebDockableFrame preview = null;
        WebDockableFrame maximized = null;
        for ( final WebDockableFrame frame : dockablePane.frames )
        {
            if ( frame.isPreview () )
            {
                preview = frame;
            }
            else if ( frame.isDocked () && frame.isMaximized () )
            {
                maximized = frame;
            }
        }

        // Positioning frames and content
        resizableAreas.clear ();
        if ( maximized == null )
        {
            // Placing frames normally
            root.setSize ( inner.getSize () );
            root.layout ( dockablePane, inner, resizableAreas );
        }
        else
        {
            // Simply hide all frames behind visible area
            // This is required to avoid issues with opaque components on other frames overlapping with maximized one
            // Plus this also brings some level of rendering optimization so it is a convenient thing to do
            for ( final WebDockableFrame frame : dockablePane.frames )
            {
                if ( frame != preview && frame != maximized && frame.isDocked () )
                {
                    frame.setBounds ( 0, 0, 0, 0 );
                }
            }
        }

        // Positioning preview frame
        if ( preview != null )
        {
            // Retrieving preview bounds
            previewBounds = getPreviewBounds ( dockablePane, preview );

            // Updating frame bounds
            preview.setBounds ( previewBounds );
            root.get ( preview.getId () ).setBounds ( previewBounds );

            // Moving frame to the topmost possible Z-index after glass pane
            dockablePane.setComponentZOrder ( preview, 1 );
        }
        else
        {
            // Resetting preview bounds
            previewBounds = null;
        }

        // Positioning maximized frame
        if ( maximized != null )
        {
            // Updating frame bounds
            maximized.setBounds ( outer );
            root.get ( maximized.getId () ).setBounds ( outer );

            // Moving frame to the topmost possible Z-index after glass pane and preview frame
            dockablePane.setComponentZOrder ( maximized, preview != null ? 2 : 1 );
        }

        // Positioning glass layer
        // It is placed over whole dockable pane for calculations convenience
        final JComponent glassLayer = dockablePane.getGlassLayer ();
        if ( glassLayer != null )
        {
            glassLayer.setBounds ( 0, 0, w, h );
        }
    }

    @NotNull
    @Override
    public Rectangle getOuterBounds ( @NotNull final WebDockablePane dockablePane )
    {
        final int w = dockablePane.getWidth ();
        final int h = dockablePane.getHeight ();
        final Insets bi = dockablePane.getInsets ();
        return new Rectangle ( bi.left, bi.top, w - bi.left - bi.right, h - bi.top - bi.bottom );
    }

    @NotNull
    @Override
    public Rectangle getInnerBounds ( @NotNull final WebDockablePane dockablePane )
    {
        final Rectangle bounds = getOuterBounds ( dockablePane );
        if ( sidebarSizes.size () > 0 )
        {
            int north = sidebarSizes.get ( CompassDirection.north );
            if ( north > 0 )
            {
                north += dockablePane.getSidebarSpacing ();
            }

            int west = sidebarSizes.get ( CompassDirection.west );
            if ( west > 0 )
            {
                west += dockablePane.getSidebarSpacing ();
            }

            int east = sidebarSizes.get ( CompassDirection.east );
            if ( east > 0 )
            {
                east += dockablePane.getSidebarSpacing ();
            }

            int south = sidebarSizes.get ( CompassDirection.south );
            if ( south > 0 )
            {
                south += dockablePane.getSidebarSpacing ();
            }

            bounds.x += west;
            bounds.width -= west + east;
            bounds.y += north;
            bounds.height -= north + south;
        }
        return bounds;
    }

    /**
     * Returns {@link DockableFrameState#preview} state bounds for the specified {@link WebDockableFrame}.
     *
     * @param dockablePane {@link WebDockablePane}
     * @param frame        {@link WebDockableFrame}
     * @return {@link DockableFrameState#preview} state bounds for the specified {@link WebDockableFrame}
     */
    @NotNull
    protected Rectangle getPreviewBounds ( @NotNull final WebDockablePane dockablePane, @NotNull final WebDockableFrame frame )
    {
        final Rectangle pb;
        final DockableElement element = root.get ( frame.getId () );
        final Rectangle inner = getInnerBounds ( dockablePane );
        switch ( frame.getPosition () )
        {
            case north:
            {
                final int height = Math.min ( element.getSize ().height, inner.height );
                pb = new Rectangle ( inner.x, inner.y, inner.width, height );
                break;
            }
            case west:
            {
                final int width = Math.min ( element.getSize ().width, inner.width );
                pb = new Rectangle ( inner.x, inner.y, width, inner.height );
                break;
            }
            case south:
            {
                final int height = Math.min ( element.getSize ().height, inner.height );
                pb = new Rectangle ( inner.x, inner.y + inner.height - height, inner.width, height );
                break;
            }
            case east:
            {
                final int width = Math.min ( element.getSize ().width, inner.width );
                pb = new Rectangle ( inner.x + inner.width - width, inner.y, width, inner.height );
                break;
            }
            default:
            {
                throw new RuntimeException ( "Unknown frame position: " + frame.getPosition () );
            }
        }
        return pb;
    }

    /**
     * Returns current position of the specified {@link WebDockableFrame} relative to content.
     *
     * @param frame {@link WebDockableFrame}
     * @return current position of the specified {@link WebDockableFrame} relative to content
     */
    @NotNull
    protected CompassDirection getFramePosition ( @NotNull final WebDockableFrame frame )
    {
        CompassDirection position = null;

        // Looking for frame relative to content position
        DockableContainer parent = content.getParent ();
        DockableElement previous = content;
        int divider;
        while ( parent != null )
        {
            // Divider element index
            divider = parent.indexOf ( previous );

            // Elements before content
            for ( int i = 0; i < divider; i++ )
            {
                final DockableElement element = parent.get ( i );
                if ( Objects.equals ( element.getId (), frame.getId () ) ||
                        element instanceof DockableContainer && ( ( DockableContainer ) element ).contains ( frame.getId () ) )
                {
                    position = parent.getOrientation ().isHorizontal () ? west : north;
                    break;
                }
            }
            if ( position != null )
            {
                break;
            }

            // Elements after content
            for ( int i = divider + 1; i < parent.getElementCount (); i++ )
            {
                final DockableElement element = parent.get ( i );
                if ( Objects.equals ( element.getId (), frame.getId () ) ||
                        element instanceof DockableContainer && ( ( DockableContainer ) element ).contains ( frame.getId () ) )
                {
                    position = parent.getOrientation ().isHorizontal () ? east : south;
                    break;
                }
            }
            if ( position != null )
            {
                break;
            }

            // Going higher in the structure
            previous = parent;
            parent = parent.getParent ();
        }

        // Position must be found
        if ( position == null )
        {
            throw new RuntimeException ( "Specified frame cannot be found in model: " + frame.getId () );
        }

        return position;
    }

    /**
     * Returns {@link List} of visible {@link SidebarButton}.
     * This method is necessary to ensure that {@link SidebarButton}s order doesn't depend on {@link WebDockableFrame}s order.
     * That is why we collect them from the elements structure rather than going through all {@link WebDockableFrame}s.
     *
     * @param dockablePane {@link WebDockablePane}
     * @param side         buttons side
     * @return {@link List} of visible {@link SidebarButton}
     */
    @NotNull
    protected List<SidebarButton> getVisibleButtons ( @NotNull final WebDockablePane dockablePane, @NotNull final CompassDirection side )
    {
        final List<SidebarButton> buttons = new ArrayList<SidebarButton> ( 3 );
        DockableContainer parent = content.getParent ();
        DockableElement previous = content;
        int divider;
        while ( parent != null )
        {
            // Divider element index
            divider = parent.indexOf ( previous );

            if ( parent.getOrientation ().isHorizontal () ? side == west : side == north )
            {
                // Elements before content
                for ( int i = 0; i < divider; i++ )
                {
                    collectVisibleButtons ( dockablePane, parent.get ( i ), buttons );
                }
            }
            else if ( parent.getOrientation ().isHorizontal () ? side == east : side == south )
            {
                // Elements after content
                for ( int i = divider + 1; i < parent.getElementCount (); i++ )
                {
                    collectVisibleButtons ( dockablePane, parent.get ( i ), buttons );
                }
            }

            // Going higher in the structure
            previous = parent;
            parent = parent.getParent ();
        }
        return buttons;
    }

    /**
     * Collects visible {@link SidebarButton}s from the specified {@link DockableElement}.
     *
     * @param dockablePane {@link WebDockablePane}
     * @param element      {@link DockableElement} to collect sidebar buttons from
     * @param buttons      {@link List} to add {@link SidebarButton} to
     */
    protected void collectVisibleButtons ( @NotNull final WebDockablePane dockablePane, @NotNull final DockableElement element,
                                           @NotNull final List<SidebarButton> buttons )
    {
        if ( element instanceof DockableFrameElement )
        {
            // Frame might not yet be added to the pane so we have to be careful here
            final WebDockableFrame frame = dockablePane.findFrame ( element.getId () );
            if ( frame != null )
            {
                if ( frame.isSidebarButtonVisible () )
                {
                    buttons.add ( frame.getSidebarButton () );
                }
            }
        }
        else if ( element instanceof DockableContainer )
        {
            final DockableContainer container = ( DockableContainer ) element;
            for ( int i = 0; i < container.getElementCount (); i++ )
            {
                collectVisibleButtons ( dockablePane, container.get ( i ), buttons );
            }
        }
    }

    /**
     * Returns sidebar width.
     *
     * @param side       buttons side
     * @param barButtons sidebar buttons
     * @return sidebar width
     */
    protected int calculateBarSize ( @NotNull final CompassDirection side, @NotNull final List<? extends JComponent> barButtons )
    {
        int width = 0;
        for ( final JComponent component : barButtons )
        {
            final Dimension ps = component.getPreferredSize ();
            width = Math.max ( width, side == north || side == south ? ps.height : ps.width );
        }
        return width;
    }

    @NotNull
    @Override
    public Dimension preferredLayoutSize ( @NotNull final Container container )
    {
        // todo Use structure to recursively go through sizes
        return new Dimension ( 0, 0 );
    }

    @NotNull
    @Override
    public Pair<String, String> getDescriptors ( @NotNull final Container container, @NotNull final Component component, final int index )
    {
        final Pair<String, String> descriptors;
        final WebDockablePane dockablePane = ( WebDockablePane ) container;
        if ( dockablePane.isGroupElements () && dockablePane.getContentSpacing () == 0 )
        {
            if ( component == dockablePane.getContent () )
            {
                // We don't want to affect content decoration, so we'll just let developer handle it's visuals
                // Normally you wouldn't want to have any extra borders in content as they will be provided by frames and sidebars
                descriptors = new Pair<String, String> ();
            }
            else if ( component instanceof SidebarButton )
            {
                // Buttons do not have any sides or lines
                // todo Probably let buttons have everything and only group them between eachother?
                // todo Or let them have left/right sides and other two are attached
                /*final SidebarButton sidebarButton = ( SidebarButton ) component;
                final WebDockableFrame frame = sidebarButton.getFrame ();
                final CompassDirection position = frame.getPosition ();*/
                descriptors = new Pair<String, String> (
                        DecorationUtils.toString ( false, false, false, false ),
                        DecorationUtils.toString (
                                false,
                                false,
                                false /*position == west || position == east*/,
                                false /*position == north || position == south*/
                        )
                );
            }
            else if ( component instanceof WebDockableFrame )
            {
                final WebDockableFrame frame = ( WebDockableFrame ) component;
                if ( frame.isMaximized () )
                {
                    // Maximized frames don't need any sides or lines displayed
                    descriptors = new Pair<String, String> (
                            DecorationUtils.toString ( false, false, false, false ),
                            DecorationUtils.toString ( false, false, false, false )
                    );
                }
                else
                {
                    // For non-maximized frames we have to account for their location
                    // It is pretty complex so it's separated into separate check methods
                    final DockableElement frameElement = root.get ( frame.getId () );
                    final DockableContainer frameContainer = frameElement.getParent ();
                    if ( frameContainer != null )
                    {
                        final String sides = DecorationUtils.toString ( false, false, false, false );
                        final String lines = DecorationUtils.toString (
                                isStartLineNeeded ( dockablePane, frameElement, frameContainer, vertical ),
                                isStartLineNeeded ( dockablePane, frameElement, frameContainer, horizontal ),
                                isEndLineNeeded ( dockablePane, frameElement, frameContainer, vertical ),
                                isEndLineNeeded ( dockablePane, frameElement, frameContainer, horizontal )
                        );
                        descriptors = new Pair<String, String> ( sides, lines );
                    }
                    else
                    {
                        // This shouldn't normally happen
                        throw new RuntimeException ( "Frame doesn't have container: " + frame );
                    }
                }
            }
            else
            {
                // This shouldn't normally happen
                throw new RuntimeException ( "Unknown layout element: " + component );
            }
        }
        else
        {
            // Whenever grouping is disabled or spacing is greater than zero we let components handle their borders
            // It is simply not reasonable to setup any kinds of grouping when components will be at least few pixels away from each other
            descriptors = new Pair<String, String> ();
        }
        return descriptors;
    }

    /**
     * Returns whether or not start line is needed for the specified {@link DockableElement}.
     * Condition is: (At start in pane for orientation && Start sidebar visible) || (Follows content side-to-side in line)
     *
     * @param dockablePane {@link WebDockablePane}
     * @param element      {@link DockableElement}
     * @param container    {@link DockableContainer} of the {@link DockableElement}
     * @param orientation  check {@link Orientation}
     * @return {@code true} if start line is needed for the specified {@link DockableElement}, {@code false} otherwise
     */
    protected boolean isStartLineNeeded ( @NotNull final WebDockablePane dockablePane, @NotNull final DockableElement element,
                                          @NotNull final DockableContainer container, @NotNull final Orientation orientation )
    {
        final boolean needed;
        final Orientation containerOrientation = container.getOrientation ();
        if ( containerOrientation == orientation )
        {
            // Our element side check orientation is equal to it's container orientation
            final DockableElement previous = getPreviousVisible ( dockablePane, container, element );
            if ( previous != null )
            {
                // Element is not first in container
                // We need to check if it is content or a container with content at the end
                needed = isContentAtTheEnd ( dockablePane, previous, orientation );
            }
            else
            {
                // Element is first in container
                // If we have parent - we don't know yet if we're first overall and we need to check further
                // If not - we are first element and start line is not needed, it will be handled by the sidebar or outer decoration
                final DockableContainer containerParent = container.getParent ();
                needed = containerParent != null && isStartLineNeeded ( dockablePane, container, containerParent, orientation );
            }
        }
        else
        {
            // Our element side check orientation is not equal to it's container orientation, we can proceed to parent right away
            // If we have parent - we don't know yet if we're first overall and we need to check further
            // If not - we are first element and start line is not needed, it will be handled by the sidebar or outer decoration
            final DockableContainer containerParent = container.getParent ();
            needed = containerParent != null && isStartLineNeeded ( dockablePane, container, containerParent, orientation );
        }
        return needed;
    }

    /**
     * Returns whether or not end line is needed for the specified {@link DockableElement}.
     * Condition is: (At end in pane for orientation && End sidebar visible) || (At end of container and content is at either side)
     *
     * @param dockablePane {@link WebDockablePane}
     * @param element      {@link DockableElement}
     * @param container    {@link DockableContainer} of the {@link DockableElement}
     * @param orientation  check {@link Orientation}
     * @return {@code true} if end line is needed for the specified {@link DockableElement}, {@code false} otherwise
     */
    protected boolean isEndLineNeeded ( @NotNull final WebDockablePane dockablePane, @NotNull final DockableElement element,
                                        @NotNull final DockableContainer container, @NotNull final Orientation orientation )
    {
        final boolean needed;
        final Orientation containerOrientation = container.getOrientation ();
        if ( containerOrientation == orientation )
        {
            // Our element side check orientation is equal to it's container orientation
            final DockableElement next = getNextVisible ( dockablePane, container, element );
            if ( next != null )
            {
                // Element is not last in container, end line always needed
                needed = true;
            }
            else
            {
                // Element is last in container
                // If we have parent - we don't know yet if we're last overall and we need to check further
                // If not - we are last element and end line is not needed, it will be handled by the sidebar or outer decoration
                final DockableContainer containerParent = container.getParent ();
                needed = containerParent != null && isEndLineNeeded ( dockablePane, container, containerParent, orientation );
            }
        }
        else
        {
            // Checking all neighbour elements to see if they have content at end
            boolean contentAtEndOfNearbyElement = false;
            for ( int index = 0; index < container.getElementCount (); index++ )
            {
                final DockableElement other = container.get ( index );
                if ( other != element && isContentAtTheEnd ( dockablePane, other, orientation ) )
                {
                    contentAtEndOfNearbyElement = true;
                    break;
                }
            }
            if ( contentAtEndOfNearbyElement )
            {
                // We don't need end line when we're in line with content end
                // Even if there is no other elements in front of us line will be handled by sidebar or outer decoration
                needed = false;
            }
            else
            {

                // Our element side check orientation is not equal to it's container orientation, we can proceed to parent right away
                // If we have parent - we don't know yet if we're last overall and we need to check further
                // If not - we are last element and end line is not needed, it will be handled by the sidebar or outer decoration
                final DockableContainer containerParent = container.getParent ();
                needed = containerParent != null && isEndLineNeeded ( dockablePane, container, containerParent, orientation );
            }
        }
        return needed;
    }

    /**
     * Returns whether or not specified {@link DockableElement} is content or has content at the start.
     *
     * @param dockablePane {@link WebDockablePane}
     * @param element      {@link DockableElement}
     * @param orientation  check {@link Orientation}
     * @return {@code true} if specified {@link DockableElement} is content or has content at the start, {@code false} otherwise
     */
    protected boolean isContentAtTheStart ( @NotNull final WebDockablePane dockablePane, @NotNull final DockableElement element,
                                            @NotNull final Orientation orientation )
    {
        final boolean contentAtTheStart;
        if ( element instanceof DockableContentElement )
        {
            contentAtTheStart = true;
        }
        else if ( element instanceof DockableContainer )
        {
            final DockableContainer container = ( DockableContainer ) element;
            if ( container.getOrientation () == orientation )
            {
                final DockableElement firstElement = getFirstVisible ( dockablePane, container );
                contentAtTheStart = firstElement != null && isContentAtTheStart ( dockablePane, firstElement, orientation );
            }
            else
            {
                boolean contentAtTheStartOfOne = false;
                for ( int index = 0; index < container.getElementCount (); index++ )
                {
                    if ( isContentAtTheStart ( dockablePane, container.get ( index ), orientation ) )
                    {
                        contentAtTheStartOfOne = true;
                        break;
                    }
                }
                contentAtTheStart = contentAtTheStartOfOne;
            }
        }
        else
        {
            contentAtTheStart = false;
        }
        return contentAtTheStart;
    }

    /**
     * Returns whether or not specified {@link DockableElement} is content or has content at the end.
     *
     * @param dockablePane {@link WebDockablePane}
     * @param element      {@link DockableElement}
     * @param orientation  check {@link Orientation}
     * @return {@code true} if specified {@link DockableElement} is content or has content at the end, {@code false} otherwise
     */
    protected boolean isContentAtTheEnd ( @NotNull final WebDockablePane dockablePane, @NotNull final DockableElement element,
                                          @NotNull final Orientation orientation )
    {
        final boolean contentAtTheEnd;
        if ( element instanceof DockableContentElement )
        {
            contentAtTheEnd = true;
        }
        else if ( element instanceof DockableContainer )
        {
            final DockableContainer container = ( DockableContainer ) element;
            if ( container.getOrientation () == orientation )
            {
                final DockableElement lastElement = getLastVisible ( dockablePane, container );
                contentAtTheEnd = lastElement != null && isContentAtTheEnd ( dockablePane, lastElement, orientation );
            }
            else
            {
                boolean contentAtTheEndOfOne = false;
                for ( int index = 0; index < container.getElementCount (); index++ )
                {
                    if ( isContentAtTheEnd ( dockablePane, container.get ( index ), orientation ) )
                    {
                        contentAtTheEndOfOne = true;
                        break;
                    }
                }
                contentAtTheEnd = contentAtTheEndOfOne;
            }
        }
        else
        {
            contentAtTheEnd = false;
        }
        return contentAtTheEnd;
    }

    /**
     * Returns previous visible sibling for the specified {@link DockableElement} in {@link DockableContainer}.
     *
     * @param dockablePane {@link WebDockablePane}
     * @param container    {@link DockableContainer}
     * @param element      {@link DockableElement} to find previous visible sibling for
     * @return previous visible sibling for the specified {@link DockableElement} in {@link DockableContainer}
     */
    @Nullable
    protected DockableElement getPreviousVisible ( @NotNull final WebDockablePane dockablePane, @NotNull final DockableContainer container,
                                                   @NotNull final DockableElement element )
    {
        DockableElement previousVisible = null;
        for ( int i = container.indexOf ( element ) - 1; i >= 0; i-- )
        {
            final DockableElement sibling = container.get ( i );
            if ( sibling.isVisible ( dockablePane ) )
            {
                previousVisible = sibling;
                break;
            }
        }
        return previousVisible;
    }

    /**
     * Returns next visible sibling for the specified {@link DockableElement} in {@link DockableContainer}.
     *
     * @param dockablePane {@link WebDockablePane}
     * @param container    {@link DockableContainer}
     * @param element      {@link DockableElement} to find next visible sibling for
     * @return next visible sibling for the specified {@link DockableElement} in {@link DockableContainer}
     */
    @Nullable
    protected DockableElement getNextVisible ( @NotNull final WebDockablePane dockablePane, @NotNull final DockableContainer container,
                                               @NotNull final DockableElement element )
    {
        DockableElement nextVisible = null;
        for ( int i = container.indexOf ( element ) + 1; i < container.getElementCount (); i++ )
        {
            final DockableElement sibling = container.get ( i );
            if ( sibling.isVisible ( dockablePane ) )
            {
                nextVisible = sibling;
                break;
            }
        }
        return nextVisible;
    }

    /**
     * Returns first visible {@link DockableElement} in {@link DockableContainer}.
     *
     * @param dockablePane {@link WebDockablePane}
     * @param container    {@link DockableContainer}
     * @return first visible {@link DockableElement} in {@link DockableContainer}
     */
    @Nullable
    protected DockableElement getFirstVisible ( @NotNull final WebDockablePane dockablePane, @NotNull final DockableContainer container )
    {
        DockableElement lastVisible = null;
        for ( int i = 0; i < container.getElementCount (); i++ )
        {
            final DockableElement element = container.get ( i );
            if ( element.isVisible ( dockablePane ) )
            {
                lastVisible = element;
                break;
            }
        }
        return lastVisible;
    }

    /**
     * Returns last visible {@link DockableElement} in {@link DockableContainer}.
     *
     * @param dockablePane {@link WebDockablePane}
     * @param container    {@link DockableContainer}
     * @return last visible {@link DockableElement} in {@link DockableContainer}
     */
    @Nullable
    protected DockableElement getLastVisible ( @NotNull final WebDockablePane dockablePane, @NotNull final DockableContainer container )
    {
        DockableElement lastVisible = null;
        for ( int i = container.getElementCount () - 1; i >= 0; i-- )
        {
            final DockableElement element = container.get ( i );
            if ( element.isVisible ( dockablePane ) )
            {
                lastVisible = element;
                break;
            }
        }
        return lastVisible;
    }

    @NotNull
    @Override
    public Rectangle getFloatingBounds ( @NotNull final WebDockablePane dockablePane, @NotNull final WebDockableFrame frame,
                                         @NotNull final WebDialog dialog )
    {
        final DockableFrameElement element = root.get ( frame.getId () );
        final Rectangle previewBounds;
        if ( element.getFloatingBounds () == null )
        {
            // Frame haven't been in floating state before
            // We will either use provided last bounds within dockable pane or simply use preview bounds
            final Rectangle lastBounds = element.getBounds ();
            previewBounds = lastBounds != null ? lastBounds : getPreviewBounds ( dockablePane, frame );

            // Adjusting bounds to location on screen
            final Point los = CoreSwingUtils.locationOnScreen ( dockablePane );
            previewBounds.x += los.x;
            previewBounds.y += los.y;
        }
        else
        {
            // Restoring previously saved floating bounds
            previewBounds = new Rectangle ( element.getFloatingBounds () );
        }

        // Adjusting location for dialog decorations
        final Insets rpi = dialog.getRootPane ().getInsets ();
        final Insets ci = dialog.getContentPane ().getInsets ();
        previewBounds.x -= rpi.left + ci.left;
        previewBounds.y -= rpi.top + ci.top;
        previewBounds.width += rpi.left + ci.left + ci.right + rpi.right;
        previewBounds.height += rpi.top + ci.top + ci.bottom + rpi.bottom;

        return previewBounds;
    }

    @Nullable
    @Override
    public ResizeData getResizeData ( final int x, final int y )
    {
        ResizeData resizeData = null;
        if ( previewBounds == null || !previewBounds.contains ( x, y ) )
        {
            for ( final ResizeData area : resizableAreas )
            {
                if ( area.bounds ().contains ( x, y ) )
                {
                    resizeData = area;
                    break;
                }
            }
        }
        return resizeData;
    }
}