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

package com.alee.laf.slider;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.PainterSupport;
import com.alee.global.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.label.WebLabelStyle;
import com.alee.utils.ColorUtils;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.BorderMethods;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author Mikle Garin
 * @author Michka Popoff
 */

public class WebSliderUI extends BasicSliderUI implements BorderMethods
{
    public static final int MAX_DARKNESS = 5;

    protected Color trackBgTop = WebSliderStyle.trackBgTop;
    protected Color trackBgBottom = WebSliderStyle.trackBgBottom;
    protected int trackHeight = WebSliderStyle.trackHeight;
    protected int trackRound = WebSliderStyle.trackRound;
    protected int trackShadeWidth = WebSliderStyle.trackShadeWidth;

    protected boolean drawProgress = WebSliderStyle.drawProgress;
    protected Color progressTrackBgTop = WebSliderStyle.progressTrackBgTop;
    protected Color progressTrackBgBottom = WebSliderStyle.progressTrackBgBottom;
    protected Color progressBorderColor = WebSliderStyle.progressBorderColor;
    protected int progressRound = WebSliderStyle.progressRound;
    protected int progressShadeWidth = WebSliderStyle.progressShadeWidth;

    protected boolean drawThumb = WebSliderStyle.drawThumb;
    protected Color thumbBgTop = WebSliderStyle.thumbBgTop;
    protected Color thumbBgBottom = WebSliderStyle.thumbBgBottom;
    protected int thumbWidth = WebSliderStyle.thumbWidth;
    protected int thumbHeight = WebSliderStyle.thumbHeight;
    protected int thumbRound = WebSliderStyle.thumbRound;
    protected int thumbShadeWidth = WebSliderStyle.thumbShadeWidth;
    protected boolean angledThumb = WebSliderStyle.angledThumb;
    protected boolean sharpThumbAngle = WebSliderStyle.sharpThumbAngle;
    protected int thumbAngleLength = WebSliderStyle.thumbAngleLength;

    protected boolean animated = WebSliderStyle.animated;

    protected boolean rolloverDarkBorderOnly = WebSliderStyle.rolloverDarkBorderOnly;

    protected Insets margin = WebLabelStyle.margin;
    protected Painter painter = WebLabelStyle.painter;

    protected boolean invertMouseWheelDirection = WebSliderStyle.invertMouseWheelDirection;

    /**
     * Slider listeners.
     */
    protected PropertyChangeListener propertyChangeListener;
    protected MouseWheelListener mouseWheelListener;
    protected ChangeListener changeListener;
    protected MouseAdapter mouseAdapter;

    /**
     * Runtime variables.
     */
    protected boolean rollover = false;
    protected int rolloverDarkness = 0;
    protected WebTimer rolloverTimer;

    public WebSliderUI ( final JSlider b )
    {
        super ( b );
    }

    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebSliderUI ( ( JSlider ) c );
    }

    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Default settings
        SwingUtils.setOrientation ( slider );
        LookAndFeel.installProperty ( slider, WebLookAndFeel.OPAQUE_PROPERTY, Boolean.FALSE );
        slider.setForeground ( StyleConstants.textColor );
        PainterSupport.installPainter ( slider, this.painter );
        updateBorder ();

        // Orientation change listener
        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updateBorder ();
            }
        };
        slider.addPropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, propertyChangeListener );

        // Rollover mouse wheel scroll
        mouseWheelListener = new MouseWheelListener ()
        {
            @Override
            public void mouseWheelMoved ( final MouseWheelEvent e )
            {
                final int v = slider.getValue () - e.getWheelRotation ();
                slider.setValue ( Math.min ( Math.max ( slider.getMinimum (), v ), slider.getMaximum () ) );
            }
        };
        slider.addMouseWheelListener ( mouseWheelListener );

        // todo Requires optimizations
        // Fixed value change repaint
        changeListener = new ChangeListener ()
        {
            @Override
            public void stateChanged ( final ChangeEvent e )
            {
                slider.repaint ();
            }
        };
        slider.addChangeListener ( changeListener );

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
                    slider.repaint ();
                }
                else if ( !rollover && rolloverDarkness > 0 )
                {
                    rolloverDarkness--;
                    slider.repaint ();
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
                slider.repaint ();
            }

            @Override
            public void mouseReleased ( final MouseEvent e )
            {
                slider.repaint ();
            }

            @Override
            public void mouseDragged ( final MouseEvent e )
            {
                slider.repaint ();
            }

            @Override
            public void mouseEntered ( final MouseEvent e )
            {
                rollover = true;
                if ( isAnimated () && c.isEnabled () )
                {
                    rolloverTimer.start ();
                }
                else
                {
                    rolloverDarkness = MAX_DARKNESS;
                    slider.repaint ();
                }
            }

            @Override
            public void mouseExited ( final MouseEvent e )
            {
                rollover = false;
                if ( isAnimated () && c.isEnabled () )
                {
                    rolloverTimer.start ();
                }
                else
                {
                    rolloverDarkness = 0;
                    slider.repaint ();
                }
            }
        };
        slider.addMouseListener ( mouseAdapter );
        slider.addMouseMotionListener ( mouseAdapter );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        PainterSupport.uninstallPainter ( slider, this.painter );

        slider.removePropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, propertyChangeListener );
        slider.removeMouseWheelListener ( mouseWheelListener );
        slider.removeChangeListener ( changeListener );
        slider.removeMouseListener ( mouseAdapter );
        slider.removeMouseMotionListener ( mouseAdapter );

        super.uninstallUI ( c );
    }

    @Override
    public void updateBorder ()
    {
        if ( slider != null )
        {
            // Preserve old borders
            if ( SwingUtils.isPreserveBorders ( slider ) )
            {
                return;
            }

            // Actual margin
            final boolean ltr = slider.getComponentOrientation ().isLeftToRight ();
            final Insets m = new Insets ( margin.top, ltr ? margin.left : margin.right, margin.bottom, ltr ? margin.right : margin.left );

            // Calculating additional borders
            if ( painter != null )
            {
                // Painter borders
                final Insets pi = painter.getMargin ( slider );
                m.top += pi.top;
                m.bottom += pi.bottom;
                m.left += ltr ? pi.left : pi.right;
                m.right += ltr ? pi.right : pi.left;
            }

            // Installing border
            slider.setBorder ( LafUtils.createWebBorder ( m ) );
        }
    }

    public boolean isAnimated ()
    {
        return animated;
    }

    public void setAnimated ( final boolean animated )
    {
        this.animated = animated;
    }

    public boolean isRolloverDarkBorderOnly ()
    {
        return rolloverDarkBorderOnly;
    }

    public void setRolloverDarkBorderOnly ( final boolean rolloverDarkBorderOnly )
    {
        this.rolloverDarkBorderOnly = rolloverDarkBorderOnly;
    }

    public boolean isInvertMouseWheelDirection ()
    {
        return invertMouseWheelDirection;
    }

    public void setInvertMouseWheelDirection ( final boolean invert )
    {
        this.invertMouseWheelDirection = invert;
    }

    public Color getTrackBgTop ()
    {
        return trackBgTop;
    }

    public void setTrackBgTop ( final Color trackBgTop )
    {
        this.trackBgTop = trackBgTop;
    }

    public Color getTrackBgBottom ()
    {
        return trackBgBottom;
    }

    public void setTrackBgBottom ( final Color trackBgBottom )
    {
        this.trackBgBottom = trackBgBottom;
    }

    public int getTrackHeight ()
    {
        return trackHeight;
    }

    public void setTrackHeight ( final int trackHeight )
    {
        this.trackHeight = trackHeight;
    }

    public int getTrackRound ()
    {
        return trackRound;
    }

    public void setTrackRound ( final int trackRound )
    {
        this.trackRound = trackRound;
    }

    public int getTrackShadeWidth ()
    {
        return trackShadeWidth;
    }

    public void setTrackShadeWidth ( final int trackShadeWidth )
    {
        this.trackShadeWidth = trackShadeWidth;
    }

    public boolean isDrawProgress ()
    {
        return drawProgress;
    }

    public void setDrawProgress ( final boolean drawProgress )
    {
        this.drawProgress = drawProgress;
    }

    public Color getProgressTrackBgTop ()
    {
        return progressTrackBgTop;
    }

    public void setProgressTrackBgTop ( final Color progressTrackBgTop )
    {
        this.progressTrackBgTop = progressTrackBgTop;
    }

    public Color getProgressTrackBgBottom ()
    {
        return progressTrackBgBottom;
    }

    public void setProgressTrackBgBottom ( final Color progressTrackBgBottom )
    {
        this.progressTrackBgBottom = progressTrackBgBottom;
    }

    public Color getProgressBorderColor ()
    {
        return progressBorderColor;
    }

    public void setProgressBorderColor ( final Color progressBorderColor )
    {
        this.progressBorderColor = progressBorderColor;
    }

    public int getProgressRound ()
    {
        return progressRound;
    }

    public void setProgressRound ( final int progressRound )
    {
        this.progressRound = progressRound;
    }

    public int getProgressShadeWidth ()
    {
        return progressShadeWidth;
    }

    public void setProgressShadeWidth ( final int progressShadeWidth )
    {
        this.progressShadeWidth = progressShadeWidth;
    }

    public boolean isDrawThumb ()
    {
        return drawThumb;
    }

    public void setDrawThumb ( final boolean drawThumb )
    {
        this.drawThumb = drawThumb;
    }

    public Color getThumbBgTop ()
    {
        return thumbBgTop;
    }

    public void setThumbBgTop ( final Color thumbBgTop )
    {
        this.thumbBgTop = thumbBgTop;
    }

    public Color getThumbBgBottom ()
    {
        return thumbBgBottom;
    }

    public void setThumbBgBottom ( final Color thumbBgBottom )
    {
        this.thumbBgBottom = thumbBgBottom;
    }

    public int getThumbWidth ()
    {
        return thumbWidth;
    }

    public void setThumbWidth ( final int thumbWidth )
    {
        this.thumbWidth = thumbWidth;
    }

    public int getThumbHeight ()
    {
        return thumbHeight;
    }

    public void setThumbHeight ( final int thumbHeight )
    {
        this.thumbHeight = thumbHeight;
    }

    public int getThumbRound ()
    {
        return thumbRound;
    }

    public void setThumbRound ( final int thumbRound )
    {
        this.thumbRound = thumbRound;
    }

    public int getThumbShadeWidth ()
    {
        return thumbShadeWidth;
    }

    public void setThumbShadeWidth ( final int thumbShadeWidth )
    {
        this.thumbShadeWidth = thumbShadeWidth;
    }

    public boolean isAngledThumb ()
    {
        return angledThumb;
    }

    public void setAngledThumb ( final boolean angledThumb )
    {
        this.angledThumb = angledThumb;
    }

    public boolean isSharpThumbAngle ()
    {
        return sharpThumbAngle;
    }

    public void setSharpThumbAngle ( final boolean sharpThumbAngle )
    {
        this.sharpThumbAngle = sharpThumbAngle;
    }

    public int getThumbAngleLength ()
    {
        return thumbAngleLength;
    }

    public void setThumbAngleLength ( final int thumbAngleLength )
    {
        this.thumbAngleLength = thumbAngleLength;
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

    public Painter getPainter ()
    {
        return painter;
    }

    public void setPainter ( final Painter painter )
    {
        PainterSupport.uninstallPainter ( slider, this.painter );
        this.painter = painter;
        PainterSupport.installPainter ( slider, this.painter );
        updateBorder ();
    }

    @Override
    protected Dimension getThumbSize ()
    {
        if ( slider.getOrientation () == JSlider.HORIZONTAL )
        {
            return new Dimension ( thumbWidth, thumbHeight );
        }
        else
        {
            return new Dimension ( thumbHeight, thumbWidth );
        }
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        // Force painter to draw background
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, SwingUtils.size ( c ), c );
        }

        super.paint ( g, c );
    }

    @Override
    public void paintFocus ( final Graphics g )
    {
        // Do not paint default focus
    }

    @Override
    public void paintThumb ( final Graphics g )
    {
        if ( drawThumb )
        {
            final Graphics2D g2d = ( Graphics2D ) g;
            final Object aa = GraphicsUtils.setupAntialias ( g2d );

            // Thumb shape
            final Shape ts = getThumbShape ();

            //            // Thumb shade
            //            if ( slider.isEnabled () )
            //            {
            //                LafUtils.drawShade ( g2d, ts, StyleConstants.shadeColor, thumbShadeWidth );
            //            }

            // Thumb background
            if ( slider.getOrientation () == JSlider.HORIZONTAL )
            {
                g2d.setPaint ( new GradientPaint ( 0, thumbRect.y, thumbBgTop, 0, thumbRect.y + thumbRect.height, thumbBgBottom ) );
            }
            else
            {
                g2d.setPaint ( new GradientPaint ( thumbRect.x, 0, thumbBgTop, thumbRect.x + thumbRect.width, 0, thumbBgBottom ) );
            }
            g2d.fill ( ts );

            // Thumb border
            g2d.setPaint ( slider.isEnabled () ? StyleConstants.darkBorderColor : StyleConstants.disabledBorderColor );
            g2d.draw ( ts );

            // Thumb focus
            //        LafUtils.drawCustomWebFocus ( g2d, slider, FocusType.fieldFocus, ts );

            GraphicsUtils.restoreAntialias ( g2d, aa );
        }
    }


    protected Color getBorderColor ()
    {
        return ColorUtils.getIntermediateColor ( StyleConstants.borderColor, StyleConstants.darkBorderColor, getProgress () );
    }

    protected float getProgress ()
    {
        return ( float ) rolloverDarkness / MAX_DARKNESS;
    }

    protected Shape getThumbShape ()
    {
        if ( angledThumb && ( slider.getPaintLabels () || slider.getPaintTicks () ) )
        {
            if ( slider.getOrientation () == JSlider.HORIZONTAL )
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
                if ( slider.getComponentOrientation ().isLeftToRight () )
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

    @Override
    public void paintTrack ( final Graphics g )
    {
        final Graphics2D g2d = ( Graphics2D ) g;
        final Object aa = GraphicsUtils.setupAntialias ( g2d );

        // Track shape
        final Shape ss = getTrackShape ();

        // Track background & shade
        {
            // Track shade
            if ( slider.isEnabled () )
            {
                GraphicsUtils.drawShade ( g2d, ss, slider.isFocusOwner () ? StyleConstants.fieldFocusColor : StyleConstants.shadeColor,
                        trackShadeWidth );
            }

            // Track background
            if ( slider.getOrientation () == JSlider.HORIZONTAL )
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
            if ( slider.isEnabled () )
            {
                GraphicsUtils.drawShade ( g2d, ps, StyleConstants.shadeColor, progressShadeWidth );
            }

            // Progress background
            final Rectangle bounds = ss.getBounds ();
            if ( slider.getOrientation () == JSlider.HORIZONTAL )
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
            g2d.setPaint ( slider.isEnabled () ? progressBorderColor : StyleConstants.disabledBorderColor );
            g2d.draw ( ps );
        }

        // Track border & focus
        {
            // Track border
            g2d.setPaint (
                    slider.isEnabled () ? rolloverDarkBorderOnly && !isDragging () ? getBorderColor () : StyleConstants.darkBorderColor :
                            StyleConstants.disabledBorderColor );
            g2d.draw ( ss );
        }

        GraphicsUtils.restoreAntialias ( g2d, aa );
    }

    protected Shape getTrackShape ()
    {
        if ( trackRound > 0 )
        {
            if ( slider.getOrientation () == JSlider.HORIZONTAL )
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
            if ( slider.getOrientation () == JSlider.HORIZONTAL )
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
            if ( slider.getOrientation () == JSlider.HORIZONTAL )
            {
                final int x;
                final int w;
                if ( slider.getComponentOrientation ().isLeftToRight () )
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
            if ( slider.getOrientation () == JSlider.HORIZONTAL )
            {
                final int x;
                final int w;
                if ( slider.getComponentOrientation ().isLeftToRight () )
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
}
