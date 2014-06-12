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

package com.alee.extended.statusbar;

import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;

/**
 * User: mgarin Date: 17.10.11 Time: 18:57
 */

public class WebCursorTracker extends WebStatusLabel
{
    public static final ImageIcon CURSOR_ICON = new ImageIcon ( WebCursorTracker.class.getResource ( "icons/cursor.png" ) );

    private AWTEventListener listener = null;

    public WebCursorTracker ()
    {
        super ();

        setIcon ( CURSOR_ICON );

        listener = new AWTEventListener ()
        {
            @Override
            public void eventDispatched ( AWTEvent event )
            {
                if ( event instanceof MouseEvent )
                {
                    MouseEvent e = ( MouseEvent ) event;
                    if ( event.getID () == MouseEvent.MOUSE_ENTERED )
                    {
                        Toolkit.getDefaultToolkit ().addAWTEventListener ( this, AWTEvent.MOUSE_MOTION_EVENT_MASK );
                        updatePosition ( e );
                    }
                    else if ( event.getID () == MouseEvent.MOUSE_EXITED )
                    {
                        Toolkit.getDefaultToolkit ().removeAWTEventListener ( this );
                        Toolkit.getDefaultToolkit ().addAWTEventListener ( listener, AWTEvent.MOUSE_EVENT_MASK );
                        updatePosition ( e );
                    }
                    else if ( event.getID () == MouseEvent.MOUSE_MOVED || event.getID () == MouseEvent.MOUSE_DRAGGED )
                    {
                        updatePosition ( e );
                    }
                }
            }
        };
        Toolkit.getDefaultToolkit ().addAWTEventListener ( listener, AWTEvent.MOUSE_EVENT_MASK );
    }

    private void updatePosition ( final MouseEvent e )
    {
        SwingUtils.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                if ( e != null && e.getComponent () != null &&
                        SwingUtils.getWindowAncestor ( e.getComponent () ) != null )
                {
                    Point wl = SwingUtils.getWindowAncestor ( e.getComponent () ).getLocation ();
                    Point ml = MouseInfo.getPointerInfo ().getLocation ();
                    WebCursorTracker.this.setText ( "x:" + ( ml.x - wl.x ) + " y:" + ( ml.y - wl.y ) );
                }
                else
                {
                    WebCursorTracker.this.setText ( "Outside of area" );
                }
            }
        } );
    }
}
