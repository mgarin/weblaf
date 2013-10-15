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
import com.alee.extended.painter.PainterSupport;
import com.alee.laf.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.BorderMethods;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

/**
 * Custom UI for JLabel component.
 *
 * @author Mikle Garin
 */

public class WebLabelUI extends BasicLabelUI implements BorderMethods
{
    /**
     * Style settings.
     */
    protected Insets margin = WebLabelStyle.margin;
    protected Painter painter = WebLabelStyle.painter;
    protected boolean drawShade = WebLabelStyle.drawShade;
    protected Color shadeColor = WebLabelStyle.shadeColor;
    protected Float transparency = WebLabelStyle.transparency;

    /**
     * JLabel instance to which this UI is applied.
     */
    protected JLabel label;

    /**
     * Label listeners.
     */
    protected PropertyChangeListener propertyChangeListener;

    /**
     * Returns an instance of the WebLabelUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebLabelUI
     */
    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebLabelUI ();
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

        // Saving label to local variable
        label = ( JLabel ) c;

        // Default settings
        SwingUtils.setOrientation ( label );
        label.setBackground ( WebLabelStyle.backgroundColor );
        PainterSupport.installPainter ( label, this.painter );
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
        label.addPropertyChangeListener ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY, propertyChangeListener );
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
        PainterSupport.uninstallPainter ( label, this.painter );

        // Removing label listeners
        label.removePropertyChangeListener ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY, propertyChangeListener );

        // Clearing link to label component
        label = null;

        super.uninstallUI ( c );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBorder ()
    {
        if ( label != null )
        {
            // Actual margin
            final boolean ltr = label.getComponentOrientation ().isLeftToRight ();
            final Insets m = new Insets ( margin.top, ltr ? margin.left : margin.right, margin.bottom, ltr ? margin.right : margin.left );

            // Calculating additional borders
            if ( painter != null )
            {
                // Painter borders
                final Insets pi = painter.getMargin ( label );
                m.top += pi.top;
                m.bottom += pi.bottom;
                m.left += ltr ? pi.left : pi.right;
                m.right += ltr ? pi.right : pi.left;
            }

            // Installing border
            label.setBorder ( LafUtils.createWebBorder ( m ) );
        }
    }

    /**
     * Returns component margin.
     *
     * @return component margin
     */
    public Insets getMargin ()
    {
        return margin;
    }

    /**
     * Sets component margin.
     *
     * @param margin component margin
     */
    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        updateBorder ();
    }

    /**
     * Returns component painter.
     *
     * @return component painter
     */
    public Painter getPainter ()
    {
        return painter;
    }

    /**
     * Sets component painter.
     *
     * @param painter component painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.uninstallPainter ( label, this.painter );
        this.painter = painter;
        PainterSupport.installPainter ( label, this.painter );
        updateBorder ();
    }

    /**
     * Returns whether text shade is displayed or not.
     *
     * @return true if text shade is displayed, false otherwise
     */
    public boolean isDrawShade ()
    {
        return drawShade;
    }

    /**
     * Sets whether text shade should be displayed or not.
     *
     * @param drawShade whether text shade should be displayed or not
     */
    public void setDrawShade ( final boolean drawShade )
    {
        this.drawShade = drawShade;
        label.repaint ();
    }

    /**
     * Returns text shade color.
     *
     * @return text shade color
     */
    public Color getShadeColor ()
    {
        return shadeColor;
    }

    /**
     * Sets text shade color.
     *
     * @param shadeColor text shade color
     */
    public void setShadeColor ( final Color shadeColor )
    {
        this.shadeColor = shadeColor;
        label.repaint ();
    }

    /**
     * Returns label transparency.
     *
     * @return label transparency
     */
    public Float getTransparency ()
    {
        return transparency;
    }

    /**
     * Sets label transparency.
     *
     * @param transparency label transparency
     */
    public void setTransparency ( final Float transparency )
    {
        this.transparency = transparency;
        label.repaint ();
    }

    /**
     * Paints label.
     *
     * @param g graphics
     * @param c component
     */
    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        final Graphics2D g2d = ( Graphics2D ) g;

        // Force painter to draw background
        if ( painter != null )
        {
            painter.paint ( g2d, SwingUtils.size ( c ), c );
        }

        final Composite oc = LafUtils.setupAlphaComposite ( g2d, transparency, transparency != null );
        final Map textHints = drawShade ? StyleConstants.defaultTextRenderingHints : StyleConstants.textRenderingHints;
        final Map oldHints = SwingUtils.setupTextAntialias ( g2d, textHints );
        super.paint ( g, c );
        SwingUtils.restoreTextAntialias ( g2d, oldHints );
        LafUtils.restoreComposite ( g2d, oc, transparency != null );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintEnabledText ( final JLabel l, final Graphics g, final String s, final int textX, final int textY )
    {
        if ( drawShade )
        {
            g.setColor ( l.getForeground () );
            paintShadowText ( g, s, textX, textY );
        }
        else
        {
            super.paintEnabledText ( l, g, s, textX, textY );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintDisabledText ( final JLabel l, final Graphics g, final String s, final int textX, final int textY )
    {
        if ( l.isEnabled () && drawShade )
        {
            g.setColor ( l.getBackground ().darker () );
            paintShadowText ( g, s, textX, textY );
        }
        else
        {
            super.paintDisabledText ( l, g, s, textX, textY );
        }
    }

    /**
     * Paints custom text shade.
     *
     * @param g     graphics context
     * @param s     text
     * @param textX text X coordinate
     * @param textY text Y coordinate
     */
    protected void paintShadowText ( final Graphics g, final String s, final int textX, final int textY )
    {
        g.translate ( textX, textY );
        LafUtils.paintTextShadow ( ( Graphics2D ) g, s, shadeColor );
        g.translate ( -textX, -textY );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize ( final JComponent c )
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