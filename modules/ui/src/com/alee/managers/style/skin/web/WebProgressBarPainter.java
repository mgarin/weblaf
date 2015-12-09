package com.alee.managers.style.skin.web;

import com.alee.global.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.progressbar.IProgressBarPainter;
import com.alee.laf.progressbar.WebProgressBarUI;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.ThreadUtils;
import com.alee.utils.swing.AncestorAdapter;
import com.alee.utils.swing.WebTimer;
import sun.swing.DefaultLookup;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author Alexandr Zernov
 */

public class WebProgressBarPainter<E extends JProgressBar, U extends WebProgressBarUI> extends AbstractDecorationPainter<E, U>
        implements IProgressBarPainter<E, U>
{
    /**
     * todo Make these as style settings
     */
    protected static final int indeterminateStep = 20;
    protected static final int determinateAnimationWidth = 120;
    protected static final AffineTransform moveX = new AffineTransform ();

    static
    {
        moveX.translate ( indeterminateStep * 2, 0 );
    }

    /**
     * Style settings.
     */
    protected Integer innerRound;
    protected Integer preferredProgressWidth;
    protected Color bgTop;
    protected Color bgBottom;
    protected Color progressTopColor;
    protected Color progressBottomColor;
    protected Color progressEnabledBorderColor;
    protected Color progressDisabledBorderColor;
    protected Color indeterminateBorder;
    protected Color highlightWhite;
    protected Color highlightDarkWhite;
    protected Boolean paintIndeterminateBorder;
    protected Integer determinateAnimationPause;

    /**
     * Listeners.
     */
    protected PropertyChangeListener propertyChangeListener;
    protected AncestorAdapter ancestorAdapter;

    /**
     * Runtime variables.
     */
    protected int animationLocation = 0;
    protected WebTimer animator = null;

    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Ancestor listener
        ancestorAdapter = new AncestorAdapter ()
        {
            @Override
            public void ancestorAdded ( final AncestorEvent event )
            {
                updateAnimator ( c );
            }

            @Override
            public void ancestorRemoved ( final AncestorEvent event )
            {
                updateAnimator ( c );
            }
        };
        c.addAncestorListener ( ancestorAdapter );

        // Change listeners
        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updateAnimator ( c );
            }
        };
        c.addPropertyChangeListener ( WebLookAndFeel.INDETERMINATE_PROPERTY, propertyChangeListener );
        c.addPropertyChangeListener ( WebLookAndFeel.ENABLED_PROPERTY, propertyChangeListener );

        // Initializing animator
        updateAnimator ( c );
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Removing listeners
        c.removePropertyChangeListener ( WebLookAndFeel.INDETERMINATE_PROPERTY, propertyChangeListener );
        c.removePropertyChangeListener ( WebLookAndFeel.ENABLED_PROPERTY, propertyChangeListener );
        c.removeAncestorListener ( ancestorAdapter );

        // Stopping progress
        stopAnimator ();

        super.uninstall ( c, ui );
    }

    private void updateAnimator ( final JProgressBar progressBar )
    {
        stopAnimator ();
        if ( SwingUtils.getWindowAncestor ( progressBar ) != null && progressBar.isShowing () && progressBar.isEnabled () )
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
                        progressBar.repaint ();
                    }
                } );
                animator.setUseEventDispatchThread ( false );
            }
            else
            {
                animationLocation = -determinateAnimationWidth;
                animator = new WebTimer ( "WebProgressBarUI.animator", StyleConstants.animationDelay, determinateAnimationPause,
                        new ActionListener ()
                        {
                            @Override
                            public void actionPerformed ( final ActionEvent e )
                            {
                                if ( animationLocation < getProgressWidth () )
                                {
                                    animationLocation += 15;
                                    progressBar.repaint ();
                                }
                                else
                                {
                                    animationLocation = -determinateAnimationWidth;
                                    progressBar.repaint ();

                                    ThreadUtils.sleepSafely ( determinateAnimationPause );
                                }
                            }
                        } );
            }
            animator.setUseEventDispatchThread ( false );
            animator.start ();
        }
    }

    private void stopAnimator ()
    {
        if ( animator != null )
        {
            animator.stop ();
            animator = null;
        }
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        // Painting progress bar
        super.paint ( g2d, bounds, c, ui );

        //Delegates painting to one of two methods:
        //paintDeterminate or paintIndeterminate.
        final Object aa = GraphicsUtils.setupAntialias ( g2d );
        if ( c.isIndeterminate () )
        {
            paintIndeterminate ( g2d, c );
        }
        else
        {
            paintDeterminate ( g2d, c );
        }
        GraphicsUtils.restoreAntialias ( g2d, aa );
    }

    protected void paintIndeterminate ( final Graphics g, final JComponent c )
    {
        final Graphics2D g2d = ( Graphics2D ) g;

        // Outer border
        paintProgressBarBorder ( c, g2d );

        // Indeterminate view

        final Shape is = getProgressShape ( c );

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

        if ( component.isStringPainted () && component.getString () != null &&
                component.getString ().trim ().length () > 0 )
        {
            final int tw = g2d.getFontMetrics ().stringWidth ( component.getString () );
            final float percentage = ( float ) tw / ( component.getWidth () * 2 );
            final float start = 0.5f - percentage;
            final float end = 0.5f + percentage;
            g2d.setPaint (
                    new LinearGradientPaint ( 0, 0, component.getWidth (), 0, new float[]{ start / 2, start, end, end + ( 1f - end ) / 2 },
                            new Color[]{ StyleConstants.transparent, highlightWhite, highlightWhite, StyleConstants.transparent } ) );
            g2d.fill ( is );
        }

        g2d.setClip ( oldClip );

        // Inner border
        if ( c.isEnabled () )
        {
            GraphicsUtils.drawShade ( g2d, is, StyleConstants.shadeColor, shadeWidth );
        }
        g2d.setPaint ( c.isEnabled () ? this.progressEnabledBorderColor : this.progressDisabledBorderColor );
        g2d.draw ( is );

        // Text
        drawProgressBarText ( g2d );
    }

    protected void paintDeterminate ( final Graphics g, final JComponent c )
    {
        final Graphics2D g2d = ( Graphics2D ) g;

        // Outer border
        paintProgressBarBorder ( c, g2d );

        // Progress bar
        if ( component.getValue () > component.getMinimum () )
        {
            final Shape is = getInnerProgressShape ( c );

            if ( c.isEnabled () )
            {
                GraphicsUtils.drawShade ( g2d, is, StyleConstants.shadeColor, shadeWidth );
            }

            if ( component.getOrientation () == JProgressBar.HORIZONTAL )
            {
                g2d.setPaint ( new GradientPaint ( 0, shadeWidth, progressTopColor, 0, c.getHeight () - shadeWidth, progressBottomColor ) );
            }
            else
            {
                if ( ltr )
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
                if ( component.getOrientation () == JProgressBar.HORIZONTAL )
                {
                    g2d.setPaint ( new RadialGradientPaint ( shadeWidth * 2 + animationLocation + determinateAnimationWidth / 2,
                            component.getHeight () / 2, determinateAnimationWidth / 2, new float[]{ 0f, 1f },
                            new Color[]{ highlightDarkWhite, StyleConstants.transparent } ) );
                }
                else
                {
                    if ( ltr )
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
            g2d.setPaint ( c.isEnabled () ? this.progressEnabledBorderColor : this.progressDisabledBorderColor );
            g2d.draw ( is );
        }

        // Text
        drawProgressBarText ( g2d );
    }

    private void drawProgressBarText ( final Graphics2D g2d )
    {
        if ( component.isStringPainted () )
        {
            if ( component.getOrientation () == JProgressBar.VERTICAL )
            {
                g2d.translate ( component.getWidth () / 2, component.getHeight () / 2 );
                g2d.rotate ( ( ltr ? -1 : 1 ) * Math.PI / 2 );
                g2d.translate ( -component.getWidth () / 2, -component.getHeight () / 2 );
            }

            final String string = component.getString ();
            final Point ts = LafUtils.getTextCenterShift ( g2d.getFontMetrics (), string );
            if ( !component.isEnabled () )
            {
                g2d.setPaint ( Color.WHITE );
                g2d.drawString ( string, component.getWidth () / 2 + ts.x + 1, component.getHeight () / 2 + ts.y + 1 );
            }
            g2d.setPaint ( component.isEnabled () ? component.getForeground () : StyleConstants.disabledTextColor );
            g2d.drawString ( string, component.getWidth () / 2 + ts.x, component.getHeight () / 2 + ts.y );

            if ( component.getOrientation () == JProgressBar.VERTICAL )
            {
                g2d.rotate ( ( ltr ? 1 : -1 ) * Math.PI / 2 );
            }
        }
    }

    private void paintProgressBarBorder ( final JComponent c, final Graphics2D g2d )
    {
        final Shape bs = getProgressShape ( c );

        if ( c.isEnabled () )
        {
            GraphicsUtils.drawShade ( g2d, bs, StyleConstants.shadeColor, shadeWidth );
        }

        if ( component.getOrientation () == JProgressBar.HORIZONTAL )
        {
            g2d.setPaint ( new GradientPaint ( 0, shadeWidth, bgTop, 0, c.getHeight () - shadeWidth, bgBottom ) );
        }
        else
        {
            g2d.setPaint ( new GradientPaint ( shadeWidth, 0, bgTop, c.getWidth () - shadeWidth, 0, bgBottom ) );
        }
        g2d.fill ( bs );

        g2d.setPaint ( c.isEnabled () ? this.progressEnabledBorderColor : this.progressDisabledBorderColor );
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
        if ( component.getOrientation () == JProgressBar.HORIZONTAL )
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
            if ( ltr )
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

    private int getProgressWidth ()
    {
        final int progress;
        if ( component.isIndeterminate () )
        {
            if ( component.getOrientation () == JProgressBar.HORIZONTAL )
            {
                progress = component.getWidth () - shadeWidth * 4;
            }
            else
            {
                progress = component.getHeight () - shadeWidth * 4;
            }
        }
        else
        {
            if ( component.getOrientation () == JProgressBar.HORIZONTAL )
            {
                progress = ( int ) ( ( float ) ( component.getWidth () - shadeWidth * 4 ) *
                        ( ( float ) ( component.getValue () - component.getMinimum () ) /
                                ( float ) ( component.getMaximum () - component.getMinimum () ) ) );
            }
            else
            {
                progress = ( int ) ( ( float ) ( component.getHeight () - shadeWidth * 4 ) *
                        ( ( float ) ( component.getValue () - component.getMinimum () ) /
                                ( float ) ( component.getMaximum () - component.getMinimum () ) ) );
            }
        }
        return progress;
    }

    @Override
    public Dimension getPreferredSize ()
    {
        final Dimension size;
        final Insets border = component.getInsets ();
        final FontMetrics fontSizer = component.getFontMetrics ( component.getFont () );

        if ( component.getOrientation () == JProgressBar.HORIZONTAL )
        {
            size = new Dimension ( getPreferredInnerHorizontal () );
            // Ensure that the progress string will fit
            if ( component.isStringPainted () )
            {
                // I'm doing this for completeness.
                final String progString = component.getString ();
                final int stringWidth = SwingUtilities2.stringWidth ( component, fontSizer, progString );
                if ( stringWidth > size.width )
                {
                    size.width = stringWidth;
                }
                // This uses both Height and Descent to be sure that
                // there is more than enough room in the progress bar
                // for everything.
                // This does have a strange dependency on
                // getStringPlacememnt() in a funny way.
                final int stringHeight = fontSizer.getHeight () + fontSizer.getDescent ();
                if ( stringHeight > size.height )
                {
                    size.height = stringHeight;
                }
            }
        }
        else
        {
            size = new Dimension ( getPreferredInnerVertical () );
            // Ensure that the progress string will fit.
            if ( component.isStringPainted () )
            {
                final String progString = component.getString ();
                final int stringHeight = fontSizer.getHeight () + fontSizer.getDescent ();
                if ( stringHeight > size.width )
                {
                    size.width = stringHeight;
                }
                // This is also for completeness.
                final int stringWidth = SwingUtilities2.stringWidth ( component, fontSizer, progString );
                if ( stringWidth > size.height )
                {
                    size.height = stringWidth;
                }
            }
        }

        size.width += border.left + border.right;
        size.height += border.top + border.bottom;
        return size;
    }

    protected Dimension getPreferredInnerHorizontal ()
    {
        Dimension horizDim = ( Dimension ) DefaultLookup.get ( component, ui, "ProgressBar.horizontalSize" );
        if ( horizDim == null )
        {
            horizDim = new Dimension ( 150, 12 );
        }
        if ( preferredProgressWidth != null )
        {
            horizDim.width = preferredProgressWidth;
        }
        return horizDim;
    }

    protected Dimension getPreferredInnerVertical ()
    {
        Dimension vertDim = ( Dimension ) DefaultLookup.get ( component, ui, "ProgressBar.vertictalSize" );
        if ( vertDim == null )
        {
            vertDim = new Dimension ( 12, 150 );
        }
        if ( preferredProgressWidth != null )
        {
            vertDim.height = preferredProgressWidth;
        }
        return vertDim;
    }
}