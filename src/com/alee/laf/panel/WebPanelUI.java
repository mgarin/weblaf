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

package com.alee.laf.panel;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.PainterSupport;
import com.alee.laf.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.focus.DefaultFocusTracker;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.focus.FocusTracker;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.swing.BorderMethods;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Custom UI for JPanel component.
 *
 * @author Mikle Garin
 */

public class WebPanelUI extends BasicPanelUI implements ShapeProvider, BorderMethods
{
    /**
     * Style settings.
     */
    private boolean undecorated = WebPanelStyle.undecorated;
    private boolean drawFocus = WebPanelStyle.drawFocus;
    private int round = WebPanelStyle.round;
    private int shadeWidth = WebPanelStyle.shadeWidth;
    private Insets margin = WebPanelStyle.margin;
    private Stroke borderStroke = WebPanelStyle.borderStroke;
    private boolean drawBackground = WebPanelStyle.drawBackground;
    private boolean webColored = WebPanelStyle.webColored;
    private Painter painter = WebPanelStyle.painter;
    private boolean drawTop = WebPanelStyle.drawTop;
    private boolean drawLeft = WebPanelStyle.drawLeft;
    private boolean drawBottom = WebPanelStyle.drawBottom;
    private boolean drawRight = WebPanelStyle.drawRight;

    /**
     * Panel to which this UI is applied.
     */
    private JPanel panel;

    /**
     * Panel listeners.
     */
    private PropertyChangeListener propertyChangeListener;

    /**
     * Panel focus tracker.
     */
    protected FocusTracker focusTracker;

    /**
     * Whether panel is focused or owns focused component or not.
     */
    protected boolean focused = false;

    /**
     * Returns an instance of the WebPanelUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebPanelUI
     */
    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebPanelUI ();
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

        // Saving panel to local variable
        panel = ( JPanel ) c;

        // Default settings
        SwingUtils.setOrientation ( panel );
        panel.setOpaque ( true );
        panel.setBackground ( WebPanelStyle.backgroundColor );
        PainterSupport.installPainter ( panel, this.painter );

        // Updating border
        updateBorder ();

        // Orientation change listener
        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updateBorder ();
            }
        };
        panel.addPropertyChangeListener ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY, propertyChangeListener );

        // Focus tracker for the panel content
        focusTracker = new DefaultFocusTracker ()
        {
            @Override
            public boolean isTrackingEnabled ()
            {
                return !undecorated && drawFocus;
            }

            @Override
            public void focusChanged ( final boolean focused )
            {
                WebPanelUI.this.focused = focused;
                panel.repaint ();
            }
        };
        FocusManager.addFocusTracker ( panel, focusTracker );
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        PainterSupport.uninstallPainter ( panel, this.painter );

        panel.removePropertyChangeListener ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY, propertyChangeListener );

        FocusManager.removeFocusTracker ( focusTracker );

        super.uninstallUI ( c );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape provideShape ()
    {
        if ( painter != null || undecorated )
        {
            return SwingUtils.size ( panel );
        }
        else
        {
            return getPanelShape ( panel, true );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBorder ()
    {
        if ( panel != null )
        {
            // Actual margin
            final boolean ltr = panel.getComponentOrientation ().isLeftToRight ();
            final Insets m = new Insets ( margin.top, ltr ? margin.left : margin.right, margin.bottom, ltr ? margin.right : margin.left );

            // Calculating additional borders
            if ( painter != null )
            {
                // Painter borders
                final Insets pi = painter.getMargin ( panel );
                m.top += pi.top;
                m.bottom += pi.bottom;
                m.left += ltr ? pi.left : pi.right;
                m.right += ltr ? pi.right : pi.left;
            }
            else if ( !undecorated )
            {
                // Styling borders
                final boolean actualDrawLeft = ltr ? drawLeft : drawRight;
                final boolean actualDrawRight = ltr ? drawRight : drawLeft;
                m.top += drawTop ? shadeWidth + 1 : 0;
                m.left += actualDrawLeft ? shadeWidth + 1 : 0;
                m.bottom += drawBottom ? shadeWidth + 1 : 0;
                m.right += actualDrawRight ? shadeWidth + 1 : 0;
            }

            // Installing border
            panel.setBorder ( LafUtils.createWebBorder ( m ) );
        }
    }

    /**
     * Returns whether panel is undecorated or not.
     *
     * @return true if panel is undecorated, false otherwise
     */
    public boolean isUndecorated ()
    {
        return undecorated;
    }

    /**
     * Sets whether panel should be undecorated or not.
     *
     * @param undecorated whether panel should be undecorated or not
     */
    public void setUndecorated ( final boolean undecorated )
    {
        this.undecorated = undecorated;
        updateBorder ();

        // todo Bad workaround
        // Makes panel non-opaque when it becomes decorated
        if ( painter == null && !undecorated )
        {
            panel.setOpaque ( false );
        }
    }

    /**
     * Returns whether panel should display when it owns focus or not.
     *
     * @return true if panel should display when it owns focus, false otherwise
     */
    public boolean isDrawFocus ()
    {
        return drawFocus;
    }

    /**
     * Sets whether panel should display when it owns focus or not.
     *
     * @param drawFocus whether panel should display when it owns focus or not
     */
    public void setDrawFocus ( final boolean drawFocus )
    {
        this.drawFocus = drawFocus;
    }

    /**
     * Returns panel background painter.
     *
     * @return panel background painter
     */
    public Painter getPainter ()
    {
        return painter;
    }

    /**
     * Sets panel background painter.
     *
     * @param painter new panel background painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.uninstallPainter ( panel, this.painter );

        this.painter = painter;
        PainterSupport.installPainter ( panel, this.painter );
        updateBorder ();

        // Changes panel opacity according to specified painter
        if ( painter != null )
        {
            panel.setOpaque ( painter.isOpaque ( panel ) );
        }
    }

    /**
     * Returns decoration rounding.
     *
     * @return decoration rounding
     */
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

    /**
     * Sets decoration rounding.
     *
     * @param round new decoration rounding
     */
    public void setRound ( final int round )
    {
        this.round = round;
    }

    /**
     * Returns decoration shade width.
     *
     * @return decoration shade width
     */
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

    /**
     * Sets decoration shade width.
     *
     * @param shadeWidth new decoration shade width
     */
    public void setShadeWidth ( final int shadeWidth )
    {
        this.shadeWidth = shadeWidth;
        updateBorder ();
    }

    /**
     * Returns panel margin.
     *
     * @return panel margin
     */
    public Insets getMargin ()
    {
        return margin;
    }

    /**
     * Sets panel margin.
     *
     * @param margin new panel margin
     */
    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        updateBorder ();
    }

    /**
     * Returns decoration border stroke.
     *
     * @return decoration border stroke
     */
    public Stroke getBorderStroke ()
    {
        return borderStroke;
    }

    /**
     * Sets decoration border stroke.
     *
     * @param stroke new decoration border stroke
     */
    public void setBorderStroke ( final Stroke stroke )
    {
        this.borderStroke = stroke;
    }

    /**
     * Returns whether should draw background or not.
     *
     * @return true if should draw background, false otherwise
     */
    public boolean isDrawBackground ()
    {
        return drawBackground;
    }

    /**
     * Sets whether should draw background or not.
     *
     * @param drawBackground whether should draw background or not
     */
    public void setDrawBackground ( final boolean drawBackground )
    {
        this.drawBackground = drawBackground;
    }

    /**
     * Returns whether should draw web-colored background or not.
     *
     * @return true if should draw web-colored background, false otherwise
     */
    public boolean isWebColored ()
    {
        return webColored;
    }

    /**
     * Sets whether should draw web-colored background or not.
     *
     * @param webColored whether should draw web-colored background or not
     */
    public void setWebColored ( final boolean webColored )
    {
        this.webColored = webColored;
    }

    /**
     * Returns whether should draw top panel side or not.
     *
     * @return true if should draw top panel side, false otherwise
     */
    public boolean isDrawTop ()
    {
        return drawTop;
    }

    /**
     * Sets whether should draw top panel side or not.
     *
     * @param drawTop whether should draw top panel side or not
     */
    public void setDrawTop ( final boolean drawTop )
    {
        this.drawTop = drawTop;
        updateBorder ();
    }

    /**
     * Returns whether should draw left panel side or not.
     *
     * @return true if should draw left panel side, false otherwise
     */
    public boolean isDrawLeft ()
    {
        return drawLeft;
    }

    /**
     * Sets whether should draw left panel side or not.
     *
     * @param drawLeft whether should draw left panel side or not
     */
    public void setDrawLeft ( final boolean drawLeft )
    {
        this.drawLeft = drawLeft;
        updateBorder ();
    }

    /**
     * Returns whether should draw bottom panel side or not.
     *
     * @return true if should draw bottom panel side, false otherwise
     */
    public boolean isDrawBottom ()
    {
        return drawBottom;
    }

    /**
     * Sets whether should draw bottom panel side or not.
     *
     * @param drawBottom whether should draw bottom panel side or not
     */
    public void setDrawBottom ( final boolean drawBottom )
    {
        this.drawBottom = drawBottom;
        updateBorder ();
    }

    /**
     * Returns whether should draw right panel side or not.
     *
     * @return true if should draw right panel side, false otherwise
     */
    public boolean isDrawRight ()
    {
        return drawRight;
    }

    /**
     * Sets whether should draw right panel side or not.
     *
     * @param drawRight whether should draw right panel side or not
     */
    public void setDrawRight ( final boolean drawRight )
    {
        this.drawRight = drawRight;
        updateBorder ();
    }

    /**
     * Sets whether should draw specific panel sides or not.
     *
     * @param top    whether should draw top panel side or not
     * @param left   whether should draw left panel side or not
     * @param bottom whether should draw bottom panel side or not
     * @param right  whether should draw right panel side or not
     */
    public void setDrawSides ( final boolean top, final boolean left, final boolean bottom, final boolean right )
    {
        this.drawTop = top;
        this.drawLeft = left;
        this.drawBottom = bottom;
        this.drawRight = right;
        updateBorder ();
    }

    /**
     * Paints panel.
     *
     * @param g graphics
     * @param c component
     */
    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        // To be applied for all childs painting
        LafUtils.setupSystemTextHints ( g );

        if ( painter != null )
        {
            // Use background painter instead of default UI graphics
            painter.paint ( ( Graphics2D ) g, SwingUtils.size ( c ), c );
        }
        else if ( !undecorated )
        {
            // Checking need of painting
            final boolean anyBorder = drawTop || drawRight || drawBottom || drawLeft;
            if ( anyBorder || drawBackground )
            {
                final Graphics2D g2d = ( Graphics2D ) g;
                final Object aa = LafUtils.setupAntialias ( g2d );

                // Border shape
                final Shape borderShape = getPanelShape ( c, false );

                // Outer shadow
                if ( anyBorder && c.isEnabled () )
                {
                    LafUtils.drawShade ( g2d, borderShape,
                            drawFocus && focused ? StyleConstants.fieldFocusColor : StyleConstants.shadeColor, shadeWidth );
                }

                // Background
                if ( drawBackground )
                {
                    // Bg shape
                    final Shape bgShape = getPanelShape ( c, true );

                    // Draw bg
                    if ( webColored )
                    {
                        // Setup cached gradient paint
                        final Rectangle bgBounds = bgShape.getBounds ();
                        g2d.setPaint ( LafUtils.getWebGradientPaint ( 0, bgBounds.y, 0, bgBounds.y + bgBounds.height ) );
                    }
                    else
                    {
                        // Setup single color paint
                        g2d.setPaint ( c.getBackground () );
                    }
                    g2d.fill ( bgShape );
                }

                // Border
                if ( anyBorder )
                {
                    final Stroke os = LafUtils.setupStroke ( g2d, borderStroke, borderStroke != null );
                    g2d.setPaint ( c.isEnabled () ? StyleConstants.darkBorderColor : StyleConstants.disabledBorderColor );
                    g2d.draw ( borderShape );
                    LafUtils.restoreStroke ( g2d, os, borderStroke != null );
                }

                LafUtils.restoreAntialias ( g2d, aa );
            }
        }
    }

    /**
     * Returns panel shape.
     *
     * @param c          component
     * @param background whether should return background shape or not
     * @return panel shape
     */
    private Shape getPanelShape ( final JComponent c, final boolean background )
    {
        // Changing draw marks in case of RTL orientation
        final boolean ltr = c.getComponentOrientation ().isLeftToRight ();
        final boolean actualDrawLeft = ltr ? drawLeft : drawRight;
        final boolean actualDrawRight = ltr ? drawRight : drawLeft;

        // Width and height
        final int w = c.getWidth ();
        final int h = c.getHeight ();

        if ( background )
        {
            final Point[] corners = new Point[ 4 ];
            final boolean[] rounded = new boolean[ 4 ];

            corners[ 0 ] = p ( actualDrawLeft ? shadeWidth : 0, drawTop ? shadeWidth : 0 );
            rounded[ 0 ] = actualDrawLeft && drawTop;

            corners[ 1 ] = p ( actualDrawRight ? w - shadeWidth : w, drawTop ? shadeWidth : 0 );
            rounded[ 1 ] = actualDrawRight && drawTop;

            corners[ 2 ] = p ( actualDrawRight ? w - shadeWidth : w, drawBottom ? h - shadeWidth : h );
            rounded[ 2 ] = actualDrawRight && drawBottom;

            corners[ 3 ] = p ( actualDrawLeft ? shadeWidth : 0, drawBottom ? h - shadeWidth : h );
            rounded[ 3 ] = actualDrawLeft && drawBottom;

            return LafUtils.createRoundedShape ( round > 0 ? round + 1 : 0, corners, rounded );
        }
        else
        {
            final GeneralPath shape = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
            boolean connect = false;
            boolean moved = false;
            if ( drawTop )
            {
                shape.moveTo ( actualDrawLeft ? shadeWidth + round : 0, shadeWidth );
                if ( actualDrawRight )
                {
                    shape.lineTo ( w - shadeWidth - round - 1, shadeWidth );
                    shape.quadTo ( w - shadeWidth - 1, shadeWidth, w - shadeWidth - 1, shadeWidth + round );
                }
                else
                {
                    shape.lineTo ( w - 1, shadeWidth );
                }
                connect = true;
            }
            if ( actualDrawRight )
            {
                if ( !connect )
                {
                    shape.moveTo ( w - shadeWidth - 1, drawTop ? shadeWidth + round : 0 );
                    moved = true;
                }
                if ( drawBottom )
                {
                    shape.lineTo ( w - shadeWidth - 1, h - shadeWidth - round - 1 );
                    shape.quadTo ( w - shadeWidth - 1, h - shadeWidth - 1, w - shadeWidth - round - 1, h - shadeWidth - 1 );
                }
                else
                {
                    shape.lineTo ( w - shadeWidth - 1, h - 1 );
                }
                connect = true;
            }
            else
            {
                connect = false;
            }
            if ( drawBottom )
            {
                if ( !connect )
                {
                    shape.moveTo ( actualDrawRight ? w - shadeWidth - round - 1 : w - 1, h - shadeWidth - 1 );
                    moved = true;
                }
                if ( actualDrawLeft )
                {
                    shape.lineTo ( shadeWidth + round, h - shadeWidth - 1 );
                    shape.quadTo ( shadeWidth, h - shadeWidth - 1, shadeWidth, h - shadeWidth - round - 1 );
                }
                else
                {
                    shape.lineTo ( 0, h - shadeWidth - 1 );
                }
                connect = true;
            }
            else
            {
                connect = false;
            }
            if ( actualDrawLeft )
            {
                if ( !connect )
                {
                    shape.moveTo ( shadeWidth, drawBottom ? h - shadeWidth - round - 1 : h - 1 );
                    moved = true;
                }
                if ( drawTop )
                {
                    shape.lineTo ( shadeWidth, shadeWidth + round );
                    shape.quadTo ( shadeWidth, shadeWidth, shadeWidth + round, shadeWidth );
                    if ( !moved )
                    {
                        shape.closePath ();
                    }
                }
                else
                {
                    shape.lineTo ( shadeWidth, 0 );
                }
            }
            return shape;
        }
    }

    /**
     * Returns point for the specified coordinates.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return point for the specified coordinates
     */
    private Point p ( final int x, final int y )
    {
        return new Point ( x, y );
    }
}