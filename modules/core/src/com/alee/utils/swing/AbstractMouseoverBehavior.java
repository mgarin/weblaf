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

package com.alee.utils.swing;

import com.alee.api.Behavior;
import com.alee.utils.CompareUtils;
import com.alee.utils.CoreSwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Mikle Garin
 */

public abstract class AbstractMouseoverBehavior<C extends JComponent, V> extends MouseAdapter implements ComponentListener, Behavior
{
    /**
     * Component into which this behavior is installed.
     */
    protected final C component;

    /**
     * Whether or not behavior should only track mouseover events when component is enabled.
     */
    protected boolean enabledOnly;

    /**
     * Current mouseover object.
     * It is saved explicitly to properly provide previous state object.
     */
    protected V mouseoverObject;

    /**
     * Constructs behavior for the specified component.
     *
     * @param component component into which this behavior is installed
     */
    public AbstractMouseoverBehavior ( final C component )
    {
        this ( component, true );
    }

    /**
     * Constructs behavior for the specified component.
     *
     * @param component   component into which this behavior is installed
     * @param enabledOnly whether or not behavior should only track mouseover events when component is enabled
     */
    public AbstractMouseoverBehavior ( final C component, final boolean enabledOnly )
    {
        super ();
        this.enabledOnly = enabledOnly;
        this.component = component;
    }

    /**
     * Installs behavior into component.
     */
    public void install ()
    {
        component.addMouseListener ( this );
        component.addMouseMotionListener ( this );
        component.addComponentListener ( this );
    }

    /**
     * Uninstalls behavior from the component.
     */
    public void uninstall ()
    {
        component.removeMouseListener ( this );
        component.removeMouseMotionListener ( this );
        component.removeComponentListener ( this );
    }

    /**
     * Returns whether or not behavior should only track mouseover events when component is enabled.
     *
     * @return true if behavior should only track mouseover events when component is enabled, false otherwise
     */
    public boolean isEnabledOnly ()
    {
        return enabledOnly;
    }

    /**
     * Sets whether or not behavior should only track mouseover events when component is enabled.
     *
     * @param enabledOnly whether or not behavior should only track mouseover events when component is enabled
     */
    public void setEnabledOnly ( final boolean enabledOnly )
    {
        this.enabledOnly = enabledOnly;
        updateMouseover ();
    }

    @Override
    public void mouseEntered ( final MouseEvent e )
    {
        updateMouseover ( e );
    }

    @Override
    public void mouseMoved ( final MouseEvent e )
    {
        updateMouseover ( e );
    }

    @Override
    public void mouseDragged ( MouseEvent e )
    {
        updateMouseover ( e );
    }

    @Override
    public void mouseExited ( final MouseEvent e )
    {
        clearMouseover ();
    }

    @Override
    public void componentResized ( final ComponentEvent e )
    {
        updateMouseover ();
    }

    @Override
    public void componentMoved ( final ComponentEvent e )
    {
        updateMouseover ();
    }

    @Override
    public void componentShown ( final ComponentEvent e )
    {
        updateMouseover ();
    }

    @Override
    public void componentHidden ( final ComponentEvent e )
    {
        updateMouseover ();
    }

    /**
     * Updates mouseover index.
     */
    protected void updateMouseover ()
    {
        if ( component.isShowing () )
        {
            final Point mousePoint = CoreSwingUtils.getMousePoint ( component );
            if ( component.getVisibleRect ().contains ( mousePoint ) )
            {
                updateMouseover ( mousePoint );
            }
            else
            {
                clearMouseover ();
            }
        }
        else
        {
            clearMouseover ();
        }
    }

    /**
     * Updates mouseover index.
     *
     * @param e mouse event
     */
    protected void updateMouseover ( final MouseEvent e )
    {
        updateMouseover ( e.getPoint () );
    }

    /**
     * Updates mouseover path.
     *
     * @param point mouse position on the component
     */
    protected void updateMouseover ( final Point point )
    {
        // Disabled components aren't affected
        if ( !enabledOnly || component.isEnabled () )
        {
            checkMouseoverChange ( getObjectAt ( point ) );
        }
    }

    /**
     * Returns object at the specified point.
     *
     * @param point mouseover point
     * @return object at the specified point
     */
    protected abstract V getObjectAt ( Point point );

    /**
     * Clears mouseover path.
     */
    protected void clearMouseover ()
    {
        checkMouseoverChange ( null );
    }

    /**
     * Checks mouseover object change and fires event if it has changed.
     *
     * @param object mouseover object
     */
    protected void checkMouseoverChange ( final V object )
    {
        if ( !CompareUtils.equals ( object, mouseoverObject ) )
        {
            final V previousPath = mouseoverObject;
            this.mouseoverObject = object;
            mouseoverChanged ( previousPath, mouseoverObject );
        }
    }

    /**
     * Informs when mouseover object changes.
     *
     * @param previous previous mouseover object
     * @param current  current mouseover object
     */
    public abstract void mouseoverChanged ( V previous, V current );
}