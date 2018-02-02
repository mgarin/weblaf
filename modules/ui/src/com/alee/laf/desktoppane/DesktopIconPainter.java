package com.alee.laf.desktoppane;

import com.alee.painter.decoration.AbstractContainerPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JInternalFrame.JDesktopIcon} component.
 * It is used as {@link WebDesktopIconUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class DesktopIconPainter<C extends JInternalFrame.JDesktopIcon, U extends WebDesktopIconUI, D extends IDecoration<C, D>>
        extends AbstractContainerPainter<C, U, D> implements IDesktopIconPainter<C, U>
{
    /**
     * Implementation is used completely from {@link com.alee.painter.decoration.AbstractContainerPainter}.
     */
}