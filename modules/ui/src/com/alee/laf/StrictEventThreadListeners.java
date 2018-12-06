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

package com.alee.laf;

import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Mix of listeners checking Event Dispatch Thread.
 *
 * @author Mikle Garin
 */
public class StrictEventThreadListeners implements HierarchyListener, PropertyChangeListener, ComponentListener, MouseListener,
        MouseWheelListener, MouseMotionListener, KeyListener, FocusListener
{
    @Override
    public void hierarchyChanged ( final HierarchyEvent e )
    {
        WebLookAndFeel.checkEventDispatchThread ();
    }

    @Override
    public void propertyChange ( final PropertyChangeEvent evt )
    {
        WebLookAndFeel.checkEventDispatchThread ();
    }

    @Override
    public void componentResized ( final ComponentEvent e )
    {
        WebLookAndFeel.checkEventDispatchThread ();
    }

    @Override
    public void componentMoved ( final ComponentEvent e )
    {
        WebLookAndFeel.checkEventDispatchThread ();
    }

    @Override
    public void componentShown ( final ComponentEvent e )
    {
        WebLookAndFeel.checkEventDispatchThread ();
    }

    @Override
    public void componentHidden ( final ComponentEvent e )
    {
        WebLookAndFeel.checkEventDispatchThread ();
    }

    @Override
    public void mouseClicked ( final MouseEvent e )
    {
        WebLookAndFeel.checkEventDispatchThread ();
    }

    @Override
    public void mousePressed ( final MouseEvent e )
    {
        WebLookAndFeel.checkEventDispatchThread ();
    }

    @Override
    public void mouseReleased ( final MouseEvent e )
    {
        WebLookAndFeel.checkEventDispatchThread ();
    }

    @Override
    public void mouseEntered ( final MouseEvent e )
    {
        WebLookAndFeel.checkEventDispatchThread ();
    }

    @Override
    public void mouseExited ( final MouseEvent e )
    {
        WebLookAndFeel.checkEventDispatchThread ();
    }

    @Override
    public void mouseWheelMoved ( final MouseWheelEvent e )
    {
        WebLookAndFeel.checkEventDispatchThread ();
    }

    @Override
    public void mouseDragged ( final MouseEvent e )
    {
        WebLookAndFeel.checkEventDispatchThread ();
    }

    @Override
    public void mouseMoved ( final MouseEvent e )
    {
        WebLookAndFeel.checkEventDispatchThread ();
    }

    @Override
    public void keyTyped ( final KeyEvent e )
    {
        WebLookAndFeel.checkEventDispatchThread ();
    }

    @Override
    public void keyPressed ( final KeyEvent e )
    {
        WebLookAndFeel.checkEventDispatchThread ();
    }

    @Override
    public void keyReleased ( final KeyEvent e )
    {
        WebLookAndFeel.checkEventDispatchThread ();
    }

    @Override
    public void focusGained ( final FocusEvent e )
    {
        WebLookAndFeel.checkEventDispatchThread ();
    }

    @Override
    public void focusLost ( final FocusEvent e )
    {
        WebLookAndFeel.checkEventDispatchThread ();
    }
}