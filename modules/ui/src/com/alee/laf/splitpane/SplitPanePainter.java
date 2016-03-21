package com.alee.laf.splitpane;

import com.alee.painter.decoration.AbstractContainerPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for JSplitPane component.
 * It is used as WebSplitPaneUI default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class SplitPanePainter<E extends JSplitPane, U extends WebSplitPaneUI, D extends IDecoration<E, D>>
        extends AbstractContainerPainter<E, U, D> implements ISplitPanePainter<E, U>
{
    /**
     * Implementation is used completely from {@link com.alee.painter.decoration.AbstractContainerPainter}.
     */
}