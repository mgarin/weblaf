package com.alee.managers.style.skin.web;

import com.alee.extended.layout.ToolbarLayout;
import com.alee.laf.menu.MenuBarPainter;
import com.alee.laf.menu.MenuBarStyle;
import com.alee.laf.menu.WebMenuBarStyle;
import com.alee.laf.menu.WebMenuBarUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Alexandr Zernov
 */

public class WebMenuBarPainter<E extends JMenuBar, U extends WebMenuBarUI> extends WebDecorationPainter<E, U>
        implements MenuBarPainter<E, U>
{
    /**
     * Style settings.
     */
    protected Color topBgColor = WebMenuBarStyle.topBgColor;
    protected Color bottomBgColor = WebMenuBarStyle.bottomBgColor;
    protected MenuBarStyle menuBarStyle = WebMenuBarStyle.menuBarStyle;

    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        component.setLayout ( new ToolbarLayout ( 0 ) );
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        super.paint ( g2d, bounds, c, ui );

        //        if ( menuBarStyle == MenuBarStyle.attached )
        //        {
        //            final Shape border =
        //                    new Line2D.Double ( 0, c.getHeight () - 1 - shadeWidth, c.getWidth () - 1, c.getHeight () - 1 - shadeWidth );
        //
        //            GraphicsUtils.drawShade ( g2d, border, StyleConstants.shadeColor, shadeWidth );
        //
        //            g2d.setPaint ( new GradientPaint ( 0, 0, topBgColor, 0, c.getHeight (), bottomBgColor ) );
        //            g2d.fillRect ( 0, 0, c.getWidth (), c.getHeight () - shadeWidth );
        //
        //            g2d.setPaint ( borderColor );
        //            g2d.draw ( border );
        //        }
        //        else
        //        {
        //            LafUtils.drawWebStyle ( g2d, c, StyleConstants.shadeColor, shadeWidth, round, true, true, borderColor );
        //        }
    }
}