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

package com.alee.laf.progressbar;

import com.alee.laf.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.ThreadUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.swing.AncestorAdapter;
import com.alee.utils.swing.BorderMethods;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * User: mgarin Date: 28.04.11 Time: 15:05
 */

public class WebProgressBarUI extends BasicProgressBarUI implements ShapeProvider, BorderMethods
{
    private static final int indeterminateStep = 20;
    private static final int determinateAnimationWidth = 120;

    private static final AffineTransform moveX = new AffineTransform ();

    static
    {
        moveX.translate ( indeterminateStep * 2, 0 );
    }

    private Color bgTop = WebProgressBarStyle.bgTop;
    private Color bgBottom = WebProgressBarStyle.bgBottom;

    private Color progressTopColor = WebProgressBarStyle.progressTopColor;
    private Color progressBottomColor = WebProgressBarStyle.progressBottomColor;

    private Color indeterminateBorder = WebProgressBarStyle.indeterminateBorder;

    private Color highlightWhite = WebProgressBarStyle.highlightWhite;
    private Color highlightDarkWhite = WebProgressBarStyle.highlightDarkWhite;

    private int round = WebProgressBarStyle.round;
    private int innerRound = WebProgressBarStyle.innerRound;
    private int shadeWidth = WebProgressBarStyle.shadeWidth;

    private boolean paintIndeterminateBorder = WebProgressBarStyle.paintIndeterminateBorder;

    private int preferredProgressWidth = WebProgressBarStyle.preferredProgressWidth;

    private final int determinateAnimationPause = 1500;
    private int animationLocation = 0;
    private WebTimer animator = null;

    private PropertyChangeListener propertyChangeListener;

    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebProgressBarUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Default settings
        SwingUtils.setOrientation ( progressBar );
        LookAndFeel.installProperty ( progressBar, WebLookAndFeel.OPAQUE_PROPERTY, Boolean.FALSE );
        progressBar.setBorderPainted ( false );
        progressBar.setForeground ( Color.DARK_GRAY );

        // Updating border
        updateBorder ();

        // Change listeners
        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updateAnimator ( progressBar );
            }
        };
        progressBar.addPropertyChangeListener ( "indeterminate", propertyChangeListener );
        progressBar.addPropertyChangeListener ( "enabled", propertyChangeListener );

        updateAnimator ( progressBar );
        c.addAncestorListener ( new AncestorAdapter ()
        {
            @Override
            public void ancestorAdded ( final AncestorEvent event )
            {
                updateAnimator ( progressBar );
            }

            @Override
            public void ancestorRemoved ( final AncestorEvent event )
            {
                updateAnimator ( progressBar );
            }
        } );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        c.removePropertyChangeListener ( propertyChangeListener );

        if ( animator != null )
        {
            animator.stop ();
        }

        super.uninstallUI ( c );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBorder ()
    {
        if ( progressBar != null )
        {
            // Preserve old borders
            if ( SwingUtils.isPreserveBorders ( progressBar ) )
            {
                return;
            }

            progressBar.setBorder ( LafUtils.createWebBorder ( shadeWidth + 1, shadeWidth + 1, shadeWidth + 1, shadeWidth + 1 ) );
        }
    }

    public int getRound ()
    {
        return round;
    }

    public void setRound ( final int round )
    {
        this.round = round;
    }

    public int getInnerRound ()
    {
        return innerRound;
    }

    public void setInnerRound ( final int innerRound )
    {
        this.innerRound = innerRound;
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

    public boolean isPaintIndeterminateBorder ()
    {
        return paintIndeterminateBorder;
    }

    public void setPaintIndeterminateBorder ( final boolean paintIndeterminateBorder )
    {
        this.paintIndeterminateBorder = paintIndeterminateBorder;
    }

    public int getPreferredProgressWidth ()
    {
        return preferredProgressWidth;
    }

    public void setPreferredProgressWidth ( final int preferredProgressWidth )
    {
        this.preferredProgressWidth = preferredProgressWidth;
    }

    public Color getBgTop ()
    {
        return bgTop;
    }

    public void setBgTop ( final Color bgTop )
    {
        this.bgTop = bgTop;
    }

    public Color getBgBottom ()
    {
        return bgBottom;
    }

    public void setBgBottom ( final Color bgBottom )
    {
        this.bgBottom = bgBottom;
    }

    public Color getProgressTopColor ()
    {
        return progressTopColor;
    }

    public void setProgressTopColor ( final Color progressTopColor )
    {
        this.progressTopColor = progressTopColor;
    }

    public Color getProgressBottomColor ()
    {
        return progressBottomColor;
    }

    public void setProgressBottomColor ( final Color progressBottomColor )
    {
        this.progressBottomColor = progressBottomColor;
    }

    public Color getIndeterminateBorder ()
    {
        return indeterminateBorder;
    }

    public void setIndeterminateBorder ( final Color indeterminateBorder )
    {
        this.indeterminateBorder = indeterminateBorder;
    }

    public Color getHighlightWhite ()
    {
        return highlightWhite;
    }

    public void setHighlightWhite ( final Color highlightWhite )
    {
        this.highlightWhite = highlightWhite;
    }

    public Color getHighlightDarkWhite ()
    {
        return highlightDarkWhite;
    }

    public void setHighlightDarkWhite ( final Color highlightDarkWhite )
    {
        this.highlightDarkWhite = highlightDarkWhite;
    }

    @Override
    public Shape provideShape ()
    {
        return LafUtils.getWebBorderShape ( progressBar, getShadeWidth (), getRound () );
    }

    private void updateAnimator ( final JProgressBar progressBar )
    {
        if ( animator != null )
        {
            animator.stop ();
        }
        if ( SwingUtils.getWindowAncestor ( progressBar ) != null && progressBar.isShowing () )
        {
            if ( progressBar.isEnabled () )
            {
                if ( progressBar.isIndeterminate () )
                {
                    animationLocation = 0;
                    animator = new WebTimer ( "WebProgressBarUI.animator", StyleConstants.animationDelay, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            if ( animationLocation < indeterminateStep * 2 - 1 )
                            {
                                animationLocation++;
                            }
                            else
                            {
                                animationLocation = 0;
                            }
                            SwingUtilities.invokeLater ( new Runnable ()
                            {
                                @Override
                                public void run ()
                                {
                                    progressBar.repaint ();
                                }
                            } );
                        }
                    } );
                    animator.setUseEventDispatchThread ( false );
                }
                else
                {
                    animationLocation = -determinateAnimationWidth;
                    animator = new WebTimer ( "WebProgressBarUI.animator", StyleConstants.animationDelay, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            if ( animationLocation < getProgressWidth () )
                            {
                                animationLocation += 15;
                                refresh ( progressBar );
                            }
                            else
                            {
                                animationLocation = -determinateAnimationWidth;
                                refresh ( progressBar );
                                ThreadUtils.sleepSafely ( determinateAnimationPause );
                            }
                        }

                        private void refresh ( final JProgressBar progressBar )
                        {
                            if ( !progressBar.isIndeterminate () && progressBar.getValue () > progressBar.getMinimum () )
                            {
                                SwingUtilities.invokeLater ( new Runnable ()
                                {
                                    @Override
                                    public void run ()
                                    {
                                        progressBar.repaint ();
                                    }
                                } );
                            }
                        }
                    } );
                }
                animator.setUseEventDispatchThread ( false );
                animator.start ();
            }
        }
    }

    @Override
    protected Dimension getPreferredInnerHorizontal ()
    {
        final Dimension ph = super.getPreferredInnerHorizontal ();
        ph.width = preferredProgressWidth;
        return ph;
    }

    @Override
    protected Dimension getPreferredInnerVertical ()
    {
        final Dimension pv = super.getPreferredInnerVertical ();
        pv.height = preferredProgressWidth;
        return pv;
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        final Object aa = LafUtils.setupAntialias ( g );
        super.paint ( g, c );
        LafUtils.restoreAntialias ( g, aa );
    }

    @Override
    protected void paintIndeterminate ( final Graphics g, final JComponent c )
    {
        final Graphics2D g2d = ( Graphics2D ) g;

        // Outer border
        paintProgressBarBorder ( c, g2d );

        // Indeterminate view

        final Shape is = getInnerProgressShape ( c );

        final Shape oldClip = g2d.getClip ();
        final Area newClip = new Area ( is );
        newClip.intersect ( new Area ( oldClip ) );
        g2d.setClip ( newClip );

        final GeneralPath bs = getIndeterminateProgressShape ( c );
        final AffineTransform at = new AffineTransform ();
        at.translate ( animationLocation, 0 );
        bs.transform ( at );

        int x = 0;
        while ( x < c.getWidth () - shadeWidth * 2 - 4 - 1 + indeterminateStep * 2 )
        {
            g2d.setPaint ( new GradientPaint ( 0, shadeWidth, progressTopColor, 0, c.getHeight () - shadeWidth, progressBottomColor ) );
            g2d.fill ( bs );

            if ( paintIndeterminateBorder )
            {
                g2d.setPaint ( indeterminateBorder );
                g2d.draw ( bs );
            }

            x += indeterminateStep * 2;
            bs.transform ( moveX );
        }

        if ( progressBar.isStringPainted () && progressBar.getString () != null &&
                progressBar.getString ().trim ().length () > 0 )
        {
            final int tw = g2d.getFontMetrics ().stringWidth ( progressBar.getString () );
            final float percentage = ( float ) tw / ( progressBar.getWidth () * 2 );
            final float start = 0.5f - percentage;
            final float end = 0.5f + percentage;
            g2d.setPaint ( new LinearGradientPaint ( 0, 0, progressBar.getWidth (), 0,
                    new float[]{ start / 2, start, end, end + ( 1f - end ) / 2 },
                    new Color[]{ StyleConstants.transparent, highlightWhite, highlightWhite, StyleConstants.transparent } ) );
            g2d.fill ( is );
        }

        g2d.setClip ( oldClip );

        // Inner border
        if ( c.isEnabled () )
        {
            LafUtils.drawShade ( g2d, is, StyleConstants.shadeColor, shadeWidth );
        }
        g2d.setPaint ( c.isEnabled () ? Color.GRAY : Color.LIGHT_GRAY );
        g2d.draw ( is );

        // Text
        drawProgressBarText ( g2d );
    }

    @Override
    protected void paintDeterminate ( final Graphics g, final JComponent c )
    {
        final Graphics2D g2d = ( Graphics2D ) g;

        // Outer border
        paintProgressBarBorder ( c, g2d );

        // Progress bar
        if ( progressBar.getValue () > progressBar.getMinimum () )
        {
            final Shape is = getInnerProgressShape ( c );

            if ( c.isEnabled () )
            {
                LafUtils.drawShade ( g2d, is, StyleConstants.shadeColor, shadeWidth );
            }

            if ( progressBar.getOrientation () == JProgressBar.HORIZONTAL )
            {
                g2d.setPaint ( new GradientPaint ( 0, shadeWidth, progressTopColor, 0, c.getHeight () - shadeWidth, progressBottomColor ) );
            }
            else
            {
                if ( progressBar.getComponentOrientation ().isLeftToRight () )
                {
                    g2d.setPaint (
                            new GradientPaint ( shadeWidth, 0, progressTopColor, c.getWidth () - shadeWidth, 0, progressBottomColor ) );
                }
                else
                {
                    g2d.setPaint (
                            new GradientPaint ( shadeWidth, 0, progressBottomColor, c.getWidth () - shadeWidth, 0, progressTopColor ) );
                }
            }
            g2d.fill ( is );

            // Running highlight
            if ( c.isEnabled () )
            {
                final Shape oldClip = g2d.getClip ();
                final Area newClip = new Area ( is );
                newClip.intersect ( new Area ( oldClip ) );
                g2d.setClip ( newClip );
                if ( progressBar.getOrientation () == JProgressBar.HORIZONTAL )
                {
                    g2d.setPaint ( new RadialGradientPaint ( shadeWidth * 2 + animationLocation + determinateAnimationWidth / 2,
                            progressBar.getHeight () / 2, determinateAnimationWidth / 2, new float[]{ 0f, 1f },
                            new Color[]{ highlightDarkWhite, StyleConstants.transparent } ) );
                }
                else
                {
                    if ( progressBar.getComponentOrientation ().isLeftToRight () )
                    {
                        g2d.setPaint ( new RadialGradientPaint ( c.getWidth () / 2, c.getHeight () - shadeWidth * 2 - animationLocation -
                                determinateAnimationWidth / 2, determinateAnimationWidth / 2, new float[]{ 0f, 1f },
                                new Color[]{ highlightDarkWhite, StyleConstants.transparent } ) );
                    }
                    else
                    {
                        g2d.setPaint ( new RadialGradientPaint ( c.getWidth () / 2,
                                shadeWidth * 2 + animationLocation + determinateAnimationWidth / 2, determinateAnimationWidth / 2,
                                new float[]{ 0f, 1f }, new Color[]{ highlightDarkWhite, StyleConstants.transparent } ) );
                    }
                }
                g2d.fill ( is );
                g2d.setClip ( oldClip );
            }

            // Border
            g2d.setPaint ( c.isEnabled () ? Color.GRAY : Color.LIGHT_GRAY );
            g2d.draw ( is );
        }

        // Text
        drawProgressBarText ( g2d );
    }

    private void drawProgressBarText ( final Graphics2D g2d )
    {
        if ( progressBar.isStringPainted () )
        {
            if ( progressBar.getOrientation () == JProgressBar.VERTICAL )
            {
                g2d.translate ( progressBar.getWidth () / 2, progressBar.getHeight () / 2 );
                g2d.rotate ( ( progressBar.getComponentOrientation ().isLeftToRight () ? -1 : 1 ) * Math.PI / 2 );
                g2d.translate ( -progressBar.getWidth () / 2, -progressBar.getHeight () / 2 );
            }

            final String string = progressBar.getString ();
            final Point ts = LafUtils.getTextCenterShear ( g2d.getFontMetrics (), string );
            if ( !progressBar.isEnabled () )
            {
                g2d.setPaint ( Color.WHITE );
                g2d.drawString ( string, progressBar.getWidth () / 2 + ts.x + 1, progressBar.getHeight () / 2 + ts.y + 1 );
            }
            g2d.setPaint ( progressBar.isEnabled () ? progressBar.getForeground () : StyleConstants.disabledTextColor );
            g2d.drawString ( string, progressBar.getWidth () / 2 + ts.x, progressBar.getHeight () / 2 + ts.y );

            if ( progressBar.getOrientation () == JProgressBar.VERTICAL )
            {
                g2d.rotate ( ( progressBar.getComponentOrientation ().isLeftToRight () ? 1 : -1 ) * Math.PI / 2 );
            }
        }
    }

    private void paintProgressBarBorder ( final JComponent c, final Graphics2D g2d )
    {
        final Shape bs = getProgressShape ( c );

        if ( c.isEnabled () )
        {
            LafUtils.drawShade ( g2d, bs, StyleConstants.shadeColor, shadeWidth );
        }

        if ( progressBar.getOrientation () == JProgressBar.HORIZONTAL )
        {
            g2d.setPaint ( new GradientPaint ( 0, shadeWidth, bgTop, 0, c.getHeight () - shadeWidth, bgBottom ) );
        }
        else
        {
            g2d.setPaint ( new GradientPaint ( shadeWidth, 0, bgTop, c.getWidth () - shadeWidth, 0, bgBottom ) );
        }
        g2d.fill ( bs );

        g2d.setPaint ( c.isEnabled () ? Color.GRAY : Color.LIGHT_GRAY );
        g2d.draw ( bs );
    }

    private Shape getProgressShape ( final JComponent c )
    {
        if ( round > 0 )
        {
            return new RoundRectangle2D.Double ( shadeWidth, shadeWidth, c.getWidth () - shadeWidth * 2 - 1,
                    c.getHeight () - shadeWidth * 2 - 1, round * 2, round * 2 );
        }
        else
        {
            return new Rectangle2D.Double ( shadeWidth, shadeWidth, c.getWidth () - shadeWidth * 2 - 1,
                    c.getHeight () - shadeWidth * 2 - 1 );
        }
    }

    private Shape getInnerProgressShape ( final JComponent c )
    {
        final int progress = getProgressWidth ();
        if ( progressBar.getOrientation () == JProgressBar.HORIZONTAL )
        {
            if ( innerRound > 0 )
            {
                return new RoundRectangle2D.Double ( shadeWidth * 2, shadeWidth * 2, progress - 1, c.getHeight () - shadeWidth * 4 - 1,
                        innerRound * 2, innerRound * 2 );
            }
            else
            {
                return new Rectangle2D.Double ( shadeWidth * 2, shadeWidth * 2, progress - 1, c.getHeight () - shadeWidth * 4 - 1 );
            }
        }
        else
        {
            if ( progressBar.getComponentOrientation ().isLeftToRight () )
            {
                if ( innerRound > 0 )
                {
                    return new RoundRectangle2D.Double ( shadeWidth * 2, c.getHeight () - progress - shadeWidth * 2,
                            c.getWidth () - shadeWidth * 4 - 1, progress - 1, innerRound * 2, innerRound * 2 );
                }
                else
                {
                    return new Rectangle2D.Double ( shadeWidth * 2, c.getHeight () - progress - shadeWidth * 2,
                            c.getWidth () - shadeWidth * 4 - 1, progress - 1 );
                }
            }
            else
            {
                if ( innerRound > 0 )
                {
                    return new RoundRectangle2D.Double ( shadeWidth * 2, shadeWidth * 2, c.getWidth () - shadeWidth * 4 - 1, progress - 1,
                            innerRound * 2, innerRound * 2 );
                }
                else
                {
                    return new Rectangle2D.Double ( shadeWidth * 2, shadeWidth * 2, c.getWidth () - shadeWidth * 4 - 1, progress - 1 );
                }
            }
        }
    }

    private int getProgressWidth ()
    {
        final int progress;
        if ( progressBar.isIndeterminate () )
        {
            if ( progressBar.getOrientation () == JProgressBar.HORIZONTAL )
            {
                progress = progressBar.getWidth () - shadeWidth * 4;
            }
            else
            {
                progress = progressBar.getHeight () - shadeWidth * 4;
            }
        }
        else
        {
            if ( progressBar.getOrientation () == JProgressBar.HORIZONTAL )
            {
                progress = ( int ) ( ( float ) ( progressBar.getWidth () - shadeWidth * 4 ) *
                        ( ( float ) ( progressBar.getValue () - progressBar.getMinimum () ) /
                                ( float ) ( progressBar.getMaximum () - progressBar.getMinimum () ) ) );
            }
            else
            {
                progress = ( int ) ( ( float ) ( progressBar.getHeight () - shadeWidth * 4 ) *
                        ( ( float ) ( progressBar.getValue () - progressBar.getMinimum () ) /
                                ( float ) ( progressBar.getMaximum () - progressBar.getMinimum () ) ) );
            }
        }
        return progress;
    }


    private GeneralPath getIndeterminateProgressShape ( final JComponent c )
    {
        // Small inner
        //        GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        //        gp.moveTo ( shadeWidth * 2 - indeterminateStep * 2, c.getHeight () - shadeWidth*2 - 1 );
        //        gp.lineTo ( shadeWidth * 2 - indeterminateStep, shadeWidth*2 );
        //        gp.lineTo ( shadeWidth * 2, shadeWidth*2 );
        //        gp.lineTo ( shadeWidth * 2 - indeterminateStep, c.getHeight () - shadeWidth*2 - 1 );
        //        gp.closePath ();
        //        return gp;

        // Large inner
        //        GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        //        gp.moveTo ( shadeWidth * 2 - indeterminateStep * 2, c.getHeight () - shadeWidth * 2 );
        //        gp.lineTo ( shadeWidth * 2 - indeterminateStep, shadeWidth * 2 - 1 );
        //        gp.lineTo ( shadeWidth * 2, shadeWidth * 2 - 1 );
        //        gp.lineTo ( shadeWidth * 2 - indeterminateStep, c.getHeight () - shadeWidth * 2 );
        //        gp.closePath ();
        //        return gp;

        // Outer
        final GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        gp.moveTo ( shadeWidth * 2 - indeterminateStep * 2, c.getHeight () - shadeWidth - 1 );
        gp.lineTo ( shadeWidth * 2 - indeterminateStep, shadeWidth );
        gp.lineTo ( shadeWidth * 2, shadeWidth );
        gp.lineTo ( shadeWidth * 2 - indeterminateStep, c.getHeight () - shadeWidth - 1 );
        gp.closePath ();
        return gp;
    }
}