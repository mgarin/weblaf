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

import javax.swing.*;

/**
 * User: mgarin Date: 19.04.12 Time: 12:36
 * <p/>
 * This TransferHandler allows you to proxify drop event to another JComponent
 */

public class ProxyDropHandler extends TransferHandler
{
    // Component onto which drop should be proxified
    private JComponent component;

    public ProxyDropHandler ( JComponent component )
    {
        super ();
        this.component = component;
    }

    @Override
    public boolean canImport ( TransferHandler.TransferSupport info )
    {
        TransferHandler th = component.getTransferHandler ();
        return th != null && th.canImport ( info );
    }

    @Override
    public boolean importData ( TransferHandler.TransferSupport info )
    {
        TransferHandler th = component.getTransferHandler ();
        return th != null && th.importData ( info );
    }
}