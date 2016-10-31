package com.alee.laf.progressbar;

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.Bounds;
import com.alee.painter.DefaultPainter;
import com.alee.painter.PainterSupport;
import com.alee.painter.SectionPainter;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.CompareUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.List;

/**
 * Basic painter for {@link JProgressBar} component.
 * It is used as {@link WebProgressBarUI} default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 * @author Mikle Garin
 */

public class ProgressBarPainter<E extends JProgressBar, U extends WebProgressBarUI, D extends IDecoration<E, D>>
        extends AbstractDecorationPainter<E, U, D> implements IProgressBarPainter<E, U>, ChangeListener
{
    /**
     * Style settings.
     * todo Replace with general getMinimumSize method in painters
     */
    protected Dimension minimumContentSize;

    /**
     * Progress line painter.
     */
    @DefaultPainter ( ProgressPainter.class )
    protected IProgressPainter progressPainter;

    /**
     * Progress text painter.
     */
    @DefaultPainter ( ProgressTextPainter.class )
    protected IProgressTextPainter progressTextPainter;

    /**
     * Cached last progress bar value.
     */
    protected transient int value;

    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Properly installing section painters
        this.progressPainter = PainterSupport.installSectionPainter ( this, progressPainter, null, c, ui );
        this.progressTextPainter = PainterSupport.installSectionPainter ( this, progressTextPainter, null, c, ui );

        // Value listener
        value = c.getValue ();
        c.addChangeListener ( this );
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Value listener
        c.removeChangeListener ( this );
        value = -1;

        // Properly uninstalling section painters
        this.progressTextPainter = PainterSupport.uninstallSectionPainter ( progressTextPainter, c, ui );
        this.progressPainter = PainterSupport.uninstallSectionPainter ( progressPainter, c, ui );

        super.uninstall ( c, ui );
    }

    @Override
    public void stateChanged ( final ChangeEvent e )
    {
        // Check value change
        final int newValue = component.getValue ();
        if ( newValue != value )
        {
            // Perform states update only for non-indeterminate progress bar
            if ( !component.isIndeterminate () )
            {
                // Update decoration on border value changes
                final int min = component.getMinimum ();
                final int max = component.getMaximum ();
                if ( value == min || value == max || newValue == min || newValue == max )
                {
                    updateDecorationState ();
                }
            }

            // Save current value
            value = newValue;
        }
    }

    @Override
    protected void propertyChanged ( final String property, final Object oldValue, final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChanged ( property, oldValue, newValue );

        // Update animator on progress state changes
        if ( CompareUtils.equals ( property, WebLookAndFeel.INDETERMINATE_PROPERTY, WebLookAndFeel.ORIENTATION_PROPERTY ) )
        {
            updateDecorationState ();
        }
    }

    @Override
    protected List<SectionPainter<E, U>> getSectionPainters ()
    {
        return asList ( progressPainter, progressTextPainter );
    }

    @Override
    protected List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
        states.add ( isHorizontal () ? DecorationState.horizontal : DecorationState.vertical );
        states.add ( component.isIndeterminate () ? DecorationState.indeterminate : DecorationState.progress );
        if ( !component.isIndeterminate () )
        {
            final boolean min = component.getValue () == component.getMinimum ();
            final boolean max = component.getValue () == component.getMaximum ();
            if ( min )
            {
                states.add ( DecorationState.minimum );
            }
            if ( max )
            {
                states.add ( DecorationState.maximum );
            }
            if ( !min && !max )
            {
                states.add ( DecorationState.intermediate );
            }
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
                PainterSupport.paintSection ( progressPainter, g2d, component, ui, bounds );
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
                    PainterSupport.paintSection ( progressPainter, g2d, component, ui, bounds );
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
            // Painting progress text
            PainterSupport.paintSection ( progressTextPainter, g2d, component, ui, bounds );
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