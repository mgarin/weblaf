package com.alee.laf.filechooser;

import com.alee.painter.decoration.AbstractContainerPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for JFileChooser component.
 * It is used as WebFileChooserUI default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class FileChooserPainter<E extends JFileChooser, U extends WebFileChooserUI, D extends IDecoration<E, D>>
        extends AbstractContainerPainter<E, U, D> implements IFileChooserPainter<E, U>
{
    /**
     * Implementation is used completely from {@link com.alee.painter.decoration.AbstractContainerPainter}.
     */
}