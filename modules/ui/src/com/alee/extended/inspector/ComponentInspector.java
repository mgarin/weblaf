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

package com.alee.extended.inspector;

import com.alee.laf.WebFonts;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.managers.style.MarginSupport;
import com.alee.managers.style.PaddingSupport;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.util.Map;

/**
 * Custom component that visually displays component margin, border, padding and content areas.
 * It is designed to be used only as a part of {@link com.alee.extended.inspector.InterfaceTree}.
 *
 * @author Mikle Garin
 * @see com.alee.extended.inspector.InterfaceTree
 */

public final class ComponentInspector extends JComponent implements ComponentListener
{
    /**
     * Internal constants.
     */
    private static final Insets emptyInsets = new Insets ( 0, 0, 0, 0 );
    private static final Color marginColor = new Color ( 255, 255, 0, 50 );
    private static final Color borderColor = new Color ( 255, 0, 0, 50 );
    private static final Color paddingColor = new Color ( 0, 200, 0, 50 );
    private static final Color contentColor = new Color ( 0, 0, 255, 50 );
    private static final int sizeTipHeight = 20;

    /**
     * Inspected component.
     */
    private Component component;

    /**
     * Constructs new component inspector.
     */
    public ComponentInspector ()
    {
        super ();
        setOpaque ( false );
        setFont ( WebFonts.getSystemTextFont () );
    }

    /**
     * Displays this inspector over the specified component.
     *
     * @param component component to be inspected
     */
    public void install ( final Component component )
    {
        this.component = component;
        updateBounds ();
        component.addComponentListener ( this );
    }

    /**
     * Hides this component inspector.
     */
    public void uninstall ()
    {
        component.removeComponentListener ( this );
        component = null;
    }

    @Override
    public void componentResized ( final ComponentEvent e )
    {
        updateBounds ();
    }

    @Override
    public void componentMoved ( final ComponentEvent e )
    {
        updateBounds ();
    }

    @Override
    public void componentShown ( final ComponentEvent e )
    {
        updateBounds ();
    }

    @Override
    public void componentHidden ( final ComponentEvent e )
    {
        updateBounds ();
    }

    /**
     * Updates inspector position.
     */
    protected void updateBounds ()
    {
        final Component glassPane = SwingUtils.getGlassPane ( component );
        final Rectangle rb = SwingUtils.getRelativeBounds ( component, glassPane );
        final int tipWidth = getFontMetrics ( getFont () ).stringWidth ( getSizeTip () ) + 8;
        setBounds ( new Rectangle ( rb.x, rb.y - sizeTipHeight, Math.max ( tipWidth, rb.width ), rb.height + sizeTipHeight ) );
    }

    /**
     * Returns inspected component size tip text.
     *
     * @return inspected component size tip text
     */
    protected String getSizeTip ()
    {
        return component.getWidth () + " x " + component.getHeight () + " px";
    }

    @Override
    protected void paintComponent ( final Graphics g )
    {
        if ( component != null )
        {
            final Graphics2D g2d = ( Graphics2D ) g;

            // Painting size tip
            paintSizeTip ( g2d );

            // Painting areas
            paintAreas ( g2d );
        }
    }

    /**
     * Paints component size tip.
     *
     * @param g2d graphics context
     */
    protected void paintSizeTip ( final Graphics2D g2d )
    {
        final FontMetrics fm = g2d.getFontMetrics ( g2d.getFont () );
        final String sizeTip = getSizeTip ();
        final int shearY = LafUtils.getTextCenterShiftY ( fm );
        final int tipWidth = fm.stringWidth ( sizeTip ) + 8;
        final GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        gp.moveTo ( 0, 4 );
        gp.quadTo ( 0, 0, 4, 0 );
        gp.lineTo ( tipWidth - 4, 0 );
        gp.quadTo ( tipWidth, 0, tipWidth, 4 );
        if ( component.getWidth () < tipWidth )
        {
            gp.lineTo ( tipWidth, sizeTipHeight - 4 );
            gp.quadTo ( tipWidth, sizeTipHeight, tipWidth - 4, sizeTipHeight );
        }
        else
        {
            gp.lineTo ( tipWidth, sizeTipHeight );
        }
        gp.lineTo ( 0, sizeTipHeight );
        gp.closePath ();

        // Background
        final Object aa = GraphicsUtils.setupAntialias ( g2d );
        g2d.setPaint ( Color.BLACK );
        g2d.fill ( gp );
        GraphicsUtils.restoreAntialias ( g2d, aa );

        // Text
        final Map taa = SwingUtils.setupTextAntialias ( g2d );
        g2d.setPaint ( Color.WHITE );
        g2d.drawString ( sizeTip, 4, sizeTipHeight / 2 + shearY );
        SwingUtils.restoreTextAntialias ( g2d, taa );
    }

    /**
     * Paints component areas.
     *
     * @param g2d graphics context
     */
    protected void paintAreas ( final Graphics2D g2d )
    {
        final ComponentUI ui = LafUtils.getUI ( component );
        final Rectangle bounds = new Rectangle ( 0, sizeTipHeight, component.getWidth (), getHeight () - sizeTipHeight );
        if ( ui != null )
        {
            // Component margin
            Insets m = ui instanceof MarginSupport ? ( ( MarginSupport ) ui ).getMargin () : null;
            m = m != null ? m : emptyInsets;

            // Component padding
            Insets p = ui instanceof PaddingSupport ? ( ( PaddingSupport ) ui ).getPadding () : null;
            p = p != null ? p : emptyInsets;

            // Component painter border
            Insets b = ( ( JComponent ) component ).getInsets ();
            b = b != null && ( b.top > 0 || b.left > 0 || b.bottom > 0 || b.right > 0 ) ?
                    new Insets ( b.top - m.top - p.top, b.left - m.left - p.left, b.bottom - m.bottom - p.bottom,
                            b.right - m.right - p.right ) : emptyInsets;

            // Computing area sizes
            final Rectangle sr = new Rectangle ( bounds );
            final Rectangle mr = new Rectangle ( sr.x + m.left, sr.y + m.top, sr.width - m.left - m.right, sr.height - m.top - m.bottom );
            final Rectangle br = new Rectangle ( mr.x + b.left, mr.y + b.top, mr.width - b.left - b.right, mr.height - b.top - b.bottom );
            final Rectangle pr = new Rectangle ( br.x + p.left, br.y + p.top, br.width - p.left - p.right, br.height - p.top - p.bottom );

            // Painting component margin area
            if ( m != emptyInsets )
            {
                g2d.setPaint ( marginColor );
                final Area ma = new Area ( sr );
                ma.exclusiveOr ( new Area ( mr ) );
                g2d.fill ( ma );
            }

            // Painting component painter border area
            if ( b != emptyInsets )
            {
                g2d.setPaint ( borderColor );
                final Area ba = new Area ( mr );
                ba.exclusiveOr ( new Area ( br ) );
                g2d.fill ( ba );
            }

            // Painting component padding area
            if ( p != emptyInsets )
            {
                g2d.setPaint ( paddingColor );
                final Area pa = new Area ( br );
                pa.exclusiveOr ( new Area ( pr ) );
                g2d.fill ( pa );
            }

            // Painting component bounds area
            g2d.setPaint ( contentColor );
            g2d.fill ( pr );
        }
        else
        {
            // Painting component bounds area
            g2d.setPaint ( contentColor );
            g2d.fill ( bounds );
        }
    }
}