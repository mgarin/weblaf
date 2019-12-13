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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.data.CompassDirection;
import com.alee.api.jdk.Objects;
import com.alee.extended.behavior.VisibilityBehavior;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.glasspane.GlassPaneManager;
import com.alee.managers.glasspane.WebGlassPane;
import com.alee.painter.PainterSupport;
import com.alee.painter.decoration.shape.Round;
import com.alee.painter.decoration.shape.ShapeType;
import com.alee.painter.decoration.shape.Sides;
import com.alee.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.geom.Area;
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
    @Nullable
    private transient Component component;

    /**
     * Highlighter glasspane.
     */
    @Nullable
    private transient WebGlassPane glassPane;

    /**
     * Highlighted component visibility listener.
     */
    @Nullable
    private transient VisibilityBehavior visibilityListener;

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
    public void install ( @NotNull final Component component )
    {
        if ( this.component == null && this.glassPane == null && visibilityListener == null )
        {
            this.component = Objects.requireNonNull ( component, "Component cannot be null" );

            // Retrieving glass pane
            final WebGlassPane webGlassPane = GlassPaneManager.getGlassPane ( component );
            this.glassPane = Objects.requireNonNull ( webGlassPane, "Component window must have a glass pane" );

            // Updating highligther position
            updateBounds ();

            // Adding highligthed component listeners
            component.addComponentListener ( this );
            component.addHierarchyBoundsListener ( this );
            visibilityListener = new VisibilityBehavior<Component> ( component )
            {
                @Override
                protected void hidden ( @NotNull final Component component )
                {
                    ComponentHighlighter.this.uninstall ();
                }
            };
            visibilityListener.install ();

            // Displaying highlighter on glass pane
            this.glassPane.showComponent ( this );
        }
    }

    /**
     * Hides this component highlighter.
     */
    public void uninstall ()
    {
        if ( component != null && glassPane != null && visibilityListener != null )
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
    @Nullable
    public Component getComponent ()
    {
        return component;
    }

    @Override
    public void ancestorMoved ( @NotNull final HierarchyEvent e )
    {
        updateBounds ();
    }

    @Override
    public void ancestorResized ( @NotNull final HierarchyEvent e )
    {
        updateBounds ();
    }

    @Override
    public void componentResized ( @NotNull final ComponentEvent e )
    {
        updateBounds ();
    }

    @Override
    public void componentMoved ( @NotNull final ComponentEvent e )
    {
        updateBounds ();
    }

    @Override
    public void componentShown ( @NotNull final ComponentEvent e )
    {
        // Ignored event
    }

    @Override
    public void componentHidden ( @NotNull final ComponentEvent e )
    {
        uninstall ();
    }

    /**
     * Updates highlighter position.
     */
    private void updateBounds ()
    {
        if ( component != null && glassPane != null )
        {
            final Dimension glassPaneSize = glassPane.getSize ();
            final Rectangle componentBounds = CoreSwingUtils.getRelativeBounds ( component, glassPane );
            final int tipWidth = getTipWidth ();
            final CompassDirection tipDirection = getTipDirection ( tipWidth, glassPaneSize, componentBounds );
            final Rectangle tipBounds = getTipBounds ( true, tipWidth, tipDirection, componentBounds );
            final Rectangle bodyBounds = getBodyBounds ( true, tipWidth, tipDirection, componentBounds );
            setBounds ( GeometryUtils.getNonNullContainingRect ( tipBounds, bodyBounds ) );
        }
    }

    /**
     * Returns tip width.
     *
     * @return tip width
     */
    private int getTipWidth ()
    {
        return getFontMetrics ( getFont () ).stringWidth ( getSizeTip () ) + 8;
    }

    /**
     * Returns tip {@link CompassDirection}.
     *
     * @param tipWidth        tip width
     * @param glassPaneSize   glass pane size
     * @param componentBounds inspected component bounds
     * @return tip {@link CompassDirection}
     */
    @NotNull
    private CompassDirection getTipDirection ( final int tipWidth, @NotNull final Dimension glassPaneSize,
                                               @NotNull final Rectangle componentBounds )
    {
        final CompassDirection direction;
        final boolean fromLeft = componentBounds.x + tipWidth < glassPaneSize.width ||
                componentBounds.x + componentBounds.width - tipWidth < 0;
        if ( componentBounds.y > sizeTipHeight )
        {
            direction = fromLeft ? CompassDirection.northWest : CompassDirection.northEast;
        }
        else
        {
            direction = fromLeft ? CompassDirection.southWest : CompassDirection.southEast;
        }
        return direction;
    }

    /**
     * Returns tip bounds.
     *
     * @param relative        whether bounds should be relative to position on glass pane or not
     * @param tipWidth        tip width
     * @param tipDirection    tip {@link CompassDirection}
     * @param componentBounds inspected component bounds
     * @return tip bounds
     */
    @NotNull
    private Rectangle getTipBounds ( final boolean relative, final int tipWidth, @NotNull final CompassDirection tipDirection,
                                     @NotNull final Rectangle componentBounds )
    {
        final int x;
        if ( tipDirection == CompassDirection.northWest || tipDirection == CompassDirection.southWest )
        {
            x = relative ? componentBounds.x : 0;
        }
        else
        {
            x = relative ? componentBounds.x + componentBounds.width - tipWidth : 0;
        }
        final int y;
        if ( tipDirection == CompassDirection.northEast || tipDirection == CompassDirection.northWest )
        {
            y = relative ? componentBounds.y - sizeTipHeight : 0;
        }
        else
        {
            y = relative ? componentBounds.y + componentBounds.height : componentBounds.height;
        }
        final int w = relative ? tipWidth : tipWidth - 1;
        final int h = relative ? sizeTipHeight : sizeTipHeight - 1;
        return new Rectangle ( x, y, w, h );
    }

    /**
     * Returns body bounds.
     *
     * @param relative        whether bounds should be relative to position on glass pane or not
     * @param tipWidth        tip width
     * @param tipDirection    tip {@link CompassDirection}
     * @param componentBounds inspected component bounds
     * @return body bounds
     */
    @NotNull
    private Rectangle getBodyBounds ( final boolean relative, final int tipWidth, @NotNull final CompassDirection tipDirection,
                                      @NotNull final Rectangle componentBounds )
    {
        final int x;
        if ( tipDirection == CompassDirection.northWest || tipDirection == CompassDirection.southWest )
        {
            x = relative ? componentBounds.x : 0;
        }
        else
        {
            x = relative ? componentBounds.x : tipWidth - componentBounds.width;
        }
        final int y;
        if ( tipDirection == CompassDirection.northEast || tipDirection == CompassDirection.northWest )
        {
            y = relative ? componentBounds.y : sizeTipHeight;
        }
        else
        {
            y = relative ? componentBounds.y : 0;
        }
        final int w = componentBounds.width;
        final int h = componentBounds.height;
        return new Rectangle ( x, y, w, h );
    }

    /**
     * Returns highlighted component size tip text.
     *
     * @return highlighted component size tip text
     */
    @NotNull
    private String getSizeTip ()
    {
        return component != null ? component.getWidth () + " x " + component.getHeight () + " px" : "? x ?";
    }

    @Override
    protected void paintComponent ( @NotNull final Graphics g )
    {
        if ( component != null && glassPane != null )
        {
            paintHighlight ( ( Graphics2D ) g, component, glassPane );
        }
    }

    /**
     * Paints highlight.
     *
     * @param g2d       {@link Graphics2D}
     * @param component {@link Component} to highlight
     * @param glassPane {@link WebGlassPane}
     */
    private void paintHighlight ( @NotNull final Graphics2D g2d, @NotNull final Component component,
                                  @NotNull final WebGlassPane glassPane )
    {
        final Dimension glassPaneSize = glassPane.getSize ();
        final Rectangle componentBounds = CoreSwingUtils.getRelativeBounds ( component, glassPane );
        final int tipWidth = getTipWidth ();
        final CompassDirection tipPosition = getTipDirection ( tipWidth, glassPaneSize, componentBounds );
        final Rectangle tipBounds = getTipBounds ( false, tipWidth, tipPosition, componentBounds );
        final Rectangle bodyBounds = getBodyBounds ( false, tipWidth, tipPosition, componentBounds );

        // Painting size tip
        paintSizeTip ( g2d, component, tipBounds, tipPosition );

        // Painting areas
        paintAreas ( g2d, component, bodyBounds );
    }

    /**
     * Paints component size tip.
     *
     * @param g2d         {@link Graphics2D}
     * @param component   {@link Component} to highlight
     * @param tipBounds   tip bounds
     * @param tipPosition tip position
     */
    private void paintSizeTip ( @NotNull final Graphics2D g2d, @NotNull final Component component, @NotNull final Rectangle tipBounds,
                                @NotNull final CompassDirection tipPosition )
    {
        final FontMetrics fm = g2d.getFontMetrics ( g2d.getFont () );
        final int shearY = LafUtils.getTextCenterShiftY ( fm );

        // Creating shape
        final boolean top = tipPosition == CompassDirection.northWest || tipPosition == CompassDirection.northEast;
        final boolean left = tipPosition == CompassDirection.northWest || tipPosition == CompassDirection.southWest;
        final boolean longTip = component.getWidth () < tipBounds.width;

        final Object aa = GraphicsUtils.setupAntialias ( g2d );

        final Round round = new Round (
                top || !left && longTip ? 5 : 0,
                top || left && longTip ? 5 : 0,
                !top || left && longTip ? 5 : 0,
                !top || !left && longTip ? 5 : 0
        );
        final Sides sides = new Sides ( true );

        g2d.setPaint ( Color.WHITE );
        g2d.fill ( ShapeUtils.createFillShape ( 0, tipBounds, round, sides, ShapeType.background ) );

        g2d.setPaint ( Color.DARK_GRAY );
        g2d.draw ( ShapeUtils.createBorderShape ( 0, tipBounds, round, sides ) );

        GraphicsUtils.restoreAntialias ( g2d, aa );

        // Text
        final Map taa = SwingUtils.setupTextAntialias ( g2d );
        g2d.setPaint ( Color.BLACK );
        g2d.drawString ( getSizeTip (), tipBounds.x + 4, tipBounds.y + tipBounds.height / 2 + 1 + shearY );
        SwingUtils.restoreTextAntialias ( g2d, taa );
    }

    /**
     * Paints component areas.
     *
     * @param g2d        {@link Graphics2D}
     * @param component  {@link Component} to highlight
     * @param bodyBounds body bounds
     */
    private void paintAreas ( @NotNull final Graphics2D g2d, @NotNull final Component component, @NotNull final Rectangle bodyBounds )
    {
        if ( component instanceof JComponent && LafUtils.hasWebLafUI ( ( JComponent ) component ) )
        {
            final JComponent jComponent = ( JComponent ) component;

            // Component margin
            final Insets margin = PainterSupport.getMargin ( jComponent, true );
            final Insets m = margin != null ? margin : emptyInsets;

            // Component padding
            final Insets padding = PainterSupport.getPadding ( jComponent, true );
            final Insets p = padding != null ? padding : emptyInsets;

            // Component painter border
            final Insets insets = jComponent.getInsets ();
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
            final Rectangle sr = new Rectangle ( bodyBounds );
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
            paintContentArea ( g2d, bodyBounds );
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
    private void paintComplexArea ( @NotNull final Graphics2D g2d, @NotNull final Insets insets, @NotNull final Rectangle outer,
                                    @NotNull final Rectangle inner, @NotNull final Color color )
    {
        if ( !insets.equals ( emptyInsets ) )
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
    private void paintContentArea ( @NotNull final Graphics2D g2d, @NotNull final Rectangle bounds )
    {
        g2d.setPaint ( contentColor );
        g2d.fill ( bounds );
    }

    /**
     * Ensure we are as hidden as possible.
     * This will also ensure that other {@link ComponentHighlighter}s cannot see us.
     */
    @Override
    public boolean contains ( final int x, final int y )
    {
        return false;
    }
}