package com.alee.managers.style.skin.web;

import com.alee.laf.menu.PopupMenuSeparatorPainter;
import com.alee.laf.menu.WebPopupMenuSeparatorStyle;
import com.alee.laf.menu.WebPopupMenuSeparatorUI;
import com.alee.utils.GraphicsUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @author Alexandr Zernov
 */

public class WebPopupMenuSeparatorPainter<E extends JSeparator, U extends WebPopupMenuSeparatorUI> extends WebDecorationPainter<E, U>
        implements PopupMenuSeparatorPainter<E, U>
{
    /**
     * Style settings.
     */
    protected Color color = WebPopupMenuSeparatorStyle.color;
    protected Stroke stroke = WebPopupMenuSeparatorStyle.stroke;
    protected int spacing = WebPopupMenuSeparatorStyle.spacing;
    protected int sideSpacing = WebPopupMenuSeparatorStyle.sideSpacing;

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        super.paint ( g2d, bounds, c, ui );

        final Object aa = GraphicsUtils.setupAntialias ( g2d );
        final Stroke stroke = GraphicsUtils.setupStroke ( g2d, this.stroke );
        g2d.setColor ( color );
        g2d.drawLine ( sideSpacing, c.getHeight () / 2, c.getWidth () - sideSpacing - 1, c.getHeight () / 2 );
        GraphicsUtils.restoreStroke ( g2d, stroke );
        GraphicsUtils.restoreAntialias ( g2d, aa );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize ()
    {
        return new Dimension ( 0, spacing * 2 + 1 );
    }
}
