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

import com.alee.laf.WebLookAndFeel;
import com.alee.utils.LafUtils;

import javax.swing.*;
import java.lang.ref.WeakReference;
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
     * It is highly recommended to call this method only from EDT.
     *
     * @param component component painter is applied to
     * @param painter   painter to install
     */
    public static void installPainter ( final JComponent component, final Painter painter )
    {
        // Simply ignore this call if empty painter is set or component doesn't exist
        if ( component == null || painter == null )
        {
            return;
        }

        // Installing painter
        Map<Painter, PainterListener> listeners = installedPainters.get ( component );
        if ( listeners == null )
        {
            listeners = new WeakHashMap<Painter, PainterListener> ( 1 );
            installedPainters.put ( component, listeners );
        }
        if ( !installedPainters.containsKey ( painter ) )
        {
            // Installing painter
            painter.install ( component );

            // Applying initial component settings
            LookAndFeel.installProperty ( component, WebLookAndFeel.OPAQUE_PROPERTY, painter.isOpaque ( component ) );

            // Updating border
            LafUtils.updateBorder ( component );

            // Creating weak references to use them inside the listener
            // Otherwise we will force it to keep strong reference to component and painter if we use them directly
            final WeakReference<JComponent> c = new WeakReference<JComponent> ( component );
            final WeakReference<Painter> p = new WeakReference<Painter> ( painter );

            // Adding painter listener
            final PainterListener listener = new PainterListener ()
            {
                @Override
                public void repaint ()
                {
                    // Forcing component to be repainted
                    c.get ().repaint ();
                }

                @Override
                public void repaint ( final int x, final int y, final int width, final int height )
                {
                    // Forcing component to be repainted
                    c.get ().repaint ( x, y, width, height );
                }

                @Override
                public void revalidate ()
                {
                    // todo Move to separate "updateBorder" method in PainterListener?
                    // Forcing border updates
                    LafUtils.updateBorder ( c.get () );

                    // Forcing layout updates
                    c.get ().revalidate ();
                }

                @Override
                public void updateOpacity ()
                {
                    // Updating component opacity according to painter
                    c.get ().setOpaque ( p.get ().isOpaque ( c.get () ) );
                }
            };
            painter.addPainterListener ( listener );
            listeners.put ( painter, listener );
        }
    }

    /**
     * Uninstalls painter from the specified component.
     * It is highly recommended to call this method only from EDT.
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
            // Uninstalling painter
            painter.uninstall ( component );

            // Removing painter listener
            listeners.remove ( painter );
        }
    }
}