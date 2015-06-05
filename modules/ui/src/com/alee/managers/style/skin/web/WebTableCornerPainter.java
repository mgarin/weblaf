package com.alee.managers.style.skin.web;

import com.alee.extended.painter.AbstractPainter;
import com.alee.laf.panel.PanelPainter;
import com.alee.laf.panel.WebPanelUI;
import com.alee.laf.table.WebTableStyle;
import com.alee.utils.CompareUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @author Alexandr Zernov
 */

public class WebTableCornerPainter<E extends JPanel, U extends WebPanelUI> extends AbstractPainter<E, U> implements PanelPainter<E, U>
{
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        // todo Proper painting for RTL
        final int width = component.getWidth ();
        final int height = component.getHeight ();

        // Highlight
        g2d.setPaint ( WebTableStyle.headerTopLineColor );
        g2d.drawLine ( 0, 0, width - 1, 0 );

        // Background
        g2d.setPaint ( createBackgroundPaint ( 0, 1, 0, height - 1 ) );
        g2d.fillRect ( 0, 1, width, height - 1 );

        // Bottom line
        g2d.setColor ( WebTableStyle.headerBottomLineColor );
        g2d.drawLine ( 0, height - 1, width - 1, height - 1 );

        // Right line
        if ( ltr )
        {
            g2d.setColor ( WebTableStyle.gridColor );
            g2d.drawLine ( 0, 2, 0, height - 4 );
        }
        else
        {
            g2d.setColor ( WebTableStyle.gridColor );
            g2d.drawLine ( width - 1, 2, width - 1, height - 4 );
        }
    }

    protected Paint createBackgroundPaint ( final int x1, final int y1, final int x2, final int y2 )
    {
        final Color topBgColor = WebTableStyle.headerTopBgColor;
        final Color bottomBgColor = WebTableStyle.headerBottomBgColor;

        if ( bottomBgColor == null || CompareUtils.equals ( topBgColor, bottomBgColor ) )
        {
            return topBgColor;
        }
        else
        {
            return new GradientPaint ( x1, y1, topBgColor, x2, y2, bottomBgColor );
        }
    }
}
