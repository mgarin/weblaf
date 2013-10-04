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
import com.alee.extended.window.TestFrame;
import com.alee.laf.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.colorchooser.WebColorChooserPanel;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.managers.hotkey.HotkeyRunnable;
import com.alee.managers.language.LanguageManager;
import com.alee.utils.*;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.ninepatch.NinePatchIcon;

import javax.swing.*;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.GeneralPath;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Custom UI for JPopupMenu component.
 *
 * @author Mikle Garin
 */

public class WebPopupMenuUI extends BasicPopupMenuUI implements ShapeProvider
{
    /**
     * Style settings.
     */
    protected PopupMenuStyle popupMenuStyle = WebPopupMenuStyle.popupMenuStyle;
    protected Color backgroundColor = WebPopupMenuStyle.backgroundColor;
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
    protected PropertyChangeListener visibilityChangeListener;
    protected PropertyChangeListener jdkSevenFixListener;

    /**
     * Runtime variables.
     */
    protected boolean transparent = false;
    protected int relativeCorner = 0;
    protected int cornerSide = SwingConstants.NORTH;

    /**
     * Returns an instance of the WebPopupMenuUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebPopupMenuUI
     */
    @SuppressWarnings ( "UnusedParameters" )
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
        popupMenu.setBackground ( Color.WHITE );
        updateBorder ();

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

                        // Update menu opacity state
                        ancestor = SwingUtils.getWindowAncestor ( popupMenu );
                        if ( ancestor != null && ancestor.getClass ().getCanonicalName ().endsWith ( "HeavyWeightWindow" ) )
                        {
                            ProprietaryUtils.setWindowOpaque ( ancestor, false );
                        }
                    }
                    else
                    {
                        // Update menu opacity state
                        if ( ancestor != null && ancestor.getClass ().getCanonicalName ().endsWith ( "HeavyWeightWindow" ) )
                        {
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
        if ( transparent )
        {
            popupMenu.removePropertyChangeListener ( WebLookAndFeel.VISIBLE_PROPERTY, visibilityChangeListener );
        }
        else
        {
            // Workaround for menu with non-opaque parent window
            if ( SystemUtils.isJava7orAbove () )
            {
                popupMenu.removePropertyChangeListener ( WebLookAndFeel.VISIBLE_PROPERTY, jdkSevenFixListener );
            }
        }

        super.uninstallUI ( c );
    }

    /**
     * Updates custom UI border.
     */
    protected void updateBorder ()
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
                    if ( popupMenuStyle == PopupMenuStyle.simple )
                    {
                        m.top += shadeWidth + 1 + round;
                        m.left += shadeWidth + 1;
                        m.bottom += shadeWidth + 1 + round;
                        m.right += shadeWidth + 1;
                    }
                    else
                    {
                        // todo left/bottom/right corner
                        m.top += ( cornerSide == SwingConstants.NORTH ? cornerWidth : shadeWidth ) + 1 + round;
                        m.left += shadeWidth + 1;
                        m.bottom += ( cornerSide == SwingConstants.SOUTH ? cornerWidth : shadeWidth ) + 1 + round;
                        m.right += shadeWidth + 1;
                    }
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

            // Old
            // popupMenu.setBorder ( popupMenu instanceof BasicComboPopup ? BorderFactory.createEmptyBorder ( 1, 1, 1, 1 ) :
            //         BorderFactory.createEmptyBorder ( 0, 0, 0, 0 ) );
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
    public void setPopupMenuStyle ( PopupMenuStyle style )
    {
        this.popupMenuStyle = style;
        updateBorder ();
    }

    public int getRound ()
    {
        return round;
    }

    public void setRound ( int round )
    {
        // todo
        this.round = round;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Popup getPopup ( JPopupMenu popup, int x, int y )
    {
        // Updating menu elements
        for ( int i = 0; i < popup.getComponentCount (); i++ )
        {
            Component component = popup.getComponent ( i );
            if ( component instanceof JMenu )
            {
                ButtonUI ui = ( ( JMenu ) component ).getUI ();
                if ( ui instanceof WebMenuUI )
                {
                    ( ( WebMenuUI ) ui ).updateBorder ();
                }
            }
            else if ( component instanceof JMenuItem )
            {
                ButtonUI ui = ( ( JMenuItem ) component ).getUI ();
                if ( ui instanceof WebMenuItemUI )
                {
                    ( ( WebMenuItemUI ) ui ).updateBorder ();
                }
            }
        }

        final Component invoker = popup.getInvoker ();
        final Point los = invoker.getLocationOnScreen ();
        if ( invoker instanceof JMenu )
        {
            if ( invoker.getParent () instanceof JPopupMenu )
            {
                // Displaying simple-styled sub-menu
                setPopupMenuStyle ( PopupMenuStyle.simple );
                if ( fixLocation )
                {
                    x += ( los.getX () < x ? -1 : 1 ) * ( transparent ? shadeWidth - menuSpacing : 0 );
                    y -= transparent ? shadeWidth + round + 1 : round;
                }
            }
            else if ( !WebPopupMenuStyle.dropdownStyleForMenuBar )
            {
                // Displaying simple-styled top-level menu
                setPopupMenuStyle ( PopupMenuStyle.simple );
                if ( fixLocation )
                {
                    x += ( los.getX () <= x ? -1 : 1 ) * ( transparent ? shadeWidth - menuSpacing : 0 );
                    y -= transparent ? shadeWidth + round + 1 : round;
                }
            }
            else
            {
                // Displaying dropdown-styled top-level menu
                setPopupMenuStyle ( PopupMenuStyle.dropdown );
                if ( fixLocation )
                {
                    x += ( los.getX () <= x ? -1 : 1 ) * ( transparent ? shadeWidth : 0 );
                }
                relativeCorner = los.x + invoker.getWidth () / 2 - x;
                cornerSide = los.y <= y ? SwingConstants.NORTH : SwingConstants.SOUTH;
            }
        }
        else
        {
            // todo Update corner location
            // todo Update corner side
            // relativeCorner = ...
            relativeCorner = los.x + invoker.getWidth () / 2 - x;
        }

        updateBorder ();

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
            final Shape border = getComplexShape ( mb, false );
            final Shape fill = getComplexShape ( mb, true );

            // Background
            g2d.setColor ( backgroundColor );
            g2d.fill ( fill );

            if ( popupMenuStyle == PopupMenuStyle.dropdown && round == 0 )
            {
                Component component = popupMenu.getComponent ( 0 );
                if ( component instanceof JMenuItem )
                {
                    JMenuItem menuItem = ( JMenuItem ) component;
                    if ( menuItem.getModel ().isArmed () )
                    {
                        final Shape dropdown = createDropdownCornerShape ( mb, true );
                        g2d.setColor ( WebMenuItemStyle.topBg );
                        g2d.fill ( dropdown );
                    }
                }
            }

            // Border
            g2d.setPaint ( borderColor );
            g2d.draw ( border );
        }
        else
        {
            // Background
            g2d.setColor ( backgroundColor );
            g2d.fillRoundRect ( 1, 1, c.getWidth () - 2, c.getHeight () - 2, round * 2, round * 2 );

            // Border
            g2d.setColor ( borderColor );
            g2d.drawRoundRect ( 0, 0, c.getWidth () - 1, c.getHeight () - 1, round * 2, round * 2 );
        }

        LafUtils.restoreAntialias ( g2d, aa );
    }

    protected Rectangle getShadeBounds ( final Rectangle mb )
    {
        switch ( popupMenuStyle )
        {
            case simple:
            {
                return new Rectangle ( 0, 0, mb.width, mb.height );
            }
            case dropdown:
            {
                // todo Change depending on popup menu position
                final int shear = Math.max ( 0, cornerWidth - shadeWidth );
                return new Rectangle ( 0, shear, mb.width, mb.height - shear );
            }
            default:
            {
                return null;
            }
        }
    }

    protected Shape getComplexShape ( final Rectangle mb, final boolean fill )
    {
        switch ( popupMenuStyle )
        {
            case simple:
            {
                return createSimpleShape ( mb, fill );
            }
            case dropdown:
            {
                // todo Change depending on popup menu position
                return createDropdownShape ( mb, fill );
            }
            default:
            {
                return null;
            }
        }
    }

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

    protected GeneralPath createDropdownCornerShape ( final Rectangle mb, final boolean fill )
    {
        final boolean north = cornerSide == SwingConstants.NORTH;
        final boolean south = cornerSide == SwingConstants.SOUTH;

        // Painting shear
        final int shear = fill ? 1 : 0;

        // Corner left spacing
        final int ds = shadeWidth + shear + round + cornerWidth * 2;
        final int spacing = relativeCorner < shadeWidth + round + cornerWidth ? 0 : Math.min ( relativeCorner - ds, mb.width - ds * 2 );

        // Side spacings
        final int top = north ? cornerWidth + shear : shadeWidth + shear;
        final int botom = south ? mb.height - 1 - cornerWidth : mb.height - 1 - shadeWidth;
        final int left = shadeWidth + shear;
        //        final int right = mb.width - 1 - shadeWidth;

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

    protected GeneralPath createDropdownShape ( final Rectangle mb, final boolean fill )
    {
        final boolean north = cornerSide == SwingConstants.NORTH;
        final boolean south = cornerSide == SwingConstants.SOUTH;

        // Painting shear
        final int shear = fill ? 1 : 0;

        // Corner left spacing
        final int ds = shadeWidth + shear + round + cornerWidth * 2;
        final int spacing = relativeCorner < shadeWidth + round + cornerWidth ? 0 : Math.min ( relativeCorner - ds, mb.width - ds * 2 );

        // Side spacings
        final int top = north ? cornerWidth + shear : shadeWidth + shear;
        final int botom = south ? mb.height - 1 - cornerWidth : mb.height - 1 - shadeWidth;
        final int left = shadeWidth + shear;
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

    public static void main ( String[] args )
    {
        WebLookAndFeel.install ();
        //        WebLookAndFeel.setDecorateAllWindows ( true );

        //        StyleConstants.smallRound = 0;
        //        StyleConstants.mediumRound = 0;
        //        StyleConstants.bigRound = 0;
        //        StyleConstants.largeRound = 0;

        final WebPopupMenu menu = new WebPopupMenu ();
        menu.add ( new WebColorChooserPanel ( false ).setMargin ( 0, 7, 0, 7 ) );

        TestFrame tf = new TestFrame ( new WebButton ( "Simple menu", WebLookAndFeel.getIcon ( 16 ), new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                menu.showUnder ( ( Component ) e.getSource () );
            }
        } ), 250 );

        WebMenuBar menuBar = new WebMenuBar ();
        menuBar.setUndecorated ( true );
        menuBar.add ( fillMenu ( new WebMenu ( "File" ) ) );
        menuBar.add ( fillMenu ( new WebMenu ( "Edit" ) ) );
        menuBar.add ( fillMenu ( new WebMenu ( "Help" ) ) );
        menuBar.add ( fillMenu ( new WebMenu ( "About" ) ) );
        tf.setJMenuBar ( menuBar );

        tf.packAndCenter ();
        tf.displayFrame ();

        HotkeyManager.registerHotkey ( Hotkey.ALT_R, new HotkeyRunnable ()
        {
            @Override
            public void run ( KeyEvent e )
            {
                LanguageManager.changeOrientation ();
            }
        } );
    }

    private static WebMenu fillMenu ( WebMenu menu )
    {
        final ImageIcon icon = WebLookAndFeel.getIcon ( 16 );
        //  ImageUtils.combineIcons ( icon, icon )

        menu.add ( new WebMenuItem ( "Test item", icon )
        {
            {
                setAccelerator ( Hotkey.CTRL_X.getKeyStroke () );
            }
        } );
        menu.add ( new WebMenuItem ( "<html>Some <b>html</b> <u>content</u></html>", icon ) );
        menu.addSeparator ();
        menu.add ( new WebMenuItem ( "1234567890", icon )
        {
            {
                setAccelerator ( Hotkey.SPACE.getKeyStroke () );
            }
        } );
        menu.add ( new WebMenu ( "Sub menu", icon )
        {
            {
                add ( new WebMenuItem ( "Sub 1" ) );
                add ( new WebMenuItem ( "Sub 2" ) );
                add ( new WebMenuItem ( "Sub 3" ) );
                add ( new WebMenu ( "Sub menu" )
                {
                    {
                        add ( new WebMenuItem ( "Sub 1" ) );
                        add ( new WebMenuItem ( "Sub 2" ) );
                        add ( new WebMenuItem ( "Sub 3" ) );
                    }
                } );
            }
        } );
        menu.add ( new WebMenuItem ( "!@%#$^%*^%#", icon ) );
        menu.add ( new WebMenuItem ( "Something else", icon )
        {
            {
                setEnabled ( false );
            }
        } );
        menu.add ( new WebMenu ( "Disabled menu", icon )
        {
            {
                setEnabled ( false );
                add ( new WebMenuItem ( "Sub 1" ) );
            }
        } );
        menu.addSeparator ();
        menu.add ( new WebMenuItem ( "QWERTYSDASZXO", icon )
        {
            {
                setEnabled ( false );
                setAccelerator ( Hotkey.ALT_F4.getKeyStroke () );
            }
        } );
        menu.add ( new WebMenuItem ( "Last element", icon ) );
        return menu;
    }
}