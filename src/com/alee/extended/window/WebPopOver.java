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
import com.alee.laf.menu.PopupPainterStyle;
import com.alee.laf.menu.WebPopupPainter;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.WindowFollowAdapter;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Custom stylish pop-over dialog with styling corner auto-aligned to invoker component.
 * It may also act as a simple dialog with custom styling if configured so.
 *
 * @author Mikle Garin
 * @see WebDialog
 * @see WebPopupPainter
 * @see PopOverSourcePoint
 * @see PopOverDirection
 * @see PopOverAlignment
 */

public class WebPopOver extends WebDialog
{
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
     * WebPopOver style painter.
     */
    protected WebPopupPainter<WebPanel> painter;

    /**
     * Whether WebPopOver is attached to invoker component or not.
     */
    protected boolean attached = false;

    /**
     * Preferred direction in which WebPopOver should be displayed.
     */
    protected PopOverDirection preferredDirection = null;

    /**
     * Preferred WebPopOver alignment relative to display source point.
     */
    protected PopOverAlignment preferredAlignment = null;

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

        setUndecorated ( true );
        setWindowOpaque ( false );

        painter = new WebPopupPainter<WebPanel> ()
        {
            @Override
            public float getShadeOpacity ()
            {
                final float actualShadeOpacity = super.getShadeOpacity ();
                return WebPopOver.this.isFocused () ? actualShadeOpacity : actualShadeOpacity * 0.7f;
            }
        };
        painter.setBorderColor ( WebPopOverStyle.borderColor );
        painter.setRound ( WebPopOverStyle.round );
        painter.setShadeWidth ( WebPopOverStyle.shadeWidth );
        painter.setShadeOpacity ( WebPopOverStyle.shadeOpacity );
        painter.setCornerWidth ( WebPopOverStyle.cornerWidth );
        painter.setTransparency ( WebPopOverStyle.transparency );
        painter.setPopupPainterStyle ( PopupPainterStyle.simple );

        container = new WebPanel ( painter );
        container.setBackground ( Color.WHITE );
        setContentPane ( container );

        final ComponentMoveAdapter moveAdapter = new ComponentMoveAdapter ()
        {
            @Override
            protected Rectangle getDragStartBounds ( final MouseEvent e )
            {
                if ( movable )
                {
                    final int sw = painter.getShadeWidth ();
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
                    painter.setPopupPainterStyle ( PopupPainterStyle.simple );
                    painter.fireUpdate ();
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
                container.repaint ();
            }

            @Override
            public void windowLostFocus ( final WindowEvent e )
            {
                container.repaint ();
            }
        } );
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
    public WebPopupPainter<WebPanel> getPainter ()
    {
        return painter;
    }

    /**
     * Sets base WebPopOver painter.
     *
     * @param painter base WebPopOver painter
     */
    public void setPainter ( final WebPopupPainter<WebPanel> painter )
    {
        this.painter = painter;
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
        return painter.getBorderColor ();
    }

    /**
     * Sets popup menu border color.
     *
     * @param color new popup menu border color
     */
    public void setBorderColor ( final Color color )
    {
        painter.setBorderColor ( color );
    }

    /**
     * Returns popup menu border corners rounding.
     *
     * @return popup menu border corners rounding
     */
    @Override
    public int getRound ()
    {
        return painter.getRound ();
    }

    /**
     * Sets popup menu border corners rounding.
     *
     * @param round new popup menu border corners rounding
     */
    @Override
    public void setRound ( final int round )
    {
        painter.setRound ( round );
    }

    /**
     * Returns popup menu shade width.
     *
     * @return popup menu shade width
     */
    @Override
    public int getShadeWidth ()
    {
        return painter.getShadeWidth ();
    }

    /**
     * Sets popup menu shade width.
     *
     * @param width new popup menu shade width
     */
    @Override
    public void setShadeWidth ( final int width )
    {
        painter.setShadeWidth ( width );
    }

    /**
     * Returns popup menu shade opacity.
     *
     * @return popup menu shade opacity
     */
    public float getShadeOpacity ()
    {
        return painter.getShadeOpacity ();
    }

    /**
     * Sets popup menu shade opacity.
     *
     * @param opacity new popup menu shade opacity
     */
    public void setShadeOpacity ( final float opacity )
    {
        painter.setShadeOpacity ( opacity );
    }

    /**
     * Returns popup menu dropdown style corner width.
     *
     * @return popup menu dropdown style corner width
     */
    public int getCornerWidth ()
    {
        return painter.getCornerWidth ();
    }

    /**
     * Sets popup menu dropdown style corner width.
     *
     * @param width popup menu dropdown style corner width
     */
    public void setCornerWidth ( final int width )
    {
        painter.setCornerWidth ( width );
    }

    /**
     * Returns popup menu background transparency.
     *
     * @return popup menu background transparency
     */
    public float getTransparency ()
    {
        return painter.getTransparency ();
    }

    /**
     * Sets popup menu background transparency.
     *
     * @param transparency popup menu background transparency
     */
    public void setTransparency ( final float transparency )
    {
        painter.setTransparency ( transparency );
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
        painter.setPopupPainterStyle ( PopupPainterStyle.simple );

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
        painter.setPopupPainterStyle ( PopupPainterStyle.simple );

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
        // Updating WebPopOver variables
        attached = true;
        preferredDirection = direction;
        preferredAlignment = alignment;

        // Updating dialog location on screen and size
        pack ();
        updatePopOverLocation ( invoker );
        installPopOverLocationUpdater ( invoker );

        // Displaying dialog
        setVisible ( true );
        return this;
    }

    /**
     * Updates WebPopOver location on screen.
     *
     * @param invoker invoker component
     */
    protected void updatePopOverLocation ( final Component invoker )
    {
        final Point actualLocation;
        if ( invoker instanceof Window )
        {
            // Applying proper painter style
            painter.setPopupPainterStyle ( PopupPainterStyle.simple );

            // Determining final WebPopOver position
            final Rectangle ib = invoker.getBounds ();
            final Dimension size = getSize ();
            actualLocation = new Point ( ib.x + ib.width / 2 - size.width / 2, ib.y + ib.height / 2 - size.height / 2 );
        }
        else
        {
            // Applying proper painter style
            painter.setPopupPainterStyle ( PopupPainterStyle.dropdown );

            // WebPopOver preferred size without shade
            final Dimension size = getSize ();
            final int sw = getShadeWidth ();
            final int round = getRound ();
            final int cw = getCornerWidth ();
            final Dimension ps = new Dimension ( size.width - sw * 2, size.height - sw * 2 );
            final Dimension screenSize = Toolkit.getDefaultToolkit ().getScreenSize ();
            final boolean ltr = getComponentOrientation ().isLeftToRight ();

            // Determining actual direction
            final PopOverDirection actualDirection = getActualDirection ( invoker, ltr, cw, ps, screenSize );
            painter.setCornerSide ( actualDirection.getCornerSide ( ltr ) );

            // Determining position according to alignment
            actualLocation = getActualLocation ( invoker, ltr, round, cw, ps, screenSize, actualDirection );
            actualLocation.x -= sw;
            actualLocation.y -= sw;

            // Updating corner position
            painter.setCornerAlignment ( -1 );
            painter.setRelativeCorner ( getRelativeCorner ( invoker, actualDirection, actualLocation ) );
        }

        // Updating WebPopOver location
        setLocation ( actualLocation );
    }

    /**
     * Installs listeners to update WebPopOver location.
     *
     * @param invoker invoker component
     */
    protected void installPopOverLocationUpdater ( final Component invoker )
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
                    updatePopOverLocation ( invoker );
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
                    updatePopOverLocation ( invoker );
                    windowFollowAdapter.updateLastLocation ();
                }
            }

            @Override
            public void componentResized ( final ComponentEvent e )
            {
                if ( attached )
                {
                    updatePopOverLocation ( invoker );
                    windowFollowAdapter.updateLastLocation ();
                }
            }
        };
        invoker.addComponentListener ( invokerAdapter );

        // WebPopOver orientation change listener
        final PropertyChangeListener orientationListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updatePopOverLocation ( invoker );
                windowFollowAdapter.updateLastLocation ();
            }
        };
        addPropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, orientationListener );

        addWindowListener ( new WindowAdapter ()
        {
            @Override
            public void windowClosed ( final WindowEvent e )
            {
                removeWindowListener ( this );
                invokerWindow.removeComponentListener ( invokerWindowAdapter );
                invokerWindow.removeComponentListener ( windowFollowAdapter );
                invoker.removeComponentListener ( invokerAdapter );
                removePropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, orientationListener );
            }
        } );
    }

    /**
     * Returns relative corner position.
     *
     * @param invoker         invoker component
     * @param actualDirection actual WebPopOver direction
     * @param actualLocation  actual WebPopOver location
     * @return relative corner position
     */
    protected int getRelativeCorner ( final Component invoker, final PopOverDirection actualDirection, final Point actualLocation )
    {
        final Point los = invoker.getLocationOnScreen ();
        final Dimension size = invoker.getSize ();
        switch ( actualDirection )
        {
            case up:
            case down:
                return los.x + size.width / 2 - actualLocation.x;
            case left:
            case right:
                return los.y + size.height / 2 - actualLocation.y;
        }
        return -1;
    }

    /**
     * Returns actual WebPopOver location.
     * Shade width is not yet taken into account within this location.
     *
     * @param invoker         invoker component
     * @param ltr             whether LTR orientation is active or not
     * @param round           corners round
     * @param cw              corner width
     * @param ps              WebPopOver size without shade widths
     * @param screenSize      screen size
     * @param actualDirection actual WebPopOver direction     @return actual WebPopOver location
     */
    protected Point getActualLocation ( final Component invoker, final boolean ltr, final int round, final int cw, final Dimension ps,
                                        final Dimension screenSize, final PopOverDirection actualDirection )
    {
        final Point sp = getActualSourcePoint ( invoker, ltr, actualDirection );
        if ( actualDirection == PopOverDirection.up )
        {
            if ( preferredAlignment == PopOverAlignment.centered )
            {
                final Point location = new Point ( sp.x - ps.width / 2, sp.y - cw - ps.height );
                return checkRightCollision ( checkLeftCollision ( location ), ps, screenSize );
            }
            else if ( preferredAlignment == ( ltr ? PopOverAlignment.leading : PopOverAlignment.trailing ) )
            {
                return checkLeftCollision ( new Point ( sp.x + cw * 2 + round - ps.width, sp.y - cw - ps.height ) );
            }
            else if ( preferredAlignment == ( ltr ? PopOverAlignment.trailing : PopOverAlignment.leading ) )
            {
                return checkRightCollision ( new Point ( sp.x - cw * 2 - round, sp.y - cw - ps.height ), ps, screenSize );
            }
        }
        else if ( actualDirection == PopOverDirection.down )
        {
            if ( preferredAlignment == PopOverAlignment.centered )
            {
                final Point location = new Point ( sp.x - ps.width / 2, sp.y + cw );
                return checkRightCollision ( checkLeftCollision ( location ), ps, screenSize );
            }
            else if ( preferredAlignment == ( ltr ? PopOverAlignment.leading : PopOverAlignment.trailing ) )
            {
                return checkLeftCollision ( new Point ( sp.x + cw * 2 + round - ps.width, sp.y + cw ) );
            }
            else if ( preferredAlignment == ( ltr ? PopOverAlignment.trailing : PopOverAlignment.leading ) )
            {
                return checkRightCollision ( new Point ( sp.x - cw * 2 - round, sp.y + cw ), ps, screenSize );
            }
        }
        else if ( actualDirection == ( ltr ? PopOverDirection.left : PopOverDirection.right ) )
        {
            if ( preferredAlignment == PopOverAlignment.centered )
            {
                final Point location = new Point ( sp.x - cw - ps.width, sp.y - ps.height / 2 );
                return checkBottomCollision ( checkTopCollision ( location ), ps, screenSize );
            }
            else if ( preferredAlignment == PopOverAlignment.leading )
            {
                return checkTopCollision ( new Point ( sp.x - cw - ps.width, sp.y + cw * 2 + round - ps.height ) );
            }
            else if ( preferredAlignment == PopOverAlignment.trailing )
            {
                return checkBottomCollision ( new Point ( sp.x - cw - ps.width, sp.y - cw * 2 - round ), ps, screenSize );
            }
        }
        else if ( actualDirection == ( ltr ? PopOverDirection.right : PopOverDirection.left ) )
        {
            if ( preferredAlignment == PopOverAlignment.centered )
            {
                final Point location = new Point ( sp.x + cw, sp.y - ps.height / 2 );
                return checkBottomCollision ( checkTopCollision ( location ), ps, screenSize );
            }
            else if ( preferredAlignment == PopOverAlignment.leading )
            {
                return checkTopCollision ( new Point ( sp.x + cw, sp.y + cw * 2 + round - ps.height ) );
            }
            else if ( preferredAlignment == PopOverAlignment.trailing )
            {
                return checkBottomCollision ( new Point ( sp.x + cw, sp.y - cw * 2 - round ), ps, screenSize );
            }
        }
        return null;
    }

    /**
     * Checks whether WebPopOver will collide with top screen border and modifies location accordingly.
     *
     * @param location approximate WebPopOver location
     * @return either modified or unmodified WebPopOver location
     */
    protected Point checkTopCollision ( final Point location )
    {
        if ( location.y < 0 )
        {
            location.y = 0;
        }
        return location;
    }

    /**
     * Checks whether WebPopOver will collide with bottom screen border and modifies location accordingly.
     *
     * @param location   approximate WebPopOver location
     * @param ps         WebPopOver size without shade widths
     * @param screenSize screen size
     * @return either modified or unmodified WebPopOver location
     */
    protected Point checkBottomCollision ( final Point location, final Dimension ps, final Dimension screenSize )
    {
        if ( location.y + ps.height > screenSize.height )
        {
            location.y = screenSize.height - ps.height;
        }
        return location;
    }

    /**
     * Checks whether WebPopOver will collide with left screen border and modifies location accordingly.
     *
     * @param location approximate WebPopOver location
     * @return either modified or unmodified WebPopOver location
     */
    protected Point checkLeftCollision ( final Point location )
    {
        if ( location.x < 0 )
        {
            location.x = 0;
        }
        return location;
    }

    /**
     * Checks whether WebPopOver will collide with right screen border and modifies location accordingly.
     *
     * @param location   approximate WebPopOver location
     * @param ps         WebPopOver size without shade widths
     * @param screenSize screen size
     * @return either modified or unmodified WebPopOver location
     */
    protected Point checkRightCollision ( final Point location, final Dimension ps, final Dimension screenSize )
    {
        if ( location.x + ps.width > screenSize.width )
        {
            location.x = screenSize.width - ps.width;
        }
        return location;
    }

    /**
     * Returns actual direction depending on preferred WebPopOver direction, its sizes and source point.
     *
     * @param invoker    invoker component
     * @param ltr        whether LTR orientation is active or not
     * @param cw         corner with
     * @param ps         WebPopOver size without shade widths
     * @param screenSize screen size    @return actual WebPopOver direction
     */
    protected PopOverDirection getActualDirection ( final Component invoker, final boolean ltr, final int cw, final Dimension ps,
                                                    final Dimension screenSize )
    {
        for ( final PopOverDirection checkedDirection : preferredDirection.getPriority () )
        {
            final Point sp = getActualSourcePoint ( invoker, ltr, checkedDirection );
            if ( checkedDirection == PopOverDirection.up )
            {
                if ( sp.y - cw - ps.height > 0 )
                {
                    return checkedDirection;
                }
            }
            else if ( checkedDirection == PopOverDirection.down )
            {
                if ( sp.y + cw + ps.height < screenSize.height )
                {
                    return checkedDirection;
                }
            }
            else if ( checkedDirection == ( ltr ? PopOverDirection.left : PopOverDirection.right ) )
            {
                if ( sp.x - cw - ps.width > 0 )
                {
                    return checkedDirection;
                }
            }
            else if ( checkedDirection == ( ltr ? PopOverDirection.right : PopOverDirection.left ) )
            {
                if ( sp.x + cw + ps.width < screenSize.width )
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
     * @param invoker   invoker component
     * @param ltr       whether LTR orientation is active or not
     * @param direction WebPopOver direction  @return actual source point
     */
    protected Point getActualSourcePoint ( final Component invoker, final boolean ltr, final PopOverDirection direction )
    {
        final Point los = invoker.getLocationOnScreen ();
        final Dimension size = invoker.getSize ();
        if ( popOverSourcePoint == PopOverSourcePoint.componentCenter )
        {
            return new Point ( los.x + size.width / 2, los.y + size.height / 2 );
        }
        else
        {
            if ( direction == PopOverDirection.up )
            {
                return new Point ( los.x + size.width / 2, los.y );
            }
            else if ( direction == PopOverDirection.down )
            {
                return new Point ( los.x + size.width / 2, los.y + size.height );
            }
            else if ( direction == ( ltr ? PopOverDirection.left : PopOverDirection.right ) )
            {
                return new Point ( los.x, los.y + size.height / 2 );
            }
            else if ( direction == ( ltr ? PopOverDirection.right : PopOverDirection.left ) )
            {
                return new Point ( los.x + size.width, los.y + size.height / 2 );
            }
        }
        return null;
    }
}