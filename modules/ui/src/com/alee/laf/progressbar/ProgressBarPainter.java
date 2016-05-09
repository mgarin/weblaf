package com.alee.laf.progressbar;

import com.alee.global.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.Bounds;
import com.alee.painter.DefaultPainter;
import com.alee.painter.PainterSupport;
import com.alee.painter.SectionPainter;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.CompareUtils;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Basic painter for JProgressBar component.
 * It is used as WebProgressBarUI default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 * @author Mikle Garin
 */

public class ProgressBarPainter<E extends JProgressBar, U extends WebProgressBarUI, D extends IDecoration<E, D>>
        extends AbstractDecorationPainter<E, U, D> implements IProgressBarPainter<E, U>
{
    /**
     * Style settings.
     */
    protected Dimension minimumContentSize;

    /**
     * Progress line painter.
     */
    @DefaultPainter (ProgressPainter.class)
    protected IProgressPainter progressPainter;

    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Properly installing section painters
        this.progressPainter = PainterSupport.installSectionPainter ( this, progressPainter, null, c, ui );
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Properly uninstalling section painters
        this.progressPainter = PainterSupport.uninstallSectionPainter ( progressPainter, c, ui );

        super.uninstall ( c, ui );
    }

    @Override
    protected void propertyChange ( final String property, final Object oldValue, final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChange ( property, oldValue, newValue );

        // Update animator on progress state changes
        if ( CompareUtils.equals ( property, WebLookAndFeel.INDETERMINATE_PROPERTY, WebLookAndFeel.ORIENTATION_PROPERTY ) )
        {
            updateDecorationState ();
        }
    }

    @Override
    protected List<SectionPainter<E, U>> getSectionPainters ()
    {
        return asList ( progressPainter );
    }

    @Override
    protected List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
        if ( isHorizontal () )
        {
            states.add ( DecorationState.horizontal );
        }
        else
        {
            states.add ( DecorationState.vertical );
        }
        if ( component.isIndeterminate () )
        {
            states.add ( DecorationState.indeterminate );
        }
        return states;
    }

    @Override
    protected void paintContent ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        // Painting progress line
        paintProgress ( g2d, Bounds.border.of ( c ) );

        // Painting text
        paintText ( g2d, Bounds.padding.of ( c ) );
    }

    /**
     * Paints progress line.
     *
     * @param g2d    graphics context
     * @param bounds painting bounds
     */
    protected void paintProgress ( final Graphics2D g2d, final Rectangle bounds )
    {
        if ( progressPainter != null )
        {
            if ( component.isIndeterminate () )
            {
                // Painting indeterminate progress
                progressPainter.paint ( g2d, bounds, component, ui );
            }
            else
            {
                // Calculating actual progress size
                final boolean hor = isHorizontal ();
                final int min = component.getMinimum ();
                final float progress = ( float ) ( component.getValue () - min ) / ( component.getMaximum () - min );
                final int p = Math.round ( ( hor ? bounds.width : bounds.height ) * progress );
                final Dimension ps = progressPainter.getPreferredSize ();

                // Painting progress only when it fits into provided bounds
                // todo Must use minimum size in future when it will be available in painters
                // todo Also probably must do this inside of the decoration painter itself?
                if ( p > ( hor ? ps.width : ps.height ) )
                {
                    if ( hor )
                    {
                        if ( !ltr )
                        {
                            bounds.x = bounds.x + bounds.width - p;
                        }
                        bounds.width = p;
                    }
                    else
                    {
                        if ( ltr )
                        {
                            bounds.y = bounds.y + bounds.height - p;
                        }
                        bounds.height = p;
                    }
                    progressPainter.paint ( g2d, bounds, component, ui );
                }
            }
        }
    }

    /**
     * Paints progress bar text.
     *
     * @param g2d    graphics context
     * @param bounds painting bounds
     */
    protected void paintText ( final Graphics2D g2d, final Rectangle bounds )
    {
        if ( component.isStringPainted () )
        {
            final Map aa = SwingUtils.setupTextAntialias ( g2d );
            final Shape oc = GraphicsUtils.setupClip ( g2d, bounds );

            final Point mid = new Point ( bounds.x + bounds.width / 2, bounds.y + bounds.height / 2 );
            final boolean hor = isHorizontal ();
            if ( !hor )
            {
                g2d.translate ( mid.x, mid.y );
                g2d.rotate ( ( ltr ? -1 : 1 ) * Math.PI / 2 );
                g2d.translate ( -mid.x, -mid.y );
            }

            final String string = component.getString ();
            final Point ts = LafUtils.getTextCenterShift ( g2d.getFontMetrics (), string );

            if ( !component.isEnabled () )
            {
                g2d.setPaint ( Color.WHITE );
                g2d.drawString ( string, mid.x + ts.x + 1, mid.y + ts.y + 1 );
            }
            g2d.setPaint ( component.isEnabled () ? component.getForeground () : StyleConstants.disabledTextColor );
            g2d.drawString ( string, mid.x + ts.x, mid.y + ts.y );

            if ( !hor )
            {
                g2d.rotate ( ( ltr ? 1 : -1 ) * Math.PI / 2 );
            }

            GraphicsUtils.restoreClip ( g2d, oc );
            SwingUtils.restoreTextAntialias ( g2d, aa );
        }
    }

    /**
     * Returns minimum content area size.
     *
     * @return minimum content area size
     */
    protected Dimension getMinimumContentSize ()
    {
        if ( component != null && minimumContentSize != null )
        {
            final boolean hor = isHorizontal ();
            return new Dimension ( hor ? minimumContentSize.width : minimumContentSize.height,
                    hor ? minimumContentSize.height : minimumContentSize.width );
        }
        return new Dimension ();
    }

    /**
     * Returns whether or not progress bar is horizontal.
     *
     * @return true if progress bar is horizontal, false otherwise
     */
    protected boolean isHorizontal ()
    {
        return component.getOrientation () == SwingConstants.HORIZONTAL;
    }

    @Override
    public Dimension getPreferredSize ()
    {
        // Minimum size
        final Dimension min = getMinimumContentSize ();
        int w = min.width;
        int h = min.height;

        // Text size
        if ( component.isStringPainted () )
        {
            final boolean hor = isHorizontal ();
            final FontMetrics fontSizer = component.getFontMetrics ( component.getFont () );
            final String progString = component.getString ();
            final int stringWidth = SwingUtils.stringWidth ( fontSizer, progString );
            final int stringHeight = fontSizer.getHeight () + fontSizer.getDescent ();
            w = Math.max ( w, hor ? stringWidth : stringHeight );
            h = Math.max ( h, hor ? stringHeight : stringWidth );
        }

        // Final size
        final Insets border = component.getInsets ();
        return new Dimension ( border.left + w + border.right, border.top + h + border.bottom );
    }
}