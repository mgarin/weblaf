package com.alee.laf.desktoppane;

import com.alee.painter.decoration.AbstractContainerPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for JDesktopPane component.
 * It is used as WebDesktopPaneUI default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class DesktopPanePainter<E extends JDesktopPane, U extends WebDesktopPaneUI, D extends IDecoration<E, D>>
        extends AbstractContainerPainter<E, U, D> implements IDesktopPanePainter<E, U>
{
    /**
     * Implementation is used completely from {@link com.alee.painter.decoration.AbstractContainerPainter}.
     */
}