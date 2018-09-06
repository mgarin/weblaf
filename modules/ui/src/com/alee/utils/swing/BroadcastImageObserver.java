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

import com.alee.api.jdk.BiConsumer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

/**
 * Special {@link ImageObserver} implementaion that can broadcast update events to other {@link ImageObserver}s.
 *
 * @author Mikle Garin
 */
public class BroadcastImageObserver implements ImageObserver
{
    /**
     * {@link ImageObserver}s to broadcast update events to.
     * These {@link ImageObserver}s are kept in memory as long as this {@link BroadcastImageObserver} is in memory.
     */
    protected final List<ImageObserver> observers = new ArrayList<ImageObserver> ( 2 );

    /**
     * {@link ImageObserver}s to broadcast update events to.
     * These {@link ImageObserver}s are tied to specific component and will be disposed whenever component is destroyed.
     */
    protected final WeakComponentDataList<JComponent, ImageObserver> componentObservers =
            new WeakComponentDataList<JComponent, ImageObserver> ( "BroadcastImageObserver.ImageObserver", 50 );

    /**
     * Adds {@link ImageObserver} to broadcast update events to.
     *
     * @param observer {@link ImageObserver} to add
     */
    public void addObserver ( final ImageObserver observer )
    {
        synchronized ( observers )
        {
            if ( !observers.contains ( observer ) )
            {
                observers.add ( observer );
            }
        }
    }

    /**
     * Removes {@link ImageObserver} from broadcast list.
     *
     * @param observer {@link ImageObserver} to remove
     */
    public void removeObserver ( final ImageObserver observer )
    {
        synchronized ( observers )
        {
            observers.remove ( observer );
        }
    }

    /**
     * Adds {@link ImageObserver} to broadcast update events to.
     *
     * @param component {@link JComponent} to tie {@link ImageObserver} to
     * @param observer  {@link ImageObserver} to add
     */
    public void addObserver ( final JComponent component, final ImageObserver observer )
    {
        if ( !componentObservers.containsData ( component, observer ) )
        {
            componentObservers.add ( component, observer );
        }
    }

    /**
     * Removes {@link ImageObserver} from broadcast list.
     *
     * @param component {@link JComponent} to untie {@link ImageObserver} from
     * @param observer  {@link ImageObserver} to remove
     */
    public void removeObserver ( final JComponent component, final ImageObserver observer )
    {
        componentObservers.remove ( component, observer );
    }

    @Override
    public boolean imageUpdate ( final Image img, final int flags, final int x, final int y, final int width, final int height )
    {
        if ( ( flags & ( FRAMEBITS | ALLBITS ) ) != 0 )
        {
            synchronized ( observers )
            {
                for ( final ImageObserver observer : observers )
                {
                    observer.imageUpdate ( img, flags, x, y, width, height );
                }
            }
            componentObservers.forEachData ( new BiConsumer<JComponent, ImageObserver> ()
            {
                @Override
                public void accept ( final JComponent component, final ImageObserver observer )
                {
                    observer.imageUpdate ( img, flags, x, y, width, height );
                }
            } );
        }
        return ( flags & ( ALLBITS | ABORT ) ) == 0;
    }
}