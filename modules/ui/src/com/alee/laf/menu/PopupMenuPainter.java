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

package com.alee.laf.menu;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.combobox.WebComboBoxUI;
import com.alee.utils.*;

import javax.swing.*;
import java.awt.*;

/**
 * Base painter for JPopupMenu component.
 * It is used as WebPopupMenuUI default styling.
 *
 * @author Mikle Garin
 */

public class PopupMenuPainter<E extends JPopupMenu, U extends WebPopupMenuUI> extends AbstractPopupPainter<E, U>
        implements IPopupMenuPainter<E, U>
{
    /**
     * todo 1. Incorrect menu placement when corner is off (spacing == shade)
     * todo 2. When using popupMenuWay take invoker shade into account (if its UI has one -> ShadeProvider interface)
     * todo 3. Add left/right corners display support
     */

    /**
     * Style settings.
     */
    protected int menuSpacing = 1;
    protected boolean fixLocation = true;
    protected boolean dropdownStyleForMenuBar = true;

    /**
     * Runtime variables.
     */
    protected PopupMenuType popupMenuType = null;

    @Override
    protected void propertyChange ( final String property, final Object oldValue, final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChange ( property, oldValue, newValue );

        // Visibility property changes
        if ( CompareUtils.equals ( property, WebLookAndFeel.VISIBLE_PROPERTY ) )
        {
            // Updating menu type
            if ( newValue == Boolean.TRUE )
            {
                final Component invoker = component.getInvoker ();
                if ( invoker != null )
                {
                    if ( invoker instanceof JMenu )
                    {
                        if ( invoker.getParent () instanceof JPopupMenu )
                        {
                            setPopupMenuType ( PopupMenuType.menuBarSubMenu );
                        }
                        else
                        {
                            setPopupMenuType ( PopupMenuType.menuBarMenu );
                        }
                    }
                    else if ( invoker instanceof JComboBox )
                    {
                        setPopupMenuType ( PopupMenuType.comboBoxMenu );
                    }
                    else
                    {
                        setPopupMenuType ( PopupMenuType.customPopupMenu );
                    }
                }
                else
                {
                    setPopupMenuType ( PopupMenuType.customPopupMenu );
                }
            }

            // Either install or uninstall popup settings
            if ( newValue == Boolean.TRUE )
            {
                // For unix systems it is not required to repeat this update
                if ( !SystemUtils.isUnix () )
                {
                    // Install custom popup window settings
                    installPopupSettings ( SwingUtils.getWindowAncestor ( component ), component );
                }
            }
            else
            {
                // Uninstall custom popup window settings
                uninstallPopupSettings ( SwingUtils.getWindowAncestor ( component ), component );
            }
        }
    }

    @Override
    protected void orientationChange ()
    {
        // Performing default actions
        super.orientationChange ();

        // todo Probably just update location properly?
        // Hiding menu on orientation changes
        if ( component.isShowing () )
        {
            component.setVisible ( false );
        }
    }

    /**
     * Returns spacing between popup menus.
     *
     * @return spacing between popup menus
     */
    public int getMenuSpacing ()
    {
        return menuSpacing;
    }

    /**
     * Sets spacing between popup menus.
     *
     * @param spacing spacing between popup menus
     */
    public void setMenuSpacing ( final int spacing )
    {
        this.menuSpacing = spacing;
    }

    /**
     * Returns whether should fix initial popup menu location or not.
     *
     * @return true if should fix initial popup menu location, false otherwise
     */
    public boolean isFixLocation ()
    {
        return fixLocation;
    }

    /**
     * Sets whether should fix initial popup menu location or not.
     * If set to true popup menu will try to use best possible location to show up.
     * <p>
     * This is set to true by default to place menubar and menu popups correctly.
     * You might want to set this to false for some specific popup menu, but not all of them at once.
     *
     * @param fix whether should fix initial popup menu location or not
     */
    public void setFixLocation ( final boolean fix )
    {
        this.fixLocation = fix;
    }

    /**
     * Sets popup menu type.
     * This value is updated right before popup menu window becomes visible.
     * You can use it to draw different popup menu decoration for each popup menu type.
     *
     * @param type popup menu type
     */
    public void setPopupMenuType ( final PopupMenuType type )
    {
        this.popupMenuType = type;
        if ( popupMenuType == PopupMenuType.menuBarSubMenu )
        {
            setPopupStyle ( PopupStyle.simple );
        }
    }

    @Override
    public Insets getBorders ()
    {
        final Insets margin = super.getBorders ();
        margin.top += round;
        margin.bottom += round;
        return margin;
    }

    @Override
    protected void paintTransparentPopup ( final Graphics2D g2d, final E popupMenu )
    {
        final Dimension menuSize = popupMenu.getSize ();

        // Painting shade
        paintShade ( g2d, popupMenu, menuSize );

        // Painting background
        paintBackground ( g2d, popupMenu, menuSize );

        // Painting dropdown corner fill
        // This is a specific for WebPopupMenuUI feature
        paintDropdownCornerFill ( g2d, popupMenu, menuSize );

        // Painting border
        paintBorder ( g2d, popupMenu, menuSize );
    }

    /**
     * Paints dropdown-styled popup menu corner fill if menu item near it is selected.
     *
     * @param g2d       graphics context
     * @param popupMenu popup menu
     * @param menuSize  menu size
     */
    protected void paintDropdownCornerFill ( final Graphics2D g2d, final E popupMenu, final Dimension menuSize )
    {
        // Checking whether corner should be filled or not
        if ( popupStyle == PopupStyle.dropdown && round == 0 )
        {
            // Check that menu item is attached to menu side
            final boolean top = cornerSide == TOP;
            final boolean stick = top ? getBorders ().top == 0 : getBorders ().bottom == 0;
            if ( stick )
            {
                // todo Implement corner support
                //                // Checking that we can actually retrieve what item wants to fill corner with
                //                final int zIndex = top ? 0 : popupMenu.getComponentCount () - 1;
                //                final Component component = popupMenu.getComponent ( zIndex );
                //                if ( popupMenu instanceof BasicComboPopup )
                //                {
                //                    // Filling corner according to combobox preferences
                //                    if ( component instanceof JScrollPane )
                //                    {
                //                        // Usually there will be a scrollpane with list as the first element
                //                        final JScrollPane scrollPane = ( JScrollPane ) component;
                //                        final JList list = ( JList ) scrollPane.getViewport ().getView ();
                //                        if ( top && list.getSelectedIndex () == 0 )
                //                        {
                //                            // Filling top corner when first list element is selected
                //                            final WebComboBoxUI ui = geComboBoxUI ( popupMenu );
                //                            if ( ui != null )
                //                            {
                //                                g2d.setPaint ( ui.getNorthCornerFill () );
                //                                g2d.fill ( getDropdownCornerShape ( popupMenu, menuSize, true ) );
                //                            }
                //                        }
                //                        else if ( !top && list.getSelectedIndex () == list.getModel ().getSize () - 1 )
                //                        {
                //                            // Filling bottom corner when last list element is selected
                //                            final WebComboBoxUI ui = geComboBoxUI ( popupMenu );
                //                            if ( ui != null )
                //                            {
                //                                g2d.setPaint ( ui.getSouthCornerFill () );
                //                                g2d.fill ( getDropdownCornerShape ( popupMenu, menuSize, true ) );
                //                            }
                //                        }
                //                    }
                //                }
                //                else if ( component instanceof JMenuItem )
                //                {
                //                    // Filling corner if selected menu item is placed nearby
                //                    final JMenuItem menuItem = ( JMenuItem ) component;
                //                    if ( menuItem.isEnabled () && ( menuItem.getModel ().isArmed () || menuItem.isSelected () ) )
                //                    {
                //                        // Filling corner properly
                //                        if ( menuItem.getUI () instanceof WebMenuUI )
                //                        {
                //                            // Filling corner according to WebMenu styling
                //                            final WebMenuUI ui = ( WebMenuUI ) menuItem.getUI ();
                //                            g2d.setPaint ( top ? ui.getNorthCornerFill () : ui.getSouthCornerFill () );
                //                            g2d.fill ( getDropdownCornerShape ( popupMenu, menuSize, true ) );
                //                        }
                //                        else if ( menuItem.getUI () instanceof WebMenuItemUI )
                //                        {
                //                            // Filling corner according to WebMenuItem styling
                //                            final WebMenuItemUI ui = ( WebMenuItemUI ) menuItem.getUI ();
                //                            g2d.setPaint ( top ? ui.getNorthCornerFill () : ui.getSouthCornerFill () );
                //                            g2d.fill ( getDropdownCornerShape ( popupMenu, menuSize, true ) );
                //                        }
                //                    }
                //                }
            }
        }
    }

    /**
     * Returns combobox UI for the specified combobox popup menu.
     *
     * @param popupMenu popup menu to retrieve combobox UI for
     * @return combobox UI for the specified combobox popup menu
     */
    protected WebComboBoxUI geComboBoxUI ( final E popupMenu )
    {
        final JComboBox comboBox = ReflectUtils.getFieldValueSafely ( popupMenu, "comboBox" );
        return comboBox != null && comboBox.getUI () instanceof WebComboBoxUI ? ( WebComboBoxUI ) comboBox.getUI () : null;
    }

    @Override
    public Point preparePopupMenu ( final E popupMenu, final Component invoker, int x, int y )
    {
        // Updating popup location according to popup menu UI settings
        if ( invoker != null )
        {
            // Default corner position according to invoker's orientation
            relativeCorner = ltr ? 0 : Integer.MAX_VALUE;

            // Calculating position variables
            final boolean showing = invoker.isShowing ();
            final Point los = showing ? invoker.getLocationOnScreen () : new Point ( 0, 0 );
            final boolean fixLocation = this.fixLocation && showing;
            final int sideWidth = getSideWidth ();

            // Calculating final position variables
            if ( invoker instanceof JMenu )
            {
                if ( invoker.getParent () instanceof JPopupMenu )
                {
                    // Displaying simple-styled sub-menu
                    // It is displayed to left or right of the menu item
                    setPopupStyle ( PopupStyle.simple );
                    if ( fixLocation )
                    {
                        // Invoker X-location on screen also works as orientation indicator, so we don't need to check it here
                        x += ( los.x <= x ? -1 : 1 ) * ( shaped ? sideWidth - menuSpacing : -menuSpacing );
                        y += ( los.y <= y ? -1 : 1 ) * ( shaped ? sideWidth + 1 + round : round );
                    }
                }
                else if ( !dropdownStyleForMenuBar )
                {
                    // Displaying simple-styled top-level menu
                    // It is displayed below or above the menu bar
                    setPopupStyle ( PopupStyle.simple );
                    if ( fixLocation )
                    {
                        // Invoker X-location on screen also works as orientation indicator, so we don't need to check it here
                        x += ( los.x <= x ? -1 : 1 ) * ( shaped ? sideWidth - menuSpacing : -menuSpacing );
                        y -= shaped ? sideWidth + round + 1 : round;
                    }
                }
                else
                {
                    // Displaying dropdown-styled top-level menu
                    // It is displayed below or above the menu bar
                    setPopupStyle ( PopupStyle.dropdown );
                    cornerSide = los.y <= y ? TOP : BOTTOM;
                    if ( fixLocation )
                    {
                        // Invoker X-location on screen also works as orientation indicator, so we don't need to check it here
                        x += ( los.x <= x ? -1 : 1 ) * ( shaped ? sideWidth : 0 );
                        y += ( los.y <= y ? -1 : 1 ) * ( shaped ? sideWidth - cornerWidth : 0 );
                    }
                    relativeCorner = los.x + invoker.getWidth () / 2 - x;
                }
            }
            else
            {
                final boolean dropdown = popupStyle == PopupStyle.dropdown;
                if ( invoker instanceof JComboBox && popupMenu.getName ().equals ( "ComboPopup.popup" ) )
                {
                    cornerSide = los.y <= y ? TOP : BOTTOM;
                    if ( fixLocation )
                    {
                        x += shaped ? -sideWidth : 0;
                        if ( cornerSide == TOP )
                        {
                            y -= shaped ? sideWidth - ( dropdown ? cornerWidth : 0 ) : 0;
                        }
                        else
                        {
                            // Invoker preferred size is required instead of actual height
                            // This is because the original position takes it into account instead of height
                            final int ih = invoker.getPreferredSize ().height;
                            y -= ih + ( shaped ? sideWidth - ( dropdown ? cornerWidth : 0 ) : 0 );
                        }
                    }
                    relativeCorner = los.x + invoker.getWidth () / 2 - x;
                }
                else
                {
                    if ( fixLocation )
                    {
                        // Applying new location according to specified popup menu way
                        final PopupMenuWay popupMenuWay = ui.getPopupMenuWay ();
                        if ( popupMenuWay != null )
                        {
                            final Dimension ps = popupMenu.getPreferredSize ();
                            final Dimension is = invoker.getSize ();
                            final int cornerShear = dropdown ? sideWidth - cornerWidth : 0;
                            switch ( popupMenuWay )
                            {
                                case aboveStart:
                                {
                                    x = ( ltr ? los.x : los.x + is.width - ps.width ) + ( shaped ? ltr ? -sideWidth : sideWidth : 0 );
                                    y = los.y - ps.height + cornerShear;
                                    relativeCorner = ltr ? 0 : Integer.MAX_VALUE;
                                    break;
                                }
                                case aboveMiddle:
                                {
                                    x = los.x + is.width / 2 - ps.width / 2;
                                    y = los.y - ps.height + cornerShear;
                                    relativeCorner = los.x + invoker.getWidth () / 2 - x;
                                    break;
                                }
                                case aboveEnd:
                                {
                                    x = ( ltr ? los.x + is.width - ps.width : los.x ) + ( shaped ? ltr ? sideWidth : -sideWidth : 0 );
                                    y = los.y - ps.height + cornerShear;
                                    relativeCorner = ltr ? Integer.MAX_VALUE : 0;
                                    break;
                                }
                                case belowStart:
                                {
                                    x = ( ltr ? los.x : los.x + is.width - ps.width ) + ( shaped ? ltr ? -sideWidth : sideWidth : 0 );
                                    y = los.y + is.height - cornerShear;
                                    relativeCorner = ltr ? 0 : Integer.MAX_VALUE;
                                    break;
                                }
                                case belowMiddle:
                                {
                                    x = los.x + is.width / 2 - ps.width / 2;
                                    y = los.y + is.height - cornerShear;
                                    relativeCorner = los.x + invoker.getWidth () / 2 - x;
                                    break;
                                }
                                case belowEnd:
                                {
                                    x = ( ltr ? los.x + is.width - ps.width : los.x ) + ( shaped ? ltr ? sideWidth : -sideWidth : 0 );
                                    y = los.y + is.height - cornerShear;
                                    relativeCorner = ltr ? Integer.MAX_VALUE : 0;
                                    break;
                                }
                            }
                            cornerSide = popupMenuWay.getCornerSide ();
                        }
                    }
                }
            }
        }
        else
        {
            // Default corner position
            relativeCorner = 0;
        }

        return p ( x, y );
    }

    @Override
    public void configurePopup ( final E popupMenu, final Component invoker, final int x, final int y, final Popup popup )
    {
        // Retrieve component directly from the popup
        final Component window = ReflectUtils.callMethodSafely ( popup, "getComponent" );
        if ( window instanceof Window )
        {
            // Install custom popup window settings
            installPopupSettings ( ( Window ) window, popupMenu );
        }
    }

    /**
     * Configures popup menu window opacity and shape.
     *
     * @param window    popup menu window
     * @param popupMenu popup menu
     */
    protected void installPopupSettings ( final Window window, final E popupMenu )
    {
        if ( window != null && shaped && SwingUtils.isHeavyWeightWindow ( window ) )
        {
            // Workaround to remove Mac OS X shade around the window
            if ( window instanceof JWindow && SystemUtils.isMac () )
            {
                ( ( JWindow ) window ).getRootPane ().putClientProperty ( "Window.shadow", Boolean.FALSE );
            }

            // Updating window opacity state in case menu is displayed in a heavy-weight popup
            if ( SwingUtils.isHeavyWeightWindow ( window ) )
            {
                // Either change window opacity or shape if it is possible
                if ( ProprietaryUtils.isWindowTransparencyAllowed () )
                {
                    // Change window opacity
                    ProprietaryUtils.setWindowOpaque ( window, false );
                }
                else if ( ProprietaryUtils.isWindowShapeAllowed () )
                {
                    // Change window shape
                    window.pack ();
                    final Rectangle bounds = window.getBounds ();
                    ++bounds.width;
                    ++bounds.height;
                    final Shape shape = provideShape ( popupMenu, bounds );
                    ProprietaryUtils.setWindowShape ( window, shape );
                }
            }
        }
    }

    /**
     * Unconfigures popup menu window opacity and shape.
     *
     * @param window    popup menu window
     * @param popupMenu popup menu
     */
    @SuppressWarnings ("UnusedParameters")
    protected void uninstallPopupSettings ( final Window window, final E popupMenu )
    {
        if ( window != null && shaped && SwingUtils.isHeavyWeightWindow ( window ) )
        {
            // Restoring menu opacity state in case menu is in a separate heavy-weight window
            ProprietaryUtils.setWindowOpaque ( window, true );
            ProprietaryUtils.setWindowShape ( window, null );
        }
    }
}