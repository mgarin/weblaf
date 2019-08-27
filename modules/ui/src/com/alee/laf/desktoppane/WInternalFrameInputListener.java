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

package com.alee.laf.desktoppane;

import com.alee.api.annotations.NotNull;
import com.alee.api.jdk.Objects;
import com.alee.laf.AbstractUIInputListener;
import com.alee.laf.UIActionMap;
import com.alee.laf.WebLookAndFeel;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;

/**
 * Basic UI input listener for {@link WInternalFrameUI} implementation.
 * It is partially based on Swing {@link javax.swing.plaf.basic.BasicInternalFrameUI} but cleaned up and optimized.
 * It is also a replacement for {@code javax.swing.plaf.basic.BasicInternalFrameUI.BorderListener} providing frame resize capabilities.
 *
 * There are a few improvements added:
 * - It doesn't use {@link RootPaneContainer}'s glass pane to avoid hiding it when it was added by someone else
 * - It filters out any {@link MouseEvent} except for left mouse clicks in {@link #mouseClicked(MouseEvent)}
 *
 * @param <C> {@link JInternalFrame} type
 * @param <U> {@link WInternalFrameUI} type
 * @author David Kloba
 * @author Rich Schiavi
 * @author Mikle Garin
 */
public class WInternalFrameInputListener<C extends JInternalFrame, U extends WInternalFrameUI<C>> extends AbstractUIInputListener<C, U>
        implements InternalFrameInputListener<C>, PropertyChangeListener, ComponentListener, MouseListener, MouseMotionListener,
        WindowFocusListener, SwingConstants
{
    /**
     * todo 1. Add action map for menu from {@link javax.swing.plaf.basic.BasicInternalFrameUI}
     */

    /**
     * Empty resize direction.
     */
    protected static final int RESIZE_NONE = 0;

    /**
     * {@link JInternalFrame}'s parent bounds.
     */
    private Rectangle parentBounds;

    /**
     * Whether or not {@link JInternalFrame} is being dragged.
     */
    protected boolean dragging = false;

    /**
     * Whether or not {@link JInternalFrame} is being resized.
     */
    protected boolean resizing = false;

    /**
     * Mouse X location in absolute coordinate system.
     */
    protected int absX;

    /**
     * Mouse Y location in absolute coordinate system.
     */
    protected int absY;

    /**
     * Mouse X location in source view's coordinate system.
     */
    protected int relX;

    /**
     * Mouse Y location in source view's coordinate system.
     */
    protected int relY;

    /**
     * Starting {@link JInternalFrame} bounds.
     */
    protected Rectangle startingBounds;

    /**
     * Resize direction.
     */
    protected int resizeDir;

    /**
     * Marker for discarding mouse release actions.
     */
    protected boolean discardRelease = false;

    /**
     * Virtual resize corner size.
     */
    protected int resizeCornerSize = 16;

    /**
     * Whether or not this {@link ComponentListener} is currently installed in {@link JInternalFrame} parent.
     */
    protected boolean componentListenerAdded = false;

    @Override
    public void install ( @NotNull final C component )
    {
        super.install ( component );

        // Installing listeners
        component.addPropertyChangeListener ( this );
        component.addMouseListener ( this );
        component.addMouseMotionListener ( this );

        // Installing parent component listener
        if ( component.getParent () != null && !componentListenerAdded )
        {
            component.getParent ().addComponentListener ( WInternalFrameInputListener.this );
            componentListenerAdded = true;
        }

        // Installing ActionMap
        final UIActionMap actionMap = new UIActionMap ();
        // todo BasicLookAndFeel.installAudioActionMap ( actionMap );
        SwingUtilities.replaceUIActionMap ( component, actionMap );
    }

    @Override
    public void uninstall ( @NotNull final C component )
    {
        // Uninstalling InputMap
        SwingUtilities.replaceUIInputMap ( component, JComponent.WHEN_IN_FOCUSED_WINDOW, null );

        // Uninstalling ActionMap
        SwingUtilities.replaceUIActionMap ( component, null );

        // Uninstalling parent component listener
        if ( component.getParent () != null && componentListenerAdded )
        {
            component.getParent ().removeComponentListener ( WInternalFrameInputListener.this );
            componentListenerAdded = false;
        }

        // Uninstalling listeners
        component.removeMouseMotionListener ( this );
        component.removeMouseListener ( this );
        component.removePropertyChangeListener ( this );

        super.uninstall ( component );
    }

    @Override
    public void installPane ( @NotNull final Component component )
    {
        component.addMouseListener ( this );
        component.addMouseMotionListener ( this );
    }

    @Override
    public void uninstallPane ( @NotNull final Component component )
    {
        component.removeMouseMotionListener ( this );
        component.removeMouseListener ( this );
    }

    @Override
    public void propertyChange ( final PropertyChangeEvent evt )
    {
        final String prop = evt.getPropertyName ();
        final Object newValue = evt.getNewValue ();
        final Object oldValue = evt.getOldValue ();
        if ( Objects.equals ( prop, JInternalFrame.IS_CLOSED_PROPERTY ) )
        {
            if ( newValue == Boolean.TRUE )
            {
                // Cancel a resize in progress if the internal frame was closed
                cancelResize ();
                if ( component.getParent () != null && componentListenerAdded )
                {
                    component.getParent ().removeComponentListener ( WInternalFrameInputListener.this );
                    componentListenerAdded = false;
                }
                componentUI.closeFrame ();
            }
        }
        else if ( Objects.equals ( prop, JInternalFrame.IS_MAXIMUM_PROPERTY ) )
        {
            if ( newValue == Boolean.TRUE )
            {
                componentUI.maximizeFrame ();
            }
            else
            {
                componentUI.minimizeFrame ();
            }
        }
        else if ( Objects.equals ( prop, JInternalFrame.IS_ICON_PROPERTY ) )
        {
            if ( newValue == Boolean.TRUE )
            {
                componentUI.iconifyFrame ();
            }
            else
            {
                componentUI.deiconifyFrame ();
            }
        }
        else if ( Objects.equals ( prop, JInternalFrame.IS_SELECTED_PROPERTY ) )
        {
            if ( newValue == Boolean.TRUE && oldValue == Boolean.FALSE )
            {
                componentUI.activateFrame ();
            }
            else if ( newValue == Boolean.FALSE && oldValue == Boolean.TRUE )
            {
                componentUI.deactivateFrame ();
            }
        }
        else if ( Objects.equals ( prop, WebLookAndFeel.ANCESTOR_PROPERTY ) )
        {
            if ( newValue == null )
            {
                // Cancel a resize in progress, if the internal frame gets a remove(), removeNotify() or setIcon(true)
                cancelResize ();
            }
            if ( component.getParent () != null )
            {
                parentBounds = component.getParent ().getBounds ();
            }
            else
            {
                parentBounds = null;
            }
            if ( component.getParent () != null && !componentListenerAdded )
            {
                component.getParent ().addComponentListener ( WInternalFrameInputListener.this );
                componentListenerAdded = true;
            }
        }
        else if ( Objects.equals ( prop, JInternalFrame.TITLE_PROPERTY, WebInternalFrame.CLOSABLE_PROPERTY,
                WebInternalFrame.ICONABLE_PROPERTY, WebInternalFrame.MAXIMIZABLE_PROPERTY ) )
        {
            final Dimension dim = component.getMinimumSize ();
            final Dimension frame_dim = component.getSize ();
            if ( dim.width > frame_dim.width )
            {
                component.setSize ( dim.width, frame_dim.height );
            }
        }
    }

    /**
     * Invoked when a JInternalFrame's parent's size changes.
     */
    @Override
    public void componentResized ( final ComponentEvent e )
    {
        // Get the JInternalFrame's parent container size
        final Rectangle parentNewBounds = ( ( Component ) e.getSource () ).getBounds ();
        JInternalFrame.JDesktopIcon icon = null;

        if ( component != null )
        {
            icon = component.getDesktopIcon ();

            // Resize the internal frame if it is maximized and relocate the associated icon as well
            if ( component.isMaximum () )
            {
                component.setBounds ( 0, 0, parentNewBounds.width, parentNewBounds.height );
            }
        }

        // Relocate the icon base on the new parent bounds.
        if ( icon != null )
        {
            final Rectangle iconBounds = icon.getBounds ();
            final int y = iconBounds.y + parentNewBounds.height - parentBounds.height;
            icon.setBounds ( iconBounds.x, y, iconBounds.width, iconBounds.height );
        }

        // Update the new parent bounds for next resize.
        if ( !parentBounds.equals ( parentNewBounds ) )
        {
            parentBounds = parentNewBounds;
        }

        // Validate the component tree for this container.
        if ( component != null )
        {
            component.validate ();
        }
    }

    @Override
    public void componentMoved ( final ComponentEvent e )
    {
    }

    @Override
    public void componentShown ( final ComponentEvent e )
    {
    }

    @Override
    public void componentHidden ( final ComponentEvent e )
    {
    }

    @Override
    public void windowGainedFocus ( final WindowEvent e )
    {
    }

    /**
     * Cancels resize which may be in progress, when a WINDOW_LOST_FOCUS event occurs.
     * May be caused by an Alt-Tab or a modal dialog popup.
     */
    @Override
    public void windowLostFocus ( final WindowEvent e )
    {
        cancelResize ();
    }

    @Override
    public void mouseClicked ( final MouseEvent e )
    {
        if ( e.getClickCount () > 1 && e.getSource () == componentUI.getNorthPane () && SwingUtils.isLeftMouseButton ( e ) )
        {
            if ( component.isIconifiable () && component.isIcon () )
            {
                try
                {
                    component.setIcon ( false );
                }
                catch ( final PropertyVetoException ignored )
                {
                }
            }
            else if ( component.isMaximizable () )
            {
                if ( !component.isMaximum () )
                {
                    try
                    {
                        component.setMaximum ( true );
                    }
                    catch ( final PropertyVetoException ignored )
                    {
                    }
                }
                else
                {
                    try
                    {
                        component.setMaximum ( false );
                    }
                    catch ( final PropertyVetoException ignored )
                    {
                    }
                }
            }
        }
    }

    /**
     * Cancels resize which may be in progress.
     */
    protected void cancelResize ()
    {
        if ( resizing )
        {
            finishMouseReleased ();
        }
    }

    /**
     * Factor out {@link #finishMouseReleased()} from {@link #mouseReleased(MouseEvent)},
     * so that it can be called by cancelResize() without passing it a {@code null} {@link MouseEvent}.
     */
    protected void finishMouseReleased ()
    {
        if ( !discardRelease )
        {
            if ( resizeDir == RESIZE_NONE )
            {
                componentUI.getDesktopManager ().endDraggingFrame ( component );
                dragging = false;
            }
            else
            {
                // Remove the WindowFocusListener for handling a
                // WINDOW_LOST_FOCUS event with a cancelResize().
                final Window windowAncestor = SwingUtilities.getWindowAncestor ( component );
                if ( windowAncestor != null )
                {
                    windowAncestor.removeWindowFocusListener ( WInternalFrameInputListener.this );
                }
                //                final Container c = internalFrame.getTopLevelAncestor ();
                //                if ( c instanceof RootPaneContainer )
                //                {
                //                    final Component glassPane = ( ( RootPaneContainer ) c ).getGlassPane ();
                //                    glassPane.setCursor ( Cursor.getPredefinedCursor ( Cursor.DEFAULT_CURSOR ) );
                //                    glassPane.setVisible ( false );
                //                }
                componentUI.getDesktopManager ().endResizingFrame ( component );
                resizing = false;
                restoreFrameCursor ();
            }
            absX = 0;
            absY = 0;
            relX = 0;
            relY = 0;
            startingBounds = null;
            resizeDir = RESIZE_NONE;

            // Set discardRelease to true, so that only a mousePressed() which sets it to false,
            // will allow entry to the above code for finishing a resize.
            discardRelease = true;
        }
        else
        {
            discardRelease = false;
        }
    }

    @Override
    public void mouseReleased ( final MouseEvent e )
    {
        finishMouseReleased ();
    }

    @Override
    public void mousePressed ( final MouseEvent e )
    {
        final Component source = ( Component ) e.getSource ();

        relX = e.getX ();
        relY = e.getY ();

        final Point p = SwingUtilities.convertPoint ( source, e.getX (), e.getY (), null );
        absX = p.x;
        absY = p.y;

        startingBounds = component.getBounds ();
        resizeDir = RESIZE_NONE;
        discardRelease = false;

        try
        {
            component.setSelected ( true );
        }
        catch ( final PropertyVetoException ignored )
        {
        }

        final Point ep = new Point ( relX, relY );
        if ( source == componentUI.getNorthPane () )
        {
            final Point np = componentUI.getNorthPane ().getLocation ();
            ep.x += np.x;
            ep.y += np.y;
        }

        final Insets i = component.getInsets ();
        if ( source == componentUI.getNorthPane () &&
                ep.x > i.left && ep.y > i.top && ep.x < component.getWidth () - i.right )
        {
            componentUI.getDesktopManager ().beginDraggingFrame ( component );
            dragging = true;
        }
        else if ( component.isResizable () )
        {
            if ( source == component || source == componentUI.getNorthPane () )
            {
                if ( ep.x <= i.left )
                {
                    if ( ep.y < resizeCornerSize + i.top )
                    {
                        resizeDir = NORTH_WEST;
                    }
                    else if ( ep.y > component.getHeight () - resizeCornerSize - i.bottom )
                    {
                        resizeDir = SOUTH_WEST;
                    }
                    else
                    {
                        resizeDir = WEST;
                    }
                }
                else if ( ep.x >= component.getWidth () - i.right )
                {
                    if ( ep.y < resizeCornerSize + i.top )
                    {
                        resizeDir = NORTH_EAST;
                    }
                    else if ( ep.y > component.getHeight () - resizeCornerSize - i.bottom )
                    {
                        resizeDir = SOUTH_EAST;
                    }
                    else
                    {
                        resizeDir = EAST;
                    }
                }
                else if ( ep.y <= i.top )
                {
                    if ( ep.x < resizeCornerSize + i.left )
                    {
                        resizeDir = NORTH_WEST;
                    }
                    else if ( ep.x > component.getWidth () - resizeCornerSize - i.right )
                    {
                        resizeDir = NORTH_EAST;
                    }
                    else
                    {
                        resizeDir = NORTH;
                    }
                }
                else if ( ep.y >= component.getHeight () - i.bottom )
                {
                    if ( ep.x < resizeCornerSize + i.left )
                    {
                        resizeDir = SOUTH_WEST;
                    }
                    else if ( ep.x > component.getWidth () - resizeCornerSize - i.right )
                    {
                        resizeDir = SOUTH_EAST;
                    }
                    else
                    {
                        resizeDir = SOUTH;
                    }
                }
                else
                {
                    // The mouse press happened inside the frame, not in the border
                    discardRelease = true;
                }
                if ( resizeDir != RESIZE_NONE )
                {
                    //            Cursor s = Cursor.getPredefinedCursor ( Cursor.DEFAULT_CURSOR );
                    //            switch ( resizeDir )
                    //            {
                    //                case SOUTH:
                    //                    s = Cursor.getPredefinedCursor ( Cursor.S_RESIZE_CURSOR );
                    //                    break;
                    //                case NORTH:
                    //                    s = Cursor.getPredefinedCursor ( Cursor.N_RESIZE_CURSOR );
                    //                    break;
                    //                case WEST:
                    //                    s = Cursor.getPredefinedCursor ( Cursor.W_RESIZE_CURSOR );
                    //                    break;
                    //                case EAST:
                    //                    s = Cursor.getPredefinedCursor ( Cursor.E_RESIZE_CURSOR );
                    //                    break;
                    //                case SOUTH_EAST:
                    //                    s = Cursor.getPredefinedCursor ( Cursor.SE_RESIZE_CURSOR );
                    //                    break;
                    //                case SOUTH_WEST:
                    //                    s = Cursor.getPredefinedCursor ( Cursor.SW_RESIZE_CURSOR );
                    //                    break;
                    //                case NORTH_WEST:
                    //                    s = Cursor.getPredefinedCursor ( Cursor.NW_RESIZE_CURSOR );
                    //                    break;
                    //                case NORTH_EAST:
                    //                    s = Cursor.getPredefinedCursor ( Cursor.NE_RESIZE_CURSOR );
                    //                    break;
                    //            }
                    //            final Container c = component.getTopLevelAncestor ();
                    //            if ( c instanceof RootPaneContainer )
                    //            {
                    //                final Component glassPane = ( ( RootPaneContainer ) c ).getGlassPane ();
                    //                glassPane.setVisible ( true );
                    //                glassPane.setCursor ( s );
                    //            }

                    componentUI.getDesktopManager ().beginResizingFrame ( component, resizeDir );
                    resizing = true;

                    // Add the WindowFocusListener for handling a WINDOW_LOST_FOCUS event with a cancelResize()
                    final Window windowAncestor = SwingUtilities.getWindowAncestor ( component );
                    if ( windowAncestor != null )
                    {
                        windowAncestor.addWindowFocusListener ( WInternalFrameInputListener.this );
                    }
                }
            }
        }
    }

    @Override
    public void mouseDragged ( final MouseEvent e )
    {
        // (STEVE) Yucky work around for bug ID 4106552
        if ( startingBounds != null )
        {
            final Component source = ( Component ) e.getSource ();

            final Point p = SwingUtilities.convertPoint ( source, e.getX (), e.getY (), null );
            int deltaX = absX - p.x;
            int deltaY = absY - p.y;

            final Dimension min = component.getMinimumSize ();
            final Dimension max = component.getMaximumSize ();

            int newX;
            int newY;
            final int newW;
            final int newH;

            // Handle a MOVE
            if ( dragging )
            {
                // Don't allow moving of frames if maximixed or left mouse button was not used
                if ( !component.isMaximum () && ( e.getModifiers () & InputEvent.BUTTON1_MASK ) == InputEvent.BUTTON1_MASK )
                {
                    final int pWidth;
                    final int pHeight;
                    final Dimension s = component.getParent ().getSize ();
                    pWidth = s.width;
                    pHeight = s.height;


                    newX = startingBounds.x - deltaX;
                    newY = startingBounds.y - deltaY;

                    // Make sure we stay in-bounds
                    final Insets i = component.getInsets ();
                    if ( newX + i.left <= -relX )
                    {
                        newX = -relX - i.left + 1;
                    }
                    if ( newY + i.top <= -relY )
                    {
                        newY = -relY - i.top + 1;
                    }
                    if ( newX + relX + i.right >= pWidth )
                    {
                        newX = pWidth - relX - i.right - 1;
                    }
                    if ( newY + relY + i.bottom >= pHeight )
                    {
                        newY = pHeight - relY - i.bottom - 1;
                    }

                    componentUI.getDesktopManager ().dragFrame ( component, newX, newY );
                }
            }
            else if ( component.isResizable () )
            {
                newX = component.getX ();
                newY = component.getY ();

                parentBounds = component.getParent ().getBounds ();

                if ( resizeDir == NORTH )
                {
                    if ( startingBounds.height + deltaY < min.height )
                    {
                        deltaY = -( startingBounds.height - min.height );
                    }
                    else if ( startingBounds.height + deltaY > max.height )
                    {
                        deltaY = max.height - startingBounds.height;
                    }
                    if ( startingBounds.y - deltaY < 0 )
                    {
                        deltaY = startingBounds.y;
                    }

                    newX = startingBounds.x;
                    newY = startingBounds.y - deltaY;
                    newW = startingBounds.width;
                    newH = startingBounds.height + deltaY;

                    componentUI.getDesktopManager ().resizeFrame ( component, newX, newY, newW, newH );
                }
                else if ( resizeDir == NORTH_EAST )
                {
                    if ( startingBounds.height + deltaY < min.height )
                    {
                        deltaY = -( startingBounds.height - min.height );
                    }
                    else if ( startingBounds.height + deltaY > max.height )
                    {
                        deltaY = max.height - startingBounds.height;
                    }
                    if ( startingBounds.y - deltaY < 0 )
                    {
                        deltaY = startingBounds.y;
                    }

                    if ( startingBounds.width - deltaX < min.width )
                    {
                        deltaX = startingBounds.width - min.width;
                    }
                    else if ( startingBounds.width - deltaX > max.width )
                    {
                        deltaX = -( max.width - startingBounds.width );
                    }
                    if ( startingBounds.x + startingBounds.width - deltaX > parentBounds.width )
                    {
                        deltaX = startingBounds.x + startingBounds.width - parentBounds.width;
                    }

                    newX = startingBounds.x;
                    newY = startingBounds.y - deltaY;
                    newW = startingBounds.width - deltaX;
                    newH = startingBounds.height + deltaY;

                    componentUI.getDesktopManager ().resizeFrame ( component, newX, newY, newW, newH );
                }
                else if ( resizeDir == EAST )
                {
                    if ( startingBounds.width - deltaX < min.width )
                    {
                        deltaX = startingBounds.width - min.width;
                    }
                    else if ( startingBounds.width - deltaX > max.width )
                    {
                        deltaX = -( max.width - startingBounds.width );
                    }
                    if ( startingBounds.x + startingBounds.width - deltaX > parentBounds.width )
                    {
                        deltaX = startingBounds.x + startingBounds.width - parentBounds.width;
                    }

                    newW = startingBounds.width - deltaX;
                    newH = startingBounds.height;

                    componentUI.getDesktopManager ().resizeFrame ( component, newX, newY, newW, newH );
                }
                else if ( resizeDir == SOUTH_EAST )
                {
                    if ( startingBounds.width - deltaX < min.width )
                    {
                        deltaX = startingBounds.width - min.width;
                    }
                    else if ( startingBounds.width - deltaX > max.width )
                    {
                        deltaX = -( max.width - startingBounds.width );
                    }
                    if ( startingBounds.x + startingBounds.width - deltaX > parentBounds.width )
                    {
                        deltaX = startingBounds.x + startingBounds.width - parentBounds.width;
                    }

                    if ( startingBounds.height - deltaY < min.height )
                    {
                        deltaY = startingBounds.height - min.height;
                    }
                    else if ( startingBounds.height - deltaY > max.height )
                    {
                        deltaY = -( max.height - startingBounds.height );
                    }
                    if ( startingBounds.y + startingBounds.height - deltaY > parentBounds.height )
                    {
                        deltaY = startingBounds.y + startingBounds.height - parentBounds.height;
                    }

                    newW = startingBounds.width - deltaX;
                    newH = startingBounds.height - deltaY;

                    componentUI.getDesktopManager ().resizeFrame ( component, newX, newY, newW, newH );
                }
                else if ( resizeDir == SOUTH )
                {
                    if ( startingBounds.height - deltaY < min.height )
                    {
                        deltaY = startingBounds.height - min.height;
                    }
                    else if ( startingBounds.height - deltaY > max.height )
                    {
                        deltaY = -( max.height - startingBounds.height );
                    }
                    if ( startingBounds.y + startingBounds.height - deltaY > parentBounds.height )
                    {
                        deltaY = startingBounds.y + startingBounds.height - parentBounds.height;
                    }

                    newW = startingBounds.width;
                    newH = startingBounds.height - deltaY;

                    componentUI.getDesktopManager ().resizeFrame ( component, newX, newY, newW, newH );
                }
                else if ( resizeDir == SOUTH_WEST )
                {
                    if ( startingBounds.height - deltaY < min.height )
                    {
                        deltaY = startingBounds.height - min.height;
                    }
                    else if ( startingBounds.height - deltaY > max.height )
                    {
                        deltaY = -( max.height - startingBounds.height );
                    }
                    if ( startingBounds.y + startingBounds.height - deltaY > parentBounds.height )
                    {
                        deltaY = startingBounds.y + startingBounds.height - parentBounds.height;
                    }

                    if ( startingBounds.width + deltaX < min.width )
                    {
                        deltaX = -( startingBounds.width - min.width );
                    }
                    else if ( startingBounds.width + deltaX > max.width )
                    {
                        deltaX = max.width - startingBounds.width;
                    }
                    if ( startingBounds.x - deltaX < 0 )
                    {
                        deltaX = startingBounds.x;
                    }

                    newX = startingBounds.x - deltaX;
                    newY = startingBounds.y;
                    newW = startingBounds.width + deltaX;
                    newH = startingBounds.height - deltaY;

                    componentUI.getDesktopManager ().resizeFrame ( component, newX, newY, newW, newH );
                }
                else if ( resizeDir == WEST )
                {
                    if ( startingBounds.width + deltaX < min.width )
                    {
                        deltaX = -( startingBounds.width - min.width );
                    }
                    else if ( startingBounds.width + deltaX > max.width )
                    {
                        deltaX = max.width - startingBounds.width;
                    }
                    if ( startingBounds.x - deltaX < 0 )
                    {
                        deltaX = startingBounds.x;
                    }

                    newX = startingBounds.x - deltaX;
                    newY = startingBounds.y;
                    newW = startingBounds.width + deltaX;
                    newH = startingBounds.height;

                    componentUI.getDesktopManager ().resizeFrame ( component, newX, newY, newW, newH );
                }
                else if ( resizeDir == NORTH_WEST )
                {
                    if ( startingBounds.width + deltaX < min.width )
                    {
                        deltaX = -( startingBounds.width - min.width );
                    }
                    else if ( startingBounds.width + deltaX > max.width )
                    {
                        deltaX = max.width - startingBounds.width;
                    }
                    if ( startingBounds.x - deltaX < 0 )
                    {
                        deltaX = startingBounds.x;
                    }

                    if ( startingBounds.height + deltaY < min.height )
                    {
                        deltaY = -( startingBounds.height - min.height );
                    }
                    else if ( startingBounds.height + deltaY > max.height )
                    {
                        deltaY = max.height - startingBounds.height;
                    }
                    if ( startingBounds.y - deltaY < 0 )
                    {
                        deltaY = startingBounds.y;
                    }

                    newX = startingBounds.x - deltaX;
                    newY = startingBounds.y - deltaY;
                    newW = startingBounds.width + deltaX;
                    newH = startingBounds.height + deltaY;

                    componentUI.getDesktopManager ().resizeFrame ( component, newX, newY, newW, newH );
                }
            }
        }
    }

    @Override
    public void mouseMoved ( final MouseEvent e )
    {
        if ( component.isResizable () )
        {
            @NotNull final Object source = e.getSource ();
            if ( source == component || source == componentUI.getNorthPane () )
            {
                final Insets i = component.getInsets ();
                final Point ep = new Point ( e.getX (), e.getY () );
                if ( source == componentUI.getNorthPane () )
                {
                    final Point np = componentUI.getNorthPane ().getLocation ();
                    ep.x += np.x;
                    ep.y += np.y;
                }
                if ( ep.x <= i.left )
                {
                    if ( ep.y < resizeCornerSize + i.top )
                    {
                        component.setCursor ( Cursor.getPredefinedCursor ( Cursor.NW_RESIZE_CURSOR ) );
                    }
                    else if ( ep.y > component.getHeight () - resizeCornerSize - i.bottom )
                    {
                        component.setCursor ( Cursor.getPredefinedCursor ( Cursor.SW_RESIZE_CURSOR ) );
                    }
                    else
                    {
                        component.setCursor ( Cursor.getPredefinedCursor ( Cursor.W_RESIZE_CURSOR ) );
                    }
                }
                else if ( ep.x >= component.getWidth () - i.right )
                {
                    if ( e.getY () < resizeCornerSize + i.top )
                    {
                        component.setCursor ( Cursor.getPredefinedCursor ( Cursor.NE_RESIZE_CURSOR ) );
                    }
                    else if ( ep.y > component.getHeight () - resizeCornerSize - i.bottom )
                    {
                        component.setCursor ( Cursor.getPredefinedCursor ( Cursor.SE_RESIZE_CURSOR ) );
                    }
                    else
                    {
                        component.setCursor ( Cursor.getPredefinedCursor ( Cursor.E_RESIZE_CURSOR ) );
                    }
                }
                else if ( ep.y <= i.top )
                {
                    if ( ep.x < resizeCornerSize + i.left )
                    {
                        component.setCursor ( Cursor.getPredefinedCursor ( Cursor.NW_RESIZE_CURSOR ) );
                    }
                    else if ( ep.x > component.getWidth () - resizeCornerSize - i.right )
                    {
                        component.setCursor ( Cursor.getPredefinedCursor ( Cursor.NE_RESIZE_CURSOR ) );
                    }
                    else
                    {
                        component.setCursor ( Cursor.getPredefinedCursor ( Cursor.N_RESIZE_CURSOR ) );
                    }
                }
                else if ( ep.y >= component.getHeight () - i.bottom )
                {
                    if ( ep.x < resizeCornerSize + i.left )
                    {
                        component.setCursor ( Cursor.getPredefinedCursor ( Cursor.SW_RESIZE_CURSOR ) );
                    }
                    else if ( ep.x > component.getWidth () - resizeCornerSize - i.right )
                    {
                        component.setCursor ( Cursor.getPredefinedCursor ( Cursor.SE_RESIZE_CURSOR ) );
                    }
                    else
                    {
                        component.setCursor ( Cursor.getPredefinedCursor ( Cursor.S_RESIZE_CURSOR ) );
                    }
                }
                else
                {
                    restoreFrameCursor ();
                }
            }
            else
            {
                restoreFrameCursor ();
            }
        }
    }

    @Override
    public void mouseEntered ( final MouseEvent e )
    {
        restoreFrameCursor ();
    }

    @Override
    public void mouseExited ( final MouseEvent e )
    {
        restoreFrameCursor ();
    }

    /**
     * Restore {@link JInternalFrame} cursor to previously set.
     */
    protected void restoreFrameCursor ()
    {
        if ( !resizing )
        {
            Cursor s = component.getLastCursor ();
            if ( s == null )
            {
                s = Cursor.getPredefinedCursor ( Cursor.DEFAULT_CURSOR );
            }
            component.setCursor ( s );
        }
    }
}