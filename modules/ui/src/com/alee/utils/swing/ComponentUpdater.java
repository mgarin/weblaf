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

import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * This class allows you to install (and uninstall if needed) component updater to any JComponent quickly without any additional coding,
 * which allows you to add optimized rolling updates for the component from the added ActionListeners.
 *
 * @author Mikle Garin
 */
public class ComponentUpdater extends WebTimer implements AncestorListener
{
    private JComponent component;

    public ComponentUpdater ( final JComponent component )
    {
        super ( SwingUtils.frameRateDelay ( 36 ) );
        initialize ( component );
    }

    public ComponentUpdater ( final JComponent component, final long delay )
    {
        super ( delay );
        initialize ( component );
    }

    public ComponentUpdater ( final JComponent component, final String name, final long delay )
    {
        super ( name, delay );
        initialize ( component );
    }

    public ComponentUpdater ( final JComponent component, final long delay, final long initialDelay )
    {
        super ( delay, initialDelay );
        initialize ( component );
    }

    public ComponentUpdater ( final JComponent component, final String name, final long delay, final long initialDelay )
    {
        super ( name, delay, initialDelay );
        initialize ( component );
    }

    public ComponentUpdater ( final JComponent component, final long delay, final ActionListener listener )
    {
        super ( delay, listener );
        initialize ( component );
    }

    public ComponentUpdater ( final JComponent component, final String name, final long delay, final ActionListener listener )
    {
        super ( name, delay, listener );
        initialize ( component );
    }

    public ComponentUpdater ( final JComponent component, final long delay, final long initialDelay, final ActionListener listener )
    {
        super ( delay, initialDelay, listener );
        initialize ( component );
    }

    public ComponentUpdater ( final JComponent component, final String name, final long delay, final long initialDelay,
                              final ActionListener listener )
    {
        super ( name, delay, initialDelay, listener );
        initialize ( component );
    }

    private void initialize ( final JComponent component )
    {
        this.component = component;
        setUseDaemonThread ( true );
        setUseEventDispatchThread ( true );
        component.addAncestorListener ( this );
    }

    public void uninstall ()
    {
        component.removeAncestorListener ( this );
    }

    public Component getComponent ()
    {
        return component;
    }

    @Override
    public void ancestorAdded ( final AncestorEvent event )
    {
        start ();
    }

    @Override
    public void ancestorRemoved ( final AncestorEvent event )
    {
        stop ();
    }

    @Override
    public void ancestorMoved ( final AncestorEvent event )
    {
        //
    }

    public static ComponentUpdater install ( final JComponent component )
    {
        uninstall ( component );
        return new ComponentUpdater ( component );
    }

    public static ComponentUpdater install ( final JComponent component, final long delay )
    {
        uninstall ( component );
        return new ComponentUpdater ( component, delay );
    }

    public static ComponentUpdater install ( final JComponent component, final String name, final long delay )
    {
        uninstall ( component );
        return new ComponentUpdater ( component, name, delay );
    }

    public static ComponentUpdater install ( final JComponent component, final long delay, final long initialDelay )
    {
        uninstall ( component );
        return new ComponentUpdater ( component, delay, initialDelay );
    }

    public static ComponentUpdater install ( final JComponent component, final String name, final long delay, final long initialDelay )
    {
        uninstall ( component );
        return new ComponentUpdater ( component, name, delay, initialDelay );
    }

    public static ComponentUpdater install ( final JComponent component, final long delay, final ActionListener listener )
    {
        uninstall ( component );
        return new ComponentUpdater ( component, delay, listener );
    }

    public static ComponentUpdater install ( final JComponent component, final String name, final long delay,
                                             final ActionListener listener )
    {
        uninstall ( component );
        return new ComponentUpdater ( component, name, delay, listener );
    }

    public static ComponentUpdater install ( final JComponent component, final long delay, final long initialDelay,
                                             final ActionListener listener )
    {
        uninstall ( component );
        return new ComponentUpdater ( component, delay, initialDelay, listener );
    }

    public static ComponentUpdater install ( final JComponent component, final String name, final long delay, final long initialDelay,
                                             final ActionListener listener )
    {
        uninstall ( component );
        return new ComponentUpdater ( component, name, delay, initialDelay, listener );
    }

    public static void uninstall ( final JComponent component )
    {
        for ( final AncestorListener listener : component.getAncestorListeners () )
        {
            if ( listener instanceof ComponentUpdater )
            {
                component.removeAncestorListener ( listener );
            }
        }
    }

    public static boolean isInstalled ( final JComponent component )
    {
        for ( final AncestorListener listener : component.getAncestorListeners () )
        {
            if ( listener instanceof ComponentUpdater )
            {
                return true;
            }
        }
        return false;
    }
}