package com.alee.laf.filechooser;

import com.alee.painter.decoration.AbstractContainerPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JFileChooser} component.
 * It is used as {@link WebFileChooserUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */
public class FileChooserPainter<C extends JFileChooser, U extends WFileChooserUI, D extends IDecoration<C, D>>
        extends AbstractContainerPainter<C, U, D> implements IFileChooserPainter<C, U>
{
    /**
     * Implementation is used completely from {@link AbstractContainerPainter}.
     */
}