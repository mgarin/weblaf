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

import com.alee.extended.painter.PainterSupport;
import com.alee.laf.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.utils.LafUtils;
import com.alee.utils.ProprietaryUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.SystemUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.swing.BorderMethods;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import java.awt.*;
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
     * todo 2. When using popupMenuWay take invoker shade into account (if its UI has one -> ShadeProvider interface)
     */

    /**
     * Painter style settings.
     */
    protected PopupPainterStyle popupPainterStyle = WebPopupMenuStyle.popupPainterStyle;
    protected Color borderColor = WebPopupMenuStyle.borderColor;
    protected int round = WebPopupMenuStyle.round;
    protected int shadeWidth = WebPopupMenuStyle.shadeWidth;
    protected float shadeOpacity = WebPopupMenuStyle.shadeOpacity;
    protected int cornerWidth = WebPopupMenuStyle.cornerWidth;
    protected float transparency = WebPopupMenuStyle.transparency;

    /**
     * Component style settings.
     */
    protected Insets margin = WebPopupMenuStyle.margin;
    protected PopupMenuPainter painter = WebPopupMenuStyle.painter;
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
    protected int cornerSide = NORTH;
    protected int relativeCorner = 0;
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
        LookAndFeel.installProperty ( popupMenu, WebLookAndFeel.OPAQUE_PROPERTY, Boolean.FALSE );
        popupMenu.setBackground ( WebPopupMenuStyle.backgroundColor );

        // Initializing default painer
        setPainter ( new PopupMenuPainter () );

        // Popup orientation change listener
        orientationChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                popupMenu.setVisible ( false );
            }
        };
        popupMenu.addPropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, orientationChangeListener );

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
                            setPopupPainterStyle ( PopupPainterStyle.simple );
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
        setPainter ( null );

        // Removing listeners
        popupMenu.removePropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, orientationChangeListener );
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
            // Preserve old borders
            if ( SwingUtils.isPreserveBorders ( popupMenu ) )
            {
                return;
            }

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
    public PopupPainterStyle getPopupPainterStyle ()
    {
        return popupPainterStyle;
    }

    /**
     * Sets popup menu style.
     *
     * @param style new popup menu style
     */
    public void setPopupPainterStyle ( final PopupPainterStyle style )
    {
        this.popupPainterStyle = style;
        if ( painter != null )
        {
            painter.setPopupPainterStyle ( style );
        }
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
        if ( painter != null )
        {
            painter.setBorderColor ( color );
        }
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
        if ( painter != null )
        {
            painter.setRound ( round );
        }
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
        if ( painter != null )
        {
            painter.setShadeWidth ( width );
        }
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
        if ( painter != null )
        {
            painter.setShadeOpacity ( opacity );
        }
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
        if ( painter != null )
        {
            painter.setCornerWidth ( width );
        }
    }

    /**
     * Returns popup menu background transparency.
     *
     * @return popup menu background transparency
     */
    public float getTransparency ()
    {
        return transparency;
    }

    /**
     * Sets popup menu background transparency.
     *
     * @param transparency popup menu background transparency
     */
    public void setTransparency ( final float transparency )
    {
        this.transparency = transparency;
        if ( painter != null )
        {
            painter.setTransparency ( transparency );
        }
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
        updateBorder ();
    }

    /**
     * Returns popup menu painter.
     *
     * @return popup menu painter
     */
    public PopupMenuPainter getPainter ()
    {
        return painter;
    }

    /**
     * Sets popup menu painter.
     * Pass null to remove popup menu painter.
     *
     * @param painter new popup menu painter
     */
    public void setPainter ( final PopupMenuPainter painter )
    {
        PainterSupport.uninstallPainter ( popupMenu, this.painter );
        this.painter = painter;
        applyPainterSettings ( painter );
        PainterSupport.installPainter ( popupMenu, this.painter );
    }

    /**
     * Applies UI settings to this specific painter.
     *
     * @param painter popup menu painter
     */
    protected void applyPainterSettings ( final PopupMenuPainter painter )
    {
        if ( painter != null )
        {
            // Style settings
            painter.setPopupPainterStyle ( popupPainterStyle );
            painter.setBorderColor ( borderColor );
            painter.setRound ( round );
            painter.setShadeWidth ( shadeWidth );
            painter.setShadeOpacity ( shadeOpacity );
            painter.setCornerWidth ( cornerWidth );
            painter.setTransparency ( transparency );

            // Runtime variables
            painter.setTransparent ( transparent );
            painter.setCornerSide ( cornerSide );
            painter.setRelativeCorner ( relativeCorner );
        }
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
                    setPopupPainterStyle ( PopupPainterStyle.simple );
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
                    setPopupPainterStyle ( PopupPainterStyle.simple );
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
                    setPopupPainterStyle ( PopupPainterStyle.dropdown );
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
                final boolean dropdown = popupPainterStyle == PopupPainterStyle.dropdown;
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

        if ( painter != null )
        {
            painter.setCornerSide ( cornerSide );
            painter.setRelativeCorner ( relativeCorner );
        }

        return super.getPopup ( popup, x, y );
    }

    /**
     * Paints popup menu decorations.
     * The whole painting process is delegated to installed painter class.
     *
     * @param g graphics context
     * @param c popup menu component
     */
    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, SwingUtils.size ( popupMenu ), popupMenu );
        }
    }
}