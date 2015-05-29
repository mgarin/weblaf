package com.alee.managers.style.skin.web;

import com.alee.global.StyleConstants;
import com.alee.laf.button.AbstractButtonPainter;
import com.alee.laf.button.WebButtonStyle;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.AncestorAdapter;
import com.alee.utils.swing.WebTimer;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

/**
 * @author Mikle Garin
 */

public class WebAbstractButtonPainter<E extends AbstractButton, U extends BasicButtonUI> extends WebDecorationPainter<E, U>
        implements AbstractButtonPainter<E, U>
{
    /**
     * todo 1. Properly check situations when button sides are painted within multi-row/column WebButtonGroup
     * todo 2. Properly paint side-borders when grouped buttons are rollover decorated only
     */

    // todo Remove?
    protected static final Stroke defaultInnerShade = new BasicStroke ( 3f );

    /**
     * Style settings.
     */
    protected float shadeToggleIconTransparency = WebButtonStyle.shadeToggleIconTransparency;
    protected int innerShadeWidth = WebButtonStyle.innerShadeWidth;
    protected int leftRightSpacing = WebButtonStyle.leftRightSpacing;
    protected Color topBgColor = WebButtonStyle.topBgColor;
    protected Color bottomBgColor = WebButtonStyle.bottomBgColor;
    protected Color topSelectedBgColor = WebButtonStyle.topSelectedBgColor;
    protected Color bottomSelectedBgColor = WebButtonStyle.bottomSelectedBgColor;
    protected Color selectedForeground = WebButtonStyle.selectedForeground;
    protected Color shadeColor = WebButtonStyle.shadeColor;
    protected Color innerShadeColor = WebButtonStyle.innerShadeColor;
    protected Color defaultButtonShadeColor = WebButtonStyle.defaultButtonShadeColor;
    protected Color shineColor = WebButtonStyle.shineColor;
    protected boolean animate = WebButtonStyle.animate;
    protected boolean showDisabledShade = WebButtonStyle.showDisabledShade;
    protected boolean shadeToggleIcon = WebButtonStyle.shadeToggleIcon;
    protected boolean moveIconOnPress = WebButtonStyle.moveIconOnPress;
    protected boolean rolloverShine = WebButtonStyle.rolloverShine;
    protected boolean rolloverDarkBorderOnly = WebButtonStyle.rolloverDarkBorderOnly;
    protected boolean rolloverShadeOnly = WebButtonStyle.rolloverShadeOnly;
    protected boolean rolloverDecoratedOnly = WebButtonStyle.rolloverDecoratedOnly;

    /**
     * Listeners.
     */
    protected MouseAdapter mouseAdapter;
    protected AncestorListener ancestorListener;

    /**
     * Runtime variables.
     */
    protected Color transparentShineColor = new Color ( shineColor.getRed (), shineColor.getGreen (), shineColor.getBlue (), 0 );
    protected boolean rollover = false;
    protected float transparency = 0f;
    protected Point mousePoint = null;
    protected WebTimer animator = null;

    /**
     * Painting variables.
     */
    protected boolean pressed;
    protected ButtonModel model;
    protected Rectangle viewRect = new Rectangle ();
    protected Rectangle textRect = new Rectangle ();
    protected Rectangle iconRect = new Rectangle ();

    /**
     * {@inheritDoc}
     */
    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Rollover listener
        mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mouseEntered ( final MouseEvent e )
            {
                rollover = true;
                c.getModel ().setRollover ( true );
                mousePoint = e.getPoint ();

                stopAnimator ();
                if ( animate && ( rolloverShine || rolloverDecoratedOnly || rolloverShadeOnly ) )
                {
                    animator = new WebTimer ( "WebButtonUI.fadeInTimer", StyleConstants.fastAnimationDelay, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            transparency += 0.075f;
                            if ( transparency >= 1f )
                            {
                                transparency = 1f;
                                animator.stop ();
                            }
                            updateTransparentShineColor ();
                            refresh ( c );
                        }
                    } );
                    animator.start ();
                }
                else
                {
                    transparency = 1f;
                    updateTransparentShineColor ();
                    refresh ( c );
                }
            }

            @Override
            public void mouseExited ( final MouseEvent e )
            {
                mousePoint = e.getPoint ();

                stopAnimator ();
                if ( animate && ( rolloverShine || rolloverDecoratedOnly || rolloverShadeOnly ) )
                {
                    animator = new WebTimer ( "WebButtonUI.fadeOutTimer", StyleConstants.fastAnimationDelay, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            transparency -= 0.075f;
                            if ( transparency <= 0f )
                            {
                                rollover = false;
                                c.getModel ().setRollover ( false );
                                transparency = 0f;
                                mousePoint = null;
                                animator.stop ();
                            }
                            updateTransparentShineColor ();
                            refresh ( c );
                        }
                    } );
                    animator.start ();
                }
                else
                {
                    rollover = false;
                    c.getModel ().setRollover ( false );
                    transparency = 0f;
                    mousePoint = null;
                    updateTransparentShineColor ();
                    refresh ( c );
                }
            }

            @Override
            public void mouseReleased ( final MouseEvent e )
            {
                // Fix for highlight stuck
                if ( !c.isShowing () || windowLostFocus () )
                {
                    mousePoint = null;
                    refresh ( c );
                }
            }

            private boolean windowLostFocus ()
            {
                final Window wa = SwingUtils.getWindowAncestor ( c );
                return wa == null || !wa.isVisible () || !wa.isActive ();
            }

            private void stopAnimator ()
            {
                if ( animator != null )
                {
                    animator.stop ();
                }
            }

            @Override
            public void mouseDragged ( final MouseEvent e )
            {
                mousePoint = e.getPoint ();
                if ( rolloverShine )
                {
                    refresh ( c );
                }
            }

            @Override
            public void mouseMoved ( final MouseEvent e )
            {
                mousePoint = e.getPoint ();
                if ( rolloverShine )
                {
                    refresh ( c );
                }
            }

            private void refresh ( final JComponent c )
            {
                if ( c.isEnabled () && ( animator == null || !animator.isRunning () ) )
                {
                    repaint ();
                }
            }
        };
        c.addMouseListener ( mouseAdapter );
        c.addMouseMotionListener ( mouseAdapter );

        // Ancestor listener
        ancestorListener = new AncestorAdapter ()
        {
            @Override
            public void ancestorRemoved ( final AncestorEvent event )
            {
                rollover = false;
                c.getModel ().setRollover ( false );
                transparency = 0f;
                mousePoint = null;
                updateTransparentShineColor ();
                repaint ();
            }
        };
        c.addAncestorListener ( ancestorListener );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Removing listeners
        c.removeMouseListener ( mouseAdapter );
        c.removeMouseMotionListener ( mouseAdapter );
        c.removeAncestorListener ( ancestorListener );

        super.uninstall ( c, ui );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getBorders ()
    {
        if ( undecorated )
        {
            return new Insets ( 0, leftRightSpacing, 0, leftRightSpacing );
        }
        else
        {
            final Insets borders = super.getBorders ();
            borders.top += innerShadeWidth;
            borders.left += innerShadeWidth + leftRightSpacing;
            borders.bottom += innerShadeWidth;
            borders.right += innerShadeWidth + leftRightSpacing;
            return borders;
        }
    }

    /**
     * Updates atually used shine color.
     */
    protected void updateTransparentShineColor ()
    {
        transparentShineColor = new Color ( shineColor.getRed (), shineColor.getGreen (), shineColor.getBlue (),
                Math.round ( transparency * shineColor.getAlpha () ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        // Defining base variables
        model = c.getModel ();
        pressed = model.isPressed () || model.isSelected ();

        // Calculating bounds we will need late
        calculateBounds ( SwingUtilities2.getFontMetrics ( c, g2d ), bounds );

        // Painting button
        super.paint ( g2d, bounds, c, ui );

        // Painting icon
        paintIcon ( g2d, bounds );

        // Painting text
        paintText ( g2d, bounds );

        // Cleaning up
        model = null;
    }

    /**
     * Calculates view, icon and text bounds for future usage.
     *
     * @param fm font metrics
     */
    protected void calculateBounds ( final FontMetrics fm )
    {
        calculateBounds ( fm, new Rectangle ( Short.MAX_VALUE, Short.MAX_VALUE ) );
    }

    /**
     * Calculates view, icon and text bounds for future usage.
     *
     * @param fm     font metrics
     * @param bounds paint bounds
     */
    protected void calculateBounds ( final FontMetrics fm, final Rectangle bounds )
    {
        final Insets i = component.getInsets ();
        viewRect.x = bounds.x + i.left;
        viewRect.y = bounds.y + i.top;
        viewRect.width = bounds.width - ( i.right + viewRect.x );
        viewRect.height = bounds.height - ( i.bottom + viewRect.y );

        textRect.x = textRect.y = textRect.width = textRect.height = 0;
        iconRect.x = iconRect.y = iconRect.width = iconRect.height = 0;

        // Layout the text and icon
        SwingUtilities.layoutCompoundLabel ( component, fm, component.getText (), component.getIcon (), component.getVerticalAlignment (),
                component.getHorizontalAlignment (), component.getVerticalTextPosition (), component.getHorizontalTextPosition (), viewRect,
                iconRect, textRect, component.getText () == null ? 0 : component.getIconTextGap () );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintBackground ( final Graphics2D g2d, final Rectangle bounds, final Shape backgroundShape )
    {
        g2d.setPaint ( new GradientPaint ( 0, paintTop ? shadeWidth : 0, getCurrentTopBgColor ( pressed ), 0,
                component.getHeight () - ( paintBottom ? shadeWidth : 0 ), getCurrentBottomBgColor ( pressed ) ) );
        g2d.fill ( backgroundShape );

        // Cursor-following highlight
        if ( rolloverShine && mousePoint != null && component.isEnabled () )
        {
            final Shape oldClip = GraphicsUtils.intersectClip ( g2d, backgroundShape );
            g2d.setPaint ( new RadialGradientPaint ( mousePoint.x, component.getHeight (), component.getWidth (), new float[]{ 0f, 1f },
                    new Color[]{ transparentShineColor, StyleConstants.transparent } ) );
            g2d.fill ( backgroundShape );
            GraphicsUtils.restoreClip ( g2d, oldClip );
        }
    }

    /**
     * Paints button icon.
     *
     * @param g2d    graphics context
     * @param bounds paint bounds
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void paintIcon ( final Graphics2D g2d, final Rectangle bounds )
    {
        if ( component.getIcon () != null )
        {
            Icon icon = component.getIcon ();
            Icon tmpIcon = null;

            if ( icon == null )
            {
                return;
            }

            Icon selectedIcon = null;

            /* the fallback icon should be based on the selected state */
            if ( model.isSelected () )
            {
                selectedIcon = component.getSelectedIcon ();
                if ( selectedIcon != null )
                {
                    icon = selectedIcon;
                }
            }

            if ( !model.isEnabled () )
            {
                if ( model.isSelected () )
                {
                    tmpIcon = component.getDisabledSelectedIcon ();
                    if ( tmpIcon == null )
                    {
                        tmpIcon = selectedIcon;
                    }
                }

                if ( tmpIcon == null )
                {
                    tmpIcon = component.getDisabledIcon ();
                }
            }
            else if ( model.isPressed () && model.isArmed () )
            {
                tmpIcon = component.getPressedIcon ();
            }
            else if ( component.isRolloverEnabled () && model.isRollover () )
            {
                if ( model.isSelected () )
                {
                    tmpIcon = component.getRolloverSelectedIcon ();
                    if ( tmpIcon == null )
                    {
                        tmpIcon = selectedIcon;
                    }
                }

                if ( tmpIcon == null )
                {
                    tmpIcon = component.getRolloverIcon ();
                }
            }

            if ( tmpIcon != null )
            {
                icon = tmpIcon;
            }

            if ( model.isPressed () && model.isArmed () )
            {
                icon.paintIcon ( component, g2d, iconRect.x, iconRect.y );
            }
            else
            {
                icon.paintIcon ( component, g2d, iconRect.x, iconRect.y );
            }
        }
    }

    /**
     * Paints button text.
     *
     * @param g2d    graphics context
     * @param bounds paint bounds
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void paintText ( final Graphics2D g2d, final Rectangle bounds )
    {
        final Map map = SwingUtils.setupTextAntialias ( g2d );
        final String text = component.getText ();
        if ( text != null && !text.equals ( "" ) )
        {
            final View v = ( View ) component.getClientProperty ( BasicHTML.propertyKey );
            if ( v != null )
            {
                v.paint ( g2d, textRect );
            }
            else
            {
                final FontMetrics fm = SwingUtils.getFontMetrics ( component, g2d );
                final int mnemonicIndex = component.getDisplayedMnemonicIndex ();

                // Drawing text
                if ( model.isEnabled () )
                {
                    // Drawing normal text
                    g2d.setColor ( model.isPressed () || model.isSelected () ? selectedForeground : component.getForeground () );
                    SwingUtils.drawStringUnderlineCharAt ( g2d, text, mnemonicIndex, textRect.x, textRect.y + fm.getAscent () );
                }
                else
                {
                    // todo Paint single-colored text
                    // Drawing disabled text
                    g2d.setColor ( component.getBackground ().brighter () );
                    SwingUtils.drawStringUnderlineCharAt ( g2d, text, mnemonicIndex, textRect.x + 1, textRect.y + fm.getAscent () + 1 );
                    g2d.setColor ( component.getBackground ().darker () );
                    SwingUtils.drawStringUnderlineCharAt ( g2d, text, mnemonicIndex, textRect.x, textRect.y + fm.getAscent () );
                }
            }
        }
        SwingUtils.restoreTextAntialias ( g2d, map );
    }

    /**
     * Returns current top background color.
     *
     * @param pressed whether button is pressed or not
     * @return current top background color
     */
    protected Color getCurrentTopBgColor ( final boolean pressed )
    {
        return pressed ? topSelectedBgColor : topBgColor;
    }

    /**
     * Returns current bottom background color.
     *
     * @param pressed whether button is pressed or not
     * @return current bottom background color
     */
    protected Color getCurrentBottomBgColor ( final boolean pressed )
    {
        return pressed ? bottomSelectedBgColor : bottomBgColor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getContentPreferredSize ()
    {
        // Calculating icon and text rects union as content size
        calculateBounds ( component.getFontMetrics ( component.getFont () ) );
        return textRect.union ( iconRect ).getSize ();
    }

    /**
     * todo Review old button painting mechanism
     */

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
}