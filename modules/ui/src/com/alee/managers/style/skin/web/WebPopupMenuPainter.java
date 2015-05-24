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

package com.alee.managers.style.skin.web;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.combobox.WebComboBoxUI;
import com.alee.laf.menu.*;
import com.alee.utils.ProprietaryUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.SystemUtils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboPopup;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Base painter for JPopupMenu component.
 * It is used as WebPopupMenuUI default styling.
 *
 * @author Mikle Garin
 */

public class WebPopupMenuPainter<E extends JPopupMenu, U extends WebPopupMenuUI> extends WebPopupPainter<E, U>
        implements PopupMenuPainter<E, U>
{
    /**
     * todo 1. Incorrect menu placement when corner is off (spacing == shade)
     * todo 2. When using popupMenuWay take invoker shade into account (if its UI has one -> ShadeProvider interface)
     * todo 3. Add left/right corners display support
     */

    /**
     * Style settings.
     */
    protected int menuSpacing = WebPopupMenuStyle.menuSpacing;
    protected boolean fixLocation = WebPopupMenuStyle.fixLocation;

    /**
     * Listeners.
     */
    protected PropertyChangeListener popupMenuTypeUpdater;
    protected PropertyChangeListener visibilityChangeListener;
    protected PropertyChangeListener jdkSevenFixListener;

    /**
     * Runtime variables.
     */
    protected PopupMenuType popupMenuType = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Popup menu type updater
        popupMenuTypeUpdater = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                if ( evt.getNewValue () == Boolean.TRUE )
                {
                    // Update menu style
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
            }
        };
        component.addPropertyChangeListener ( WebLookAndFeel.VISIBLE_PROPERTY, popupMenuTypeUpdater );

        // Special listeners which set proper popup window opacity when needed
        if ( transparent )
        {
            visibilityChangeListener = new PropertyChangeListener ()
            {
                private Window ancestor;

                @Override
                public void propertyChange ( final PropertyChangeEvent evt )
                {
                    if ( evt.getNewValue () == Boolean.TRUE )
                    {
                        ancestor = SwingUtils.getWindowAncestor ( component );

                        // Workaround to remove Mac OS X shade around the menu window
                        if ( ancestor instanceof JWindow && SystemUtils.isMac () )
                        {
                            ( ( JWindow ) ancestor ).getRootPane ().putClientProperty ( "Window.shadow", Boolean.FALSE );
                        }

                        // Updating menu opacity state in case menu is in a separate heavy-weight window
                        if ( SwingUtils.isHeavyWeightWindow ( ancestor ) )
                        {
                            ProprietaryUtils.setWindowOpaque ( ancestor, false );
                            if ( !ProprietaryUtils.isWindowTransparencyAllowed () && ProprietaryUtils.isWindowShapeAllowed () )
                            {
                                final Rectangle bounds = ancestor.getBounds ();
                                ++bounds.width;
                                ++bounds.height;
                                final Shape shape = provideShape ( component, bounds );
                                ProprietaryUtils.setWindowShape ( ancestor, shape );
                            }
                        }
                    }
                    else
                    {
                        // Restoring menu opacity state in case menu is in a separate heavy-weight window
                        if ( SwingUtils.isHeavyWeightWindow ( ancestor ) )
                        {
                            ProprietaryUtils.setWindowOpaque ( ancestor, true );
                            ProprietaryUtils.setWindowShape ( ancestor, null );
                        }
                    }
                }
            };
            component.addPropertyChangeListener ( WebLookAndFeel.VISIBLE_PROPERTY, visibilityChangeListener );
        }
        else if ( SystemUtils.isJava7orAbove () )
        {
            // Workaround for menu with non-opaque parent window
            jdkSevenFixListener = new PropertyChangeListener ()
            {
                @Override
                public void propertyChange ( final PropertyChangeEvent evt )
                {
                    if ( evt.getNewValue () == Boolean.TRUE )
                    {
                        final Window ancestor = SwingUtils.getWindowAncestor ( component );
                        if ( SwingUtils.isHeavyWeightWindow ( ancestor ) )
                        {
                            final Component parent = ancestor.getParent ();
                            if ( parent != null && parent instanceof Window && !ProprietaryUtils.isWindowOpaque ( ( Window ) parent ) )
                            {
                                ProprietaryUtils.setWindowOpaque ( ancestor, false );
                            }
                        }
                    }
                }
            };
            component.addPropertyChangeListener ( WebLookAndFeel.VISIBLE_PROPERTY, jdkSevenFixListener );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Removing listeners
        if ( transparent )
        {
            component.removePropertyChangeListener ( WebLookAndFeel.VISIBLE_PROPERTY, visibilityChangeListener );
            visibilityChangeListener = null;
        }
        else if ( SystemUtils.isJava7orAbove () )
        {
            component.removePropertyChangeListener ( WebLookAndFeel.VISIBLE_PROPERTY, jdkSevenFixListener );
            jdkSevenFixListener = null;
        }
        component.removePropertyChangeListener ( WebLookAndFeel.VISIBLE_PROPERTY, popupMenuTypeUpdater );
        popupMenuTypeUpdater = null;

        super.uninstall ( c, ui );
    }

    /**
     * {@inheritDoc}
     */
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
     * <p/>
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getMargin ()
    {
        final Insets margin = super.getMargin ();
        margin.top += round;
        margin.bottom += round;
        return margin;
    }

    /**
     * {@inheritDoc}
     */
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
            final boolean stick = top ? getMargin ().top == 0 : getMargin ().bottom == 0;
            if ( stick )
            {
                // Checking that we can actually retrieve what item wants to fill corner with
                final int zIndex = top ? 0 : popupMenu.getComponentCount () - 1;
                final Component component = popupMenu.getComponent ( zIndex );
                if ( popupMenu instanceof BasicComboPopup )
                {
                    // Filling corner according to combobox preferences
                    if ( component instanceof JScrollPane )
                    {
                        // Usually there will be a scrollpane with list as the first element
                        final JScrollPane scrollPane = ( JScrollPane ) component;
                        final JList list = ( JList ) scrollPane.getViewport ().getView ();
                        if ( top && list.getSelectedIndex () == 0 )
                        {
                            // Filling top corner when first list element is selected
                            final WebComboBoxUI ui = geComboBoxUI ( popupMenu );
                            if ( ui != null )
                            {
                                g2d.setPaint ( ui.getNorthCornerFill () );
                                g2d.fill ( getDropdownCornerShape ( popupMenu, menuSize, true ) );
                            }
                        }
                        else if ( !top && list.getSelectedIndex () == list.getModel ().getSize () - 1 )
                        {
                            // Filling bottom corner when last list element is selected
                            final WebComboBoxUI ui = geComboBoxUI ( popupMenu );
                            if ( ui != null )
                            {
                                g2d.setPaint ( ui.getSouthCornerFill () );
                                g2d.fill ( getDropdownCornerShape ( popupMenu, menuSize, true ) );
                            }
                        }
                    }
                }
                else if ( component instanceof JMenuItem )
                {
                    // Filling corner if selected menu item is placed nearby
                    final JMenuItem menuItem = ( JMenuItem ) component;
                    if ( menuItem.isEnabled () && ( menuItem.getModel ().isArmed () || menuItem.isSelected () ) )
                    {
                        // Filling corner properly
                        if ( menuItem.getUI () instanceof WebMenuUI )
                        {
                            // Filling corner according to WebMenu styling
                            final WebMenuUI ui = ( WebMenuUI ) menuItem.getUI ();
                            g2d.setPaint ( top ? ui.getNorthCornerFill () : ui.getSouthCornerFill () );
                            g2d.fill ( getDropdownCornerShape ( popupMenu, menuSize, true ) );
                        }
                        else if ( menuItem.getUI () instanceof WebMenuItemUI )
                        {
                            // Filling corner according to WebMenuItem styling
                            final WebMenuItemUI ui = ( WebMenuItemUI ) menuItem.getUI ();
                            g2d.setPaint ( top ? ui.getNorthCornerFill () : ui.getSouthCornerFill () );
                            g2d.fill ( getDropdownCornerShape ( popupMenu, menuSize, true ) );
                        }
                    }
                }
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Point preparePopupMenu ( final E popupMenu, final Component invoker, int x, int y )
    {
        // Updating popup location according to popup menu UI settings
        if ( invoker != null )
        {
            // Default corner position according to invoker's orientation
            final boolean ltr = invoker.getComponentOrientation ().isLeftToRight ();
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
                        x += ( los.x <= x ? -1 : 1 ) * ( transparent ? sideWidth - menuSpacing : -menuSpacing );
                        y += ( los.y <= y ? -1 : 1 ) * ( transparent ? sideWidth + 1 + round : round );
                    }
                }
                else if ( !WebPopupMenuStyle.dropdownStyleForMenuBar )
                {
                    // Displaying simple-styled top-level menu
                    // It is displayed below or above the menu bar
                    setPopupStyle ( PopupStyle.simple );
                    if ( fixLocation )
                    {
                        // Invoker X-location on screen also works as orientation indicator, so we don't need to check it here
                        x += ( los.x <= x ? -1 : 1 ) * ( transparent ? sideWidth - menuSpacing : -menuSpacing );
                        y -= transparent ? sideWidth + round + 1 : round;
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
                        x += ( los.x <= x ? -1 : 1 ) * ( transparent ? sideWidth : 0 );
                        y += ( los.y <= y ? -1 : 1 ) * ( transparent ? sideWidth - cornerWidth : 0 );
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
                        x += transparent ? -sideWidth : 0;
                        if ( cornerSide == TOP )
                        {
                            y -= transparent ? sideWidth - ( dropdown ? cornerWidth : 0 ) : 0;
                        }
                        else
                        {
                            // Invoker preferred size is required instead of actual height
                            // This is because the original position takes it into account instead of height
                            final int ih = invoker.getPreferredSize ().height;
                            y -= ih + ( transparent ? sideWidth - ( dropdown ? cornerWidth : 0 ) : 0 );
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
                                    x = ( ltr ? los.x : los.x + is.width - ps.width ) + ( transparent ? ltr ? -sideWidth : sideWidth : 0 );
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
                                    x = ( ltr ? los.x + is.width - ps.width : los.x ) + ( transparent ? ltr ? sideWidth : -sideWidth : 0 );
                                    y = los.y - ps.height + cornerShear;
                                    relativeCorner = ltr ? Integer.MAX_VALUE : 0;
                                    break;
                                }
                                case belowStart:
                                {
                                    x = ( ltr ? los.x : los.x + is.width - ps.width ) + ( transparent ? ltr ? -sideWidth : sideWidth : 0 );
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
                                    x = ( ltr ? los.x + is.width - ps.width : los.x ) + ( transparent ? ltr ? sideWidth : -sideWidth : 0 );
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

        return new Point ( x, y );
    }
}