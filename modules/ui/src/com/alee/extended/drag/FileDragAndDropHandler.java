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

package com.alee.extended.drag;

import com.alee.utils.DragUtils;
import com.alee.utils.drag.FileTransferable;

import javax.swing.*;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Custom TransferHandler that provides easy and convenient way to support files DnD to/from any Swing component.
 * <p>
 * To provide drag functionality override {@code filesDropped()} method and enable drag operation through methods or constructor.
 * You will also have to call "exportAsDrag" method of TransferHandler to initialize drag operation from where you want it to start.
 * Usually that action is done from MouseMotionListener drag method.
 * <p>
 * To provide drop functionality imply override {@code filesDropped()} method and enable drop operation through methods or constructor.
 *
 * @author Mikle Garin
 */

public class FileDragAndDropHandler extends TransferHandler
{
    /**
     * Whether files drag is enabled or not.
     */
    protected boolean dragEnabled;

    /**
     * Desired drag action.
     */
    protected int dragAction = COPY;

    /**
     * Whether files drop is enabled or not.
     */
    protected boolean dropEnabled;

    /**
     * Constructs new FileDragAndDropHandler that allows only file drop operations.
     */
    public FileDragAndDropHandler ()
    {
        this ( false, true );
    }

    /**
     * Constructs new FileDragAndDropHandler that might allow drag and/or drop operations.
     *
     * @param dragEnabled whether drag operations are allowed
     * @param dropEnabled whether drop operations are allowed
     */
    public FileDragAndDropHandler ( final boolean dragEnabled, final boolean dropEnabled )
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
     * Returns desired drag action.
     * This is either {@code TransferHandler.COPY} or {@code TransferHandler.MOVE} constant.
     *
     * @return desired drag action
     */
    public int getDragAction ()
    {
        return dragAction;
    }

    /**
     * Sets desired drag action.
     * This should be either {@code TransferHandler.COPY} or {@code TransferHandler.MOVE} constant.
     *
     * @param action new desired drag action
     */
    public void setDragAction ( final int action )
    {
        this.dragAction = action;
    }

    @Override
    public int getSourceActions ( final JComponent c )
    {
        return getDragAction ();
    }

    @Override
    protected Transferable createTransferable ( final JComponent c )
    {
        if ( isDragEnabled () )
        {
            final List<File> draggedFiles = filesDragged ();
            return draggedFiles != null && draggedFiles.size () > 0 ? new FileTransferable ( draggedFiles ) : null;
        }
        else
        {
            return null;
        }
    }

    /**
     * Informs that drag action has started and list of dragged files should be provided.
     * You can override this method to provide custom list of dragged files.
     *
     * @return list of dragged files
     */
    public List<File> filesDragged ()
    {
        final File file = fileDragged ();
        return file != null ? Arrays.asList ( file ) : null;
    }

    /**
     * Informs that drag action has started and dragged file should be provided.
     * You can override this method to provide custom dragged file.
     *
     * @return dragged file
     */
    public File fileDragged ()
    {
        return null;
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

    @Override
    public boolean canImport ( final TransferHandler.TransferSupport info )
    {
        return isDropEnabled ();
    }

    @Override
    public boolean importData ( final TransferHandler.TransferSupport info )
    {
        return info.isDrop () && importData ( info.getTransferable () );
    }

    /**
     * Performs data import checks and action.
     * Returns whether drop was completed or not.
     *
     * @param t dropped transferable
     * @return true if drop was completed, false otherwise
     */
    public boolean importData ( final Transferable t )
    {
        return isDropEnabled () && filesDropped ( DragUtils.getImportedFiles ( t ) );
    }

    /**
     * Informs that specified files were dropped.
     * You can override this method to perform desired actions.
     *
     * @param files list of dropped files
     * @return true if drop was completed, false otherwise
     */
    public boolean filesDropped ( final List<File> files )
    {
        return true;
    }
}