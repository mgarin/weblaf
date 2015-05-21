package com.alee.managers.style.skin.web;

import com.alee.global.StyleConstants;
import com.alee.laf.button.ButtonPainter;
import com.alee.laf.button.WebButtonStyle;
import com.alee.laf.button.WebButtonUI;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.AncestorAdapter;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Mikle Garin
 */

public class WebButtonPainter<E extends JButton, U extends WebButtonUI> extends WebDecorationPainter<E, U> implements ButtonPainter<E, U>
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

    protected void updateTransparentShineColor ()
    {
        transparentShineColor = new Color ( shineColor.getRed (), shineColor.getGreen (), shineColor.getBlue (),
                Math.round ( transparency * shineColor.getAlpha () ) );
    }

    @Override
    public void setViewRect ( final Rectangle rect )
    {
        this.viewRect = rect;
    }

    @Override
    public void setTextRect ( final Rectangle rect )
    {
        this.textRect = rect;
    }

    @Override
    public void setIconRect ( final Rectangle rect )
    {
        this.iconRect = rect;
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        model = c.getModel ();
        pressed = model.isPressed () || model.isSelected ();

        // Painting button
        super.paint ( g2d, bounds, c, ui );

        // Painting icon
        paintIcon ( g2d, bounds, c, ui );

        // Painting text
        paintText ( g2d, bounds, c, ui );
    }

    @Override
    protected void paintBackground ( final Graphics2D g2d, final Rectangle bounds, final E c, final Shape backgroundShape )
    {
        g2d.setPaint ( new GradientPaint ( 0, paintTop ? shadeWidth : 0, getCurrentTopBgColor ( pressed ), 0,
                c.getHeight () - ( paintBottom ? shadeWidth : 0 ), getCurrentBottomBgColor ( pressed ) ) );
        g2d.fill ( backgroundShape );

        // Cursor-following highlight
        if ( rolloverShine && mousePoint != null && c.isEnabled () )
        {
            final Shape oldClip = GraphicsUtils.intersectClip ( g2d, backgroundShape );
            g2d.setPaint ( new RadialGradientPaint ( mousePoint.x, c.getHeight (), c.getWidth (), new float[]{ 0f, 1f },
                    new Color[]{ transparentShineColor, StyleConstants.transparent } ) );
            g2d.fill ( backgroundShape );
            GraphicsUtils.restoreClip ( g2d, oldClip );
        }
    }

    protected void paintIcon ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        if ( c.getIcon () != null )
        {
            Icon icon = c.getIcon ();
            Icon tmpIcon = null;

            if ( icon == null )
            {
                return;
            }

            Icon selectedIcon = null;

            /* the fallback icon should be based on the selected state */
            if ( model.isSelected () )
            {
                selectedIcon = c.getSelectedIcon ();
                if ( selectedIcon != null )
                {
                    icon = selectedIcon;
                }
            }

            if ( !model.isEnabled () )
            {
                if ( model.isSelected () )
                {
                    tmpIcon = c.getDisabledSelectedIcon ();
                    if ( tmpIcon == null )
                    {
                        tmpIcon = selectedIcon;
                    }
                }

                if ( tmpIcon == null )
                {
                    tmpIcon = c.getDisabledIcon ();
                }
            }
            else if ( model.isPressed () && model.isArmed () )
            {
                tmpIcon = c.getPressedIcon ();
            }
            else if ( c.isRolloverEnabled () && model.isRollover () )
            {
                if ( model.isSelected () )
                {
                    tmpIcon = c.getRolloverSelectedIcon ();
                    if ( tmpIcon == null )
                    {
                        tmpIcon = selectedIcon;
                    }
                }

                if ( tmpIcon == null )
                {
                    tmpIcon = c.getRolloverIcon ();
                }
            }

            if ( tmpIcon != null )
            {
                icon = tmpIcon;
            }

            if ( model.isPressed () && model.isArmed () )
            {
                icon.paintIcon ( c, g2d, iconRect.x, iconRect.y );
            }
            else
            {
                icon.paintIcon ( c, g2d, iconRect.x, iconRect.y );
            }
        }
    }

    protected void paintText ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        final String text = c.getText ();
        if ( text != null && !text.equals ( "" ) )
        {
            final View v = ( View ) c.getClientProperty ( BasicHTML.propertyKey );
            if ( v != null )
            {
                v.paint ( g2d, textRect );
            }
            else
            {
                final FontMetrics fm = SwingUtils.getFontMetrics ( c, g2d );
                final int mnemonicIndex = c.getDisplayedMnemonicIndex ();

                // Drawing text
                if ( model.isEnabled () )
                {
                    // Drawing normal text
                    g2d.setColor ( model.isPressed () || model.isSelected () ? selectedForeground : c.getForeground () );
                    SwingUtils.drawStringUnderlineCharAt ( g2d, text, mnemonicIndex, textRect.x, textRect.y + fm.getAscent () );
                }
                else
                {
                    // todo Paint single-colored text
                    // Drawing disabled text
                    g2d.setColor ( c.getBackground ().brighter () );
                    SwingUtils.drawStringUnderlineCharAt ( g2d, text, mnemonicIndex, textRect.x, textRect.y + fm.getAscent () );
                    g2d.setColor ( c.getBackground ().darker () );
                    SwingUtils.drawStringUnderlineCharAt ( g2d, text, mnemonicIndex, textRect.x - 1, textRect.y + fm.getAscent () - 1 );
                }
            }
        }
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
}