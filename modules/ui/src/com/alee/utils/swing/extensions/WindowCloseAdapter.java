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

package com.alee.utils.swing.extensions;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Adapter for {@link ComponentListener} and {@link WindowListener} that informs about window becoming invisible.
 *
 * @author Mikle Garin
 */
public abstract class WindowCloseAdapter implements ComponentListener, WindowListener
{
    /**
     * todo 1. Replace with {@link com.alee.extended.behavior.VisibilityBehavior}
     */

    @Override
    public void componentResized ( final ComponentEvent e )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void componentMoved ( final ComponentEvent e )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void componentShown ( final ComponentEvent e )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void componentHidden ( final ComponentEvent e )
    {
        closed ( e );
    }

    @Override
    public void windowOpened ( final WindowEvent e )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void windowClosing ( final WindowEvent e )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void windowClosed ( final WindowEvent e )
    {
        closed ( e );
    }

    @Override
    public void windowIconified ( final WindowEvent e )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void windowDeiconified ( final WindowEvent e )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void windowActivated ( final WindowEvent e )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void windowDeactivated ( final WindowEvent e )
    {
        /**
         * Do nothing by default.
         */
    }

    /**
     * Notifies that window just closed.
     *
     * @param e component event
     */
    public abstract void closed ( ComponentEvent e );
}