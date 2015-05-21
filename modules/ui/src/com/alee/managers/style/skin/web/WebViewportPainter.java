package com.alee.managers.style.skin.web;

import com.alee.extended.painter.AbstractPainter;
import com.alee.laf.viewport.ViewportPainter;
import com.alee.laf.viewport.WebViewportUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Alexandr Zernov
 */

public class WebViewportPainter<E extends JViewport, U extends WebViewportUI> extends AbstractPainter<E, U> implements ViewportPainter<E, U>
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
    }
}
