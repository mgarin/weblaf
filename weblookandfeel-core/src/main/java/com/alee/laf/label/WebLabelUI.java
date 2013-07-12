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

package com.alee.laf.label;

import com.alee.extended.painter.Painter;
import com.alee.laf.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

/**
 * User: mgarin Date: 26.07.11 Time: 13:16
 */

public class WebLabelUI extends BasicLabelUI
{
    private Insets margin = WebLabelStyle.margin;
    private Painter painter = WebLabelStyle.painter;
    private boolean drawShade = WebLabelStyle.drawShade;
    private Color shadeColor = WebLabelStyle.shadeColor;
    private Float transparency = WebLabelStyle.transparency;

    private JLabel label;
    private PropertyChangeListener propertyChangeListener;

    public static ComponentUI createUI ( JComponent c )
    {
        return new WebLabelUI ();
    }

    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Saving label to local variable
        label = ( JLabel ) c;

        // Default settings
        SwingUtils.setOrientation ( label );
        label.setBackground ( WebLabelStyle.backgroundColor );

        // Updating border
        updateBorder ();

        // Orientation change listener
        propertyChangeListener = new PropertyChangeListener ()
        {
            public void propertyChange ( PropertyChangeEvent evt )
            {
                updateBorder ();
            }
        };
        label.addPropertyChangeListener ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY, propertyChangeListener );
    }

    public void uninstallUI ( JComponent c )
    {
        label.removePropertyChangeListener ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY, propertyChangeListener );

        label = null;

        super.uninstallUI ( c );
    }

    private void updateBorder ()
    {
        if ( label != null )
        {
            // Actual margin
            boolean ltr = label.getComponentOrientation ().isLeftToRight ();
            Insets m = new Insets ( margin.top, ltr ? margin.left : margin.right, margin.bottom, ltr ? margin.right : margin.left );

            // Calculating additional borders
            if ( painter != null )
            {
                // Painter borders
                Insets pi = painter.getMargin ( label );
                m.top += pi.top;
                m.bottom += pi.bottom;
                m.left += ltr ? pi.left : pi.right;
                m.right += ltr ? pi.right : pi.left;
            }

            // Installing border
            label.setBorder ( LafUtils.createWebBorder ( m ) );
        }
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

    public Painter getPainter ()
    {
        return painter;
    }

    public void setPainter ( Painter painter )
    {
        this.painter = painter;
        updateBorder ();
    }

    public boolean isDrawShade ()
    {
        return drawShade;
    }

    public void setDrawShade ( boolean drawShade )
    {
        this.drawShade = drawShade;
        label.repaint ();
    }

    public Color getShadeColor ()
    {
        return shadeColor;
    }

    public void setShadeColor ( Color shadeColor )
    {
        this.shadeColor = shadeColor;
        label.repaint ();
    }

    public Float getTransparency ()
    {
        return transparency;
    }

    public void setTransparency ( Float transparency )
    {
        this.transparency = transparency;
        label.repaint ();
    }

    public void paint ( Graphics g, JComponent c )
    {
        final Graphics2D g2d = ( Graphics2D ) g;
        final Composite oc = LafUtils.setupAlphaComposite ( g2d, transparency, transparency != null );

        // Use background painter instead of default UI graphics
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, SwingUtils.size ( c ), c );
        }

        final Map textHints = drawShade ? StyleConstants.defaultTextRenderingHints : StyleConstants.textRenderingHints;
        final Map oldHints = SwingUtils.setupTextAntialias ( g2d, c, textHints );
        super.paint ( g, c );
        SwingUtils.restoreTextAntialias ( g2d, oldHints );

        LafUtils.restoreComposite ( g2d, oc, transparency != null );
    }

    protected void paintEnabledText ( JLabel l, Graphics g, String s, int textX, int textY )
    {
        if ( drawShade )
        {
            g.setColor ( l.getForeground () );
            paintShadowText ( g, s, textX, textY );
        }
        else
        {
            super.paintEnabledText ( l, g, s, textX, textY );
            //            int mnemIndex = l.getDisplayedMnemonicIndex ();
            //            g.setColor ( l.getForeground () );
            //            SwingUtils.drawStringUnderlineCharAt ( l, g, s, mnemIndex, textX, textY );
        }
    }

    protected void paintDisabledText ( JLabel l, Graphics g, String s, int textX, int textY )
    {
        if ( l.isEnabled () && drawShade )
        {
            g.setColor ( l.getBackground ().darker () );
            paintShadowText ( g, s, textX, textY );
        }
        else
        {
            super.paintDisabledText ( l, g, s, textX, textY );
            //            int accChar = l.getDisplayedMnemonicIndex ();
            //            Color background = l.getBackground ();
            //            g.setColor ( background.brighter () );
            //            SwingUtils.drawStringUnderlineCharAt ( l, g, s, accChar, textX + 1, textY + 1 );
            //            g.setColor ( background.darker () );
            //            SwingUtils.drawStringUnderlineCharAt ( l, g, s, accChar, textX, textY );
        }
    }

    private void paintShadowText ( Graphics g, String s, int textX, int textY )
    {
        g.translate ( textX, textY );
        LafUtils.paintTextShadow ( ( Graphics2D ) g, s, shadeColor );
        g.translate ( -textX, -textY );
    }

    public Dimension getPreferredSize ( JComponent c )
    {
        Dimension ps = super.getPreferredSize ( c );
        if ( painter != null )
        {
            if ( c.getLayout () != null )
            {
                ps = SwingUtils.max ( ps, c.getLayout ().preferredLayoutSize ( c ) );
            }
            ps = SwingUtils.max ( ps, painter.getPreferredSize ( c ) );
        }
        return ps;
    }
}