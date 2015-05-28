package com.alee.managers.style.skin.web;

import com.alee.extended.checkbox.CheckState;
import com.alee.global.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.checkbox.CheckIcon;
import com.alee.laf.checkbox.WebCheckBoxStyle;
import com.alee.laf.list.WebListElement;
import com.alee.laf.radiobutton.BasicStateButtonPainter;
import com.alee.laf.radiobutton.WebRadioButtonStyle;
import com.alee.laf.tree.WebTreeElement;
import com.alee.managers.style.StyleException;
import com.alee.utils.ColorUtils;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import javax.swing.plaf.basic.BasicRadioButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author Alexandr Zernov
 */

public class WebBasicStateButtonPainter<E extends AbstractButton, U extends BasicRadioButtonUI> extends WebAbstractButtonPainter<E, U>
        implements BasicStateButtonPainter<E, U>
{
    /**
     * Maximum border/background darkness.
     */
    public static final int MAX_DARKNESS = 5;

    /**
     * Animation updates delay in milliseconds.
     */
    public static final long UPDATE_DELAY = 40L;

    /**
     * Background gradient fractions.
     */
    protected static final float[] fractions = { 0f, 1f };

    /**
     * Style settings.
     */
    protected int iconWidth = 16;
    protected int iconHeight = 16;
    protected Stroke borderStroke = new BasicStroke ( 1.5f );
    protected Insets margin = WebRadioButtonStyle.margin;
    protected Color darkBorderColor = WebRadioButtonStyle.darkBorderColor;
    protected Color topBgColor = WebRadioButtonStyle.topBgColor;
    protected Color bottomBgColor = WebRadioButtonStyle.bottomBgColor;
    protected Color topSelectedBgColor = WebRadioButtonStyle.topSelectedBgColor;
    protected Color bottomSelectedBgColor = WebRadioButtonStyle.bottomSelectedBgColor;
    protected boolean animated = WebRadioButtonStyle.animated;
    protected boolean rolloverDarkBorderOnly = WebRadioButtonStyle.rolloverDarkBorderOnly;

    /**
     * Listeners.
     */
    protected MouseAdapter mouseAdapter;
    protected ItemListener itemListener;
    protected PropertyChangeListener modelChangeListener;
    protected PropertyChangeListener enabledStateListener;

    /**
     * Runtime variables.
     */
    protected CheckIcon stateIcon;
    protected boolean checking;
    protected WebTimer checkTimer;

    protected int bgDarkness = 0;
    protected boolean rollover;
    protected WebTimer bgTimer;

    /**
     * {@inheritDoc}
     */
    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Initial check state
        stateIcon = createCheckStateIcon ();
        stateIcon.setEnabled ( component.isEnabled () );
        stateIcon.setState ( component.isSelected () ? CheckState.checked : CheckState.unchecked );

        // Animation
        animated = isAnimatedByDefault ();

        // State icon
        component.setIcon ( createIcon () );

        // component state change listeners
        installEnabledStateListeners ();
        installRolloverListeners ();
        installStateChangeListeners ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Removing listeners
        uninstallEnabledStateListeners ();
        uninstallRolloverListeners ();
        uninstallStateChangeListeners ();

        component.setIcon ( null );
        stateIcon = null;

        super.uninstall ( c, ui );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        super.paint ( g2d, bounds, c, ui );
    }

    /**
     * Returns background colors.
     *
     * @return background colors
     */
    protected Color[] getBgColors ()
    {
        if ( component.isEnabled () )
        {
            final float darkness = getBgDarkness ();
            if ( darkness < 1f )
            {
                return new Color[]{ ColorUtils.getIntermediateColor ( topBgColor, topSelectedBgColor, darkness ),
                        ColorUtils.getIntermediateColor ( bottomBgColor, bottomSelectedBgColor, darkness ) };
            }
            else
            {
                return new Color[]{ topSelectedBgColor, bottomSelectedBgColor };
            }
        }
        else
        {
            return new Color[]{ topBgColor, bottomBgColor };
        }
    }

    /**
     * Returns background darkness.
     *
     * @return background darkness
     */
    protected float getBgDarkness ()
    {
        return ( float ) bgDarkness / MAX_DARKNESS;
    }

    /**
     * Installs enabled state listeners.
     */
    protected void installEnabledStateListeners ()
    {
        enabledStateListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                stateIcon.setEnabled ( component.isEnabled () );
            }
        };
        component.addPropertyChangeListener ( WebLookAndFeel.ENABLED_PROPERTY, enabledStateListener );
    }

    /**
     * Uninstalls enabled state listeners.
     */
    protected void uninstallEnabledStateListeners ()
    {
        component.removePropertyChangeListener ( WebLookAndFeel.ENABLED_PROPERTY, enabledStateListener );
        enabledStateListener = null;
    }

    /**
     * Installs rollover listeners.
     */
    protected void installRolloverListeners ()
    {
        // Background fade animation
        bgTimer = new WebTimer ( "WebStateButton.bgUpdater", UPDATE_DELAY, new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( rollover && bgDarkness < MAX_DARKNESS )
                {
                    bgDarkness++;
                    component.repaint ();
                }
                else if ( !rollover && bgDarkness > 0 )
                {
                    bgDarkness--;
                    component.repaint ();
                }
                else
                {
                    bgTimer.stop ();
                }
            }
        } );
        mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mouseEntered ( final MouseEvent e )
            {
                rollover = true;
                if ( isAnimationAllowed () )
                {
                    bgTimer.start ();
                }
                else
                {
                    bgDarkness = MAX_DARKNESS;
                    component.repaint ();
                }
            }

            @Override
            public void mouseExited ( final MouseEvent e )
            {
                rollover = false;
                if ( isAnimationAllowed () )
                {
                    bgTimer.start ();
                }
                else
                {
                    bgDarkness = 0;
                    component.repaint ();
                }
            }
        };
        component.addMouseListener ( mouseAdapter );
    }

    /**
     * Uninstalls rollover listeners.
     */
    protected void uninstallRolloverListeners ()
    {
        component.removeMouseListener ( mouseAdapter );
        mouseAdapter = null;
        bgTimer.stop ();
    }

    /**
     * Installs state change listeners.
     */
    protected void installStateChangeListeners ()
    {
        // Selection changes animation
        checkTimer = new WebTimer ( "WebStateButton.iconUpdater", UPDATE_DELAY, new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( stateIcon.isTransitionCompleted () )
                {
                    stateIcon.finishTransition ();
                    checkTimer.stop ();
                }
                else
                {
                    stateIcon.doStep ();
                    component.repaint ();
                }
            }
        } );
        itemListener = new ItemListener ()
        {
            @Override
            public void itemStateChanged ( final ItemEvent e )
            {
                performStateChanged ();
            }
        };
        component.addItemListener ( itemListener );

        // Proper state update on model changes
        modelChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent e )
            {
                performStateChanged ();
            }
        };
        component.addPropertyChangeListener ( WebLookAndFeel.MODEL_PROPERTY, modelChangeListener );
    }

    /**
     * Uninstalls state change listeners.
     */
    protected void uninstallStateChangeListeners ()
    {
        component.removeItemListener ( itemListener );
        itemListener = null;
        component.removePropertyChangeListener ( modelChangeListener );
        modelChangeListener = null;
        checkTimer.stop ();
        checkTimer = null;
    }

    /**
     * Performs visual state change.
     * In case animation is possible and enabled state change will be animated.
     */
    protected void performStateChanged ()
    {
        if ( isAnimationAllowed () )
        {
            stateIcon.setNextState ( component.isSelected () ? CheckState.checked : CheckState.unchecked );
            checkTimer.start ();
        }
        else
        {
            checkTimer.stop ();
            stateIcon.setState ( component.isSelected () ? CheckState.checked : CheckState.unchecked );
            component.repaint ();
        }
    }

    /**
     * Returns whether component can be animated right now or not.
     *
     * @return true if component can be animated right now, false otherwise
     */
    protected boolean isAnimationAllowed ()
    {
        return animated && component.isEnabled () && component.isShowing () && isAnimationAllowedByParent ();
    }

    /**
     * Returns whether or not component animation is allowed by parent.
     *
     * @return true if component animation is allowed by parent, false otherwise
     */
    protected boolean isAnimationAllowedByParent ()
    {
        // Workaround for list and tree elements, those should not be animated, at least yet
        // The only exception here is {@link com.alee.extended.list.WebCheckBoxListElement} but we don't mention it here
        final Container parent = component.getParent ();
        return parent == null || !WebListElement.class.isAssignableFrom ( component.getParent ().getClass () ) &&
                !WebTreeElement.class.isAssignableFrom ( component.getParent ().getClass () ) &&
                !ReflectUtils.containsInClassOrSuperclassName ( parent.getClass (), "com.jidesoft.swing.CheckBoxList" ) &&
                !ReflectUtils.containsInClassOrSuperclassName ( parent.getClass (), "com.jidesoft.swing.CheckBoxTreeCellRenderer" );
    }

    /**
     * Returns whether component should be animated by default or not.
     *
     * @return true if component should be animated by default, false otherwise
     */
    protected boolean isAnimatedByDefault ()
    {
        // Workaround for Jide tristate checkbox
        return WebCheckBoxStyle.animated &&
                !ReflectUtils.containsInClassOrSuperclassName ( component.getClass (), "com.jidesoft.swing.TristateCheckBox" );
    }

    /**
     * Returns icon bounds.
     *
     * @return icon bounds
     */
    @Override
    public Rectangle getIconRect ()
    {
        return iconRect != null ? new Rectangle ( iconRect ) : new Rectangle ();
    }

    /**
     * Creates component state icon.
     *
     * @return component state icon
     */
    protected CheckIcon createCheckStateIcon ()
    {
        throw new StyleException ( "Basic state icon is not supported" );
    }

    /**
     * Creates and returns component state icon.
     *
     * @return component state icon
     */
    protected Icon createIcon ()
    {
        return new StateIcon ();
    }

    /**
     * Paints component state icon.
     *
     * @param g2d 2D graphics context
     * @param x   icon location X coordinate
     * @param y   icon location Y coordinate
     */
    protected void paintIcon ( final Graphics2D g2d, final int x, final int y )
    {
        // Icon background
        paintIconBackground ( g2d, x, y );

        // Check icon
        paintStateIcon ( g2d, x, y );
    }

    /**
     * Paints component state icon background.
     *
     * @param g2d 2D graphics context
     * @param x   icon location X coordinate
     * @param y   icon location Y coordinate
     */
    protected void paintIconBackground ( final Graphics2D g2d, final int x, final int y )
    {
        final boolean enabled = component.isEnabled ();

        // Button size and shape
        final Rectangle iconRect =
                new Rectangle ( x + shadeWidth, y + shadeWidth, iconWidth - shadeWidth * 2 - 1, iconHeight - shadeWidth * 2 - 1 );
        final RoundRectangle2D shape =
                new RoundRectangle2D.Double ( iconRect.x, iconRect.y, iconRect.width, iconRect.height, round * 2, round * 2 );

        // Shade
        if ( enabled )
        {
            final Color shadeColor = component.isFocusOwner () ? StyleConstants.fieldFocusColor : StyleConstants.shadeColor;
            GraphicsUtils.drawShade ( g2d, shape, shadeColor, shadeWidth );
        }

        // Background
        final int radius = Math.round ( ( float ) Math.sqrt ( iconRect.width * iconRect.width / 2 ) );
        final int cx = iconRect.x + iconRect.width / 2;
        final int cy = iconRect.y + iconRect.height / 2;
        g2d.setPaint ( new RadialGradientPaint ( cx, cy, radius, fractions, getBgColors () ) );
        g2d.fill ( shape );

        // Border
        final Stroke os = GraphicsUtils.setupStroke ( g2d, borderStroke );
        g2d.setPaint ( enabled ?
                rolloverDarkBorderOnly ? ColorUtils.getIntermediateColor ( borderColor, darkBorderColor, getBgDarkness () ) :
                        darkBorderColor : disabledBorderColor );
        g2d.draw ( shape );
        GraphicsUtils.restoreStroke ( g2d, os );
    }

    /**
     * Paints component state icon.
     *
     * @param g2d 2D graphics context
     * @param x   icon location X coordinate
     * @param y   icon location Y coordinate
     */
    protected void paintStateIcon ( final Graphics2D g2d, final int x, final int y )
    {
        stateIcon.paintIcon ( component, g2d, x, y, iconWidth, iconHeight );
    }

    /**
     * Custom state icon.
     */
    protected class StateIcon implements Icon
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public void paintIcon ( final Component c, final Graphics g, final int x, final int y )
        {
            // Updating actual icon rect
            iconRect = new Rectangle ( x, y, iconWidth, iconHeight );

            // Painting state icon
            final Graphics2D g2d = ( Graphics2D ) g;
            final Object aa = GraphicsUtils.setupAntialias ( g2d );
            WebBasicStateButtonPainter.this.paintIcon ( g2d, x, y );
            GraphicsUtils.restoreAntialias ( g2d, aa );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getIconWidth ()
        {
            return iconWidth;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getIconHeight ()
        {
            return iconHeight;
        }
    }
}