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

import com.alee.api.data.CompassDirection;
import com.alee.api.data.Orientation;
import com.alee.api.jdk.Objects;
import com.alee.extended.dock.data.*;
import com.alee.extended.dock.drag.FrameDragData;
import com.alee.extended.dock.drag.FrameDropData;
import com.alee.extended.dock.drag.FrameTransferable;
import com.alee.laf.grouping.AbstractGroupingLayout;
import com.alee.laf.window.WebDialog;
import com.alee.utils.CollectionUtils;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.general.Pair;

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
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see WebDockablePane
 * @see DockablePaneModel
 */
public class WebDockablePaneModel extends AbstractGroupingLayout implements DockablePaneModel
{
    /**
     * Side width used to calculated side frame attachment area.
     */
    protected static final int dropSide = 40;

    /**
     * Root element on the {@link WebDockablePane}.
     */
    protected DockableContainer root;

    /**
     * Content element reference.
     */
    protected DockableContentElement content;

    /**
     * Sidebar widths cache.
     */
    protected final Map<CompassDirection, Integer> sidebarWidths = new HashMap<CompassDirection, Integer> ();

    /**
     * Resizable areas cache.
     * Used to optimize resize areas detection.
     */
    protected final List<ResizeData> resizableAreas = new ArrayList<ResizeData> ();

    /**
     * Preview frame bounds.
     * Saved to check intersection with resizable areas.
     */
    protected Rectangle previewBounds = null;

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
    public WebDockablePaneModel ( final DockableContainer root )
    {
        super ();
        setGroupButtons ( false );
        setRoot ( root );
    }

    @Override
    public DockableContainer getRoot ()
    {
        return root;
    }

    @Override
    public void setRoot ( final DockableContainer root )
    {
        this.root = root;
        this.content = root.get ( DockableContentElement.ID );
        this.root.added ( null );
    }

    @Override
    public <T extends DockableElement> T getElement ( final String id )
    {
        return root.get ( id );
    }

    @Override
    public void updateFrame ( final WebDockablePane dockablePane, final WebDockableFrame frame )
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
    }

    @Override
    public void removeFrame ( final WebDockablePane dockablePane, final WebDockableFrame frame )
    {
        if ( root.contains ( frame.getId () ) )
        {
            // Removing frame state
            final DockableFrameElement element = root.get ( frame.getId () );
            removeStructureElement ( element );
        }
    }

    /**
     * Adds specified {@code newElement} relative to another {@code element} shifting to the specified {@code direction}.
     *
     * @param element    element relative to which {@code newElement} should be added
     * @param newElement element to add
     * @param direction  placement direction
     */
    protected void addStructureElement ( final DockableElement element, final DockableElement newElement, final CompassDirection direction )
    {
        final Orientation orientation = direction == north || direction == south ? vertical : horizontal;
        if ( element == root )
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
            final DockableContainer parent = element.getParent ();
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
                list.setSize ( size );
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
    protected void removeStructureElement ( final DockableElement element )
    {
        if ( element == root )
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
            if ( container != root && container.getElementCount () <= 1 )
            {
                // Moving single child up
                if ( container.getElementCount () == 1 )
                {
                    final DockableContainer containerParent = container.getParent ();
                    final int index = containerParent.indexOf ( container );
                    containerParent.add ( index, container.get ( 0 ) );
                }

                // Removing empty container
                removeStructureElement ( container );
            }
        }
    }

    @Override
    public FrameDropData dropData ( final WebDockablePane dockablePane, final TransferHandler.TransferSupport support )
    {
        if ( !support.getTransferable ().isDataFlavorSupported ( FrameTransferable.dataFlavor ) )
        {
            return null;
        }
        try
        {
            // Retrieving draged frame data
            final FrameDragData drag = ( FrameDragData ) support.getTransferable ().getTransferData ( FrameTransferable.dataFlavor );

            // Checking frame existence on this dockable pane
            // This is needed to avoid drag between different panes
            if ( dockablePane.getFrame ( drag.getId () ) == null )
            {
                return null;
            }

            // Basic drag information
            final String id = drag.getId ();
            final Point dropPoint = support.getDropLocation ().getDropPoint ();

            // Checking global side drop
            final Rectangle innerBounds = getInnerBounds ( dockablePane );
            final FrameDropData globalDrop = createDropData ( id, root, innerBounds, dropPoint, dropSide );
            if ( globalDrop != null )
            {
                return globalDrop;
            }

            // Checking content side drop
            // We cannot use "getComponentAt" method here as it will point at glass pane
            final JComponent paneContent = dockablePane.getContent ();
            if ( paneContent != null )
            {
                final Point location = paneContent.getLocation ();
                if ( paneContent.contains ( dropPoint.x - location.x, dropPoint.y - location.y ) )
                {
                    final Rectangle dropBounds = getDropBounds ( paneContent );
                    return createDropData ( id, content, dropBounds, dropPoint, dropSide * 2 );
                }
            }

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
                        return createDropData ( id, element, dropBounds, dropPoint, dropSide * 2 );
                    }
                }
            }

            // No information
            return null;
        }
        catch ( final Exception e )
        {
            throw new RuntimeException ( "Unknown frame drop exception occured", e );
        }
    }

    /**
     * Returns drop location bounds.
     *
     * @param component drop location component
     * @return drop location bounds
     */
    protected Rectangle getDropBounds ( final JComponent component )
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
    protected FrameDropData createDropData ( final String id, final DockableElement element, final Rectangle bounds, final Point dropPoint,
                                             final int dropSide )
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
            return new FrameDropData ( id, bounds, element, direction );
        }
        else
        {
            return null;
        }
    }

    @Override
    public boolean drop ( final WebDockablePane dockablePane, final TransferHandler.TransferSupport support )
    {
        final FrameDropData dropData = dropData ( dockablePane, support );
        if ( dropData != null )
        {
            // Removing frame from initial location first
            final DockableElement element = root.get ( dropData.getId () );
            removeStructureElement ( element );

            // Adding frame to target location
            addStructureElement ( dropData.getElement (), element, dropData.getDirection () );

            // Updating frame position
            final WebDockableFrame frame = dockablePane.getFrame ( element.getId () );
            final CompassDirection position = getFramePosition ( frame );
            frame.setPosition ( position );

            // Updating dockable
            dockablePane.revalidate ();
            dockablePane.repaint ();

            // Informing frame listeners
            frame.fireFrameMoved ( position );
        }
        return false;
    }

    @Override
    public void layoutContainer ( final Container container )
    {
        // Base settings
        final WebDockablePane dockablePane = ( WebDockablePane ) container;
        final int w = dockablePane.getWidth ();
        final int h = dockablePane.getHeight ();

        // Outer bounds, sidebars are placed within these bounds
        final Rectangle outer = getOuterBounds ( dockablePane );

        // Positioning sidebar elements
        final List<CompassDirection> locations = CollectionUtils.asList ( north, west, south, east );
        final Map<CompassDirection, List<JComponent>> allButtons = new HashMap<CompassDirection, List<JComponent>> ();
        for ( final CompassDirection location : locations )
        {
            final List<JComponent> barButtons = getVisibleButtons ( dockablePane, location );
            allButtons.put ( location, barButtons );
            sidebarWidths.put ( location, calculateBarWidth ( location, barButtons ) );
        }
        for ( final CompassDirection location : locations )
        {
            // Retrieving bar cache
            final List<JComponent> buttons = allButtons.get ( location );
            if ( buttons.size () > 0 )
            {
                // Calculating bar bounds
                final int barWidth = sidebarWidths.get ( location );
                final Rectangle bounds;
                final int fw;
                final int lw;
                switch ( location )
                {
                    case north:
                        fw = sidebarWidths.get ( west );
                        lw = sidebarWidths.get ( east );
                        bounds = new Rectangle ( outer.x + fw, outer.y, outer.width - fw - lw, barWidth );
                        break;

                    case west:
                        fw = sidebarWidths.get ( north );
                        lw = sidebarWidths.get ( south );
                        bounds = new Rectangle ( outer.x, outer.y + fw, barWidth, outer.height - fw - lw );
                        break;

                    case south:
                        fw = sidebarWidths.get ( west );
                        lw = sidebarWidths.get ( east );
                        bounds = new Rectangle ( outer.x + fw, outer.y + outer.height - barWidth, outer.width - fw - lw, barWidth );
                        break;

                    case east:
                        fw = sidebarWidths.get ( north );
                        lw = sidebarWidths.get ( south );
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
        if ( dockablePane.frames != null )
        {
            for ( final WebDockableFrame frame : dockablePane.frames )
            {
                if ( frame.isPreview () )
                {
                    preview = frame;
                }
                if ( frame.isDocked () && frame.isMaximized () )
                {
                    maximized = frame;
                }
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
            if ( dockablePane.frames != null )
            {
                for ( final WebDockableFrame frame : dockablePane.frames )
                {
                    if ( frame != preview && frame != maximized && frame.isDocked () )
                    {
                        frame.setBounds ( 0, 0, 0, 0 );
                    }
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
            maximized.setBounds ( inner );
            root.get ( maximized.getId () ).setBounds ( inner );

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

    /**
     * Returns outer pane bounds.
     * These bounds include sidebar and content elements.
     *
     * @param dockablePane {@link WebDockablePane}
     * @return outer pane bounds
     */
    protected Rectangle getOuterBounds ( final WebDockablePane dockablePane )
    {
        final int w = dockablePane.getWidth ();
        final int h = dockablePane.getHeight ();
        final Insets bi = dockablePane.getInsets ();
        return new Rectangle ( bi.left, bi.top, w - bi.left - bi.right, h - bi.top - bi.bottom );
    }

    /**
     * Returns inner pane bounds.
     * These bounds include only content elements.
     *
     * @param dockablePane {@link WebDockablePane}
     * @return inner pane bounds
     */
    protected Rectangle getInnerBounds ( final WebDockablePane dockablePane )
    {
        final Rectangle bounds = getOuterBounds ( dockablePane );
        if ( sidebarWidths.size () > 0 )
        {
            bounds.x += sidebarWidths.get ( west );
            bounds.width -= sidebarWidths.get ( west ) + sidebarWidths.get ( east );
            bounds.y += sidebarWidths.get ( north );
            bounds.height -= sidebarWidths.get ( north ) + sidebarWidths.get ( south );
        }
        return bounds;
    }

    /**
     * Returns frame bounds for {@link DockableFrameState#preview} state.
     *
     * @param dockablePane {@link WebDockablePane}
     * @param frame        {@link WebDockableFrame}
     * @return frame bounds for {@link DockableFrameState#preview} state
     */
    protected Rectangle getPreviewBounds ( final WebDockablePane dockablePane, final WebDockableFrame frame )
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
     * Returns current frame position relative to content.
     *
     * @param frame dockable frame
     * @return current frame position relative to content
     */
    protected CompassDirection getFramePosition ( final WebDockableFrame frame )
    {
        // Looking for frame relative to content position
        DockableContainer parent = content.getParent ();
        int divider = parent.indexOf ( content );
        while ( parent != null )
        {
            // Elements before content
            for ( int i = 0; i < divider; i++ )
            {
                final DockableElement element = parent.get ( i );
                if ( Objects.equals ( element.getId (), frame.getId () ) ||
                        element instanceof DockableContainer && ( ( DockableContainer ) element ).contains ( frame.getId () ) )
                {
                    return parent.getOrientation ().isHorizontal () ? west : north;
                }
            }

            // Elements after content
            for ( int i = divider + 1; i < parent.getElementCount (); i++ )
            {
                final DockableElement element = parent.get ( i );
                if ( Objects.equals ( element.getId (), frame.getId () ) ||
                        element instanceof DockableContainer && ( ( DockableContainer ) element ).contains ( frame.getId () ) )
                {
                    return parent.getOrientation ().isHorizontal () ? east : south;
                }
            }

            // Going higher in the structure
            final DockableElement old = parent;
            parent = parent.getParent ();
            if ( parent != null )
            {
                divider = parent.indexOf ( old );
            }
        }
        throw new RuntimeException ( "Specified frame cannot be found in model: " + frame.getId () );
    }

    /**
     * Returns visible sidebar buttons list.
     *
     * @param dockablePane {@link WebDockablePane}
     * @param side         buttons side
     * @return visible sidebar buttons list
     */
    protected List<JComponent> getVisibleButtons ( final WebDockablePane dockablePane, final CompassDirection side )
    {
        final List<JComponent> buttons = new ArrayList<JComponent> ( 3 );
        DockableContainer parent = content.getParent ();
        int divider = parent.indexOf ( content );
        while ( parent != null )
        {
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
            final DockableElement old = parent;
            parent = parent.getParent ();
            if ( parent != null )
            {
                divider = parent.indexOf ( old );
            }
        }
        return buttons;
    }

    /**
     * Collects visible sidebar buttons at the specified side inside the specified {@link DockableElement}.
     *
     * @param dockablePane {@link WebDockablePane}
     * @param element      element to collect sidebar buttons from
     * @param buttons      buttons list to fill in
     */
    protected void collectVisibleButtons ( final WebDockablePane dockablePane, final DockableElement element,
                                           final List<JComponent> buttons )
    {
        if ( element instanceof DockableFrameElement )
        {
            // Frame might not yet be added to the pane so we have to be careful here
            final WebDockableFrame frame = dockablePane.getFrame ( element.getId () );
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
    protected int calculateBarWidth ( final CompassDirection side, final List<? extends JComponent> barButtons )
    {
        int width = 0;
        for ( final JComponent component : barButtons )
        {
            final Dimension ps = component.getPreferredSize ();
            width = Math.max ( width, side == north || side == south ? ps.height : ps.width );
        }
        return width;
    }

    @Override
    public Dimension preferredLayoutSize ( final Container container )
    {
        // todo Use structure to recursively go through sizes
        return new Dimension ( 0, 0 );
    }

    @Override
    public Pair<String, String> getDescriptors ( final Container container, final Component component, final int index )
    {
        // todo Group buttons for grouped frames (tabs) when grouping is available
        // todo Group frames in case it is enabled here (simply group up the whole center area)
        return new Pair<String, String> ();
    }

    @Override
    public ResizeData getResizeData ( final int x, final int y )
    {
        if ( previewBounds != null && previewBounds.contains ( x, y ) )
        {
            return null;
        }
        for ( final ResizeData resizeData : resizableAreas )
        {
            if ( resizeData.bounds ().contains ( x, y ) )
            {
                return resizeData;
            }
        }
        return null;
    }

    @Override
    public Rectangle getFloatingBounds ( final WebDockablePane dockablePane, final WebDockableFrame frame, final WebDialog dialog )
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
}