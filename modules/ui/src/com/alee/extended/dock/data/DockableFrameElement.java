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

package com.alee.extended.dock.data;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.extended.dock.DockableFrameState;
import com.alee.extended.dock.WebDockableFrame;
import com.alee.extended.dock.WebDockablePane;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.xml.RectangleConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.awt.*;
import java.util.List;

/**
 * {@link DockableElement} representing dockable frame.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see WebDockablePane
 */
@XStreamAlias ( "DockableFrame" )
public class DockableFrameElement extends AbstractDockableElement
{
    /**
     * Saved dockable frame state.
     * It always stores current frame state whatever it could be.
     */
    @NotNull
    @XStreamAsAttribute
    protected DockableFrameState state;

    /**
     * State to restore frame into from {@link DockableFrameState#minimized}.
     * It is not exactly previous frame state, but the state which is meaningful in terms of restoration.
     * It can be either {@link DockableFrameState#docked} or {@link DockableFrameState#floating}.
     */
    @NotNull
    @XStreamAsAttribute
    protected DockableFrameState restoreState;

    /**
     * Bounds for {@link DockableFrameState#floating} frame state dialog.
     * These bounds contain position of the frame on the screen not including dialog decorations.
     * Initially these bounds are {@code null} and only filled in on the first floating state use.
     */
    @Nullable
    @XStreamAsAttribute
    @XStreamConverter ( RectangleConverter.class )
    protected Rectangle floatingBounds;

    /**
     * Constructs new frame element.
     *
     * @param frame {@link WebDockableFrame} to take initial settings from
     */
    public DockableFrameElement ( @NotNull final WebDockableFrame frame )
    {
        this ( frame.getId (), frame.getState (), frame.getRestoreState (), frame.getPreferredSize () );
    }

    /**
     * Constructs new frame element.
     *
     * @param frame {@link WebDockableFrame} to take initial settings from
     * @param state initial and restore {@link DockableFrameState}
     */
    public DockableFrameElement ( @NotNull final WebDockableFrame frame, @NotNull final DockableFrameState state )
    {
        this ( frame.getId (), state, state, frame.getPreferredSize () );
    }

    /**
     * Constructs new frame element.
     *
     * @param frame {@link WebDockableFrame} to take initial settings from
     * @param size  initial {@link WebDockableFrame} size
     */
    public DockableFrameElement ( @NotNull final WebDockableFrame frame, @NotNull final Dimension size )
    {
        this ( frame.getId (), frame.getState (), frame.getRestoreState (), size );
    }

    /**
     * Constructs new frame element.
     *
     * @param frame {@link WebDockableFrame} to take initial settings from
     * @param state initial and restore {@link DockableFrameState}
     * @param size  initial {@link WebDockableFrame} size
     */
    public DockableFrameElement ( @NotNull final WebDockableFrame frame, @NotNull final DockableFrameState state,
                                  @NotNull final Dimension size )
    {
        this ( frame.getId (), state, state, size );
    }

    /**
     * Constructs new frame element.
     *
     * @param id    {@link WebDockableFrame} identifier
     * @param state initial {@link DockableFrameState}
     * @param size  initial {@link WebDockableFrame} size
     */
    public DockableFrameElement ( @NotNull final String id, @NotNull final DockableFrameState state, @NotNull final Dimension size )
    {
        this ( id, state, state, size );
    }

    /**
     * Constructs new frame element.
     *
     * @param id           {@link WebDockableFrame} identifier
     * @param state        initial {@link DockableFrameState}
     * @param restoreState {@link DockableFrameState} to restore to
     * @param size         initial {@link WebDockableFrame} size
     */
    public DockableFrameElement ( @NotNull final String id, @NotNull final DockableFrameState state,
                                  @NotNull final DockableFrameState restoreState, @NotNull final Dimension size )
    {
        super ( id, size );
        this.state = state;
        this.restoreState = restoreState;
        this.floatingBounds = null;
    }

    /**
     * Returns saved dockable frame state.
     *
     * @return saved dockable frame state
     */
    @NotNull
    public DockableFrameState getState ()
    {
        return state;
    }

    /**
     * Sets saved dockable frame state.
     *
     * @param state saved dockable frame state
     */
    public void setState ( @NotNull final DockableFrameState state )
    {
        this.state = state;
    }

    /**
     * Returns state to restore frame into from {@link DockableFrameState#minimized}.
     *
     * @return state to restore frame into from {@link DockableFrameState#minimized}
     */
    @NotNull
    public DockableFrameState getRestoreState ()
    {
        return restoreState;
    }

    /**
     * Sets state to restore frame into from {@link DockableFrameState#minimized}.
     *
     * @param state state to restore frame into from {@link DockableFrameState#minimized}
     */
    public void setRestoreState ( @NotNull final DockableFrameState state )
    {
        this.restoreState = state;
    }

    /**
     * Returns bounds for {@link DockableFrameState#floating} frame state dialog.
     *
     * @return bounds for {@link DockableFrameState#floating} frame state dialog
     */
    @Nullable
    public Rectangle getFloatingBounds ()
    {
        return floatingBounds;
    }

    /**
     * Sets bounds for {@link DockableFrameState#floating} frame state dialog.
     *
     * @param bounds bounds for {@link DockableFrameState#floating} frame state dialog
     */
    public void setFloatingBounds ( @Nullable final Rectangle bounds )
    {
        this.floatingBounds = bounds;
    }

    /**
     * Saves bounds for {@link DockableFrameState#floating} frame state dialog.
     *
     * @param frame {@link WebDockableFrame}
     */
    public void saveFloatingBounds ( @NotNull final WebDockableFrame frame )
    {
        final Rectangle bounds = CoreSwingUtils.getBoundsOnScreen ( frame, false );
        setFloatingBounds ( bounds );
    }

    @Override
    public boolean isContent ()
    {
        return false;
    }

    @Override
    public boolean isVisible ( @NotNull final WebDockablePane dockablePane )
    {
        final WebDockableFrame frame = dockablePane.findFrame ( getId () );
        return frame != null && frame.isDocked ();
    }

    @Override
    public void layout ( @NotNull final WebDockablePane dockablePane, @NotNull final Rectangle bounds,
                         @NotNull final List<ResizeData> resizeableAreas )
    {
        // Saving bounds
        setBounds ( bounds );

        // Placing dockable frame
        dockablePane.getFrame ( getId () ).setBounds ( bounds );
    }

    @NotNull
    @Override
    public Dimension getMinimumSize ( @NotNull final WebDockablePane dockablePane )
    {
        final Dimension min = dockablePane.getMinimumElementSize ();

        // Validating size
        // This is made here to optimize performance
        if ( size.width < min.width || size.height < min.height )
        {
            setSize ( new Dimension ( Math.max ( size.width, min.width ), Math.max ( size.height, min.height ) ) );
        }

        return min;
    }
}