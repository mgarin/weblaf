package com.alee.laf.tooltip;

import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;
import java.awt.*;
import java.util.Map;

/**
 * @author Alexandr Zernov
 * @author Mikle Garin
 */

public class ToolTipPainter<E extends JToolTip, U extends WebToolTipUI, D extends IDecoration<E, D>>
        extends AbstractDecorationPainter<E, U, D> implements IToolTipPainter<E, U>
{
    @Override
    protected void paintContent ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        // Painting tooltip text
        paintText ( g2d, bounds, c );
    }

    /**
     * Paints tooltip text.
     *
     * @param bounds paint bounds
     * @param g2d    graphics context
     * @param c      tooltip component
     */
    protected void paintText ( final Graphics2D g2d, final Rectangle bounds, final E c )
    {
        final Font font = c.getFont ();

        final Map taa = SwingUtils.setupTextAntialias ( g2d );
        final Font of = GraphicsUtils.setupFont ( g2d, font );
        final Paint op = GraphicsUtils.setupPaint ( g2d, c.getForeground () );

        // Calculating tooltip text bounds
        final Insets i = c.getInsets ();
        final Rectangle textR = new Rectangle ( bounds.x + i.left, bounds.y + i.top, bounds.width - ( i.left + i.right ),
                bounds.height - ( i.top + i.bottom ) );

        // Painting tooltip text
        final View htmlView = ( View ) c.getClientProperty ( BasicHTML.propertyKey );
        if ( htmlView != null )
        {
            // Painting HTML tooltip
            htmlView.paint ( g2d, textR );
        }
        else
        {
            // Painting plain text tooltip
            final String tipText = c.getTipText () != null ? c.getTipText () : "";
            final FontMetrics fm = SwingUtils.getFontMetrics ( c, g2d, font );
            SwingUtils.drawString ( g2d, tipText, textR.x, textR.y + fm.getAscent () );
        }

        GraphicsUtils.restorePaint ( g2d, op );
        GraphicsUtils.restoreFont ( g2d, of );
        SwingUtils.restoreTextAntialias ( g2d, taa );
    }

    @Override
    public Dimension getPreferredSize ()
    {
        final Insets i = component.getInsets ();
        final Dimension prefSize = new Dimension ( i.left + i.right, i.top + i.bottom );
        final View v = ( View ) component.getClientProperty ( BasicHTML.propertyKey );
        if ( v != null )
        {
            prefSize.width += ( int ) v.getPreferredSpan ( View.X_AXIS );
            prefSize.height += ( int ) v.getPreferredSpan ( View.Y_AXIS );
        }
        else
        {
            final Font font = component.getFont ();
            final FontMetrics fm = component.getFontMetrics ( font );
            final String tipText = component.getTipText () != null ? component.getTipText () : "";
            prefSize.width += SwingUtils.stringWidth ( fm, tipText );
            prefSize.height += fm.getHeight ();
        }
        return prefSize;
    }
}