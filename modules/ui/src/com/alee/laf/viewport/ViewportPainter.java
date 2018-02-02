package com.alee.laf.viewport;

import com.alee.managers.style.Bounds;
import com.alee.painter.AbstractPainter;

import javax.swing.*;
import java.awt.*;

/**
 * Basic painter for {@link JViewport} component.
 * It is used as {@link WViewportUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public class ViewportPainter<C extends JViewport, U extends WViewportUI> extends AbstractPainter<C, U> implements IViewportPainter<C, U>
{
    @Override
    public void updateBorder ()
    {
        // {@link javax.swing.JViewport} doesn't support border so we do nothing here
    }

    @Override
    public void paint ( final Graphics2D g2d, final C c, final U ui, final Bounds bounds )
    {
        // Empty by default
    }
}