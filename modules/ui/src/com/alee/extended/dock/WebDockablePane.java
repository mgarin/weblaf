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
 * Implementation of a pane containing dockable frames.
 * Frames can be added to the pane, repositioned within it, minimized, previewed, made floating or closed.
 * Positioning of the frames is handled by the {@link DockablePaneModel}.
 * Resize of the frames and their drop position while drag is handled by the glass layer.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
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
    public static final String SIDEBAR_VISIBILITY_PROPERTY = "sidebarVisibility";
    public static final String SIDEBAR_BUTTON_ACTION_PROPERTY = "sidebarButtonAction";
    public static final String CONTENT_SPACING_PROPERTY = "contentSpacing";
    public static final String RESIZE_GRIPPER_PROPERTY = "resizeGripper";
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
     * @see com.alee.extended.dock.SidebarVisibility
     */
    protected SidebarVisibility sidebarVisibility;

    /**
     * Sidebar button action.
     */
    protected SidebarButtonAction sidebarButtonAction;

    /**
     * Spacing between content elements.
     */
    protected int contentSpacing;

    /**
     * Content elements resize gripper width.
     * This setting represents actual draggable area width and not visual representation width.
     * It can be larger than {@link #contentSpacing} and will overlap elements in that case blocking their mouse events.
     */
    protected int resizeGripper;

    /**
     * Minimum size of {@link com.alee.extended.dock.WebDockableFrame} or content component added onto the dockable pane.
     */
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
     * List of frames added to this pane.
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
    public WebDockablePane ( final StyleId id )
    {
        super ();
        this.frames = new ArrayList<WebDockableFrame> ( 3 );
        setModel ( createDefaultModel () );
        updateUI ();
        setStyleId ( id );
    }

    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.dockablepane;
    }

    /**
     * Returns sidebar buttons visibility type.
     *
     * @return sidebar buttons visibility type
     */
    public SidebarVisibility getSidebarVisibility ()
    {
        return sidebarVisibility;
    }

    /**
     * Sets sidebar buttons visibility type.
     *
     * @param visibility sidebar buttons visibility type
     * @return this pane
     */
    public WebDockablePane setSidebarVisibility ( final SidebarVisibility visibility )
    {
        if ( this.sidebarVisibility != visibility )
        {
            final SidebarVisibility old = this.sidebarVisibility;
            this.sidebarVisibility = visibility;
            firePropertyChange ( SIDEBAR_VISIBILITY_PROPERTY, old, visibility );
        }
        return this;
    }

    /**
     * Returns sidebar button action.
     *
     * @return sidebar button action
     */
    public SidebarButtonAction getSidebarButtonAction ()
    {
        return sidebarButtonAction;
    }

    /**
     * Sets sidebar button action.
     *
     * @param action sidebar button action
     * @return this pane
     */
    public WebDockablePane setSidebarButtonAction ( final SidebarButtonAction action )
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
     * @return this pane
     */
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
    public int getResizeGripper ()
    {
        return resizeGripper;
    }

    /**
     * Sets content elements resize gripper width.
     *
     * @param resizeGripper content elements resize gripper width
     * @return this pane
     */
    public WebDockablePane setResizeGripper ( final int resizeGripper )
    {
        if ( this.resizeGripper != resizeGripper )
        {
            final int old = this.resizeGripper;
            this.resizeGripper = resizeGripper;
            firePropertyChange ( RESIZE_GRIPPER_PROPERTY, old, resizeGripper );
        }
        return this;
    }

    /**
     * Returns minimum size of {@link com.alee.extended.dock.WebDockableFrame} or content component added onto the dockable pane.
     *
     * @return minimum size of {@link com.alee.extended.dock.WebDockableFrame} or content component added onto the dockable pane
     */
    public Dimension getMinimumElementSize ()
    {
        return minimumElementSize;
    }

    /**
     * Sets minimum size of {@link com.alee.extended.dock.WebDockableFrame} or content component added onto the dockable pane.
     *
     * @param size minimum size of {@link com.alee.extended.dock.WebDockableFrame} or content component added onto the dockable pane
     * @return this pane
     */
    public WebDockablePane setMinimumElementSize ( final Dimension size )
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
     * @return this pane
     */
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
    public DockablePaneModel getModel ()
    {
        return model;
    }

    /**
     * Sets model containing information about dockable pane elements.
     *
     * @param model model containing information about dockable pane elements
     * @return this pane
     */
    public WebDockablePane setModel ( final DockablePaneModel model )
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
    protected DockablePaneModel createDefaultModel ()
    {
        return new WebDockablePaneModel ();
    }

    /**
     * Returns dockable pane element states data.
     * It contains data which can be used to restore dockable element states.
     *
     * @return dockable pane element states data
     * @see #setState(com.alee.extended.dock.data.DockableContainer)
     */
    public DockableContainer getState ()
    {
        return getModel ().getRoot ();
    }

    /**
     * Sets dockable pane element states data.
     * This data can be retrieved from dockable pane at any time in runtime.
     *
     * @param state dockable pane element states data
     * @return this pane
     * @see #getState()
     */
    public WebDockablePane setState ( final DockableContainer state )
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
    public JComponent getGlassLayer ()
    {
        return glassLayer;
    }

    /**
     * Sets glass layer component.
     *
     * @param glassLayer glass layer component
     * @return this pane
     */
    public WebDockablePane setGlassLayer ( final JComponent glassLayer )
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
     * Returns list of dockable frames added to this pane.
     *
     * @return list of dockable frames added to this pane
     */
    public List<WebDockableFrame> getFrames ()
    {
        return CollectionUtils.copy ( frames );
    }

    /**
     * Returns list of dockable frames added to this pane and positioned on the specified side relative to the content area.
     *
     * @param position frames position relative to content area
     * @return list of dockable frames added to this pane and positioned on the specified side relative to the content area
     */
    public List<WebDockableFrame> getFrames ( final CompassDirection position )
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
     * Returns opened frame with the specified ID or {@code null} if no such frame exists.
     *
     * @param id frame ID
     * @return opened frame with the specified ID or {@code null} if no such frame exists
     */
    public WebDockableFrame getFrame ( final String id )
    {
        for ( final WebDockableFrame frame : frames )
        {
            if ( Objects.equals ( id, frame.getId () ) )
            {
                return frame;
            }
        }
        return null;
    }

    /**
     * Adds specified dockable frame into this pane.
     * Position of the frame will get restored by its ID if it was saved at least once before.
     * Note that this frame might be in closed state, in that case it will still not be visible.
     *
     * @param frame dockable frame to add
     * @return added frame
     */
    public WebDockableFrame addFrame ( final WebDockableFrame frame )
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
     * Removes specified dockable frame from this pane.
     * This will completely remove frame and its data from pane model.
     *
     * @param frame dockable frame to remove
     * @return removed frame
     */
    public WebDockableFrame removeFrame ( final WebDockableFrame frame )
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
     * Returns current content component.
     *
     * @return current content component
     */
    public JComponent getContent ()
    {
        return content;
    }

    /**
     * Sets content component.
     *
     * @param content content component
     * @return previous content component
     */
    public JComponent setContent ( final JComponent content )
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
     * Adds new {@link com.alee.extended.dock.DockableFrameListener}.
     *
     * @param listener {@link com.alee.extended.dock.DockableFrameListener} to add
     */
    public void addFrameListener ( final DockableFrameListener listener )
    {
        listenerList.add ( DockableFrameListener.class, listener );
    }

    /**
     * Removes specified {@link com.alee.extended.dock.DockableFrameListener}.
     *
     * @param listener {@link com.alee.extended.dock.DockableFrameListener} to remove
     */
    public void removeFrameListener ( final DockableFrameListener listener )
    {
        listenerList.remove ( DockableFrameListener.class, listener );
    }

    /**
     * Informs listeners about frame being added.
     *
     * @param frame        {@link com.alee.extended.dock.WebDockableFrame} which was added
     * @param dockablePane {@link com.alee.extended.dock.WebDockablePane} where frame was added
     */
    public void fireFrameAdded ( final WebDockableFrame frame, final WebDockablePane dockablePane )
    {
        for ( final DockableFrameListener listener : listenerList.getListeners ( DockableFrameListener.class ) )
        {
            listener.frameAdded ( frame, dockablePane );
        }
    }

    /**
     * Informs listeners about frame state change.
     *
     * @param frame    {@link com.alee.extended.dock.WebDockableFrame}
     * @param oldState previous frame state
     * @param newState current frame state
     */
    public void fireFrameStateChanged ( final WebDockableFrame frame, final DockableFrameState oldState, final DockableFrameState newState )
    {
        for ( final DockableFrameListener listener : listenerList.getListeners ( DockableFrameListener.class ) )
        {
            listener.frameStateChanged ( frame, oldState, newState );
        }
    }

    /**
     * Informs listeners about frame being moved.
     *
     * @param frame    {@link com.alee.extended.dock.WebDockableFrame}
     * @param position current frame position relative to content
     */
    public void fireFrameMoved ( final WebDockableFrame frame, final CompassDirection position )
    {
        for ( final DockableFrameListener listener : listenerList.getListeners ( DockableFrameListener.class ) )
        {
            listener.frameMoved ( frame, position );
        }
    }

    /**
     * Informs listeners about frame being removed.
     *
     * @param frame        {@link com.alee.extended.dock.WebDockableFrame} which was removed
     * @param dockablePane {@link com.alee.extended.dock.WebDockablePane} where frame was removed from
     */
    public void fireFrameRemoved ( final WebDockableFrame frame, final WebDockablePane dockablePane )
    {
        for ( final DockableFrameListener listener : listenerList.getListeners ( DockableFrameListener.class ) )
        {
            listener.frameRemoved ( frame, dockablePane );
        }
    }

    @Override
    public void applyComponentOrientation ( final ComponentOrientation orientation )
    {
        if ( orientation == null )
        {
            throw new NullPointerException ( "ComponentOrientation must not be null" );
        }
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

    @Override
    public String getUIClassID ()
    {
        return StyleManager.getDescriptor ( this ).getUIClassId ();
    }
}