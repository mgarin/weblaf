package com.alee.laf.colorchooser;

import com.alee.painter.decoration.AbstractContainerPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JColorChooser} component.
 * It is used as {@link WebColorChooserUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class ColorChooserPainter<C extends JColorChooser, U extends WColorChooserUI, D extends IDecoration<C, D>>
        extends AbstractContainerPainter<C, U, D> implements IColorChooserPainter<C, U>
{
    /**
     * Implementation is used completely from {@link com.alee.painter.decoration.AbstractContainerPainter}.
     */
}