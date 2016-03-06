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

import javax.swing.*;

/**
 * This TransferHandler allows you to proxify drop event to another JComponent.
 *
 * @author Mikle Garin
 */

public class ProxyDropHandler extends TransferHandler
{
    /**
     * Component onto which drop should be proxified.
     */
    private JComponent component;

    /**
     * Constructs new ProxyDropHandler that passes drop actions to parent components.
     */
    public ProxyDropHandler ()
    {
        super ();
        this.component = null;
    }

    /**
     * Constructs new ProxyDropHandler that passes drop actions to specified component.
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
     * Null means that drop actions are passes to parent components.
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
        if ( component != null )
        {
            final TransferHandler th = component.getTransferHandler ();
            return th != null && th.canImport ( info );
        }
        else
        {
            return DragUtils.canPassDrop ( info );
        }
    }

    @Override
    public boolean importData ( final TransferHandler.TransferSupport info )
    {
        if ( component != null )
        {
            final TransferHandler th = component.getTransferHandler ();
            return th != null && th.importData ( info );
        }
        else
        {
            return DragUtils.passDropAction ( info );
        }
    }
}