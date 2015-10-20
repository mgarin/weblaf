package com.alee.managers.style.skin.web;

import com.alee.laf.rootpane.RootPanePainter;
import com.alee.laf.rootpane.WebRootPaneStyle;
import com.alee.laf.rootpane.WebRootPaneUI;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.NinePatchUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.ninepatch.NinePatchIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * @author Alexandr Zernov
 */

public class WebRootPanePainter<E extends JRootPane, U extends WebRootPaneUI> extends WebDecorationPainter<E, U>
        implements RootPanePainter<E, U>
{
    /**
     * Style settings.
     */
    protected int inactiveShadeWidth = WebRootPaneStyle.inactiveShadeWidth;
    protected Color topBg = WebRootPaneStyle.topBg;
    protected Color middleBg = WebRootPaneStyle.middleBg;
    protected Color innerBorderColor = WebRootPaneStyle.innerBorderColor;

    public Color getTopBg ()
    {
        return topBg;
    }

    public void setTopBg ( final Color topBg )
    {
        this.topBg = topBg;
    }

    public Color getMiddleBg ()
    {
        return middleBg;
    }

    public void setMiddleBg ( final Color middleBg )
    {
        this.middleBg = middleBg;
    }

    public Color getInnerBorderColor ()
    {
        return innerBorderColor;
    }

    public void setInnerBorderColor ( final Color innerBorderColor )
    {
        this.innerBorderColor = innerBorderColor;
    }

    public int getInactiveShadeWidth ()
    {
        return inactiveShadeWidth;
    }

    public void setInactiveShadeWidth ( final int inactiveShadeWidth )
    {
        this.inactiveShadeWidth = inactiveShadeWidth;
    }

    @Override
    public Insets getBorders ()
    {
        return ui.isStyled () ? super.getBorders () : null;
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        // todo Merge with decoration
        if ( ui.isStyled () )
        {
            final Object aa = GraphicsUtils.setupAntialias ( g2d );
            final boolean max = ui.isFrameMaximized ();

            if ( max )
            {
                // Background
                g2d.setPaint ( new GradientPaint ( 0, 0, topBg, 0, 30, middleBg ) );
                g2d.fillRect ( 0, 0, c.getWidth (), c.getHeight () );

                // Border
                g2d.setPaint ( borderColor );
                g2d.drawRect ( 0, 0, c.getWidth () - 1, c.getHeight () - 1 );
                g2d.setPaint ( innerBorderColor );
                g2d.drawRect ( 1, 1, c.getWidth () - 3, c.getHeight () - 3 );

                // Watermark
                if ( ui.isDrawWatermark () )
                {
                    final Shape old = GraphicsUtils.intersectClip ( g2d, getWatermarkClip ( c ) );
                    g2d.drawImage ( ui.getWatermark ().getImage (), 2, 2, null );
                    GraphicsUtils.restoreClip ( g2d, old );
                }
            }
            else
            {
                // Shade
                if ( shadeWidth > 0 )
                {
                    final int diff = isActive ( c ) ? 0 : shadeWidth - inactiveShadeWidth;
                    getShadeIcon ( c ).paintIcon ( g2d, diff, diff, c.getWidth () - diff * 2, c.getHeight () - diff * 2 );
                }

                // Background
                g2d.setPaint ( new GradientPaint ( 0, shadeWidth, topBg, 0, shadeWidth + 30, middleBg ) );
                g2d.fillRoundRect ( shadeWidth, shadeWidth, c.getWidth () - shadeWidth * 2, c.getHeight () - shadeWidth * 2, round * 2,
                        round * 2 );

                // Border
                g2d.setPaint ( borderColor );
                g2d.drawRoundRect ( shadeWidth, shadeWidth, c.getWidth () - shadeWidth * 2 - 1, c.getHeight () - shadeWidth * 2 - 1,
                        round * 2 - 2, round * 2 - 2 );
                g2d.setPaint ( innerBorderColor );
                g2d.drawRoundRect ( shadeWidth + 1, shadeWidth + 1, c.getWidth () - shadeWidth * 2 - 3, c.getHeight () - shadeWidth * 2 - 3,
                        round * 2 - 4, round * 2 - 4 );

                // Watermark
                if ( ui.isDrawWatermark () )
                {
                    final Shape old = GraphicsUtils.intersectClip ( g2d, getWatermarkClip ( c ) );
                    g2d.drawImage ( ui.getWatermark ().getImage (), shadeWidth + 2, shadeWidth + 2, null );
                    GraphicsUtils.restoreClip ( g2d, old );
                }
            }

            GraphicsUtils.restoreAntialias ( g2d, aa );
        }
    }

    protected RoundRectangle2D.Double getWatermarkClip ( final JComponent c )
    {
        return new RoundRectangle2D.Double ( shadeWidth + 2, shadeWidth + 2, c.getWidth () - shadeWidth * 2 - 3,
                c.getHeight () - shadeWidth * 2 - 3, round * 2 - 4, round * 2 - 4 );
    }

    protected NinePatchIcon getShadeIcon ( final JComponent c )
    {
        if ( shadeWidth > 0 )
        {
            return NinePatchUtils.getShadeIcon ( getShadeWidth ( c ), round, 0.8f );
        }
        else
        {
            return null;
        }
    }

    protected int getShadeWidth ( final JComponent c )
    {
        return isActive ( c ) ? shadeWidth : inactiveShadeWidth;
    }

    protected boolean isActive ( final JComponent c )
    {
        return SwingUtils.getWindowAncestor ( c ).isFocused ();
    }
}
