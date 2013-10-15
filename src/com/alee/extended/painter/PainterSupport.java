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

package com.alee.extended.painter;

import com.alee.utils.LafUtils;
import com.alee.utils.swing.BorderMethods;

import javax.swing.*;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * This special class provides basic methods to link painter with components.
 *
 * @author Mikle Garin
 */

public final class PainterSupport
{
    /**
     * Installed painters map.
     */
    private static final Map<JComponent, Map<Painter, PainterListener>> installedPainters =
            new WeakHashMap<JComponent, Map<Painter, PainterListener>> ();

    /**
     * Installs painter into the specified component.
     *
     * @param component component painter is applied to
     * @param painter   painter to install
     */
    public static void installPainter ( final JComponent component, final Painter painter )
    {
        if ( component == null || painter == null )
        {
            return;
        }
        Map<Painter, PainterListener> listeners = installedPainters.get ( component );
        if ( listeners == null )
        {
            listeners = new WeakHashMap<Painter, PainterListener> ( 1 );
            installedPainters.put ( component, listeners );
        }
        if ( !installedPainters.containsKey ( painter ) )
        {
            final PainterListener listener = new PainterListener ()
            {
                @Override
                public void repaint ()
                {
                    component.repaint ();
                }

                @Override
                public void revalidate ()
                {
                    final BorderMethods borderMethods = LafUtils.getBorderMethods ( component );
                    if ( borderMethods != null )
                    {
                        borderMethods.updateBorder ();
                    }
                    component.revalidate ();
                }
            };
            painter.addPainterListener ( listener );
            listeners.put ( painter, listener );
        }
    }

    /**
     * Uninstalls painter from the specified component.
     *
     * @param component component painter is uninstalled from
     * @param painter   painter to uninstall
     */
    public static void uninstallPainter ( final JComponent component, final Painter painter )
    {
        if ( component == null || painter == null )
        {
            return;
        }
        final Map<Painter, PainterListener> listeners = installedPainters.get ( component );
        if ( listeners != null )
        {
            listeners.remove ( painter );
        }
    }
}