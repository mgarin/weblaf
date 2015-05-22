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

package com.alee.laf.button;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.PainterSupport;
import com.alee.managers.style.StyleManager;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.laf.Styleable;
import com.alee.utils.swing.DataRunnable;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

/**
 * Custom UI for JButton component.
 *
 * @author Mikle Garin
 */

public class WebButtonUI extends BasicButtonUI implements Styleable, ShapeProvider, SwingConstants
{
    /**
     * Component painter.
     */
    protected ButtonPainter painter;

    /**
     * Runtime variables.
     */
    protected String styleId = null;
    protected AbstractButton button = null;

    /**
     * Cached old values from button to restore on LAF change.
     */
    protected boolean oldFocusPainted;
    protected boolean oldContentAreaFilled;
    protected boolean oldBorderPainted;
    protected boolean oldFocusable;

    /**
     * Returns an instance of the WebButtonUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebButtonUI
     */
    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebButtonUI ();
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

        // Saving button to local variable
        button = ( AbstractButton ) c;

        // Saving old button settings
        oldFocusPainted = button.isFocusPainted ();
        oldContentAreaFilled = button.isContentAreaFilled ();
        oldBorderPainted = button.isBorderPainted ();
        oldFocusable = button.isFocusable ();

        // Applying skin
        StyleManager.applySkin ( button );
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.removeSkin ( button );

        // Restoring old button settings
        button.setBorderPainted ( oldBorderPainted );
        button.setContentAreaFilled ( oldContentAreaFilled );
        button.setFocusPainted ( oldFocusPainted );
        button.setFocusable ( oldFocusable );

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
        StyleManager.applySkin ( button );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape provideShape ()
    {
        return PainterSupport.getShape ( button, painter );
    }

    /**
     * Returns panel painter.
     *
     * @return panel painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets button painter.
     * Pass null to remove button painter.
     *
     * @param painter new button painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( button, new DataRunnable<ButtonPainter> ()
        {
            @Override
            public void run ( final ButtonPainter newPainter )
            {
                WebButtonUI.this.painter = newPainter;
            }
        }, this.painter, painter, ButtonPainter.class, AdaptiveButtonPainter.class );
    }

    /**
     * Paints button.
     *
     * @param g graphics
     * @param c component
     */
    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            // todo Move to painter?
            final AbstractButton b = ( AbstractButton ) c;
            final ButtonModel model = b.getModel ();
            final FontMetrics fm = SwingUtilities2.getFontMetrics ( b, g );
            final int width = b.getWidth ();
            final int height = b.getHeight ();

            // These rectangles/insets are allocated once for all ButtonUI.paint() calls.
            // Re-using rectangles rather than allocating them in each paint call substantially reduced the time it took paint to run.
            // Obviously, this method can't be re-entered.
            final Rectangle viewRect = new Rectangle ();
            final Rectangle textRect = new Rectangle ();
            final Rectangle iconRect = new Rectangle ();

            final Insets i = b.getInsets ();
            viewRect.x = i.left;
            viewRect.y = i.top;
            viewRect.width = width - ( i.right + viewRect.x );
            viewRect.height = height - ( i.bottom + viewRect.y );

            textRect.x = textRect.y = textRect.width = textRect.height = 0;
            iconRect.x = iconRect.y = iconRect.width = iconRect.height = 0;

            // Layout the text and icon
            SwingUtilities.layoutCompoundLabel ( b, fm, b.getText (), b.getIcon (), b.getVerticalAlignment (), b.getHorizontalAlignment (),
                    b.getVerticalTextPosition (), b.getHorizontalTextPosition (), viewRect, iconRect, textRect,
                    b.getText () == null ? 0 : b.getIconTextGap () );

            // Painting button
            painter.setViewRect ( viewRect );
            painter.setIconRect ( textRect );
            painter.setIconRect ( iconRect );
            painter.paint ( ( Graphics2D ) g, SwingUtils.size ( c ), c, this );
        }
    }

    //    @Override
    //    public void paint ( final Graphics g, final JComponent c )
    //    {
    //        final AbstractButton button = ( AbstractButton ) c;
    //        final ButtonModel buttonModel = button.getModel ();
    //
    //        final Graphics2D g2d = ( Graphics2D ) g;
    //        final Object aa = GraphicsUtils.setupAntialias ( g2d );
    //
    //        if ( painter != null || !undecorated )
    //        {
    //            if ( painter != null )
    //            {
    //                // Use background painter instead of default UI graphics
    //                painter.paint ( g2d, SwingUtils.size ( c ), c );
    //            }
    //            else if ( !undecorated )
    //            {
    //                final boolean pressed = buttonModel.isPressed () || buttonModel.isSelected ();
    //                final Shape borderShape = getButtonShape ( button, true );
    //                if ( isDrawButton ( c, buttonModel ) )
    //                {
    //                    // Rollover decorated only transparency
    //                    final boolean animatedTransparency = animate && rolloverDecoratedOnly && !pressed;
    //                    final Composite oldComposite = GraphicsUtils.setupAlphaComposite ( g2d, transparency, animatedTransparency );
    //
    //                    // Shade
    //                    if ( drawShade && ( c.isEnabled () || isInButtonGroup ( button ) || showDisabledShade ) &&
    //                            ( !rolloverShadeOnly || rollover ) )
    //                    {
    //                        final boolean setInner = !animatedTransparency && rolloverShadeOnly;
    //                        final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, transparency, setInner );
    //                        if ( shadeWidth < 3 )
    //                        {
    //                            GraphicsUtils.drawShade ( g2d, borderShape, getShadeColor ( c ), shadeWidth );
    //                        }
    //                        else
    //                        {
    //                            // todo Properly paint shade color
    //
    //                            final int w = button.getWidth ();
    //                            final int h = button.getHeight ();
    //
    //                            // Changing line marks in case of RTL orientation
    //                            final boolean ltr = button.getComponentOrientation ().isLeftToRight ();
    //                            final boolean actualPaintLeft = ltr ? drawLeft : drawRight;
    //                            final boolean actualPaintRight = ltr ? drawRight : drawLeft;
    //
    //                            // Retrieve shade 9-patch icon
    //                            final NinePatchIcon shade = NinePatchUtils.getShadeIcon ( shadeWidth, round * 2, transparency );
    //
    //                            // Calculate shade bounds and paint it
    //                            final int x = actualPaintLeft ? 0 : -shadeWidth * 2;
    //                            final int width = w + ( actualPaintLeft ? 0 : shadeWidth * 2 ) + ( actualPaintRight ? 0 : shadeWidth * 2 );
    //                            final int y = drawTop ? 0 : -shadeWidth * 2;
    //                            final int height = h + ( drawTop ? 0 : shadeWidth * 2 ) + ( drawBottom ? 0 : shadeWidth * 2 );
    //                            shade.paintIcon ( g2d, x, y, width, height );
    //                        }
    //                        GraphicsUtils.restoreComposite ( g2d, oc, setInner );
    //                    }
    //
    //                    // Background
    //                    g2d.setPaint ( new GradientPaint ( 0, drawTop ? shadeWidth : 0, getCurrentTopBgColor ( pressed ), 0,
    //                            button.getHeight () - ( drawBottom ? shadeWidth : 0 ), getCurrentBottomBgColor ( pressed ) ) );
    //                    g2d.fill ( getButtonShape ( button, false ) );
    //
    //                    // Cursor-following highlight
    //                    if ( rolloverShine && mousePoint != null && c.isEnabled () )
    //                    {
    //                        final Shape oldClip = GraphicsUtils.intersectClip ( g2d, borderShape );
    //                        g2d.setPaint ( new RadialGradientPaint ( mousePoint.x, c.getHeight (), c.getWidth (), new float[]{ 0f, 1f },
    //                                new Color[]{ transparentShineColor, StyleConstants.transparent } ) );
    //                        g2d.fill ( borderShape );
    //                        GraphicsUtils.restoreClip ( g2d, oldClip );
    //                    }
    //
    //                    // Inner shade
    //                    if ( pressed )
    //                    {
    //                        GraphicsUtils.drawShade ( g2d, borderShape, innerShadeColor, innerShadeWidth, borderShape );
    //                    }
    //
    //                    // Default button inner shade
    //                    if ( isDefaultButton () )
    //                    {
    //                        final Shape oldClip = GraphicsUtils.setupClip ( g2d, borderShape );
    //                        final Stroke stroke = GraphicsUtils.setupStroke ( g2d, defaultInnerShade );
    //                        g2d.setPaint ( defaultButtonShadeColor );
    //                        g2d.draw ( borderShape );
    //                        GraphicsUtils.restoreStroke ( g2d, stroke );
    //                        GraphicsUtils.restoreClip ( g2d, oldClip );
    //                    }
    //
    //                    // Border
    //                    g2d.setPaint ( c.isEnabled () ? rolloverDarkBorderOnly && rollover ? getBorderColor () :
    //                            !rolloverDarkBorderOnly ? StyleConstants.darkBorderColor : StyleConstants.borderColor :
    //                            StyleConstants.disabledBorderColor );
    //                    g2d.draw ( borderShape );
    //
    //                    // Changing line marks in case of RTL orientation
    //                    final boolean ltr = button.getComponentOrientation ().isLeftToRight ();
    //                    final boolean actualDrawLeft = ltr ? drawLeft : drawRight;
    //                    final boolean actualDrawLeftLine = ltr ? drawLeftLine : drawRightLine;
    //                    final boolean actualDrawRight = ltr ? drawRight : drawLeft;
    //                    final boolean actualDrawRightLine = ltr ? drawRightLine : drawLeftLine;
    //
    //                    // Side-border
    //                    if ( !drawTop && drawTopLine )
    //                    {
    //                        final int x = actualDrawLeft ? shadeWidth : 0;
    //                        g2d.setPaint ( c.isEnabled () || isAfterEnabledButton ( button ) ? StyleConstants.darkBorderColor :
    //                                StyleConstants.disabledBorderColor );
    //                        g2d.drawLine ( x, 0, x + c.getWidth () - ( actualDrawLeft ? shadeWidth : 0 ) -
    //                                ( actualDrawRight ? shadeWidth + 1 : 0 ), 0 );
    //                    }
    //                    if ( !actualDrawLeft && actualDrawLeftLine )
    //                    {
    //                        final int y = drawTop ? shadeWidth : 0;
    //                        g2d.setPaint ( c.isEnabled () || isAfterEnabledButton ( button ) ? StyleConstants.darkBorderColor :
    //                                StyleConstants.disabledBorderColor );
    //                        g2d.drawLine ( 0, y, 0, y + c.getHeight () - ( drawTop ? shadeWidth : 0 ) -
    //                                ( drawBottom ? shadeWidth + 1 : 0 ) );
    //                    }
    //                    if ( !drawBottom && drawBottomLine )
    //                    {
    //                        final int x = actualDrawLeft ? shadeWidth : 0;
    //                        g2d.setPaint ( c.isEnabled () || isBeforeEnabledButton ( button ) ? StyleConstants.darkBorderColor :
    //                                StyleConstants.disabledBorderColor );
    //                        g2d.drawLine ( x, c.getHeight () - 1, x + c.getWidth () - ( actualDrawLeft ? shadeWidth : 0 ) -
    //                                ( actualDrawRight ? shadeWidth + 1 : 0 ), c.getHeight () - 1 );
    //                    }
    //                    if ( !actualDrawRight && actualDrawRightLine )
    //                    {
    //                        final int y = drawTop ? shadeWidth : 0;
    //                        g2d.setPaint ( c.isEnabled () || isBeforeEnabledButton ( button ) ? StyleConstants.darkBorderColor :
    //                                StyleConstants.disabledBorderColor );
    //                        g2d.drawLine ( c.getWidth () - 1, y, c.getWidth () - 1, y + c.getHeight () - ( drawTop ? shadeWidth : 0 ) -
    //                                ( drawBottom ? shadeWidth + 1 : 0 ) );
    //                    }
    //
    //                    GraphicsUtils.restoreComposite ( g2d, oldComposite, animatedTransparency );
    //                }
    //            }
    //        }
    //
    //        // Special text and icon translation effect on button press
    //        if ( buttonModel.isPressed () && moveIconOnPress )
    //        {
    //            g2d.translate ( 1, 1 );
    //        }
    //
    //        GraphicsUtils.restoreAntialias ( g2d, aa );
    //
    //        // Default text and icon drawing
    //        final Map hints = SwingUtils.setupTextAntialias ( g2d );
    //        super.paint ( g, c );
    //        SwingUtils.restoreTextAntialias ( g2d, hints );
    //    }
    //
    //    /**
    //     * Returns whether this button is default within its root pane or not.
    //     *
    //     * @return true if this button is default within its root pane, false otherwise
    //     */
    //    protected boolean isDefaultButton ()
    //    {
    //        return button instanceof JButton && ( ( JButton ) button ).isDefaultButton ();
    //    }
    //
    //    /**
    //     * Returns whether this button placed after another enabled button inside a WebButtonGroup container or not.
    //     * This check is required to paint button sides with proper colors.
    //     *
    //     * @param button this button
    //     * @return true if this button placed after another enabled button inside a WebButtonGroup container, false otherwise
    //     */
    //    protected boolean isAfterEnabledButton ( final AbstractButton button )
    //    {
    //        final Container container = button.getParent ();
    //        if ( container != null && container instanceof WebButtonGroup )
    //        {
    //            final int zOrder = container.getComponentZOrder ( button );
    //            if ( zOrder > 0 )
    //            {
    //                final WebButtonGroup group = ( WebButtonGroup ) container;
    //                final Component before = group.getComponent ( zOrder - 1 );
    //                if ( before instanceof WebButton )
    //                {
    //                    return before.isEnabled ();
    //                }
    //            }
    //        }
    //        return false;
    //    }
    //
    //    /**
    //     * Returns whether this button placed before another enabled button inside a WebButtonGroup container or not.
    //     * This check is required to paint button sides with proper colors.
    //     *
    //     * @param button this button
    //     * @return true if this button placed before another enabled button inside a WebButtonGroup container, false otherwise
    //     */
    //    protected boolean isBeforeEnabledButton ( final AbstractButton button )
    //    {
    //        final Container container = button.getParent ();
    //        if ( container != null && container instanceof WebButtonGroup )
    //        {
    //            final int zOrder = container.getComponentZOrder ( button );
    //            if ( zOrder < container.getComponentCount () - 1 )
    //            {
    //                final WebButtonGroup group = ( WebButtonGroup ) container;
    //                final Component before = group.getComponent ( zOrder + 1 );
    //                if ( before instanceof WebButton )
    //                {
    //                    return before.isEnabled ();
    //                }
    //            }
    //        }
    //        return false;
    //    }
    //
    //    /**
    //     * Returns whether this button is inside WebButtonGroup or not.
    //     *
    //     * @param button this button
    //     * @return true if this button is inside WebButtonGroup, false otherwise
    //     */
    //    protected boolean isInButtonGroup ( final AbstractButton button )
    //    {
    //        final Container container = button.getParent ();
    //        return container != null && container instanceof WebButtonGroup;
    //    }
    //
    //    private Color getShadeColor ( final JComponent c )
    //    {
    //        return isFocusActive ( c ) ? StyleConstants.fieldFocusColor : shadeColor;
    //    }
    //
    //    private boolean isFocusActive ( final JComponent c )
    //    {
    //        return c.isEnabled () && drawFocus && c.isFocusOwner ();
    //    }
    //
    //    private boolean isDrawButton ( final JComponent c, final ButtonModel buttonModel )
    //    {
    //        return rolloverDecoratedOnly && rollover && c.isEnabled () ||
    //                animate && transparency > 0f && c.isEnabled () || !rolloverDecoratedOnly ||
    //                buttonModel.isSelected () || buttonModel.isPressed ();
    //    }

    //    private Color getBorderColor ()
    //    {
    //        return ColorUtils.getIntermediateColor ( StyleConstants.borderColor, StyleConstants.darkBorderColor, transparency );
    //    }
    //
    //    @Override
    //    protected void paintIcon ( final Graphics g, final JComponent c, final Rectangle iconRect )
    //    {
    //        final AbstractButton button = ( AbstractButton ) c;
    //        final ButtonModel buttonModel = button.getModel ();
    //        final Graphics2D g2d = ( Graphics2D ) g;
    //
    //        final boolean shadeToggleIcon = this.shadeToggleIcon && button instanceof JToggleButton &&
    //                !buttonModel.isSelected ();
    //        final Composite old = GraphicsUtils.setupAlphaComposite ( g2d, shadeToggleIconTransparency, shadeToggleIcon );
    //
    //        super.paintIcon ( g, c, iconRect );
    //
    //        GraphicsUtils.restoreComposite ( g2d, old, shadeToggleIcon );
    //    }
    //
    //    protected Shape getButtonShape ( final AbstractButton button, final boolean border )
    //    {
    //        // Changing line marks in case of RTL orientation
    //        final boolean ltr = button.getComponentOrientation ().isLeftToRight ();
    //        final boolean actualDrawLeft = ltr ? drawLeft : drawRight;
    //        final boolean actualDrawRight = ltr ? drawRight : drawLeft;
    //
    //        // Determining border coordinates
    //        final int x = actualDrawLeft ? shadeWidth : -shadeWidth - round - 1;
    //        final int y = drawTop ? shadeWidth : -shadeWidth - round - 1;
    //        final int maxX = actualDrawRight ? button.getWidth () - shadeWidth - 1 : button.getWidth () + shadeWidth + round;
    //        final int maxY = drawBottom ? button.getHeight () - shadeWidth - 1 : button.getHeight () + shadeWidth + round;
    //        final int width = maxX - x;
    //        final int height = maxY - y;
    //
    //        // Creating border shape
    //        if ( round > 0 )
    //        {
    //            return new RoundRectangle2D.Double ( x, y, width, height, round * 2 + ( border ? 0 : 1 ), round * 2 + ( border ? 0 : 1 ) );
    //        }
    //        else
    //        {
    //            return new Rectangle2D.Double ( x, y, width, height );
    //        }
    //    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ), painter );
    }
}
