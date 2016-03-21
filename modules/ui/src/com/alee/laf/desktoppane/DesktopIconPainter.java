package com.alee.laf.desktoppane;

import com.alee.painter.decoration.AbstractContainerPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for JDesktopIcon component.
 * It is used as WebDesktopIconUI default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class DesktopIconPainter<E extends JInternalFrame.JDesktopIcon, U extends WebDesktopIconUI, D extends IDecoration<E, D>>
        extends AbstractContainerPainter<E, U, D> implements IDesktopIconPainter<E, U>
{
    /**
     * Implementation is used completely from {@link com.alee.painter.decoration.AbstractContainerPainter}.
     */
}