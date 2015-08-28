package com.alee.managers.style.skin.web;

import com.alee.laf.tooltip.ToolTipPainter;
import com.alee.laf.tooltip.WebToolTipUI;
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

public class WebToolTipPainter<E extends JComponent, U extends WebToolTipUI> extends WebDecorationPainter<E, U>
        implements ToolTipPainter<E, U>
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        super.paint ( g2d, bounds, c, ui );

        paintText ( g2d, bounds, c, ui );
    }

    protected void paintText ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        final Map taa = SwingUtils.setupTextAntialias ( g2d );

        final Font font = c.getFont ();
        final FontMetrics metrics = SwingUtilities2.getFontMetrics ( c, g2d, font );
        final Dimension size = c.getSize ();

        g2d.setColor ( c.getForeground () );
        // fix for bug 4153892
        String tipText = ( ( JToolTip ) c ).getTipText ();
        if ( tipText == null )
        {
            tipText = "";
        }

        final Insets insets = c.getInsets ();
        final Rectangle paintTextR = new Rectangle ( insets.left + 3, insets.top, size.width - ( insets.left + insets.right ) - 6,
                size.height - ( insets.top + insets.bottom ) );
        final View v = ( View ) c.getClientProperty ( BasicHTML.propertyKey );
        if ( v != null )
        {
            v.paint ( g2d, paintTextR );
        }
        else
        {
            g2d.setFont ( font );
            SwingUtilities2.drawString ( c, g2d, tipText, paintTextR.x, paintTextR.y + metrics.getAscent () );
        }

        SwingUtils.restoreTextAntialias ( g2d, taa );
    }
}