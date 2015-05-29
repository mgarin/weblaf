package com.alee.managers.style.skin.web;

import com.alee.extended.painter.AbstractPainter;
import com.alee.laf.separator.AbstractSeparatorPainter;
import com.alee.laf.separator.WebSeparator;
import com.alee.laf.separator.WebSeparatorStyle;
import com.alee.utils.GraphicsUtils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSeparatorUI;
import java.awt.*;

/**
 * @author Alexandr Zernov
 */

public class WebAbstractSeparatorPainter<E extends JSeparator, U extends BasicSeparatorUI> extends AbstractPainter<E, U>
        implements AbstractSeparatorPainter<E, U>
{
    // todo
    protected static final float[] fractions = new float[]{ 0.0f, 0.5f, 1f };

    /**
     * Style settings.
     */
    protected Color separatorLightUpperColor = WebSeparatorStyle.separatorLightUpperColor;
    protected Color separatorLightColor = WebSeparatorStyle.separatorLightColor;
    protected Color separatorUpperColor = WebSeparatorStyle.separatorUpperColor;
    protected Color separatorColor = WebSeparatorStyle.separatorColor;
    protected boolean reversedColors = WebSeparatorStyle.reversedColors;
    protected boolean drawLeadingLine = WebSeparatorStyle.drawLeadingLine;
    protected boolean drawTrailingLine = WebSeparatorStyle.drawTrailingLine;

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        final Object aa = GraphicsUtils.setupAntialias ( g2d );

        final Insets insets = c.getInsets ();
        final int x = insets.left;
        final int y = insets.top;
        final int width = c.getWidth () - insets.left - insets.right;
        final int height = c.getHeight () - insets.top - insets.bottom;

        final boolean drawSideLines = drawLeadingLine || drawTrailingLine;
        if ( c.getOrientation () == WebSeparator.VERTICAL )
        {
            if ( height > 0 )
            {
                if ( drawSideLines )
                {
                    g2d.setPaint ( new LinearGradientPaint ( x, y, x, y + height, fractions, getLightColors () ) );
                    if ( drawLeadingLine )
                    {
                        g2d.drawLine ( x, y, x, y + height );
                    }
                    if ( drawTrailingLine )
                    {
                        g2d.drawLine ( x + ( drawLeadingLine ? 2 : 1 ), y, x + ( drawLeadingLine ? 2 : 1 ), y + height );
                    }
                }

                g2d.setPaint ( new LinearGradientPaint ( x, y, x, y + height, fractions, getDarkColors () ) );
                g2d.drawLine ( x + ( drawLeadingLine ? 1 : 0 ), y, x + ( drawLeadingLine ? 1 : 0 ), y + height );
            }
        }
        else
        {
            if ( width > 0 )
            {
                if ( drawSideLines )
                {
                    g2d.setPaint ( new LinearGradientPaint ( x, y, x + width, y, fractions, getLightColors () ) );
                    if ( drawLeadingLine )
                    {
                        g2d.drawLine ( x, y, x + width, y );
                    }
                    if ( drawTrailingLine )
                    {
                        g2d.drawLine ( x, y + ( drawLeadingLine ? 2 : 1 ), x + width, y + ( drawLeadingLine ? 2 : 1 ) );
                    }
                }

                g2d.setPaint ( new LinearGradientPaint ( x, y, x + width, y, fractions, getDarkColors () ) );
                g2d.drawLine ( x, y + ( drawLeadingLine ? 1 : 0 ), x + width, y + ( drawLeadingLine ? 1 : 0 ) );
            }
        }

        GraphicsUtils.restoreAntialias ( g2d, aa );
    }

    protected Color[] getLightColors ()
    {
        return reversedColors ? new Color[]{ separatorUpperColor, separatorColor, separatorUpperColor } :
                new Color[]{ separatorLightUpperColor, Color.WHITE, separatorLightUpperColor };
    }

    protected Color[] getDarkColors ()
    {
        return reversedColors ? new Color[]{ separatorLightUpperColor, Color.WHITE, separatorLightUpperColor } :
                new Color[]{ separatorUpperColor, separatorColor, separatorUpperColor };
    }
}
