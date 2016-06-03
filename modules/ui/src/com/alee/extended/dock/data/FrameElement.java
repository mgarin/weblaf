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

import com.alee.extended.dock.DockableFrameState;
import com.alee.extended.dock.WebDockableFrame;
import com.alee.extended.dock.WebDockablePane;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.awt.*;
import java.util.List;

/**
 * Dockable structure element representing dockable frame..
 *
 * @author Mikle Garin
 */

@XStreamAlias ("FrameElement")
public class FrameElement extends AbstractStructureElement
{
    /**
     * Saved dockable frame state.
     */
    @XStreamAsAttribute
    protected DockableFrameState state;

    /**
     * State to restore frame into from {@link com.alee.extended.dock.DockableFrameState#hidden}.
     */
    @XStreamAsAttribute
    protected DockableFrameState restoreState;

    /**
     * Constructs new frame element.
     *
     * @param frame dockable frame
     */
    public FrameElement ( final WebDockableFrame frame )
    {
        super ( frame.getId () );
        setState ( frame.getState () );
        setRestoreState ( frame.getRestoreState () );
        setSize ( frame.getPreferredSize () );
    }

    /**
     * Returns saved dockable frame state.
     *
     * @return saved dockable frame state
     */
    public DockableFrameState getState ()
    {
        return state;
    }

    /**
     * Sets saved dockable frame state.
     *
     * @param state saved dockable frame state
     */
    public void setState ( final DockableFrameState state )
    {
        this.state = state;
    }

    /**
     * Returns state to restore frame into from {@link com.alee.extended.dock.DockableFrameState#hidden}.
     *
     * @return state to restore frame into from {@link com.alee.extended.dock.DockableFrameState#hidden}
     */
    public DockableFrameState getRestoreState ()
    {
        return restoreState;
    }

    /**
     * Sets state to restore frame into from {@link com.alee.extended.dock.DockableFrameState#hidden}.
     *
     * @param restoreState state to restore frame into from {@link com.alee.extended.dock.DockableFrameState#hidden}
     */
    public void setRestoreState ( final DockableFrameState restoreState )
    {
        this.restoreState = restoreState;
    }

    @Override
    public boolean isContent ()
    {
        return false;
    }

    @Override
    public boolean isVisible ( final WebDockablePane dockablePane )
    {
        final WebDockableFrame frame = dockablePane.getFrame ( getId () );
        return frame != null && frame.isVisibleOnPane ();
    }

    @Override
    public void layout ( final WebDockablePane dockablePane, final Rectangle bounds, final List<ResizeData> resizeableAreas )
    {
        // Saving bounds
        setBounds ( bounds );

        // Placing dockable frame
        dockablePane.getFrame ( getId () ).setBounds ( bounds );
    }

    @Override
    public Dimension getMinimumSize ( final WebDockablePane dockablePane )
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