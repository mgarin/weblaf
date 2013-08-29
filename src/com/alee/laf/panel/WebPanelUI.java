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
import com.alee.laf.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.focus.FocusTracker;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * User: mgarin Date: 26.07.11 Time: 13:11
 */

public class WebPanelUI extends BasicPanelUI implements ShapeProvider, FocusTracker
{
    private boolean undecorated = WebPanelStyle.undecorated;
    private boolean drawFocus = WebPanelStyle.drawFocus;
    private int round = WebPanelStyle.round;
    private int shadeWidth = WebPanelStyle.shadeWidth;
    private Insets margin = WebPanelStyle.margin;
    private Stroke borderStroke = WebPanelStyle.borderStroke;
    private boolean drawBackground = WebPanelStyle.drawBackground;
    private boolean webColored = WebPanelStyle.webColored;
    private Painter painter = WebPanelStyle.painter;
    private ShapeProvider clipProvider = WebPanelStyle.clipProvider;

    private boolean drawTop = WebPanelStyle.drawTop;
    private boolean drawLeft = WebPanelStyle.drawLeft;
    private boolean drawBottom = WebPanelStyle.drawBottom;
    private boolean drawRight = WebPanelStyle.drawRight;

    private JPanel panel = null;

    private PropertyChangeListener propertyChangeListener;

    private boolean focused = false;

    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( JComponent c )
    {
        return new WebPanelUI ();
    }

    @Override
    public void installUI ( JComponent c )
    {
        super.installUI ( c );

        // Saving panel to local variable
        panel = ( JPanel ) c;

        // Default settings
        SwingUtils.setOrientation ( panel );
        panel.setOpaque ( true );
        panel.setBackground ( WebPanelStyle.backgroundColor );

        // Updating border
        updateBorder ();

        // Orientation change listener
        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( PropertyChangeEvent evt )
            {
                updateBorder ();
            }
        };
        panel.addPropertyChangeListener ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY, propertyChangeListener );

        // Focus tracker
        updateFocusTracker ();
    }

    @Override
    public void uninstallUI ( JComponent c )
    {
        panel.removePropertyChangeListener ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY, propertyChangeListener );

        FocusManager.unregisterFocusTracker ( WebPanelUI.this );

        super.uninstallUI ( c );
    }

    private void updateFocusTracker ()
    {
        if ( !undecorated && drawFocus )
        {
            FocusManager.registerFocusTracker ( WebPanelUI.this );
        }
        else
        {
            FocusManager.unregisterFocusTracker ( WebPanelUI.this );
        }
    }

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

    @Override
    public boolean isEnabled ()
    {
        return true;
    }

    @Override
    public Component getComponent ()
    {
        return panel;
    }

    @Override
    public boolean isUniteWithChilds ()
    {
        return true;
    }

    @Override
    public void focusChanged ( boolean focused )
    {
        this.focused = focused;
        panel.repaint ();
    }

    private void updateBorder ()
    {
        if ( panel != null )
        {
            // Actual margin
            boolean ltr = panel.getComponentOrientation ().isLeftToRight ();
            Insets m = new Insets ( margin.top, ltr ? margin.left : margin.right, margin.bottom, ltr ? margin.right : margin.left );

            // Calculating additional borders
            if ( painter != null )
            {
                // Painter borders
                Insets pi = painter.getMargin ( panel );
                m.top += pi.top;
                m.bottom += pi.bottom;
                m.left += ltr ? pi.left : pi.right;
                m.right += ltr ? pi.right : pi.left;
            }
            else if ( !undecorated )
            {
                // Styling borders
                boolean actualDrawLeft = ltr ? drawLeft : drawRight;
                boolean actualDrawRight = ltr ? drawRight : drawLeft;
                m.top += drawTop ? shadeWidth + 1 : 0;
                m.left += actualDrawLeft ? shadeWidth + 1 : 0;
                m.bottom += drawBottom ? shadeWidth + 1 : 0;
                m.right += actualDrawRight ? shadeWidth + 1 : 0;
            }

            // Installing border
            panel.setBorder ( LafUtils.createWebBorder ( m ) );
        }
    }

    private void updateOpacity ()
    {
        if ( painter != null )
        {
            panel.setOpaque ( painter.isOpaque ( panel ) );
        }
    }

    public boolean isUndecorated ()
    {
        return undecorated;
    }

    public void setUndecorated ( boolean undecorated )
    {
        this.undecorated = undecorated;
        updateFocusTracker ();
        updateBorder ();

        // todo Bad workaround
        // Makes panel non-opaque when it becomes decorated
        if ( painter == null && !undecorated )
        {
            panel.setOpaque ( false );
        }
    }

    public boolean isDrawFocus ()
    {
        return drawFocus;
    }

    public void setDrawFocus ( boolean drawFocus )
    {
        this.drawFocus = drawFocus;
        updateFocusTracker ();
    }

    public Painter getPainter ()
    {
        return painter;
    }

    public void setPainter ( Painter painter )
    {
        this.painter = painter;
        updateBorder ();
        updateOpacity ();
    }

    public ShapeProvider getClipProvider ()
    {
        return clipProvider;
    }

    public void setClipProvider ( ShapeProvider clipProvider )
    {
        this.clipProvider = clipProvider;
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

    public void setRound ( int round )
    {
        this.round = round;
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

    public void setShadeWidth ( int shadeWidth )
    {
        this.shadeWidth = shadeWidth;
        updateBorder ();
    }

    public Insets getMargin ()
    {
        return margin;
    }

    public void setMargin ( Insets margin )
    {
        this.margin = margin;
        updateBorder ();
    }

    public Stroke getBorderStroke ()
    {
        return borderStroke;
    }

    public void setBorderStroke ( Stroke stroke )
    {
        this.borderStroke = stroke;
    }

    public boolean isDrawBackground ()
    {
        return drawBackground;
    }

    public void setDrawBackground ( boolean drawBackground )
    {
        this.drawBackground = drawBackground;
    }

    public boolean isWebColored ()
    {
        return webColored;
    }

    public void setWebColored ( boolean webColored )
    {
        this.webColored = webColored;
    }

    public boolean isDrawBottom ()
    {
        return drawBottom;
    }

    public void setDrawBottom ( boolean drawBottom )
    {
        this.drawBottom = drawBottom;
        updateBorder ();
    }

    public boolean isDrawLeft ()
    {
        return drawLeft;
    }

    public void setDrawLeft ( boolean drawLeft )
    {
        this.drawLeft = drawLeft;
        updateBorder ();
    }

    public boolean isDrawRight ()
    {
        return drawRight;
    }

    public void setDrawRight ( boolean drawRight )
    {
        this.drawRight = drawRight;
        updateBorder ();
    }

    public boolean isDrawTop ()
    {
        return drawTop;
    }

    public void setDrawTop ( boolean drawTop )
    {
        this.drawTop = drawTop;
        updateBorder ();
    }

    public void setDrawSides ( boolean top, boolean left, boolean bottom, boolean right )
    {
        this.drawTop = top;
        this.drawLeft = left;
        this.drawBottom = bottom;
        this.drawRight = right;
        updateBorder ();
    }

    @Override
    public void paint ( Graphics g, JComponent c )
    {
        // To be applied for all childs painting
        LafUtils.setupSystemTextHints ( g );

        Shape clip = clipProvider != null ? clipProvider.provideShape () : null;
        Shape oldClip = null;
        if ( clip != null )
        {
            oldClip = LafUtils.intersectClip ( ( Graphics2D ) g, clip );
        }
        if ( painter != null )
        {
            // Use background painter instead of default UI graphics
            painter.paint ( ( Graphics2D ) g, SwingUtils.size ( c ), c );
        }
        else if ( !undecorated )
        {
            // Checking need of painting
            boolean anyBorder = drawTop || drawRight || drawBottom || drawLeft;
            if ( anyBorder || drawBackground )
            {
                Graphics2D g2d = ( Graphics2D ) g;
                Object aa = LafUtils.setupAntialias ( g2d );

                // Border shape
                Shape borderShape = getPanelShape ( c, false );

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
                    Shape bgShape = getPanelShape ( c, true );

                    // Draw bg
                    if ( webColored )
                    {
                        // Setup cached gradient paint
                        Rectangle bgBounds = bgShape.getBounds ();
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
                    Stroke os = LafUtils.setupStroke ( g2d, borderStroke, borderStroke != null );
                    g2d.setPaint ( c.isEnabled () ? StyleConstants.darkBorderColor : StyleConstants.disabledBorderColor );
                    g2d.draw ( borderShape );
                    LafUtils.restoreStroke ( g2d, os, borderStroke != null );
                }

                LafUtils.restoreAntialias ( g2d, aa );
            }
        }
        if ( clip != null )
        {
            LafUtils.restoreClip ( g, oldClip );
        }
    }

    private Shape getPanelShape ( JComponent c, boolean bg )
    {
        // Changing draw marks in case of RTL orientation
        boolean ltr = c.getComponentOrientation ().isLeftToRight ();
        boolean actualDrawLeft = ltr ? drawLeft : drawRight;
        boolean actualDrawRight = ltr ? drawRight : drawLeft;

        // Width and height
        int w = c.getWidth ();
        int h = c.getHeight ();

        if ( bg )
        {
            Point[] corners = new Point[ 4 ];
            boolean[] rounded = new boolean[ 4 ];

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
            GeneralPath shape = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
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

    private Point p ( int x, int y )
    {
        return new Point ( x, y );
    }

    @Override
    public Dimension getPreferredSize ( JComponent c )
    {
        //        Dimension ps = c.getLayout () != null ? c.getLayout ().preferredLayoutSize ( c ) : null;
        //        if ( painter != null )
        //        {
        //            ps = SwingUtils.max ( ps, painter.getPreferredSize ( c ) );
        //        }
        //        return ps;
        return null;
    }

    @Override
    public Dimension getMaximumSize ( JComponent c )
    {
        // Fix for some of the Swing layouts
        return null;
    }
}