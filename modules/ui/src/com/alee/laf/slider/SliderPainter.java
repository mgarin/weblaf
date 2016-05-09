package com.alee.laf.slider;

import com.alee.global.StyleConstants;
import com.alee.painter.AbstractPainter;
import com.alee.utils.ColorUtils;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.MathUtils;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Dictionary;
import java.util.Enumeration;

/**
 * Basic painter for JSlider component.
 * It is used as WebSliderUI default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public class SliderPainter<E extends JSlider, U extends WebSliderUI> extends AbstractPainter<E, U> implements ISliderPainter<E, U>
{
    public static final int MAX_DARKNESS = 5;

    /**
     * Style settings.
     */
    protected int trackHeight;
    protected int trackRound;
    protected int trackShadeWidth;
    protected int progressRound;
    protected int progressShadeWidth;
    protected int thumbWidth;
    protected int thumbHeight;
    protected int thumbRound;
    protected int thumbAngleLength;
    protected Color trackBgTop;
    protected Color trackBgBottom;
    protected Color progressTrackBgTop;
    protected Color progressTrackBgBottom;
    protected Color progressBorderColor;
    protected Color thumbBgTop;
    protected Color thumbBgBottom;
    protected boolean drawProgress;
    protected boolean drawThumb;
    protected boolean angledThumb;
    protected boolean sharpThumbAngle;
    protected boolean rolloverDarkBorderOnly;
    protected boolean animated;

    /**
     * Listeners.
     */
    protected MouseWheelListener mouseWheelListener;
    protected ChangeListener changeListener;
    protected MouseAdapter mouseAdapter;

    /**
     * Runtime variables.
     */
    protected boolean rollover = false;
    protected int rolloverDarkness = 0;
    protected WebTimer rolloverTimer;

    /**
     * Painting variables.
     */
    protected int trackBuffer = 0;  // The distance that the track is from the side of the control
    protected Insets insetCache = null;
    protected Rectangle focusRect = null;
    protected Rectangle contentRect = null;
    protected Rectangle labelRect = null;
    protected Rectangle tickRect = null;
    protected Rectangle trackRect = null;
    protected Rectangle thumbRect = null;
    protected boolean leftToRightCache = true;
    protected boolean dragging = false;

    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Initializing caches
        insetCache = c.getInsets ();
        leftToRightCache = c.getComponentOrientation ().isLeftToRight ();
        focusRect = new Rectangle ();
        contentRect = new Rectangle ();
        labelRect = new Rectangle ();
        tickRect = new Rectangle ();
        trackRect = new Rectangle ();
        thumbRect = new Rectangle ();

        // Rollover mouse wheel scroll
        mouseWheelListener = new MouseWheelListener ()
        {
            @Override
            public void mouseWheelMoved ( final MouseWheelEvent e )
            {
                final int v = component.getValue () - e.getWheelRotation ();
                component.setValue ( MathUtils.limit ( component.getMinimum (), v, component.getMaximum () ) );
            }
        };
        component.addMouseWheelListener ( mouseWheelListener );

        // todo Requires optimizations
        // Fixed value change repaint
        changeListener = new ChangeListener ()
        {
            @Override
            public void stateChanged ( final ChangeEvent e )
            {
                calculateThumbLocation ();
                component.repaint ();
            }
        };
        component.addChangeListener ( changeListener );

        // todo Requires optimizations
        // Rollover animator
        rolloverTimer = new WebTimer ( "WebProgressBarUI.animator", 40, new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( rollover && rolloverDarkness < MAX_DARKNESS )
                {
                    rolloverDarkness++;
                    component.repaint ();
                }
                else if ( !rollover && rolloverDarkness > 0 )
                {
                    rolloverDarkness--;
                    component.repaint ();
                }
                else
                {
                    rolloverTimer.stop ();
                }
            }
        } );
        mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
            {
                component.repaint ();
            }

            @Override
            public void mouseReleased ( final MouseEvent e )
            {
                component.repaint ();
            }

            @Override
            public void mouseDragged ( final MouseEvent e )
            {
                component.repaint ();
            }

            @Override
            public void mouseEntered ( final MouseEvent e )
            {
                rollover = true;
                if ( animated && c.isEnabled () )
                {
                    rolloverTimer.start ();
                }
                else
                {
                    rolloverDarkness = MAX_DARKNESS;
                    component.repaint ();
                }
            }

            @Override
            public void mouseExited ( final MouseEvent e )
            {
                rollover = false;
                if ( animated && c.isEnabled () )
                {
                    rolloverTimer.start ();
                }
                else
                {
                    rolloverDarkness = 0;
                    component.repaint ();
                }
            }
        };
        component.addMouseListener ( mouseAdapter );
        component.addMouseMotionListener ( mouseAdapter );
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Removing listeners
        component.removeMouseWheelListener ( mouseWheelListener );
        mouseWheelListener = null;
        component.removeChangeListener ( changeListener );
        changeListener = null;
        component.removeMouseListener ( mouseAdapter );
        component.removeMouseMotionListener ( mouseAdapter );
        mouseAdapter = null;

        // Cleaning up caches
        insetCache = null;
        leftToRightCache = true;
        focusRect = null;
        contentRect = null;
        labelRect = null;
        tickRect = null;
        trackRect = null;
        thumbRect = null;

        super.uninstall ( c, ui );
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        recalculateIfInsetsChanged ();
        recalculateIfOrientationChanged ();
        final Rectangle clip = g2d.getClipBounds ();

        if ( component.getPaintTrack () && !clip.intersects ( trackRect ) )
        {
            calculateGeometry ();
        }
        if ( component.getPaintTrack () && clip.intersects ( trackRect ) )
        {
            paintTrack ( g2d );
        }
        if ( component.getPaintTicks () && clip.intersects ( tickRect ) )
        {
            paintTicks ( g2d );
        }
        if ( component.getPaintLabels () && clip.intersects ( labelRect ) )
        {
            paintLabels ( g2d );
        }
        //        if ( component.hasFocus() && clip.intersects( focusRect ) ) {
        //            paintFocus( g2d );
        //        }
        if ( clip.intersects ( thumbRect ) )
        {
            paintThumb ( g2d );
        }
    }

    protected void recalculateIfInsetsChanged ()
    {
        final Insets newInsets = component.getInsets ();
        if ( !newInsets.equals ( insetCache ) )
        {
            insetCache = newInsets;
            calculateGeometry ();
        }
    }

    protected void recalculateIfOrientationChanged ()
    {
        if ( ltr != leftToRightCache )
        {
            leftToRightCache = ltr;
            calculateGeometry ();
        }
    }

    protected void calculateGeometry ()
    {
        calculateFocusRect ();
        calculateContentRect ();
        calculateThumbSize ();
        calculateTrackBuffer ();
        calculateTrackRect ();
        calculateTickRect ();
        calculateLabelRect ();
        calculateThumbLocation ();
    }

    protected void calculateFocusRect ()
    {
        focusRect.x = insetCache.left;
        focusRect.y = insetCache.top;
        focusRect.width = component.getWidth () - ( insetCache.left + insetCache.right );
        focusRect.height = component.getHeight () - ( insetCache.top + insetCache.bottom );
    }

    protected void calculateThumbSize ()
    {
        final Dimension size = getThumbSize ();
        thumbRect.setSize ( size.width, size.height );
    }

    protected void calculateContentRect ()
    {
        contentRect.x = focusRect.x;
        contentRect.y = focusRect.y;
        contentRect.width = focusRect.width;
        contentRect.height = focusRect.height;
    }

    protected void calculateThumbLocation ()
    {
        if ( component.getSnapToTicks () )
        {
            final int sliderValue = component.getValue ();
            int snappedValue = sliderValue;
            final int majorTickSpacing = component.getMajorTickSpacing ();
            final int minorTickSpacing = component.getMinorTickSpacing ();
            int tickSpacing = 0;

            if ( minorTickSpacing > 0 )
            {
                tickSpacing = minorTickSpacing;
            }
            else if ( majorTickSpacing > 0 )
            {
                tickSpacing = majorTickSpacing;
            }

            if ( tickSpacing != 0 )
            {
                // If it's not on a tick, change the value
                if ( ( sliderValue - component.getMinimum () ) % tickSpacing != 0 )
                {
                    final float temp = ( float ) ( sliderValue - component.getMinimum () ) / ( float ) tickSpacing;
                    final int whichTick = Math.round ( temp );
                    snappedValue = component.getMinimum () + ( whichTick * tickSpacing );
                }

                if ( snappedValue != sliderValue )
                {
                    component.setValue ( snappedValue );
                }
            }
        }

        if ( component.getOrientation () == JSlider.HORIZONTAL )
        {
            final int valuePosition = xPositionForValue ( component.getValue () );

            thumbRect.x = valuePosition - ( thumbRect.width / 2 );
            thumbRect.y = trackRect.y;
        }
        else
        {
            final int valuePosition = yPositionForValue ( component.getValue () );

            thumbRect.x = trackRect.x;
            thumbRect.y = valuePosition - ( thumbRect.height / 2 );
        }
    }

    protected void calculateTrackBuffer ()
    {
        if ( component.getPaintLabels () && component.getLabelTable () != null )
        {
            final Component highLabel = getHighestValueLabel ();
            final Component lowLabel = getLowestValueLabel ();

            if ( component.getOrientation () == JSlider.HORIZONTAL )
            {
                trackBuffer = Math.max ( highLabel.getBounds ().width, lowLabel.getBounds ().width ) / 2;
                trackBuffer = Math.max ( trackBuffer, thumbRect.width / 2 );
            }
            else
            {
                trackBuffer = Math.max ( highLabel.getBounds ().height, lowLabel.getBounds ().height ) / 2;
                trackBuffer = Math.max ( trackBuffer, thumbRect.height / 2 );
            }
        }
        else
        {
            if ( component.getOrientation () == JSlider.HORIZONTAL )
            {
                trackBuffer = thumbRect.width / 2;
            }
            else
            {
                trackBuffer = thumbRect.height / 2;
            }
        }
    }

    protected void calculateTrackRect ()
    {
        int centerSpacing; // used to center sliders added using BorderLayout.CENTER (bug 4275631)
        if ( component.getOrientation () == JSlider.HORIZONTAL )
        {
            centerSpacing = thumbRect.height;
            if ( component.getPaintTicks () )
            {
                centerSpacing += getTickLength ();
            }
            if ( component.getPaintLabels () )
            {
                centerSpacing += getHeightOfTallestLabel ();
            }
            trackRect.x = contentRect.x + trackBuffer;
            trackRect.y = contentRect.y + ( contentRect.height - centerSpacing - 1 ) / 2;
            trackRect.width = contentRect.width - ( trackBuffer * 2 );
            trackRect.height = thumbRect.height;
        }
        else
        {
            centerSpacing = thumbRect.width;
            if ( ltr )
            {
                if ( component.getPaintTicks () )
                {
                    centerSpacing += getTickLength ();
                }
                if ( component.getPaintLabels () )
                {
                    centerSpacing += getWidthOfWidestLabel ();
                }
            }
            else
            {
                if ( component.getPaintTicks () )
                {
                    centerSpacing -= getTickLength ();
                }
                if ( component.getPaintLabels () )
                {
                    centerSpacing -= getWidthOfWidestLabel ();
                }
            }
            trackRect.x = contentRect.x + ( contentRect.width - centerSpacing - 1 ) / 2;
            trackRect.y = contentRect.y + trackBuffer;
            trackRect.width = thumbRect.width;
            trackRect.height = contentRect.height - ( trackBuffer * 2 );
        }
    }

    /**
     * Gets the height of the tick area for horizontal sliders and the width of the
     * tick area for vertical sliders.  BasicSliderUI uses the returned value to
     * determine the tick area rectangle.  If you want to give your ticks some room,
     * make this larger than you need and paint your ticks away from the sides in paintTicks().
     */
    protected int getTickLength ()
    {
        return 8;
    }

    protected void calculateTickRect ()
    {
        if ( component.getOrientation () == JSlider.HORIZONTAL )
        {
            tickRect.x = trackRect.x;
            tickRect.y = trackRect.y + trackRect.height;
            tickRect.width = trackRect.width;
            tickRect.height = ( component.getPaintTicks () ) ? getTickLength () : 0;
        }
        else
        {
            tickRect.width = ( component.getPaintTicks () ) ? getTickLength () : 0;
            if ( ltr )
            {
                tickRect.x = trackRect.x + trackRect.width;
            }
            else
            {
                tickRect.x = trackRect.x - tickRect.width;
            }
            tickRect.y = trackRect.y;
            tickRect.height = trackRect.height;
        }
    }

    protected void calculateLabelRect ()
    {
        if ( component.getPaintLabels () )
        {
            if ( component.getOrientation () == JSlider.HORIZONTAL )
            {
                labelRect.x = tickRect.x - trackBuffer;
                labelRect.y = tickRect.y + tickRect.height;
                labelRect.width = tickRect.width + ( trackBuffer * 2 );
                labelRect.height = getHeightOfTallestLabel ();
            }
            else
            {
                if ( ltr )
                {
                    labelRect.x = tickRect.x + tickRect.width;
                    labelRect.width = getWidthOfWidestLabel ();
                }
                else
                {
                    labelRect.width = getWidthOfWidestLabel ();
                    labelRect.x = tickRect.x - labelRect.width;
                }
                labelRect.y = tickRect.y - trackBuffer;
                labelRect.height = tickRect.height + ( trackBuffer * 2 );
            }
        }
        else
        {
            if ( component.getOrientation () == JSlider.HORIZONTAL )
            {
                labelRect.x = tickRect.x;
                labelRect.y = tickRect.y + tickRect.height;
                labelRect.width = tickRect.width;
                labelRect.height = 0;
            }
            else
            {
                if ( ltr )
                {
                    labelRect.x = tickRect.x + tickRect.width;
                }
                else
                {
                    labelRect.x = tickRect.x;
                }
                labelRect.y = tickRect.y;
                labelRect.width = 0;
                labelRect.height = tickRect.height;
            }
        }
    }

    protected int xPositionForValue ( final int value )
    {
        final int min = component.getMinimum ();
        final int max = component.getMaximum ();
        final int trackLength = trackRect.width;
        final double valueRange = ( double ) max - ( double ) min;
        final double pixelsPerValue = ( double ) trackLength / valueRange;
        final int trackLeft = trackRect.x;
        final int trackRight = trackRect.x + ( trackRect.width - 1 );
        int xPosition;

        if ( !drawInverted () )
        {
            xPosition = trackLeft;
            xPosition += Math.round ( pixelsPerValue * ( ( double ) value - min ) );
        }
        else
        {
            xPosition = trackRight;
            xPosition -= Math.round ( pixelsPerValue * ( ( double ) value - min ) );
        }

        xPosition = Math.max ( trackLeft, xPosition );
        xPosition = Math.min ( trackRight, xPosition );

        return xPosition;
    }

    protected int yPositionForValue ( final int value )
    {
        return yPositionForValue ( value, trackRect.y, trackRect.height );
    }

    /**
     * Returns the y location for the specified value.
     * No checking is done on the arguments.
     * In particular if {@code trackHeight} is negative undefined results may occur.
     *
     * @param value       the slider value to get the location for
     * @param trackY      y-origin of the track
     * @param trackHeight the height of the track
     */
    protected int yPositionForValue ( final int value, final int trackY, final int trackHeight )
    {
        final int min = component.getMinimum ();
        final int max = component.getMaximum ();
        final double valueRange = ( double ) max - ( double ) min;
        final double pixelsPerValue = ( double ) trackHeight / valueRange;
        final int trackBottom = trackY + ( trackHeight - 1 );
        int yPosition;

        if ( !drawInverted () )
        {
            yPosition = trackY;
            yPosition += Math.round ( pixelsPerValue * ( ( double ) max - value ) );
        }
        else
        {
            yPosition = trackY;
            yPosition += Math.round ( pixelsPerValue * ( ( double ) value - min ) );
        }

        yPosition = Math.max ( trackY, yPosition );
        yPosition = Math.min ( trackBottom, yPosition );

        return yPosition;
    }

    protected boolean drawInverted ()
    {
        if ( component.getOrientation () == JSlider.HORIZONTAL )
        {
            if ( ltr )
            {
                return component.getInverted ();
            }
            else
            {
                return !component.getInverted ();
            }
        }
        else
        {
            return component.getInverted ();
        }
    }

    /**
     * Returns the biggest value that has an entry in the label table.
     *
     * @return biggest value that has an entry in the label table, or
     * null.
     */
    protected Integer getHighestValue ()
    {
        final Dictionary dictionary = component.getLabelTable ();

        if ( dictionary == null )
        {
            return null;
        }

        final Enumeration keys = dictionary.keys ();

        Integer max = null;

        while ( keys.hasMoreElements () )
        {
            final Integer i = ( Integer ) keys.nextElement ();

            if ( max == null || i > max )
            {
                max = i;
            }
        }

        return max;
    }

    /**
     * Returns the smallest value that has an entry in the label table.
     *
     * @return smallest value that has an entry in the label table, or
     * null.
     */
    protected Integer getLowestValue ()
    {
        final Dictionary dictionary = component.getLabelTable ();

        if ( dictionary == null )
        {
            return null;
        }

        final Enumeration keys = dictionary.keys ();

        Integer min = null;

        while ( keys.hasMoreElements () )
        {
            final Integer i = ( Integer ) keys.nextElement ();

            if ( min == null || i < min )
            {
                min = i;
            }
        }

        return min;
    }

    /**
     * Returns the label that corresponds to the highest slider value in the label table.
     *
     * @see javax.swing.JSlider#setLabelTable
     */
    protected Component getLowestValueLabel ()
    {
        final Integer min = getLowestValue ();
        if ( min != null )
        {
            return ( Component ) component.getLabelTable ().get ( min );
        }
        return null;
    }

    /**
     * Returns the label that corresponds to the lowest slider value in the label table.
     *
     * @see javax.swing.JSlider#setLabelTable
     */
    protected Component getHighestValueLabel ()
    {
        final Integer max = getHighestValue ();
        if ( max != null )
        {
            return ( Component ) component.getLabelTable ().get ( max );
        }
        return null;
    }

    protected int getWidthOfWidestLabel ()
    {
        final Dictionary dictionary = component.getLabelTable ();
        int widest = 0;
        if ( dictionary != null )
        {
            final Enumeration keys = dictionary.keys ();
            while ( keys.hasMoreElements () )
            {
                final Component label = ( Component ) dictionary.get ( keys.nextElement () );
                widest = Math.max ( label.getPreferredSize ().width, widest );
            }
        }
        return widest;
    }

    protected int getHeightOfTallestLabel ()
    {
        final Dictionary dictionary = component.getLabelTable ();
        int tallest = 0;
        if ( dictionary != null )
        {
            final Enumeration keys = dictionary.keys ();
            while ( keys.hasMoreElements () )
            {
                final Component label = ( Component ) dictionary.get ( keys.nextElement () );
                tallest = Math.max ( label.getPreferredSize ().height, tallest );
            }
        }
        return tallest;
    }

    protected Dimension getThumbSize ()
    {
        if ( component.getOrientation () == JSlider.HORIZONTAL )
        {
            return new Dimension ( thumbWidth, thumbHeight );
        }
        else
        {
            return new Dimension ( thumbHeight, thumbWidth );
        }
    }

    public void paintThumb ( final Graphics2D g2d )
    {
        if ( drawThumb )
        {
            final Object aa = GraphicsUtils.setupAntialias ( g2d );

            // Thumb shape
            final Shape ts = getThumbShape ();

            //            // Thumb shade
            //            if ( slider.isEnabled () )
            //            {
            //                LafUtils.drawShade ( g2d, ts, StyleConstants.shadeColor, thumbShadeWidth );
            //            }

            // Thumb background
            if ( component.getOrientation () == JSlider.HORIZONTAL )
            {
                g2d.setPaint ( new GradientPaint ( 0, thumbRect.y, thumbBgTop, 0, thumbRect.y + thumbRect.height, thumbBgBottom ) );
            }
            else
            {
                g2d.setPaint ( new GradientPaint ( thumbRect.x, 0, thumbBgTop, thumbRect.x + thumbRect.width, 0, thumbBgBottom ) );
            }
            g2d.fill ( ts );

            // Thumb border
            g2d.setPaint ( component.isEnabled () ? StyleConstants.darkBorderColor : StyleConstants.disabledBorderColor );
            g2d.draw ( ts );

            // Thumb focus
            //        LafUtils.drawCustomWebFocus ( g2d, slider, FocusType.fieldFocus, ts );

            GraphicsUtils.restoreAntialias ( g2d, aa );
        }
    }

    protected Shape getThumbShape ()
    {
        if ( angledThumb && ( component.getPaintLabels () || component.getPaintTicks () ) )
        {
            if ( component.getOrientation () == JSlider.HORIZONTAL )
            {
                final GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
                gp.moveTo ( thumbRect.x, thumbRect.y + thumbRound );
                gp.quadTo ( thumbRect.x, thumbRect.y, thumbRect.x + thumbRound, thumbRect.y );
                gp.lineTo ( thumbRect.x + thumbRect.width - thumbRound, thumbRect.y );
                gp.quadTo ( thumbRect.x + thumbRect.width, thumbRect.y, thumbRect.x + thumbRect.width, thumbRect.y + thumbRound );
                gp.lineTo ( thumbRect.x + thumbRect.width, thumbRect.y + thumbRect.height - thumbAngleLength );
                if ( sharpThumbAngle )
                {
                    gp.lineTo ( thumbRect.x + thumbRect.width / 2, thumbRect.y + thumbRect.height );
                    gp.lineTo ( thumbRect.x, thumbRect.y + thumbRect.height - thumbAngleLength );
                }
                else
                {
                    gp.quadTo ( thumbRect.x + thumbRect.width, thumbRect.y + thumbRect.height - thumbAngleLength / 2,
                            thumbRect.x + thumbRect.width / 2, thumbRect.y + thumbRect.height );
                    gp.quadTo ( thumbRect.x, thumbRect.y + thumbRect.height - thumbAngleLength / 2, thumbRect.x,
                            thumbRect.y + thumbRect.height - thumbAngleLength );
                }
                gp.closePath ();
                return gp;
            }
            else
            {
                final GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
                if ( ltr )
                {
                    gp.moveTo ( thumbRect.x, thumbRect.y + thumbRound );
                    gp.quadTo ( thumbRect.x, thumbRect.y, thumbRect.x + thumbRound, thumbRect.y );
                    gp.lineTo ( thumbRect.x + thumbRect.width - thumbAngleLength, thumbRect.y );
                    if ( sharpThumbAngle )
                    {
                        gp.lineTo ( thumbRect.x + thumbRect.width, thumbRect.y + thumbRect.height / 2 );
                        gp.lineTo ( thumbRect.x + thumbRect.width - thumbAngleLength, thumbRect.y + thumbRect.height );
                    }
                    else
                    {
                        gp.quadTo ( thumbRect.x + thumbRect.width - thumbAngleLength / 2, thumbRect.y, thumbRect.x + thumbRect.width,
                                thumbRect.y + thumbRect.height / 2 );
                        gp.quadTo ( thumbRect.x + thumbRect.width - thumbAngleLength / 2, thumbRect.y + thumbRect.height,
                                thumbRect.x + thumbRect.width - thumbAngleLength, thumbRect.y + thumbRect.height );
                    }
                    gp.lineTo ( thumbRect.x + thumbRound, thumbRect.y + thumbRect.height );
                    gp.quadTo ( thumbRect.x, thumbRect.y + thumbRect.height, thumbRect.x, thumbRect.y + thumbRect.height - thumbRound );
                }
                else
                {
                    gp.moveTo ( thumbRect.x + thumbRect.width - thumbRound, thumbRect.y );
                    gp.quadTo ( thumbRect.x + thumbRect.width, thumbRect.y, thumbRect.x + thumbRect.width, thumbRect.y + thumbRound );
                    gp.lineTo ( thumbRect.x + thumbRect.width, thumbRect.y + thumbRect.height - thumbRound );
                    gp.quadTo ( thumbRect.x + thumbRect.width, thumbRect.y + thumbRect.height, thumbRect.x + thumbRect.width - thumbRound,
                            thumbRect.y + thumbRect.height );
                    gp.lineTo ( thumbRect.x + thumbAngleLength, thumbRect.y + thumbRect.height );
                    if ( sharpThumbAngle )
                    {
                        gp.lineTo ( thumbRect.x, thumbRect.y + thumbRect.height / 2 );
                        gp.lineTo ( thumbRect.x + thumbAngleLength, thumbRect.y );
                    }
                    else
                    {
                        gp.quadTo ( thumbRect.x + thumbAngleLength / 2, thumbRect.y + thumbRect.height, thumbRect.x,
                                thumbRect.y + thumbRect.height / 2 );
                        gp.quadTo ( thumbRect.x + thumbAngleLength / 2, thumbRect.y, thumbRect.x + thumbAngleLength, thumbRect.y );
                    }
                }
                gp.closePath ();
                return gp;
            }
        }
        else
        {
            return new RoundRectangle2D.Double ( thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height, thumbRound * 2,
                    thumbRound * 2 );
        }
    }

    public void paintTrack ( final Graphics2D g2d )
    {
        final Object aa = GraphicsUtils.setupAntialias ( g2d );

        // Track shape
        final Shape ss = getTrackShape ();

        // Track background & shade
        {
            // Track shade
            if ( component.isEnabled () )
            {
                GraphicsUtils.drawShade ( g2d, ss, component.isFocusOwner () ? StyleConstants.fieldFocusColor : new Color ( 210, 210, 210 ),
                        trackShadeWidth );
            }

            // Track background
            if ( component.getOrientation () == JSlider.HORIZONTAL )
            {
                g2d.setPaint ( new GradientPaint ( 0, trackRect.y, trackBgTop, 0, trackRect.y + trackRect.height, trackBgBottom ) );
            }
            else
            {
                g2d.setPaint ( new GradientPaint ( trackRect.x, 0, trackBgTop, trackRect.x + trackRect.width, 0, trackBgBottom ) );
            }
            g2d.fill ( ss );
        }

        // Inner progress line
        if ( drawProgress )
        {
            // Progress shape
            final Shape ps = getProgressShape ();

            // Progress shade
            if ( component.isEnabled () )
            {
                GraphicsUtils.drawShade ( g2d, ps, new Color ( 210, 210, 210 ), progressShadeWidth );
            }

            // Progress background
            final Rectangle bounds = ss.getBounds ();
            if ( component.getOrientation () == JSlider.HORIZONTAL )
            {
                g2d.setPaint ( new GradientPaint ( 0, bounds.y + progressShadeWidth, progressTrackBgTop, 0,
                        bounds.y + bounds.height - progressShadeWidth, progressTrackBgBottom ) );
            }
            else
            {
                g2d.setPaint ( new GradientPaint ( bounds.x + progressShadeWidth, 0, progressTrackBgTop,
                        bounds.x + bounds.width - progressShadeWidth, 0, progressTrackBgBottom ) );
            }
            g2d.fill ( ps );

            // Progress border
            g2d.setPaint ( component.isEnabled () ? progressBorderColor : StyleConstants.disabledBorderColor );
            g2d.draw ( ps );
        }

        // Track border & focus
        {
            // Track border
            g2d.setPaint (
                    component.isEnabled () ? rolloverDarkBorderOnly && !dragging ? getBorderColor () : StyleConstants.darkBorderColor :
                            StyleConstants.disabledBorderColor );
            g2d.draw ( ss );
        }

        GraphicsUtils.restoreAntialias ( g2d, aa );
    }

    protected Shape getTrackShape ()
    {
        if ( trackRound > 0 )
        {
            if ( component.getOrientation () == JSlider.HORIZONTAL )
            {
                return new RoundRectangle2D.Double ( trackRect.x - trackRound, trackRect.y + trackRect.height / 2 - trackHeight / 2,
                        trackRect.width + trackRound * 2 - 1, trackHeight, trackRound * 2, trackRound * 2 );
            }
            else
            {
                return new RoundRectangle2D.Double ( trackRect.x + trackRect.width / 2 - trackHeight / 2, trackRect.y - trackRound,
                        trackHeight, trackRect.height + trackRound * 2 - 1, trackRound * 2, trackRound * 2 );
            }
        }
        else
        {
            if ( component.getOrientation () == JSlider.HORIZONTAL )
            {
                return new Rectangle2D.Double ( trackRect.x, trackRect.y + trackRect.height / 2 - trackHeight / 2, trackRect.width - 1,
                        trackHeight );
            }
            else
            {
                return new Rectangle2D.Double ( trackRect.x + trackRect.width / 2 - trackHeight / 2, trackRect.y, trackHeight,
                        trackRect.height - 1 );
            }
        }
    }

    protected Shape getProgressShape ()
    {
        if ( trackRound > 0 )
        {
            if ( component.getOrientation () == JSlider.HORIZONTAL )
            {
                final int x;
                final int w;
                if ( ltr )
                {
                    x = trackRect.x - trackRound + progressShadeWidth;
                    w = thumbRect.x + thumbRect.width / 2 + progressRound - x;
                }
                else
                {
                    x = thumbRect.x + thumbRect.width / 2 - progressRound;
                    w = trackRect.x + trackRect.width + trackRound -
                            progressShadeWidth - 1 - x;
                }
                return new RoundRectangle2D.Double ( x, trackRect.y + trackRect.height / 2 - trackHeight / 2 + progressShadeWidth, w,
                        trackHeight - progressShadeWidth * 2, progressRound * 2, progressRound * 2 );
            }
            else
            {
                final int y = thumbRect.y + thumbRect.height / 2;
                final int h = trackRect.y + trackRect.height + trackRound - progressShadeWidth - y - 1;
                return new RoundRectangle2D.Double ( trackRect.x + progressShadeWidth + trackRect.width / 2 - trackHeight / 2, y,
                        trackHeight - progressShadeWidth * 2, h, progressRound * 2, progressRound * 2 );
            }
        }
        else
        {
            if ( component.getOrientation () == JSlider.HORIZONTAL )
            {
                final int x;
                final int w;
                if ( ltr )
                {
                    x = trackRect.x + progressShadeWidth;
                    w = thumbRect.x + thumbRect.width / 2 - x;
                }
                else
                {
                    x = thumbRect.x + thumbRect.width / 2;
                    w = trackRect.x + trackRect.width - progressShadeWidth - 1 - x;
                }
                return new Rectangle2D.Double ( x, trackRect.y + trackRect.height / 2 - trackHeight / 2 + progressShadeWidth, w,
                        trackHeight - progressShadeWidth * 2 );
            }
            else
            {
                final int y = trackRect.y + progressShadeWidth;
                final int h = thumbRect.y + thumbRect.height / 2 - y;
                return new Rectangle2D.Double ( trackRect.x + progressShadeWidth + trackRect.width / 2 - trackHeight / 2, y,
                        trackHeight - progressShadeWidth * 2, h );
            }
        }
    }

    public void paintTicks ( final Graphics g )
    {
        final Rectangle tickBounds = tickRect;

        g.setColor ( /*DefaultLookup.getColor ( component, this, "Slider.tickColor",*/ Color.black/* ) */ );

        if ( component.getOrientation () == JSlider.HORIZONTAL )
        {
            g.translate ( 0, tickBounds.y );

            int value = component.getMinimum ();
            int xPos;

            if ( component.getMinorTickSpacing () > 0 )
            {
                while ( value <= component.getMaximum () )
                {
                    xPos = xPositionForValue ( value );
                    paintMinorTickForHorizontalSlider ( g, tickBounds, xPos );
                    value += component.getMinorTickSpacing ();
                }
            }

            if ( component.getMajorTickSpacing () > 0 )
            {
                value = component.getMinimum ();

                while ( value <= component.getMaximum () )
                {
                    xPos = xPositionForValue ( value );
                    paintMajorTickForHorizontalSlider ( g, tickBounds, xPos );
                    value += component.getMajorTickSpacing ();
                }
            }

            g.translate ( 0, -tickBounds.y );
        }
        else
        {
            g.translate ( tickBounds.x, 0 );

            int value = component.getMinimum ();
            int yPos;

            if ( component.getMinorTickSpacing () > 0 )
            {
                int offset = 0;
                if ( !ltr )
                {
                    offset = tickBounds.width - tickBounds.width / 2;
                    g.translate ( offset, 0 );
                }

                while ( value <= component.getMaximum () )
                {
                    yPos = yPositionForValue ( value );
                    paintMinorTickForVerticalSlider ( g, tickBounds, yPos );
                    value += component.getMinorTickSpacing ();
                }

                if ( !ltr )
                {
                    g.translate ( -offset, 0 );
                }
            }

            if ( component.getMajorTickSpacing () > 0 )
            {
                value = component.getMinimum ();
                if ( !ltr )
                {
                    g.translate ( 2, 0 );
                }

                while ( value <= component.getMaximum () )
                {
                    yPos = yPositionForValue ( value );
                    paintMajorTickForVerticalSlider ( g, tickBounds, yPos );
                    value += component.getMajorTickSpacing ();
                }

                if ( !ltr )
                {
                    g.translate ( -2, 0 );
                }
            }
            g.translate ( -tickBounds.x, 0 );
        }
    }

    protected void paintMinorTickForHorizontalSlider ( final Graphics g, final Rectangle tickBounds, final int x )
    {
        g.drawLine ( x, 0, x, tickBounds.height / 2 - 1 );
    }

    protected void paintMajorTickForHorizontalSlider ( final Graphics g, final Rectangle tickBounds, final int x )
    {
        g.drawLine ( x, 0, x, tickBounds.height - 2 );
    }

    protected void paintMinorTickForVerticalSlider ( final Graphics g, final Rectangle tickBounds, final int y )
    {
        g.drawLine ( 0, y, tickBounds.width / 2 - 1, y );
    }

    protected void paintMajorTickForVerticalSlider ( final Graphics g, final Rectangle tickBounds, final int y )
    {
        g.drawLine ( 0, y, tickBounds.width - 2, y );
    }

    public void paintLabels ( final Graphics g )
    {
        final Rectangle labelBounds = labelRect;

        final Dictionary dictionary = component.getLabelTable ();
        if ( dictionary != null )
        {
            final Enumeration keys = dictionary.keys ();
            final int minValue = component.getMinimum ();
            final int maxValue = component.getMaximum ();
            final boolean enabled = component.isEnabled ();
            while ( keys.hasMoreElements () )
            {
                final Integer key = ( Integer ) keys.nextElement ();
                final int value = key;
                if ( value >= minValue && value <= maxValue )
                {
                    final Component label = ( Component ) dictionary.get ( key );
                    if ( label instanceof JComponent )
                    {
                        label.setEnabled ( enabled );
                    }
                    if ( component.getOrientation () == JSlider.HORIZONTAL )
                    {
                        g.translate ( 0, labelBounds.y );
                        paintHorizontalLabel ( g, value, label );
                        g.translate ( 0, -labelBounds.y );
                    }
                    else
                    {
                        int offset = 0;
                        if ( !ltr )
                        {
                            offset = labelBounds.width - label.getPreferredSize ().width;
                        }
                        g.translate ( labelBounds.x + offset, 0 );
                        paintVerticalLabel ( g, value, label );
                        g.translate ( -labelBounds.x - offset, 0 );
                    }
                }
            }
        }
    }

    /**
     * Called for every label in the label table.  Used to draw the labels for horizontal sliders.
     * The graphics have been translated to labelRect.y already.
     *
     * @see JSlider#setLabelTable
     */
    protected void paintHorizontalLabel ( final Graphics g, final int value, final Component label )
    {
        final int labelCenter = xPositionForValue ( value );
        final int labelLeft = labelCenter - ( label.getPreferredSize ().width / 2 );
        g.translate ( labelLeft, 0 );
        label.paint ( g );
        g.translate ( -labelLeft, 0 );
    }

    /**
     * Called for every label in the label table.  Used to draw the labels for vertical sliders.
     * The graphics have been translated to labelRect.x already.
     *
     * @see JSlider#setLabelTable
     */
    protected void paintVerticalLabel ( final Graphics g, final int value, final Component label )
    {
        final int labelCenter = yPositionForValue ( value );
        final int labelTop = labelCenter - ( label.getPreferredSize ().height / 2 );
        g.translate ( 0, labelTop );
        label.paint ( g );
        g.translate ( 0, -labelTop );
    }

    protected float getProgress ()
    {
        return ( float ) rolloverDarkness / MAX_DARKNESS;
    }

    protected Color getBorderColor ()
    {
        return ColorUtils.getIntermediateColor ( StyleConstants.borderColor, StyleConstants.darkBorderColor, getProgress () );
    }

    @Override
    public void setDragging ( final boolean dragging )
    {
        this.dragging = dragging;
    }
}