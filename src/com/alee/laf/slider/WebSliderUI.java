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

import com.alee.laf.StyleConstants;
import com.alee.utils.ColorUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
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

/**
 * User: mgarin Date: 26.05.11 Time: 18:03
 */

public class WebSliderUI extends BasicSliderUI
{
    public static final int MAX_DARKNESS = 5;

    private Color trackBgTop = WebSliderStyle.trackBgTop;
    private Color trackBgBottom = WebSliderStyle.trackBgBottom;
    private int trackHeight = WebSliderStyle.trackHeight;
    private int trackRound = WebSliderStyle.trackRound;
    private int trackShadeWidth = WebSliderStyle.trackShadeWidth;

    private boolean drawProgress = WebSliderStyle.drawProgress;
    private int progressRound = WebSliderStyle.progressRound;
    private int progressShadeWidth = WebSliderStyle.progressShadeWidth;

    private boolean drawThumb = WebSliderStyle.drawThumb;
    private Color thumbBgTop = WebSliderStyle.thumbBgTop;
    private Color thumbBgBottom = WebSliderStyle.thumbBgBottom;
    private int thumbWidth = WebSliderStyle.thumbWidth;
    private int thumbHeight = WebSliderStyle.thumbHeight;
    private int thumbRound = WebSliderStyle.thumbRound;
    private int thumbShadeWidth = WebSliderStyle.thumbShadeWidth;
    private boolean angledThumb = WebSliderStyle.angledThumb;
    private boolean sharpThumbAngle = WebSliderStyle.sharpThumbAngle;
    private int thumbAngleLength = WebSliderStyle.thumbAngleLength;

    private boolean animated = WebSliderStyle.animated;

    private boolean rolloverDarkBorderOnly = WebSliderStyle.rolloverDarkBorderOnly;

    private MouseWheelListener mouseWheelListener;
    private ChangeListener changeListener;
    private MouseAdapter mouseAdapter;

    private boolean rollover = false;
    private int rolloverDarkness = 0;
    private WebTimer rolloverTimer;

    public WebSliderUI ( JSlider b )
    {
        super ( b );
    }

    public static ComponentUI createUI ( JComponent c )
    {
        return new WebSliderUI ( ( JSlider ) c );
    }

    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Default settings
        SwingUtils.setOrientation ( slider );
        slider.setOpaque ( false );

        // Rollover mouse wheel scroll
        mouseWheelListener = new MouseWheelListener ()
        {
            @Override
            public void mouseWheelMoved ( MouseWheelEvent e )
            {
                slider.setValue (
                        Math.min ( Math.max ( slider.getMinimum (), slider.getValue () + e.getWheelRotation () ), slider.getMaximum () ) );
            }
        };
        slider.addMouseWheelListener ( mouseWheelListener );

        // todo Requires optimizations
        // Fixed value change repaint
        changeListener = new ChangeListener ()
        {
            @Override
            public void stateChanged ( ChangeEvent e )
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
            public void actionPerformed ( ActionEvent e )
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
            public void mousePressed ( MouseEvent e )
            {
                slider.repaint ();
            }

            @Override
            public void mouseReleased ( MouseEvent e )
            {
                slider.repaint ();
            }

            @Override
            public void mouseDragged ( MouseEvent e )
            {
                slider.repaint ();
            }

            @Override
            public void mouseEntered ( MouseEvent e )
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
            public void mouseExited ( MouseEvent e )
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
    public void uninstallUI ( JComponent c )
    {
        slider.removeMouseWheelListener ( mouseWheelListener );
        slider.removeChangeListener ( changeListener );
        slider.removeMouseListener ( mouseAdapter );
        slider.removeMouseMotionListener ( mouseAdapter );

        super.uninstallUI ( c );
    }

    public boolean isAnimated ()
    {
        return animated;
    }

    public void setAnimated ( boolean animated )
    {
        this.animated = animated;
    }

    public boolean isRolloverDarkBorderOnly ()
    {
        return rolloverDarkBorderOnly;
    }

    public void setRolloverDarkBorderOnly ( boolean rolloverDarkBorderOnly )
    {
        this.rolloverDarkBorderOnly = rolloverDarkBorderOnly;
    }

    public Color getTrackBgTop ()
    {
        return trackBgTop;
    }

    public void setTrackBgTop ( Color trackBgTop )
    {
        this.trackBgTop = trackBgTop;
    }

    public Color getTrackBgBottom ()
    {
        return trackBgBottom;
    }

    public void setTrackBgBottom ( Color trackBgBottom )
    {
        this.trackBgBottom = trackBgBottom;
    }

    public int getTrackHeight ()
    {
        return trackHeight;
    }

    public void setTrackHeight ( int trackHeight )
    {
        this.trackHeight = trackHeight;
    }

    public int getTrackRound ()
    {
        return trackRound;
    }

    public void setTrackRound ( int trackRound )
    {
        this.trackRound = trackRound;
    }

    public int getTrackShadeWidth ()
    {
        return trackShadeWidth;
    }

    public void setTrackShadeWidth ( int trackShadeWidth )
    {
        this.trackShadeWidth = trackShadeWidth;
    }

    public boolean isDrawProgress ()
    {
        return drawProgress;
    }

    public void setDrawProgress ( boolean drawProgress )
    {
        this.drawProgress = drawProgress;
    }

    public int getProgressRound ()
    {
        return progressRound;
    }

    public void setProgressRound ( int progressRound )
    {
        this.progressRound = progressRound;
    }

    public int getProgressShadeWidth ()
    {
        return progressShadeWidth;
    }

    public void setProgressShadeWidth ( int progressShadeWidth )
    {
        this.progressShadeWidth = progressShadeWidth;
    }

    public boolean isDrawThumb ()
    {
        return drawThumb;
    }

    public void setDrawThumb ( boolean drawThumb )
    {
        this.drawThumb = drawThumb;
    }

    public Color getThumbBgTop ()
    {
        return thumbBgTop;
    }

    public void setThumbBgTop ( Color thumbBgTop )
    {
        this.thumbBgTop = thumbBgTop;
    }

    public Color getThumbBgBottom ()
    {
        return thumbBgBottom;
    }

    public void setThumbBgBottom ( Color thumbBgBottom )
    {
        this.thumbBgBottom = thumbBgBottom;
    }

    public int getThumbWidth ()
    {
        return thumbWidth;
    }

    public void setThumbWidth ( int thumbWidth )
    {
        this.thumbWidth = thumbWidth;
    }

    public int getThumbHeight ()
    {
        return thumbHeight;
    }

    public void setThumbHeight ( int thumbHeight )
    {
        this.thumbHeight = thumbHeight;
    }

    public int getThumbRound ()
    {
        return thumbRound;
    }

    public void setThumbRound ( int thumbRound )
    {
        this.thumbRound = thumbRound;
    }

    public int getThumbShadeWidth ()
    {
        return thumbShadeWidth;
    }

    public void setThumbShadeWidth ( int thumbShadeWidth )
    {
        this.thumbShadeWidth = thumbShadeWidth;
    }

    public boolean isAngledThumb ()
    {
        return angledThumb;
    }

    public void setAngledThumb ( boolean angledThumb )
    {
        this.angledThumb = angledThumb;
    }

    public boolean isSharpThumbAngle ()
    {
        return sharpThumbAngle;
    }

    public void setSharpThumbAngle ( boolean sharpThumbAngle )
    {
        this.sharpThumbAngle = sharpThumbAngle;
    }

    public int getThumbAngleLength ()
    {
        return thumbAngleLength;
    }

    public void setThumbAngleLength ( int thumbAngleLength )
    {
        this.thumbAngleLength = thumbAngleLength;
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
    public void paintFocus ( Graphics g )
    {
        // Do not paint default focus
    }

    @Override
    public void paintThumb ( Graphics g )
    {
        if ( drawThumb )
        {
            Graphics2D g2d = ( Graphics2D ) g;
            Object aa = LafUtils.setupAntialias ( g2d );

            // Thumb shape
            Shape ts = getThumbShape ();

            // Thumb shade
            if ( slider.isEnabled () )
            {
                LafUtils.drawShade ( g2d, ts, StyleConstants.shadeColor, thumbShadeWidth );
            }

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

            LafUtils.restoreAntialias ( g2d, aa );
        }
    }


    private Color getBorderColor ()
    {
        return ColorUtils.getIntermediateColor ( StyleConstants.borderColor, StyleConstants.darkBorderColor, getProgress () );
    }

    private float getProgress ()
    {
        return ( float ) rolloverDarkness / MAX_DARKNESS;
    }

    private Shape getThumbShape ()
    {
        if ( angledThumb && ( slider.getPaintLabels () || slider.getPaintTicks () ) )
        {
            if ( slider.getOrientation () == JSlider.HORIZONTAL )
            {
                GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
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
                GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
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
    public void paintTrack ( Graphics g )
    {
        Graphics2D g2d = ( Graphics2D ) g;
        Object aa = LafUtils.setupAntialias ( g2d );

        // Track shape
        Shape ss = getTrackShape ();

        // Track background & shade
        {
            // Track shade
            if ( slider.isEnabled () )
            {
                LafUtils.drawShade ( g2d, ss, slider.isFocusOwner () ? StyleConstants.fieldFocusColor : StyleConstants.shadeColor,
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
            Shape ps = getProgressShape ();

            // Progress shade
            if ( slider.isEnabled () )
            {
                LafUtils.drawShade ( g2d, ps, StyleConstants.shadeColor, progressShadeWidth );
            }

            // Progress background
            Rectangle bounds = ss.getBounds ();
            if ( slider.getOrientation () == JSlider.HORIZONTAL )
            {
                g2d.setPaint (
                        new GradientPaint ( 0, bounds.y + progressShadeWidth, Color.WHITE, 0, bounds.y + bounds.height - progressShadeWidth,
                                new Color ( 223, 223, 223 ) ) );
            }
            else
            {
                g2d.setPaint (
                        new GradientPaint ( bounds.x + progressShadeWidth, 0, Color.WHITE, bounds.x + bounds.width - progressShadeWidth, 0,
                                new Color ( 223, 223, 223 ) ) );
            }
            g2d.fill ( ps );

            // Progress border
            g2d.setPaint ( slider.isEnabled () ? StyleConstants.darkBorderColor : StyleConstants.disabledBorderColor );
            g2d.draw ( ps );
        }

        // Track border & focus
        {
            // Track border
            g2d.setPaint ( slider.isEnabled () ?
                    ( rolloverDarkBorderOnly && !isDragging () ? getBorderColor () : StyleConstants.darkBorderColor ) :
                    StyleConstants.disabledBorderColor );
            g2d.draw ( ss );
        }

        LafUtils.restoreAntialias ( g2d, aa );
    }

    private Shape getTrackShape ()
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

    private Shape getProgressShape ()
    {
        if ( trackRound > 0 )
        {
            if ( slider.getOrientation () == JSlider.HORIZONTAL )
            {
                int x;
                int w;
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
                int y = thumbRect.y + thumbRect.height / 2;
                int h = trackRect.y + trackRect.height + trackRound - progressShadeWidth - y - 1;
                return new RoundRectangle2D.Double ( trackRect.x + progressShadeWidth + trackRect.width / 2 - trackHeight / 2, y,
                        trackHeight - progressShadeWidth * 2, h, progressRound * 2, progressRound * 2 );
            }
        }
        else
        {
            if ( slider.getOrientation () == JSlider.HORIZONTAL )
            {
                int x;
                int w;
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
                int y = trackRect.y + progressShadeWidth;
                int h = thumbRect.y + thumbRect.height / 2 - y;
                return new Rectangle2D.Double ( trackRect.x + progressShadeWidth + trackRect.width / 2 - trackHeight / 2, y,
                        trackHeight - progressShadeWidth * 2, h );
            }
        }
    }
}
