package com.alee.laf.desktoppane;

import com.alee.painter.decoration.AbstractContainerPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for JDesktopPane component.
 * It is used as WebDesktopPaneUI default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */
public class DesktopPanePainter<C extends JDesktopPane, U extends WDesktopPaneUI, D extends IDecoration<C, D>>
        extends AbstractContainerPainter<C, U, D> implements IDesktopPanePainter<C, U>
{
    /**
     * Implementation is used completely from {@link com.alee.painter.decoration.AbstractContainerPainter}.
     */
}