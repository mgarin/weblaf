package com.alee.laf.toolbar;

import com.alee.painter.AbstractPainter;
import com.alee.laf.panel.PanelPainter;
import com.alee.laf.panel.WebPanelUI;

import javax.swing.*;
import java.awt.*;

/**
 * Painter class for toolbar corners.
 *
 * @author Alexandr Zernov
 */

public class WebToolBarCornerPainter<E extends JPanel, U extends WebPanelUI> extends AbstractPainter<E, U>
        implements PanelPainter<E, U>, SwingConstants
{
    /**
     * Constant fractions.
     */
    protected static final float[] fractions = new float[]{ 0f, 0.5f };

    /**
     * Corner position which defines which toolbars this corner connects.
     * For example when in NORTH_WEST position it should connect horizontal toolbar at its left side with a vertical toolbar.
     */
    protected int position = NORTH_WEST;

    /**
     * Constant colors.
     */
    protected static final Color[] colors = new Color[]{ WebToolBarStyle.bottomBgColor, WebToolBarStyle.topBgColor };

    /**
     * Returns corner position.
     *
     * @return corner position
     */
    public int getPosition ()
    {
        return position;
    }

    /**
     * Sets corner position.
     *
     * @param position corner position
     */
    public void setPosition ( final int position )
    {
        this.position = position;
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        final boolean top = position == NORTH_WEST || position == NORTH_EAST;
        final boolean left =
                ltr ? ( position == NORTH_WEST || position == SOUTH_WEST ) : ( position == NORTH_EAST || position == SOUTH_EAST );
        if ( top )
        {
            if ( left )
            {
                final Rectangle gradientBounds = new Rectangle ( bounds.x, bounds.y, bounds.width * 2, bounds.height * 2 );
                g2d.setPaint ( new RadialGradientPaint ( gradientBounds, fractions, colors, MultipleGradientPaint.CycleMethod.NO_CYCLE ) );
                g2d.fill ( bounds );

                g2d.setPaint ( c.isEnabled () ? WebToolBarStyle.borderColor : WebToolBarStyle.disabledBorderColor );
                g2d.drawLine ( bounds.x + bounds.width - 1, bounds.y + bounds.height - 1, bounds.x + bounds.width - 1,
                        bounds.y + bounds.height - 1 );
            }
            else
            {
                final Rectangle gradientBounds = new Rectangle ( bounds.x - bounds.width, bounds.y, bounds.width * 2, bounds.height * 2 );
                g2d.setPaint ( new RadialGradientPaint ( gradientBounds, fractions, colors, MultipleGradientPaint.CycleMethod.NO_CYCLE ) );
                g2d.fill ( bounds );

                g2d.setPaint ( c.isEnabled () ? WebToolBarStyle.borderColor : WebToolBarStyle.disabledBorderColor );
                g2d.drawLine ( bounds.x, bounds.y + bounds.height - 1, bounds.x, bounds.y + bounds.height - 1 );
            }
        }
        else
        {
            if ( left )
            {
                final Rectangle gradientBounds = new Rectangle ( bounds.x, bounds.y - bounds.height, bounds.width * 2, bounds.height * 2 );
                g2d.setPaint ( new RadialGradientPaint ( gradientBounds, fractions, colors, MultipleGradientPaint.CycleMethod.NO_CYCLE ) );
                g2d.fill ( bounds );

                g2d.setPaint ( c.isEnabled () ? WebToolBarStyle.borderColor : WebToolBarStyle.disabledBorderColor );
                g2d.drawLine ( bounds.x + bounds.width - 1, bounds.y, bounds.x + bounds.width - 1, bounds.y );
            }
            else
            {
                final Rectangle gradientBounds =
                        new Rectangle ( bounds.x - bounds.width, bounds.y - bounds.height, bounds.width * 2, bounds.height * 2 );
                g2d.setPaint ( new RadialGradientPaint ( gradientBounds, fractions, colors, MultipleGradientPaint.CycleMethod.NO_CYCLE ) );
                g2d.fill ( bounds );

                g2d.setPaint ( c.isEnabled () ? WebToolBarStyle.borderColor : WebToolBarStyle.disabledBorderColor );
                g2d.drawLine ( bounds.x, bounds.y, bounds.x, bounds.y );
            }
        }
    }
}