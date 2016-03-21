package com.alee.laf.table;

import com.alee.laf.panel.IPanelPainter;
import com.alee.laf.panel.WebPanelUI;
import com.alee.painter.AbstractPainter;
import com.alee.utils.CompareUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @author Alexandr Zernov
 */

public class TableCornerPainter<E extends JPanel, U extends WebPanelUI> extends AbstractPainter<E, U> implements IPanelPainter<E, U>
{
    /**
     * todo 1. Create `WebCanvas` + `CanvasPainter -> ICanvasPainter` that you could extend upon to paint custom things
     */

    /**
     * Default top header line color.
     */
    public static Color topLineColor;

    /**
     * Default bottom header line color.
     */
    public static Color bottomLineColor;

    /**
     * Default top header background color.
     */
    public static Color topBgColor;

    /**
     * Default bottom header background color.
     */
    public static Color bottomBgColor;

    /**
     * Table grid color.
     */
    public static Color gridColor;

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        final int width = component.getWidth ();
        final int height = component.getHeight ();

        // Highlight
        g2d.setPaint ( topLineColor );
        g2d.drawLine ( 0, 0, width - 1, 0 );

        // Background
        g2d.setPaint ( createBackgroundPaint ( 0, 1, 0, height - 1 ) );
        g2d.fillRect ( 0, 1, width, height - 1 );

        // Bottom line
        g2d.setPaint ( bottomLineColor );
        g2d.drawLine ( 0, height - 1, width - 1, height - 1 );

        // Right line
        if ( ltr )
        {
            g2d.setPaint ( gridColor );
            g2d.drawLine ( 0, 2, 0, height - 4 );
        }
        else
        {
            g2d.setPaint ( gridColor );
            g2d.drawLine ( width - 1, 2, width - 1, height - 4 );
        }
    }

    protected Paint createBackgroundPaint ( final int x1, final int y1, final int x2, final int y2 )
    {
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