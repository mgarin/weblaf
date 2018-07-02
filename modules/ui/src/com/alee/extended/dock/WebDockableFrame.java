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

import com.alee.api.Identifiable;
import com.alee.api.data.CompassDirection;
import com.alee.api.jdk.Objects;
import com.alee.extended.WebContainer;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.painter.decoration.Stateful;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Frame element for {@link WebDockablePane}.
 * It can have its own content which can be easily moved within dockable pane or even outside of its bounds.
 * Frame usually take side space within dockable pane and its center area is taken by some custom content.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see DockableFrameDescriptor
 * @see WDockableFrameUI
 * @see WebDockableFrameUI
 * @see IDockableFramePainter
 * @see DockablePanePainter
 * @see WebContainer
 */
public class WebDockableFrame extends WebContainer<WebDockableFrame, WDockableFrameUI> implements Identifiable, Stateful
{
    /**
     * Component properties.
     */
    public static final String FRAME_ID_PROPERTY = "frameId";
    public static final String STATE_PROPERTY = "state";
    public static final String MAXIMIZED_PROPERTY = "maximized";
    public static final String RESTORE_STATE_PROPERTY = "restoreState";
    public static final String DRAGGABLE_PROPERTY = "draggable";
    public static final String FLOATABLE_PROPERTY = "floatable";
    public static final String MAXIMIZABLE_PROPERTY = "maximizable";
    public static final String CLOSABLE_PROPERTY = "closable";
    public static final String ICON_PROPERTY = "icon";
    public static final String TITLE_PROPERTY = "title";
    public static final String POSITION_PROPERTY = "position";
    public static final String DOCKABLE_PANE_PROPERTY = "dockablePane";

    /**
     * Frame ID unique within its dockable pane.
     * It is used to connect frame with its position data inside dockable pane model.
     */
    protected String frameId;

    /**
     * Current frame state.
     */
    protected DockableFrameState state;

    /**
     * Whether or not frame is maximized.
     */
    protected boolean maximized;

    /**
     * State to restore frame into from {@link com.alee.extended.dock.DockableFrameState#minimized}.
     */
    protected DockableFrameState restoreState;

    /**
     * Position of this frame on dockable pane.
     * This value will only be used if dockable pane didn't save any position for this frame yet.
     */
    protected CompassDirection position;

    /**
     * Frame icon.
     */
    protected Icon icon;

    /**
     * Frame title.
     */
    protected String title;

    /**
     * Whether or not frame can be dragged.
     */
    protected boolean draggable;

    /**
     * Whether or not frame can be separated into floating window from the UI.
     * You can still float the frame from the code even if this setting is set to {@code false}.
     */
    protected boolean floatable;

    /**
     * Whether or not frame can be maximized.
     * It can only be maximized when it is in {@link com.alee.extended.dock.DockableFrameState#docked} or
     * {@link com.alee.extended.dock.DockableFrameState#floating} states.
     */
    protected boolean maximizable;

    /**
     * Whether or not frame can be closed from the UI.
     * You can still close the frame from the code even if this setting is set to {@code false}.
     */
    protected boolean closable;

    /**
     * Dockable pane this frame is added into.
     */
    protected WebDockablePane dockablePane;

    /**
     * Constructs new {@link com.alee.extended.dock.WebDockableFrame}.
     *
     * @param frameId unique frame ID
     * @param title   frame title
     */
    public WebDockableFrame ( final String frameId, final String title )
    {
        this ( StyleId.auto, frameId, title );
    }

    /**
     * Constructs new {@link com.alee.extended.dock.WebDockableFrame}.
     *
     * @param frameId unique frame ID
     * @param icon    frame icon
     */
    public WebDockableFrame ( final String frameId, final Icon icon )
    {
        this ( StyleId.auto, frameId, icon );
    }

    /**
     * Constructs new {@link com.alee.extended.dock.WebDockableFrame}.
     *
     * @param frameId unique frame ID
     * @param icon    frame icon
     * @param title   frame title
     */
    public WebDockableFrame ( final String frameId, final Icon icon, final String title )
    {
        this ( StyleId.auto, frameId, icon, title );
    }

    /**
     * Constructs new {@link com.alee.extended.dock.WebDockableFrame}.
     *
     * @param id      style ID
     * @param frameId unique frame ID
     * @param title   frame title
     */
    public WebDockableFrame ( final StyleId id, final String frameId, final String title )
    {
        this ( id, frameId, null, title );
    }

    /**
     * Constructs new {@link com.alee.extended.dock.WebDockableFrame}.
     *
     * @param id      style ID
     * @param frameId unique frame ID
     * @param icon    frame icon
     */
    public WebDockableFrame ( final StyleId id, final String frameId, final Icon icon )
    {
        this ( id, frameId, icon, "" );
    }

    /**
     * Constructs new {@link com.alee.extended.dock.WebDockableFrame}.
     *
     * @param id      style ID
     * @param frameId unique frame ID
     * @param icon    frame icon
     * @param title   frame title
     */
    public WebDockableFrame ( final StyleId id, final String frameId, final Icon icon, final String title )
    {
        super ();
        setId ( frameId );
        setIcon ( icon );
        setTitle ( title );
        updateUI ();
        setStyleId ( id );
    }

    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.dockableframe;
    }

    @Override
    public String getId ()
    {
        return frameId;
    }

    /**
     * Sets frame ID unique within its dockable pane.
     *
     * @param id frame ID unique within its dockable pane
     * @return this frame
     */
    public WebDockableFrame setId ( final String id )
    {
        if ( Objects.notEquals ( this.frameId, id ) )
        {
            final String old = this.frameId;
            this.frameId = id;
            firePropertyChange ( FRAME_ID_PROPERTY, old, id );
        }
        return this;
    }

    @Override
    public List<String> getStates ()
    {
        return CollectionUtils.asList ( getState ().name () );
    }

    /**
     * Returns current frame state.
     *
     * @return current frame state
     */
    public DockableFrameState getState ()
    {
        return state;
    }

    /**
     * Sets frame state.
     *
     * @param state frame state
     * @return this frame
     */
    public WebDockableFrame setState ( final DockableFrameState state )
    {
        if ( this.state != state )
        {
            final DockableFrameState old = this.state;
            this.state = state;
            firePropertyChange ( STATE_PROPERTY, old, state );
        }
        return this;
    }

    /**
     * Returns whether or not frame is maximized.
     *
     * @return true if frame is maximized, false otherwise
     */
    public boolean isMaximized ()
    {
        return maximized;
    }

    /**
     * Sets whether or not frame is maximized.
     *
     * @param maximized whether or not frame is maximized
     * @return this frame
     */
    public WebDockableFrame setMaximized ( final boolean maximized )
    {
        if ( Objects.notEquals ( this.maximized, maximized ) )
        {
            final boolean old = this.maximized;
            this.maximized = maximized;
            firePropertyChange ( MAXIMIZED_PROPERTY, old, maximized );
        }
        return this;
    }

    /**
     * Returns state to restore frame into from {@link com.alee.extended.dock.DockableFrameState#minimized}.
     *
     * @return state to restore frame into from {@link com.alee.extended.dock.DockableFrameState#minimized}
     */
    public DockableFrameState getRestoreState ()
    {
        return restoreState;
    }

    /**
     * Sets state to restore frame into from {@link com.alee.extended.dock.DockableFrameState#minimized}.
     *
     * @param state state to restore frame into from {@link com.alee.extended.dock.DockableFrameState#minimized}
     * @return this frame
     */
    public WebDockableFrame setRestoreState ( final DockableFrameState state )
    {
        if ( this.restoreState != state )
        {
            final DockableFrameState old = this.restoreState;
            this.restoreState = state;
            firePropertyChange ( RESTORE_STATE_PROPERTY, old, state );
        }
        return this;
    }

    /**
     * Returns position of this frame on dockable pane.
     *
     * @return position of this frame on dockable pane
     */
    public CompassDirection getPosition ()
    {
        return position;
    }

    /**
     * Sets position of this frame on dockable pane.
     *
     * @param position position of this frame on dockable pane
     * @return this frame
     */
    public WebDockableFrame setPosition ( final CompassDirection position )
    {
        if ( this.position != position )
        {
            final CompassDirection old = this.position;
            this.position = position;
            firePropertyChange ( POSITION_PROPERTY, old, position );
        }
        return this;
    }

    /**
     * Returns frame icon.
     *
     * @return frame icon
     */
    public Icon getIcon ()
    {
        return icon;
    }

    /**
     * Sets frame icon.
     *
     * @param icon frame icon
     * @return this frame
     */
    public WebDockableFrame setIcon ( final Icon icon )
    {
        if ( this.icon != icon )
        {
            final Icon old = this.icon;
            this.icon = icon;
            firePropertyChange ( ICON_PROPERTY, old, icon );
        }
        return this;
    }

    /**
     * Returns frame title.
     *
     * @return frame title
     */
    public String getTitle ()
    {
        return title;
    }

    /**
     * Sets frame title.
     *
     * @param title frame title
     * @return this frame
     */
    public WebDockableFrame setTitle ( final String title )
    {
        if ( Objects.notEquals ( this.title, title ) )
        {
            final String old = this.title;
            this.title = title;
            firePropertyChange ( TITLE_PROPERTY, old, title );
        }
        return this;
    }

    /**
     * Returns whether or not frame can be dragged.
     *
     * @return true if frame can be dragged, false otherwise
     */
    public boolean isDraggable ()
    {
        return draggable;
    }

    /**
     * Sets whether or not frame can be dragged.
     *
     * @param draggable whether or not frame can be dragged
     * @return this frame
     */
    public WebDockableFrame setDraggable ( final boolean draggable )
    {
        if ( this.draggable != draggable )
        {
            final boolean old = this.draggable;
            this.draggable = draggable;
            firePropertyChange ( DRAGGABLE_PROPERTY, old, draggable );
        }
        return this;
    }

    /**
     * Returns whether or not frame can be separated into floating window from the UI.
     *
     * @return true if frame can be separated into floating window from the UI, false otherwise
     */
    public boolean isFloatable ()
    {
        return floatable;
    }

    /**
     * Sets whether or not frame can be separated into floating window from the UI.
     *
     * @param floatable whether or not frame can be separated into floating window from the UI
     * @return this frame
     */
    public WebDockableFrame setFloatable ( final boolean floatable )
    {
        if ( this.floatable != floatable )
        {
            final boolean old = this.floatable;
            this.floatable = floatable;
            firePropertyChange ( FLOATABLE_PROPERTY, old, floatable );
        }
        return this;
    }

    /**
     * Returns whether or not frame can be maximized.
     *
     * @return true if frame can be maximized, false otherwise
     */
    public boolean isMaximizable ()
    {
        return maximizable;
    }

    /**
     * Sets whether or not frame can be maximized.
     *
     * @param maximizable whether or not frame can be maximized
     * @return this frame
     */
    public WebDockableFrame setMaximizable ( final boolean maximizable )
    {
        if ( this.maximizable != maximizable )
        {
            final boolean old = this.maximizable;
            this.maximizable = maximizable;
            firePropertyChange ( MAXIMIZABLE_PROPERTY, old, maximizable );
        }
        return this;
    }

    /**
     * Returns whether or not frame can be closed from the UI.
     *
     * @return true if frame can be closed from the UI, false otherwise
     */
    public boolean isClosable ()
    {
        return closable;
    }

    /**
     * Sets whether or not frame can be closed from the UI.
     *
     * @param closable whether or not frame can be closed from the UI
     * @return this frame
     */
    public WebDockableFrame setClosable ( final boolean closable )
    {
        if ( this.closable != closable )
        {
            final boolean old = this.closable;
            this.closable = closable;
            firePropertyChange ( CLOSABLE_PROPERTY, old, closable );
        }
        return this;
    }

    /**
     * Returns dockable pane this frame is added into.
     *
     * @return dockable pane this frame is added into
     */
    public WebDockablePane getDockablePane ()
    {
        return dockablePane;
    }

    /**
     * Sets dockable pane this frame is added into.
     *
     * @param dockablePane dockable pane this frame is added into
     * @return this frame
     */
    protected WebDockableFrame setDockablePane ( final WebDockablePane dockablePane )
    {
        if ( this.dockablePane != dockablePane )
        {
            final WebDockablePane old = this.dockablePane;
            this.dockablePane = dockablePane;
            firePropertyChange ( DOCKABLE_PANE_PROPERTY, old, dockablePane );
        }
        return this;
    }

    /**
     * Returns sidebar button for this frame.
     *
     * @return sidebar button for this frame
     */
    public JComponent getSidebarButton ()
    {
        return getUI ().getSidebarButton ();
    }

    /**
     * Returns whether or not sidebar button is currently visible.
     *
     * @return true if sidebar button is currently visible, false otherwise
     */
    public boolean isSidebarButtonVisible ()
    {
        final WebDockablePane dockablePane = getDockablePane ();
        if ( dockablePane != null )
        {
            switch ( dockablePane.getSidebarVisibility () )
            {
                case never:
                    return false;

                case minimized:
                    return isMinimized () || isPreview ();

                case anyMinimized:
                    if ( !isClosed () )
                    {
                        for ( final WebDockableFrame frame : dockablePane.getFrames ( getPosition () ) )
                        {
                            if ( frame.isMinimized () || frame.isPreview () )
                            {
                                return true;
                            }
                        }
                    }
                    return false;

                case always:
                    return isOpened ();
            }
        }
        return false;
    }

    /**
     * Returns whether or not frame is closed.
     *
     * @return true if frame is closed, false otherwise
     */
    public boolean isClosed ()
    {
        return state == DockableFrameState.closed;
    }

    /**
     * Returns whether or not frame is opened.
     *
     * @return true if frame is opened, false otherwise
     */
    public boolean isOpened ()
    {
        return state != DockableFrameState.closed;
    }

    /**
     * Returns whether or not frame is minimized.
     *
     * @return true if frame is minimized, false otherwise
     */
    public boolean isMinimized ()
    {
        return state == DockableFrameState.minimized;
    }

    /**
     * Returns whether or not frame is in preview state.
     *
     * @return true if frame is in preview state, false otherwise
     */
    public boolean isPreview ()
    {
        return state == DockableFrameState.preview;
    }

    /**
     * Returns whether or not frame is docked.
     *
     * @return true if frame is docked, false otherwise
     */
    public boolean isDocked ()
    {
        return state == DockableFrameState.docked;
    }

    /**
     * Returns whether or not frame is floating.
     *
     * @return true if frame is floating, false otherwise
     */
    public boolean isFloating ()
    {
        return state == DockableFrameState.floating;
    }

    /**
     * Hides this dockable frame, only its sidebar button will be visible.
     * If {@link #maximized} was {@code true} it will be switched to {@code false}.
     *
     * @return this frame
     */
    public WebDockableFrame minimize ()
    {
        return setState ( DockableFrameState.minimized );
    }

    /**
     * Docks this dockable frame to its last position on the pane.
     *
     * @return this frame
     */
    public WebDockableFrame dock ()
    {
        return setState ( DockableFrameState.docked );
    }

    /**
     * Displays frame preview on top of other pane elements, sidebar button will still be kept visible.
     *
     * @return this frame
     */
    public WebDockableFrame preview ()
    {
        return setState ( DockableFrameState.preview );
    }

    /**
     * Displays frame on a separate non-modal dialog window attached to dockable frame window.
     *
     * @return this frame
     */
    public WebDockableFrame detach ()
    {
        return setState ( DockableFrameState.floating );
    }

    /**
     * Maximizes dockable frame.
     * Frame will be made
     */
    public void maximize ()
    {
        if ( !isMaximized () )
        {
            // Restoring frame state if needed
            if ( !isDocked () && !isFloating () )
            {
                restore ();
            }

            // Maximize frame
            setMaximized ( true );
        }
        else
        {
            // Restore frame size
            setMaximized ( false );
        }
    }

    /**
     * Restores either docked or floating state of this dockable frame.
     *
     * @return this frame
     */
    public WebDockableFrame restore ()
    {
        return setState ( getRestoreState () );
    }

    /**
     * Closes this dockable frame.
     *
     * @return this frame
     */
    public WebDockableFrame close ()
    {
        return setState ( DockableFrameState.closed );
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
     */
    public void fireFrameAdded ()
    {
        for ( final DockableFrameListener listener : listenerList.getListeners ( DockableFrameListener.class ) )
        {
            listener.frameAdded ( this, dockablePane );
        }
    }

    /**
     * Informs listeners about frame state change.
     *
     * @param oldState previous frame state
     * @param newState current frame state
     */
    public void fireFrameStateChanged ( final DockableFrameState oldState, final DockableFrameState newState )
    {
        for ( final DockableFrameListener listener : listenerList.getListeners ( DockableFrameListener.class ) )
        {
            listener.frameStateChanged ( this, oldState, newState );
        }
    }

    /**
     * Informs listeners about frame being moved.
     *
     * @param position current frame position relative to content
     */
    public void fireFrameMoved ( final CompassDirection position )
    {
        for ( final DockableFrameListener listener : listenerList.getListeners ( DockableFrameListener.class ) )
        {
            listener.frameMoved ( this, position );
        }
    }

    /**
     * Informs listeners about frame being removed.
     */
    public void fireFrameRemoved ()
    {
        for ( final DockableFrameListener listener : listenerList.getListeners ( DockableFrameListener.class ) )
        {
            listener.frameRemoved ( this, dockablePane );
        }
    }

    @Override
    public void applyComponentOrientation ( final ComponentOrientation orientation )
    {
        super.applyComponentOrientation ( orientation );
        getSidebarButton ().applyComponentOrientation ( orientation );
    }

    /**
     * Returns the look and feel (LaF) object that renders this component.
     *
     * @return the {@link WDockableFrameUI} object that renders this component
     */
    public WDockableFrameUI getUI ()
    {
        return ( WDockableFrameUI ) ui;
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WDockableFrameUI}
     */
    public void setUI ( final WDockableFrameUI ui )
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