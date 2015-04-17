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

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.managers.style.StyleManager;
import com.alee.managers.style.skin.web.PopupStyle;
import com.alee.managers.style.skin.web.WebPopOverPainter;
import com.alee.managers.style.skin.web.WebPopupPainter;
import com.alee.utils.CollectionUtils;
import com.alee.utils.EventUtils;
import com.alee.utils.ProprietaryUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.Styleable;
import com.alee.utils.swing.AncestorAdapter;
import com.alee.utils.swing.DataProvider;
import com.alee.utils.swing.WindowFollowAdapter;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom stylish pop-over dialog with a special corner that follows invoker component.
 * It may also act as a simple dialog with custom styling if configured so.
 *
 * @author Mikle Garin
 * @see WebDialog
 * @see WebPopupPainter
 * @see PopOverSourcePoint
 * @see PopOverDirection
 * @see PopOverAlignment
 */

public class WebPopOver extends WebDialog implements Styleable, PopOverEventMethods
{
    /**
     * todo 1. Fix directional WebPopOver location when dragged to/from secondary screen
     * todo 2. Add "opened" listener event
     */

    /**
     * Whether WebPopOver should be movable or not.
     */
    protected boolean movable = WebPopOverStyle.movable;

    /**
     * WebPopOver display source point.
     */
    protected PopOverSourcePoint popOverSourcePoint = WebPopOverStyle.popOverSourcePoint;

    /**
     * WebPopOver components container.
     */
    protected WebPanel container;

    /**
     * Whether WebPopOver is attached to invoker component or not.
     */
    protected boolean attached = false;

    /**
     * Preferred direction in which WebPopOver should be displayed.
     */
    protected PopOverDirection preferredDirection = null;

    /**
     * Current display direction.
     */
    protected PopOverDirection currentDirection = null;

    /**
     * Preferred WebPopOver alignment relative to display source point.
     */
    protected PopOverAlignment preferredAlignment = null;

    /**
     * WebPopOver state listeners.
     */
    protected List<PopOverListener> popOverListeners = new ArrayList<PopOverListener> ( 1 );

    /**
     * Constructs new WebPopOver dialog.
     */
    public WebPopOver ()
    {
        super ();
    }

    /**
     * Constructs new WebPopOver dialog.
     *
     * @param owner dialog owner frame
     */
    public WebPopOver ( final Frame owner )
    {
        super ( owner );
    }

    /**
     * Constructs new WebPopOver dialog.
     *
     * @param owner dialog owner frame
     * @param title dialog title (might not be displayed depending on styling)
     */
    public WebPopOver ( final Frame owner, final String title )
    {
        super ( owner, title );
    }

    /**
     * Constructs new WebPopOver dialog.
     *
     * @param owner dialog owner dialog
     */
    public WebPopOver ( final Dialog owner )
    {
        super ( owner );
    }

    /**
     * Constructs new WebPopOver dialog.
     *
     * @param owner dialog owner dialog
     * @param title dialog title (might not be displayed depending on styling)
     */
    public WebPopOver ( final Dialog owner, final String title )
    {
        super ( owner, title );
    }

    /**
     * Constructs new WebPopOver dialog.
     *
     * @param owner dialog owner component
     */
    public WebPopOver ( final Component owner )
    {
        super ( owner );
    }

    /**
     * Constructs new WebPopOver dialog.
     *
     * @param owner dialog owner component
     * @param title dialog title (might not be displayed depending on styling)
     */
    public WebPopOver ( final Component owner, final String title )
    {
        super ( owner, title );
    }

    /**
     * Constructs new WebPopOver dialog.
     *
     * @param owner dialog owner window
     */
    public WebPopOver ( final Window owner )
    {
        super ( owner );
    }

    /**
     * Constructs new WebPopOver dialog.
     *
     * @param owner dialog owner window
     * @param title dialog title (might not be displayed depending on styling)
     */
    public WebPopOver ( final Window owner, final String title )
    {
        super ( owner, title );
    }

    /**
     * WebPopOver settings initialization.
     */
    @Override
    protected void initialize ()
    {
        super.initialize ();

        getRootPane ().setWindowDecorationStyle ( JRootPane.NONE );
        setUndecorated ( true );
        setWindowOpaque ( false );

        // todo Custom shade opacity when not focused
        //        painter = new WebPopupPainter<WebPanel> ()
        //        {
        //            @Override
        //            public float getShadeTransparency ()
        //            {
        //                final float actualShadeOpacity = super.getShadeTransparency ();
        //                return WebPopOver.this.isFocused () ? actualShadeOpacity : actualShadeOpacity * 0.7f;
        //            }
        //        };
        //        painter.setBorderColor ( WebPopOverStyle.borderColor );
        //        painter.setRound ( WebPopOverStyle.round );
        //        painter.setShadeWidth ( WebPopOverStyle.shadeWidth );
        //        painter.setShadeTransparency ( WebPopOverStyle.shadeTransparency );
        //        painter.setCornerWidth ( WebPopOverStyle.cornerWidth );
        //        painter.setTransparency ( WebPopOverStyle.transparency );
        //        painter.setPopupStyle ( PopupStyle.simple );

        container = new WebPanel ( /*painter*/ );
        container.setStyleId ( "pop-over" );
        setContentPane ( container );

        if ( !ProprietaryUtils.isWindowTransparencyAllowed () && ProprietaryUtils.isWindowShapeAllowed () )
        {
            final ComponentAdapter resizeAdapter = new ComponentAdapter ()
            {
                @Override
                public void componentResized ( ComponentEvent e )
                {
                    if ( getPainter () != null )
                    {
                        Rectangle bounds = getBounds (); ++bounds.width; ++bounds.height;
                        ProprietaryUtils.setWindowShape ( WebPopOver.this, getPainter ().provideShape ( container, bounds ) );
                    }
                }
            };
            addComponentListener ( resizeAdapter );
        }

        final ComponentMoveAdapter moveAdapter = new ComponentMoveAdapter ()
        {
            @Override
            protected Rectangle getDragStartBounds ( final MouseEvent e )
            {
                if ( movable )
                {
                    final int sw = getShadeWidth ();
                    return new Rectangle ( sw, sw, container.getWidth () - sw * 2, container.getHeight () - sw * 2 );
                }
                else
                {
                    return null;
                }
            }

            @Override
            public void mouseDragged ( final MouseEvent e )
            {
                // De-attach dialog
                if ( dragging && attached )
                {
                    attached = false;
                    preferredDirection = null;
                    setPopupStyle ( PopupStyle.simple );
                    fireDetached ();
                }

                super.mouseDragged ( e );
            }
        };
        container.addMouseListener ( moveAdapter );
        container.addMouseMotionListener ( moveAdapter );

        addWindowFocusListener ( new WindowFocusListener ()
        {
            @Override
            public void windowGainedFocus ( final WindowEvent e )
            {
                setPopOverFocused ( true );
            }

            @Override
            public void windowLostFocus ( final WindowEvent e )
            {
                setPopOverFocused ( false );
            }
        } );

        // Removing all listeners on window close event
        final PopOverCloseListener closeListener = new PopOverCloseListener ()
        {
            @Override
            public void popOverClosed ()
            {
                fireClosed ();
            }
        };
        addComponentListener ( closeListener );
        addWindowListener ( closeListener );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleId ()
    {
        return container.getStyleId ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyleId ( final String id )
    {
        container.setStyleId ( id );
    }

    /**
     * Sets WebPopOver content background color.
     *
     * @param color new content background color
     */
    public void setContentBackground ( final Color color )
    {
        container.setBackground ( color );
    }

    /**
     * Returns WebPopOver content background color.
     *
     * @return WebPopOver content background color
     */
    public Color getContentBackground ()
    {
        return container.getBackground ();
    }

    /**
     * Returns WebPopOver container margin.
     *
     * @return WebPopOver container margin
     */
    public Insets getMargin ()
    {
        return container.getMargin ();
    }

    /**
     * Sets WebPopOver container margin.
     *
     * @param margin margin insets
     * @return WebPopOver
     */
    public WebPopOver setMargin ( final Insets margin )
    {
        container.setMargin ( margin );
        return this;
    }

    /**
     * Sets WebPopOver container margin.
     *
     * @param top    top margin
     * @param left   left margin
     * @param bottom bottom margin
     * @param right  right margin
     * @return WebPopOver
     */
    public WebPopOver setMargin ( final int top, final int left, final int bottom, final int right )
    {
        container.setMargin ( top, left, bottom, right );
        return this;
    }

    /**
     * Sets WebPopOver container margin.
     *
     * @param margin sides margin
     * @return WebPopOver
     */
    public WebPopOver setMargin ( final int margin )
    {
        container.setMargin ( margin );
        return this;
    }

    /**
     * Returns base WebPopOver painter.
     *
     * @return base WebPopOver painter
     */
    public WebPopOverPainter getPainter ()
    {
        return ( WebPopOverPainter ) container.getPainter ();
    }

    /**
     * Sets base WebPopOver painter.
     *
     * @param painter base WebPopOver painter
     */
    public void setPainter ( final WebPopOverPainter painter )
    {
        container.setPainter ( painter );
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
     * Returns popup display source point.
     *
     * @return popup display source point
     */
    public PopOverSourcePoint getPopOverSourcePoint ()
    {
        return popOverSourcePoint;
    }

    /**
     * Sets popup display source point.
     *
     * @param popOverSourcePoint popup display source point
     */
    public void setPopOverSourcePoint ( final PopOverSourcePoint popOverSourcePoint )
    {
        this.popOverSourcePoint = popOverSourcePoint;
    }

    /**
     * Returns popup menu border color.
     *
     * @return popup menu border color
     */
    public Color getBorderColor ()
    {
        return getPainter ().getBorderColor ();
    }

    /**
     * Sets popup menu border color.
     *
     * @param color new popup menu border color
     */
    public void setBorderColor ( final Color color )
    {
        StyleManager.setCustomPainterProperty ( container, "borderColor", color );
    }

    /**
     * Returns popup menu border corners rounding.
     *
     * @return popup menu border corners rounding
     */
    @Override
    public int getRound ()
    {
        return getPainter ().getRound ();
    }

    /**
     * Sets popup menu border corners rounding.
     *
     * @param round new popup menu border corners rounding
     */
    @Override
    public void setRound ( final int round )
    {
        StyleManager.setCustomPainterProperty ( container, "round", round );
    }

    /**
     * Returns popup menu shade width.
     *
     * @return popup menu shade width
     */
    @Override
    public int getShadeWidth ()
    {
        return getPainter ().getShadeWidth ();
    }

    /**
     * Sets popup menu shade width.
     *
     * @param width new popup menu shade width
     */
    @Override
    public void setShadeWidth ( final int width )
    {
        StyleManager.setCustomPainterProperty ( container, "shadeWidth", width );
    }

    /**
     * Returns popup menu shade transparency.
     *
     * @return popup menu shade transparency
     */
    public float getShadeTransparency ()
    {
        return getPainter ().getShadeTransparency ();
    }

    /**
     * Sets popup menu shade transparency.
     *
     * @param transparency new popup menu shade transparency
     */
    public void setShadeTransparency ( final float transparency )
    {
        StyleManager.setCustomPainterProperty ( container, "shadeTransparency", transparency );
    }

    /**
     * Returns popup menu dropdown style corner width.
     *
     * @return popup menu dropdown style corner width
     */
    public int getCornerWidth ()
    {
        return getPainter ().getCornerWidth ();
    }

    /**
     * Sets popup menu dropdown style corner width.
     *
     * @param width popup menu dropdown style corner width
     */
    public void setCornerWidth ( final int width )
    {
        StyleManager.setCustomPainterProperty ( container, "cornerWidth", width );
    }

    /**
     * Returns popup menu background transparency.
     *
     * @return popup menu background transparency
     */
    public float getTransparency ()
    {
        return getPainter ().getTransparency ();
    }

    /**
     * Sets popup menu background transparency.
     *
     * @param transparency popup menu background transparency
     */
    public void setTransparency ( final float transparency )
    {
        StyleManager.setCustomPainterProperty ( container, "transparency", transparency );
    }

    /**
     * Returns popup style.
     * This method is made protected because requesting style is necessary only within the WebPopOver.
     *
     * @return popup style
     */
    protected PopupStyle getPopupStyle ()
    {
        return getPainter ().getPopupStyle ();
    }

    /**
     * Sets popup style.
     * This method is made protected because modifying style is necessary only within the WebPopOver.
     *
     * @param style new popup style
     */
    protected void setPopupStyle ( final PopupStyle style )
    {
        StyleManager.setCustomPainterProperty ( container, "popupStyle", style );
    }

    /**
     * Returns dropdown style corner side.
     *
     * @return dropdown style corner side
     */
    protected int getCornerSide ()
    {
        return getPainter ().getCornerSide ();
    }

    /**
     * Sets dropdown style corner side.
     *
     * @param cornerSide dropdown style corner side
     */
    protected void setCornerSide ( final int cornerSide )
    {
        StyleManager.setCustomPainterProperty ( container, "cornerSide", cornerSide );
    }

    /**
     * Returns relative dropdown corner position.
     *
     * @return relative dropdown corner position
     */
    protected int getRelativeCorner ()
    {
        return getPainter ().getRelativeCorner ();
    }

    /**
     * Sets relative dropdown corner position.
     *
     * @param relativeCorner relative dropdown corner position
     */
    protected void setRelativeCorner ( final int relativeCorner )
    {
        StyleManager.setCustomPainterProperty ( container, "relativeCorner", relativeCorner );
    }

    /**
     * Returns dropdown corner alignment.
     *
     * @return dropdown corner alignment
     */
    protected int getCornerAlignment ()
    {
        return getPainter ().getCornerAlignment ();
    }

    /**
     * Sets dropdown corner alignment.
     *
     * @param cornerAlignment dropdown corner alignment
     */
    protected void setCornerAlignment ( final int cornerAlignment )
    {
        StyleManager.setCustomPainterProperty ( container, "cornerAlignment", cornerAlignment );
    }

    /**
     * Returns whether this WebPopOver is focus owner or not.
     *
     * @return true if this WebPopOver is focus owner, false otherwise
     */
    public boolean isPopOverFocused ()
    {
        return getPainter ().isPopOverFocused ();
    }

    /**
     * Sets whether this WebPopOver is focus owner or not.
     *
     * @param focused whether this WebPopOver is focus owner or not
     */
    public void setPopOverFocused ( final boolean focused )
    {
        StyleManager.setCustomPainterProperty ( container, "popOverFocused", focused );
    }

    /**
     * Displays unattached WebPopOver at the specified screen location.
     *
     * @param location WebPopOver location on screen
     * @return displayed WebPopOver
     */
    public WebPopOver show ( final PopOverLocation location )
    {
        // Updating WebPopOver variables
        attached = false;
        preferredDirection = null;
        setPopupStyle ( PopupStyle.simple );

        // Updating dialog location on screen and size
        final Dimension ss = Toolkit.getDefaultToolkit ().getScreenSize ();
        pack ();
        switch ( location )
        {
            case center:
            {
                setLocation ( ss.width / 2 - getWidth () / 2, ss.height / 2 - getHeight () / 2 );
                break;
            }
            case topLeft:
            {
                setLocation ( 0, 0 );
                break;
            }
            case topRight:
            {
                setLocation ( ss.width - getWidth (), 0 );
                break;
            }
            case bottomLeft:
            {
                setLocation ( 0, ss.height - getHeight () );
                break;
            }
            case bottomRight:
            {
                setLocation ( ss.width - getWidth (), ss.height - getHeight () );
                break;
            }
            case topCenter:
            {
                setLocation ( ss.width / 2 - getWidth () / 2, 0 );
                break;
            }
            case bottomCenter:
            {
                setLocation ( ss.width / 2 - getWidth () / 2, ss.height - getHeight () );
                break;
            }
            case leftCenter:
            {
                setLocation ( 0, ss.height / 2 - getHeight () / 2 );
                break;
            }
            case rightCenter:
            {
                setLocation ( ss.width - getWidth (), ss.height / 2 - getHeight () / 2 );
                break;
            }
        }

        // Displaying dialog
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
        // Updating WebPopOver variables
        attached = false;
        preferredDirection = null;
        setPopupStyle ( PopupStyle.simple );

        // Updating dialog location on screen and size
        pack ();
        setLocation ( x - getShadeWidth (), y - getShadeWidth () );

        // Displaying dialog
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
        return show ( invoker, preferredDirection );
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
        return show ( invoker, x, y, preferredDirection );
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
        return show ( invoker, x, y, w, h, preferredDirection );
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
        return show ( invoker, new DataProvider<Rectangle> ()
        {
            @Override
            public Rectangle provide ()
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
     * @param boundsProvider source area provider
     * @return displayed WebPopOver
     */
    public WebPopOver show ( final Component invoker, final DataProvider<Rectangle> boundsProvider )
    {
        return show ( invoker, boundsProvider, preferredDirection );
    }

    /**
     * Displays WebPopOver attached to the invoker component area and faced to specified direction.
     * It will also be aligned using the specified alignment type when possible.
     * WebPopOver opened in this way will always auto-follow invoker's ancestor window.
     *
     * @param invoker        invoker component
     * @param boundsProvider source area provider
     * @param direction      preferred display direction
     * @return displayed WebPopOver
     */
    public WebPopOver show ( final Component invoker, final DataProvider<Rectangle> boundsProvider, final PopOverDirection direction )
    {
        return show ( invoker, boundsProvider, direction, PopOverAlignment.centered );
    }

    /**
     * Displays WebPopOver attached to the invoker component area and faced to specified direction.
     * It will also be aligned using the specified alignment type when possible.
     * WebPopOver opened in this way will always auto-follow invoker's ancestor window.
     *
     * @param invoker        invoker component
     * @param boundsProvider source area provider
     * @param direction      preferred display direction
     * @param alignment      preferred display alignment
     * @return displayed WebPopOver
     */
    public WebPopOver show ( final Component invoker, final DataProvider<Rectangle> boundsProvider, final PopOverDirection direction,
                             final PopOverAlignment alignment )
    {
        // Fire reopened event
        // This event is fired before any show operation repeated
        if ( isShowing () )
        {
            fireReopened ();
        }

        // Translating coordinates into screen coordinates system
        final DataProvider<Rectangle> actualBoundsProvider = boundsProvider == null ? null : new DataProvider<Rectangle> ()
        {
            private Rectangle lastBounds = new Rectangle ();

            @Override
            public Rectangle provide ()
            {
                // Invoker might be hidden while WebPopOver is still visible
                // This is why we should simply stop updating its position when that happens
                // It is not the best workaround but at least it will keep us safe from exceptions
                if ( invoker.isShowing () )
                {
                    final Rectangle bounds = boundsProvider.provide ();
                    final Point los = invoker.getLocationOnScreen ();
                    lastBounds = new Rectangle ( los.x + bounds.x, los.y + bounds.y, bounds.width, bounds.height );
                }
                return lastBounds;
            }
        };

        // Updating WebPopOver variables
        attached = true;
        preferredDirection = direction != null ? direction : PopOverDirection.down;
        preferredAlignment = alignment;

        // Updating dialog location on screen and size
        pack ();
        updatePopOverLocation ( invoker, actualBoundsProvider );
        installPopOverLocationUpdater ( invoker, actualBoundsProvider );

        // Displaying dialog
        setVisible ( true );

        // Fire opened event
        // Note that if this pop-over is modal this event will be fired only after it is closed
        // Unfortunately there is no good way to provide this event after dialog is opened but before it is closed in that case
        fireOpened ();

        return this;
    }

    /**
     * Updates WebPopOver location on screen.
     *
     * @param invoker invoker component
     */
    protected void updatePopOverLocation ( final Component invoker, final DataProvider<Rectangle> invokerBoundsProvider )
    {
        if ( invokerBoundsProvider != null )
        {
            updatePopOverLocation ( invokerBoundsProvider.provide () );
        }
        else
        {
            updatePopOverLocation ( invoker );
        }
    }

    /**
     * Updates WebPopOver location on screen.
     *
     * @param invoker invoker component
     */
    protected void updatePopOverLocation ( final Component invoker )
    {
        if ( invoker instanceof Window )
        {
            // Applying proper painter style
            setPopupStyle ( PopupStyle.simple );

            // Determining final WebPopOver position
            final Rectangle ib = invoker.getBounds ();
            final Dimension size = getSize ();

            // Updating current direction
            currentDirection = null;

            // Updating WebPopOver location
            setLocation ( ib.x + ib.width / 2 - size.width / 2, ib.y + ib.height / 2 - size.height / 2 );
        }
        else if ( invoker.isShowing () )
        {
            // Updating WebPopOver location in a smarter way
            updatePopOverLocation ( SwingUtils.getBoundsOnScreen ( invoker ) );
        }
    }

    /**
     * Updates WebPopOver location on screen.
     *
     * @param invokerBounds invoker component bounds on screen
     */
    protected void updatePopOverLocation ( final Rectangle invokerBounds )
    {
        // Applying proper painter style
        setPopupStyle ( PopupStyle.dropdown );

        // WebPopOver preferred size without shade
        final Dimension size = getSize ();
        final int sw = getShadeWidth ();
        final int round = getRound ();
        final int cw = getCornerWidth ();
        final Dimension ps = new Dimension ( size.width - sw * 2, size.height - sw * 2 );
        final Rectangle screenBounds = getGraphicsConfiguration ().getBounds ();
        final boolean ltr = getComponentOrientation ().isLeftToRight ();

        // Determining actual direction
        final PopOverDirection actualDirection = getActualDirection ( invokerBounds, ltr, cw, ps, screenBounds );
        setCornerSide ( actualDirection.getCornerSide ( ltr ) );
        currentDirection = actualDirection;

        // Determining position according to alignment
        final Point actualLocation = getActualLocation ( invokerBounds, ltr, round, cw, ps, screenBounds, actualDirection );
        actualLocation.x -= sw;
        actualLocation.y -= sw;

        // Updating corner position
        setCornerAlignment ( -1 );
        setRelativeCorner ( getRelativeCorner ( invokerBounds, actualDirection, actualLocation ) );

        // Updating WebPopOver location
        setLocation ( actualLocation );
    }

    /**
     * Installs listeners to update WebPopOver location.
     *
     * @param invoker invoker component
     */
    protected void installPopOverLocationUpdater ( final Component invoker, final DataProvider<Rectangle> invokerBoundsProvider )
    {
        // Invoker component window
        final Window invokerWindow = SwingUtils.getWindowAncestor ( invoker );

        // Invoker window follow adapter
        final WindowFollowAdapter windowFollowAdapter = new WindowFollowAdapter ( WebPopOver.this, invokerWindow )
        {
            @Override
            public boolean isEnabled ()
            {
                return !attached;
            }
        };
        invokerWindow.addComponentListener ( windowFollowAdapter );

        // Invoker window adapter
        final ComponentAdapter invokerWindowAdapter = new ComponentAdapter ()
        {
            @Override
            public void componentMoved ( final ComponentEvent e )
            {
                if ( attached )
                {
                    updatePopOverLocation ( invoker, invokerBoundsProvider );
                    windowFollowAdapter.updateLastLocation ();
                }
            }
        };
        invokerWindow.addComponentListener ( invokerWindowAdapter );

        // Invoker component adapter
        final ComponentAdapter invokerAdapter = new ComponentAdapter ()
        {
            @Override
            public void componentMoved ( final ComponentEvent e )
            {
                if ( attached )
                {
                    updatePopOverLocation ( invoker, invokerBoundsProvider );
                    windowFollowAdapter.updateLastLocation ();
                }
            }

            @Override
            public void componentResized ( final ComponentEvent e )
            {
                if ( attached )
                {
                    updatePopOverLocation ( invoker, invokerBoundsProvider );
                    windowFollowAdapter.updateLastLocation ();
                }
            }
        };
        invoker.addComponentListener ( invokerAdapter );

        final AncestorAdapter ancestorAdapter;
        if ( invoker instanceof JComponent )
        {
            ancestorAdapter = new AncestorAdapter ()
            {
                @Override
                public void ancestorMoved ( final AncestorEvent event )
                {
                    if ( attached )
                    {
                        updatePopOverLocation ( invoker, invokerBoundsProvider );
                        windowFollowAdapter.updateLastLocation ();
                    }
                }

                // todo Probably hide WebPopOver in some cases here?
                //                @Override
                //                public void ancestorRemoved ( final AncestorEvent event )
                //                {
                //                    super.ancestorRemoved ( event );
                //                }
            };
            ( ( JComponent ) invoker ).addAncestorListener ( ancestorAdapter );
        }
        else
        {
            ancestorAdapter = null;
        }

        // WebPopOver orientation change listener
        final PropertyChangeListener orientationListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updatePopOverLocation ( invoker, invokerBoundsProvider );
                windowFollowAdapter.updateLastLocation ();
            }
        };
        addPropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, orientationListener );

        // Removing all listeners on window close event
        addPopOverListener ( new PopOverAdapter ()
        {
            @Override
            public void reopened ( final WebPopOver popOver )
            {
                destroy ();
            }

            @Override
            public void closedWebPopOver ( final WebPopOver popOver )
            {
                destroy ();
            }

            protected void destroy ()
            {
                removePopOverListener ( this );
                invokerWindow.removeComponentListener ( invokerWindowAdapter );
                invokerWindow.removeComponentListener ( windowFollowAdapter );
                invoker.removeComponentListener ( invokerAdapter );
                if ( invoker instanceof JComponent )
                {
                    ( ( JComponent ) invoker ).removeAncestorListener ( ancestorAdapter );
                }
                removePropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, orientationListener );
            }
        } );
    }

    /**
     * Returns relative corner position.
     *
     * @param ib              invoker component bounds on screen
     * @param actualDirection actual WebPopOver direction
     * @param actualLocation  actual WebPopOver location
     * @return relative corner position
     */
    protected int getRelativeCorner ( final Rectangle ib, final PopOverDirection actualDirection, final Point actualLocation )
    {
        switch ( actualDirection )
        {
            case up:
            case down:
                return ib.x + ib.width / 2 - actualLocation.x;
            case left:
            case right:
                return ib.y + ib.height / 2 - actualLocation.y;
        }
        return -1;
    }

    /**
     * Returns actual WebPopOver location.
     * Shade width is not yet taken into account within this location.
     *
     * @param ib              invoker component bounds on screen
     * @param ltr             whether LTR orientation is active or not
     * @param round           corners round
     * @param cw              corner width
     * @param ps              WebPopOver size without shade widths
     * @param screenBounds    screen bounds
     * @param actualDirection actual WebPopOver direction
     * @return actual WebPopOver location
     */
    protected Point getActualLocation ( final Rectangle ib, final boolean ltr, final int round, final int cw, final Dimension ps,
                                        final Rectangle screenBounds, final PopOverDirection actualDirection )
    {
        final Point sp = getActualSourcePoint ( ib, ltr, actualDirection );
        if ( actualDirection == PopOverDirection.up )
        {
            if ( preferredAlignment == PopOverAlignment.centered )
            {
                final Point location = new Point ( sp.x - ps.width / 2, sp.y - cw - ps.height );
                return checkRightCollision ( checkLeftCollision ( location, screenBounds ), ps, screenBounds );
            }
            else if ( preferredAlignment == ( ltr ? PopOverAlignment.leading : PopOverAlignment.trailing ) )
            {
                return checkLeftCollision ( new Point ( sp.x + cw * 2 + round - ps.width, sp.y - cw - ps.height ), screenBounds );
            }
            else if ( preferredAlignment == ( ltr ? PopOverAlignment.trailing : PopOverAlignment.leading ) )
            {
                return checkRightCollision ( new Point ( sp.x - cw * 2 - round, sp.y - cw - ps.height ), ps, screenBounds );
            }
        }
        else if ( actualDirection == PopOverDirection.down )
        {
            if ( preferredAlignment == PopOverAlignment.centered )
            {
                final Point location = new Point ( sp.x - ps.width / 2, sp.y + cw );
                return checkRightCollision ( checkLeftCollision ( location, screenBounds ), ps, screenBounds );
            }
            else if ( preferredAlignment == ( ltr ? PopOverAlignment.leading : PopOverAlignment.trailing ) )
            {
                return checkLeftCollision ( new Point ( sp.x + cw * 2 + round - ps.width, sp.y + cw ), screenBounds );
            }
            else if ( preferredAlignment == ( ltr ? PopOverAlignment.trailing : PopOverAlignment.leading ) )
            {
                return checkRightCollision ( new Point ( sp.x - cw * 2 - round, sp.y + cw ), ps, screenBounds );
            }
        }
        else if ( actualDirection == ( ltr ? PopOverDirection.left : PopOverDirection.right ) )
        {
            if ( preferredAlignment == PopOverAlignment.centered )
            {
                final Point location = new Point ( sp.x - cw - ps.width, sp.y - ps.height / 2 );
                return checkBottomCollision ( checkTopCollision ( location, screenBounds ), ps, screenBounds );
            }
            else if ( preferredAlignment == PopOverAlignment.leading )
            {
                return checkTopCollision ( new Point ( sp.x - cw - ps.width, sp.y + cw * 2 + round - ps.height ), screenBounds );
            }
            else if ( preferredAlignment == PopOverAlignment.trailing )
            {
                return checkBottomCollision ( new Point ( sp.x - cw - ps.width, sp.y - cw * 2 - round ), ps, screenBounds );
            }
        }
        else if ( actualDirection == ( ltr ? PopOverDirection.right : PopOverDirection.left ) )
        {
            if ( preferredAlignment == PopOverAlignment.centered )
            {
                final Point location = new Point ( sp.x + cw, sp.y - ps.height / 2 );
                return checkBottomCollision ( checkTopCollision ( location, screenBounds ), ps, screenBounds );
            }
            else if ( preferredAlignment == PopOverAlignment.leading )
            {
                return checkTopCollision ( new Point ( sp.x + cw, sp.y + cw * 2 + round - ps.height ), screenBounds );
            }
            else if ( preferredAlignment == PopOverAlignment.trailing )
            {
                return checkBottomCollision ( new Point ( sp.x + cw, sp.y - cw * 2 - round ), ps, screenBounds );
            }
        }
        return null;
    }

    /**
     * Checks whether WebPopOver will collide with top screen border and modifies location accordingly.
     *
     * @param location     approximate WebPopOver location
     * @param screenBounds screen bounds
     * @return either modified or unmodified WebPopOver location
     */
    protected Point checkTopCollision ( final Point location, final Rectangle screenBounds )
    {
        if ( location.y < screenBounds.y )
        {
            location.y = screenBounds.y;
        }
        return location;
    }

    /**
     * Checks whether WebPopOver will collide with bottom screen border and modifies location accordingly.
     *
     * @param location     approximate WebPopOver location
     * @param ps           WebPopOver size without shade widths
     * @param screenBounds screen bounds
     * @return either modified or unmodified WebPopOver location
     */
    protected Point checkBottomCollision ( final Point location, final Dimension ps, final Rectangle screenBounds )
    {
        if ( location.y + ps.height > screenBounds.y + screenBounds.height )
        {
            location.y = screenBounds.y + screenBounds.height - ps.height;
        }
        return location;
    }

    /**
     * Checks whether WebPopOver will collide with left screen border and modifies location accordingly.
     *
     * @param location     approximate WebPopOver location
     * @param screenBounds screen bounds
     * @return either modified or unmodified WebPopOver location
     */
    protected Point checkLeftCollision ( final Point location, final Rectangle screenBounds )
    {
        if ( location.x < screenBounds.x )
        {
            location.x = screenBounds.x;
        }
        return location;
    }

    /**
     * Checks whether WebPopOver will collide with right screen border and modifies location accordingly.
     *
     * @param location     approximate WebPopOver location
     * @param ps           WebPopOver size without shade widths
     * @param screenBounds screen bounds
     * @return either modified or unmodified WebPopOver location
     */
    protected Point checkRightCollision ( final Point location, final Dimension ps, final Rectangle screenBounds )
    {
        if ( location.x + ps.width > screenBounds.x + screenBounds.width )
        {
            location.x = screenBounds.x + screenBounds.width - ps.width;
        }
        return location;
    }

    /**
     * Returns actual direction depending on preferred WebPopOver direction, its sizes and source point.
     *
     * @param ib           invoker component bounds on screen
     * @param ltr          whether LTR orientation is active or not
     * @param cw           corner with
     * @param ps           WebPopOver size without shade widths
     * @param screenBounds screen bounds
     * @return actual WebPopOver direction
     */
    protected PopOverDirection getActualDirection ( final Rectangle ib, final boolean ltr, final int cw, final Dimension ps,
                                                    final Rectangle screenBounds )
    {
        for ( final PopOverDirection checkedDirection : preferredDirection.getPriority () )
        {
            final Point sp = getActualSourcePoint ( ib, ltr, checkedDirection );
            if ( checkedDirection == PopOverDirection.up )
            {
                if ( sp.y - cw - ps.height > screenBounds.y )
                {
                    return checkedDirection;
                }
            }
            else if ( checkedDirection == PopOverDirection.down )
            {
                if ( sp.y + cw + ps.height < screenBounds.y + screenBounds.height )
                {
                    return checkedDirection;
                }
            }
            else if ( checkedDirection == ( ltr ? PopOverDirection.left : PopOverDirection.right ) )
            {
                if ( sp.x - cw - ps.width > screenBounds.x )
                {
                    return checkedDirection;
                }
            }
            else if ( checkedDirection == ( ltr ? PopOverDirection.right : PopOverDirection.left ) )
            {
                if ( sp.x + cw + ps.width < screenBounds.x + screenBounds.width )
                {
                    return checkedDirection;
                }
            }
        }
        return preferredDirection;
    }

    /**
     * Returns actual source point depending on WebPopOver direction and invoker component location on screen.
     *
     * @param ib        invoker component bounds on screen
     * @param ltr       whether LTR orientation is active or not
     * @param direction WebPopOver direction  @return actual source point
     */
    protected Point getActualSourcePoint ( final Rectangle ib, final boolean ltr, final PopOverDirection direction )
    {
        if ( popOverSourcePoint == PopOverSourcePoint.componentCenter )
        {
            return new Point ( ib.x + ib.width / 2, ib.y + ib.height / 2 );
        }
        else
        {
            if ( direction == PopOverDirection.up )
            {
                return new Point ( ib.x + ib.width / 2, ib.y );
            }
            else if ( direction == PopOverDirection.down )
            {
                return new Point ( ib.x + ib.width / 2, ib.y + ib.height );
            }
            else if ( direction == ( ltr ? PopOverDirection.left : PopOverDirection.right ) )
            {
                return new Point ( ib.x, ib.y + ib.height / 2 );
            }
            else if ( direction == ( ltr ? PopOverDirection.right : PopOverDirection.left ) )
            {
                return new Point ( ib.x + ib.width, ib.y + ib.height / 2 );
            }
        }
        return null;
    }

    /**
     * Returns preferred direction in which WebPopOver should be displayed.
     *
     * @return preferred direction in which WebPopOver should be displayed
     */
    public PopOverDirection getPreferredDirection ()
    {
        return preferredDirection;
    }

    /**
     * Sets preferred direction in which WebPopOver should be displayed.
     *
     * @param direction preferred direction in which WebPopOver should be displayed
     */
    public void setPreferredDirection ( final PopOverDirection direction )
    {
        this.preferredDirection = direction;
    }

    /**
     * Returns current display direction.
     *
     * @return current display direction
     */
    public PopOverDirection getCurrentDirection ()
    {
        return currentDirection;
    }

    /**
     * Sets current display direction.
     *
     * @param direction current display direction
     */
    public void setCurrentDirection ( final PopOverDirection direction )
    {
        this.currentDirection = direction;
    }

    /**
     * Adds pop-over listener.
     *
     * @param listener pop-over listener
     */
    public void addPopOverListener ( final PopOverListener listener )
    {
        popOverListeners.add ( listener );
    }

    /**
     * Removes pop-over listener.
     *
     * @param listener pop-over listener
     */
    public void removePopOverListener ( final PopOverListener listener )
    {
        popOverListeners.remove ( listener );
    }

    /**
     * Informs that WebPopOver was opened.
     */
    public void fireOpened ()
    {
        for ( final PopOverListener listener : CollectionUtils.copy ( popOverListeners ) )
        {
            listener.opened ( this );
        }
    }

    /**
     * Informs that WebPopOver.show was called while it was opened forcing it to update location.
     */
    public void fireReopened ()
    {
        for ( final PopOverListener listener : CollectionUtils.copy ( popOverListeners ) )
        {
            listener.reopened ( this );
        }
    }

    /**
     * Informs that user dragged WebPopOver so that it became unattached from invoker component.
     */
    public void fireDetached ()
    {
        for ( final PopOverListener listener : CollectionUtils.copy ( popOverListeners ) )
        {
            listener.detached ( this );
        }
    }

    /**
     * Informs that WebPopOver was closed due to losing focus or some other cause.
     */
    public void fireClosed ()
    {
        for ( final PopOverListener listener : CollectionUtils.copy ( popOverListeners ) )
        {
            listener.closedWebPopOver ( this );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PopOverAdapter onOpen ( final PopOverEventRunnable runnable )
    {
        return EventUtils.onOpen ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PopOverAdapter onReopen ( final PopOverEventRunnable runnable )
    {
        return EventUtils.onReopen ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PopOverAdapter onDetach ( final PopOverEventRunnable runnable )
    {
        return EventUtils.onDetach ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PopOverAdapter onClose ( final PopOverEventRunnable runnable )
    {
        return EventUtils.onClose ( this, runnable );
    }
}