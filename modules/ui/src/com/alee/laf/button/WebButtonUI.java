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
import com.alee.extended.panel.WebButtonGroup;
import com.alee.global.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.utils.*;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.ninepatch.NinePatchIcon;
import com.alee.utils.swing.AncestorAdapter;
import com.alee.utils.swing.BorderMethods;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

/**
 * @author Mikle Garin
 */

public class WebButtonUI extends BasicButtonUI implements ShapeProvider, SwingConstants, BorderMethods
{
    /**
     * todo 1. Properly check situations when button sides are painted within multi-row/column WebButtonGroup
     * todo 2. Properly paint side-borders when grouped buttons are rollover decorated only
     */

    protected static final Stroke defaultInnerShade = new BasicStroke ( 3f );

    protected Color topBgColor = WebButtonStyle.topBgColor;
    protected Color bottomBgColor = WebButtonStyle.bottomBgColor;
    protected Color topSelectedBgColor = WebButtonStyle.topSelectedBgColor;
    protected Color bottomSelectedBgColor = WebButtonStyle.bottomSelectedBgColor;
    protected Color selectedForeground = WebButtonStyle.selectedForeground;
    protected boolean rolloverShine = WebButtonStyle.rolloverShine;
    protected Color shineColor = WebButtonStyle.shineColor;
    protected boolean rolloverDarkBorderOnly = WebButtonStyle.rolloverDarkBorderOnly;
    protected int round = WebButtonStyle.round;
    protected boolean drawShade = WebButtonStyle.drawShade;
    protected boolean rolloverShadeOnly = WebButtonStyle.rolloverShadeOnly;
    protected boolean showDisabledShade = WebButtonStyle.showDisabledShade;
    protected int shadeWidth = WebButtonStyle.shadeWidth;
    protected Insets margin = WebButtonStyle.margin;
    protected Color shadeColor = WebButtonStyle.shadeColor;
    protected int innerShadeWidth = WebButtonStyle.innerShadeWidth;
    protected Color innerShadeColor = WebButtonStyle.innerShadeColor;
    protected Color defaultButtonShadeColor = WebButtonStyle.defaultButtonShadeColor;
    protected int leftRightSpacing = WebButtonStyle.leftRightSpacing;
    protected boolean shadeToggleIcon = WebButtonStyle.shadeToggleIcon;
    protected float shadeToggleIconTransparency = WebButtonStyle.shadeToggleIconTransparency;
    protected boolean drawFocus = WebButtonStyle.drawFocus;
    protected boolean rolloverDecoratedOnly = WebButtonStyle.rolloverDecoratedOnly;
    protected boolean animate = WebButtonStyle.animate;
    protected boolean undecorated = WebButtonStyle.undecorated;
    protected Painter painter = WebButtonStyle.painter;
    protected boolean moveIconOnPress = WebButtonStyle.moveIconOnPress;

    protected boolean drawTop = true;
    protected boolean drawLeft = true;
    protected boolean drawBottom = true;
    protected boolean drawRight = true;

    protected boolean drawTopLine = false;
    protected boolean drawLeftLine = false;
    protected boolean drawBottomLine = false;
    protected boolean drawRightLine = false;

    protected Color transparentShineColor = new Color ( shineColor.getRed (), shineColor.getGreen (), shineColor.getBlue (), 0 );

    protected boolean rollover = false;
    protected float transparency = 0f;

    protected Point mousePoint = null;
    protected WebTimer animator = null;
    protected AbstractButton button = null;

    // Cached old values from button to restore on LAF change.
    protected boolean oldFocusPainted;
    protected boolean oldContentAreaFilled;
    protected boolean oldBorderPainted;
    protected boolean oldFocusable;

    protected MouseAdapter mouseAdapter;
    protected AncestorListener ancestorListener;
    protected PropertyChangeListener propertyChangeListener;

    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebButtonUI ();
    }

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

        // Default settings
        SwingUtils.setOrientation ( button );
        button.setFocusPainted ( false );
        button.setContentAreaFilled ( false );
        button.setBorderPainted ( false );
        button.setFocusable ( true );
        LookAndFeel.installProperty ( button, WebLookAndFeel.OPAQUE_PROPERTY, Boolean.FALSE );
        PainterSupport.installPainter ( button, this.painter );

        // Updating border
        updateBorder ();

        // Rollover listener
        mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mouseEntered ( final MouseEvent e )
            {
                rollover = true;
                button.getModel ().setRollover ( true );
                mousePoint = e.getPoint ();

                stopAnimator ();
                if ( painter == null &&
                        animate && ( rolloverShine || rolloverDecoratedOnly || rolloverShadeOnly ) )
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
                            if ( c.isEnabled () )
                            {
                                c.repaint ();
                            }
                        }
                    } );
                    animator.start ();
                }
                else
                {
                    transparency = 1f;
                    updateTransparentShineColor ();
                    c.repaint ();
                }

                refresh ( c );
            }

            @Override
            public void mouseExited ( final MouseEvent e )
            {
                mousePoint = e.getPoint ();

                stopAnimator ();
                if ( painter == null &&
                        animate && ( rolloverShine || rolloverDecoratedOnly || rolloverShadeOnly ) )
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
                                button.getModel ().setRollover ( false );
                                transparency = 0f;
                                mousePoint = null;
                                animator.stop ();
                            }
                            updateTransparentShineColor ();
                            if ( c.isEnabled () )
                            {
                                c.repaint ();
                            }
                        }
                    } );
                    animator.start ();
                }
                else
                {
                    rollover = false;
                    button.getModel ().setRollover ( false );
                    transparency = 0f;
                    mousePoint = null;
                    updateTransparentShineColor ();
                    c.repaint ();
                }

                refresh ( c );
            }

            @Override
            public void mouseReleased ( final MouseEvent e )
            {
                // Fix for highlight stuck
                if ( !c.isShowing () || windowLostFocus () )
                {
                    mousePoint = null;
                    c.repaint ();
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
                if ( painter == null && rolloverShine )
                {
                    refresh ( c );
                }
            }

            @Override
            public void mouseMoved ( final MouseEvent e )
            {
                mousePoint = e.getPoint ();
                if ( painter == null && rolloverShine )
                {
                    refresh ( c );
                }
            }

            private void refresh ( final JComponent c )
            {
                if ( c.isEnabled () )
                {
                    if ( animator == null || !animator.isRunning () )
                    {
                        c.repaint ();
                    }
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
                button.getModel ().setRollover ( false );
                transparency = 0f;
                mousePoint = null;
                updateTransparentShineColor ();
                c.repaint ();
            }
        };
        c.addAncestorListener ( ancestorListener );

        // Orientation change listener
        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updateBorder ();
            }
        };
        c.addPropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, propertyChangeListener );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        PainterSupport.uninstallPainter ( button, this.painter );

        c.removeMouseListener ( mouseAdapter );
        c.removeMouseMotionListener ( mouseAdapter );
        c.removeAncestorListener ( ancestorListener );
        c.removePropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, propertyChangeListener );

        button.setBorderPainted ( oldBorderPainted );
        button.setContentAreaFilled ( oldContentAreaFilled );
        button.setFocusPainted ( oldFocusPainted );
        button.setFocusable ( oldFocusable );

        super.uninstallUI ( c );
    }

    @Override
    public Shape provideShape ()
    {
        if ( painter != null || undecorated )
        {
            return SwingUtils.size ( button );
        }
        else
        {
            return getButtonShape ( button, true );
        }
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

    public Color getTopSelectedBgColor ()
    {
        return topSelectedBgColor;
    }

    public void setTopSelectedBgColor ( final Color topSelectedBgColor )
    {
        this.topSelectedBgColor = topSelectedBgColor;
    }

    public Color getBottomSelectedBgColor ()
    {
        return bottomSelectedBgColor;
    }

    public void setBottomSelectedBgColor ( final Color bottomSelectedBgColor )
    {
        this.bottomSelectedBgColor = bottomSelectedBgColor;
    }

    public Color getSelectedForeground ()
    {
        return selectedForeground;
    }

    public void setSelectedForeground ( final Color selectedForeground )
    {
        this.selectedForeground = selectedForeground;
    }

    public boolean isRolloverDarkBorderOnly ()
    {
        return rolloverDarkBorderOnly;
    }

    public void setRolloverDarkBorderOnly ( final boolean rolloverDarkBorderOnly )
    {
        this.rolloverDarkBorderOnly = rolloverDarkBorderOnly;
    }

    public boolean isRolloverShine ()
    {
        return rolloverShine;
    }

    public void setRolloverShine ( final boolean rolloverShine )
    {
        this.rolloverShine = rolloverShine;
    }

    public Color getShineColor ()
    {
        return shineColor;
    }

    public void setShineColor ( final Color shineColor )
    {
        this.shineColor = shineColor;
        updateTransparentShineColor ();
    }

    private void updateTransparentShineColor ()
    {
        transparentShineColor = new Color ( shineColor.getRed (), shineColor.getGreen (), shineColor.getBlue (),
                Math.round ( transparency * shineColor.getAlpha () ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBorder ()
    {
        if ( button != null )
        {
            // Preserve old borders
            //            if ( SwingUtils.isPreserveBorders ( button ) )
            //            {
            //                return;
            //            }

            // Installing newly created border
            button.setBorder ( LafUtils.createWebBorder ( getBorderInsets () ) );
        }
    }

    /**
     * Returns component border insets used for WebLaF border.
     *
     * @return component border insets
     */
    protected Insets getBorderInsets ()
    {
        // Actual margin
        final boolean ltr = button.getComponentOrientation ().isLeftToRight ();
        final Insets m = new Insets ( margin.top, ( ltr ? margin.left : margin.right ) + leftRightSpacing, margin.bottom,
                ( ltr ? margin.right : margin.left ) + leftRightSpacing );

        // Applying border
        if ( painter != null )
        {
            // Painter borders
            final Insets pi = painter.getMargin ( button );
            m.top += pi.top;
            m.bottom += pi.bottom;
            m.left += ltr ? pi.left : pi.right;
            m.right += ltr ? pi.right : pi.left;
        }
        else if ( !undecorated )
        {
            // Styling borders
            final boolean actualDrawLeft = ltr ? drawLeft : drawRight;
            final boolean actualDrawLeftLine = ltr ? drawLeftLine : drawRightLine;
            final boolean actualDrawRight = ltr ? drawRight : drawLeft;
            final boolean actualDrawRightLine = ltr ? drawRightLine : drawLeftLine;
            m.top += ( drawTop ? ( shadeWidth + 1 ) : drawTopLine ? 1 : 0 ) + innerShadeWidth;
            m.left += ( actualDrawLeft ? ( shadeWidth + 1 ) : actualDrawLeftLine ? 1 : 0 ) + innerShadeWidth;
            m.bottom += ( drawBottom ? ( shadeWidth + 1 ) : drawBottomLine ? 1 : 0 ) + innerShadeWidth;
            m.right += ( actualDrawRight ? ( shadeWidth + 1 ) : actualDrawRightLine ? 1 : 0 ) + innerShadeWidth;
        }

        return m;
    }

    public boolean isDrawShade ()
    {
        return drawShade;
    }

    public void setDrawShade ( final boolean drawShade )
    {
        this.drawShade = drawShade;
    }

    public boolean isRolloverShadeOnly ()
    {
        return rolloverShadeOnly;
    }

    public void setRolloverShadeOnly ( final boolean rolloverShadeOnly )
    {
        this.rolloverShadeOnly = rolloverShadeOnly;
    }

    public boolean isShowDisabledShade ()
    {
        return showDisabledShade;
    }

    public void setShowDisabledShade ( final boolean showDisabledShade )
    {
        this.showDisabledShade = showDisabledShade;
    }

    public int getShadeWidth ()
    {
        return shadeWidth;
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

    public Color getShadeColor ()
    {
        return shadeColor;
    }

    public void setShadeColor ( final Color shadeColor )
    {
        this.shadeColor = shadeColor;
    }

    public int getInnerShadeWidth ()
    {
        return innerShadeWidth;
    }

    public void setInnerShadeWidth ( final int innerShadeWidth )
    {
        this.innerShadeWidth = innerShadeWidth;
        updateBorder ();
    }

    public Color getInnerShadeColor ()
    {
        return innerShadeColor;
    }

    public void setInnerShadeColor ( final Color innerShadeColor )
    {
        this.innerShadeColor = innerShadeColor;
    }

    public Color getDefaultButtonShadeColor ()
    {
        return defaultButtonShadeColor;
    }

    public void setDefaultButtonShadeColor ( final Color defaultButtonShadeColor )
    {
        this.defaultButtonShadeColor = defaultButtonShadeColor;
    }

    public int getLeftRightSpacing ()
    {
        return leftRightSpacing;
    }

    public void setLeftRightSpacing ( final int leftRightSpacing )
    {
        this.leftRightSpacing = leftRightSpacing;
        updateBorder ();
    }

    public int getRound ()
    {
        return round;
    }

    public void setRound ( final int round )
    {
        this.round = round;
    }

    public boolean isShadeToggleIcon ()
    {
        return shadeToggleIcon;
    }

    public void setShadeToggleIcon ( final boolean shadeToggleIcon )
    {
        this.shadeToggleIcon = shadeToggleIcon;
    }

    public float getShadeToggleIconTransparency ()
    {
        return shadeToggleIconTransparency;
    }

    public void setShadeToggleIconTransparency ( final float shadeToggleIconTransparency )
    {
        this.shadeToggleIconTransparency = shadeToggleIconTransparency;
    }

    public boolean isUndecorated ()
    {
        return undecorated;
    }

    public void setUndecorated ( final boolean undecorated )
    {
        this.undecorated = undecorated;
        updateBorder ();
    }

    public Painter getPainter ()
    {
        return painter;
    }

    public void setPainter ( final Painter painter )
    {
        PainterSupport.uninstallPainter ( button, this.painter );

        this.painter = painter;
        PainterSupport.installPainter ( button, this.painter );
        updateBorder ();
    }

    public boolean isMoveIconOnPress ()
    {
        return moveIconOnPress;
    }

    public void setMoveIconOnPress ( final boolean moveIconOnPress )
    {
        this.moveIconOnPress = moveIconOnPress;
    }

    public boolean isRolloverDecoratedOnly ()
    {
        return rolloverDecoratedOnly;
    }

    public void setRolloverDecoratedOnly ( final boolean rolloverDecoratedOnly )
    {
        this.rolloverDecoratedOnly = rolloverDecoratedOnly;
    }

    public boolean isAnimate ()
    {
        return animate;
    }

    public void setAnimate ( final boolean animate )
    {
        this.animate = animate;
    }

    public boolean isDrawFocus ()
    {
        return drawFocus;
    }

    public void setDrawFocus ( final boolean drawFocus )
    {
        this.drawFocus = drawFocus;
    }

    public boolean isDrawBottom ()
    {
        return drawBottom;
    }

    public void setDrawBottom ( final boolean drawBottom )
    {
        this.drawBottom = drawBottom;
        updateBorder ();
    }

    public boolean isDrawLeft ()
    {
        return drawLeft;
    }

    public void setDrawLeft ( final boolean drawLeft )
    {
        this.drawLeft = drawLeft;
        updateBorder ();
    }

    public boolean isDrawRight ()
    {
        return drawRight;
    }

    public void setDrawRight ( final boolean drawRight )
    {
        this.drawRight = drawRight;
        updateBorder ();
    }

    public boolean isDrawTop ()
    {
        return drawTop;
    }

    public void setDrawTop ( final boolean drawTop )
    {
        this.drawTop = drawTop;
        updateBorder ();
    }

    public void setDrawSides ( final boolean top, final boolean left, final boolean bottom, final boolean right )
    {
        this.drawTop = top;
        this.drawLeft = left;
        this.drawBottom = bottom;
        this.drawRight = right;
        updateBorder ();
    }

    public boolean isDrawTopLine ()
    {
        return drawTopLine;
    }

    public void setDrawTopLine ( final boolean drawTopLine )
    {
        this.drawTopLine = drawTopLine;
        updateBorder ();
    }

    public boolean isDrawLeftLine ()
    {
        return drawLeftLine;
    }

    public void setDrawLeftLine ( final boolean drawLeftLine )
    {
        this.drawLeftLine = drawLeftLine;
        updateBorder ();
    }

    public boolean isDrawBottomLine ()
    {
        return drawBottomLine;
    }

    public void setDrawBottomLine ( final boolean drawBottomLine )
    {
        this.drawBottomLine = drawBottomLine;
        updateBorder ();
    }

    public boolean isDrawRightLine ()
    {
        return drawRightLine;
    }

    public void setDrawRightLine ( final boolean drawRightLine )
    {
        this.drawRightLine = drawRightLine;
        updateBorder ();
    }

    public void setDrawLines ( final boolean top, final boolean left, final boolean bottom, final boolean right )
    {
        this.drawTopLine = top;
        this.drawLeftLine = left;
        this.drawBottomLine = bottom;
        this.drawRightLine = right;
        updateBorder ();
    }

    public boolean isRollover ()
    {
        return rollover;
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        final AbstractButton button = ( AbstractButton ) c;
        final ButtonModel buttonModel = button.getModel ();

        final Graphics2D g2d = ( Graphics2D ) g;
        final Object aa = GraphicsUtils.setupAntialias ( g2d );

        if ( painter != null || !undecorated )
        {
            if ( painter != null )
            {
                // Use background painter instead of default UI graphics
                painter.paint ( g2d, SwingUtils.size ( c ), c );
            }
            else if ( !undecorated )
            {
                final boolean pressed = buttonModel.isPressed () || buttonModel.isSelected ();
                final Shape borderShape = getButtonShape ( button, true );
                if ( isDrawButton ( c, buttonModel ) )
                {
                    // Rollover decorated only transparency
                    final boolean animatedTransparency = animate && rolloverDecoratedOnly && !pressed;
                    final Composite oldComposite = GraphicsUtils.setupAlphaComposite ( g2d, transparency, animatedTransparency );

                    // Shade
                    if ( drawShade && ( c.isEnabled () || isInButtonGroup ( button ) || showDisabledShade ) &&
                            ( !rolloverShadeOnly || rollover ) )
                    {
                        final boolean setInner = !animatedTransparency && rolloverShadeOnly;
                        final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, transparency, setInner );
                        if ( shadeWidth < 3 )
                        {
                            GraphicsUtils.drawShade ( g2d, borderShape, getShadeColor ( c ), shadeWidth );
                        }
                        else
                        {
                            // todo Properly paint shade color

                            final int w = button.getWidth ();
                            final int h = button.getHeight ();

                            // Changing line marks in case of RTL orientation
                            final boolean ltr = button.getComponentOrientation ().isLeftToRight ();
                            final boolean actualPaintLeft = ltr ? drawLeft : drawRight;
                            final boolean actualPaintRight = ltr ? drawRight : drawLeft;

                            // Retrieve shade 9-patch icon
                            final NinePatchIcon shade = NinePatchUtils.getShadeIcon ( shadeWidth, round * 2, transparency );

                            // Calculate shade bounds and paint it
                            final int x = actualPaintLeft ? 0 : -shadeWidth * 2;
                            final int width = w + ( actualPaintLeft ? 0 : shadeWidth * 2 ) + ( actualPaintRight ? 0 : shadeWidth * 2 );
                            final int y = drawTop ? 0 : -shadeWidth * 2;
                            final int height = h + ( drawTop ? 0 : shadeWidth * 2 ) + ( drawBottom ? 0 : shadeWidth * 2 );
                            shade.paintIcon ( g2d, x, y, width, height );
                        }
                        GraphicsUtils.restoreComposite ( g2d, oc, setInner );
                    }

                    // Background
                    g2d.setPaint ( new GradientPaint ( 0, drawTop ? shadeWidth : 0, getCurrentTopBgColor ( pressed ), 0,
                            button.getHeight () - ( drawBottom ? shadeWidth : 0 ), getCurrentBottomBgColor ( pressed ) ) );
                    g2d.fill ( getButtonShape ( button, false ) );

                    // Cursor-following highlight
                    if ( rolloverShine && mousePoint != null && c.isEnabled () )
                    {
                        final Shape oldClip = GraphicsUtils.intersectClip ( g2d, borderShape );
                        g2d.setPaint ( new RadialGradientPaint ( mousePoint.x, c.getHeight (), c.getWidth (), new float[]{ 0f, 1f },
                                new Color[]{ transparentShineColor, StyleConstants.transparent } ) );
                        g2d.fill ( borderShape );
                        GraphicsUtils.restoreClip ( g2d, oldClip );
                    }

                    // Inner shade
                    if ( pressed )
                    {
                        GraphicsUtils.drawShade ( g2d, borderShape, innerShadeColor, innerShadeWidth, borderShape );
                    }

                    // Default button inner shade
                    if ( isDefaultButton () )
                    {
                        final Shape oldClip = GraphicsUtils.setupClip ( g2d, borderShape );
                        final Stroke stroke = GraphicsUtils.setupStroke ( g2d, defaultInnerShade );
                        g2d.setPaint ( defaultButtonShadeColor );
                        g2d.draw ( borderShape );
                        GraphicsUtils.restoreStroke ( g2d, stroke );
                        GraphicsUtils.restoreClip ( g2d, oldClip );
                    }

                    // Border
                    g2d.setPaint ( c.isEnabled () ? rolloverDarkBorderOnly && rollover ? getBorderColor () :
                            !rolloverDarkBorderOnly ? StyleConstants.darkBorderColor : StyleConstants.borderColor :
                            StyleConstants.disabledBorderColor );
                    g2d.draw ( borderShape );

                    // Changing line marks in case of RTL orientation
                    final boolean ltr = button.getComponentOrientation ().isLeftToRight ();
                    final boolean actualDrawLeft = ltr ? drawLeft : drawRight;
                    final boolean actualDrawLeftLine = ltr ? drawLeftLine : drawRightLine;
                    final boolean actualDrawRight = ltr ? drawRight : drawLeft;
                    final boolean actualDrawRightLine = ltr ? drawRightLine : drawLeftLine;

                    // Side-border
                    if ( !drawTop && drawTopLine )
                    {
                        final int x = actualDrawLeft ? shadeWidth : 0;
                        g2d.setPaint ( c.isEnabled () || isAfterEnabledButton ( button ) ? StyleConstants.darkBorderColor :
                                StyleConstants.disabledBorderColor );
                        g2d.drawLine ( x, 0, x + c.getWidth () - ( actualDrawLeft ? shadeWidth : 0 ) -
                                ( actualDrawRight ? shadeWidth + 1 : 0 ), 0 );
                    }
                    if ( !actualDrawLeft && actualDrawLeftLine )
                    {
                        final int y = drawTop ? shadeWidth : 0;
                        g2d.setPaint ( c.isEnabled () || isAfterEnabledButton ( button ) ? StyleConstants.darkBorderColor :
                                StyleConstants.disabledBorderColor );
                        g2d.drawLine ( 0, y, 0, y + c.getHeight () - ( drawTop ? shadeWidth : 0 ) -
                                ( drawBottom ? shadeWidth + 1 : 0 ) );
                    }
                    if ( !drawBottom && drawBottomLine )
                    {
                        final int x = actualDrawLeft ? shadeWidth : 0;
                        g2d.setPaint ( c.isEnabled () || isBeforeEnabledButton ( button ) ? StyleConstants.darkBorderColor :
                                StyleConstants.disabledBorderColor );
                        g2d.drawLine ( x, c.getHeight () - 1, x + c.getWidth () - ( actualDrawLeft ? shadeWidth : 0 ) -
                                ( actualDrawRight ? shadeWidth + 1 : 0 ), c.getHeight () - 1 );
                    }
                    if ( !actualDrawRight && actualDrawRightLine )
                    {
                        final int y = drawTop ? shadeWidth : 0;
                        g2d.setPaint ( c.isEnabled () || isBeforeEnabledButton ( button ) ? StyleConstants.darkBorderColor :
                                StyleConstants.disabledBorderColor );
                        g2d.drawLine ( c.getWidth () - 1, y, c.getWidth () - 1, y + c.getHeight () - ( drawTop ? shadeWidth : 0 ) -
                                ( drawBottom ? shadeWidth + 1 : 0 ) );
                    }

                    GraphicsUtils.restoreComposite ( g2d, oldComposite, animatedTransparency );
                }
            }
        }

        // Special text and icon translation effect on button press
        if ( buttonModel.isPressed () && moveIconOnPress )
        {
            g2d.translate ( 1, 1 );
        }

        GraphicsUtils.restoreAntialias ( g2d, aa );

        // Default text and icon drawing
        final Map hints = SwingUtils.setupTextAntialias ( g2d );
        super.paint ( g, c );
        SwingUtils.restoreTextAntialias ( g2d, hints );
    }

    /**
     * Returns whether this button is default within its root pane or not.
     *
     * @return true if this button is default within its root pane, false otherwise
     */
    protected boolean isDefaultButton ()
    {
        return button instanceof JButton && ( ( JButton ) button ).isDefaultButton ();
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
     * Returns whether this button placed after another enabled button inside a WebButtonGroup container or not.
     * This check is required to paint button sides with proper colors.
     *
     * @param button this button
     * @return true if this button placed after another enabled button inside a WebButtonGroup container, false otherwise
     */
    protected boolean isAfterEnabledButton ( final AbstractButton button )
    {
        final Container container = button.getParent ();
        if ( container != null && container instanceof WebButtonGroup )
        {
            final int zOrder = container.getComponentZOrder ( button );
            if ( zOrder > 0 )
            {
                final WebButtonGroup group = ( WebButtonGroup ) container;
                final Component before = group.getComponent ( zOrder - 1 );
                if ( before instanceof WebButton )
                {
                    return before.isEnabled ();
                }
            }
        }
        return false;
    }

    /**
     * Returns whether this button placed before another enabled button inside a WebButtonGroup container or not.
     * This check is required to paint button sides with proper colors.
     *
     * @param button this button
     * @return true if this button placed before another enabled button inside a WebButtonGroup container, false otherwise
     */
    protected boolean isBeforeEnabledButton ( final AbstractButton button )
    {
        final Container container = button.getParent ();
        if ( container != null && container instanceof WebButtonGroup )
        {
            final int zOrder = container.getComponentZOrder ( button );
            if ( zOrder < container.getComponentCount () - 1 )
            {
                final WebButtonGroup group = ( WebButtonGroup ) container;
                final Component before = group.getComponent ( zOrder + 1 );
                if ( before instanceof WebButton )
                {
                    return before.isEnabled ();
                }
            }
        }
        return false;
    }

    /**
     * Returns whether this button is inside WebButtonGroup or not.
     *
     * @param button this button
     * @return true if this button is inside WebButtonGroup, false otherwise
     */
    protected boolean isInButtonGroup ( final AbstractButton button )
    {
        final Container container = button.getParent ();
        return container != null && container instanceof WebButtonGroup;
    }

    private Color getShadeColor ( final JComponent c )
    {
        return isFocusActive ( c ) ? StyleConstants.fieldFocusColor : shadeColor;
    }

    private boolean isFocusActive ( final JComponent c )
    {
        return c.isEnabled () && drawFocus && c.isFocusOwner ();
    }

    private boolean isDrawButton ( final JComponent c, final ButtonModel buttonModel )
    {
        return rolloverDecoratedOnly && rollover && c.isEnabled () ||
                animate && transparency > 0f && c.isEnabled () || !rolloverDecoratedOnly ||
                buttonModel.isSelected () || buttonModel.isPressed ();
    }

    @Override
    protected void paintText ( final Graphics g, final JComponent c, final Rectangle textRect, final String text )
    {
        final AbstractButton b = ( AbstractButton ) c;
        final ButtonModel model = b.getModel ();
        final FontMetrics fm = SwingUtils.getFontMetrics ( c, g );
        final int mnemonicIndex = b.getDisplayedMnemonicIndex ();

        // Drawing text
        if ( model.isEnabled () )
        {
            // Drawing normal text
            g.setColor ( model.isPressed () || model.isSelected () ? selectedForeground : b.getForeground () );
            SwingUtils.drawStringUnderlineCharAt ( g, text, mnemonicIndex, textRect.x + getTextShiftOffset (),
                    textRect.y + fm.getAscent () + getTextShiftOffset () );
        }
        else
        {
            // Drawing disabled text
            g.setColor ( b.getBackground ().brighter () );
            SwingUtils.drawStringUnderlineCharAt ( g, text, mnemonicIndex, textRect.x, textRect.y + fm.getAscent () );
            g.setColor ( b.getBackground ().darker () );
            SwingUtils.drawStringUnderlineCharAt ( g, text, mnemonicIndex, textRect.x - 1, textRect.y + fm.getAscent () - 1 );
        }
    }

    private Color getBorderColor ()
    {
        return ColorUtils.getIntermediateColor ( StyleConstants.borderColor, StyleConstants.darkBorderColor, transparency );
    }

    @Override
    protected void paintIcon ( final Graphics g, final JComponent c, final Rectangle iconRect )
    {
        final AbstractButton button = ( AbstractButton ) c;
        final ButtonModel buttonModel = button.getModel ();
        final Graphics2D g2d = ( Graphics2D ) g;

        final boolean shadeToggleIcon = this.shadeToggleIcon && button instanceof JToggleButton &&
                !buttonModel.isSelected ();
        final Composite old = GraphicsUtils.setupAlphaComposite ( g2d, shadeToggleIconTransparency, shadeToggleIcon );

        super.paintIcon ( g, c, iconRect );

        GraphicsUtils.restoreComposite ( g2d, old, shadeToggleIcon );
    }

    protected Shape getButtonShape ( final AbstractButton button, final boolean border )
    {
        // Changing line marks in case of RTL orientation
        final boolean ltr = button.getComponentOrientation ().isLeftToRight ();
        final boolean actualDrawLeft = ltr ? drawLeft : drawRight;
        final boolean actualDrawRight = ltr ? drawRight : drawLeft;

        // Determining border coordinates
        final int x = actualDrawLeft ? shadeWidth : -shadeWidth - round - 1;
        final int y = drawTop ? shadeWidth : -shadeWidth - round - 1;
        final int maxX = actualDrawRight ? button.getWidth () - shadeWidth - 1 : button.getWidth () + shadeWidth + round;
        final int maxY = drawBottom ? button.getHeight () - shadeWidth - 1 : button.getHeight () + shadeWidth + round;
        final int width = maxX - x;
        final int height = maxY - y;

        // Creating border shape
        if ( round > 0 )
        {
            return new RoundRectangle2D.Double ( x, y, width, height, round * 2 + ( border ? 0 : 1 ), round * 2 + ( border ? 0 : 1 ) );
        }
        else
        {
            return new Rectangle2D.Double ( x, y, width, height );
        }
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        Dimension ps = super.getPreferredSize ( c );

        // Background painter preferred size
        if ( painter != null )
        {
            ps = SwingUtils.max ( ps, painter.getPreferredSize ( c ) );
        }

        return ps;
    }
}
