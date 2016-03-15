package com.alee.managers.style.skin.web;

import com.alee.global.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.progressbar.IProgressBarPainter;
import com.alee.laf.progressbar.IProgressPainter;
import com.alee.laf.progressbar.WebProgressBarUI;
import com.alee.managers.style.skin.web.data.DecorationState;
import com.alee.managers.style.skin.web.data.decoration.IDecoration;
import com.alee.painter.PainterSupport;
import com.alee.utils.CompareUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * @author Alexandr Zernov
 */

public class WebProgressBarPainter<E extends JProgressBar, U extends WebProgressBarUI, D extends IDecoration<E, D>>
        extends AbstractDecorationPainter<E, U, D> implements IProgressBarPainter<E, U>
{
    /**
     * Progress line painter.
     */
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
        if ( CompareUtils.equals ( property, WebLookAndFeel.INDETERMINATE_PROPERTY, WebLookAndFeel.ENABLED_PROPERTY ) )
        {
            updateDecorationState ();
        }
    }

    @Override
    protected List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
        if ( component.isIndeterminate () )
        {
            states.add ( DecorationState.indeterminate );
        }
        return states;
    }

    @Override
    protected void paintContent ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        // Calculating inner bounds
        final Rectangle b = b ( bounds, component.getInsets () );

        // Painting progress line
        paintProgress ( g2d, b );

        // Painting text
        paintProgressBarText ( g2d, b );
    }

    protected void paintProgress ( final Graphics2D g2d, final Rectangle bounds )
    {
        if ( progressPainter != null )
        {
            progressPainter.paint ( g2d, bounds, component, ui );
        }
    }

    protected void paintProgressBarText ( final Graphics2D g2d, final Rectangle bounds )
    {
        if ( component.isStringPainted () )
        {
            final Map aa = SwingUtils.setupTextAntialias ( g2d );

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

            SwingUtils.restoreTextAntialias ( g2d, aa );
        }
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
                final int stringWidth = SwingUtils.stringWidth ( fontSizer, progString );
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
                final int stringWidth = SwingUtils.stringWidth ( fontSizer, progString );
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
        Dimension horizDim = ( Dimension ) UIManager.get ( "ProgressBar.horizontalSize" );
        if ( horizDim == null )
        {
            horizDim = new Dimension ( 150, 12 );
        }
        return horizDim;
    }

    protected Dimension getPreferredInnerVertical ()
    {
        Dimension vertDim = ( Dimension ) UIManager.get ( "ProgressBar.vertictalSize" );
        if ( vertDim == null )
        {
            vertDim = new Dimension ( 12, 150 );
        }
        return vertDim;
    }
}