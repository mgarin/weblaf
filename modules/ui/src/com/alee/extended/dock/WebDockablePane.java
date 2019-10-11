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
import com.alee.api.jdk.Objects;
import com.alee.extended.WebContainer;
import com.alee.extended.dock.data.DockableContainer;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a pane containing {@link WebDockableFrame}s.
 * {@link WebDockableFrame}s can be added to the pane, repositioned within it, minimized, previewed, made floating or closed.
 * Note that closing the frame doesn't remove it from the pane, but only changes it's state to {@link DockableFrameState#closed}.
 * Positioning of the frames is handled by the currently installed {@link DockablePaneModel} implementation.
 * Resize of the {@link WebDockableFrame}s and their drop position while dragged is handled by the {@link DockablePaneGlassLayer}.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see WebDockablePaneModel
 * @see DockablePaneModel
 * @see DockablePaneGlassLayer
 * @see DockablePaneDescriptor
 * @see WDockablePaneUI
 * @see WebDockablePaneUI
 * @see IDockablePanePainter
 * @see DockablePanePainter
 * @see WebContainer
 */
public class WebDockablePane extends WebContainer<WebDockablePane, WDockablePaneUI>
{
    /**
     * Component properties.
     */
    public static final String SIDEBAR_BUTTON_VISIBILITY_PROPERTY = "sidebarButtonVisibility";
    public static final String SIDEBAR_BUTTON_ACTION_PROPERTY = "sidebarButtonAction";
    public static final String GROUP_ELEMENTS_PROPERTY = "groupElements";
    public static final String SIDEBAR_SPACING_PROPERTY = "sidebarSpacing";
    public static final String CONTENT_SPACING_PROPERTY = "contentSpacing";
    public static final String RESIZE_GRIPPER_WIDTH_PROPERTY = "resizeGripperWidth";
    public static final String MINIMUM_ELEMENT_SIZE_PROPERTY = "minimumElementSize";
    public static final String OCCUPY_MINIMUM_SIZE_FOR_CHILDREN_PROPERTY = "occupyMinimumSizeForChildren";
    public static final String MODEL_PROPERTY = "model";
    public static final String MODEL_STATE_PROPERTY = "modelState";
    public static final String GLASS_LAYER_PROPERTY = "glassLayer";
    public static final String FRAMES_PROPERTY = "frames";
    public static final String FRAME_PROPERTY = "frame";
    public static final String CONTENT_PROPERTY = "content";

    /**
     * Sidebar buttons visibility type.
     *
     * @see SidebarButtonVisibility
     */
    @NotNull
    protected SidebarButtonVisibility sidebarButtonVisibility;

    /**
     * Sidebar button action.
     */
    @NotNull
    protected SidebarButtonAction sidebarButtonAction;

    /**
     * Whether or not dockable pane elements should be visually grouped.
     * This will only work with zero {@link #contentSpacing} as there is no point to visually group elements otherwise.
     * Also note that {@link #sidebarSpacing} value will have no effect on this setting as it is used to provide correct padding.
     */
    protected boolean groupElements;

    /**
     * Spacing between sidebars elements and other elements.
     * todo Remove once sidebars are properly implemented as separate containers, it will be covered by container's padding
     */
    protected int sidebarSpacing;

    /**
     * Spacing between content elements.
     * todo Move into DockablePaneLayout once it's separated from the model
     */
    protected int contentSpacing;

    /**
     * Content elements resize gripper width.
     * This setting represents actual draggable area width and not visual representation width.
     * It can be larger than {@link #contentSpacing} and will overlap elements in that case blocking their mouse events.
     */
    protected int resizeGripperWidth;

    /**
     * Minimum size of {@link WebDockableFrame} or content component added onto the dockable pane.
     */
    @NotNull
    protected Dimension minimumElementSize;

    /**
     * Whether containers minimum size should include children minimum sizes or simply be equal to {@link #minimumElementSize}.
     */
    protected boolean occupyMinimumSizeForChildren;

    /**
     * Model containing information about dockable pane elements.
     * It stores all information on frames and content location specifics.
     */
    protected DockablePaneModel model;

    /**
     * Glass layer component.
     * It usually handles frames resize and displays frame placement locations while any frame is being dragged.
     */
    protected JComponent glassLayer;

    /**
     * List of frames added to this {@link WebDockablePane}.
     * This list only contains frames opened frames.
     * Upon closing frame it gets removed from the pane and this list.
     */
    protected final List<WebDockableFrame> frames;

    /**
     * Content component.
     * Content takes all free space left after frames positioning.
     */
    protected JComponent content;

    /**
     * Constructs new dockable pane.
     */
    public WebDockablePane ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs new dockable pane.
     *
     * @param id style ID
     */
    public WebDockablePane ( @NotNull final StyleId id )
    {
        this.sidebarButtonVisibility = SidebarButtonVisibility.minimized;
        this.sidebarButtonAction = SidebarButtonAction.restore;
        this.groupElements = false;
        this.sidebarSpacing = 0;
        this.contentSpacing = 0;
        this.resizeGripperWidth = 10;
        this.minimumElementSize = new Dimension ( 40, 40 );
        this.occupyMinimumSizeForChildren = true;
        this.frames = new ArrayList<WebDockableFrame> ( 3 );
        setModel ( createDefaultModel () );
        updateUI ();
        setStyleId ( id );
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.dockablepane;
    }

    /**
     * Returns {@link SidebarButton}s visibility condition.
     *
     * @return {@link SidebarButton}s visibility condition
     */
    @NotNull
    public SidebarButtonVisibility getSidebarButtonVisibility ()
    {
        return sidebarButtonVisibility;
    }

    /**
     * Sets {@link SidebarButton}s visibility condition.
     *
     * @param condition {@link SidebarButton}s visibility condition
     * @return this {@link WebDockablePane}
     */
    @NotNull
    public WebDockablePane setSidebarButtonVisibility ( @NotNull final SidebarButtonVisibility condition )
    {
        if ( this.sidebarButtonVisibility != condition )
        {
            final SidebarButtonVisibility old = this.sidebarButtonVisibility;
            this.sidebarButtonVisibility = condition;
            firePropertyChange ( SIDEBAR_BUTTON_VISIBILITY_PROPERTY, old, condition );
        }
        return this;
    }

    /**
     * Returns sidebar button action.
     *
     * @return sidebar button action
     */
    @NotNull
    public SidebarButtonAction getSidebarButtonAction ()
    {
        return sidebarButtonAction;
    }

    /**
     * Sets sidebar button action.
     *
     * @param action sidebar button action
     * @return this {@link WebDockablePane}
     */
    @NotNull
    public WebDockablePane setSidebarButtonAction ( @NotNull final SidebarButtonAction action )
    {
        if ( this.sidebarButtonAction != action )
        {
            final SidebarButtonAction old = this.sidebarButtonAction;
            this.sidebarButtonAction = action;
            firePropertyChange ( SIDEBAR_BUTTON_ACTION_PROPERTY, old, action );
        }
        return this;
    }

    /**
     * Returns whether or not dockable pane elements should be visually grouped.
     *
     * @return {@code true} if dockable pane elements should be visually grouped, {@code false} otherwise
     */
    public boolean isGroupElements ()
    {
        return groupElements;
    }

    /**
     * Sets whether or not dockable pane elements should be visually grouped.
     * Note that this will only work with zero {@link #contentSpacing} as there is no point to visually group elements otherwise.
     * Also note that {@link #sidebarSpacing} value will have no effect on this setting as it is used to provide correct padding.
     *
     * @param group whether or not dockable pane elements should be visually grouped
     * @return this {@link WebDockablePane}
     */
    @NotNull
    public WebDockablePane setGroupElements ( final boolean group )
    {
        if ( this.groupElements != group )
        {
            final boolean old = this.groupElements;
            this.groupElements = group;
            firePropertyChange ( GROUP_ELEMENTS_PROPERTY, old, group );
        }
        return this;
    }

    /**
     * Returns spacing between sidebars elements and other elements.
     *
     * @return spacing between sidebars elements and other elements
     */
    public int getSidebarSpacing ()
    {
        return sidebarSpacing;
    }

    /**
     * Sets spacing between sidebars elements and other elements.
     *
     * @param spacing spacing between sidebars elements and other elements
     * @return this {@link WebDockablePane}
     */
    @NotNull
    public WebDockablePane setSidebarSpacing ( final int spacing )
    {
        if ( this.sidebarSpacing != spacing )
        {
            final int old = this.sidebarSpacing;
            this.sidebarSpacing = spacing;
            firePropertyChange ( SIDEBAR_SPACING_PROPERTY, old, spacing );
        }
        return this;
    }

    /**
     * Returns spacing between content elements.
     *
     * @return spacing between content elements
     */
    public int getContentSpacing ()
    {
        return contentSpacing;
    }

    /**
     * Sets spacing between content elements.
     *
     * @param spacing spacing between content elements
     * @return this {@link WebDockablePane}
     */
    @NotNull
    public WebDockablePane setContentSpacing ( final int spacing )
    {
        if ( this.contentSpacing != spacing )
        {
            final int old = this.contentSpacing;
            this.contentSpacing = spacing;
            firePropertyChange ( CONTENT_SPACING_PROPERTY, old, spacing );
        }
        return this;
    }

    /**
     * Returns content elements resize gripper width.
     *
     * @return content elements resize gripper width
     */
    public int getResizeGripperWidth ()
    {
        return resizeGripperWidth;
    }

    /**
     * Sets content elements resize gripper width.
     *
     * @param resizeGripperWidth content elements resize gripper width
     * @return this {@link WebDockablePane}
     */
    @NotNull
    public WebDockablePane setResizeGripperWidth ( final int resizeGripperWidth )
    {
        if ( this.resizeGripperWidth != resizeGripperWidth )
        {
            final int old = this.resizeGripperWidth;
            this.resizeGripperWidth = resizeGripperWidth;
            firePropertyChange ( RESIZE_GRIPPER_WIDTH_PROPERTY, old, resizeGripperWidth );
        }
        return this;
    }

    /**
     * Returns minimum size of {@link WebDockableFrame} or content component added onto the dockable pane.
     *
     * @return minimum size of {@link WebDockableFrame} or content component added onto the dockable pane
     */
    @NotNull
    public Dimension getMinimumElementSize ()
    {
        return minimumElementSize;
    }

    /**
     * Sets minimum size of {@link WebDockableFrame} or content component added onto the dockable pane.
     *
     * @param size minimum size of {@link WebDockableFrame} or content component added onto the dockable pane
     * @return this {@link WebDockablePane}
     */
    @NotNull
    public WebDockablePane setMinimumElementSize ( @NotNull final Dimension size )
    {
        if ( this.minimumElementSize != size )
        {
            final Dimension old = this.minimumElementSize;
            this.minimumElementSize = size;
            firePropertyChange ( MINIMUM_ELEMENT_SIZE_PROPERTY, old, size );
        }
        return this;
    }

    /**
     * Returns whether containers minimum size should include children sizes or simply be equal to {@link #minimumElementSize}.
     *
     * @return true if containers minimum size should include children sizes, false if it should be equal to {@link #minimumElementSize}
     */
    public boolean isOccupyMinimumSizeForChildren ()
    {
        return occupyMinimumSizeForChildren;
    }

    /**
     * Sets whether containers minimum size should include children sizes or simply be equal to {@link #minimumElementSize}.
     *
     * @param occupy whether containers minimum size should include children sizes or simply be equal to {@link #minimumElementSize}
     * @return this {@link WebDockablePane}
     */
    @NotNull
    public WebDockablePane setOccupyMinimumSizeForChildren ( final boolean occupy )
    {
        if ( this.occupyMinimumSizeForChildren != occupy )
        {
            final boolean old = this.occupyMinimumSizeForChildren;
            this.occupyMinimumSizeForChildren = occupy;
            firePropertyChange ( OCCUPY_MINIMUM_SIZE_FOR_CHILDREN_PROPERTY, old, occupy );
        }
        return this;
    }

    /**
     * Returns model containing information about dockable pane elements.
     *
     * @return model containing information about dockable pane elements
     */
    @NotNull
    public DockablePaneModel getModel ()
    {
        return model;
    }

    /**
     * Sets model containing information about dockable pane elements.
     *
     * @param model model containing information about dockable pane elements
     * @return this {@link WebDockablePane}
     */
    @NotNull
    public WebDockablePane setModel ( @NotNull final DockablePaneModel model )
    {
        if ( this.model != model )
        {
            final DockablePaneModel old = this.model;
            this.model = model;
            setLayout ( model );
            firePropertyChange ( MODEL_PROPERTY, old, model );
        }
        return this;
    }

    /**
     * Returns default {@link DockablePaneModel} implementation to be used.
     *
     * @return default {@link DockablePaneModel} implementation to be used
     */
    @NotNull
    protected DockablePaneModel createDefaultModel ()
    {
        return new WebDockablePaneModel ();
    }

    /**
     * Returns dockable pane element states data.
     * It contains data which can be used to restore dockable element states.
     *
     * @return dockable pane element states data
     * @see #setState(DockableContainer)
     */
    @NotNull
    public DockableContainer getState ()
    {
        return getModel ().getRoot ();
    }

    /**
     * Sets dockable pane element states data.
     * This data can be retrieved from dockable pane at any time in runtime.
     *
     * @param state dockable pane element states data
     * @return this {@link WebDockablePane}
     * @see #getState()
     */
    @NotNull
    public WebDockablePane setState ( @NotNull final DockableContainer state )
    {
        // Changing root element
        if ( getModel ().getRoot () != state )
        {
            final DockableContainer old = getModel ().getRoot ();
            getModel ().setRoot ( state );
            firePropertyChange ( MODEL_STATE_PROPERTY, old, model );
        }
        return this;
    }

    /**
     * Returns glass layer component.
     *
     * @return glass layer component
     */
    @Nullable
    public JComponent getGlassLayer ()
    {
        return glassLayer;
    }

    /**
     * Sets glass layer component.
     *
     * @param glassLayer glass layer component
     * @return this {@link WebDockablePane}
     */
    @NotNull
    public WebDockablePane setGlassLayer ( @Nullable final JComponent glassLayer )
    {
        if ( this.glassLayer != glassLayer )
        {
            final JComponent old = this.glassLayer;
            this.glassLayer = glassLayer;
            firePropertyChange ( GLASS_LAYER_PROPERTY, old, glassLayer );
        }
        return this;
    }

    /**
     * Returns list of dockable frames added to this {@link WebDockablePane}.
     *
     * @return list of dockable frames added to this {@link WebDockablePane}
     */
    @NotNull
    public List<WebDockableFrame> getFrames ()
    {
        return CollectionUtils.copy ( frames );
    }

    /**
     * Returns {@link List} of {@link WebDockableFrame}s added to this {@link WebDockablePane} and positioned on the specified side.
     *
     * @param position frames position relative to content area
     * @return {@link List} of {@link WebDockableFrame}s added to this {@link WebDockablePane} and positioned on the specified side
     * @see WebDockableFrame#getPosition()
     */
    @NotNull
    public List<WebDockableFrame> getFrames ( @NotNull final CompassDirection position )
    {
        final List<WebDockableFrame> positioned = new ArrayList<WebDockableFrame> ( frames.size () );
        for ( final WebDockableFrame frame : frames )
        {
            if ( frame.getPosition () == position )
            {
                positioned.add ( frame );
            }
        }
        return positioned;
    }

    /**
     * Returns {@link WebDockableFrame} with the specified identifier.
     *
     * @param id {@link WebDockableFrame} identifier
     * @return {@link WebDockableFrame} with the specified identifier
     */
    @NotNull
    public WebDockableFrame getFrame ( @NotNull final String id )
    {
        final WebDockableFrame frameById = findFrame ( id );
        if ( frameById == null )
        {
            throw new RuntimeException ( "Unable to find frame with identifier: " + id );
        }
        return frameById;
    }

    /**
     * Returns {@link WebDockableFrame} with the specified identifier or {@code null} if it doesn't exist within {@link WebDockablePane}.
     *
     * @param id {@link WebDockableFrame} identifier
     * @return {@link WebDockableFrame} with the specified identifier or {@code null} if it doesn't exist within {@link WebDockablePane}
     */
    @Nullable
    public WebDockableFrame findFrame ( @NotNull final String id )
    {
        WebDockableFrame frameById = null;
        for ( final WebDockableFrame frame : frames )
        {
            if ( Objects.equals ( id, frame.getId () ) )
            {
                frameById = frame;
                break;
            }
        }
        return frameById;
    }

    /**
     * Returns currently maximized {@link WebDockableFrame} or {@code null} if none maximized.
     *
     * @return currently maximized {@link WebDockableFrame} or {@code null} if none maximized
     */
    @Nullable
    public WebDockableFrame getMaximizedFrame ()
    {
        WebDockableFrame maximizedFrame = null;
        for ( final WebDockableFrame frame : frames )
        {
            if ( frame.isMaximized () )
            {
                maximizedFrame = frame;
                break;
            }
        }
        return maximizedFrame;
    }

    /**
     * Adds specified {@link WebDockableFrame} into this {@link WebDockablePane}.
     * Position of {@link WebDockableFrame} will get restored by its identifier if it's state was saved at least once before.
     * Note that {@link WebDockableFrame} might be in closed state, in that case it will still not be visible.
     *
     * @param frame {@link WebDockableFrame} to add
     * @return added {@link WebDockableFrame}
     */
    @NotNull
    public WebDockableFrame addFrame ( @NotNull final WebDockableFrame frame )
    {
        if ( !frames.contains ( frame ) )
        {
            final List<WebDockableFrame> old = CollectionUtils.copy ( frames );
            frames.add ( frame );
            firePropertyChange ( FRAMES_PROPERTY, old, frames );
            firePropertyChange ( FRAME_PROPERTY, null, frame );
        }
        return frame;
    }

    /**
     * Removes specified {@link WebDockableFrame} from this {@link WebDockablePane}.
     * This will completely remove {@link WebDockableFrame} and its data from {@link WebDockablePaneModel}.
     *
     * @param frame {@link WebDockableFrame} to remove
     * @return removed {@link WebDockableFrame}
     */
    @NotNull
    public WebDockableFrame removeFrame ( @NotNull final WebDockableFrame frame )
    {
        if ( frames.contains ( frame ) )
        {
            final List<WebDockableFrame> old = CollectionUtils.copy ( frames );
            frames.remove ( frame );
            firePropertyChange ( FRAMES_PROPERTY, old, frames );
            firePropertyChange ( FRAME_PROPERTY, frame, null );
        }
        return frame;
    }

    /**
     * Returns current content {@link JComponent}.
     *
     * @return current content {@link JComponent}
     */
    @Nullable
    public JComponent getContent ()
    {
        return content;
    }

    /**
     * Sets content {@link JComponent}.
     *
     * @param content content {@link JComponent}
     * @return previous content {@link JComponent}
     */
    @Nullable
    public JComponent setContent ( @Nullable final JComponent content )
    {
        final JComponent old = this.content;
        if ( this.content != content )
        {
            this.content = content;
            firePropertyChange ( CONTENT_PROPERTY, old, content );
        }
        return old;
    }

    /**
     * Adds new {@link DockablePaneListener}.
     *
     * @param listener {@link DockablePaneListener} to add
     */
    public void addDockablePaneListener ( @NotNull final DockablePaneListener listener )
    {
        listenerList.add ( DockablePaneListener.class, listener );
    }

    /**
     * Removes specified {@link DockablePaneListener}.
     *
     * @param listener {@link DockablePaneListener} to remove
     */
    public void removeDockablePaneListener ( @NotNull final DockablePaneListener listener )
    {
        listenerList.remove ( DockablePaneListener.class, listener );
    }

    /**
     * Informs listeners about {@link WebDockableFrame} being added.
     *
     * @param frame        {@link WebDockableFrame} which was added
     * @param dockablePane {@link WebDockablePane} where frame was added
     */
    public void fireFrameAdded ( @NotNull final WebDockableFrame frame, @NotNull final WebDockablePane dockablePane )
    {
        for ( final DockablePaneListener listener : listenerList.getListeners ( DockablePaneListener.class ) )
        {
            listener.frameAdded ( frame, dockablePane );
        }
    }

    /**
     * Informs listeners about {@link WebDockableFrame} state change.
     *
     * @param frame    {@link WebDockableFrame}
     * @param oldState previous frame state
     * @param newState current frame state
     */
    public void fireFrameStateChanged ( @NotNull final WebDockableFrame frame, @NotNull final DockableFrameState oldState,
                                        @NotNull final DockableFrameState newState )
    {
        for ( final DockablePaneListener listener : listenerList.getListeners ( DockablePaneListener.class ) )
        {
            listener.frameStateChanged ( frame, oldState, newState );
        }
    }

    /**
     * Informs listeners about {@link WebDockableFrame} being moved.
     *
     * @param frame    {@link WebDockableFrame}
     * @param position current frame position relative to content
     */
    public void fireFrameMoved ( @NotNull final WebDockableFrame frame, @NotNull final CompassDirection position )
    {
        for ( final DockablePaneListener listener : listenerList.getListeners ( DockablePaneListener.class ) )
        {
            listener.frameMoved ( frame, position );
        }
    }

    /**
     * Informs listeners about {@link WebDockableFrame} being removed.
     *
     * @param frame        {@link WebDockableFrame} which was removed
     * @param dockablePane {@link WebDockablePane} where frame was removed from
     */
    public void fireFrameRemoved ( @NotNull final WebDockableFrame frame, @NotNull final WebDockablePane dockablePane )
    {
        for ( final DockablePaneListener listener : listenerList.getListeners ( DockablePaneListener.class ) )
        {
            listener.frameRemoved ( frame, dockablePane );
        }
    }

    @Override
    public void applyComponentOrientation ( @NotNull final ComponentOrientation orientation )
    {
        setComponentOrientation ( orientation );
        synchronized ( getTreeLock () )
        {
            for ( final WebDockableFrame frame : frames )
            {
                frame.applyComponentOrientation ( orientation );
            }
            final JComponent content = getContent ();
            if ( content != null )
            {
                content.applyComponentOrientation ( orientation );
            }
        }
    }

    /**
     * Returns the look and feel (LaF) object that renders this component.
     *
     * @return the {@link WDockablePaneUI} object that renders this component
     */
    public WDockablePaneUI getUI ()
    {
        return ( WDockablePaneUI ) ui;
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WDockablePaneUI}
     */
    public void setUI ( final WDockablePaneUI ui )
    {
        super.setUI ( ui );
    }

    @Override
    public void updateUI ()
    {
        StyleManager.getDescriptor ( this ).updateUI ( this );
    }

    @NotNull
    @Override
    public String getUIClassID ()
    {
        return StyleManager.getDescriptor ( this ).getUIClassId ();
    }
}