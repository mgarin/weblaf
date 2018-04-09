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

import com.alee.extended.layout.MultiLayout;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.style.ShapeMethods;
import com.alee.managers.style.StyleId;

import java.awt.*;

/**
 * This special container is used to place various custom WebLaF popups on it.
 * These lightweight popups are visible only within the window's root pane bounds.
 *
 * @author Mikle Garin
 * @see com.alee.managers.popup.PopupManager
 * @see com.alee.managers.popup.WebInnerPopup
 */
public class PopupLayer extends WebPanel
{
    /**
     * Constructs new popup layer.
     */
    public PopupLayer ()
    {
        this ( new MultiLayout () );
    }

    /**
     * Constructs new popup layer with the specified custom layout manager.
     *
     * @param layoutManager custom layout manager for this layer
     */
    public PopupLayer ( final LayoutManager layoutManager )
    {
        super ( StyleId.panelTransparent, layoutManager );
        setOpaque ( false );
    }

    /**
     * Returns popup layer actual layout.
     *
     * @return popup layer actual layout
     */
    public MultiLayout getMultiLayout ()
    {
        return ( MultiLayout ) getLayout ();
    }

    /**
     * Adds layout manager to this popup layer.
     *
     * @param layoutManager layout manager to add
     */
    public void addLayoutManager ( final LayoutManager layoutManager )
    {
        getMultiLayout ().addLayoutManager ( layoutManager );
    }

    /**
     * Removes layout manager from this glass pane.
     *
     * @param layoutManager layout manager to remove
     */
    public void removeLayoutManager ( final LayoutManager layoutManager )
    {
        getMultiLayout ().removeLayoutManager ( layoutManager );
    }

    /**
     * Hides all popups visible on this popup layer.
     */
    public void hideAllPopups ()
    {
        // todo Call hidePopup on popup instead
        removeAll ();
        setVisible ( false );
    }

    /**
     * Displays the specified popup on this popup layer.
     *
     * @param popup popup to display
     */
    public void showPopup ( final WebInnerPopup popup )
    {
        // Informing that popup will now become visible
        popup.firePopupWillBeOpened ();

        // Updating popup layer
        setBounds ( new Rectangle ( 0, 0, getParent ().getWidth (), getParent ().getHeight () ) );

        // Adding popup
        add ( popup, 0 );

        // Updating popup position and content
        setVisible ( true );
        popup.revalidate ();
        popup.repaint ();
    }

    /**
     * Hides specified popup displayed on this popup layer.
     *
     * @param popup popup to hide
     */
    public void hidePopup ( final WebInnerPopup popup )
    {
        // Ignore hide
        if ( !popup.isShowing () || popup.getParent () != PopupLayer.this )
        {
            return;
        }

        // Informing that popup will now become invisible
        popup.firePopupWillBeClosed ();

        // Removing popup
        final Rectangle bounds = popup.getBounds ();
        remove ( popup );
        revalidate ();
        repaint ( bounds );
    }

    /**
     * Unlike default "contains" method this one returns true only if some of popups displayed on this layer contains the specified point.
     * Popup layer itself is not taken into account and doesn't absorb any mouse events because of that.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return true if some of popups displayed on this popup later contains the specified point, false otherwise
     */
    @Override
    public boolean contains ( final int x, final int y )
    {
        for ( final Component child : getComponents () )
        {
            final Point l = child.getLocation ();
            if ( child instanceof ShapeMethods )
            {
                final Shape shape = ( ( ShapeMethods ) child ).getShape ();
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

    /**
     * Returns whether the specified point is within bounds of this popup layer or not.
     * This method returns default "contains" method result and might be used by some classes that would like to change layer's behavior.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return true if the specified point is within bounds of this popup layer, false otherwise
     */
    public boolean normalContains ( final int x, final int y )
    {
        return super.contains ( x, y );
    }
}