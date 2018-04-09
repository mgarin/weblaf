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

package com.alee.managers.drag.transfer;

import javax.swing.*;

/**
 * Abstract {@link TransferHandler} providing simplified API for transfer handler implementation.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-DragManager">How to use DragManager</a>
 * @see com.alee.managers.drag.DragManager
 */
public abstract class AbstractTransferHandler extends TransferHandler
{
    /**
     * Whether files drag is enabled or not.
     */
    protected boolean dragEnabled;

    /**
     * Whether files drop is enabled or not.
     */
    protected boolean dropEnabled;

    /**
     * Desired drag action.
     */
    protected int sourceAction = COPY;

    /**
     * Constructs new {@link com.alee.managers.drag.transfer.AbstractTransferHandler}.
     *
     * @param dragEnabled whether drag operations are allowed
     * @param dropEnabled whether drop operations are allowed
     */
    public AbstractTransferHandler ( final boolean dragEnabled, final boolean dropEnabled )
    {
        super ();
        setDragEnabled ( dragEnabled );
        setDropEnabled ( dropEnabled );
    }

    /**
     * Returns whether drag is enabled or not.
     * If {@code false} is set any drag actions will be blocked.
     *
     * @return true if drag is enabled, false otherwise
     */
    public boolean isDragEnabled ()
    {
        return dragEnabled;
    }

    /**
     * Sets whether drag is enabled or not.
     * If {@code false} is set any drag actions will be blocked.
     *
     * @param enabled whether drag is enabled or not
     */
    public void setDragEnabled ( final boolean enabled )
    {
        this.dragEnabled = enabled;
    }

    /**
     * Returns whether drop is enabled or not.
     * If {@code false} is returned any drop actions are blocked.
     *
     * @return true if drop is enabled, false otherwise
     */
    public boolean isDropEnabled ()
    {
        return dropEnabled;
    }

    /**
     * Sets whether drop is enabled or not.
     * If {@code false} is set any drop actions will be blocked.
     *
     * @param enabled whether drop is enabled or not
     */
    public void setDropEnabled ( final boolean enabled )
    {
        this.dropEnabled = enabled;
    }

    /**
     * Returns desired drag action.
     *
     * @param component component holding data to be transferred
     * @return desired drag action
     * @see TransferHandler#COPY
     * @see TransferHandler#MOVE
     * @see TransferHandler#LINK
     */
    @Override
    public int getSourceActions ( final JComponent component )
    {
        return sourceAction;
    }

    /**
     * Sets desired drag action.
     *
     * @param action new desired drag action
     * @see TransferHandler#COPY
     * @see TransferHandler#MOVE
     * @see TransferHandler#LINK
     */
    public void setSourceAction ( final int action )
    {
        this.sourceAction = action;
    }
}