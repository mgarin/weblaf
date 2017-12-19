package com.alee.laf.splitpane;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for {@link JSplitPane} component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface ISplitPanePainter<E extends JSplitPane, U extends WSplitPaneUI> extends SpecificPainter<E, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}