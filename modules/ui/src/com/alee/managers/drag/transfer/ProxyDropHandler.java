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
import java.awt.*;

/**
 * This {@link TransferHandler} allows you to proxify drop event to another {@link JComponent}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-DragManager">How to use DragManager</a>
 * @see com.alee.managers.drag.DragManager
 */
public class ProxyDropHandler extends TransferHandler
{
    /**
     * {@link JComponent} onto which drop should be proxified.
     */
    private JComponent component;

    /**
     * Constructs new {@link com.alee.managers.drag.transfer.ProxyDropHandler} that passes drop actions to parent components.
     */
    public ProxyDropHandler ()
    {
        super ();
        this.component = null;
    }

    /**
     * Constructs new {@link com.alee.managers.drag.transfer.ProxyDropHandler} that passes drop actions to specified component.
     *
     * @param component component to pass drop actions to
     */
    public ProxyDropHandler ( final JComponent component )
    {
        super ();
        this.component = component;
    }

    /**
     * Returns component to pass drop actions to.
     * {@code null} component means that drop actions are passes to parent components.
     *
     * @return component to pass drop actions to
     */
    public JComponent getComponent ()
    {
        return component;
    }

    /**
     * Sets component to pass drop actions to.
     * If set to null drop actions will be passes to parent components.
     *
     * @param component component to pass drop actions to
     */
    public void setComponent ( final JComponent component )
    {
        this.component = component;
    }

    @Override
    public boolean canImport ( final TransferHandler.TransferSupport info )
    {
        return canImportProxy ( component != null ? component : info.getComponent (), info );
    }

    /**
     * Returns whether can pass drop action to closest component parent that has its own TransferHandler.
     * This might be used to make some components that has drag handler transparent for drop actions.
     *
     * @param component component to pass drop action from
     * @param info      transfer support
     * @return true if drop action succeed, false otherwise
     */
    protected boolean canImportProxy ( final Component component, final TransferHandler.TransferSupport info )
    {
        final Container parent = component.getParent ();
        if ( parent != null && parent instanceof JComponent )
        {
            final TransferHandler th = ( ( JComponent ) parent ).getTransferHandler ();
            return th != null ? th.canImport ( info ) : canImportProxy ( parent, info );
        }
        return false;
    }

    @Override
    public boolean importData ( final TransferHandler.TransferSupport info )
    {
        return importDataProxy ( component != null ? component : info.getComponent (), info );
    }

    /**
     * Passes drop action to closest component parent that has its own TransferHandler.
     * This might be used to make some components that has drag handler transparent for drop actions.
     *
     * @param component component to pass drop action from
     * @param info      transfer support
     * @return true if drop action succeed, false otherwise
     */
    protected boolean importDataProxy ( final Component component, final TransferHandler.TransferSupport info )
    {
        final Container parent = component.getParent ();
        if ( parent != null && parent instanceof JComponent )
        {
            final TransferHandler th = ( ( JComponent ) parent ).getTransferHandler ();
            return th != null ? th.importData ( info ) : importDataProxy ( parent, info );
        }
        return false;
    }
}