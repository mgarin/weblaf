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

package com.alee.extended.window;

import com.alee.api.jdk.Supplier;
import com.alee.laf.rootpane.WebRootPaneUI;
import com.alee.laf.window.WebDialog;
import com.alee.managers.style.StyleId;
import com.alee.painter.Painter;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;

/**
 * Custom stylish pop-over dialog with a special corner that follows invoker component.
 * It may also act as a simple dialog with custom styling if configured so.
 *
 * @author Mikle Garin
 * @see WebDialog
 * @see PopOverPainter
 * @see com.alee.laf.menu.AbstractPopupPainter
 * @see PopOverSourcePoint
 * @see PopOverDirection
 * @see PopOverAlignment
 */
public class WebPopOver extends WebDialog implements PopOverEventMethods
{
    /**
     * Special key used to reference popover instance to which root pane is attached.
     */
    public static final String POPOVER_INSTANCE = "popover.instance";

    /**
     * WebPopOver state listeners.
     */
    protected EventListenerList listenerList = new EventListenerList ();

    /**
     * Whether WebPopOver should be movable or not.
     * todo Rename to draggable
     */
    protected boolean movable = true;

    /**
     * Constructs new WebPopOver dialog.
     */
    public WebPopOver ()
    {
        super ( StyleId.popover );
    }

    /**
     * Constructs new WebPopOver dialog.
     *
     * @param owner dialog owner frame
     */
    public WebPopOver ( final Frame owner )
    {
        super ( StyleId.popover, owner );
    }

    /**
     * Constructs new WebPopOver dialog.
     *
     * @param owner dialog owner frame
     * @param title dialog title (might not be displayed depending on styling)
     */
    public WebPopOver ( final Frame owner, final String title )
    {
        super ( StyleId.popover, owner, title );
    }

    /**
     * Constructs new WebPopOver dialog.
     *
     * @param owner dialog owner dialog
     */
    public WebPopOver ( final Dialog owner )
    {
        super ( StyleId.popover, owner );
    }

    /**
     * Constructs new WebPopOver dialog.
     *
     * @param owner dialog owner dialog
     * @param title dialog title (might not be displayed depending on styling)
     */
    public WebPopOver ( final Dialog owner, final String title )
    {
        super ( StyleId.popover, owner, title );
    }

    /**
     * Constructs new WebPopOver dialog.
     *
     * @param owner dialog owner component
     */
    public WebPopOver ( final Component owner )
    {
        super ( StyleId.popover, owner );
    }

    /**
     * Constructs new WebPopOver dialog.
     *
     * @param owner dialog owner component
     * @param title dialog title (might not be displayed depending on styling)
     */
    public WebPopOver ( final Component owner, final String title )
    {
        super ( StyleId.popover, owner, title );
    }

    /**
     * Constructs new WebPopOver dialog.
     *
     * @param owner dialog owner window
     */
    public WebPopOver ( final Window owner )
    {
        super ( StyleId.popover, owner );
    }

    /**
     * Constructs new WebPopOver dialog.
     *
     * @param owner dialog owner window
     * @param title dialog title (might not be displayed depending on styling)
     */
    public WebPopOver ( final Window owner, final String title )
    {
        super ( StyleId.popover, owner, title );
    }

    /**
     * Constructs new WebPopOver dialog.
     *
     * @param id style ID
     */
    public WebPopOver ( final StyleId id )
    {
        super ( id );
    }

    /**
     * Constructs new WebPopOver dialog.
     *
     * @param id    style ID
     * @param owner dialog owner frame
     */
    public WebPopOver ( final StyleId id, final Frame owner )
    {
        super ( id, owner );
    }

    /**
     * Constructs new WebPopOver dialog.
     *
     * @param id    style ID
     * @param owner dialog owner frame
     * @param title dialog title (might not be displayed depending on styling)
     */
    public WebPopOver ( final StyleId id, final Frame owner, final String title )
    {
        super ( id, owner, title );
    }

    /**
     * Constructs new WebPopOver dialog.
     *
     * @param id    style ID
     * @param owner dialog owner dialog
     */
    public WebPopOver ( final StyleId id, final Dialog owner )
    {
        super ( id, owner );
    }

    /**
     * Constructs new WebPopOver dialog.
     *
     * @param id    style ID
     * @param owner dialog owner dialog
     * @param title dialog title (might not be displayed depending on styling)
     */
    public WebPopOver ( final StyleId id, final Dialog owner, final String title )
    {
        super ( id, owner, title );
    }

    /**
     * Constructs new WebPopOver dialog.
     *
     * @param id    style ID
     * @param owner dialog owner component
     */
    public WebPopOver ( final StyleId id, final Component owner )
    {
        super ( id, owner );
    }

    /**
     * Constructs new WebPopOver dialog.
     *
     * @param id    style ID
     * @param owner dialog owner component
     * @param title dialog title (might not be displayed depending on styling)
     */
    public WebPopOver ( final StyleId id, final Component owner, final String title )
    {
        super ( id, owner, title );
    }

    /**
     * Constructs new WebPopOver dialog.
     *
     * @param id    style ID
     * @param owner dialog owner window
     */
    public WebPopOver ( final StyleId id, final Window owner )
    {
        super ( id, owner );
    }

    /**
     * Constructs new WebPopOver dialog.
     *
     * @param id    style ID
     * @param owner dialog owner window
     * @param title dialog title (might not be displayed depending on styling)
     */
    public WebPopOver ( final StyleId id, final Window owner, final String title )
    {
        super ( id, owner, title );
    }

    /**
     * WebPopOver settings initialization.
     *
     * @param owner owner window
     * @param id    initial style ID
     * @param title dialog title
     */
    @Override
    protected void initialize ( final Window owner, final StyleId id, final String title )
    {
        // Initializing base settings
        super.initialize ( owner, id, title );

        // Properly undecorating dialog
        // todo This should be removed along with proper popover styling
        getRootPane ().setWindowDecorationStyle ( JRootPane.NONE );
        setUndecorated ( true );
        setWindowOpaque ( false );
    }

    @Override
    protected JRootPane createRootPane ()
    {
        final JRootPane rootPane = super.createRootPane ();
        rootPane.putClientProperty ( POPOVER_INSTANCE, this );
        return rootPane;
    }

    /**
     * Returns whether this WebPopOver is movable or not.
     *
     * @return true if this WebPopOver is movable, false otherwise
     */
    public boolean isMovable ()
    {
        return movable;
    }

    /**
     * Sets whether this WebPopOver should be movable or not.
     *
     * @param movable whether this WebPopOver should be movable or not
     */
    public void setMovable ( final boolean movable )
    {
        this.movable = movable;
    }

    /**
     * Displays unattached WebPopOver at the specified screen location.
     *
     * @param location WebPopOver location on screen
     * @return displayed WebPopOver
     */
    public WebPopOver show ( final PopOverLocation location )
    {
        // Performing pre-open operations
        preOpen ();

        // Configuring popover position
        final IPopOverPainter painter = getPainter ();
        if ( painter != null )
        {
            painter.configure ( this, location );
        }

        // Displaying popover
        setVisible ( true );

        return this;
    }

    /**
     * Displays unattached WebPopOver at the specified location.
     *
     * @param location WebPopOver location
     * @return displayed WebPopOver
     */
    public WebPopOver show ( final Point location )
    {
        return show ( location.x, location.y );
    }

    /**
     * Displays unattached WebPopOver at the specified location.
     *
     * @param x WebPopOver X location
     * @param y WebPopOver Y location
     * @return displayed WebPopOver
     */
    public WebPopOver show ( final int x, final int y )
    {
        // Performing pre-open operations
        preOpen ();

        // Configuring popover position
        final IPopOverPainter painter = getPainter ();
        if ( painter != null )
        {
            painter.configure ( this, x, y );
        }

        // Displaying popover
        setVisible ( true );

        return this;
    }

    /**
     * Displays WebPopOver attached to the invoker component and faced to preferred direction.
     * WebPopOver opened in this way will always auto-follow invoker's ancestor window.
     *
     * @param invoker invoker component
     * @return displayed WebPopOver
     */
    public WebPopOver show ( final Component invoker )
    {
        return show ( invoker, PopOverDirection.down );
    }

    /**
     * Displays WebPopOver attached to the invoker component and faced to specified direction.
     * WebPopOver opened in this way will always auto-follow invoker's ancestor window.
     *
     * @param invoker   invoker component
     * @param direction preferred display direction
     * @return displayed WebPopOver
     */
    public WebPopOver show ( final Component invoker, final PopOverDirection direction )
    {
        return show ( invoker, direction, PopOverAlignment.centered );
    }

    /**
     * Displays WebPopOver attached to the invoker component and faced to specified direction.
     * It will also be aligned using the specified alignment type when possible.
     * WebPopOver opened in this way will always auto-follow invoker's ancestor window.
     *
     * @param invoker   invoker component
     * @param direction preferred display direction
     * @param alignment preferred display alignment
     * @return displayed WebPopOver
     */
    public WebPopOver show ( final Component invoker, final PopOverDirection direction, final PopOverAlignment alignment )
    {
        return show ( invoker, null, direction, alignment );
    }

    /**
     * Displays WebPopOver attached to the invoker component coordinates and faced to specified direction.
     * It will also be aligned using the specified alignment type when possible.
     * WebPopOver opened in this way will always auto-follow invoker's ancestor window.
     *
     * @param invoker invoker component
     * @param x       source area X coordinate in invoker's component coordinate system
     * @param y       source area Y coordinate in invoker's component coordinate system
     * @return displayed WebPopOver
     */
    public WebPopOver show ( final Component invoker, final int x, final int y )
    {
        return show ( invoker, x, y, PopOverDirection.down );
    }

    /**
     * Displays WebPopOver attached to the invoker component coordinates and faced to specified direction.
     * It will also be aligned using the specified alignment type when possible.
     * WebPopOver opened in this way will always auto-follow invoker's ancestor window.
     *
     * @param invoker   invoker component
     * @param x         source area X coordinate in invoker's component coordinate system
     * @param y         source area Y coordinate in invoker's component coordinate system
     * @param direction preferred display direction
     * @return displayed WebPopOver
     */
    public WebPopOver show ( final Component invoker, final int x, final int y, final PopOverDirection direction )
    {
        return show ( invoker, x, y, direction, PopOverAlignment.centered );
    }

    /**
     * Displays WebPopOver attached to the invoker component coordinates and faced to specified direction.
     * It will also be aligned using the specified alignment type when possible.
     * WebPopOver opened in this way will always auto-follow invoker's ancestor window.
     *
     * @param invoker   invoker component
     * @param x         source area X coordinate in invoker's component coordinate system
     * @param y         source area Y coordinate in invoker's component coordinate system
     * @param direction preferred display direction
     * @param alignment preferred display alignment
     * @return displayed WebPopOver
     */
    public WebPopOver show ( final Component invoker, final int x, final int y, final PopOverDirection direction,
                             final PopOverAlignment alignment )
    {
        return show ( invoker, x, y, 0, 0, direction, alignment );
    }

    /**
     * Displays WebPopOver attached to the invoker component area and faced to specified direction.
     * It will also be aligned using the specified alignment type when possible.
     * WebPopOver opened in this way will always auto-follow invoker's ancestor window.
     *
     * @param invoker invoker component
     * @param x       source area X coordinate in invoker's component coordinate system
     * @param y       source area Y coordinate in invoker's component coordinate system
     * @param w       source area width
     * @param h       source area height
     * @return displayed WebPopOver
     */
    public WebPopOver show ( final Component invoker, final int x, final int y, final int w, final int h )
    {
        return show ( invoker, x, y, w, h, PopOverDirection.down );
    }

    /**
     * Displays WebPopOver attached to the invoker component area and faced to specified direction.
     * It will also be aligned using the specified alignment type when possible.
     * WebPopOver opened in this way will always auto-follow invoker's ancestor window.
     *
     * @param invoker   invoker component
     * @param x         source area X coordinate in invoker's component coordinate system
     * @param y         source area Y coordinate in invoker's component coordinate system
     * @param w         source area width
     * @param h         source area height
     * @param direction preferred display direction
     * @return displayed WebPopOver
     */
    public WebPopOver show ( final Component invoker, final int x, final int y, final int w, final int h, final PopOverDirection direction )
    {
        return show ( invoker, x, y, w, h, direction, PopOverAlignment.centered );
    }

    /**
     * Displays WebPopOver attached to the invoker component area and faced to specified direction.
     * It will also be aligned using the specified alignment type when possible.
     * WebPopOver opened in this way will always auto-follow invoker's ancestor window.
     *
     * @param invoker   invoker component
     * @param x         source area X coordinate in invoker's component coordinate system
     * @param y         source area Y coordinate in invoker's component coordinate system
     * @param w         source area width
     * @param h         source area height
     * @param direction preferred display direction
     * @param alignment preferred display alignment
     * @return displayed WebPopOver
     */
    public WebPopOver show ( final Component invoker, final int x, final int y, final int w, final int h, final PopOverDirection direction,
                             final PopOverAlignment alignment )
    {
        final Rectangle bounds = new Rectangle ( x, y, w, h );
        return show ( invoker, new Supplier<Rectangle> ()
        {
            @Override
            public Rectangle get ()
            {
                return bounds;
            }
        }, direction, alignment );
    }

    /**
     * Displays WebPopOver attached to the invoker component area and faced to specified direction.
     * It will also be aligned using the specified alignment type when possible.
     * WebPopOver opened in this way will always auto-follow invoker's ancestor window.
     *
     * @param invoker        invoker component
     * @param boundsSupplier source area supplier
     * @return displayed WebPopOver
     */
    public WebPopOver show ( final Component invoker, final Supplier<Rectangle> boundsSupplier )
    {
        return show ( invoker, boundsSupplier, PopOverDirection.down );
    }

    /**
     * Displays WebPopOver attached to the invoker component area and faced to specified direction.
     * It will also be aligned using the specified alignment type when possible.
     * WebPopOver opened in this way will always auto-follow invoker's ancestor window.
     *
     * @param invoker        invoker component
     * @param boundsSupplier source area supplier
     * @param direction      preferred display direction
     * @return displayed WebPopOver
     */
    public WebPopOver show ( final Component invoker, final Supplier<Rectangle> boundsSupplier, final PopOverDirection direction )
    {
        return show ( invoker, boundsSupplier, direction, PopOverAlignment.centered );
    }

    /**
     * Displays WebPopOver attached to the invoker component area and faced to specified direction.
     * It will also be aligned using the specified alignment type when possible.
     * WebPopOver opened in this way will always auto-follow invoker's ancestor window.
     *
     * @param invoker        invoker component
     * @param boundsSupplier source area provider
     * @param direction      preferred display direction
     * @param alignment      preferred display alignment
     * @return displayed WebPopOver
     */
    public WebPopOver show ( final Component invoker, final Supplier<Rectangle> boundsSupplier, final PopOverDirection direction,
                             final PopOverAlignment alignment )
    {
        // todo Doesn't position properly for window-type invoker

        // Performing pre-open operations
        preOpen ();

        // Configuring popover position
        final IPopOverPainter painter = getPainter ();
        if ( painter != null )
        {
            painter.configure ( this, invoker, boundsSupplier, direction, alignment );
        }

        // Displaying popover
        setVisible ( true );

        return this;
    }

    /**
     * Returns popover painter.
     *
     * @return popover painter
     */
    protected IPopOverPainter getPainter ()
    {
        // todo Acquire painter properly
        final Painter painter = ( ( WebRootPaneUI ) getUI () ).getPainter ();
        return painter instanceof IPopOverPainter ? ( IPopOverPainter ) painter : null;
    }

    /**
     * Performs popover pre-open operations.
     */
    protected void preOpen ()
    {
        // Fire opening event
        fireOpening ();

        // Fire reopened event
        // This event is fired before any show operation repeated
        if ( isShowing () )
        {
            fireReopened ();
        }
    }

    /**
     * Adds pop-over listener.
     *
     * @param listener pop-over listener
     */
    public void addPopOverListener ( final PopOverListener listener )
    {
        listenerList.add ( PopOverListener.class, listener );
    }

    /**
     * Removes pop-over listener.
     *
     * @param listener pop-over listener
     */
    public void removePopOverListener ( final PopOverListener listener )
    {
        listenerList.remove ( PopOverListener.class, listener );
    }

    /**
     * Informs that WebPopOver is being opened.
     */
    public void fireOpening ()
    {
        for ( final PopOverListener listener : listenerList.getListeners ( PopOverListener.class ) )
        {
            listener.opening ( this );
        }
    }

    /**
     * Informs that WebPopOver was opened.
     */
    public void fireOpened ()
    {
        for ( final PopOverListener listener : listenerList.getListeners ( PopOverListener.class ) )
        {
            listener.opened ( this );
        }
    }

    /**
     * Informs that WebPopOver.show was called while it was opened forcing it to update location.
     */
    public void fireReopened ()
    {
        for ( final PopOverListener listener : listenerList.getListeners ( PopOverListener.class ) )
        {
            listener.reopened ( this );
        }
    }

    /**
     * Informs that user dragged WebPopOver so that it became unattached from invoker component.
     */
    public void fireDetached ()
    {
        for ( final PopOverListener listener : listenerList.getListeners ( PopOverListener.class ) )
        {
            listener.detached ( this );
        }
    }

    /**
     * Informs that WebPopOver was closed due to losing focus or some other cause.
     */
    public void fireClosed ()
    {
        for ( final PopOverListener listener : listenerList.getListeners ( PopOverListener.class ) )
        {
            listener.closed ( this );
        }
    }

    @Override
    public PopOverAdapter onOpen ( final PopOverEventRunnable runnable )
    {
        return PopOverEventMethodsImpl.onOpen ( this, runnable );
    }

    @Override
    public PopOverAdapter onReopen ( final PopOverEventRunnable runnable )
    {
        return PopOverEventMethodsImpl.onReopen ( this, runnable );
    }

    @Override
    public PopOverAdapter onDetach ( final PopOverEventRunnable runnable )
    {
        return PopOverEventMethodsImpl.onDetach ( this, runnable );
    }

    @Override
    public PopOverAdapter onClose ( final PopOverEventRunnable runnable )
    {
        return PopOverEventMethodsImpl.onClose ( this, runnable );
    }
}