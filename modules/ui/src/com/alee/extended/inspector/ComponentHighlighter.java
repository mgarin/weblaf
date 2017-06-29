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

import com.alee.extended.behavior.ComponentVisibilityBehavior;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.glasspane.GlassPaneManager;
import com.alee.managers.glasspane.WebGlassPane;
import com.alee.managers.style.MarginSupport;
import com.alee.managers.style.PaddingSupport;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.util.Map;

/**
 * Custom component that visually displays component margin, border, padding and content areas.
 * It is designed to be used only as a part of {@link InterfaceTree}.
 *
 * @author Mikle Garin
 */

public final class ComponentHighlighter extends JComponent implements ComponentListener, HierarchyBoundsListener
{
    /**
     * Basic color scheme.
     */
    public static final Color marginColor = new Color ( 255, 255, 0, 50 );
    public static final Color borderColor = new Color ( 255, 0, 0, 50 );
    public static final Color paddingColor = new Color ( 0, 200, 0, 50 );
    public static final Color contentColor = new Color ( 0, 0, 255, 50 );

    /**
     * Internal constants.
     */
    private static final Insets emptyInsets = new Insets ( 0, 0, 0, 0 );
    private static final int sizeTipHeight = 20;

    /**
     * Highlighted component.
     */
    private Component component;

    /**
     * Highlighted component visibility listener.
     */
    private ComponentVisibilityBehavior visibilityListener;

    /**
     * Highlighter glasspane.
     */
    private WebGlassPane glassPane;

    /**
     * Constructs new component highlighter.
     */
    public ComponentHighlighter ()
    {
        super ();
        setOpaque ( false );
        setFont ( WebLookAndFeel.globalTextFont );
    }

    /**
     * Displays this highlighter over the specified component.
     *
     * @param component component to be highlighted
     */
    public void install ( final Component component )
    {
        if ( this.component == null && this.glassPane == null )
        {
            this.component = component;
            this.glassPane = GlassPaneManager.getGlassPane ( component );

            // Updating highligther position
            updateBounds ();

            // Adding highligthed component listeners
            component.addComponentListener ( this );
            component.addHierarchyBoundsListener ( this );
            visibilityListener = new ComponentVisibilityBehavior ( component )
            {
                @Override
                public void displayed ()
                {
                    // Ignored event
                }

                @Override
                public void hidden ()
                {
                    ComponentHighlighter.this.uninstall ();
                }
            };
            visibilityListener.install ();

            // Displaying highlighter on glass pane
            glassPane.showComponent ( this );
        }
    }

    /**
     * Hides this component highlighter.
     */
    public void uninstall ()
    {
        if ( component != null && glassPane != null )
        {
            // Hiding highlighter from glass pane
            glassPane.hideComponent ( this );
            glassPane = null;

            // Removing listeners and references
            visibilityListener.uninstall ();
            visibilityListener = null;
            component.removeHierarchyBoundsListener ( this );
            component.removeComponentListener ( this );
            component = null;
        }
    }

    /**
     * Returns highlighted component.
     *
     * @return highlighted component
     */
    public Component getComponent ()
    {
        return component;
    }

    @Override
    public void ancestorMoved ( final HierarchyEvent e )
    {
        updateBounds ();
    }

    @Override
    public void ancestorResized ( final HierarchyEvent e )
    {
        updateBounds ();
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
        // Ignored event
    }

    @Override
    public void componentHidden ( final ComponentEvent e )
    {
        uninstall ();
    }

    /**
     * Updates highlighter position.
     */
    private void updateBounds ()
    {
        final Component glassPane = SwingUtils.getGlassPane ( component );
        final Rectangle rb = SwingUtils.getRelativeBounds ( component, glassPane );
        final int tipWidth = getFontMetrics ( getFont () ).stringWidth ( getSizeTip () ) + 8;
        setBounds ( new Rectangle ( rb.x, rb.y - sizeTipHeight, Math.max ( tipWidth, rb.width ), rb.height + sizeTipHeight ) );
    }

    /**
     * Returns highlighted component size tip text.
     *
     * @return highlighted component size tip text
     */
    private String getSizeTip ()
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
    private void paintSizeTip ( final Graphics2D g2d )
    {
        final FontMetrics fm = g2d.getFontMetrics ( g2d.getFont () );
        final String sizeTip = getSizeTip ();
        final int shearY = LafUtils.getTextCenterShiftY ( fm );
        final int tipWidth = fm.stringWidth ( sizeTip ) + 8 - 1;
        final int tipHeight = sizeTipHeight - 1;
        final GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        gp.moveTo ( 0, 4 );
        gp.quadTo ( 0, 0, 4, 0 );
        gp.lineTo ( tipWidth - 4, 0 );
        gp.quadTo ( tipWidth, 0, tipWidth, 4 );
        if ( component.getWidth () < tipWidth )
        {
            gp.lineTo ( tipWidth, tipHeight - 4 );
            gp.quadTo ( tipWidth, tipHeight, tipWidth - 4, tipHeight );
        }
        else
        {
            gp.lineTo ( tipWidth, tipHeight );
        }
        gp.lineTo ( 0, tipHeight );
        gp.closePath ();

        // Background
        final Object aa = GraphicsUtils.setupAntialias ( g2d );
        g2d.setPaint ( Color.WHITE );
        g2d.fill ( gp );
        g2d.setPaint ( Color.BLACK );
        g2d.draw ( gp );
        GraphicsUtils.restoreAntialias ( g2d, aa );

        // Text
        final Map taa = SwingUtils.setupTextAntialias ( g2d );
        g2d.setPaint ( Color.BLACK );
        g2d.drawString ( sizeTip, 4, sizeTipHeight / 2 + shearY );
        SwingUtils.restoreTextAntialias ( g2d, taa );
    }

    /**
     * Paints component areas.
     *
     * @param g2d graphics context
     */
    private void paintAreas ( final Graphics2D g2d )
    {
        final Rectangle bounds = new Rectangle ( 0, sizeTipHeight, component.getWidth (), getHeight () - sizeTipHeight );
        if ( LafUtils.hasUI ( component ) )
        {
            final ComponentUI ui = LafUtils.getUI ( component );
            if ( ui != null )
            {
                // todo Probably request these settings through other means?
                // todo Through Bounds maybe?

                // Component margin
                final Insets margin = ui instanceof MarginSupport ? ( ( MarginSupport ) ui ).getMargin () : null;
                final Insets m = margin != null ? margin : emptyInsets;

                // Component padding
                final Insets padding = ui instanceof PaddingSupport ? ( ( PaddingSupport ) ui ).getPadding () : null;
                final Insets p = padding != null ? padding : emptyInsets;

                // Component painter border
                final Insets insets = component instanceof JComponent ? ( ( JComponent ) component ).getInsets () : emptyInsets;
                final Insets b;
                if ( insets != null && ( insets.top > 0 || insets.left > 0 || insets.bottom > 0 || insets.right > 0 ) )
                {
                    b = new Insets ( insets.top - m.top - p.top, insets.left - m.left - p.left,
                            insets.bottom - m.bottom - p.bottom, insets.right - m.right - p.right );
                }
                else
                {
                    b = emptyInsets;
                }

                // Computing area sizes
                final Rectangle sr = new Rectangle ( bounds );
                final Rectangle mr = new Rectangle ( sr.x + m.left, sr.y + m.top,
                        sr.width - m.left - m.right, sr.height - m.top - m.bottom );
                final Rectangle br = new Rectangle ( mr.x + b.left, mr.y + b.top,
                        mr.width - b.left - b.right, mr.height - b.top - b.bottom );
                final Rectangle pr = new Rectangle ( br.x + p.left, br.y + p.top,
                        br.width - p.left - p.right, br.height - p.top - p.bottom );

                // Painting component margin area
                paintComplexArea ( g2d, m, sr, mr, marginColor );

                // Painting component painter border area
                paintComplexArea ( g2d, b, mr, br, borderColor );

                // Painting component padding area
                paintComplexArea ( g2d, p, br, pr, paddingColor );

                // Painting component bounds area
                paintContentArea ( g2d, pr );
            }
            else
            {
                // Painting component bounds area
                paintContentArea ( g2d, bounds );
            }
        }
        else
        {
            // Painting component bounds area
            paintContentArea ( g2d, bounds );
        }
    }

    /**
     * Paints complex component area.
     *
     * @param g2d    graphics context
     * @param insets area insets
     * @param outer  outer area bounds
     * @param inner  inner area bounds
     * @param color  area color
     */
    private void paintComplexArea ( final Graphics2D g2d, final Insets insets, final Rectangle outer, final Rectangle inner,
                                    final Color color )
    {
        if ( insets != emptyInsets )
        {
            g2d.setPaint ( color );
            final Area ma = new Area ( outer );
            ma.exclusiveOr ( new Area ( inner ) );
            g2d.fill ( ma );
        }
    }

    /**
     * Paints component content area.
     *
     * @param g2d    graphics context
     * @param bounds content area bounds
     */
    private void paintContentArea ( final Graphics2D g2d, final Rectangle bounds )
    {
        g2d.setPaint ( contentColor );
        g2d.fill ( bounds );
    }
}