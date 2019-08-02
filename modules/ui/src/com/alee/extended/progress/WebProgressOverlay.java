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

package com.alee.extended.progress;

import com.alee.extended.panel.WebOverlay;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.NoOpMouseListener;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;

/**
 * @author Mikle Garin
 */
public class WebProgressOverlay extends WebOverlay
{
    public static final String ANIMATOR_ID = "WebProgressOverlay.animator";
    public static final String OPACITY_ANIMATOR_ID = "WebProgressOverlay.opacityAnimator";

    private int progressWidth = 15;
    private int speed = 1;
    private Color progressColor = Color.GRAY;

    private ProgressLayer progressLayer;

    public WebProgressOverlay ()
    {
        super ();
        initializeProgressLayer ();
    }

    public WebProgressOverlay ( final JComponent component )
    {
        super ( component );
        initializeProgressLayer ();
    }

    protected void initializeProgressLayer ()
    {
        progressLayer = new ProgressLayer ();
        progressLayer.setVisible ( false );
        addOverlay ( progressLayer );
    }

    public boolean isShowLoad ()
    {
        return progressLayer.isShowLoad ();
    }

    public void setShowLoad ( final boolean showLoad )
    {
        progressLayer.setShowLoad ( showLoad );
    }

    public boolean isConsumeEvents ()
    {
        return progressLayer.isConsumeEvents ();
    }

    public void setConsumeEvents ( final boolean consumeEvents )
    {
        progressLayer.setConsumeEvents ( consumeEvents );
    }

    public int getProgressWidth ()
    {
        return progressWidth;
    }

    public void setProgressWidth ( final int progressWidth )
    {
        this.progressWidth = progressWidth;
    }

    public int getSpeed ()
    {
        return speed;
    }

    public void setSpeed ( final int speed )
    {
        this.speed = speed;
    }

    public Color getProgressColor ()
    {
        return progressColor;
    }

    public void setProgressColor ( final Color progressColor )
    {
        this.progressColor = progressColor;
    }

    private class ProgressLayer extends JComponent
    {
        private boolean showLoad = false;
        private boolean consumeEvents = true;

        private WebTimer opacityAnimator = null;
        private WebTimer animator = null;
        private int opacity = 0;
        private int loadProgress = 0;

        public ProgressLayer ()
        {
            super ();
            SwingUtils.setOrientation ( this );
            NoOpMouseListener.install ( this );
        }

        @Override
        public boolean contains ( final int x, final int y )
        {
            return consumeEvents && super.contains ( x, y );
        }

        public boolean isConsumeEvents ()
        {
            return consumeEvents;
        }

        public void setConsumeEvents ( final boolean consumeEvents )
        {
            this.consumeEvents = consumeEvents;
        }

        public boolean isShowLoad ()
        {
            return showLoad;
        }

        public void setShowLoad ( final boolean showLoad )
        {
            this.showLoad = showLoad;
            if ( showLoad )
            {
                ProgressLayer.this.setVisible ( true );

                stopOpacityAnimator ();
                opacityAnimator = new WebTimer ( "WebProgressOverlay.opacityAnimator", SwingUtils.frameRateDelay ( 24 ), new ActionListener ()
                {
                    @Override
                    public void actionPerformed ( final ActionEvent e )
                    {
                        if ( opacity < 128 )
                        {
                            opacity += 8;
                        }
                        else
                        {
                            opacity = 128;
                            opacityAnimator.stop ();
                        }
                    }
                } );
                opacityAnimator.start ();

                stopAnimator ();
                animator = new WebTimer ( ANIMATOR_ID, SwingUtils.frameRateDelay ( 36 ), new ActionListener ()
                {
                    @Override
                    public void actionPerformed ( final ActionEvent e )
                    {
                        if ( loadProgress < getProgressWidth () * 2 )
                        {
                            loadProgress += speed;
                        }
                        else
                        {
                            loadProgress = 0;
                        }
                        if ( ProgressLayer.this.isVisible () )
                        {
                            ProgressLayer.this.repaint ();
                        }
                    }
                } );
                animator.setNonBlockingStop ( true );
                animator.start ();
            }
            else
            {
                stopOpacityAnimator ();
                opacityAnimator = new WebTimer ( OPACITY_ANIMATOR_ID, SwingUtils.frameRateDelay ( 36 ), new ActionListener ()
                {
                    @Override
                    public void actionPerformed ( final ActionEvent e )
                    {
                        if ( opacity > 0 )
                        {
                            opacity -= 8;
                        }
                        else
                        {
                            opacity = 0;
                            opacityAnimator.stop ();

                            stopAnimator ();

                            ProgressLayer.this.setVisible ( false );
                        }
                    }
                } );
                opacityAnimator.setNonBlockingStop ( true );
                opacityAnimator.start ();
            }
        }

        private void stopAnimator ()
        {
            if ( animator != null && animator.isRunning () )
            {
                animator.stop ();
                animator = null;
            }
        }

        private void stopOpacityAnimator ()
        {
            if ( opacityAnimator != null && opacityAnimator.isRunning () )
            {
                opacityAnimator.stop ();
            }
        }

        @Override
        protected void paintComponent ( final Graphics g )
        {
            super.paintComponent ( g );

            if ( opacity > 0 )
            {
                final int w = getWidth ();
                final int h = getHeight ();
                final int pw = getProgressWidth ();
                if ( w > 0 && h > 0 && pw > 0 )
                {
                    final Graphics2D g2d = ( Graphics2D ) g;
                    final Object aa = GraphicsUtils.setupAntialias ( g2d );

                    final Shape clip = LafUtils.getShape ( WebProgressOverlay.this.getComponent () );
                    final Shape oldClip = GraphicsUtils.intersectClip ( g2d, clip, clip != null );

                    // todo Draw correctly when width is less than height
                    g2d.setPaint ( new Color ( progressColor.getRed (), progressColor.getGreen (), progressColor.getBlue (), opacity ) );
                    for ( int i = loadProgress % ( pw * 2 ); i < w + 2 * h; i += pw * 2 )
                    {
                        if ( i <= 0 )
                        {
                            continue;
                        }

                        final GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );

                        // Top side lines
                        gp.moveTo ( i < w ? i : w, i < w ? 0 : Math.min ( h, i - w ) );
                        if ( i - pw < w && i > w )
                        {
                            gp.lineTo ( w, 0 );
                        }
                        gp.lineTo ( i - pw < w ? Math.max ( 0, i - pw ) : w, i - pw < w ? 0 : i - pw - w );

                        // Bottom side lines
                        gp.lineTo ( i - pw < h ? 0 : i - pw - h, i - pw < h ? Math.max ( 0, i - pw ) : h );
                        if ( i - pw < h && i > h )
                        {
                            gp.lineTo ( 0, h );
                        }
                        gp.lineTo ( i < h ? 0 : Math.min ( w, i - h ), i < h ? i : h );

                        gp.closePath ();
                        g2d.fill ( gp );
                    }

                    GraphicsUtils.restoreClip ( g, oldClip, clip != null );
                    GraphicsUtils.restoreAntialias ( g2d, aa );
                }
            }
        }
    }
}