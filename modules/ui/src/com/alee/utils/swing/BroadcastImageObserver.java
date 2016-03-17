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

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom image observer for image updates broadcasting.
 *
 * @author Mikle Garin
 */

public class BroadcastImageObserver implements ImageObserver
{
    /**
     * Observers to broadcast update events to.
     */
    protected final List<ImageObserver> observers = new ArrayList<ImageObserver> ( 10 );

    /**
     * Adds new observer to broadcast update events to.
     *
     * @param observer image observer
     */
    public void addObserver ( final ImageObserver observer )
    {
        synchronized ( observers )
        {
            observers.add ( observer );
        }
    }

    /**
     * Removes observer from broadcast list.
     *
     * @param observer image observer
     */
    public void removeObserver ( final ImageObserver observer )
    {
        synchronized ( observers )
        {
            observers.remove ( observer );
        }
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
        }
        return ( flags & ( ALLBITS | ABORT ) ) == 0;
    }
}