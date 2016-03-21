package com.alee.laf.colorchooser;

import com.alee.painter.decoration.AbstractContainerPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for JColorChooser component.
 * It is used as WebColorChooserUI default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class ColorChooserPainter<E extends JColorChooser, U extends WebColorChooserUI, D extends IDecoration<E, D>>
        extends AbstractContainerPainter<E, U, D> implements IColorChooserPainter<E, U>
{
    /**
     * Implementation is used completely from {@link com.alee.painter.decoration.AbstractContainerPainter}.
     */
}