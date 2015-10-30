package com.alee.managers.style.skin.web;

import com.alee.laf.desktoppane.InternalFramePainter;
import com.alee.laf.desktoppane.WebInternalFrameUI;
import com.alee.utils.GraphicsUtils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * @author Alexandr Zernov
 */

public class WebInternalFramePainter<E extends JInternalFrame, U extends WebInternalFrameUI> extends WebDecorationPainter<E, U>
        implements InternalFramePainter<E, U>
{
    /**
     * Painting variables
     */
    protected BasicInternalFrameTitlePane titlePane = null;

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        final Object aa = GraphicsUtils.setupAntialias ( g2d );

        // Painting decoration
        super.paint ( g2d, bounds, c, ui );

        // Inner border
        final Insets insets = c.getInsets ();
        final int sideSpacing = ui.getSideSpacing ();
        final int x = insets.left + 3 + sideSpacing;
        final int y = insets.top + titlePane.getHeight () - 1;
        final int w = c.getWidth () - 1 - insets.left - 3 - sideSpacing - insets.right - 3 - sideSpacing;
        final int h = c.getHeight () - 1 - insets.top - titlePane.getHeight () + 1 - insets.bottom - 3 - sideSpacing;
        final int round = ( this.round - 1 ) * 2;
        g2d.setPaint ( Color.GRAY );
        g2d.draw ( new RoundRectangle2D.Double ( x, y, w, h, round, round ) );

        GraphicsUtils.restoreAntialias ( g2d, aa );

        titlePane = null;
    }

    @Override
    public void prepareToPaint ( final BasicInternalFrameTitlePane titlePane )
    {
        this.titlePane = titlePane;
    }
}