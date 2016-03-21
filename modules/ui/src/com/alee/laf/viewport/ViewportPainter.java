package com.alee.laf.viewport;

import com.alee.painter.AbstractPainter;

import javax.swing.*;
import java.awt.*;

/**
 * Basic painter for JViewport component.
 * It is used as WebViewportUI default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public class ViewportPainter<E extends JViewport, U extends WebViewportUI> extends AbstractPainter<E, U> implements IViewportPainter<E, U>
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