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
import com.alee.global.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.StyleManager;
import com.alee.managers.style.skin.web.PopupStyle;
import com.alee.managers.style.skin.web.WebPopupPainter;
import com.alee.utils.*;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.laf.Styleable;
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

public class WebPopupMenuUI extends BasicPopupMenuUI implements SwingConstants, Styleable, ShapeProvider, BorderMethods
{
    /**
     * UI style settings.
     */
    protected Insets margin = WebPopupMenuStyle.margin;
    protected int menuSpacing = WebPopupMenuStyle.menuSpacing;
    protected boolean fixLocation = WebPopupMenuStyle.fixLocation;

    /**
     * Component painter.
     */
    protected PopupMenuPainter painter;

    /**
     * Menu listeners.
     */
    protected PropertyChangeListener popupMenuTypeUpdater;
    protected PropertyChangeListener orientationChangeListener;
    protected PropertyChangeListener visibilityChangeListener;
    protected PropertyChangeListener jdkSevenFixListener;

    /**
     * Runtime variables.
     */
    protected String styleId = null;
    protected boolean transparent = false;
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

        // Default settings
        transparent = ProprietaryUtils.isWindowTransparencyAllowed () || ProprietaryUtils.isWindowShapeAllowed ();
        SwingUtils.setOrientation ( popupMenu );
        SwingUtils.setHandlesEnableStateMark ( popupMenu );

        // Applying skin
        StyleManager.applySkin ( popupMenu );

        // Popup menu type updater
        popupMenuTypeUpdater = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                if ( evt.getNewValue () == Boolean.TRUE )
                {
                    // Update menu style
                    final Component invoker = popupMenu.getInvoker ();
                    if ( invoker != null )
                    {
                        if ( invoker instanceof JMenu )
                        {
                            if ( invoker.getParent () instanceof JPopupMenu )
                            {
                                painter.setPopupMenuType ( PopupMenuType.menuBarSubMenu );
                            }
                            else
                            {
                                painter.setPopupMenuType ( PopupMenuType.menuBarMenu );
                            }
                        }
                        else if ( invoker instanceof JComboBox )
                        {
                            painter.setPopupMenuType ( PopupMenuType.comboBoxMenu );
                        }
                        else
                        {
                            painter.setPopupMenuType ( PopupMenuType.customPopupMenu );
                        }
                    }
                    else
                    {
                        painter.setPopupMenuType ( PopupMenuType.customPopupMenu );
                    }
                }
            }
        };
        popupMenu.addPropertyChangeListener ( WebLookAndFeel.VISIBLE_PROPERTY, popupMenuTypeUpdater );

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
                @Override
                public void propertyChange ( final PropertyChangeEvent evt )
                {
                    if ( evt.getNewValue () == Boolean.FALSE )
                    {
                        // Restoring menu opacity state in case menu is in a separate heavy-weight window
                        Window ancestor = SwingUtils.getWindowAncestor ( popupMenu );
                        if ( SwingUtils.isHeavyWeightWindow ( ancestor ) )
                        {
                            ProprietaryUtils.setWindowOpaque ( ancestor, true );
                            ProprietaryUtils.setWindowShape ( ancestor, null );
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
        // Removing listeners
        popupMenu.removePropertyChangeListener ( WebLookAndFeel.VISIBLE_PROPERTY, popupMenuTypeUpdater );
        popupMenuTypeUpdater = null;
        popupMenu.removePropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, orientationChangeListener );
        orientationChangeListener = null;
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

        // Uninstalling applied skin
        StyleManager.removeSkin ( popupMenu );

        super.uninstallUI ( c );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleId ()
    {
        return styleId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyleId ( final String id )
    {
        this.styleId = id;
        StyleManager.applySkin ( popupMenu );
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
     * {@inheritDoc}
     */
    @Override
    public void updateBorder ()
    {
        LafUtils.updateBorder ( popupMenu, margin, painter );
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
        if ( painter != null )
        {
            painter.setMenuSpacing ( spacing );
        }
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
     * @param fix whether popup menu should try to fix its initial location when displayed or not
     */
    public void setFixLocation ( final boolean fix )
    {
        this.fixLocation = fix;
        if ( painter != null )
        {
            painter.setFixLocation ( fix );
        }
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
        if ( painter != null )
        {
            painter.setPopupMenuWay ( way );
        }
    }

    /**
     * Returns popup style.
     *
     * @return popup style
     */
    public PopupStyle getPopupStyle ()
    {
        final PopupStyle popupStyle = StyleManager.getPainterPropertyValue ( popupMenu, "popupStyle" );
        return popupStyle != null ? popupStyle : WebPopupMenuStyle.popupStyle;
    }

    /**
     * Sets popup style.
     *
     * @param style new popup style
     */
    public void setPopupStyle ( final PopupStyle style )
    {
        StyleManager.setCustomPainterProperty ( popupMenu, "popupStyle", style );
    }

    /**
     * Returns popup border color.
     *
     * @return popup border color
     */
    public Color getBorderColor ()
    {
        final Color borderColor = StyleManager.getPainterPropertyValue ( popupMenu, "borderColor" );
        return borderColor != null ? borderColor : WebPopupMenuStyle.borderColor;
    }

    /**
     * Sets popup border color.
     *
     * @param color new popup border color
     */
    public void setBorderColor ( final Color color )
    {
        StyleManager.setCustomPainterProperty ( popupMenu, "borderColor", color );
    }

    /**
     * Returns decoration corners rounding.
     *
     * @return decoration corners rounding
     */
    public int getRound ()
    {
        final Integer round = StyleManager.getPainterPropertyValue ( popupMenu, "round" );
        return round != null ? round : WebPopupMenuStyle.round;
    }

    /**
     * Sets decoration corners rounding.
     *
     * @param round decoration corners rounding
     */
    public void setRound ( final int round )
    {
        StyleManager.setCustomPainterProperty ( popupMenu, "round", round );
    }

    /**
     * Returns decoration shade width.
     *
     * @return decoration shade width
     */
    public int getShadeWidth ()
    {
        final Integer shadeWidth = StyleManager.getPainterPropertyValue ( popupMenu, "shadeWidth" );
        return shadeWidth != null ? shadeWidth : WebPopupMenuStyle.shadeWidth;
    }

    /**
     * Sets decoration shade width.
     *
     * @param shadeWidth decoration shade width
     */
    public void setShadeWidth ( final int shadeWidth )
    {
        StyleManager.setCustomPainterProperty ( popupMenu, "shadeWidth", shadeWidth );
    }

    /**
     * Returns popup shade transparency.
     *
     * @return popup shade transparency
     */
    public float getShadeTransparency ()
    {
        final Float shadeTransparency = StyleManager.getPainterPropertyValue ( popupMenu, "shadeTransparency" );
        return shadeTransparency != null ? shadeTransparency : WebPopupMenuStyle.shadeTransparency;
    }

    /**
     * Sets popup shade transparency.
     *
     * @param opacity new popup shade transparency
     */
    public void setShadeTransparency ( final float opacity )
    {
        StyleManager.setCustomPainterProperty ( popupMenu, "shadeTransparency", opacity );
    }

    /**
     * Returns popup dropdown style corner width.
     *
     * @return popup dropdown style corner width
     */
    public int getCornerWidth ()
    {
        final Integer cornerWidth = StyleManager.getPainterPropertyValue ( popupMenu, "cornerWidth" );
        return cornerWidth != null ? cornerWidth : WebPopupMenuStyle.cornerWidth;
    }

    /**
     * Sets popup dropdown style corner width.
     *
     * @param width popup dropdown style corner width
     */
    public void setCornerWidth ( final int width )
    {
        StyleManager.setCustomPainterProperty ( popupMenu, "cornerWidth", width );
    }

    /**
     * Returns dropdown corner alignment.
     *
     * @return dropdown corner alignment
     */
    public int getCornerAlignment ()
    {
        final Integer cornerAlignment = StyleManager.getPainterPropertyValue ( popupMenu, "cornerAlignment" );
        return cornerAlignment != null ? cornerAlignment : WebPopupMenuStyle.cornerAlignment;
    }

    /**
     * Sets dropdown corner alignment.
     *
     * @param cornerAlignment dropdown corner alignment
     */
    public void setCornerAlignment ( final int cornerAlignment )
    {
        StyleManager.setCustomPainterProperty ( popupMenu, "cornerAlignment", cornerAlignment );
    }

    /**
     * Returns popup background transparency.
     *
     * @return popup background transparency
     */
    public float getTransparency ()
    {
        final Float transparency = StyleManager.getPainterPropertyValue ( popupMenu, "transparency" );
        return transparency != null ? transparency : WebPopupMenuStyle.transparency;
    }

    /**
     * Sets popup background transparency.
     *
     * @param transparency popup background transparency
     */
    public void setTransparency ( final float transparency )
    {
        StyleManager.setCustomPainterProperty ( popupMenu, "transparency", transparency );
    }

    /**
     * Returns popup menu painter.
     *
     * @return popup menu painter
     */
    public Painter getPainter ()
    {
        return LafUtils.getAdaptedPainter ( painter );
    }

    /**
     * Sets popup menu painter.
     * Pass null to remove popup menu painter.
     *
     * @param painter new popup menu painter
     */
    public void setPainter ( final Painter painter )
    {
        // Creating adaptive painter if required
        final PopupMenuPainter properPainter =
                LafUtils.getProperPainter ( painter, PopupMenuPainter.class, AdaptivePopupMenuPainter.class );

        // Properly updating painter
        PainterSupport.uninstallPainter ( popupMenu, this.painter );
        final Painter oldPainter = this.painter;
        this.painter = properPainter;
        applyPainterSettings ( properPainter );
        PainterSupport.installPainter ( popupMenu, properPainter );

        // Firing painter change event
        // This is made using reflection because required method is protected within Component class
        LafUtils.firePainterChanged ( popupMenu, oldPainter, properPainter );
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
            // UI settings
            painter.setMenuSpacing ( menuSpacing );
            painter.setFixLocation ( fixLocation );

            // Runtime variables
            painter.setTransparent ( transparent );
            painter.setPopupMenuWay ( popupMenuWay );
        }
    }

    /**
     * Returns the {@code Popup} that will be responsible for displaying the {@code JPopupMenu}.
     * Also does necessary modifications to popup coordinates in case they are actually required.
     *
     * @param popup JPopupMenu requesting Popup
     * @param x     screen x location Popup is to be shown at
     * @param y     screen y location Popup is to be shown at
     * @return Popup that will show the JPopupMenu
     */
    @Override
    public Popup getPopup ( final JPopupMenu popup, int x, int y )
    {
        if ( painter != null )
        {
            // Updating painter settings
            painter.setMenuSpacing ( menuSpacing );
            painter.setFixLocation ( fixLocation );
            painter.setPopupMenuWay ( popupMenuWay );

            // Preparing popup menu for display
            final Point fixed = painter.preparePopupMenu ( popup, popup.getInvoker (), x, y );
            if ( fixLocation )
            {
                x = fixed.x;
                y = fixed.y;
            }

            // Resetting preferred popup menu display way
            popupMenuWay = null;
        }

        final Popup p = super.getPopup ( popup, x, y );

        if ( transparent )
        {
            final Component host = ReflectUtils.callMethodSafely ( p, "getComponent" );
            if ( host instanceof Window )
            {
                final Window ancestor = ( Window ) host;

                // Workaround to remove Mac OS X shade around the menu window
                if ( ancestor instanceof JWindow && SystemUtils.isMac () )
                {
                  ( ( JWindow ) ancestor ).getRootPane ().putClientProperty ( "Window.shadow", Boolean.FALSE );
                }

                ProprietaryUtils.setWindowOpaque ( ancestor, false );

                if ( painter instanceof WebPopupPainter && !ProprietaryUtils.isWindowTransparencyAllowed () && ProprietaryUtils.isWindowShapeAllowed () )
                {
                  ancestor.pack ();
                  Rectangle bounds = ancestor.getBounds (); ++bounds.width; ++bounds.height;
                  Shape shape = ( ( WebPopupPainter ) painter ).provideShape ( popupMenu, bounds );
                  ProprietaryUtils.setWindowShape ( ancestor, shape );
                }
            }
        }

        return p;
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
            painter.paint ( ( Graphics2D ) g, SwingUtils.size ( c ), c );
        }
    }
}