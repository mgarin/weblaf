package com.alee.laf.viewport;

import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import javax.swing.border.Border;

/**
 * Basic painter for {@link JViewport} component.
 * It is used as {@link WViewportUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */
public class ViewportPainter<C extends JViewport, U extends WViewportUI, D extends IDecoration<C, D>>
        extends AbstractDecorationPainter<C, U, D> implements IViewportPainter<C, U>
{
    @Override
    public void updateBorder ()
    {
        /**
         * {@link javax.swing.JViewport} doesn't support border so we do nothing here.
         * If we would allow this method - we would instantly get an exception from {@link JViewport#setBorder(Border)} method.
         */
    }
}