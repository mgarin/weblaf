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

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.PainterSupport;
import com.alee.laf.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.utils.*;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.ninepatch.NinePatchIcon;
import com.alee.utils.swing.BorderMethods;

import javax.swing.*;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Custom UI for JPopupMenu component.
 *
 * @author Mikle Garin
 */

public class WebPopupMenuUI extends BasicPopupMenuUI implements SwingConstants, ShapeProvider, BorderMethods
{
    /**
     * todo 1. Left/Right corner
     * todo 2. Proper painter styling
     * todo 3. When using popupMenuWay take invoker shade into account (if its UI has one -> ShadeProvider interface)
     */

    /**
     * Style settings.
     */
    protected PopupMenuStyle popupMenuStyle = WebPopupMenuStyle.popupMenuStyle;
    protected Color borderColor = WebPopupMenuStyle.borderColor;
    protected int round = WebPopupMenuStyle.round;
    protected int shadeWidth = WebPopupMenuStyle.shadeWidth;
    protected float shadeOpacity = WebPopupMenuStyle.shadeOpacity;
    protected int cornerWidth = WebPopupMenuStyle.cornerWidth;
    protected Insets margin = WebPopupMenuStyle.margin;
    protected Painter painter = WebPopupMenuStyle.painter;
    protected int menuSpacing = WebPopupMenuStyle.menuSpacing;
    protected boolean fixLocation = WebPopupMenuStyle.fixLocation;

    /**
     * Menu listeners.
     */
    protected PropertyChangeListener orientationChangeListener;
    protected PropertyChangeListener visibilityChangeListener;
    protected PropertyChangeListener jdkSevenFixListener;

    /**
     * Runtime variables.
     */
    protected boolean transparent = false;
    protected int relativeCorner = 0;
    protected int cornerSide = NORTH;
    protected PopupMenuWay popupMenuWay = null;

    /**
     * Returns an instance of the WebPopupMenuUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebPopupMenuUI
     */
    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebPopupMenuUI ();
    }

    /**
     * Installs UI in the specified component.
     *
     * @param c component for this UI
     */
    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        transparent = ProprietaryUtils.isWindowTransparencyAllowed ();

        // Default settings
        SwingUtils.setOrientation ( popupMenu );
        popupMenu.setOpaque ( false );
        popupMenu.setBackground ( WebPopupMenuStyle.backgroundColor );
        PainterSupport.installPainter ( popupMenu, this.painter );
        updateBorder ();

        // Popup orientation change listener
        orientationChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( PropertyChangeEvent evt )
            {
                popupMenu.setVisible ( false );
            }
        };
        popupMenu.addPropertyChangeListener ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY, orientationChangeListener );

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
                        // Update menu style
                        final Component invoker = popupMenu.getInvoker ();
                        if ( invoker != null && invoker instanceof JMenu && invoker.getParent () instanceof JPopupMenu )
                        {
                            setPopupMenuStyle ( PopupMenuStyle.simple );
                        }

                        ancestor = SwingUtils.getWindowAncestor ( popupMenu );
                        if ( ancestor instanceof JWindow )
                        {
                            // Workaround to remove Mac OS X shade around the menu window
                            ( ( JWindow ) ancestor ).getRootPane ().putClientProperty ( "Window.shadow", Boolean.FALSE );
                        }
                        if ( ancestor != null && ancestor.getClass ().getCanonicalName ().endsWith ( "HeavyWeightWindow" ) )
                        {
                            // Updating menu opacity state
                            ProprietaryUtils.setWindowOpaque ( ancestor, false );
                        }
                    }
                    else
                    {
                        if ( ancestor != null && ancestor.getClass ().getCanonicalName ().endsWith ( "HeavyWeightWindow" ) )
                        {
                            // Restoring menu opacity state
                            ProprietaryUtils.setWindowOpaque ( ancestor, true );
                        }
                    }
                }
            };
            popupMenu.addPropertyChangeListener ( WebLookAndFeel.VISIBLE_PROPERTY, visibilityChangeListener );
        }
        else
        {
            // Workaround for menu with non-opaque parent window
            if ( SystemUtils.isJava7orAbove () )
            {
                jdkSevenFixListener = new PropertyChangeListener ()
                {
                    @Override
                    public void propertyChange ( final PropertyChangeEvent evt )
                    {
                        if ( evt.getNewValue () == Boolean.TRUE )
                        {
                            final Window ancestor = SwingUtils.getWindowAncestor ( popupMenu );
                            if ( ancestor != null && ancestor.getClass ().getCanonicalName ().endsWith ( "HeavyWeightWindow" ) )
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
                popupMenu.addPropertyChangeListener ( WebLookAndFeel.VISIBLE_PROPERTY, jdkSevenFixListener );
            }
        }
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling painter
        PainterSupport.uninstallPainter ( popupMenu, this.painter );

        // Removing listeners
        popupMenu.removePropertyChangeListener ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY, orientationChangeListener );
        if ( transparent )
        {
            popupMenu.removePropertyChangeListener ( WebLookAndFeel.VISIBLE_PROPERTY, visibilityChangeListener );
            visibilityChangeListener = null;
        }
        else
        {
            // Workaround for menu with non-opaque parent window
            if ( SystemUtils.isJava7orAbove () )
            {
                popupMenu.removePropertyChangeListener ( WebLookAndFeel.VISIBLE_PROPERTY, jdkSevenFixListener );
                jdkSevenFixListener = null;
            }
        }

        super.uninstallUI ( c );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBorder ()
    {
        if ( popupMenu != null )
        {
            // Actual margin
            final boolean ltr = popupMenu.getComponentOrientation ().isLeftToRight ();
            final Insets m = new Insets ( margin.top, ltr ? margin.left : margin.right, margin.bottom, ltr ? margin.right : margin.left );

            // Calculating additional borders
            if ( painter != null )
            {
                // Painter borders
                final Insets pi = painter.getMargin ( popupMenu );
                m.top += pi.top;
                m.left += ltr ? pi.left : pi.right;
                m.bottom += pi.bottom;
                m.right += ltr ? pi.right : pi.left;
            }
            else
            {
                if ( transparent )
                {
                    m.top += shadeWidth + 1 + round;
                    m.left += shadeWidth + 1;
                    m.bottom += shadeWidth + 1 + round;
                    m.right += shadeWidth + 1;
                }
                else
                {
                    m.top += 1 + round;
                    m.left += 1;
                    m.bottom += 1 + round;
                    m.right += 1;
                }
            }

            // Installing border
            popupMenu.setBorder ( LafUtils.createWebBorder ( m ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape provideShape ()
    {
        return LafUtils.getWebBorderShape ( popupMenu, 0, StyleConstants.smallRound );
    }

    /**
     * Returns popup menu style.
     *
     * @return popup menu style
     */
    public PopupMenuStyle getPopupMenuStyle ()
    {
        return popupMenuStyle;
    }

    /**
     * Sets popup menu style.
     *
     * @param style new popup menu style
     */
    public void setPopupMenuStyle ( final PopupMenuStyle style )
    {
        this.popupMenuStyle = style;
        updateBorder ();
    }

    /**
     * Returns popup menu border color.
     *
     * @return popup menu border color
     */
    public Color getBorderColor ()
    {
        return borderColor;
    }

    /**
     * Sets popup menu border color.
     *
     * @param color new popup menu border color
     */
    public void setBorderColor ( final Color color )
    {
        this.borderColor = color;
    }

    /**
     * Returns popup menu border corners rounding.
     *
     * @return popup menu border corners rounding
     */
    public int getRound ()
    {
        return round;
    }

    /**
     * Sets popup menu border corners rounding.
     *
     * @param round new popup menu border corners rounding
     */
    public void setRound ( final int round )
    {
        this.round = round;
        updateBorder ();
    }

    /**
     * Returns popup menu shade width.
     *
     * @return popup menu shade width
     */
    public int getShadeWidth ()
    {
        return shadeWidth;
    }

    /**
     * Sets popup menu shade width.
     *
     * @param width new popup menu shade width
     */
    public void setShadeWidth ( final int width )
    {
        this.shadeWidth = width;
        updateBorder ();
    }

    /**
     * Returns popup menu shade opacity.
     *
     * @return popup menu shade opacity
     */
    public float getShadeOpacity ()
    {
        return shadeOpacity;
    }

    /**
     * Sets popup menu shade opacity.
     *
     * @param opacity new popup menu shade opacity
     */
    public void setShadeOpacity ( final float opacity )
    {
        this.shadeOpacity = opacity;
    }

    /**
     * Returns popup menu dropdown style corner width.
     *
     * @return popup menu dropdown style corner width
     */
    public int getCornerWidth ()
    {
        return cornerWidth;
    }

    /**
     * Sets popup menu dropdown style corner width.
     *
     * @param width popup menu dropdown style corner width
     */
    public void setCornerWidth ( final int width )
    {
        this.cornerWidth = width;
    }

    /**
     * Returns popup menu content margin.
     *
     * @return popup menu content margin
     */
    public Insets getMargin ()
    {
        return margin;
    }

    /**
     * Sets popup menu content margin.
     *
     * @param margin new popup menu content margin
     */
    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
    }

    /**
     * Returns popup menu painter.
     *
     * @return popup menu painter
     */
    public Painter getPainter ()
    {
        return painter;
    }

    /**
     * Sets popup menu painter.
     *
     * @param painter new popup menu painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.uninstallPainter ( popupMenu, this.painter );
        this.painter = painter;
        PainterSupport.installPainter ( popupMenu, this.painter );
        updateBorder ();
    }

    /**
     * Returns spacing between menubar popup menus.
     *
     * @return spacing between menubar popup menus
     */
    public int getMenuSpacing ()
    {
        return menuSpacing;
    }

    /**
     * Sets spacing between menubar popup menus.
     *
     * @param spacing new spacing between menubar popup menus
     */
    public void setMenuSpacing ( final int spacing )
    {
        this.menuSpacing = spacing;
    }

    /**
     * Returns whether popup menu should try to fix its initial location when displayed or not.
     *
     * @return true if popup menu should try to fix its initial location when displayed, false otherwise
     */
    public boolean isFixLocation ()
    {
        return fixLocation;
    }

    /**
     * Sets whether popup menu should try to fix its initial location when displayed or not.
     *
     * @param fixLocation whether popup menu should try to fix its initial location when displayed or not
     */
    public void setFixLocation ( final boolean fixLocation )
    {
        this.fixLocation = fixLocation;
    }

    /**
     * Assists popup menu to allow it choose the best position relative to invoker.
     * Its value nullified right after first usage to avoid popup menu display issues in future.
     *
     * @param way approximate popup menu display way
     */
    public void setPopupMenuWay ( final PopupMenuWay way )
    {
        this.popupMenuWay = way;
    }

    /**
     * Returns side where corner is currently displayed.
     *
     * @return side where corner is currently displayed
     */
    public int getCornerSide ()
    {
        return cornerSide;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Popup getPopup ( final JPopupMenu popup, int x, int y )
    {
        // Updating menu elements
        // Be aware because at this point menu size changes
        // todo Use static style so that i don't need to update menu elements from here
        for ( int i = 0; i < popup.getComponentCount (); i++ )
        {
            final Component component = popup.getComponent ( i );
            if ( component instanceof JMenu )
            {
                final ButtonUI ui = ( ( JMenu ) component ).getUI ();
                if ( ui instanceof WebMenuUI )
                {
                    ( ( WebMenuUI ) ui ).updateBorder ();
                }
            }
            else if ( component instanceof JMenuItem )
            {
                final ButtonUI ui = ( ( JMenuItem ) component ).getUI ();
                if ( ui instanceof WebMenuItemUI )
                {
                    ( ( WebMenuItemUI ) ui ).updateBorder ();
                }
            }
        }

        // Position calculations constants
        final Component invoker = popup.getInvoker ();
        final Point los = invoker.isShowing () ? invoker.getLocationOnScreen () : null;
        final boolean fixLocation = this.fixLocation && invoker.isShowing ();
        final boolean ltr = invoker.getComponentOrientation ().isLeftToRight ();

        // Default corner position
        relativeCorner = ltr ? 0 : Integer.MAX_VALUE;

        // Updating popup location according to popup menu UI settings
        if ( invoker != null )
        {
            if ( invoker instanceof JMenu )
            {
                if ( invoker.getParent () instanceof JPopupMenu )
                {
                    // Displaying simple-styled sub-menu
                    // It is displayed to left or right of the menu item
                    setPopupMenuStyle ( PopupMenuStyle.simple );
                    if ( fixLocation )
                    {
                        // Invoker X-location on screen also works as orientation indicator, so we don't need to check it here
                        x += ( los.x <= x ? -1 : 1 ) * ( transparent ? shadeWidth - menuSpacing : -menuSpacing );
                        y += ( los.y <= y ? -1 : 1 ) * ( transparent ? shadeWidth + 1 + round : round );
                    }
                }
                else if ( !WebPopupMenuStyle.dropdownStyleForMenuBar )
                {
                    // Displaying simple-styled top-level menu
                    // It is displayed below or above the menu bar
                    setPopupMenuStyle ( PopupMenuStyle.simple );
                    if ( fixLocation )
                    {
                        // Invoker X-location on screen also works as orientation indicator, so we don't need to check it here
                        x += ( los.x <= x ? -1 : 1 ) * ( transparent ? shadeWidth - menuSpacing : -menuSpacing );
                        y -= transparent ? shadeWidth + round + 1 : round;
                    }
                }
                else
                {
                    // Displaying dropdown-styled top-level menu
                    // It is displayed below or above the menu bar
                    setPopupMenuStyle ( PopupMenuStyle.dropdown );
                    cornerSide = los.y <= y ? NORTH : SOUTH;
                    if ( fixLocation )
                    {
                        // Invoker X-location on screen also works as orientation indicator, so we don't need to check it here
                        x += ( los.x <= x ? -1 : 1 ) * ( transparent ? shadeWidth : 0 );
                        y += ( los.y <= y ? -1 : 1 ) * ( transparent ? shadeWidth - cornerWidth : 0 );
                    }
                    relativeCorner = los.x + invoker.getWidth () / 2 - x;
                }
            }
            else
            {
                final boolean dropdown = popupMenuStyle == PopupMenuStyle.dropdown;
                if ( invoker instanceof JComboBox && popup.getName ().equals ( "ComboPopup.popup" ) )
                {
                    cornerSide = los.y <= y ? NORTH : SOUTH;
                    if ( fixLocation )
                    {
                        x += transparent ? -shadeWidth : 0;
                        if ( cornerSide == NORTH )
                        {
                            y -= transparent ? ( shadeWidth - ( dropdown ? cornerWidth : 0 ) ) : 0;
                        }
                        else
                        {
                            y -= invoker.getHeight () + ( transparent ? ( shadeWidth - ( dropdown ? cornerWidth : 0 ) ) : 0 );
                        }
                    }
                    relativeCorner = los.x + invoker.getWidth () / 2 - x;
                }
                else
                {
                    if ( fixLocation )
                    {
                        // Applying new location according to specified popup menu way
                        if ( popupMenuWay != null )
                        {
                            final Dimension ps = popupMenu.getPreferredSize ();
                            final Dimension is = invoker.getSize ();
                            final int cornerShear = dropdown ? shadeWidth - cornerWidth : 0;
                            switch ( popupMenuWay )
                            {
                                case aboveStart:
                                {
                                    x = ( ltr ? los.x : los.x + is.width - ps.width ) +
                                            ( transparent ? ( ltr ? -shadeWidth : shadeWidth ) : 0 );
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
                                    x = ( ltr ? los.x + is.width - ps.width : los.x ) +
                                            ( transparent ? ( ltr ? shadeWidth : -shadeWidth ) : 0 );
                                    y = los.y - ps.height + cornerShear;
                                    relativeCorner = ltr ? Integer.MAX_VALUE : 0;
                                    break;
                                }
                                case belowStart:
                                {
                                    x = ( ltr ? los.x : los.x + is.width - ps.width ) +
                                            ( transparent ? ( ltr ? -shadeWidth : shadeWidth ) : 0 );
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
                                    x = ( ltr ? los.x + is.width - ps.width : los.x ) +
                                            ( transparent ? ( ltr ? shadeWidth : -shadeWidth ) : 0 );
                                    y = los.y + is.height - cornerShear;
                                    relativeCorner = ltr ? Integer.MAX_VALUE : 0;
                                    break;
                                }
                            }
                            cornerSide = popupMenuWay.getCornerSide ();
                            popupMenuWay = null;
                        }
                    }
                }
            }
        }

        return super.getPopup ( popup, x, y );
    }

    /**
     * Paints popup menu decoration.
     *
     * @param g graphics context
     * @param c popup menu component
     */
    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        final Graphics2D g2d = ( Graphics2D ) g;
        final Object aa = LafUtils.setupAntialias ( g2d );

        if ( transparent )
        {
            final Rectangle mb = SwingUtils.getBoundsOnScreen ( popupMenu );

            // Shade
            final NinePatchIcon shade = NinePatchUtils.getShadeIcon ( shadeWidth, round * 2, shadeOpacity );
            shade.setComponent ( popupMenu );
            shade.paintIcon ( g2d, getShadeBounds ( mb ) );

            // Menu direction from component: -> down -> right
            final Shape border = getBorderShape ( mb, false );
            final Shape fill = getBorderShape ( mb, true );

            // Background
            g2d.setColor ( popupMenu.getBackground () );
            g2d.fill ( fill );

            // Corner fill
            fillDropdownCorner ( g2d, mb );

            // Border
            g2d.setPaint ( borderColor );
            g2d.draw ( border );
        }
        else
        {
            // Background
            g2d.setColor ( popupMenu.getBackground () );
            g2d.fillRoundRect ( 1, 1, popupMenu.getWidth () - 2, popupMenu.getHeight () - 2, round * 2, round * 2 );

            // Border
            g2d.setColor ( borderColor );
            g2d.drawRoundRect ( 0, 0, popupMenu.getWidth () - 1, popupMenu.getHeight () - 1, round * 2, round * 2 );
        }

        LafUtils.restoreAntialias ( g2d, aa );
    }

    /**
     * Fills dropdown-styled popup menu corner if menu item near it is selected.
     *
     * @param g2d graphics context
     * @param mb  menu bounds on screen
     */
    protected void fillDropdownCorner ( Graphics2D g2d, Rectangle mb )
    {
        if ( popupMenuStyle == PopupMenuStyle.dropdown && round == 0 )
        {
            // Checking whether corner should be filled or not
            final boolean north = cornerSide == NORTH;
            final int zIndex = north ? 0 : popupMenu.getComponentCount () - 1;
            final Component component = popupMenu.getComponent ( zIndex );
            if ( component instanceof JMenuItem )
            {
                final JMenuItem menuItem = ( JMenuItem ) component;
                final ButtonModel model = menuItem.getModel ();
                if ( model.isArmed () || model.isSelected () )
                {
                    // Filling corner properly
                    if ( menuItem.getUI () instanceof WebMenuUI )
                    {
                        final WebMenuUI ui = ( WebMenuUI ) menuItem.getUI ();
                        g2d.setPaint ( north ? ui.getNorthCornerFill () : ui.getSouthCornerFill () );
                        g2d.fill ( createDropdownCornerShape ( mb, true ) );
                    }
                    else if ( menuItem.getUI () instanceof WebMenuItemUI )
                    {
                        final WebMenuItemUI ui = ( WebMenuItemUI ) menuItem.getUI ();
                        g2d.setPaint ( north ? ui.getNorthCornerFill () : ui.getSouthCornerFill () );
                        g2d.fill ( createDropdownCornerShape ( mb, true ) );
                    }
                }
            }
        }
    }

    /**
     * Returns popup menu shade bounds.
     *
     * @param mb menu bounds on screen
     * @return popup menu shade bounds
     */
    protected Rectangle getShadeBounds ( final Rectangle mb )
    {
        switch ( popupMenuStyle )
        {
            case simple:
            case dropdown:
            {
                return new Rectangle ( 0, 0, mb.width, mb.height );
            }
            default:
            {
                return null;
            }
        }
    }

    /**
     * Returns popup menu border shape.
     *
     * @param mb   menu bounds on screen
     * @param fill whether it is a fill shape or not
     * @return popup menu border shape
     */
    protected Shape getBorderShape ( final Rectangle mb, final boolean fill )
    {
        switch ( popupMenuStyle )
        {
            case simple:
            {
                return createSimpleShape ( mb, fill );
            }
            case dropdown:
            {
                return createDropdownShape ( mb, fill );
            }
            default:
            {
                return null;
            }
        }
    }

    /**
     * Creates and returns simple menu shape.
     *
     * @param mb   menu bounds on screen
     * @param fill whether it is a fill shape or not
     * @return simple menu shape
     */
    protected GeneralPath createSimpleShape ( final Rectangle mb, final boolean fill )
    {
        final int shear = fill ? 1 : 0;
        final GeneralPath shape = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        final int top = shadeWidth + shear;
        final int left = shadeWidth + shear;
        final int bottom = mb.height - 1 - shadeWidth;
        final int right = mb.width - 1 - shadeWidth;
        shape.moveTo ( left, top + round );
        shape.quadTo ( left, top, left + round, top );
        shape.lineTo ( right - round, top );
        shape.quadTo ( right, top, right, top + round );
        shape.lineTo ( right, bottom - round );
        shape.quadTo ( right, bottom, right - round, bottom );
        shape.lineTo ( left + round, bottom );
        shape.quadTo ( left, bottom, left, bottom - round );
        shape.closePath ();
        return shape;
    }

    /**
     * Creates and returns dropdown style shape.
     *
     * @param mb   menu bounds on screen
     * @param fill whether it is a fill shape or not
     * @return dropdown style shape
     */
    protected GeneralPath createDropdownShape ( final Rectangle mb, final boolean fill )
    {
        final boolean north = cornerSide == NORTH;
        final boolean south = cornerSide == SOUTH;

        // Painting shear
        final int shear = fill ? 1 : 0;

        // Corner left spacing
        final int ds = shadeWidth + shear + round + cornerWidth * 2;
        final int spacing = relativeCorner < shadeWidth + round + cornerWidth ? 0 : Math.min ( relativeCorner - ds, mb.width - ds * 2 );

        // Side spacings
        final int top = shadeWidth + shear;
        final int left = shadeWidth + shear;
        final int botom = mb.height - 1 - shadeWidth;
        final int right = mb.width - 1 - shadeWidth;

        final GeneralPath shape = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        shape.moveTo ( left, top + round );
        shape.quadTo ( left, top, left + round, top );
        if ( north )
        {
            // Top corner
            shape.lineTo ( left + round + spacing + cornerWidth, top );
            shape.lineTo ( left + round + spacing + cornerWidth * 2, top - cornerWidth );
            shape.lineTo ( left + round + spacing + cornerWidth * 2 + 1, top - cornerWidth );
            shape.lineTo ( left + round + spacing + cornerWidth * 3 + 1, top );
        }
        shape.lineTo ( right - round, top );
        shape.quadTo ( right, top, right, top + round );
        shape.lineTo ( right, botom - round );
        shape.quadTo ( right, botom, right - round, botom );
        if ( south )
        {
            // Bottom corner
            shape.lineTo ( left + round + spacing + cornerWidth * 3 + 1, botom );
            shape.lineTo ( left + round + spacing + cornerWidth * 2 + 1, botom + cornerWidth );
            shape.lineTo ( left + round + spacing + cornerWidth * 2, botom + cornerWidth );
            shape.lineTo ( left + round + spacing + cornerWidth, botom );
        }
        shape.lineTo ( left + round, botom );
        shape.quadTo ( left, botom, shadeWidth, botom - round );
        shape.closePath ();
        return shape;
    }

    /**
     * Creates and returns dropdown style corner shape.
     * It is used to paint corner fill when menu item at the same as corner side of popup menu is selected.
     *
     * @param mb   menu bounds on screen
     * @param fill whether it is a fill shape or not
     * @return dropdown style corner shape
     */
    protected GeneralPath createDropdownCornerShape ( final Rectangle mb, final boolean fill )
    {
        final boolean north = cornerSide == NORTH;
        final boolean south = cornerSide == SOUTH;

        // Painting shear
        final int shear = fill ? 1 : 0;

        // Corner left spacing
        final int ds = shadeWidth + shear + round + cornerWidth * 2;
        final int spacing = relativeCorner < shadeWidth + round + cornerWidth ? 0 : Math.min ( relativeCorner - ds, mb.width - ds * 2 );

        // Side spacings
        final int top = shadeWidth + shear;
        final int left = shadeWidth + shear;
        final int botom = mb.height - 1 - shadeWidth;

        final GeneralPath shape = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        if ( north )
        {
            // Top corner
            shape.moveTo ( left + round + spacing + cornerWidth, top );
            shape.lineTo ( left + round + spacing + cornerWidth * 2, top - cornerWidth );
            shape.lineTo ( left + round + spacing + cornerWidth * 2 + 1, top - cornerWidth );
            shape.lineTo ( left + round + spacing + cornerWidth * 3 + 1, top );
            shape.closePath ();
        }
        if ( south )
        {
            // Bottom corner
            shape.moveTo ( left + round + spacing + cornerWidth * 3 + 1, botom );
            shape.lineTo ( left + round + spacing + cornerWidth * 2 + 1, botom + cornerWidth );
            shape.lineTo ( left + round + spacing + cornerWidth * 2, botom + cornerWidth );
            shape.lineTo ( left + round + spacing + cornerWidth, botom );
            shape.closePath ();
        }
        return shape;
    }
}