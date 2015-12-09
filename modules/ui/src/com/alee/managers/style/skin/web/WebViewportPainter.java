package com.alee.managers.style.skin.web;

import com.alee.painter.AbstractPainter;
import com.alee.laf.viewport.IViewportPainter;
import com.alee.laf.viewport.WebViewportUI;

import javax.swing.*;
import java.awt.*;

/**
 * Web-style painter for JViewport component.
 * It is used as WebViewportUI default painter.
 *
 * @author Alexandr Zernov
 */

public class WebViewportPainter<E extends JViewport, U extends WebViewportUI> extends AbstractPainter<E, U> implements IViewportPainter<E, U>
{
    @Override
    public void updateBorder ()
    {
        // {@link javax.swing.JViewport} doesn't support border so we do nothing here
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        // Empty by default
    }
}