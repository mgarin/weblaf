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

package com.alee.managers.popup;

import com.alee.laf.panel.WebPanel;
import com.alee.utils.laf.ShapeProvider;

import java.awt.*;

/**
 * User: mgarin Date: 26.03.12 Time: 14:49
 */

public class PopupLayer extends WebPanel
{
    public PopupLayer ()
    {
        super ( ( LayoutManager ) null );
        setOpaque ( false );
    }

    public void hideAllPopups ()
    {
        removeAll ();
        setVisible ( false );
    }

    public void showPopup ( WebPopup popup )
    {
        // Informing that popup will now become visible
        popup.firePopupWillBeOpened ();

        // Adding popup
        add ( popup, 0 );
        setBounds ( new Rectangle ( 0, 0, getParent ().getWidth (), getParent ().getHeight () ) );
        setVisible ( true );
        revalidate ();
        repaint ();
    }

    public void hidePopup ( WebPopup popup )
    {
        // Ignore hide
        if ( !popup.isShowing () || popup.getParent () != PopupLayer.this )
        {
            return;
        }

        // Informing that popup will now become invisible
        popup.firePopupWillBeClosed ();

        // Removing popup
        Rectangle bounds = popup.getBounds ();
        remove ( popup );
        revalidate ();
        repaint ( bounds );

        // Hiding layer if no popups left
        if ( getComponentCount () == 0 )
        {
            setVisible ( false );
        }
    }

    public boolean contains ( int x, int y )
    {
        for ( Component child : getComponents () )
        {
            Point l = child.getLocation ();
            if ( child instanceof ShapeProvider )
            {
                Shape shape = ( ( ShapeProvider ) child ).provideShape ();
                if ( shape != null && shape.contains ( x - l.x, y - l.y ) )
                {
                    return true;
                }
            }
            else
            {
                if ( child.getBounds ().contains ( x, y ) )
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean normalContains ( int x, int y )
    {
        return super.contains ( x, y );
    }
}