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

package com.alee.laf.toolbar;

import com.alee.extended.layout.ToolbarLayout;
import com.alee.extended.painter.Painter;
import com.alee.extended.painter.PainterSupport;
import com.alee.global.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.ProprietaryUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.swing.AncestorAdapter;
import com.alee.utils.swing.BorderMethods;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolBarUI;
import java.awt.*;
import java.awt.event.WindowListener;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author Mikle Garin
 */

public class WebToolBarUI extends BasicToolBarUI implements ShapeProvider, BorderMethods
{
    public static final int gripperSpace = 5;

    private Color topBgColor = WebToolBarStyle.topBgColor;
    private Color bottomBgColor = WebToolBarStyle.bottomBgColor;
    private Color borderColor = WebToolBarStyle.borderColor;
    private Color disabledBorderColor = WebToolBarStyle.disabledBorderColor;

    private boolean undecorated = WebToolBarStyle.undecorated;
    private int round = WebToolBarStyle.round;
    private int shadeWidth = WebToolBarStyle.shadeWidth;
    private Insets margin = WebToolBarStyle.margin;
    private int spacing = WebToolBarStyle.spacing;
    private ToolbarStyle toolbarStyle = WebToolBarStyle.toolbarStyle;
    private Painter painter = WebToolBarStyle.painter;

    private final Color middleColor = new Color ( 158, 158, 158 );
    private final Color[] gradient = new Color[]{ StyleConstants.transparent, middleColor, middleColor, StyleConstants.transparent };
    private final float[] fractions = { 0f, 0.33f, 0.66f, 1f };

    private AncestorListener ancestorListener;
    private PropertyChangeListener propertyChangeListener;
    private PropertyChangeListener componentOrientationListener;

    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebToolBarUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Default settings
        SwingUtils.setOrientation ( toolBar );
        LookAndFeel.installProperty ( toolBar, WebLookAndFeel.OPAQUE_PROPERTY, Boolean.FALSE );
        PainterSupport.installPainter ( toolBar, this.painter );

        // Updating border and layout
        updateBorder ();
        updateLayout ( toolBar, true );

        // Border and layout update listeners
        ancestorListener = new AncestorAdapter ()
        {
            @Override
            public void ancestorAdded ( final AncestorEvent event )
            {
                updateBorder ();
                updateLayout ( toolBar, false );
            }
        };
        toolBar.addAncestorListener ( ancestorListener );
        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updateBorder ();
                updateLayout ( toolBar, false );
            }
        };
        toolBar.addPropertyChangeListener ( WebLookAndFeel.TOOLBAR_FLOATABLE_PROPERTY, propertyChangeListener );
        toolBar.addPropertyChangeListener ( WebLookAndFeel.TOOLBAR_ORIENTATION_PROPERTY, propertyChangeListener );
        componentOrientationListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updateBorder ();
            }
        };
        toolBar.addPropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, componentOrientationListener );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        PainterSupport.uninstallPainter ( toolBar, this.painter );

        c.removeAncestorListener(ancestorListener);
        c.removePropertyChangeListener ( WebLookAndFeel.TOOLBAR_ORIENTATION_PROPERTY, propertyChangeListener );
        c.removePropertyChangeListener ( WebLookAndFeel.TOOLBAR_FLOATABLE_PROPERTY, propertyChangeListener );
        c.removePropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, componentOrientationListener );

        super.uninstallUI ( c );

        // Swing doesn't cleanup this value on its own
        toolBar = null;
    }

    @Override
    public Shape provideShape ()
    {
        if ( painter != null || undecorated )
        {
            return SwingUtils.size ( toolBar );
        }
        else
        {
            return LafUtils.getWebBorderShape ( toolBar, getShadeWidth (), getRound () );
        }
    }

    public boolean isUndecorated ()
    {
        return undecorated;
    }

    public void setUndecorated ( final boolean undecorated )
    {
        this.undecorated = undecorated;

        // Updating border
        updateBorder ();

        // Updating opaque value
        if ( painter != null && !undecorated )
        {
            toolBar.setOpaque ( false );
        }
    }

    public Painter getPainter ()
    {
        return painter;
    }

    public void setPainter ( final Painter painter )
    {
        PainterSupport.uninstallPainter ( toolBar, this.painter );

        this.painter = painter;
        PainterSupport.installPainter ( toolBar, this.painter );
        updateBorder ();
    }

    public int getRound ()
    {
        if ( undecorated )
        {
            return 0;
        }
        else
        {
            return round;
        }
    }

    public void setRound ( final int round )
    {
        this.round = round;
    }

    public Color getTopBgColor ()
    {
        return topBgColor;
    }

    public void setTopBgColor ( final Color topBgColor )
    {
        this.topBgColor = topBgColor;
    }

    public Color getBottomBgColor ()
    {
        return bottomBgColor;
    }

    public void setBottomBgColor ( final Color bottomBgColor )
    {
        this.bottomBgColor = bottomBgColor;
    }

    public Color getBorderColor ()
    {
        return borderColor;
    }

    public void setBorderColor ( final Color borderColor )
    {
        this.borderColor = borderColor;
    }

    public Color getDisabledBorderColor ()
    {
        return disabledBorderColor;
    }

    public void setDisabledBorderColor ( final Color disabledBorderColor )
    {
        this.disabledBorderColor = disabledBorderColor;
    }

    public int getShadeWidth ()
    {
        if ( undecorated )
        {
            return 0;
        }
        else
        {
            return shadeWidth;
        }
    }

    public void setShadeWidth ( final int shadeWidth )
    {
        this.shadeWidth = shadeWidth;
        updateBorder ();
    }

    public Insets getMargin ()
    {
        return margin;
    }

    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        updateBorder ();
    }

    public ToolbarStyle getToolbarStyle ()
    {
        return toolbarStyle;
    }

    public void setToolbarStyle ( final ToolbarStyle toolbarStyle )
    {
        this.toolbarStyle = toolbarStyle;
        updateBorder ();
    }

    public int getSpacing ()
    {
        return spacing;
    }

    public void setSpacing ( final int spacing )
    {
        this.spacing = spacing;
        updateLayout ( toolBar, false );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBorder ()
    {
        if ( toolBar != null )
        {
            // Preserve old borders
            if ( SwingUtils.isPreserveBorders ( toolBar ) )
            {
                return;
            }

            if ( painter != null )
            {
                // Background insets
                final Insets bi = painter.getMargin ( toolBar );
                toolBar.setBorder ( LafUtils.createWebBorder ( margin.top + bi.top, margin.left + bi.left, margin.bottom + bi.bottom,
                        margin.right + bi.right ) );
            }
            else if ( !undecorated )
            {
                // Web-style insets
                final int gripperSpacing = toolBar.isFloatable () ? gripperSpace : 0;
                final boolean horizontal = toolBar.getOrientation () == WebToolBar.HORIZONTAL;
                final boolean ltr = toolBar.getComponentOrientation ().isLeftToRight ();
                if ( toolbarStyle.equals ( ToolbarStyle.standalone ) )
                {
                    if ( isFloating () )
                    {
                        toolBar.setBorder ( LafUtils.createWebBorder ( margin.top + ( !horizontal ? gripperSpacing : 0 ),
                                margin.left + ( horizontal && ltr ? gripperSpacing : 0 ), margin.bottom,
                                margin.right + ( horizontal && !ltr ? gripperSpacing : 0 ) ) );
                    }
                    else
                    {
                        toolBar.setBorder ( LafUtils.createWebBorder ( margin.top + 1 + shadeWidth + ( !horizontal ? gripperSpacing : 0 ),
                                margin.left + 1 + shadeWidth +
                                        ( horizontal && ltr ? gripperSpacing : 0 ), margin.bottom + 1 + shadeWidth,
                                margin.right + 1 + shadeWidth +
                                        ( horizontal && !ltr ? gripperSpacing : 0 ) ) );
                    }
                }
                else
                {
                    if ( isFloating () )
                    {
                        toolBar.setBorder ( LafUtils.createWebBorder ( margin.top + ( !horizontal ? gripperSpacing : 0 ),
                                margin.left + ( horizontal && ltr ? gripperSpacing : 0 ), margin.bottom,
                                margin.right + ( horizontal && !ltr ? gripperSpacing : 0 ) ) );
                    }
                    else
                    {
                        toolBar.setBorder ( LafUtils.createWebBorder ( margin.top + ( !horizontal ? gripperSpacing : 0 ),
                                margin.left + ( horizontal && ltr ? gripperSpacing : 0 ) +
                                        ( !horizontal && !ltr ? 1 : 0 ), margin.bottom + ( horizontal ? 1 : 0 ),
                                margin.right + ( horizontal && !ltr ? gripperSpacing : 0 ) +
                                        ( !horizontal && ltr ? 1 : 0 ) ) );
                    }
                }
            }
            else
            {
                // Empty insets
                toolBar.setBorder ( LafUtils.createWebBorder ( margin.top, margin.left, margin.bottom, margin.right ) );
            }
        }
    }

    private void updateLayout ( final JComponent c, final boolean install )
    {
        final boolean installed = c.getLayout () instanceof ToolbarLayout;
        if ( !install && !installed )
        {
            return;
        }

        final ToolbarLayout layout = new ToolbarLayout ( spacing, toolBar.getOrientation () );
        if ( installed )
        {
            final ToolbarLayout old = ( ToolbarLayout ) c.getLayout ();
            layout.setConstraints ( old.getConstraints () );
        }
        c.setLayout ( layout );
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            // Use background painter instead of default UI graphics
            painter.paint ( ( Graphics2D ) g, SwingUtils.size ( c ), c );
        }
        else if ( !undecorated )
        {
            final Graphics2D g2d = ( Graphics2D ) g;
            final Object aa = GraphicsUtils.setupAntialias ( g2d );

            final boolean horizontal = toolBar.getOrientation () == WebToolBar.HORIZONTAL;
            final boolean ltr = c.getComponentOrientation ().isLeftToRight ();

            // Painting border and background
            if ( isFloating () )
            {
                if ( horizontal )
                {
                    g2d.setPaint ( new GradientPaint ( 0, c.getHeight () / 2, topBgColor, 0, c.getHeight (), bottomBgColor ) );
                }
                else
                {
                    g2d.setPaint ( new GradientPaint ( c.getWidth () / 2, 0, topBgColor, c.getWidth (), 0, bottomBgColor ) );
                }
                g2d.fillRect ( 0, 0, c.getWidth (), c.getHeight () );
            }
            else
            {
                if ( toolbarStyle.equals ( ToolbarStyle.standalone ) )
                {
                    final RoundRectangle2D rr = new RoundRectangle2D.Double ( shadeWidth, shadeWidth, c.getWidth () - shadeWidth * 2 - 1,
                            c.getHeight () - shadeWidth * 2 - 1, round, round );

                    if ( c.isEnabled () )
                    {
                        GraphicsUtils.drawShade ( g2d, rr, StyleConstants.shadeColor, shadeWidth );
                    }

                    if ( horizontal )
                    {
                        g2d.setPaint ( new GradientPaint ( 0, c.getHeight () / 2, topBgColor, 0, c.getHeight (), bottomBgColor ) );
                    }
                    else
                    {
                        g2d.setPaint ( new GradientPaint ( c.getWidth () / 2, 0, topBgColor, c.getWidth (), 0, bottomBgColor ) );
                    }
                    g2d.fill ( rr );

                    g2d.setPaint ( c.isEnabled () ? borderColor : disabledBorderColor );
                    g2d.draw ( rr );
                }
                else
                {
                    if ( horizontal )
                    {
                        g2d.setPaint ( new GradientPaint ( 0, c.getHeight () / 2, topBgColor, 0, c.getHeight (), bottomBgColor ) );
                    }
                    else
                    {
                        g2d.setPaint ( new GradientPaint ( c.getWidth () / 2, 0, topBgColor, c.getWidth (), 0, bottomBgColor ) );
                    }
                    g2d.fillRect ( 0, 0, c.getWidth (), c.getHeight () );

                    g2d.setPaint ( c.isEnabled () ? borderColor : disabledBorderColor );
                    if ( horizontal )
                    {
                        g2d.drawLine ( 0, c.getHeight () - 1, c.getWidth () - 1, c.getHeight () - 1 );
                    }
                    else
                    {
                        if ( ltr )
                        {
                            g2d.drawLine ( c.getWidth () - 1, 0, c.getWidth () - 1, c.getHeight () - 1 );
                        }
                        else
                        {
                            g2d.drawLine ( 0, 0, 0, c.getHeight () - 1 );
                        }
                    }
                }
            }

            // Painting gripper
            if ( toolBar.isFloatable () )
            {
                if ( toolBar.getOrientation () == WebToolBar.HORIZONTAL )
                {
                    final int gradY = shadeWidth + 1;
                    final int gradEndY = c.getHeight () - shadeWidth - 2;
                    if ( gradEndY > gradY )
                    {
                        g2d.setPaint ( new LinearGradientPaint ( 0, gradY, 0, gradEndY, fractions, gradient ) );

                        // Determining gripper X coordinate
                        int x = toolbarStyle.equals ( ToolbarStyle.standalone ) ?
                                shadeWidth + 1 + margin.left + ( isFloating () ? -1 : 1 ) : margin.left + gripperSpace / 2 - 1;
                        if ( !ltr )
                        {
                            x = c.getWidth () - x - 2;
                        }

                        // Painting gripper
                        for ( int i = c.getHeight () / 2 - 3; i >= gradY; i -= 4 )
                        {
                            g2d.fillRect ( x, i, 2, 2 );
                        }
                        for ( int i = c.getHeight () / 2 + 1; i + 2 <= gradEndY; i += 4 )
                        {
                            g2d.fillRect ( x, i, 2, 2 );
                        }
                    }
                }
                else
                {
                    final int gradX = shadeWidth + 1;
                    final int gradEndX = c.getWidth () - shadeWidth - 2;
                    if ( gradEndX > gradX )
                    {
                        g2d.setPaint ( new LinearGradientPaint ( gradX, 0, gradEndX, 0, fractions, gradient ) );

                        // Determining gripper Y coordinate
                        final int y =
                                toolbarStyle.equals ( ToolbarStyle.standalone ) ? shadeWidth + 1 + margin.top + ( isFloating () ? -1 : 1 ) :
                                        margin.top + gripperSpace / 2 - 1;

                        // Painting gripper
                        for ( int i = c.getWidth () / 2 - 3; i >= gradX; i -= 4 )
                        {
                            g2d.fillRect ( i, y, 2, 2 );
                        }
                        for ( int i = c.getWidth () / 2 + 1; i + 2 <= gradEndX; i += 4 )
                        {
                            g2d.fillRect ( i, y, 2, 2 );
                        }
                    }
                }
            }

            GraphicsUtils.restoreAntialias ( g2d, aa );
        }
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        Dimension ps = c.getLayout () != null ? c.getLayout ().preferredLayoutSize ( c ) : null;
        if ( painter != null )
        {
            ps = SwingUtils.max ( ps, painter.getPreferredSize ( c ) );
        }
        return ps;
    }

    @Override
    protected RootPaneContainer createFloatingWindow ( final JToolBar toolbar )
    {
        class ToolBarDialog extends WebDialog
        {
            public ToolBarDialog ( final Frame owner, final String title, final boolean modal )
            {
                super ( owner, title, modal );
            }

            public ToolBarDialog ( final Dialog owner, final String title, final boolean modal )
            {
                super ( owner, title, modal );
            }

            // Override createRootPane() to automatically resize
            // the frame when contents change
            @Override
            protected JRootPane createRootPane ()
            {
                final JRootPane rootPane = new JRootPane ()
                {
                    private boolean packing = false;

                    @Override
                    public void validate ()
                    {
                        super.validate ();
                        if ( !packing )
                        {
                            packing = true;
                            pack ();
                            packing = false;
                        }
                    }
                };
                rootPane.setOpaque ( true );
                return rootPane;
            }
        }

        final JDialog dialog;
        final Window window = SwingUtils.getWindowAncestor ( toolbar );
        if ( window instanceof Frame )
        {
            dialog = new ToolBarDialog ( ( Frame ) window, toolbar.getName (), false );
        }
        else if ( window instanceof Dialog )
        {
            dialog = new ToolBarDialog ( ( Dialog ) window, toolbar.getName (), false );
        }
        else
        {
            dialog = new ToolBarDialog ( ( Frame ) null, toolbar.getName (), false );
        }

        dialog.getRootPane ().setName ( "ToolBar.FloatingWindow" );
        dialog.setTitle ( toolbar.getName () );
        dialog.setResizable ( false );
        final WindowListener wl = createFrameListener ();
        dialog.addWindowListener ( wl );
        //        dialog.setUndecorated ( true );
        return dialog;
    }

    @Override
    protected DragWindow createDragWindow ( final JToolBar toolbar )
    {
        final DragWindow dragWindow = super.createDragWindow ( toolbar );
        ProprietaryUtils.setWindowOpacity ( dragWindow, 0.5f );
        return dragWindow;
    }

    @Override
    protected void installRolloverBorders ( final JComponent c )
    {
        // Do not touch any elements here as it will break WebLaF borders
    }

    @Override
    protected void installNonRolloverBorders ( final JComponent c )
    {
        // Do not touch any elements here as it will break WebLaF borders
    }

    @Override
    protected void installNormalBorders ( final JComponent c )
    {
        // Do not touch any elements here as it will break WebLaF borders
    }

    @Override
    protected void setBorderToRollover ( final Component c )
    {
        // Do not touch any elements here as it will break WebLaF borders
    }

    @Override
    protected void setBorderToNonRollover ( final Component c )
    {
        // Do not touch any elements here as it will break WebLaF borders
    }

    @Override
    protected void setBorderToNormal ( final Component c )
    {
        // Do not touch any elements here as it will break WebLaF borders
    }
}
