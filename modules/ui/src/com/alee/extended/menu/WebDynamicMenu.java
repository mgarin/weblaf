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

package com.alee.extended.menu;

import com.alee.extended.window.WebPopup;
import com.alee.laf.window.WebWindow;
import com.alee.managers.style.StyleId;
import com.alee.utils.GeometryUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom dynamic menu with pretty display/hide animations.
 *
 * @author Mikle Garin
 */

public class WebDynamicMenu extends WebPopup
{
    /**
     * todo 1. Add sliding down vertical menu (with text and selection background)
     */

    /**
     * Radius of the menu background in px.
     */
    protected int radius;

    /**
     * First element position angle in degrees.
     * Counted from the circle top-most point.
     */
    protected double startingAngle;

    /**
     * Angle which is available for menu elements.
     * It is 360 degrees by default (whole circle available).
     */
    protected double angleRange;

    /**
     * Menu animation type.
     */
    protected DynamicMenuType type;

    /**
     * Menu hide animation type.
     */
    protected DynamicMenuType hideType;

    /**
     * Menu animation direction.
     */
    protected boolean clockwise;

    /**
     * Menu items list.
     */
    protected List<WebDynamicMenuItem> items = new ArrayList<WebDynamicMenuItem> ();

    /**
     * Index of menu item that caused menu to close.
     * This might affect the hiding animation.
     */
    protected int hidingCause = -1;

    /**
     * Constructs new dynamic menu.
     */
    public WebDynamicMenu ()
    {
        super ( StyleId.panelTransparent, new DynamicMenuLayout () );

        // Popup settings
        setAnimate ( true );
        setFadeStepSize ( 0.04f );
        setWindowOpaque ( false );
        setWindowOpacity ( 0f );
        setFollowInvoker ( true );
        setCloseOnOuterAction ( true );

        // Menu settings
        setRadius ( 60 );
        setStartingAngle ( 0 );
        setAngleRange ( 360 );
        setType ( DynamicMenuType.shutter );
        setHideType ( null );
        setClockwise ( true );
    }

    public int getRadius ()
    {
        return radius;
    }

    public void setRadius ( final int radius )
    {
        this.radius = radius;
    }

    public double getStartingAngle ()
    {
        return startingAngle;
    }

    public void setStartingAngle ( final double startingAngle )
    {
        this.startingAngle = startingAngle;
    }

    public double getAngleRange ()
    {
        return angleRange;
    }

    public void setAngleRange ( final double angleRange )
    {
        this.angleRange = angleRange;
    }

    public DynamicMenuType getType ()
    {
        return type;
    }

    public void setType ( final DynamicMenuType type )
    {
        this.type = type;
    }

    public DynamicMenuType getHideType ()
    {
        return hideType != null ? hideType : type;
    }

    public void setHideType ( final DynamicMenuType hideType )
    {
        this.hideType = hideType;
    }

    public boolean isClockwise ()
    {
        return clockwise;
    }

    public void setClockwise ( final boolean clockwise )
    {
        this.clockwise = clockwise;
    }

    public List<WebDynamicMenuItem> getItems ()
    {
        return items;
    }

    public WebDynamicMenuItem addItem ( final ImageIcon icon )
    {
        return addItem ( icon, null );
    }

    public WebDynamicMenuItem addItem ( final ImageIcon icon, final ActionListener action )
    {
        return addItem ( new WebDynamicMenuItem ( icon, action ) );
    }

    public WebDynamicMenuItem addItem ( final WebDynamicMenuItem menuItem )
    {
        items.add ( menuItem );

        //        final WebImage menuItem = new WebImage ( item.getIcon () )
        //        {
        //            @Override
        //            protected void paintComponent ( final Graphics g )
        //            {
        //                if ( item.isDrawBorder () )
        //                {
        //                    final Graphics2D g2d = ( Graphics2D ) g;
        //                    final Object aa = GraphicsUtils.setupAntialias ( g2d );
        //
        //                    final Area outer = new Area ( new Ellipse2D.Double ( 0, 0, getWidth (), getHeight () ) );
        //                    final Ellipse2D.Double inner = new Ellipse2D.Double ( 2, 2, getWidth () - 4, getHeight () - 4 );
        //                    outer.exclusiveOr ( new Area ( inner ) );
        //
        //                    g2d.setPaint ( isEnabled () ? item.getBorderColor () : item.getDisabledBorderColor () );
        //                    g2d.fill ( outer );
        //
        //                    g2d.setPaint ( Color.WHITE );
        //                    g2d.fill ( inner );
        //
        //                    GraphicsUtils.restoreAntialias ( g2d, aa );
        //                }
        //
        //                super.paintComponent ( g );
        //            }
        //        };
        menuItem.setEnabled ( menuItem.getAction () != null );
        menuItem.setMargin ( menuItem.getMargin () );

        menuItem.addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
            {
                final ActionListener action = menuItem.getAction ();
                if ( action != null )
                {
                    action.actionPerformed ( new ActionEvent ( e.getSource (), 0, "Action performed" ) );
                }
                hideMenu ( getComponentZOrder ( menuItem ) );
            }
        } );
        add ( menuItem );

        return menuItem;
    }

    /**
     * Returns index of menu item that caused menu to close.
     *
     * @return index of menu item that caused menu to close
     */
    public int getHidingCause ()
    {
        return hidingCause;
    }

    @Override
    public WebDynamicMenu showPopup ( final Component invoker, final int x, final int y )
    {
        showMenu ( invoker, x, y );
        return this;
    }

    /**
     * Displays dynamic menu for the specified invoker location.
     *
     * @param invoker  menu invoker
     * @param location menu location
     */
    public void showMenu ( final Component invoker, final Point location )
    {
        showMenu ( invoker, location.x, location.y );
    }

    /**
     * Displays dynamic menu for the specified invoker location.
     *
     * @param invoker menu invoker
     * @param x       menu location X coordinate
     * @param y       menu location Y coordinate
     */
    public void showMenu ( final Component invoker, final int x, final int y )
    {
        // Displaying menu
        final Point displayPoint = getActualLayout ().getDisplayPoint ( this, x, y );
        super.showPopup ( invoker, displayPoint.x, displayPoint.y );
    }

    @Override
    protected void showAnimationStepPerformed ()
    {
        revalidate ();
    }

    @Override
    public WebPopup hidePopup ()
    {
        hideMenu ();
        return this;
    }

    /**
     * Hides dynamic menu.
     */
    public void hideMenu ()
    {
        hideMenu ( -1 );
    }

    /**
     * Hides dynamic menu.
     *
     * @param index menu item that forced menu to hide
     */
    public void hideMenu ( final int index )
    {
        // Sets hiding cause
        this.hidingCause = index;

        // Start hiding menu
        super.hidePopup ();
    }

    @Override
    protected void hideAnimationStepPerformed ()
    {
        if ( visibilityProgress > 0f )
        {
            revalidate ();
        }
        else
        {
            hidingCause = -1;
            revalidate ();
        }
    }

    /**
     * Returns menu item angle relative to vertical axis.
     *
     * @param item menu item
     * @return menu item center point angle
     */
    public double getItemAngle ( final Component item )
    {
        return getItemAngle ( getComponentZOrder ( item ) );
    }

    /**
     * Returns menu item angle relative to vertical axis.
     *
     * @param index menu item index
     * @return menu item center point angle
     */
    public double getItemAngle ( final int index )
    {
        return GeometryUtils.toDegrees ( getActualLayout ().getItemAngle ( this, index ) );
    }

    /**
     * Returns actual menu layout manager.
     *
     * @return actual menu layout manager
     */
    public DynamicMenuLayout getActualLayout ()
    {
        return ( DynamicMenuLayout ) getLayout ();
    }

    /**
     * Returns whether any dynamic menu is currently displayed or not.
     *
     * @return true if any dynamic menu is currently displayed, false otherwise
     */
    public static boolean isAnyDynamicMenuDisplayed ()
    {
        for ( final Window window : Window.getWindows () )
        {
            if ( window.isShowing () && window instanceof WebWindow &&
                    ( ( WebWindow ) window ).getContentPane () instanceof WebDynamicMenu )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Hides all visible dynamic menus.
     */
    public static void hideAllDynamicMenus ()
    {
        for ( final Window window : Window.getWindows () )
        {
            if ( window.isShowing () && window instanceof WebWindow &&
                    ( ( WebWindow ) window ).getContentPane () instanceof WebDynamicMenu )
            {
                final WebDynamicMenu menu = ( WebDynamicMenu ) ( ( WebWindow ) window ).getContentPane ();
                menu.hideMenu ();
            }
        }
    }
}