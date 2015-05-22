package com.alee.managers.style.skin.web;

import com.alee.laf.tooltip.ToolTipPainter;
import com.alee.laf.tooltip.WebToolTipUI;
import com.alee.laf.tooltip.WebTooltipStyle;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.SwingUtils;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;
import java.awt.*;
import java.util.Map;

/**
 * @author Alexandr Zernov
 */

public class WebTooltipPainter<E extends JComponent, U extends WebToolTipUI> extends WebDecorationPainter<E, U>
        implements ToolTipPainter<E, U>
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        final Object aa = GraphicsUtils.setupAntialias ( g2d );
        final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, WebTooltipStyle.trasparency );

        g2d.setPaint ( c.getBackground () );
        g2d.fillRoundRect ( 0, 0, c.getWidth (), c.getHeight (), WebTooltipStyle.round * 2, WebTooltipStyle.round * 2 );

        GraphicsUtils.restoreComposite ( g2d, oc );
        GraphicsUtils.restoreAntialias ( g2d, aa );

        super.paint ( g2d, bounds, c, ui );

        final Map taa = SwingUtils.setupTextAntialias ( g2d );

        // Painting text
        paintText ( g2d, bounds, c, ui );

        SwingUtils.restoreTextAntialias ( g2d, taa );
    }

    protected void paintText ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        Font font = c.getFont ();
        FontMetrics metrics = SwingUtilities2.getFontMetrics ( c, g2d, font );
        Dimension size = c.getSize ();

        g2d.setColor ( c.getForeground () );
        // fix for bug 4153892
        String tipText = ( ( JToolTip ) c ).getTipText ();
        if ( tipText == null )
        {
            tipText = "";
        }

        Insets insets = c.getInsets ();
        Rectangle paintTextR = new Rectangle ( insets.left + 3, insets.top, size.width - ( insets.left + insets.right ) - 6,
                size.height - ( insets.top + insets.bottom ) );
        View v = ( View ) c.getClientProperty ( BasicHTML.propertyKey );
        if ( v != null )
        {
            v.paint ( g2d, paintTextR );
        }
        else
        {
            g2d.setFont ( font );
            SwingUtilities2.drawString ( c, g2d, tipText, paintTextR.x, paintTextR.y + metrics.getAscent () );
        }
    }
}
