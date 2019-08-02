package com.alee.laf.viewport;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for {@link JViewport} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public interface IViewportPainter<C extends JViewport, U extends WViewportUI> extends SpecificPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}